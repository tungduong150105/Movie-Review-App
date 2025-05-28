package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;

public class ProductionCompany {
    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private int id;
    @SerializedName("logo_path")
    private String logoPath;
    @SerializedName("origin_country")
    private String originCountry;

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public String getOriginCountry() {
        return originCountry;
    }
}