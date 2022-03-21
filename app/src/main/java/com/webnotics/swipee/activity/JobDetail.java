package com.webnotics.swipee.activity;

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
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webnotics.swipee.CustomUi.FlowLayout;
import com.webnotics.swipee.CustomUi.PopinsRegularTextView;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.Seeker.AppliedJobsActivity;
import com.webnotics.swipee.activity.Seeker.AppointmentActivity;
import com.webnotics.swipee.activity.Seeker.FeaturedJobsActivity;
import com.webnotics.swipee.activity.Seeker.JobListActivity;
import com.webnotics.swipee.activity.Seeker.LikedJobsActivity;
import com.webnotics.swipee.activity.Seeker.MatchedCompanyActivity;
import com.webnotics.swipee.activity.Seeker.SavedJobsActivity;
import com.webnotics.swipee.activity.Seeker.SeekerHomeActivity;
import com.webnotics.swipee.activity.company.CompanyAppoimentActivity;
import com.webnotics.swipee.activity.company.CompanyHomeActivity;
import com.webnotics.swipee.activity.company.PostedJobActivity;
import com.webnotics.swipee.fragments.seeker.MatchFragments;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.stream.IntStream;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobDetail extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    public static JobDetail instance;
    Context mContext;
    Rest rest;
    ImageView iv_back, iv_accept, iv_reject, iv_meetingType, iv_more;
    CircleImageView iv_logo;
    TextView tv_title, tv_title1, tv_location, tv_location1, tv_jobtype, tv_experience, tv_degree, tv_salary,
            tv_about, tv_perk, tv_apply, tv_postedfor;
    FlowLayout flow_skill;
    LinearLayout ll_main, ll_button, ll_appointment, ll_applied, join, ll_job_action, ll_action;
    private String from = "";
    private String company_id = "";
    private String resumeId = "";
    private String company_logo = "";
    TextView tv_industry, tv_workdays, tv_opening, tv_workshift, tv_appointment, tv_appointmenttime, tv_report, tv_block, tv_reschedule, tv_cancel_application, tv_closed, tv_active, tv_inactive;
    RelativeLayout rl_companyinfo;
    private String apply_id = "";
    private String company_name = "";
    private String company_state_name = "";
    private String company_city_name = "";
    private String company_country_name = "";
    private String posted_by = "";
    private String is_own_job = "";
    private String job_id = "";
    private String appointment_id = "";
    private String job_status_type = "";
    private String hiring_status_type = "";
    public JsonArray appointments = new JsonArray();
    private String match_id = "";
    private String company_match_status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        setContentView(R.layout.activity_job_detail);
        if (getIntent() != null) {
            job_id = getIntent().getStringExtra("id") != null ? getIntent().getStringExtra("id") : "";
            from = getIntent().getStringExtra("from") != null ? getIntent().getStringExtra("from") : "";
        }

        mContext = this;
        instance = this;
        rest = new Rest(mContext);

        iv_back = findViewById(R.id.iv_back);
        iv_logo = findViewById(R.id.iv_logo);
        tv_title = findViewById(R.id.tv_title);
        tv_title1 = findViewById(R.id.tv_title1);
        tv_location = findViewById(R.id.tv_location);
        tv_location1 = findViewById(R.id.tv_location1);
        tv_jobtype = findViewById(R.id.tv_jobtype);
        tv_experience = findViewById(R.id.tv_experience);
        tv_degree = findViewById(R.id.tv_degree);
        tv_salary = findViewById(R.id.tv_salary);
        tv_about = findViewById(R.id.tv_about);
        flow_skill = findViewById(R.id.flow_skill);
        tv_perk = findViewById(R.id.tv_perk);
        ll_main = findViewById(R.id.ll_main);
        tv_apply = findViewById(R.id.tv_apply);
        ll_button = findViewById(R.id.ll_button);
        iv_accept = findViewById(R.id.iv_accept);
        iv_reject = findViewById(R.id.iv_reject);
        tv_postedfor = findViewById(R.id.tv_postedfor);
        ll_appointment = findViewById(R.id.ll_appointment);
        ll_applied = findViewById(R.id.ll_applied);
        join = findViewById(R.id.join);
        tv_appointment = findViewById(R.id.tv_appointment);
        tv_appointmenttime = findViewById(R.id.tv_appointmenttime);
        iv_meetingType = findViewById(R.id.iv_meetingType);
        rl_companyinfo = findViewById(R.id.rl_companyinfo);
        iv_more = findViewById(R.id.iv_more);
        ll_job_action = findViewById(R.id.ll_job_action);
        tv_cancel_application = findViewById(R.id.tv_cancel_application);
        tv_block = findViewById(R.id.tv_block);
        tv_reschedule = findViewById(R.id.tv_reschedule);
        tv_report = findViewById(R.id.tv_report);
        ll_action = findViewById(R.id.ll_action);
        tv_active = findViewById(R.id.tv_active);
        tv_inactive = findViewById(R.id.tv_inactive);
        tv_closed = findViewById(R.id.tv_closed);
        tv_workdays = findViewById(R.id.tv_workdays);
        tv_opening = findViewById(R.id.tv_opening);
        tv_workshift = findViewById(R.id.tv_workshift);
        tv_industry = findViewById(R.id.tv_industry);

        iv_more.setVisibility(View.GONE);
        ll_job_action.setVisibility(View.GONE);


        resumeId = Config.GetCVID();

        if (rest.isInterentAvaliable()) {
            if (!TextUtils.isEmpty(job_id)) {
                AppController.ShowDialogue("", mContext);
                getJobDetail();
            }
        } else rest.AlertForInternet();

        iv_back.setOnClickListener(v -> onBackPressed());
        iv_accept.setOnClickListener(v -> {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
            hashMap.put(ParaName.KEY_JOBPOSTID, job_id);
            hashMap.put(ParaName.KEY_USERSTATUS, "A");
            hashMap.put(ParaName.KEY_COMPANYID, company_id);
            hashMap.put(ParaName.KEY_MATCHID, match_id);
            hashMap.put(ParaName.KEY_COMPANYSTATUS, company_match_status);

            if (rest.isInterentAvaliable()) {
                AppController.ShowDialogue("", mContext);
                postLikeDislike(hashMap);
            } else rest.AlertForInternet();
        });
        iv_reject.setOnClickListener(v -> {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
            hashMap.put(ParaName.KEY_JOBPOSTID, job_id);
            hashMap.put(ParaName.KEY_USERSTATUS, "R");
            hashMap.put(ParaName.KEY_COMPANYID, company_id);
            hashMap.put(ParaName.KEY_MATCHID, match_id);
            hashMap.put(ParaName.KEY_COMPANYSTATUS, company_match_status);
            if (rest.isInterentAvaliable()) {
                AppController.ShowDialogue("", mContext);
                postLikeDislike(hashMap);
            } else rest.AlertForInternet();
        });

        tv_apply.setOnClickListener(v -> {
            if (Config.GetPACKAGEEXP().equalsIgnoreCase("N")) {
                if (Config.GetLeftApplyCount() > 0 || Config.GetPackageId().equalsIgnoreCase("3")) {
                    if (!TextUtils.isEmpty(resumeId)) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                        hashMap.put(ParaName.KEY_JOBPOSTID, job_id);
                        hashMap.put(ParaName.KEY_CVID, resumeId);
                        if (rest.isInterentAvaliable()) {
                            AppController.ShowDialogue("", mContext);
                            postApplyJob(hashMap);
                        } else rest.AlertForInternet();
                    } else rest.showToast("Upload resume first.");
                } else rest.showToast("Your daily apply limit exceeded");
            } else rest.showToast(getString(R.string.packageexpseekr));

        });

        iv_logo.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(company_logo))
                AppController.callFullImage(mContext, company_logo);
        });

        rl_companyinfo.setOnClickListener(v -> startActivity(new Intent(mContext, CompanyProfile.class).putExtra("company_id", company_id)));

        iv_more.setOnClickListener(v -> {
            if (ll_job_action.getVisibility() == View.VISIBLE)
                ll_job_action.setVisibility(View.GONE);
            else ll_job_action.setVisibility(View.VISIBLE);
        });
        tv_block.setOnClickListener(v -> {
            blockJobPopup();
            ll_job_action.setVisibility(View.GONE);
        });
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
            cancel.setOnClickListener(v14 -> progressdialog.dismiss());
            submit.setOnClickListener(v15 -> {
                if (!TextUtils.isEmpty(reason.getText().toString().replaceAll(" ", ""))) {
                    AppController.ShowDialogue("", mContext);
                    if (Config.isSeeker()) reportJob(job_id, apply_id, reason.getText().toString());
                    progressdialog.dismiss();
                } else rest.showToast("Enter report reason");

            });

            try {
                progressdialog.show();
            } catch (Exception ignored) {}
        });
        tv_cancel_application.setOnClickListener(v -> {
            ll_job_action.setVisibility(View.GONE);
            cancelApplicationPopup();
        });
        tv_reschedule.setOnClickListener(v -> {
            ll_job_action.setVisibility(View.GONE);
            startActivity(new Intent(mContext, AppointmentAction.class)
                    .putExtra("company_id", company_id)
                    .putExtra("company_logo", company_logo)
                    .putExtra("company_name", company_name)
                    .putExtra("company_city_name", company_city_name)
                    .putExtra("company_state_name", company_state_name)
                    .putExtra("company_country_name", company_country_name)
                    .putExtra("posted_by", posted_by)
                    .putExtra("is_own_job", is_own_job)
                    .putExtra("apply_id", apply_id)
                    .putExtra("job_id", job_id)
                    .putExtra("appointment_id", appointment_id)
                    .putExtra("from", JobDetail.class.getSimpleName())
                    .putExtra("action", "Reschedule")
            );
        });

        tv_active.setOnClickListener(v -> {
            if (!job_status_type.equalsIgnoreCase("Active")) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(ParaName.KEY_JOBPOSTID, job_id);
                hashMap.put(ParaName.KEY_JOBSTATUS, "Y");
                hashMap.put(ParaName.KEY_ISHIRINGCLOSED, "N");
                setJob(hashMap);
            }

        });
        tv_inactive.setOnClickListener(v -> {
            if (job_status_type.equalsIgnoreCase("Active") || hiring_status_type.equalsIgnoreCase("Closed")) {
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put(ParaName.KEY_JOBPOSTID, job_id);
                hashMap.put(ParaName.KEY_JOBSTATUS, "N");
                hashMap.put(ParaName.KEY_ISHIRINGCLOSED, "N");
                setJob(hashMap);
            }

        });

        tv_closed.setOnClickListener(v -> {
            if (!hiring_status_type.equalsIgnoreCase("Closed")) {
                Dialog progressdialog = new Dialog(mContext);
                progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressdialog.setContentView(R.layout.close_job_popup);
                progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.gravity = Gravity.CENTER;
                progressdialog.getWindow().setAttributes(lp);

                RadioButton radioButton1 = progressdialog.findViewById(R.id.radioButton1);
                radioButton1.setChecked(true);
                RadioButton radioButton2 = progressdialog.findViewById(R.id.radioButton2);
                RadioButton radioButton3 = progressdialog.findViewById(R.id.radioButton3);
                RadioButton radioButton4 = progressdialog.findViewById(R.id.radioButton4);
                RadioButton radioButton5 = progressdialog.findViewById(R.id.radioButton5);

                progressdialog.findViewById(R.id.tv_close).setOnClickListener(v13 -> {
                    String reason = "";
                    if (radioButton1.isChecked()) reason = radioButton1.getText().toString();
                    else if (radioButton2.isChecked()) reason = radioButton2.getText().toString();
                    else if (radioButton3.isChecked()) reason = radioButton3.getText().toString();
                    else if (radioButton4.isChecked()) reason = radioButton4.getText().toString();
                    else if (radioButton5.isChecked()) reason = radioButton5.getText().toString();
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(ParaName.KEY_JOBPOSTID, job_id);
                    hashMap.put(ParaName.KEY_ISHIRINGCLOSED, "Y");
                    hashMap.put(ParaName.KEY_JOBSTATUS, "N");
                    hashMap.put(ParaName.KEY_CLOSINGREASON, reason);
                    setJob(hashMap);
                    progressdialog.dismiss();

                });
                progressdialog.findViewById(R.id.tv_later).setOnClickListener(v1 -> progressdialog.dismiss());
                progressdialog.findViewById(R.id.iv_close).setOnClickListener(v12 -> progressdialog.dismiss());
                try {
                    progressdialog.show();
                } catch (Exception ignored) {}
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (from.equalsIgnoreCase("Notification"))
            startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "match"));
        finish();
    }

    private void cancelApplicationPopup() {
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
        tv_title.setText("Cancel Application");
        tv_detail.setText("Are you sure, you want to cancel this job application ?");

        progressdialog.findViewById(R.id.tv_yes).setOnClickListener(v -> {
            progressdialog.dismiss();
            AppController.ShowDialogue("", mContext);
            cancelJob(apply_id);

        });
        progressdialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> progressdialog.dismiss());
        try {
            progressdialog.show();
        } catch (Exception ignored) {
        }

    }

    private void blockJobPopup() {
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
        tv_title.setText("Block Job");
        tv_detail.setText("Are you sure, you want to block this job ?");

        progressdialog.findViewById(R.id.tv_yes).setOnClickListener(v -> {
            progressdialog.dismiss();
            AppController.ShowDialogue("", mContext);
            if (Config.isSeeker())
                blockJob(job_id, apply_id);

        });
        progressdialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> progressdialog.dismiss());
        try {
            progressdialog.show();
        } catch (Exception ignored) {
        }

    }

    private void getJobDetail() {
        SwipeeApiClient.swipeeServiceInstance().getSingleJobData(Config.GetUserToken(), job_id, Config.isSeeker() ? "seeker" : "").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 200 && response.body().get("status").getAsBoolean()) {
                        JsonObject jsonObject = response.body();
                        JsonObject dataObject = jsonObject.has("data") ? !jsonObject.get("data").isJsonNull() ? jsonObject.get("data").getAsJsonObject() : new JsonObject() : new JsonObject();
                        JsonObject job_data = dataObject.has("job_data") ? !dataObject.get("job_data").isJsonNull() ? dataObject.get("job_data").getAsJsonObject() : new JsonObject() : new JsonObject();
                        //JsonArray advert_companies=dataObject.has("advert_companies")?dataObject.get("advert_companies").getAsJsonArray():new JsonArray();
                        if (job_data.size() > 0) {
                            company_name = job_data.has("company_name") ? job_data.get("company_name").isJsonNull() ? "" : job_data.get("company_name").getAsString() : "";
                            company_id = job_data.has("company_id") ? job_data.get("company_id").isJsonNull() ? "" : job_data.get("company_id").getAsString() : "";
                            company_logo = job_data.has("company_logo") ? job_data.get("company_logo").isJsonNull() ? "" : job_data.get("company_logo").getAsString() : "";
                            String job_title = job_data.has("job_title") ? job_data.get("job_title").isJsonNull() ? "" : job_data.get("job_title").getAsString() : "";
                            String job_description = job_data.has("job_description") ? job_data.get("job_description").isJsonNull() ? "" : job_data.get("job_description").getAsString() : "";
                            String job_experience = job_data.has("job_experience") ? job_data.get("job_experience").isJsonNull() ? "" : job_data.get("job_experience").getAsString() : "";
                            String job_city = job_data.has("job_city") ? job_data.get("job_city").isJsonNull() ? "" : job_data.get("job_city").getAsString() : "";
                            String job_state = job_data.has("job_state") ? job_data.get("job_state").isJsonNull() ? "" : job_data.get("job_state").getAsString() : "";
                            String job_country = job_data.has("job_country") ? job_data.get("job_country").isJsonNull() ? "" : job_data.get("job_country").getAsString() : "";
                            String job_skills = job_data.has("job_skills") ? job_data.get("job_skills").isJsonNull() ? "" : job_data.get("job_skills").getAsString() : "";
                            String job_type = job_data.has("job_type") ? job_data.get("job_type").isJsonNull() ? "" : job_data.get("job_type").getAsString() : "";
                            String job_benefits = job_data.has("job_benefits") ? job_data.get("job_benefits").isJsonNull() ? "" : job_data.get("job_benefits").getAsString() : "";
                            String job_degree = job_data.has("job_degree") ? job_data.get("job_degree").isJsonNull() ? "" : job_data.get("job_degree").getAsString() : "";
                            String job_max_salary = job_data.has("job_max_salary") ? job_data.get("job_max_salary").isJsonNull() ? "" : job_data.get("job_max_salary").getAsString() : "";
                            company_match_status = job_data.has("company_match_status") ? job_data.get("company_match_status").isJsonNull() ? "" : job_data.get("company_match_status").getAsString() : "";
                            String user_match_status = job_data.has("user_match_status") ? job_data.get("user_match_status").isJsonNull() ? "" : job_data.get("user_match_status").getAsString() : "";
                            match_id = job_data.has("match_id") ? job_data.get("match_id").isJsonNull() ? "" : job_data.get("match_id").getAsString() : "";
                            String job_opening_numbers = job_data.has("job_opening_numbers") ? job_data.get("job_opening_numbers").isJsonNull() ? "" : job_data.get("job_opening_numbers").getAsString() : "";
                            String job_working_days = job_data.has("job_working_days") ? job_data.get("job_working_days").isJsonNull() ? "" : job_data.get("job_working_days").getAsString() : "";
                            String job_shift = job_data.has("job_shift") ? job_data.get("job_shift").isJsonNull() ? "" : job_data.get("job_shift").getAsString() : "";
                            String job_industry = job_data.has("job_industry") ? job_data.get("job_industry").isJsonNull() ? "" : job_data.get("job_industry").getAsString() : "";
                            tv_workdays.setText(MessageFormat.format("{0} Working Days", job_working_days));
                            tv_workshift.setText(job_shift);
                            tv_opening.setText(job_opening_numbers);
                            tv_industry.setText(job_industry);

                            company_state_name = job_data.has("company_state_name") ? job_data.get("company_state_name").isJsonNull() ? "" : job_data.get("company_state_name").getAsString() : "";
                            company_city_name = job_data.has("company_city_name") ? job_data.get("company_city_name").isJsonNull() ? "" : job_data.get("company_city_name").getAsString() : "";
                            company_country_name = job_data.has("company_country_name") ? job_data.get("company_country_name").isJsonNull() ? "" : job_data.get("company_country_name").getAsString() : "";
                            is_own_job = job_data.has("is_own_job") ? job_data.get("is_own_job").isJsonNull() ? "" : job_data.get("is_own_job").getAsString() : "";
                            posted_by = job_data.has("posted_by") ? job_data.get("posted_by").isJsonNull() ? "" : job_data.get("posted_by").getAsString() : "";
                            apply_id = job_data.has("job_apply_id") ? job_data.get("job_apply_id").isJsonNull() ? "" : job_data.get("job_apply_id").getAsString() : "";
                            job_status_type = job_data.has("job_status_type") ? job_data.get("job_status_type").isJsonNull() ? "" : job_data.get("job_status_type").getAsString() : "";
                            hiring_status_type = job_data.has("hiring_status_type") ? job_data.get("hiring_status_type").isJsonNull() ? "" : job_data.get("hiring_status_type").getAsString() : "";
                            if (Config.isSeeker()) {
                                ll_action.setVisibility(View.GONE);
                            } else {
                                ll_action.setVisibility(View.VISIBLE);

                                if (hiring_status_type.equalsIgnoreCase("Closed")) {
                                    tv_closed.setBackgroundColor(getColor(R.color.red));
                                    tv_active.setBackgroundColor(getColor(R.color.white_light));
                                    tv_inactive.setBackgroundColor(getColor(R.color.white_light));

                                    tv_closed.setTextColor(getColor(R.color.white));
                                    tv_active.setTextColor(getColor(R.color.black));
                                    tv_inactive.setTextColor(getColor(R.color.black));
                                } else if (hiring_status_type.equalsIgnoreCase("Open")) {
                                    if (job_status_type.equalsIgnoreCase("Active")) {
                                        tv_closed.setBackgroundColor(getColor(R.color.white_light));
                                        tv_active.setBackgroundColor(getColor(R.color.green));
                                        tv_inactive.setBackgroundColor(getColor(R.color.white_light));

                                        tv_closed.setTextColor(getColor(R.color.black));
                                        tv_active.setTextColor(getColor(R.color.white));
                                        tv_inactive.setTextColor(getColor(R.color.black));
                                    } else {
                                        tv_closed.setBackgroundColor(getColor(R.color.white_light));
                                        tv_active.setBackgroundColor(getColor(R.color.white_light));
                                        tv_inactive.setBackgroundColor(getColor(R.color.gray));

                                        tv_closed.setTextColor(getColor(R.color.black));
                                        tv_active.setTextColor(getColor(R.color.black));
                                        tv_inactive.setTextColor(getColor(R.color.black));
                                    }
                                }
                            }

                            JsonObject resume = job_data.has("user_resumes") ? job_data.get("user_resumes").isJsonNull() ? new JsonObject() : job_data.get("user_resumes").getAsJsonObject() : new JsonObject();
                            appointments = job_data.has("appointment") ? job_data.get("appointment").isJsonNull() ? new JsonArray() : job_data.get("appointment").getAsJsonArray() : new JsonArray();

                            resumeId = resume.has("cv_id") ? resume.get("cv_id").getAsString() : "";
                            Config.SetCVID(resumeId);
                            iv_more.setVisibility(View.VISIBLE);
                            if (is_own_job.equalsIgnoreCase("N"))
                                tv_postedfor.setVisibility(View.VISIBLE);
                            else tv_postedfor.setVisibility(View.GONE);
                            tv_postedfor.setText(MessageFormat.format("Posted for- {0}", company_name));
                            tv_title.setText(job_title);
                            tv_title1.setText(posted_by);
                            tv_location.setText(MessageFormat.format("{0}, {1}, {2}", job_city, job_state, job_country));
                            tv_location1.setText(MessageFormat.format("{0}, {1}, {2}", company_city_name, company_state_name, company_country_name));
                            tv_experience.setText(MessageFormat.format("{0} Yrs", job_experience));
                            tv_jobtype.setText(job_type);
                            tv_degree.setText(job_degree);
                            tv_salary.setText(MessageFormat.format("{0} {1}", job_max_salary, getString(R.string.salarypermonth)));
                            tv_about.setText(job_description);
                            ll_main.setVisibility(View.VISIBLE);
                            iv_more.setVisibility(Config.isSeeker() ? View.VISIBLE : View.GONE);

                            if (from.equalsIgnoreCase(NotificationAppointmentAction.class.getSimpleName())) {
                                ll_button.setVisibility(View.GONE);
                                iv_reject.setVisibility(View.GONE);
                                iv_accept.setVisibility(View.GONE);
                                ll_applied.setVisibility(View.GONE);
                                ll_appointment.setVisibility(View.GONE);
                                iv_more.setVisibility(View.GONE);
                                ll_action.setVisibility(View.GONE);
                            } else if (from.equalsIgnoreCase(CompanyAppoimentActivity.class.getSimpleName()) || from.equalsIgnoreCase(PostedJobActivity.class.getSimpleName())) {
                                ll_button.setVisibility(View.GONE);
                                iv_reject.setVisibility(View.GONE);
                                iv_accept.setVisibility(View.GONE);
                                ll_applied.setVisibility(View.GONE);
                                ll_appointment.setVisibility(View.GONE);
                            } else if (!TextUtils.isEmpty(apply_id)) {
                                ll_button.setVisibility(View.GONE);
                                iv_reject.setVisibility(View.GONE);
                                iv_accept.setVisibility(View.GONE);
                                if (appointments.size() > 0) {
                                    JsonObject appointment = appointments.get(0).getAsJsonObject();
                                    String appointment_id = appointment.has("appointment_id") ? appointment.get("appointment_id").getAsString() : "";
                                    String appointment_type = appointment.has("appointment_type") ? appointment.get("appointment_type").getAsString() : "";
                                    String appointment_date = appointment.has("appointment_date") ? appointment.get("appointment_date").getAsString() : "";
                                    String appointment_start_at = appointment.has("appointment_start_at") ? appointment.get("appointment_start_at").getAsString() : "";
                                    String appointment_end_at = appointment.has("appointment_end_at") ? appointment.get("appointment_end_at").getAsString() : "";
                                    String job_title1 = appointment.has("job_title") ? appointment.get("job_title").getAsString() : "";

                                    tv_appointment.setText(job_title1);
                                    ll_appointment.setVisibility(View.VISIBLE);
                                    boolean isShow = IntStream.range(0, appointments.size()).anyMatch(i -> appointments.get(i).getAsJsonObject().get("appointment_status").getAsString().equalsIgnoreCase("A"));
                                    tv_reschedule.setVisibility(isShow ? View.VISIBLE : View.GONE);
                                    tv_cancel_application.setVisibility(View.VISIBLE);
                                    ll_applied.setVisibility(View.GONE);
                                    SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                                    SimpleDateFormat formatout = new SimpleDateFormat("hh:mm aa");
                                    Date date;
                                    try {
                                        date = format.parse(appointment_start_at);
                                        String date1 = formatout.format(date);
                                        tv_appointmenttime.setText(String.format("%s %s %s", "Appointment scheduled at: ", appointment_date, date1.toUpperCase(Locale.ROOT)));
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Date calendarDate = Calendar.getInstance().getTime();
                                    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                    try {
                                        Date date2 = formatDate.parse(appointment_date + " " + appointment_start_at);
                                        Date date3 = formatDate.parse(appointment_date + " " + appointment_end_at);
                                        if (date2 != null && date3 != null) {
                                            if (date2.before(calendarDate) && date3.after(calendarDate)) {
                                                join.setVisibility(View.VISIBLE);
                                                if (appointment_type.equalsIgnoreCase("chat"))
                                                    iv_meetingType.setImageResource(R.drawable.ic_icon_material_chat_bubble);
                                                else if (appointment_type.equalsIgnoreCase("online_meeting"))
                                                    iv_meetingType.setImageResource(R.drawable.ic_icon_awesome_video_fill);
                                                else if (appointment_type.equalsIgnoreCase("call"))
                                                    iv_meetingType.setImageResource(R.drawable.ic_telephonefill);

                                                ll_appointment.setOnClickListener(v -> {
                                                    Intent resultIntent = new Intent(mContext, AppointmentDetail.class);
                                                    resultIntent.putExtra(ParaName.KEY_APPOINTMENTID, appointment_id);
                                                    resultIntent.putExtra("from", JobDetail.class.getSimpleName());
                                                    mContext.startActivity(resultIntent);
                                                });

                                            } else join.setVisibility(View.GONE);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    ll_applied.setVisibility(View.VISIBLE);
                                    ll_appointment.setVisibility(View.GONE);
                                    tv_reschedule.setVisibility(View.GONE);
                                    tv_cancel_application.setVisibility(View.VISIBLE);
                                }
                            } else if (from.equalsIgnoreCase(AppliedJobsActivity.class.getSimpleName())) {
                                ll_button.setVisibility(View.GONE);
                                iv_reject.setVisibility(View.GONE);
                                iv_accept.setVisibility(View.GONE);
                                tv_reschedule.setVisibility(View.GONE);
                                ll_applied.setVisibility(View.VISIBLE);
                                ll_appointment.setVisibility(View.GONE);
                                tv_cancel_application.setVisibility(View.VISIBLE);
                            } else if (from.equalsIgnoreCase(NotificationActivity.class.getSimpleName()) || from.equalsIgnoreCase("Notification") || from.equalsIgnoreCase(SavedJobsActivity.class.getSimpleName()) || from.equalsIgnoreCase(LikedJobsActivity.class.getSimpleName())
                                    || from.equalsIgnoreCase(JobListActivity.class.getSimpleName()) || from.equalsIgnoreCase(MatchFragments.class.getSimpleName())
                                    || from.equalsIgnoreCase(MatchedCompanyActivity.class.getSimpleName()) || from.equalsIgnoreCase(FeaturedJobsActivity.class.getSimpleName())) {
                                if (company_match_status.equalsIgnoreCase("A") && user_match_status.equalsIgnoreCase("A")) {
                                    if (job_status_type.equalsIgnoreCase("Active") && hiring_status_type.equalsIgnoreCase("Open")) {
                                        ll_button.setVisibility(View.VISIBLE);
                                        tv_apply.setVisibility(View.VISIBLE);
                                    } else {
                                        ll_button.setVisibility(View.GONE);
                                        tv_apply.setVisibility(View.GONE);
                                    }

                                    iv_reject.setVisibility(View.GONE);
                                    iv_accept.setVisibility(View.GONE);
                                    ll_appointment.setVisibility(View.GONE);
                                    ll_applied.setVisibility(View.GONE);
                                    tv_reschedule.setVisibility(View.GONE);
                                    tv_cancel_application.setVisibility(View.GONE);
                                } else if (company_match_status.equalsIgnoreCase("A")) {
                                    if (job_status_type.equalsIgnoreCase("Active") && hiring_status_type.equalsIgnoreCase("Open")) {
                                        ll_button.setVisibility(View.VISIBLE);
                                        tv_apply.setVisibility(View.VISIBLE);
                                    } else {
                                        ll_button.setVisibility(View.GONE);
                                        tv_apply.setVisibility(View.GONE);
                                    }

                                    iv_reject.setVisibility(View.GONE);
                                    iv_accept.setVisibility(View.GONE);
                                    ll_appointment.setVisibility(View.GONE);
                                    ll_applied.setVisibility(View.GONE);
                                    tv_reschedule.setVisibility(View.GONE);
                                    tv_cancel_application.setVisibility(View.GONE);
                                } else if (!user_match_status.equalsIgnoreCase("A")) {
                                    ll_button.setVisibility(View.GONE);
                                    tv_apply.setVisibility(View.GONE);
                                    iv_reject.setVisibility(View.VISIBLE);
                                    iv_accept.setVisibility(View.VISIBLE);
                                    ll_appointment.setVisibility(View.GONE);
                                    ll_applied.setVisibility(View.GONE);
                                    tv_reschedule.setVisibility(View.GONE);
                                    tv_cancel_application.setVisibility(View.GONE);
                                } else {
                                    ll_button.setVisibility(View.GONE);
                                    iv_reject.setVisibility(View.GONE);
                                    iv_accept.setVisibility(View.GONE);
                                    ll_appointment.setVisibility(View.GONE);
                                    ll_applied.setVisibility(View.GONE);
                                    tv_reschedule.setVisibility(View.GONE);
                                    tv_cancel_application.setVisibility(View.GONE);
                                }
                            } else {
                                ll_button.setVisibility(View.GONE);
                                iv_reject.setVisibility(View.GONE);
                                iv_accept.setVisibility(View.GONE);
                                ll_appointment.setVisibility(View.GONE);
                                ll_applied.setVisibility(View.GONE);
                                tv_reschedule.setVisibility(View.GONE);
                                tv_cancel_application.setVisibility(View.GONE);
                            }

                            Glide.with(mContext).load(company_logo)
                                    .apply(new RequestOptions().placeholder(R.drawable.ic_swipee_black).error(R.drawable.ic_swipee_black)).into(iv_logo);
                            job_benefits = getString(R.string.dot) + getString(R.string.space) + job_benefits;
                            job_benefits = job_benefits.replaceAll(",", getString(R.string.nextline) + getString(R.string.dot) + getString(R.string.space));
                            tv_perk.setText(Html.fromHtml(job_benefits));
                            String[] strings = job_skills.split(",");
                            if (strings.length > 0)
                                for (String string : strings) {

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
                                    bt.setText(string);
                                    bt.setAllCaps(false);
                                    bt.setTextSize(12f);
                                    bt.setTag(string);
                                    bt.setMaxLines(1);
                                    bt.setEllipsize(TextUtils.TruncateAt.END);
                                    bt.setTextColor(mContext.getResources().getColor(R.color.white));
                                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                                    layoutParams1.setMargins(0, 0, 0, 0);
                                    bt.setLayoutParams(layoutParams1);

                                    linearLayout.addView(bt);
                                    linearLayoutF.addView(linearLayout);
                                    linearLayoutF.setTag(string);
                                    flow_skill.addView(linearLayoutF);
                                }
                        }
                    } else if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        setBackPressed();
                        AppController.loggedOut(mContext);
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
        SwipeeApiClient.swipeeServiceInstance().postJobAcceptReject(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 200 && response.body().get("status").getAsBoolean()) {
                        setBackPressed();
                    } else if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        setBackPressed();
                        AppController.loggedOut(mContext);
                    }
                } else rest.showToast("Something went wrong");
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }

    private void postApplyJob(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().postApplyJob(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 200 && response.body().get("status").getAsBoolean()) {
                        Config.SetLeftApplyCount(Config.GetLeftApplyCount() - 1);
                        setBackPressed();
                    } else {
                        rest.showToast(response.body().get("message").getAsString());
                        if (response.body().get("code").getAsInt() == 203) {
                            setBackPressed();
                            AppController.loggedOut(mContext);
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


    private void blockJob(String jobId, String applyId) {
        SwipeeApiClient.swipeeServiceInstance().blockJob(Config.GetUserToken(), applyId, jobId).enqueue(new Callback<JsonObject>() {
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

    private void reportJob(String jobId, String applyId, String issue) {
        SwipeeApiClient.swipeeServiceInstance().reportJob(Config.GetUserToken(), applyId, jobId, issue).enqueue(new Callback<JsonObject>() {
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

    private void cancelJob(String applyId) {
        SwipeeApiClient.swipeeServiceInstance().cancelJobApplication(Config.GetUserToken(), applyId).enqueue(new Callback<JsonObject>() {
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

        } else if (from.equalsIgnoreCase("Notification")) {
            if (Config.isSeeker()) startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "match"));
            else startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", "match"));
        } else if (from.equalsIgnoreCase(NotificationActivity.class.getSimpleName())) {
            if (NotificationActivity.instance != null) NotificationActivity.instance.finish();
            if (Config.isSeeker()) startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "match"));
            else startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", "match"));
        } else if (from.equalsIgnoreCase(AppliedJobsActivity.class.getSimpleName())) {
            if (AppliedJobsActivity.instance != null)
                AppliedJobsActivity.instance.onBackPressed();
        } else if (from.equalsIgnoreCase(SavedJobsActivity.class.getSimpleName())) {
            if (SavedJobsActivity.instance != null)
                SavedJobsActivity.instance.onBackPressed();
            startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "match"));
        } else if (from.equalsIgnoreCase(FeaturedJobsActivity.class.getSimpleName())) {
            if (FeaturedJobsActivity.instance != null)
                FeaturedJobsActivity.instance.onBackPressed();
            startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "match"));
        } else if (from.equalsIgnoreCase(LikedJobsActivity.class.getSimpleName())) {
            if (LikedJobsActivity.instance != null)
                LikedJobsActivity.instance.onBackPressed();
            startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "match"));
        } else if (from.equalsIgnoreCase(MatchFragments.class.getSimpleName())) {
            startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "match"));
        } else if (from.equalsIgnoreCase(MatchedCompanyActivity.class.getSimpleName())) {
            if (MatchedCompanyActivity.instance != null)
                MatchedCompanyActivity.instance.onBackPressed();
            startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "match"));
        } else if (from.equalsIgnoreCase(JobListActivity.class.getSimpleName())) {
            if (JobListActivity.instance != null)
                JobListActivity.instance.onBackPressed();
            startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "near"));
        } else if (from.equalsIgnoreCase(AppointmentActivity.class.getSimpleName())) {
            if (AppointmentActivity.instance != null)
                AppointmentActivity.instance.onBackPressed();
            startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "match"));
        } else if (from.equalsIgnoreCase(CompanyAppoimentActivity.class.getSimpleName())) {
            if (CompanyAppoimentActivity.instance != null)
                CompanyAppoimentActivity.instance.onBackPressed();
            startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", JobDetail.class.getSimpleName()));
        } else if (from.equalsIgnoreCase(PostedJobActivity.class.getSimpleName())) {
            if (PostedJobActivity.instance != null)
                PostedJobActivity.instance.onBackPressed();
            startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", JobDetail.class.getSimpleName()));
        }  finish();

    }

    public void setJob(HashMap<String, String> hashMap) {
        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            setJobStatus(hashMap);
        }
    }

    private void setJobStatus(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().setPostedStatus(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responceBody = response.body();
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        setBackPressed();
                        AppController.loggedOut(mContext);
                    } else if (responceBody.get("code").getAsInt() == 200 && responceBody.get("status").getAsBoolean())
                        setBackPressed();
                    else rest.showToast(responceBody.get("message").getAsString());
                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }

}