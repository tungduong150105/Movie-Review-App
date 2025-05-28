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

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<VideoResult> videoList;
    private Context context;
    private OnVideoClickListener onVideoClickListener;

    public interface OnVideoClickListener {
        void onVideoClick(VideoResult video);
    }

    public VideoAdapter(Context context, List<VideoResult> videoList, OnVideoClickListener listener) {
        this.context = context;
        this.videoList = videoList;
        this.onVideoClickListener = listener;
    }
    public List<VideoResult> getVideoList() {
        return videoList != null ? videoList : new ArrayList<>();
    }
    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoResult video = videoList.get(position);

        holder.videoTitleTextView.setText(video.getName());
        String thumbnailUrl = "https://img.youtube.com/vi/" + video.getKey() + "/mqdefault.jpg";

        Glide.with(context)
                .load(thumbnailUrl)
                .placeholder(R.drawable.rounded_image_background)
                .error(R.drawable.rounded_image_background)
                .into(holder.videoThumbnailImageView);

        holder.itemView.setOnClickListener(v -> {
            if (onVideoClickListener != null) {
                onVideoClickListener.onVideoClick(video);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList == null ? 0 : videoList.size();
    }

    public void updateVideos(List<VideoResult> newVideos) {
        this.videoList.clear();
        if (newVideos != null) {
            this.videoList.addAll(newVideos);
        }
        notifyDataSetChanged();
    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {
        ImageView videoThumbnailImageView;
        TextView videoTitleTextView;

        VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            videoThumbnailImageView = itemView.findViewById(R.id.videoThumbnailImageView);
            videoTitleTextView = itemView.findViewById(R.id.videoTitleTextView);
        }
    }
}