package com.example.healthcareapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.healthcareapp.Database.DatabaseHelper;
import com.example.healthcareapp.ImageHelper;
import com.example.healthcareapp.Model.NotificationListModel;
import com.example.healthcareapp.R;

import java.util.ArrayList;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.ViewHolder> {

    Context context;
    ArrayList<NotificationListModel> notificationList;
    DatabaseHelper dbHelper;

    public NotificationListAdapter(Context context, ArrayList<NotificationListModel> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
        dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationListModel notification = notificationList.get(position);
        byte[] image = dbHelper.getMedicineImage(notification.getMedicineId());
        int id = notification.getId();

        holder.imgMedicine.setImageBitmap(ImageHelper.bytesToBitmap(image));
        holder.txtTitle.setText(notification.getTitle());
        holder.txtMessage.setText(notification.getMessage());

        holder.btnCancel.setOnClickListener(v ->{
            boolean isDeleted = dbHelper.deleteNotificationById(id);
            if (isDeleted){
                notificationList.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView lytNotification;
        ImageView imgMedicine;
        TextView txtTitle, txtMessage;
        ImageButton btnCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lytNotification = itemView.findViewById(R.id.lytNotification);
            imgMedicine = itemView.findViewById(R.id.imgMedicine);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            btnCancel = itemView.findViewById(R.id.btnCancel);
        }
    }
}
