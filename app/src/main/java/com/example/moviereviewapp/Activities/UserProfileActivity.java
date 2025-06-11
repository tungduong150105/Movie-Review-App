package com.example.moviereviewapp.Activities;

import static java.lang.Long.parseLong;

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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviereviewapp.Adapters.BaseActivity;
import com.example.moviereviewapp.Adapters.MovieItemAdapter;
import com.example.moviereviewapp.Adapters.PersonAdapter;
import com.example.moviereviewapp.Models.Person;
import com.example.moviereviewapp.Models.TMDBAPI;
import com.example.moviereviewapp.Models.UserAPI;
import com.example.moviereviewapp.Models.movies;
import com.example.moviereviewapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;


import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserProfileActivity extends BaseActivity implements MovieItemAdapter.OnItemClickListener, PersonAdapter.OnPersonClickListener  {
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

    //Favorite actors
    LinearLayout linearLayout_actor;
    TextView textView_FavoriteActors_Number_UserProfile, textView_SeeAll_FavoriteActors_UserProfile, textView_NoFavoriteActors_UserProfile;
    androidx.recyclerview.widget.RecyclerView recycleView_FavoriteActors_UserProfile;

    //List phim theo muc dich
    //Todo: sử dụng adapter và movie models tương tự mainscreen
    List<movies> recentlyViewedMovieList, ratingMovieList, watchListMovieList;
    //Favorite actors list
    List<Person> favoriteActorsList;

    //Adapter phim theo muc dich
    MovieItemAdapter recentlyViewedAdapter, ratingAdapter, watchListAdapter;

    //Adapter cho actors
    PersonAdapter favoriteActorsAdapter;

    String username;
    String token;
    UserAPI userAPI;
    TMDBAPI tmdbAPI;
    String session_id;
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
        setupBottomBar(this,"profile");
        userAPI = new UserAPI();
        tmdbAPI = new TMDBAPI();

        username = getIntent().getStringExtra("username");
        token = getIntent().getStringExtra("token");

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            session_id = extra.getString("session_id");
            Log.d("MainScreen", username + " " + token + " " + session_id);
            assert session_id != null;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", "aa");

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
        linearLayout_actor = findViewById(R.id.linearLayout_actor);
        textView_FavoriteActors_Number_UserProfile = findViewById(R.id.textView_FavoriteActors_Number_UserProfile);
        textView_SeeAll_FavoriteActors_UserProfile = findViewById(R.id.textView_SeeAll_FavoriteActors_UserProfile);
        textView_NoFavoriteActors_UserProfile = findViewById(R.id.textView_NoFavoriteActors_UserProfile);
        recycleView_FavoriteActors_UserProfile = findViewById(R.id.recycleView_FavoriteActors_UserProfile);

        // Khởi tạo danh sách rỗng
        recentlyViewedMovieList = new ArrayList<>();
        ratingMovieList = new ArrayList<>();
        watchListMovieList = new ArrayList<>();
        favoriteActorsList = new ArrayList<>();

        //ToDo: Xử lý kiểm tra tình trạng đăng nhập
        if (userId == null) {
        } else {
            // Nếu đã đăng nhập, tiếp tục với các thao tác khác
            imageView_Logout_UserProfile.setVisibility(View.VISIBLE);
            textView_Username_UserProfile.setText(username);
            btn_SignIn_SignUp_UserProfile.setVisibility(View.GONE);//Ẩn nút đăng nhập

            linearLayout3.setVisibility(View.VISIBLE);//Hiển thị ô chứa số lượng phim trong Ratings
            textView_Ratings_UserProfile.setText(String.valueOf(ratingMovieList.size()));//Hiển thị số lượng phim trong Ratings
            Log.d("MainScreen1", String.valueOf(ratingMovieList.size()));

            linearLayout40.setVisibility(View.VISIBLE);//Hiển thị ô chứa số lượng phim trong Wishlist
            textView_Wishlist_UserProfile.setText(String.valueOf(watchListMovieList.size()));//Hiển thị số lượng phim trong Wishlist
            Log.d("MainScreen1", String.valueOf(watchListMovieList.size()));

            getRatingList();
            getWatchList();
            getRecentList();
            getActorList();

        }



        //Su kien dang xuat cho nut log out
        imageView_Logout_UserProfile.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            //ToDo: Xử lý logout
        });

        //ToDo: Chuyển sang LoginActivity khi nhấn nút Sign In
        btn_SignIn_SignUp_UserProfile.setOnClickListener(v -> {
            Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
            startActivity(intent);
        });


        Log.d("MainScreen", String.valueOf(recentlyViewedMovieList.size()));
        Log.d("MainScreen", String.valueOf(ratingMovieList.size()));
        Log.d("MainScreen", String.valueOf(watchListMovieList.size()));
        Log.d("MainScreen", String.valueOf(favoriteActorsList.size()));

        //ToDo: Cách hiển thị danh sách phim theo từng đề mục ở dưới

