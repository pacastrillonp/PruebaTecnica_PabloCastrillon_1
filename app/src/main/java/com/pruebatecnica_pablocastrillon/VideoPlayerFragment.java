package com.pruebatecnica_pablocastrillon;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.VideoView;


/**
 * Created by pablo.castrillon on 4/02/2018.
 */

public class VideoPlayerFragment extends Fragment  {
    private VideoView videoView;
    private Boolean Playing = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.video_player_fregment, container, false);
        videoView = (VideoView) view.findViewById(R.id.vv_player);



        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        view.setSystemUiVisibility(uiOptions);


        videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath() + "/Media/Arkbox.mp4");
        Playing = true;
        videoView.start();

        view.setOnTouchListener();

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
}
