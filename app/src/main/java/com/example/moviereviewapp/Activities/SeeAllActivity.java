package com.example.moviereviewapp.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviereviewapp.Adapters.MovieSeeAllAdapter;
import com.example.moviereviewapp.Adapters.MovieSeeAllRatingAdapter;
import com.example.moviereviewapp.Adapters.MovieUserProfileAdapter;
import com.example.moviereviewapp.Adapters.PersonAdapter;
import com.example.moviereviewapp.Adapters.PersonSeeAllAdapter;
import com.example.moviereviewapp.Adapters.TVSeriesSeeAllAdapter;
import com.example.moviereviewapp.Adapters.TrendingSeeallAdapter;
import com.example.moviereviewapp.Adapters.tvseriesadapter;
import com.example.moviereviewapp.Models.Movie_UserProfile;
import com.example.moviereviewapp.Models.movies;
import com.example.moviereviewapp.Models.trendingall;
import com.example.moviereviewapp.Models.tvseries;
import com.example.moviereviewapp.Models.Person;
import com.example.moviereviewapp.R;

import java.util.ArrayList;
import java.util.List;

public class SeeAllActivity extends AppCompatActivity {

    ImageView back_SeeAll_Btn;
    TextView textView_Title_SeeAll;

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
                    recyclerView.setAdapter(movieAdapter);
                } else {
                    MovieSeeAllRatingAdapter ratingAdapter = new MovieSeeAllRatingAdapter(this, movieList);
                    recyclerView.setAdapter(ratingAdapter);
                }
                break;

            case "tvseries":
                List<tvseries> seriesList = (List<tvseries>) getIntent().getSerializableExtra("tvseriesList");
                if (seriesList == null) {
                    seriesList = new ArrayList<>();
                    Toast.makeText(this, "No TV series found", Toast.LENGTH_SHORT).show();
                }

                TVSeriesSeeAllAdapter seriesAdapter = new TVSeriesSeeAllAdapter (this,seriesList);
                recyclerView.setAdapter(seriesAdapter);
                break;


            case "trending":
                List<trendingall> trendingList = (List<trendingall>) getIntent().getSerializableExtra("trendingList");
                if (trendingList == null) {
                    trendingList = new ArrayList<>();
                    Toast.makeText(this, "No movies found", Toast.LENGTH_SHORT).show();
                }

                TrendingSeeallAdapter trendingAdapter = new TrendingSeeallAdapter (this,trendingList);
                recyclerView.setAdapter(trendingAdapter);
                break;

            case "person":
                ArrayList<Person> personList = (ArrayList<Person>) getIntent().getSerializableExtra("personList");
                if (personList == null) {
                    personList = new ArrayList<>();
                    Toast.makeText(this, "No persons found", Toast.LENGTH_SHORT).show();
                }

                PersonSeeAllAdapter personAdapter = new PersonSeeAllAdapter( this, personList);
                recyclerView.setAdapter(personAdapter);
                break;

            default:
                Toast.makeText(this, "Unknown type: " + type, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
