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
import com.example.moviereviewapp.Models.UserAPI;
import com.example.moviereviewapp.Models.trendingall;
import com.example.moviereviewapp.Models.tvseries;
import com.example.moviereviewapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class tvseriesadapter extends RecyclerView.Adapter<tvseriesadapter.ViewHolder> {
    public interface OnRefreshListener {
        void onRefresh();
    }
    private OnRefreshListener onRefreshListener;
    List<tvseries> tv;
    Context context;
    public String token;
    UserAPI userAPI = new UserAPI();
    public tvseriesadapter(List<tvseries> tv, String token, OnRefreshListener onRefreshListener) {
        this.tv = tv;
        this.token = token;
        this.onRefreshListener = onRefreshListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies, parent, false);
        return new ViewHolder(inflate);
    }
    private tvseriesadapter.OnItemClickListener listener;

    // Setter cho listener
    public void setOnItemClickListener(tvseriesadapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    // Interface để xử lý click
    public interface OnItemClickListener {
        void onItemClick(tvseries movie);
    }

    @Override
    public void onBindViewHolder(@NonNull tvseriesadapter.ViewHolder holder, int position) {
        tvseries move = tv.get(position);
        holder.titleTxt.setText(move.getName());
        holder.ratingTxt.setText(String.valueOf(move.getRating()));
        holder.yearTxt.setText(move.getDate());
        holder.lengthTxt.setText(move.getEps());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(move);
                }
            }
        });
        Glide.with(context).load(move.getPosterid()).into(holder.posterimg);

        //ToDo: Lấy trạng thái yêu thích của diễn viên từ cơ sở dữ liệu và gán cho biến isFavorite

        //Xử lý hiển thị tvseries yêu thích
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
                JSONObject json = new JSONObject();
                try {
                    json.put("_id", move.getId());
                    json.put("type_name", "tv");
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
                boolean isBookmarked = move.getIsInWatchList();
                if (!isBookmarked) {
                    //Nếu chưa có trong danh sách yêu thích thì thêm vào danh sách
                    holder.alphaa.setAlpha(1f);
                    holder.alphaa.setImageResource(R.drawable.yellow_bookmark);
                    holder.iconImage.setImageResource(R.drawable.black_tick);
                    // TODO: Xử lý thêm diễn viên vào danh sách yêu thích trong cơ sở dữ liệu dưới đây
                    // TODO: cập nhật trạng thái yêu thích của diễn viên trong cơ sở dữ liệu
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
                    // TODO: Xử lý xóa diễn viên khỏi danh sách yêu thích trong cơ sở dữ liệu dưới đây
                    // TODO: cập nhật trạng thái yêu thích của diễn viên trong cơ sở dữ liệu
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
    }

    @Override
    public int getItemCount() {
        return tv.size();
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
