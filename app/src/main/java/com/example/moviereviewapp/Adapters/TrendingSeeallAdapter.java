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

import java.util.List;

public class TrendingSeeallAdapter extends RecyclerView.Adapter<TrendingSeeallAdapter.MovieViewHolder>{
    private Context context;
    private List<trendingall> movieList;
    public TrendingSeeallAdapter(Context context, List<trendingall> movieList) {
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
        trendingall movie = movieList.get(position);
        holder.title.setText(movie.getName());
        holder.releaseDate.setText(movie.getDate());
        holder.length.setText(movie.getLength());

        holder.frameBookmark.setOnClickListener(new View.OnClickListener() {
            boolean isBookmarked = false;

            @Override
            public void onClick(View v) {
                isBookmarked = !isBookmarked;

                if (isBookmarked) {
                    holder.alphaa.setAlpha(1f);
                    holder.alphaa.setImageResource(R.drawable.yellow_bookmark);
                    holder.iconImage.setImageResource(R.drawable.black_tick);
                    //ToDo: Xử lý hành động khi nút "Bookmark" được nhấn trong SeeAll Activity
                } else {
                    holder.alphaa.setAlpha(0.6f);
                    holder.alphaa.setImageResource(R.drawable.black_small_bookmark);
                    holder.iconImage.setImageResource(R.drawable.fill_plus_icon);
                    //ToDo: Xử lý hành động khi nút "Bookmark" bị bỏ chọn trong SeeAll Activity
                }
            }
        });
        String imageUrl = "https://image.tmdb.org/t/p/w500" + movie.getPosterid();


        Glide.with(context).load(imageUrl).into(holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatar, iconImage, alphaa;
        TextView title, releaseDate, length;
        FrameLayout frameBookmark;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgView_Avatar_SeeAll_Viewholder);
            title = itemView.findViewById(R.id.textView_Title_SeeAll_Viewholder);
            releaseDate = itemView.findViewById(R.id.textView_ReleaseDate_SeeAll_Viewholder);
            length = itemView.findViewById(R.id.textView_Length_SeeAll_Viewholder);
            frameBookmark = itemView.findViewById(R.id.frame_SeeAll_Viewholder);
            iconImage = itemView.findViewById(R.id.bookmark_SeeAll_Viewholder);
            alphaa = itemView.findViewById(R.id.alphaa_SeeAll_Viewholder);
        }
    }
}
