package com.webnotics.swipee.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.adapter.NotificationAdapter;
import com.webnotics.swipee.model.seeker.NotificationModel;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static NotificationActivity instance;
    Rest rest;
    Context mContext;
    ImageView iv_back,iv_more;
    TextView tv_nodata,tv_mute;
    RecyclerView rv_notification;
    LinearLayout ll_nodata,ll_mute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        setContentView(R.layout.activity_notification);
        mContext=this;
        rest=new Rest(mContext);
        instance=this;
        if (rest.isInterentAvaliable()){
           AppController.ShowDialogue("",mContext);
           if (Config.isSeeker())
            getNotificationList();
           else getCompanyNotificationList();
        }else  rest.AlertForInternet();

        iv_back=findViewById(R.id.iv_back);
        tv_nodata=findViewById(R.id.tv_nodata);
        ll_nodata=findViewById(R.id.ll_nodata);
        rv_notification=findViewById(R.id.rv_notification);
        ll_mute=findViewById(R.id.ll_mute);
        tv_mute=findViewById(R.id.tv_mute);
        iv_more=findViewById(R.id.iv_more);
        iv_back.setOnClickListener(v -> finish());
        if (Config.isMute()){
            tv_mute.setText("Unmute");
        }else tv_mute.setText("Mute");

        tv_mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Config.isMute()){
                    setNotificationSetting("N");
                }else {
                    setNotificationSetting("Y");
                }
                ll_mute.setVisibility(View.GONE);
            }
        });
        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_mute.getVisibility()==View.VISIBLE){
                    ll_mute.setVisibility(View.GONE);
                }else {
                    ll_mute.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void callService(){
        try {
            if (Config.isSeeker())
                getNotificationList();
            else getCompanyNotificationList();
        }catch (Exception ignored){}

    }
    private void getNotificationList() {
        SwipeeApiClient.swipeeServiceInstance().getUserNotification(Config.GetUserToken()).enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(@NonNull Call<NotificationModel> call, @NonNull Response<NotificationModel> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200){
                    NotificationModel notificationModel= response.body();
                    if (notificationModel!=null){
                        if (notificationModel.getCode()==200){
                            if (notificationModel.getFcm_mute().equalsIgnoreCase("Y")){
                                Config.setMuteNotification(true);
                                tv_mute.setText("Unmute");
                            }else {
                                Config.setMuteNotification(false);
                                tv_mute.setText("Mute");
                            }
                            tv_nodata.setVisibility(View.GONE);
                            ll_nodata.setVisibility(View.GONE);
                            rv_notification.setVisibility(View.VISIBLE);
                            rv_notification.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            NotificationAdapter stateAdapter = new NotificationAdapter(mContext, notificationModel.getData());
                            rv_notification.setAdapter(stateAdapter);
                        }else   if (notificationModel.getCode()==203){
                            rest.showToast(notificationModel.getMessage());
                            AppController.loggedOut(mContext);
                            finish();
                        }else {
                            rv_notification.setVisibility(View.GONE);
                            tv_nodata.setVisibility(View.VISIBLE);
                            ll_nodata.setVisibility(View.VISIBLE);
                            tv_nodata.setText(notificationModel.getMessage());
                        }

                    }else rest.showToast("Something went wrong");
                }else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<NotificationModel> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }
    private void getCompanyNotificationList() {
        SwipeeApiClient.swipeeServiceInstance().getCompanyNotification(Config.GetUserToken()).enqueue(new Callback<NotificationModel>() {
            @Override
            public void onResponse(@NonNull Call<NotificationModel> call, @NonNull Response<NotificationModel> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200){
                    NotificationModel notificationModel= response.body();
                    if (notificationModel!=null){
                        if (notificationModel.getCode()==200){
                            if (notificationModel.getFcm_mute().equalsIgnoreCase("Y")){
                                Config.setMuteNotification(true);
                                tv_mute.setText("Unmute");
                            }else {
                                Config.setMuteNotification(false);
                                tv_mute.setText("Mute");
                            }
                            tv_nodata.setVisibility(View.GONE);
                            ll_nodata.setVisibility(View.GONE);
                            rv_notification.setVisibility(View.VISIBLE);
                            rv_notification.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            NotificationAdapter stateAdapter = new NotificationAdapter(mContext, notificationModel.getData());
                            rv_notification.setAdapter(stateAdapter);
                        }else  if (notificationModel.getCode()==203){
                            rest.showToast(notificationModel.getMessage());
                            AppController.loggedOut(mContext);
                            finish();
                        }else {
                            rv_notification.setVisibility(View.GONE);
                            tv_nodata.setVisibility(View.VISIBLE);
                            ll_nodata.setVisibility(View.VISIBLE);
                            tv_nodata.setText(notificationModel.getMessage());
                        }

                    }else rest.showToast("Something went wrong");
                }else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<NotificationModel> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            instance=null;
        }catch (Exception ignored){}
    }

    private void setNotificationSetting(String isMute){
        AppController.ShowDialogue("", mContext);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
        hashMap.put(ParaName.KEY_FCMMUTE, isMute);
        SwipeeApiClient.swipeeServiceInstance().setNotificationSetting(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();

                    } else if (response.body().get("code").getAsInt() == 200 && response.body().get("status").getAsBoolean()) {
                              if (isMute.equalsIgnoreCase("N")){
                                  Config.setMuteNotification(false);
                                  tv_mute.setText("Mute");
                              }else if (isMute.equalsIgnoreCase("Y")){
                                  Config.setMuteNotification(true);
                                  tv_mute.setText("Unmute");
                              }
                    }

                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }
}