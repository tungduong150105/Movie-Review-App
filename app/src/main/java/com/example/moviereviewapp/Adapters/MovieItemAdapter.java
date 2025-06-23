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


public class MovieItemAdapter extends RecyclerView.Adapter<MovieItemAdapter.ViewHolder> {
    public interface OnRefreshListener {
        void onRefresh();
    }
    private  List<movies> movie;
    Context context;
    private OnRefreshListener refreshListener;
    UserAPI userAPI = new UserAPI();
    public String token;

    public MovieItemAdapter(List<movies> movie, String token) {
        this.movie = movie;
        this.token = token;
        this.refreshListener = refreshListener;
    }
    public MovieItemAdapter(List<movies> movie, String token, OnRefreshListener refreshListener) {
        this.movie = movie;
        this.token = token;
        this.refreshListener = refreshListener;
    }
    private MovieItemAdapter.OnItemClickListener listener;

    // Setter cho listener
    public void setOnItemClickListener(MovieItemAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    // Interface để xử lý click
    public interface OnItemClickListener {
        void onItemClick(movies movie);
    }

    // Constructor



    @NonNull
    @Override
    public MovieItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieItemAdapter.ViewHolder holder, int position) {
        movies move = movie.get(position);
        holder.titleTxt.setText(move.getMoviename());
        holder.ratingTxt.setText(String.valueOf(move.getRating()));
        holder.yearTxt.setText(move.getReleasedate());
        holder.lengthTxt.setText(move.getLength());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(move);
                }
            }
        });
        Glide.with(context).load(move.getPosterurl()).into(holder.posterimg);

        //ToDo: Lấy trạng thái watchlist của phim từ cơ sở dữ liệu và gán cho biến isInWatchList

        //Xử lý hiển thị phim đã thêm vào watchlist hay chưa
        if (move.getIsInWatchList()) {
            holder.alphaa.setAlpha(1f);
            holder.alphaa.setImageResource(R.drawable.yellow_bookmark);
            holder.iconImage.setImageResource(R.drawable.black_tick);
        } else {
            holder.alphaa.setAlpha(0.6f);
            holder.alphaa.setImageResource(R.drawable.black_small_bookmark);
            holder.iconImage.setImageResource(R.drawable.fill_plus_icon);
        }

        //ToDo: Xử lý sự kiện click vào nút bookmark
        holder.frameBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isBookmarked = move.getIsInWatchList();
                JSONObject json = new JSONObject();
                try {
                    json.put("_id", move.getMovieId());
                    json.put("type_name", "movie");
                    json.put("name", move.getMoviename());
                    json.put("img_url", move.getPosterurl());
                    json.put("release_day", move.getReleasedate());
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
                if (isBookmarked) {
                    //Nếu đã có trong watchlist thì xóa khỏi watchlist
                    holder.alphaa.setAlpha(0.6f);
                    holder.alphaa.setImageResource(R.drawable.black_small_bookmark);
                    holder.iconImage.setImageResource(R.drawable.fill_plus_icon);
                    // TODO: Xử lý xóa phim khỏi watchlist trong cơ sở dữ liệu dưới đây
                    // TODO: cập nhật trạng thái watchlist của phim trong cơ sở dữ liệu
                    move.setIsInWatchlist(false);
                    userAPI.call_api_auth_del(userAPI.get_UserAPI() + "/watchlist/delete", token, json.toString(), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            refreshListener.onRefresh();
                        }
                    });
                } else {
                    //Nếu chưa có trong watchlist thì thêm vào watchlist
                    holder.alphaa.setAlpha(1f);
                    holder.alphaa.setImageResource(R.drawable.yellow_bookmark);
                    holder.iconImage.setImageResource(R.drawable.black_tick);
                    // TODO: Xử lý thêm phim vào watchlist trong cơ sở dữ liệu dưới đây
                    // TODO: cập nhật trạng thái watchlist của phim trong cơ sở dữ liệu
                    move.setIsInWatchlist(true);
                    userAPI.call_api_auth(userAPI.get_UserAPI() + "/watchlist/add", token, json.toString(), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            refreshListener.onRefresh();
                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return movie.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView posterimg, iconImage,alphaa;
        FrameLayout frameBookmark;
        TextView titleTxt, ratingTxt, yearTxt, lengthTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            posterimg = itemView.findViewById(R.id.posterimg);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            ratingTxt = itemView.findViewById(R.id.ratingTxt);
            yearTxt = itemView.findViewById(R.id.yearTxt);
            lengthTxt = itemView.findViewById(R.id.lengthTxt);
            frameBookmark = itemView.findViewById(R.id.frame);
            iconImage = itemView.findViewById(R.id.bookmark);
            alphaa=itemView.findViewById(R.id.alphaa);
        }
    }
}
