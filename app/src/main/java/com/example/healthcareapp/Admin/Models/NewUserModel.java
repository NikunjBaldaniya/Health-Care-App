package com.example.healthcareapp.Admin.Models;

import android.graphics.Bitmap;

import com.example.healthcareapp.Model.BannerItems;

public class NewUserModel {
    private int ID;
    private Bitmap UserImage;
    private String UserName;
    private String UserActivityLevel;

    public NewUserModel(int ID, Bitmap userImage, String userName, String userActivityLevel) {
        this.ID = ID;
        UserImage = userImage;
        UserName = userName;
        UserActivityLevel = userActivityLevel;
    }

    public int getID() {
        return ID;
    }

    public Bitmap getUserImage() {
        return UserImage;
    }

    public String getUserName() {
        return UserName;
    }

    public String getUserActivityLevel() {
        return UserActivityLevel;
    }
}