//        //ToDo: XỬ LÝ CÁC DANH SÁCH PHIM TỪ ĐÂY
//        //ToDo: Su dung viewholder_movie cho cac muc hien thi phim trong user profile (tuong ung voi adapter)
        recentlyViewedAdapter = new MovieItemAdapter(recentlyViewedMovieList);
        ratingAdapter = new MovieItemAdapter(ratingMovieList);
        watchListAdapter = new MovieItemAdapter(watchListMovieList);
        recentlyViewedAdapter.setOnItemClickListener(this);
        ratingAdapter.setOnItemClickListener(this);
        watchListAdapter.setOnItemClickListener(this);

        recycleView_Ratings_UserProfile.setAdapter(ratingAdapter);
        recycleView_RecentlyViewed_UserProfile.setAdapter(recentlyViewedAdapter);
        recycleView_Watchlist_UserProfile.setAdapter(watchListAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.item_spacing);

        recycleView_Ratings_UserProfile.setLayoutManager(layoutManager);
        recycleView_RecentlyViewed_UserProfile.setLayoutManager(layoutManager2);
        recycleView_Watchlist_UserProfile.setLayoutManager(layoutManager3);

        //Xử lý danh sách diễn viên
        favoriteActorsAdapter = new PersonAdapter((ArrayList<Person>) favoriteActorsList, UserProfileActivity.this);
        recycleView_FavoriteActors_UserProfile.setAdapter(favoriteActorsAdapter);
        recycleView_FavoriteActors_UserProfile.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


