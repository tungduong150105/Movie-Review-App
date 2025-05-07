package com.example.moviereviewapp.Activities;

import com.google.gson.annotations.SerializedName;

public class MovieDetail {
    @SerializedName("runtime")
    private int runtime;
    @SerializedName("rating")
    private double rating;

    public long getRevenue() {
        return revenue;
    }
    @SerializedName("id")
    private int Id;
    public int getId() {
        return Id;
    }

    @SerializedName("revenue")
    private long revenue;

    public int getRuntime() {
        return runtime;
    }
    public double getRating() {
        return rating;
    }
}
