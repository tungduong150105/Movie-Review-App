package com.example.moviereviewapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviereviewapp.Models.Movie_UserProfile;
import com.example.moviereviewapp.R;

import java.util.List;

public class MovieUserProfileAdapter extends RecyclerView.Adapter<MovieUserProfileAdapter.MovieViewHolder> {

    private Context context;
    private List<Movie_UserProfile> movieList;

    public MovieUserProfileAdapter(Context context, List<Movie_UserProfile> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie_UserProfile movie = movieList.get(position);

        holder.title.setText(movie.getTitle());
        holder.releaseDate.setText(movie.getReleaseDate());
        holder.yourRating.setText(String.valueOf(movie.getYourRating()));
        holder.averageRating.setText(String.valueOf(movie.getAverageRating()));

        // Load ảnh bằng Glide hoặc Picasso
        Glide.with(context)
                .load(movie.getImgURl())
                .placeholder(R.drawable.placeholder)
                .into(holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatar;
        TextView title, releaseDate, yourRating, averageRating;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgView_Avatar_Viewholder);
            title = itemView.findViewById(R.id.textView_MovieTitle_Viewholder);
            releaseDate = itemView.findViewById(R.id.textView_ReleaseDate_Viewholder);
            yourRating = itemView.findViewById(R.id.textView_YourRating_ViewHolder);
            averageRating = itemView.findViewById(R.id.textView_AverageRating_ViewHolder);
        }
    }
}
