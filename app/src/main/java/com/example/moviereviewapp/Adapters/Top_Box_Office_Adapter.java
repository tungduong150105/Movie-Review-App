package com.example.moviereviewapp.Adapters;



import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.moviereviewapp.Models.movies;
import com.example.moviereviewapp.R;

import java.util.ArrayList;

public class  Top_Box_Office_Adapter extends ArrayAdapter<movies> {

    public Top_Box_Office_Adapter(@NonNull Context context, ArrayList<movies> movie) {
        super(context, 0, movie);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.movieboxoffice, parent, false);
        }

        movies movie = getItem(position);

        TextView tvRank = convertView.findViewById(R.id.numberTxt);
        TextView tvTitle = convertView.findViewById(R.id.movienameTxt);
        TextView tvRevenue = convertView.findViewById(R.id.boxofficeTxt);
        ImageView bookmark = convertView.findViewById(R.id.imageView15);

        if (movie != null) {
            tvRank.setText(String.valueOf(position + 1));
            tvTitle.setText(movie.getMoviename() != null ? movie.getMoviename() : "No Title");

            // Bắt đầu marquee bằng cách chọn TextView
            tvTitle.setSelected(true);  // 👈 Bắt buộc để chạy marquee

            long revenue = movie.getRevenue();
            double revenueInMillions = revenue / 1_000_000.0;
            String formattedRevenue = String.format("%.1f M", revenueInMillions);
            tvRevenue.setText("$" + formattedRevenue);
            ViewGroup.LayoutParams layoutParams = bookmark.getLayoutParams();
            float density = getContext().getResources().getDisplayMetrics().density;
            layoutParams.width = (int) (60 * density);
            layoutParams.height = (int) (60 * density);
            bookmark.setLayoutParams(layoutParams);

            //ToDo: Lấy trạng thái yêu thích của diễn viên từ cơ sở dữ liệu và gán cho biến isInWatchList

            //Xử lý hiển thị diễn viên yêu thích
            if (movie.getIsInWatchList()) {
                int px15 = (int) (15 * density);  // Convert 15dp to px
                bookmark.setPadding(px15, px15, px15, px15);
                bookmark.setImageResource(R.drawable.black_tick);
                layoutParams = bookmark.getLayoutParams();
                layoutParams.width = (int) (40 * density);
                layoutParams.height = (int) (40 * density);
                bookmark.setLayoutParams(layoutParams);
                bookmark.setBackgroundResource(R.drawable.yellow_bookmark);
            } else {
                bookmark.setPadding(0, 0, 0, 0);
                bookmark.setImageResource(R.drawable.bookmarkreal);
                layoutParams = bookmark.getLayoutParams();
                layoutParams.width = (int) (60 * density);
                layoutParams.height = (int) (60 * density);
                bookmark.setLayoutParams(layoutParams);
                bookmark.setBackgroundResource(0);
            }

            //ToDo: Xử lý sự kiện click vào nút bookmark
            bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isBookmarked = movie.getIsInWatchList();
                    float density = getContext().getResources().getDisplayMetrics().density;
                    int px15 = (int) (15 * density);  // Convert 15dp to px
                    int px25 = (int) (25 * density);  // Convert 25dp to px

                    if (!isBookmarked) {
                        //Nếu chưa có trong danh sách yêu thích thì thêm vào danh sách
                        bookmark.setPadding(px15, px15, px15, px15);
                        bookmark.setImageResource(R.drawable.black_tick);
                        ViewGroup.LayoutParams layoutParams = bookmark.getLayoutParams();
                        layoutParams.width = (int) (40 * density);
                        layoutParams.height = (int) (40 * density);
                        bookmark.setLayoutParams(layoutParams);
                        bookmark.setBackgroundResource(R.drawable.yellow_bookmark);
                        //ToDo: Xử lý hành động khi nút "Bookmark" được nhấn trong SeeAll Activity
                        // TODO: cập nhật trạng thái yêu thích của movies trong cơ sở dữ liệu
                        movie.setIsInWatchlist(true);

                    } else {
                        //Nếu đã có trong danh sách yêu thích thì xóa khỏi danh sách
                        bookmark.setPadding(0, 0, 0, 0);
                        bookmark.setImageResource(R.drawable.bookmarkreal);
                        ViewGroup.LayoutParams layoutParams = bookmark.getLayoutParams();
                        layoutParams.width = (int) (60 * density);
                        layoutParams.height = (int) (60 * density);
                        bookmark.setLayoutParams(layoutParams);
                        bookmark.setBackgroundResource(0);
                        //ToDo: Xử lý hành động khi nút "Bookmark" bị bỏ chọn trong SeeAll Activity
                        // TODO: cập nhật trạng thái yêu thích của movies trong cơ sở dữ liệu
                        movie.setIsInWatchlist(false);

                    }
                }
            });
        }
        return convertView;
    }
}
