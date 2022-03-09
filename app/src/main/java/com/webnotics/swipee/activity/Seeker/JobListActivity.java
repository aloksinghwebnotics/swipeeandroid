package com.webnotics.swipee.activity.Seeker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.webnotics.swipee.adapter.seeeker.JobListAdapter;
import com.webnotics.swipee.model.seeker.AppliedJobData;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.rest.Rest;

import java.util.ArrayList;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobListActivity extends AppCompatActivity {
    Rest rest;
    Context mContext;
    ImageView iv_back;
    TextView tv_nodata;
    RecyclerView rv_data;
    String companyId;
    LinearLayout ll_nodata;
    @SuppressLint("StaticFieldLeak")
    public static JobListActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        setContentView(R.layout.activity_job_list);
        mContext = this;
        instance = this;
        rest = new Rest(mContext);
        if (getIntent() != null) companyId = getIntent().getStringExtra("id") != null ? getIntent().getStringExtra("id") : "";
        if (rest.isInterentAvaliable()) {
            if (!TextUtils.isEmpty(companyId)) {
                AppController.ShowDialogue("", mContext);
                getCompanyJobList();
            }
        } else rest.AlertForInternet();

        iv_back = findViewById(R.id.iv_back);
        tv_nodata = findViewById(R.id.tv_nodata);
        ll_nodata = findViewById(R.id.ll_nodata);
        rv_data = findViewById(R.id.rv_data);

        iv_back.setOnClickListener(v -> finish());
    }

    private void getCompanyJobList() {
        SwipeeApiClient.swipeeServiceInstance().getCompanyJobList(Config.GetUserToken(), companyId).enqueue(new Callback<JsonObject>() {
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
                        JsonArray job_data = dataObject.has("jobs_listing") ? dataObject.get("jobs_listing").getAsJsonArray() : new JsonArray();
                        if (job_data.size() > 0) {
                            ArrayList<AppliedJobData> appliedJobDataList = new ArrayList<>();
                            IntStream.range(0, job_data.size()).mapToObj(i -> job_data.get(i).getAsJsonObject()).forEach(object -> {
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
                            });
                            rv_data.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            JobListAdapter jobsAdapter = new JobListAdapter(mContext, appliedJobDataList);
                            rv_data.setAdapter(jobsAdapter);
                            ll_nodata.setVisibility(View.GONE);
                        } else {
                            tv_nodata.setText(response.body().get("message").getAsString());
                            tv_nodata.setVisibility(View.VISIBLE);
                            ll_nodata.setVisibility(View.VISIBLE);
                            rv_data.setVisibility(View.GONE);
                        }
                    } else {
                        tv_nodata.setText(response.body().get("message").getAsString());
                        tv_nodata.setVisibility(View.VISIBLE);
                        ll_nodata.setVisibility(View.VISIBLE);
                        rv_data.setVisibility(View.GONE);
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