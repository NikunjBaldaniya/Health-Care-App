package com.example.healthcareapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Adapter.OrderListAdapter;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.Model.OrderListModel;

import java.util.ArrayList;
import java.util.Currency;

public class MedicineOrderProductsActivity extends AppCompatActivity {

    private Button btnBack;
    private RecyclerView rvOrderItems;
    private ImageView imgOrderNotFound;
    private DatabaseHelper dbHelper;
    private ArrayList<OrderListModel> orderList;
    private OrderListAdapter orderListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_medicine_order_products);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnBack = findViewById(R.id.btnBack);
        rvOrderItems = findViewById(R.id.rvOrderItems);
        imgOrderNotFound = findViewById(R.id.imgOrderNotFound);
        SharedPreferences prefs = this.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String email = prefs.getString("userEmail", null);

        dbHelper = new DatabaseHelper(this);
        int userId = dbHelper.getUserIdByEmail(email);

        orderList = new ArrayList<>();
        orderListAdapter = new OrderListAdapter(this, orderList);
        rvOrderItems.setLayoutManager(new LinearLayoutManager(this));
        rvOrderItems.setAdapter(orderListAdapter);

        displayOrderDataByUserId(userId);

        btnBack.setOnClickListener(v ->finish());
    }

    private void displayOrderDataByUserId(int userId){
        orderList.clear();
        Cursor cursor = dbHelper.getOrderByUserID(userId);
        if (cursor.getCount() == 0){
            imgOrderNotFound.setVisibility(View.VISIBLE);
        } else {
            imgOrderNotFound.setVisibility(View.GONE);
            while(cursor.moveToNext()){
                orderList.add(new OrderListModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("MedicineID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("OrderDate"))
                ));
            }
        }
    }
}