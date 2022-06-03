package com.swipee.in.activity.company;

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

import com.swipee.in.R;
import com.swipee.in.UrlManager.AppController;
import com.swipee.in.UrlManager.Config;
import com.swipee.in.adapter.company.ResumesAdapter;
import com.swipee.in.model.company.ResumesModel;
import com.swipee.in.rest.Rest;
import com.swipee.in.rest.SwipeeApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResumesActivity extends AppCompatActivity {
   private  final Context mContext=ResumesActivity.this;
    private Rest rest;
    ImageView iv_back;
    TextView tv_nodata, tv_title;
    LinearLayout ll_nodata;
    RecyclerView rv_Likejob;
    @SuppressLint("StaticFieldLeak")
    public static ResumesActivity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumes);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        instance = this;
        rest = new Rest(mContext);
        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            callResumeList();
        } else rest.AlertForInternet();

        iv_back = findViewById(R.id.iv_back);
        tv_nodata = findViewById(R.id.tv_nodata);
        ll_nodata = findViewById(R.id.ll_nodata);
        rv_Likejob = findViewById(R.id.rv_Likejob);
        tv_title = findViewById(R.id.tv_title);

        iv_back.setOnClickListener(v -> finish());
    }

    private void callResumeList() {
        SwipeeApiClient.swipeeServiceInstance().getResumes(Config.GetUserToken()).enqueue(new Callback<ResumesModel>() {
            @Override
            public void onResponse(@NonNull Call<ResumesModel> call, @NonNull Response<ResumesModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    ResumesModel responseBody = response.body();
                    if (responseBody.getCode()==203){
                        rest.showToast(responseBody.getMessage());
                        AppController.loggedOut(mContext);
                        if (CompanyHomeActivity.instance!=null)CompanyHomeActivity.instance.finish();
                        finish();
                    }else if (responseBody.getCode()==200 && responseBody.isStatus()) {
                        if (responseBody.getData().getUsers_listing().size()>0){
                            rv_Likejob.setLayoutManager(new GridLayoutManager(mContext, 2));
                            ResumesAdapter appliedJobsAdapter = new ResumesAdapter(mContext, responseBody.getData().getUsers_listing());
                            rv_Likejob.setAdapter(appliedJobsAdapter);
                            rv_Likejob.setVisibility(View.VISIBLE);
                            ll_nodata.setVisibility(View.GONE);
                        }else {
                            tv_nodata.setText(response.body().getMessage());
                            tv_nodata.setVisibility(View.VISIBLE);
                            ll_nodata.setVisibility(View.VISIBLE);
                            rv_Likejob.setVisibility(View.GONE);
                        }

                    }else{
                        rest.showToast(responseBody.getMessage());
                        finish();
                    }

                } else{
                    rest.showToast("Something went wrong");
                    finish();
                }

            }

            @Override
            public void onFailure(@NonNull Call<ResumesModel> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });

    }
}