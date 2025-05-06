package com.example.moviereviewapp.Activities;

import com.google.gson.annotations.SerializedName;

public class PersonDetail {
    @SerializedName("birthday")
    private String birthday;

    @SerializedName("deathday")
    private String deathday;

    @SerializedName("id")
    private int id;

    public String getBirthday() {
        return birthday;
    }

    public String getDeathday() {
        return deathday;
    }
}
