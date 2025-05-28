package com.example.moviereviewapp.Activities;

import com.example.moviereviewapp.Models.PersonCreditsContainer;
import com.example.moviereviewapp.Models.PersonImagesContainer;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class PersonDetail {
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("deathday")
    private String deathday;
    @SerializedName("id")
    private int id;
    @SerializedName("name")
    private String name;
    @SerializedName("biography")
    private String biography;
    @SerializedName("place_of_birth")
    private String placeOfBirth;
    @SerializedName("profile_path")
    private String profilePath;
    @SerializedName("known_for_department")
    private String knownForDepartment;
    @SerializedName("also_known_as")
    private List<String> alsoKnownAs;
    @SerializedName("homepage")
    private String homepage;
    @SerializedName("imdb_id")
    private String imdbId;
    
    // Fields from append_to_response
    @SerializedName("combined_credits")
    private PersonCreditsContainer combinedCredits;
    
    @SerializedName("images")
    private PersonImagesContainer images;

    public int getId() { return id; }
    public String getName() { return name; }
    public String getBiography() { return biography; }
    public String getPlaceOfBirth() { return placeOfBirth; }
    public String getProfilePath() { return profilePath; }
    public String getKnownForDepartment() { return knownForDepartment; }
    public String getBirthday() { return birthday; }
    public String getDeathday() { return deathday; }
    public List<String> getAlsoKnownAs() { return alsoKnownAs; }
    public String getHomepage() { return homepage; }
    public String getImdbId() { return imdbId; }
    public PersonCreditsContainer getCombinedCredits() { return combinedCredits; }
    public PersonImagesContainer getImages() { return images; }
    
    public String getProfileUrl() {
        if (profilePath != null && !profilePath.isEmpty()) {
            return "https://image.tmdb.org/t/p/w500" + profilePath;
        }
        return null;
    }
}