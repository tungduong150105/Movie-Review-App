package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TvShowImages {
    private int id;
    private List<Image> backdrops;
    private List<Image> posters;
    private List<Image> stills;

    public int getId() {
        return id;
    }

    public List<Image> getBackdrops() {
        return backdrops;
    }

    public List<Image> getPosters() {
        return posters;
    }

    public List<Image> getStills() {
        return stills;
    }
}