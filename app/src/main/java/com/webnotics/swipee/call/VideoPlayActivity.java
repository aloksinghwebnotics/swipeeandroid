package com.webnotics.swipee.call;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.webnotics.swipee.R;


public class VideoPlayActivity extends AppCompatActivity {

    VideoView videoView;

    private MediaController mediaController;

    ImageView backImg;

    String uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);

        uri = getIntent().getStringExtra("VideoUri");

        initView();
    }

    private void initView() {

        videoView = findViewById(R.id.videoView_videoPlayAct);

        backImg = findViewById(R.id.backImg_videoPlayAct);

        videoView.setVideoURI(Uri.parse(uri));

        videoView.requestFocus();

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        videoView.start();

        if (mediaController == null) {

            mediaController = new MediaController(this);

            // Set the videoView that acts as the anchor for the MediaController.
            mediaController.setAnchorView(videoView);


            // Set MediaController for VideoView
            videoView.setMediaController(mediaController);
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}
