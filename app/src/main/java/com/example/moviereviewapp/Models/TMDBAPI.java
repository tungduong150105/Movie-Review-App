package com.example.moviereviewapp.Models;

import android.util.Log;

import java.io.IOException;
import java.net.URI;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import java.net.URI;
import java.util.concurrent.TimeUnit;

public class TMDBAPI {
    public String get_url_add_rating(String itemType, String movie_id, String session_id) {
        if (itemType.equals("movie")) {
            return "https://api.themoviedb.org/3/movie/" + movie_id + "/rating?api_key=fed0e4b63e1ef5ed6a678cd279a00884&guest_session_id=" + session_id;
        } else {
            return "https://api.themoviedb.org/3/tv" + movie_id + "/rating?api_key=fed0e4b63e1ef5ed6a678cd279a00884&guest_session_id=" + session_id;
        }
    }
    public String get_url_new_session() {
        return "https://api.themoviedb.org/3/authentication/guest_session/new";
    }

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build();
    MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
    public void post_api(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("accept", "application/json")
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
    public void get_api(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmZWQwZTRiNjNlMWVmNWVkNmE2NzhjZDI3OWEwMDg4NCIsIm5iZiI6MTc0MzA0MjA2MC45NTcsInN1YiI6IjY3ZTRiNjBjNDIxZWI4YzMzMWJhMmQ1NyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.JsgAgcRx5Sib0D4SvfmnuwUEMWPV6hPv2winrt86vk0")
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
