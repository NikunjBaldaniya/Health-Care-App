package com.example.healthcareapp.Admin;

import android.os.Bundle;
import android.os.Message;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.ImageHelper;
import com.example.healthcareapp.R;

public class AdminMedicineOrderDetailsActivity extends AppCompatActivity {

    private Button btnBack, btnDelete;
    private ImageView imgMedicine;
    private TextView txtMedicineName, txtUserName, txtUserEmail, txtMedicineQuantity, txtMedicinePrice, txtOrderDate, txtAddress, txtMedicineTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_medicine_order_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnBack = findViewById(R.id.btnBack);
        btnDelete = findViewById(R.id.btnDelete);
        imgMedicine = findViewById(R.id.imgMedicine);
        txtMedicineName = findViewById(R.id.txtMedicineName);
        txtUserName = findViewById(R.id.txtUserName);
        txtUserEmail = findViewById(R.id.txtUserEmail);
        txtMedicineQuantity = findViewById(R.id.txtMedicineQuantity);
        txtMedicinePrice = findViewById(R.id.txtMedicinePrice);
        txtOrderDate = findViewById(R.id.txtOrderDate);
        txtAddress = findViewById(R.id.txtAddress);
        txtMedicineTotalPrice = findViewById(R.id.txtMedicineTotalPrice);
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        int orderId = getIntent().getIntExtra("ID", 0);
        int medicineId = dbHelper.getMedicineIDbyOrderID(orderId);
        int userId = dbHelper.getUserIDbyOrderID(orderId);
        double price = dbHelper.getMedicinePriceById(medicineId);
        double totalPrice = dbHelper.getPricebyOrderID(orderId);
        int quantity = dbHelper.getQuantityByOrderID(orderId);
        byte[] image = dbHelper.getMedicineImage(medicineId);

        // set data on image and text view
        imgMedicine.setImageBitmap(ImageHelper.bytesToBitmap(image));
        txtMedicinePrice.setText(String.valueOf(price));
        txtMedicineTotalPrice.setText(String.valueOf(totalPrice));
        txtMedicineQuantity.setText(String.valueOf(quantity));
        txtMedicineName.setText(dbHelper.getMedicineName(medicineId));
        txtUserEmail.setText(dbHelper.getEmailByUserId(userId));
        txtUserName.setText(dbHelper.getNameByUserId(userId));
        txtOrderDate.setText(dbHelper.getOrderDateByOrderID(orderId));
        txtAddress.setText(dbHelper.getAddressByUserId(userId));

        btnDelete.setOnClickListener(v ->{
            String medicineName = dbHelper.getMedicineName(medicineId);
            String Title = "Cancel Order";
            String message = "Oops! " + medicineName + " is unavailable, so your order has been cancelled.";
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Cancel Ordered");
            alert.setMessage("Are you sure you want to cancel product ordered?");
            alert.setPositiveButton("Yes", (dialog, which) ->{
                boolean isDeleted = dbHelper.deleteMedicineOrder(orderId);
                if (isDeleted) {
                    boolean isInserted =  dbHelper.insertNotification(userId, medicineId, Title, message);
                    if (isInserted){
                        Toast.makeText(this, "Product Order is Cancel!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(this, "Unable to cancel product order!", Toast.LENGTH_SHORT).show();
                }
            });
            alert.setNegativeButton("No", (dialog, which) ->{
                dialog.cancel();
            });
            alert.show();
        });
        btnBack.setOnClickListener(v -> finish());
    }
}