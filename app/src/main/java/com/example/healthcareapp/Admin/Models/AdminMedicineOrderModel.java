package com.example.healthcareapp.Admin.Models;

public class AdminMedicineOrderModel {
    private int id;
    private int medicineId;
    private int userId;
    private double price;
    private int quantity;


    public AdminMedicineOrderModel(int id, int medicineId, int userId, double price, int quantity) {
        this.id = id;
        this.medicineId = medicineId;
        this.userId = userId;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public int getUserId() {
        return userId;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}
