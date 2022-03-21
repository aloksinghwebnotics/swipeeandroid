package com.webnotics.swipee.activity.company;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.potyvideo.library.AndExoPlayerView;
import com.webnotics.swipee.CustomUi.FlowLayout;
import com.webnotics.swipee.CustomUi.NestedListView;
import com.webnotics.swipee.CustomUi.PopinsRegularTextView;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.AppointmentAction;
import com.webnotics.swipee.activity.AppointmentDetail;
import com.webnotics.swipee.activity.NotificationActivity;
import com.webnotics.swipee.activity.NotificationAppointmentAction;
import com.webnotics.swipee.activity.Seeker.SeekerHomeActivity;
import com.webnotics.swipee.adapter.seeeker.UserPreferenceAdapter;
import com.webnotics.swipee.chat.MainChatActivity;
import com.webnotics.swipee.model.seeker.EmployeeUserDetails;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetail extends AppCompatActivity implements View.OnClickListener {

    Context mContext;
    Rest rest;
    private String userId = "";
    private String jobId = "";
    ImageView iv_back, iv_profile, iv_more, iv_meetingType;
    private String from = "";
    TextView tv_name, tv_location, tv_shortlisted, tv_hire, tv_reject, tv_about, tv_education, tv_experience, tv_download, tv_appointment;
    FlowLayout flowlayout, flow_language, flow_jobtype;
    LinearLayout ll_action, ll_job_action, ll_appointment, join;
    RelativeLayout rl_main, rl_video;
    TextView tv_video, tv_title_language, appoint_job_title, tv_appointmenttime, tv_title_about, tv_title_experience, tv_title_education, tv_username, tv_report, tv_block, tv_reschedule, tv_cancel_application;
    private String cv_file_link = "";
    private String apply_id = "";
    private String first_name = "";
    private String last_name = "";
    private String user_profile = "";
    private JsonArray job_skills = new JsonArray();
    private String state = "";
    private String city = "";
    ImageView iv_accept, iv_reject, iv_video, iv_videoplay;
    @SuppressLint("StaticFieldLeak")
    public static UserDetail instance;
    private String matchId = "";
    private String name = "";
    private String country = "";
    public JsonArray appointment_data = new JsonArray();
    AndExoPlayerView vv_video;
    private String video_file_link = "";
    NestedListView list_preferences;
    private TextView tv_title_job_type,tv_title_prefrences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.colorPrimary));
        setContentView(R.layout.activity_user_detail);
        if (getIntent() != null) {
            userId = getIntent().getStringExtra("id") != null ? getIntent().getStringExtra("id") : "";
            from = getIntent().getStringExtra("from") != null ? getIntent().getStringExtra("from") : "";
            jobId = getIntent().getStringExtra("job_id") != null ? getIntent().getStringExtra("job_id") : "";
            name = getIntent().getStringExtra("name") != null ? getIntent().getStringExtra("name") : "";
            apply_id = getIntent().getStringExtra("apply_id") != null ? getIntent().getStringExtra("apply_id") : "";
        }

        mContext = this;
        rest = new Rest(mContext);
        instance = this;
        iv_back = findViewById(R.id.iv_back);
        rl_main = findViewById(R.id.rl_main);
        ll_action = findViewById(R.id.ll_action);
        flowlayout = findViewById(R.id.flowlayout);
        tv_name = findViewById(R.id.tv_name);
        tv_location = findViewById(R.id.tv_location);
        tv_shortlisted = findViewById(R.id.tv_shortlisted);
        tv_hire = findViewById(R.id.tv_hire);
        tv_reject = findViewById(R.id.tv_reject);
        tv_about = findViewById(R.id.tv_about);
        tv_education = findViewById(R.id.tv_education);
        tv_experience = findViewById(R.id.tv_experience);
        tv_download = findViewById(R.id.tv_download);
        tv_appointment = findViewById(R.id.tv_appointment);
        flow_language = findViewById(R.id.flow_language);
        iv_profile = findViewById(R.id.iv_profile);
        tv_title_language = findViewById(R.id.tv_title_language);
        tv_title_about = findViewById(R.id.tv_title_about);
        tv_title_experience = findViewById(R.id.tv_title_experience);
        tv_title_education = findViewById(R.id.tv_title_education);
        iv_accept = findViewById(R.id.iv_accept);
        iv_reject = findViewById(R.id.iv_reject);
        tv_username = findViewById(R.id.tv_username);
        iv_more = findViewById(R.id.iv_more);
        ll_job_action = findViewById(R.id.ll_job_action);
        tv_cancel_application = findViewById(R.id.tv_cancel_application);
        tv_block = findViewById(R.id.tv_block);
        tv_reschedule = findViewById(R.id.tv_reschedule);
        tv_report = findViewById(R.id.tv_report);
        tv_appointmenttime = findViewById(R.id.tv_appointmenttime);
        iv_meetingType = findViewById(R.id.iv_meetingType);
        ll_appointment = findViewById(R.id.ll_appointment);
        join = findViewById(R.id.join);
        appoint_job_title = findViewById(R.id.appoint_job_title);
        tv_video = findViewById(R.id.tv_video);
        rl_video = findViewById(R.id.rl_video);
        vv_video = findViewById(R.id.vv_video);
        iv_video = findViewById(R.id.iv_video);
        iv_videoplay = findViewById(R.id.iv_videoplay);
        flow_jobtype = findViewById(R.id.flow_jobtype);
        tv_title_job_type = findViewById(R.id.tv_title_job_type);
        tv_title_prefrences = findViewById(R.id.tv_title_prefrences);
        list_preferences = findViewById(R.id.list_preferences);

        if (!TextUtils.isEmpty(name))
            tv_username.setText(MessageFormat.format("{0}''s Profile", name));

        if (rest.isInterentAvaliable()) {
            if (!TextUtils.isEmpty(userId)) {
                AppController.ShowDialogue("", mContext);
                getUserDetail();
            }
        } else rest.AlertForInternet();

        iv_back.setOnClickListener(v -> finish());
        tv_appointment.setOnClickListener(this);
        tv_download.setOnClickListener(this);
        tv_shortlisted.setOnClickListener(this);
        tv_hire.setOnClickListener(this);
        tv_reject.setOnClickListener(this);
        iv_accept.setOnClickListener(this);
        iv_reject.setOnClickListener(this);
        iv_profile.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(user_profile))
                AppController.callFullImage(mContext, user_profile);
        });

        iv_videoplay.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(video_file_link)) {
                vv_video.setSource(video_file_link);
                vv_video.setVisibility(View.VISIBLE);
                iv_video.setVisibility(View.GONE);
                iv_videoplay.setVisibility(View.GONE);
            }

        });
        iv_more.setOnClickListener(v -> ll_job_action.setVisibility(ll_job_action.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));
        tv_block.setOnClickListener(v -> blockUserPopup());
        tv_report.setOnClickListener(v -> {
            ll_job_action.setVisibility(View.GONE);
            Dialog progressdialog = new Dialog(mContext);
            progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            progressdialog.setContentView(R.layout.job_action_popup);
            progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.CENTER;
            progressdialog.getWindow().setAttributes(lp);
            TextView cancel = progressdialog.findViewById(R.id.tv_cancel);
            TextView submit = progressdialog.findViewById(R.id.tv_submit);
            EditText reason = progressdialog.findViewById(R.id.et_reason);

            cancel.setOnClickListener(v1 -> progressdialog.dismiss());
            submit.setOnClickListener(v12 -> {
                if (!TextUtils.isEmpty(reason.getText().toString().replaceAll(" ", ""))) {
                    AppController.ShowDialogue("", mContext);
                    if (!Config.isSeeker())
                        reportUser(userId, reason.getText().toString());
                    progressdialog.dismiss();
                } else rest.showToast("Enter report reason");

            });

            try {
                progressdialog.show();
            } catch (Exception ignored) {
            }
        });
        tv_cancel_application.setOnClickListener(v -> {
            ll_job_action.setVisibility(View.GONE);
            ll_job_action.setVisibility(View.GONE);
            startActivity(new Intent(mContext, AppointmentAction.class)
                    .putExtra("company_id", userId)
                    .putExtra("company_logo", user_profile)
                    .putExtra("company_name", first_name + " " + last_name)
                    .putExtra("company_city_name", city)
                    .putExtra("company_state_name", state)
                    .putExtra("company_country_name", country)
                    .putExtra("posted_by", first_name + " " + last_name)
                    .putExtra("is_own_job", "Y")
                    .putExtra("apply_id", apply_id)
                    .putExtra("job_id", jobId)
                    .putExtra("from", UserDetail.class.getSimpleName())
                    .putExtra("action", "Cancel")
            );
        });
        tv_reschedule.setOnClickListener(v -> {
            ll_job_action.setVisibility(View.GONE);
            startActivity(new Intent(mContext, AppointmentAction.class)
                    .putExtra("company_id", userId)
                    .putExtra("company_logo", user_profile)
                    .putExtra("company_name", first_name + " " + last_name)
                    .putExtra("company_city_name", city)
                    .putExtra("company_state_name", state)
                    .putExtra("company_country_name", country)
                    .putExtra("posted_by", first_name + " " + last_name)
                    .putExtra("is_own_job", "Y")
                    .putExtra("apply_id", apply_id)
                    .putExtra("job_id", jobId)
                    .putExtra("from", UserDetail.class.getSimpleName())
                    .putExtra("action", "Reschedule")
            );
        });

    }

    private void blockUserPopup() {
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
        tv_title.setText("Block User");
        tv_detail.setText("Are you sure, you want to block this user ?");

        progressdialog.findViewById(R.id.tv_yes).setOnClickListener(v -> {
            progressdialog.dismiss();
            AppController.ShowDialogue("", mContext);
            if (!Config.isSeeker())
                blockUser(userId);
        });
        progressdialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> progressdialog.dismiss());
        try {
            progressdialog.show();
        } catch (Exception ignored) {}

        ll_job_action.setVisibility(View.GONE);
    }

    private void reportUser(String uid, String issue) {
        SwipeeApiClient.swipeeServiceInstance().reportUser(Config.GetUserToken(), uid, issue).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        setBackPressed();
                        AppController.loggedOut(mContext);

                    } else if (response.body().get("code").getAsInt() == 200 && response.body().get("status").getAsBoolean())
                        setBackPressed();
                    else rest.showToast(response.body().get("message").getAsString());
                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });

    }

    private void blockUser(String uid) {
        SwipeeApiClient.swipeeServiceInstance().blockUser(Config.GetUserToken(), uid).enqueue(new Callback<JsonObject>() {
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

    public void setBackPressed() {
        if (from.equalsIgnoreCase(NotificationAppointmentAction.class.getSimpleName())) {
            onBackPressed();
        } else if (from.equalsIgnoreCase("Notification")) {
            if (Config.isSeeker())
                startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "match"));
            else
                startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", "match"));
            onBackPressed();
        } else if (from.equalsIgnoreCase(NotificationActivity.class.getSimpleName())) {
            if (NotificationActivity.instance != null)
                NotificationActivity.instance.finish();
            if (Config.isSeeker())
                startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "match"));
            else
                startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", "match"));
            onBackPressed();
        } else if (from.equalsIgnoreCase(MatchedUserActivity.class.getSimpleName())) {
            if (MatchedUserActivity.instance != null) MatchedUserActivity.instance.onBackPressed();
            onBackPressed();
        } else if (from.equalsIgnoreCase(LikedUserActivity.class.getSimpleName())) {
            if (LikedUserActivity.instance != null) LikedUserActivity.instance.onBackPressed();
            onBackPressed();
        } else if (from.equalsIgnoreCase(CompanyNearBy.class.getSimpleName())) {
            if (CompanyNearBy.instance != null) CompanyNearBy.instance.onBackPressed();
            onBackPressed();
        }
        if (from.equalsIgnoreCase(CompanyAppoimentActivity.class.getSimpleName())) {
            if (CompanyAppoimentActivity.instance != null)
                CompanyAppoimentActivity.instance.onBackPressed();
            startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", UserDetail.class.getSimpleName()));
            onBackPressed();
        } else if (from.equalsIgnoreCase(PostedJobActivity.class.getSimpleName())) {
            if (PostedJobActivity.instance != null)
                PostedJobActivity.instance.onBackPressed();
            startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", UserDetail.class.getSimpleName()));
            onBackPressed();
        } else if (from.equalsIgnoreCase(MainChatActivity.class.getSimpleName())) {
            if (MainChatActivity.instance != null)
                MainChatActivity.instance.backPressed();
            onBackPressed();
        } else onBackPressed();

    }


    private void getUserDetail() {
        SwipeeApiClient.swipeeServiceInstance().getSingleUserData(Config.GetUserToken(), apply_id, userId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        setBackPressed();
                        AppController.loggedOut(mContext);
                    } else if (response.body().get("status").getAsBoolean()) {
                        JsonObject jsonObject = response.body();
                        JsonObject dataObject = jsonObject.has("data") ? !jsonObject.get("data").isJsonNull() ? jsonObject.get("data").getAsJsonObject() : new JsonObject() : new JsonObject();
                        JsonObject job_data = dataObject.has("job_user_data") ? !dataObject.get("job_user_data").isJsonNull() ? dataObject.get("job_user_data").getAsJsonObject() : new JsonObject() : new JsonObject();
                        //JsonArray advert_companies=dataObject.has("advert_companies")?dataObject.get("advert_companies").getAsJsonArray():new JsonArray();
                        if (job_data.size() > 0) {

                            userId = job_data.has("user_id") ? job_data.get("user_id").isJsonNull() ? "" : job_data.get("user_id").getAsString() : "";
                            first_name = job_data.has("first_name") ? job_data.get("first_name").isJsonNull() ? "" : job_data.get("first_name").getAsString() : "";
                            last_name = job_data.has("last_name") ? job_data.get("last_name").isJsonNull() ? "" : job_data.get("last_name").getAsString() : "";
                            user_profile = job_data.has("user_profile") ? job_data.get("user_profile").isJsonNull() ? "" : job_data.get("user_profile").getAsString() : "";
                            matchId = job_data.has("match_id") ? job_data.get("match_id").isJsonNull() ? "" : job_data.get("match_id").getAsString() : "";
                            job_skills = job_data.has("skill_name") ? job_data.get("skill_name").isJsonNull() ? new JsonArray() : job_data.get("skill_name").getAsJsonArray() : new JsonArray();
                            country = job_data.has("country") ? job_data.get("country").isJsonNull() ? "" : job_data.get("country").getAsString() : "";
                            state = job_data.has("state") ? job_data.get("state").isJsonNull() ? "" : job_data.get("state").getAsString() : "";
                            city = job_data.has("city") ? job_data.get("city").isJsonNull() ? "" : job_data.get("city").getAsString() : "";
                            String carrier_objective = job_data.has("carrier_objective") ? job_data.get("carrier_objective").isJsonNull() ? "" : job_data.get("carrier_objective").getAsString() : "";
                            String company_match_status = job_data.has("company_match_status") ? job_data.get("company_match_status").isJsonNull() ? "" : job_data.get("company_match_status").getAsString() : "";
                            String user_match_status = job_data.has("user_match_status") ? job_data.get("user_match_status").isJsonNull() ? "" : job_data.get("user_match_status").getAsString() : "";
                            String company_action = job_data.has("company_action") ? job_data.get("company_action").isJsonNull() ? "" : job_data.get("company_action").getAsString() : "";
                            apply_id = job_data.has("apply_id") ? job_data.get("apply_id").isJsonNull() ? "" : job_data.get("apply_id").getAsString() : "";
                            JsonObject user_resumes = job_data.has("user_resumes") ? job_data.get("user_resumes").isJsonNull() ? new JsonObject() : job_data.get("user_resumes").getAsJsonObject() : new JsonObject();
                            video_file_link = job_data.has("user_video") ? job_data.get("user_video").isJsonNull() ? "" : job_data.get("user_video").getAsString() : "";
                            cv_file_link = user_resumes.has("cv_file_link") ? user_resumes.get("cv_file_link").isJsonNull() ? "" : user_resumes.get("cv_file_link").getAsString() : "";
                            JsonArray user_work_experience = job_data.has("user_work_experience") ? job_data.get("user_work_experience").isJsonNull() ? new JsonArray() : job_data.get("user_work_experience").getAsJsonArray() : new JsonArray();
                            JsonArray user_eductaion = job_data.has("user_eductaion") ? job_data.get("user_eductaion").isJsonNull() ? new JsonArray() : job_data.get("user_eductaion").getAsJsonArray() : new JsonArray();
                            JsonArray user_preferences = job_data.has("user_preferences") ? job_data.get("user_preferences").isJsonNull() ? new JsonArray() : job_data.get("user_preferences").getAsJsonArray() : new JsonArray();
                            JsonArray user_job_types = job_data.has("user_job_types") ? job_data.get("user_job_types").isJsonNull() ? new JsonArray() : job_data.get("user_job_types").getAsJsonArray() : new JsonArray();
                            JsonArray user_languages = job_data.has("user_languages") ? job_data.get("user_languages").isJsonNull() ? new JsonArray() : job_data.get("user_languages").getAsJsonArray() : new JsonArray();
                            appointment_data = dataObject.has("appointment_data") ? dataObject.get("appointment_data").isJsonNull() ? new JsonArray() : dataObject.get("appointment_data").getAsJsonArray() : new JsonArray();

                            if (user_job_types.size() > 0) {
                                setJobtypeFlow(user_job_types);
                                flow_jobtype.setVisibility(View.VISIBLE);
                                tv_title_job_type.setVisibility(View.VISIBLE);

                            } else {
                                flow_jobtype.setVisibility(View.GONE);
                                tv_title_job_type.setVisibility(View.GONE);
                            }
                            if (user_preferences.size() > 0) {
                                ArrayList<EmployeeUserDetails.Data.User_Preferences> mArrayuseruserpreference = new ArrayList<>();
                                for (int j=0;j<user_preferences.size();j++){
                                    JsonObject dataObj = user_preferences.get(j).getAsJsonObject();
                                    String preference_id = dataObj.has("preference_id") ? dataObj.get("preference_id").getAsString() : "";
                                    JsonArray location_data = dataObj.has("location_data") ? dataObj.get("location_data").getAsJsonArray() : new JsonArray();
                                    JsonArray industry_data = dataObj.has("industry_data") ? dataObj.get("industry_data").getAsJsonArray() : new JsonArray();
                                    JsonObject expected_salary = dataObj.has("expected_salary") ? dataObj.get("expected_salary").getAsJsonObject() : new JsonObject();
                                    String expected_salary_number = expected_salary.has("expected_salary_number") ? expected_salary.get("expected_salary_number").getAsString() : "";
                                    String expected_salary_words = expected_salary.has("expected_salary_words") ? expected_salary.get("expected_salary_words").getAsString() : "";
                                    ArrayList<EmployeeUserDetails.Data.User_Preferences.Location_Data> locationData = new ArrayList<>();
                                    ArrayList<EmployeeUserDetails.Data.User_Preferences.Industry_Data> industryData = new ArrayList<>();
                                    EmployeeUserDetails.Data.User_Preferences.Expected_Salary expectedSalary = new EmployeeUserDetails.Data.User_Preferences.Expected_Salary(expected_salary_number, expected_salary_words);
                                    for (int i = 0; i < location_data.size(); i++) {
                                        JsonObject locationObj = location_data.get(i).getAsJsonObject();
                                        long location_id = locationObj.has("location_id") ? locationObj.get("location_id").getAsLong() : 0;
                                        String location_name = locationObj.has("location_name") ? locationObj.get("location_name").getAsString() : "";
                                        EmployeeUserDetails.Data.User_Preferences.Location_Data locationData1 = new EmployeeUserDetails.Data.User_Preferences.Location_Data(String.valueOf(location_id), location_name);
                                        locationData.add(locationData.size(), locationData1);
                                    }
                                    for (int i = 0; i < industry_data.size(); i++) {
                                        JsonObject industryObj = industry_data.get(i).getAsJsonObject();
                                        long industry_id = industryObj.has("industry_id") ? industryObj.get("industry_id").getAsLong() : 0;
                                        String industry_name = industryObj.has("industry_name") ? industryObj.get("industry_name").getAsString() : "";
                                        EmployeeUserDetails.Data.User_Preferences.Industry_Data industry_data1 = new EmployeeUserDetails.Data.User_Preferences.Industry_Data(String.valueOf(industry_id), industry_name);
                                        industryData.add(industryData.size(), industry_data1);
                                    }
                                    EmployeeUserDetails.Data.User_Preferences user_preferencesF = new EmployeeUserDetails.Data.User_Preferences(preference_id, locationData, industryData, expectedSalary);
                                    mArrayuseruserpreference.add(user_preferencesF);
                                }

                                UserPreferenceAdapter adapter = new UserPreferenceAdapter(mContext, mArrayuseruserpreference);
                                list_preferences.setAdapter(adapter);
                                list_preferences.setDivider(null);
                                tv_title_prefrences.setVisibility(View.VISIBLE);
                                list_preferences.setVisibility(View.VISIBLE);

                            } else {
                                tv_title_prefrences.setVisibility(View.GONE);
                                list_preferences.setVisibility(View.GONE);
                            }

                            tv_username.setText(MessageFormat.format("{0}''s Profile", first_name));
                            if (TextUtils.isEmpty(video_file_link)) {
                                rl_video.setVisibility(View.GONE);
                                tv_video.setVisibility(View.GONE);
                            } else {
                                rl_video.setVisibility(View.VISIBLE);
                                iv_video.setVisibility(View.VISIBLE);
                                tv_video.setVisibility(View.VISIBLE);
                                iv_videoplay.setVisibility(View.VISIBLE);
                                vv_video.setVisibility(View.GONE);
                                try {
                                    Glide.with(mContext)
                                            .load(video_file_link)
                                            .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * 16))))
                                            .into(iv_video);
                                }catch (Exception ignored){}
                            }

                            if (Config.isSeeker()) {
                                iv_more.setVisibility(View.GONE);
                            } else iv_more.setVisibility(View.VISIBLE);

                            if (TextUtils.isEmpty(apply_id))
                                ll_action.setVisibility(View.GONE);
                            else ll_action.setVisibility(View.VISIBLE);
                            if (from.equalsIgnoreCase(NotificationAppointmentAction.class.getSimpleName())){
                                iv_more.setVisibility(View.GONE);
                                ll_action.setVisibility(View.GONE);
                                tv_appointment.setVisibility(View.GONE);
                                tv_cancel_application.setVisibility(View.GONE);
                                tv_reschedule.setVisibility(View.GONE);
                                ll_appointment.setVisibility(View.GONE);
                                iv_reject.setVisibility(View.GONE);
                                iv_accept.setVisibility(View.GONE);
                            }else
                            if (user_match_status.equalsIgnoreCase("A") && company_match_status.equalsIgnoreCase("A") ) {

                                if (appointment_data.size() > 0) {
                                    JsonObject appointmentObj = appointment_data.get(0).getAsJsonObject();
                                    String appointment_id = appointmentObj.has("appointment_id") ? appointmentObj.get("appointment_id").getAsString() : "";
                                    String appointment_type = appointmentObj.has("appointment_type") ? appointmentObj.get("appointment_type").getAsString() : "";
                                    String appointment_date = appointmentObj.has("appointment_date") ? appointmentObj.get("appointment_date").getAsString() : "";
                                    String appointment_start_at = appointmentObj.has("appointment_start_at") ? appointmentObj.get("appointment_start_at").getAsString() : "";
                                    String appointment_end_at = appointmentObj.has("appointment_end_at") ? appointmentObj.get("appointment_end_at").getAsString() : "";
                                    String job_title = appointmentObj.has("job_title") ? appointmentObj.get("job_title").getAsString() : "";
                                    appoint_job_title.setText(job_title);
                                    SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                                    SimpleDateFormat formatout = new SimpleDateFormat("hh:mm aa");
                                    Date date;
                                    try {
                                        date = format.parse(appointment_start_at);
                                        String date1 = formatout.format(date);
                                        tv_appointmenttime.setText(String.format("%s %s %s", "Appointment scheduled at:", appointment_date, date1.toUpperCase(Locale.ROOT)));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Date calendarDate = Calendar.getInstance().getTime();
                                    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    try {
                                        Date date2 = formatDate.parse(appointment_date + " " + appointment_start_at);
                                        Date date3 = formatDate.parse(appointment_date + " " + appointment_end_at);
                                        if (date2 != null && date3 != null) {
                                            if (date2.before(calendarDate) && date3.after(calendarDate)) {
                                                join.setVisibility(View.VISIBLE);
                                                iv_more.setVisibility(View.GONE);
                                                if (appointment_type.equalsIgnoreCase("chat"))
                                                    iv_meetingType.setImageResource(R.drawable.ic_icon_material_chat_bubble);
                                                else if (appointment_type.equalsIgnoreCase("online_meeting"))
                                                    iv_meetingType.setImageResource(R.drawable.ic_icon_awesome_video_fill);
                                                else if (appointment_type.equalsIgnoreCase("call"))
                                                    iv_meetingType.setImageResource(R.drawable.ic_telephonefill);
                                                ll_appointment.setOnClickListener(v -> {
                                                    Intent resultIntent = new Intent(mContext, AppointmentDetail.class);
                                                    resultIntent.putExtra(ParaName.KEY_APPOINTMENTID, appointment_id);
                                                    resultIntent.putExtra("from", UserDetail.class.getSimpleName());
                                                    mContext.startActivity(resultIntent);
                                                });
                                            } else join.setVisibility(View.GONE);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    ll_appointment.setVisibility(View.VISIBLE);
                                    boolean isShow = false;
                                    for (int i = 0; i < appointment_data.size(); i++) {
                                        if (appointment_data.get(i).getAsJsonObject().get("appointment_status").getAsString().equalsIgnoreCase("A")) {
                                            isShow = true;
                                            break;
                                        }
                                    }
                                    if (isShow) {
                                        tv_cancel_application.setText("Cancel Appointment");
                                        tv_cancel_application.setVisibility(View.VISIBLE);
                                        tv_reschedule.setVisibility(View.VISIBLE);
                                    } else {
                                        tv_cancel_application.setVisibility(View.GONE);
                                        tv_reschedule.setVisibility(View.GONE);
                                    }
                                    tv_appointment.setVisibility(View.GONE);
                                } else {
                                    if (!TextUtils.isEmpty(apply_id))
                                    tv_appointment.setVisibility(View.VISIBLE);
                                    else tv_appointment.setVisibility(View.GONE);
                                    tv_cancel_application.setVisibility(View.GONE);
                                    tv_reschedule.setVisibility(View.GONE);
                                    ll_appointment.setVisibility(View.GONE);
                                }

                            } else if (company_match_status.equalsIgnoreCase("A") ) {
                                if ( !TextUtils.isEmpty(apply_id))
                                    tv_appointment.setVisibility(View.VISIBLE);
                                else tv_appointment.setVisibility(View.GONE);
                                iv_reject.setVisibility(View.GONE);
                                iv_accept.setVisibility(View.GONE);
                            } else {
                                if (!company_match_status.equalsIgnoreCase("A") && !TextUtils.isEmpty(jobId)) {
                                    iv_reject.setVisibility(View.VISIBLE);
                                    iv_accept.setVisibility(View.VISIBLE);
                                } else {
                                    iv_reject.setVisibility(View.GONE);
                                    iv_accept.setVisibility(View.GONE);
                                }
                                ll_action.setVisibility(View.GONE);
                                tv_appointment.setVisibility(View.GONE);
                            }

                            tv_name.setText(MessageFormat.format("{0} {1}", first_name, last_name));
                            tv_location.setText(MessageFormat.format("{0}, {1}", city, state));
                            if (TextUtils.isEmpty(carrier_objective)) {
                                tv_title_about.setVisibility(View.GONE);
                                tv_about.setVisibility(View.GONE);
                            } else {
                                tv_about.setText(carrier_objective);
                                tv_title_about.setVisibility(View.VISIBLE);
                                tv_about.setVisibility(View.VISIBLE);
                            }
                            if (TextUtils.isEmpty(cv_file_link)) {
                                tv_download.setVisibility(View.INVISIBLE);
                            } else tv_download.setVisibility(View.VISIBLE);

                            setAction(company_action);

                            Glide.with(mContext).load(user_profile)
                                    .apply(new RequestOptions().placeholder(R.drawable.white_light_semiround_bg)
                                            .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * 24))))
                                            .error(R.drawable.white_light_semiround_bg)).into(iv_profile);
                            if (user_work_experience.size() > 0) {
                                String workexp = "%#@&";
                                for (int m = 0; m < user_work_experience.size(); m++) {
                                    JsonObject workobj = user_work_experience.get(m).getAsJsonObject();
                                    workexp = workexp + workobj.get("work_from").getAsString() + " - " + (workobj.get("currently_working").getAsString().equalsIgnoreCase("Y")?"Present":workobj.get("work_to").getAsString()) + getString(R.string.next1) + getString(R.string.space) +
                                            getString(R.string.space1) +
                                            workobj.get("experience_title").getAsString() + (m == user_work_experience.size() - 1 ? "" : getString(R.string.nextline) + "%#@&");
                                    workexp = workexp.replaceAll("%#@&", getString(R.string.dot) + getString(R.string.space));
                                    tv_experience.setText(Html.fromHtml(workexp));
                                }
                                tv_title_experience.setVisibility(View.VISIBLE);
                                tv_experience.setVisibility(View.VISIBLE);
                            } else {
                                tv_title_experience.setVisibility(View.GONE);
                                tv_experience.setVisibility(View.GONE);
                            }

                            if (user_eductaion.size() > 0) {
                                String workexp = "%#@&";
                                for (int m = 0; m < user_eductaion.size(); m++) {
                                    JsonObject workobj = user_eductaion.get(m).getAsJsonObject();
                                    workexp = workexp + workobj.get("degree_name").getAsString() + getString(R.string.next1) + "%&&##" +
                                            workobj.get("college_university_name").getAsString() + (m == user_eductaion.size() - 1 ? "" : getString(R.string.nextline) + "%#@&");
                                    workexp = workexp.replaceAll("%#@&", getString(R.string.degreehtml) + getString(R.string.space));
                                    workexp = workexp.replaceAll("%&&##", getString(R.string.collegehtml) + getString(R.string.space1));
                                    tv_education.setText(Html.fromHtml(workexp));
                                }
                                tv_education.setVisibility(View.VISIBLE);
                                tv_title_education.setVisibility(View.VISIBLE);
                            } else {
                                tv_education.setVisibility(View.GONE);
                                tv_title_education.setVisibility(View.GONE);
                            }

                            if (job_skills.size() > 0) {
                                for (int k = 0; k < job_skills.size(); k++) {
                                    LinearLayout linearLayout = new LinearLayout(mContext);
                                    LinearLayout linearLayoutF = new LinearLayout(mContext);
                                    FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                                            FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                                    LinearLayout.LayoutParams layoutParamsF = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT, (int) (mContext.getResources().getDisplayMetrics().density * 32));
                                    layoutParamsF.setMargins((int) (mContext.getResources().getDisplayMetrics().density * 5), 0, (int) (mContext.getResources().getDisplayMetrics().density * 5), (int) (mContext.getResources().getDisplayMetrics().density * 8));
                                    linearLayoutF.setLayoutParams(layoutParams);
                                    linearLayout.setLayoutParams(layoutParamsF);
                                    linearLayout.setPadding((int) (mContext.getResources().getDisplayMetrics().density * 8), 0, (int) (mContext.getResources().getDisplayMetrics().density * 8), 0);
                                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                    linearLayout.setGravity(Gravity.CENTER);
                                    linearLayout.setBackgroundResource(R.drawable.primary_semiround_bg);
                                    PopinsRegularTextView bt = new PopinsRegularTextView(mContext);
                                    bt.setText(job_skills.get(k).getAsString());
                                    bt.setAllCaps(false);
                                    bt.setTextSize(12f);
                                    bt.setMaxLines(1);
                                    bt.setEllipsize(TextUtils.TruncateAt.END);
                                    bt.setTag(job_skills.get(k).getAsString());
                                    bt.setTextColor(mContext.getResources().getColor(R.color.white));
                                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    layoutParams1.setMargins(0, 0, 0, 0);
                                    bt.setLayoutParams(layoutParams1);
                                    linearLayout.addView(bt);
                                    linearLayoutF.addView(linearLayout);
                                    linearLayout.setTag(job_skills.get(k).getAsString());
                                    linearLayoutF.setTag(job_skills.get(k).getAsString());
                                    flowlayout.addView(linearLayoutF);
                                }
                            }

                            if (user_languages.size() > 0) {
                                for (int l = 0; l < user_languages.size(); l++) {
                                    JsonObject jangObj = user_languages.get(l).getAsJsonObject();
                                    String langis = jangObj.has("language_name") ? jangObj.get("language_name").getAsString() : "";
                                    if (!TextUtils.isEmpty(langis)) {

                                        LinearLayout linearLayout = new LinearLayout(mContext);
                                        LinearLayout linearLayoutF = new LinearLayout(mContext);
                                        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                                                FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                                        LinearLayout.LayoutParams layoutParamsF = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT, (int) (mContext.getResources().getDisplayMetrics().density * 32));
                                        layoutParamsF.setMargins((int) (mContext.getResources().getDisplayMetrics().density * 5), 0, (int) (mContext.getResources().getDisplayMetrics().density * 5), (int) (mContext.getResources().getDisplayMetrics().density * 8));
                                        linearLayoutF.setLayoutParams(layoutParams);
                                        linearLayout.setLayoutParams(layoutParamsF);
                                        linearLayout.setPadding((int) (mContext.getResources().getDisplayMetrics().density * 8), 0, (int) (mContext.getResources().getDisplayMetrics().density * 8), 0);
                                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                                        linearLayout.setGravity(Gravity.CENTER);
                                        linearLayout.setBackgroundResource(R.drawable.primary_semiround_bg);
                                        PopinsRegularTextView bt = new PopinsRegularTextView(mContext);
                                        bt.setText(langis);
                                        bt.setAllCaps(false);
                                        bt.setMaxLines(1);
                                        bt.setEllipsize(TextUtils.TruncateAt.END);
                                        bt.setTextSize(12f);
                                        bt.setTag(langis);
                                        bt.setTextColor(mContext.getResources().getColor(R.color.white));
                                        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                                                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                        layoutParams1.setMargins(0, 0, 0, 0);
                                        bt.setLayoutParams(layoutParams1);
                                        linearLayout.addView(bt);
                                        linearLayoutF.addView(linearLayout);
                                        linearLayoutF.setTag(langis);
                                        flow_language.addView(linearLayoutF);
                                    }

                                }
                                flow_language.setVisibility(View.VISIBLE);
                                tv_title_language.setVisibility(View.VISIBLE);
                            } else {
                                flow_language.setVisibility(View.GONE);
                                tv_title_language.setVisibility(View.GONE);
                            }
                            rl_main.setVisibility(View.VISIBLE);
                        }
                    }
                } else rest.showToast("Something went wrong");
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }

    private void setAction(String company_action) {
        if (TextUtils.isEmpty(company_action)) {
            tv_shortlisted.setTextColor(getColor(R.color.black));
            tv_hire.setTextColor(getColor(R.color.black));
            tv_reject.setTextColor(getColor(R.color.black));
            tv_shortlisted.setBackgroundResource(R.color.white_light);
            tv_hire.setBackgroundResource(R.color.white_light);
            tv_reject.setBackgroundResource(R.color.white_light);
        } else if (company_action.equalsIgnoreCase("Shortlist")) {
            tv_shortlisted.setTextColor(getColor(R.color.white));
            tv_hire.setTextColor(getColor(R.color.black));
            tv_reject.setTextColor(getColor(R.color.black));
            tv_shortlisted.setBackgroundResource(R.color.colorPrimary);
            tv_hire.setBackgroundResource(R.color.white_light);
            tv_reject.setBackgroundResource(R.color.white_light);
        } else if (company_action.equalsIgnoreCase("Hire")) {
            tv_shortlisted.setTextColor(getColor(R.color.black));
            tv_hire.setTextColor(getColor(R.color.white));
            tv_reject.setTextColor(getColor(R.color.black));
            tv_shortlisted.setBackgroundResource(R.color.white_light);
            tv_hire.setBackgroundResource(R.color.colorPrimary);
            tv_reject.setBackgroundResource(R.color.white_light);
        } else if (company_action.equalsIgnoreCase("Cancel")) {
            tv_shortlisted.setTextColor(getColor(R.color.black));
            tv_hire.setTextColor(getColor(R.color.black));
            tv_reject.setTextColor(getColor(R.color.white));
            tv_shortlisted.setBackgroundResource(R.color.white_light);
            tv_hire.setBackgroundResource(R.color.white_light);
            tv_reject.setBackgroundResource(R.color.colorPrimary);

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_appointment:
                startActivity(new Intent(mContext, CreateAppointment.class)
                        .putExtra("user_id", userId)
                        .putExtra("first_name", first_name)
                        .putExtra("last_name", last_name)
                        .putExtra("user_profile", user_profile)
                        .putExtra("city", city)
                        .putExtra("state", state)
                        .putExtra("job_skills", job_skills.toString())
                        .putExtra("apply_id", apply_id)
                        .putExtra("job_id", jobId));

                break;


            case R.id.iv_reject:
                if (!TextUtils.isEmpty(jobId) && !TextUtils.isEmpty(userId)) {

                    HashMap<String, String> hashMap1 = new HashMap<>();
                    hashMap1.put(ParaName.KEYTOKEN, Config.GetUserToken());
                    hashMap1.put(ParaName.KEY_MATCHID, matchId);
                    hashMap1.put(ParaName.KEY_COMPANYSTATUS, "R");
                    hashMap1.put(ParaName.KEY_UID, userId);
                    hashMap1.put(ParaName.KEY_JOBID, jobId);
                    if (rest.isInterentAvaliable()) {
                        AppController.ShowDialogue("", mContext);
                        postLikeDislike(hashMap1);
                    } else rest.AlertForInternet();
                }
                break;
            case R.id.iv_accept:

                if (!TextUtils.isEmpty(jobId) && !TextUtils.isEmpty(userId)) {

                    HashMap<String, String> hashMap1 = new HashMap<>();
                    hashMap1.put(ParaName.KEYTOKEN, Config.GetUserToken());
                    hashMap1.put(ParaName.KEY_MATCHID, matchId);
                    hashMap1.put(ParaName.KEY_COMPANYSTATUS, "A");
                    hashMap1.put(ParaName.KEY_UID, userId);
                    hashMap1.put(ParaName.KEY_JOBID, jobId);
                    if (rest.isInterentAvaliable()) {
                        AppController.ShowDialogue("", mContext);
                        postLikeDislike(hashMap1);
                    } else rest.AlertForInternet();
                }

                break;

            case R.id.tv_download:
                if (!TextUtils.isEmpty(cv_file_link)) {
                   /* Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(cv_file_link));
                    startActivity(browserIntent);*/
                    AppController.callResume(mContext, cv_file_link);
                  /*  AppController.ShowDialogue("",mContext);
                    new RetrivePDFfromUrl().execute(cv_file_link    );*/
                }
                break;
            case R.id.tv_shortlisted:
                if (!TextUtils.isEmpty(apply_id)) {
                    if (rest.isInterentAvaliable()) {
                        if (!TextUtils.isEmpty(userId)) {
                            AppController.ShowDialogue("", mContext);
                            callActionService("Shortlist");
                        }

                    } else rest.AlertForInternet();
                }


                break;
            case R.id.tv_hire:
                if (!TextUtils.isEmpty(apply_id)) {
                    if (rest.isInterentAvaliable()) {
                        if (!TextUtils.isEmpty(userId)) {
                            AppController.ShowDialogue("", mContext);
                            callActionService("Hire");
                        }
                    } else rest.AlertForInternet();
                }
                break;
            case R.id.tv_reject:
                if (!TextUtils.isEmpty(apply_id)) {
                    if (rest.isInterentAvaliable()) {
                        if (!TextUtils.isEmpty(userId)) {
                            AppController.ShowDialogue("", mContext);
                            callActionService("Cancel");
                        }

                    } else rest.AlertForInternet();
                }
                break;
            default:
                break;
        }

    }

    private void callActionService(String action) {
        SwipeeApiClient.swipeeServiceInstance().companyAction(Config.GetUserToken(), apply_id, action).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    if (!responseBody.isJsonNull()) {
                        if (response.body().get("code").getAsInt() == 203) {
                            rest.showToast(response.body().get("message").getAsString());
                            setBackPressed();
                            AppController.loggedOut(mContext);
                        } else if (responseBody.get("status").getAsBoolean()) {
                            setAction(action);
                        }
                    }
                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });

    }

    private void postLikeDislike(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().postUSerAcceptReject(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject repo = response.body();
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        setBackPressed();
                        AppController.loggedOut(mContext);
                    } else if (repo.get("status").getAsBoolean()) {
                        startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", UserDetail.class.getSimpleName()));
                        UserDetail.instance.setBackPressed();
                    } else rest.showToast(repo.get("message").getAsString());
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }

    @Override
    protected void onPause() {
        if (vv_video != null) vv_video.pausePlayer();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void setJobtypeFlow(JsonArray mArrayUserJobType) {
        flow_jobtype.removeAllViews();
        if (mArrayUserJobType != null) {
            for (int i = 0; i < mArrayUserJobType.size(); i++) {
                LinearLayout linearLayout = new LinearLayout(mContext);
                LinearLayout linearLayoutF = new LinearLayout(mContext);
                FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                        FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams layoutParamsF = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, (int) (mContext.getResources().getDisplayMetrics().density * 32));
                layoutParamsF.setMargins((int) (mContext.getResources().getDisplayMetrics().density * 5), 0, (int) (mContext.getResources().getDisplayMetrics().density * 5), (int) (mContext.getResources().getDisplayMetrics().density * 8));
                linearLayoutF.setLayoutParams(layoutParams);
                linearLayout.setLayoutParams(layoutParamsF);
                linearLayout.setPadding((int) (mContext.getResources().getDisplayMetrics().density * 8), 0, (int) (mContext.getResources().getDisplayMetrics().density * 8), 0);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setBackgroundResource(R.drawable.primary_semiround_bg);
                PopinsRegularTextView bt = new PopinsRegularTextView(mContext);
                bt.setText(mArrayUserJobType.get(i).getAsJsonObject().get("job_type_name").getAsString());
                bt.setAllCaps(false);
                bt.setTextSize(12f);
                bt.setMaxLines(1);
                bt.setEllipsize(TextUtils.TruncateAt.END);
                bt.setTag(mArrayUserJobType.get(i).getAsJsonObject().get("job_type_name").getAsString());
                bt.setTextColor(mContext.getResources().getColor(R.color.white));
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams1.setMargins(0, 0, 0, 0);
                bt.setLayoutParams(layoutParams1);
                linearLayout.addView(bt);
                linearLayoutF.addView(linearLayout);
                linearLayoutF.setTag(mArrayUserJobType.get(i).getAsJsonObject().get("job_type_name").getAsString());
                flow_jobtype.addView(linearLayoutF);

            }
        }
    }

}