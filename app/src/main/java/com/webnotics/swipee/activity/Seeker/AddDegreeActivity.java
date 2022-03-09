package com.webnotics.swipee.activity.Seeker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.webnotics.swipee.adapter.seeeker.DegreeAdapter;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.rest.Rest;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddDegreeActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    Context mContext;
    Rest rest;
    ListView mListView;
    EditText et_search;
    ImageView iv_back;
    private DegreeAdapter degreeAdapter;
    private String degreeId = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_degree);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));

        mContext = this;
        rest = new Rest(mContext);

        if (getIntent() != null)
            degreeId = getIntent().getStringExtra("degreeId") != null ? getIntent().getStringExtra("degreeId") : "";
        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            callDegreeList();
        } else rest.AlertForInternet();

        mListView = findViewById(R.id.mlistview);
        et_search = findViewById(R.id.et_search);
        iv_back = findViewById(R.id.iv_back);

        iv_back.setOnClickListener(this);
        et_search.addTextChangedListener(this);

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (degreeAdapter != null)
            degreeAdapter.getFilter().filter(s.toString().trim());
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

    private void callDegreeList() {
        SwipeeApiClient.swipeeServiceInstance().getDegree().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    JsonArray mArrayListData = responseBody.has("data") ? responseBody.get("data").getAsJsonArray() : new JsonArray();
                    ArrayList<JsonObject> jsonObjectArrayList = IntStream.range(0, mArrayListData.size()).mapToObj(i -> mArrayListData.get(i).getAsJsonObject()).collect(Collectors.toCollection(ArrayList::new));
                    if (mArrayListData.size() > 0) {
                        degreeAdapter = new DegreeAdapter(AddDegreeActivity.this, jsonObjectArrayList);
                        mListView.setAdapter(degreeAdapter);
                    }
                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });
    }

    public void selectedData(String name, String id, String level) {
        startActivity(new Intent(mContext, AddEducation.class).putExtra("from", "degree").putExtra("level", level).putExtra("id", id).putExtra("name", name));
        finish();
    }

}