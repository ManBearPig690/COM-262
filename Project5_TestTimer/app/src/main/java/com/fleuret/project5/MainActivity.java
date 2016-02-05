package com.fleuret.project5;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.fleuret.ch10_ex5.R;

public class MainActivity extends Activity {

    private TextView messageTextView;
    Timer timer = new Timer(true);
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        messageTextView = (TextView) findViewById(R.id.messageTextView);
        startTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    private void startTimer() {
        final long startMillis = System.currentTimeMillis();
        final FileIO downloader = new FileIO(this);
        i = 0;
        TimerTask task = new TimerTask() {
            
            @Override
            public void run() {
                long elapsedMillis = System.currentTimeMillis() - startMillis;
                downloader.downloadFile();
                updateView(elapsedMillis);

            }
        };
        timer.scheduleAtFixedRate(task, 10000, 10000);
    }

    private void updateView(final long elapsedMillis) {
        i++;
        // UI changes need to be run on the UI thread
        messageTextView.post(new Runnable() {

            int elapsedSeconds = (int) elapsedMillis / 1000;

            @Override
            public void run() {
                messageTextView.setText("Seconds: " + elapsedSeconds + "\nFile Downloaded ("+ i +") times.");
            }
        });
    }


    public void onStopClick(View view){
        timer.cancel();
    }

    public void onStartClick(View view) {
        timer = new Timer(true);
        startTimer();
    }
}