package com.example.moviereviewapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.moviereviewapp.Models.SearchMovieModel;
import com.example.moviereviewapp.R;

import java.util.List;

public class SearchMovieAdapter extends ArrayAdapter<SearchMovieModel> {
    private final Context mContext;
    private final int mResource;
    public SearchMovieAdapter(@NonNull Context context, int resource, @NonNull List<SearchMovieModel> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(mResource, null);
        }

        SearchMovieModel p = getItem(position);

        if (p != null) {
            ImageView image = v.findViewById(R.id.imageMovie);
            TextView title = v.findViewById(R.id.textMovieName);
            TextView releaseDate = v.findViewById(R.id.textReleaseDate);
            TextView actors = v.findViewById(R.id.textActors);
            if (image != null) {
                Glide.with(mContext).load("https://image.tmdb.org/t/p/w500" + p.getImageUrl()).into(image);
            }
            if (title != null) {
                title.setText(p.getTitle());
            }
            if (releaseDate != null) {
                releaseDate.setText(p.getReleaseDate());
            }
            if (actors != null) {
                actors.setText(p.getActors());
            }
        }

        return v;
    }
}
