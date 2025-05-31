package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;

public class AuthorDetails {
    private String name;
    private String username;
    @SerializedName("avatar_path")
    private String avatarPath;
    private Float rating;

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public Float getRating() {
        return rating;
    }
}