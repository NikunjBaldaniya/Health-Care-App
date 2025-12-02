package com.example.healthcareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthcareapp.Database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MedicineDetailsActivity extends AppCompatActivity {

    private TextView txtDescription;
    boolean isExpanded = false;

    String description;

    // Text Length
    int maxLength = 100;

    private int medicineID;

    private ImageView image;
    private Button btnBack, btnAddToCart, btnBuyNow;
    private TextView txtMedicineName, txtCategory, txtExpiryDate, txtPrice, txtRating;
    private DatabaseHelper dbHelper;
    private ImageHelper imageHelper;
    String readMore = "Read More";
    String readLess = "Read Less";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicine_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtDescription = findViewById(R.id.txtDescription);
        image = findViewById(R.id.image);
        txtMedicineName = findViewById(R.id.txtMedicineName);
        txtCategory = findViewById(R.id.txtCategory);
        txtExpiryDate = findViewById(R.id.txtExpiryDate);
        txtPrice = findViewById(R.id.txtPrice);
        txtRating = findViewById(R.id.txtRating);
        btnBack = findViewById(R.id.btnBack);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        btnBuyNow = findViewById(R.id.btnBuyNow);

        medicineID = getIntent().getIntExtra("ID", 1);

        dbHelper = new DatabaseHelper(this);
        imageHelper = new ImageHelper();

        // Get and Set image
        byte[] imageByte = dbHelper.getMedicineImage(medicineID);
        image.setImageBitmap(imageHelper.bytesToBitmap(imageByte));

        // get and set name
        String medicineName = dbHelper.getMedicineName(medicineID);
        txtMedicineName.setText(medicineName);

        // get and set medicine description
        description = dbHelper.getMedicineDescription(medicineID);
        txtDescription.setText(description);

        // get and set category
        txtCategory.setText(dbHelper.getMedicineCategory(medicineID));

        // get and set expiry date
        txtExpiryDate.setText(dbHelper.getMedicineExpiryDate(medicineID));

        // get and set price
        double medicinePrice = dbHelper.getMedicinePrice(medicineID);
        txtPrice.setText(String.valueOf(medicinePrice));

        // get and set rating
        txtRating.setText(String.valueOf(dbHelper.getMedicineRating(medicineID)));

        btnBuyNow.setOnClickListener(v ->{
            Intent intent = new Intent(this, PaymentSuccessActivity.class);
            intent.putExtra("Price", medicinePrice);
            startActivity(intent);
        });

        btnBack.setOnClickListener(v -> finish());

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

        // SharedPreferences access
        SharedPreferences prefs = this.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String email = prefs.getString("userEmail", null);
        int userId = dbHelper.getUserIdByEmail(email);

        int quantity = 1;

        btnAddToCart.setOnClickListener(v ->{
            boolean isInCart = dbHelper.isItemInCart(medicineID, userId);
            if (isInCart){
                Toast.makeText(this, "This item is already in cart!!", Toast.LENGTH_SHORT).show();
            } else {
                boolean result = dbHelper.insertCart(medicineName, imageByte, medicineID, userId, quantity, medicinePrice);
                if (result){
                    Toast.makeText(this, medicineName + " added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBuyNow.setOnClickListener(v ->{
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            boolean isInserted = dbHelper.insertMedicineOrder(userId, medicineID, medicinePrice, quantity, currentDate);
            if (isInserted){
                Intent intent = new Intent(this, PaymentSuccessActivity.class);
                intent.putExtra("Price", medicinePrice);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
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