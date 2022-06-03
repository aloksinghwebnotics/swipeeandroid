package com.swipee.in.services;

import android.app.Service;
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

    String tag="TestService";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("xdddddd","on creat");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStart(intent, startId);
        try {
            HashMap<String,String> hashMap=new HashMap<>();
            hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
            hashMap.put(ParaName.KEY_ISONLINE,"N");
            sendOnline(hashMap);
        }catch (Exception e){}

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
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 200 && response.body().get("status").getAsBoolean()) {
                        Config.setUserStats(true);
                    }
                }
                stopSelf();
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                stopSelf();
            }
        });
    }
    }
