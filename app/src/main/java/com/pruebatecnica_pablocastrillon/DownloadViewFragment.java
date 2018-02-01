package com.pruebatecnica_pablocastrillon;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by pablo.castrillon on 31/01/2018.
 */

public class DownloadViewFragment extends Fragment implements View.OnClickListener {

    private Button buttonDowloadVideo;
    private ProgressBar progressBarVideoDownload;

    private ProgressBar progressBar;

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.download_view_fragment, container, false);

        buttonDowloadVideo = (Button) view.findViewById(R.id.bt_download_video);
        progressBarVideoDownload = (ProgressBar) view.findViewById(R.id.pb_download_video);
        buttonDowloadVideo.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.bt_download_video):
                progressBarVideoDownload.setVisibility(View.VISIBLE);
                setProgressBar(progressBarVideoDownload);
                final DownloadVideo downloadLogTask = new DownloadVideo(getActivity());
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


                output = new FileOutputStream( "Arkbox.mp4");

                // creando folder


//                if (isExternalStorageWritable() || isExternalStorageReadable())
//
//
//                    File video = new File(getMedeiatorageDir("Media"), "Arkbox.mp4");
//                    try {
//                        output = new FileOutputStream(video);
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }


//
//                File mediaFolder = new File(Environment.getExternalStorageDirectory() + "/Media/");
//                File media = new File(Environment.getExternalStorageDirectory() + "/Media/" + "Arkbox.mp4");
//
//                if (!mediaFolder.exists()) {
//                    mediaFolder.mkdir();
//                }
//
//
//                File video = new File(new File(Environment.getExternalStorageDirectory() + "/Media/"), "Arkbox.mp4");
//                if (video.exists()) {
//                    video.delete();
//                }
//                try {
//                    output = new FileOutputStream(video);
//                    output.flush();
//                    output.close();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//    File video = new File(mediaFolder.getAbsolutePath(), "Arkbox.mp4");
//

//                output = new FileOutputStream(video);


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
            getMedeiatorageDir("Media");
        }


        @Override
        protected void onProgressUpdate(Integer... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            getProgressBar().setIndeterminate(false);
            getProgressBar().setMax(100);
            getProgressBar().setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            mWakeLock.release();
            if (result != null) {
                Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();
                getProgressBar().setVisibility(View.GONE);
                buttonDowloadVideo.setEnabled(true);

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
            Toast.makeText(getContext(), "Directory not created", Toast.LENGTH_SHORT).show();
//            Log.e(LOG_TAG, "Directory not created");
        }
        return file;
    }
}
