package com.example.amritansh.beatbox.Activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.amritansh.beatbox.R;
import com.example.amritansh.beatbox.fragments.PlaySongFragment;

public class PlaySongActivity extends SingleFragmentActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_play_song;
    }

    @Override
    protected String getToolbarTitle() {
        return getIntent().getStringExtra("title");
    }

    @Override
    protected boolean showToolbar() {
        return true;
    }

    @Override
    protected int getFragmentContainerID() {
        return R.id.container;
    }

    @Override
    protected Fragment getFragment() {

//        PlaySongFragment fragment = new PlaySongFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("url", getIntent().getStringExtra("url"));
//        bundle.putString("title", getIntent().getStringExtra("title"));
//        bundle.putString("artist", getIntent().getStringExtra("artist"));
//        fragment.setArguments(bundle);

        String songUrl = getIntent().getStringExtra("url");
        String songTitle = getIntent().getStringExtra("title");
        String songArtist = getIntent().getStringExtra("artist");

        return PlaySongFragment.getFragment(songUrl, songTitle, songArtist);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
