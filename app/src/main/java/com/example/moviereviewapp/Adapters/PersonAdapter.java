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
import com.example.moviereviewapp.Models.UserAPI;
import com.example.moviereviewapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {
    public interface OnRefreshListener {
        void onRefreshPerson();
    }
    private OnRefreshListener onRefreshListener;
    Context context;
    List<Person> persons;
    private OnPersonClickListener personClickListener;
    String token;
    UserAPI userAPI = new UserAPI();
    
    // Interface for person click events
    public interface OnPersonClickListener {
        void onPersonClick(Person person);
    }

    public PersonAdapter(ArrayList<Person> person) {
        this.persons = person;
    }
    
    public PersonAdapter(ArrayList<Person> person, OnPersonClickListener listener, String token, OnRefreshListener onRefreshListener) {
        this.persons = person;
        this.personClickListener = listener;
        this.token = token;
        this.onRefreshListener = onRefreshListener;
    }

    public PersonAdapter(ArrayList<Person> person, OnPersonClickListener listener, String token) {
        this.persons = person;
        this.personClickListener = listener;
        this.token = token;
    }
    
    public PersonAdapter(Context context, ArrayList<Person> person) {
        this.context = context;
        this.persons = person;
    }
    
    public PersonAdapter(Context context, ArrayList<Person> person, OnPersonClickListener listener) {
        this.context = context;
        this.persons = person;
        this.personClickListener = listener;
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

        //ToDo: Lấy trạng thái yêu thích của diễn viên từ cơ sở dữ liệu và gán cho biến isFavorite

        //Xử lý hiển thị diễn viên yêu thích
        if (person.getIsFavorite()) {
            holder.favourite.setImageResource(R.drawable.yellow_filled_heart);
        } else {
            holder.favourite.setImageResource(R.drawable.white_heart_icon);
        }

        //ToDo: Xử lý sự kiện click vào nút bookmark
        holder.frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isBookmarked = person.getIsFavorite();
                JSONObject json = new JSONObject();
                try {
                    json.put("person_id", person.getPersonid());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                if (isBookmarked) {
                    //Nếu đã có trong danh sách yêu thích thì xóa khỏi danh sách
                    holder.favourite.setImageResource(R.drawable.white_heart_icon);
                    // TODO: Xử lý xóa diễn viên khỏi danh sách yêu thích trong cơ sở dữ liệu dưới đây
                    // TODO: cập nhật trạng thái yêu thích của diễn viên trong cơ sở dữ liệu
                    person.setIsFavorite(false);
                    userAPI.call_api_auth_del(userAPI.get_UserAPI() + "/person/delete", token, json.toString(), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            onRefreshListener.onRefreshPerson();
                        }
                    });
                } else {
                    //Nếu chưa có trong danh sách yêu thích thì thêm vào danh sách
                    holder.favourite.setImageResource(R.drawable.yellow_filled_heart);
                    // TODO: Xử lý thêm diễn viên vào danh sách yêu thích trong cơ sở dữ liệu dưới đây
                    // TODO: cập nhật trạng thái yêu thích của diễn viên trong cơ sở dữ liệu
                    person.setIsFavorite(true);
                    userAPI.call_api_auth(userAPI.get_UserAPI() + "/person/add", token, json.toString(), new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            onRefreshListener.onRefreshPerson();
                        }
                    });
                }
            }
        });

        holder.itemView.setOnClickListener(v -> {
            if (personClickListener != null) {
                personClickListener.onPersonClick(person);
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

    public List<Person> getPersonList() {
        return persons;
    }
}