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
import com.example.moviereviewapp.Models.UserAPI;
import com.example.moviereviewapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class SimilarItemsAdapter extends RecyclerView.Adapter<SimilarItemsAdapter.SimilarViewHolder> {
    private List<SimilarItem> items;
    private Context context;
    private final String TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
    private OnItemClickListener listener;
    private boolean isPersonDetailContext = false;

    public interface OnItemClickListener {
        void onItemClick(SimilarItem item);
    }

    public interface OnRefreshListener {
        void onRefresh();
    }
    private OnRefreshListener onRefreshListener;
    String token;

    public SimilarItemsAdapter(Context context, List<SimilarItem> items, OnItemClickListener listener, boolean isPersonDetailContext) {
        this.context = context;
        this.items = items;
        this.listener = listener;
        this.isPersonDetailContext = isPersonDetailContext;
    }
    public SimilarItemsAdapter(Context context, List<SimilarItem> items, OnItemClickListener listener, boolean isPersonDetailContext, OnRefreshListener onRefreshListener, String token) {
        this.context = context;
        this.items = items;
        this.listener = listener;
        this.isPersonDetailContext = isPersonDetailContext;
        this.onRefreshListener = onRefreshListener;
        this.token = token;
    }

    public SimilarItemsAdapter(Context context, List<SimilarItem> items, OnItemClickListener listener) {
        this(context, items, listener, false);
    }

    public List<SimilarItem> getItems() {
        return items;
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

        // In person detail, show character name instead of length
        if (isPersonDetailContext && item.getCharacter() != null && !item.getCharacter().isEmpty()) {
            holder.lengthTextView.setText(item.getCharacter());
            holder.lengthTextView.setVisibility(View.VISIBLE);
        } else if (item.getLength() != null && !item.getLength().isEmpty()) {
            holder.lengthTextView.setText(item.getLength());
            holder.lengthTextView.setVisibility(View.VISIBLE);
        } else {
            // If no length available
            holder.lengthTextView.setVisibility(View.GONE);
        }
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
        TextView lengthTextView;
        ImageView iconImage, alphaa;
        FrameLayout frameBookmark;

        public SimilarViewHolder(@NonNull View itemView) {
            super(itemView);
            posterImageView = itemView.findViewById(R.id.posterimg);
            titleTextView = itemView.findViewById(R.id.titleTxt);
            yearTextView = itemView.findViewById(R.id.yearTxt);
            lengthTextView = itemView.findViewById(R.id.lengthTxt);
            try {
                ratingTextView = itemView.findViewById(R.id.ratingTxt);
            } catch (Exception e) {
                ratingTextView = null;
            }
            frameBookmark = itemView.findViewById(R.id.frame);
            iconImage = itemView.findViewById(R.id.bookmark);
            alphaa = itemView.findViewById(R.id.alphaa);
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

            //TODO: Lấy trạng thái của phim trong cơ sở dữ liệu và gán cho biến isInWatchList

            //Xử lý hiển thị phim trong watchlist
            if (item.getIsInWatchList()) {
                alphaa.setAlpha(1f);
                alphaa.setImageResource(R.drawable.yellow_bookmark);
                iconImage.setImageResource(R.drawable.black_tick);
            } else {
                alphaa.setAlpha(0.6f);
                alphaa.setImageResource(R.drawable.black_small_bookmark);
                iconImage.setImageResource(R.drawable.fill_plus_icon);
            }

            UserAPI userAPI = new UserAPI();

            //ToDo: Xử lý sự kiện click vào nút bookmark
            frameBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isBookmarked = item.getIsInWatchList();
                    JSONObject json = new JSONObject();
                    try {
                        json.put("_id", item.getId());
                        json.put("type_name", item.getTitle() != null ? "movie" : "tv");
                        json.put("name", item.getOriginalTitle());
                        json.put("img_url", item.getPosterPath());
                        json.put("release_day", item.getReleaseDate());
                        json.put("rating", item.getVoteAverage());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    userAPI.call_api_auth(userAPI.get_UserAPI() + "/movieinfo/add", token, json.toString(), new Callback() {
                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        }

                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        }
                    });
                    if (!isBookmarked) {
                        //Nếu chưa có trong danh sách yêu thích thì thêm vào danh sách
                        alphaa.setAlpha(1f);
                        alphaa.setImageResource(R.drawable.yellow_bookmark);
                        iconImage.setImageResource(R.drawable.black_tick);
                        // ToDo: Xử lý hành động khi nút "Bookmark" được nhấn trong SeeAll Activity
                        // TODO: cập nhật trạng thái watchlist của movies trong cơ sở dữ liệu
                        item.setIsInWatchlist(true);
                        userAPI.call_api_auth(userAPI.get_UserAPI() + "/watchlist/add", token, json.toString(), new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if (onRefreshListener != null) {
                                    onRefreshListener.onRefresh();
                                }
                            }
                        });
                    } else {
                        //Nếu đã có trong danh sách yêu thích thì xóa khỏi danh sách
                        alphaa.setAlpha(0.6f);
                        alphaa.setImageResource(R.drawable.black_small_bookmark);
                        iconImage.setImageResource(R.drawable.fill_plus_icon);
                        // ToDo: Xử lý hành động khi nút "Bookmark" bị bỏ chọn trong SeeAll Activity
                        // TODO: cập nhật trạng thái watchlist của movies trong cơ sở dữ liệu
                        item.setIsInWatchlist(false);
                        userAPI.call_api_auth_del(userAPI.get_UserAPI() + "/watchlist/delete", token, json.toString(), new Callback() {
                            @Override
                            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            }

                            @Override
                            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                                if (onRefreshListener != null) {
                                    onRefreshListener.onRefresh();
                                }
                            }
                        });
                    }
                }
            });

        }
    }
}