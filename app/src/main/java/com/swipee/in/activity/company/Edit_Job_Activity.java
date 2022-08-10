package com.swipee.in.activity.company;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.swipee.in.CustomUi.FlowLayout;
import com.swipee.in.CustomUi.PopinsRegularTextView;
import com.swipee.in.R;
import com.swipee.in.UrlManager.AppController;
import com.swipee.in.UrlManager.Config;
import com.swipee.in.activity.JobDetail;
import com.swipee.in.activity.Seeker.AddCarrierObjectiveActivity;
import com.swipee.in.activity.Seeker.AddLanguageActivity;
import com.swipee.in.activity.Seeker.AddSkillActivity;
import com.swipee.in.model.company.CommonModel;
import com.swipee.in.rest.ParaName;
import com.swipee.in.rest.Rest;
import com.swipee.in.rest.SwipeeApiClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Edit_Job_Activity extends AppCompatActivity implements View.OnClickListener {

    private  ArrayList<CommonModel> perkArraylist=new ArrayList<>();
    Rest rest;
    Context mContext = Edit_Job_Activity.this;
    private final ArrayList<String> skillIds = new ArrayList<>();
    private final ArrayList<String> languagesIds = new ArrayList<>();
    private final ArrayList<String> perksIds = new ArrayList<>();
    private JsonArray salary_data = new JsonArray();
    private JsonArray perk_benefits = new JsonArray();
    private JsonArray job_opening_numberList = new JsonArray();
    TextView tv_job_desc, tv_job_opening, tv_job_minsalary, tv_maxsalary,tv_next;
    FlowLayout flow_skills, flow_perks, flow_languages;
    ImageView iv_add_desc, iv_show_desc, iv_add_opening, iv_show_opening, iv_add_skill, iv_show_skill, iv_add_minsalary, iv_show_minsalary,
            iv_add_maxsalary, iv_show_maxsalary, iv_add_perks, iv_show_perks, iv_add_languages, iv_show_languages;
    RelativeLayout rl_languages, rl_perks, rl_maxsalary, rl_minsalary, rl_skills, rl_opening, rl_desc;
    RelativeLayout perk_sheet, opening_bottom_sheet, min_salary_bottom_sheet, max_salary_bottom_sheet;
    BottomSheetBehavior bottomsheet_perk, bottomsheet_opening, bottomsheet_min_salary, bottomsheet_max_salary;
    ListView list_perk;
    RecyclerView rv_opening, rv_min_salary, rv_max_salary;
    private String job_opening_numbers="";
    private String job_min_salary="";
    private String job_max_salary="";
    private static String tempMinSalary="";
    private static String tempNoOpening="";
    private static String tempMaxSalary="";
    @SuppressLint("StaticFieldLeak")
    public static Edit_Job_Activity instance;
    public ArrayList<CommonModel> commonModelsLanguage = new ArrayList<>();
    private String job_description="";
    private String job_id="";
    private String from="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_job);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        rest = new Rest(mContext);
        instance=this;

        rl_desc = findViewById(R.id.rl_desc);
        rl_opening = findViewById(R.id.rl_opening);
        rl_skills = findViewById(R.id.rl_skills);
        rl_minsalary = findViewById(R.id.rl_minsalary);
        rl_maxsalary = findViewById(R.id.rl_maxsalary);
        rl_perks = findViewById(R.id.rl_perks);
        rl_languages = findViewById(R.id.rl_languages);
        tv_job_desc = findViewById(R.id.tv_job_desc);
        tv_job_opening = findViewById(R.id.tv_job_opening);
        tv_job_minsalary = findViewById(R.id.tv_job_minsalary);
        tv_maxsalary = findViewById(R.id.tv_maxsalary);
        flow_skills = findViewById(R.id.flow_skills);
        flow_perks = findViewById(R.id.flow_perks);
        flow_languages = findViewById(R.id.flow_languages);
        findViewById(R.id.iv_back).setOnClickListener(v -> finish());
        iv_add_desc = findViewById(R.id.iv_add_desc);
        iv_show_desc = findViewById(R.id.iv_show_desc);
        iv_add_opening = findViewById(R.id.iv_add_opening);
        iv_show_opening = findViewById(R.id.iv_show_opening);
        iv_add_skill = findViewById(R.id.iv_add_skill);
        iv_show_skill = findViewById(R.id.iv_show_skill);
        iv_add_minsalary = findViewById(R.id.iv_add_minsalary);
        iv_show_minsalary = findViewById(R.id.iv_show_minsalary);
        iv_add_maxsalary = findViewById(R.id.iv_add_maxsalary);
        iv_show_maxsalary = findViewById(R.id.iv_show_maxsalary);
        iv_add_perks = findViewById(R.id.iv_add_perks);
        iv_show_perks = findViewById(R.id.iv_show_perks);
        iv_add_languages = findViewById(R.id.iv_add_languages);
        iv_show_languages = findViewById(R.id.iv_show_languages);
        tv_next = findViewById(R.id.tv_next);

        if (getIntent() != null) {
             job_id = getIntent().getStringExtra("job_id");
            from = getIntent().getStringExtra("from");
            if (!TextUtils.isEmpty(job_id))
                if (rest.isInterentAvaliable()) {
                    AppController.ShowDialogue("", mContext);
                    callEditJobData(job_id);
                } else rest.AlertForInternet();
        }

        perkBottomSheet();
        openingBottomSheet();
        minSalaryBottomSheet();
        maxSalaryBottomSheet();

        iv_add_desc.setOnClickListener(this);
        iv_show_desc.setOnClickListener(this);
        iv_add_opening.setOnClickListener(this);
        iv_show_opening.setOnClickListener(this);
        iv_add_skill.setOnClickListener(this);
        iv_show_skill.setOnClickListener(this);
        iv_add_minsalary.setOnClickListener(this);
        iv_show_minsalary.setOnClickListener(this);
        iv_add_maxsalary.setOnClickListener(this);
        iv_show_maxsalary.setOnClickListener(this);
        iv_add_perks.setOnClickListener(this);
        iv_show_perks.setOnClickListener(this);
        iv_add_languages.setOnClickListener(this);
        iv_show_languages.setOnClickListener(this);
        tv_next.setOnClickListener(this);

    }

    private void resetArrow(){
        iv_show_desc.setRotation(90);
        iv_show_opening.setRotation(90);
        iv_show_skill.setRotation(90);
        iv_show_minsalary.setRotation(90);
        iv_show_maxsalary.setRotation(90);
        iv_show_perks.setRotation(90);
        iv_show_languages.setRotation(90);
    }

    private void callEditJobData(String job_id) {
        SwipeeApiClient.swipeeServiceInstance().getEditJobData(Config.GetUserToken(), job_id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        if (CompanyHomeActivity.instance != null)
                            CompanyHomeActivity.instance.finish();
                        AppController.loggedOut(mContext);
                        finish();

                    } else if (response.body().get("code").getAsInt() == 200 && response.body().get("status").getAsBoolean()) {
                        JsonObject jsonObject = response.body();
                        JsonObject dataObject = jsonObject.has("data") ? !jsonObject.get("data").isJsonNull() ? jsonObject.get("data").getAsJsonObject() : new JsonObject() : new JsonObject();
                        JsonObject job_data = dataObject.has("job_data") ? !dataObject.get("job_data").isJsonNull() ? dataObject.get("job_data").getAsJsonObject() : new JsonObject() : new JsonObject();
                        salary_data = dataObject.has("salary_data") ? !dataObject.get("salary_data").isJsonNull() ? dataObject.get("salary_data").getAsJsonArray() : new JsonArray() : new JsonArray();
                        perk_benefits = dataObject.has("perk_benefits") ? !dataObject.get("perk_benefits").isJsonNull() ? dataObject.get("perk_benefits").getAsJsonArray() : new JsonArray() : new JsonArray();
                        job_opening_numberList = dataObject.has("job_opening_numbers") ? !dataObject.get("job_opening_numbers").isJsonNull() ? dataObject.get("job_opening_numbers").getAsJsonArray() : new JsonArray() : new JsonArray();
                        JsonArray languages = dataObject.has("languages") ? !dataObject.get("languages").isJsonNull() ? dataObject.get("languages").getAsJsonArray() : new JsonArray() : new JsonArray();
                        if (job_data.size() > 0) {
                             job_opening_numbers = job_data.has("job_opening_numbers") ? job_data.get("job_opening_numbers").isJsonNull() ? "" : job_data.get("job_opening_numbers").getAsString() : "";
                             job_description = job_data.has("job_description") ? job_data.get("job_description").isJsonNull() ? "" : job_data.get("job_description").getAsString() : "";
                            String job_salary = job_data.has("job_salary") ? job_data.get("job_salary").isJsonNull() ? "" : job_data.get("job_salary").getAsString() : "";
                             job_max_salary = job_data.has("job_max_salary") ? job_data.get("job_max_salary").isJsonNull() ? "" : job_data.get("job_max_salary").getAsString() : "";
                             job_min_salary = job_data.has("job_min_salary") ? job_data.get("job_min_salary").isJsonNull() ? "" : job_data.get("job_min_salary").getAsString() : "";
                            String job_language_id = job_data.has("job_language_id") ? job_data.get("job_language_id").isJsonNull() ? "" : job_data.get("job_language_id").getAsString() : "";
                            JsonArray job_languages = job_data.has("job_languages") ? job_data.get("job_languages").isJsonNull() ? new JsonArray() : job_data.get("job_languages").getAsJsonArray() : new JsonArray();
                            JsonArray job_benefits = job_data.has("job_benefits") ? job_data.get("job_benefits").isJsonNull() ? new JsonArray() : job_data.get("job_benefits").getAsJsonArray() : new JsonArray();
                            JsonArray skill_data = job_data.has("skill_data") ? job_data.get("skill_data").isJsonNull() ? new JsonArray() : job_data.get("skill_data").getAsJsonArray() : new JsonArray();

                            tempMinSalary=job_min_salary;
                            tempMaxSalary=job_max_salary;
                            tv_job_desc.setText(job_description);

                            tv_job_minsalary.setText(String.format("%s%s", job_min_salary, getResources().getString(R.string.salarypermonth)));
                            tv_maxsalary.setText(String.format("%s%s", job_max_salary, getResources().getString(R.string.salarypermonth)));
                            tv_job_opening.setText(job_opening_numbers);

                            skillIds.clear();
                            ArrayList<String>  skillNames=new ArrayList<>();
                            IntStream.range(0, skill_data.size()).mapToObj(i -> skill_data.get(i).getAsJsonObject()).forEach(jsonObject1 -> {
                                skillIds.add(skillIds.size(), jsonObject1.get("skill_id").getAsString());
                                skillNames.add(skillNames.size(), jsonObject1.get("skill_name").getAsString());
                            });
                            setSkillFlow(skillNames);

                            languagesIds.clear();
                            ArrayList<String>  langNames=new ArrayList<>();
                            IntStream.range(0, job_languages.size()).mapToObj(i -> job_languages.get(i).getAsJsonObject()).forEach(jsonObject1 -> {
                                languagesIds.add(languagesIds.size(), jsonObject1.get("language_id").getAsString());
                                langNames.add(langNames.size(), jsonObject1.get("language_name").getAsString());
                            });
                                setLanguagesFlow(langNames);

                            if (job_benefits.size()>0){
                                ArrayList<CommonModel> commonModels=new ArrayList<>();
                                IntStream.range(0, job_benefits.size()).mapToObj(i -> job_benefits.get(i).getAsJsonObject()).forEach(object -> {
                                    String name = object.get("benefit_name").getAsString();
                                    String benefit_id = object.get("benefit_id").getAsString();
                                    commonModels.add(commonModels.size(), new CommonModel(benefit_id, name, true));
                                });
                                setPerkFlowList(commonModels);
                            }

                        }
                        commonModelsLanguage.clear();
                        IntStream.range(0, languages.size()).mapToObj(i -> languages.get(i).getAsJsonObject()).forEach(langObj -> {
                            String language_id = langObj.get("language_id").getAsString();
                            String language_name = langObj.get("language_name").getAsString();
                            commonModelsLanguage.add(commonModelsLanguage.size(), new CommonModel(language_id, language_name, false));
                        });
                    } else rest.showToast(response.body().get("message").getAsString());
                } else {
                    rest.showToast("Something went wrong");
                    finish();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
                finish();
            }
        });

    }

    private void setSkillFlow(ArrayList<String> skill_data) {
        flow_skills.removeAllViews();
        IntStream.range(0, skill_data.size()).forEach(i -> {
            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout linearLayoutF = new LinearLayout(mContext);
            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                    FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams layoutParamsF = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, (int) (getResources().getDisplayMetrics().density * 32));
            layoutParamsF.setMargins((int) (getResources().getDisplayMetrics().density * 5), 0, (int) (mContext.getResources().getDisplayMetrics().density * 5), (int) (mContext.getResources().getDisplayMetrics().density * 8));
            linearLayoutF.setLayoutParams(layoutParams);
            linearLayout.setLayoutParams(layoutParamsF);
            linearLayout.setPadding((int) (getResources().getDisplayMetrics().density * 8), 0, (int) (mContext.getResources().getDisplayMetrics().density * 8), 0);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setBackgroundResource(R.drawable.primary_semiround_bg);
            PopinsRegularTextView bt = new PopinsRegularTextView(mContext);
            bt.setText(skill_data.get(i));
            bt.setAllCaps(false);
            bt.setTextSize(12f);
            bt.setTag(skill_data.get(i));
            bt.setMaxLines(1);
            bt.setEllipsize(TextUtils.TruncateAt.END);
            bt.setTextColor(getResources().getColor(R.color.white));
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(0, 0, 0, 0);
            bt.setLayoutParams(layoutParams1);
            linearLayout.addView(bt);
            linearLayoutF.addView(linearLayout);
            linearLayoutF.setTag(skill_data.get(i));
            flow_skills.addView(linearLayoutF);
        });
    }

    private void setLanguagesFlow(ArrayList<String> job_languages) {
        flow_languages.removeAllViews();
        IntStream.range(0, job_languages.size()).forEach(i -> {
            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout linearLayoutF = new LinearLayout(mContext);
            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                    FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams layoutParamsF = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, (int) (getResources().getDisplayMetrics().density * 32));
            layoutParamsF.setMargins((int) (getResources().getDisplayMetrics().density * 5), 0, (int) (mContext.getResources().getDisplayMetrics().density * 5), (int) (mContext.getResources().getDisplayMetrics().density * 8));
            linearLayoutF.setLayoutParams(layoutParams);
            linearLayout.setLayoutParams(layoutParamsF);
            linearLayout.setPadding((int) (getResources().getDisplayMetrics().density * 8), 0, (int) (mContext.getResources().getDisplayMetrics().density * 8), 0);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setBackgroundResource(R.drawable.primary_semiround_bg);
            PopinsRegularTextView bt = new PopinsRegularTextView(mContext);
            bt.setText(job_languages.get(i));
            bt.setAllCaps(false);
            bt.setTextSize(12f);
            bt.setTag(job_languages.get(i));
            bt.setMaxLines(1);
            bt.setEllipsize(TextUtils.TruncateAt.END);
            bt.setTextColor(getResources().getColor(R.color.white));
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(0, 0, 0, 0);
            bt.setLayoutParams(layoutParams1);
            linearLayout.addView(bt);
            linearLayoutF.addView(linearLayout);
            linearLayoutF.setTag(job_languages.get(i));
            flow_languages.addView(linearLayoutF);
        });
    }

    private void setPerkFlowList(ArrayList<CommonModel> job_benefits) {
        perksIds.clear();
        flow_perks.removeAllViews();
        job_benefits.forEach(obj -> {
            perksIds.add(obj.getId());
            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout linearLayoutF = new LinearLayout(mContext);
            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                    FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams layoutParamsF = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, (int) (getResources().getDisplayMetrics().density * 32));
            layoutParamsF.setMargins((int) (getResources().getDisplayMetrics().density * 5), 0, (int) (mContext.getResources().getDisplayMetrics().density * 5), (int) (mContext.getResources().getDisplayMetrics().density * 8));
            linearLayoutF.setLayoutParams(layoutParams);
            linearLayout.setLayoutParams(layoutParamsF);
            linearLayout.setPadding((int) (getResources().getDisplayMetrics().density * 8), 0, (int) (mContext.getResources().getDisplayMetrics().density * 8), 0);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setBackgroundResource(R.drawable.primary_semiround_bg);
            PopinsRegularTextView bt = new PopinsRegularTextView(mContext);
            bt.setText(obj.getName());
            bt.setAllCaps(false);
            bt.setTextSize(12f);
            bt.setTag(obj.getName());
            bt.setMaxLines(1);
            bt.setEllipsize(TextUtils.TruncateAt.END);
            bt.setTextColor(getResources().getColor(R.color.white));
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(0, 0, 0, 0);
            bt.setLayoutParams(layoutParams1);
            linearLayout.addView(bt);
            linearLayoutF.addView(linearLayout);
            linearLayoutF.setTag(obj.getName());
            flow_perks.addView(linearLayoutF);
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_show_desc:
                rl_desc.setVisibility(rl_desc.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                rl_languages.setVisibility(View.GONE);
                rl_skills.setVisibility(View.GONE);
                rl_opening.setVisibility(View.GONE);
                rl_maxsalary.setVisibility(View.GONE);
                rl_minsalary.setVisibility(View.GONE);
                rl_perks.setVisibility(View.GONE);
                resetArrow();
                iv_show_desc.setRotation(rl_desc.getVisibility() == View.VISIBLE ?270:90);
                break;

            case R.id.iv_show_opening:
                rl_opening.setVisibility(rl_opening.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                rl_languages.setVisibility(View.GONE);
                rl_skills.setVisibility(View.GONE);
                rl_desc.setVisibility(View.GONE);
                rl_maxsalary.setVisibility(View.GONE);
                rl_minsalary.setVisibility(View.GONE);
                rl_perks.setVisibility(View.GONE);
                resetArrow();
                iv_show_opening.setRotation(rl_opening.getVisibility() == View.VISIBLE ?270:90);
                break;

            case R.id.iv_show_skill:
                rl_skills.setVisibility(rl_skills.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                rl_languages.setVisibility(View.GONE);
                rl_opening.setVisibility(View.GONE);
                rl_desc.setVisibility(View.GONE);
                rl_maxsalary.setVisibility(View.GONE);
                rl_minsalary.setVisibility(View.GONE);
                rl_perks.setVisibility(View.GONE);
                resetArrow();
                iv_show_skill.setRotation(rl_skills.getVisibility() == View.VISIBLE ?270:90);
                break;

            case R.id.iv_show_minsalary:
                rl_minsalary.setVisibility(rl_minsalary.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                rl_languages.setVisibility(View.GONE);
                rl_opening.setVisibility(View.GONE);
                rl_desc.setVisibility(View.GONE);
                rl_maxsalary.setVisibility(View.GONE);
                rl_skills.setVisibility(View.GONE);
                rl_perks.setVisibility(View.GONE);
                resetArrow();
                iv_show_minsalary.setRotation(rl_minsalary.getVisibility() == View.VISIBLE ?270:90);
                break;

            case R.id.iv_show_maxsalary:
                rl_maxsalary.setVisibility(rl_maxsalary.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                rl_languages.setVisibility(View.GONE);
                rl_opening.setVisibility(View.GONE);
                rl_desc.setVisibility(View.GONE);
                rl_minsalary.setVisibility(View.GONE);
                rl_skills.setVisibility(View.GONE);
                rl_perks.setVisibility(View.GONE);
                resetArrow();
                iv_show_maxsalary.setRotation(rl_maxsalary.getVisibility() == View.VISIBLE ?270:90);
                break;

            case R.id.iv_show_perks:
                rl_perks.setVisibility(rl_perks.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                rl_languages.setVisibility(View.GONE);
                rl_opening.setVisibility(View.GONE);
                rl_desc.setVisibility(View.GONE);
                rl_minsalary.setVisibility(View.GONE);
                rl_skills.setVisibility(View.GONE);
                rl_maxsalary.setVisibility(View.GONE);
                resetArrow();
                iv_show_perks.setRotation(rl_perks.getVisibility() == View.VISIBLE ?270:90);
                break;

            case R.id.iv_show_languages:
                rl_languages.setVisibility(rl_languages.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                rl_perks.setVisibility(View.GONE);
                rl_opening.setVisibility(View.GONE);
                rl_desc.setVisibility(View.GONE);
                rl_minsalary.setVisibility(View.GONE);
                rl_skills.setVisibility(View.GONE);
                rl_maxsalary.setVisibility(View.GONE);
                resetArrow();
                iv_show_languages.setRotation(rl_languages.getVisibility() == View.VISIBLE ?270:90);
                break;

            case R.id.iv_add_desc:
                mContext.startActivity(new Intent(mContext, AddCarrierObjectiveActivity.class).putExtra("from","editJob").putExtra("data", job_description));

                break;
            case R.id.iv_add_opening:
                if (job_opening_numberList.size() > 0) callOpeningSheet();
                break;

            case R.id.iv_add_skill:
                mContext.startActivity(new Intent(mContext, AddSkillActivity.class).putExtra("from","editJob").putStringArrayListExtra("StringArrayList", skillIds));
                break;
            case R.id.iv_add_minsalary:
                if (salary_data.size() > 0) callMinSalarySheet();

                break;

            case R.id.iv_add_maxsalary:
                if (!TextUtils.isEmpty(job_min_salary)){
                    if (salary_data.size() > 0) callMaxSalarySheet();
                }else rest.showToast("Select min salary first.");

                break;

            case R.id.iv_add_perks:
                if (perk_benefits.size() > 0) callPerkSheet();
                break;

            case R.id.iv_add_languages:
                startActivity(new Intent(mContext, AddLanguageActivity.class).putStringArrayListExtra("StringArrayList", languagesIds).putExtra("from", "editJob"));
                break;

            case R.id.btn_done_perk:
                ArrayList<CommonModel> commonModels= IntStream.range(0, perkArraylist.size()).filter(i -> perkArraylist.get(i).isSelected()).mapToObj(i -> perkArraylist.get(i)).collect(Collectors.toCollection(ArrayList::new));
                setPerkFlowList(commonModels);
                bottomsheet_perk.setState(BottomSheetBehavior.STATE_COLLAPSED);

                break;
            case R.id.iv_close_perk:
                bottomsheet_perk.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.close_opening:
                bottomsheet_opening.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.done_opening:
                tv_job_opening.setText(String.format("%s", tempNoOpening));
                job_opening_numbers= tempNoOpening;
                bottomsheet_opening.setState(BottomSheetBehavior.STATE_COLLAPSED);

                break;
            case R.id.close_min_salary:
                bottomsheet_min_salary.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.done_min_salary:
              if (!TextUtils.isEmpty(tempMinSalary)){
                  tv_job_minsalary.setText(String.format("%s%s", tempMinSalary, getResources().getString(R.string.salarypermonth)));
                  job_min_salary = tempMinSalary;
                  bottomsheet_min_salary.setState(BottomSheetBehavior.STATE_COLLAPSED);
                  tv_maxsalary.setText("");
                  job_max_salary="";
                  tempMaxSalary="";
              }

                break;
            case R.id.close_max_salary:
                bottomsheet_max_salary.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.done_max_salary:
                if (!TextUtils.isEmpty(tempMaxSalary)){
                    tv_maxsalary.setText(String.format("%s%s", tempMaxSalary, getResources().getString(R.string.salarypermonth)));
                    job_max_salary = tempMaxSalary;
                    bottomsheet_max_salary.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                break;

                case R.id.tv_next:

                    if (!TextUtils.isEmpty(job_id)){
                        HashMap<String,String> hashMap=new HashMap<>();
                        hashMap.put(ParaName.KEYTOKEN,Config.GetUserToken());
                        hashMap.put(ParaName.KEY_JOBPOSTID,job_id);
                        hashMap.put(ParaName.KEY_JOBDESCRIPTION,job_description);
                        hashMap.put(ParaName.KEY_JOBOPENING,job_opening_numbers);
                        hashMap.put(ParaName.KEY_JOBSKILLID,skillIds.toString());
                        hashMap.put(ParaName.KEY_JOBMINSALARY,job_min_salary);
                        hashMap.put(ParaName.KEY_JOBMAXSALARY,job_max_salary);
                        hashMap.put(ParaName.KEY_JOBBENEFITID,perksIds.toString());
                        hashMap.put(ParaName.KEY_JOBLANGUAGE,languagesIds.toString());
                        if (rest.isInterentAvaliable()){
                            AppController.ShowDialogue("",mContext);
                            saveEditJobData(hashMap);
                        }else rest.AlertForInternet();
                    }
                break;
            default:
                break;
        }
    }

    private void perkBottomSheet() {
        perk_sheet = findViewById(R.id.perk_sheet);
        bottomsheet_perk = BottomSheetBehavior.from(perk_sheet);
        ImageView iv_close_perk = perk_sheet.findViewById(R.id.iv_close_perk);
        Button btn_done_perk = perk_sheet.findViewById(R.id.btn_done_perk);
        list_perk = perk_sheet.findViewById(R.id.list_perk);
        btn_done_perk.setOnClickListener(this);
        perk_sheet.setOnClickListener(this);
        iv_close_perk.setOnClickListener(this);
    }

    private void openingBottomSheet() {
        opening_bottom_sheet = findViewById(R.id.opening_bottom_sheet);
        bottomsheet_opening = BottomSheetBehavior.from(opening_bottom_sheet);
        ImageView close_opening = opening_bottom_sheet.findViewById(R.id.close_opening);
        Button done_opening = opening_bottom_sheet.findViewById(R.id.done_opening);
        rv_opening = opening_bottom_sheet.findViewById(R.id.rv_opening);
        done_opening.setOnClickListener(this);
        opening_bottom_sheet.setOnClickListener(this);
        close_opening.setOnClickListener(this);
    }

    private void minSalaryBottomSheet() {
        min_salary_bottom_sheet = findViewById(R.id.min_salary_bottom_sheet);
        bottomsheet_min_salary = BottomSheetBehavior.from(min_salary_bottom_sheet);
        ImageView close_min_salary = min_salary_bottom_sheet.findViewById(R.id.close_min_salary);
        Button done_min_salary = min_salary_bottom_sheet.findViewById(R.id.done_min_salary);
        rv_min_salary = min_salary_bottom_sheet.findViewById(R.id.rv_min_salary);
        done_min_salary.setOnClickListener(this);
        min_salary_bottom_sheet.setOnClickListener(this);
        close_min_salary.setOnClickListener(this);
    }

    private void maxSalaryBottomSheet() {
        max_salary_bottom_sheet = findViewById(R.id.max_salary_bottom_sheet);
        bottomsheet_max_salary = BottomSheetBehavior.from(max_salary_bottom_sheet);
        ImageView close_max_salary = max_salary_bottom_sheet.findViewById(R.id.close_max_salary);
        Button done_max_salary = max_salary_bottom_sheet.findViewById(R.id.done_max_salary);
        rv_max_salary = max_salary_bottom_sheet.findViewById(R.id.rv_max_salary);
        done_max_salary.setOnClickListener(this);
        max_salary_bottom_sheet.setOnClickListener(this);
        close_max_salary.setOnClickListener(this);
    }

    private void callPerkSheet() {

        bottomsheet_perk.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomsheet_perk.setDraggable(false);
        bottomsheet_perk.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                    bottomsheet_perk.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });
        if (perk_benefits.size() > 0) {
            ArrayList<CommonModel> commonModels = new ArrayList<>();
            IntStream.range(0, perk_benefits.size()).mapToObj(i -> perk_benefits.get(i).getAsJsonObject()).map(jsonObject1 -> new CommonModel(jsonObject1.get("benefit_id").getAsString(),
                    jsonObject1.get("benefit_name").getAsString(), perksIds.contains(jsonObject1.get("benefit_id").getAsString()))).forEach(commonModel -> commonModels.add(commonModels.size(), commonModel));
            PerkAdapter perkAdapter = new PerkAdapter(mContext, commonModels);
            list_perk.setAdapter(perkAdapter);
            perkAdapter.notifyDataSetChanged();
        }
    }

    private void callOpeningSheet() {

        bottomsheet_opening.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomsheet_opening.setDraggable(false);
        bottomsheet_opening.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                    bottomsheet_opening.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });
        if (job_opening_numberList.size() > 0) {
            ArrayList<CommonModel> commonModels = IntStream.range(0, job_opening_numberList.size()).mapToObj(i -> new CommonModel(String.valueOf(i + 1), job_opening_numberList.get(i).getAsString(),
                    job_opening_numberList.get(i).getAsString().equalsIgnoreCase(job_opening_numbers))).collect(Collectors.toCollection(ArrayList::new));
            rv_opening.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            OpeningAdapter openingAdapter = new OpeningAdapter(mContext, commonModels);
            rv_opening.setAdapter(openingAdapter);
            rv_opening.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        }

    }

    private void callMinSalarySheet() {

        bottomsheet_min_salary.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomsheet_min_salary.setDraggable(false);
        bottomsheet_min_salary.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                    bottomsheet_min_salary.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

        if (salary_data.size() > 0) {
            ArrayList<CommonModel> commonModels=new ArrayList<>();
            IntStream.range(0, salary_data.size()).mapToObj(i -> salary_data.get(i).getAsJsonObject()).forEach(salaryObj -> {
                commonModels.add(commonModels.size(), new CommonModel(salaryObj.get("range_id").getAsString(), salaryObj.get("min_salary").getAsString(), salaryObj.get("min_salary").getAsString().equalsIgnoreCase(job_min_salary)));
                if (200000 > Integer.parseInt(salaryObj.get("max_salary").getAsString()))
                    commonModels.add(commonModels.size(), new CommonModel(salaryObj.get("range_id").getAsString(), salaryObj.get("max_salary").getAsString(), salaryObj.get("max_salary").getAsString().equalsIgnoreCase(job_min_salary)));
            });
            rv_min_salary.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            MinSalaryAdapter minSalaryAdapter = new MinSalaryAdapter(mContext, commonModels);
            rv_min_salary.setAdapter(minSalaryAdapter);
            rv_min_salary.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        }
    }

    private void callMaxSalarySheet() {

        bottomsheet_max_salary.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomsheet_max_salary.setDraggable(false);
        bottomsheet_max_salary.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                    bottomsheet_max_salary.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });
        if (salary_data.size() > 0) {
            ArrayList<CommonModel> commonModels=new ArrayList<>();
            IntStream.range(0, salary_data.size()).mapToObj(i -> salary_data.get(i).getAsJsonObject()).forEach(salaryObj -> {
                if (Integer.parseInt(job_min_salary) < Integer.parseInt(salaryObj.get("min_salary").getAsString())) {
                    commonModels.add(commonModels.size(), new CommonModel(salaryObj.get("range_id").getAsString(), salaryObj.get("min_salary").getAsString(), salaryObj.get("min_salary").getAsString().equalsIgnoreCase(job_max_salary)));
                    commonModels.add(commonModels.size(), new CommonModel(salaryObj.get("range_id").getAsString(), salaryObj.get("max_salary").getAsString(), salaryObj.get("max_salary").getAsString().equalsIgnoreCase(job_max_salary)));
                } else if (Integer.parseInt(job_min_salary) < Integer.parseInt(salaryObj.get("max_salary").getAsString())) {
                    commonModels.add(commonModels.size(), new CommonModel(salaryObj.get("range_id").getAsString(), salaryObj.get("max_salary").getAsString(), salaryObj.get("max_salary").getAsString().equalsIgnoreCase(job_max_salary)));
                }
            });
            rv_max_salary.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            MaxSalaryAdapter maxSalaryAdapter = new MaxSalaryAdapter(mContext, commonModels);
            rv_max_salary.setAdapter(maxSalaryAdapter);
            rv_max_salary.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        }
    }

    public void setLanguage(ArrayList<String> mArrayListid, ArrayList<String> mArrayListName) {
        languagesIds.clear();
        languagesIds.addAll(mArrayListid);
        setLanguagesFlow(mArrayListName);
    }

    public void setSkills(ArrayList<String> mArrayListid, ArrayList<String> mArrayListName) {
        skillIds.clear();
        skillIds.addAll(mArrayListid);
        setSkillFlow(mArrayListName);
    }

    public void setDescription(String desc) {
        job_description=desc;
        tv_job_desc.setText(desc);
    }

    public class PerkAdapter extends BaseAdapter {

        Activity mContext;

        public PerkAdapter(Context mContext, ArrayList<CommonModel> languageArraylist1) {
            this.mContext = (Activity) mContext;
            perkArraylist = languageArraylist1;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return perkArraylist.size();
        }

        @Override
        public Object getItem(int pos) {
            // TODO Auto-generated method stub
            return perkArraylist.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            // TODO Auto-generated method stub
            return perkArraylist.indexOf(getItem(pos));
        }

        @SuppressLint("ViewHolder")
        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            LayoutInflater inflater = mContext.getLayoutInflater();
            convertView = inflater.inflate(R.layout.addbenefitsrowitems, null);
            TextView nameTxt = convertView.findViewById(R.id.tv_countryname);
            CheckBox mCheckImgView = convertView.findViewById(R.id.chkbox);
            //SET DATA TO THEM
            nameTxt.setText(perkArraylist.get(pos).getName());
            mCheckImgView.setChecked(perkArraylist.get(pos).isSelected());

            mCheckImgView.setOnClickListener(view -> {

                perkArraylist.get(pos).setSelected(!perkArraylist.get(pos).isSelected());
                notifyDataSetChanged();
            });
            nameTxt.setOnClickListener(view -> {

                perkArraylist.get(pos).setSelected(!perkArraylist.get(pos).isSelected());
                notifyDataSetChanged();
            });
            return convertView;
        }
    }

    public static class OpeningAdapter extends RecyclerView.Adapter<OpeningAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<CommonModel> data;
        int oldPos;
        private OpeningAdapter.MyViewHolder oldHolder;

        public OpeningAdapter(Context mContext, ArrayList<CommonModel> data) {
            // TODO Auto-generated constructor stub
            this.mContext = mContext;
            this.data = data;
        }

        @NonNull
        @Override
        public OpeningAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.child_state, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull OpeningAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

            String name = data.get(position).getName();
            holder.radioButton.setClickable(false);
            holder.radioButton.setChecked(data.get(position).isSelected());
            if (data.get(position).isSelected()){
                oldHolder = holder;
                oldPos = position;
                tempNoOpening = name;
            }
            holder.tv_item.setText(name);
            holder.itemView.setOnClickListener(v -> {
                if (oldHolder != null) {
                    oldHolder.radioButton.setChecked(false);
                    data.get(oldPos).setSelected(false);
                }
                holder.radioButton.setChecked(true);
                oldHolder = holder;
                oldPos = position;
                tempNoOpening = name;
                data.get(position).setSelected(true);
            });
        }


        @Override
        public long getItemId(int pos) {

            return 0;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            PopinsRegularTextView tv_item;
            RadioButton radioButton;

            public MyViewHolder(View view) {
                super(view);

                tv_item = view.findViewById(R.id.tv_item);
                radioButton = view.findViewById(R.id.radioButton);

            }
        }
    }

    public static class MinSalaryAdapter extends RecyclerView.Adapter<MinSalaryAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<CommonModel> data;
        int oldPos;
        private MinSalaryAdapter.MyViewHolder oldHolder;


        public MinSalaryAdapter(Context mContext, ArrayList<CommonModel> data) {

            // TODO Auto-generated constructor stub

            this.mContext = mContext;
            this.data = data;
        }

        @NonNull
        @Override
        public MinSalaryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.child_state, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MinSalaryAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

            String name = data.get(position).getName();
            holder.radioButton.setClickable(false);
            holder.radioButton.setChecked(data.get(position).isSelected());
            if (data.get(position).isSelected()){
                oldHolder = holder;
                oldPos = position;
                tempMinSalary = name;
            }
            holder.tv_item.setText(name);
            holder.itemView.setOnClickListener(v -> {
                if (oldHolder != null) {
                    oldHolder.radioButton.setChecked(false);
                    data.get(oldPos).setSelected(false);
                }
                holder.radioButton.setChecked(true);
                oldHolder = holder;
                oldPos = position;
                tempMinSalary = name;
                data.get(position).setSelected(true);
            });
        }


        @Override
        public long getItemId(int pos) {

            return 0;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            PopinsRegularTextView tv_item;
            RadioButton radioButton;

            public MyViewHolder(View view) {
                super(view);

                tv_item = view.findViewById(R.id.tv_item);
                radioButton = view.findViewById(R.id.radioButton);

            }
        }
    }

    public static class MaxSalaryAdapter extends RecyclerView.Adapter<MaxSalaryAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<CommonModel> data;
        int oldPos;
        private MaxSalaryAdapter.MyViewHolder oldHolder;


        public MaxSalaryAdapter(Context mContext, ArrayList<CommonModel> data) {

            // TODO Auto-generated constructor stub

            this.mContext = mContext;
            this.data = data;


        }


        @NonNull
        @Override
        public MaxSalaryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.child_state, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MaxSalaryAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

            String name = data.get(position).getName();
            holder.radioButton.setClickable(false);
            holder.radioButton.setChecked(data.get(position).isSelected());
            if (data.get(position).isSelected()){
                oldHolder = holder;
                oldPos = position;
                tempMaxSalary = name;
            }
            holder.tv_item.setText(name);
            holder.itemView.setOnClickListener(v -> {
                if (oldHolder != null) {
                    oldHolder.radioButton.setChecked(false);
                    data.get(oldPos).setSelected(false);
                }
                holder.radioButton.setChecked(true);
                oldHolder = holder;
                oldPos = position;
                tempMaxSalary = name;
                data.get(position).setSelected(true);
            });
        }


        @Override
        public long getItemId(int pos) {

            return 0;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public static class MyViewHolder extends RecyclerView.ViewHolder {

            PopinsRegularTextView tv_item;
            RadioButton radioButton;

            public MyViewHolder(View view) {
                super(view);

                tv_item = view.findViewById(R.id.tv_item);
                radioButton = view.findViewById(R.id.radioButton);

            }
        }
    }

    private void saveEditJobData(HashMap<String,String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().saveEditJob(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());

                        if (from.equalsIgnoreCase(JobDetail.class.getSimpleName())){
                            if (JobDetail.instance!=null) JobDetail.instance.setBackPressed();
                        }else if (from.equalsIgnoreCase(PostedJobActivity.class.getSimpleName())){
                            if (PostedJobActivity.instance!=null) PostedJobActivity.instance.finish();
                        }
                        if (CompanyHomeActivity.instance != null)
                            CompanyHomeActivity.instance.finish();
                        AppController.loggedOut(mContext);
                        finish();

                    } else if (response.body().get("code").getAsInt() == 200 && response.body().get("status").getAsBoolean()) {
                        rest.showToast(response.body().get("message").getAsString());
                        if (from.equalsIgnoreCase(JobDetail.class.getSimpleName())){
                            if (JobDetail.instance!=null) JobDetail.instance.setBackPressed();
                        }else    if (from.equalsIgnoreCase(PostedJobActivity.class.getSimpleName())){
                            if (PostedJobActivity.instance!=null) PostedJobActivity.instance.finish();
                        }
                        finish();
                    } else rest.showToast(response.body().get("message").getAsString());
                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });

    }
}