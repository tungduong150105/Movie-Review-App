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

public class MovieSeeAllRatingAdapter extends RecyclerView.Adapter<MovieSeeAllRatingAdapter.RatingMovieViewHolder> {
    private Context context;
    private List<Movie_UserProfile> movieList;
    public MovieSeeAllRatingAdapter(Context context, List<Movie_UserProfile> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public RatingMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_viewall_rating_movie, parent, false);
        return new RatingMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieSeeAllRatingAdapter.RatingMovieViewHolder holder, int position) {
        Movie_UserProfile movie = movieList.get(position);
        holder.title.setText(movie.getTitle());
        holder.releaseDate.setText(movie.getReleaseDate());
        holder.averageRating.setText(String.valueOf(movie.getAverageRating()));
        holder.yourRating.setText(String.valueOf(movie.getAverageRating()));

        Glide.with(context)
                .load(movie.getImgURl())
                .placeholder(R.drawable.placeholder)
                .into(holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class RatingMovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar;
        TextView title, releaseDate, averageRating, yourRating;
        public RatingMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgView_Avatar_SeeAll_Rating_Viewholder);
            title = itemView.findViewById(R.id.textView_Title_SeeAll_Rating_Viewholder);
            releaseDate = itemView.findViewById(R.id.textView_ReleaseDate_SeeAll_Rating_Viewholder);
            averageRating = itemView.findViewById(R.id.textView_AverageRating_SeeAll_Rating_Viewholder);
            yourRating = itemView.findViewById(R.id.textView_YourRating_SeeAll_Rating_Viewholder);
        }
    }
}
