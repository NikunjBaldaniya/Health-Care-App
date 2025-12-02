package com.example.healthcareapp.Admin.Models;

import android.graphics.Bitmap;

public class AdminMedicineModel {
    private Bitmap imgMedicine;
    private String txtMedicineName;
    private Double txtMedicinePrice;
    private int medicineID;

    public AdminMedicineModel(int medicineID, Bitmap imgMedicine, String txtMedicineName, Double txtMedicinePrice) {
        this.medicineID = medicineID;
        this.imgMedicine = imgMedicine;
        this.txtMedicineName = txtMedicineName;
        this.txtMedicinePrice = txtMedicinePrice;
    }

    public int getMedicineID() {
        return medicineID;
    }

    public Bitmap getImgMedicine() {
        return imgMedicine;
    }

    public String getTxtMedicineName() {
        return txtMedicineName;
    }

    public Double getTxtMedicinePrice() {
        return txtMedicinePrice;
    }
}
