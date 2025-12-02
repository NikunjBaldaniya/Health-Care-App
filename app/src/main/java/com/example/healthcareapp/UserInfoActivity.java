package com.example.healthcareapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthcareapp.Database.DatabaseHelper;

public class UserInfoActivity extends AppCompatActivity {

    private FrameLayout container;
    private Button btnBack, btnNext;
    private int currentStep = 0;

    // Gender
    private ConstraintLayout lytMale, lytFemale;
    private RadioButton rbMale, rbFemale;

    // Age and Blood
    private EditText etAge;
    private AutoCompleteTextView bloodGroupDropDown;

    // Height and Weight
    private EditText etHeight, etWeight, etAddress;
    private String gender = "", age = "", blood = "", height = "", weight = "";
    private String name, email, password;
    // Activity
    private ConstraintLayout lytSedentary, lytModerate, lytActive;
    private String selectedActivity = "";
    private String address;
    DatabaseHelper databaseHelper;
    private Bitmap bitmapImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        container = findViewById(R.id.container);
        btnBack = findViewById(R.id.btnBack);
        btnNext = findViewById(R.id.btnNext);
        bitmapImage = BitmapFactory.decodeResource(getResources(), R.drawable.profile);
        byte[] imageByte = ImageHelper.bitmapToBytes(bitmapImage);

        // Get Data From Sign Up Activity
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        password = intent.getStringExtra("password");

        updateLayout();

        btnBack.setOnClickListener(v -> {
            if (currentStep > 0) {
                currentStep--;
                updateLayout();
            }
        });

