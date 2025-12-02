package com.example.healthcareapp.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.healthcareapp.Adapter.ArticleAdapter;
import com.example.healthcareapp.Adapter.BannerAdapter;
import com.example.healthcareapp.Adapter.PopularDoctorListAdapter;
import com.example.healthcareapp.Adapter.PopularProductAdapter;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.DoctorActivity;
import com.example.healthcareapp.ImageHelper;
import com.example.healthcareapp.MedicineSearchActivity;
import com.example.healthcareapp.Model.ArticalModel;
import com.example.healthcareapp.Model.BannerItems;
import com.example.healthcareapp.Model.DoctorListModel;
import com.example.healthcareapp.Model.PopularProductListModel;
import com.example.healthcareapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    ViewPager2 viewPager2;

    ImageButton btnDoctor, btnSOS, btnNearbyHospital, btnNearbyMedical;
    RecyclerView rvArticles, rvMedicines, rvDoctor;
    ArrayList<ArticalModel> articleList;
    ArticleAdapter articleAdapter;
    Handler handler = new Handler();
    Runnable runnable;
    ImageView noInternetImage, imgUser;
    TextView txtUserName;
    private static final int CALL_PERMISSION_CODE = 101;
    String userName;
    Bitmap userImage;
    List<DoctorListModel> listDoctor;
    CardView search;
    private  String API_KEY = "<PROVIDE_API_KEY_HERE>"; // Get API Key From : https://newsdata.io/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager2 = view.findViewById(R.id.viewPager);
        rvMedicines = view.findViewById(R.id.rvMedicines);
        rvDoctor = view.findViewById(R.id.rvDoctor);
        rvArticles = view.findViewById(R.id.rvArticles);
        btnDoctor = view.findViewById(R.id.btnDoctor);
        btnSOS = view.findViewById(R.id.btnSOS);
        btnNearbyHospital = view.findViewById(R.id.btnNearbyHospital);
        btnNearbyMedical = view.findViewById(R.id.btnNearbyMedical);
        noInternetImage = view.findViewById(R.id.noInternetImage);
        imgUser = view.findViewById(R.id.imgUser);
        txtUserName = view.findViewById(R.id.txtUserName);
        search = view.findViewById(R.id.search);

        // SharedPreferences access
        SharedPreferences prefs = getActivity().getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String email = prefs.getString("userEmail", null);

        // Get User Name and Image From Database
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        ImageHelper imageHelper = new ImageHelper();

        //Get User ID
        int userId = dbHelper.getUserIdByEmail(email);

        // Get and Set User Name
        userName = dbHelper.getNameByUserId(userId);
        txtUserName.setText(userName);

        // Get and Set User Image
        userImage = imageHelper.bytesToBitmap(dbHelper.getImageByUserId(userId));
        if (userImage != null){
            imgUser.setImageBitmap(userImage);
        }

        if (isInternetOn(requireContext())) {
            noInternetImage.setVisibility(View.GONE);
            rvArticles.setVisibility(View.VISIBLE);
        } else {
            noInternetImage.setVisibility(View.VISIBLE);
            rvArticles.setVisibility(View.GONE);
        }

        search.setOnClickListener(v -> startActivity(new Intent(getContext(), MedicineSearchActivity.class)));

        // This for banner
        ArrayList<BannerItems> items = new ArrayList<>();

        items.add(new BannerItems(R.drawable.blood_pressure_control_tablet));
        items.add(new BannerItems(R.drawable.constipation_relief_medicine));
        items.add(new BannerItems(R.drawable.skin_allergy_cream));
        items.add(new BannerItems(R.drawable.gas_relief_medicine));
        items.add(new BannerItems(R.drawable.diabetes_control_medicine));


        BannerAdapter adapter = new BannerAdapter(items, viewPager2);

        viewPager2.setAdapter(adapter);

        runnable = new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager2.getCurrentItem();
                int nextItem = currentItem + 1;
                if (nextItem >= items.size()) {
                    nextItem = 0;
                }
                viewPager2.setCurrentItem(nextItem, true);
                handler.postDelayed(this, 3500);
            }
        };
        handler.postDelayed(runnable, 3500);

        // This is for Article recycler View

        articleList = new ArrayList<>();

        articleAdapter = new ArticleAdapter(getContext(), articleList);
        rvArticles.setAdapter(articleAdapter);
        fetchNewsData();


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvMedicines.setLayoutManager(layoutManager);


        // Top 5 Medicine recycler View
        List<PopularProductListModel> popularProductListModels = dbHelper.getTopFiveMedicines();
        PopularProductAdapter medicineAdapter = new PopularProductAdapter(getContext(), popularProductListModels);
        rvMedicines.setAdapter(medicineAdapter);

        // This is for Top Doctors recycler View

        LinearLayoutManager doctorLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvDoctor.setLayoutManager(doctorLayoutManager);


        listDoctor = dbHelper.getTopFiveDoctors();

        PopularDoctorListAdapter popularDoctorListAdapter = new PopularDoctorListAdapter(getContext(), listDoctor);
        rvDoctor.setAdapter(popularDoctorListAdapter);

        // Goto Doctor Activity
        btnDoctor.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), DoctorActivity.class));
        });

        // Call the Ambulance using SOS button
        btnSOS.setOnClickListener(v -> {
            makeEmergencyCall();
        });

        // Go to Find Nearby Hospital
        btnNearbyHospital.setOnClickListener(v -> {
            try {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=nearby hospitals");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(mapIntent);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Unable to open map", Toast.LENGTH_SHORT).show();
            }
        });

        // Go to Find Nearby Medical
        btnNearbyMedical.setOnClickListener(v -> {
            try {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=nearby medical shop");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(mapIntent);
            } catch (Exception e) {
                Toast.makeText(getContext(), "Unable to open map", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    private boolean isInternetOn(Context context) {
        ConnectivityManager cm = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            return (networkInfo != null && networkInfo.isConnected());
        }
        return false;
    }


    private void fetchNewsData() {
        String url = "https://newsdata.io/api/1/news?apikey=" + API_KEY + "&country=in&language=en&category=health";

        RequestQueue queue = Volley.newRequestQueue(getContext());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray articles = response.getJSONArray("results");
                        for (int i = 0; i < articles.length(); i++) {
                            JSONObject obj = articles.getJSONObject(i);

                            String title = obj.optString("title");
                            String pubDate = obj.optString("pubDate");
                            String imgUrl = obj.optString("image_url");

                            articleList.add(new ArticalModel(title, pubDate, imgUrl));
                        }
                        articleAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Log.e("Volley", "Error: " + error.toString())
        );

        queue.add(request);
    }

    private void makeEmergencyCall() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CALL_PHONE}, CALL_PERMISSION_CODE);

        } else {
            callAmbulance();
        }
    }

    private void callAmbulance() {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:108"));
        startActivity(callIntent);
    }

}