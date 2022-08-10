package com.swipee.in.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
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
import com.swipee.in.R;
import com.swipee.in.UrlManager.AppController;
import com.swipee.in.UrlManager.Config;
import com.swipee.in.adapter.BlockedUserAdapter;
import com.swipee.in.model.company.LikedUserModel;
import com.swipee.in.rest.Rest;
import com.swipee.in.rest.SwipeeApiClient;

import java.util.ArrayList;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlockedUsers extends AppCompatActivity {
    Rest rest;
    Context mContext;
    ImageView iv_back;
    TextView tv_nodata, tv_title;
    RecyclerView rv_Likejob;
    @SuppressLint("StaticFieldLeak")
    public static BlockedUsers instance;
    LinearLayout ll_nodata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocked_users);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));

        mContext = this;
        instance = this;
        rest = new Rest(mContext);
        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            if (Config.isSeeker()) getBlockedCompanies();
            else getBlockedUsers();
        } else rest.AlertForInternet();

        iv_back = findViewById(R.id.iv_back);
        tv_nodata = findViewById(R.id.tv_nodata);
        rv_Likejob = findViewById(R.id.rv_Likejob);
        tv_title = findViewById(R.id.tv_title);
        ll_nodata = findViewById(R.id.ll_nodata);
        tv_title.setText(Config.isSeeker()?"Blocked Jobs":"Blocked Users");

        iv_back.setOnClickListener(v -> finish());
    }

    private void getBlockedUsers() {
        SwipeeApiClient.swipeeServiceInstance().getBlockedUserList(Config.GetUserToken()).enqueue(new Callback<JsonObject>() {
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
                        JsonArray dataObject = jsonObject.has("data") ? jsonObject.get("data").getAsJsonArray() : new JsonArray();
                        if (dataObject.size() > 0) {
                                                  ArrayList<LikedUserModel> likedDataList = new ArrayList<>();
                            for (int i = 0; i < dataObject.size(); i++) {
                                JsonObject object = dataObject.get(i).getAsJsonObject();
                                String user_id = object.has("user_id") ? object.get("user_id").isJsonNull() ? "" : object.get("user_id").getAsString() : "";
                                String first_name = object.has("first_name") ? object.get("first_name").isJsonNull() ? "" : object.get("first_name").getAsString() : "";
                                String last_name = object.has("last_name") ? object.get("last_name").isJsonNull() ? "" : object.get("last_name").getAsString() : "";
                                String user_profile = object.has("user_profile") ? object.get("user_profile").isJsonNull() ? "" : object.get("user_profile").getAsString() : "";
                                String country = object.has("country") ? object.get("country").isJsonNull() ? "" : object.get("country").getAsString() : "";
                                String state = object.has("state") ? object.get("state").isJsonNull() ? "" : object.get("state").getAsString() : "";
                                String city = object.has("city") ? object.get("city").isJsonNull() ? "" : object.get("city").getAsString() : "";
                                JsonArray skill_name = object.has("skill_name") ? object.get("skill_name").isJsonNull() ? new JsonArray() : object.get("skill_name").getAsJsonArray() : new JsonArray();
                                String skill_name1 = "";
                                if (skill_name.size() > 0) {
                                    for (int j = 0; j < skill_name.size(); j++)
                                        skill_name1 = skill_name1 + skill_name.get(j).getAsString() + ((j == skill_name.size() - 1) ? "" : ", ");
                                } else skill_name1 = "";
                                LikedUserModel jobData = new LikedUserModel(user_id, first_name, last_name, user_profile, skill_name1, country, state, city, "", "");
                                likedDataList.add(likedDataList.size(), jobData);
                            }
                            rv_Likejob.setLayoutManager(new GridLayoutManager(mContext, 2));
                            BlockedUserAdapter appliedJobsAdapter = new BlockedUserAdapter(BlockedUsers.this, likedDataList);
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

    private void getBlockedCompanies() {
        SwipeeApiClient.swipeeServiceInstance().getBlockedCompanyList(Config.GetUserToken()).enqueue(new Callback<JsonObject>() {
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
                        JsonArray dataObject = jsonObject.has("data") ? jsonObject.get("data").getAsJsonArray() : new JsonArray();
                        if (dataObject.size() > 0) {                
                            ArrayList<LikedUserModel> likedDataList = new ArrayList<>();
                            IntStream.range(0, dataObject.size()).mapToObj(i -> dataObject.get(i).getAsJsonObject()).forEach(object -> {
                                String user_id = object.has("company_id") ? object.get("company_id").isJsonNull() ? "" : object.get("company_id").getAsString() : "";
                                String job_post_id = object.has("job_post_id") ? object.get("job_post_id").isJsonNull() ? "" : object.get("job_post_id").getAsString() : "";
                                String job_title = object.has("job_title") ? object.get("job_title").isJsonNull() ? "" : object.get("job_title").getAsString() : "";
                                String last_name = object.has("company_last_name") ? object.get("company_last_name").isJsonNull() ? "" : object.get("company_last_name").getAsString() : "";
                                String user_profile = object.has("user_profile") ? object.get("user_profile").isJsonNull() ? "" : object.get("user_profile").getAsString() : "";
                                String country = object.has("country") ? object.get("country").isJsonNull() ? "" : object.get("country").getAsString() : "";
                                String state = object.has("state") ? object.get("state").isJsonNull() ? "" : object.get("state").getAsString() : "";
                                String city = object.has("city") ? object.get("city").isJsonNull() ? "" : object.get("city").getAsString() : "";
                                String skill_name1 = "";
                                LikedUserModel jobData = new LikedUserModel(user_id, job_title, last_name, user_profile, skill_name1, country, state, city, job_post_id, "");
                                likedDataList.add(likedDataList.size(), jobData);
                            });
                            rv_Likejob.setLayoutManager(new GridLayoutManager(mContext, 2));
                            BlockedUserAdapter appliedJobsAdapter = new BlockedUserAdapter(BlockedUsers.this, likedDataList);
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

    public void unBlockUser(String id,String first_name,String job_id) {
        Dialog progressdialog = new Dialog(mContext);
        progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressdialog.setContentView(R.layout.cancel_appointment_popup);
        progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        progressdialog.getWindow().setAttributes(lp);
        TextView tv_title = progressdialog.findViewById(R.id.tv_title);
        TextView tv_detail = progressdialog.findViewById(R.id.tv_detail);
        tv_title.setText(String.format("Unblock %s", first_name));
        tv_detail.setText(String.format("Are you sure, you want to unblock %s?", first_name));

        progressdialog.findViewById(R.id.tv_yes).setOnClickListener(v -> {
            progressdialog.dismiss();
            if (!Config.isSeeker()){
                AppController.ShowDialogue("", mContext);
                unblockUser(id);
            }else if (Config.isSeeker()){
                AppController.ShowDialogue("", mContext);
                unblockCompanies(id,job_id);
            }
        });
        progressdialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> progressdialog.dismiss());
        try {
            progressdialog.show();
        } catch (Exception ignored) {}

    }

    private void unblockUser(String id) {
        SwipeeApiClient.swipeeServiceInstance().unblockUser(Config.GetUserToken(), id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        setBackPressed();
                        AppController.loggedOut(mContext);
                    } else if (response.body().get("code").getAsInt() == 200 && response.body().get("status").getAsBoolean()) {
                        setBackPressed();
                    } else rest.showToast(response.body().get("message").getAsString());
                } else rest.showToast("Something went wrong");
            }
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }

    private void unblockCompanies(String id,String job_id) {
        SwipeeApiClient.swipeeServiceInstance().unblockJob(Config.GetUserToken(), id,job_id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        setBackPressed();
                        AppController.loggedOut(mContext);
                    } else if (response.body().get("code").getAsInt() == 200 && response.body().get("status").getAsBoolean()) {
                        setBackPressed();
                    } else rest.showToast(response.body().get("message").getAsString());
                } else rest.showToast("Something went wrong");
            }
            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });

    }

    private void setBackPressed() {
        if (SettingsActivity.instance!=null) SettingsActivity.instance.finish();
        finish();
    }

}