package com.example.healthcareapp.Admin.Models;

import android.graphics.Bitmap;

public class AdminUserModel {
    private int id;
    private Bitmap img;
    private String name;
    private String email;

    public AdminUserModel(int id, String name, Bitmap img, String email) {
        this.id = id;
        this.img = img;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }
    public Bitmap getImg() {
        return img;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
