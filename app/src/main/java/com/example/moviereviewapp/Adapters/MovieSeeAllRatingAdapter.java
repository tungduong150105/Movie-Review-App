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
import com.example.moviereviewapp.Models.movies;
import com.example.moviereviewapp.R;

import java.util.List;

public class MovieSeeAllRatingAdapter extends RecyclerView.Adapter<MovieSeeAllRatingAdapter.RatingMovieViewHolder> {
    private Context context;
    private List<movies> movieList;
    public MovieSeeAllRatingAdapter(Context context, List<movies> movieList) {
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
        movies movie = movieList.get(position);
        holder.title.setText(movie.getMoviename());
        holder.releaseDate.setText(movie.getReleasedate());
        holder.averageRating.setText(String.valueOf(movie.getRating()));
        holder.length.setText(movie.getLength());

        //ToDo: Xử lý hiển thị đánh giá của user
        //holder.userRating.setText(String.valueOf(movie.getUserrating()));
//        if (movie.getUserrating() == 0) {
//            holder.userRating.setVisibility(View.GONE);
//            holder.userRatingImg.setVisibility(View.GONE);
//        } else {
//            holder.userRating.setVisibility(View.VISIBLE);
//            holder.userRatingImg.setVisibility(View.VISIBLE);
//        }

        holder.frameBookmark.setOnClickListener(new View.OnClickListener() {
            boolean isBookmarked = false;

            @Override
            public void onClick(View v) {
                isBookmarked = !isBookmarked;

                if (isBookmarked) {
                    holder.alphaa.setAlpha(1f);
                    holder.alphaa.setImageResource(R.drawable.yellow_bookmark);
                    holder.iconImage.setImageResource(R.drawable.black_tick);
                    //ToDo: Xử lý hành động khi nút "Bookmark" được nhấn trong SeeAllRating Activity
                } else {
                    holder.alphaa.setAlpha(0.6f);
                    holder.alphaa.setImageResource(R.drawable.black_small_bookmark);
                    holder.iconImage.setImageResource(R.drawable.fill_plus_icon);
                    //ToDo: Xử lý hành động khi nút "Bookmark" bị bỏ chọn trong SeeAllRating Activity
                }
            }
        });

//        Glide.with(context)
//                .load(movie.getPosterurl())
//                .placeholder(R.drawable.placeholder)
//                .into(holder.imgAvatar);
        Glide.with(context).load(movie.getPosterurl()).into(holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class RatingMovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAvatar, iconImage, alphaa, userRatingImg;
        TextView title, releaseDate, averageRating, length, userRating;
        FrameLayout frameBookmark;
        public RatingMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgView_Avatar_SeeAll_Rating_Viewholder);
            title = itemView.findViewById(R.id.textView_Title_SeeAll_Rating_Viewholder);
            releaseDate = itemView.findViewById(R.id.textView_ReleaseDate_SeeAll_Rating_Viewholder);
            averageRating = itemView.findViewById(R.id.textView_AverageRating_SeeAll_Rating_Viewholder);
            length = itemView.findViewById(R.id.textView_Length_SeeAll_Rating_Viewholder);
            frameBookmark = itemView.findViewById(R.id.frame_SeeAllRating_Viewholder);
            iconImage = itemView.findViewById(R.id.bookmark_SeeAllRating_Viewholder);
            alphaa = itemView.findViewById(R.id.alphaa_SeeAllRating_Viewholder);
            userRating = itemView.findViewById(R.id.textView_UserRating_SeeAll_Rating_Viewholder);
            userRatingImg = itemView.findViewById(R.id.imageView_UserRating_SeeAll_Rating_Viewholder);
        }
    }
}
