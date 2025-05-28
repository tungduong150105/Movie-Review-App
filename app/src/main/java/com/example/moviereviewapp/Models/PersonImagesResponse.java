package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PersonImagesResponse {
    private int id;
    
    @SerializedName("profiles")
    private List<Image> profiles;

    public int getId() {
        return id;
    }

    public List<Image> getProfiles() {
        return profiles;
    }
}