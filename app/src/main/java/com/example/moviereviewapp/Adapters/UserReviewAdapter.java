package com.example.moviereviewapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviereviewapp.Models.UserReview;
import com.example.moviereviewapp.R;

import java.util.List;

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.ReviewViewHolder> {

    private List<UserReview> reviews;
    private OnReviewClickListener listener;

    public interface OnReviewClickListener {
        void onReviewClick(UserReview review);
    }

    public UserReviewAdapter(List<UserReview> reviews, OnReviewClickListener listener) {
        this.reviews = reviews;
        this.listener = listener;
    }
    public List<UserReview> getReviews() {
        return reviews;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        UserReview review = reviews.get(position);
        // Display rating if available
        if (review.getRating() > 0) {
            holder.ratingTextView.setText(String.valueOf(review.getRating()));
            holder.ratingTextView.setVisibility(View.VISIBLE);
        } else {
            holder.ratingTextView.setVisibility(View.GONE);
        }

        holder.usernameTextView.setText(review.getUsername());
        holder.contentTextView.setText(review.getContent());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onReviewClick(review);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews != null ? reviews.size() : 0;
    }

    public void updateReviews(List<UserReview> newReviews) {
        this.reviews = newReviews;
        notifyDataSetChanged();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView ratingTextView;
        TextView usernameTextView;
        TextView contentTextView;

        ReviewViewHolder(View itemView) {
            super(itemView);
            ratingTextView = itemView.findViewById(R.id.reviewRatingTextView);
            usernameTextView = itemView.findViewById(R.id.reviewerNameTextView);
            contentTextView = itemView.findViewById(R.id.reviewContentTextView);
        }
    }
}