package com.vincent.smartenergymeter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.media.tv.TvContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

        private static final String TAG = LoginActivity.class.getSimpleName();
        private Button btnLogin;
        private Button btnLinkToRegister;
        private EditText inputUsername;
        private EditText inputPassword;
        private ProgressDialog pDialog;
        private FirebaseAuth mAuth;
        private DatabaseReference mFirebaseDbRef;
        private FirebaseDatabase mFirebaseInstance;
        private String[] email = {null};

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);
            getSupportActionBar().hide();
            //initialize_auth
            mAuth = FirebaseAuth.getInstance();
            //initialize firebase
            mFirebaseInstance = FirebaseDatabase.getInstance();


            inputUsername = (EditText) findViewById(R.id.username);
            inputPassword = (EditText) findViewById(R.id.password);
            btnLogin = (Button) findViewById(R.id.btnLogin);
            btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);

            // Progress dialog
            pDialog = new ProgressDialog(this);
            pDialog.setCancelable(false);

            mAuth = FirebaseAuth.getInstance();
            // Login button Click Event
            btnLogin.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    String username = inputUsername.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();
                    // Check for empty data in the form
                    if (!username.isEmpty() && !password.isEmpty()) {
                        // login user
                        checkLogin(username, password);
                    } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getApplicationContext(),
                                "Please enter the credentials!", Toast.LENGTH_LONG)
                                .show();
                    }
                }

            });

            // Link to Register Screen
            btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

                public void onClick(View view) {
                    Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }

        public void checkLogin(final String username, final String password){
            showProgressDialog("Logging in...");
            Log.w(TAG, "Username is "+username+" Password is "+password);
            //getting ref
            mFirebaseDbRef = mFirebaseInstance.getReference();
            //getting email from firebase
            mFirebaseDbRef.child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for(DataSnapshot adress : dataSnapshot.getChildren()) {
                        if(adress.getValue(Users.class).getUsername().equals(username)) {
                            email[0] = adress.getValue(Users.class).getEmail();
                            Log.d("EMAIL_FOUND", email[0]+" password :"+password);
                            mAuth.signInWithEmailAndPassword(email[0], password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                hideProgressDialog();
                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent i = new Intent(getApplicationContext(),MeterActivity.class);
                                startActivity(i);
                                finish();
                                //updateUI(user);
                            } else {
                                hideProgressDialog();
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                                }
                            });
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    hideProgressDialog();

                }
            });
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
