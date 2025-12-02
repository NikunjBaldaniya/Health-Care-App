package com.example.healthcareapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.healthcareapp.Adapter.BannerAdapter;
import com.example.healthcareapp.Adapter.PopularProductAdapter;
import com.example.healthcareapp.CartActivity;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.ImageHelper;
import com.example.healthcareapp.MedicineSearchActivity;
import com.example.healthcareapp.MedicinesActivity;
import com.example.healthcareapp.Model.BannerItems;
import com.example.healthcareapp.Model.PopularProductListModel;
import com.example.healthcareapp.R;
import com.example.healthcareapp.TopMedicinesActivity;

import java.util.ArrayList;
import java.util.List;
public class MedicinFragment extends Fragment {

    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();
    private Runnable slideRunnable;
    private TextView txtSeePopularAll, txtSeeAllProduct;
    private RecyclerView recyclerView, rcProduct;
    private ImageButton btnCart;
    private ConstraintLayout lytCartItemCount;
    private TextView txtCartItemCount;
    DatabaseHelper dbHelper;
    ImageHelper imageHelper;
    ArrayList<PopularProductListModel> productList;
    private CardView search;
    private int userId;
    private PopularProductAdapter productAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicin, container, false);

        viewPager2 = view.findViewById(R.id.viewPager);
        txtSeePopularAll = view.findViewById(R.id.txtSeePopularAll);
        txtSeeAllProduct = view.findViewById(R.id.txtSeeAllProduct);
        recyclerView = view.findViewById(R.id.recyclerView);
        rcProduct = view.findViewById(R.id.rcProduct);
        btnCart = view.findViewById(R.id.btnCart);
        lytCartItemCount = view.findViewById(R.id.lytCartItemCount);
        txtCartItemCount = view.findViewById(R.id.txtCartItemCount);
        search = view.findViewById(R.id.search);

        search.setOnClickListener(v -> startActivity(new Intent(getContext(), MedicineSearchActivity.class)));
        txtSeePopularAll.setOnClickListener(v -> startActivity(new Intent(getContext(), TopMedicinesActivity.class)));
        txtSeeAllProduct.setOnClickListener(v -> startActivity(new Intent(getContext(), MedicinesActivity.class)));

        List<BannerItems>  bannerItemsList = new ArrayList<>();
        bannerItemsList.add(new BannerItems(R.drawable.diabetes_control_medicine));
        bannerItemsList.add(new BannerItems(R.drawable.gas_relief_medicine));
        bannerItemsList.add(new BannerItems(R.drawable.skin_allergy_cream));
        bannerItemsList.add(new BannerItems(R.drawable.constipation_relief_medicine));
        bannerItemsList.add(new BannerItems(R.drawable.blood_pressure_control_tablet));

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
        btnCart.setOnClickListener(v ->startActivity(new Intent(getContext(), CartActivity.class)));


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        LinearLayoutManager salesLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rcProduct.setLayoutManager(salesLayoutManager);

        dbHelper = new DatabaseHelper(getContext());
        List<PopularProductListModel> popularProductListModels = dbHelper.getTopFiveMedicines();
        PopularProductAdapter adapter = new PopularProductAdapter(getContext(), popularProductListModels);
        recyclerView.setAdapter(adapter);

        SharedPreferences prefs = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String email = prefs.getString("userEmail", null);

        userId = dbHelper.getUserIdByEmail(email);

        updateCartCounter();

        productList = new ArrayList<>();
        productAdapter = new PopularProductAdapter(getContext(), productList);
        rcProduct.setAdapter(productAdapter);

        displayMedicineData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        displayMedicineData();
        updateCartCounter();
    }

    private void updateCartCounter() {
        int itemCount = dbHelper.getCountByValue(userId);
        if (itemCount == 0) {
            lytCartItemCount.setVisibility(View.GONE);
        } else {
            lytCartItemCount.setVisibility(View.VISIBLE);
            txtCartItemCount.setText(String.valueOf(itemCount));
        }
    }


    private void displayMedicineData() {
        imageHelper = new ImageHelper();
        productList.clear();
        Cursor cursor = dbHelper.getAllMedicines();
        if (cursor.getCount() == 0) {
            Toast.makeText(getContext(), "No Data!", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                byte[] imageByte = cursor.getBlob(cursor.getColumnIndexOrThrow("Image"));
                Bitmap imageBit = null;
                if (imageByte != null) {
                    imageBit = imageHelper.bytesToBitmap(imageByte);
                }
                productList.add(new PopularProductListModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        imageBit,
                        cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("Rating")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("Price"))
                ));
            }

        }
        cursor.close();
        productAdapter.notifyDataSetChanged();
    }
}