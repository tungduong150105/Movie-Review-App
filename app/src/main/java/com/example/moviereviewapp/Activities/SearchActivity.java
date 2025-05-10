package com.example.moviereviewapp.Activities;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moviereviewapp.Adapters.SearchMovieAdapter;
import com.example.moviereviewapp.Models.SearchMovieModel;
import com.example.moviereviewapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {
    private Timer timer;
    private SearchMovieAdapter adapter;
    private final List<SearchMovieModel> searchResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText inputSearch = findViewById(R.id.inputSearch);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (timer != null) {
                    timer.cancel();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
                    if (s.isEmpty()) return;
                }
                timer = new Timer();
                timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            try {
                                handleSearch(s.toString());
                            } catch (JSONException | IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }, 1000);
            }
        });

        ListView listView = findViewById(R.id.searchResults);

        adapter = new SearchMovieAdapter(getApplicationContext(), R.layout.custom_listview_search_movie, searchResults);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            SearchMovieModel movie = searchResults.get(position);
            Intent intent = new Intent(SearchActivity.this, Rating.class);
            intent.putExtra("movie_id", movie.getId());
            startActivity(intent);
        });

        TextView recent = findViewById(R.id.recent);
        recent.setOnClickListener(v -> {
            new Thread(() -> {
                try {
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
                    inputSearch.setText("");
                    ComingSoon();
                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        });
    }

    private void Recent() throws IOException, JSONException {
    }
    private void ComingSoon() throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();

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
        OkHttpClient client = new OkHttpClient();

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

    public void backToMain(View view) {
        finish();
    }
}