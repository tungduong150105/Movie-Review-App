package com.example.moviereviewapp.Activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.moviereviewapp.Models.UserAPI;
import com.example.moviereviewapp.Models.movies;
import com.example.moviereviewapp.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CommentActivity extends AppCompatActivity {
    UserAPI userAPI = new UserAPI();
    String token;
    int _id;
    String type_nane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_comment);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            token = extra.getString("token");
            _id = extra.getInt("itemId");
            type_nane = extra.getString("itemType");
        }

        new Thread(() -> {
            try {
                Movie movieData = getData(type_nane, String.valueOf(_id));
                runOnUiThread(() -> {
                    ImageView imageView = findViewById(R.id.imageMovie);
                    TextView title = findViewById(R.id.movieName);
                    TextView releaseDate = findViewById(R.id.movieDate);

                    Glide.with(this).load("https://image.tmdb.org/t/p/w500" + movieData.Url_img).into(imageView);
                    title.setText(movieData.Title);
                    releaseDate.setText(movieData.release_date);
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).start();


        TextInputEditText comment = findViewById(R.id.editTextComment);

        ImageView back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> {
            comment.setText("");
            finish();
        });

        Button submit = findViewById(R.id.submitButton);
        submit.setOnClickListener(v -> {
            JSONObject body = new JSONObject();
            try {
                body.put("type_name", "movie");
                body.put("_id", String.valueOf(_id));
                body.put("body", comment.getText().toString());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            userAPI.call_api_auth(userAPI.get_UserAPI() + "/comment/add", token, body.toString(), new okhttp3.Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    runOnUiThread(() -> {
                        Toast.makeText(CommentActivity.this, "Comment added successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }
            });
        });
    }

    private Movie getData(String type_name, String _id) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/" + (type_name.equals("movie") ? "movie" : "tv") + "/" + _id + "?api_key=fed0e4b63e1ef5ed6a678cd279a00884")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmZWQwZTRiNjNlMWVmNWVkNmE2NzhjZDI3OWEwMDg4NCIsIm5iZiI6MTc0MzA0MjA2MC45NTcsInN1YiI6IjY3ZTRiNjBjNDIxZWI4YzMzMWJhMmQ1NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.JsgAgcRx5Sib0D4SvfmnuwUEMWPV6hPv2winrt86vk0")
                .build();

        AtomicReference<movies> movie = new AtomicReference<>(new movies());
        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            assert response.body() != null;
            String jsonString = response.body().string();
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                int id;
                String Title;
                String Url_img;
                String release_date;
                if (type_name.equals("movie")) {
                    Title = jsonObject.getString("original_title");
                } else {
                    Title = jsonObject.getString("original_name");
                }
                Url_img = jsonObject.getString("poster_path");
                release_date = jsonObject.getString("release_date");
                return new Movie(Title, Url_img, release_date);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

    private class Movie {
        String Title;
        String Url_img;
        String release_date;

        Movie(String Title, String Url_img, String release_date) {
            this.Title = Title;
            this.Url_img = Url_img;
            this.release_date = release_date;
        }
    }
}