package com.webnotics.swipee.activity.Seeker;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.adapter.seeeker.AppliedJobsAdapter;
import com.webnotics.swipee.model.seeker.AppliedJobData;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.rest.Rest;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SavedJobsActivity extends AppCompatActivity {
    Rest rest;
    Context mContext;
    ImageView iv_back;
    TextView tv_nodata;
    RecyclerView rv_savedjob;
    LinearLayout ll_nodata;
    @SuppressLint("StaticFieldLeak")
    public static SavedJobsActivity instance;
    private AppliedJobsAdapter appliedJobsAdapter;
    ArrayList<AppliedJobData> appliedJobDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        setContentView(R.layout.activity_saved_jobs2);
        mContext = this;
        instance = this;
        rest = new Rest(mContext);
        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            getSavedJobs();
        } else rest.AlertForInternet();

        iv_back = findViewById(R.id.iv_back);
        tv_nodata = findViewById(R.id.tv_nodata);
        ll_nodata = findViewById(R.id.ll_nodata);
        rv_savedjob = findViewById(R.id.rv_savedjob);

        iv_back.setOnClickListener(v -> finish());
    }

    private void getSavedJobs() {
        SwipeeApiClient.swipeeServiceInstance().getSavedJobs(Config.GetUserToken()).enqueue(new Callback<JsonObject>() {
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
                        JsonArray job_data = dataObject.has("job_data") ? dataObject.get("job_data").getAsJsonArray() : new JsonArray();
                        if (job_data.size() > 0) {
                            appliedJobDataList = new ArrayList<>();
                            for (int i = 0; i < job_data.size(); i++) {
                                JsonObject object = job_data.get(i).getAsJsonObject();
                                String job_post_id = object.has("job_post_id") ? object.get("job_post_id").isJsonNull() ? "" : object.get("job_post_id").getAsString() : "";
                                String company_name = object.has("company_name") ? object.get("company_name").isJsonNull() ? "" : object.get("company_name").getAsString() : "";
                                String company_logo = object.has("company_logo") ? object.get("company_logo").isJsonNull() ? "" : object.get("company_logo").getAsString() : "";
                                String job_title = object.has("job_title") ? object.get("job_title").isJsonNull() ? "" : object.get("job_title").getAsString() : "";
                                String job_experience = object.has("job_experience") ? object.get("job_experience").isJsonNull() ? "" : object.get("job_experience").getAsString() : "";
                                String job_type = object.has("job_type") ? object.get("job_type").isJsonNull() ? "" : object.get("job_type").getAsString() : "";
                                String job_city = object.has("job_city") ? object.get("job_city").isJsonNull() ? "" : object.get("job_city").getAsString() : "";
                                String job_state = object.has("job_state") ? object.get("job_state").isJsonNull() ? "" : object.get("job_state").getAsString() : "";
                                String job_country = object.has("job_country") ? object.get("job_country").isJsonNull() ? "" : object.get("job_country").getAsString() : "";
                                String job_skills = object.has("job_skills") ? object.get("job_skills").isJsonNull() ? "" : object.get("job_skills").getAsString() : "";
                                String user_job_status = object.has("user_job_status") ? object.get("user_job_status").isJsonNull() ? "" : object.get("user_job_status").getAsString() : "";
                                String company_action_status = "";
                                AppliedJobData jobData = new AppliedJobData(job_post_id, company_name, company_logo, job_title, job_experience, job_type, job_city,
                                        job_state, job_country, job_skills, user_job_status, company_action_status);
                                appliedJobDataList.add(appliedJobDataList.size(), jobData);
                            }
                            rv_savedjob.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            appliedJobsAdapter = new AppliedJobsAdapter(mContext, appliedJobDataList);
                            rv_savedjob.setAdapter(appliedJobsAdapter);
                            rv_savedjob.setVisibility(View.VISIBLE);
                            ll_nodata.setVisibility(View.GONE);
                        } else {
                            tv_nodata.setText(response.body().get("message").getAsString());
                            tv_nodata.setVisibility(View.VISIBLE);
                            ll_nodata.setVisibility(View.VISIBLE);
                            rv_savedjob.setVisibility(View.GONE);
                        }
                    } else {
                        tv_nodata.setText(response.body().get("message").getAsString());
                        tv_nodata.setVisibility(View.VISIBLE);
                        ll_nodata.setVisibility(View.VISIBLE);
                        rv_savedjob.setVisibility(View.GONE);
                    }
                } else {
                    AppController.dismissProgressdialog();
                    rest.showToast("Something went wrong");
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });

    }

    public void callSaveJobService(String job_post_id, int pos) {
        AppController.ShowDialogue("", mContext);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
        hashMap.put(ParaName.KEY_JOBPOSTID, job_post_id);
        hashMap.put(ParaName.KEY_SAVESTATUS, "0");

        SwipeeApiClient.swipeeServiceInstance().postSaveJob(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 200 && response.body().get("status").getAsBoolean()) {
                        if (appliedJobDataList.size() > 0)
                            appliedJobDataList.remove(pos);
                        if (appliedJobsAdapter != null)
                            appliedJobsAdapter.notifyDataSetChanged();
                    } else if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    }


                } else {
                    AppController.dismissProgressdialog();
                    rest.showToast("Something went wrong");
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });


    }
}