package com.example.amritansh.beatbox.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.amritansh.beatbox.R;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

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

    final SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
    private MediaPlayer mediaPlayer;
    private Thread thread;

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

        initSeekbar();
    }

    private void initSeekbar() {

        seekBar.setMax(mediaPlayer.getDuration());

//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                seekBar.setProgress(mediaPlayer.getCurrentPosition());
//            }
//        },0, 200);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer!=null && fromUser){
                    mediaPlayer.seekTo(progress);
                }

                int currentTime = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                leftTime.setText(dateFormat.format(new Date(currentTime)));
                rightTime.setText(dateFormat.format(new Date(duration-currentTime)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//
//                if (mediaPlayer != null && b) {
//
//                    mediaPlayer.seekTo(i);
//
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//                Log.i("Seek", "ProgressBar touched");
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//                Log.i("Seek", "progress stopped");
//            }
//
//        });

    }

    @OnClick(R.id.play_button)
    public void play(){
        if (isPlay) {
            isPlay = false;
            playButton.setBackgroundResource(R.drawable.icon_pause);
            mediaPlayer.start();
            updateThread();
        }else {
            isPlay = true;
            playButton.setBackgroundResource(R.drawable.icon_play);
            mediaPlayer.pause();
        }
    }

    public void updateThread(){
        final Handler seekHandler = new Handler();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                int currentTime = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                leftTime.setText(dateFormat.format(new Date(currentTime)));
                rightTime.setText(dateFormat.format(new Date(duration-currentTime)));

                seekHandler.postDelayed(this, 50);
            }
        });
    }
}
