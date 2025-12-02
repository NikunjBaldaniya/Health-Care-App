package com.example.healthcareapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword;
    private CheckBox chkTP;
    private Button btnSignUp, btnBack;
    private TextView txtTerms, txtPolicy, txtSignIn, txtTPError, txtPasswordError;
    TextInputLayout editTextPassword;
    public static String name="", email="", password="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        chkTP = findViewById(R.id.chkTP);
        btnBack = findViewById(R.id.btnBack);
        btnSignUp = findViewById(R.id.btnSignUp);
        txtTerms = findViewById(R.id.txtTerms);
        txtPolicy = findViewById(R.id.txtPolicy);
        txtSignIn = findViewById(R.id.txtSignIn);
        txtTPError = findViewById(R.id.txtTPError);
        txtPasswordError = findViewById(R.id.txtPasswordError);
        editTextPassword = findViewById(R.id.editTextPasswordSignUp);

        btnBack.setOnClickListener( v ->finish());

        btnSignUp.setOnClickListener(v ->{
            name = etName.getText().toString().trim();
            email = etEmail.getText().toString().trim();
            password = etPassword.getText().toString().trim();
            boolean isValid = true;

            if (name.isEmpty()) {
                etName.setError("Please Enter Your Name");
                isValid = false;
            } else {
                etName.setError(null);
            }

            if (email.isEmpty()) {
                etEmail.setError("Please Enter Your Email!");
                isValid = false;
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.setError("Please Enter Correct Email!");
                isValid = false;
            } else {
                etEmail.setError(null);
            }
            if (password.isEmpty()) {
                txtPasswordError.setText("ⓘ Please Enter Your Password");
                isValid = false;
            } else if (password.length() < 8) {
                txtPasswordError.setText("ⓘ Please enter at least 8 characters");
                isValid = false;
            } else {
                txtPasswordError.setText(null);
            }

            if (!chkTP.isChecked()) {
                txtTPError.setText("ⓘ Please Accept Terms and Conditions");
                isValid = false;
            } else {
                txtTPError.setText("");
            }

            if (isValid) {
                SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("userEmail", email);
                editor.putBoolean("isLoggedIn", true);
                editor.apply();
                Intent intent = new Intent(this, UserInfoActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("password", password);
                startActivity(intent);
                finish();
            }
        });

        txtTerms.setOnClickListener(v -> {
            startActivity(new Intent(this, TermsConditionsActivity.class));
        });

        txtPolicy.setOnClickListener(v -> {
            startActivity(new Intent(this, PrivacyPolicyActivity.class));
        });

        txtSignIn.setOnClickListener(v ->{
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        });
    }
}