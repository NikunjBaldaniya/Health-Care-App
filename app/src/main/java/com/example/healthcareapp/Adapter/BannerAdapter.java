package com.example.healthcareapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.healthcareapp.Model.BannerItems;
import com.example.healthcareapp.R;

import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.ViewHolder> {
    private List<BannerItems> bannerItemsList;
    private ViewPager2 viewPager2;

    public BannerAdapter(List<BannerItems> bannerItemsList, ViewPager2 viewPager2) {
        this.bannerItemsList = bannerItemsList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_advertisement_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setImageResource(bannerItemsList.get(position).getImage());

        if(position == bannerItemsList.size()-2){
            viewPager2.post(holder.runnable);
        }

    }

    @Override
    public int getItemCount() {
        return bannerItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }


        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                bannerItemsList.addAll(bannerItemsList);
                notifyDataSetChanged();
            }
        };
    }
}
