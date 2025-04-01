package com.example.moviereviewapp.Models;

public class SearchMovieModel {
    private final String imageUrl;
    private final String title;
    private final String releaseDate;
    private final String actors;
    public SearchMovieModel(String imageUrl, String title, String releaseDate, String actors) {
        this.imageUrl = imageUrl;
        this.title = title;
        this.releaseDate = releaseDate;
        this.actors = actors;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public String getTitle() {
        return title;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public String getActors() {
        return actors;
    }
}
