package com.example.amritansh.beatbox.models;

import android.graphics.Bitmap;

public class Song {

    private String title, songUri, artist;
    private String thumbUrl;

    public Song(String title, String songUri, String thumbUrl, String artist){
        this.title = title;
        this.songUri = songUri;
        this.thumbUrl = thumbUrl;
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public String getSongUri() {
        return songUri;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getartist() {
        return artist;
    }
}
