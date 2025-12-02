package com.example.healthcareapp.Model;

public class HistoryModel {
    private int ID;

    private String history;

    public HistoryModel(int ID, String history) {
        this.ID = ID;
        this.history = history;
    }

    public int getID() {
        return ID;
    }

    public String getHistory() {
        return history;
    }
}
