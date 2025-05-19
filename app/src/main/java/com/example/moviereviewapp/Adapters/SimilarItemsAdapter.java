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
import com.example.moviereviewapp.Models.SimilarItem;
import com.example.moviereviewapp.R;

import java.util.List;
import java.util.Locale;

public class SimilarItemsAdapter extends RecyclerView.Adapter<SimilarItemsAdapter.SimilarViewHolder> {
    private List<SimilarItem> items;
    private Context context;
    private final String TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(SimilarItem item);
    }

    public SimilarItemsAdapter(Context context, List<SimilarItem> items, OnItemClickListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SimilarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.movies, parent, false);
        return new SimilarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SimilarViewHolder holder, int position) {
        SimilarItem item = items.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    public void updateData(List<SimilarItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    class SimilarViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImageView;
        TextView titleTextView;
        TextView yearTextView;
        TextView ratingTextView;

        public SimilarViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.posterimg);
            titleTextView = itemView.findViewById(R.id.titleTxt);
            yearTextView = itemView.findViewById(R.id.yearTxt);
            try {
                ratingTextView = itemView.findViewById(R.id.ratingTxt);
            } catch (Exception e) {
                ratingTextView = null;
            }
        }

        void bind(final SimilarItem item, final OnItemClickListener listener) {
            if (item.getTitle() != null) {
                titleTextView.setText(item.getTitle());
            } else {
                titleTextView.setText("N/A");
            }
            String releaseDate = item.getReleaseDate();
            String year = "N/A";
            if (releaseDate != null && releaseDate.length() >= 4) {
                year = releaseDate.substring(0, 4);
            }
            yearTextView.setText(year);
            if (ratingTextView != null) {
                ratingTextView.setText(String.format(Locale.US, "%.1f", item.getVoteAverage()));
                ratingTextView.setVisibility(View.VISIBLE);
            }
            if (item.getPosterPath() != null) {
                Glide.with(context)
                        .load(TMDB_IMAGE_BASE_URL + item.getPosterPath())
                        .placeholder(R.drawable.reacherposter)
                        .error(R.drawable.reacherposter)
                        .into(posterImageView);
            } else {
                Glide.with(context)
                        .load(R.drawable.reacherposter)
                        .into(posterImageView);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(item);
                    }
                }
            });
        }
    }
}