package com.example.moviereviewapp.Adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviereviewapp.Models.ChatMessage;
import com.example.moviereviewapp.R;

import java.util.List;

public class DiscussAdapter extends RecyclerView.Adapter<MessageHolder> {
    String username;
    List<ChatMessage> chatMessageList;
    public DiscussAdapter(String username, List<ChatMessage> chatMessageList) {
        this.username = username;
        this.chatMessageList = chatMessageList;
    }
    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_sent_message, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_received_message, parent, false);
        }
        return new MessageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            TextView sentMessage = holder.itemView.findViewById(R.id.sentMessage);
            sentMessage.setText(chatMessageList.get(position).getMessage());
            TextView userName = holder.itemView.findViewById(R.id.userName);
            userName.setText(chatMessageList.get(position).getName());
        } else {
            TextView receivedMessage = holder.itemView.findViewById(R.id.receivedMessage);
            receivedMessage.setText(chatMessageList.get(position).getMessage());
            TextView sentName = holder.itemView.findViewById(R.id.sentName);
            sentName.setText(chatMessageList.get(position).getName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (chatMessageList.get(position).getName().equals(username)) {
            return 0;
        }
        return 1;
    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<ChatMessage> chatMessageList) {
        this.chatMessageList = chatMessageList;
        notifyDataSetChanged();
    }
}
