package com.swipee.in.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.potyvideo.library.AndExoPlayerView;
import com.swipee.in.R;

public class VideoPlayer extends AppCompatActivity {
    private AndExoPlayerView vv_video;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        vv_video=findViewById(R.id.vv_video);
        vv_video.setSource(getIntent().getStringExtra("url"));
        vv_video.setPlayWhenReady(true);
        vv_video.setShowController(true);
    }
}