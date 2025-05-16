package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieImages {
    private int id;
    private List<Image> backdrops;
    private List<Image> posters;

    public int getId() {
        return id;
    }

    public List<Image> getBackdrops() {
        return backdrops;
    }

    public List<Image> getPosters() {
        return posters;
    }
}