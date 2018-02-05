package com.pruebatecnica_pablocastrillon;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.pruebatecnica_pablocastrillon.Network.WebService;
import com.pruebatecnica_pablocastrillon.model.GetNotification;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.transform.Templates;

/**
 * Created by pablo.castrillon on 31/01/2018.
 */

public class DownloadViewFragment extends Fragment implements View.OnClickListener {

    private Button buttonDowloadVideo;
    private ProgressBar progressBarVideoDownload;
    private TextView textViewDowloadVideo;
    private ProgressBar progressBar;
    private GetNotification getNotification;
    private WebService webService;
    private DownLoadFinishListener downLoadFinishListener;


    private VideoPlayerFragment videoPlayerFragment;
    private String VideoPlayerFragmentTag = "videoPlayerFragment";


    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    private TextView textView;


    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public DownLoadFinishListener getDownLoadFinishListener() {
        return downLoadFinishListener;
    }

    public void setDownLoadFinishListener(DownLoadFinishListener downLoadFinishListener) {
        this.downLoadFinishListener = downLoadFinishListener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.download_view_fragment, container, false);

        buttonDowloadVideo = (Button) view.findViewById(R.id.bt_download_video);
        progressBarVideoDownload = (ProgressBar) view.findViewById(R.id.pb_download_video);
        textViewDowloadVideo = (TextView) view.findViewById(R.id.tv_download_video);
        webService = new WebService(getContext());
        buttonDowloadVideo.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.bt_download_video):
                progressBarVideoDownload.setVisibility(View.VISIBLE);
                setProgressBar(progressBarVideoDownload);
                setTextView(textViewDowloadVideo);
                final DownloadVideo downloadLogTask = new DownloadVideo(getActivity());


                getNotification = new GetNotification(1, "2016-12-07 14:45:00", 0);
                webService.postNotificationRequest(getNotification);


                downloadLogTask.execute("https://s3.amazonaws.com/tekus/media/Arkbox.mp4");
                buttonDowloadVideo.setEnabled(false);
                break;

        }
    }


    private class DownloadVideo extends AsyncTask<String, Integer, String> {

        private Context context;
        private PowerManager.WakeLock mWakeLock;


        public DownloadVideo(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... sUrl) {
            InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;
            try {
                URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                // expect HTTP 200 OK, so we don't mistakenly save error report
                // instead of the file
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // this will be useful to display download percentage
                // might be -1: server did not report the length
                int fileLength = connection.getContentLength();

                // download the file
                input = connection.getInputStream();

                File video = new File(Environment.getExternalStorageDirectory().getPath() + "/Media/", "Arkbox.mp4");
                output = new FileOutputStream(video);


                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    // allow canceling with back button
                    if (isCancelled()) {
                        input.close();
                        return null;
                    }
                    total += count;
                    // publishing the progress....
                    if (fileLength > 0) // only if total length is known
                        publishProgress((int) (total * 100 / fileLength));
                    output.write(data, 0, count);
                }
            } catch (Exception e) {
                return e.toString();
            } finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                }

                if (connection != null)
                    connection.disconnect();
            }
            return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();


            if (isExternalStorageReadable() && isExternalStorageWritable()) {
                getMedeiatorageDir("Media");
            }

        }


        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            getProgressBar().setIndeterminate(false);
            getProgressBar().setMax(100);
            getProgressBar().setProgress(progress[0]);
//            webService.putNotification();

            getTextView().setText(String.valueOf(progress[0]) + " %");
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            if (result != null) {
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
                getProgressBar().setVisibility(View.GONE);
                downLoadFinishListener.PlayVideo();

                buttonDowloadVideo.setEnabled(true);
//                videoPlayerFragment = new VideoPlayerFragment();
//                addFragment(videoPlayerFragment, VideoPlayerFragmentTag);
            }


        }
    }


    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public File getMedeiatorageDir(String mediaName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStorageDirectory(), mediaName);
        if (!file.mkdirs()) {
            Log.e("Directory not created", "Directory not created");
        }
        return file;
    }


    public String localDateTime() {
        //TODO: devolver el tiempo que se requiere
        return "";
    }


    public void addFragment(VideoPlayerFragment fragment, String tag) {
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

    public interface DownLoadFinishListener {
        void PlayVideo ();


    }

}
