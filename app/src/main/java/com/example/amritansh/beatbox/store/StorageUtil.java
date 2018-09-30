package com.example.amritansh.beatbox.store;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.amritansh.beatbox.models.Song;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class StorageUtil {
    private SharedPreferences preference;
    private Context context;
    private static final String SHARED_PREF = "shared_pref";
    private static final String NEXT_SONG = "next_song";
    private static final String PREV_SONG = "prev_song";
    private static final String CURRENT_SONG = "current_song";
    private static final String INDEX = "index";

    public StorageUtil(Context context){
        this.context = context;
    }

    public void storeCurrentSong(Song song){
        storeSong(song, CURRENT_SONG);
    }

    public void storeNextSong(Song song){
        storeSong(song, NEXT_SONG);
    }

    public void storePrevSong(Song song){
        storeSong(song, PREV_SONG);
    }

    public Song getCurrentSong(){
        Song song = getSong(CURRENT_SONG);
        return song;
    }

    public Song getNextSong(){
        Song song = getSong(NEXT_SONG);
        return song;
    }

    public Song getPrevSong(){
        Song song = getSong(PREV_SONG);
        return song;
    }

    public void clearPreferences(){
        preference = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.clear();
        editor.apply();
    }

    private void storeSong(Song song, String KEY){
        preference = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        Gson gson = new Gson();
        String json = gson.toJson(song);
        editor.putString(KEY, json);
        editor.apply();
    }

    private Song getSong(String KEY){
        preference = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = preference.getString(KEY, null);
        Type type = new TypeToken<Song>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public void storeCurrentSongIndex(int index){
        preference = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putInt(INDEX, index);
        editor.apply();
    }

    public int getStoreIndex(){
        preference = context.getSharedPreferences(SHARED_PREF, Context.MODE_PRIVATE);
        return preference.getInt(INDEX, -1);
    }
}
