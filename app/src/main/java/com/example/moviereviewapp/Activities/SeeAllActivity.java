package com.example.moviereviewapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.example.moviereviewapp.Models.Movie_UserProfile;
import com.example.moviereviewapp.R;

import java.util.ArrayList;
import java.util.List;

public class SeeAllActivity extends AppCompatActivity {
    ImageView back_SeeAll_Btn;
    //ToDo: kich hoat su kien onClick cho back_SeeAll_Btn de nguoi dung tro lai hoat dong truoc
    TextView textView_Title_SeeAll;
    //ToDo: hien thi ten truong hop muon hien thi
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

//        ImageView back = findViewById(R.id.back_SeeAll_Btn);
//        back.setOnClickListener(v -> {
//            Intent intent = new Intent(SeeAllActivity.this, UserProfileActivity.class);
//            startActivity(intent);
//        });

        RecyclerView recyclerView = findViewById(R.id.recycleView_Movie_SeeAll);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        List<Movie_UserProfile> movieList = new ArrayList<>();
        movieList.add(new Movie_UserProfile("", "Kaguya", "2024", 8.5, 8));
        movieList.add(new Movie_UserProfile("", "Gotoubun", "2022", 4.5, 8));
        movieList.add(new Movie_UserProfile("", "Kaguya111111111111111111111111111111111111111111111111111111111111111111111111111111", "2024", 8.5, 8));
        movieList.add(new Movie_UserProfile("", "Gotoubun1", "2022", 4.5, 8));

        //ToDo: su dung viewholder_viewall_movie cho recently viewed (tuong ung voi adapter)
        //ToDo: su dung viewholder_viewall_rating_movie cho rating, watchlist (tuong ung voi adapter)
        MovieSeeAllRatingAdapter adapter = new MovieSeeAllRatingAdapter(this, movieList);
        recyclerView.setAdapter(adapter);
    }
}