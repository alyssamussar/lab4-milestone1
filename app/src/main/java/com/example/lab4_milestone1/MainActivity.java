package com.example.lab4_milestone1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;




public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private Button start;
    private Button stop;
    private volatile boolean stopThread = false;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = findViewById(R.id.startButton);
        start.setOnClickListener(this); // calling onClick() method
        stop = findViewById(R.id.stopButton);
        stop.setOnClickListener(this); // calling onClick() method
        textView = findViewById(R.id.textView1);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.startButton:
                textView = findViewById(R.id.textView1);
                startDownload(findViewById(R.id.startButton));
                break;
            case R.id.stopButton:
                stopDownload(findViewById(R.id.stopButton));
                break;
        }
    }

    public void startDownload(View view) {
        stopThread = false;
        ExampleRunnable runnable = new ExampleRunnable();
        new Thread(runnable).start();
    }

    public void stopDownload(View view) {

        stopThread = true;
        textView.setText(" ");
    }

    public void mockFileDownloader() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                start.setText("Downloading...");
            }
        });

        for (int downloadProgress = 0; downloadProgress <= 100; downloadProgress += 10) {
            if (stopThread) {
                runOnUiThread(new Runnable(){

                    @Override
                    public void run() {
                        start.setText("Start");
                    }
                });
                return;
            }

            int finalDownloadProgress = downloadProgress;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    textView.setText("Download Progress: " + finalDownloadProgress + "%");
                }
            });


            Log.d(TAG, "Download Progress: " + downloadProgress + "%");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        runOnUiThread(new Runnable(){
            @Override
            public void run() {
                start.setText("Start");
            }
        });
    }

    class ExampleRunnable implements Runnable {
        @Override
        public void run() {
            mockFileDownloader();
        }
    }
}