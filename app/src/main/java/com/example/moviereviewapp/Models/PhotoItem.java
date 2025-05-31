package com.example.moviereviewapp.Models;

import java.io.Serializable;

public class PhotoItem implements Serializable {
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