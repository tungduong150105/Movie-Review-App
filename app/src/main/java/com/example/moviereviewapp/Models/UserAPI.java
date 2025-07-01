package com.example.moviereviewapp.Models;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class UserAPI {
//    public String get_UserAPI() {
//        return "https://06f9-113-161-73-175.ngrok-free.app";
//    }
    public String get_UserAPI() {
//        return "https://9c3c-113-161-73-175.ngrok-free.app";
//        return "http://10.0.2.2:3000";
        return "https://7e55-14-169-36-254.ngrok-free.app";
    }
    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .build();
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
    public void call_api_patch(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .patch(body)
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
    public void call_api_auth_get(String url, String token, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
    public void call_api_auth_upd(String url, String token, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
    public void call_api_auth_del(String url, String token, String json, Callback callback) {
        RequestBody body = RequestBody.create(json, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .delete(body)
                .addHeader("Authorization", "Bearer " + token)
                .build();
        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
