package com.example.moviereviewapp.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.moviereviewapp.Models.TMDBAPI;
import com.example.moviereviewapp.Models.UserAPI;
import com.example.moviereviewapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Rating extends AppCompatActivity {
    TMDBAPI api;
    UserAPI userAPI;
    ImageView img;
    TextView title;
    RatingBar ratingBar;
    String itemType;
    int movie_id;
    String token;
    Boolean isRated = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        api = new TMDBAPI();
        userAPI = new UserAPI();
        title = findViewById(R.id.title);
        img = findViewById(R.id.img);
        TextView textBack = findViewById(R.id.textBack);
        textBack.setOnClickListener(v -> finish());
        ratingBar = findViewById(R.id.ratingBar);
        TextView ratingTxt = findViewById(R.id.ratingTxt);
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> ratingTxt.setText("Rating: " + rating));
        Button rateButton = findViewById(R.id.rateButton);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemType = extras.getString("itemType");
            movie_id = extras.getInt("itemId", -1);
            token = extras.getString("token");
            assert movie_id != -1;
            new Thread(() -> {
                try {
                    getData(String.valueOf(movie_id));
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        } else {
            movie_id = -1;
            itemType = "";
            token = "";
        }

        rateButton.setOnClickListener(v -> {
            int rating = (int) ratingBar.getRating();

            JSONObject json = new JSONObject();
            try {
                json.put("type_name", itemType);
                json.put("_id", movie_id);
                json.put("rating", rating);
                if (!isRated) {
                    userAPI.call_api_auth(userAPI.get_UserAPI() + "/rating/add", token, json.toString(), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            runOnUiThread(() -> Toast.makeText(Rating.this, "Failed to add rating, please try again", Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) {
                            runOnUiThread(() -> Toast.makeText(Rating.this, "Rating added", Toast.LENGTH_SHORT).show());
                        }
                    });
                } else {
                    userAPI.call_api_auth_upd(userAPI.get_UserAPI() + "/rating/update", token, json.toString(), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            runOnUiThread(() -> Toast.makeText(Rating.this, "Failed to update rating, please try again", Toast.LENGTH_SHORT).show());
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) {
                            if (response.code() == 200) {
                                runOnUiThread(() -> Toast.makeText(Rating.this, "Rating updated", Toast.LENGTH_SHORT).show());
                            }
                        }
                    });
                }
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void getData(String movie_id) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/" + (itemType.equals("movie") ? "movie" : "tv") + "/" + movie_id + "?language=en-US&api_key=fed0e4b63e1ef5ed6a678cd279a00884")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmZWQwZTRiNjNlMWVmNWVkNmE2NzhjZDI3OWEwMDg4NCIsIm5iZiI6MTc0MzA0MjA2MC45NTcsInN1YiI6IjY3ZTRiNjBjNDIxZWI4YzMzMWJhMmQ1NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.JsgAgcRx5Sib0D4SvfmnuwUEMWPV6hPv2winrt86vk0")
                .build();

        Response response = client.newCall(request).execute();

        JSONObject json = new JSONObject();
        json.put("type_name", itemType);
        json.put("_id", movie_id);
        userAPI.call_api_auth_get(userAPI.get_UserAPI() + "/rating/get?type_name=" + itemType + "&_id=" + movie_id, token, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.code() == 200) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        JSONObject ratingObject = jsonObject.getJSONObject("rating");
                        int rating = ratingObject.getInt("rating");
                        isRated = true;
                        runOnUiThread(() -> ratingBar.setRating(rating));
                    } catch (JSONException | IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        if (response.code() == 200) {
            assert response.body() != null;
            String jsonString = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonString);

            new Thread(() -> {
                try {
                    String Title;
                    String Url_img;
                    if (itemType.equals("movie")) {
                         Title = jsonObject.getString("original_title");
                    } else {
                         Title = jsonObject.getString("original_name");
                    }
                    Url_img = jsonObject.getString("poster_path");
                    runOnUiThread(() -> {
                        title.setText(Title);
                        Glide.with(Rating.this).load("https://image.tmdb.org/t/p/w500" + Url_img).into(img);
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
    }
}