package com.webnotics.swipee.activity.company;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonObject;
import com.webnotics.swipee.CustomUi.FlowLayout;
import com.webnotics.swipee.CustomUi.PopinsRegularTextView;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobPreview extends AppCompatActivity {

    Context mContext;
    Rest rest;
    ImageView iv_back;
    CircleImageView iv_logo;
    TextView tv_title, tv_postedfor, tv_title1, tv_opening, tv_workshift, tv_workdays, tv_location, tv_location1, tv_jobtype, tv_jobtype1, tv_experience, tv_degree, tv_salary,
            tv_about, tv_perk, tv_apply, tv_language;
    FlowLayout flow_skill;
    LinearLayout ll_main, ll_button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        setContentView(R.layout.activity_job_preview);

        mContext = this;
        rest = new Rest(mContext);

        iv_back = findViewById(R.id.iv_back);
        tv_workdays = findViewById(R.id.tv_workdays);
        tv_opening = findViewById(R.id.tv_opening);
        tv_workshift = findViewById(R.id.tv_workshift);
        iv_logo = findViewById(R.id.iv_logo);
        tv_title = findViewById(R.id.tv_title);
        tv_title1 = findViewById(R.id.tv_title1);
        tv_location = findViewById(R.id.tv_location);
        tv_location1 = findViewById(R.id.tv_location1);
        tv_jobtype = findViewById(R.id.tv_jobtype);
        tv_jobtype1 = findViewById(R.id.tv_jobtype1);
        tv_experience = findViewById(R.id.tv_experience);
        tv_degree = findViewById(R.id.tv_degree);
        tv_salary = findViewById(R.id.tv_salary);
        tv_about = findViewById(R.id.tv_about);
        flow_skill = findViewById(R.id.flow_skill);
        tv_perk = findViewById(R.id.tv_perk);
        tv_language = findViewById(R.id.tv_language);
        ll_main = findViewById(R.id.ll_main);
        tv_apply = findViewById(R.id.tv_apply);
        ll_button = findViewById(R.id.ll_button);
        tv_postedfor = findViewById(R.id.tv_postedfor);

        if (getIntent() != null) {
            Intent intent = getIntent();
            String industryName = intent.getStringExtra("industryName");
            String company = intent.getStringExtra("company");
            String jobTitle = intent.getStringExtra("jobTitle");
            String employmentType = intent.getStringExtra("employmentType");
            String jobLocation = intent.getStringExtra("jobLocation");
            String numberOpening = intent.getStringExtra("numberOpening");
            String qualificationName = intent.getStringExtra("qualificationName");
            String experience = intent.getStringExtra("experience");
            String salaryMin = intent.getStringExtra("salaryMin");
            String salaryMax = intent.getStringExtra("salaryMax");
            String salaryType = intent.getStringExtra("salaryType");
            String description = intent.getStringExtra("description");
            String perkBenefit = intent.getStringExtra("perkBenefit");
            String workShift = intent.getStringExtra("workShift");
            String workDays = intent.getStringExtra("workDays");
            String languages = intent.getStringExtra("languages");
            String isOwn = intent.getStringExtra("isOwn");
            String isFresher = intent.getStringExtra("isFresher");
            String cityId = intent.getStringExtra("cityId");
            String industryId = intent.getStringExtra("industryId");
            String employmentId = intent.getStringExtra("employmentId");
            String workDayId = intent.getStringExtra("workDayId");
            String workShiftId = intent.getStringExtra("workShiftId");
            String salaryTypeId = intent.getStringExtra("salaryTypeId");
            String experienceId = intent.getStringExtra("experienceId");
            ArrayList<String> numbersList = (ArrayList<String>) intent.getSerializableExtra("skillList");
            ArrayList<String> skillIds = (ArrayList<String>) intent.getSerializableExtra("skillListIds");
            ArrayList<String> perkIds = (ArrayList<String>) intent.getSerializableExtra("perkIds");
            ArrayList<String> degreeLevels = (ArrayList<String>) intent.getSerializableExtra("degreeLevels");
            ArrayList<String> degreeIds = (ArrayList<String>) intent.getSerializableExtra("degreeIds");
            ArrayList<String> languageIds = (ArrayList<String>) intent.getSerializableExtra("languageIds");

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
            hashMap.put(ParaName.KEY_COMPANYNAME, company);
            hashMap.put(ParaName.KEY_JOBTITLE, jobTitle);
            hashMap.put(ParaName.KEY_JOBDESCRIPTION, description);
            hashMap.put(ParaName.KEY_JOBSKILLID, skillIds.toString());
            hashMap.put(ParaName.KEY_CITY, cityId);
            hashMap.put(ParaName.KEY_JOBTYPEID, employmentId);
            hashMap.put(ParaName.KEY_INDUSTRY, industryId);
            hashMap.put(ParaName.KEY_ISOWNJOB, isOwn);
            hashMap.put(ParaName.KEY_JOBOPENING, numberOpening);
            hashMap.put(ParaName.KEY_JOBDEGREEID, degreeIds.toString());
            hashMap.put(ParaName.KEY_DEGREETYPEID, degreeLevels.toString());
            hashMap.put(ParaName.KEY_EXPERIENCEID, experienceId);
            hashMap.put(ParaName.KEY_ISFRESHERJOB, isFresher);
            hashMap.put(ParaName.KEY_JOBMINSALARY, salaryMin);
            hashMap.put(ParaName.KEY_JOBMAXSALARY, salaryMax);
            hashMap.put(ParaName.KEY_JOBSALARYPERIOD, salaryTypeId);
            hashMap.put(ParaName.KEY_JOBBENEFITID, perkIds.toString());
            hashMap.put(ParaName.KEY_JOBSHIFTID, workShiftId);
            hashMap.put(ParaName.KEY_JOBWORKINGDAY, workDayId);
            hashMap.put(ParaName.KEY_JOBLANGUAGE, languageIds.toString());

            tv_title.setText(industryName);
            tv_title1.setText(Config.GeCompanyName());
            tv_location.setText(jobLocation);
            tv_location1.setText(MessageFormat.format("{0} ,{1}", Config.GetCity(), Config.GetState()));
            tv_experience.setText(MessageFormat.format("{0} Yrs", experience));
            tv_jobtype.setText(employmentType);
            tv_jobtype1.setText(employmentType);
            tv_degree.setText(qualificationName);
            tv_salary.setText(MessageFormat.format("{0} {1}", salaryMax, getString(R.string.salarypermonth)));
            tv_about.setText(description);
            tv_workdays.setText(MessageFormat.format("{0} Working Days", workDays));
            tv_workshift.setText(workShift);
            tv_opening.setText(numberOpening);
            ll_main.setVisibility(View.VISIBLE);
            if (isOwn.equalsIgnoreCase("N")) {
                tv_postedfor.setVisibility(View.VISIBLE);
                tv_postedfor.setText(MessageFormat.format("Posted for- {0}", company));
            } else tv_postedfor.setVisibility(View.GONE);

            languages = getString(R.string.dot) + getString(R.string.space) + languages;
            languages = languages.replaceAll(",", getString(R.string.nextline) + getString(R.string.dot) + getString(R.string.space));
            tv_language.setText(Html.fromHtml(languages));
            Glide.with(mContext).load(Config.GetPICKURI())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_swipee_black).error(R.drawable.ic_swipee_black)).into(iv_logo);
            if (perkIds.size() > 0) {
                perkBenefit = getString(R.string.dot) + getString(R.string.space) + perkBenefit;
                perkBenefit = perkBenefit.replaceAll(",", getString(R.string.nextline) + getString(R.string.dot) + getString(R.string.space));
                tv_perk.setText(Html.fromHtml(perkBenefit));

            } else tv_perk.setText("No perks added.");
            if (numbersList.size() > 0)
                IntStream.range(0, numbersList.size()).forEach(i -> {
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
                    bt.setText(numbersList.get(i));
                    bt.setAllCaps(false);
                    bt.setMaxLines(1);
                    bt.setEllipsize(TextUtils.TruncateAt.END);
                    bt.setTextSize(12f);
                    bt.setTag(numbersList.get(i));
                    bt.setTextColor(mContext.getResources().getColor(R.color.white));
                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams1.setMargins(0, 0, 0, 0);
                    bt.setLayoutParams(layoutParams1);
                    linearLayout.addView(bt);
                    linearLayout.setTag(numbersList.get(i));
                    linearLayoutF.setTag(numbersList.get(i));
                    linearLayoutF.addView(linearLayout);
                    flow_skill.addView(linearLayoutF);
                });
            iv_logo.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(Config.GetPICKURI()))
                    AppController.callFullImage(mContext, Config.GetPICKURI());
            });
            tv_apply.setOnClickListener(v -> {

                if (Config.GetPACKAGEEXP().equalsIgnoreCase("Y")) {
                    rest.showToast(getString(R.string.packageexprectr));
                } else if (Config.GetLeftPostCount() <= 0) {
                    rest.showToast(getString(R.string.planexpire));
                } else if (rest.isInterentAvaliable()) {
                    AppController.ShowDialogue("", mContext);
                    saveJobPost(hashMap);
                } else rest.AlertForInternet();

            });
        }

        iv_back.setOnClickListener(v -> finish());


    }


    private void saveJobPost(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().savePostJob(hashMap).enqueue(new Callback<JsonObject>() {
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
                        Config.SetLeftPostCount(Config.GetLeftPostCount() - 1);
                        JsonObject jsonObject = responceBody.has("data") ? responceBody.get("data").getAsJsonObject() : new JsonObject();
                        JsonObject job_data = jsonObject.has("job_data") ? jsonObject.get("job_data").getAsJsonObject() : new JsonObject();
                        int job_post_id = job_data.has("job_post_id") ? job_data.get("job_post_id").getAsInt() : 0;
                        startActivity(new Intent(mContext, JobPostRule.class).putExtra("job_post_id", job_post_id));
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