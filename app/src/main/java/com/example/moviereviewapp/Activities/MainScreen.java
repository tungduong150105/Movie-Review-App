package com.example.moviereviewapp.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;


import com.example.moviereviewapp.Adapters.MovieAdapter;
import com.example.moviereviewapp.Adapters.MovieItemAdapter;
import com.example.moviereviewapp.Adapters.PersonAdapter;
import com.example.moviereviewapp.Adapters.Top_Box_Office_Adapter;
import com.example.moviereviewapp.Adapters.movietrendingadapter;
import com.example.moviereviewapp.Adapters.tvseriesadapter;
import com.example.moviereviewapp.Models.Person;
import com.example.moviereviewapp.Models.movies;
import com.example.moviereviewapp.Models.trendingall;
import com.example.moviereviewapp.Models.tvseries;
import com.example.moviereviewapp.R;
import com.example.moviereviewapp.databinding.ActivityMainScreenBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainScreen extends AppCompatActivity {
    private ActivityMainScreenBinding binding;
    private MovieAdapter adapter;
    private MovieItemAdapter itemadapter, topratedadapter;
    private movietrendingadapter trendingadapter;

    private Top_Box_Office_Adapter topBoxOfficeAdapter;

    private PersonAdapter personadapter;
    private tvseriesadapter tvserieontheairsadapter, topratedtvseriesAdapter;
    private ArrayList<Person> person=new ArrayList<>();
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
    private int lastScrollY=0;
    private final int MAX_PAGE = 50;
    private int currentPage = 1;
   private static final String TMDB_API_KEY="75d6190f47f7d58c6d0511ca393d2f7d";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        setToolbar();
        setupRecyclerView();

        fetchMovies();
        fetchupcmingmovies();
        fetchtopratedmovies();

      comingrecyclerview();
      topratedrecyclerview();
      trendingrecyclerview();
      fetchtrendingmovies();

      personrecyclerview();
        fetchAllPersons();


        topboxofficeadapter();
        fetchtopboxoffice();

        tvseriesrecyclerview();
        fetchontheair();

        topratedTvseriesRecyclerview();
        fetchtopratedtvseries();



    }
    private void topratedTvseriesRecyclerview(){
        topratedtvseriesAdapter =new tvseriesadapter(toprated);
        binding.topratedrecyclerview.setLayoutManager (new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.topratedrecyclerview.setAdapter(topratedtvseriesAdapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        binding.topratedrecyclerview.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
    }
    private void fetchtopratedtvseries(){
        TMDBApi api = RetrofitClient.getApiService();
         topratedCall= api.getTvSeriesTopRated("75d6190f47f7d58c6d0511ca393d2f7d",1);
        topratedCall.enqueue(new Callback<tvseriesresponse>() {
            @Override
            public void onResponse(@NonNull Call<tvseriesresponse> call, @NonNull Response<tvseriesresponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    toprated.clear();
                    List<tvseries> results = response.body().getResults();

                    Log.d("TVSERIES", "Fetched " + results.size() + " series.");

                    toprated.addAll(results);

                    topratedtvseriesAdapter.notifyDataSetChanged();
                    for (tvseries movie : results) {
                        int movieId = movie.getId();
                        Call<tvseriesdetail> detailCall = api.getTvSeriesDetail(movieId, TMDB_API_KEY);
                        detailCall.enqueue(new Callback<tvseriesdetail>() {
                            @Override
                            public void onResponse(Call<tvseriesdetail> call, Response<tvseriesdetail> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    int epsnumber=response.body().getnumberofepsidose();
                                    movie.setepsnumber(epsnumber);
                                    Log.d("OntheAir", "Fetching number of epsidoses: " + movie.getName() + " (ID: " + movie.getId() + ")");

                                    topratedtvseriesAdapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<tvseriesdetail> call, Throwable t) {
                                Log.e("DETAIL_ERROR", "Lỗi khi lấy runtime cho movieId: " + movieId, t);
                            }
                        });
                    }


                }
                else {
                    showError("Failed to load movies: " + response.message());


                }
            }

            @Override
            public void onFailure(Call<tvseriesresponse> call, Throwable t) {

            }
        });

    }

    private  void tvseriesrecyclerview(){
       tvserieontheairsadapter =new tvseriesadapter(ontheair);
        binding.ontheairrecyclerview.setLayoutManager (new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.ontheairrecyclerview.setAdapter(tvserieontheairsadapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        binding.ontheairrecyclerview.addItemDecoration(new SpacingItemDecoration(spacingInPixels));

    }
    private void fetchontheair(){
        TMDBApi api = RetrofitClient.getApiService();
        ontheairCall = api.getTvSeriesontheair("75d6190f47f7d58c6d0511ca393d2f7d",1);
        ontheairCall.enqueue(new Callback<tvseriesresponse>() {
            @Override
            public void onResponse(@NonNull Call<tvseriesresponse> call, @NonNull Response<tvseriesresponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ontheair.clear();
                    List<tvseries> results = response.body().getResults();

                    Log.d("TVSERIES", "Fetched " + results.size() + " series.");

                    ontheair.addAll(results);

                    tvserieontheairsadapter.notifyDataSetChanged();
                    for (tvseries movie : results) {
                        int movieId = movie.getId();
                        Call<tvseriesdetail> detailCall = api.getTvSeriesDetail(movieId, TMDB_API_KEY);
                        detailCall.enqueue(new Callback<tvseriesdetail>() {
                            @Override
                            public void onResponse(Call<tvseriesdetail> call, Response<tvseriesdetail> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    int epsnumber=response.body().getnumberofepsidose();
                                    movie.setepsnumber(epsnumber);
                                    Log.d("OntheAir", "Fetching number of epsidoses: " + movie.getName() + " (ID: " + movie.getId() + ")");

                                    tvserieontheairsadapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<tvseriesdetail> call, Throwable t) {
                                Log.e("DETAIL_ERROR", "Lỗi khi lấy runtime cho movieId: " + movieId, t);
                            }
                        });
                    }


                }
                else {
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
                    Collections.sort(topboxofficelist, (m1, m2) -> Long.compare( m2.getRevenue(),  m1.getRevenue()));


                    topBoxOfficeAdapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                Log.e("TopBoxOffice", "Error fetching revenue for movie ID: " + movie.getMovieId() + " - " + t.getMessage(), t);
            }
        });
    }








    private void topboxofficeadapter(){
        topBoxOfficeAdapter =new Top_Box_Office_Adapter(this,topboxofficelist);
        binding.topboxofficelistview.setAdapter(topBoxOfficeAdapter);
    }
    private void fetchAllPersons() {
        person.clear();
        currentPage = 1;
        fetchPersonPage(currentPage);
    }

    private void fetchPersonPage(int page) {
        TMDBApi api = RetrofitClient.getApiService();
       personCall= api.getPerson(TMDB_API_KEY, page);
        personCall.enqueue(new Callback<PersonResoponse>() {
            @Override
            public void onResponse(Call<PersonResoponse> call, Response<PersonResoponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Person> results = response.body().getPersons();


                    for (Person p : results) {
                        fetchPersonDetail(p); // Lấy thêm birthdate, deathday
                    }

                    if (currentPage < MAX_PAGE) {
                        currentPage++;
                        fetchPersonPage(currentPage); // Gọi tiếp trang kế
                    }
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
    private void personrecyclerview(){
        personadapter =new PersonAdapter(person);
        binding.personrecycleview.setLayoutManager (new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.personrecycleview.setAdapter(personadapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        binding.personrecycleview.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
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
                            if (isBirthdayToday(detail.getBirthday())) {
                                person.add(basicPerson);
                                personadapter.notifyItemInserted(person.size() - 1);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PersonDetail> call, Throwable t) {
                        Log.e("API_DETAIL_ERROR", "Lỗi lấy chi tiết: " + t.getMessage(), t);
                    }
                });
    }
    private boolean isBirthdayToday(String birthday) {
        if (birthday == null || birthday.isEmpty()) return false;


        try {
            String[] parts = birthday.split("-");
            if (parts.length < 3) return false;

            int birthMonth = Integer.parseInt(parts[1]);
            int birthDay = Integer.parseInt(parts[2]);

            java.util.Calendar today = java.util.Calendar.getInstance();
            int todayMonth = today.get(java.util.Calendar.MONTH) + 1;
            int todayDay = today.get(java.util.Calendar.DAY_OF_MONTH);

            return (birthMonth == todayMonth && birthDay == todayDay);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private  void fetchtrendingmovies(){
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
                    for (trendingall movie : results) {
                        int movieId = movie.getId().hashCode();
                        Call<MovieDetail> detailCall = api.getMovieDetail(movieId, TMDB_API_KEY);
                        detailCall.enqueue(new Callback<MovieDetail>() {
                            @Override
                            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    int runtime = response.body().getRuntime();
                                    movie.setLengthFromRuntime(runtime);

                                    trendingadapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<MovieDetail> call, Throwable t) {
                                Log.e("DETAIL_ERROR", "Lỗi khi lấy runtime cho movieId: " + movieId, t);
                            }
                        });
                    }

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
    private void trendingrecyclerview(){
        trendingadapter =new movietrendingadapter(trendinglist);
        binding.trendingrecyclerview.setLayoutManager (new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.trendingrecyclerview.setAdapter(trendingadapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        binding.trendingrecyclerview.addItemDecoration(new SpacingItemDecoration(spacingInPixels));

    }


    private void fetchtopratedmovies(){
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
                        Call<MovieDetail> detailCall = api.getMovieDetail(movieId, TMDB_API_KEY);
                        detailCall.enqueue(new Callback<MovieDetail>() {
                            @Override
                            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    int runtime = response.body().getRuntime();
                                    movie.setLengthFromRuntime(runtime);

                                    topratedadapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<MovieDetail> call, Throwable t) {
                                Log.e("DETAIL_ERROR", "Lỗi khi lấy runtime cho movieId: " + movieId, t);
                            }
                        });
                    }

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

    private void topratedrecyclerview(){
        topratedadapter =new MovieItemAdapter(topratedmovieslist);
        binding.topickrecyclerview.setLayoutManager (new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        binding.topickrecyclerview.setAdapter(topratedadapter);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
        binding.topickrecyclerview.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
    }
    private void setupRecyclerView() {

        try {
            adapter = new MovieAdapter(moviesList);
            binding.toplistmovie.setAdapter(adapter);
            binding.toplistmovie.setOffscreenPageLimit(1);

        } catch (Exception e) {
            Log.e("RecyclerViewError", "Setup failed: " + e.getMessage());
            e.printStackTrace();
        }







    }
private void comingrecyclerview(){
        itemadapter =new MovieItemAdapter(upcominglist);
        binding.comingsoonrecyclerview.setLayoutManager (new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

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

    private void fetchupcmingmovies(){
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
                        Call<MovieDetail> detailCall = api.getMovieDetail(movieId, TMDB_API_KEY);
                        detailCall.enqueue(new Callback<MovieDetail>() {
                            @Override
                            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    int runtime = response.body().getRuntime();


                                    movie.setLengthFromRuntime(runtime);
                                    itemadapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(Call<MovieDetail> call, Throwable t) {
                                Log.e("DETAIL_ERROR", "Lỗi khi lấy runtime cho movieId: " + movieId, t);
                            }
                        });
                    }

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

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        if (moviesCall != null) {
            moviesCall.cancel();
        }
        super.onDestroy();
    }



    private void setToolbar() {
        binding.ivHome.setOnClickListener(v -> {
            binding.ivHome.setImageResource(R.drawable.clicked_home_icon);
            binding.ivSearch.setImageResource(R.drawable.search_icon);
            binding.ivPlay.setImageResource(R.drawable.playicon);
            binding.ivProfile.setImageResource(R.drawable.usericon);
        });

        binding.ivSearch.setOnClickListener(v -> {
            binding.ivHome.setImageResource(R.drawable.homeicon);
            binding.ivSearch.setImageResource(R.drawable.clicked_search_icon);
            binding.ivPlay.setImageResource(R.drawable.playicon);
            binding.ivProfile.setImageResource(R.drawable.usericon);
        });

        binding.ivPlay.setOnClickListener(v -> {
            binding.ivHome.setImageResource(R.drawable.homeicon);
            binding.ivSearch.setImageResource(R.drawable.search_icon);
            binding.ivPlay.setImageResource(R.drawable.clicked_play_icon);
            binding.ivProfile.setImageResource(R.drawable.usericon);
        });

        binding.ivProfile.setOnClickListener(v -> {
            binding.ivHome.setImageResource(R.drawable.homeicon);
            binding.ivSearch.setImageResource(R.drawable.search_icon);
            binding.ivPlay.setImageResource(R.drawable.playicon);
            binding.ivProfile.setImageResource(R.drawable.clicked_user_icon);
        });

        binding.scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = binding.scrollView.getScrollY();
            if (scrollY > lastScrollY) {
                binding.bottomBar.setBackgroundColor(0xAA0F0F0F);
            } else if (scrollY < lastScrollY) {
                binding.bottomBar.setBackgroundColor(0xFF111111);
            }
            lastScrollY = scrollY;
        });
    }
}
