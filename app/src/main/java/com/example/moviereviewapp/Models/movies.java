package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;

public class movies {
    public movies(String moviename, long revenue,String trailertext, String posterurl, String backdropurl, double like, double rating, String releasedate) {
        this.moviename = moviename;
        this.trailertext = trailertext;
        this.posterurl = posterurl;
        this.revenue=revenue;
        this.backdropurl = backdropurl;
        this.like = like;
        this.rating = rating;
        this.releasedate = releasedate;
    }

    public movies() {}

    @SerializedName("id")
    private int movieId;

    @SerializedName("title")
    private String moviename;

    @SerializedName("release_date")
    private String releasedate;

    @SerializedName("overview")
    private String trailertext;

    @SerializedName("poster_path")
    private String posterurl;

    @SerializedName("backdrop_path")
    private String backdropurl;


    private double like;
    private long revenue;
    movies(String moviename,long revenue){
        this.moviename=moviename;
        this.revenue=revenue;
    }
    public long getRevenue() {
        return revenue;
    }
    public void setRevenue(long revenue) {
        this.revenue = revenue;
    }

    @SerializedName("vote_average")
    private double rating;

    private String length;

    public int getMovieId() {
        return movieId;
    }

    public String getMoviename() {
        return moviename;
    }

    public String getReleasedate() {
        if (releasedate != null && releasedate.length() >= 4) {
            return releasedate.substring(0, 4);
        }
        return "N/A";
    }

    public String getTrailertext() {
        return trailertext;
    }

    public String getPosterurl() {
        return "https://image.tmdb.org/t/p/w500" + posterurl;
    }

    public String getBackdropurl() {
        return "https://image.tmdb.org/t/p/w500" + backdropurl;
    }

    public double getLike() {
        return like;
    }

    public double getRating() {
        return Math.round(rating * 10.0) / 10.0;
    }


    public String getLength() {
        return length;
    }

    public void setLengthFromRuntime(int runtimeMinutes) {
        int hours = runtimeMinutes / 60;
        int minutes = runtimeMinutes % 60;
        this.length = hours + "h " + minutes + "m";
    }
    public void setRating(double rating) {

        this.rating = Math.round(rating * 10.0) / 10.0;
    }
}
