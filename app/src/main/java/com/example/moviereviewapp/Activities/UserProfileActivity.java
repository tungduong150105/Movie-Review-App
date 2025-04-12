package com.example.moviereviewapp.Activities;

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

import com.example.moviereviewapp.Adapters.MovieUserProfileAdapter;
import com.example.moviereviewapp.Models.Movie_UserProfile;
import com.example.moviereviewapp.R;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {
    TextView textView_Username_UserProfile;
    //ToDo: textView_Username_UserProfile se hien thi ten nguoi dung, neu khong thi hien thi "Sign In"
    ImageView imageView_Logout_UserProfile;
    //ToDo: kich hoat su kien onClick cho imageView_Logout_UserProfile de nguoi dung log out

    androidx.appcompat.widget.AppCompatButton btn_SignIn_SignUp_UserProfile;
    //ToDo: kich hoat su kien onClick cho btn_SignIn_SignUp_UserProfile de nguoi dung chuyen sang LoginActivity

    TextView textView_Ratings_UserProfile, textView_Wishlist_UserProfile, textView_List_UserProfile;
    //ToDo: cac textview tren hien thi so luong phim tuong ung

    //Recently viewed
    TextView textView_RecentlyViewed_Number_UserProfile, textView_SeeAll_RecentlyViewed_UserProfile;
    //ToDo: textView_RecentlyViewed_Number_UserProfile hien thi so luong phim recently viewed, mac dinh bang 0 neu chua dang nhap
    //ToDo: kich hoat su kien onClick cho textView_SeeAll_RecentlyViewed_UserProfile de nguoi dung chuyen sang SeeAllActivity ung voi Recenctly viewed

    TextView textView_NoRecentlyViewed_UserProfile, textView_RecentlyViewed_Cap_UserProfile;
    //ToDo: cac textview tren visibility = visible khi co phim recently viewed, nguoc lai visibility = gone
    RecyclerView recycleView_RecentlyViewed_UserProfile;
    //ToDo: visibility = gone khi khong co phim, nguoc lai visibility = visible (visibility nguoc voi textview o tren)

    //Rating
    TextView textView_Ratings_Number_UserProfile, textView_SeeAll_Ratings_UserProfile, textView_NoRatings_UserProfile;
    ImageView imgView_Ratings_UserProfile;
    com.google.android.material.button.MaterialButton btn_Ratings_UserProfile;
    RecyclerView recycleView_Ratings_UserProfile;
    //ToDo: cach hien thi giong nhu Recently viewed, nhung co them imageview, button.

    //Watchlist
    TextView textView_Watchlist_Number_UserProfile, textView_SeeAll_Watchlist_UserProfile, textView_NoWatchlist_UserProfile, textView_Watchlist_Cap_UserProfile;
    ImageView imgView_Watchlist_UserProfile;
    com.google.android.material.button.MaterialButton btn_Watchlist_UserProfile;
    RecyclerView recycleView_Watchlist_UserProfile;
    //ToDo: tuong tu Recently viewed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        RecyclerView recyclerView1 = findViewById(R.id.recycleView_Ratings_UserProfile);
        RecyclerView recyclerView2 = findViewById(R.id.recycleView_RecentlyViewed_UserProfile);
        RecyclerView recyclerView3 = findViewById(R.id.recycleView_Watchlist_UserProfile);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView1.setLayoutManager(layoutManager);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView3.setLayoutManager(layoutManager3);

        List<Movie_UserProfile> movieList = new ArrayList<>();
        //Gioi han 72 ky tu, neu hon thi them dau ...
        movieList.add(new Movie_UserProfile("", "Kaguya", "2024", 8.5, 8));
        movieList.add(new Movie_UserProfile("", "Gotoubun", "2022", 4.5, 8));
        movieList.add(new Movie_UserProfile("", "Kaguya111111111111111111111111111111111111111111111111111111111111111111111111111111", "2024", 8.5, 8));
        movieList.add(new Movie_UserProfile("", "Gotoubun1", "2022", 4.5, 8));

        //ToDo: Su dung viewholder_movie cho cac muc hien thi phim trong user profile (tuong ung voi adapter)
        MovieUserProfileAdapter adapter = new MovieUserProfileAdapter(this, movieList);
        MovieUserProfileAdapter adapter2 = new MovieUserProfileAdapter(this, movieList);
        MovieUserProfileAdapter adapter3 = new MovieUserProfileAdapter(this, movieList);
        recyclerView1.setAdapter(adapter);
        recyclerView2.setAdapter(adapter2);
        recyclerView3.setAdapter(adapter3);
    }
}