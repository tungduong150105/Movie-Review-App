package com.example.moviereviewapp.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VideoResult implements Serializable {
    @SerializedName("iso_639_1")
    private String iso6391;

    @SerializedName("iso_3166_1")
    private String iso31661;

    @SerializedName("name")
    private String name;

    @SerializedName("key")
    private String key;

    @SerializedName("site")
    private String site;

    @SerializedName("size")
    private int size;

    @SerializedName("type")
    private String type;

    @SerializedName("official")
    private boolean official;

    @SerializedName("published_at")
    private String publishedAt;

    @SerializedName("id")
    private String id;

    public String getIso6391() { return iso6391; }
    public String getIso31661() { return iso31661; }
    public String getName() { return name; }
    public String getKey() { return key; }
    public String getSite() { return site; }
    public int getSize() { return size; }
    public String getType() { return type; }
    public boolean isOfficial() { return official; }
    public String getPublishedAt() { return publishedAt; }
    public String getId() { return id; }
}