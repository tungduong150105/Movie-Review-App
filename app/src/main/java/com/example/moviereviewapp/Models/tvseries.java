package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class tvseries implements Serializable {


    @SerializedName("id")
    private int id;

    public tvseries(int id, String name, String posterid, String backdropid, String overview, double rating, String date, String eps) {
        this.id = id;
        this.name = name;
        this.posterid = posterid;
        this.backdropid = backdropid;
        this.overview = overview;
        this.rating = rating;
        this.date = date;
        this.eps = eps;
    }

    public int getId() {
        return id;
    }
    public void setepsnumber(int epsa){
     this.eps=epsa+"eps";
    }

    public String getName() {
        return name;
    }

    public String getPosterid() {
        return"https://image.tmdb.org/t/p/w500"+ posterid;
    }

    public String getBackdropid() {
        return backdropid;
    }

    public String getOverview() {
        return overview;
    }

    public double getRating() {
        return Math.round(rating * 10.0) / 10.0;
    }

    public String getDate() {
        if (date != null && date.length() >= 4) {
            return date.substring(0, 4);
        }
        return "N/A";
    }

    public String getEps() {
        return eps;
    }

    @SerializedName("name")

    private String name;
    @SerializedName("poster_path")
    private String posterid;
    @SerializedName("backdrop_path")
    private String backdropid;
    @SerializedName("overview")
    private String overview;
    @SerializedName("vote_average")
    private double rating;
    @SerializedName("first_air_date")
    private String date;
    private String eps;
}
