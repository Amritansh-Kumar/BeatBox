package com.example.amritansh.beatbox.Activities;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amritansh.beatbox.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_ID = 1;

    @BindView(R.id.toolbar_main)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.toolbar_text)
    TextView toolbarTitle;

    protected abstract int getLayoutResourceId();

    protected abstract String getToolbarTitle();

    protected abstract boolean showToolbar();

    protected abstract int getFragmentContainerID();

    protected abstract Fragment getFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResourceId());
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        checkForPermissions();

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = getFragment();
        if (fragment != null) {
            fm.beginTransaction()
              .add(getFragmentContainerID(), fragment)
              .addToBackStack(null)
              .commit();
        }

        handleToolbar();
    }

    private void handleToolbar(){
        if (showToolbar()) {
            setSupportActionBar(toolbar);
            toolbar.setVisibility(View.VISIBLE);
            if (getToolbarTitle() != null) {
                toolbarTitle.setVisibility(View.VISIBLE);
                toolbarTitle.setText(getToolbarTitle());
                }
        } else {
            toolbar.setVisibility(View.GONE);
        }
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
            }
        }else {
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
            }
        }else {

        }

    }
}
