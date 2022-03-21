package com.webnotics.swipee.activity.company;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.adapter.company.LikedUserAdapter;
import com.webnotics.swipee.model.company.LikedUserModel;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.rest.Rest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchedUserActivity extends AppCompatActivity {
    Rest rest;
    Context mContext;
    ImageView iv_back;
    TextView tv_nodata, tv_title;
    RecyclerView rv_Likejob;
    @SuppressLint("StaticFieldLeak")
    public static MatchedUserActivity instance;
    LinearLayout ll_nodata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        setContentView(R.layout.activity_mached_company);
        mContext = this;
        instance = this;
        rest = new Rest(mContext);
        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            getMatchedJobs();
        } else rest.AlertForInternet();

        iv_back = findViewById(R.id.iv_back);
        tv_nodata = findViewById(R.id.tv_nodata);
        rv_Likejob = findViewById(R.id.rv_Likejob);
        tv_title = findViewById(R.id.tv_title);
        ll_nodata = findViewById(R.id.ll_nodata);
        tv_title.setText("Matched User");

        iv_back.setOnClickListener(v -> finish());
    }

    private void getMatchedJobs() {
        SwipeeApiClient.swipeeServiceInstance().getMatchedUserList(Config.GetUserToken()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (response.body().get("status").getAsBoolean()) {
                        JsonObject jsonObject = response.body();
                        JsonObject dataObject = jsonObject.has("data") ? jsonObject.get("data").getAsJsonObject() : new JsonObject();
                        JsonArray job_data = dataObject.has("users_listing") ? dataObject.get("users_listing").getAsJsonArray() : new JsonArray();
                        if (job_data.size() > 0) {
                            ArrayList<LikedUserModel> likedDataList = new ArrayList<>();
                            for (int i = 0; i < job_data.size(); i++) {
                                JsonObject object = job_data.get(i).getAsJsonObject();
                                String user_id = object.has("user_id") ? object.get("user_id").isJsonNull() ? "" : object.get("user_id").getAsString() : "";
                                String first_name = object.has("first_name") ? object.get("first_name").isJsonNull() ? "" : object.get("first_name").getAsString() : "";
                                String last_name = object.has("last_name") ? object.get("last_name").isJsonNull() ? "" : object.get("last_name").getAsString() : "";
                                String user_profile = object.has("user_profile") ? object.get("user_profile").isJsonNull() ? "" : object.get("user_profile").getAsString() : "";
                                String country = object.has("country") ? object.get("country").isJsonNull() ? "" : object.get("country").getAsString() : "";
                                String state = object.has("state") ? object.get("state").isJsonNull() ? "" : object.get("state").getAsString() : "";
                                String city = object.has("city") ? object.get("city").isJsonNull() ? "" : object.get("city").getAsString() : "";
                                JsonArray skill_name = object.has("skill_name") ? object.get("skill_name").isJsonNull() ? new JsonArray() : object.get("skill_name").getAsJsonArray() : new JsonArray();
                                String job_id = object.has("job_id") ? object.get("job_id").isJsonNull() ? "" : object.get("job_id").getAsString() : "";
                                String apply_id = object.has("apply_id") ? object.get("apply_id").isJsonNull() ? "" : object.get("apply_id").getAsString() : "";
                                String skill_name1 = "";
                                if (skill_name.size() > 0) {
                                    for (int j = 0; j < skill_name.size(); j++)
                                        skill_name1 = skill_name1 + skill_name.get(j).getAsString() + ((j == skill_name.size() - 1) ? "" : ", ");
                                } else skill_name1 = "";
                                LikedUserModel jobData = new LikedUserModel(user_id, first_name, last_name, user_profile, skill_name1, country, state, city, job_id, apply_id);
                                likedDataList.add(likedDataList.size(), jobData);
                            }
                            rv_Likejob.setLayoutManager(new GridLayoutManager(mContext, 2));
                            LikedUserAdapter appliedJobsAdapter = new LikedUserAdapter(mContext, likedDataList);
                            rv_Likejob.setAdapter(appliedJobsAdapter);
                            rv_Likejob.setVisibility(View.VISIBLE);
                            ll_nodata.setVisibility(View.GONE);
                        } else {
                            tv_nodata.setText(response.body().get("message").getAsString());
                            tv_nodata.setVisibility(View.VISIBLE);
                            ll_nodata.setVisibility(View.VISIBLE);
                            rv_Likejob.setVisibility(View.GONE);
                        }
                    } else {
                        tv_nodata.setText(response.body().get("message").getAsString());
                        tv_nodata.setVisibility(View.VISIBLE);
                        ll_nodata.setVisibility(View.VISIBLE);
                        rv_Likejob.setVisibility(View.GONE);
                    }
                } else rest.showToast("Something went wrong");
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });

    }
}