package com.example.moviereviewapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moviereviewapp.R;
import com.example.moviereviewapp.databinding.ActivityTitleDetailBinding;

import com.example.moviereviewapp.Activities.MovieDetail;
import com.example.moviereviewapp.Activities.tvseriesdetail;

import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TitleDetailActivity extends AppCompatActivity {

    private ActivityTitleDetailBinding binding;
    private static final String TMDB_API_KEY = "75d6190f47f7d58c6d0511ca393d2f7d";
    private static final String TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    private int itemId = -1;
    private String itemType = null;

    private Call<MovieDetail> movieDetailCall;
    private Call<tvseriesdetail> tvShowDetailCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTitleDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.episodesTextView.setVisibility(View.GONE);
        binding.seasonTextView.setVisibility(View.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemId = extras.getInt("itemId", -1);
            itemType = extras.getString("itemType");

            Log.d("TitleDetailActivity", "Received Item ID: " + itemId);
            Log.d("TitleDetailActivity", "Received Item Type: " + itemType);

            if (itemId != -1 && itemType != null) {
                if (itemType.equals("movie")) {
                    fetchMovieDetail(itemId);
                } else if (itemType.equals("tv")) {
                    fetchTvShowDetail(itemId);
                } else {
                    Log.e("TitleDetailActivity", "Unknown item type: " + itemType);
                    showError("Could not load details for this item.");
                }
            } else {
                Log.e("TitleDetailActivity", "Missing item ID or type in Intent");
                showError("Could not load details. Missing information.");
            }

        } else {
            Log.e("TitleDetailActivity", "No extras found in Intent");
            showError("Could not load details. No information provided.");
        }
    }

    private void fetchMovieDetail(int movieId) {
        TMDBApi api = RetrofitClient.getApiService();
        movieDetailCall = api.getMovieDetail(movieId, TMDB_API_KEY);

        movieDetailCall.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieDetail movieDetail = response.body();
                    Log.d("TitleDetailActivity", "Fetched Movie Detail: " + movieDetail.getTitle());

                    binding.titleTextView.setText(movieDetail.getTitle());
                    binding.overviewTextView.setText(movieDetail.getOverview());
                    binding.ratingTextView.setText("Rating: " + movieDetail.getRating());

                    if (binding.runtimeTextView != null) {
                        binding.runtimeTextView.setText("Runtime: " + movieDetail.getRuntime() + " minutes");
                        binding.runtimeTextView.setVisibility(View.VISIBLE);
                    }
                    binding.episodesTextView.setVisibility(View.GONE);
                    binding.seasonTextView.setVisibility(View.GONE);

                    if (binding.posterImageView != null && movieDetail.getPosterPath() != null) {
                        Glide.with(TitleDetailActivity.this)
                                .load(TMDB_IMAGE_BASE_URL + movieDetail.getPosterPath())
                                .into(binding.posterImageView);
                    }

                } else {
                    Log.e("TitleDetailActivity", "Failed to fetch movie detail: " + response.code());
                    showError("Failed to load movie details.");
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                Log.e("TitleDetailActivity", "Error fetching movie detail: " + t.getMessage(), t);
                showError("Error loading movie details.");
            }
        });
    }

    private void fetchTvShowDetail(int tvShowId) {
        TMDBApi api = RetrofitClient.getApiService();
        tvShowDetailCall = api.getTvSeriesDetail(tvShowId, TMDB_API_KEY);

        tvShowDetailCall.enqueue(new Callback<tvseriesdetail>() {
            @Override
            public void onResponse(Call<tvseriesdetail> call, Response<tvseriesdetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvseriesdetail tvShowDetail = response.body();
                    Log.d("TitleDetailActivity", "Fetched TV Show Detail: " + tvShowDetail.getName());

                    binding.titleTextView.setText(tvShowDetail.getName());
                    binding.overviewTextView.setText(tvShowDetail.getOverview());
                    binding.ratingTextView.setText("Rating: " + tvShowDetail.getRating());

                    if (binding.episodesTextView != null) {
                        binding.episodesTextView.setText("Episodes: " + tvShowDetail.getnumberofepsidose());
                        binding.episodesTextView.setVisibility(View.VISIBLE);
                    }
                    if (binding.seasonTextView != null) {
                        binding.seasonTextView.setText("Season: " + tvShowDetail.getSeasonnumber());
                        binding.seasonTextView.setVisibility(View.VISIBLE);
                    }
                    if (binding.runtimeTextView != null) {

                        binding.runtimeTextView.setText("Episode Runtime: " + tvShowDetail.getRuntime() + " minutes");
                        binding.runtimeTextView.setVisibility(View.VISIBLE);
                    }

                    if (binding.posterImageView != null && tvShowDetail.getPosterPath() != null) {
                        Glide.with(TitleDetailActivity.this)
                                .load(TMDB_IMAGE_BASE_URL + tvShowDetail.getPosterPath())
                                .into(binding.posterImageView);
                    }

                } else {
                    Log.e("TitleDetailActivity", "Failed to fetch TV show detail: " + response.code());
                    showError("Failed to load TV show details.");
                }
            }

            @Override
            public void onFailure(Call<tvseriesdetail> call, Throwable t) {
                Log.e("TitleDetailActivity", "Error fetching TV show detail: " + t.getMessage(), t);
                showError("Error loading TV show details.");
            }
        });
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        if (movieDetailCall != null) {
            movieDetailCall.cancel();
        }
        if (tvShowDetailCall != null) {
            tvShowDetailCall.cancel();
        }
        super.onDestroy();
    }

}