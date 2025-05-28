package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PersonImagesContainer {
    @SerializedName("profiles")
    private List<Image> profiles;
    
    public List<Image> getProfiles() { return profiles; }
}