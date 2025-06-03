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
import com.example.moviereviewapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Rating extends AppCompatActivity {
    TMDBAPI api;
    ImageView img;
    TextView title;
    RatingBar ratingBar;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        api = new TMDBAPI();
        title = findViewById(R.id.title);
        img = findViewById(R.id.img);
        TextView textBack = findViewById(R.id.textBack);
        textBack.setOnClickListener(v -> finish());
        ratingBar = findViewById(R.id.ratingBar);
        TextView ratingTxt = findViewById(R.id.ratingTxt);
        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> ratingTxt.setText("Rating: " + rating));
        Button rateButton = findViewById(R.id.rateButton);

        String itemType;
        String movie_id;
        String session_id;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemType = extras.getString("itemType");
            movie_id = extras.getString("movie_id");
            session_id = extras.getString("session_id");
            assert movie_id != null;
            Log.d("Rating", movie_id);
            new Thread(() -> {
                try {
                    getData(movie_id);
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        } else {
            itemType = "";
            movie_id = "";
            session_id = "";
        }

        rateButton.setOnClickListener(v -> {
            int rating = (int) ratingBar.getRating();
            JSONObject body = new JSONObject();
            try {
                body.put("value", rating);
                Log.d("Rating", body.toString());
                api.post_api(api.get_url_add_rating(itemType, movie_id, session_id), body.toString(), new Callback() {
                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                       runOnUiThread(() -> Toast.makeText(Rating.this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) {
                        Log.d("Rating", response.toString());
                        if (response.code() == 201) {
                            runOnUiThread(() -> Toast.makeText(Rating.this, "Rating successful", Toast.LENGTH_SHORT).show());
                        }
                    }
                });
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }
    private void getData(String movie_id) throws IOException, JSONException {
        Log.d("Rating", "getData");
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/" + movie_id + "?language=en-US&api_key=fed0e4b63e1ef5ed6a678cd279a00884")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmZWQwZTRiNjNlMWVmNWVkNmE2NzhjZDI3OWEwMDg4NCIsIm5iZiI6MTc0MzA0MjA2MC45NTcsInN1YiI6IjY3ZTRiNjBjNDIxZWI4YzMzMWJhMmQ1NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.JsgAgcRx5Sib0D4SvfmnuwUEMWPV6hPv2winrt86vk0")
                .build();

        Response response = client.newCall(request).execute();

        Log.d("Rating", response.toString());

        if (response.code() == 200) {
            assert response.body() != null;
            String jsonString = response.body().string();
            JSONObject jsonObject = new JSONObject(jsonString);

            new Thread(() -> {
                try {
                    String Title = jsonObject.getString("original_title");
                    String Url_img = jsonObject.getString("poster_path");
                    Log.d("Rating", "Title: " + Title);
                    Log.d("Rating", "Url_img: " + Url_img);
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