package com.example.moviereviewapp.Activities;;


import com.example.moviereviewapp.Models.tvseries;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class tvseriesresponse {
    @SerializedName("results")
    private List<tvseries> results;

    public List<tvseries> getResults() {
        return results;
    }

}
