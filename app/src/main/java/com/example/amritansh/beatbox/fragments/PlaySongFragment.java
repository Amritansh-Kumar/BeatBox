package com.example.amritansh.beatbox.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.amritansh.beatbox.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlaySongFragment extends Fragment {

    @BindView(R.id.play)
    ImageView playButton;
//    @BindView(R.id.pause)
//    Button pauseButton;
    private MediaPlayer mediaPlayer;
    private boolean isPlay = true;

    public PlaySongFragment() {
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

        Bundle bundle = getArguments();
        String songUri = bundle.getString("url");
        init(songUri);
    }

    private void init(String songUri) {
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(songUri);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.play)
    public void play(){
        if (isPlay) {
            isPlay = false;
            playButton.setImageResource(R.drawable.icon_pause);
            mediaPlayer.start();
        }else {
            isPlay = true;
            playButton.setImageResource(R.drawable.icon_play);
            mediaPlayer.pause();
        }
    }

    @OnClick(R.id.pause)
    public void pause(){
        mediaPlayer.pause();
    }

}
