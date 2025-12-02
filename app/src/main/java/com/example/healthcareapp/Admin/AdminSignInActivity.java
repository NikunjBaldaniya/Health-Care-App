package com.example.healthcareapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.healthcareapp.R;

public class AdminSignInActivity extends AppCompatActivity {

    private EditText etName, etPassword;
    private  static final String ADMIN_NAME = "Admin";
    private  static final String ADMIN_PASSWORD = "Admin123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        TextView txtPasswordError = findViewById(R.id.txtPasswordError);
        Button btnBack = findViewById(R.id.btnBack);
        Button btnSignIn = findViewById(R.id.btnSignIn);

        btnBack.setOnClickListener(v -> finish());


        btnSignIn.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            // Name Validation
            if (name.isEmpty()) {
                etName.setError("Please Enter Name !!");
            } else if (!name.equals(ADMIN_NAME)) {
                etName.setError("Please Enter Correct Admin Name !!");
            }

            // Password Validation
            if (password.isEmpty()) {
                txtPasswordError.setText("ⓘ Please Enter Password !!");
            } else if (!password.equals(ADMIN_PASSWORD)) {
                txtPasswordError.setText("ⓘ Please Enter Correct Admin Password !!");
            } else {
                txtPasswordError.setText("");
            }

           if (!name.isEmpty() && name.equals(ADMIN_NAME) && !password.isEmpty() && password.equals(ADMIN_PASSWORD)) {
               startActivity(new Intent(this, AdminDashboardActivity.class));
               finish();
               Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show();
            }
        });


    }
}