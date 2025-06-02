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
import com.example.moviereviewapp.Models.movies;
import com.example.moviereviewapp.Models.trendingall;
import com.example.moviereviewapp.R;

import java.util.ArrayList;
import java.util.List;

public class movietrendingadapter extends RecyclerView.Adapter<movietrendingadapter.ViewHolder> {

    private List<trendingall> movie;
    private Context context;

    // Khai báo listener
    private OnItemClickListener listener;

    // Setter cho listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Interface để xử lý click
    public interface OnItemClickListener {
        void onItemClick(trendingall movie);
    }

    // Constructor
    public movietrendingadapter(List<trendingall> movie) {
        this.movie = movie != null ? movie : new ArrayList<>();
    }
    public movietrendingadapter(List<trendingall> moviesList, movietrendingadapter.OnItemClickListener listener) {
        this.movie = moviesList != null ? moviesList : new ArrayList<>();
        this.listener = listener;
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
        // Bookmark toggle
        holder.frameBookmark.setOnClickListener(new View.OnClickListener() {
            boolean isBookmarked = false;

            @Override
            public void onClick(View v) {
                isBookmarked = !isBookmarked;
                if (isBookmarked) {
                    holder.alphaa.setAlpha(1f);
                    holder.alphaa.setImageResource(R.drawable.yellow_bookmark);
                    holder.iconImage.setImageResource(R.drawable.black_tick);
                } else {
                    holder.alphaa.setAlpha(0.6f);
                    holder.alphaa.setImageResource(R.drawable.black_small_bookmark);
                    holder.iconImage.setImageResource(R.drawable.fill_plus_icon);
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
