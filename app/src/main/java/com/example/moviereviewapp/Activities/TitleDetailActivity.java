package com.example.moviereviewapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.moviereviewapp.Adapters.PersonAdapter;
import com.example.moviereviewapp.Adapters.PhotoAdapter;
import com.example.moviereviewapp.Adapters.SimilarItemsAdapter;
import com.example.moviereviewapp.Models.Image;
import com.example.moviereviewapp.Models.Keyword;
import com.example.moviereviewapp.Models.MovieImages;
import com.example.moviereviewapp.Models.MovieKeywordResponse;
import com.example.moviereviewapp.Models.PhotoItem;
import com.example.moviereviewapp.Models.SimilarItem;
import com.example.moviereviewapp.Models.SimilarItemsResponse;
import com.example.moviereviewapp.Models.TvShowImages;
import com.example.moviereviewapp.Models.TvShowKeywordResponse;
import com.example.moviereviewapp.R;
import com.example.moviereviewapp.databinding.ActivityTitleDetailBinding;
import com.example.moviereviewapp.Models.Person;
import com.example.moviereviewapp.Models.Crew;
import com.example.moviereviewapp.Models.Genre;
import com.bumptech.glide.Glide;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TitleDetailActivity extends AppCompatActivity {
    private ActivityTitleDetailBinding binding;
    private final String TMDB_API_KEY = "75d6190f47f7d58c6d0511ca393d2f7d";
    private final String TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private int itemId = -1;
    private String itemType = null;
    private Call<MovieDetail> movieDetailCall;
    private Call<tvseriesdetail> tvShowDetailCall;
    private PersonAdapter castAdapter;
    private PhotoAdapter photoAdapter;
    private SimilarItemsAdapter similarItemsAdapter;
    private Call<SimilarItemsResponse> similarItemsCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTitleDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemId = extras.getInt("itemId", -1);
            itemType = extras.getString("itemType");
            Log.d("TitleDetailActivity", "Received Item ID: " + itemId);
            Log.d("TitleDetailActivity", "Received Item Type: " + itemType);
            if (itemId != -1 && itemType != null) {
                if ("movie".equals(itemType)) {
                    fetchMovieDetail(itemId);
                    fetchSimilarItems(itemId, "movie");
                } else if ("tv".equals(itemType)) {
                    fetchTvSeriesDetail(itemId);
                    fetchSimilarItems(itemId, "tv");
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
        if (binding.topCastRecyclerView != null) {
            castAdapter = new PersonAdapter(new ArrayList<>());
            binding.topCastRecyclerView.setAdapter(castAdapter);
        }
        if (binding.photosRecyclerView != null) {
            photoAdapter = new PhotoAdapter(new ArrayList<>());
            binding.photosRecyclerView.setAdapter(photoAdapter);
        }
        setupSimilarItemsRecyclerView();
        hideDetailSections();
        setupClickListeners();
    }

    private void hideDetailSections() {
        binding.movieDescriptionTextView.setVisibility(View.GONE);
        binding.directorTextView.setVisibility(View.GONE);
        binding.directorNameTextView.setVisibility(View.GONE);
        binding.writersTextView.setVisibility(View.GONE);
        binding.writersNamesTextView.setVisibility(View.GONE);
        binding.yearTextView.setVisibility(View.GONE);
        binding.releaseDateTextView.setVisibility(View.GONE);
        binding.releaseDateDetailTextView.setVisibility(View.GONE);
        binding.releaseDateValueTextView.setVisibility(View.GONE);
        binding.comingSoonTextView.setVisibility(View.GONE);
        binding.countryOriginTextView.setVisibility(View.GONE);
        binding.countryOriginValueTextView.setVisibility(View.GONE);
        binding.languageSpokenTextView.setVisibility(View.GONE);
        binding.languageSpokenValueTextView.setVisibility(View.GONE);
        binding.productionCompaniesTextView.setVisibility(View.GONE);
        binding.productionCompaniesValueTextView.setVisibility(View.GONE);
        binding.runtimeTextView.setVisibility(View.GONE);
        binding.runtimeValueTextView.setVisibility(View.GONE);
        binding.plotSummaryTextView.setVisibility(View.GONE);
        binding.plotDetailsTextView.setVisibility(View.GONE);
        binding.taglineTitleTextView.setVisibility(View.GONE);
        binding.taglineDetailsTextView.setVisibility(View.GONE);
    }

    private void fetchMovieDetail(int movieId) {
        TMDBApi api = RetrofitClient.getApiService();
        movieDetailCall = api.getMovieDetailWithCreditsAndGenres(movieId, TMDB_API_KEY, "credits");
        movieDetailCall.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieDetail movieDetail = response.body();
                    Log.d("TitleDetailActivity", "Fetched Movie Detail: " + movieDetail.getTitle());
                    updateUiWithDetails(movieDetail, null);
                    fetchMovieImages(movieId);
                    fetchMovieKeywords(movieId);
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

    private void fetchTvSeriesDetail(int tvShowId) {
        TMDBApi api = RetrofitClient.getApiService();
        tvShowDetailCall = api.getTvSeriesDetailWithCreditsAndGenres(tvShowId, TMDB_API_KEY, "credits");
        tvShowDetailCall.enqueue(new Callback<tvseriesdetail>() {
            @Override
            public void onResponse(Call<tvseriesdetail> call, Response<tvseriesdetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tvseriesdetail tvShowDetail = response.body();
                    Log.d("TitleDetailActivity", "Fetched TV Show Detail: " + tvShowDetail.getName());
                    updateUiWithDetails(null, tvShowDetail);
                    fetchTvShowImages(tvShowId);
                    fetchTvShowKeywords(tvShowId);
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

    private void updateUiWithDetails(MovieDetail movieDetail, tvseriesdetail tvShowDetail) {
        String title = (movieDetail != null) ? movieDetail.getTitle() : tvShowDetail.getName();
        binding.titleTextView.setText(title);
        String overview = (movieDetail != null) ? movieDetail.getOverview() : tvShowDetail.getOverview();
        if (overview != null && !overview.isEmpty()) {
            binding.plotDetailsTextView.setText(overview);
            binding.movieDescriptionTextView.setText(overview);
            binding.plotSummaryTextView.setVisibility(View.VISIBLE);
            binding.plotDetailsTextView.setVisibility(View.VISIBLE);
            binding.movieDescriptionTextView.setVisibility(View.VISIBLE);
        } else {
            binding.plotSummaryTextView.setVisibility(View.GONE);
            binding.plotDetailsTextView.setVisibility(View.GONE);
            binding.movieDescriptionTextView.setVisibility(View.GONE);
        }
        List<Genre> genres = (movieDetail != null) ? movieDetail.getGenres() : tvShowDetail.getGenres();
        displayGenres(genres);
        List<Person> cast = (movieDetail != null) ? movieDetail.getCast() : tvShowDetail.getCast();
        if (castAdapter != null) {
            castAdapter.setPersonList(cast != null ? cast : new ArrayList<>());
        }
        String posterPath = (movieDetail != null) ? movieDetail.getPosterPath() : tvShowDetail.getPosterPath();
        if (posterPath != null) {
            Glide.with(this)
                    .load(TMDB_IMAGE_BASE_URL + posterPath)
                    .placeholder(R.drawable.mission_impossible_7__dead_reckoning_part_2_poster_by_andrewvm_dg3rz84)
                    .into(binding.moviePosterImageView);
        }
        List<Crew> crewList = (movieDetail != null) ? movieDetail.getCrew() : tvShowDetail.getCrew();
        if (crewList != null) {
        } else {
            binding.directorTextView.setVisibility(View.GONE);
            binding.directorNameTextView.setVisibility(View.GONE);
            binding.writersTextView.setVisibility(View.GONE);
            binding.writersNamesTextView.setVisibility(View.GONE);
        }
        if (movieDetail != null) {
            updateUiForMovie(movieDetail);
        } else if (tvShowDetail != null) {
            updateUiForTvSeries(tvShowDetail);
        }
    }

    private void updateUiForMovie(MovieDetail movieDetail) {
        String releaseDate = movieDetail.getReleaseDate();
        if (releaseDate != null && !releaseDate.isEmpty()) {
            try {
                SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date date = apiDateFormat.parse(releaseDate);
                Date currentDate = new Date();
                if (date != null && date.after(currentDate)) {
                    binding.comingSoonTextView.setVisibility(View.VISIBLE);
                    binding.releaseDateDetailTextView.setVisibility(View.VISIBLE);
                } else {
                    binding.comingSoonTextView.setVisibility(View.GONE);
                    binding.releaseDateDetailTextView.setVisibility(View.GONE);
                }
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.US);
                if (date != null) {
                    binding.yearTextView.setText(yearFormat.format(date));
                    binding.yearTextView.setVisibility(View.VISIBLE);
                    binding.releaseDateTextView.setText(String.format(Locale.US, "%s (%s)", movieDetail.getTitle(), yearFormat.format(date)));
                    binding.releaseDateTextView.setVisibility(View.VISIBLE);
                    SimpleDateFormat detailDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
                    binding.releaseDateValueTextView.setText(detailDateFormat.format(date));
                    binding.releaseDateValueTextView.setVisibility(View.VISIBLE);
                } else {
                    binding.yearTextView.setVisibility(View.GONE);
                    binding.releaseDateTextView.setVisibility(View.GONE);
                    binding.releaseDateDetailTextView.setVisibility(View.GONE);
                    binding.releaseDateValueTextView.setVisibility(View.GONE);
                    binding.comingSoonTextView.setVisibility(View.GONE);
                }
            } catch (ParseException e) {
                Log.e("TitleDetailActivity", "Error parsing movie release date: " + releaseDate, e);
                binding.yearTextView.setVisibility(View.GONE);
                binding.releaseDateTextView.setVisibility(View.GONE);
                binding.releaseDateDetailTextView.setVisibility(View.GONE);
                binding.releaseDateValueTextView.setVisibility(View.GONE);
                binding.comingSoonTextView.setVisibility(View.GONE);
            }
        } else {
            binding.yearTextView.setVisibility(View.GONE);
            binding.releaseDateTextView.setVisibility(View.GONE);
            binding.releaseDateDetailTextView.setVisibility(View.GONE);
            binding.releaseDateValueTextView.setVisibility(View.GONE);
            binding.comingSoonTextView.setVisibility(View.GONE);
            Log.d("TitleDetailActivity", "Movie release date is null or empty.");
        }
        if (movieDetail.getOriginCountry() != null && !movieDetail.getOriginCountry().isEmpty()) {
            binding.countryOriginValueTextView.setText(android.text.TextUtils.join(", ", movieDetail.getOriginCountry()));
            binding.countryOriginTextView.setVisibility(View.VISIBLE);
            binding.countryOriginValueTextView.setVisibility(View.VISIBLE);
        } else {
            binding.countryOriginTextView.setVisibility(View.GONE);
            binding.countryOriginValueTextView.setVisibility(View.GONE);
        }
        if (movieDetail.getSpokenLanguages() != null && !movieDetail.getSpokenLanguages().isEmpty()) {
            List<String> languageNames = new ArrayList<>();
            for (MovieDetail.SpokenLanguage language : movieDetail.getSpokenLanguages()) {
                if (language.getName() != null) {
                    languageNames.add(language.getName());
                }
            }
            binding.languageSpokenValueTextView.setText(android.text.TextUtils.join(", ", languageNames));
            binding.languageSpokenTextView.setVisibility(View.VISIBLE);
            binding.languageSpokenValueTextView.setVisibility(View.VISIBLE);
        } else {
            binding.languageSpokenTextView.setVisibility(View.GONE);
            binding.languageSpokenValueTextView.setVisibility(View.GONE);
        }
        if (movieDetail.getProductionCompanies() != null && !movieDetail.getProductionCompanies().isEmpty()) {
            List<String> companyNames = new ArrayList<>();
            for (MovieDetail.ProductionCompany company : movieDetail.getProductionCompanies()) {
                if (company.getName() != null) {
                    companyNames.add(company.getName());
                }
            }
            binding.productionCompaniesValueTextView.setText(android.text.TextUtils.join(", ", companyNames));
            binding.productionCompaniesTextView.setVisibility(View.VISIBLE);
            binding.productionCompaniesValueTextView.setVisibility(View.VISIBLE);
        } else {
            binding.productionCompaniesTextView.setVisibility(View.GONE);
            binding.productionCompaniesValueTextView.setVisibility(View.GONE);
        }
        if (movieDetail.getRuntime() > 0) {
            binding.runtimeValueTextView.setText(movieDetail.getRuntime() + " minutes");
            binding.runtimeTextView.setVisibility(View.VISIBLE);
            binding.runtimeValueTextView.setVisibility(View.VISIBLE);
        } else {
            binding.runtimeTextView.setVisibility(View.GONE);
            binding.runtimeValueTextView.setVisibility(View.GONE);
        }
    }

    private void updateUiForTvSeries(tvseriesdetail tvShowDetail) {
        String firstAirDate = tvShowDetail.getFirstAirDate();
        String lastAirDate = tvShowDetail.getLastAirDate();
        Date currentDate = new Date();
        if (firstAirDate != null && !firstAirDate.isEmpty()) {
            try {
                SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                Date firstDate = apiDateFormat.parse(firstAirDate);
                if (firstDate != null && firstDate.after(currentDate)) {
                    binding.comingSoonTextView.setVisibility(View.VISIBLE);
                } else {
                    binding.comingSoonTextView.setVisibility(View.GONE);
                }
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.US);
                if (firstDate != null) {
                    binding.yearTextView.setText(yearFormat.format(firstDate));
                    binding.yearTextView.setVisibility(View.VISIBLE);
                    String airDatesRange = yearFormat.format(firstDate) + " - ";
                    if (lastAirDate != null && !lastAirDate.isEmpty()) {
                        Date lastDate = apiDateFormat.parse(lastAirDate);
                        airDatesRange += yearFormat.format(lastDate);
                    } else {
                        airDatesRange += "?";
                    }
                    binding.releaseDateTextView.setText(tvShowDetail.getName() + " (" + airDatesRange + ")");
                    binding.releaseDateTextView.setVisibility(View.VISIBLE);
                } else {
                    binding.yearTextView.setVisibility(View.GONE);
                    binding.releaseDateTextView.setVisibility(View.GONE);
                    binding.comingSoonTextView.setVisibility(View.GONE);
                }
            } catch (ParseException e) {
                Log.e("TitleDetailActivity", "Error parsing TV show first air date: " + firstAirDate, e);
                binding.yearTextView.setVisibility(View.GONE);
                binding.releaseDateTextView.setVisibility(View.GONE);
                binding.comingSoonTextView.setVisibility(View.GONE);
            }
        } else {
            binding.yearTextView.setVisibility(View.GONE);
            binding.releaseDateTextView.setVisibility(View.GONE);
            binding.comingSoonTextView.setVisibility(View.GONE);
            Log.d("TitleDetailActivity", "TV show first air date is null or empty.");
        }
        if (tvShowDetail.getOriginCountry() != null && !tvShowDetail.getOriginCountry().isEmpty()) {
            binding.countryOriginValueTextView.setText(android.text.TextUtils.join(", ", tvShowDetail.getOriginCountry()));
            binding.countryOriginTextView.setVisibility(View.VISIBLE);
            binding.countryOriginValueTextView.setVisibility(View.VISIBLE);
        } else {
            binding.countryOriginTextView.setVisibility(View.GONE);
            binding.countryOriginValueTextView.setVisibility(View.GONE);
        }
        if (tvShowDetail.getSpokenLanguages() != null && !tvShowDetail.getSpokenLanguages().isEmpty()) {
            List<String> languageNames = new ArrayList<>();
            for (tvseriesdetail.SpokenLanguage language : tvShowDetail.getSpokenLanguages()) {
                if (language.getName() != null) {
                    languageNames.add(language.getName());
                }
            }
            binding.languageSpokenValueTextView.setText(android.text.TextUtils.join(", ", languageNames));
            binding.languageSpokenTextView.setVisibility(View.VISIBLE);
            binding.languageSpokenValueTextView.setVisibility(View.VISIBLE);
        } else {
            binding.languageSpokenTextView.setVisibility(View.GONE);
            binding.languageSpokenValueTextView.setVisibility(View.GONE);
        }
        if (tvShowDetail.getProductionCompanies() != null && !tvShowDetail.getProductionCompanies().isEmpty()) {
            List<String> companyNames = new ArrayList<>();
            for (tvseriesdetail.ProductionCompany company : tvShowDetail.getProductionCompanies()) {
                if (company.getName() != null) {
                    companyNames.add(company.getName());
                }
            }
            binding.productionCompaniesValueTextView.setText(android.text.TextUtils.join(", ", companyNames));
            binding.productionCompaniesTextView.setVisibility(View.VISIBLE);
            binding.productionCompaniesValueTextView.setVisibility(View.VISIBLE);
        } else {
            binding.productionCompaniesTextView.setVisibility(View.GONE);
            binding.productionCompaniesValueTextView.setVisibility(View.GONE);
        }
        int[] episodeRunTimes = tvShowDetail.getEpisodeRunTime();
        if (episodeRunTimes != null && episodeRunTimes.length > 0) {
            int runtime = episodeRunTimes[0];
            if (runtime > 0) {
                binding.runtimeValueTextView.setText(runtime + " minutes");
                binding.runtimeTextView.setVisibility(View.VISIBLE);
                binding.runtimeValueTextView.setVisibility(View.VISIBLE);
            } else {
                binding.runtimeTextView.setVisibility(View.GONE);
                binding.runtimeValueTextView.setVisibility(View.GONE);
            }
        } else {
            binding.runtimeTextView.setVisibility(View.GONE);
            binding.runtimeValueTextView.setVisibility(View.GONE);
        }
        binding.releaseDateDetailTextView.setVisibility(View.GONE);
        binding.releaseDateValueTextView.setVisibility(View.GONE);
    }

    private void fetchMovieImages(int movieId) {
        TMDBApi api = RetrofitClient.getApiService();
        Call<MovieImages> call = api.getMovieImages(movieId, TMDB_API_KEY);
        call.enqueue(new Callback<MovieImages>() {
            @Override
            public void onResponse(Call<MovieImages> call, Response<MovieImages> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MovieImages movieImages = response.body();
                    List<PhotoItem> photoItems = new ArrayList<>();
                    if (movieImages.getBackdrops() != null) {
                        for (Image image : movieImages.getBackdrops()) {
                            if (image.getFilePath() != null) {
                                photoItems.add(new PhotoItem(TMDB_IMAGE_BASE_URL + image.getFilePath(), image.getAspectRatio()));
                            }
                        }
                    }
                    if (movieImages.getPosters() != null) {
                        for (Image image : movieImages.getPosters()) {
                            if (image.getFilePath() != null) {
                                photoItems.add(new PhotoItem(TMDB_IMAGE_BASE_URL + image.getFilePath(), image.getAspectRatio()));
                            }
                        }
                    }
                    Collections.shuffle(photoItems);
                    if (photoAdapter != null) {
                        photoAdapter.setPhotoList(photoItems);
                    }
                } else {
                    Log.e("TitleDetailActivity", "Failed to fetch movie images: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<MovieImages> call, Throwable t) {
                Log.e("TitleDetailActivity", "Error fetching movie images: " + t.getMessage(), t);
            }
        });
    }

    private void fetchTvShowImages(int tvShowId) {
        TMDBApi api = RetrofitClient.getApiService();
        Call<TvShowImages> call = api.getTvShowImages(tvShowId, TMDB_API_KEY);
        call.enqueue(new Callback<TvShowImages>() {
            @Override
            public void onResponse(Call<TvShowImages> call, Response<TvShowImages> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TvShowImages tvShowImages = response.body();
                    List<PhotoItem> photoItems = new ArrayList<>();
                    if (tvShowImages.getBackdrops() != null) {
                        for (Image image : tvShowImages.getBackdrops()) {
                            if (image.getFilePath() != null) {
                                photoItems.add(new PhotoItem(TMDB_IMAGE_BASE_URL + image.getFilePath(), image.getAspectRatio()));
                            }
                        }
                    }
                    if (tvShowImages.getPosters() != null) {
                        for (Image image : tvShowImages.getPosters()) {
                            if (image.getFilePath() != null) {
                                photoItems.add(new PhotoItem(TMDB_IMAGE_BASE_URL + image.getFilePath(), image.getAspectRatio()));
                            }
                        }
                    }
                    Collections.shuffle(photoItems);
                    if (photoAdapter != null) {
                        photoAdapter.setPhotoList(photoItems);
                    }
                } else {
                    Log.e("TitleDetailActivity", "Failed to fetch TV show images: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TvShowImages> call, Throwable t) {
                Log.e("TitleDetailActivity", "Error fetching TV show images: " + t.getMessage(), t);
            }
        });
    }

    private void fetchMovieKeywords(int movieId) {
        TMDBApi api = RetrofitClient.getApiService();
        Call<MovieKeywordResponse> call = api.getMovieKeywords(movieId, TMDB_API_KEY);
        call.enqueue(new Callback<MovieKeywordResponse>() {
            @Override
            public void onResponse(Call<MovieKeywordResponse> call, Response<MovieKeywordResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getKeywords() != null) {
                    List<Keyword> keywords = response.body().getKeywords();
                    displayKeywords(keywords);
                } else {
                    Log.e("TitleDetailActivity", "Failed to fetch movie keywords: " + response.code());
                    binding.keywordsTextView.setVisibility(View.GONE);
                    binding.keywordsFlexboxLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<MovieKeywordResponse> call, Throwable t) {
                Log.e("TitleDetailActivity", "Error fetching movie keywords: " + t.getMessage(), t);
                binding.keywordsTextView.setVisibility(View.GONE);
                binding.keywordsFlexboxLayout.setVisibility(View.GONE);
            }
        });
    }

    private void fetchTvShowKeywords(int tvShowId) {
        TMDBApi api = RetrofitClient.getApiService();
        Call<TvShowKeywordResponse> call = api.getTvShowKeywords(tvShowId, TMDB_API_KEY);
        call.enqueue(new Callback<TvShowKeywordResponse>() {
            @Override
            public void onResponse(Call<TvShowKeywordResponse> call, Response<TvShowKeywordResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getKeywords() != null) {
                    List<Keyword> keywords = response.body().getKeywords();
                    displayKeywords(keywords);
                } else {
                    Log.e("TitleDetailActivity", "Failed to fetch TV show keywords: " + response.code());
                    binding.keywordsTextView.setVisibility(View.GONE);
                    binding.keywordsFlexboxLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<TvShowKeywordResponse> call, Throwable t) {
                Log.e("TitleDetailActivity", "Error fetching TV show keywords: " + t.getMessage(), t);
                binding.keywordsTextView.setVisibility(View.GONE);
                binding.keywordsFlexboxLayout.setVisibility(View.GONE);
            }
        });
    }

    private void displayGenres(List<Genre> genres) {
        binding.mainGenresFlowLayout.removeAllViews();
        binding.storylineGenresFlowLayout.removeAllViews();
        if (genres != null && !genres.isEmpty()) {
            LayoutInflater inflater = LayoutInflater.from(this);
            for (Genre genre : genres) {
                if (genre.getName() != null) {
                    String genreName = genre.getName();
                    TextView mainGenreTextView = (TextView) inflater.inflate(R.layout.item_genre_keyword, binding.mainGenresFlowLayout, false);
                    mainGenreTextView.setText(genreName);
                    binding.mainGenresFlowLayout.addView(mainGenreTextView);
                    TextView storylineGenreTextView = (TextView) inflater.inflate(R.layout.item_genre_keyword, binding.storylineGenresFlowLayout, false);
                    storylineGenreTextView.setText(genreName);
                    binding.storylineGenresFlowLayout.addView(storylineGenreTextView);
                }
            }
            binding.storylineGenresTextView.setVisibility(View.VISIBLE);
        } else {
            Log.d("TitleDetailActivity", "Genre list is null or empty.");
            binding.storylineGenresTextView.setVisibility(View.GONE);
        }
    }

    private void displayKeywords(List<Keyword> keywords) {
        binding.keywordsFlexboxLayout.removeAllViews();
        if (keywords != null && !keywords.isEmpty()) {
            binding.keywordsTextView.setVisibility(View.VISIBLE);
            binding.keywordsFlexboxLayout.setVisibility(View.VISIBLE);
            LayoutInflater inflater = LayoutInflater.from(this);
            for (Keyword keyword : keywords) {
                if (keyword.getName() != null) {
                    String keywordName = keyword.getName();
                    TextView keywordTextView = (TextView) inflater.inflate(R.layout.item_genre_keyword, binding.keywordsFlexboxLayout, false);
                    keywordTextView.setText(keywordName);
                    binding.keywordsFlexboxLayout.addView(keywordTextView);
                }
            }
        } else {
            Log.d("TitleDetailActivity", "Keyword list is null or empty.");
            binding.keywordsTextView.setVisibility(View.GONE);
            binding.keywordsFlexboxLayout.setVisibility(View.GONE);
        }
    }

    private void setupSimilarItemsRecyclerView() {
        if (binding.moreLikeThisRecyclerView != null) {
            similarItemsAdapter = new SimilarItemsAdapter(this, new ArrayList<>(), new SimilarItemsAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(SimilarItem item) {
                    Log.d("TitleDetailActivity", "Clicked similar item: ID " + item.getId() + ", Title: " + item.getTitle());
                    Intent intent = new Intent(TitleDetailActivity.this, TitleDetailActivity.class);
                    intent.putExtra("itemId", item.getId());
                    if ("movie".equals(itemType)) {
                        intent.putExtra("itemType", "movie");
                    } else if ("tv".equals(itemType)) {
                        intent.putExtra("itemType", "tv");
                    }
                    startActivity(intent);
                }
            });
            binding.moreLikeThisRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.moreLikeThisRecyclerView.setAdapter(similarItemsAdapter);
        } else {
            Log.e("TitleDetailActivity", "moreLikeThisRecyclerView is null. Check your layout and binding.");
        }
    }

    private void fetchSimilarItems(int currentItemId, String currentItemType) {
        TMDBApi api = RetrofitClient.getApiService();
        if ("movie".equals(currentItemType)) {
            similarItemsCall = api.getSimilarMovies(currentItemId, TMDB_API_KEY, "en-US", 1);
        } else if ("tv".equals(currentItemType)) {
            similarItemsCall = api.getSimilarTvShows(currentItemId, TMDB_API_KEY, "en-US", 1);
        } else {
            Log.e("TitleDetailActivity", "Unknown item type for fetching similar items: " + currentItemType);
            return;
        }
        if (similarItemsCall == null) return;
        similarItemsCall.enqueue(new Callback<SimilarItemsResponse>() {
            @Override
            public void onResponse(Call<SimilarItemsResponse> call, Response<SimilarItemsResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getResults() != null) {
                    List<SimilarItem> similarItems = response.body().getResults();
                    if (similarItemsAdapter != null && !similarItems.isEmpty()) {
                        similarItemsAdapter.updateData(similarItems);
                        binding.moreLikeThisTextView.setVisibility(View.VISIBLE);
                        binding.moreLikeThisRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        binding.moreLikeThisTextView.setVisibility(View.GONE);
                        binding.moreLikeThisRecyclerView.setVisibility(View.GONE);
                        Log.d("TitleDetailActivity", "No similar items found or adapter is null.");
                    }
                } else {
                    Log.e("TitleDetailActivity", "Failed to fetch similar items: " + response.code());
                    binding.moreLikeThisTextView.setVisibility(View.GONE);
                    binding.moreLikeThisRecyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<SimilarItemsResponse> call, Throwable t) {
                if (call.isCanceled()) {
                    Log.d("TitleDetailActivity", "Similar items call was cancelled.");
                } else {
                    Log.e("TitleDetailActivity", "Error fetching similar items: " + t.getMessage(), t);
                }
                binding.moreLikeThisTextView.setVisibility(View.GONE);
                binding.moreLikeThisRecyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void setupClickListeners() {
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