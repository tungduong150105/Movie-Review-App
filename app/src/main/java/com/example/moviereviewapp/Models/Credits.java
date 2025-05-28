package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Credits {
    @SerializedName("cast")
    private List<Person> cast;
    @SerializedName("crew")
    private List<Crew> crew;

    public List<Person> getCast() {
        return cast;
    }

    public List<Crew> getCrew() {
        return crew;
    }
}
