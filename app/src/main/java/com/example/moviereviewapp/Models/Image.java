package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("aspect_ratio")
    private double aspectRatio;
    @SerializedName("file_path")
    private String filePath;
    private int height;
    @SerializedName("iso_639_1")
    private String iso6391;
    private double voteAverage;
    private int voteCount;
    private int width;

    public double getAspectRatio() {
        return aspectRatio;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getHeight() {
        return height;
    }

    public String getIso6391() {
        return iso6391;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public int getWidth() {
        return width;
    }
}