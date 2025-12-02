package com.example.healthcareapp.Model;

public class NotificationListModel {
    private int id;
    private int userId;
    private int medicineId;
    private String title;
    private String message;

    public NotificationListModel(int id, int userId, int medicineId, String title, String message) {
        this.id = id;
        this.userId = userId;
        this.medicineId = medicineId;
        this.title = title;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public int getUserId() {
        return userId;
    }

    public int getMedicineId() {
        return medicineId;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }
}
