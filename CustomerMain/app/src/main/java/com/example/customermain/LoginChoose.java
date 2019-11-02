package com.example.customermain;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class LoginChoose extends AppCompatActivity {
    Button btnm,btnc;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_choose);
        final Intent manager = new Intent(getApplicationContext(),ManagerLogin.class);
        final Intent canteen = new Intent(getApplicationContext(),CustomerLogin.class);
        btnm = findViewById(R.id.btn_manager);
        btnc = findViewById(R.id.btn_canteen);
        mAuth = FirebaseAuth.getInstance();
        btnm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(manager);
            }
        });

        btnc.setOnClickListener(new  View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(canteen);

            }
        });
    }
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            if(mAuth.getCurrentUser().getProviders().get(0).equals("google.com")){
                startCustomerActivity();
            }
            else
            {
                startManagerActivity();
            }
        }
    }
    private void startCustomerActivity() {
        Intent intent = new Intent(this, CustomeMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    private void startManagerActivity() {
        Intent intent = new Intent(this, ManagerMain.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
