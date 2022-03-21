package com.webnotics.swipee.activity.Seeker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.adapter.seeeker.JobTitleAdapter;
import com.webnotics.swipee.fragments.seeker.ProfileFragments;
import com.webnotics.swipee.model.seeker.JobTitleModel;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WorkExperience extends AppCompatActivity implements View.OnClickListener {
    public String jobId = "";
    public String jobName = "";
    ImageView iv_back, iv_close;
    TextView tv_save, tv_job, tv_startdate, tv_enddate;
    RelativeLayout rl_startdate, rl_enddate, jobtype_sheet;
    CheckBox cb_pursuing;
    Context mContext;
    Rest rest;
    EditText et_company, et_workdesc;
    BottomSheetBehavior jobtype_sheetBottom;
    private RecyclerView listview;
    private final ArrayList<JobTitleModel> jsonObjectArrayList = new ArrayList<>();
    private String jobIdFinal = "";
    private String user_experience_id = "";
    private String functional_id;
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_experience);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        rest = new Rest(mContext);

        iv_back = findViewById(R.id.iv_back);
        tv_save = findViewById(R.id.tv_save);
        et_company = findViewById(R.id.et_company);
        tv_job = findViewById(R.id.tv_job);
        tv_startdate = findViewById(R.id.tv_startdate);
        tv_enddate = findViewById(R.id.tv_enddate);
        rl_startdate = findViewById(R.id.rl_startdate);
        rl_enddate = findViewById(R.id.rl_enddate);
        cb_pursuing = findViewById(R.id.cb_pursuing);
        et_workdesc = findViewById(R.id.et_workdesc);

        jobtype_sheet = findViewById(R.id.jobtype_sheet);
        jobtype_sheetBottom = BottomSheetBehavior.from(jobtype_sheet);
        iv_close = jobtype_sheet.findViewById(R.id.close);
        Button doneeeee = jobtype_sheet.findViewById(R.id.doneeeee);
        listview = jobtype_sheet.findViewById(R.id.listviewlanguage);

        iv_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        iv_close.setOnClickListener(this);

        rl_startdate.setOnClickListener(this);
        rl_enddate.setOnClickListener(this);
        tv_enddate.setOnClickListener(this);
        tv_startdate.setOnClickListener(this);
        tv_job.setOnClickListener(this);
        doneeeee.setOnClickListener(this);

        cb_pursuing.setOnCheckedChangeListener((buttonView, isChecked) -> {
            rl_enddate.setVisibility(isChecked ? View.INVISIBLE : View.VISIBLE);

        });

        if (getIntent() != null) {
            if (getIntent().getStringExtra("user_experience_id") != null) {

                user_experience_id = getIntent().getStringExtra("user_experience_id") != null ? getIntent().getStringExtra("user_experience_id") : "";
                String experience_title = getIntent().getStringExtra("experience_title") != null ? getIntent().getStringExtra("experience_title") : "";
                functional_id = getIntent().getStringExtra("functional_id") != null ? getIntent().getStringExtra("functional_id") : "";
                String company_name = getIntent().getStringExtra("company_name") != null ? getIntent().getStringExtra("company_name") : "";
                String start_date = getIntent().getStringExtra("start_date") != null ? getIntent().getStringExtra("start_date") : "";
                String end_date = getIntent().getStringExtra("end_date") != null ? getIntent().getStringExtra("end_date") : "";
                String currently_working = getIntent().getStringExtra("currently_working") != null ? getIntent().getStringExtra("currently_working") : "";
                String description = getIntent().getStringExtra("description") != null ? getIntent().getStringExtra("description") : "";
                isEdit = getIntent().getBooleanExtra("edit", false);
                tv_job.setText(experience_title);
                et_company.setText(company_name);
                et_workdesc.setText(description);
                tv_startdate.setText(start_date);
                tv_enddate.setText(end_date);
                jobIdFinal = functional_id;
                cb_pursuing.setChecked(currently_working.equalsIgnoreCase("Y"));

            }
        }

        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            callJobTitleList();
        } else rest.AlertForInternet();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.close:
                jobtype_sheetBottom.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.tv_job:
                if (jsonObjectArrayList.size() > 0)
                    callJobTypeSheet();
                else callJobTitleList();
                break;
            case R.id.doneeeee:

                jobtype_sheetBottom.setState(BottomSheetBehavior.STATE_COLLAPSED);
                jobIdFinal = jobId;
                String jobNameFinal = jobName;
                tv_job.setText(jobNameFinal);
                IntStream.range(0, jsonObjectArrayList.size()).forEach(i -> jsonObjectArrayList.get(i).setSelected(jsonObjectArrayList.get(i).getId().equalsIgnoreCase(jobIdFinal)));
                break;
            case R.id.tv_save:

                if (TextUtils.isEmpty(tv_job.getText().toString()))
                    rest.showToast("Select Job Type");
                else if (TextUtils.isEmpty(et_company.getText().toString()))
                    rest.showToast("Enter Company Name");
                else if (TextUtils.isEmpty(tv_startdate.getText().toString()))
                    rest.showToast("Select Start Date");
                else if (TextUtils.isEmpty(tv_enddate.getText().toString()) && !cb_pursuing.isChecked())
                    rest.showToast("Select End Date");
                else {
                    if (rest.isInterentAvaliable()) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                        hashMap.put(ParaName.KEY_JOBTITLE, jobIdFinal);
                        hashMap.put(ParaName.KEY_USEREXPID, user_experience_id);
                        hashMap.put(ParaName.KEY_COMPANYNAME, et_company.getText().toString());
                        hashMap.put(ParaName.KEY_CURENTLYWORKING, cb_pursuing.isChecked() ? "Y" : "N");
                        hashMap.put(ParaName.KEY_WORKFROM, tv_startdate.getText().toString());
                        hashMap.put(ParaName.KEY_WORKTO, tv_enddate.getText().toString());
                        hashMap.put(ParaName.KEY_DESCRIPTION, et_workdesc.getText().toString());
                        AppController.ShowDialogue("", mContext);
                        saveEducation(hashMap);
                    } else rest.AlertForInternet();

                }

                break;
            case R.id.rl_enddate:
            case R.id.tv_enddate:
                if (TextUtils.isEmpty(tv_startdate.getText().toString()))
                    rest.showToast("Select Start Date First");
                else setEndDate();
                break;
            case R.id.rl_startdate:
            case R.id.tv_startdate:
                setStartDate();
                break;

            default:
                break;
        }

    }

    private void setStartDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                (view, year, monthOfYear, dayOfMonth) -> {
                    int mothfinal = monthOfYear + 1;
                    tv_startdate.setText(year + "-" + mothfinal + "-" + dayOfMonth);
                    tv_enddate.setText("");
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void setEndDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                (view, year, monthOfYear, dayOfMonth) -> {
                    int mothfinal = monthOfYear + 1;
                    tv_enddate.setText(year + "-" + mothfinal + "-" + dayOfMonth);

                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        String dtStart = tv_startdate.getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-M-d");
        Date date;
        try {
            date = format.parse(dtStart);
            {
                Calendar c1 = Calendar.getInstance();
                c1.setTime(date);
                c1.add(Calendar.DATE, 1);
                date = c1.getTime();
                datePickerDialog.getDatePicker().setMinDate(date.getTime());
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        datePickerDialog.show();
    }

    private void saveEducation(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().addWorExp(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (response.body().get("status").getAsBoolean()) {
                        if (ProfileFragments.instance != null)
                            ProfileFragments.instance.setExperienceData(isEdit, response.body().has("data") ? response.body().get("data").isJsonArray() ? response.body().get("data").getAsJsonArray() : new JsonArray() : new JsonArray());
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

    private void callJobTypeSheet() {
        jobName = "";
        jobId = "";
        jobtype_sheetBottom.setState(BottomSheetBehavior.STATE_EXPANDED);
        jobtype_sheetBottom.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING)
                    jobtype_sheetBottom.setState(BottomSheetBehavior.STATE_EXPANDED);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

        JobTitleAdapter langadapter = new JobTitleAdapter(WorkExperience.this, jsonObjectArrayList);
        listview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        listview.setNestedScrollingEnabled(false);
        listview.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
        listview.setAdapter(langadapter);

    }

    private void callJobTitleList() {
        SwipeeApiClient.swipeeServiceInstance().getJobTitle().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    JsonArray mArrayListData = responseBody.get("data").getAsJsonArray();
                    jsonObjectArrayList.clear();
                    IntStream.range(0, mArrayListData.size()).mapToObj(i -> mArrayListData.get(i).getAsJsonObject()).map(jsonObject -> new JobTitleModel(jsonObject.get("functional_id").getAsString(), jsonObject.get("functional_name").getAsString(), jsonObject.get("functional_id").getAsString().equalsIgnoreCase(functional_id))).forEach(jobTitleModel -> jsonObjectArrayList.add(jsonObjectArrayList.size(), jobTitleModel));
                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });
    }


}