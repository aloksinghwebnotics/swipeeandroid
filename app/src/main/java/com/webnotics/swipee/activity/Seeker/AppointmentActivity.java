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

import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.adapter.seeeker.AppointmentAdapter;
import com.webnotics.swipee.model.seeker.AppointmentModel;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentActivity extends AppCompatActivity {
    Rest rest;
    Context mContext;
    ImageView iv_back;
    TextView tv_nodata;
    RecyclerView rv_appointment;
    LinearLayout ll_nodata;
    @SuppressLint("StaticFieldLeak")
    public static AppointmentActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        setContentView(R.layout.activity_appoiment);
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
        SwipeeApiClient.swipeeServiceInstance().getUserAppointmentData(Config.GetUserToken(), "appointment_list").enqueue(new Callback<AppointmentModel>() {
            @Override
            public void onResponse(@NonNull Call<AppointmentModel> call, @NonNull Response<AppointmentModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200) {
                    AppointmentModel appointmentModel = response.body();
                    if (appointmentModel != null) {
                        if (appointmentModel.getCode() == 200) {
                            tv_nodata.setVisibility(View.GONE);
                            ll_nodata.setVisibility(View.GONE);
                            rv_appointment.setVisibility(View.VISIBLE);
                            ArrayList<AppointmentModel.Data> appointment_data=appointmentModel.getData().getAppointment_data();
                            appointment_data.sort(new DateComparator());
                            rv_appointment.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            AppointmentAdapter stateAdapter = new AppointmentAdapter(mContext, appointment_data);
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
            public void onFailure(@NonNull Call<AppointmentModel> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }


    private static class DateComparator implements Comparator<AppointmentModel.Data> {
        public int compare(AppointmentModel.Data s2, AppointmentModel.Data s1) {
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date2 = formatDate.parse(s1.getAppointment_date_time());
                Date date3 = formatDate.parse(s2.getAppointment_date_time());
                if (date2 != null && date3 != null) {
                    if (date2.before(date3)) return -1;
                    else   if (date2.after(date3)) return 1;
                    else return 0;

                }else return 0;
            } catch (ParseException e) {
                e.printStackTrace();
                return 0;
            }

        }

    }
}