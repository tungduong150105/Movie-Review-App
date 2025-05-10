package com.example.moviereviewapp.Activities;

import com.google.gson.annotations.SerializedName;

public class tvseriesdetail {
    @SerializedName("number_of_episodes")
    public int epsnumber;

    @SerializedName("season_number")
    public int seasonnumber;

    @SerializedName("episode_run_time")
    public int[] episodeRunTime;

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("series_id")
    private int Id;

    @SerializedName("runtime")
    public int runtime;

    @SerializedName("vote_average")
    private double rating;

    public String getName() {
        return name;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public int getId() {
        return id;
    }

    public int getRuntime() {
        return runtime;
    }

    public int getSeasonnumber() {
        return seasonnumber;
    }

    public int getnumberofepsidose() {
        return epsnumber;
    }

    public double getRating() {
        return rating;
    }
}