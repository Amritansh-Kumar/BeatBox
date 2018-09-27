package com.example.amritansh.beatbox.models;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.amritansh.beatbox.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LoadSongs {

    private ArrayList<Song> songsList;
    private Context context;

    public LoadSongs(Context context){
        this.context = context;
    }

    public ArrayList<Song> getSongs(){
        songsList = new ArrayList<>();
        FetchSongsThread fetchSongsThread = new FetchSongsThread();

        Thread thread = new Thread(fetchSongsThread);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return songsList;
    }


    public class FetchSongsThread implements Runnable{
        private final Uri URI = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        private final String[] PROJECTION = {MediaStore.Audio.Media.TITLE,
                                             MediaStore.Audio.Media.DATA,
                                             MediaStore.Audio.Media.ARTIST,
                                             MediaStore.Audio.Media._ID};
        private final String SELECTION = MediaStore.Audio.Media.IS_MUSIC + "!=0";
        private final String SORT_ORDER = MediaStore.Audio.Media.DEFAULT_SORT_ORDER;

        @Override
        public void run() {
            Cursor cursor = context.getContentResolver()
                    .query(URI, PROJECTION, SELECTION, null, SORT_ORDER);

            if (cursor != null){
                while (cursor.moveToNext()){
                    String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio
                            .Media.TITLE));
                    String uri = cursor.getString(cursor.getColumnIndex(MediaStore.Audio
                            .Media.DATA));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio
                            .Media.ARTIST));
//                    String thumburl = String.valueOf(cursor.getColumnIndex(MediaStore.Audio
//                            .Albums.ALBUM_ART));

                    String thumburl = "R.drawable";

//                    String title = cursor.getString(0);
//                    String songUri = cursor.getString(1);
//                    String artist = cursor.getString(2);
//                    long albumId = cursor.getLong(3);
//
//
//
//                    Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
//                    Uri uri = ContentUris.withAppendedId(sArtworkUri, albumId);

//                    String thumburl = uri.toString();
//                    Log.d("testing", "testinguri" + thumburl);

//                    ContentResolver res = context.getContentResolver();
//                    InputStream in = null;
//                    Bitmap artwork = BitmapFactory.decodeResource(context.getResources(), R.drawable
//                            .ic_launcher_background);;
//                    try {
//                        in = res.openInputStream(uri);
//                        artwork = BitmapFactory.decodeStream(in);
//                        Log.d("artwork", "testingartwork" + artwork);
//                        in.close();
//                    } catch (FileNotFoundException e) {
//                        Log.d("artwork", "testingartwork filenotfound");
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        Log.d("artwork", "testingartwork ioexception");
//                        e.printStackTrace();
//                    }


                    Song song = new Song(title, uri, thumburl, artist);

                    songsList.add(song);
                }
            }
        }
    }

}
