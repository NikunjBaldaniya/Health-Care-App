package com.example.healthcareapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthcareapp.Database.DatabaseHelper;

import org.w3c.dom.Text;

public class ResetPasswordActivity extends AppCompatActivity {

    private TextView txtPasswordError, txtConfirmPasswordError;
    private EditText etPassword, etConfirmPassword;
    private Button btnBack, btnResetPassword;
    private String email;
    private String password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_reset_password);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btnBack);
        btnResetPassword = findViewById(R.id.btnResetPassword);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        txtPasswordError = findViewById(R.id.txtPasswordError);
        txtConfirmPasswordError = findViewById(R.id.txtConfirmPasswordError);
        DatabaseHelper dbHelper = new DatabaseHelper(this);


        SharedPreferences prefs = this.getSharedPreferences("Forgot", MODE_PRIVATE);
        email = prefs.getString("Email", null);

        int id = dbHelper.getUserIdByEmail(email);

        btnResetPassword.setOnClickListener(v ->{
            password = etPassword.getText().toString().trim();
            confirmPassword = etConfirmPassword.getText().toString().trim();

             if (isValueValid(password, confirmPassword)){
                 boolean isUpdated = dbHelper.updatePassword(id, confirmPassword);
                 if (isUpdated){
                     startActivity(new Intent(this, SignUpLoginScreenActivity.class));
                     finish();
                     SharedPreferences.Editor editor = prefs.edit();
                     editor.clear();
                     editor.apply();
                 } else {
                     Toast.makeText(this, "Password Not Update!!", Toast.LENGTH_SHORT).show();
                 }
             }
        });

        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, SignInActivity.class));
            finish();
        });
    }

    private boolean isValueValid(String password, String confirmPassword){
        boolean isValid = true;

        if (password.isEmpty()){
            txtPasswordError.setText("ⓘ Please Enter Your Password");
            txtPasswordError.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            txtPasswordError.setVisibility(View.GONE);
        }

        if (confirmPassword.isEmpty()){
            txtConfirmPasswordError.setText("ⓘ Please Enter Your Confirm Password");
            txtConfirmPasswordError.setVisibility(View.VISIBLE);
            isValid = false;
        } else {
            txtConfirmPasswordError.setVisibility(View.GONE);
        }

        if (!confirmPassword.equals(password)){
            txtConfirmPasswordError.setText("ⓘ Your Password and Confirm Password Mismatch!!");
            isValid = false;
        }
        return isValid;
    }
}