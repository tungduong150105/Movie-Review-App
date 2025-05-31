package com.example.moviereviewapp.Activities;

import com.example.moviereviewapp.Models.Credits;
import com.example.moviereviewapp.Models.Crew;
import com.example.moviereviewapp.Models.Genre;
import com.example.moviereviewapp.Models.Person;
import com.example.moviereviewapp.Models.ProductionCompany;
import com.example.moviereviewapp.Models.SpokenLanguage;
import com.google.gson.annotations.SerializedName;

import java.util.List;

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
    @SerializedName("first_air_date")
    private String firstAirDate;
    @SerializedName("last_air_date")
    private String lastAirDate;
    @SerializedName("credits")
    private Credits credits;
    @SerializedName("genres")
    private List<Genre> genres;
    @SerializedName("origin_country")
    private List<String> originCountry;
    @SerializedName("spoken_languages")
    private List<SpokenLanguage> spokenLanguages;
    @SerializedName("production_companies")
    private List<ProductionCompany> productionCompanies;
    @SerializedName("vote_count")
    private int voteCount;

    public int getVoteCount() {
        return voteCount;
    }
    public List<String> getOriginCountry() {
        return originCountry;
    }

    public List<SpokenLanguage> getSpokenLanguages() {
        return spokenLanguages;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public List<Person> getCast() {
        if (credits != null) {
            return credits.getCast();
        }
        return null;
    }

    public List<Crew> getCrew() {
        if (credits != null) {
            return credits.getCrew();
        }
        return null;
    }

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public String getLastAirDate() {
        return lastAirDate;
    }

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

    public int[] getEpisodeRunTime() {
        return episodeRunTime;
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