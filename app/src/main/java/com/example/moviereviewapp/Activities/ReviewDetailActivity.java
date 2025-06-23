package com.example.moviereviewapp.Activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moviereviewapp.Models.UserReview;
import com.example.moviereviewapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReviewDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_review_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Initialize views
        ImageView backButton = findViewById(R.id.back_ReviewDetail_Btn);
        TextView headerTitle = findViewById(R.id.reviewTitleHeader);
        ImageView ratingStarIcon = findViewById(R.id.ratingStarIcon);
        TextView ratingTextView = findViewById(R.id.ratingTextView);
        TextView authorDateTextView = findViewById(R.id.authorDateTextView);
        TextView contentTextView = findViewById(R.id.reviewContentTextView);

        // Set back button click listener
        backButton.setOnClickListener(v -> finish());

        // Get review data from intent
        UserReview review = (UserReview) getIntent().getSerializableExtra("review");
        String movieTitle = getIntent().getStringExtra("movieTitle");

        if (review != null) {
            // Set header title with movie name
            headerTitle.setText("User review: " + movieTitle);

            // Handle rating display
            if (review.getRating() > 0) {
                ratingTextView.setText(String.valueOf(review.getRating()));
                ratingStarIcon.setVisibility(View.VISIBLE);
                ratingTextView.setVisibility(View.VISIBLE);
            } else {
                ratingStarIcon.setVisibility(View.GONE);
                ratingTextView.setVisibility(View.GONE);
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
                    // If date parsing fails, use the original string
                    if (!review.getCreatedAt().isEmpty()) {
                        formattedDate = " • " + review.getCreatedAt();
                    }
                }
            }

            authorDateTextView.setText(review.getUsername() + formattedDate);
            contentTextView.setText(review.getContent());
        } else {
            finish();
        }
    }
}