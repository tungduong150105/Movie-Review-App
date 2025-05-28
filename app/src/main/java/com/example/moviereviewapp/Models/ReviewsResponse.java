package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ReviewsResponse {
    private int page;
    private List<ReviewResult> results;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("total_results")
    private int totalResults;

    public int getPage() {
        return page;
    }

    public List<ReviewResult> getResults() {
        return results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }
}