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
import com.example.moviereviewapp.Models.Person;
import com.example.moviereviewapp.R;

import java.util.ArrayList;
import java.util.List;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {
    Context context;
    List<Person> persons;

    public PersonAdapter(ArrayList<Person> person) {
        this.persons = person;
    }

    @NonNull
    @Override
    public PersonAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.actorborntoday, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonAdapter.ViewHolder holder, int position) {
        Person person = persons.get(position);
        holder.titleTxt.setText(person.getName());
        holder.ageTxt.setText(person.getAgeOrLifespan());
        if (person.getCharacter() != null && !person.getCharacter().isEmpty()) {
            holder.ageTxt.setText(person.getCharacter());
            holder.frame.setVisibility(View.GONE);
        } else {
            holder.ageTxt.setText(person.getAgeOrLifespan());
            holder.frame.setVisibility(View.VISIBLE);
        }
        Glide.with(context).load(person.getProfileUrl()).into(holder.posterimg);
        holder.frame.setOnClickListener(new View.OnClickListener() {
            boolean isBookmarked = false;

            @Override
            public void onClick(View v) {
                isBookmarked = !isBookmarked;
                if (isBookmarked) {
                    holder.favourite.setImageResource(R.drawable.yellow_filled_heart);
                } else {
                    holder.favourite.setImageResource(R.drawable.white_heart_icon);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTxt, ageTxt;
        ImageView posterimg, background, favourite;
        FrameLayout frame;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            ageTxt = itemView.findViewById(R.id.yearTxt);
            posterimg = itemView.findViewById(R.id.posterimg);
            frame = itemView.findViewById(R.id.frame);
            favourite = itemView.findViewById(R.id.bookmark);
            background = itemView.findViewById(R.id.alphaa);
        }
    }

    public void setPersonList(List<Person> newPersonList) {
        this.persons = newPersonList;
        notifyDataSetChanged();
    }
}