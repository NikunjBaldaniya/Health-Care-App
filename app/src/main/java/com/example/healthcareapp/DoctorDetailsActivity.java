package com.example.healthcareapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthcareapp.Database.DatabaseHelper;

public class DoctorDetailsActivity extends AppCompatActivity {

    private ImageView imgDoctor;
    private TextView txtDoctorName, txtJobTitle, txtRating;
    private ImageButton btnWhatsApp, btnPhone, btnInstagram, btnLocation;
    private Button btnBack;
    private TextView txtDescription;

    boolean isExpanded = false;
    String description;
    int maxLength = 100;

    ImageHelper imageHelper;
    DatabaseHelper dbHelper;

    private String instaUserName;
    private String whatsAppNumber;
    private String phoneNumber;
    private String location;

    private int doctorId;
    String readMore = "Read More";
    String readLess = "Read Less";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor_details);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Now safely get doctorId from Intent
        Intent intent = getIntent();
        if (intent != null) {
            doctorId = intent.getIntExtra("ID", -1);
        }

        if (doctorId == -1) {
            // fallback if no ID passed
            finish();
            return;
        }

        imgDoctor = findViewById(R.id.imgDoctor);
        txtDescription = findViewById(R.id.txtDescription);
        txtDoctorName = findViewById(R.id.txtDoctorName);
        txtJobTitle = findViewById(R.id.txtJobTitle);
        txtRating = findViewById(R.id.txtRating);
        btnWhatsApp = findViewById(R.id.btnWhatsApp);
        btnPhone = findViewById(R.id.btnPhone);
        btnInstagram = findViewById(R.id.btnInstagram);
        btnLocation = findViewById(R.id.btnLocation);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        // Initialize helpers before use
        dbHelper = new DatabaseHelper(this);
        imageHelper = new ImageHelper();

        imgDoctor.setImageBitmap(imageHelper.bytesToBitmap(dbHelper.getDoctorImage(doctorId)));
        description = dbHelper.getDoctorDescription(doctorId);
        txtDoctorName.setText(dbHelper.getDoctorName(doctorId));
        txtJobTitle.setText(dbHelper.getDoctorJobTitle(doctorId));
        txtRating.setText(String.valueOf(dbHelper.getDoctorRating(doctorId)));

        whatsAppNumber = dbHelper.getDoctorWhatsAppNumber(doctorId);
        phoneNumber = dbHelper.getDoctorPhoneNumber(doctorId);
        instaUserName = dbHelper.getDoctorInstagramUsername(doctorId);
        location = dbHelper.getDoctorLocation(doctorId);

        btnWhatsApp.setOnClickListener(v -> {
            String fullNumber = "+91" + whatsAppNumber;
            String url = "https://wa.me/" + fullNumber;

            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            i.setPackage("com.whatsapp");

            try {
                startActivity(i);
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });

        btnPhone.setOnClickListener(v -> {
            String fullNumber = "+91" + phoneNumber;
            Intent i = new Intent(Intent.ACTION_DIAL);
            i.setData(Uri.parse("tel:" + fullNumber));
            startActivity(i);
        });

        btnInstagram.setOnClickListener(v -> {
            Uri uri = Uri.parse("http://instagram.com/_u/" + instaUserName);
            Intent instagram = new Intent(Intent.ACTION_VIEW, uri);
            instagram.setPackage("com.instagram.android");

            try {
                startActivity(instagram);
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://instagram.com/" + instaUserName)));
            }
        });

        btnLocation.setOnClickListener(v -> {
            Uri uri = Uri.parse(location);
            Intent i = new Intent(Intent.ACTION_VIEW, uri);
            i.setPackage("com.google.android.apps.maps");

            try {
                startActivity(i);
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
        });

        showShortText();
        String text = txtDescription.getText().toString();
        txtDescription.setOnClickListener(v -> {
            if (text.length() > maxLength){
                if (isExpanded) {
                    showShortText();
                } else {
                    showFullText();
                }
                isExpanded = !isExpanded;
            }
        });
    }

    private void showShortText() {
        if (description != null && description.length() > maxLength) {
            // Trim description
            String shortDesc = description.substring(0, maxLength) + ".... ";

            // SpannableString for "Read More"
            SpannableString ssReadMore = new SpannableString(readMore);
            ssReadMore.setSpan(new ForegroundColorSpan(Color.BLUE), 0, readMore.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ssReadMore.setSpan(new StyleSpan(Typeface.BOLD), 0, readMore.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            // Final text = shortDesc + styled Read More
            SpannableStringBuilder builder = new SpannableStringBuilder();
            builder.append(shortDesc);
            builder.append(ssReadMore);

            txtDescription.setText(builder);
        } else {
            txtDescription.setText(description);
        }
    }

    private void showFullText() {
        // SpannableString for "Read Less"
        SpannableString ssReadLess = new SpannableString(readLess);
        ssReadLess.setSpan(new ForegroundColorSpan(Color.BLUE), 0, readLess.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ssReadLess.setSpan(new StyleSpan(Typeface.BOLD), 0, readLess.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Final text = description + styled Read Less
        SpannableStringBuilder builder = new SpannableStringBuilder();
        builder.append(description).append(" ").append(ssReadLess);

        txtDescription.setText(builder);
    }

}
