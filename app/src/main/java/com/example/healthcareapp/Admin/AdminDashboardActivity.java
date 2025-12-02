package com.example.healthcareapp.Admin;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.healthcareapp.Admin.Fragments.AdminDoctorsFragment;
import com.example.healthcareapp.Admin.Fragments.AdminHomeFragment;
import com.example.healthcareapp.Admin.Fragments.AdminMedicineOrderFragment;
import com.example.healthcareapp.Admin.Fragments.AdminMedicinsFragment;
import com.example.healthcareapp.Admin.Fragments.AdminUsersFragment;
import com.example.healthcareapp.databinding.ActivityAdminDashboardBinding;
import com.example.healthcareapp.R;
import com.example.healthcareapp.fragments.HomeFragment;

public class AdminDashboardActivity extends AppCompatActivity {

    ActivityAdminDashboardBinding binding;
    int selectedItem = R.id.home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAdminDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState != null){
            selectedItem = savedInstanceState.getInt("selectedItem", R.id.home);
        }

        binding.adminBottomNavigation.setOnItemSelectedListener(item -> {
            selectedItem = item.getItemId();
            loadFragment(selectedItem);
            return true;
        });

        loadFragment(selectedItem);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedItem", selectedItem);
    }

    private void loadFragment(int itemId) {
        Fragment fragment = null;

        if (itemId == R.id.home) {
            fragment = new AdminHomeFragment();
        } else if (itemId == R.id.medicine) {
            fragment = new AdminMedicinsFragment();
        } else if (itemId == R.id.doctor) {
            fragment = new AdminDoctorsFragment();
        } else if (itemId == R.id.user) {
            fragment = new AdminUsersFragment();
        } else if (itemId == R.id.order) {
            fragment = new AdminMedicineOrderFragment();
        }

        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayout, fragment)
                    .commit();
        }
    }
}
