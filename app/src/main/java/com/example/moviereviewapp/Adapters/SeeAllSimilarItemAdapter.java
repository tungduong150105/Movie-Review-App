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
import com.example.moviereviewapp.Models.SimilarItem;
import com.example.moviereviewapp.R;

import java.util.List;

public class SeeAllSimilarItemAdapter extends RecyclerView.Adapter<SeeAllSimilarItemAdapter.ViewHolder> {
    private Context context;
    private List<SimilarItem> movieList;
    public SeeAllSimilarItemAdapter(Context context, List<SimilarItem> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public SeeAllSimilarItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_viewall_rating_movie, parent, false);
        return new SeeAllSimilarItemAdapter.ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull SeeAllSimilarItemAdapter.ViewHolder holder, int position) {
        SimilarItem movie = movieList.get(position);
        holder.title.setText(movie.getTitle());
        holder.releaseDate.setText(movie.getReleaseDate());
        holder.length.setText(movie.getLength());
        holder.average.setText(movie.getVoteAverage()+"");

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

        Glide.with(context).load(movie.getPosterPath()).into(holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvatar, iconImage, alphaa;
        TextView title, releaseDate, length,average;
        FrameLayout frameBookmark;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.imgView_Avatar_SeeAll_Rating_Viewholder);
            title = itemView.findViewById(R.id.textView_Title_SeeAll_Rating_Viewholder);
            average=itemView.findViewById(R.id.textView_AverageRating_SeeAll_Rating_Viewholder);
            releaseDate = itemView.findViewById(R.id.textView_ReleaseDate_SeeAll_Rating_Viewholder);
            length = itemView.findViewById(R.id.textView_Length_SeeAll_Rating_Viewholder);
            frameBookmark = itemView.findViewById(R.id.frame_SeeAllRating_Viewholder);
            iconImage = itemView.findViewById(R.id.bookmark_SeeAllRating_Viewholder);
            alphaa = itemView.findViewById(R.id.alphaa_SeeAllRating_Viewholder);
        }
    }
}

