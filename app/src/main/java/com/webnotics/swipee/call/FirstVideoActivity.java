package com.webnotics.swipee.call;

import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.Seeker.SeekerHomeActivity;
import com.webnotics.swipee.activity.company.CompanyHomeActivity;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.text.MessageFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirstVideoActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    ImageView  calldecline, callaccept;
    ImageView img_profile;
    TextView name, email;
    Rest rest;
/*
    private MediaPlayer mediaPlayer;
*/
    private Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firstvideocall);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Important: have to do the following in order to show without unlocking
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        init();

    }

    public void init() {
        mContext = this;
        rest = new Rest(mContext);
       /* try {
            RingtoneManager ringtoneManager=new RingtoneManager()
            mediaPlayer = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
            mediaPlayer.setVolume(1f, 1f);
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        try {
             ringtone = RingtoneManager.getRingtone(getApplicationContext(), Settings.System.DEFAULT_RINGTONE_URI);
            ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
        img_profile = findViewById(R.id.img_profile);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        calldecline = findViewById(R.id.calldecline);
        callaccept = findViewById(R.id.callaccept);
        Log.d("user_access_token", getIntent().getStringExtra("user_access_token"));
        Log.d("room_name", getIntent().getStringExtra("room_name"));

        Glide.with(mContext)
                .load(getIntent().getStringExtra("user_profile"))
                .apply(new RequestOptions().placeholder(R.drawable.ic_profile_select).error(R.drawable.ic_profile_select))
                .into(img_profile);

        name.setText(getIntent().getStringExtra("user_name"));
        email.setText(MessageFormat.format("{0} {1}", getIntent().getStringExtra("user_phone_code"), getIntent().getStringExtra("user_mobile")));
        callaccept.setOnClickListener(this);
        calldecline.setOnClickListener(this);
        if (!getIntent().getStringExtra("appointment_type").equalsIgnoreCase("call")) {
             name.setVisibility(View.GONE);
             email.setVisibility(View.GONE);
        }else {
            name.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.callaccept:
                Intent intent;
                if (getIntent().getStringExtra("appointment_type").equalsIgnoreCase("call")) {
                    if (ringtone!=null && ringtone.isPlaying()) {
                        ringtone.stop();
                    }
                    intent = new Intent(mContext, AudioActivity.class);
                    intent.putExtra("imgurl", getIntent().getStringExtra("user_profile"));
                    intent.putExtra("user_name", getIntent().getStringExtra("user_name"));
                    intent.putExtra("appointment_id",getIntent().getStringExtra("Aid"));
                    intent.putExtra("phnno", getIntent().getStringExtra("user_phone_code") + " " + getIntent().getStringExtra("user_mobile"));

                } else {
                    if (ringtone!=null && ringtone.isPlaying()) {
                        ringtone.stop();
                    }
                    intent = new Intent(mContext, VideoActivity.class).putExtra("appointment_id",getIntent().getStringExtra("Aid"));
                }

                intent.putExtra("accestoken", getIntent().getStringExtra("user_access_token"));
                intent.putExtra("rroom", getIntent().getStringExtra("room_name"));

                mContext.startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
                break;
            case R.id.calldecline:
                if (ringtone!=null && ringtone.isPlaying()) {
                    ringtone.stop();
                }

                if (getIntent().getStringExtra("appointment_type").equalsIgnoreCase("call")){
                    if (Config.isSeeker() && !getIntent().getStringExtra("role_id").equalsIgnoreCase("3")){
                        userRejectAudioCall(getIntent().getStringExtra("Aid"));
                    }else {
                        companyRejectAudioCall(getIntent().getStringExtra("Aid"));
                    }
                } else{
                    if (Config.isSeeker() && !getIntent().getStringExtra("role_id").equalsIgnoreCase("3")){
                            userRejectVideoCall(getIntent().getStringExtra("Aid"));
                    }else {
                        companyRejectVideoCall(getIntent().getStringExtra("Aid"));
                    }
                }
                break;
        }
    }

    private void companyRejectVideoCall(String aid) {
        AppController.ShowDialogueWhite("", mContext);
        SwipeeApiClient.swipeeServiceInstance().companyRejectVideo(Config.GetUserToken(),aid).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    if (responseBody.get("code").getAsInt()==203){
                        rest.showToast(responseBody.get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();

                    }else
                    if (responseBody.get("code").getAsInt()==200 &&responseBody.get("status").getAsBoolean()){
                        startActivity(new Intent(mContext,CompanyHomeActivity.class));
                        finish();
                    }

                } else {
                    rest.showToast("Something went wrong");
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });
    }

    private void userRejectVideoCall(String aid) {
        AppController.ShowDialogueWhite("", mContext);
        SwipeeApiClient.swipeeServiceInstance().userRejectVideo(Config.GetUserToken(),aid).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    if (responseBody.get("code").getAsInt()==203){
                        rest.showToast(responseBody.get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();

                    }else
                    if (responseBody.get("code").getAsInt()==200 &&responseBody.get("status").getAsBoolean()){
                        startActivity(new Intent(mContext, SeekerHomeActivity.class));
                        finish();
                    }

                } else {
                    rest.showToast("Something went wrong");
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });

    }
    private void userRejectAudioCall(String aid) {
        AppController.ShowDialogueWhite("", mContext);
        SwipeeApiClient.swipeeServiceInstance().userRejectAudio(Config.GetUserToken(),aid).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    if (responseBody.get("code").getAsInt()==203){
                        rest.showToast(responseBody.get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    }else
                    if (responseBody.get("code").getAsInt()==200 &&responseBody.get("status").getAsBoolean()){
                        startActivity(new Intent(mContext, SeekerHomeActivity.class));
                        finish();
                    }

                } else {
                    rest.showToast("Something went wrong");
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });

    }
    private void companyRejectAudioCall(String aid) {
        AppController.ShowDialogueWhite("", mContext);
        SwipeeApiClient.swipeeServiceInstance().companyRejectAudio(Config.GetUserToken(),aid).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    if (responseBody.get("code").getAsInt()==203){
                        rest.showToast(responseBody.get("message").getAsString());
                             AppController.loggedOut(mContext);
                             finish();
                    }else
                    if (responseBody.get("code").getAsInt()==200 &&responseBody.get("status").getAsBoolean()){
                        startActivity(new Intent(mContext,CompanyHomeActivity.class));
                        finish();
                    }

                } else {
                    rest.showToast("Something went wrong");
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });

    }
    @Override
    public void onBackPressed() {

    }


}
