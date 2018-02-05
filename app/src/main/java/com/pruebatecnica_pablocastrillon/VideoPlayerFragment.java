package com.pruebatecnica_pablocastrillon;

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

public class VideoPlayerFragment extends Fragment {
    private VideoView videoView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.video_player_fregment, container, false);
        videoView = (VideoView) view.findViewById(R.id.vv_player);
        videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath()+"/Media/Arkbox.mp4");
        videoView.start();

        return view;
    }
}
