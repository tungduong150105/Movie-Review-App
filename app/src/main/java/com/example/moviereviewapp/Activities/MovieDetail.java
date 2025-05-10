package com.example.moviereviewapp.Activities;

import com.google.gson.annotations.SerializedName;

public class MovieDetail {
    @SerializedName("runtime")
    private int runtime;

    @SerializedName("vote_average")
    private double rating;

    @SerializedName("id")
    private int id;

    @SerializedName("revenue")
    private long revenue;

    @SerializedName("title")
    private String title;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public long getRevenue() {
        return revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public double getRating() {
        return rating;
    }
}