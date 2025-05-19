package com.example.moviereviewapp.Activities;

import com.example.moviereviewapp.Models.Crew;
import com.example.moviereviewapp.Models.Genre;
import com.example.moviereviewapp.Models.Person;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieDetail {
    @SerializedName("runtime")
    private int runtime;
    @SerializedName("vote_average")
    private double rating;
    @SerializedName("id")
    private int id;
    @SerializedName("revenue")
    private long revenue;
    @SerializedName("title")
    private String title;
    @SerializedName("overview")
    private String overview;
    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("tagline")
    private String tagline;
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

    public String getReleaseDate() {
        return releaseDate;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public long getRevenue() {
        return revenue;
    }

    public int getRuntime() {
        return runtime;
    }

    public double getRating() {
        return rating;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public static class Credits {
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

    public static class SpokenLanguage {
        @SerializedName("english_name")
        private String englishName;
        @SerializedName("name")
        private String name;

        public String getEnglishName() {
            return englishName;
        }

        public String getName() {
            return name;
        }
    }

    public static class ProductionCompany {
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
}