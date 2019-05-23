package sg.edu.rp.c346.fyp_1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rengwuxian.materialedittext.MaterialEditText;

import sg.edu.rp.c346.fyp_1.Model.UserProfile;

public class MainActivity extends AppCompatActivity {
    EditText edtUser, edtPassword;
    TextView userSignUp;
    Button btnLogin;

    ProgressDialog progressDialog;
    TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtUser = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        userSignUp = findViewById(R.id.tvSignUp);
        btnLogin = findViewById(R.id.btnlogin);
        tvForgotPassword = findViewById(R.id.tvForgotPwd);

        progressDialog = new ProgressDialog(this);

        /*if (user != null){
            finish();
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        }
        */

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(edtUser.getText().toString(), edtPassword.getText().toString());
            }
        });

        userSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, signup.class));

            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showForgotPwdDialog();
            }
        });


    /*tvsignup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent2 = new Intent (MainActivity.this, forgotpass.class);
                    startActivity(intent2);
                }
            });
*/
    }

    private void showForgotPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password");
        builder.setMessage("Enter your secret code");

        LayoutInflater inflater = this.getLayoutInflater();
        View forgot_view = inflater.inflate(R.layout.activity_forgotpass, null);

        builder.setView(forgot_view);
        builder.setIcon(R.drawable.ic_security_black_24dp);

        final MaterialEditText etUserName = forgot_view.findViewById(R.id.etUserName);
        final MaterialEditText etSecureCode = forgot_view.findViewById(R.id.etSecureCode);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("User").document("DOTlnSFpzK59YGnsmy6w");

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()){
                                if (etUserName.getText().toString().equals(document.getString("Username"))){
                                    if (etSecureCode.getText().toString().equals(document.getString("SecureCode"))){
                                        String password = document.getString("Password");
                                        Toast.makeText(MainActivity.this, "The password is: " + password, Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(MainActivity.this, "Wrong secure code", Toast.LENGTH_SHORT).show();
                                    }
                                } else{
                                    Toast.makeText(MainActivity.this, "Wrong Username", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void validate(final String userName, final String userPassword) {
        progressDialog.setMessage("Verifying your account.... ");
        progressDialog.show();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("User").document("DOTlnSFpzK59YGnsmy6w");

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        if (userName.equals(document.getString("Username"))){
                            if (userPassword.equals(document.getString("Password"))){
                                progressDialog.dismiss();
                                startActivity(new Intent(MainActivity.this, Home.class));
                                //checkEmailVerification();
                            } else{
                                Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        } else{
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    /*private void checkEmailVerification() {
        Boolean emailflag = firebaseUser.isEmailVerified();

        if(emailflag){
            finish();
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
        }  else {
            Toast.makeText(this, "Verify your email", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }

    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                startActivity(new Intent(MainActivity.this, Home.class));
                return true;
            case R.id.sign_in:
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                return true;
            case R.id.contact_us:
                startActivity(new Intent(MainActivity.this, contactus.class));
                return true;
            case R.id.view_service:
                startActivity(new Intent(MainActivity.this, ViewActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}











