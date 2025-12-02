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

public class BannerScrollAdapter extends RecyclerView.Adapter<BannerScrollAdapter.ViewHolder> {
    private List<BannerItems> bannerItemsList;
    private ViewPager2 viewPager2;

    public BannerScrollAdapter(List<BannerItems> bannerItemsList, ViewPager2 viewPager2) {
        this.bannerItemsList = bannerItemsList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.banner_advertisement_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setImage(bannerItemsList.get(position));

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

        void setImage(BannerItems bannerItems){
            imageView.setImageResource(bannerItems.getImage());
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
