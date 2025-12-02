package com.example.healthcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthcareapp.Admin.AdminSignInActivity;

public class SignUpLoginScreenActivity extends AppCompatActivity {

    Button btnLogin, btnSignUp, btnAdminSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up_login_screen);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Find View By Id's
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnAdminSignIn = findViewById(R.id.btnAdminSignIn);

        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignUpLoginScreenActivity.this, SignInActivity.class));
            finish();
        });

        btnSignUp.setOnClickListener(v -> {
            startActivity(new Intent(SignUpLoginScreenActivity.this, SignUpActivity.class));
            finish();
        });

        btnAdminSignIn.setOnClickListener(v -> {
            startActivity(new Intent(SignUpLoginScreenActivity.this, AdminSignInActivity.class));
            finish();
        });
    }
}