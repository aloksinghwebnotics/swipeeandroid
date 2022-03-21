package com.webnotics.swipee.activity.Seeker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webnotics.swipee.CustomUi.FlowLayout;
import com.webnotics.swipee.CustomUi.PopinsRegularTextView;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.fragments.seeker.ProfileFragments;
import com.webnotics.swipee.model.seeker.EmployeeUserDetails;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddJobPreferences extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_back;
    TextView tv_save;
    Context mContext;
    Rest rest;
    private ArrayList<String> salaryList;
    private Spinner spn_sallery;
    RelativeLayout rl_industry, rl_location;
    private final ArrayList<String> stringList = new ArrayList<>();
    private final ArrayList<String> stringListid = new ArrayList<>();
    FlowLayout flow_location, flow_desired;
    private final ArrayList<String> desiredIdList = new ArrayList<>();
    private final ArrayList<String> desiredNameList = new ArrayList<>();
    private String prefId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job_preferences);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));

        mContext = this;
        rest = new Rest(mContext);
        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            callSalaryList();
        } else rest.AlertForInternet();
        if (getIntent() != null)
            prefId = getIntent().getStringExtra("prefId") != null ? getIntent().getStringExtra("prefId") : "";
        iv_back = findViewById(R.id.iv_back);
        tv_save = findViewById(R.id.tv_save);
        spn_sallery = findViewById(R.id.spn_gender);
        rl_industry = findViewById(R.id.rl_industry);
        rl_location = findViewById(R.id.rl_location);
        flow_location = findViewById(R.id.flow_location);
        flow_desired = findViewById(R.id.flow_desired);

        iv_back.setOnClickListener(this);
        rl_industry.setOnClickListener(this);
        rl_location.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        spn_sallery.setAlpha(0.40f);
        spn_sallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) spn_sallery.setAlpha(0.40f);
                else spn_sallery.setAlpha(1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_location:
                startActivity(new Intent(mContext, AddJobLocationActivity.class));
                break;

            case R.id.rl_industry:
                startActivity(new Intent(mContext, AddDesiredIndustryActivity.class));
                break;

            case R.id.tv_save:

                if (stringListid.size() == 0) rest.showToast("Select job location");
                else if (desiredIdList.size() == 0) rest.showToast("Select desired industry");
                else if (spn_sallery.getSelectedItemPosition() == 0) rest.showToast("Select salary");
                else {
                    if (rest.isInterentAvaliable()) {
                        AppController.ShowDialogue("", mContext);
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                        hashMap.put(ParaName.KEY_LOCATIONIDS, stringListid.toString());
                        hashMap.put(ParaName.KEY_INDUSTRYIDS, desiredIdList.toString());
                        hashMap.put(ParaName.KEY_EXPCTSALARY, spn_sallery.getSelectedItem().toString());
                        hashMap.put(ParaName.KEY_PREFID, prefId);
                        saveJobPreferences(hashMap);
                    } else rest.AlertForInternet();
                }
                break;

            default:
                break;
        }
    }

    private void callSalaryList() {
        SwipeeApiClient.swipeeServiceInstance().getSalary().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    JsonArray mArrayListData = responseBody.get("data").getAsJsonArray();
                    salaryList = new ArrayList<>();
                    IntStream.range(0, mArrayListData.size()).mapToObj(i -> mArrayListData.get(i).getAsJsonObject().get("salary_range").getAsString()).forEach(salary -> {
                        salaryList.add(salaryList.size(), salary.substring(0, salary.indexOf("-")));
                        salaryList.add(salaryList.size(), salary.substring(salary.indexOf("-") + 1));
                    });
                    salaryList.add(0, "Select Salary");
                    ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item, salaryList);
                    genderAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spn_sallery.setAdapter(genderAdapter);
                    spn_sallery.setSelection(0);
                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String from = intent.getStringExtra("from") != null ? intent.getStringExtra("from") : "";
            if (from.equalsIgnoreCase("location")) {
                stringList.clear();
                stringListid.clear();
                stringListid.addAll(intent.getStringArrayListExtra("StringArrayList"));
                stringList.addAll(intent.getStringArrayListExtra("StringArrayName"));
                flow_location.removeAllViews();
                IntStream.range(0, stringList.size()).forEach(i -> {
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
                    bt.setText(stringList.get(i));
                    bt.setAllCaps(false);
                    bt.setTextSize(12f);
                    bt.setMaxLines(1);
                    bt.setEllipsize(TextUtils.TruncateAt.END);
                    bt.setTag(stringList.get(i));
                    bt.setTextColor(mContext.getResources().getColor(R.color.white));
                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams1.setMargins(0, 0, 0, 0);
                    bt.setLayoutParams(layoutParams1);
                    linearLayout.addView(bt);
                    linearLayoutF.addView(linearLayout);
                    linearLayout.setTag(stringList.get(i));
                    linearLayoutF.setTag(stringList.get(i));
                    flow_location.addView(linearLayoutF);
                });
            } else if (from.equalsIgnoreCase("desired")) {
                desiredIdList.clear();
                desiredNameList.clear();
                desiredIdList.addAll(intent.getStringArrayListExtra("StringArrayList"));
                desiredNameList.addAll(intent.getStringArrayListExtra("StringArrayName"));
                flow_desired.removeAllViews();
                IntStream.range(0, desiredNameList.size()).forEach(i -> {
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
                    bt.setText(desiredNameList.get(i));
                    bt.setAllCaps(false);
                    bt.setMaxLines(1);
                    bt.setEllipsize(TextUtils.TruncateAt.END);
                    bt.setTextSize(12f);
                    bt.setTag(desiredNameList.get(i));
                    bt.setTextColor(mContext.getResources().getColor(R.color.white));
                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams1.setMargins(0, 0, 0, 0);
                    bt.setLayoutParams(layoutParams1);
                    linearLayout.addView(bt);
                    linearLayoutF.addView(linearLayout);
                    linearLayoutF.setTag(desiredNameList.get(i));
                    flow_desired.addView(linearLayoutF);
                });
            }
        }
    }


    private void saveJobPreferences(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().addJobPreference(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (response.body().get("status").getAsBoolean()) {
                        ArrayList<EmployeeUserDetails.Data.User_Preferences> mArrayUserPreference = new ArrayList<>();
                        if (response.body().has("data")) {
                            JsonArray data = response.body().get("data").getAsJsonArray();
                            if (data.size() > 0) {
                                JsonObject dataObj = data.get(0).getAsJsonObject();
                                String preference_id = dataObj.has("preference_id") ? dataObj.get("preference_id").getAsString() : "";
                                JsonArray location_data = dataObj.has("location_data") ? dataObj.get("location_data").getAsJsonArray() : new JsonArray();
                                JsonArray industry_data = dataObj.has("industry_data") ? dataObj.get("industry_data").getAsJsonArray() : new JsonArray();
                                JsonObject expected_salary = dataObj.has("expected_salary") ? dataObj.get("expected_salary").getAsJsonObject() : new JsonObject();
                                String expected_salary_number = expected_salary.has("expected_salary_number") ? expected_salary.get("expected_salary_number").getAsString() : "";
                                String expected_salary_words = expected_salary.has("expected_salary_words") ? expected_salary.get("expected_salary_words").getAsString() : "";
                                ArrayList<EmployeeUserDetails.Data.User_Preferences.Location_Data> locationData = new ArrayList<>();
                                ArrayList<EmployeeUserDetails.Data.User_Preferences.Industry_Data> industryData = new ArrayList<>();
                                EmployeeUserDetails.Data.User_Preferences.Expected_Salary expectedSalary = new EmployeeUserDetails.Data.User_Preferences.Expected_Salary(expected_salary_number, expected_salary_words);
                                IntStream.range(0, location_data.size()).mapToObj(i -> location_data.get(i).getAsJsonObject()).forEach(locationObj -> {
                                    long location_id = locationObj.has("location_id") ? locationObj.get("location_id").getAsLong() : 0;
                                    String location_name = locationObj.has("location_name") ? locationObj.get("location_name").getAsString() : "";
                                    EmployeeUserDetails.Data.User_Preferences.Location_Data locationData1 = new EmployeeUserDetails.Data.User_Preferences.Location_Data(String.valueOf(location_id), location_name);
                                    locationData.add(locationData.size(), locationData1);
                                });
                                IntStream.range(0, industry_data.size()).mapToObj(i -> industry_data.get(i).getAsJsonObject()).forEach(industryObj -> {
                                    long industry_id = industryObj.has("industry_id") ? industryObj.get("industry_id").getAsLong() : 0;
                                    String industry_name = industryObj.has("industry_name") ? industryObj.get("industry_name").getAsString() : "";
                                    EmployeeUserDetails.Data.User_Preferences.Industry_Data industry_data1 = new EmployeeUserDetails.Data.User_Preferences.Industry_Data(String.valueOf(industry_id), industry_name);
                                    industryData.add(industryData.size(), industry_data1);
                                });
                                EmployeeUserDetails.Data.User_Preferences user_preferences = new EmployeeUserDetails.Data.User_Preferences(preference_id, locationData, industryData, expectedSalary);

                                mArrayUserPreference.add(user_preferences);
                                if (ProfileFragments.instance != null)
                                    ProfileFragments.instance.setPreferences(mArrayUserPreference);
                            }
                        }
                        rest.showToast(response.body().get("message").getAsString());
                        finish();
                    }
                } else rest.showToast("Something went wrong");
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });

    }
}