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

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.ImageHelper;
import com.example.healthcareapp.MedicineDetailsActivity;
import com.example.healthcareapp.Model.PopularProductListModel;
import com.example.healthcareapp.R;

import java.util.List;

public class PopularProductAdapter extends RecyclerView.Adapter<PopularProductAdapter.ViewHolder> {

    Context context;
    List<PopularProductListModel> listModels;
    DatabaseHelper dbHelper;
    ImageHelper imageHelper;

    public PopularProductAdapter(Context context, List<PopularProductListModel> listModels) {
        this.context = context;
        this.listModels = listModels;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_product_list_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PopularProductListModel model = listModels.get(position);
        int medicineID = model.getID();
        String medicineName = model.getName();
        double medicinePrice = model.getPrice();
        int quantity = 1;

        imageHelper = new ImageHelper();
        byte[] medicineImage = imageHelper.bitmapToBytes(model.getImage());

        holder.imgMedicine.setImageBitmap(model.getImage());
        holder.txtMedicineName.setText(model.getName());
        holder.txtMedicineName.setSelected(true);
        holder.txtRating.setText(String.valueOf(model.getRating()));
        holder.txtMedicinePrice.setText(String.valueOf(model.getPrice()));

        holder.cartPopularMedicine.setOnClickListener(v ->{
            Intent intent = new Intent(context, MedicineDetailsActivity.class);
            intent.putExtra("ID", medicineID);
            context.startActivity(intent);
        });
        // SharedPreferences access
        SharedPreferences prefs = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE);
        String email = prefs.getString("userEmail", null);
        int userId = dbHelper.getUserIdByEmail(email);

        holder.btnAddToCart.setOnClickListener(v -> {
            boolean isInCart = dbHelper.isItemInCart(userId, medicineID);
            if (isInCart){
                Toast.makeText(context, "This item is already in cart!!", Toast.LENGTH_SHORT).show();
            } else {
                boolean result = dbHelper.insertCart(medicineName, medicineImage, medicineID, userId, quantity, medicinePrice);
                if (result){
                    Toast.makeText(context, model.getName() + " added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cartPopularMedicine;
        ImageView imgMedicine;
        TextView txtMedicineName, txtRating, txtMedicinePrice;
        ImageButton btnAddToCart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cartPopularMedicine = itemView.findViewById(R.id.cartPopularMedicine);
            imgMedicine = itemView.findViewById(R.id.imgMedicine);
            txtMedicineName = itemView.findViewById(R.id.txtMedicineName);
            txtRating = itemView.findViewById(R.id.txtRating);
            txtMedicinePrice = itemView.findViewById(R.id.txtMedicinePrice);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}
