package com.example.healthcareapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.ImageHelper;
import com.example.healthcareapp.MedicineDetailsActivity;
import com.example.healthcareapp.Model.PopularProductListModel;
import com.example.healthcareapp.R;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {
    Context context;
    List<PopularProductListModel> medicineList;
    DatabaseHelper dbHelper;
    public MedicineAdapter(Context context, List<PopularProductListModel> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_medicine_horizontal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ImageHelper imageHelper = new ImageHelper();

        int medicineID = medicineList.get(position).getID();
        String medicineName = medicineList.get(position).getName();
        byte[] medicineImage = imageHelper.bitmapToBytes(medicineList.get(position).getImage());
        int quantity = 1;
        double medicinePrice = medicineList.get(position).getPrice();

        holder.imgMedicine.setImageBitmap(medicineList.get(position).getImage());
        holder.txtMedicineName.setText(medicineName);
        holder.txtMedicinePrice.setText(String.valueOf(medicineList.get(position).getPrice()));
        holder.txtRating.setText(String.valueOf(medicineList.get(position).getRating()));


        holder.lytMedicine.setOnClickListener(v -> {
            Intent intent = new Intent(context, MedicineDetailsActivity.class);
            intent.putExtra("ID", medicineID);
            context.startActivity(intent);
        });

        SharedPreferences prefs = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String email = prefs.getString("userEmail", null);
        int userId = dbHelper.getUserIdByEmail(email);

        holder.btnAddToCart.setOnClickListener(v ->{
            boolean isInCart = dbHelper.isItemInCart(userId, medicineID);
            if (isInCart){
                Toast.makeText(context, "This item is already in cart!!", Toast.LENGTH_SHORT).show();
            } else {
                boolean result = dbHelper.insertCart(medicineName, medicineImage, medicineID, userId, quantity, medicinePrice);
                if (result){
                    Toast.makeText(context,medicineName + " added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView lytMedicine;
        ImageView imgMedicine;
        TextView txtMedicineName, txtMedicinePrice, txtRating;
        ImageButton btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lytMedicine = itemView.findViewById(R.id.lytMedicine);
            imgMedicine = itemView.findViewById(R.id.imgMedicine);
            txtMedicineName = itemView.findViewById(R.id.txtMedicineName);
            txtMedicinePrice = itemView.findViewById(R.id.txtMedicinePrice);
            txtRating = itemView.findViewById(R.id.txtRating);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
