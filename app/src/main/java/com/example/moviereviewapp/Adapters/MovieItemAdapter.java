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
import com.example.moviereviewapp.Models.movies;
import com.example.moviereviewapp.Models.trendingall;
import com.example.moviereviewapp.R;

import java.util.ArrayList;
import java.util.List;




public class MovieItemAdapter extends RecyclerView.Adapter<MovieItemAdapter.ViewHolder> {
    private  List<movies> movie;
    Context context;

    public MovieItemAdapter(List<movies> movie) {
        this.movie = movie;
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
