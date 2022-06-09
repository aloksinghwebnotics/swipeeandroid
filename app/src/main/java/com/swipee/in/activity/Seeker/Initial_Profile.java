package com.swipee.in.activity.Seeker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.swipee.in.CustomUi.FlowLayout;
import com.swipee.in.CustomUi.NestedListView;
import com.swipee.in.CustomUi.PopinsRegularTextView;
import com.swipee.in.R;
import com.swipee.in.UrlManager.AppController;
import com.swipee.in.UrlManager.Config;
import com.swipee.in.adapter.seeeker.ExperienceAdapter;
import com.swipee.in.adapter.seeeker.LangAdapter;
import com.swipee.in.adapter.seeeker.UserEducationAdapter;
import com.swipee.in.adapter.seeeker.UserPreferenceAdapter;
import com.swipee.in.model.seeker.EmployeeUserDetails;
import com.swipee.in.model.seeker.JobTypeModel;
import com.swipee.in.model.seeker.LangModel;
import com.swipee.in.rest.Rest;
import com.swipee.in.rest.SwipeeApiClient;

import java.util.ArrayList;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Initial_Profile extends AppCompatActivity implements View.OnClickListener {
    ImageView mImageViewemptypeclose, iv_add_jobtype, iv_show_jobtype, iv_add_experience, iv_show_experience,
            iv_add_education, iv_show_education, iv_add_preferences, iv_show_preferences, iv_add_languages, iv_show_languages;
    RelativeLayout employeetypebottom_sheet, rl_jobtype, rl_experience, rl_education, rl_preferences, rl_languages;
    private final Context mContext = Initial_Profile.this;
    private Rest rest;
    TextView tv_next;
    FlowLayout flow_jobtype, flow_languages;
    private ArrayList<EmployeeUserDetails.Data.UserWorkExperience> mArrayuserworkexperience = new ArrayList<>();
    private ArrayList<EmployeeUserDetails.Data.User_Eductaion> mArrayuserusereducation = new ArrayList<>();
    private ArrayList<EmployeeUserDetails.Data.User_Preferences> mArrayuseruserpreference = new ArrayList<>();
    private NestedListView list_experience, list_education, list_preferences;
    private ArrayList<EmployeeUserDetails.Data.User_Languages> mArrayUserListLang = new ArrayList<>();
    private ArrayList<EmployeeUserDetails.Data.User_Job_Type> mArrayUserJobType = new ArrayList<>();
    ArrayList<JobTypeModel.Data> mArrayListJobType = new ArrayList<>();
    ArrayList<LangModel> mArrayListjbtype = new ArrayList<>();
    public static Initial_Profile instance;
    private final ArrayList<String> stringLangIds = new ArrayList<>();
    BottomSheetBehavior bottomsheetemployeetype;
    private ListView listviewemptype;
    private String prefId = "";
    RelativeLayout ll_main;
    ArrayList<String> locId = new ArrayList<>();
    ArrayList<String> industryId = new ArrayList<>();
    ArrayList<String> locName = new ArrayList<>();
    ArrayList<String> industryName = new ArrayList<>();
    private String expectedSalary = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_profile);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));

        rest = new Rest(this);
        instance = this;

        iv_add_jobtype = findViewById(R.id.iv_add_jobtype);
        iv_show_jobtype = findViewById(R.id.iv_show_jobtype);
        iv_add_experience = findViewById(R.id.iv_add_experience);
        iv_show_experience = findViewById(R.id.iv_show_experience);
        iv_add_education = findViewById(R.id.iv_add_education);
        iv_show_education = findViewById(R.id.iv_show_education);
        iv_add_preferences = findViewById(R.id.iv_add_preferences);
        iv_show_preferences = findViewById(R.id.iv_show_preferences);
        iv_add_languages = findViewById(R.id.iv_add_languages);
        iv_show_languages = findViewById(R.id.iv_show_languages);
        ll_main = findViewById(R.id.ll_main);
        tv_next = findViewById(R.id.tv_next);

        rl_jobtype = findViewById(R.id.rl_jobtype);
        rl_experience = findViewById(R.id.rl_experience);
        rl_education = findViewById(R.id.rl_education);
        rl_preferences = findViewById(R.id.rl_preferences);
        rl_languages = findViewById(R.id.rl_languages);

        list_experience = findViewById(R.id.list_experience);
        list_education = findViewById(R.id.list_education);
        list_preferences = findViewById(R.id.list_preferences);
        flow_jobtype = findViewById(R.id.flow_jobtype);
        flow_languages = findViewById(R.id.flow_languages);

        employeetypebottom_sheet = findViewById(R.id.employeetypebottom_sheet);
        bottomsheetemployeetype = BottomSheetBehavior.from(employeetypebottom_sheet);
        mImageViewemptypeclose = employeetypebottom_sheet.findViewById(R.id.closetype);
        Button doneeeeetype = employeetypebottom_sheet.findViewById(R.id.donetype);
        listviewemptype = employeetypebottom_sheet.findViewById(R.id.listviewemptype);

        setVisibilityGone();
        iv_show_jobtype.setOnClickListener(this);
        iv_show_experience.setOnClickListener(this);
        iv_show_education.setOnClickListener(this);
        iv_show_preferences.setOnClickListener(this);
        iv_show_languages.setOnClickListener(this);
        doneeeeetype.setOnClickListener(this);
        mImageViewemptypeclose.setOnClickListener(this);

        iv_add_jobtype.setOnClickListener(this);
        iv_add_experience.setOnClickListener(this);
        iv_add_education.setOnClickListener(this);
        iv_add_preferences.setOnClickListener(this);
        iv_add_languages.setOnClickListener(this);
        tv_next.setOnClickListener(this);

        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            getProfileData();
        } else rest.AlertForInternet();
