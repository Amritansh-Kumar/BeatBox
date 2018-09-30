package com.example.amritansh.beatbox.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.app.NotificationCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import com.example.amritansh.beatbox.R;
import com.example.amritansh.beatbox.fragments.SongsListFragment;
import com.example.amritansh.beatbox.models.Song;
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
    private static final int NOTIFICATION_ID = 501;

    private MediaSessionManager mediaSessionManager;
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;

    public boolean isPlaying = true;

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

        if (mediaSessionManager == null) {
            initMediaSession();
        }
        initMediaPlayer();

        createNotification();
        handleIntent(intent);
        return START_STICKY;
    }

    private void initMediaPlayer() {
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

    public void playNextSong() {
        storageUtil.storePrevSong(storageUtil.getCurrentSong());
        storageUtil.storeCurrentSong(storageUtil.getNextSong());

        int index = storageUtil.getStoreIndex();
        int newCurrentIndex = index + 1;
        if (index == SongsListFragment.songsList.size() - 1) {
            newCurrentIndex = 0;
        }
        int nextSongIndex = newCurrentIndex + 1;
        if (newCurrentIndex == SongsListFragment.songsList.size() - 1) {
            nextSongIndex = 0;
        }
        storageUtil.storeCurrentSongIndex(newCurrentIndex);
        storageUtil.storeNextSong(SongsListFragment.songsList.get(nextSongIndex));
        initMediaPlayer();
    }

    public void playPrevSong() {
        storageUtil.storeNextSong(storageUtil.getCurrentSong());
        storageUtil.storeCurrentSong(storageUtil.getPrevSong());

        int index = storageUtil.getStoreIndex();
        int newCurrentIndex = index - 1;
        if (index == 0) {
            newCurrentIndex = SongsListFragment.songsList.size() - 1;
        }
        int prevSongIndex = newCurrentIndex - 1;
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
                    playAudio();
                    isPlaying = true;
                    createNotification();
                }

                @Override
                public void onPause() {
                    super.onPause();
                    pauseAudio();
                    isPlaying = false;
                    createNotification();
                }

                @Override
                public void onSkipToNext() {
                    super.onSkipToNext();
                    playNextSong();
                    updateSongDetails();
                    isPlaying = true;
                    createNotification();
                }

                @Override
                public void onSkipToPrevious() {
                    super.onSkipToPrevious();
                    playPrevSong();
                    updateSongDetails();
                    isPlaying = true;
                    createNotification();
                }

                @Override
                public void onStop() {
                    super.onStop();
                    removeNotification();
                    stopSelf();
                }
            });
        }
    }


    private void updateSongDetails() {
        Song song = storageUtil.getCurrentSong();
        Bitmap albumArt = BitmapFactory.decodeResource(getResources(),
                R.drawable.music);

        mediaSession.setMetadata(new MediaMetadataCompat.Builder()
                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt)
                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, song.getartist())
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, song.getTitle())
                .build());
    }

    public void createNotification() {
        int notificationAction = R.drawable.icon_pause;
        PendingIntent pendingIntentAction = null;

        if (isPlaying) {
            notificationAction = R.drawable.icon_pause;
            pendingIntentAction = getPendingIntentAction(1);
        } else {
            notificationAction = R.drawable.icon_play;
            pendingIntentAction = getPendingIntentAction(0);
        }

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.music);
        Song currentSong = storageUtil.getCurrentSong();

        android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app
                .NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        builder.setShowWhen(true)
               .setStyle(new NotificationCompat.MediaStyle()
                       // Attach our MediaSession token
                       .setMediaSession(mediaSession.getSessionToken())
                       // Show our playback controls in the compat view
                       .setShowActionsInCompactView(0, 1, 2))
               // Set the Notification color
               .setColor(getResources().getColor(R.color.colorAccent))
               // Set the large and small icons
               .setLargeIcon(largeIcon)
               .setSmallIcon(android.R.drawable.stat_sys_headset)
               // Set Notification content information
               .setContentText(currentSong.getartist())
               .setContentInfo(currentSong.getartist())
               .setContentTitle(currentSong.getTitle())
               // Add playback actions
               .addAction(android.R.drawable.ic_media_previous, "previous", getPendingIntentAction(3))
               .addAction(notificationAction, "pause", pendingIntentAction)
               .addAction(android.R.drawable.ic_media_next, "next", getPendingIntentAction(2));

        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify
                (NOTIFICATION_ID, builder.build());

    }

    private PendingIntent getPendingIntentAction(int action) {
        Intent notificationIntent = new Intent(getApplicationContext(), PlaySongService.class);

        switch (action) {
            case 0:
                notificationIntent.setAction(ACTION_PLAY);
                break;
            case 1:
                notificationIntent.setAction(ACTION_PAUSE);
                break;
            case 2:
                notificationIntent.setAction(ACTION_NEXT);
                break;
            case 3:
                notificationIntent.setAction(ACTION_PREVIOUS);
                break;
            default:
                break;
        }
        return PendingIntent.getService(getApplicationContext(), action, notificationIntent, 0);
    }

    private void handleIntent(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            transportControls.play();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            transportControls.pause();
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            transportControls.skipToNext();
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            transportControls.skipToPrevious();
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            transportControls.stop();
        }
    }

    private void removeNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
