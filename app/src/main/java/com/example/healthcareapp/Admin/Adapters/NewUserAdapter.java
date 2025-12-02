package com.example.healthcareapp.Admin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Admin.Models.NewUserModel;
import com.example.healthcareapp.Admin.UpdateUsersActivity;
import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.R;

import java.util.List;

public class NewUserAdapter extends RecyclerView.Adapter<NewUserAdapter.ViewHolder> {
    Context context;
    List<NewUserModel> list;
    DatabaseHelper dbHelper;

    public NewUserAdapter(Context context, List<NewUserModel> list) {
        this.context = context;
        this.list = list;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_new_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int id = list.get(position).getID();
        holder.imgUser.setImageBitmap(list.get(position).getUserImage());
        holder.txtUserName.setText(list.get(position).getUserName());
        holder.txtUserName.setSelected(true);
        holder.txtUserActivityLevel.setText(list.get(position).getUserActivityLevel());

        holder.lytAdminNewUsers.setOnClickListener(v -> {
            Intent intent = new Intent(context, UpdateUsersActivity.class);
            intent.putExtra("ID", id);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView lytAdminNewUsers;
        ImageView imgUser;
        TextView txtUserName, txtUserActivityLevel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            lytAdminNewUsers = itemView.findViewById(R.id.lytAdminNewUsers);
            imgUser = itemView.findViewById(R.id.imgUser);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtUserActivityLevel = itemView.findViewById(R.id.txtUserActivityLevel);

        }
    }
}