////////////

        /* */

        //////////
    }

    private void setVisibilityGone() {
        rl_jobtype.setVisibility(View.GONE);
        rl_experience.setVisibility(View.GONE);
        rl_education.setVisibility(View.GONE);
        rl_preferences.setVisibility(View.GONE);
        rl_languages.setVisibility(View.GONE);

        iv_show_jobtype.setRotation(90);
        iv_show_experience.setRotation(90);
        iv_show_education.setRotation(90);
        iv_show_preferences.setRotation(90);
        iv_show_languages.setRotation(90);
    }

    private void callServiceJobType() {
        SwipeeApiClient.swipeeServiceInstance().getJobType(Config.GetUserToken()).enqueue(new Callback<JobTypeModel>() {
            @Override
            public void onResponse(@NonNull Call<JobTypeModel> call, @NonNull Response<JobTypeModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200) {
                    JobTypeModel langModel = response.body();
                    if (langModel != null)
                        if (langModel.isStatus() && langModel.getCode() == 200) {
                            mArrayListJobType.clear();
                            mArrayListjbtype.clear();
                            mArrayListJobType = langModel.getData();
                            callJobTypeSheet();
                        } else if (langModel.getCode() == 203) {
                            rest.showToast(langModel.getMessage());
                            AppController.loggedOut(mContext);
                        }
                } else rest.showToast("Something went wrong");
            }

            @Override
            public void onFailure(@NonNull Call<JobTypeModel> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {

            case R.id.iv_show_jobtype:

                if (rl_jobtype.getVisibility() == View.VISIBLE) {
                    setVisibilityGone();
                } else {
                    setVisibilityGone();
                    iv_show_jobtype.setRotation(270);
                    rl_jobtype.setVisibility(View.VISIBLE);
                }
                break;


            case R.id.iv_show_experience:

                if (rl_experience.getVisibility() == View.VISIBLE) {
                    setVisibilityGone();
                } else {
                    setVisibilityGone();
                    iv_show_experience.setRotation(270);
                    rl_experience.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.iv_show_education:

                if (rl_education.getVisibility() == View.VISIBLE) {
                    setVisibilityGone();
                } else {
                    setVisibilityGone();
                    iv_show_education.setRotation(270);
                    rl_education.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.iv_show_preferences:

                if (rl_preferences.getVisibility() == View.VISIBLE) {
                    setVisibilityGone();
                } else {
                    setVisibilityGone();
                    iv_show_preferences.setRotation(270);
                    rl_preferences.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.iv_show_languages:

                if (rl_languages.getVisibility() == View.VISIBLE) {
                    setVisibilityGone();
                } else {
                    setVisibilityGone();
                    iv_show_languages.setRotation(270);
                    rl_languages.setVisibility(View.VISIBLE);
                }

                break;

            case R.id.iv_add_jobtype:
                if (mArrayListJobType.isEmpty()) {
                    if (rest.isInterentAvaliable()) {
                        AppController.ShowDialogue("", mContext);
                        callServiceJobType();
                    } else rest.AlertForInternet();
                } else callJobTypeSheet();

                break;

            case R.id.iv_add_experience:
                startActivity(new Intent(mContext, WorkExperience.class).putExtra("from", Initial_Profile.class.getSimpleName()));
                break;

            case R.id.iv_add_education:
                startActivity(new Intent(mContext, AddEducation.class).putExtra("from", Initial_Profile.class.getSimpleName()));
                break;

            case R.id.iv_add_preferences:
                startActivity(new Intent(mContext, AddJobPreferences.class).putExtra("prefId", prefId).putExtra("from", Initial_Profile.class.getSimpleName())
                        .putStringArrayListExtra("StringArrayList", locId)
                        .putStringArrayListExtra("StringArrayListLocName", locName)
                        .putExtra("salary", expectedSalary)
                        .putStringArrayListExtra("StringArrayListIndName", industryName)
                        .putStringArrayListExtra("StringArrayListInd", industryId));
                break;

            case R.id.iv_add_languages:
                startActivity(new Intent(mContext, AddLanguageActivity.class).putStringArrayListExtra("StringArrayList", stringLangIds).putExtra("from", Initial_Profile.class.getSimpleName()));
                break;
            case R.id.closetype:
                bottomsheetemployeetype.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.donetype:
                ArrayList<String> ids1 = new ArrayList<>();
                ArrayList<EmployeeUserDetails.Data.User_Job_Type> mArrayUserJobTypetemp = new ArrayList<>();
                IntStream.range(0, mArrayListjbtype.size()).filter(i -> mArrayListjbtype.get(i).isSelected()).forEach(i -> {
                    ids1.add(mArrayListjbtype.get(i).getId());
                    EmployeeUserDetails.Data.User_Job_Type modil = new EmployeeUserDetails.Data.User_Job_Type(mArrayListjbtype.get(i).getId(), mArrayListjbtype.get(i).getName());
                    mArrayUserJobTypetemp.add(mArrayUserJobTypetemp.size(), modil);
                });
                if (mArrayUserJobTypetemp.size() > 0) {
                    mArrayUserJobType.clear();
                    mArrayUserJobType = mArrayUserJobTypetemp;
                    setJobtypeFlow(mArrayUserJobType);
                    saveJobType(ids1.toString());
                    bottomsheetemployeetype.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else rest.showToast("Select at least 1 job type.");

                break;

            case R.id.tv_next:
                if (mArrayUserJobType.size() == 0) {
                    rest.showToast("Please select job type");
                } else if (mArrayuserusereducation == null || mArrayuserusereducation.size() == 0) {
                    rest.showToast("Please add your education");
                } else if (mArrayuseruserpreference == null || mArrayuseruserpreference.size() == 0) {
                    rest.showToast("Please add your job preferences");
                } else if (stringLangIds == null || stringLangIds.size() == 0) {
                    rest.showToast("Please add your languages");
                } else {
                    AppController.ShowDialogue("", mContext);
                    completeProfile();
                }
                break;
            default:
                break;
        }
    }

    private void setLanguageFlow(ArrayList<EmployeeUserDetails.Data.User_Languages> mArrayUserListLang) {
        flow_languages.removeAllViews();
        stringLangIds.clear();
        if (mArrayUserListLang != null) {
            IntStream.range(0, mArrayUserListLang.size()).forEach(i -> {
                LinearLayout linearLayout = new LinearLayout(mContext);
                FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                        FlowLayout.LayoutParams.WRAP_CONTENT, (int) (mContext.getResources().getDisplayMetrics().density * 32));
                layoutParams.setMargins((int) (mContext.getResources().getDisplayMetrics().density * 5), 0, (int) (mContext.getResources().getDisplayMetrics().density * 5), (int) (mContext.getResources().getDisplayMetrics().density * 8));
                linearLayout.setPadding(16, 0, 16, 0);
                linearLayout.setPadding((int) (mContext.getResources().getDisplayMetrics().density * 8), 0, (int) (mContext.getResources().getDisplayMetrics().density * 8), 0);
                linearLayout.setLayoutParams(layoutParams);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setBackgroundResource(R.drawable.primary_semiround_bg);
                PopinsRegularTextView bt = new PopinsRegularTextView(mContext);
                bt.setText(mArrayUserListLang.get(i).getLanguage_name());
                bt.setAllCaps(false);
                bt.setTextSize(12f);
                bt.setMaxLines(1);
                bt.setEllipsize(TextUtils.TruncateAt.END);
                bt.setTag(mArrayUserListLang.get(i).getLanguage_name());
                bt.setTextColor(mContext.getResources().getColor(R.color.white));
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams1.setMargins(0, 0, 0, 0);
                bt.setLayoutParams(layoutParams1);
                linearLayout.addView(bt);
                linearLayout.setTag(mArrayUserListLang.get(i).getLanguage_name());
                stringLangIds.add(stringLangIds.size(), mArrayUserListLang.get(i).getLanguage_id());
                flow_languages.addView(linearLayout);
            });

            if (mArrayUserListLang.size() > 0)
                iv_add_languages.setImageResource(R.drawable.ic_icon_feather_edit);
            else iv_add_languages.setImageResource(R.drawable.ic_plus);
        }
    }

    public void setLangFlowfromAddLang(ArrayList<String> mArrayListUserSkills, ArrayList<String> skillId) {
        if (flow_languages != null) {
            stringLangIds.clear();
            stringLangIds.addAll(skillId);
            flow_languages.removeAllViews();
            IntStream.range(0, mArrayListUserSkills.size()).forEach(i -> {
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
                bt.setText(mArrayListUserSkills.get(i));
                bt.setAllCaps(false);
                bt.setTextSize(12f);
                bt.setMaxLines(1);
                bt.setEllipsize(TextUtils.TruncateAt.END);
                bt.setTag(mArrayListUserSkills.get(i));
                bt.setTextColor(mContext.getResources().getColor(R.color.white));
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams1.setMargins(0, 0, 0, 0);
                bt.setLayoutParams(layoutParams1);
                linearLayout.addView(bt);
                linearLayoutF.addView(linearLayout);
                linearLayoutF.setTag(mArrayListUserSkills.get(i));
                flow_languages.addView(linearLayoutF);
            });
            if (mArrayListUserSkills.size() > 0)
                iv_add_languages.setImageResource(R.drawable.ic_icon_feather_edit);
            else iv_add_languages.setImageResource(R.drawable.ic_plus);
        }

    }

    private void setJobtypeFlow(ArrayList<EmployeeUserDetails.Data.User_Job_Type> mArrayUserJobType) {
        flow_jobtype.removeAllViews();
        if (mArrayUserJobType != null) {
            IntStream.range(0, mArrayUserJobType.size()).forEach(i -> {
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
                bt.setText(mArrayUserJobType.get(i).getJob_type_name());
                bt.setAllCaps(false);
                bt.setTextSize(12f);
                bt.setMaxLines(1);
                bt.setEllipsize(TextUtils.TruncateAt.END);
                bt.setTag(mArrayUserJobType.get(i).getJob_type_name());
                bt.setTextColor(mContext.getResources().getColor(R.color.white));
                LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams1.setMargins(0, 0, 0, 0);
                bt.setLayoutParams(layoutParams1);
                linearLayout.addView(bt);
                linearLayoutF.addView(linearLayout);
                linearLayoutF.setTag(mArrayUserJobType.get(i).getJob_type_name());
                flow_jobtype.addView(linearLayoutF);
            });
            if (mArrayUserJobType.size() > 0) {
                iv_add_jobtype.setImageResource(R.drawable.ic_icon_feather_edit);
            } else iv_add_jobtype.setImageResource(R.drawable.ic_plus);
        }
    }


    private void callJobTypeSheet() {
        mArrayListjbtype.clear();
        bottomsheetemployeetype.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomsheetemployeetype.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                    bottomsheetemployeetype.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });


        IntStream.range(0, mArrayListJobType.size()).forEach(i -> {
            LangModel model = new LangModel();
            model.setId(mArrayListJobType.get(i).getJob_type_id());
            model.setName(mArrayListJobType.get(i).getJob_type_name());
            model.setSelected(false);
            mArrayListjbtype.add(model);
        });
        if (mArrayUserJobType != null) {
            if (mArrayUserJobType.size() != 0) {
                for (int i = 0; i < mArrayUserJobType.size(); i++) {
                    for (int j = 0; j < mArrayListjbtype.size(); j++) {
                        if (mArrayUserJobType.get(i).getJob_type_id().equalsIgnoreCase(mArrayListjbtype.get(j).getId())) {
                            LangModel model = new LangModel();
                            model.setId(mArrayListJobType.get(j).getJob_type_id());
                            model.setName(mArrayListJobType.get(j).getJob_type_name());
                            model.setSelected(true);
                            mArrayListjbtype.set(j, model);
                        }
                    }
                }
            }
        }

        LangAdapter langadapter = new LangAdapter(mContext, mArrayListjbtype);
        listviewemptype.setAdapter(langadapter);
        langadapter.notifyDataSetChanged();

    }

    private void saveJobType(String jobtype) {
        SwipeeApiClient.swipeeServiceInstance().addJobType(Config.GetUserToken(), jobtype).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    }
                }
                getProfileData();
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                getProfileData();
            }
        });
    }

    public void setExperienceData(boolean isEdit, JsonArray jsonElements) {
        if (isEdit) {
            if (mArrayuserworkexperience != null) {
                if (jsonElements.size() > 0) {
                    JsonObject object = jsonElements.get(0).getAsJsonObject();
                    if (object.has("user_experience_id")) {
                        IntStream.range(0, mArrayuserworkexperience.size()).filter(i -> object.get("user_experience_id").getAsString().equalsIgnoreCase(mArrayuserworkexperience.get(i).getUser_experience_id())).forEach(i -> {
                            mArrayuserworkexperience.get(i).setUser_experience_id(object.get("user_experience_id").getAsString());
                            mArrayuserworkexperience.get(i).setCompany_name(object.get("company_name").getAsString());
                            mArrayuserworkexperience.get(i).setExperience_title(object.get("experience_title").getAsString());
                            mArrayuserworkexperience.get(i).setCurrently_working(object.get("currently_working").getAsString());
                            mArrayuserworkexperience.get(i).setFunctional_id(object.get("functional_id").getAsString());
                            mArrayuserworkexperience.get(i).setWork_from(object.get("work_from").getAsString());
                            mArrayuserworkexperience.get(i).setWork_to(object.get("work_to").getAsString());
                            mArrayuserworkexperience.get(i).setDescription(object.get("description").getAsString());
                            mArrayuserworkexperience.get(i).setStart_date(object.get("start_date").getAsString());
                            mArrayuserworkexperience.get(i).setEnd_date(object.get("end_date").getAsString());
                        });
                        if (mArrayuserworkexperience.size() != 0) {
                            ExperienceAdapter adapter = new ExperienceAdapter(mContext, mArrayuserworkexperience);
                            list_experience.setAdapter(adapter);
                        }
                    }
                }
            }
        } else {
            if (mArrayuserworkexperience == null) mArrayuserworkexperience = new ArrayList<>();
            if (jsonElements.size() > 0) {
                JsonObject object = jsonElements.get(0).getAsJsonObject();
                if (object.has("user_experience_id")) {
                    EmployeeUserDetails.Data.UserWorkExperience model = new EmployeeUserDetails.Data.UserWorkExperience(
                            object.get("user_experience_id").getAsString(), object.get("company_name").getAsString(), object.get("experience_title").getAsString()
                            , object.get("work_from").getAsString(), object.get("work_to").getAsString(), object.get("currently_working").getAsString()
                            , object.get("description").getAsString(), object.get("functional_id").getAsString(), object.get("start_date").getAsString(), object.get("end_date").getAsString());
                    mArrayuserworkexperience.add(mArrayuserworkexperience.size(), model);
                }
                if (mArrayuserworkexperience.size() != 0) {
                    ExperienceAdapter adapter = new ExperienceAdapter(mContext, mArrayuserworkexperience);
                    list_experience.setAdapter(adapter);
                }

            }
        }

    }

    public void setEducationData(boolean isEdit, JsonObject jsonElements) {
        if (isEdit) {
            if (mArrayuserusereducation != null) {
                if (jsonElements.size() > 0) {
                    if (jsonElements.has("user_education_id")) {
                        IntStream.range(0, mArrayuserusereducation.size()).filter(i -> jsonElements.get("user_education_id").getAsString().equalsIgnoreCase(mArrayuserusereducation.get(i).getUser_education_id())).forEach(i -> {
                            mArrayuserusereducation.get(i).setUser_education_id(jsonElements.get("user_education_id").getAsString());
                            mArrayuserusereducation.get(i).setCollege_university_id(jsonElements.get("college_university_id").getAsString());
                            mArrayuserusereducation.get(i).setCollege_university_name(jsonElements.get("college_university_name").getAsString());
                            mArrayuserusereducation.get(i).setDegree_level_id(jsonElements.get("degree_level_id").getAsString());
                            mArrayuserusereducation.get(i).setDegree_level(jsonElements.get("degree_level").getAsString());
                            mArrayuserusereducation.get(i).setDegree_type_id(jsonElements.get("degree_type_id").getAsString());
                            mArrayuserusereducation.get(i).setDegree_name(jsonElements.get("degree_name").getAsString());
                            mArrayuserusereducation.get(i).setFrom(jsonElements.get("from").getAsString());
                            mArrayuserusereducation.get(i).setStart_date(jsonElements.get("start_date").getAsString());
                            mArrayuserusereducation.get(i).setEnd_date(jsonElements.get("end_date").getAsString());
                            mArrayuserusereducation.get(i).setTo(jsonElements.get("to").getAsString());
                            mArrayuserusereducation.get(i).setCurrently_pursuing(jsonElements.get("currently_pursuing").getAsString());
                        });

                        if (mArrayuserusereducation.size() != 0) {
                            UserEducationAdapter adapter = new UserEducationAdapter(mContext, mArrayuserusereducation);
                            list_education.setAdapter(adapter);
                        }
                    }
                }
            }
        } else {
            if (mArrayuserusereducation == null) mArrayuserusereducation = new ArrayList<>();
            if (jsonElements.size() > 0) {
                JsonObject object = jsonElements;
                if (object.has("user_education_id")) {
                    EmployeeUserDetails.Data.User_Eductaion model = new EmployeeUserDetails.Data.User_Eductaion(object.get("user_education_id").getAsString(), object.get("college_university_name").getAsString(),
                            object.get("degree_level").getAsString(),
                            object.get("degree_name").getAsString(),
                            object.get("from").getAsString(),
                            object.get("to").getAsString(),
                            object.get("currently_pursuing").getAsString(),
                            object.get("college_university_id").getAsString(),
                            object.get("degree_level_id").getAsString(),
                            object.get("degree_type_id").getAsString(),
                            object.get("start_date").getAsString(),
                            object.get("end_date").getAsString()
                    );
                    mArrayuserusereducation.add(mArrayuserusereducation.size(), model);
                }
                if (mArrayuserusereducation.size() != 0) {
                    UserEducationAdapter adapter = new UserEducationAdapter(mContext, mArrayuserusereducation);
                    list_education.setAdapter(adapter);
                }
            }
        }
    }

    public void setPreferences(ArrayList<EmployeeUserDetails.Data.User_Preferences> userPreferences) {
        mArrayuseruserpreference.clear();
        mArrayuseruserpreference.addAll(userPreferences);
        locId = new ArrayList<>();
        industryId = new ArrayList<>();
        locName = new ArrayList<>();
        industryName = new ArrayList<>();
        for (int i = 0; i < mArrayuseruserpreference.size(); i++) {
            expectedSalary = mArrayuseruserpreference.get(0).getExpected_salary().getExpected_salary_number();
            for (int j = 0; j < mArrayuseruserpreference.get(i).getLocation_data().size(); j++) {
                locId.add(locId.size(), mArrayuseruserpreference.get(i).getLocation_data().get(j).getLocation_id());
                locName.add(locName.size(), mArrayuseruserpreference.get(i).getLocation_data().get(j).getLocation_name());
            }
            for (int j = 0; j < mArrayuseruserpreference.get(i).getIndustry_data().size(); j++) {
                industryId.add(industryId.size(), mArrayuseruserpreference.get(i).getIndustry_data().get(j).getIndustry_id());
                industryName.add(industryName.size(), mArrayuseruserpreference.get(i).getIndustry_data().get(j).getIndustry_name());
            }
        }
        UserPreferenceAdapter adapter = new UserPreferenceAdapter(mContext, mArrayuseruserpreference);
        list_preferences.setAdapter(adapter);
        if (mArrayuseruserpreference.size() > 0) {
            iv_add_preferences.setImageResource(R.drawable.ic_icon_feather_edit);
            prefId = mArrayuseruserpreference.get(0).getPreference_id();
        } else iv_add_preferences.setImageResource(R.drawable.ic_plus);

    }

    private void getProfileData() {
        SwipeeApiClient.swipeeServiceInstance().userdetail(Config.GetUserToken()).enqueue(new Callback<EmployeeUserDetails>() {
            @Override
            public void onResponse(@NonNull Call<EmployeeUserDetails> call, @NonNull Response<EmployeeUserDetails> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getCode() == 200) {
                        if (response.body().isStatus()) {
                            ll_main.setVisibility(View.VISIBLE);
                            setDataResponce(response.body());
                        } else rest.showToast(response.body().getMessage());
                    } else if (response.body().getCode() == 203) {
                        rest.showToast(response.body().getMessage());
                        AppController.loggedOut(mContext);
                    }
                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<EmployeeUserDetails> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }

    private void setDataResponce(EmployeeUserDetails employeeuserdetail1) {

        mArrayuserworkexperience.clear();
        mArrayuserusereducation.clear();
        mArrayuseruserpreference.clear();
        mArrayUserListLang.clear();
        mArrayUserJobType.clear();

        EmployeeUserDetails employeeuserdetail = employeeuserdetail1;
        if (employeeuserdetail.getCode() == 200 && employeeuserdetail.getData().getUser_profile_data() != null) {
            Config.SetEmail(employeeuserdetail.getData().getUser_profile_data().getEmail());
            Config.SetMobileNo(employeeuserdetail.getData().getUser_profile_data().getMobile_no());
            Config.SetPhoneCode(employeeuserdetail.getData().getUser_profile_data().getPhone_code());
            Config.SetCountry(employeeuserdetail.getData().getUser_profile_data().getCountry());
            Config.SetCountryName(employeeuserdetail.getData().getUser_profile_data().getCountry());
            Config.SetStateName(employeeuserdetail.getData().getUser_profile_data().getState());
            Config.SetCityName(employeeuserdetail.getData().getUser_profile_data().getCity());
            Config.SetState(employeeuserdetail.getData().getUser_profile_data().getState());
            Config.SetCity(employeeuserdetail.getData().getUser_profile_data().getCity());
            Config.SetDob(employeeuserdetail.getData().getUser_profile_data().getUser_dob());
            Config.SetStateId(employeeuserdetail.getData().getUser_profile_data().getState_id());
            Config.SetCityId(employeeuserdetail.getData().getUser_profile_data().getCity_id());
            Config.SetGender(employeeuserdetail.getData().getUser_profile_data().getGender());
            Config.SetId(employeeuserdetail.getData().getUser_profile_data().getUser_id());
            if (employeeuserdetail.getData().getUser_profile_data().getMiddle_name().length() != 0) {
                Config.SetName(employeeuserdetail.getData().getUser_profile_data().getFirst_name() + " " +
                        employeeuserdetail.getData().getUser_profile_data().getMiddle_name() + " " +
                        employeeuserdetail.getData().getUser_profile_data().getLast_name());
            } else {
                Config.SetName(employeeuserdetail.getData().getUser_profile_data().getFirst_name() + " " +
                        employeeuserdetail.getData().getUser_profile_data().getLast_name());
            }
            Config.SetFName(employeeuserdetail.getData().getUser_profile_data().getFirst_name());
            Config.SetLName(employeeuserdetail.getData().getUser_profile_data().getLast_name());
            Config.SetLat(employeeuserdetail.getData().getUser_profile_data().getLatitude());
            Config.SetPICKURI(employeeuserdetail.getData().getUser_profile_data().getUser_profile());
            Config.SetLongg(employeeuserdetail.getData().getUser_profile_data().getLongitude());
            Config.SetRadiuss(String.valueOf(employeeuserdetail.getData()
                    .getUser_profile_data().getDefault_radius()));
        } else rest.showToast(employeeuserdetail.getMessage());
        mArrayuserworkexperience = employeeuserdetail.getData().getUser_work_experience();
        mArrayuserusereducation = employeeuserdetail.getData().getUser_eductaion();
        mArrayuseruserpreference = employeeuserdetail.getData().getUser_preferences();

        if (mArrayuserworkexperience != null) {
            if (mArrayuserworkexperience.size() != 0) {
                ExperienceAdapter adapter = new ExperienceAdapter(mContext, mArrayuserworkexperience);
                list_experience.setAdapter(adapter);
                list_preferences.setDivider(null);
            }
        }

        if (mArrayuserusereducation != null) {
            if (mArrayuserusereducation.size() != 0) {
                UserEducationAdapter adapter = new UserEducationAdapter(mContext, mArrayuserusereducation);
                list_education.setAdapter(adapter);
            }
        }

        if (mArrayuseruserpreference != null) {
            if (mArrayuseruserpreference.size() != 0) {
                locId = new ArrayList<>();
                industryId = new ArrayList<>();
                locName = new ArrayList<>();
                industryName = new ArrayList<>();
                for (int i = 0; i < mArrayuseruserpreference.size(); i++) {
                    expectedSalary = mArrayuseruserpreference.get(0).getExpected_salary().getExpected_salary_number();
                    for (int j = 0; j < mArrayuseruserpreference.get(i).getLocation_data().size(); j++) {
                        locId.add(locId.size(), mArrayuseruserpreference.get(i).getLocation_data().get(j).getLocation_id());
                        locName.add(locName.size(), mArrayuseruserpreference.get(i).getLocation_data().get(j).getLocation_name());
                    }
                    for (int j = 0; j < mArrayuseruserpreference.get(i).getIndustry_data().size(); j++) {
                        industryId.add(industryId.size(), mArrayuseruserpreference.get(i).getIndustry_data().get(j).getIndustry_id());
                        industryName.add(industryName.size(), mArrayuseruserpreference.get(i).getIndustry_data().get(j).getIndustry_name());
                    }
                }
                iv_add_preferences.setImageResource(R.drawable.ic_icon_feather_edit);
                UserPreferenceAdapter adapter = new UserPreferenceAdapter(mContext, mArrayuseruserpreference);
                list_preferences.setAdapter(adapter);
                prefId = mArrayuseruserpreference.get(0).getPreference_id();
            } else iv_add_preferences.setImageResource(R.drawable.ic_plus);
        }

        mArrayUserListLang = employeeuserdetail.getData().getUser_languages();
        setLanguageFlow(mArrayUserListLang);

        mArrayUserJobType.clear();
        mArrayUserJobType = employeeuserdetail.getData().getUser_job_type();
        setJobtypeFlow(mArrayUserJobType);
    }

    private void completeProfile() {

        SwipeeApiClient.swipeeServiceInstance().profilecomplete(Config.GetUserToken(), "Y").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.body() != null) {
                    if (response.body().get("code").getAsInt() == 200 && response.body().get("status").getAsBoolean()) {
                        Config.setRemember(true);
                        Config.SetIsUserLogin(true);
                        Config.SetIsSeeker(true);
                        mContext.startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "new"));
                        finish();
                    } else rest.showToast(response.body().get("message").getAsString());
                } else rest.showToast("Something went wrong.");

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }
}