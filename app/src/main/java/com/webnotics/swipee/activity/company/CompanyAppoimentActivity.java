package com.webnotics.swipee.activity.company;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.adapter.company.CompanyAppointmentAdapter;
import com.webnotics.swipee.model.company.CompanyAppointmentModel;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyAppoimentActivity extends AppCompatActivity {
    Rest rest;
    Context mContext;
    ImageView iv_back;
    LinearLayout ll_nodata;
    TextView tv_nodata;
    RecyclerView rv_appointment;
    @SuppressLint("StaticFieldLeak")
    public static CompanyAppoimentActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        setContentView(R.layout.activity_company_appoiment);
        mContext = this;
        instance = this;
        rest = new Rest(mContext);
        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            getAppointmentList();
        } else rest.AlertForInternet();

        iv_back = findViewById(R.id.iv_back);
        tv_nodata = findViewById(R.id.tv_nodata);
        rv_appointment = findViewById(R.id.rv_appointment);
        ll_nodata = findViewById(R.id.ll_nodata);

        iv_back.setOnClickListener(v -> finish());
    }

    private void getAppointmentList() {
        SwipeeApiClient.swipeeServiceInstance().getCompanyAppointmentData(Config.GetUserToken()).enqueue(new Callback<CompanyAppointmentModel>() {
            @Override
            public void onResponse(@NonNull Call<CompanyAppointmentModel> call, @NonNull Response<CompanyAppointmentModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200) {
                    CompanyAppointmentModel appointmentModel = response.body();
                    if (appointmentModel != null) {
                        if (appointmentModel.getCode() == 200) {
                            tv_nodata.setVisibility(View.GONE);
                            ll_nodata.setVisibility(View.GONE);
                            rv_appointment.setVisibility(View.VISIBLE);
                            rv_appointment.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            CompanyAppointmentAdapter stateAdapter = new CompanyAppointmentAdapter(CompanyAppoimentActivity.this, appointmentModel.getData().getAppointment_data());
                            rv_appointment.setAdapter(stateAdapter);
                        } else if (appointmentModel.getCode() == 203) {
                            rest.showToast(appointmentModel.getMessage());
                            AppController.loggedOut(mContext);
                            finish();
                        } else {
                            rv_appointment.setVisibility(View.GONE);
                            tv_nodata.setVisibility(View.VISIBLE);
                            ll_nodata.setVisibility(View.VISIBLE);
                            tv_nodata.setText(appointmentModel.getMessage());
                        }

                    } else rest.showToast("Something went wrong");
                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<CompanyAppointmentModel> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }


    public void callCancel(String id, String appointment_number, String user_id) {
        Dialog progressdialog = new Dialog(mContext);
        progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressdialog.setContentView(R.layout.cancel_appointment_popup);
        progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        progressdialog.getWindow().setAttributes(lp);

        progressdialog.findViewById(R.id.tv_yes).setOnClickListener(v -> {
            progressdialog.dismiss();
            if (rest.isInterentAvaliable()) cancelAppointment(id, appointment_number, user_id);
            else rest.AlertForInternet();

        });
        progressdialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> progressdialog.dismiss());
        try {
            progressdialog.show();
        } catch (Exception ignored) {
        }
    }

    public void cancelAppointment(String id, String appointment_number, String user_id) {
        AppController.ShowDialogue("", mContext);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(ParaName.KEY_APPOINTMENTID, id);
        hashMap.put(ParaName.KEY_APPOINTMENTSTATUS, "C");
        hashMap.put(ParaName.KEY_APPOINTMENTNUMBER, appointment_number);
        hashMap.put(ParaName.KEY_UID, user_id);
        hashMap.put(ParaName.KEY_UNIQUENOTIFYNUMBER, "");
        hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
        SwipeeApiClient.swipeeServiceInstance().setAppointmentStatus(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responceBody = response.body();
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (responceBody.get("status").getAsBoolean()) {
                        startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", CompanyAppoimentActivity.class.getSimpleName()));
                        finish();
                    } else rest.showToast(responceBody.get("message").getAsString());

                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });

    }
}