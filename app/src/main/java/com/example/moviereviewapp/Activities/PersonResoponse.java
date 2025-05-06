package com.example.moviereviewapp.Activities;

import com.example.moviereviewapp.Models.Person;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PersonResoponse {
    @SerializedName("results")
    private List<Person> persons;
    public List<Person> getPersons(){
        return persons;
    }
}
