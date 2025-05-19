package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class VideoResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("results")
    private List<VideoResult> results;

    public int getId() {
        return id;
    }

    public List<VideoResult> getResults() {
        return results;
    }
}