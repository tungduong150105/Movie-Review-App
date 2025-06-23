package com.example.moviereviewapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviereviewapp.Models.Movie_UserProfile;
import com.example.moviereviewapp.Models.UserAPI;
import com.example.moviereviewapp.Models.movies;
import com.example.moviereviewapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieSeeAllAdapter extends RecyclerView.Adapter<MovieSeeAllAdapter.MovieViewHolder>{
    String token;
    private Context context;
    private List<movies> movieList;
    public MovieSeeAllAdapter(Context context, List<movies> movieList, String token) {
        this.context = context;
        this.movieList = movieList;
        this.token = token;
    }
    private MovieSeeAllAdapter.OnItemClickListener listener;

    // Setter cho listener
    public void setOnItemClickListener(MovieSeeAllAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    // Interface để xử lý click
    public interface OnItemClickListener {
        void onItemClick(movies movie);
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_viewall_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        movies movie = movieList.get(position);
        holder.title.setText(movie.getMoviename());
        holder.releaseDate.setText(movie.getReleasedate());
        holder.length.setText(movie.getLength());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(movie);
                }
            }
        });

        //TODO: Lấy trạng thái watchlist của phim từ cơ sở dữ liệu và gán cho biến isInWatchList

        //Xử lý hiển thị trạng thái watchlist
        if (movie.getIsInWatchList()) {
            holder.alphaa.setAlpha(1f);
            holder.alphaa.setImageResource(R.drawable.yellow_bookmark);
            holder.iconImage.setImageResource(R.drawable.black_tick);
        } else {
            holder.alphaa.setAlpha(0.6f);
            holder.alphaa.setImageResource(R.drawable.black_small_bookmark);
            holder.iconImage.setImageResource(R.drawable.fill_plus_icon);
        }
        UserAPI userAPI = new UserAPI();

        //TODO: Xử lý sự kiện click cho nút "Bookmark"
        holder.frameBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isBookmarked = movie.getIsInWatchList();
                JSONObject json = new JSONObject();
                try {
                    json.put("_id", movie.getMovieId());
                    json.put("type_name", "movie");
                    json.put("name", movie.getMoviename());
                    json.put("img_url", movie.getPosterurl());
                    json.put("release_day", movie.getReleasedate());
                    json.put("rating", movie.getRating());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                userAPI.call_api_auth(userAPI.get_UserAPI() + "/movieinfo/add", token, json.toString(), new Callback() {
                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    }

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    }
                });
                if (!isBookmarked) {
                    //Nếu chưa có trong watchlist, thêm vào
                    holder.alphaa.setAlpha(1f);
                    holder.alphaa.setImageResource(R.drawable.yellow_bookmark);
                    holder.iconImage.setImageResource(R.drawable.black_tick);
                    //ToDo: Xử lý hành động khi nút "Bookmark" được nhấn trong SeeAll Activity
                    //TODO: Cập nhật trạng thái watchlist của phim vào cơ sở dữ liệu
                    movie.setIsInWatchlist(true);
                    userAPI.call_api_auth(userAPI.get_UserAPI() + "/watchlist/add", token, json.toString(), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        }
                    });
                } else {
                    //Nếu đã có trong watchlist, xóa khỏi
                    holder.alphaa.setAlpha(0.6f);
                    holder.alphaa.setImageResource(R.drawable.black_small_bookmark);
                    holder.iconImage.setImageResource(R.drawable.fill_plus_icon);
                    //ToDo: Xử lý hành động khi nút "Bookmark" bị bỏ chọn trong SeeAll Activity
                    //TODO: Cập nhật trạng thái watchlist của phim vào cơ sở dữ liệu
                    movie.setIsInWatchlist(false);
                    userAPI.call_api_auth_del(userAPI.get_UserAPI() + "/watchlist/delete", token, json.toString(), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        }
                    });
                }
            }
        });
        Glide.with(context).load(movie.getPosterurl()).into(holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatar, iconImage, alphaa;
        TextView title, releaseDate, length;
        FrameLayout frameBookmark;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgView_Avatar_SeeAll_Viewholder);
            title = itemView.findViewById(R.id.textView_Title_SeeAll_Viewholder);
            releaseDate = itemView.findViewById(R.id.textView_ReleaseDate_SeeAll_Viewholder);
            length = itemView.findViewById(R.id.textView_Length_SeeAll_Viewholder);
            frameBookmark = itemView.findViewById(R.id.frame_SeeAll_Viewholder);
            iconImage = itemView.findViewById(R.id.bookmark_SeeAll_Viewholder);
            alphaa = itemView.findViewById(R.id.alphaa_SeeAll_Viewholder);
        }
    }
}
