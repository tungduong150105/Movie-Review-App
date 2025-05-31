package com.example.moviereviewapp.Adapters;

import static androidx.core.graphics.RectKt.transform;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.moviereviewapp.Models.movies;
import com.example.moviereviewapp.R;

import java.util.ArrayList;
import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<movies> moviesList;
    Context context;

    public interface OnItemClickListener {
        void onItemClick(movies movie);
    }

    private OnItemClickListener listener;

    public MovieAdapter(List<movies> moviesList, OnItemClickListener listener) {
        this.moviesList = moviesList != null ? moviesList : new ArrayList<>();
        this.listener = listener;
    }
    public List<movies> getMoviesList() {
        return moviesList;
    }

    public void setMoviesList(List<movies> movies) {
        this.moviesList = movies;
        notifyDataSetChanged();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MovieViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        movies movie = moviesList.get(position);

        holder.titletxt.setText(movie.getMoviename());
        holder.liketxt.setText(String.valueOf(movie.getLike()));
        holder.trailerlinetxt.setText(movie.getTrailertext());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(movie);
                }
            }
        });
        holder.frameBookmark.setOnClickListener(new View.OnClickListener() {
            boolean isBookmarked = false;

            @Override
            public void onClick(View v) {
                isBookmarked = !isBookmarked;

                if (isBookmarked) {
                    Log.d("Watchlist", movie.getMovieId() + "");
                    holder.alphaa.setAlpha(1f);
                    holder.alphaa.setImageResource(R.drawable.yellow_bookmark);
                    holder.bookmark.setImageResource(R.drawable.black_tick);
                } else {
                    holder.alphaa.setAlpha(0.6f);
                    holder.alphaa.setImageResource(R.drawable.black_small_bookmark);
                    holder.bookmark.setImageResource(R.drawable.fill_plus_icon);
                }
            }
        });


        // Trong onBindViewHolder
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                holder.trailerlinetxt.setSelected(true);
            }
        }, 2000);

        Glide.with(context).load(movie.getPosterurl()).into(holder.posterurl);
        Glide.with(context).load(movie.getBackdropurl()).into(holder.backimageurl);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView posterurl, backimageurl, bookmark, alphaa;
        TextView titletxt, liketxt, trailerlinetxt;

        FrameLayout frameBookmark;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            posterurl = itemView.findViewById(R.id.posterurl);
            backimageurl = itemView.findViewById(R.id.backlinkurl);
            titletxt = itemView.findViewById(R.id.titleTxt);
            liketxt = itemView.findViewById(R.id.likeTxt);
            trailerlinetxt = itemView.findViewById(R.id.trailerlineTxt);
            bookmark = itemView.findViewById(R.id.bookmark);
            alphaa = itemView.findViewById(R.id.alphaa);
            frameBookmark = itemView.findViewById(R.id.frame);

        }
    }
}
