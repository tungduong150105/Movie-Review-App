package com.example.moviereviewapp.Adapters;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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

        //ToDo: Lấy trạng thái watchlist của phim từ cơ sở dữ liệu và gán cho biến isInWatchList

        //Xử lý hiển thị phim đã thêm vào watchlist hay chưa
        if (movie.getIsInWatchList()) {
            holder.alphaa.setAlpha(1f);
            holder.alphaa.setImageResource(R.drawable.yellow_bookmark);
            holder.bookmark.setImageResource(R.drawable.black_tick);
        } else {
            holder.alphaa.setAlpha(0.6f);
            holder.alphaa.setImageResource(R.drawable.black_small_bookmark);
            holder.bookmark.setImageResource(R.drawable.fill_plus_icon);
        }

        //ToDo: Xử lý sự kiện click vào nút bookmark
        holder.frameBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isBookmarked = movie.getIsInWatchList();
                if (!isBookmarked) {
                    //Nếu chưa thì thêm vô watchlist
                    Log.d("Watchlist", movie.getMovieId() + "");
                    holder.alphaa.setAlpha(1f);
                    holder.alphaa.setImageResource(R.drawable.yellow_bookmark);
                    holder.bookmark.setImageResource(R.drawable.black_tick);
                    // TODO: Xử lý thêm phim vào watchlist trong cơ sở dữ liệu dưới đây
                    // TODO: cập nhật trạng thái watchlist của phim trong cơ sở dữ liệu
                    movie.setIsInWatchlist(true);

                } else {
                    //Nếu đã có trong watchlist thì xóa khỏi watchlist
                    holder.alphaa.setAlpha(0.6f);
                    holder.alphaa.setImageResource(R.drawable.black_small_bookmark);
                    holder.bookmark.setImageResource(R.drawable.fill_plus_icon);
                    // TODO: Xử lý xóa phim khỏi watchlist trong cơ sở dữ liệu dưới đây
                    // TODO: cập nhật trạng thái watchlist của phim trong cơ sở dữ liệu
                    movie.setIsInWatchlist(false);

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
