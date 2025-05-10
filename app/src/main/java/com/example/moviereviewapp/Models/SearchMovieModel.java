package com.example.moviereviewapp.Models;

public class SearchMovieModel {
    private final String id;
    private final String imageUrl;
    private final String title;
    private final String releaseDate;
    public SearchMovieModel(String id, String imageUrl, String title, String releaseDate) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.releaseDate = releaseDate;
    }
    public String getId() {
        return id;
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
}
