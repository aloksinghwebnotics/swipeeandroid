package com.webnotics.swipee.activity.Seeker;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.fragments.seeker.ProfileFragments;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddEducation extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_back;
    TextView tv_save, tv_college, tv_degree, tv_startdate, tv_enddate;
    RelativeLayout rl_startdate, rl_enddate;
    CheckBox cb_pursuing;
    Context mContext;
    Rest rest;
    private String collegeId = "";
    private String degreeId = "";
    private String degreeLevel = "";
    private String user_education_id = "";
    private boolean isEdit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_education);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        rest = new Rest(mContext);

        iv_back = findViewById(R.id.iv_back);
        tv_save = findViewById(R.id.tv_save);
        tv_college = findViewById(R.id.tv_college);
        tv_degree = findViewById(R.id.tv_degree);
        tv_startdate = findViewById(R.id.tv_startdate);
        tv_enddate = findViewById(R.id.tv_enddate);
        rl_startdate = findViewById(R.id.rl_startdate);
        rl_enddate = findViewById(R.id.rl_enddate);
        cb_pursuing = findViewById(R.id.cb_pursuing);

        iv_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        tv_college.setOnClickListener(this);
        tv_degree.setOnClickListener(this);
        rl_startdate.setOnClickListener(this);
        rl_enddate.setOnClickListener(this);
        tv_enddate.setOnClickListener(this);
        tv_startdate.setOnClickListener(this);
        cb_pursuing.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                rl_enddate.setVisibility(View.INVISIBLE);
            } else rl_enddate.setVisibility(View.VISIBLE);

        });
        if (getIntent() != null) {
            if (getIntent().getStringExtra("user_education_id") != null) {
                user_education_id = getIntent().getStringExtra("user_education_id") != null ? getIntent().getStringExtra("user_education_id") : "";
                String college_university_name = getIntent().getStringExtra("college_university_name") != null ? getIntent().getStringExtra("college_university_name") : "";
                String college_university_id = getIntent().getStringExtra("college_university_id") != null ? getIntent().getStringExtra("college_university_id") : "";
                String degree_level_id = getIntent().getStringExtra("degree_level_id") != null ? getIntent().getStringExtra("degree_level_id") : "";
                String start_date = getIntent().getStringExtra("start_date") != null ? getIntent().getStringExtra("start_date") : "";
                String end_date = getIntent().getStringExtra("end_date") != null ? getIntent().getStringExtra("end_date") : "";
                String degree_level = getIntent().getStringExtra("degree_level") != null ? getIntent().getStringExtra("degree_level") : "";
                String degree_type_id = getIntent().getStringExtra("degree_type_id") != null ? getIntent().getStringExtra("degree_type_id") : "";
                String degree_name = getIntent().getStringExtra("degree_name") != null ? getIntent().getStringExtra("degree_name") : "";
                String currently_pursuing = getIntent().getStringExtra("currently_pursuing") != null ? getIntent().getStringExtra("currently_pursuing") : "";
                isEdit = getIntent().getBooleanExtra("edit", false);
                tv_college.setText(college_university_name);
                tv_degree.setText(degree_name);
                tv_startdate.setText(start_date);
                tv_enddate.setText(end_date);
                if (currently_pursuing.equalsIgnoreCase("Y")) {
                    cb_pursuing.setChecked(true);
                } else cb_pursuing.setChecked(false);
                collegeId = college_university_id;
                degreeId = degree_type_id;
                degreeLevel = degree_level_id;
            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_save:

                if (TextUtils.isEmpty(tv_college.getText().toString()) || TextUtils.isEmpty(collegeId)) {
                    rest.showToast("Select College/University");
                } else if (TextUtils.isEmpty(tv_degree.getText().toString()) || TextUtils.isEmpty(degreeId)) {
                    rest.showToast("Select Degree");
                } else if (TextUtils.isEmpty(tv_startdate.getText().toString())) {
                    rest.showToast("Select Start Date");
                } else if (TextUtils.isEmpty(tv_enddate.getText().toString()) && !cb_pursuing.isChecked()) {
                    rest.showToast("Select End Date");
                } else {

                    if (rest.isInterentAvaliable()) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                        hashMap.put(ParaName.KEY_USEREDUCATIONID, user_education_id);
                        hashMap.put(ParaName.KEY_COLLEGENAME, collegeId);
                        hashMap.put(ParaName.KEY_DEGREENAME, degreeId);
                        hashMap.put(ParaName.KEY_CUREENTLYPERSUING, cb_pursuing.isChecked() ? "Y" : "N");
                        hashMap.put(ParaName.KEY_STARTDATE, tv_startdate.getText().toString());
                        hashMap.put(ParaName.KEY_ENDDATE, tv_enddate.getText().toString());
                        hashMap.put(ParaName.KEY_DEGREELEVEL, degreeLevel);
                        AppController.ShowDialogue("", mContext);
                        saveEducation(hashMap);
                    } else rest.AlertForInternet();


                }

                break;
            case R.id.tv_college:
                startActivity(new Intent(mContext, AddCollegeActivity.class).putExtra("collegeId", collegeId));
                break;
            case R.id.tv_degree:

                startActivity(new Intent(mContext, AddDegreeActivity.class).putExtra("degreeId", degreeId));
                break;
            case R.id.rl_enddate:
            case R.id.tv_enddate:
                if (TextUtils.isEmpty(tv_startdate.getText().toString())) {
                    rest.showToast("Select Start Date First");
                } else setEndDate();
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            String from = intent.getStringExtra("from") != null ? intent.getStringExtra("from") : "";
            if (from.equalsIgnoreCase("college")) {
                tv_college.setText(intent.getStringExtra("name"));
                collegeId = intent.getStringExtra("id");
            } else if (from.equalsIgnoreCase("degree")) {
                tv_degree.setText(intent.getStringExtra("name"));
                degreeId = intent.getStringExtra("id");
                degreeLevel = intent.getStringExtra("level");
            }
        }
    }

    private void saveEducation(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().addEducation(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (response.body().get("status").getAsBoolean()) {
                        if (ProfileFragments.instance != null) {
                            ProfileFragments.instance.setEducationData(isEdit, response.body().has("data") ? response.body().get("data").isJsonObject() ? response.body().get("data").getAsJsonObject() : new JsonObject() : new JsonObject());
                        }
                        rest.showToast(response.body().get("message").getAsString());
                        finish();
                    }
                } else {
                    AppController.dismissProgressdialog();
                    rest.showToast("Something went wrong");
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });

    }

}