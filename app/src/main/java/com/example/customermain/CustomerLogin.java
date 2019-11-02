package com.example.customermain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class CustomerLogin extends AppCompatActivity {
    private static final int RC_SIGN_IN = 100;
    GoogleSignInClient mGoogleSignInClient;

    SignInButton mgoogleloginbtn;
    ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Login");
        // enable back button
        //actionBar.setHomeAsUpIndicator(R.drawable.back_icon);
        // before mAuth
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        mAuth = FirebaseAuth.getInstance();
        // Initialize Firebase Auth
        progressDialog = new ProgressDialog(this);

        // Handle google login btn click
        mgoogleloginbtn = findViewById(R.id.googleLoginBtn);
        mgoogleloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Begin google sign in process
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(CustomerLogin.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();

                            // if user sign in first time then get and show user info from google account
                            if(task.getResult().getAdditionalUserInfo().isNewUser())
                            {
                                // set userid and user email from auth
                                String uid = user.getUid();
                                String name = user.getDisplayName();
                                String email = user.getEmail();
                                String phone = user.getPhoneNumber();
                                final String[] token = new String[1];
                                // when user login store user info in firebase database too
                                // using Hashmap


                                HashMap<Object,String> hashMap = new HashMap<>();
                                // put info in Hashmap
                                hashMap.put("email",email);
                                hashMap.put("name",name);

                                if(phone!=null)
                                {
                                    hashMap.put("mobile_number",phone);
                                }
                                else
                                {
                                    hashMap.put("mobile_number","");
                                }
                                hashMap.put("uid",uid);
                                hashMap.put("address","");
                                // Firebase database instance
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                // path to store user data named "User"
                                DatabaseReference reference = database.getReference("Users");
                                // put data within hashmap in database
                                reference.child(uid).setValue(hashMap);
                            }



                            // go to profile activity after logged in
                            Toast.makeText(CustomerLogin.this, "\n Login Successful "+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),CustomeMain.class));
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(CustomerLogin.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CustomerLogin.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
}
