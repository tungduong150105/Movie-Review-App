package com.example.moviereviewapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviereviewapp.Adapters.MovieItemAdapter;
import com.example.moviereviewapp.Models.movies;
import com.example.moviereviewapp.Models.trendingall;
import com.example.moviereviewapp.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {
    TextView textView_Username_UserProfile;
    //textView_Username_UserProfile se hien thi ten nguoi dung, neu khong thi hien thi "Sign In"
    ImageView imageView_Logout_UserProfile;
    //kich hoat su kien onClick cho imageView_Logout_UserProfile de nguoi dung log out

    androidx.appcompat.widget.AppCompatButton btn_SignIn_SignUp_UserProfile;
    //kich hoat su kien onClick cho btn_SignIn_SignUp_UserProfile de nguoi dung chuyen sang LoginActivity

    TextView textView_Ratings_UserProfile,
            textView_Wishlist_UserProfile;
    //cac textview tren hien thi so luong phim tuong ung
    LinearLayout linearLayout3, linearLayout40;

    //Recently viewed
    TextView textView_RecentlyViewed_Number_UserProfile,
        textView_SeeAll_RecentlyViewed_UserProfile;
    //textView_RecentlyViewed_Number_UserProfile hien thi so luong phim recently viewed, mac dinh bang 0 neu chua dang nhap
    //kich hoat su kien onClick cho textView_SeeAll_RecentlyViewed_UserProfile de nguoi dung chuyen sang SeeAllActivity ung voi Recenctly viewed

    TextView textView_NoRecentlyViewed_UserProfile,
            textView_RecentlyViewed_Cap_UserProfile;
    //cac textview tren visibility = visible khi co phim recently viewed, nguoc lai visibility = gone
    RecyclerView recycleView_RecentlyViewed_UserProfile;
    //visibility = gone khi khong co phim, nguoc lai visibility = visible (visibility nguoc voi textview o tren)

    //Rating
    TextView textView_Ratings_Number_UserProfile,
            textView_SeeAll_Ratings_UserProfile,
            textView_NoRatings_UserProfile;
    ImageView imgView_Ratings_UserProfile;
    com.google.android.material.button.MaterialButton btn_Ratings_UserProfile;
    RecyclerView recycleView_Ratings_UserProfile;
    //ToDo: cach hien thi giong nhu Recently viewed, nhung co them imageview, button.

    //Watchlist
    TextView textView_Watchlist_Number_UserProfile,
            textView_SeeAll_Watchlist_UserProfile,
            textView_NoWatchlist_UserProfile,
            textView_Watchlist_Cap_UserProfile;
    ImageView imgView_Watchlist_UserProfile;
    com.google.android.material.button.MaterialButton btn_Watchlist_UserProfile;
    RecyclerView recycleView_Watchlist_UserProfile;
    //ToDo: tuong tu Recently viewed

    //List phim theo muc dich
    //Todo: sử dụng adapter và movie models tương tự mainscreen
    List<movies> recentlyViewedMovieList, ratingMovieList, watchListMovieList;
    //Adapter phim theo muc dich
    MovieItemAdapter recentlyViewedAdapter, ratingAdapter, watchListAdapter;

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
        fetchtopratedmovies();
        fetchMovies();

        //Khoi tao thuoc tinh
        linearLayout3 = findViewById(R.id.linearLayout3);
        linearLayout40 = findViewById(R.id.linearLayout40);
        textView_Username_UserProfile = findViewById(R.id.textView_Username_UserProfile);
        imageView_Logout_UserProfile = findViewById(R.id.imageView_Logout_UserProfile);
        btn_SignIn_SignUp_UserProfile = findViewById(R.id.btn_SignIn_SignUp_UserProfile);
        textView_Ratings_UserProfile = findViewById(R.id.textView_Ratings_UserProfile);
        textView_Wishlist_UserProfile = findViewById(R.id.textView_Wishlist_UserProfile);
        textView_RecentlyViewed_Number_UserProfile = findViewById(R.id.textView_RecentlyViewed_Number_UserProfile);
        textView_SeeAll_RecentlyViewed_UserProfile = findViewById(R.id.textView_SeeAll_RecentlyViewed_UserProfile);
        textView_NoRecentlyViewed_UserProfile = findViewById(R.id.textView_NoRecentlyViewed_UserProfile);
        textView_RecentlyViewed_Cap_UserProfile = findViewById(R.id.textView_RecentlyViewed_Cap_UserProfile);
        recycleView_RecentlyViewed_UserProfile = findViewById(R.id.recycleView_RecentlyViewed_UserProfile);
        textView_Ratings_Number_UserProfile = findViewById(R.id.textView_Ratings_Number_UserProfile);
        textView_SeeAll_Ratings_UserProfile = findViewById(R.id.textView_SeeAll_Ratings_UserProfile);
        textView_NoRatings_UserProfile = findViewById(R.id.textView_NoRatings_UserProfile);
        imgView_Ratings_UserProfile = findViewById(R.id.imgView_Ratings_UserProfile);
        btn_Ratings_UserProfile = findViewById(R.id.btn_Ratings_UserProfile);
        recycleView_Ratings_UserProfile = findViewById(R.id.recycleView_Ratings_UserProfile);
        textView_Watchlist_Number_UserProfile = findViewById(R.id.textView_Watchlist_Number_UserProfile);
        textView_SeeAll_Watchlist_UserProfile = findViewById(R.id.textView_SeeAll_Watchlist_UserProfile);
        textView_NoWatchlist_UserProfile = findViewById(R.id.textView_NoWatchlist_UserProfile);
        textView_Watchlist_Cap_UserProfile = findViewById(R.id.textView_Watchlist_Cap_UserProfile);
        imgView_Watchlist_UserProfile = findViewById(R.id.imgView_Watchlist_UserProfile);
        btn_Watchlist_UserProfile = findViewById(R.id.btn_Watchlist_UserProfile);
        recycleView_Watchlist_UserProfile = findViewById(R.id.recycleView_Watchlist_UserProfile);

        // Khởi tạo danh sách rỗng
        recentlyViewedMovieList = new ArrayList<>();
        ratingMovieList = new ArrayList<>();
        watchListMovieList = new ArrayList<>();

        //ToDo: Xử lý kiểm tra tình trạng đăng nhập
        if (userId == null) {
            // Nếu chưa đăng nhập, chuyển về LoginActivity
//            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish(); // Đóng màn hình hiện tại
        } else {
            // Nếu đã đăng nhập, tiếp tục với các thao tác khác
            imageView_Logout_UserProfile.setVisibility(View.VISIBLE);
            textView_Username_UserProfile.setText(userId);
            btn_SignIn_SignUp_UserProfile.setVisibility(View.GONE);//Ẩn nút đăng nhập

            linearLayout3.setVisibility(View.VISIBLE);//Hiển thị ô chứa số lượng phim trong Ratings
            textView_Ratings_UserProfile.setText(String.valueOf(ratingMovieList.size()));//Hiển thị số lượng phim trong Ratings

            linearLayout40.setVisibility(View.VISIBLE);//Hiển thị ô chứa số lượng phim trong Wishlist
            textView_Wishlist_UserProfile.setText(String.valueOf(watchListMovieList.size()));//Hiển thị số lượng phim trong Wishlist
        }

        //Su kien dang xuat cho nut log out
        imageView_Logout_UserProfile.setOnClickListener(v -> {
            //ToDo: Xử lý logout
        });

        //ToDo: Chuyển sang LoginActivity khi nhấn nút Sign In
        btn_SignIn_SignUp_UserProfile.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        //ToDo: Cách hiển thị danh sách phim theo từng đề mục ở dưới
//        recentlyViewedMovieList.add(new movies("ggggg1", 999, "gghg", "ghggh", "khkhk", 8.5, 8, "4/4/4"));
//        recentlyViewedMovieList.add(new movies("ggggg2", 999, "gghg", "ghggh", "khkhk", 8.5, 8, "4/4/4"));
//        recentlyViewedMovieList.add(new movies("ggggg3", 999, "gghg", "ghggh", "khkhk", 8.5, 8, "4/4/4"));
//        recentlyViewedMovieList.add(new movies("ggggg4", 999, "gghg", "ghggh", "khkhk", 8.5, 8, "4/4/4"));
//
//        ratingMovieList.add(new movies("ggggg1", 999, "gghg", "ghggh", "khkhk", 8.5, 8, "4/4/4"));
//        ratingMovieList.add(new movies("ggggg2", 999, "gghg", "ghggh", "khkhk", 8.5, 8, "4/4/4"));
//        ratingMovieList.add(new movies("ggggg3", 999, "gghg", "ghggh", "khkhk", 8.5, 8, "4/4/4"));
//        ratingMovieList.add(new movies("ggggg4", 999, "gghg", "ghggh", "khkhk", 8.5, 8, "4/4/4"));
//
//        watchListMovieList.add(new movies("ggggg1", 999, "gghg", "ghggh", "khkhk", 8.5, 8, "4/4/4"));
//        watchListMovieList.add(new movies("ggggg2", 999, "gghg", "ghggh", "khkhk", 8.5, 8, "4/4/4"));
//        watchListMovieList.add(new movies("ggggg3", 999, "gghg", "ghggh", "khkhk", 8.5, 8, "4/4/4"));
//        watchListMovieList.add(new movies("ggggg4", 999, "gghg", "ghggh", "khkhk", 8.5, 8, "4/4/4"));
//
//        //ToDo: XỬ LÝ CÁC DANH SÁCH PHIM TỪ ĐÂY
//        //ToDo: Su dung viewholder_movie cho cac muc hien thi phim trong user profile (tuong ung voi adapter)
//        recentlyViewedAdapter = new MovieItemAdapter(recentlyViewedMovieList);
//        ratingAdapter = new MovieItemAdapter(ratingMovieList);
//        watchListAdapter = new MovieItemAdapter(watchListMovieList);
//
//        recycleView_Ratings_UserProfile.setAdapter(ratingAdapter);
//        recycleView_RecentlyViewed_UserProfile.setAdapter(recentlyViewedAdapter);
//        recycleView_Watchlist_UserProfile.setAdapter(watchListAdapter);
//
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
//
//        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);
//
//        recycleView_Ratings_UserProfile.setLayoutManager(layoutManager);
//        recycleView_RecentlyViewed_UserProfile.setLayoutManager(layoutManager2);
//        recycleView_Watchlist_UserProfile.setLayoutManager(layoutManager3);
//
//        //ToDo: Xử lý khi Recently Viewed Movie có dữ liệu
//        textView_NoRecentlyViewed_UserProfile.setVisibility(View.GONE);
//        textView_RecentlyViewed_Cap_UserProfile.setVisibility(View.GONE);
//        recycleView_RecentlyViewed_UserProfile.setVisibility(View.VISIBLE);
//        textView_RecentlyViewed_Number_UserProfile.setVisibility(View.VISIBLE);
//        textView_SeeAll_RecentlyViewed_UserProfile.setVisibility(View.VISIBLE);
//        recycleView_RecentlyViewed_UserProfile.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
//        textView_SeeAll_RecentlyViewed_UserProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //ToDo: chuyển sang SeeAllActivity cho Recently Viewed
//                Intent intent = new Intent(UserProfileActivity.this, SeeAllActivity.class);
//                intent.putExtra("movieList",  (Serializable) recentlyViewedMovieList);
//                intent.putExtra("title", "Recently Viewed");
//                startActivity(intent);
//            }
//        });
//        textView_RecentlyViewed_Number_UserProfile.setText(String.valueOf(recentlyViewedMovieList.size()));
//
//        //ToDo: Xử lý khi Rating Movie có dữ liệu
//        btn_Ratings_UserProfile.setVisibility(View.GONE);
//        imgView_Ratings_UserProfile.setVisibility(View.GONE);
//        textView_NoRatings_UserProfile.setVisibility(View.GONE);
//        recycleView_Ratings_UserProfile.setVisibility(View.VISIBLE);
//        textView_Ratings_Number_UserProfile.setVisibility(View.VISIBLE);
//        textView_SeeAll_Ratings_UserProfile.setVisibility(View.VISIBLE);
//        recycleView_Ratings_UserProfile.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
//        textView_SeeAll_Ratings_UserProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //ToDo: chuyển sang SeeAllActivity cho Rating
//                Intent intent = new Intent(UserProfileActivity.this, SeeAllActivity.class);
//                intent.putExtra("movieList",  (Serializable)ratingMovieList);
//                intent.putExtra("title", "Rating");
//                startActivity(intent);
//            }
//        });
//        textView_Ratings_Number_UserProfile.setText(String.valueOf(ratingMovieList.size()));
//
//        //ToDo: Xử lý khi Watchlist Movie có dữ liệu
//        textView_Watchlist_Cap_UserProfile.setVisibility(View.GONE);
//        btn_Watchlist_UserProfile.setVisibility(View.GONE);
//        imgView_Watchlist_UserProfile.setVisibility(View.GONE);
//        textView_NoWatchlist_UserProfile.setVisibility(View.GONE);
//        recycleView_Watchlist_UserProfile.setVisibility(View.VISIBLE);
//        textView_Watchlist_Number_UserProfile.setVisibility(View.VISIBLE);
//        textView_SeeAll_Watchlist_UserProfile.setVisibility(View.VISIBLE);
//        recycleView_Watchlist_UserProfile.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
//        textView_SeeAll_Watchlist_UserProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //ToDo: chuyển sang SeeAllActivity cho Watchlist
//                Intent intent = new Intent(UserProfileActivity.this, SeeAllActivity.class);
//                intent.putExtra("movieList",  (Serializable)watchListMovieList);
//                intent.putExtra("title", "Watchlist");
//                startActivity(intent);
//            }
//        });
//        textView_Watchlist_Number_UserProfile.setText(String.valueOf(watchListMovieList.size()));
    }

    public void onClick_SignIn_SignUp_UserProfile(View view) {
        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void onClick_Ratings_UserProfile(View view) {
        //ToDo: Xử lý nút trong Ratings khi chưa có phim
        Intent intent = new Intent(UserProfileActivity.this, SeeAllActivity.class);
        intent.putExtra("movieList",  (Serializable)moviesListInRatingList);
        intent.putExtra("title", "Fan favorites");
        startActivity(intent);
    }

    public void onClick_Watchlist_UserProfile(View view) {
        //ToDo: Xử lý nút trong Wishlist khi chưa có phim
        Intent intent = new Intent(UserProfileActivity.this, SeeAllActivity.class);
        intent.putExtra("movieList",  (Serializable)topratedmovieslist);
        intent.putExtra("title", "Top rated movies");
        startActivity(intent);
    }

//    private void setupMovieSection(
//            List<movies> movieList,
//            RecyclerView recyclerView,
//            TextView textViewNumber,
//            TextView textViewSeeAll,
//            ImageView imageView,
//            TextView textViewNoData,
//            TextView textViewCap,
//            TextView textViewTitle,
//            MovieItemAdapter adapterTarget,
//            boolean isVisible,
//            int sectionType // 1: RecentlyViewed, 2: Ratings, 3: Wishlist
//    ) {
//        runOnUiThread(() -> {
//            // Cập nhật danh sách tương ứng
//            switch (sectionType) {
//                case 1:
//                    recentlyViewedMovieList = movieList;
//                    recentlyViewedAdapter = new MovieItemAdapter(movieList);
//                    recyclerView.setAdapter(recentlyViewedAdapter);
//                    break;
//                case 2:
//                    ratingMovieList = movieList;
//                    ratingAdapter = new MovieItemAdapter(movieList);
//                    recyclerView.setAdapter(ratingAdapter);
//                    break;
//                case 3:
//                    watchListMovieList = movieList;
//                    watchListAdapter = new MovieItemAdapter(movieList);
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
//
//    @Override
//    public void onResponse(Call call, Response response) throws IOException {
//        if (response.isSuccessful()) {
//            String responseData = response.body().string();
//
//            Gson gson = new Gson();
//            Type listType = new TypeToken<ArrayList<movies>>() {}.getType();
//            List<movies> movieList = gson.fromJson(responseData, listType);
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
//            Type listType = new TypeToken<ArrayList<movies>>() {}.getType();
//            List<movies> movieList = gson.fromJson(responseData, listType);
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
//            Type listType = new TypeToken<ArrayList<movies>>() {}.getType();
//            List<movies> movieList = gson.fromJson(responseData, listType);
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
    private static final String TMDB_API_KEY = "75d6190f47f7d58c6d0511ca393d2f7d";
    private List<movies> topratedmovieslist = new ArrayList<movies>();
    private List<movies> moviesListInRatingList = new ArrayList<movies>();
    private Call<MovieResponse> moviesCall;

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

//                    topratedadapter.notifyDataSetChanged();

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

//                                    topratedadapter.notifyDataSetChanged();
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

    private void fetchMovies() {
        TMDBApi api = RetrofitClient.getApiService();
        moviesCall = api.getTrendingMovies(TMDB_API_KEY);

        moviesCall.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    moviesListInRatingList.clear();
                    List<movies> results = response.body().getResults();
                    Log.d("MOVIES", "Fetched " + results.size() + " movies.");
                    moviesListInRatingList.addAll(results);
//                    adapter.notifyDataSetChanged();
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
}