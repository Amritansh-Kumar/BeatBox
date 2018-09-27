package com.example.amritansh.beatbox.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.amritansh.beatbox.R;

import java.io.IOException;
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

    final SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
    private MediaPlayer mediaPlayer;
    private final Handler seekHandler = new Handler();

    private boolean isPlaying = true;

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
        songTitle.setText(bundle.getString("title"));
        songArtist.setText(bundle.getString("artist"));
        String songUri = bundle.getString("url");
        init(songUri);

        playSong();
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

                leftTime.setText(dateFormat.format(new Date(currentTime - (30*60*1000))));
                rightTime.setText(dateFormat.format(new Date(duration-currentTime - (30*60*1000))));
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
        if (isPlaying) {
            isPlaying = false;
            mediaPlayer.pause();
            playButton.setBackgroundResource(R.drawable.icon_play);
        }else {
            isPlaying = true;
            playButton.setBackgroundResource(R.drawable.icon_pause);
            playSong();
        }
    }

    private void playSong(){
        mediaPlayer.start();
        updateThread();
    }

    public void updateThread(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                int currentTime = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();

                leftTime.setText(dateFormat.format(new Date(currentTime - (30*60*1000))));
                rightTime.setText(dateFormat.format(new Date(duration-currentTime - (30*60*1000))));

                seekHandler.postDelayed(this, 1000);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        seekHandler.removeCallbacksAndMessages(null);
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
