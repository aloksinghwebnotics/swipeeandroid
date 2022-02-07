package com.webnotics.swipee.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.webnotics.swipee.R;

public class videoPlayerActivity extends AppCompatActivity {
    VideoView vv_video;
    ProgressBar progressBar;


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vedio_player);

        vv_video = findViewById(R.id.vv_video);

        progressBar = findViewById(R.id.progressBar);

         vv_video.setVideoURI(Uri.parse(getIntent().getStringExtra("uri")));
         vv_video.start();
        vv_video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setScreenOnWhilePlaying(true);

            }
        });



    }

  }