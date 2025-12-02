package com.example.healthcareapp.Model;

public class ArticalModel {
    String title, pubDate, imageUrl;

    public ArticalModel(String title, String pubDate, String imageUrl) {
        this.title = title;

        this.pubDate = pubDate;
        this.imageUrl = imageUrl;
    }

    public String getTitle() { return title; }
    public String getPubDate() { return pubDate; }
    public String getImageUrl() { return imageUrl; }
}
