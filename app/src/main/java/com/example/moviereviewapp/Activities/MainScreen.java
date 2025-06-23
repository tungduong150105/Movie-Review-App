package com.example.moviereviewapp.Activities;

import static java.lang.Integer.parseInt;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.example.moviereviewapp.Adapters.BaseActivity;
import com.example.moviereviewapp.Adapters.MovieAdapter;
import com.example.moviereviewapp.Adapters.MovieItemAdapter;
import com.example.moviereviewapp.Adapters.PersonAdapter;
import com.example.moviereviewapp.Adapters.Top_Box_Office_Adapter;
import com.example.moviereviewapp.Adapters.movietrendingadapter;
import com.example.moviereviewapp.Adapters.tvseriesadapter;
import com.example.moviereviewapp.Models.Person;
import com.example.moviereviewapp.Models.UserAPI;
import com.example.moviereviewapp.Models.movies;
import com.example.moviereviewapp.Models.trendingall;
import com.example.moviereviewapp.Models.tvseries;
import com.example.moviereviewapp.R;
import com.example.moviereviewapp.databinding.ActivityMainScreenBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScreen extends BaseActivity implements MovieAdapter.OnItemClickListener, MovieAdapter.OnRefreshListener, MovieItemAdapter.OnRefreshListener, movietrendingadapter.OnItemClickListener, movietrendingadapter.OnRefreshListener, MovieItemAdapter.OnItemClickListener, PersonAdapter.OnPersonClickListener, PersonAdapter.OnRefreshListener, tvseriesadapter.OnRefreshListener, tvseriesadapter.OnItemClickListener {
    private ActivityMainScreenBinding binding;
    private MovieAdapter adapter;
    private MovieItemAdapter itemadapter, topratedadapter;
    private movietrendingadapter trendingadapter;
    private Top_Box_Office_Adapter topBoxOfficeAdapter;
    private PersonAdapter personadapter;
    private tvseriesadapter tvserieontheairsadapter, topratedtvseriesAdapter;
    private ArrayList<Person> person = new ArrayList<>();
    private ArrayList<trendingall> trendinglist = new ArrayList<>();
    private ArrayList<movies> moviesList = new ArrayList<>();
    private ArrayList<movies> upcominglist = new ArrayList<>();
    private ArrayList<movies> topratedmovieslist = new ArrayList<>();
    private ArrayList<movies> topboxofficelist = new ArrayList<>();
    private ArrayList<movies> nowplaying = new ArrayList<>();
    private ArrayList<tvseries> ontheair = new ArrayList<>();
    private ArrayList<tvseries> toprated = new ArrayList<>();
    private Call<tvseriesresponse> ontheairCall, topratedCall;
    private Call<MovieResponse> moviesCall, Topoffficecall;
    private Call<trendingresponse> trendingCall;
    private Call<PersonResoponse> personCall;
    private int lastScrollY = 0;
    private final int MAX_PAGE = 50;
    private int currentPage = 1;
    private static final String TMDB_API_KEY = "75d6190f47f7d58c6d0511ca393d2f7d";
    String username;
    String token;
    String session_id;
    UserAPI userAPI = new UserAPI();
    List<String> person_in_like = new ArrayList<>();
    List<Integer> movie_in_watchlist = new ArrayList<>();
    List<Integer> tv_in_watchlist = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            username = extra.getString("username");
            token = extra.getString("token");
            session_id = extra.getString("session_id");
            Log.d("MainScreen", username + " " + token + " " + session_id);
            assert session_id != null;
            assert token != null;
            assert username != null;
        }

        setupBottomBar(this, "home");
        setupRecyclerView();
        comingrecyclerview();
        topratedrecyclerview();
        trendingrecyclerview();
        personrecyclerview();
