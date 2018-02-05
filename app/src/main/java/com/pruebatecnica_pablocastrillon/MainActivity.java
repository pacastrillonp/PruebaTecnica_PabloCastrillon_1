package com.pruebatecnica_pablocastrillon;

import android.Manifest;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity implements DownloadViewFragment.DownLoadFinishListener {

    private DownloadViewFragment downloadViewFragment;
    private VideoPlayerFragment videoPlayerFragment;
    private String VideoPlayerFragmentTag = "VideoPlayerFragmentTag";
    private String DownloadViewFragmenTag = "DownloadViewFragmenTag";
    private static final int MY_PERMISSIONS_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_main);

            File dir = new File(Environment.getExternalStorageDirectory().getPath() + "/Media/Arkbox.mp4");
            if (dir.exists()) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                videoPlayerFragment = new VideoPlayerFragment();
                addFragment(videoPlayerFragment, VideoPlayerFragmentTag);
            } else {
                downloadViewFragment = new DownloadViewFragment();
                downloadViewFragment.setDownLoadFinishListener(this);
                addFragment(downloadViewFragment, DownloadViewFragmenTag);

            }




            if (Build.VERSION.SDK_INT >= 23) {
                verifyPermission();

            }
        }
    }


    public void addFragment(android.support.v4.app.Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.Fragment previousFragment = fragmentManager.findFragmentByTag(tag);

        if (previousFragment == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(android.R.id.content, fragment, tag);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commitAllowingStateLoss();
        } else {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.show(previousFragment);
        }
    }


    private void removeFragment(String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.Fragment fragment = fragmentManager.findFragmentByTag(tag);
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.remove(fragment);

        transaction.commitAllowingStateLoss();
    }

    private void verifyPermission() {

        if (((ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)) +
                (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (this, Manifest.permission.INTERNET) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.WRITE_EXTERNAL_STORAGE))) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST);

            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST: {

                if (grantResults.length > 0) {

                    boolean internetPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (internetPermission && readExternalFile) {
                        Toast.makeText(getApplicationContext(), R.string.permission_granted, Toast.LENGTH_SHORT).show();
                    } else {
                        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                        Toast.makeText(getApplicationContext(), R.string.permission_denied, Toast.LENGTH_SHORT).show();
                    }

                }


            }


        }

    }

    @Override
    public void PlayVideo() {
        removeFragment(DownloadViewFragmenTag);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        videoPlayerFragment = new VideoPlayerFragment();
        addFragment(videoPlayerFragment, VideoPlayerFragmentTag);
    }
}
