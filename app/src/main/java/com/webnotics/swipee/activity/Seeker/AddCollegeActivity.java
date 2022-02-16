package com.webnotics.swipee.activity.Seeker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.adapter.seeeker.CollegeAdapter;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.room_database.College_room_abstract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCollegeActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    Context mContext;
    Rest rest;
    ListView mListView;
    EditText et_search;
    ImageView iv_back;
    private CollegeAdapter collegeAdapter;
    private String collegeId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_college);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));

        mContext = this;
        rest = new Rest(mContext);
        if (getIntent() != null) {
            collegeId = getIntent().getStringExtra("collegeId") != null ? getIntent().getStringExtra("collegeId") : "";
        }

        mListView = findViewById(R.id.mlistview);
        et_search = findViewById(R.id.et_search);
        iv_back = findViewById(R.id.iv_back);

        iv_back.setOnClickListener(this);
        et_search.addTextChangedListener(this);
      /*  ArrayList<JsonObject> collegeList=callCollegeFromDB();
        if (collegeList.size()>0){
            collegeAdapter = new CollegeAdapter(AddCollegeActivity.this, collegeList);
            mListView.setAdapter(collegeAdapter);
        }else {
            if (rest.isInterentAvaliable()) {
                AppController.ShowDialogue("", mContext);
                callCollegeList();
            } else rest.AlertForInternet();

        }*/


        String data=  readFromFile();
        try {
            JSONArray jarray = new JSONArray(data);
            if (jarray.length()>0){
                  ArrayList<JsonObject> collegeList=new ArrayList<>();
                for (int i = 0; i < jarray.length(); i++) {
                    JSONObject jsonObject= jarray.getJSONObject(i);
                    String university_college_id=jsonObject.getString("university_college_id");
                    String university_college_name=jsonObject.getString("university_college_name");
                    boolean selected=jsonObject.getBoolean("selected");
                    JsonObject object=new JsonObject();
                    object.addProperty("university_college_id",university_college_id);
                    object.addProperty("university_college_name",university_college_name);
                    object.addProperty("selected",selected);
                    collegeList.add(collegeList.size(),object);
                }

                collegeAdapter = new CollegeAdapter(AddCollegeActivity.this, collegeList);
                mListView.setAdapter(collegeAdapter);



            }else {
                if (rest.isInterentAvaliable()) {
                    AppController.ShowDialogue("", mContext);
                    callCollegeList();
                } else {
                    rest.AlertForInternet();
                }
            }
        } catch (JSONException e) {
            if (rest.isInterentAvaliable()) {
                AppController.ShowDialogue("", mContext);
                callCollegeList();
            } else {
                rest.AlertForInternet();
            }
        }



    }


    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = openFileInput("college.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
                Log.d("skskskksks",ret);
            }
        }
        catch (FileNotFoundException e) {

            Log.e("login activity", "File not found: " + e.toString());
            return ret;
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
            return ret;
        }

        return ret;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (collegeAdapter != null)
            collegeAdapter.getFilter().filter(s.toString().trim());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            default:
                break;

        }

    }

    private void callCollegeList() {
        SwipeeApiClient.swipeeServiceInstance().getCollege().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    JsonArray mArrayListData = responseBody.has("data") ? responseBody.get("data").getAsJsonArray() : new JsonArray();
                    if (mArrayListData.size()>0){
                        writeCollege(mArrayListData.toString());
                        String data=  readFromFile();
                        try {
                            JSONArray jarray = new JSONArray(data);
                            if (jarray.length()>0){
                                ArrayList<JsonObject> collegeList=new ArrayList<>();
                                for (int i = 0; i < jarray.length(); i++) {
                                    JSONObject jsonObject= jarray.getJSONObject(i);
                                    String university_college_id=jsonObject.getString("university_college_id");
                                    String university_college_name=jsonObject.getString("university_college_name");
                                    boolean selected=jsonObject.getBoolean("selected");
                                    JsonObject object=new JsonObject();
                                    object.addProperty("university_college_id",university_college_id);
                                    object.addProperty("university_college_name",university_college_name);
                                    object.addProperty("selected",selected);
                                    collegeList.add(collegeList.size(),object);
                                }

                                collegeAdapter = new CollegeAdapter(AddCollegeActivity.this, collegeList);
                                mListView.setAdapter(collegeAdapter);
                                Config.SetCollegeRefreshDate(Calendar.getInstance().getTime().toString());


                            }
                        } catch (JSONException e) {
                        }

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

    public void selectedData(String name, String id) {
        startActivity(new Intent(mContext, AddEducation.class).putExtra("from", "college").putExtra("id", id).putExtra("name", name));
        finish();
    }
    private void writeCollege(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("college.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private  ArrayList<JsonObject> callCollegeFromDB() {
        ArrayList<JsonObject> temp_prodlist = new ArrayList<>();

        Cursor cursor = College_room_abstract.getDatabase(mContext).college_room_interface().getAllData();
        if (cursor != null) {
            if (cursor.moveToFirst() && cursor.getCount() > 0) {
                try {
                    do {
                         @SuppressLint("Range") String university_college_id = cursor.getString(cursor.getColumnIndex("university_college_id"));
                        @SuppressLint("Range") String university_college_name = cursor.getString(cursor.getColumnIndex("university_college_name"));
                        @SuppressLint("Range") int selected = cursor.getInt(cursor.getColumnIndex("selected"));
                        JsonObject object=new JsonObject();
                        object.addProperty("university_college_id",university_college_id);
                        object.addProperty("university_college_name",university_college_name);
                        object.addProperty("selected", selected == 1);
                        temp_prodlist.add(object);

                    } while (cursor.moveToNext());

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    cursor.close();
                }

            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return temp_prodlist;
    }


}