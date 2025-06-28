package com.example.moviereviewapp.Activities;


import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.moviereviewapp.Adapters.BaseActivity;
import com.example.moviereviewapp.Adapters.SearchMovieAdapter;
import com.example.moviereviewapp.Models.SearchMovieModel;
import com.example.moviereviewapp.Models.UserAPI;
import com.example.moviereviewapp.Models.movies;
import com.example.moviereviewapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends BaseActivity {
    private Timer timer;
    private SearchMovieAdapter adapter;
    private final List<SearchMovieModel> searchResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setupBottomBar(this,"search");
        EditText inputSearch = findViewById(R.id.inputSearch);
        TextView Recent = findViewById(R.id.recent);
        Recent.setTextColor(Color.parseColor("#EFB33F"));
        TextView ComingSoon = findViewById(R.id.comingSoon);
        ComingSoon.setTextColor(Color.parseColor("#FFFFFF"));
        try {
            Recent();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        TextView search = findViewById(R.id.textView);
        search.setOnClickListener(v -> {
            String query = inputSearch.getText().toString();
            new Thread(() -> {
                try {
                    Recent.setTextColor(Color.parseColor("#FFFFFF"));
                    ComingSoon.setTextColor(Color.parseColor("#FFFFFF"));
                    handleSearch(query);
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });

        ListView listView = findViewById(R.id.searchResults);

        adapter = new SearchMovieAdapter(getApplicationContext(), R.layout.custom_listview_search_movie, searchResults);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            SearchMovieModel movie = searchResults.get(position);
            Intent intent = new Intent(SearchActivity.this, TitleDetailActivity.class);
            intent.putExtra("itemId", Integer.parseInt(movie.getId()));
            intent.putExtra("itemType", "movie");
            intent.putExtra("username", getIntent().getStringExtra("username"));
            intent.putExtra("token", getIntent().getStringExtra("token"));
            intent.putExtra("session_id", getIntent().getStringExtra("session_id"));
            startActivity(intent);
        });



        TextView recent = findViewById(R.id.recent);
        recent.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    runOnUiThread(() -> {
                        Recent.setTextColor(Color.parseColor("#EFB33F"));
                        ComingSoon.setTextColor(Color.parseColor("#FFFFFF"));
                    });
                    Recent();
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });

        TextView comingSoon = findViewById(R.id.comingSoon);
        comingSoon.setOnClickListener(v -> {
            new Thread(() -> {
                try {
                    runOnUiThread(() -> {
                        Recent.setTextColor(Color.parseColor("#FFFFFF"));
                        ComingSoon.setTextColor(Color.parseColor("#EFB33F"));
                    });
                    inputSearch.setText("");
                    ComingSoon();
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });
    }

    UserAPI userAPI = new UserAPI();
    private void Recent() throws IOException, JSONException {
        userAPI.call_api_auth_get(userAPI.get_UserAPI() + "/recent/list", token, new okhttp3.Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.code() == 200) {
                    assert response.body() != null;
                    String json_string = response.body().string();
                    List<SearchMovieModel> values = new ArrayList<>();
                    try {
                        JSONObject json_object = new JSONObject(json_string);
                        JSONArray json_array = json_object.getJSONArray("recent");
                        for (int i = json_array.length() - 1; i >= 0; --i) {
                            JSONObject jsonObject = json_array.getJSONObject(i);
                            String _id = jsonObject.getString("_id");
                            if (!jsonObject.getString("detail").equals("null")) {
                                JSONObject detail = jsonObject.getJSONObject("detail");
                                String url_img = detail.getString("img_url");
                                String title = detail.getString("name");
                                String release_date = detail.getString("release_day");
                                values.add(new SearchMovieModel(_id, url_img, title, release_date));
                            }
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    runOnUiThread(() -> {
                        adapter.clear();
                        adapter.addAll(values);
                        adapter.notifyDataSetChanged();
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }
        });
    }
    private void ComingSoon() throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();


        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/upcoming?language=en-US&page=1")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmZWQwZTRiNjNlMWVmNWVkNmE2NzhjZDI3OWEwMDg4NCIsIm5iZiI6MTc0MzA0MjA2MC45NTcsInN1YiI6IjY3ZTRiNjBjNDIxZWI4YzMzMWJhMmQ1NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.JsgAgcRx5Sib0D4SvfmnuwUEMWPV6hPv2winrt86vk0")
                .build();

        Response response = client.newCall(request).execute();
        Log.d("ComingSoon", request.toString());
        if (response.code() == 200) {
            Log.d("ComingSoon", "hello");
            assert response.body() != null;
            String jsonString = response.body().string();
            JSONObject json = new JSONObject(jsonString);
            List<SearchMovieModel> values = new ArrayList<>();
            new Thread(() -> {
                JSONArray results;
                try {
                    results = json.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject movie = results.getJSONObject(i);
                        if (movie.getString("poster_path").equals("null")) {
                            continue;
                        }
                        values.add(new SearchMovieModel(
                                movie.getString("id"),
                                movie.getString("poster_path"),
                                movie.getString("title"),
                                movie.getString("release_date")
                        ));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                runOnUiThread(() -> {
                    adapter.clear();
                    adapter.addAll(values);
                    adapter.notifyDataSetChanged();
                });
            }).start();
        }
    }

    private void handleSearch(String query) throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
                .build();

        Log.d("SearchActivity", "Query: " + query);

        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/search/movie?query=" + query + "&include_adult=true&language=en-US&page=1&api_key=fed0e4b63e1ef5ed6a678cd279a00884&append_to_response=credits")
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmZWQwZTRiNjNlMWVmNWVkNmE2NzhjZDI3OWEwMDg4NCIsIm5iZiI6MTc0MzA0MjA2MC45NTcsInN1YiI6IjY3ZTRiNjBjNDIxZWI4YzMzMWJhMmQ1NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.JsgAgcRx5Sib0D4SvfmnuwUEMWPV6hPv2winrt86vk0")
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            assert response.body() != null;
            String jsonString = response.body().string();
            JSONObject json = new JSONObject(jsonString);
            List<SearchMovieModel> values = new ArrayList<>();
            new Thread(() -> {
                JSONArray results;
                try {
                    results = json.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject movie = results.getJSONObject(i);
                        if (movie.getString("poster_path").equals("null")) {
                            continue;
                        }
                        values.add(new SearchMovieModel(
                                movie.getString("id"),
                                movie.getString("poster_path"),
                                movie.getString("title"),
                                movie.getString("release_date")
                        ));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                runOnUiThread(() -> {
                    adapter.clear();
                    adapter.addAll(values);
                    adapter.notifyDataSetChanged();
                });
            }).start();
        }
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

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            assert response.body() != null;
            String jsonString = response.body().string();
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                int id;
                String Title;
                String Url_img;
                String Release_date;
                if (type_name.equals("movie")) {
                    Title = jsonObject.getString("original_title");
                } else {
                    Title = jsonObject.getString("original_name");
                }
                Url_img = jsonObject.getString("poster_path");
                Release_date = jsonObject.optString("release_date", "");
                return new Movie(Title, Url_img, Release_date);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    private class Movie {
        String title;
        String url_img;
        String release_date;
        Movie(String title, String url_img, String release_date) {
            this.title = title;
            this.url_img = url_img;
            this.release_date = release_date;
        }
    }
}