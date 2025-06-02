package com.example.moviereviewapp.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviereviewapp.Adapters.MovieSeeAllAdapter;
import com.example.moviereviewapp.Adapters.MovieSeeAllRatingAdapter;
import com.example.moviereviewapp.Adapters.PersonSeeAllAdapter;
import com.example.moviereviewapp.Adapters.PhotoGridAdapter;
import com.example.moviereviewapp.Adapters.ReviewSeeAllAdapter;
import com.example.moviereviewapp.Adapters.SeeAllSimilarItemAdapter;
import com.example.moviereviewapp.Adapters.TVSeriesSeeAllAdapter;
import com.example.moviereviewapp.Adapters.TrendingSeeallAdapter;
import com.example.moviereviewapp.Adapters.VideoSeeAllAdapter;
import com.example.moviereviewapp.Models.PhotoItem;
import com.example.moviereviewapp.Models.SimilarItem;
import com.example.moviereviewapp.Models.UserReview;
import com.example.moviereviewapp.Models.VideoResult;
import com.example.moviereviewapp.Models.movies;
import com.example.moviereviewapp.Models.trendingall;
import com.example.moviereviewapp.Models.tvseries;
import com.example.moviereviewapp.Models.Person;
import com.example.moviereviewapp.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class SeeAllActivity extends AppCompatActivity implements MovieSeeAllAdapter.OnItemClickListener,MovieSeeAllRatingAdapter.OnItemClickListener,TVSeriesSeeAllAdapter.OnItemClickListener,TrendingSeeallAdapter.OnItemClickListener,PersonSeeAllAdapter.OnItemClickListener {

    ImageView back_SeeAll_Btn;
    TextView textView_Title_SeeAll;
    private FrameLayout videoOverlayContainer;
    private YouTubePlayerView youtubePlayerView;
    private YouTubePlayer activePlayer;
    private String currentVideoId = null;
    private FrameLayout photoOverlayContainer;
    private ImageView overlayPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_all);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textView_Title_SeeAll = findViewById(R.id.textView_Title_SeeAll);
        back_SeeAll_Btn = findViewById(R.id.back_SeeAll_Btn);

        // Nút quay lại
        back_SeeAll_Btn.setOnClickListener(v -> finish());

        RecyclerView recyclerView = findViewById(R.id.recycleView_Movie_SeeAll);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // Nhận dữ liệu từ Intent
        String title = getIntent().getStringExtra("title");
        String type = getIntent().getStringExtra("type");

        textView_Title_SeeAll.setText(title);
        if (type == null) {
            Toast.makeText(this, "No type specified", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (type) {
            case "movies":
                List<movies> movieList = (List<movies>) getIntent().getSerializableExtra("movieList");
                if (movieList == null) {
                    movieList = new ArrayList<>();
                    Toast.makeText(this, "No movies found", Toast.LENGTH_SHORT).show();
                }

                if (title != null && title.equals("Recently Viewed")) {
                    MovieSeeAllAdapter movieAdapter = new MovieSeeAllAdapter(this, movieList);
                    movieAdapter.setOnItemClickListener(this);
                    recyclerView.setAdapter(movieAdapter);
                } else {
                    MovieSeeAllRatingAdapter ratingAdapter = new MovieSeeAllRatingAdapter(this, movieList);
                    ratingAdapter.setOnItemClickListener(this);
                    recyclerView.setAdapter(ratingAdapter);
                }
                break;

            case "tvseries":
                List<tvseries> seriesList = (List<tvseries>) getIntent().getSerializableExtra("tvseriesList");
                if (seriesList == null) {
                    seriesList = new ArrayList<>();
                    Toast.makeText(this, "No TV series found", Toast.LENGTH_SHORT).show();
                }

                TVSeriesSeeAllAdapter seriesAdapter = new TVSeriesSeeAllAdapter(this, seriesList);
                seriesAdapter.setOnItemClickListener(this);
                recyclerView.setAdapter(seriesAdapter);
                break;


            case "trending":
                List<trendingall> trendingList = (List<trendingall>) getIntent().getSerializableExtra("trendingList");
                if (trendingList == null) {
                    trendingList = new ArrayList<>();
                    Toast.makeText(this, "No movies found", Toast.LENGTH_SHORT).show();
                }

                TrendingSeeallAdapter trendingAdapter = new TrendingSeeallAdapter(this, trendingList);
                trendingAdapter.setOnItemClickListener(this);
                recyclerView.setAdapter(trendingAdapter);
                break;

            case "person":
                ArrayList<Person> personList = (ArrayList<Person>) getIntent().getSerializableExtra("personList");
                if (personList == null) {
                    personList = new ArrayList<>();
                    Toast.makeText(this, "No persons found", Toast.LENGTH_SHORT).show();
                }

                PersonSeeAllAdapter personAdapter = new PersonSeeAllAdapter(this, personList);
                personAdapter.setOnItemClickListener(this);
                recyclerView.setAdapter(personAdapter);
                break;


            case "similaritem":
                ArrayList<SimilarItem> similarList = (ArrayList<SimilarItem>) getIntent().getSerializableExtra("movieList");
                if (similarList == null) {
                    similarList = new ArrayList<>();
                    Toast.makeText(this, "No persons found", Toast.LENGTH_SHORT).show();
                }

                SeeAllSimilarItemAdapter SeeAllSimilarItemAdapter = new SeeAllSimilarItemAdapter(this, similarList);

                recyclerView.setAdapter(SeeAllSimilarItemAdapter);
                break;

            case "videos":
                ArrayList<VideoResult> videoList = (ArrayList<VideoResult>) getIntent().getSerializableExtra("videoList");
                if (videoList == null) {
                    videoList = new ArrayList<>();
                    Toast.makeText(this, "No videos found", Toast.LENGTH_SHORT).show();
                }

                VideoSeeAllAdapter videoAdapter = new VideoSeeAllAdapter(this, videoList, videoId -> {
                    showVideoOverlay(videoId);
                });
                recyclerView.setAdapter(videoAdapter);
                setupVideoOverlay();
                break;

            case "photos":
                ArrayList<PhotoItem> photoList = (ArrayList<PhotoItem>) getIntent().getSerializableExtra("photoList");
                if (photoList == null) {
                    photoList = new ArrayList<>();
                    Toast.makeText(this, "No photos found", Toast.LENGTH_SHORT).show();
                }

                // Set up grid layout manager with 3 columns and proper spacing
                int spanCount = 3;
                GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
                recyclerView.setLayoutManager(gridLayoutManager);

                PhotoGridAdapter photoAdapter = new PhotoGridAdapter(this, photoList, photoUrl -> {
                    showPhotoOverlay(photoUrl);
                });
                recyclerView.setAdapter(photoAdapter);
                break;
            case "reviews":
                ArrayList<UserReview> reviewsList = (ArrayList<UserReview>) getIntent().getSerializableExtra("reviewsList");
                String reviewMovieTitle = getIntent().getStringExtra("movieTitle");

                if (reviewsList == null) {
                    reviewsList = new ArrayList<>();
                    Toast.makeText(this, "No reviews found", Toast.LENGTH_SHORT).show();
                }

                ReviewSeeAllAdapter reviewAdapter = new ReviewSeeAllAdapter(this, reviewsList, reviewMovieTitle);
                recyclerView.setAdapter(reviewAdapter);

                int reviewSpacing = getResources().getDimensionPixelSize(R.dimen.review_item_spacing);
                recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                    @Override
                    public void getItemOffsets(@NonNull android.graphics.Rect outRect, @NonNull View view,
                                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        outRect.bottom = reviewSpacing;
                        if (parent.getChildAdapterPosition(view) == 0) {
                            outRect.top = reviewSpacing;
                        }
                        outRect.left = reviewSpacing;
                        outRect.right = reviewSpacing;
                    }
                });
                break;
            default:
                Toast.makeText(this, "Unknown type: " + type, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onItemClick(movies movie){
        Log.d("See more ", "Clicked on movie: " + movie.getMoviename());
        Intent intent = new Intent(this, TitleDetailActivity.class);
        intent.putExtra("itemType", "movie");
        intent.putExtra("itemId", movie.getMovieId());
        startActivity(intent);
    }
    private void setupVideoOverlay() {
        try {
            videoOverlayContainer = findViewById(R.id.videoOverlayContainer);
            if (videoOverlayContainer == null) {
                Log.e("SeeAllActivity", "videoOverlayContainer not found in layout");
                return;
            }

            youtubePlayerView = findViewById(R.id.overlayYouTubePlayerView);
            if (youtubePlayerView == null) {
                Log.e("SeeAllActivity", "overlayYouTubePlayerView not found in layout");
                return;
            }

            getLifecycle().addObserver(youtubePlayerView);
            youtubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    activePlayer = youTubePlayer;
                    if (videoOverlayContainer.getVisibility() == View.VISIBLE &&
                            currentVideoId != null && !currentVideoId.isEmpty()) {
                        activePlayer.loadVideo(currentVideoId, 0);
                    }
                }

                @Override
                public void onError(@NonNull YouTubePlayer youTubePlayer, PlayerConstants.PlayerError error) {
                    Toast.makeText(SeeAllActivity.this, "Error playing video: " + error.toString(), Toast.LENGTH_SHORT).show();
                    hideVideoOverlay();
                }
            });

            ImageView closeButton = findViewById(R.id.closeOverlayButton);
            if (closeButton != null) {
                closeButton.setOnClickListener(v -> hideVideoOverlay());
            } else {
                Log.e("SeeAllActivity", "closeOverlayButton not found in layout");
            }
        } catch (Exception e) {
            Log.e("SeeAllActivity", "Error setting up video overlay: " + e.getMessage(), e);
            Toast.makeText(this, "Error setting up video player", Toast.LENGTH_SHORT).show();
        }
    }

    private void showVideoOverlay(String videoId) {
        try {
            if (TextUtils.isEmpty(videoId)) {
                Toast.makeText(this, "Video not available", Toast.LENGTH_SHORT).show();
                return;
            }
            if (videoOverlayContainer == null) {
                Log.e("SeeAllActivity", "videoOverlayContainer is null");
                return;
            }
            currentVideoId = videoId;
            videoOverlayContainer.setVisibility(View.VISIBLE);
            if (activePlayer != null) {
                activePlayer.loadVideo(videoId, 0);
            } else {
                Log.w("SeeAllActivity", "activePlayer is null, video will load when player is ready");
            }
        } catch (Exception e) {
            Log.e("SeeAllActivity", "Error showing video overlay: " + e.getMessage(), e);
            Toast.makeText(this, "Error playing video", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideVideoOverlay() {
        if (activePlayer != null) {
            activePlayer.pause();
        }
        videoOverlayContainer.setVisibility(View.GONE);
        currentVideoId = null;
    }

    private void setupPhotoOverlay() {
        photoOverlayContainer = findViewById(R.id.photoOverlayContainer);
        if (photoOverlayContainer == null) {
            Log.e("SeeAllActivity", "photoOverlayContainer not found in layout");
            return;
        }

        overlayPhotoView = findViewById(R.id.overlayPhotoView);
        if (overlayPhotoView == null) {
            Log.e("SeeAllActivity", "overlayPhotoView not found in layout");
            return;
        }

        ImageButton closeButton = findViewById(R.id.closePhotoOverlayButton);
        if (closeButton != null) {
            closeButton.setOnClickListener(v -> hidePhotoOverlay());
        }
    }

    private void showPhotoOverlay(String photoUrl) {
        if (TextUtils.isEmpty(photoUrl)) {
            Toast.makeText(this, "Photo not available", Toast.LENGTH_SHORT).show();
            return;
        }

        if (photoOverlayContainer == null || overlayPhotoView == null) {
            setupPhotoOverlay();
        }

        photoOverlayContainer.setVisibility(View.VISIBLE);
        Glide.with(this)
                .load(photoUrl)
                .into(overlayPhotoView);
    }

    private void hidePhotoOverlay() {
        if (photoOverlayContainer != null) {
            photoOverlayContainer.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (youtubePlayerView != null) {
            youtubePlayerView.release();
        }
    }
    @Override
    public void onItemClick(trendingall movi) {
        Log.d("onItemClick", "Clicked movie ID: " + movi.getId());
        Intent intent = new Intent(this, TitleDetailActivity.class);
        intent.putExtra("itemType", "movie");
        intent.putExtra("itemId", movi.getId());
        startActivity(intent);
    }
    @Override
    public void onItemClick(Person person){


        Intent intent = new Intent(this, PersonDetailActivity.class);

        intent.putExtra("personId", person.getPersonid());
        startActivity(intent);
    }
    @Override
    public void onItemClick(tvseries movi) {
        Log.d("onItemClick", "Clicked movie ID: " + movi.getId());
        Intent intent = new Intent(this, TitleDetailActivity.class);
        intent.putExtra("itemType", "tv");
        intent.putExtra("itemId", movi.getId());
        startActivity(intent);
    }
}
