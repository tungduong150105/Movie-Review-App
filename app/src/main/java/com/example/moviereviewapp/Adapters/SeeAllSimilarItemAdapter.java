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

        //TODO: Lấy trạng thái của watchlist của phim trong cơ sở dữ liệu và gán cho biến isInWatchlist

        //Xử lý trạng thái của nút "Bookmark"
        if (movie.getIsInWatchList()) {
            holder.alphaa.setAlpha(1f);
            holder.alphaa.setImageResource(R.drawable.yellow_bookmark);
            holder.iconImage.setImageResource(R.drawable.black_tick);
        } else {
            holder.alphaa.setAlpha(0.6f);
            holder.alphaa.setImageResource(R.drawable.black_small_bookmark);
            holder.iconImage.setImageResource(R.drawable.fill_plus_icon);
        }

        //Xử lý sự kiện khi nút "Bookmark" được nhấn
        holder.frameBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isBookmarked = movie.getIsInWatchList();
                if (!isBookmarked) {
                    //Nếu chưa có trong danh sách yêu thích thì thêm vào danh sách
                    holder.alphaa.setAlpha(1f);
                    holder.alphaa.setImageResource(R.drawable.yellow_bookmark);
                    holder.iconImage.setImageResource(R.drawable.black_tick);
                    //ToDo: Xử lý hành động khi nút "Bookmark" được nhấn trong SeeAll Activity
                    //ToDo: cập nhật trạng thái yêu thích của phim trong cơ sở dữ liệu
                    movie.setIsInWatchlist(true);

                } else {
                    //Nếu đã có trong danh sách yêu thích thì xóa khỏi danh sách
                    holder.alphaa.setAlpha(0.6f);
                    holder.alphaa.setImageResource(R.drawable.black_small_bookmark);
                    holder.iconImage.setImageResource(R.drawable.fill_plus_icon);
                    //ToDo: Xử lý hành động khi nút "Bookmark" bị bỏ chọn trong SeeAll Activity
                    //ToDo: cập nhật trạng thái yêu thích của phim trong cơ sở dữ liệu
                    movie.setIsInWatchlist(false);

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

