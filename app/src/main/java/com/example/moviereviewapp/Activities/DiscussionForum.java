package com.example.moviereviewapp.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviereviewapp.Adapters.BaseActivity;
import com.example.moviereviewapp.Adapters.DiscussAdapter;
import com.example.moviereviewapp.Models.ChatMessage;
import com.example.moviereviewapp.Models.UserAPI;
import com.example.moviereviewapp.R;
import com.example.moviereviewapp.Socket.EmptyClient;

import org.java_websocket.client.WebSocketClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;


public class DiscussionForum extends AppCompatActivity {
    static List<ChatMessage> chatMessageList;
    static DiscussAdapter adapter;
    EditText inputMessage;
    UserAPI userAPI;
    private OkHttpClient client;
    private String token;
    RecyclerView recyclerView;
    WebSocket webSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_discussion_forum);
        int movie_id;
        String movie_name;
        String username = "";
        token = "";
        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            movie_id = extra.getInt("movie_id", -1);
            movie_name = extra.getString("movie_name");
            username = extra.getString("username");
            token = extra.getString("token");
        } else {
            movie_id = 0;
            movie_name = "";
        }

        userAPI = new UserAPI();

        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(v -> {
            disconnect();
            finish();
        });

        TextView groupName = findViewById(R.id.groupName);
        groupName.setText(movie_name);

        chatMessageList = new ArrayList<>();

        recyclerView = findViewById(R.id.discussionForum);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DiscussAdapter(username, chatMessageList);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                                                   @Override
                                                   public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                                                       if (bottom < oldBottom) {
                                                           recyclerView.postDelayed(new Runnable() {
                                                               @Override
                                                               public void run() {
                                                                   if (chatMessageList.size() > 0) {
                                                                       recyclerView.smoothScrollToPosition(chatMessageList.size() - 1);
                                                                   }
                                                               }
                                                           }, 100);
                                                       }
                                                   }
                                               }
        );

        inputMessage = findViewById(R.id.inputMessage);

        ImageView send = findViewById(R.id.send);
        send.setOnClickListener(v -> {
            String message = inputMessage.getText().toString();
            if (message.isEmpty()) {
                return;
            }
            sendMessage(String.valueOf(movie_id), message);
            inputMessage.setText("");
            inputMessage.clearFocus();
            adapter.setData(chatMessageList);
        });

        client = new OkHttpClient();

        getMessages(String.valueOf(movie_id));
    }

    private void getMessages(String movie_id) {
        JSONObject getMessagesBody = new JSONObject();
        try {
            getMessagesBody.put("movie_id", String.valueOf(movie_id));
            userAPI.call_api_auth(userAPI.get_UserAPI() + "/message/find", token, getMessagesBody.toString(), new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.d("DiscussionForum", "Failed to get messages");
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    if (response.code() == 200) {
                        try {
                            assert response.body() != null;
                            JSONObject json = new JSONObject(response.body().string());
                            JSONArray arr = json.getJSONArray("messages");
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject message = arr.getJSONObject(i);
                                String username = message.getString("username");
                                String messageText = message.getString("message");
                                chatMessageList.add(new ChatMessage(username, messageText));
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    adapter.setData(chatMessageList);
                                    recyclerView.scrollToPosition(chatMessageList.size() - 1);
                                }
                            });
                            connect();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendMessage(String movie_id, String message) {
        JSONObject json = new JSONObject();
        try {
            json.put("movie_id", movie_id);
            json.put("message", message);
            userAPI.call_api_auth(userAPI.get_UserAPI() + "/message/add", token, json.toString(), new Callback() {
                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                }

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    Log.e("DiscussionForum", "Failed to send message", e);
                }
            });
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private class WebSocketClient extends WebSocketListener {
        private static final int NORMAL_CLOSURE_STATUS = 1000;

        @Override
        public void onOpen(WebSocket webSocket, @NonNull Response response) {
            String message = "{\"command\":\"subscribe\",\"identifier\":\"{\\\"channel\\\":\\\"MessagesChannel\\\"}\"}";
            webSocket.send(message);
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
            Log.d("WebSocket", "Received text: " + text);
            if (!text.contains("{\"type\":\"ping\"") && !text.contains("{\"type\":\"welcome\"")) {
                try {
                    JSONObject json = new JSONObject(text);
                    if (json.has("message")) {
                        JSONObject full_message = json.getJSONObject("message");
                        JSONObject message = full_message.getJSONObject("Message");
                        String messageText = message.getString("message");
                        String username = message.getString("username");
                        chatMessageList.add(new ChatMessage(username, messageText));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.setData(chatMessageList);
                                recyclerView.scrollToPosition(chatMessageList.size() - 1);
                            }
                        });
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        @Override
        public void onMessage(@NonNull WebSocket webSocket, ByteString bytes) {
            Log.d("WebSocket", "Received bytes: " + bytes.hex());
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, @NonNull String reason) {
            webSocket.close(NORMAL_CLOSURE_STATUS, null);
            Log.d("WebSocket", "Closing: " + code + "/" + reason);
        }

        @Override
        public void onFailure(@NonNull WebSocket webSocket, Throwable t, Response response) {
            Log.d("WebSocket", "Error: " + t.getMessage());
        }
    }

    private void connect() {
        Request request = new Request.Builder()
                .url("https://9c3c-113-161-73-175.ngrok-free.app/cable")
//                .url("ws://10.0.2.2:3000/cable")
                .addHeader("Authorization", "Bearer " + token)
                .build();
        WebSocketClient listener = new WebSocketClient();
        webSocket = client.newWebSocket(request, listener);

    }

    private void disconnect() {
        webSocket.close(1000, null);
        client.dispatcher().executorService().shutdown();
    }
}