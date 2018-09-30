package com.example.amritansh.beatbox.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

public class PlaySongService extends Service implements MediaPlayer.OnCompletionListener {

    private final MusicBinder musicBinder = new MusicBinder();
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String songUri = intent.getStringExtra("music");

        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(songUri);
            mediaPlayer.prepare();
            playAudio();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return START_STICKY;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Log.d("console log", "songcompleted");
        mediaPlayer.seekTo(0);
        playAudio();
    }

    public class MusicBinder extends Binder {
        public PlaySongService getService() {
            return PlaySongService.this;
        }
    }

    public IBinder onBind(Intent intent) {
        return musicBinder;
    }

    public void playAudio() {
        mediaPlayer.start();
    }

    public int getCurrentPosition() {
        return mediaPlayer.getCurrentPosition();
    }

    public int getDuration() {
        return mediaPlayer.getDuration();
    }

    public void seekMediaPlayer(int progress) {
        mediaPlayer.seekTo(progress);
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void pauseAudio() {
        mediaPlayer.pause();
    }


    @Override
    public void onDestroy() {
        mediaPlayer.release();
        mediaPlayer = null;
        super.onDestroy();
    }
}
