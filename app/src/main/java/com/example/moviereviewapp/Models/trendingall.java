package com.example.moviereviewapp.Models;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class trendingall implements Serializable {
    public String getName() {
        if(name!=null){
            return name;
        }
        else{
            return title;

        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @SerializedName("media_type")
    public String type;
    public String getPosterid() {
        return posterid;
    }

    public String getBackdropid() {
        return backdropid;
    }

    public String getOverview() {
        return overview;
    }

    public double getAveragevote() {
        return Math.round(getAveragevote() * 10.0) / 10.0;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        if (date != null && date.length() >= 4) {
            return date.substring(0, 4);
        }
        else if(releasedate!=null&&releasedate.length()>=4){
            return releasedate.substring(0, 4);

        }
        return "N/A";
    }

    public String getPopularity() {
        return popularity;
    }

    @SerializedName("name")
private String name;

    @SerializedName("title")
    private String title;
    @SerializedName("poster_path")
    private String posterid;
    @SerializedName("backdrop_path")
private String backdropid;
    @SerializedName("release_date")
private String releasedate;
    public trendingall(String name,String releasedate,String type,String title, String length, String posterid, String backdropid, String overview, int id, String date, String popularity) {
        this.name = name;
        this.posterid = posterid;
        this.backdropid = backdropid;
        this.overview = overview;

        this.id = id;
        this.date = date;
        this.popularity = popularity;
        this.title=title;
        this.length=length;
        this.releasedate=releasedate;
        this.type=type;

    }
    private String length;

    @SerializedName("overview")
private String overview;

    @SerializedName("vote_average")
    private double rating;
    public String getLength() {
        return length;
    }
    public void setLength(String length){
        this.length=length;
    }
    public double getRating() {


        return Math.round(rating * 10.0) / 10.0;
    }


    @SerializedName("id")
private int id;
    @SerializedName("first_air_date")
private String date;
    @SerializedName("popularity")
private String popularity;
    public void setLengthFromRuntime(int runtimeMinutes) {
        int hours = runtimeMinutes / 60;
        int minutes = runtimeMinutes % 60;
        this.length = hours + "h " + minutes + "m";
    }
    public void setRating(double rating) {

        this.rating = Math.round(rating * 10.0) / 10.0;
    }

    private boolean IsInWatchList = false;
    public boolean getIsInWatchList() {
        return IsInWatchList;
    }
    public void setIsInWatchList(boolean isInWatchList) {
        IsInWatchList = isInWatchList;
    }
}
