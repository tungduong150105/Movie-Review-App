package com.example.moviereviewapp.Activities;

import com.google.gson.annotations.SerializedName;

public class tvseriesdetail {

    @SerializedName("number_of_episodes")
    public int epsnumber;

    @SerializedName("season_number")
    public int seasonnumber;

    public int getRuntime() {
        return runtime;
    }
    @SerializedName("series_id")
    private int Id;

    public int getSeasonnumber() {
        return seasonnumber;
    }

    public int getnumberofepsidose() {
        return epsnumber;
    }

    @SerializedName("runtime")
    public int runtime;



}