//        topboxofficeadapter();
        tvseriesrecyclerview();
        topratedTvseriesRecyclerview();
        initSeeAll();

        EditText search = findViewById(R.id.searchTxt);
        search.setOnClickListener(v -> {
            Intent intent = new Intent(MainScreen.this, SearchActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("token", token);
            intent.putExtra("session_id", session_id);
            startActivity(intent);
        });

        movie_in_watchlist.clear();
        tv_in_watchlist.clear();
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
                        fetchMovies();
                        fetchupcmingmovies();
                        fetchtopratedmovies();
                        fetchtrendingmovies();
                        fetchontheair();
                        fetchtopratedtvseries();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

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
                        fetchAllPersons();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void initPersonFavorite() {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        movie_in_watchlist.clear();
        tv_in_watchlist.clear();
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
                        fetchMovies();
                        fetchupcmingmovies();
                        fetchtopratedmovies();
                        fetchtrendingmovies();
                        fetchontheair();
                        fetchtopratedtvseries();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }
        });

        person_in_like.clear();
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
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }

                for (Person v : person) {
                    if (person_in_like.contains(v.getPersonid())) {
                        v.setIsFavorite(true);
                    } else {
                        v.setIsFavorite(false);
                    }
                }

                runOnUiThread(() -> {
                    personadapter.notifyDataSetChanged();
                });
            }
        });
    }

    private void initSeeAll() {
        binding.seealltvseriestoprated.setOnClickListener(V -> {
            Intent intent = new Intent(MainScreen.this, SeeAllActivity.class);
            intent.putExtra("type", "tvseries");
            intent.putExtra("title", "Top rated Tv Series");
            intent.putExtra("tvseriesList", toprated);
            intent.putExtra("token", token);
            intent.putExtra("username", username);
            if (toprated != null) {
                startActivity(intent);
            }
        });

        binding.seeallcomingsoon.setOnClickListener(V -> {
            Intent intent = new Intent(MainScreen.this, SeeAllActivity.class);
            intent.putExtra("type", "movies");
            intent.putExtra("title", "Coming soon");
            intent.putExtra("movieList", upcominglist);
            intent.putExtra("token", token);
            intent.putExtra("username", username);
            if (upcominglist != null) {
                startActivity(intent);
            }
        });

        binding.seealltoprated.setOnClickListener(V -> {
            Intent intent = new Intent(MainScreen.this, SeeAllActivity.class);
            intent.putExtra("type", "movies");
            intent.putExtra("title", "Top rated movies just for you!");
            intent.putExtra("movieList", topratedmovieslist);
            intent.putExtra("token", token);
            intent.putExtra("username", username);
            if (topratedmovieslist != null) {
                startActivity(intent);
            }
        });

        binding.seeallontheair.setOnClickListener(V -> {
            Intent intent = new Intent(MainScreen.this, SeeAllActivity.class);
            intent.putExtra("type", "tvseries");
            intent.putExtra("title", "Airing today");
            intent.putExtra("tvseriesList", ontheair);
            intent.putExtra("token", token);
            if (ontheair != null) {
                startActivity(intent);
            }
        });

        binding.seeallperson.setOnClickListener(V -> {
            Intent intent = new Intent(MainScreen.this, SeeAllActivity.class);
            intent.putExtra("type", "person");
            intent.putExtra("title", "Born today");
            intent.putExtra("personList", person);
            intent.putExtra("token", token);
            if (person != null) {
                startActivity(intent);
            }
        });

        binding.seaalltrending.setOnClickListener(V -> {
            Intent intent = new Intent(MainScreen.this, SeeAllActivity.class);
            intent.putExtra("type", "trending");
            intent.putExtra("title", "Fan Favorites");
            intent.putExtra("trendingList", trendinglist);
            intent.putExtra("token", token);
            if (trendinglist != null) {
                startActivity(intent);
            }
        });
    }

    private void topratedTvseriesRecyclerview() {
        topratedtvseriesAdapter = new tvseriesadapter(toprated, token, this);
        topratedtvseriesAdapter.setOnItemClickListener(this);
        binding.topratedrecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.topratedrecyclerview.setAdapter(topratedtvseriesAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        binding.topratedrecyclerview.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
    }

    private void fetchtopratedtvseries() {
        TMDBApi api = RetrofitClient.getApiService();
        topratedCall = api.getTvSeriesTopRated("75d6190f47f7d58c6d0511ca393d2f7d", 1);
        topratedCall.enqueue(new Callback<tvseriesresponse>() {
            @Override
            public void onResponse(@NonNull Call<tvseriesresponse> call, @NonNull Response<tvseriesresponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    toprated.clear();
                    List<tvseries> results = response.body().getResults();

                    Log.d("TVSERIES", "Fetched " + results.size() + " series.");

                    for (tvseries movie : results) {
                        int movieId = movie.getId();
//                        Call<tvseriesdetail> detailCall = api.getTvSeriesDetail(movieId, TMDB_API_KEY);
//                        detailCall.enqueue(new Callback<tvseriesdetail>() {
//                            @Override
//                            public void onResponse(Call<tvseriesdetail> call, Response<tvseriesdetail> response) {
//                                if (response.isSuccessful() && response.body() != null) {
//                                    int epsnumber = response.body().getnumberofepsidose();
//                                    movie.setepsnumber(epsnumber);
//                                    toprated.add(movie);
                                    if (tv_in_watchlist.contains(movie.getId())) {
                                        movie.setinwatchlist(true);
                                    }
//                                    Log.d("OntheAir", "Fetching number of epsidoses: " + movie.getName() + " (ID: " + movie.getId() + ")");
//                                    topratedtvseriesAdapter.notifyDataSetChanged();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<tvseriesdetail> call, Throwable t) {
//                                Log.e("DETAIL_ERROR", "Lỗi khi lấy runtime cho movieId: " + movieId, t);
//                            }
//                        });
                    }
                    toprated.addAll(results);
                    topratedtvseriesAdapter.notifyDataSetChanged();
                } else {
                    showError("Failed to load movies: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<tvseriesresponse> call, Throwable t) {
            }
        });
    }

    private void tvseriesrecyclerview() {
        tvserieontheairsadapter = new tvseriesadapter(ontheair, token, this);
        tvserieontheairsadapter.setOnItemClickListener(this);
        binding.ontheairrecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.ontheairrecyclerview.setAdapter(tvserieontheairsadapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        binding.ontheairrecyclerview.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
    }

    private void fetchontheair() {
        TMDBApi api = RetrofitClient.getApiService();
        ontheairCall = api.getTvSeriesontheair("75d6190f47f7d58c6d0511ca393d2f7d", 1);
        ontheairCall.enqueue(new Callback<tvseriesresponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(@NonNull Call<tvseriesresponse> call, @NonNull Response<tvseriesresponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ontheair.clear();
                    List<tvseries> results = response.body().getResults();

                    Log.d("TVSERIES", "Fetched " + results.size() + " series.");

                    for (tvseries movie : results) {
                        int movieId = movie.getId();
//                        Call<tvseriesdetail> detailCall = api.getTvSeriesDetail(movieId, TMDB_API_KEY);
//                        detailCall.enqueue(new Callback<tvseriesdetail>() {
//                            @Override
//                            public void onResponse(Call<tvseriesdetail> call, Response<tvseriesdetail> response) {
//                                if (response.isSuccessful() && response.body() != null) {
//                                    int epsnumber = response.body().getnumberofepsidose();
//                                    movie.setepsnumber(epsnumber);
                                    if (tv_in_watchlist.contains(movie.getId())) {
                                        movie.setinwatchlist(true);
                                        Log.d("ontheair", movie.getName() + "gg");
                                    }
//                                    Log.d("OntheAir", "Fetching number of epsidoses: " + movie.getName() + " (ID: " + movie.getId() + ")");
//                                    tvserieontheairsadapter.notifyDataSetChanged();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<tvseriesdetail> call, Throwable t) {
//                                Log.e("DETAIL_ERROR", "Lỗi khi lấy runtime cho movieId: " + movieId, t);
//                            }
//                        });
                    }

                    ontheair.addAll(results);
                    tvserieontheairsadapter.notifyDataSetChanged();
                } else {
                    showError("Failed to load movies: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<tvseriesresponse> call, Throwable t) {
            }
        });


    }

    private void fetchtopboxoffice() {
        TMDBApi api = RetrofitClient.getApiService();
        topboxofficelist.clear();

        for (int i = 1; i <= 9; i++) { // test 2 pages trước cho nhẹ
            Log.d("TopBoxOffice", "Fetching page: " + i);
            moviesCall = api.getNowPlayingMovies("75d6190f47f7d58c6d0511ca393d2f7d", i);
            moviesCall.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        List<movies> result = response.body().getResults();

                        Log.d("TopBoxOffice", "Fetched " + result.size() + " movies from page.");

                        for (movies movie : result) {
                            Log.d("TopBoxOffice", "Fetching revenue for movie: " + movie.getMoviename() + " (ID: " + movie.getMovieId() + ")");
                            fetchMovieRevenue(api, movie);
                            topboxofficelist.add(movie);
                        }

                        topBoxOfficeAdapter.notifyDataSetChanged();
                    } else {
                        Log.e("TopBoxOffice", "Failed to load movies: " + response.code() + " - " + response.message());
                        showError("Failed to load movies: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    Log.e("TopBoxOffice", "Error loading movies: " + t.getMessage(), t);
                    showError("Failed to load movies: " + t.getMessage());
                }
            });
        }
    }

    private void fetchMovieRevenue(TMDBApi api, movies movie) {
        Call<MovieDetail> detailCall = api.getMovieDetail(movie.getMovieId(), "75d6190f47f7d58c6d0511ca393d2f7d");
        detailCall.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    long revenue = response.body().getRevenue();
                    movie.setRevenue(revenue);

                    Log.d("TopBoxOffice", "Revenue fetched for " + movie.getMoviename() + ": " + revenue);

                    Collections.sort(topboxofficelist, (m1, m2) -> Long.compare(m2.getRevenue(), m1.getRevenue()));
                    topBoxOfficeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                Log.e("TopBoxOffice", "Error fetching revenue for movie ID: " + movie.getMovieId() + " - " + t.getMessage(), t);
            }
        });
    }


