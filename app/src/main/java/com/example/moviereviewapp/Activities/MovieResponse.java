package com.example.moviereviewapp.Activities;


import com.example.moviereviewapp.Models.movies;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResponse {
    @SerializedName("results")
    private List<movies> results;

    public List<movies> getResults() {
        return results;
    }

}

