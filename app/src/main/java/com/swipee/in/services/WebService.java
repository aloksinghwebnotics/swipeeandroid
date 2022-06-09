package com.swipee.in.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.swipee.in.UrlManager.Config;
import com.swipee.in.rest.ParaName;
import com.swipee.in.rest.SwipeeApiClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("xdddddd","on creat");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStart(intent, startId);
        try {
            if (intent!=null && intent.hasExtra(ParaName.KEY_JOBPOSTID)){
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                hashMap.put(ParaName.KEY_JOBPOSTID,intent.getStringExtra(ParaName.KEY_JOBPOSTID));
                hashMap.put(ParaName.KEY_USERSTATUS, intent.getStringExtra(ParaName.KEY_USERSTATUS));
                hashMap.put(ParaName.KEY_COMPANYID, intent.getStringExtra(ParaName.KEY_COMPANYID));
                hashMap.put(ParaName.KEY_MATCHID,"");
                hashMap.put(ParaName.KEY_COMPANYSTATUS, intent.getStringExtra(ParaName.KEY_COMPANYSTATUS));
                postLikeDislike(hashMap);
                if (intent.getIntExtra(ParaName.KEY_UNIQUENOTIFYNUMBER,0)== 121) {
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext()
                            .getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.cancel(intent.getIntExtra(ParaName.KEY_UNIQUENOTIFYNUMBER,0));
                }
            }else {
                HashMap<String,String> hashMap=new HashMap<>();
                hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                hashMap.put(ParaName.KEY_ISONLINE,"N");
                sendOnline(hashMap);
            }

        }catch (Exception ignored){}

        return  startId;
    }
    @Override
    public void onDestroy() {
        Log.d("xdddddd","onDestroy");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void sendOnline(HashMap<String,String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().sendOnlineOffline(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                stopSelf();
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                stopSelf();
            }
        });
    }

    private void postLikeDislike(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().postJobAcceptReject(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                stopSelf();
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                stopSelf();
            }
        });
    }

    }
