package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;

public class Crew {
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("job")
    private String job;
    @SerializedName("department")
    private String department;
    @SerializedName("profile_path")
    private String profilePath;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public String getDepartment() {
        return department;
    }

    public String getProfilePath() {
        return profilePath;
    }

    public String getProfileUrl() {
        if (profilePath != null && !profilePath.isEmpty()) {
            return "https://image.tmdb.org/t/p/w500" + profilePath;
        }
        return null;
    }
}