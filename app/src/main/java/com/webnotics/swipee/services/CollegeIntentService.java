package com.webnotics.swipee.services;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.room_database.College_model;
import com.webnotics.swipee.room_database.College_room_abstract;
import com.webnotics.swipee.room_database.Location_model;
import com.webnotics.swipee.room_database.Location_room_abstract;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollegeIntentService extends IntentService {

    public CollegeIntentService() {
        super("CollegeIntentService");
    }
   boolean location=false;
   boolean college=false;
    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatout = new SimpleDateFormat("dd MM yyyy");
            if (TextUtils.isEmpty(Config.GetCollegeRefreshDate())){
                Log.d("hhhhh","Hit from empty");
                callCollegeList();
            }else {
                Date d1   = format.parse(Config.GetCollegeRefreshDate());
                Date d2 = format.parse(Calendar.getInstance().getTime().toString());
                if (d1!=null && d2!=null){
                    String date1=formatout.format(d1);
                    String date2=formatout.format(d2);
                    Date final1=formatout.parse(date1);
                    Date final2=formatout.parse(date2);
                    if (final1!=null && final2!=null){
                        if(final1.compareTo(final2) != 0) {
                            Log.d("hhhhh","Hit from date");
                            callCollegeList();
                        }else {
                            college=true;
                        }
                    }else {
                        Log.d("hhhhh","Hit from null");
                        callCollegeList();
                    }
                }else callCollegeList();



            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
            SimpleDateFormat formatout = new SimpleDateFormat("dd MM yyyy");
            if (TextUtils.isEmpty(Config.GetLocationRefreshDate())){
                callLocationList();
            }else {
                Date d1   = format.parse(Config.GetLocationRefreshDate());
                Date d2 = format.parse(Calendar.getInstance().getTime().toString());
                if (d1!=null && d2!=null){
                    String date1=formatout.format(d1);
                    String date2=formatout.format(d2);
                    Date final1=formatout.parse(date1);
                    Date final2=formatout.parse(date2);
                    if (final1!=null && final2!=null){
                        if(final1.compareTo(final2) != 0) {
                            Log.d("hhhhh","Hit from date");
                            callLocationList();
                        }else {
                            location=true;
                        }
                    }else {
                        Log.d("hhhhh","Hit from null");
                        callLocationList();
                    }
                }else    callLocationList();

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

       if (location&& college)
           stopSelf();
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
                            College_room_abstract.getDatabase(getBaseContext()).college_room_interface().insertData(new College_model(university_college_id,university_college_name,selected?1:0));
                        }
                        Config.SetCollegeRefreshDate(Calendar.getInstance().getTime().toString());
                        college=true;
                        if (location){
                            stopSelf();
                        }
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });
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
                                Location_room_abstract.getDatabase(getBaseContext()).location_room_interface().insertData(new Location_model(location_id,location_name,state_name,0));
                            }
                            Config.SetLocationRefreshDate(Calendar.getInstance().getTime().toString());
                            location=true;
                            if (college){
                                stopSelf();
                            }
                        }

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
