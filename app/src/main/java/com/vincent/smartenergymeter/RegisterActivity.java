package com.vincent.smartenergymeter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = RegisterActivity.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputUsername;
    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputRePassword;
    private DatabaseReference mFirebaseDbRef;
    private FirebaseDatabase mFirebaseInstance;
    private ProgressDialog pDialog;
    //declare_auth
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        inputFullName = (EditText) findViewById(R.id.name);
        inputEmail = (EditText) findViewById(R.id.email);
        inputUsername = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);
        inputRePassword = (EditText) findViewById(R.id.re_password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //initialize_auth
        mAuth = FirebaseAuth.getInstance();
        //initialize firebase
        mFirebaseInstance = FirebaseDatabase.getInstance();



//        // Check if user is already logged in or not
//        if (session.isLoggedIn()) {
//            // User is already logged in. Take him to main activity
//            Intent intent = new Intent(RegisterActivity.this,
//                    MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(!inputRePassword.getText().toString().equals(inputPassword.getText().toString())){
                    inputPassword.setError("Password must match re-typed password");
                }
                String name = inputFullName.getText().toString().trim();
                String username = inputUsername.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {
                    registerUser(name, username, email, password);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //if current user is already logged in bypass login
    }

    public void registerUser(final String name, final String username, String email, String password){

        Log.d(TAG, "createAccount:" + email);

        showProgressDialog("Creating a new account...");

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //saving other user infos
                            mFirebaseDbRef = mFirebaseInstance.getReference("users");
                            Users mUsers = new Users(user.getEmail(),name,username);
                            mFirebaseDbRef.push().setValue(mUsers);

                            Toast.makeText(RegisterActivity.this, "User Registered Successfully.",
                                    Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),
                                    LoginActivity.class);
                            startActivity(i);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //
                        hideProgressDialog();
                    }
                });
    }


    public boolean validateForm(){
        return false;
    }

    public void showProgressDialog(String message){
        if (!pDialog.isShowing())
            pDialog.setMessage(message);
            pDialog.show();
    };
    public void hideProgressDialog(){
        if (pDialog.isShowing())
            pDialog.dismiss();
    };
}
