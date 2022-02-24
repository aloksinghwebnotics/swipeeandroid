package com.webnotics.swipee.fragments.seeker;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.potyvideo.library.AndExoPlayerView;
import com.webnotics.swipee.CustomUi.FlowLayout;
import com.webnotics.swipee.CustomUi.NestedListView;
import com.webnotics.swipee.CustomUi.PopinsRegularTextView;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.Seeker.AddCarrierObjectiveActivity;
import com.webnotics.swipee.activity.Seeker.AddEducation;
import com.webnotics.swipee.activity.Seeker.AddJobPreferences;
import com.webnotics.swipee.activity.Seeker.AddLanguageActivity;
import com.webnotics.swipee.activity.Seeker.AddSkillActivity;
import com.webnotics.swipee.activity.Seeker.EditProfileActivity;
import com.webnotics.swipee.activity.Seeker.SeekerHomeActivity;
import com.webnotics.swipee.activity.Seeker.WorkExperience;
import com.webnotics.swipee.adapter.seeeker.ExperienceAdapter;
import com.webnotics.swipee.adapter.seeeker.LangAdapter;
import com.webnotics.swipee.adapter.seeeker.UserEducationAdapter;
import com.webnotics.swipee.adapter.seeeker.UserPreferenceAdapter;
import com.webnotics.swipee.fragments.Basefragment;
import com.webnotics.swipee.model.seeker.EmployeeUserDetails;
import com.webnotics.swipee.model.seeker.JobTypeModel;
import com.webnotics.swipee.model.seeker.LangModel;
import com.webnotics.swipee.rest.ApiUrls;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragments extends Basefragment implements View.OnClickListener {


    private static final int DOCUMENT_FILE_REQUEST = 453;
    ImageView mImageViewemptypeclose, iv_add_objective, iv_show_objective, iv_add_jobtype, iv_show_jobtype, iv_add_keyskill, iv_show_keyskill, iv_add_experience, iv_show_experience,
            iv_add_education, iv_show_education, iv_add_preferences, iv_show_preferences, iv_add_languages, iv_show_languages, iv_add_resume, iv_show_resume;
    RelativeLayout  employeetypebottom_sheet, rl_objective, rl_jobtype, rl_keyskill, rl_experience, rl_education, rl_preferences, rl_languages, rl_resume;
    Rest rest;
    Context mContext;
    public TextView tv_your_name, tv_resume, tv_your_email, tv_your_mobile, tv_objective_value, tv_edit,tv_video;
    CircleImageView civ_profile;
    FlowLayout flow_keyskill, flow_jobtype, flow_languages;
    private ArrayList<EmployeeUserDetails.Data.User_Key_Skills> mArrayListUserSkills = new ArrayList<>();
    private ArrayList<EmployeeUserDetails.Data.UserWorkExperience> mArrayuserworkexperience = new ArrayList<>();
    private ArrayList<EmployeeUserDetails.Data.User_Eductaion> mArrayuserusereducation = new ArrayList<>();
    private ArrayList<EmployeeUserDetails.Data.User_Preferences> mArrayuseruserpreference = new ArrayList<>();
    private NestedListView list_experience, list_education, list_preferences;
    private ArrayList<EmployeeUserDetails.Data.User_Languages> mArrayUserListLang = new ArrayList<>();
    private ArrayList<EmployeeUserDetails.Data.User_Job_Type> mArrayUserJobType = new ArrayList<>();
    ArrayList<JobTypeModel.Data> mArrayListJobType = new ArrayList<>();
    ArrayList<LangModel> mArrayListjbtype = new ArrayList<>();
    BottomSheetBehavior  bottomsheetemployeetype;
    private ListView  listviewemptype;
    private boolean isHit = true;
    private String objective = "";
    private final ArrayList<String> skillIdList = new ArrayList<>();
    private String prefId = "";
    private String pickUri="";
    @SuppressLint("StaticFieldLeak")
    public static  ProfileFragments instance;
    private final ArrayList<String> stringLangIds = new ArrayList<>();
    RelativeLayout rl_video;
    private String videoUrl="";
    private AndExoPlayerView vv_video;
    ImageView iv_video,iv_videoplay;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_screen, container, false);
        mContext = getActivity();
        rest = new Rest(mContext);
        instance=this;
        iv_add_objective = rootView.findViewById(R.id.iv_add_objective);
        iv_show_objective = rootView.findViewById(R.id.iv_show_objective);
        iv_add_jobtype = rootView.findViewById(R.id.iv_add_jobtype);
        iv_show_jobtype = rootView.findViewById(R.id.iv_show_jobtype);
        iv_add_keyskill = rootView.findViewById(R.id.iv_add_keyskill);
        iv_show_keyskill = rootView.findViewById(R.id.iv_show_keyskill);
        iv_add_experience = rootView.findViewById(R.id.iv_add_experience);
        iv_show_experience = rootView.findViewById(R.id.iv_show_experience);
        iv_add_education = rootView.findViewById(R.id.iv_add_education);
        iv_show_education = rootView.findViewById(R.id.iv_show_education);
        iv_add_preferences = rootView.findViewById(R.id.iv_add_preferences);
        iv_show_preferences = rootView.findViewById(R.id.iv_show_preferences);
        iv_add_languages = rootView.findViewById(R.id.iv_add_languages);
        iv_show_languages = rootView.findViewById(R.id.iv_show_languages);
        iv_show_resume = rootView.findViewById(R.id.iv_show_resume);
        iv_add_resume = rootView.findViewById(R.id.iv_add_resume);

        tv_video = rootView.findViewById(R.id.tv_video);
        rl_video = rootView.findViewById(R.id.rl_video);
        vv_video = rootView.findViewById(R.id.vv_video);
        iv_video = rootView.findViewById(R.id.iv_video);
        iv_videoplay = rootView.findViewById(R.id.iv_videoplay);

        rl_objective = rootView.findViewById(R.id.rl_objective);
        rl_jobtype = rootView.findViewById(R.id.rl_jobtype);
        rl_keyskill = rootView.findViewById(R.id.rl_keyskill);
        rl_experience = rootView.findViewById(R.id.rl_experience);
        rl_education = rootView.findViewById(R.id.rl_education);
        rl_preferences = rootView.findViewById(R.id.rl_preferences);
        rl_languages = rootView.findViewById(R.id.rl_languages);
        rl_resume = rootView.findViewById(R.id.rl_resume);

        tv_your_name = rootView.findViewById(R.id.tv_your_name);
        civ_profile = rootView.findViewById(R.id.civ_profile);
        tv_your_mobile = rootView.findViewById(R.id.tv_your_mobile);
        tv_your_email = rootView.findViewById(R.id.tv_your_email);
        flow_keyskill = rootView.findViewById(R.id.flow_keyskill);
        list_experience = rootView.findViewById(R.id.list_experience);
        list_education = rootView.findViewById(R.id.list_education);
        list_preferences = rootView.findViewById(R.id.list_preferences);
        flow_jobtype = rootView.findViewById(R.id.flow_jobtype);
        flow_languages = rootView.findViewById(R.id.flow_languages);
        tv_objective_value = rootView.findViewById(R.id.tv_objective_value);
        tv_resume = rootView.findViewById(R.id.tv_resume);
        tv_edit = rootView.findViewById(R.id.tv_edit);

        employeetypebottom_sheet = rootView.findViewById(R.id.employeetypebottom_sheet);
        bottomsheetemployeetype = BottomSheetBehavior.from(employeetypebottom_sheet);
        mImageViewemptypeclose = employeetypebottom_sheet.findViewById(R.id.closetype);
        Button doneeeeetype = employeetypebottom_sheet.findViewById(R.id.donetype);
        listviewemptype = employeetypebottom_sheet.findViewById(R.id.listviewemptype);

        setVisibilityGone();
        iv_show_objective.setOnClickListener(this);
        iv_show_jobtype.setOnClickListener(this);
        iv_show_keyskill.setOnClickListener(this);
        iv_show_experience.setOnClickListener(this);
        iv_show_education.setOnClickListener(this);
        iv_show_preferences.setOnClickListener(this);
        iv_show_languages.setOnClickListener(this);
        iv_show_resume.setOnClickListener(this);

        iv_add_objective.setOnClickListener(this);
        iv_add_jobtype.setOnClickListener(this);
        iv_add_keyskill.setOnClickListener(this);
        iv_add_experience.setOnClickListener(this);
        iv_add_education.setOnClickListener(this);
        iv_add_preferences.setOnClickListener(this);
        iv_add_languages.setOnClickListener(this);
        iv_add_resume.setOnClickListener(this);
        tv_edit.setOnClickListener(this);

        doneeeeetype.setOnClickListener(this);
        mImageViewemptypeclose.setOnClickListener(this);
        tv_resume.setOnClickListener(this);

        civ_profile.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(pickUri))
                AppController.callFullImage(mContext,pickUri);
        });
        iv_videoplay.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(videoUrl))
                callPlayer(videoUrl);
              //  VideoPlayerActivity.start(getActivity(),videoUrl);
        });

        if (SeekerHomeActivity.instance!=null){
            if (SeekerHomeActivity.instance.employeeuserdetail!=null){
                EmployeeUserDetails employeeuserdetail=SeekerHomeActivity.instance.employeeuserdetail;
                if (employeeuserdetail.isStatus()){
                     setDataResponce(employeeuserdetail);
                }else {
                    hitService();
                }
            }else  hitService();
        }else {
            hitService();
        }

        return rootView;
    }
    private void callPlayer(String url){
        vv_video.setSource(url);
        vv_video.setVisibility(View.VISIBLE);
        rl_video.setVisibility(View.GONE);
    }
    private void setDataResponce(EmployeeUserDetails employeeuserdetail1) {
        mArrayListUserSkills.clear();
        mArrayuserworkexperience.clear();
        mArrayuserusereducation.clear();
        mArrayuseruserpreference.clear();
        mArrayUserListLang.clear();
        mArrayUserJobType.clear();

        EmployeeUserDetails employeeuserdetail = employeeuserdetail1;
        if (employeeuserdetail.getCode() == 200 && employeeuserdetail.getData().getUser_profile_data() != null) {
            vv_video.setVisibility(View.GONE);
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
            Config.SetEmailVERIFY(true);
            Config.SetMobileVERIFY(true);
            Config.SetIsUserLogin(true);
            pickUri=employeeuserdetail.getData().getUser_profile_data().getUser_profile();
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
            if (employeeuserdetail.getData().getUser_profile_data().getMiddle_name().length() != 0) {
                tv_your_name.setText(MessageFormat.format("{0} {1} {2}", employeeuserdetail.getData().getUser_profile_data().getFirst_name(), employeeuserdetail.getData().getUser_profile_data().getMiddle_name(), employeeuserdetail.getData().getUser_profile_data().getLast_name()));
            } else {
                tv_your_name.setText(MessageFormat.format("{0} {1}", employeeuserdetail.getData().getUser_profile_data().getFirst_name(), employeeuserdetail.getData().getUser_profile_data().getLast_name()));
            }
            Glide.with(mContext)
                    .load(employeeuserdetail.getData().getUser_profile_data().getUser_profile())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_profile_select).error(R.drawable.ic_profile_select))
                    .into(civ_profile);

            tv_your_mobile.setText(MessageFormat.format("{0} {1}", employeeuserdetail.getData().getUser_profile_data().getPhone_code(), employeeuserdetail.getData().getUser_profile_data().getMobile_no()));
            tv_your_email.setText(employeeuserdetail.getData().getUser_profile_data().getEmail());
            videoUrl=employeeuserdetail.getData().getUser_profile_data().getUser_video();
            if (!TextUtils.isEmpty(employeeuserdetail.getData().getUser_profile_data().getUser_video())){
                Glide.with(mContext)
                        .load(employeeuserdetail.getData().getUser_profile_data().getUser_video())
                        .transform(new MultiTransformation(new CenterCrop(),new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density*16))))
                        .into(iv_video);
                rl_video.setVisibility(View.VISIBLE);
                tv_video.setVisibility(View.VISIBLE);
            }else {
                tv_video.setVisibility(View.GONE);
                rl_video.setVisibility(View.GONE);

            }
        }
        else {
            rest.showToast(employeeuserdetail.getMessage());
        }
        mArrayListUserSkills = employeeuserdetail.getData().getUser_key_skills();
        if (mArrayListUserSkills != null) {
            if (mArrayListUserSkills.size() == 0) {

            } else {
                setSkillFlow(mArrayListUserSkills);
            }
        }

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
                UserPreferenceAdapter adapter = new UserPreferenceAdapter(mContext, mArrayuseruserpreference);
                list_preferences.setAdapter(adapter);
                prefId = mArrayuseruserpreference.get(0).getPreference_id();
            }
        }

        mArrayUserListLang = employeeuserdetail.getData().getUser_languages();
        setLanguageFlow(mArrayUserListLang);

        mArrayUserJobType.clear();
        mArrayUserJobType = employeeuserdetail.getData().getUser_job_type();
        setJobtypeFlow(mArrayUserJobType);


        if (employeeuserdetail.getData().getUser_carrier_objective() != null) {
            if (employeeuserdetail.getData().getUser_carrier_objective().length() != 0) {
                objective = employeeuserdetail.getData().getUser_carrier_objective();
                tv_objective_value.setText(employeeuserdetail.getData().getUser_carrier_objective());
            }
        }
        if (employeeuserdetail.getData().getUser_resumes() != null) {
            if (employeeuserdetail.getData().getUser_resumes().getCv_file() != null) {
                Config.SetCVID(employeeuserdetail.getData().getUser_resumes().getCv_id());
                Config.SetCVTITLE(employeeuserdetail.getData().getUser_resumes().getCv_title());
                Config.SetCVFILE(employeeuserdetail.getData().getUser_resumes().getCv_file());
                Config.SetCVFILELINK(employeeuserdetail.getData().getUser_resumes().getCv_file_link());
            }
            tv_resume.setText(employeeuserdetail.getData().getUser_resumes().getCv_file());
        }
    }

    private  void hitService(){
        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            getProfileData();
        } else rest.AlertForInternet();
    }

    @Override
    public void onPause() {
        if (vv_video!=null){
            vv_video.pausePlayer();
        }
        super.onPause();
    }

    private void setVisibilityGone() {
        rl_objective.setVisibility(View.GONE);
        rl_jobtype.setVisibility(View.GONE);
        rl_keyskill.setVisibility(View.GONE);
        rl_experience.setVisibility(View.GONE);
        rl_education.setVisibility(View.GONE);
        rl_preferences.setVisibility(View.GONE);
        rl_languages.setVisibility(View.GONE);
        rl_resume.setVisibility(View.GONE);

        iv_show_objective.setRotation(90);
        iv_show_jobtype.setRotation(90);
        iv_show_keyskill.setRotation(90);
        iv_show_experience.setRotation(90);
        iv_show_education.setRotation(90);
        iv_show_preferences.setRotation(90);
        iv_show_languages.setRotation(90);
        iv_show_resume.setRotation(90);
    }

    @Override
    public int setContentView() {
        return R.layout.profile_screen;
    }

    @Override
    protected void backPressed() {
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.iv_show_objective:

                if (rl_objective.getVisibility() == View.VISIBLE) {
                    setVisibilityGone();
                } else {
                    setVisibilityGone();
                    iv_show_objective.setRotation(270);
                    rl_objective.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.iv_show_jobtype:

                if (rl_jobtype.getVisibility() == View.VISIBLE) {
                    setVisibilityGone();
                } else {
                    setVisibilityGone();
                    iv_show_jobtype.setRotation(270);
                    rl_jobtype.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.iv_show_keyskill:

                if (rl_keyskill.getVisibility() == View.VISIBLE) {
                    setVisibilityGone();
                } else {
                    setVisibilityGone();
                    iv_show_keyskill.setRotation(270);
                    rl_keyskill.setVisibility(View.VISIBLE);
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
            case R.id.iv_show_resume:

                if (rl_resume.getVisibility() == View.VISIBLE) {
                    setVisibilityGone();
                } else {
                    setVisibilityGone();
                    iv_show_resume.setRotation(270);
                    rl_resume.setVisibility(View.VISIBLE);
                }
                break;


            case R.id.iv_add_objective:
                mContext.startActivity(new Intent(mContext, AddCarrierObjectiveActivity.class).putExtra("data", objective));

                break;

            case R.id.iv_add_jobtype:
                if (mArrayListJobType.isEmpty()) {
                    if (rest.isInterentAvaliable()) {
                        AppController.ShowDialogue("", mContext);
                        callServiceJobType();
                    } else rest.AlertForInternet();
                } else {
                    callJobTypeSheet();
                }

                break;

            case R.id.iv_add_keyskill:
                mContext.startActivity(new Intent(mContext, AddSkillActivity.class).putStringArrayListExtra("StringArrayList", skillIdList));
                break;

            case R.id.iv_add_experience:
                mContext.startActivity(new Intent(mContext, WorkExperience.class));
                break;

            case R.id.iv_add_education:
                mContext.startActivity(new Intent(mContext, AddEducation.class));
                break;

            case R.id.iv_add_preferences:
                mContext.startActivity(new Intent(mContext, AddJobPreferences.class).putExtra("prefId", prefId));
                break;

            case R.id.iv_add_languages:
                mContext.startActivity(new Intent(mContext, AddLanguageActivity.class).putStringArrayListExtra("StringArrayList", stringLangIds).putExtra("from",""));
                break;

            case R.id.iv_add_resume:
                isHit = false;
                String[] supportedMimeTypes = {"application/pdf"};
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType(supportedMimeTypes[0]);
                intent.putExtra(Intent.EXTRA_MIME_TYPES, supportedMimeTypes);
                startActivityForResult(intent, DOCUMENT_FILE_REQUEST);

                break;
            case R.id.tv_edit:
                mContext.startActivity(new Intent(mContext, EditProfileActivity.class).putExtra("videoUrl",videoUrl));
                break;

            case R.id.closetype:
                bottomsheetemployeetype.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;

            case R.id.tv_resume:
                        /* AppController.ShowDialogue("",mContext);
                    new RetrivePDFfromUrl().execute( Config.GetCVFILELINK());*/
                AppController.callResume(mContext,Config.GetCVFILELINK());
                break;

            case R.id.donetype:
                ArrayList<String> ids1 = new ArrayList<>();
                ArrayList<EmployeeUserDetails.Data.User_Job_Type> mArrayUserJobTypetemp= new ArrayList<>();
                for (int i = 0; i < mArrayListjbtype.size(); i++) {
                    if (mArrayListjbtype.get(i).isSelected()) {
                        ids1.add(mArrayListjbtype.get(i).getId());
                        EmployeeUserDetails.Data.User_Job_Type modil= new EmployeeUserDetails.Data.User_Job_Type(mArrayListjbtype.get(i).getId(),mArrayListjbtype.get(i).getName());
                        mArrayUserJobTypetemp.add(mArrayUserJobTypetemp.size(),modil);
                    }
                }
                if (mArrayUserJobTypetemp.size()>0){
                    mArrayUserJobType.clear();
                    mArrayUserJobType=mArrayUserJobTypetemp;
                    setJobtypeFlow(mArrayUserJobType);
                    saveJobType(ids1.toString());
                    bottomsheetemployeetype.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }else rest.showToast("Select at least 1 job type.");

                break;

            default:
                break;
        }


    }



    private void callServiceJobType() {
        SwipeeApiClient.swipeeServiceInstance().getJobType(Config.GetUserToken()).enqueue(new Callback<JobTypeModel>() {
            @Override
            public void onResponse(@NonNull Call<JobTypeModel> call, @NonNull Response<JobTypeModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200) {
                    JobTypeModel langModel = response.body();
                    if (langModel != null)
                        if (langModel.isStatus() && langModel.getCode()==200) {
                            mArrayListJobType.clear();
                            mArrayListjbtype.clear();
                            mArrayListJobType = langModel.getData();
                            callJobTypeSheet();

                        }else if (langModel.getCode()==203){
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
    public void onResume() {
        if (isHit)
         getProfileData();


        super.onResume();
    }

    private void getProfileData() {
        isHit=false;
        SwipeeApiClient.swipeeServiceInstance().userdetail(Config.GetUserToken()).enqueue(new Callback<EmployeeUserDetails>() {
            @Override
            public void onResponse(@NonNull Call<EmployeeUserDetails> call, @NonNull Response<EmployeeUserDetails> response) {
                AppController.dismissProgressdialog();
                isHit=true;
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getCode()==200){
                        if (response.body().isStatus()){
                        if (SeekerHomeActivity.instance!=null)
                            SeekerHomeActivity.instance.employeeuserdetail=response.body();
                       setDataResponce(response.body());
                    }else {
                        rest.showToast(response.body().getMessage());
                    }
                    }else if (response.body().getCode()==203){
                        rest.showToast(response.body().getMessage());
                        AppController.loggedOut(mContext);
                    }

                } else {
                    rest.showToast("Something went wrong");
                }

            }

            @Override
            public void onFailure(@NonNull Call<EmployeeUserDetails> call, @NonNull Throwable t) {
                isHit=true;
                AppController.dismissProgressdialog();
            }
        });
    }

    private void setSkillFlow(ArrayList<EmployeeUserDetails.Data.User_Key_Skills> mArrayListUserSkills) {
        skillIdList.clear();
        flow_keyskill.removeAllViews();
        for (int i = 0; i < mArrayListUserSkills.size(); i++) {
            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout linearLayoutF = new LinearLayout(mContext);
            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                    FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams layoutParamsF = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, (int) (mContext.getResources().getDisplayMetrics().density*32));
            layoutParamsF.setMargins((int) (mContext.getResources().getDisplayMetrics().density*5), 0, (int) (mContext.getResources().getDisplayMetrics().density*5), (int) (mContext.getResources().getDisplayMetrics().density*8));
            linearLayoutF.setLayoutParams(layoutParams);
            linearLayout.setLayoutParams(layoutParamsF);
            linearLayout.setPadding((int) (mContext.getResources().getDisplayMetrics().density*8), 0, (int) (mContext.getResources().getDisplayMetrics().density*8), 0);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setBackgroundResource(R.drawable.primary_semiround_bg);
            PopinsRegularTextView bt = new PopinsRegularTextView(mContext);
            bt.setText(mArrayListUserSkills.get(i).getSkill_name());
            bt.setAllCaps(false);
            bt.setTextSize(12f);
            bt.setMaxLines(1);
            bt.setEllipsize(TextUtils.TruncateAt.END);
            bt.setTag(mArrayListUserSkills.get(i).getSkill_name());
            bt.setTextColor(mContext.getResources().getColor(R.color.white));
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(0, 0, 0, 0);
            bt.setLayoutParams(layoutParams1);

            linearLayout.addView(bt);
            linearLayoutF.addView(linearLayout);
            linearLayoutF.setTag(mArrayListUserSkills.get(i).getSkill_name());
            skillIdList.add(mArrayListUserSkills.get(i).getSkill_id());
            flow_keyskill.addView(linearLayoutF);

        }
    }

    public void setSkillFlowfromAddSkill(ArrayList<String> mArrayListUserSkills,ArrayList<String> skillId) {
        if (flow_keyskill!=null){
            skillIdList.clear();
            skillIdList.addAll(skillId);
            flow_keyskill.removeAllViews();
            for (int i = 0; i < mArrayListUserSkills.size(); i++) {
                LinearLayout linearLayout = new LinearLayout(mContext);
                LinearLayout linearLayoutF = new LinearLayout(mContext);
                FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                        FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams layoutParamsF = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, (int) (mContext.getResources().getDisplayMetrics().density*32));
                layoutParamsF.setMargins((int) (mContext.getResources().getDisplayMetrics().density*5), 0, (int) (mContext.getResources().getDisplayMetrics().density*5), (int) (mContext.getResources().getDisplayMetrics().density*8));
                linearLayoutF.setLayoutParams(layoutParams);
                linearLayout.setLayoutParams(layoutParamsF);
                linearLayout.setPadding((int) (mContext.getResources().getDisplayMetrics().density*8), 0, (int) (mContext.getResources().getDisplayMetrics().density*8), 0);
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
                flow_keyskill.addView(linearLayoutF);

            }
        }

    }


    private void setLanguageFlow(ArrayList<EmployeeUserDetails.Data.User_Languages> mArrayUserListLang) {
        flow_languages.removeAllViews();
        stringLangIds.clear();
        if (mArrayUserListLang!=null){
            for (int i = 0; i < mArrayUserListLang.size(); i++) {
                LinearLayout linearLayout = new LinearLayout(mContext);
                FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                        FlowLayout.LayoutParams.WRAP_CONTENT, (int) (mContext.getResources().getDisplayMetrics().density*32));
                layoutParams.setMargins((int) (mContext.getResources().getDisplayMetrics().density*5), 0, (int) (mContext.getResources().getDisplayMetrics().density*5), (int) (mContext.getResources().getDisplayMetrics().density*8));                                        linearLayout.setPadding(16, 0, 16, 0);
                linearLayout.setPadding((int) (mContext.getResources().getDisplayMetrics().density*8), 0, (int) (mContext.getResources().getDisplayMetrics().density*8), 0);
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
                stringLangIds.add(stringLangIds.size(),mArrayUserListLang.get(i).getLanguage_id());

                flow_languages.addView(linearLayout);

            }
        }
    }
    public void setLangFlowfromAddLang(ArrayList<String> mArrayListUserSkills,ArrayList<String> skillId) {
        if (flow_languages!=null){
            stringLangIds.clear();
            stringLangIds.addAll(skillId);
            flow_languages.removeAllViews();
            for (int i = 0; i < mArrayListUserSkills.size(); i++) {
                LinearLayout linearLayout = new LinearLayout(mContext);
                LinearLayout linearLayoutF = new LinearLayout(mContext);
                FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                        FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams layoutParamsF = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, (int) (mContext.getResources().getDisplayMetrics().density*32));
                layoutParamsF.setMargins((int) (mContext.getResources().getDisplayMetrics().density*5), 0, (int) (mContext.getResources().getDisplayMetrics().density*5), (int) (mContext.getResources().getDisplayMetrics().density*8));
                linearLayoutF.setLayoutParams(layoutParams);
                linearLayout.setLayoutParams(layoutParamsF);
                linearLayout.setPadding((int) (mContext.getResources().getDisplayMetrics().density*8), 0, (int) (mContext.getResources().getDisplayMetrics().density*8), 0);
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

            }
        }

    }
    private void setJobtypeFlow(ArrayList<EmployeeUserDetails.Data.User_Job_Type> mArrayUserJobType) {
        flow_jobtype.removeAllViews();
        if (mArrayUserJobType!=null){
            for (int i = 0; i < mArrayUserJobType.size(); i++) {
                LinearLayout linearLayout = new LinearLayout(mContext);
                LinearLayout linearLayoutF = new LinearLayout(mContext);
                FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                        FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams layoutParamsF = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, (int) (mContext.getResources().getDisplayMetrics().density*32));
                layoutParamsF.setMargins((int) (mContext.getResources().getDisplayMetrics().density*5), 0, (int) (mContext.getResources().getDisplayMetrics().density*5), (int) (mContext.getResources().getDisplayMetrics().density*8));
                linearLayoutF.setLayoutParams(layoutParams);
                linearLayout.setLayoutParams(layoutParamsF);
                linearLayout.setPadding((int) (mContext.getResources().getDisplayMetrics().density*8), 0, (int) (mContext.getResources().getDisplayMetrics().density*8), 0);
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

            }
        }
    }



    private void callJobTypeSheet() {
        mArrayListjbtype.clear();
        bottomsheetemployeetype.setState(BottomSheetBehavior.STATE_EXPANDED);

        bottomsheetemployeetype.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    bottomsheetemployeetype.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });


        for (int i = 0; i < mArrayListJobType.size(); i++) {
            LangModel model = new LangModel();
            model.setId(mArrayListJobType.get(i).getJob_type_id());
            model.setName(mArrayListJobType.get(i).getJob_type_name());
            model.setSelected(false);
            mArrayListjbtype.add(model);
        }
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
                if (response.code()==200 && response.body()!=null){
                    if (response.body().get("code").getAsInt()==203){
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        getActivity().finish();
                    }else
                        onResume();
                }else onResume();

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                onResume();
            }
        });

    }


    public void setObjective(String obj) {
        if (tv_objective_value!=null)
            tv_objective_value.setText(obj);
    }

    public void setExperienceData(boolean isEdit, JsonArray jsonElements) {
        if (isEdit){
            if (mArrayuserworkexperience != null) {
                if (jsonElements.size()>0){
                JsonObject object=    jsonElements.get(0).getAsJsonObject();
                if (object.has("user_experience_id")){
                    for (int i=0;i<mArrayuserworkexperience.size();i++){
                        if (object.get("user_experience_id").getAsString().equalsIgnoreCase(mArrayuserworkexperience.get(i).getUser_experience_id())){
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
                        }
                    }
                    if (mArrayuserworkexperience.size() != 0) {
                        ExperienceAdapter adapter = new ExperienceAdapter(mContext, mArrayuserworkexperience);
                        list_experience.setAdapter(adapter);
                    }
                }
                }
            }
        }else {
            if (mArrayuserworkexperience == null) {
                mArrayuserworkexperience=new ArrayList<>();
            }
            if (jsonElements.size()>0){
                JsonObject object=    jsonElements.get(0).getAsJsonObject();
                if (object.has("user_experience_id")){
                    EmployeeUserDetails.Data.UserWorkExperience model=new EmployeeUserDetails.Data.UserWorkExperience(
                            object.get("user_experience_id").getAsString(),object.get("company_name").getAsString(),object.get("experience_title").getAsString()
                            ,object.get("work_from").getAsString(),object.get("work_to").getAsString(),object.get("currently_working").getAsString()
                  ,object.get("description").getAsString(),object.get("functional_id").getAsString(),object.get("start_date").getAsString(),object.get("end_date").getAsString());
                         mArrayuserworkexperience.add(mArrayuserworkexperience.size(),model);
                    }
                    if (mArrayuserworkexperience.size() != 0) {
                        ExperienceAdapter adapter = new ExperienceAdapter(mContext, mArrayuserworkexperience);
                        list_experience.setAdapter(adapter);
                    }

            }
        }

    }

    public void setEducationData(boolean isEdit, JsonObject jsonElements) {
        if (isEdit){
            if (mArrayuserusereducation != null) {
                if (jsonElements.size()>0){
                    if (jsonElements.has("user_education_id")){
                        for (int i=0;i<mArrayuserusereducation.size();i++){
                            if (jsonElements.get("user_education_id").getAsString().equalsIgnoreCase(mArrayuserusereducation.get(i).getUser_education_id())){
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
                            }
                        }

                            if (mArrayuserusereducation.size() != 0) {
                                UserEducationAdapter adapter = new UserEducationAdapter(mContext, mArrayuserusereducation);
                                list_education.setAdapter(adapter);
                            }

                    }
                }
            }
        }else {
            if (mArrayuserusereducation == null) {
                mArrayuserusereducation=new ArrayList<>();
            }
            if (jsonElements.size()>0){
                JsonObject object=    jsonElements;
                if (object.has("user_experience_id")){
                    EmployeeUserDetails.Data.User_Eductaion model=new EmployeeUserDetails.Data.User_Eductaion(object.get("user_education_id").getAsString(),object.get("college_university_name").getAsString(),
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
                    mArrayuserusereducation.add(mArrayuserusereducation.size(),model);
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
        UserPreferenceAdapter adapter = new UserPreferenceAdapter(mContext, mArrayuseruserpreference);
        list_preferences.setAdapter(adapter);
        if (mArrayuseruserpreference.size()>0)
        prefId = mArrayuseruserpreference.get(0).getPreference_id();
    }

    @SuppressLint("StaticFieldLeak")
    public class UploadFile extends AsyncTask<Object, String, String> {
        String file_name = "";
        Uri uri;
        @SuppressLint("StaticFieldLeak")
        Context context;

        public UploadFile(Uri uri, Context context) {
            this.uri = uri;
            this.context = context;
        }

        @Override
        protected String doInBackground(Object[] parms) {
            String result = null;
            try {
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";
                int bytesRead, bytesAvailable, bufferSize;
                byte[] buffer;
                int maxBufferSize = 1024 * 1024;
                //todo change URL as per client ( MOST IMPORTANT )
                URL url = new URL(ApiUrls.BASE_URL + ApiUrls.URL_ADDRESUME);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Allow Inputs &amp; Outputs.
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                // Set HTTP method to POST.
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                FileInputStream fileInputStream;
                DataOutputStream outputStream;
                outputStream = new DataOutputStream(connection.getOutputStream());
                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"user_token\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(Config.GetUserToken());
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"cv_id\"" + lineEnd);
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(Config.GetCVID());
                outputStream.writeBytes(lineEnd);

                outputStream.writeBytes(twoHyphens + boundary + lineEnd);
                outputStream.writeBytes("Content-Disposition: form-data; name=\"user_cv\";filename=\"" + "Resume.pdf" + "\"" + lineEnd);
                outputStream.writeBytes(lineEnd);

//            fileInputStream = new FileInputStream(uri.getPath());
                InputStream inputStream = context.getContentResolver().openInputStream(uri);
                bytesAvailable = inputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // Read file
                bytesRead = inputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    outputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = inputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = inputStream.read(buffer, 0, bufferSize);
                }
                outputStream.writeBytes(lineEnd);
                outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                int serverResponseCode = connection.getResponseCode();

                if (serverResponseCode == 200) {
                    StringBuilder s_buffer = new StringBuilder();
                    InputStream is = new BufferedInputStream(connection.getInputStream());
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String inputLine;
                    while ((inputLine = br.readLine()) != null) {
                        s_buffer.append(inputLine);
                    }
                    result = s_buffer.toString();
                }
                inputStream.close();
                outputStream.flush();
                outputStream.close();
                if (result != null) {
                    Log.d("result_for upload", result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            isHit=true;
            onResume();
            super.onPostExecute(s);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == DOCUMENT_FILE_REQUEST) {
            if (data == null) {
                isHit = true;
                return;
            }
            Uri selectedFileUri = data.getData();
            new UploadFile(selectedFileUri, mContext).execute();
            tv_resume.setText("Resume.pdf");

        } else isHit = true;
    }

    public void deleteEducation(String id) {

            for (int i=0;i<mArrayuserusereducation.size();i++){
                if (mArrayuserusereducation.get(i).getUser_education_id().equalsIgnoreCase(id)){
                    mArrayuserusereducation.remove(i);
                    break;
                }
            }
        UserEducationAdapter adapter = new UserEducationAdapter(mContext, mArrayuserusereducation);
        list_education.setAdapter(adapter);
        SwipeeApiClient.swipeeServiceInstance().deleteEducation(Config.GetUserToken(),id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                onResume();
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                onResume();
            }
        });
    }
    public void deleteExperience(String id) {

        for (int i=0;i<mArrayuserworkexperience.size();i++){
            if (mArrayuserworkexperience.get(i).getUser_experience_id().equalsIgnoreCase(id)){
                mArrayuserworkexperience.remove(i);
                break;
            }
        }
        ExperienceAdapter adapter = new ExperienceAdapter(mContext, mArrayuserworkexperience);
        list_experience.setAdapter(adapter);
        SwipeeApiClient.swipeeServiceInstance().deleteExperience(Config.GetUserToken(),id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                onResume();
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                onResume();
            }
        });
    }

}
