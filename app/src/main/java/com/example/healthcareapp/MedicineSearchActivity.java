    package com.example.healthcareapp;

    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.database.Cursor;
    import android.os.Bundle;
    import android.widget.ArrayAdapter;
    import android.widget.AutoCompleteTextView;
    import android.widget.Button;
    import android.widget.Toast;

    import androidx.activity.EdgeToEdge;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import com.example.healthcareapp.Adapter.MedicineSearchHistoryAdapter;
    import com.example.healthcareapp.Database.DatabaseHelper;
    import com.example.healthcareapp.Model.HistoryModel;

    import java.util.ArrayList;
    import java.util.List;

    public class MedicineSearchActivity extends AppCompatActivity {

        private Button btnBack, btnSearch;
        private AutoCompleteTextView etSearch;
        private RecyclerView rvMedicineSearchHistory;
        private List<String> medicineNames;
        private DatabaseHelper dbHelper;
        private SharedPreferences prefs;
        private int userId;
        private List<HistoryModel> historyList;
        private MedicineSearchHistoryAdapter historyAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_medicine_search);
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });

            btnSearch = findViewById(R.id.btnSearch);
            btnBack = findViewById(R.id.btnBack);
            etSearch = findViewById(R.id.etSearch);
            rvMedicineSearchHistory = findViewById(R.id.rvMedicineSearchHistory);

            prefs = this.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
            String email = prefs.getString("userEmail", null);

            dbHelper = new DatabaseHelper(this);
            userId = dbHelper.getUserIdByEmail(email);

            medicineNames = dbHelper.getAllMedicineNames();
            ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, medicineNames);
            etSearch.setAdapter(searchAdapter);

            btnBack.setOnClickListener(v -> finish());

            historyList = new ArrayList<>();
            rvMedicineSearchHistory.setLayoutManager(new LinearLayoutManager(this));
            historyAdapter = new MedicineSearchHistoryAdapter(this, historyList);
            rvMedicineSearchHistory.setAdapter(historyAdapter);
            displayData();
            historyAdapter.notifyDataSetChanged();

            btnSearch.setOnClickListener(v -> {
                String search = etSearch.getText().toString().trim();
                if (!search.isEmpty()) {
                    boolean isHistoryIn = dbHelper.isMedicineHistoryIn(search, userId);
                    if (!isHistoryIn) {
                        dbHelper.insertMedicineSearchHistory(search, userId);
                        displayData();
                    }
                    Intent intent = new Intent(this, MedicineSearchResultActivity.class);
                    intent.putExtra("SearchItem", search);
                    startActivity(intent);
                }
            });

        }

        private void displayData(){
            historyList.clear();
            Cursor cursor = dbHelper.getAllMedicineSearchHistory(userId);
            if (cursor.getCount() == 0){
                Toast.makeText(this, "No History Found!", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()){
                    historyList.add(new HistoryModel(
                            cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                            cursor.getString(cursor.getColumnIndexOrThrow("MedicineName"))
                    ));
                }
            }
            cursor.close();
            historyAdapter.notifyDataSetChanged();
        }
    }