package com.example.moviereviewapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

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

    //List phim theo muc dich
    List<Movie_UserProfile> recentlyViewedMovieList, ratingMovieList, watchListMovieList;
    //Adapter phim theo muc dich
    MovieUserProfileAdapter recentlyViewedAdapter, ratingAdapter, watchListAdapter;

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

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        if (userId == null) {
            // Nếu chưa đăng nhập, chuyển về LoginActivity
//            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish(); // Đóng màn hình hiện tại
        } else {
            // Nếu đã đăng nhập, tiếp tục với các thao tác khác
            imageView_Logout_UserProfile = findViewById(R.id.imageView_Logout_UserProfile);
            imageView_Logout_UserProfile.setVisibility(View.VISIBLE);
            textView_Username_UserProfile = findViewById(R.id.textView_Username_UserProfile);
            textView_Username_UserProfile.setText(userId);
            btn_SignIn_SignUp_UserProfile = findViewById(R.id.btn_SignIn_SignUp_UserProfile);
            btn_SignIn_SignUp_UserProfile.setVisibility(View.GONE);
        }

        //Su kien dang xuat cho nut log out
        imageView_Logout_UserProfile = findViewById(R.id.imageView_Logout_UserProfile);
        imageView_Logout_UserProfile.setOnClickListener(v -> {

        });

        btn_SignIn_SignUp_UserProfile = findViewById(R.id.btn_SignIn_SignUp_UserProfile);
        btn_SignIn_SignUp_UserProfile.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        });



//        RecyclerView recyclerView1 = findViewById(R.id.recycleView_Ratings_UserProfile);
//        RecyclerView recyclerView2 = findViewById(R.id.recycleView_RecentlyViewed_UserProfile);
//        RecyclerView recyclerView3 = findViewById(R.id.recycleView_Watchlist_UserProfile);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        recyclerView1.setLayoutManager(layoutManager);
//        recyclerView2.setLayoutManager(layoutManager2);
//        recyclerView3.setLayoutManager(layoutManager3);
//
//        List<Movie_UserProfile> movieList = new ArrayList<>();
//        //Gioi han 72 ky tu, neu hon thi them dau ...
//        movieList.add(new Movie_UserProfile("", "Kaguya", "2024", 8.5, 8));
//        movieList.add(new Movie_UserProfile("", "Gotoubun", "2022", 4.5, 8));
//        movieList.add(new Movie_UserProfile("", "Kaguya111111111111111111111111111111111111111111111111111111111111111111111111111111", "2024", 8.5, 8));
//        movieList.add(new Movie_UserProfile("", "Gotoubun1", "2022", 4.5, 8));
//
//        //ToDo: Su dung viewholder_movie cho cac muc hien thi phim trong user profile (tuong ung voi adapter)
//        MovieUserProfileAdapter adapter = new MovieUserProfileAdapter(this, movieList);
//        MovieUserProfileAdapter adapter2 = new MovieUserProfileAdapter(this, movieList);
//        MovieUserProfileAdapter adapter3 = new MovieUserProfileAdapter(this, movieList);
//        recyclerView1.setAdapter(adapter);
//        recyclerView2.setAdapter(adapter2);
//        recyclerView3.setAdapter(adapter3);
    }
    public void onClick_SignIn_SignUp_UserProfile(View view) {
        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void onClick_Ratings_UserProfile(View view) {

    }

    public void onClick_Watchlist_UserProfile(View view) {

    }

//    private void setupMovieSection(
//            List<Movie_UserProfile> movieList,
//            RecyclerView recyclerView,
//            TextView textViewNumber,
//            TextView textViewSeeAll,
//            ImageView imageView,
//            TextView textViewNoData,
//            TextView textViewCap,
//            TextView textViewTitle,
//            MovieUserProfileAdapter adapterTarget,
//            boolean isVisible,
//            int sectionType // 1: RecentlyViewed, 2: Ratings, 3: Wishlist
//    ) {
//        runOnUiThread(() -> {
//            // Cập nhật danh sách tương ứng
//            switch (sectionType) {
//                case 1:
//                    recentlyViewedMovieList = movieList;
//                    recentlyViewedAdapter = new MovieUserProfileAdapter(this, movieList);
//                    recyclerView.setAdapter(recentlyViewedAdapter);
//                    break;
//                case 2:
//                    ratingMovieList = movieList;
//                    ratingAdapter = new MovieUserProfileAdapter(this, movieList);
//                    recyclerView.setAdapter(ratingAdapter);
//                    break;
//                case 3:
//                    watchListMovieList = movieList;
//                    watchListAdapter = new MovieUserProfileAdapter(this, movieList);
//                    recyclerView.setAdapter(watchListAdapter);
//                    break;
//            }
//
//            recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//            textViewNumber.setText(String.valueOf(movieList.size()));
//
//            if (movieList.size() > 0) {
//                textViewNoData.setVisibility(View.GONE);
//                if (textViewTitle != null) textViewTitle.setVisibility(View.VISIBLE);
//                recyclerView.setVisibility(View.VISIBLE);
//            } else {
//                textViewNoData.setVisibility(View.VISIBLE);
//                if (textViewTitle != null) textViewTitle.setVisibility(View.GONE);
//                recyclerView.setVisibility(View.GONE);
//            }
//        });
//    }

//    @Override
//    public void onResponse(Call call, Response response) throws IOException {
//        if (response.isSuccessful()) {
//            String responseData = response.body().string();
//
//            Gson gson = new Gson();
//            Type listType = new TypeToken<ArrayList<Movie_UserProfile>>() {}.getType();
//            List<Movie_UserProfile> movieList = gson.fromJson(responseData, listType);
//
//            setupMovieSection(
//                    movieList,
//                    recycleView_RecentlyViewed_UserProfile,
//                    textView_RecentlyViewed_Number_UserProfile,
//                    textView_NoRecentlyViewed_UserProfile,
//                    textView_RecentlyViewed_Cap_UserProfile,
//                    null,
//                    true,
//                    1
//            );
//        }
//    }
//
//    @Override
//    public void onResponse(Call call, Response response) throws IOException {
//        if (response.isSuccessful()) {
//            String responseData = response.body().string();
//
//            Gson gson = new Gson();
//            Type listType = new TypeToken<ArrayList<Movie_UserProfile>>() {}.getType();
//            List<Movie_UserProfile> movieList = gson.fromJson(responseData, listType);
//
//            setupMovieSection(
//                    movieList,
//                    recycleView_Ratings_UserProfile,
//                    textView_Ratings_Number_UserProfile,
//                    textView_NoRatings_UserProfile,
//                    null, // Không cần caption riêng cho Ratings
//                    null,
//                    true,
//                    2
//            );
//        }
//    }
//
//    @Override
//    public void onResponse(Call call, Response response) throws IOException {
//        if (response.isSuccessful()) {
//            String responseData = response.body().string();
//
//            Gson gson = new Gson();
//            Type listType = new TypeToken<ArrayList<Movie_UserProfile>>() {}.getType();
//            List<Movie_UserProfile> movieList = gson.fromJson(responseData, listType);
//
//            setupMovieSection(
//                    movieList,
//                    recycleView_Watchlist_UserProfile,
//                    textView_Watchlist_Number_UserProfile,
//                    textView_NoWatchlist_UserProfile,
//                    textView_Watchlist_Cap_UserProfile,
//                    null,
//                    true,
//                    3
//            );
//        }
//    }

}