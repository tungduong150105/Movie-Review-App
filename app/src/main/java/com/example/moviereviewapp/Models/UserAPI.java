package com.example.moviereviewapp.Models;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class UserAPI {
    public String get_UserAPI() {
        return "https://06f9-113-161-73-175.ngrok-free.app";
    }
//    public String get_UserAPI() {
//        return "http://10.0.2.2:3000";
//    }
    OkHttpClient client = new OkHttpClient();
    MediaType mediaType = MediaType.parse("application/json");
    public void call_api(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
    public void call_api_auth(String url, String token, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
