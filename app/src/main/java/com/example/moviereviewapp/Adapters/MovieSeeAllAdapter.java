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

public class MovieSeeAllAdapter extends RecyclerView.Adapter<MovieSeeAllAdapter.MovieViewHolder>{
    private Context context;
    private List<Movie_UserProfile> movieList;
    public MovieSeeAllAdapter(Context context, List<Movie_UserProfile> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_viewall_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie_UserProfile movie = movieList.get(position);
        holder.title.setText(movie.getTitle());
        holder.releaseDate.setText(movie.getReleaseDate());

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
        TextView title, releaseDate;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgView_Avatar_SeeAll_Viewholder);
            title = itemView.findViewById(R.id.textView_Title_SeeAll_Viewholder);
            releaseDate = itemView.findViewById(R.id.textView_ReleaseDate_SeeAll_Viewholder);
        }
    }
}