//        //ToDo: Xử lý khi Recently Viewed Movie có dữ liệu
//        if (!recentlyViewedMovieList.isEmpty()) {
//            textView_NoRecentlyViewed_UserProfile.setVisibility(View.GONE);
//            textView_RecentlyViewed_Cap_UserProfile.setVisibility(View.GONE);
//            recycleView_RecentlyViewed_UserProfile.setVisibility(View.VISIBLE);
//            textView_RecentlyViewed_Number_UserProfile.setVisibility(View.VISIBLE);
//            textView_SeeAll_RecentlyViewed_UserProfile.setVisibility(View.VISIBLE);
//            recycleView_RecentlyViewed_UserProfile.addItemDecoration(new SpacingItemDecoration(spacingInPixels));
//            textView_SeeAll_RecentlyViewed_UserProfile.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //ToDo: chuyển sang SeeAllActivity cho Recently Viewed
//                    Intent intent = new Intent(UserProfileActivity.this, SeeAllActivity.class);
//                    intent.putExtra("type", "movies");
//                    intent.putExtra("movieList", (Serializable) recentlyViewedMovieList);
//                    intent.putExtra("title", "Recently Viewed");
//                    startActivity(intent);
//                }
//            });
//            textView_RecentlyViewed_Number_UserProfile.setText(String.valueOf(recentlyViewedMovieList.size()));
//        }
//
//        //ToDo: Xử lý khi Rating Movie có dữ liệu
//        if (!ratingMovieList.isEmpty()) {
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
//                intent.putExtra("type", "movies");
//                intent.putExtra("movieList", (Serializable) ratingMovieList);
//                intent.putExtra("title", "Rating");
//                startActivity(intent);
//            }
//        });
//        textView_Ratings_Number_UserProfile.setText(String.valueOf(ratingMovieList.size()));
//        }
//
//        //ToDo: Xử lý khi Watchlist Movie có dữ liệu
//        if (!watchListMovieList.isEmpty()) {
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
//                intent.putExtra("type", "movies");
//                intent.putExtra("movieList", (Serializable) watchListMovieList);
//                intent.putExtra("title", "Watchlist");
//                startActivity(intent);
//            }
//        });
//        textView_Watchlist_Number_UserProfile.setText(String.valueOf(watchListMovieList.size()));
//        }
    }

    public void getRatingList() {
        userAPI.call_api_auth_get(userAPI.get_UserAPI() + "/rating/list", token, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        JSONArray jsonArray = json.getJSONArray("rating");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            String type_name = item.getString("type_name");
                            String _id = item.getString("_id");
                            ratingMovieList.add(getData(type_name, _id));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    runOnUiThread(() -> {
                        ratingAdapter.notifyDataSetChanged();
                        textView_Ratings_Number_UserProfile.setText(String.valueOf(ratingMovieList.size()));
                        textView_Ratings_UserProfile.setText(String.valueOf(ratingMovieList.size()));
                        Log.d("MainScreen0", String.valueOf(ratingMovieList.size()));

                        if (!ratingMovieList.isEmpty()) {
                            btn_Ratings_UserProfile.setVisibility(View.GONE);
                            imgView_Ratings_UserProfile.setVisibility(View.GONE);
                            textView_NoRatings_UserProfile.setVisibility(View.GONE);
                            recycleView_Ratings_UserProfile.setVisibility(View.VISIBLE);
                            textView_Ratings_Number_UserProfile.setVisibility(View.VISIBLE);
                            textView_SeeAll_Ratings_UserProfile.setVisibility(View.VISIBLE);
                            recycleView_Ratings_UserProfile.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_spacing)));
                            textView_SeeAll_Ratings_UserProfile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //ToDo: chuyển sang SeeAllActivity cho Rating
                                    Intent intent = new Intent(UserProfileActivity.this, SeeAllActivity.class);
                                    intent.putExtra("username", username);
                                    intent.putExtra("token", token);
                                    intent.putExtra("session_id", session_id);
                                    intent.putExtra("type", "movies");
                                    intent.putExtra("movieList", (Serializable) ratingMovieList);
                                    intent.putExtra("title", "Rating");
                                    startActivity(intent);
                                }
                            });
                            textView_Ratings_Number_UserProfile.setText(String.valueOf(ratingMovieList.size()));
                        } else {
                            fetchMovies();
                        }
                    });
                }
            }
        });
    }

    public void getWatchList() {
        userAPI.call_api_auth_get(userAPI.get_UserAPI() + "/watchlist/list", token, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        JSONArray jsonArray = json.getJSONArray("watchlist");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            String type_name = item.getString("type_name");
                            String _id = item.getString("_id");
                            watchListMovieList.add(getData(type_name, _id));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    runOnUiThread(() -> {
                        watchListAdapter.notifyDataSetChanged();
                        textView_Watchlist_Number_UserProfile.setText(String.valueOf(watchListMovieList.size()));
                        textView_Wishlist_UserProfile.setText(String.valueOf(watchListMovieList.size()));
                        Log.d("MainScreen0", String.valueOf(watchListMovieList.size()));

                        if (!watchListMovieList.isEmpty()) {
                            textView_Watchlist_Cap_UserProfile.setVisibility(View.GONE);
                            btn_Watchlist_UserProfile.setVisibility(View.GONE);
                            imgView_Watchlist_UserProfile.setVisibility(View.GONE);
                            textView_NoWatchlist_UserProfile.setVisibility(View.GONE);
                            recycleView_Watchlist_UserProfile.setVisibility(View.VISIBLE);
                            textView_Watchlist_Number_UserProfile.setVisibility(View.VISIBLE);
                            textView_SeeAll_Watchlist_UserProfile.setVisibility(View.VISIBLE);
                            recycleView_Watchlist_UserProfile.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_spacing)));
                            textView_SeeAll_Watchlist_UserProfile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //ToDo: chuyển sang SeeAllActivity cho Watchlist
                                    Intent intent = new Intent(UserProfileActivity.this, SeeAllActivity.class);
                                    intent.putExtra("username", username);
                                    intent.putExtra("token", token);
                                    intent.putExtra("session_id", session_id);
                                    intent.putExtra("type", "movies");
                                    intent.putExtra("movieList", (Serializable) watchListMovieList);
                                    intent.putExtra("title", "Watchlist");
                                    startActivity(intent);
                                }
                            });
                            textView_Watchlist_Number_UserProfile.setText(String.valueOf(watchListMovieList.size()));
                        } else {
                            fetchtopratedmovies();
                        }
                    });
                }
            }
        });
    }

    public void getRecentList() {
        userAPI.call_api_auth_get(userAPI.get_UserAPI() + "/recent/list", token, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    try {
                        JSONObject json = new JSONObject(response.body().string());
                        JSONArray jsonArray = json.getJSONArray("recent");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject item = jsonArray.getJSONObject(i);
                            String type_name = item.getString("type_name");
                            String _id = item.getString("_id");
                            recentlyViewedMovieList.add(getData(type_name, _id));
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    runOnUiThread(() -> {
                        recentlyViewedAdapter.notifyDataSetChanged();
                        textView_RecentlyViewed_Number_UserProfile.setText(String.valueOf(recentlyViewedMovieList.size()));
                        Log.d("MainScreen0", String.valueOf(recentlyViewedMovieList.size()));

                        if (!recentlyViewedMovieList.isEmpty()) {
                            textView_NoRecentlyViewed_UserProfile.setVisibility(View.GONE);
                            textView_RecentlyViewed_Cap_UserProfile.setVisibility(View.GONE);
                            recycleView_RecentlyViewed_UserProfile.setVisibility(View.VISIBLE);
                            textView_RecentlyViewed_Number_UserProfile.setVisibility(View.VISIBLE);
                            textView_SeeAll_RecentlyViewed_UserProfile.setVisibility(View.VISIBLE);
                            recycleView_RecentlyViewed_UserProfile.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_spacing)));
                            textView_SeeAll_RecentlyViewed_UserProfile.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //ToDo: chuyển sang SeeAllActivity cho Recently Viewed
                                    Intent intent = new Intent(UserProfileActivity.this, SeeAllActivity.class);
                                    intent.putExtra("username", username);
                                    intent.putExtra("token", token);
                                    intent.putExtra("session_id", session_id);
                                    intent.putExtra("type", "movies");
                                    intent.putExtra("movieList", (Serializable) recentlyViewedMovieList);
                                    intent.putExtra("title", "Recently Viewed");
                                    startActivity(intent);
                                }
                            });
                            textView_RecentlyViewed_Number_UserProfile.setText(String.valueOf(recentlyViewedMovieList.size()));
                        }
                    });
                }
            }
        });
    }

    public void onClick_SignIn_SignUp_UserProfile(View view) {
        Intent intent = new Intent(UserProfileActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void onClick_Ratings_UserProfile(View view) {
        //ToDo: Xử lý nút trong Ratings khi chưa có phim
        Intent intent = new Intent(UserProfileActivity.this, SeeAllActivity.class);
        intent.putExtra("type", "movies");
        intent.putExtra("movieList", (Serializable) moviesListInRatingList);
        intent.putExtra("title", "Fan favorites");
        startActivity(intent);
    }

    public void onClick_Watchlist_UserProfile(View view) {
        //ToDo: Xử lý nút trong Wishlist khi chưa có phim
        Intent intent = new Intent(UserProfileActivity.this, SeeAllActivity.class);
        intent.putExtra("type", "movies");
        intent.putExtra("movieList", (Serializable) topratedmovieslist);
        intent.putExtra("title", "Top rated movies");
        startActivity(intent);
    }

    private movies getData(String type_name, String _id) throws IOException, JSONException {
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
                long Revenue;
                String Backdrop_path;
                double Like;
                double Rating;
                String Release_date;
                if (type_name.equals("movie")) {
                    Title = jsonObject.getString("original_title");
                } else {
                    Title = jsonObject.getString("original_name");
                }
                id = jsonObject.getInt("id");
                Url_img = jsonObject.getString("poster_path");
                Revenue = jsonObject.optLong("revenue", 0);
                Backdrop_path = jsonObject.optString("backdrop_path", "");
                Rating = jsonObject.getDouble("vote_average");
                Release_date = jsonObject.optString("release_date", "");
                movie.set(new movies(id, Title, Revenue, "", Url_img, Backdrop_path, 0.0, Rating, Release_date));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return movie.get();
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
    private retrofit2.Call<MovieResponse> moviesCall;
    private List<movies> topratedmovieslist = new ArrayList<movies>();
    private List<movies> moviesListInRatingList = new ArrayList<movies>();

    private void fetchtopratedmovies(){
        TMDBApi api = RetrofitClient.getApiService();
        moviesCall = api.getTopRatedMovies(TMDB_API_KEY);
        moviesCall.enqueue(new retrofit2.Callback<MovieResponse>() {
            @Override
            public void onResponse(retrofit2.Call<MovieResponse> call, retrofit2.Response<MovieResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    topratedmovieslist.clear();
                    List<movies> results = response.body().getResults();

                    Log.d("MOVIES", "Fetched " + results.size() + " movies.");

                    topratedmovieslist.addAll(results);

//                    topratedadapter.notifyDataSetChanged();

                    // Gọi chi tiết từng phim để lấy runtime
                    for (movies movie : results) {
                        int movieId = movie.getMovieId();
                        retrofit2.Call<MovieDetail> detailCall = api.getMovieDetail(movieId, TMDB_API_KEY);
                        detailCall.enqueue(new retrofit2.Callback<MovieDetail>() {
                            @Override
                            public void onResponse(retrofit2.Call<MovieDetail> call, retrofit2.Response<MovieDetail> response) {
                                if (response.isSuccessful() && response.body() != null) {
                                    int runtime = response.body().getRuntime();
                                    movie.setLengthFromRuntime(runtime);

//                                    topratedadapter.notifyDataSetChanged();
                                }
                            }

                            @Override
                            public void onFailure(retrofit2.Call<MovieDetail> call, Throwable t) {
                                Log.e("DETAIL_ERROR", "Lỗi khi lấy runtime cho movieId: " + movieId, t);
                            }
                        });
                    }

                } else {
                    showError("Failed to load movies: " + response.message());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<MovieResponse> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi gọi API: " + t.getMessage(), t);
                showError("Lỗi: " + t.getMessage());
            }
        });
    }

    private void fetchMovies() {
        TMDBApi api = RetrofitClient.getApiService();
        moviesCall = api.getTrendingMovies(TMDB_API_KEY);

        moviesCall.enqueue(new retrofit2.Callback<MovieResponse>() {
            @Override
            public void onResponse(retrofit2.Call<MovieResponse> call, retrofit2.Response<MovieResponse> response) {
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
            public void onFailure(retrofit2.Call<MovieResponse> call, Throwable t) {
                Log.e("API_ERROR", "Lỗi gọi API: " + t.getMessage(), t);
                showError("Lỗi: " + t.getMessage());
            }
        });
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(movies movie) {
        Log.d("ClickedMovie", "Name: " + movie.getMoviename() + ", ID: " + movie.getMovieId());
        Intent intent = new Intent(this, TitleDetailActivity.class);
        intent.putExtra("itemType", "movie");
        intent.putExtra("itemId", movie.getMovieId());
        intent.putExtra("username", username);
        intent.putExtra("token", token);
        intent.putExtra("session_id", session_id);
        startActivity(intent);
    }

    @Override
    public void onPersonClick(Person person) {
        Intent intent = new Intent(this, PersonDetailActivity.class);
        intent.putExtra("personId", person.getPersonid());
        Log.d("username",  username);
        Log.d("token",  token);
        Log.d("session_id",  session_id);
        startActivity(intent);
    }

    public void showActorList() {
        if (!favoriteActorsList.isEmpty()) {
            textView_NoFavoriteActors_UserProfile.setVisibility(View.GONE);

            recycleView_FavoriteActors_UserProfile.setVisibility(View.VISIBLE);

            textView_FavoriteActors_Number_UserProfile.setVisibility(View.VISIBLE);

            textView_SeeAll_FavoriteActors_UserProfile.setVisibility(View.VISIBLE);

            recycleView_FavoriteActors_UserProfile.addItemDecoration(new SpacingItemDecoration(getResources().getDimensionPixelSize(R.dimen.item_spacing)));

            textView_SeeAll_FavoriteActors_UserProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //ToDo: chuyển sang SeeAllActivity cho Favorite Actors
                    Intent intent = new Intent(UserProfileActivity.this, SeeAllActivity.class);
                    intent.putExtra("type", "person");
                    intent.putExtra("title", "Born today");
                    intent.putExtra("personList",  (Serializable) favoriteActorsList);
                    if (favoriteActorsList != null) {
                        startActivity(intent);
                    }
                }
            });
            textView_FavoriteActors_Number_UserProfile.setText(String.valueOf(favoriteActorsList.size()));
        }
    }

    private void getActorList() {
        //ToDo: lấy danh sách diễn viên yêu thích của user
    }
}