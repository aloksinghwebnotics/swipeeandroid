package com.webnotics.swipee.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.room_database.Location_model;
import com.webnotics.swipee.room_database.Location_room_abstract;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyIntentService extends IntentService {

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // this method is called in background thread
        callLocationList();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void callLocationList() {
        SwipeeApiClient.swipeeServiceInstance().getLocation(Config.GetUserToken()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    if (response.body().get("code").getAsInt() == 200) {
                        JsonArray mArrayListData = responseBody.has("data") ? responseBody.get("data").getAsJsonArray() : new JsonArray();
                        if (mArrayListData.size()>0){
                            for (int i = 0; i < mArrayListData.size(); i++) {
                                String location_id=mArrayListData.get(i).getAsJsonObject().get("location_id").getAsString();
                                String location_name=mArrayListData.get(i).getAsJsonObject().get("location_name").getAsString();
                                String state_name=mArrayListData.get(i).getAsJsonObject().get("state_name").getAsString();
                                Location_room_abstract.getDatabase(getApplicationContext()).location_room_interface().insertData(new Location_model(location_id,location_name,state_name,0));
                            }
                            Config.SetLocationRefreshDate(Calendar.getInstance().getTime().toString());
                            stopSelf();
                        }

                    }


                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                stopSelf();
                AppController.dismissProgressdialog();
            }
        });
    }
}
