package com.example.amritansh.beatbox;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.amritansh.beatbox.fragments.SongsListFragment;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkForPermissions();
        setContentView(R.layout.activity_main);
    }

    private void checkForPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                        .READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Need permission to read storage ",
                            Toast.LENGTH_SHORT).show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                                                                                 .READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_ID);
                }
            }else {
                loadFragment();
            }
        }else {
            loadFragment();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission
                        .READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Need permission to read storage ",
                            Toast.LENGTH_SHORT).show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                                                                                 .READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_ID);
                }
            } else {
                loadFragment();
            }
        }else {
            loadFragment();
        }

    }

    private void loadFragment(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        Fragment songsListFrag = new SongsListFragment();
        transaction.add(R.id.container, songsListFrag);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
