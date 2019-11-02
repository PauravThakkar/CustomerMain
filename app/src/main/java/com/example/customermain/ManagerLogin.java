package com.example.customermain;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ManagerLogin extends AppCompatActivity {
    EditText et_email,et_password;
    //TextView tv_forgot_password;
    Button btn_loginup;
    // ProgressBar to display progress of sign up
    private FirebaseDatabase database;
    private ProgressDialog progressDialog;

    private FirebaseAuth mAuth;
    // Declare an instance of FirebaseAuth
    private DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_login);
        database = FirebaseDatabase.getInstance();
        mRef = database.getReference();
        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);
        btn_loginup = findViewById(R.id.btn_mlogin);
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(ManagerLogin.this);

        btn_loginup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });
    }
    private void createUser() {
        final String email = et_email.getText().toString().trim();
        final String password = et_password.getText().toString().trim();

        if (email.isEmpty()) {
            et_email.setError("Email required");
            et_email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            et_password.setError("Password required");
            et_password.requestFocus();
            return;
        }



        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            startProfileActivity();
                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                userLogin(email, password);
                            } else {
                                Toast.makeText(ManagerLogin.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });
    }

    private void userLogin(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startProfileActivity();
                        } else {

                            Toast.makeText(ManagerLogin.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
    private void startProfileActivity() {
        Intent intent = new Intent(this, ManagerMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
