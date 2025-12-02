package com.example.healthcareapp;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Adapter.MedicineAdapter;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.Model.PopularProductListModel;

import java.util.ArrayList;
import java.util.List;

public class MedicineSearchResultActivity extends AppCompatActivity {

    private Button btnBack;
    private ImageButton btnSearch;
    private AutoCompleteTextView actSearch;
    private RecyclerView rvResult;
    private DatabaseHelper dbHelper;
    private ImageHelper imageHelper;
    private List<PopularProductListModel> medicineList;
    private MedicineAdapter medicineAdapter;
    private String search;
    private List<String> medicineNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_search_result);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btnBack);
        btnSearch = findViewById(R.id.btnSearch);
        actSearch = findViewById(R.id.actSearch);
        rvResult = findViewById(R.id.rvResult);
        dbHelper = new DatabaseHelper(this);
        String intentSearch = getIntent().getStringExtra("SearchItem");

        btnSearch.setOnClickListener(v ->{
            search = actSearch.getText().toString().trim();
            displayData();
        });


        if (intentSearch == null || intentSearch.isEmpty()) {
            search = actSearch.getText().toString().trim();
        } else {
            actSearch.setText(intentSearch);
            search = intentSearch;
        }


        medicineNames = dbHelper.getAllMedicineNames();
        ArrayAdapter<String> searchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, medicineNames);
        actSearch.setAdapter(searchAdapter);

        btnBack.setOnClickListener(v ->finish());

        medicineList = new ArrayList<>();
        rvResult.setLayoutManager(new LinearLayoutManager(this));
        medicineAdapter = new MedicineAdapter(this, medicineList);
        rvResult.setAdapter(medicineAdapter);
        displayData();
    }

    void displayData(){
        imageHelper = new ImageHelper();
        medicineList.clear();
        Cursor cursor = dbHelper.getSearchedMedicine(search);
        if (cursor.getCount() == 0){
            Toast.makeText(this, "Medicine Not Found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                byte[] imageByte = cursor.getBlob(cursor.getColumnIndexOrThrow("Image"));
                Bitmap imageBit = null;
                if (imageByte != null){
                    imageBit = imageHelper.bytesToBitmap(imageByte);
                }
                medicineList.add(new PopularProductListModel(cursor.getInt(cursor.getColumnIndexOrThrow("ID")),
                        imageBit,
                        cursor.getString(cursor.getColumnIndexOrThrow("Name")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("Rating")),
                        cursor.getDouble(cursor.getColumnIndexOrThrow("Price"))
                        ));
            }
        }
        cursor.close();
        medicineAdapter.notifyDataSetChanged();
    }
}