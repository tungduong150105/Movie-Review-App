package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;

public class SpokenLanguage {
    @SerializedName("english_name")
    private String englishName;
    @SerializedName("name")
    private String name;

    public String getEnglishName() {
        return englishName;
    }

    public String getName() {
        return name;
    }
}