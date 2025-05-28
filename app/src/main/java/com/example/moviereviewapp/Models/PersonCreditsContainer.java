package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PersonCreditsContainer {
    @SerializedName("cast")
    private List<SimilarItem> cast;
    
    @SerializedName("crew") 
    private List<SimilarItem> crew;
    
    public List<SimilarItem> getCast() { return cast; }
    public List<SimilarItem> getCrew() { return crew; }
}