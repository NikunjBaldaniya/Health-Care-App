package com.example.healthcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;


import com.example.healthcareapp.databinding.ActivityDashboardBinding;
import com.example.healthcareapp.fragments.HealthFragment;
import com.example.healthcareapp.fragments.HomeFragment;
import com.example.healthcareapp.fragments.MedicinFragment;
import com.example.healthcareapp.fragments.ProfileFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardActivity extends AppCompatActivity {


    ActivityDashboardBinding binding;
    FloatingActionButton fab;
    int selectedItem = R.id.home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fab = findViewById(R.id.fab);

        if (savedInstanceState != null){
            selectedItem = savedInstanceState.getInt("selectedItem", R.id.home);
        }

        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            selectedItem = item.getItemId();
            loadFragment(selectedItem);
            return true;
        });

        loadFragment(selectedItem);

        fab.setOnClickListener(v -> startActivity(new Intent(this, AiChatActivity.class)));

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedItem", selectedItem);
    }

    private void loadFragment(int itemId){
        Fragment fragment = null;

        if (itemId == R.id.home) {
            fragment = new HomeFragment();
        } else if (itemId == R.id.medicine) {
            fragment = new MedicinFragment();
        }else if (itemId == R.id.health) {
            fragment = new HealthFragment();
        } else if (itemId == R.id.profile) {
            fragment = new ProfileFragment();
        }

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .commit();
        }
    }
}