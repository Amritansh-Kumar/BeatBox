package com.example.amritansh.beatbox.Activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.widget.Toast;

import com.example.amritansh.beatbox.R;
import com.example.amritansh.beatbox.fragments.SongsListFragment;

public class SongListActivity extends SingleFragmentActivity {

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_song_list;
    }

    @Override
    protected String getToolbarTitle() {
        return "Beat Box";
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
        return SongsListFragment.getFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
