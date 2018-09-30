package com.example.amritansh.beatbox.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.amritansh.beatbox.R;import com.example.amritansh.beatbox.interfaces.SongClickListner;
import com.example.amritansh.beatbox.models.Song;
import com.example.amritansh.beatbox.store.StorageUtil;

import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongsHolder> {

    private ArrayList<Song> songsList;
    private static final int ANIMATION_DURATION = 500;
    private SongClickListner songClickListner;

    public SongAdapter(SongClickListner context){
        songClickListner = context;
    }

    @NonNull
    @Override
    public SongsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.song_row, viewGroup, false);
        return new SongsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongsHolder songsHolder, int i) {
        songsHolder.fillDetails(songsList.get(i));
        YoYo.with(Techniques.FadeIn)
            .duration(ANIMATION_DURATION)
            .playOn(songsHolder.itemView);
    }

    @Override
    public int getItemCount() {
        if (songsList != null){
            return songsList.size();
        }

        return 0;
    }

    public void updateSongsList(ArrayList<Song> songsList){
        this.songsList = songsList;
    }

    public class SongsHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.song_thumb)
        ImageView songImage;
        @BindView(R.id.song_title)
        TextView songTitle;
        @BindView(R.id.song_artist)
        TextView artist;
        private Song song;


        public SongsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void fillDetails(Song song){
            this.song = song;
//            songImage.setImageResource(R.drawable.ic_launcher_background);
            songTitle.setText(song.getTitle());
            artist.setText(song.getartist());
        }


        @OnClick(R.id.row_container)
        void songRowClicked(){

            songClickListner.onSongClickListner(song, getAdapterPosition());

        }

    }
}
