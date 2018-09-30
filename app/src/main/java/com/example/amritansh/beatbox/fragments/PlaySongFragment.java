package com.example.amritansh.beatbox.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.amritansh.beatbox.Activities.SongListActivity;
import com.example.amritansh.beatbox.R;
import com.example.amritansh.beatbox.services.PlaySongService;
import com.example.amritansh.beatbox.store.StorageUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaySongFragment extends Fragment {

    @BindView(R.id.play_button)
    Button playButton;
    @BindView(R.id.seekBar)
    SeekBar seekBar;
    @BindView(R.id.left_time)
    TextView leftTime;
    @BindView(R.id.right_time)
    TextView rightTime;
    @BindView(R.id.song_title)
    TextView songTitle;
    @BindView(R.id.song_artist)
    TextView songArtist;

    private ServiceConnection serviceConnection;

    private PlaySongService.MusicBinder musicBinder;
    private PlaySongService playSongService;
    private boolean mBound = false;
    private StorageUtil util = null;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
    private final Handler seekHandler = new Handler();

    private boolean isPlaying = true;
    private boolean newPlay = true;

    public PlaySongFragment() {

    }

    public static PlaySongFragment getFragment(String songUrl, String songTitle, String songArtist){
        Bundle bundle = new Bundle();
        bundle.putString("url", songUrl);
        bundle.putString("title", songTitle);
        bundle.putString("artist", songArtist);
        PlaySongFragment fragment = new PlaySongFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_song, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        util = new StorageUtil(getContext());

        Bundle bundle = getArguments();
        updateSongData();
        String songUri = bundle.getString("url");

        Log.d("console log", "songUri is : " + songUri );

        final Intent intent = new Intent(getActivity(), PlaySongService.class);
        intent.putExtra("music", songUri);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                musicBinder = (PlaySongService.MusicBinder) service;
                playSongService = musicBinder.getService();
                mBound = true;
                initSeekbar();
                updateThread();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mBound = false;
            }
        };

        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        getActivity().startService(intent);
    }

    // initializing the seekbar
    private void initSeekbar() {
        seekBar.setMax(playSongService.getDuration());

        // seekbar change listner
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (playSongService.getMediaPlayer() != null && fromUser) {
                    playSongService.seekMediaPlayer(progress);
                }

                int currentTime = playSongService.getCurrentPosition();
                int duration = playSongService.getDuration();

                leftTime.setText(dateFormat.format(new Date(currentTime - (30 * 60 * 1000))));
                rightTime.setText(dateFormat.format(new Date(duration - currentTime - (30 * 60 * 1000))));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @OnClick(R.id.play_button)
    public void play() {
        if (isPlaying) {
            isPlaying = false;
            playSongService.pauseAudio();
            playButton.setBackgroundResource(R.drawable.icon_play);
        } else {
            isPlaying = true;
            playButton.setBackgroundResource(R.drawable.icon_pause);
            playSongService.playAudio();
        }
    }

    @OnClick(R.id.next_btn)
    public void playNext(){
        playSongService.playNextSong();
        updateSongData();
    }

    @OnClick(R.id.prev_btn)
    public void playPrev(){
        playSongService.playPrevSong();
        updateSongData();
    }
    // updating seekbar with as music plays
    public void updateThread() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekBar.setMax(playSongService.getDuration());
                if (newPlay){
                    seekBar.setProgress(0);
                    newPlay = false;
                }else {
                    seekBar.setProgress(playSongService.getCurrentPosition());
                }

                int currentTime = playSongService.getCurrentPosition();
                int duration =  playSongService.getDuration();

                leftTime.setText(dateFormat.format(new Date(currentTime - (30 * 60 * 1000))));
                rightTime.setText(dateFormat.format(new Date(duration - currentTime - (30 * 60 * 1000))));

                seekHandler.postDelayed(this, 500);
            }
        });
    }

    private void updateSongData(){
        songTitle.setText(util.getCurrentSong().getTitle());
        songArtist.setText(util.getCurrentSong().getartist());
    }

    @Override
    public void onStop() {
        super.onStop();
        seekHandler.removeCallbacksAndMessages(null);
        getActivity().unbindService(serviceConnection);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(getActivity(), SongListActivity.class);
        getActivity().startActivity(intent);
    }
}
