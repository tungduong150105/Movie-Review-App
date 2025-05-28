package com.example.moviereviewapp.Models;

import java.io.Serializable;

public class UserReview implements Serializable {
    private String id;
    private String username;
    private int rating;
    private String title;
    private String content;
    private String createdAt;
    
    public UserReview(String id, String username, int rating, String title, String content, String createdAt) {
        this.id = id;
        this.username = username;
        this.rating = rating;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }
    
    public String getId() {
        return id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public int getRating() {
        return rating;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getContent() {
        return content;
    }
    
    public String getCreatedAt() {
        return createdAt;
    }
}