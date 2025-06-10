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
import com.example.moviereviewapp.Models.Movie_UserProfile;
import com.example.moviereviewapp.Models.Person;
import com.example.moviereviewapp.Models.movies;
import com.example.moviereviewapp.Models.trendingall;
import com.example.moviereviewapp.R;

import java.util.List;

public class PersonSeeAllAdapter extends RecyclerView.Adapter<PersonSeeAllAdapter.MovieViewHolder>{
    private Context context;
    private List<Person> movieList;
    public PersonSeeAllAdapter(Context context, List<Person> movieList) {
        this.context = context;
        this.movieList = movieList;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.personseeall, parent, false);
        return new MovieViewHolder(view);
    }
    private PersonSeeAllAdapter.OnItemClickListener listener;

    // Setter cho listener
    public void setOnItemClickListener(PersonSeeAllAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    // Interface để xử lý click
    public interface OnItemClickListener {
        void onItemClick(Person person);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Person movie = movieList.get(position);
        holder.title.setText(movie.getName()+", ");
        holder.releaseDate.setText(movie.getRole());
        holder.length.setText(movie.getAgeOrLifespan());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(movie);
                }
            }
        });

        //ToDo: Lấy trạng thái yêu thích của diễn viên từ cơ sở dữ liệu và gán cho biến isFavorite

        //Xử lý hiển thị diễn viên yêu thích
        if (movie.getIsFavorite()) {
            holder.iconImage.setImageResource(R.drawable.yellow_filled_heart);
        } else {
            holder.iconImage.setImageResource(R.drawable.white_heart_icon);
        }

        //ToDo: Xử lý sự kiện click vào nút bookmark
        holder.frameBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isBookmarked = movie.getIsFavorite();
                if (!isBookmarked) {
                    //Nếu chưa có trong danh sách yêu thích thì thêm vào danh sách
                    holder.iconImage.setImageResource(R.drawable.yellow_filled_heart);
                    // TODO: Xử lý xóa diễn viên khỏi danh sách yêu thích trong cơ sở dữ liệu dưới đây
                    // TODO: cập nhật trạng thái yêu thích của diễn viên trong cơ sở dữ liệu
                    movie.setIsFavorite(true);

                } else {
                    //Nếu đã có trong danh sách yêu thích thì xóa khỏi danh sách
                    holder.iconImage.setImageResource(R.drawable.white_heart_icon);
                    // TODO: Xử lý thêm diễn viên vào danh sách yêu thích trong cơ sở dữ liệu dưới đây
                    // TODO: cập nhật trạng thái yêu thích của diễn viên trong cơ sở dữ liệu
                    movie.setIsFavorite(false);

                }
            }
        });
        Glide.with(context).load(movie.getProfileUrl()).into(holder.imgAvatar);
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
            frameBookmark = itemView.findViewById(R.id.frame);
            iconImage = itemView.findViewById(R.id.bookmark);
            alphaa = itemView.findViewById(R.id.alphaa);
        }
    }
}
