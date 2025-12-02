package com.example.healthcareapp.Model;

import android.graphics.Bitmap;

public class PopularProductListModel {
    private Bitmap image;
    private String name;
    private double rating;
    private double price;
    private int ID;

    public PopularProductListModel(int ID, Bitmap image, String name, double rating, double price) {
        this.ID = ID;
        this.image = image;
        this.name = name;
        this.rating = rating;
        this.price = price;
    }

    public int getID() {
        return ID;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public double getRating() {
        return rating;
    }

    public double getPrice() {
        return price;
    }
}
