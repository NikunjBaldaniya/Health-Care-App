package com.example.healthcareapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Adapter.NotificationListAdapter;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.Model.NotificationListModel;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private Button btnBack;
    private RecyclerView rvNotification;
    private ImageView imgNotificationNotFound;
    private DatabaseHelper dbHelper;
    private NotificationListAdapter notificationAdapter;
    private ArrayList<NotificationListModel> notificationList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notification);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btnBack);
        rvNotification = findViewById(R.id.rvNotification);
        imgNotificationNotFound = findViewById(R.id.imgNotificationNotFound);


        dbHelper = new DatabaseHelper(this);

        notificationList = new ArrayList<>();
        notificationAdapter = new NotificationListAdapter(this, notificationList);
        rvNotification.setLayoutManager(new LinearLayoutManager(this));
        rvNotification.setAdapter(notificationAdapter);

        displayNotification();

        btnBack.setOnClickListener(v -> finish());
    }

    private void displayNotification(){
        SharedPreferences prefs = this.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String email = prefs.getString("userEmail", null);

        int userID = dbHelper.getUserIdByEmail(email);
        notificationList.clear();
        Cursor cursor = dbHelper.getNotificationByUserID(userID);
        if (cursor.getCount() == 0){
            imgNotificationNotFound.setVisibility(ImageView.VISIBLE);
        } else {
            imgNotificationNotFound.setVisibility(ImageView.GONE);
            while (cursor.moveToNext()){
                notificationList.add(new NotificationListModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("UserID")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("MedicineID")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Title")),
                        cursor.getString(cursor.getColumnIndexOrThrow("Message"))
                ));
            }
        }
        cursor.close();
        notificationAdapter.notifyDataSetChanged();
    }
}