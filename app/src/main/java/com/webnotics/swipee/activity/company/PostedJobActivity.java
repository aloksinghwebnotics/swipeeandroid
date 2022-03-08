package com.webnotics.swipee.activity.company;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.adapter.company.PostedJobAdapter;
import com.webnotics.swipee.model.company.PostedJobModel;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.rest.Rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostedJobActivity extends AppCompatActivity {
    Rest rest;
    Context mContext;
    ImageView iv_back;
    LinearLayout ll_nodata;
    TextView tv_nodata;
    RecyclerView rv_appointment;
    Spinner spn_posted;
    @SuppressLint("StaticFieldLeak")
    public static PostedJobActivity instance;
    private final ArrayList<PostedJobModel.Data> jobList = new ArrayList<>();
    private final ArrayList<PostedJobModel.Data> finalJobList = new ArrayList<>();
    private PostedJobAdapter stateAdapter;
    private boolean isActiveLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_job);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        instance = this;
        rest = new Rest(mContext);


        iv_back = findViewById(R.id.iv_back);
        tv_nodata = findViewById(R.id.tv_nodata);
        spn_posted = findViewById(R.id.spn_posted);
        rv_appointment = findViewById(R.id.rv_appointment);
        ll_nodata = findViewById(R.id.ll_nodata);
        ArrayList<String> postedList = new ArrayList<>();
        postedList.add(0, "All Jobs");
        postedList.add(1, "Active Jobs");
        postedList.add(2, "Closed Jobs");
        postedList.add(3, "Inactive Jobs");
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item, postedList);
        genderAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spn_posted.setAdapter(genderAdapter);
        rv_appointment.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        stateAdapter = new PostedJobAdapter(PostedJobActivity.this, finalJobList);
        rv_appointment.setAdapter(stateAdapter);
        spn_posted.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortedData(spn_posted.getSelectedItemPosition());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        iv_back.setOnClickListener(v -> finish());
    }

    private void sortedData(int position) {
        if (position == 0) {
            if (isActiveLoaded) {
                finalJobList.clear();
                finalJobList.addAll(jobList);
                if (stateAdapter != null)
                    stateAdapter.notifyDataSetChanged();
                if (finalJobList.size() > 0) {
                    tv_nodata.setVisibility(View.GONE);
                    ll_nodata.setVisibility(View.GONE);
                    rv_appointment.setVisibility(View.VISIBLE);
                } else {
                    tv_nodata.setText("No posted jobs data found");
                    rv_appointment.setVisibility(View.GONE);
                    tv_nodata.setVisibility(View.VISIBLE);
                    ll_nodata.setVisibility(View.VISIBLE);
                }
            } else {
                if (rest.isInterentAvaliable()) {
                    AppController.ShowDialogue("", mContext);
                    getActiveJobList("");
                } else rest.AlertForInternet();
            }
        } else if (position == 1) {
            finalJobList.clear();
            IntStream.range(0, jobList.size()).forEach(j -> jobList.get(j).setClose(false));
            for (int i = 0; i < jobList.size(); i++) {
                if (jobList.get(i).getJobTypeCount() == 0) {
                    boolean available = false;
                    for (int k = 0; k < finalJobList.size(); k++) {
                        if (finalJobList.get(k).getJob_post_id().equalsIgnoreCase(jobList.get(i).getJob_post_id())) {
                            available = true;
                            break;
                        }
                    }
                    if (!available)
                        finalJobList.add(finalJobList.size(), jobList.get(i));
                }
            }
            if (stateAdapter != null)
                stateAdapter.notifyDataSetChanged();
            if (finalJobList.size() > 0) {
                tv_nodata.setVisibility(View.GONE);
                ll_nodata.setVisibility(View.GONE);
                rv_appointment.setVisibility(View.VISIBLE);
            } else {
                tv_nodata.setText("No active jobs data found");
                rv_appointment.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
                ll_nodata.setVisibility(View.VISIBLE);
            }
        } else if (position == 2) {
            finalJobList.clear();
            IntStream.range(0, jobList.size()).forEach(j -> jobList.get(j).setClose(false));
            for (int i = 0; i < jobList.size(); i++) {
                if (jobList.get(i).getJobTypeCount() == 1) {
                    boolean available = false;
                    for (int k = 0; k < finalJobList.size(); k++) {
                        if (finalJobList.get(k).getJob_post_id().equalsIgnoreCase(jobList.get(i).getJob_post_id())) {
                            available = true;
                            break;
                        }
                    }
                    if (!available)
                        finalJobList.add(finalJobList.size(), jobList.get(i));
                }
            }
            if (stateAdapter != null)
                stateAdapter.notifyDataSetChanged();
            if (finalJobList.size() > 0) {
                tv_nodata.setVisibility(View.GONE);
                ll_nodata.setVisibility(View.GONE);
                rv_appointment.setVisibility(View.VISIBLE);
            } else {
                tv_nodata.setText("No closed jobs data found");
                rv_appointment.setVisibility(View.GONE);
                tv_nodata.setVisibility(View.VISIBLE);
                ll_nodata.setVisibility(View.VISIBLE);
            }
        } else if (position == 3) {
            finalJobList.clear();
            IntStream.range(0, jobList.size()).forEach(j -> jobList.get(j).setClose(true));
            for (int i = 0; i < jobList.size(); i++) {
                if (jobList.get(i).getJobTypeCount() == 2) {
                    boolean available = false;
                    for (int k = 0; k < finalJobList.size(); k++) {
                        if (finalJobList.get(k).getJob_post_id().equalsIgnoreCase(jobList.get(i).getJob_post_id())) {
                            available = true;
                            break;
                        }
                    }
                    if (!available)
                        finalJobList.add(finalJobList.size(), jobList.get(i));
                }
            }
            if (stateAdapter != null)
                stateAdapter.notifyDataSetChanged();
            if (finalJobList.size() > 0) {
                tv_nodata.setVisibility(View.GONE);
                ll_nodata.setVisibility(View.GONE);
                rv_appointment.setVisibility(View.VISIBLE);
            } else {
                rv_appointment.setVisibility(View.GONE);
                tv_nodata.setText("No inactive jobs data found");
                tv_nodata.setVisibility(View.VISIBLE);
                ll_nodata.setVisibility(View.VISIBLE);
            }
        }
    }


    public void setJob(String id, HashMap<String, String> hashMap, int status) {
        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            setJobStatus(hashMap, id, status);
        }
    }

    private void setJobStatus(HashMap<String, String> hashMap, String id, int status) {
        SwipeeApiClient.swipeeServiceInstance().setPostedStatus(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responceBody = response.body();
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (responceBody.get("code").getAsInt() == 200 && responceBody.get("status").getAsBoolean()) {
                        for (int i = 0; i < jobList.size(); i++) {
                            if (jobList.get(i).getJob_post_id().equalsIgnoreCase(id)) {
                                jobList.get(i).setJobTypeCount(status);
                                if (status == 1) {
                                    jobList.get(i).setHiring_status_type("Closed");
                                } else if (status == 0) {
                                    jobList.get(i).setHiring_status_type("Open");
                                    jobList.get(i).setHiring_status_type("Active");
                                } else if (status == 2) {
                                    jobList.get(i).setHiring_status_type("Open");
                                    jobList.get(i).setHiring_status_type("InActive");
                                }
                            }
                        }
                        rest.showToast(responceBody.get("message").getAsString());
                        sortedData(spn_posted.getSelectedItemPosition());
                    } else {
                        rest.showToast(responceBody.get("message").getAsString());
                    }

                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();

            }
        });
    }


    private void getActiveJobList(String filter) {
        SwipeeApiClient.swipeeServiceInstance().postedJobs(Config.GetUserToken(), filter).enqueue(new Callback<PostedJobModel>() {
            @Override
            public void onResponse(@NonNull Call<PostedJobModel> call, @NonNull Response<PostedJobModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    PostedJobModel responceBody = response.body();
                    if (responceBody.getCode() == 203) {
                        rest.showToast(responceBody.getMessage());
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (responceBody.getCode() == 200 && responceBody.isStatus()) {
                        tv_nodata.setVisibility(View.GONE);
                        ll_nodata.setVisibility(View.GONE);
                        rv_appointment.setVisibility(View.VISIBLE);
                        ArrayList<PostedJobModel.Data> dataArrayList = new ArrayList<>(responceBody.getData().getFilter_jobs());
                        for (int i = 0; i < dataArrayList.size(); i++) {
                            if (dataArrayList.get(i).getHiring_status_type().equalsIgnoreCase("Closed"))
                                dataArrayList.get(i).setJobTypeCount(1);
                            else if (dataArrayList.get(i).getHiring_status_type().equalsIgnoreCase("Open")) {
                                if (dataArrayList.get(i).getJob_status_type().equalsIgnoreCase("Active"))
                                    dataArrayList.get(i).setJobTypeCount(0);
                                else dataArrayList.get(i).setJobTypeCount(2);

                            }

                        }
                        jobList.addAll(dataArrayList);
                        finalJobList.clear();
                        finalJobList.addAll(dataArrayList);
                        if (stateAdapter != null) {
                            stateAdapter.notifyDataSetChanged();
                        }
                        isActiveLoaded = true;
                        if (finalJobList.size() > 0) {
                            tv_nodata.setVisibility(View.GONE);
                            ll_nodata.setVisibility(View.GONE);
                            rv_appointment.setVisibility(View.VISIBLE);
                        } else {
                            rv_appointment.setVisibility(View.GONE);
                            tv_nodata.setVisibility(View.VISIBLE);
                            ll_nodata.setVisibility(View.VISIBLE);
                            tv_nodata.setText("You haven't posted any job yet.");
                        }

                    } else {
                        rv_appointment.setVisibility(View.GONE);
                        tv_nodata.setVisibility(View.VISIBLE);
                        ll_nodata.setVisibility(View.VISIBLE);
                        tv_nodata.setText(responceBody.getMessage());
                    }

                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<PostedJobModel> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();

            }
        });
    }


}