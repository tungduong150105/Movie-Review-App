package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieKeywordResponse {
    @SerializedName("id")
    private int id;
    @SerializedName("keywords")
    private List<Keyword> keywords;

    public int getId() {
        return id;
    }

    public List<Keyword> getKeywords() {
        return keywords;
    }
}