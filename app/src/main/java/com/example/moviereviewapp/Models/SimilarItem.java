package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;

public class SimilarItem {
    @SerializedName("id")
    private int id;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("title")
    private String title;
    @SerializedName("name")
    private String name;
    @SerializedName("vote_average")
    private double voteAverage;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("first_air_date")
    private String firstAirDate;

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title != null ? title : name;
    }

    public String getOriginalTitle() {
        return title;
    }

    public String getOriginalName() {
        return name;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate != null ? releaseDate : firstAirDate;
    }

    public String getMovieReleaseDate() {
        return releaseDate;
    }

    public String getTvFirstAirDate() {
        return firstAirDate;
    }
}