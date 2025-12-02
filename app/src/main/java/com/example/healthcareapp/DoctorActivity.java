package com.example.healthcareapp;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.healthcareapp.Adapter.BannerAdapter;
import com.example.healthcareapp.Adapter.DoctorListAdapter;
import com.example.healthcareapp.Adapter.PopularDoctorListAdapter;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.Model.BannerItems;
import com.example.healthcareapp.Model.DoctorListModel;

import java.util.ArrayList;
import java.util.List;

public class DoctorActivity extends AppCompatActivity {

    ViewPager2 viewPager2;
    List<BannerItems> bannerItemsList = new ArrayList<>();
    Handler slideHandler = new Handler();
    Runnable slideRunnable;
    Button btnBack;
    RecyclerView rcPopularDoctor, rcDoctor;
    List<DoctorListModel> doctorList;
    DatabaseHelper dbHelper;
    ImageHelper imageHelper;
    CardView search;
    TextView seeAllPopularDoctors, seeAllDoctors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_doctor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewPager2 = findViewById(R.id.viewPager);
        rcPopularDoctor = findViewById(R.id.rcPopularDoctor);
        rcDoctor = findViewById(R.id.rcDoctor);
        btnBack = findViewById(R.id.btnBack);
        search = findViewById(R.id.search);
        seeAllPopularDoctors = findViewById(R.id.seeAllPopularDoctors);
        seeAllDoctors = findViewById(R.id.seeAllDoctors);


        search.setOnClickListener(v -> startActivity(new Intent(this, DoctorSearchActivity.class)));
        seeAllPopularDoctors.setOnClickListener(v -> startActivity(new Intent(this, TopDoctorsActivity.class)));
        seeAllDoctors.setOnClickListener(v -> startActivity(new Intent(this, DoctorsActivity.class)));


        bannerItemsList.add(new BannerItems(R.drawable.general_physician));
        bannerItemsList.add(new BannerItems(R.drawable.pediatrician));
        bannerItemsList.add(new BannerItems(R.drawable.cardiologist));
        bannerItemsList.add(new BannerItems(R.drawable.orthopedic_surgeon));
        bannerItemsList.add(new BannerItems(R.drawable.dermatologist));

        BannerAdapter bannerAdapter = new BannerAdapter(bannerItemsList, viewPager2);
        viewPager2.setAdapter(bannerAdapter);

        slideRunnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager2.getCurrentItem();
                int nextItem = currentItem + 1;
                if (nextItem >= bannerItemsList.size())
                    nextItem = 0;
                viewPager2.setCurrentItem(nextItem, true);
                slideHandler.postDelayed(this, 3500);
            }
        };

        slideHandler.postDelayed(slideRunnable, 3500);

        dbHelper = new DatabaseHelper(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcPopularDoctor.setLayoutManager(layoutManager);

        List<DoctorListModel> popularDoctorList = dbHelper.getTopFiveDoctors();
        PopularDoctorListAdapter popularDoctorListAdapter = new PopularDoctorListAdapter(this, popularDoctorList);
        rcPopularDoctor.setAdapter(popularDoctorListAdapter);


        doctorList = new ArrayList<>();

        DoctorListAdapter doctorListAdapter = new DoctorListAdapter(this, doctorList);
        rcDoctor.setLayoutManager(new LinearLayoutManager(this));
        rcDoctor.setAdapter(doctorListAdapter);

        displayDoctorData();

        // Back Button
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });
    }

    private void displayDoctorData() {
        imageHelper = new ImageHelper();
        doctorList.clear();
        Cursor cursor = dbHelper.getAllDoctors();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Data!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                byte[] imageByte = cursor.getBlob(2);
                Bitmap imageBit = null;
                if (imageByte != null) {
                    imageBit = imageHelper.bytesToBitmap(imageByte);
                }
                doctorList.add(new DoctorListModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        imageBit,
                        cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                        cursor.getString(cursor.getColumnIndexOrThrow("JobTitle")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("Rating"))
                ));
            }
        }
        cursor.close();
    }
}