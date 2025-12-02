package com.example.healthcareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Adapter.CartAdapter;
import com.example.healthcareapp.Admin.Models.AdminUserModel;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.Model.CartModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {

    private TextView txtTotalPrice;
    private Button btnBack, btnCheckout;
    private RecyclerView rvCartItems;

    private DatabaseHelper dbHelper;
    private ImageHelper imageHelper;
    private CartAdapter adapter;
    private ArrayList<CartModel> cartList;
    private double totalPriceSum;
    private ImageView imgCartNotFound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        rvCartItems = findViewById(R.id.rvCartItems);
        btnBack = findViewById(R.id.btnBack);
        btnCheckout = findViewById(R.id.btnCheckout);
        imgCartNotFound = findViewById(R.id.imgCartNotFound);

        SharedPreferences prefs = this.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String email = prefs.getString("userEmail", null);


        dbHelper = new DatabaseHelper(this);
        cartList = new ArrayList<>();

        adapter = new CartAdapter(this, cartList);
        rvCartItems.setLayoutManager(new LinearLayoutManager(this));
        rvCartItems.setAdapter(adapter);

        displayCartData();
        updateTotalPrice();

        int userID = dbHelper.getUserIdByEmail(email);

        btnCheckout.setOnClickListener(v ->{
            String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            ArrayList<Integer> idList = new ArrayList<>();
            boolean isInserted = false;
            for (CartModel cart : cartList) {
                idList.add(cart.getID());
                double price = dbHelper.getMedicinePriceById(cart.getMedicineID());
                int quantity = cart.getTxtCount();
                double actualPrice = price * quantity;
                boolean result =  dbHelper.insertMedicineOrder(userID, cart.getMedicineID(), actualPrice, quantity, currentDate);
                if (result){
                    isInserted = true;
                }
            }

            if (idList.isEmpty()){
                Toast.makeText(this, "Please Add Products in Cart", Toast.LENGTH_SHORT).show();
            } else {
                if (isInserted){
                    Intent intent = new Intent(this, PaymentSuccessActivity.class);
                    intent.putExtra("Price", totalPriceSum);
                    dbHelper.deleteAllCart(userID);
                    displayCartData();
                    updateTotalPrice();
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Something Went Wrong!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBack.setOnClickListener(v -> finish());
    }

    private void displayCartData(){
        imageHelper = new ImageHelper();
        cartList.clear();
        // SharedPreferences access
        SharedPreferences prefs = this.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String email = prefs.getString("userEmail", null);
        int userId = dbHelper.getUserIdByEmail(email);
        Cursor cursor = dbHelper.getAllCartItems(userId);
        if (cursor.getCount() == 0){
            imgCartNotFound.setVisibility(View.VISIBLE);
        } else {
            imgCartNotFound.setVisibility(View.GONE);
            while (cursor.moveToNext()){
                byte[] imgBytes = cursor.getBlob(cursor.getColumnIndexOrThrow("Image")); // Image BLOB
                Bitmap bmp = null;
                if (imgBytes != null) {
                    bmp = imageHelper.bytesToBitmap(imgBytes);
                }
                cartList.add(new CartModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("MedicineID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                        bmp,
                        cursor.getDouble(cursor.getColumnIndexOrThrow("Price")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("Quantity"))
                ));
            }
        }
        cursor.close();
        adapter.notifyDataSetChanged();
    }

    public void updateTotalPrice() {
        totalPriceSum = 0;
        for (CartModel item : cartList) {
            totalPriceSum += item.getTxtTotalPrice();
        }
        String formattedTotal = String.format("%.2f", totalPriceSum);
        txtTotalPrice.setText(String.valueOf(formattedTotal));
    }

}