        btnNext.setOnClickListener(v -> {
            switch (currentStep) {
                case 0:
                    gender = rbMale.isChecked() ? "Male" : rbFemale.isChecked() ? "Female" : "";
                    if (gender.isEmpty()) {
                        Toast.makeText(this, "Please select gender", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;

                case 1:
                    age = etAge.getText().toString();
                    blood = bloodGroupDropDown.getText().toString();
                    if (age.isEmpty() || blood.isEmpty()) {
                        Toast.makeText(this, "Fill age and blood group", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    break;

                case 2:
                    height = etHeight.getText().toString();
                    weight = etWeight.getText().toString();
                    address = etAddress.getText().toString();
                    if (height.isEmpty() || weight.isEmpty()) {
                        Toast.makeText(this, "Fill height, weight and address", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        double h = Double.parseDouble(height);
                        double w = Double.parseDouble(weight);
                        if (h < 50 || h > 300) {
                            etHeight.setError("Please enter height (50-300)");
                            return;
                        } else if (w < 10 || w > 700) {
                            etWeight.setError("Please enter weight (10-700)");
                            return;
                        }
                    }
                    break;

                case 3:
                    if (selectedActivity.isEmpty()) {
                        Toast.makeText(this, "Select activity level", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Store database
                    //User user = new User(name, image, email, password, gender, Integer.parseInt(age), Double.parseDouble(height), Double.parseDouble(weight), blood, selectedActivity);
                    databaseHelper = new DatabaseHelper(this);
                    boolean isInserted = databaseHelper.insertUser(name, imageByte, email, password, gender, address, Integer.parseInt(age), Double.parseDouble(height), Double.parseDouble(weight), blood, selectedActivity);

                    // Get User ID

                    if (isInserted) {
                        Toast.makeText(this, "User Created!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, DashboardActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "User Creation Failed!!", Toast.LENGTH_SHORT).show();
                    }
                    return;
            }
            currentStep++;
            updateLayout();
        });

        updateButtons();

    }


    private void loadGenderLayout() {
        View view = LayoutInflater.from(this).inflate(R.layout.gender_layout, container, false);
        container.removeAllViews();
        container.addView(view);

        lytMale = view.findViewById(R.id.lytMale);
        lytFemale = view.findViewById(R.id.lytFemale);
        rbMale = view.findViewById(R.id.rbMale);
        rbFemale = view.findViewById(R.id.rbFemale);

        // Initially reset state
        rbMale.setChecked(false);
        rbFemale.setChecked(false);
        lytMale.setBackgroundResource(R.drawable.card_blue_boarder);
        lytFemale.setBackgroundResource(R.drawable.card_blue_boarder);

        // Male layout click-
        lytMale.setOnClickListener(v -> {
            selectMale();
        });

        // Female layout click
        lytFemale.setOnClickListener(v -> {
            selectFemale();
        });

        // Male radio click
        rbMale.setOnClickListener(v -> {
            selectMale();
        });

        // Female radio click
        rbFemale.setOnClickListener(v -> selectFemale());
    }

    // Helper Methods
    private void selectMale() {
        rbMale.setChecked(true);
        rbFemale.setChecked(false);
        lytMale.setBackgroundResource(R.drawable.card_green_boarder);
        lytFemale.setBackgroundResource(R.drawable.card_blue_boarder);
    }

    private void selectFemale() {
        rbFemale.setChecked(true);
        rbMale.setChecked(false);
        lytFemale.setBackgroundResource(R.drawable.card_green_boarder);
        lytMale.setBackgroundResource(R.drawable.card_blue_boarder);
    }


    private void loadAgeBloodLayout() {
        View view = LayoutInflater.from(this).inflate(R.layout.age_blood_group_layout, container, false);
        container.removeAllViews();
        container.addView(view);

        etAge = view.findViewById(R.id.etAge);
        bloodGroupDropDown = view.findViewById(R.id.bloodGroupDropDown);

        // Set blood group adapter
        String[] bloodGroups = {"A⁺", "A⁻", "B⁺", "B⁻", "AB⁺", "AB⁻", "O⁺", "O⁻"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, bloodGroups);
        bloodGroupDropDown.setAdapter(adapter);
    }

    private void loadHeightWeightLayout() {
        View view = LayoutInflater.from(this).inflate(R.layout.height_weight_layout, container, false);
        container.removeAllViews();
        container.addView(view);

        etHeight = view.findViewById(R.id.etHeight);
        etWeight = view.findViewById(R.id.etWeight);
        etAddress = view.findViewById(R.id.etAddress);
    }

    private void loadActivityLevelLayout() {
        View view = LayoutInflater.from(this).inflate(R.layout.activity_level_layout, container, false);
        container.removeAllViews();
        container.addView(view);

        lytSedentary = view.findViewById(R.id.lytSedentary);
        lytModerate = view.findViewById(R.id.lytModerate);
        lytActive = view.findViewById(R.id.lytActive);

        lytSedentary.setOnClickListener(v -> {
            selectedActivity = "Sedentary";
            lytSedentary.setBackgroundResource(R.drawable.card_green_boarder);
            lytModerate.setBackgroundResource(R.drawable.card_blue_boarder);
            lytActive.setBackgroundResource(R.drawable.card_blue_boarder);
        });

        lytModerate.setOnClickListener(v -> {
            selectedActivity = "Moderate";
            lytModerate.setBackgroundResource(R.drawable.card_green_boarder);
            lytSedentary.setBackgroundResource(R.drawable.card_blue_boarder);
            lytActive.setBackgroundResource(R.drawable.card_blue_boarder);
        });

        lytActive.setOnClickListener(v -> {
            selectedActivity = "Active";
            lytActive.setBackgroundResource(R.drawable.card_green_boarder);
            lytSedentary.setBackgroundResource(R.drawable.card_blue_boarder);
            lytModerate.setBackgroundResource(R.drawable.card_blue_boarder);
        });
    }

    private void updateLayout() {
        switch (currentStep) {
            case 0:
                loadGenderLayout();
                break;
            case 1:
                loadAgeBloodLayout();
                break;
            case 2:
                loadHeightWeightLayout();
                break;
            case 3:
                loadActivityLevelLayout();
                break;
        }
        updateButtons();
    }

    private void updateButtons() {
        btnBack.setVisibility(currentStep == 0 ? View.GONE : View.VISIBLE);
        btnNext.setText(currentStep == 3 ? "Finish" : "Next");
    }
}
