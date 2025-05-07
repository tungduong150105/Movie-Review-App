package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.Calendar;

public class Person {

    public String getName() {
        return name;
    }

    public boolean isGone() {
        return isGone;
    }


    public boolean isAdult() {
        return adult;
    }

    public int getAge() {
        return age;
    }

    public int age;

    public int isGender() {
        return gender;
    }

    public String getPersonid() {
        return personid;
    }

    public String getBirthdate() {
        return birthdate;
    }
    public String getProfileUrl() {
        return "https://image.tmdb.org/t/p/w500" + profile_path;
    }

    public double getPopularity() {
        return popularity;
    }

    @SerializedName("name")
    private String name;
    private String birthdate;

    private String deathday;
    @SerializedName("id")
    private String personid;

    @SerializedName("gender")
    private int gender;

    public void setDeathday(String deathday) {
        this.deathday = deathday;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Person(String name, int age, String deathday, boolean isGone, boolean adult, String profile_path, double popularity, int gender, String personid, String birthdate) {
        this.name = name;
        this.age = age;
        this.isGone = isGone;
        this.adult = adult;
        this.profile_path = profile_path;
        this.popularity = popularity;
        this.gender = gender;
        this.personid = personid;
        this.birthdate = birthdate;
        this.deathday = deathday;
    }

    @SerializedName("popularity")
    private double popularity;

@SerializedName("profile_path")
    private String profile_path;
@SerializedName("adult")
    private boolean adult;
    private boolean isGone;

    public String getAgeOrLifespan() {
        if (birthdate == null || birthdate.isEmpty()) return "N/A";

        int birthYear = Integer.parseInt(birthdate.substring(0, 4));

        if (deathday == null || deathday.isEmpty()) {

            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            return (currentYear - birthYear) + "";
        } else {

            int deathYear = Integer.parseInt(deathday.substring(0, 4));
            return birthYear + " - " + deathYear;
        }
    }

}
