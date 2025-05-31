package com.example.moviereviewapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.moviereviewapp.Models.PhotoItem;
import com.example.moviereviewapp.R;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {
    private List<PhotoItem> photoList;
    private OnPhotoClickListener listener;

    public interface OnPhotoClickListener {
        void onPhotoClick(String photoUrl);
    }

    public PhotoAdapter(List<PhotoItem> photoList) {
        this.photoList = photoList;
    }

    public PhotoAdapter(List<PhotoItem> photoList, OnPhotoClickListener listener) {
        this.photoList = photoList;
        this.listener = listener;
    }
    public List<PhotoItem> getPhotoList() {
        return photoList;
    }
    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        PhotoItem photoItem = photoList.get(position);
        String imageUrl = photoItem.getImageUrl();
        double aspectRatio = photoItem.getAspectRatio();
        int fixedHeight = holder.itemView.getLayoutParams().height;
        int calculatedWidth = (int) (fixedHeight * aspectRatio);
        ViewGroup.LayoutParams layoutParams = holder.photoImageView.getLayoutParams();
        layoutParams.width = calculatedWidth;
        holder.photoImageView.setLayoutParams(layoutParams);
        Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.rounded_image_background)
                .error(R.drawable.rounded_image_background)
                .into(holder.photoImageView);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onPhotoClick(imageUrl);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public void setPhotoList(List<PhotoItem> photoList) {
        this.photoList = photoList;
        notifyDataSetChanged();
    }

    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImageView;

        PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImageView = itemView.findViewById(R.id.photoImageView);
        }
    }
}