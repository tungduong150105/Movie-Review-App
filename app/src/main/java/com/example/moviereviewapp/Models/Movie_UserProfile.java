package com.example.moviereviewapp.Models;

public class Movie_UserProfile {
    private String imgURl;
    private String title;
    private String releaseDate;
    private double yourRating;
    private double averageRating;

    public Movie_UserProfile(String imgURl, String title, String releaseDate, double yourRating, double averageRating) {
        this.imgURl = imgURl;
        this.title = title;
        this.releaseDate = releaseDate;
        this.yourRating = yourRating;
        this.averageRating = averageRating;
    }

    public String getImgURl() {
        return imgURl;
    }

    public void setImgURl(String imgURl) {
        this.imgURl = imgURl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public double getYourRating() {
        return yourRating;
    }

    public void setYourRating(double yourRating) {
        this.yourRating = yourRating;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
}
