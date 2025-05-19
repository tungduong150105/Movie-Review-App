package com.example.moviereviewapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.example.moviereviewapp.Adapters.PersonAdapter;
import com.example.moviereviewapp.Adapters.PhotoAdapter;
import com.example.moviereviewapp.Adapters.SimilarItemsAdapter;
import com.example.moviereviewapp.Adapters.VideoAdapter;
import com.example.moviereviewapp.Models.Crew;
import com.example.moviereviewapp.Models.Genre;
import com.example.moviereviewapp.Models.Image;
import com.example.moviereviewapp.Models.Keyword;
import com.example.moviereviewapp.Activities.MovieDetail;
import com.example.moviereviewapp.Models.MovieImages;
import com.example.moviereviewapp.Models.MovieKeywordResponse;
import com.example.moviereviewapp.Models.Person;
import com.example.moviereviewapp.Models.PhotoItem;
import com.example.moviereviewapp.Models.SimilarItem;
import com.example.moviereviewapp.Models.SimilarItemsResponse;
import com.example.moviereviewapp.Models.TvShowImages;
import com.example.moviereviewapp.Models.TvShowKeywordResponse;
import com.example.moviereviewapp.Activities.tvseriesdetail;
import com.example.moviereviewapp.Models.VideoResponse;
import com.example.moviereviewapp.Models.VideoResult;
import com.example.moviereviewapp.Models.trendingall;
import com.example.moviereviewapp.R;
import com.example.moviereviewapp.databinding.ActivityTitleDetailBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TitleDetailActivity extends AppCompatActivity {
    private static final String TAG = "TitleDetailActivity";
    private static final String TMDB_API_KEY = "75d6190f47f7d58c6d0511ca393d2f7d";
    private static final String TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private static final String ITEM_TYPE_MOVIE = "movie";
    private static final String ITEM_TYPE_TV = "tv";
    private ActivityTitleDetailBinding binding;
    private int itemId = -1;
    private String itemType = null;
    private PersonAdapter castAdapter;
    private PhotoAdapter photoAdapter;
    private SimilarItemsAdapter similarItemsAdapter;
    private VideoAdapter videoAdapter;
    private List<Call<?>> apiCalls = new ArrayList<>();
    private YouTubePlayerView youtubePlayerView;
    private YouTubePlayer activeMainPlayer;
    private YouTubePlayer activeOverlayPlayer;
    private String mainTrailerVideoId = null;
    private String currentOverlayVideoId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTitleDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (binding.mainYouTubePlayerView != null) {
            getLifecycle().addObserver(binding.mainYouTubePlayerView);
            setupMainYouTubePlayerView();
        } else {
            Log.e(TAG, "mainYouTubePlayerView not found in binding.");
        }
        if (binding.overlayYouTubePlayerView != null) {
            getLifecycle().addObserver(binding.overlayYouTubePlayerView);
            setupOverlayYouTubePlayerView();
        } else {
            Log.e(TAG, "overlayYouTubePlayerView not found in binding.");
        }
        setupRecyclerViews();
        if (!parseIntentExtras()) {
            showErrorAndFinish("Could not load details. Essential information missing.");
            return;
        }
        fetchItemDetails();
        setupClickListeners();
        setupSeeAll();
    }
    private void setupSeeAll(){

    }

    private boolean parseIntentExtras() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemId = extras.getInt("itemId", -1);
            itemType = extras.getString("itemType");
            Log.d(TAG, "Received Item ID: " + itemId + ", Type: " + itemType);
            return itemId != -1 && itemType != null;
        }
        Log.e(TAG, "No extras found in Intent");
        return false;
    }

    private void setupRecyclerViews() {
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        if (binding.topCastRecyclerView != null && binding.castTextView != null) {
            castAdapter = new PersonAdapter(new ArrayList<>());
            binding.topCastRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.topCastRecyclerView.setAdapter(castAdapter);
            binding.topCastRecyclerView.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
        } else {
            Log.w(TAG, "Top Cast RecyclerView or its title TextView is null in binding.");
        }
        if (binding.photosRecyclerView != null && binding.photosTextView != null) {
            photoAdapter = new PhotoAdapter(new ArrayList<>());
            binding.photosRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.photosRecyclerView.setAdapter(photoAdapter);
        } else {
            Log.w(TAG, "Photos RecyclerView or its title TextView is null in binding.");
        }
        if (binding.videosRecyclerView != null && binding.videosTextView != null) {
            videoAdapter = new VideoAdapter(this, new ArrayList<>(), video -> {
                playVideoInOverlay(video.getKey());
            });
            binding.videosRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.videosRecyclerView.setAdapter(videoAdapter);
        } else {
            Log.w(TAG, "Videos RecyclerView or its title TextView is null in binding.");
        }
        if (binding.moreLikeThisRecyclerView != null && binding.moreLikeThisTextView != null) {
            similarItemsAdapter = new SimilarItemsAdapter(this, new ArrayList<>(), this::onSimilarItemClicked);
            binding.moreLikeThisRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            binding.moreLikeThisRecyclerView.setAdapter(similarItemsAdapter);
            binding.moreLikeThisRecyclerView.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
        } else {
            Log.w(TAG, "Similar Items RecyclerView or its title TextView is null in binding.");
        }
    }

    private void setupMainYouTubePlayerView() {
        binding.mainYouTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                activeMainPlayer = youTubePlayer;
                Log.d(TAG, "Main YouTube Player is ready.");
                if (mainTrailerVideoId != null && !mainTrailerVideoId.isEmpty()) {
                    playMainTrailerVideo(mainTrailerVideoId);
                }
            }

            @Override
            public void onError(@NonNull YouTubePlayer youTubePlayer, com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerError error) {
                Log.e(TAG, "Main YouTube Player Error: " + error.toString());
                Toast.makeText(TitleDetailActivity.this, "Error loading main trailer", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupOverlayYouTubePlayerView() {
        binding.overlayYouTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                activeOverlayPlayer = youTubePlayer;
                Log.d(TAG, "Overlay YouTube Player is ready.");
                if (binding.overlayPlayerContainer.getVisibility() == View.VISIBLE && currentOverlayVideoId != null && !currentOverlayVideoId.isEmpty()) {
                    activeOverlayPlayer.loadVideo(currentOverlayVideoId, 0);
                }
            }

            @Override
            public void onError(@NonNull YouTubePlayer youTubePlayer, com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants.PlayerError error) {
                Log.e(TAG, "Overlay YouTube Player Error: " + error.toString());
                Toast.makeText(TitleDetailActivity.this, "Error playing video in overlay", Toast.LENGTH_SHORT).show();
                hideVideoOverlay();
            }
        });
        if (binding.closeOverlayPlayerButton != null) {
            binding.closeOverlayPlayerButton.setOnClickListener(v -> hideVideoOverlay());
        }
    }

    private void playVideoInOverlay(String videoId) {
        if (TextUtils.isEmpty(videoId)) {
            Toast.makeText(this, "Video not available for overlay.", Toast.LENGTH_SHORT).show();
            return;
        }
        currentOverlayVideoId = videoId;
        if (activeMainPlayer != null && binding.mainYouTubePlayerView.getVisibility() == View.VISIBLE) {
            activeMainPlayer.pause();
        }
        if (activeOverlayPlayer != null) {
            activeOverlayPlayer.loadVideo(videoId, 0);
            binding.overlayPlayerContainer.setVisibility(View.VISIBLE);
        } else {
            Log.d(TAG, "Overlay player not ready, video will load in onReady.");
            binding.overlayPlayerContainer.setVisibility(View.VISIBLE);
        }
    }

    private void hideVideoOverlay() {
        if (activeOverlayPlayer != null) {
            activeOverlayPlayer.pause();
        }
        binding.overlayPlayerContainer.setVisibility(View.GONE);
        currentOverlayVideoId = null;
    }

    private void playMainTrailerVideo(String videoId) {
        if (activeMainPlayer != null && videoId != null && !videoId.isEmpty()) {
            Log.d(TAG, "Playing main trailer: " + videoId);
            if (activeOverlayPlayer != null && binding.overlayPlayerContainer.getVisibility() == View.VISIBLE) {
                activeOverlayPlayer.pause();
            }
            setViewVisibility(binding.mainYouTubePlayerView, true);
            activeMainPlayer.cueVideo(videoId, 0);
        } else if (videoId == null || videoId.isEmpty()) {
            Log.d(TAG, "No main trailer video ID to play.");
            setViewVisibility(binding.mainYouTubePlayerView, false);
        } else {
            Log.d(TAG, "Main player not ready, mainTrailerVideoId will be loaded in onReady.");
            setViewVisibility(binding.mainYouTubePlayerView, false);
        }
    }

    private void fetchItemDetails() {
        if (ITEM_TYPE_MOVIE.equals(itemType)) {
            fetchMovieDetail(itemId);
            fetchVideos(itemId, ITEM_TYPE_MOVIE);
            fetchSimilarItems(itemId, ITEM_TYPE_MOVIE);
            fetchMovieImages(itemId);
            fetchMovieKeywords(itemId);
        } else if (ITEM_TYPE_TV.equals(itemType)) {
            fetchTvSeriesDetail(itemId);
            fetchVideos(itemId, ITEM_TYPE_TV);
            fetchSimilarItems(itemId, ITEM_TYPE_TV);
            fetchTvShowImages(itemId);
            fetchTvShowKeywords(itemId);
        } else {
            Log.e(TAG, "Unknown item type: " + itemType);
            showErrorAndFinish("Could not load details for this item type.");
        }
    }

    private void fetchMovieDetail(int movieId) {
        TMDBApi api = RetrofitClient.getApiService();
        Call<MovieDetail> call = api.getMovieDetailWithCreditsAndGenres(movieId, TMDB_API_KEY, "credits");
        apiCalls.add(call);
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(@NonNull Call<MovieDetail> call, @NonNull Response<MovieDetail> response) {
                apiCalls.remove(call);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Fetched Movie Detail: " + response.body().getTitle());
                    updateUiWithMovieDetails(response.body());
                } else {
                    Log.e(TAG, "Failed to fetch movie detail: " + response.code());
                    showError("Failed to load movie details.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieDetail> call, @NonNull Throwable t) {
                apiCalls.remove(call);
                Log.e(TAG, "Error fetching movie detail", t);
                showError("Error loading movie details.");
            }
        });
    }
    private void fetchPersonDetail(Person basicPerson) {
        TMDBApi api = RetrofitClient.getApiService();
        Call<PersonDetail>detail= api.getPersonDetail(basicPerson.getPersonid(), TMDB_API_KEY);

        detail.enqueue(new Callback<PersonDetail>() {
            @Override
            public void onResponse(Call<PersonDetail> call, Response<PersonDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PersonDetail detail = response.body();
                    basicPerson.setBirthdate(detail.getBirthday());
                    basicPerson.setDeathday(detail.getDeathday());

                }
            }

            @Override
            public void onFailure(Call<PersonDetail> call, Throwable t) {
                Log.e("API_DETAIL_ERROR", "Lỗi lấy chi tiết: " + t.getMessage(), t);
            }
        });
    }
    private void fetchTvSeriesDetail(int tvShowId) {
        TMDBApi api = RetrofitClient.getApiService();
        Call<tvseriesdetail> call = api.getTvSeriesDetailWithCreditsAndGenres(tvShowId, TMDB_API_KEY, "credits");
        apiCalls.add(call);
        call.enqueue(new Callback<tvseriesdetail>() {
            @Override
            public void onResponse(@NonNull Call<tvseriesdetail> call, @NonNull Response<tvseriesdetail> response) {
                apiCalls.remove(call);
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Fetched TV Show Detail: " + response.body().getName());
                    updateUiWithTvShowDetails(response.body());
                } else {
                    Log.e(TAG, "Failed to fetch TV show detail: " + response.code());
                    showError("Failed to load TV show details.");
                }
            }

            @Override
            public void onFailure(@NonNull Call<tvseriesdetail> call, @NonNull Throwable t) {
                apiCalls.remove(call);
                Log.e(TAG, "Error fetching TV show detail", t);
                showError("Error loading TV show details.");
            }
        });
    }

    private void fetchVideos(int id, String type) {
        TMDBApi api = RetrofitClient.getApiService();
        Call<VideoResponse> call;
        if (ITEM_TYPE_MOVIE.equals(type)) {
            call = api.getMovieVideos(id, TMDB_API_KEY, "en-US");
        } else if (ITEM_TYPE_TV.equals(type)) {
            call = api.getTvShowVideos(id, TMDB_API_KEY, "en-US");
        } else {
            Log.e(TAG, "Unknown type for fetching videos: " + type);
            updateVideosUI(null);
            return;
        }
        apiCalls.add(call);
        final Call<VideoResponse> finalCall = call;
        call.enqueue(new Callback<VideoResponse>() {
            @Override
            public void onResponse(@NonNull Call<VideoResponse> cbCall, @NonNull Response<VideoResponse> response) {
                apiCalls.remove(cbCall);
                if (response.isSuccessful() && response.body() != null && response.body().getResults() != null) {
                    List<VideoResult> allVideos = response.body().getResults();
                    List<VideoResult> youtubeVideos = filterYoutubeVideos(allVideos);
                    if (!youtubeVideos.isEmpty()) {
                        mainTrailerVideoId = findPrimaryTrailerId(youtubeVideos);
                        if (mainTrailerVideoId != null) {
                            playMainTrailerVideo(mainTrailerVideoId);
                        } else {
                            setViewVisibility(binding.mainYouTubePlayerView, false);
                        }
                        updateVideosRecyclerView(youtubeVideos);
                    } else {
                        Log.w(TAG, "No YouTube videos found.");
                        setViewVisibility(binding.mainYouTubePlayerView, false);
                        updateVideosRecyclerView(null);
                    }
                } else {
                    setViewVisibility(binding.mainYouTubePlayerView, false);
                    updateVideosRecyclerView(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<VideoResponse> cbCall, @NonNull Throwable t) {
                apiCalls.remove(finalCall);
                Log.e(TAG, "Error fetching videos", t);
                setViewVisibility(binding.mainYouTubePlayerView, false);
                updateVideosUI(null);
            }
        });
    }

    private List<VideoResult> filterYoutubeVideos(List<VideoResult> allVideos) {
        List<VideoResult> youtubeVideos = new ArrayList<>();
        if (allVideos != null) {
            for (VideoResult video : allVideos) {
                if ("YouTube".equalsIgnoreCase(video.getSite()) && ("Trailer".equalsIgnoreCase(video.getType()) || "Teaser".equalsIgnoreCase(video.getType()) || "Clip".equalsIgnoreCase(video.getType()) || "Featurette".equalsIgnoreCase(video.getType()))) {
                    youtubeVideos.add(video);
                }
            }
        }
        return youtubeVideos;
    }

    private String findPrimaryTrailerId(List<VideoResult> videos) {
        for (VideoResult video : videos) {
            if ("Trailer".equalsIgnoreCase(video.getType()) && video.isOfficial()) {
                return video.getKey();
            }
        }
        for (VideoResult video : videos) {
            if ("Trailer".equalsIgnoreCase(video.getType())) {
                return video.getKey();
            }
        }
        return !videos.isEmpty() ? videos.get(0).getKey() : null;
    }

    private void updateVideosUI(List<VideoResult> videos) {
        boolean hasVideos = videos != null && !videos.isEmpty();
        if (videoAdapter != null) {
            videoAdapter.updateVideos(hasVideos ? videos : new ArrayList<>());
        }
        setViewVisibility(binding.videosTextView, hasVideos);
        setViewVisibility(binding.videosRecyclerView, hasVideos);
        setViewVisibility(binding.tvSeeAllVideos, hasVideos && videos.size() > 5);
    }

    private void updateUiWithMovieDetails(MovieDetail movie) {
        binding.titleTextView.setText(movie.getTitle());
        updateOverview(movie.getOverview());
        displayGenres(movie.getGenres());
        for(Person a: movie.getCast()){
            fetchPersonDetail(a);
        };
        updateCast(movie.getCast());
        binding.seeAllCastTextView.setOnClickListener(V->{
            Intent intent=new Intent(TitleDetailActivity.this,SeeAllActivity.class);

            intent.putExtra("type","person");
            intent.putExtra("title","Cast");

            intent.putExtra("personList",(Serializable) movie.getCast());
            if(movie.getCast()!=null){
                startActivity(intent);
            }
        });
        updatePoster(movie.getPosterPath());
        updateCrew(movie.getCrew());
        updateMovieReleaseDate(movie.getReleaseDate(), movie.getTitle());
        updateTextListDetail(binding.countryOriginTextView, binding.countryOriginValueTextView, movie.getOriginCountry());
        List<String> languageNames = movie.getSpokenLanguages() != null ? movie.getSpokenLanguages().stream().map(MovieDetail.SpokenLanguage::getName).collect(Collectors.toList()) : null;
        updateTextListDetail(binding.languageSpokenTextView, binding.languageSpokenValueTextView, languageNames);
        List<String> companyNames = movie.getProductionCompanies() != null ? movie.getProductionCompanies().stream().map(MovieDetail.ProductionCompany::getName).collect(Collectors.toList()) : null;
        updateTextListDetail(binding.productionCompaniesTextView, binding.productionCompaniesValueTextView, companyNames);
        updateRuntime(binding.runtimeTextView, binding.runtimeValueTextView, movie.getRuntime() > 0 ? movie.getRuntime() + " minutes" : null);
        updateTagline(movie.getTagline());
    }

    private void updateUiWithTvShowDetails(tvseriesdetail tvShow) {
        binding.titleTextView.setText(tvShow.getName());
        updateOverview(tvShow.getOverview());
        displayGenres(tvShow.getGenres());
        for(Person a: tvShow.getCast()){
            fetchPersonDetail(a);
        };
        updateCast(tvShow.getCast());
        binding.seeAllCastTextView.setOnClickListener(V->{
            Intent intent=new Intent(TitleDetailActivity.this,SeeAllActivity.class);

            intent.putExtra("type","person");
            intent.putExtra("title","Cast");

            intent.putExtra("personList",(Serializable) tvShow.getCast());
            if(tvShow.getCast()!=null){
                startActivity(intent);
            }
        });
        updatePoster(tvShow.getPosterPath());
        updateCrew(tvShow.getCrew());
        updateTvShowAirDates(tvShow.getFirstAirDate(), tvShow.getLastAirDate(), tvShow.getName());
        updateTextListDetail(binding.countryOriginTextView, binding.countryOriginValueTextView, tvShow.getOriginCountry());
        List<String> languageNames = tvShow.getSpokenLanguages() != null ? tvShow.getSpokenLanguages().stream().map(tvseriesdetail.SpokenLanguage::getName).collect(Collectors.toList()) : null;
        updateTextListDetail(binding.languageSpokenTextView, binding.languageSpokenValueTextView, languageNames);
        List<String> companyNames = tvShow.getProductionCompanies() != null ? tvShow.getProductionCompanies().stream().map(tvseriesdetail.ProductionCompany::getName).collect(Collectors.toList()) : null;
        updateTextListDetail(binding.productionCompaniesTextView, binding.productionCompaniesValueTextView, companyNames);
        int[] runtimes = tvShow.getEpisodeRunTime();
        String runtimeStr = null;
        if (runtimes != null && runtimes.length > 0 && runtimes[0] > 0) {
            runtimeStr = runtimes[0] + " minutes (avg)";
        }
        updateRuntime(binding.runtimeTextView, binding.runtimeValueTextView, runtimeStr);
        setViewVisibility(binding.releaseDateDetailTextView, false);
        setViewVisibility(binding.releaseDateValueTextView, false);
    }

    private void updateOverview(@Nullable String overview) {
        if (!TextUtils.isEmpty(overview)) {
            binding.plotDetailsTextView.setText(overview);
            binding.movieDescriptionTextView.setText(overview);
            setViewVisibility(binding.plotSummaryTextView, true);
            setViewVisibility(binding.plotDetailsTextView, true);
            setViewVisibility(binding.movieDescriptionTextView, true);
        } else {
            setViewVisibility(binding.plotSummaryTextView, false);
            setViewVisibility(binding.plotDetailsTextView, false);
            setViewVisibility(binding.movieDescriptionTextView, false);
        }
    }

    private void updateCast(@Nullable List<Person> cast) {
        castAdapter.setPersonList(cast != null ? cast : new ArrayList<>());
        setViewVisibility(binding.topCastRecyclerView, cast != null && !cast.isEmpty());
    }

    private void updatePoster(@Nullable String posterPath) {
        if (!TextUtils.isEmpty(posterPath)) {
            Glide.with(this).load(TMDB_IMAGE_BASE_URL + posterPath).placeholder(R.drawable.rounded_image_background).error(R.drawable.rounded_image_background).into(binding.moviePosterImageView);
        } else {
            Glide.with(this).load(R.drawable.rounded_image_background).into(binding.moviePosterImageView);
        }
    }

    private void updateCrew(@Nullable List<Crew> crewList) {
        if (crewList != null && !crewList.isEmpty()) {
            String directors = crewList.stream().filter(c -> "Director".equals(c.getJob())).map(Crew::getName).collect(Collectors.joining(", "));
            setTextAndVisibility(binding.directorNameTextView, directors, binding.directorTextView);
            String writers = crewList.stream().filter(c -> "Screenplay".equals(c.getJob()) || "Writer".equals(c.getJob()) || "Story".equals(c.getJob())).map(Crew::getName).collect(Collectors.joining(", "));
            setTextAndVisibility(binding.writersNamesTextView, writers, binding.writersTextView);
        } else {
            setViewVisibility(binding.directorTextView, false);
            setViewVisibility(binding.directorNameTextView, false);
            setViewVisibility(binding.writersTextView, false);
            setViewVisibility(binding.writersNamesTextView, false);
        }
    }

    private void updateMovieReleaseDate(@Nullable String releaseDateStr, String movieTitle) {
        if (TextUtils.isEmpty(releaseDateStr)) {
            setViewVisibility(binding.yearTextView, false);
            setViewVisibility(binding.releaseDateDetailTextView, false);
            setViewVisibility(binding.releaseDateValueTextView, false);
            setViewVisibility(binding.comingSoonTextView, false);
            return;
        }
        try {
            SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date = apiDateFormat.parse(releaseDateStr);
            Date currentDate = new Date();
            if (date != null) {
                boolean isComingSoon = date.after(currentDate);
                setViewVisibility(binding.comingSoonTextView, isComingSoon);
                setViewVisibility(binding.releaseDateDetailTextView, isComingSoon);
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.US);
                String year = yearFormat.format(date);
                binding.yearTextView.setText(year);
                setViewVisibility(binding.yearTextView, true);
                SimpleDateFormat detailDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
                binding.releaseDateValueTextView.setText(detailDateFormat.format(date));
                setViewVisibility(binding.releaseDateValueTextView, true);
            }
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing movie release date: " + releaseDateStr, e);
            setViewVisibility(binding.yearTextView, false);
            setViewVisibility(binding.releaseDateDetailTextView, false);
            setViewVisibility(binding.releaseDateValueTextView, false);
            setViewVisibility(binding.comingSoonTextView, false);
        }
    }

    private void updateTvShowAirDates(@Nullable String firstAirDateStr, @Nullable String lastAirDateStr, String tvShowName) {
        if (TextUtils.isEmpty(firstAirDateStr)) {
            setViewVisibility(binding.yearTextView, false);
            setViewVisibility(binding.comingSoonTextView, false);
            return;
        }
        try {
            SimpleDateFormat apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date firstDate = apiDateFormat.parse(firstAirDateStr);
            Date currentDate = new Date();
            if (firstDate != null) {
                setViewVisibility(binding.comingSoonTextView, firstDate.after(currentDate));
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.US);
                String firstYear = yearFormat.format(firstDate);
                binding.yearTextView.setText(firstYear);
                setViewVisibility(binding.yearTextView, true);
                String airDatesRange = firstYear + " - ";
                if (!TextUtils.isEmpty(lastAirDateStr)) {
                    Date lastDate = apiDateFormat.parse(lastAirDateStr);
                    if (lastDate != null) {
                        airDatesRange += yearFormat.format(lastDate);
                    } else {
                        airDatesRange += "?";
                    }
                } else {
                    airDatesRange += "?";
                }
            }
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing TV show air date: " + firstAirDateStr, e);
            setViewVisibility(binding.yearTextView, false);
            setViewVisibility(binding.comingSoonTextView, false);
        }
    }

    private void updateTextListDetail(TextView labelView, TextView valueView, @Nullable List<String> values) {
        if (values != null && !values.isEmpty()) {
            valueView.setText(TextUtils.join(", ", values));
            setViewVisibility(labelView, true);
            setViewVisibility(valueView, true);
        } else {
            setViewVisibility(labelView, false);
            setViewVisibility(valueView, false);
        }
    }

    private void updateRuntime(TextView labelView, TextView valueView, @Nullable String runtimeText) {
        if (!TextUtils.isEmpty(runtimeText)) {
            valueView.setText(runtimeText);
            setViewVisibility(labelView, true);
            setViewVisibility(valueView, true);
        } else {
            setViewVisibility(labelView, false);
            setViewVisibility(valueView, false);
        }
    }

    private void updateTagline(@Nullable String tagline) {
        setTextAndVisibility(binding.taglineDetailsTextView, tagline, binding.taglineTitleTextView);
    }

    private void updateVideosRecyclerView(List<VideoResult> videos) {
        boolean hasVideos = videos != null && !videos.isEmpty();
        if (videoAdapter != null) {
            videoAdapter.updateVideos(hasVideos ? videos : new ArrayList<>());
        }
        if (binding.videosTextView != null) setViewVisibility(binding.videosTextView, hasVideos);
        if (binding.videosRecyclerView != null)
            setViewVisibility(binding.videosRecyclerView, hasVideos);
    }

    private void displayGenres(@Nullable List<Genre> genres) {
        binding.mainGenresFlowLayout.removeAllViews();
        binding.storylineGenresFlowLayout.removeAllViews();
        boolean hasGenres = genres != null && !genres.isEmpty();
        setViewVisibility(binding.storylineGenresTextView, hasGenres);
        if (hasGenres) {
            LayoutInflater inflater = LayoutInflater.from(this);
            for (Genre genre : genres) {
                if (genre.getName() != null) {
                    addChipToLayout(inflater, binding.mainGenresFlowLayout, genre.getName());
                    addChipToLayout(inflater, binding.storylineGenresFlowLayout, genre.getName());
                }
            }
        } else {
            Log.d(TAG, "Genre list is null or empty.");
        }
    }

    private void displayKeywords(@Nullable List<Keyword> keywords) {
        binding.keywordsFlexboxLayout.removeAllViews();
        boolean hasKeywords = keywords != null && !keywords.isEmpty();
        setViewVisibility(binding.keywordsTextView, hasKeywords);
        setViewVisibility(binding.keywordsFlexboxLayout, hasKeywords);
        if (hasKeywords) {
            LayoutInflater inflater = LayoutInflater.from(this);
            for (Keyword keyword : keywords) {
                if (keyword.getName() != null) {
                    addChipToLayout(inflater, binding.keywordsFlexboxLayout, keyword.getName());
                }
            }
        } else {
            Log.d(TAG, "Keyword list is null or empty.");
        }
    }

    private void addChipToLayout(LayoutInflater inflater, ViewGroup layout, String text) {
        TextView chipView = (TextView) inflater.inflate(R.layout.item_genre_keyword, layout, false);
        chipView.setText(text);
        layout.addView(chipView);
    }

    private void fetchMovieImages(int movieId) {
        TMDBApi api = RetrofitClient.getApiService();
        Call<MovieImages> call = api.getMovieImages(movieId, TMDB_API_KEY);
        apiCalls.add(call);
        call.enqueue(new Callback<MovieImages>() {
            @Override
            public void onResponse(@NonNull Call<MovieImages> call, @NonNull Response<MovieImages> response) {
                apiCalls.remove(call);
                if (response.isSuccessful() && response.body() != null) {
                    updatePhotoAdapterWithImages(response.body().getBackdrops(), response.body().getPosters());
                } else {
                    Log.e(TAG, "Failed to fetch movie images: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieImages> call, @NonNull Throwable t) {
                apiCalls.remove(call);
                Log.e(TAG, "Error fetching movie images", t);
            }
        });
    }

    private void fetchTvShowImages(int tvShowId) {
        TMDBApi api = RetrofitClient.getApiService();
        Call<TvShowImages> call = api.getTvShowImages(tvShowId, TMDB_API_KEY);
        apiCalls.add(call);
        call.enqueue(new Callback<TvShowImages>() {
            @Override
            public void onResponse(@NonNull Call<TvShowImages> call, @NonNull Response<TvShowImages> response) {
                apiCalls.remove(call);
                if (response.isSuccessful() && response.body() != null) {
                    updatePhotoAdapterWithImages(response.body().getBackdrops(), response.body().getPosters());
                } else {
                    Log.e(TAG, "Failed to fetch TV show images: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowImages> call, @NonNull Throwable t) {
                apiCalls.remove(call);
                Log.e(TAG, "Error fetching TV show images", t);
            }
        });
    }

    private void updatePhotoAdapterWithImages(@Nullable List<Image> backdrops, @Nullable List<Image> posters) {
        List<PhotoItem> photoItems = new ArrayList<>();
        if (backdrops != null) {
            for (Image image : backdrops) {
                if (image.getFilePath() != null) {
                    photoItems.add(new PhotoItem(TMDB_IMAGE_BASE_URL + image.getFilePath(), image.getAspectRatio()));
                }
            }
        }
        if (posters != null) {
            for (Image image : posters) {
                if (image.getFilePath() != null) {
                    photoItems.add(new PhotoItem(TMDB_IMAGE_BASE_URL + image.getFilePath(), image.getAspectRatio()));
                }
            }
        }
        Collections.shuffle(photoItems);
        photoAdapter.setPhotoList(photoItems);
        setViewVisibility(binding.photosTextView, !photoItems.isEmpty());
        setViewVisibility(binding.photosRecyclerView, !photoItems.isEmpty());
    }

    private void fetchMovieKeywords(int movieId) {
        TMDBApi api = RetrofitClient.getApiService();
        Call<MovieKeywordResponse> call = api.getMovieKeywords(movieId, TMDB_API_KEY);
        apiCalls.add(call);
        call.enqueue(new Callback<MovieKeywordResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieKeywordResponse> call, @NonNull Response<MovieKeywordResponse> response) {
                apiCalls.remove(call);
                if (response.isSuccessful() && response.body() != null) {
                    displayKeywords(response.body().getKeywords());
                } else {
                    Log.e(TAG, "Failed to fetch movie keywords: " + response.code());
                    displayKeywords(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieKeywordResponse> call, @NonNull Throwable t) {
                apiCalls.remove(call);
                Log.e(TAG, "Error fetching movie keywords", t);
                displayKeywords(null);
            }
        });
    }

    private void fetchTvShowKeywords(int tvShowId) {
        TMDBApi api = RetrofitClient.getApiService();
        Call<TvShowKeywordResponse> call = api.getTvShowKeywords(tvShowId, TMDB_API_KEY);
        apiCalls.add(call);
        call.enqueue(new Callback<TvShowKeywordResponse>() {
            @Override
            public void onResponse(@NonNull Call<TvShowKeywordResponse> call, @NonNull Response<TvShowKeywordResponse> response) {
                apiCalls.remove(call);
                if (response.isSuccessful() && response.body() != null) {
                    displayKeywords(response.body().getKeywords());
                } else {
                    Log.e(TAG, "Failed to fetch TV show keywords: " + response.code());
                    displayKeywords(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<TvShowKeywordResponse> call, @NonNull Throwable t) {
                apiCalls.remove(call);
                Log.e(TAG, "Error fetching TV show keywords", t);
                displayKeywords(null);
            }
        });
    }

    private void fetchSimilarItems(int currentItemId, String currentItemType) {
        TMDBApi api = RetrofitClient.getApiService();
        Call<SimilarItemsResponse> call = null;
        if (ITEM_TYPE_MOVIE.equals(currentItemType)) {
            call = api.getSimilarMovies(currentItemId, TMDB_API_KEY, "en-US", 1);
        } else if (ITEM_TYPE_TV.equals(currentItemType)) {
            call = api.getSimilarTvShows(currentItemId, TMDB_API_KEY, "en-US", 1);
        }
        if (call == null) {
            Log.e(TAG, "Unknown item type for fetching similar items: " + currentItemType);
            updateSimilarItemsUI(null);
            return;
        }
        apiCalls.add(call);
        final Call<SimilarItemsResponse> finalCall = call;
        call.enqueue(new Callback<SimilarItemsResponse>() {
            @Override
            public void onResponse(@NonNull Call<SimilarItemsResponse> cbCall, @NonNull Response<SimilarItemsResponse> response) {
                apiCalls.remove(finalCall);
                if (response.isSuccessful() && response.body() != null) {
                    updateSimilarItemsUI(response.body().getResults());
                } else {
                    Log.e(TAG, "Failed to fetch similar items: " + response.code());
                    updateSimilarItemsUI(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<SimilarItemsResponse> cbCall, @NonNull Throwable t) {
                apiCalls.remove(finalCall);
                if (cbCall.isCanceled()) {
                    Log.d(TAG, "Similar items call was cancelled.");
                } else {
                    Log.e(TAG, "Error fetching similar items", t);
                }
                updateSimilarItemsUI(null);
            }
        });
    }

    private void updateSimilarItemsUI(@Nullable List<SimilarItem> items) {
        boolean hasItems = items != null && !items.isEmpty();
        TMDBApi api = RetrofitClient.getApiService();
        for (SimilarItem movie : items) {
            int movieId = movie.getId();
            Call<MovieDetail> detailCall = api.getMovieDetail(movieId, TMDB_API_KEY);
            detailCall.enqueue(new Callback<MovieDetail>() {
                @Override
                public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        int runtime = response.body().getRuntime();
                        movie.setLengthFromRuntime(runtime);



                    }
                }

                @Override
                public void onFailure(Call<MovieDetail> call, Throwable t) {
                    Log.e("DETAIL_ERROR", "Lỗi khi lấy runtime cho movieId: " + movieId, t);
                }
            });
        }
        if (hasItems) {
            similarItemsAdapter.updateData(items);
        } else {
            similarItemsAdapter.updateData(new ArrayList<>());
            Log.d(TAG, "No similar items found or error occurred.");
        }
        setViewVisibility(binding.moreLikeThisTextView, hasItems);
        setViewVisibility(binding.moreLikeThisRecyclerView, hasItems);
        binding.tvSeeAllMoreLikeThis.setOnClickListener(V->{
            Intent intent=new Intent(TitleDetailActivity.this,SeeAllActivity.class);

            intent.putExtra("type","similaritem");
            intent.putExtra("title","More like this");

            intent.putExtra("movieList",(Serializable) items);
            if(items!=null){
                startActivity(intent);
            }
        });

    }

    private void onSimilarItemClicked(SimilarItem item) {
        Log.d(TAG, "Clicked similar item: ID " + item.getId() + ", Title: " + item.getTitle());
        Intent intent = new Intent(TitleDetailActivity.this, TitleDetailActivity.class);
        intent.putExtra("itemId", item.getId());
        intent.putExtra("itemType", this.itemType);
        startActivity(intent);
    }

    private void setViewVisibility(View view, boolean visible) {
        if (view != null) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private void setTextAndVisibility(TextView textView, @Nullable String text, @Nullable View... relatedViewsToShow) {
        if (textView == null) return;
        boolean hasText = !TextUtils.isEmpty(text);
        if (hasText) {
            textView.setText(text);
        }
        setViewVisibility(textView, hasText);
        if (relatedViewsToShow != null) {
            for (View relatedView : relatedViewsToShow) {
                setViewVisibility(relatedView, hasText);
            }
        }
    }

    private void setupClickListeners() {
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void showErrorAndFinish(String message) {
        showError(message);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (youtubePlayerView != null) {
            youtubePlayerView.release();
        }
        for (Call<?> call : apiCalls) {
            if (call != null && !call.isCanceled()) {
                call.cancel();
            }
        }
        apiCalls.clear();
        Log.d(TAG, "onDestroy: API calls cancelled and cleared, YouTube player released.");
    }
}