package com.example.amritansh.beatbox.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import com.example.amritansh.beatbox.fragments.SongsListFragment;
import com.example.amritansh.beatbox.store.StorageUtil;

import java.io.IOException;

public class PlaySongService extends Service implements MediaPlayer.OnCompletionListener {

    private static final String NOTIFICATION_CHANNEL_ID = "101";
    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "ACTION_NEXT";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String MEDIA_SESSION_TAG = "media_session";

    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    private final MusicBinder musicBinder = new MusicBinder();
    private MediaPlayer mediaPlayer;
    private StorageUtil storageUtil;

    @Override
    public void onCreate() {
        super.onCreate();

        storageUtil = new StorageUtil(getApplicationContext());
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        String songUri = intent.getStringExtra("music");

            if (mediaSessionManager == null){
//                initMediaSession();
            }
            initMediaPlayer();

        return START_STICKY;
    }

    private void initMediaPlayer(){
        try {
            String songUri = storageUtil.getCurrentSong().getSongUri();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(songUri);
            mediaPlayer.prepare();
            playAudio();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void playNextSong(){
        storageUtil.storePrevSong(storageUtil.getCurrentSong());
        storageUtil.storeCurrentSong(storageUtil.getNextSong());

        int index = storageUtil.getStoreIndex();
        int newCurrentIndex = index+1;
        if (index == SongsListFragment.songsList.size() - 1){
            newCurrentIndex = 0;
        }
        int nextSongIndex = newCurrentIndex + 1;
        if (newCurrentIndex == SongsListFragment.songsList.size() - 1){
            nextSongIndex = 0;
        }
        storageUtil.storeCurrentSongIndex(newCurrentIndex);
        storageUtil.storeNextSong(SongsListFragment.songsList.get(nextSongIndex));
        initMediaPlayer();
    }

    public void playPrevSong(){
        storageUtil.storeNextSong(storageUtil.getCurrentSong());
        storageUtil.storeCurrentSong(storageUtil.getPrevSong());

        int index = storageUtil.getStoreIndex();
        int newCurrentIndex = index-1;
        if (index == 0){
            newCurrentIndex = SongsListFragment.songsList.size()-1;
        }
        int prevSongIndex = newCurrentIndex-1;
        if (newCurrentIndex == 0) {
            prevSongIndex = SongsListFragment.songsList.size() - 1;
        }
        storageUtil.storeCurrentSongIndex(newCurrentIndex);
        storageUtil.storePrevSong(SongsListFragment.songsList.get(prevSongIndex));
        initMediaPlayer();
    }


    @Override
    public void onDestroy() {
        mediaPlayer.release();
        mediaPlayer = null;
        super.onDestroy();
    }


    private void initMediaSession() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mediaSessionManager = (MediaSessionManager) getApplicationContext().getSystemService(Context
                    .MEDIA_SESSION_SERVICE);

            mediaSession = new MediaSessionCompat(getApplicationContext(), MEDIA_SESSION_TAG);
            transportControls = mediaSession.getController().getTransportControls();
            mediaSession.setActive(true);
            mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

            mediaSession.setCallback(new MediaSessionCompat.Callback() {
                @Override
                public void onPlay() {
                    super.onPlay();
                }

                @Override
                public void onPause() {
                    super.onPause();
                }

                @Override
                public void onSkipToNext() {
                    super.onSkipToNext();
                }

                @Override
                public void onSkipToPrevious() {
                    super.onSkipToPrevious();
                }

                @Override
                public void onStop() {
                    super.onStop();
                }
            });
        }
    }


    public void createNotification() {

    }
}
