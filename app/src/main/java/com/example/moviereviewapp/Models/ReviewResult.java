package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;

public class ReviewResult {
    private String id;
    private String author;
    @SerializedName("author_details")
    private AuthorDetails authorDetails;
    private String content;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    private String url;

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public AuthorDetails getAuthorDetails() {
        return authorDetails;
    }

    public String getContent() {
        return content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getUrl() {
        return url;
    }
}
