package com.example.healthcareapp.Model;

public class OnboardingItem {
    private int img;
    private String txtTitle;
    private String txtDescription;

    public OnboardingItem(int img, String txtTitle, String txtDescription) {
        this.img = img;
        this.txtTitle = txtTitle;
        this.txtDescription = txtDescription;
    }

    public int getImg() {
        return img;
    }

    public String getTxtTitle() {
        return txtTitle;
    }

    public String getTxtDescription() {
        return txtDescription;
    }
}