package com.webnotics.swipee.activity.company;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.NotificationActivity;
import com.webnotics.swipee.activity.RescheduleAppointment;
import com.webnotics.swipee.activity.Seeker.SeekerHomeActivity;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.text.MessageFormat;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationAppointmentAction extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_back, iv_profile;
    TextView tv_name, tv_location,tv_datetime,tv_mode,tv_appointment,tv_accept,tv_cancel;
    Rest rest;
    Context mContext;
    private String appointmentId="";
    private String appointment_type="";
    private String appointment_number="";
    private String company_id="";
    private String company_logo="";
    private String company_name="";
    private String city="";
    private String state="";
    private String company_country_name="";
    private String posted_by="";
    private String is_own_job="";
    private String user_id="";
    private String job_title="";
    private String date="";
    @SuppressLint("StaticFieldLeak")
    public static NotificationAppointmentAction instance;
    private String from="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_appointment);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        rest = new Rest(mContext);
           instance=this;
        iv_back = findViewById(R.id.iv_back);
        iv_profile = findViewById(R.id.iv_profile);
        tv_name = findViewById(R.id.tv_name);
        tv_location = findViewById(R.id.tv_location);
        tv_datetime = findViewById(R.id.tv_datetime);
        tv_mode = findViewById(R.id.tv_mode);
        tv_appointment = findViewById(R.id.tv_appointment);
        tv_accept = findViewById(R.id.tv_accept);
        tv_cancel = findViewById(R.id.tv_cancel);
        iv_back.setOnClickListener(this);

        if (getIntent() != null) {

             company_id = getIntent().getStringExtra("company_id") != null ? getIntent().getStringExtra("company_id") : "";
             user_id = getIntent().getStringExtra("user_id") != null ? getIntent().getStringExtra("user_id") : "";
             company_logo = getIntent().getStringExtra("company_logo") != null ? getIntent().getStringExtra("company_logo") : "";
             company_name = getIntent().getStringExtra("company_name") != null ? getIntent().getStringExtra("company_name") : "";
             city = getIntent().getStringExtra("company_city_name") != null ? getIntent().getStringExtra("company_city_name") : "";
             state = getIntent().getStringExtra("company_state_name") != null ? getIntent().getStringExtra("company_state_name") : "";
             company_country_name = getIntent().getStringExtra("company_country_name") != null ? getIntent().getStringExtra("company_country_name") : "";
             posted_by = getIntent().getStringExtra("posted_by") != null ? getIntent().getStringExtra("posted_by") : "";
             is_own_job = getIntent().getStringExtra("is_own_job") != null ? getIntent().getStringExtra("is_own_job") : "";
            appointmentId = getIntent().getStringExtra("appointment_id") != null ? getIntent().getStringExtra("appointment_id") : "";
             appointment_number = getIntent().getStringExtra("appointment_number") != null ? getIntent().getStringExtra("appointment_number") : "";
            appointment_type = getIntent().getStringExtra("appointment_type") != null ? getIntent().getStringExtra("appointment_type") : "";
             job_title = getIntent().getStringExtra("job_title") != null ? getIntent().getStringExtra("job_title") : "";
             date = getIntent().getStringExtra("date") != null ? getIntent().getStringExtra("date") : "";
            from = getIntent().getStringExtra("from") != null ? getIntent().getStringExtra("from") : "";
            tv_name.setText(posted_by);
            tv_datetime.setText(date);
            if (appointment_type.equalsIgnoreCase("online_meeting")) {
                tv_mode.setText("Video Call ");
                tv_mode.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getDrawable(R.drawable.ic_video_player), null);
            } else if (appointment_type.equalsIgnoreCase("call")) {
                tv_mode.setText("Audio Call ");
                tv_mode.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getDrawable(R.drawable.ic_telephone_meeting), null);
            } else if (appointment_type.equalsIgnoreCase("chat")) {
                tv_mode.setText("Chat ");
                tv_mode.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getDrawable(R.drawable.chat_img), null);
            }
            tv_location.setText(MessageFormat.format("{0}, {1}, {2}", city, state, company_country_name));
            Glide.with(mContext).load(company_logo)
                    .apply(new RequestOptions().placeholder(R.drawable.white_light_semiround_bg)
                            .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * 24))))
                            .error(R.drawable.white_light_semiround_bg)).into(iv_profile);

            iv_profile.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(company_logo))
                    AppController.callFullImage(mContext, company_logo);
            });
            iv_back.setOnClickListener(this);
            tv_accept.setOnClickListener(this);
            tv_cancel.setOnClickListener(this);
            tv_appointment.setOnClickListener(this);
        }
    }


    @Override
    public void onBackPressed() {
        if (!from.equalsIgnoreCase(NotificationActivity.class.getSimpleName())){
            if (Config.isSeeker())
                startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "match"));
            else  startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", "match"));
        }

        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
               onBackPressed();
                break;
            case R.id.tv_cancel:
                if (Config.isSeeker()){
                    if (rest.isInterentAvaliable()){
                        AppController.ShowDialogue("", mContext);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(ParaName.KEY_APPOINTMENTID, appointmentId);
                        hashMap.put(ParaName.KEY_APPOINTMENTNUMBER, appointment_number);
                        hashMap.put(ParaName.KEY_APPOINTMENTSTATUS, "C");
                        hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                        hashMap.put(ParaName.KEY_COMPANYID, company_id);
                        statusAppointment(hashMap);
                    }else rest.AlertForInternet();
                }else {
                    cancelAppointment(appointmentId,"C",appointment_number);
                }

                break;
            case R.id.tv_accept:
                if (Config.isSeeker()){
                    if (rest.isInterentAvaliable()){
                        AppController.ShowDialogue("", mContext);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(ParaName.KEY_APPOINTMENTID, appointmentId);
                        hashMap.put(ParaName.KEY_APPOINTMENTNUMBER, appointment_number);
                        hashMap.put(ParaName.KEY_APPOINTMENTSTATUS, "A");
                        hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                        hashMap.put(ParaName.KEY_COMPANYID, company_id);
                        statusAppointment(hashMap);
                    }else rest.AlertForInternet();
                }else {
                    cancelAppointment(appointmentId,"A",appointment_number);
                }


                break;
            case R.id.tv_appointment:
                startActivity(new Intent(mContext, RescheduleAppointment.class)
                        .putExtra("company_id", company_id)
                        .putExtra("company_logo", company_logo)
                        .putExtra("company_name", company_name)
                        .putExtra("company_city_name", city)
                        .putExtra("company_state_name", state)
                        .putExtra("company_country_name", company_country_name)
                        .putExtra("posted_by", posted_by)
                        .putExtra("is_own_job", is_own_job)
                        .putExtra("apply_id", "")
                        .putExtra("job_id", "")
                        .putExtra("appointment_id", appointmentId)
                        .putExtra("appointment_number", appointment_number)
                        .putExtra("appointment_type", appointment_type)
                        .putExtra("job_title", job_title)
                        .putExtra("from", NotificationAppointmentAction.class.getSimpleName())
                        .putExtra("date", date)
                );
                break;
            default:break;
        }
    }


    public void cancelAppointment(String id, String action, String appointment_number) {
        AppController.ShowDialogue("", mContext);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(ParaName.KEY_APPOINTMENTID, id);
        hashMap.put(ParaName.KEY_APPOINTMENTSTATUS, action);
        hashMap.put(ParaName.KEY_APPOINTMENTNUMBER, appointment_number);
        hashMap.put(ParaName.KEY_UID, user_id);
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
                        rest.showToast(responceBody.get("message").getAsString());
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



    public void statusAppointment(HashMap<String,String> hashMap) {

        SwipeeApiClient.swipeeServiceInstance().setUserAppointmentStatus(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responceBody = response.body();
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (response.body().get("code").getAsInt() == 200 &&responceBody.get("status").getAsBoolean()) {
                        rest.showToast(responceBody.get("message").getAsString());
                        if (Config.isSeeker())
                            startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "match"));
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