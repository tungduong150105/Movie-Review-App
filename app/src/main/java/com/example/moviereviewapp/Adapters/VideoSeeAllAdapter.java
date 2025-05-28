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
import com.example.moviereviewapp.Models.VideoResult;
import com.example.moviereviewapp.R;

import java.util.ArrayList;
import java.util.List;

public class VideoSeeAllAdapter extends RecyclerView.Adapter<VideoSeeAllAdapter.VideoViewHolder> {
    private final Context context;
    private final List<VideoResult> videoList;
    private final OnVideoClickListener clickListener;

    public interface OnVideoClickListener {
        void onVideoClick(String videoId);
    }

    public VideoSeeAllAdapter(Context context, List<VideoResult> videoList, OnVideoClickListener clickListener) {
        this.context = context != null ? context : null;
        this.videoList = videoList != null ? videoList : new ArrayList<>();
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video_list, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoResult video = videoList.get(position);
        
        // Set video title
        holder.titleTextView.setText(video.getName());
        
        // Set video type as subtitle
        if (video.getType() != null && !video.getType().isEmpty()) {
            holder.subtitleTextView.setText(video.getType());
            holder.subtitleTextView.setVisibility(View.VISIBLE);
        } else {
            holder.subtitleTextView.setVisibility(View.GONE);
        }

        String thumbnailUrl = "https://img.youtube.com/vi/" + video.getKey() + "/mqdefault.jpg";
        Glide.with(context)
                .load(thumbnailUrl)
                .placeholder(R.drawable.rounded_image_background)
                .error(R.drawable.rounded_image_background)
                .into(holder.thumbnailImageView);

        holder.playIconImageView.setVisibility(View.VISIBLE);
        holder.itemView.setOnClickListener(v -> {
            if (clickListener != null) {
                clickListener.onVideoClick(video.getKey());
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;
        ImageView playIconImageView;
        TextView titleTextView;
        TextView subtitleTextView;

        VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.videoThumbnailImageView);
            playIconImageView = itemView.findViewById(R.id.playIconImageView);
            titleTextView = itemView.findViewById(R.id.videoTitleTextView);
            subtitleTextView = itemView.findViewById(R.id.videoSubtitleTextView);
        }
    }
}