//    private void topboxofficeadapter() {
//        topBoxOfficeAdapter = new Top_Box_Office_Adapter(this, topboxofficelist, token);
//        binding.topboxofficelistview.setAdapter(topBoxOfficeAdapter);
//    }

    private void fetchAllPersons() {
        currentPage = 1;
        fetchPersonPage(currentPage);
    }

    private void fetchPersonPage(int page) {
        TMDBApi api = RetrofitClient.getApiService();
        personCall = api.getPerson(TMDB_API_KEY, page);
        personCall.enqueue(new Callback<PersonResoponse>() {
            @Override
            public void onResponse(Call<PersonResoponse> call, Response<PersonResoponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Person> results = response.body().getPersons();

                    for (Person p : results) {
                        if (person_in_like.contains(p.getPersonid())) {
                            p.setIsFavorite(true);
                        }
                        fetchPersonDetail(p); // Lấy thêm birthdate, deathday
                    }

                    personadapter.notifyDataSetChanged();
                } else {
                    showError("Lỗi tải trang " + page + ": " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PersonResoponse> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi trang " + page + ": " + t.getMessage(), t);
                showError("Lỗi: " + t.getMessage());
            }
        });
    }

    private void personrecyclerview() {
        personadapter = new PersonAdapter(person, this, token, this);
        binding.personrecycleview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.personrecycleview.setAdapter(personadapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        binding.personrecycleview.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
    }

    public boolean isEnglishTitle(String name) {
        // Regex kiểm tra tên chỉ gồm chữ cái tiếng Anh, số, khoảng trắng và vài ký tự đặc biệt
        return name.matches("[a-zA-Z0-9 .,!?':;\\-()]+");
    }

    private void fetchPersonDetail(Person basicPerson) {
        TMDBApi api = RetrofitClient.getApiService();
        Call<PersonDetail> detail = api.getPersonDetail(basicPerson.getPersonid(), TMDB_API_KEY);

        detail.enqueue(new Callback<PersonDetail>() {
            @Override
            public void onResponse(Call<PersonDetail> call, Response<PersonDetail> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PersonDetail detail = response.body();
                    basicPerson.setBirthdate(detail.getBirthday());
                    basicPerson.setDeathday(detail.getDeathday());
                    if (basicPerson.getAgeOrLifespan() != "" && isEnglishTitle(basicPerson.getName()) == true) {
                        runOnUiThread(() -> {
                            person.add(basicPerson);
                            personadapter.notifyDataSetChanged();
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<PersonDetail> call, Throwable t) {
                Log.e("API_DETAIL_ERROR", "Lỗi lấy chi tiết: " + t.getMessage(), t);
            }
        });
    }


    private void fetchtrendingmovies() {
        TMDBApi api = RetrofitClient.getApiService();
        trendingCall = api.getTrendingAll(TMDB_API_KEY);
        trendingCall.enqueue(new Callback<trendingresponse>() {
            @Override
            public void onResponse(Call<trendingresponse> call, Response<trendingresponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    trendinglist.clear();
                    List<trendingall> results = response.body().getTrending();

                    Log.d("MOVIES", "Fetched " + results.size() + " movies.");

                    trendinglist.addAll(results);
                    trendingadapter.notifyDataSetChanged();
                    // Gọi chi tiết từng phim để lấy runtime
                    for (trendingall movie : trendinglist) {
                        int movieId = movie.getId();
                        if (movie.getType().equals("movie")) {
                            if (movie_in_watchlist.contains(movieId)) {
                                movie.setInWatchlist(true);
                            }
//                            Call<MovieDetail> detailCall = api.getMovieDetail(movieId, TMDB_API_KEY);
//                            detailCall.enqueue(new Callback<MovieDetail>() {
//                                @Override
//                                public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
//                                    if (response.isSuccessful() && response.body() != null) {
//                                        int runtime = response.body().getRuntime();

//                                        movie.setLengthFromRuntime(runtime);
//                                        trendingadapter.notifyDataSetChanged();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<MovieDetail> call, Throwable t) {
//                                    Log.e("DETAIL_ERROR", "Lỗi khi lấy runtime cho movieId: " + movieId, t);
//                                }
//                            });
                        } else if (movie.getType().equals("tv")) {
                            if (tv_in_watchlist.contains(movieId)) {
                                movie.setInWatchlist(true);
                            }
//                            Call<tvseriesdetail> detailCall = api.getTvSeriesDetail(movieId, TMDB_API_KEY);
//                            detailCall.enqueue(new Callback<tvseriesdetail>() {
//                                @Override
//                                public void onResponse(Call<tvseriesdetail> call, Response<tvseriesdetail> response) {
//                                    if (response.isSuccessful() && response.body() != null) {
//                                        int epsnumber = response.body().getnumberofepsidose();
//                                        movie.setLength(epsnumber + " eps");
//                                        trendingadapter.notifyDataSetChanged();
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Call<tvseriesdetail> call, Throwable t) {
//                                    Log.e("DETAIL_ERROR", "Lỗi khi lấy runtime cho movieId: " + movieId, t);
//                                }
//                            });
                        }
                    }

                    trendingadapter.notifyDataSetChanged();
                } else {
                    showError("Failed to load movies: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<trendingresponse> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi gọi API: " + t.getMessage(), t);
                showError("Lỗi: " + t.getMessage());
            }
        });
    }

    private void trendingrecyclerview() {
        trendingadapter = new movietrendingadapter(trendinglist, token, this);
        binding.trendingrecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        trendingadapter.setOnItemClickListener(this);
        binding.trendingrecyclerview.setAdapter(trendingadapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        binding.trendingrecyclerview.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
    }

    private void fetchtopratedmovies() {
        TMDBApi api = RetrofitClient.getApiService();
        moviesCall = api.getTopRatedMovies(TMDB_API_KEY);
        moviesCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    topratedmovieslist.clear();
                    List<movies> results = response.body().getResults();

                    Log.d("MOVIES", "Fetched " + results.size() + " movies.");

                    topratedmovieslist.addAll(results);
                    topratedadapter.notifyDataSetChanged();

                    // Gọi chi tiết từng phim để lấy runtime
                    for (movies movie : results) {
                        int movieId = movie.getMovieId();
                        if (movie_in_watchlist.contains(movieId)) {
                            movie.setInWatchlist(true);
                        }
//                        Call<MovieDetail> detailCall = api.getMovieDetail(movieId, TMDB_API_KEY);
//                        detailCall.enqueue(new Callback<MovieDetail>() {
//                            @Override
//                            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
//                                if (response.isSuccessful() && response.body() != null) {
//                                    int runtime = response.body().getRuntime();
//                                    movie.setLengthFromRuntime(runtime);

//                                    topratedadapter.notifyDataSetChanged();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<MovieDetail> call, Throwable t) {
//                                Log.e("DETAIL_ERROR", "Lỗi khi lấy runtime cho movieId: " + movieId, t);
//                            }
//                        });
                    }
                    topratedadapter.notifyDataSetChanged();
//
//                } else {
//                    showError("Failed to load movies: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi gọi API: " + t.getMessage(), t);
                showError("Lỗi: " + t.getMessage());
            }
        });
    }

    private void topratedrecyclerview() {
        topratedadapter = new MovieItemAdapter(topratedmovieslist, token, this);
        binding.topickrecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        topratedadapter.setOnItemClickListener(this);
        binding.topickrecyclerview.setAdapter(topratedadapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        binding.topickrecyclerview.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
    }

    private void setupRecyclerView() {
        try {
            adapter = new MovieAdapter(moviesList, token, this, this);
            binding.toplistmovie.setAdapter(adapter);
            binding.toplistmovie.setOffscreenPageLimit(1);
        } catch (Exception e) {
            Log.e("RecyclerViewError", "Setup failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void comingrecyclerview() {
        itemadapter = new MovieItemAdapter(upcominglist, token, this);
        binding.comingsoonrecyclerview.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        itemadapter.setOnItemClickListener(this);
        binding.comingsoonrecyclerview.setAdapter(itemadapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        binding.comingsoonrecyclerview.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
    }

    private void fetchMovies() {
        TMDBApi api = RetrofitClient.getApiService();
        moviesCall = api.getTrendingMovies(TMDB_API_KEY);

        moviesCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    moviesList.clear();
                    List<movies> results = response.body().getResults();
                    for (movies movie : results) {
                        int movieId = movie.getMovieId();
                        if (movie_in_watchlist.contains(movieId)) {
                            movie.setInWatchlist(true);
                        }
                    }
                    Log.d("MOVIES", "Fetched " + results.size() + " movies.");
                    moviesList.addAll(results);
                    adapter.notifyDataSetChanged();
                } else {
                    showError("Failed to load movies: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi gọi API: " + t.getMessage(), t);
                showError("Lỗi: " + t.getMessage());
            }
        });
    }

    private void fetchupcmingmovies() {
        TMDBApi api = RetrofitClient.getApiService();
        moviesCall = api.getUpcomingMovies(TMDB_API_KEY);
        moviesCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    upcominglist.clear();
                    List<movies> results = response.body().getResults();

                    Log.d("MOVIES", "Fetched " + results.size() + " movies.");

                    upcominglist.addAll(results);
                    itemadapter.notifyDataSetChanged();

                    // Gọi chi tiết từng phim để lấy runtime
                    for (movies movie : results) {
                        int movieId = movie.getMovieId();
                        if (movie_in_watchlist.contains(movieId)) {
                            movie.setInWatchlist(true);
                        }
//                        Call<MovieDetail> detailCall = api.getMovieDetail(movieId, TMDB_API_KEY);
//                        detailCall.enqueue(new Callback<MovieDetail>() {
//                            @Override
//                            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
//                                if (response.isSuccessful() && response.body() != null) {
//                                    int runtime = response.body().getRuntime();
//                                    movie.setLengthFromRuntime(runtime);

//                                    itemadapter.notifyDataSetChanged();
//                                }
//                            }
//
//                            @Override
//                            public void onFailure(Call<MovieDetail> call, Throwable t) {
//                                Log.e("DETAIL_ERROR", "Lỗi khi lấy runtime cho movieId: " + movieId, t);
//                            }
//                        });
                    }

//                } else {
//                    showError("Failed to load movies: " + response.message());
                }

                itemadapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi gọi API: " + t.getMessage(), t);
                showError("Lỗi: " + t.getMessage());
            }
        });
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    protected void onDestroy() {
//        if (moviesCall != null) {
//            moviesCall.cancel();
//        }
//        super.onDestroy();
//    }


// Inside your MainScreen class

    @Override
    public void onItemClick(movies movie) {
        Log.d("MainScreen", "Clicked on movie: " + movie.getMoviename());
        Intent intent = new Intent(this, TitleDetailActivity.class);
        intent.putExtra("itemType", "movie");
        intent.putExtra("itemId", movie.getMovieId());
        addUserDataToIntent(intent);
        startActivity(intent);
    }

    @Override
    public void onItemClick(trendingall movi) {
        if (movi.getType().equals("movie")) {
            Log.d("onItemClick", "Clicked movie ID: " + movi.getId());
            Intent intent = new Intent(this, TitleDetailActivity.class);
            intent.putExtra("itemType", "movie");
            intent.putExtra("itemId", movi.getId());
            addUserDataToIntent(intent);
            startActivity(intent);
        } else if (movi.getType().equals("tv")) {
            Log.d("onItemClick", "Clicked movie ID: " + movi.getId());
            Intent intent = new Intent(this, TitleDetailActivity.class);
            intent.putExtra("itemType", "tv");
            intent.putExtra("itemId", movi.getId());
            addUserDataToIntent(intent);
            startActivity(intent);
        }
    }

    @Override
    public void onPersonClick(Person person) {
        Intent intent = new Intent(this, PersonDetailActivity.class);
        intent.putExtra("personId", person.getPersonid());
        intent.putExtra("token", token);
        addUserDataToIntent(intent);
        startActivity(intent);
    }

    @Override
    public void onItemClick(tvseries movi) {
        Log.d("onItemClick", "Clicked movie ID: " + movi.getId());
        Intent intent = new Intent(this, TitleDetailActivity.class);
        intent.putExtra("itemType", "tv");
        intent.putExtra("itemId", movi.getId());
        addUserDataToIntent(intent);
        startActivity(intent);
    }

    public void addUserDataToIntent(Intent intent) {
        if (username != null) intent.putExtra("username", username);
        if (token != null) intent.putExtra("token", token);
        if (session_id != null) intent.putExtra("session_id", session_id);
        if (username != null) Log.d("username", username);
        if (token != null) Log.d("token", token);
        if (session_id != null) Log.d("session_id", session_id);
    }

    @Override
    public void onRefresh() {
        movie_in_watchlist.clear();
        tv_in_watchlist.clear();
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
                        fetchMovies();
                        fetchupcmingmovies();
                        fetchtopratedmovies();
                        fetchtrendingmovies();
                        fetchontheair();
                        fetchtopratedtvseries();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    @Override
    public void onRefreshPerson() {
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
                        fetchAllPersons();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
