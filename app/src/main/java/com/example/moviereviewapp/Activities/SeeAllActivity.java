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
import com.example.moviereviewapp.Adapters.PersonAdapter;
import com.example.moviereviewapp.Adapters.PersonSeeAllAdapter;
import com.example.moviereviewapp.Adapters.PhotoGridAdapter;
import com.example.moviereviewapp.Adapters.ReviewSeeAllAdapter;
import com.example.moviereviewapp.Adapters.SeeAllSimilarItemAdapter;
import com.example.moviereviewapp.Adapters.TVSeriesSeeAllAdapter;
import com.example.moviereviewapp.Adapters.TrendingSeeallAdapter;
import com.example.moviereviewapp.Adapters.VideoSeeAllAdapter;
import com.example.moviereviewapp.Models.PhotoItem;
import com.example.moviereviewapp.Models.SimilarItem;
import com.example.moviereviewapp.Models.UserAPI;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
    String title;
    String type;
    String username;
    String token;
    String session_id;

    List<Person> personList = new ArrayList<Person>();
    PersonSeeAllAdapter personAdapter;

    List<movies> movieList = new ArrayList<movies>();
    MovieSeeAllAdapter movieAdapter;
    MovieSeeAllRatingAdapter ratingAdapter;

    List<tvseries> seriesList = new ArrayList<tvseries>();
    TVSeriesSeeAllAdapter seriesAdapter;

    List<trendingall> trendingList = new ArrayList<trendingall>();
    TrendingSeeallAdapter trendingAdapter;


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
        title = getIntent().getStringExtra("title");
        type = getIntent().getStringExtra("type");
        session_id = getIntent().getStringExtra("session_id");
        username = getIntent().getStringExtra("username");
        token = getIntent().getStringExtra("token");

        Log.d("SeeAllActivity0", "Received session_id: " + session_id);
        Log.d("SeeAllActivity0", "Received username: " + username);
        Log.d("SeeAllActivity0", "Received token: " + token);

        textView_Title_SeeAll.setText(title);
        if (type == null) {
            Toast.makeText(this, "No type specified", Toast.LENGTH_SHORT).show();
            return;
        }
        switch (type) {
            case "movies":
                movieList = (List<movies>) getIntent().getSerializableExtra("movieList");
                if (movieList == null) {
                    movieList = new ArrayList<>();
                    Toast.makeText(this, "No movies found", Toast.LENGTH_SHORT).show();
                }

                if (title != null && title.equals("Recently Viewed")) {
                    movieAdapter = new MovieSeeAllAdapter(this, movieList, token);
                    movieAdapter.setOnItemClickListener(this);
                    recyclerView.setAdapter(movieAdapter);
                } else {
                    ratingAdapter = new MovieSeeAllRatingAdapter(this, movieList, token);
                    ratingAdapter.setOnItemClickListener(this);
                    recyclerView.setAdapter(ratingAdapter);
                }
                break;

            case "tvseries":
                seriesList = (List<tvseries>) getIntent().getSerializableExtra("tvseriesList");
                if (seriesList == null) {
                    seriesList = new ArrayList<>();
                    Toast.makeText(this, "No TV series found", Toast.LENGTH_SHORT).show();
                }

                seriesAdapter = new TVSeriesSeeAllAdapter(this, seriesList, token);
                seriesAdapter.setOnItemClickListener(this);
                recyclerView.setAdapter(seriesAdapter);
                break;


            case "trending":
                trendingList = (List<trendingall>) getIntent().getSerializableExtra("trendingList");
                if (trendingList == null) {
                    trendingList = new ArrayList<>();
                    Toast.makeText(this, "No movies found", Toast.LENGTH_SHORT).show();
                }

                trendingAdapter = new TrendingSeeallAdapter(this, trendingList, token);
                trendingAdapter.setOnItemClickListener(this);
                recyclerView.setAdapter(trendingAdapter);
                break;

            case "person":
                personList = (ArrayList<Person>) getIntent().getSerializableExtra("personList");
                if (personList == null) {
                    personList = new ArrayList<>();
                    Toast.makeText(this, "No persons found", Toast.LENGTH_SHORT).show();
                }

                personAdapter = new PersonSeeAllAdapter(this, personList, token);
                personAdapter.setOnItemClickListener(this);
                recyclerView.setAdapter(personAdapter);
                break;


            case "similaritem":
                ArrayList<SimilarItem> similarList = (ArrayList<SimilarItem>) getIntent().getSerializableExtra("movieList");
                if (similarList == null) {
                    similarList = new ArrayList<>();
                    Toast.makeText(this, "No persons found", Toast.LENGTH_SHORT).show();
                }

                SeeAllSimilarItemAdapter SeeAllSimilarItemAdapter = new SeeAllSimilarItemAdapter(this, similarList, token);

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
        intent.putExtra("username", username);
        intent.putExtra("token", token);
        intent.putExtra("session_id", session_id);
        intent.putExtra("itemType", "movie");
        intent.putExtra("itemId", movie.getMovieId());
        Log.d("MovieClick", "User '" + username + "' clicked on movie: " + movie.getMoviename() +
                " (ID: " + movie.getMovieId() + "), session_id: " + session_id);

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

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (youtubePlayerView != null) {
//            youtubePlayerView.release();
//        }
//    }
    @Override
    public void onItemClick(trendingall movi) {
        Log.d("onItemClick", "Clicked movie ID: " + movi.getId());
        Intent intent = new Intent(this, TitleDetailActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("token", token);
        intent.putExtra("session_id", session_id);
        intent.putExtra("itemType", "movie");
        intent.putExtra("itemId", movi.getId());
        startActivity(intent);
    }
    @Override
    public void onItemClick(Person person){
        Intent intent = new Intent(this, PersonDetailActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("token", token);
        intent.putExtra("session_id", session_id);
        intent.putExtra("personId", person.getPersonid());
        startActivity(intent);
    }
    @Override
    public void onItemClick(tvseries movi) {
        Log.d("onItemClick", "Clicked movie ID: " + movi.getId());
        Intent intent = new Intent(this, TitleDetailActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("token", token);
        intent.putExtra("session_id", session_id);
        intent.putExtra("itemType", "tv");
        intent.putExtra("itemId", movi.getId());
        startActivity(intent);
    }
    UserAPI userAPI = new UserAPI();
    @Override
    protected void onRestart() {
        super.onRestart();
        if (!type.equals("person")) {
            List<Integer> movie_in_watchlist = new ArrayList<>();
            List<Integer> tv_in_watchlist = new ArrayList<>();
            userAPI.call_api_auth_get(userAPI.get_UserAPI() + "/watchlist/list", token, new okhttp3.Callback() {
                @Override
                public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        String json_string = response.body().string();
                        try {
                            JSONObject json_object = new JSONObject(json_string);
                            JSONArray json_array = json_object.getJSONArray("watchlist");
                            for (int i = 0; i < json_array.length(); ++i) {
                                JSONObject item = json_array.getJSONObject(i);
                                String _id = item.getString("_id");
                                String type_name = item.getString("type_name");
                                if (type_name.equals("movie")) {
                                    movie_in_watchlist.add(Integer.parseInt(_id));
                                } else {
                                    tv_in_watchlist.add(Integer.parseInt(_id));
                                }
                            }

                            if (type.equals("movies")) {
                                for (movies m : movieList) {
                                    if (movie_in_watchlist.contains(m.getMovieId())) {
                                        m.setInWatchlist(true);
                                    } else {
                                        m.setInWatchlist(false);
                                    }
                                }
                            }

                            if (type.equals("tvseries")) {
                                for (tvseries m : seriesList) {
                                    if (tv_in_watchlist.contains(m.getId())) {
                                        m.setinwatchlist(true);
                                    } else {
                                        m.setinwatchlist(false);
                                    }
                                }
                            }

                            if (type.equals("trending")) {
                                for (trendingall m : trendingList) {
                                    if (m.getType().equals("movie") && movie_in_watchlist.contains(m.getId())) {
                                        m.setInWatchlist(true);
                                    } else if (m.getType().equals("tv") && tv_in_watchlist.contains(m.getId())) {
                                        m.setInWatchlist(true);
                                    } else {
                                        m.setInWatchlist(false);
                                    }
                                }
                            }

                            runOnUiThread(() -> {
                                if (type.equals("movies")) {
                                    if (title != null && title.equals("Recently Viewed")) {
                                        movieAdapter.notifyDataSetChanged();
                                    } else {
                                        ratingAdapter.notifyDataSetChanged();
                                    }
                                }
                                if (type.equals("tvseries")) {
                                    seriesAdapter.notifyDataSetChanged();
                                }
                                if (type.equals("trending")) {
                                    trendingAdapter.notifyDataSetChanged();
                                }
                            });
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        } else {
            List<String> person_in_like = new ArrayList<>();
            userAPI.call_api_auth_get(userAPI.get_UserAPI() + "/person/get", token, new okhttp3.Callback() {
                @Override
                public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                    if (response.code() == 200) {
                        assert response.body() != null;
                        String json_string = response.body().string();
                        try {
                            JSONObject json_object = new JSONObject(json_string);
                            JSONArray json_array = json_object.getJSONArray("person");
                            for (int i = 0; i < json_array.length(); ++i) {
                                person_in_like.add(json_array.getJSONObject(i).getString("person_id"));
                            }

                            for (Person p : personList) {
                                if (person_in_like.contains(p.getPersonid())) {
                                    p.setIsFavorite(true);
                                } else {
                                    p.setIsFavorite(false);
                                }
                            }

                            runOnUiThread(() -> {
                                personAdapter.notifyDataSetChanged();
                            });
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }
    }
}
