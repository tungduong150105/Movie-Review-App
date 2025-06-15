package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SimilarItem implements Serializable {
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

    // Crew-specific fields
    @SerializedName("job")
    private String job;
    @SerializedName("department")
    private String department;
    @SerializedName("character")
    private String character;

    public String getCharacter() {
        return character;
    }

    public String getJob() {
        return job;
    }

    public int getId() {
        return id;
    }

    public String getPosterPath() {
        return "https://image.tmdb.org/t/p/w500"+ posterPath;
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

    public String getLength() {
        return length;
    }
    public void setLengthFromRuntime(int runtimeMinutes) {
        int hours = runtimeMinutes / 60;
        int minutes = runtimeMinutes % 60;
        this.length = hours + "h " + minutes + "m";
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String length;

    public double getVoteAverage() {

            return Math.round(voteAverage * 10.0) / 10.0;


    }

    public String getReleaseDate() {
        String date = releaseDate != null ? releaseDate : firstAirDate;
        if (date != null && date.length() >= 4) {
            return date.substring(0, 4); // Lấy 4 ký tự đầu (năm)
        }
        return "N/A"; // Trường hợp null hoặc sai định dạng
    }

    public String getMovieReleaseDate() {
        return releaseDate;
    }

    public String getTvFirstAirDate() {
        return firstAirDate;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isCrew() {
        return job != null && !job.isEmpty();
    }

    private boolean isInWatchList = false;
    public boolean getIsInWatchList() {
        return isInWatchList;
    }
    public void setIsInWatchlist(boolean isInWatchList) {
        this.isInWatchList = isInWatchList;
    }
}