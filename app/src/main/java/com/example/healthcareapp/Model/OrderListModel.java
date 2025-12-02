package com.example.healthcareapp.Model;

public class OrderListModel {
    private int id;
    private int medicineId;
    private String orderDate;

    public OrderListModel(int id, int medicineId, String orderDate) {
        this.id = id;
        this.medicineId = medicineId;
        this.orderDate = orderDate;
    }

    public int getId() {
        return id;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public String getOrderDate() {
        return orderDate;
    }
}
