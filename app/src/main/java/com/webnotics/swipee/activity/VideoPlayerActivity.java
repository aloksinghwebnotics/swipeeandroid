package com.webnotics.swipee.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieProperty;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.value.LottieFrameInfo;
import com.airbnb.lottie.value.SimpleLottieValueCallback;
import com.webnotics.swipee.R;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;
import org.videolan.libvlc.util.VLCVideoLayout;

import java.util.ArrayList;

public class VideoPlayerActivity extends AppCompatActivity {

    private static final boolean USE_TEXTURE_VIEW = false;
    private static final boolean ENABLE_SUBTITLES = true;
    private static final String KEY_FILE_PATH = "KEY_FILE_PATH";

    private LibVLC mLibVLC;
    private MediaPlayer mMediaPlayer;
    private VLCVideoLayout vlcVideoLayout;
    private ImageView play_pause;
    private LottieAnimationView lottieAnimationView;
    private SeekBar seekBar;
    private long totalLength = 0;
    private long currentTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |

                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        vlcVideoLayout = findViewById(R.id.view_vlc_layout);

        play_pause = findViewById(R.id.play_pause);
        seekBar = findViewById(R.id.seekBar);
        lottieAnimationView = findViewById(R.id.animation_view);

        lottieAnimationView.addValueCallback(
                new KeyPath("**"),
                LottieProperty.COLOR_FILTER,
                new SimpleLottieValueCallback<ColorFilter>() {
                    @Override
                    public ColorFilter getValue(LottieFrameInfo<ColorFilter> frameInfo) {
                        return new PorterDuffColorFilter(getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    }
                }
        );

        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mMediaPlayer.isPlaying()) mMediaPlayer.pause();
                else mMediaPlayer.play();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) mMediaPlayer.setTime(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    private void initPlayerView() {
        Intent intent = getIntent();
        String filePath = intent.getStringExtra(KEY_FILE_PATH);

        ArrayList<String> list = new ArrayList<>();
        list.add("--no-drop-late-frames");
        list.add("--no-skip-frames");
        list.add("--rtsp-tcp");
        list.add("-vvv");
        mLibVLC = new LibVLC(this, list);

        mMediaPlayer = new MediaPlayer(mLibVLC);
        mMediaPlayer.attachViews(vlcVideoLayout, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW);

        try {

            Media media = new Media(mLibVLC, Uri.parse(filePath));
            media.setHWDecoderEnabled(true, false);
            media.addOption(":network-caching=150");
            media.addOption(":clock-jitter=0");
            media.addOption(":clock-synchro=0");

            mMediaPlayer.setMedia(media);
            media.release();

            mMediaPlayer.play();

            mMediaPlayer.setEventListener(new MediaPlayer.EventListener() {
                @Override
                public void onEvent(MediaPlayer.Event event) {

                    // Opening
                    if (event.type == MediaPlayer.Event.Opening) {
                        Log.i("Video", String.valueOf(currentTime));
                        mMediaPlayer.setTime(currentTime);

                        play_pause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    }

                    // Buffering
                    else if (event.type == MediaPlayer.Event.Buffering) {
                        if (mMediaPlayer.isPlaying()) stopAnimation();
                        else playAnimation();
                    }

                    // Playing
                    else if (event.type == MediaPlayer.Event.Playing) {

                        totalLength = mMediaPlayer.getLength();
                        seekBar.setMax((int) mMediaPlayer.getLength());
                        seekBar.setProgress((int) currentTime);
                        play_pause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                        stopAnimation();
                        updateHandler.postDelayed(updateVideoTime, 1);
                    }

                    // Paused
                    else if (event.type == MediaPlayer.Event.Paused) {
                        currentTime = mMediaPlayer.getTime();
                        play_pause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    }

                    // End Reached
                    else if (event.type == MediaPlayer.Event.EndReached) {
                        seekBar.setProgress(0);
                        mMediaPlayer.stop();
                        currentTime=0;
                        play_pause.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                    }

                    // Stopped
                    else if (event.type == MediaPlayer.Event.Stopped) {
                        Log.d("Video", "Stopped");
                    }

                    // Time Changed
                    else if (event.type == MediaPlayer.Event.TimeChanged) {


                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start(Activity activity, String filePath) {
        Intent intent = new Intent(activity, VideoPlayerActivity.class);
        intent.putExtra(KEY_FILE_PATH, filePath);
        activity.startActivity(intent);
    }

    private Handler updateHandler=new Handler();
    private Runnable updateVideoTime = new Runnable() {
        @Override
        public void run() {
            if (mMediaPlayer != null) {
                long currentPosition = mMediaPlayer.getTime();
                seekBar.setProgress((int) currentPosition);
                updateHandler.postDelayed(this, 1);
            }

        }
    };
    private void playAnimation() {
        lottieAnimationView.setVisibility(View.VISIBLE);
        lottieAnimationView.playAnimation();
    }

    private void stopAnimation() {
        lottieAnimationView.setVisibility(View.GONE);
        lottieAnimationView.pauseAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPlayerView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       if (mMediaPlayer!=null){
           mMediaPlayer.release();
           mLibVLC.release();
       }
    }
}