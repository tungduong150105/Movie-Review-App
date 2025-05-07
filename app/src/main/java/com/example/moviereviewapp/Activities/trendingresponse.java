package com.example.moviereviewapp.Activities;;


import com.example.moviereviewapp.Models.trendingall;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class trendingresponse {

    @SerializedName("results")

    private List<trendingall> trending;
    public List<trendingall> getTrending(){
        return trending;
    }
}
