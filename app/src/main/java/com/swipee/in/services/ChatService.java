package com.swipee.in.services;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.swipee.in.rest.ParaName;
import com.swipee.in.rest.SwipeeApiClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStart(intent, startId);
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(ParaName.KEY_MSGID, intent.getStringExtra(ParaName.KEY_MSGID));
            hashMap.put(ParaName.KEY_RECEIVERID, intent.getStringExtra(ParaName.KEY_RECEIVERID));
            setMsgDeliver(hashMap,startId);
        }catch (Exception e){}
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void setMsgDeliver(HashMap<String, String> hashMap, int startId) {
        SwipeeApiClient.swipeeServiceInstance().setMsgDeliver(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                ChatService.this.stopSelf(startId);
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                ChatService.this.stopSelf(startId);
            }
        });
    }
}