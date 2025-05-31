package com.example.moviereviewapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviereviewapp.Activities.ReviewDetailActivity;
import com.example.moviereviewapp.Models.UserReview;
import com.example.moviereviewapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ReviewSeeAllAdapter extends RecyclerView.Adapter<ReviewSeeAllAdapter.ReviewViewHolder> {

    private List<UserReview> reviews;
    private Context context;
    private String movieTitle;

    public ReviewSeeAllAdapter(Context context, List<UserReview> reviews, String movieTitle) {
        this.context = context;
        this.reviews = reviews;
        this.movieTitle = movieTitle;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review_see_all, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        UserReview review = reviews.get(position);

        // Handle rating display
        if (review.getRating() > 0) {
            holder.ratingTextView.setText(String.valueOf(review.getRating()));
            holder.starIcon.setVisibility(View.VISIBLE);
            holder.ratingTextView.setVisibility(View.VISIBLE);
        } else {
            holder.starIcon.setVisibility(View.GONE);
            holder.ratingTextView.setVisibility(View.GONE);
        }

        // Format date
        String formattedDate = "";
        if (review.getCreatedAt() != null && !review.getCreatedAt().isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
                SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy", Locale.US);
                Date date = inputFormat.parse(review.getCreatedAt());
                if (date != null) {
                    formattedDate = " • " + outputFormat.format(date);
                }
            } catch (Exception e) {
                if (!review.getCreatedAt().isEmpty()) {
                    formattedDate = " • " + review.getCreatedAt();
                }
            }
        }

        holder.authorDateTextView.setText(review.getUsername() + formattedDate);
        holder.contentTextView.setText(review.getContent());
        
        // Set click listener to open full review
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ReviewDetailActivity.class);
            intent.putExtra("review", review);
            intent.putExtra("movieTitle", movieTitle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return reviews != null ? reviews.size() : 0;
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        ImageView starIcon;
        TextView ratingTextView;
        TextView authorDateTextView;
        TextView contentTextView;

        ReviewViewHolder(View itemView) {
            super(itemView);
            starIcon = itemView.findViewById(R.id.reviewStarIcon);
            ratingTextView = itemView.findViewById(R.id.reviewRatingTextView);
            authorDateTextView = itemView.findViewById(R.id.authorDateTextView);
            contentTextView = itemView.findViewById(R.id.reviewContentTextView);
        }
    }
}