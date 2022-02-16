package com.webnotics.swipee.fragments.company;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webnotics.swipee.CustomUi.FlowLayout;
import com.webnotics.swipee.CustomUi.PopinsRegularTextView;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.Seeker.AddLanguageActivity;
import com.webnotics.swipee.activity.company.AddIndustryActivity;
import com.webnotics.swipee.activity.company.JobPreview;
import com.webnotics.swipee.adapter.seeeker.AddSkillAdapter;
import com.webnotics.swipee.fragments.Basefragment;
import com.webnotics.swipee.interfaces.AddSkillInterface;
import com.webnotics.swipee.model.AddSkillsModel;
import com.webnotics.swipee.model.company.CommonModel;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostJobFragments extends Basefragment implements View.OnClickListener, TextWatcher, AddSkillInterface {


    private Rest rest;
    Context mContext;
    EditText et_description, et_search, et_jobtitle, et_company;
    ListView mlistview;
    FlowLayout flowlayout;
    NestedScrollView kdkdkdkd;
    TextView tv_next, tv_back, tv_language, tv_perk,
            tv_nodata, tv_qualification, tv_location, tv_employmenttype, tv_company, tv_industry;
    CheckBox  cb_samecompany;
    RelativeLayout rl_first, rl_second, rl_third, rl_fourth, rl_fifth,rl_nodata;
    private JsonObject featured_package = new JsonObject();
    private JsonArray languagesArray = new JsonArray();
    private JsonArray working_days = new JsonArray();
    private JsonArray working_shifts = new JsonArray();
    private JsonArray perk_benefits = new JsonArray();
    private JsonArray job_opening_numbers = new JsonArray();
    private JsonArray degree_level = new JsonArray();
    private JsonArray degree_type = new JsonArray();
    private JsonArray experience = new JsonArray();
    private JsonArray salary_offer = new JsonArray();
    private JsonArray salary_type = new JsonArray();
    private JsonArray job_industry = new JsonArray();
    private JsonArray employment_type = new JsonArray();
    private JsonArray job_location = new JsonArray();
    RelativeLayout jobtypebottom_sheet, qualification_sheet, perk_sheet;
    BottomSheetBehavior bottomsheet_jobType, bottomsheet_qualification, bottomsheet_perk;
    RecyclerView rv_jobtype;
    private JobPostJobTypeAdapter jobTypeAdapter;
    static String tempIndustryId = "";
    static String tempIndustryName = "";
    static String tempjobTypeId = "";
    static String tempjobTypeName = "";
    static String tempLocationId = "";
    static String tempLocationName = "";
    private String industryId = "";
    private String employmentId = "";
    private String locationId = "";
    ListView list_qualification, list_perk, list_language;
    private QualificationAdapter qualificationAdapter;
    private PerkAdapter perkAdapter;
    ArrayList<CommonModel> qualificationArraylist = new ArrayList<>();
    ArrayList<CommonModel> perkArraylist = new ArrayList<>();
    ArrayList<CommonModel> languageArraylist = new ArrayList<>();
    ArrayList<CommonModel> qualification = new ArrayList<>();
    ArrayList<CommonModel> perks = new ArrayList<>();

    Spinner spn_openning, spn_experience, spn_minsalary, spn_maxsalary, spn_salarytype, spn_workshift, spn_workdays;
    ArrayList<JsonObject> experienceList = new ArrayList<>();
    ArrayList<JsonObject> salarytypeList = new ArrayList<>();
    ArrayList<JsonObject> salaryofferList = new ArrayList<>();
    private ArrayList<AddSkillsModel> mArrayListSkills;
    private AddSkillAdapter skilladapter;
    View.OnClickListener btnClickListener;
    AddSkillInterface addSkillsInterface;
    ArrayList<String> mArrayListSkillSelected, mArrayListSkillSelectedid;
    ImageView iv_nodata;
    public ArrayList<CommonModel> commonModels=new ArrayList<>();
    public  ArrayList<CommonModel> commonModelsLocation=new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    public static  PostJobFragments instance;
    public  ArrayList<CommonModel> commonModelsLanguage = new ArrayList<>();
    private ArrayList<String> languageIds=new ArrayList<>();
    private ArrayList<String> salarymaxArray;
    private ArrayList<String> salaryminArray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.post_job_screen, container, false);
        mContext = getActivity();
        rest = new Rest(mContext);
                  instance=this;
        tv_next = rootView.findViewById(R.id.tv_next);
        tv_back = rootView.findViewById(R.id.tv_back);
        rl_first = rootView.findViewById(R.id.rl_first);
        rl_second = rootView.findViewById(R.id.rl_second);
        rl_third = rootView.findViewById(R.id.rl_third);
        rl_fourth = rootView.findViewById(R.id.rl_fourth);
        rl_fifth = rootView.findViewById(R.id.rl_fifth);
        rl_nodata = rootView.findViewById(R.id.rl_nodata);
        tv_nodata = rootView.findViewById(R.id.tv_nodata);
        iv_nodata = rootView.findViewById(R.id.iv_nodata);

        spn_workdays = rootView.findViewById(R.id.spn_workdays);
        spn_workshift = rootView.findViewById(R.id.spn_workshift);
        spn_salarytype = rootView.findViewById(R.id.spn_salarytype);
        spn_maxsalary = rootView.findViewById(R.id.spn_maxsalary);
        spn_minsalary = rootView.findViewById(R.id.spn_minsalary);
        spn_experience = rootView.findViewById(R.id.spn_experience);
        spn_openning = rootView.findViewById(R.id.spn_openning);
        cb_samecompany = rootView.findViewById(R.id.cb_samecompany);
        tv_industry = rootView.findViewById(R.id.tv_industry);
        tv_company = rootView.findViewById(R.id.tv_company);
        tv_employmenttype = rootView.findViewById(R.id.tv_employmenttype);
        tv_location = rootView.findViewById(R.id.tv_location);
        tv_qualification = rootView.findViewById(R.id.tv_qualification);
        et_description = rootView.findViewById(R.id.et_description);
        et_search = rootView.findViewById(R.id.et_search);
        et_jobtitle = rootView.findViewById(R.id.et_jobtitle);
        et_company = rootView.findViewById(R.id.et_company);
        mlistview = rootView.findViewById(R.id.mlistview);
        flowlayout = rootView.findViewById(R.id.flowlayout);
        kdkdkdkd = rootView.findViewById(R.id.kdkdkdkd);
        tv_language = rootView.findViewById(R.id.tv_language);
        tv_perk = rootView.findViewById(R.id.tv_perk);


        jobtypeBottomSheet(rootView);
        qualificationBottomSheet(rootView);
        perkBottomSheet(rootView);

        rl_first.setVisibility(View.VISIBLE);
        tv_back.setOnClickListener(this);
        tv_next.setOnClickListener(this);

        if (Config.GetPACKAGEEXP().equalsIgnoreCase("Y")){
            rl_nodata.setVisibility(View.VISIBLE);
            iv_nodata.setImageResource(R.drawable.ic_plan_exp);
            tv_nodata.setText(getString(R.string.packageexprectr));
        }else if (Config.GetLeftPostCount()<=0){
            iv_nodata.setImageResource(R.drawable.ic_np_post);
            rl_nodata.setVisibility(View.VISIBLE);
            tv_nodata.setText(getString(R.string.planexpire));
        }else {
            rl_nodata.setVisibility(View.GONE);
            if (rest.isInterentAvaliable()) {

                AppController.ShowDialogue("", mContext);
                getPostJobStepOne();

            } else rest.AlertForInternet();
        }

        cb_samecompany.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                tv_company.setVisibility(View.VISIBLE);
                et_company.setVisibility(View.GONE);
            } else {
                tv_company.setVisibility(View.GONE);
                et_company.setVisibility(View.VISIBLE);
            }
        });
        mArrayListSkillSelected = new ArrayList<>();
        mArrayListSkillSelectedid = new ArrayList<>();
        addSkillsInterface = this;
        mArrayListSkills = new ArrayList<>();
        et_search.addTextChangedListener(this);
        btnClickListener = v -> {
            // TODO Auto-generated method stub
            FlowLayout ll = (FlowLayout) v.getParent();

            for (int j = 0; j < mArrayListSkills.size(); j++) {
                if (v.getTag().toString().equalsIgnoreCase(mArrayListSkills.get(j).getSkill_name())) {
                    AddSkillsModel model1 = new AddSkillsModel();
                    model1.setSkill_name(mArrayListSkills.get(j).getSkill_name());
                    model1.setSelected(false);
                    model1.setSkill_id(mArrayListSkills.get(j).getSkill_id());
                    mArrayListSkills.set(j, model1);
                    if (skilladapter != null)
                        skilladapter.notifyDataSetChanged();
                }
            }
            for (int z = 0; z < mArrayListSkillSelected.size(); z++) {
                if (v.getTag().toString().equalsIgnoreCase(mArrayListSkillSelected.get(z))) {
                    mArrayListSkillSelected.remove(z);
                    mArrayListSkillSelectedid.remove(z);
                    if (skilladapter != null)
                        skilladapter.getFilter().filter("");
                    et_search.setText("");
                }
            }
            ll.removeView(v);
            if (flowlayout.getMeasuredHeight() > 272) {
                kdkdkdkd.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 272));
            } else
                kdkdkdkd.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        };

        spn_workdays.setAlpha(0.4f);
        spn_workshift.setAlpha(0.4f);
     //   spn_salarytype.setAlpha(0.4f);
        spn_maxsalary.setAlpha(0.4f);
        spn_minsalary.setAlpha(0.4f);
        spn_experience.setAlpha(0.4f);
        spn_openning.setAlpha(0.4f);

        spn_workdays.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spn_workdays.setAlpha(0.4f);
                } else spn_workdays.setAlpha(1f);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spn_workshift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spn_workshift.setAlpha(0.4f);
                } else spn_workshift.setAlpha(1f);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        /*spn_salarytype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spn_salarytype.setAlpha(0.4f);
                } else spn_salarytype.setAlpha(1f);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
        spn_maxsalary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spn_maxsalary.setAlpha(0.4f);
                } else{ spn_maxsalary.setAlpha(1f);
                 if (spn_minsalary.getSelectedItemPosition()!=0)
                    if (Integer.parseInt(spn_maxsalary.getSelectedItem().toString())<Integer.parseInt(spn_minsalary.getSelectedItem().toString())){
                               rest.showToast("Max salary must be greater than min salary");
                               spn_maxsalary.setSelection(0);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spn_minsalary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spn_minsalary.setAlpha(0.4f);
                    spn_maxsalary.setSelection(0);
                    ArrayAdapter<String> maxAdapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item, salarymaxArray);
                    maxAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                    spn_maxsalary.setAdapter(maxAdapter);
                    spn_maxsalary.setSelection(0);
                } else{
                    spn_minsalary.setAlpha(1f);
                    if (spn_maxsalary.getSelectedItemPosition()>0){
                        if (Integer.parseInt(spn_minsalary.getSelectedItem().toString())>Integer.parseInt(spn_maxsalary.getSelectedItem().toString())){
                           ArrayList<String> maxTemp=new ArrayList<>();
                           maxTemp.add(0,"Max Salary");
                            for (int i=1;i<salarymaxArray.size();i++){
                                if (Integer.parseInt(spn_minsalary.getSelectedItem().toString())<Integer.parseInt(salarymaxArray.get(i))) {
                                    maxTemp.add(maxTemp.size(),salarymaxArray.get(i));
                                }
                                }
                            ArrayAdapter<String> maxAdapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item, maxTemp);
                            maxAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            spn_maxsalary.setAdapter(maxAdapter);
                            spn_maxsalary.setSelection(0);
                        }else {
                            ArrayList<String> maxTemp=new ArrayList<>();
                            String maxval=spn_maxsalary.getSelectedItem().toString();
                            maxTemp.add(0,"Max Salary");
                            for (int i=1;i<salarymaxArray.size();i++){
                                if (Integer.parseInt(spn_minsalary.getSelectedItem().toString())<Integer.parseInt(salarymaxArray.get(i))) {
                                    maxTemp.add(maxTemp.size(),salarymaxArray.get(i));
                                }
                            }
                            ArrayAdapter<String> maxAdapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item, maxTemp);
                            maxAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            spn_maxsalary.setAdapter(maxAdapter);
                            spn_maxsalary.setSelection(maxTemp.indexOf(maxval));
                        }
                    }else {
                        ArrayList<String> maxTemp=new ArrayList<>();
                        maxTemp.add(0,"Max Salary");
                        for (int i=1;i<salarymaxArray.size();i++){
                            if (Integer.parseInt(spn_minsalary.getSelectedItem().toString())<Integer.parseInt(salarymaxArray.get(i))) {
                                maxTemp.add(maxTemp.size(),salarymaxArray.get(i));
                            }
                        }
                        ArrayAdapter<String> maxAdapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item, maxTemp);
                        maxAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                        spn_maxsalary.setAdapter(maxAdapter);
                        spn_maxsalary.setSelection(0);
                    }
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spn_experience.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spn_experience.setAlpha(0.4f);

                } else spn_experience.setAlpha(1f);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spn_openning.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    spn_openning.setAlpha(0.4f);
                } else spn_openning.setAlpha(1f);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return rootView;


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (skilladapter != null)
            skilladapter.getFilter().filter(s.toString().trim());
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() == 0) {
            //  close.setVisibility(View.GONE);
        } else {
            //  close.setVisibility(View.VISIBLE);
        }
    }


    private void jobtypeBottomSheet(View rootView) {
        jobtypebottom_sheet = rootView.findViewById(R.id.employeetypebottom_sheet);
        bottomsheet_jobType = BottomSheetBehavior.from(jobtypebottom_sheet);
        ImageView iv_jobtypeclose = jobtypebottom_sheet.findViewById(R.id.closetype);
        Button btn_jobtypeDone = jobtypebottom_sheet.findViewById(R.id.done_jobtype);
        rv_jobtype = jobtypebottom_sheet.findViewById(R.id.rv_jobtype);
        btn_jobtypeDone.setOnClickListener(this);
        jobtypebottom_sheet.setOnClickListener(this);
        iv_jobtypeclose.setOnClickListener(this);
    }

    private void qualificationBottomSheet(View rootView) {
        qualification_sheet = rootView.findViewById(R.id.qualification_sheet);
        bottomsheet_qualification = BottomSheetBehavior.from(qualification_sheet);
        ImageView iv_close_qualification = qualification_sheet.findViewById(R.id.iv_close_qualification);
        Button btn_done_qualification = qualification_sheet.findViewById(R.id.btn_done_qualification);
        list_qualification = qualification_sheet.findViewById(R.id.list_qualification);
        btn_done_qualification.setOnClickListener(this);
        qualification_sheet.setOnClickListener(this);
        iv_close_qualification.setOnClickListener(this);
    }

    private void perkBottomSheet(View rootView) {
        perk_sheet = rootView.findViewById(R.id.perk_sheet);
        bottomsheet_perk = BottomSheetBehavior.from(perk_sheet);
        ImageView iv_close_perk = perk_sheet.findViewById(R.id.iv_close_perk);
        Button btn_done_perk = perk_sheet.findViewById(R.id.btn_done_perk);
        list_perk = perk_sheet.findViewById(R.id.list_perk);
        btn_done_perk.setOnClickListener(this);
        perk_sheet.setOnClickListener(this);
        iv_close_perk.setOnClickListener(this);
    }

    @Override
    public int setContentView() {
        return R.layout.post_job_screen;
    }


    @Override
    protected void backPressed() {
        if (bottomsheet_qualification.getState() == BottomSheetBehavior.STATE_EXPANDED
                || bottomsheet_jobType.getState() == BottomSheetBehavior.STATE_EXPANDED
                || bottomsheet_perk.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomsheet_qualification.setState(BottomSheetBehavior.STATE_COLLAPSED);
            bottomsheet_jobType.setState(BottomSheetBehavior.STATE_COLLAPSED);
            bottomsheet_perk.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (rl_first.getVisibility() == View.VISIBLE) {
            getActivity().finish();
        } else if (rl_second.getVisibility() == View.VISIBLE) {
            tv_back.setVisibility(View.GONE);
            rl_first.setVisibility(View.VISIBLE);
            rl_second.setVisibility(View.GONE);
        } else if (rl_third.getVisibility() == View.VISIBLE) {
            tv_back.setVisibility(View.VISIBLE);
            rl_second.setVisibility(View.VISIBLE);
            rl_third.setVisibility(View.GONE);
        } else if (rl_fourth.getVisibility() == View.VISIBLE) {
            tv_back.setVisibility(View.VISIBLE);
            rl_third.setVisibility(View.VISIBLE);
            rl_fourth.setVisibility(View.GONE);
        } else if (rl_fifth.getVisibility() == View.VISIBLE) {
            tv_back.setVisibility(View.VISIBLE);
            rl_fourth.setVisibility(View.VISIBLE);
            rl_fifth.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.tv_next:
                String companyName = "";
                if (cb_samecompany.isChecked())
                    companyName = tv_company.getText().toString();
                else companyName = et_company.getText().toString();

                if (rl_first.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(tv_industry.getText().toString()) || TextUtils.isEmpty(industryId)) {
                        rest.showToast("Select Job Industry");
                    } else if (TextUtils.isEmpty(companyName.replaceAll(" ", ""))) {
                        rest.showToast("Enter Company Name");
                    } else if (TextUtils.isEmpty(et_jobtitle.getText().toString().replaceAll(" ", ""))) {
                        rest.showToast("Enter Job Title");
                    } else if (TextUtils.isEmpty(tv_employmenttype.getText().toString()) || TextUtils.isEmpty(employmentId)) {
                        rest.showToast("Select Employment Type");
                    } else if (TextUtils.isEmpty(tv_location.getText().toString()) || TextUtils.isEmpty(locationId)) {
                        rest.showToast("Select Location");
                    } else {
                        rl_first.setVisibility(View.GONE);
                        rl_second.setVisibility(View.VISIBLE);
                        tv_back.setVisibility(View.VISIBLE);
                        if (degree_type.size() > 0 && job_opening_numbers.size() > 0 && salary_offer.size() > 0) {
                        } else {
                            if (rest.isInterentAvaliable()) {
                                AppController.ShowDialogue("", mContext);
                                getPostJobStepTwo();
                            } else rest.AlertForInternet();
                        }
                    }

                } else if (rl_second.getVisibility() == View.VISIBLE) {
                    if (spn_openning == null || spn_openning.getSelectedItemPosition() == 0) {
                        rest.showToast("Select number of opening");
                    } else if (TextUtils.isEmpty(tv_qualification.getText().toString())) {
                        rest.showToast("Select Qualification");
                    } else if (spn_experience == null || spn_experience.getSelectedItemPosition() == 0) {
                        rest.showToast("Select Work Experience");
                    } else if (spn_minsalary == null || spn_minsalary.getSelectedItemPosition() == 0) {
                        rest.showToast("Select Min Salary");
                    } else if (spn_maxsalary == null || spn_maxsalary.getSelectedItemPosition() == 0) {
                        rest.showToast("Select Max Salary");
                    } else if (Integer.parseInt(spn_maxsalary.getSelectedItem().toString()) < Integer.parseInt(spn_minsalary.getSelectedItem().toString())) {
                        rest.showToast("Max Salary must be greater than Min Salary");
                    } /*else if (spn_salarytype == null || spn_salarytype.getSelectedItemPosition() == 0) {
                        rest.showToast("Select Salary Type");
                    }*/ else {
                        if (mArrayListSkills.size() > 0) {
                        } else {
                            if (rest.isInterentAvaliable()) {
                                AppController.ShowDialogue("", mContext);
                                callSkillList();
                            } else rest.AlertForInternet();
                        }
                        tv_back.setVisibility(View.VISIBLE);
                        rl_second.setVisibility(View.GONE);
                        rl_third.setVisibility(View.VISIBLE);
                    }

                } else if (rl_third.getVisibility() == View.VISIBLE) {
                    if (mArrayListSkillSelectedid.size() < 3) {
                        rest.showToast("Select minimum 3 skills");
                    } else {
                        tv_back.setVisibility(View.VISIBLE);
                        rl_third.setVisibility(View.GONE);
                        rl_fourth.setVisibility(View.VISIBLE);
                    }

                } else if (rl_fourth.getVisibility() == View.VISIBLE) {
                    if (TextUtils.isEmpty(et_description.getText().toString().replaceAll(" ", ""))) {
                        rest.showToast("Enter job description");
                    } else {
                        tv_back.setVisibility(View.VISIBLE);
                        rl_fourth.setVisibility(View.GONE);
                        rl_fifth.setVisibility(View.VISIBLE);
                        if (languagesArray.size() > 0) {
                        } else {
                            if (rest.isInterentAvaliable()) {
                                AppController.ShowDialogue("", mContext);
                                getPostJobStepFive();
                            } else rest.AlertForInternet();
                        }
                    }

                } else if (rl_fifth.getVisibility() == View.VISIBLE) {
                   /* if (perks.size() == 0) {
                        rest.showToast("Select perks and benefits");
                    } else*/ if (spn_workshift.getSelectedItemPosition() == 0) {
                        rest.showToast("Select working shift");
                    } else if (spn_workdays.getSelectedItemPosition() == 0) {
                        rest.showToast("Select working days");
                    } else if (languageIds.size() == 0) {
                        rest.showToast("Select languages");
                    } else {
                        String industryName = tv_industry.getText().toString();
                        String company = companyName;
                        String jobTitle = et_jobtitle.getText().toString();
                        String employmentType = tv_employmenttype.getText().toString();
                        String jobLocation = tv_location.getText().toString();
                        String numberOpening = spn_openning.getSelectedItem().toString();
                        String qualificationName = tv_qualification.getText().toString();
                        String experience1 = spn_experience.getSelectedItem().toString();
                        String salaryMin = spn_minsalary.getSelectedItem().toString();
                        String salaryMax = spn_maxsalary.getSelectedItem().toString();
                     /*   String salaryType = spn_salarytype.getSelectedItem().toString();*/
                        String salaryType = "Monthly";
                        String description = et_description.getText().toString();
                        String perkBenefit = tv_perk.getText().toString();
                        String workShift = spn_workshift.getSelectedItem().toString();
                        String workDays = spn_workdays.getSelectedItem().toString();
                        String languages1 = tv_language.getText().toString();

                        Intent intent = new Intent(mContext, JobPreview.class);
                        intent.putExtra("workDays", workDays);
                        intent.putExtra("languages", languages1);
                        intent.putExtra("workShift", workShift);
                        intent.putExtra("perkBenefit", perkBenefit);
                        intent.putExtra("description", description);
                        intent.putExtra("salaryType", salaryType);
                        intent.putExtra("salaryMax", salaryMax);
                        intent.putExtra("salaryMin", salaryMin);
                        intent.putExtra("experience", experience1);
                        intent.putExtra("qualificationName", qualificationName);
                        intent.putExtra("industryName", industryName);
                        intent.putExtra("company", company);
                        intent.putExtra("employmentType", employmentType);
                        intent.putExtra("jobTitle", jobTitle);
                        intent.putExtra("jobLocation", jobLocation);
                        intent.putExtra("numberOpening", numberOpening);
                        intent.putExtra("skillList", mArrayListSkillSelected);
                        intent.putExtra("skillListIds", mArrayListSkillSelectedid);
                        intent.putExtra("isOwn", cb_samecompany.isChecked() ? "Y" : "N");
                        intent.putExtra("isFresher", spn_experience.getSelectedItemPosition()==1 ? "Y" : "N");
                        intent.putExtra("cityId", locationId);
                        intent.putExtra("industryId", industryId);
                        intent.putExtra("employmentId", employmentId);

                        ArrayList<String> degreeIds = new ArrayList<>();
                        ArrayList<String> degreeLevels = new ArrayList<>();
                        for (int a = 0; a < qualification.size(); a++) {
                            degreeLevels.add(degreeLevels.size(), qualification.get(a).getLevel());
                            degreeIds.add(degreeIds.size(), qualification.get(a).getId());
                        }
                        String experienceId = "";
                        for (int i = 0; i < experienceList.size(); i++) {
                            if (spn_experience.getSelectedItem().toString().equalsIgnoreCase(experienceList.get(i).getAsJsonObject().get("experience_name").getAsString())) {
                                experienceId = experienceList.get(i).getAsJsonObject().get("experience_id").getAsString();
                                break;
                            }
                        }
                        String salaryTypeId = "2";
                       /* for (int i = 0; i < salarytypeList.size(); i++) {
                            if (spn_salarytype.getSelectedItem().toString().equalsIgnoreCase(salarytypeList.get(i).getAsJsonObject().get("job_period_name").getAsString())) {
                                salaryTypeId = salarytypeList.get(i).getAsJsonObject().get("job_period_id").getAsString();
                                break;
                            }
                        }*/
                        String workShiftId = "";
                        for (int i = 0; i < working_shifts.size(); i++) {
                            if (spn_workshift.getSelectedItem().toString().equalsIgnoreCase(working_shifts.get(i).getAsJsonObject().get("shift_name").getAsString())) {
                                workShiftId = working_shifts.get(i).getAsJsonObject().get("shift_id").getAsString();
                                break;
                            }
                        }

                        ArrayList<String> perkIds = new ArrayList<>();
                        for (int a = 0; a < perks.size(); a++) {
                            perkIds.add(perkIds.size(), perks.get(a).getId());
                        }


                        String workDayId = "";
                        for (int i = 0; i < working_days.size(); i++) {
                            if (spn_workdays.getSelectedItem().toString().equalsIgnoreCase(working_days.get(i).getAsJsonObject().get("day_name").getAsString())) {
                                workDayId = working_days.get(i).getAsJsonObject().get("day_id").getAsString();
                                break;
                            }
                        }
                        intent.putExtra("workDayId", workDayId);
                        intent.putExtra("workShiftId", workShiftId);
                        intent.putExtra("languageIds", languageIds);
                        intent.putExtra("perkIds", perkIds);
                        intent.putExtra("salaryTypeId", salaryTypeId);
                        intent.putExtra("experienceId", experienceId);
                        intent.putExtra("degreeIds", degreeIds);
                        intent.putExtra("degreeLevels", degreeLevels);
                        mContext.startActivity(intent);
                    }


                }
                break;

            case R.id.tv_back:
                backPressed();
                break;

            case R.id.tv_industry:
                 mContext.startActivity(new Intent(mContext, AddIndustryActivity.class).putExtra("from","industry"));
                break;

            case R.id.tv_employmenttype:
                if (employment_type.size() > 0) {
                    callJobTypeSheet();
                }
                break;
            case R.id.tv_location:
                mContext.startActivity(new Intent(mContext, AddIndustryActivity.class).putExtra("from","location"));
                break;

            case R.id.tv_qualification:
                if (degree_type.size() > 0) {
                    callQualificationSheet();
                }
                break;
            case R.id.tv_perk:
                if (perk_benefits.size() > 0) {
                    callPerkSheet();
                }
                break;
            case R.id.tv_language:
                mContext.startActivity(new Intent(mContext, AddLanguageActivity.class).putStringArrayListExtra("StringArrayList", new ArrayList<>()).putExtra("from","post"));
                break;
            case R.id.closetype:
                bottomsheet_jobType.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.iv_close_qualification:
                bottomsheet_qualification.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.iv_close_perk:
                bottomsheet_perk.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.done_jobtype:
                tv_employmenttype.setText(tempjobTypeName);
                employmentId = tempjobTypeId;
                bottomsheet_jobType.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.btn_done_qualification:
                qualification.clear();
                String name = "";
                for (int i = 0; i < qualificationArraylist.size(); i++) {
                    if (qualificationArraylist.get(i).isSelected()) {
                        qualification.add(qualificationArraylist.get(i));
                        name = name + (name.equalsIgnoreCase("") ? "" : ", ") + qualificationArraylist.get(i).getName();
                    }
                }
                tv_qualification.setText(name);
                bottomsheet_qualification.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
            case R.id.btn_done_perk:
                perks.clear();
                String name1 = "";
                for (int i = 0; i < perkArraylist.size(); i++) {
                    if (perkArraylist.get(i).isSelected()) {
                        perks.add(perkArraylist.get(i));
                        name1 = name1 + (name1.equalsIgnoreCase("") ? "" : ", ") + perkArraylist.get(i).getName();
                    }
                }
                tv_perk.setText(name1);
                bottomsheet_perk.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            default:
                break;
        }

    }

    private void getPostJobStepOne() {
        SwipeeApiClient.swipeeServiceInstance().getPostJobStepOne(Config.GetUserToken()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responceBody = response.body();
                    if (response.body().get("code").getAsInt()==203){
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        getActivity().finish();
                    }else
                    if (responceBody.get("code").getAsInt() == 200 && responceBody.get("status").getAsBoolean()) {
                        JsonObject data = responceBody.has("data") ? responceBody.get("data").getAsJsonObject() : new JsonObject();
                        job_industry = data.has("job_industry") ? data.get("job_industry").getAsJsonArray() : new JsonArray();
                        employment_type = data.has("employment_type") ? data.get("employment_type").getAsJsonArray() : new JsonArray();
/*
                        job_location = data.has("job_location") ? data.get("job_location").getAsJsonArray() : new JsonArray();
*/
                        tv_company.setText(Config.GeCompanyName());
                        tv_industry.setText(Config.GetIndustry());
                        industryId=Config.GetIndustryId();


                        if (job_industry.size() > 0) {
                            commonModels = new ArrayList<>();
                            for (int i = 0; i < job_industry.size(); i++) {
                                JsonObject jsonObject = job_industry.get(i).getAsJsonObject();
                                CommonModel commonModel = new CommonModel(jsonObject.get("industry_id").getAsString(), jsonObject.get("industry_name").getAsString(), false);
                                commonModels.add(commonModels.size(), commonModel);
                            }
                            tv_industry.setOnClickListener(PostJobFragments.this);
                        }

                        String data1=  readFromFile();
                        try {
                            JSONArray jarray = new JSONArray(data1);
                            if (jarray.length()>0){
                                for (int i = 0; i < jarray.length(); i++) {
                                    JSONObject jsonObject= jarray.getJSONObject(i);
                                    String location_id=jsonObject.getString("location_id");
                                    String location_name=jsonObject.getString("location_name");
                                    String state_name=jsonObject.getString("state_name");
                                    CommonModel commonModel = new CommonModel(location_id, location_name, state_name, false);
                                    commonModelsLocation.add(commonModelsLocation.size(), commonModel);
                                }
                                tv_location.setOnClickListener(PostJobFragments.this);

                            }else {
                            }
                        } catch (JSONException e) {
                        }

                        /*if (job_location.size() > 0) {
                             commonModelsLocation = new ArrayList<>();
                            for (int i = 0; i < job_location.size(); i++) {
                                JsonObject jsonObject = job_location.get(i).getAsJsonObject();
                                CommonModel commonModel = new CommonModel(jsonObject.get("location_id").getAsString(), jsonObject.get("location_name").getAsString(), jsonObject.get("state_name").getAsString(), false);
                                commonModelsLocation.add(commonModelsLocation.size(), commonModel);
                            }
                            tv_location.setOnClickListener(PostJobFragments.this);
                        }*/
                        if (employment_type.size() > 0) {
                            ArrayList<CommonModel> commonModels = new ArrayList<>();
                            for (int i = 0; i < employment_type.size(); i++) {
                                JsonObject jsonObject = employment_type.get(i).getAsJsonObject();
                                CommonModel commonModel = new CommonModel(jsonObject.get("job_type_id").getAsString(), jsonObject.get("job_type_name").getAsString(), false);
                                commonModels.add(commonModels.size(), commonModel);
                            }
                            rv_jobtype.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            jobTypeAdapter = new JobPostJobTypeAdapter(mContext, commonModels);
                            rv_jobtype.setAdapter(jobTypeAdapter);
                            rv_jobtype.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
                            tv_employmenttype.setOnClickListener(PostJobFragments.this);
                        }

                    } else {
                        rest.showToast(responceBody.get("message").getAsString());
                    }

                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();

            }
        });
    }
    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = mContext.openFileInput("location.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
            return ret;
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
            return ret;
        }

        return ret;
    }
    private void getPostJobStepTwo() {
        SwipeeApiClient.swipeeServiceInstance().getPostJobStepTwo(Config.GetUserToken()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responceBody = response.body();
                    if (response.body().get("code").getAsInt()==203){
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        getActivity().finish();
                    }else if (responceBody.get("code").getAsInt() == 200 && responceBody.get("status").getAsBoolean()) {
                        JsonObject data = responceBody.has("data") ? responceBody.get("data").getAsJsonObject() : new JsonObject();
                        job_opening_numbers = data.has("job_opening_numbers") ? data.get("job_opening_numbers").getAsJsonArray() : new JsonArray();
                        degree_level = data.has("degree_level") ? data.get("degree_level").getAsJsonArray() : new JsonArray();
                        degree_type = data.has("degree_type") ? data.get("degree_type").getAsJsonArray() : new JsonArray();
                        experience = data.has("experience") ? data.get("experience").getAsJsonArray() : new JsonArray();
                        salary_offer = data.has("salary_offer") ? data.get("salary_offer").getAsJsonArray() : new JsonArray();
                        salary_type = data.has("salary_type") ? data.get("salary_type").getAsJsonArray() : new JsonArray();

                        if (job_opening_numbers.size() > 0) {
                            ArrayList<String> openningArray = new ArrayList<>();
                            for (int i = 0; i < job_opening_numbers.size(); i++) {
                                openningArray.add(openningArray.size(), job_opening_numbers.get(i).getAsString());
                            }
                            openningArray.add(0, "Number of openings");
                            ArrayAdapter<String> openingAdapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item, openningArray);
                            openingAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            spn_openning.setAdapter(openingAdapter);
                        }
                        if (experience.size() > 0) {
                            ArrayList<String> experienceArray = new ArrayList<>();
                            experienceList.clear();
                            for (int i = 0; i < experience.size(); i++) {
                                JsonObject openings = experience.get(i).getAsJsonObject();
                                experienceArray.add(experienceArray.size(), openings.get("experience_name").getAsString());
                                experienceList.add(experienceList.size(), openings);
                            }
                            experienceArray.add(0, "Work Experience");
                            ArrayAdapter<String> openingAdapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item, experienceArray);
                            openingAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            spn_experience.setAdapter(openingAdapter);
                        }
                        /*if (salary_type.size() > 0) {
                            ArrayList<String> salaryArray = new ArrayList<>();
                            salarytypeList.clear();
                            for (int i = 0; i < salary_type.size(); i++) {
                                JsonObject openings = salary_type.get(i).getAsJsonObject();
                                salaryArray.add(salaryArray.size(), openings.get("job_period_name").getAsString());
                                salarytypeList.add(salarytypeList.size(), openings);
                            }
                            salaryArray.add(0, "Salary Types");
                            ArrayAdapter<String> openingAdapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item, salaryArray);
                            openingAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            spn_salarytype.setAdapter(openingAdapter);
                        }*/
                        if (salary_offer.size() > 0) {
                           salaryminArray = new ArrayList<>();
                            salarymaxArray = new ArrayList<>();
                            salaryofferList.clear();
                            for (int i = 0; i < salary_offer.size(); i++) {
                                JsonObject openings = salary_offer.get(i).getAsJsonObject();
                                salaryminArray.add(salaryminArray.size(), String.valueOf(openings.get("min_salary").getAsInt()));
                                salaryminArray.add(salaryminArray.size(), String.valueOf(openings.get("max_salary").getAsInt()));
                                salaryofferList.add(salaryofferList.size(), openings);
                            }
                            salarymaxArray.addAll(salaryminArray);
                            salarymaxArray.remove(0);
                            salaryminArray.remove(salaryminArray.size()-1);
                            salaryminArray.add(0, "Min Salary");
                            salarymaxArray.add(0, "Max Salary");
                            ArrayAdapter<String> openingAdapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item, salaryminArray);
                            openingAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            spn_minsalary.setAdapter(openingAdapter);
                            ArrayAdapter<String> maxAdapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item, salarymaxArray);
                            maxAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            spn_maxsalary.setAdapter(maxAdapter);
                        }

                        if (degree_type.size() > 0) {
                            ArrayList<CommonModel> commonModels = new ArrayList<>();
                            for (int i = 0; i < degree_type.size(); i++) {
                                JsonObject jsonObject = degree_type.get(i).getAsJsonObject();
                                CommonModel commonModel = new CommonModel(jsonObject.get("degree_id").getAsString(), jsonObject.get("degree_name").getAsString(), jsonObject.get("degree_level_id").getAsString(), false);
                                commonModels.add(commonModels.size(), commonModel);
                            }
                            qualificationAdapter = new QualificationAdapter(mContext, commonModels);
                            list_qualification.setAdapter(qualificationAdapter);
                            qualificationAdapter.notifyDataSetChanged();
                            tv_qualification.setOnClickListener(PostJobFragments.this);
                        }


                    } else {
                        rest.showToast(responceBody.get("message").getAsString());
                    }

                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();

            }
        });
    }

    private void getPostJobStepFive() {
        SwipeeApiClient.swipeeServiceInstance().getPostJobStepFive(Config.GetUserToken()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responceBody = response.body();
                    if (response.body().get("code").getAsInt()==203){
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        getActivity().finish();
                    }else
                    if (responceBody.get("code").getAsInt() == 200 && responceBody.get("status").getAsBoolean()) {
                        JsonObject data = responceBody.has("data") ? responceBody.get("data").getAsJsonObject() : new JsonObject();
                        perk_benefits = data.has("perk_benefits") ? data.get("perk_benefits").getAsJsonArray() : new JsonArray();
                        working_shifts = data.has("working_shifts") ? data.get("working_shifts").getAsJsonArray() : new JsonArray();
                        working_days = data.has("working_days") ? data.get("working_days").getAsJsonArray() : new JsonArray();
                        languagesArray = data.has("languages") ? data.get("languages").getAsJsonArray() : new JsonArray();
                        featured_package = data.has("featured_package") ? data.get("featured_package").getAsJsonObject() : new JsonObject();
                        if (perk_benefits.size() > 0) {
                            ArrayList<CommonModel> commonModels = new ArrayList<>();
                            for (int i = 0; i < perk_benefits.size(); i++) {
                                JsonObject jsonObject = perk_benefits.get(i).getAsJsonObject();
                                CommonModel commonModel = new CommonModel(jsonObject.get("benefit_id").getAsString(), jsonObject.get("benefit_name").getAsString(), false);
                                commonModels.add(commonModels.size(), commonModel);
                            }
                            perkAdapter = new PerkAdapter(mContext, commonModels);
                            list_perk.setAdapter(perkAdapter);
                            perkAdapter.notifyDataSetChanged();
                            tv_perk.setOnClickListener(PostJobFragments.this);
                        }

                        if (languagesArray.size() > 0) {
                            commonModelsLanguage = new ArrayList<>();
                            for (int i = 0; i < languagesArray.size(); i++) {
                                JsonObject jsonObject = languagesArray.get(i).getAsJsonObject();
                                CommonModel commonModel = new CommonModel(jsonObject.get("language_id").getAsString(), jsonObject.get("language_name").getAsString(), false);
                                commonModelsLanguage.add(commonModelsLanguage.size(), commonModel);
                            }
                            tv_language.setOnClickListener(PostJobFragments.this);
                        }

                        if (working_days.size() > 0) {
                            ArrayList<String> workdayArray = new ArrayList<>();
                            for (int i = 0; i < working_days.size(); i++) {
                                JsonObject openings = working_days.get(i).getAsJsonObject();
                                workdayArray.add(workdayArray.size(), openings.get("day_name").getAsString());
                            }
                            workdayArray.add(0, "Working Days");
                            ArrayAdapter<String> openingAdapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item, workdayArray);
                            openingAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            spn_workdays.setAdapter(openingAdapter);
                        }

                        if (working_shifts.size() > 0) {
                            ArrayList<String> workshiftArray = new ArrayList<>();
                            for (int i = 0; i < working_shifts.size(); i++) {
                                JsonObject openings = working_shifts.get(i).getAsJsonObject();
                                workshiftArray.add(workshiftArray.size(), openings.get("shift_name").getAsString());
                            }
                            workshiftArray.add(0, "Working Shift");
                            ArrayAdapter<String> openingAdapter = new ArrayAdapter<String>(mContext, R.layout.simple_spinner_item, workshiftArray);
                            openingAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                            spn_workshift.setAdapter(openingAdapter);
                        }

                    } else {
                        rest.showToast(responceBody.get("message").getAsString());
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
        bottomsheet_jobType.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomsheet_jobType.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomsheet_jobType.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });


    }


    private void callQualificationSheet() {

        bottomsheet_qualification.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomsheet_qualification.setDraggable(false);
        bottomsheet_qualification.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomsheet_qualification.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

    }

    private void callPerkSheet() {

        bottomsheet_perk.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomsheet_perk.setDraggable(false);
        bottomsheet_perk.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomsheet_perk.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });

    }



    private void callSkillList() {
        SwipeeApiClient.swipeeServiceInstance().getSkill().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();

                    JsonArray mArrayListData = responseBody.has("data")?responseBody.get("data").getAsJsonArray():new JsonArray();
                    mArrayListSkills.clear();
                    for (int i = 0; i < mArrayListData.size(); i++) {
                        AddSkillsModel model = new AddSkillsModel();
                        model.setSkill_id(mArrayListData.get(i).getAsJsonObject().get("skill_id").getAsString());
                        model.setSkill_name(mArrayListData.get(i).getAsJsonObject().get("skill_name").getAsString());
                        model.setSelected(false);
                        mArrayListSkills.add(model);
                    }
                    skilladapter = new AddSkillAdapter(mContext, mArrayListSkills, addSkillsInterface);
                    mlistview.setAdapter(skilladapter);
                } else {
                    rest.showToast("Something went wrong");
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });
    }

    @Override
    public void skill(String data, String id) {
        {
            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout linearLayoutF = new LinearLayout(mContext);
            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                    FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams layoutParamsF = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, (int) (mContext.getResources().getDisplayMetrics().density*32));
            layoutParamsF.setMargins((int) (mContext.getResources().getDisplayMetrics().density*4), (int) (mContext.getResources().getDisplayMetrics().density*5), (int) (mContext.getResources().getDisplayMetrics().density*4), (int) (mContext.getResources().getDisplayMetrics().density*5));
            linearLayoutF.setLayoutParams(layoutParams);
            linearLayout.setLayoutParams(layoutParamsF);
            linearLayout.setPadding((int) (mContext.getResources().getDisplayMetrics().density*4), 0, (int) (mContext.getResources().getDisplayMetrics().density*4), 0);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutF.setOnClickListener(btnClickListener);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setBackgroundResource(R.drawable.primary_semiround_bg);
            PopinsRegularTextView bt = new PopinsRegularTextView(mContext);
            bt.setText(data);
            bt.setAllCaps(false);
            bt.setTag(data);
            bt.setMaxLines(1);
            bt.setEllipsize(TextUtils.TruncateAt.END);
            bt.setTextSize(12f);
            bt.setTextColor(getResources().getColor(R.color.white));
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins((int) (mContext.getResources().getDisplayMetrics().density*6), 0, (int) (mContext.getResources().getDisplayMetrics().density*32), 0);
            bt.setLayoutParams(layoutParams1);
            ImageView imageView = new ImageView(mContext);
            imageView.setImageResource(R.drawable.ic_group_99);
            LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams2.setMargins(-(int) (mContext.getResources().getDisplayMetrics().density*26), 0, 0, 0);
            imageView.setLayoutParams(layoutParams2);
            imageView.getLayoutParams().height = (int) (mContext.getResources().getDisplayMetrics().density*22);
            imageView.getLayoutParams().width = (int) (mContext.getResources().getDisplayMetrics().density*22);


            linearLayout.addView(bt);
            linearLayout.addView(imageView);
            linearLayoutF.addView(linearLayout);
            linearLayoutF.setOnClickListener(btnClickListener);
            linearLayoutF.setTag(data);
            flowlayout.addView(linearLayoutF);
            mArrayListSkillSelected.add(data);
            mArrayListSkillSelectedid.add(id);
            if (flowlayout.getMeasuredHeight() > 272) {
                kdkdkdkd.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 272));
            } else
                kdkdkdkd.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }
    }

    public void setIndustry(String name, String id) {
        tv_industry.setText(name);
        industryId = id;
        for (int i=0;i<commonModels.size();i++){
            if (id.equalsIgnoreCase(commonModels.get(i).getId())){
                commonModels.get(i).setSelected(true);
            }else commonModels.get(i).setSelected(false);
        }
    }

    public void setLocation(String name, String id) {
        tv_location.setText(name);
        locationId = id;
        for (int i=0;i<commonModelsLocation.size();i++){
            if (id.equalsIgnoreCase(commonModelsLocation.get(i).getId())){
                commonModelsLocation.get(i).setSelected(true);
            }else commonModelsLocation.get(i).setSelected(false);
        }
    }

    public void setLanguage(ArrayList<String> langid, ArrayList<String> langname) {
        String name2 = "";
        for (int i = 0; i < langname.size(); i++) {

                name2 = name2 + (name2.equalsIgnoreCase("") ? "" : ", ") +langname.get(i);

        }
        languageIds=langid;
        tv_language.setText(name2);
    }



    public static class JobPostJobTypeAdapter extends RecyclerView.Adapter<JobPostJobTypeAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<CommonModel> data;
        int oldPos;
        private JobPostJobTypeAdapter.MyViewHolder oldHolder;


        public JobPostJobTypeAdapter(Context mContext, ArrayList<CommonModel> data) {

            // TODO Auto-generated constructor stub

            this.mContext = mContext;
            this.data = data;


        }


        @NonNull
        @Override
        public JobPostJobTypeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.child_state, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull JobPostJobTypeAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

            String name = data.get(position).getName();
            String id = data.get(position).getId();
            holder.radioButton.setClickable(false);
            if (data.get(position).isSelected()) {
                holder.radioButton.setChecked(true);
            } else holder.radioButton.setChecked(false);
            holder.tv_item.setText(name);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (oldHolder != null) {
                        oldHolder.radioButton.setChecked(false);
                        data.get(oldPos).setSelected(false);
                    }
                    holder.radioButton.setChecked(true);
                    oldHolder = holder;
                    oldPos = position;
                    tempjobTypeId = id;
                    tempjobTypeName = name;
                    data.get(position).setSelected(true);
                }
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

        public class MyViewHolder extends RecyclerView.ViewHolder {

            PopinsRegularTextView tv_item;
            RadioButton radioButton;

            public MyViewHolder(View view) {
                super(view);

                tv_item = view.findViewById(R.id.tv_item);
                radioButton = view.findViewById(R.id.radioButton);

            }
        }
    }


    public class QualificationAdapter extends BaseAdapter {

        Activity mContext;


        public QualificationAdapter(Context mContext, ArrayList<CommonModel> languageArraylist1) {

            // TODO Auto-generated constructor stub

            this.mContext = (Activity) mContext;
            qualificationArraylist = languageArraylist1;

        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return qualificationArraylist.size();
        }

        @Override
        public Object getItem(int pos) {
            // TODO Auto-generated method stub
            return qualificationArraylist.get(pos);
        }

        @Override
        public long getItemId(int pos) {
            // TODO Auto-generated method stub
            return qualificationArraylist.indexOf(getItem(pos));
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
            nameTxt.setText(qualificationArraylist.get(pos).getName());
            mCheckImgView.setChecked(qualificationArraylist.get(pos).isSelected());

            mCheckImgView.setOnClickListener(view -> {

                if (!qualificationArraylist.get(pos).isSelected()) {
                    qualificationArraylist.get(pos).setSelected(true);
                } else {
                    qualificationArraylist.get(pos).setSelected(false);
                }
                notifyDataSetChanged();
            });
            nameTxt.setOnClickListener(view -> {

                if (!qualificationArraylist.get(pos).isSelected()) {
                    qualificationArraylist.get(pos).setSelected(true);
                } else {
                    qualificationArraylist.get(pos).setSelected(false);
                }
                notifyDataSetChanged();
            });
            return convertView;
        }


    }

    public class PerkAdapter extends BaseAdapter {

        Activity mContext;


        public PerkAdapter(Context mContext, ArrayList<CommonModel> languageArraylist1) {

            // TODO Auto-generated constructor stub

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

                if (!perkArraylist.get(pos).isSelected()) {
                    perkArraylist.get(pos).setSelected(true);
                } else {
                    perkArraylist.get(pos).setSelected(false);
                }
                notifyDataSetChanged();
            });
            nameTxt.setOnClickListener(view -> {

                if (!perkArraylist.get(pos).isSelected()) {
                    perkArraylist.get(pos).setSelected(true);
                } else {
                    perkArraylist.get(pos).setSelected(false);
                }
                notifyDataSetChanged();
            });
            return convertView;
        }
    }



}
