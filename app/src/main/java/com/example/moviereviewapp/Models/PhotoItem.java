package com.example.moviereviewapp.Models;

public class PhotoItem {
    private String imageUrl;
    private double aspectRatio;

    public PhotoItem(String imageUrl, double aspectRatio) {
        this.imageUrl = imageUrl;
        this.aspectRatio = aspectRatio;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getAspectRatio() {
        return aspectRatio;
    }
}