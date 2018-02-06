package com.pruebatecnica_pablocastrillon;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.VideoView;


/**
 * Created by pablo.castrillon on 4/02/2018.
 */

public class VideoPlayerFragment extends Fragment implements GestureDetector.OnDoubleTapListener {
    private VideoView videoView;
    private Boolean Playing = false;
    private NotificationsFragment notificationsFragment;
    private String  NotificationsFragmentTag = "NotificationsFragmentTag";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.video_player_fregment, container, false);
        videoView = (VideoView) view.findViewById(R.id.vv_player);


        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        view.setSystemUiVisibility(uiOptions);


        videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath() + "/Media/Arkbox.mp4");
        Playing = true;
        videoView.start();

//        view.setOnTouchListener();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoView.start();
            }
        });


        return view;
    }


    @Override
    public void onResume() {


        super.onResume();
    }

    public void addFragment(NotificationsFragment fragment, String tag) {
        FragmentManager fragmentManager = getFragmentManager();
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

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        notificationsFragment = new NotificationsFragment();
        addFragment(notificationsFragment,NotificationsFragmentTag);

        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }


}
