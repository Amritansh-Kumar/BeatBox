package com.example.amritansh.beatbox.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amritansh.beatbox.Activities.PlaySongActivity;
import com.example.amritansh.beatbox.R;
import com.example.amritansh.beatbox.adapters.SongAdapter;
import com.example.amritansh.beatbox.interfaces.SongClickListner;
import com.example.amritansh.beatbox.models.LoadSongs;
import com.example.amritansh.beatbox.models.Song;
import com.example.amritansh.beatbox.store.StorageUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SongsListFragment extends Fragment implements SongClickListner {


    @BindView(R.id.rv_songs)
    RecyclerView songsRecycler;

    private SongAdapter adapter;
    public static ArrayList<Song> songsList;

    public SongsListFragment() {

    }

    public static SongsListFragment getFragment(){
        SongsListFragment fragment = new SongsListFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_songs_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initRecycler();
    }

    private void initRecycler() {
        adapter = new SongAdapter(this);
        songsRecycler.setAdapter(adapter);
        songsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        LoadSongs loadSongs = new LoadSongs(getActivity());
        songsList = loadSongs.getSongs();

        adapter.updateSongsList(songsList);
    }

    @Override
    public void onSongClickListner(Song song, int index) {

        // store current and next song
        StorageUtil util = new StorageUtil(getContext());
        util.storeCurrentSongIndex(index);
        util.storeCurrentSong(song);

        int nextSongIndex = index+1;
        if (index == songsList.size()-1){
            nextSongIndex = 0;
        }
        util.storeNextSong(songsList.get(nextSongIndex));

        int prevSongIndex = index-1;
        if (index == 0){
            prevSongIndex = songsList.size()-1;
        }
        util.storePrevSong(songsList.get(prevSongIndex));

        Intent intent = new Intent(getActivity(), PlaySongActivity.class);
        intent.putExtra("url", song.getSongUri());
        intent.putExtra("title", song.getTitle());
        intent.putExtra("artist", song.getartist());

        startActivity(intent);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().finish();
    }
}
