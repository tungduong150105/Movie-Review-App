package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;

public class Keyword {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;

    public Keyword(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Keyword{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}