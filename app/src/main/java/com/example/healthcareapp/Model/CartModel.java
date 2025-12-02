package com.example.healthcareapp.Model;

import android.graphics.Bitmap;

public class CartModel {
    private int ID;
    private int medicineID;
    private String txtMedicineName;
    private Bitmap imgMedicine;
    private double txtTotalPrice;
    private int txtCount;

    public CartModel(int ID, int medicineID, String txtMedicineName, Bitmap imgMedicine, double txtTotalPrice, int txtCount) {
        this.ID = ID;
        this.medicineID = medicineID;
        this.txtMedicineName = txtMedicineName;
        this.imgMedicine = imgMedicine;
        this.txtTotalPrice = txtTotalPrice;
        this.txtCount = txtCount;
    }

    public int getID() {
        return ID;
    }

    public int getMedicineID() {
        return medicineID;
    }

    public String getTxtMedicineName() {
        return txtMedicineName;
    }

    public Bitmap getImgMedicine() {
        return imgMedicine;
    }

    public double getTxtTotalPrice() {
        return txtTotalPrice;
    }

    public int getTxtCount() {
        return txtCount;
    }

    public void setTxtTotalPrice(double txtTotalPrice) {
        this.txtTotalPrice = txtTotalPrice;
    }

    public void setTxtCount(int txtCount) {
        this.txtCount = txtCount;
    }

}
