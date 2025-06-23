package com.example.moviereviewapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviereviewapp.Models.UserAPI;
import com.example.moviereviewapp.Models.movies;
import com.example.moviereviewapp.Models.trendingall;
import com.example.moviereviewapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class movietrendingadapter extends RecyclerView.Adapter<movietrendingadapter.ViewHolder> {
    public interface OnRefreshListener {
        void onRefresh();
    }
    private OnRefreshListener onRefreshListener;
    private List<trendingall> movie;
    private Context context;

    // Khai báo listener
    private OnItemClickListener listener;
    public String token;
    UserAPI userAPI = new UserAPI();
    // Setter cho listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Interface để xử lý click
    public interface OnItemClickListener {
        void onItemClick(trendingall movie);
    }

    // Constructor
    public movietrendingadapter(List<trendingall> movie, String token, OnRefreshListener onRefreshListener) {
        this.movie = movie != null ? movie : new ArrayList<>();
        this.token = token;
        this.onRefreshListener = onRefreshListener;
    }
    public movietrendingadapter(List<trendingall> moviesList, movietrendingadapter.OnItemClickListener listener, String token) {
        this.movie = moviesList != null ? moviesList : new ArrayList<>();
        this.token = token;
        this.listener = listener;
    }
    public movietrendingadapter(List<trendingall> moviesList, movietrendingadapter.OnItemClickListener listener, OnRefreshListener onRefreshListener, String token) {
        this.movie = moviesList != null ? moviesList : new ArrayList<>();
        this.token = token;
        this.listener = listener;
        this.onRefreshListener = onRefreshListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movies, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        trendingall move = movie.get(position);

        holder.titleTxt.setText(move.getName());
        holder.ratingTxt.setText(String.valueOf(move.getRating()));
        holder.yearTxt.setText(move.getDate());
        holder.lengthTxt.setText(move.getLength());

        String imageUrl = "https://image.tmdb.org/t/p/w500" + move.getPosterid();
        Glide.with(context).load(imageUrl).into(holder.posterimg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(move);
                }
            }
        });

        //TODO: Lấy trạng thái của watchlist của phim trong cơ sở dữ liệu và gán cho biến isInWatchlist

        //Xử lý trạng thái của nút "Bookmark"
        if (move.getIsInWatchList()) {
            holder.alphaa.setAlpha(1f);
            holder.alphaa.setImageResource(R.drawable.yellow_bookmark);
            holder.iconImage.setImageResource(R.drawable.black_tick);
        } else {
            holder.alphaa.setAlpha(0.6f);
            holder.alphaa.setImageResource(R.drawable.black_small_bookmark);
            holder.iconImage.setImageResource(R.drawable.fill_plus_icon);
        }

        // Bookmark toggle
        //TODO: Xử lý sự kiện khi nút "Bookmark" được nhấn
        holder.frameBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isBookmarked = move.getIsInWatchList();
                JSONObject json = new JSONObject();
                try {
                    json.put("_id", move.getId());
                    json.put("type_name", move.getType());
                    json.put("name", move.getName());
                    json.put("img_url", move.getPosterid());
                    json.put("release_day", move.getDate());
                    json.put("rating", move.getRating());
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
                    //Nếu chưa có trong watchlist thì thêm vào danh sách
                    holder.alphaa.setAlpha(1f);
                    holder.alphaa.setImageResource(R.drawable.yellow_bookmark);
                    holder.iconImage.setImageResource(R.drawable.black_tick);
                    //ToDo: Xử lý hành động khi nút "Bookmark" được nhấn trong SeeAll Activity
                    //ToDo: cập nhật trạng thái yêu thích của phim trong cơ sở dữ liệu
                    move.setIsInWatchList(true);
                    userAPI.call_api_auth(userAPI.get_UserAPI() + "/watchlist/add", token, json.toString(), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            onRefreshListener.onRefresh();
                        }
                    });
                } else {
                    //Nếu đã có trong danh sách yêu thích thì xóa khỏi danh sách
                    holder.alphaa.setAlpha(0.6f);
                    holder.alphaa.setImageResource(R.drawable.black_small_bookmark);
                    holder.iconImage.setImageResource(R.drawable.fill_plus_icon);
                    //ToDo: Xử lý hành động khi nút "Bookmark" bị bỏ chọn trong SeeAll Activity
                    //ToDo: cập nhật trạng thái yêu thích của phim trong cơ sở dữ liệu
                    move.setIsInWatchList(false);
                    userAPI.call_api_auth_del(userAPI.get_UserAPI() + "/watchlist/delete", token, json.toString(), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            onRefreshListener.onRefresh();
                        }
                    });
                }
            }
        });
        // Xử lý click toàn bộ item
    }

    @Override
    public int getItemCount() {
        return movie.size();
    }

    // ViewHolder
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView posterimg, iconImage, alphaa;
        TextView titleTxt, ratingTxt, yearTxt, lengthTxt;
        FrameLayout frameBookmark;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            posterimg = itemView.findViewById(R.id.posterimg);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            ratingTxt = itemView.findViewById(R.id.ratingTxt);
            yearTxt = itemView.findViewById(R.id.yearTxt);
            lengthTxt = itemView.findViewById(R.id.lengthTxt);
            frameBookmark = itemView.findViewById(R.id.frame);
            iconImage = itemView.findViewById(R.id.bookmark);
            alphaa = itemView.findViewById(R.id.alphaa);
        }
    }
}
