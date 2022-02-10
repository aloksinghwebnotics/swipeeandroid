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
import com.webnotics.swipee.room_database.College_model;
import com.webnotics.swipee.room_database.College_room_abstract;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollegeIntentService extends IntentService {

    public CollegeIntentService() {
        super("CollegeIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        callCollegeList();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void callCollegeList() {
        SwipeeApiClient.swipeeServiceInstance().getCollege().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    JsonArray mArrayListData = responseBody.has("data") ? responseBody.get("data").getAsJsonArray() : new JsonArray();
                    if (mArrayListData.size()>0){
                        for (int i = 0; i < mArrayListData.size(); i++) {
                            JsonObject object=mArrayListData.get(i).getAsJsonObject();
                            String university_college_id=object.has("university_college_id")?!object.get("university_college_id").isJsonNull()?object.get("university_college_id").getAsString():"":"";
                            String university_college_name=object.has("university_college_name")?!object.get("university_college_name").isJsonNull()?object.get("university_college_name").getAsString():"":"";
                            boolean selected= object.has("selected") && (!object.get("selected").isJsonNull() && object.get("selected").getAsBoolean());
                            College_room_abstract.getDatabase(getApplicationContext()).college_room_interface().insertData(new College_model(university_college_id,university_college_name,selected?1:0));
                        }
                        Config.SetCollegeRefreshDate(Calendar.getInstance().getTime().toString());
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });
    }
}
