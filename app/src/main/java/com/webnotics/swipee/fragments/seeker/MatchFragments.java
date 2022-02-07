package com.webnotics.swipee.fragments.seeker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.daprlabs.cardstack.SwipeDeck;
import com.daprlabs.cardstack.SwipeFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.Seeker.JobDetail;
import com.webnotics.swipee.activity.Seeker.SeekerHomeActivity;
import com.webnotics.swipee.adapter.seeeker.FilterAdapter;
import com.webnotics.swipee.fragments.Basefragment;
import com.webnotics.swipee.interfaces.CountersInterface;
import com.webnotics.swipee.model.seeker.FilterModel;
import com.webnotics.swipee.model.seeker.FilterModelSelected;
import com.webnotics.swipee.model.seeker.UserJobListing;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MatchFragments extends Basefragment implements View.OnClickListener, TextWatcher {
    ArrayList<FilterModelSelected> jobTypeSelected = new ArrayList<>();
    ArrayList<FilterModelSelected> data = new ArrayList<>();
    ArrayList<FilterModelSelected> locationSelected = new ArrayList<>();
    ArrayList<FilterModelSelected> distanceSelected = new ArrayList<>();
    ArrayList<FilterModelSelected> designationSelected = new ArrayList<>();
    ArrayList<FilterModelSelected> qualificationSelected = new ArrayList<>();
    ArrayList<FilterModelSelected> expSelected = new ArrayList<>();
    ArrayList<FilterModelSelected> industrySelected = new ArrayList<>();
    ArrayList<FilterModelSelected> salarySelected = new ArrayList<>();
    @SuppressLint("StaticFieldLeak")
    public static MatchFragments instance;
    Context mContext;
    SwipeDeck swipeDeck;
    SwipeFrameLayout swp;
    ArrayList<UserJobListing.Data.Jobs_Listing> mArrayUser = new ArrayList<>();

    private Rest rest;
    ArrayList<FilterModel.Data.JobType> mJOnTypeArrayList = new ArrayList<>();
    ArrayList<FilterModel.Data.Location> mLocationArrayList = new ArrayList<>();
    ArrayList<FilterModel.Data.Distance> mDistanceArrayList = new ArrayList<>();
    ArrayList<FilterModel.Data.Designation> mDesignationArrayList = new ArrayList<>();
    ArrayList<FilterModel.Data.Qualification> mQualificationArrayList = new ArrayList<>();
    ArrayList<FilterModel.Data.Experience> mExperience_nameArrayList = new ArrayList<>();
    ArrayList<FilterModel.Data.Industry> mIndustryArrayList = new ArrayList<>();
    ArrayList<FilterModel.Data.Salary> mSalaryArrayList = new ArrayList<>();
    public int pos;
    SwipeDeckAdapter adapter;
    EditText et_search;
    String status = "";
    RelativeLayout nolay, datalay, noleftswipelay, filter_sheet, rl_filtersheet;
    TextView nodatatxt, tvNoswipe;
    CountersInterface countersInterface;
    private int left_swipes = 0;
    private String packgeID = "";
    BottomSheetBehavior filter_sheetBottom;
    private RecyclerView listview;
    TextView jobtype, location, distance, designation, qualifications, experience, industrys, salary, tv_apply, tv_clear;
    private boolean isFilterLoaded = false;
    private String adapterAttachedFor = "";
    private FilterAdapter filterAdapter;
    ImageView iv_accept,iv_nodata,iv_reject,iv_jobtype,iv_location,iv_distance,iv_designation,iv_qualifications,iv_experience,iv_industrys,iv_salary;

    private ArrayList<UserJobListing.Data.Jobs_Listing> mArrayUserListing = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.match_screen, container, false);

        mContext = getActivity();
        instance = this;
        rest = new Rest(mContext);
        init(rootView);
        return rootView;
    }

    public void init(View rootView) {

        nodatatxt = rootView.findViewById(R.id.nodatatxt);
        tvNoswipe = rootView.findViewById(R.id.tvNoswipe);
        nolay = rootView.findViewById(R.id.nolay);
        iv_nodata = rootView.findViewById(R.id.iv_nodata);
        noleftswipelay = rootView.findViewById(R.id.noleftswipe);
        datalay = rootView.findViewById(R.id.datalay);
        swp = rootView.findViewById(R.id.swp);
        countersInterface=SeekerHomeActivity.instance.countersInterface;

        ////////////
        filter_sheet = rootView.findViewById(R.id.filter_sheet);
        filter_sheetBottom = BottomSheetBehavior.from(filter_sheet);
        listview = filter_sheet.findViewById(R.id.listviewlanguage);
        rl_filtersheet = filter_sheet.findViewById(R.id.rl_filtersheet);
        jobtype = filter_sheet.findViewById(R.id.jobtype);
        location = filter_sheet.findViewById(R.id.location);
        distance = filter_sheet.findViewById(R.id.distance);
        designation = filter_sheet.findViewById(R.id.designation);
        qualifications = filter_sheet.findViewById(R.id.qualifications);
        experience = filter_sheet.findViewById(R.id.experience);
        industrys = filter_sheet.findViewById(R.id.industrys);
        salary = filter_sheet.findViewById(R.id.salary);
        tv_clear = filter_sheet.findViewById(R.id.tv_clear);
        tv_apply = filter_sheet.findViewById(R.id.tv_apply);
        iv_jobtype = filter_sheet.findViewById(R.id.iv_jobtype);
        iv_location = filter_sheet.findViewById(R.id.iv_location);
        iv_distance = filter_sheet.findViewById(R.id.iv_distance);
        iv_designation = filter_sheet.findViewById(R.id.iv_designation);
        iv_qualifications = filter_sheet.findViewById(R.id.iv_qualifications);
        iv_experience = filter_sheet.findViewById(R.id.iv_experience);
        iv_industrys = filter_sheet.findViewById(R.id.iv_industrys);
        iv_salary = filter_sheet.findViewById(R.id.iv_salary);
        et_search = filter_sheet.findViewById(R.id.et_search);
        et_search.addTextChangedListener(this);
        et_search.setVisibility(View.GONE);
        //////////
        swipeDeck = rootView.findViewById(R.id.swipe_deck);
        iv_accept = rootView.findViewById(R.id.iv_accept);
        iv_reject = rootView.findViewById(R.id.iv_reject);
        nodatatxt.setText(getResources().getString(R.string.nodatatxt1First));
        String text = mContext.getResources().getString(R.string.noleftswipe);
        SpannableString ss = new SpannableString(text);

        // creating clickable span to be implemented as a link
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            public void onClick(View widget) {
                if (SeekerHomeActivity.instance != null)
                    SeekerHomeActivity.instance.setPlansFragment();
            }
        };
        // setting the part of string to be act as a link
        ss.setSpan(clickableSpan1, text.lastIndexOf(".") + 1, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvNoswipe.setText(ss);
        tvNoswipe.setTextColor(getResources().getColor(R.color.colorPrimary));
        tvNoswipe.setTextSize(14);
        tvNoswipe.setMovementMethod(LinkMovementMethod.getInstance());

        rl_filtersheet.setOnClickListener(this);
        jobtype.setOnClickListener(this);
        location.setOnClickListener(this);
        distance.setOnClickListener(this);
        designation.setOnClickListener(this);
        qualifications.setOnClickListener(this);
        experience.setOnClickListener(this);
        industrys.setOnClickListener(this);
        salary.setOnClickListener(this);
        tv_clear.setOnClickListener(this);
        tv_apply.setOnClickListener(this);
        iv_reject.setOnClickListener(this);
        iv_accept.setOnClickListener(this);

        swipeDeck.setEventCallback(new SwipeDeck.SwipeEventCallback() {
            @Override
            public void cardSwipedLeft(int position) {
                if (mArrayUser.size()-1==position){
                    iv_accept.setVisibility(View.GONE);
                    iv_reject.setVisibility(View.GONE);
                }else {
                    iv_accept.setVisibility(View.VISIBLE);
                    iv_reject.setVisibility(View.VISIBLE);
                }
                if (left_swipes > 0) {
                    status = "Reject";
                    pos = position;
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                    hashMap.put(ParaName.KEY_JOBPOSTID, mArrayUser.get(position).getJob_post_id());
                    hashMap.put(ParaName.KEY_USERSTATUS, "R");
                    hashMap.put(ParaName.KEY_COMPANYID, mArrayUser.get(position).getCompany_id());

                    if (rest.isInterentAvaliable()) {
                        AppController.ShowDialogue("", mContext);
                        postLikeDislike(hashMap);
                    } else rest.AlertForInternet();
                }
                else if (!TextUtils.isEmpty(packgeID) && packgeID.equalsIgnoreCase("3")) {
                    status = "Reject";
                    pos = position;
                    HashMap<String, String> hashMap = new HashMap<>();
                    hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                    hashMap.put(ParaName.KEY_JOBPOSTID, mArrayUser.get(position).getJob_post_id());
                    hashMap.put(ParaName.KEY_USERSTATUS, "R");
                    hashMap.put(ParaName.KEY_COMPANYID, mArrayUser.get(position).getCompany_id());
                    if (rest.isInterentAvaliable()) {
                        AppController.ShowDialogue("", mContext);
                        postLikeDislike(hashMap);
                    } else rest.AlertForInternet();
                }
                else {
                    noleftswipelay.setVisibility(View.VISIBLE);
                    datalay.setVisibility(View.GONE);
                }

            }

            @Override
            public void cardSwipedRight(int position) {
                if (mArrayUser.size()-1==position){
                    iv_accept.setVisibility(View.GONE);
                    iv_reject.setVisibility(View.GONE);
                }else {
                    iv_accept.setVisibility(View.VISIBLE);
                    iv_reject.setVisibility(View.VISIBLE);
                }
                if (left_swipes > 0) {
                    if (mArrayUser.get(position).getUser_match_status() != null) {
                        if (!mArrayUser.get(position).getUser_match_status().equalsIgnoreCase("A")) {
                            status = "Accept";
                            pos = position;

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                            hashMap.put(ParaName.KEY_JOBPOSTID, mArrayUser.get(position).getJob_post_id());
                            hashMap.put(ParaName.KEY_USERSTATUS, "A");
                            hashMap.put(ParaName.KEY_COMPANYID, mArrayUser.get(position).getCompany_id());

                            if (rest.isInterentAvaliable()) {
                                AppController.ShowDialogue("", mContext);
                                postLikeDislike(hashMap);
                            } else rest.AlertForInternet();
                        } else if (mArrayUser.get(position).getCompany_match_status() != null) {
                            if (mArrayUser.get(position).getCompany_match_status().equalsIgnoreCase("A")) {
                                mContext.startActivity(new Intent(mContext, JobDetail.class).putExtra("from", MatchFragments.class.getSimpleName()).putExtra("id", mArrayUser.get(position).getJob_post_id()));
                            }
                        }
                    }
                    else {
                        status = "Accept";
                        pos = position;
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                        hashMap.put(ParaName.KEY_JOBPOSTID, mArrayUser.get(position).getJob_post_id());
                        hashMap.put(ParaName.KEY_USERSTATUS, "A");
                        hashMap.put(ParaName.KEY_COMPANYID, mArrayUser.get(position).getCompany_id());

                        if (rest.isInterentAvaliable()) {
                            AppController.ShowDialogue("", mContext);
                            postLikeDislike(hashMap);
                        } else rest.AlertForInternet();
                    }
                }
                else if (!TextUtils.isEmpty(packgeID) && packgeID.equalsIgnoreCase("3")) {
                    if (mArrayUser.get(position).getUser_match_status() != null) {
                        if (!mArrayUser.get(position).getUser_match_status().equalsIgnoreCase("A")) {
                            status = "Accept";
                            pos = position;

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                            hashMap.put(ParaName.KEY_JOBPOSTID, mArrayUser.get(position).getJob_post_id());
                            hashMap.put(ParaName.KEY_USERSTATUS, "A");
                            hashMap.put(ParaName.KEY_COMPANYID, mArrayUser.get(position).getCompany_id());

                            if (rest.isInterentAvaliable()) {
                                AppController.ShowDialogue("", mContext);
                                postLikeDislike(hashMap);
                            } else rest.AlertForInternet();
                        } else if (mArrayUser.get(position).getCompany_match_status() != null) {
                            if (mArrayUser.get(position).getCompany_match_status().equalsIgnoreCase("A")) {
                                mContext.startActivity(new Intent(mContext, JobDetail.class).putExtra("from", MatchFragments.class.getSimpleName()).putExtra("id", mArrayUser.get(position).getJob_post_id()));
                            }
                        }
                    }
                    else {
                        status = "Accept";
                        pos = position;
                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                        hashMap.put(ParaName.KEY_JOBPOSTID, mArrayUser.get(position).getJob_post_id());
                        hashMap.put(ParaName.KEY_USERSTATUS, "A");
                        hashMap.put(ParaName.KEY_COMPANYID, mArrayUser.get(position).getCompany_id());

                        if (rest.isInterentAvaliable()) {
                            AppController.ShowDialogue("", mContext);
                            postLikeDislike(hashMap);
                        } else rest.AlertForInternet();
                    }
                }
                else {
                    noleftswipelay.setVisibility(View.VISIBLE);
                    datalay.setVisibility(View.GONE);
                }


            }

            @Override
            public void cardsDepleted() {
            }

            @Override
            public void cardActionDown() {

            }

            @Override
            public void cardActionUp() {

            }
        });
        swipeDeck.setLeftImage(R.id.nopee);
        swipeDeck.setRightImage(R.id.yess);
        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            getHomeJobList();
        } else rest.AlertForInternet();


    }

    @Override
    public int setContentView() {
        return R.layout.match_screen;
    }

    @Override
    protected void backPressed() {
        if (filter_sheetBottom != null) {
            if (filter_sheetBottom.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                filter_sheetBottom.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (SeekerHomeActivity.instance != null) {
                    SeekerHomeActivity.instance.isFilterShowing = false;
                }

            } else getActivity().onBackPressed();

        } else getActivity().onBackPressed();
    }

    private void resetColor() {
        jobtype.setTextColor(getResources().getColor(R.color.nav_gray));
        location.setTextColor(getResources().getColor(R.color.nav_gray));
        distance.setTextColor(getResources().getColor(R.color.nav_gray));
        designation.setTextColor(getResources().getColor(R.color.nav_gray));
        qualifications.setTextColor(getResources().getColor(R.color.nav_gray));
        experience.setTextColor(getResources().getColor(R.color.nav_gray));
        industrys.setTextColor(getResources().getColor(R.color.nav_gray));
        salary.setTextColor(getResources().getColor(R.color.nav_gray));
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.iv_reject:
                swipeDeck.swipeTopCardLeft(1500);
                break;
            case R.id.iv_accept:
                swipeDeck.swipeTopCardRight(1500);
                break;

            case R.id.jobtype:
                hideKeyboardFrom(mContext,et_search);
                et_search.setText("");
                et_search.setVisibility(View.GONE);
                data.clear();
                data.addAll(jobTypeSelected);
                adapterAttachedFor = "JobType";
                if (filterAdapter != null)
                    filterAdapter.notifyDataSetChanged();
                resetColor();
                jobtype.setTextColor(getResources().getColor(R.color.colorPrimary));

                break;
            case R.id.location:
                hideKeyboardFrom(mContext,et_search);
                et_search.setText("");
                et_search.setVisibility(View.VISIBLE);
                data.clear();
                data.addAll(locationSelected);
                adapterAttachedFor = "Location";
                if (filterAdapter != null)
                    filterAdapter.notifyDataSetChanged();
                resetColor();
                location.setTextColor(getResources().getColor(R.color.colorPrimary));

                break;
            case R.id.distance:
                hideKeyboardFrom(mContext,et_search);
                et_search.setText("");
                et_search.setVisibility(View.GONE);
                data.clear();
                data.addAll(distanceSelected);
                adapterAttachedFor = "Distance";
                if (filterAdapter != null)
                    filterAdapter.notifyDataSetChanged();
                resetColor();
                distance.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.designation:
                hideKeyboardFrom(mContext,et_search);
                et_search.setText("");
                et_search.setVisibility(View.VISIBLE);
                data.clear();
                data.addAll(designationSelected);
                adapterAttachedFor = "Designation";
                if (filterAdapter != null)
                    filterAdapter.notifyDataSetChanged();

                resetColor();
                designation.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.qualifications:
                et_search.setVisibility(View.GONE);
                hideKeyboardFrom(mContext,et_search);
                et_search.setText("");
                data.clear();
                data.addAll(qualificationSelected);
                adapterAttachedFor = "Qualification";
                if (filterAdapter != null)
                    filterAdapter.notifyDataSetChanged();
                resetColor();
                qualifications.setTextColor(getResources().getColor(R.color.colorPrimary));

                break;
            case R.id.experience:
                et_search.setVisibility(View.GONE);
                hideKeyboardFrom(mContext,et_search);
                et_search.setText("");
                data.clear();
                data.addAll(expSelected);
                adapterAttachedFor = "Experience";
                if (filterAdapter != null)
                    filterAdapter.notifyDataSetChanged();
                resetColor();
                experience.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.industrys:
                hideKeyboardFrom(mContext,et_search);
                et_search.setText("");
                et_search.setVisibility(View.VISIBLE);
                data.clear();
                data.addAll(industrySelected);
                adapterAttachedFor = "Industry";
                if (filterAdapter != null)
                    filterAdapter.notifyDataSetChanged();
                resetColor();
                industrys.setTextColor(getResources().getColor(R.color.colorPrimary));
                break;
            case R.id.salary:
                et_search.setVisibility(View.GONE);
                hideKeyboardFrom(mContext,et_search);
                et_search.setText("");
                data.clear();
                data.addAll(salarySelected);
                adapterAttachedFor = "Salary";
                if (filterAdapter != null)
                    filterAdapter.notifyDataSetChanged();
                resetColor();
                salary.setTextColor(getResources().getColor(R.color.colorPrimary));

                break;
            case R.id.tv_clear:
                for (int i = 0; i < jobTypeSelected.size(); i++) {
                    jobTypeSelected.get(i).setSelected(false);
                }
                for (int i = 0; i < locationSelected.size(); i++) {
                    locationSelected.get(i).setSelected(false);
                }
                for (int i = 0; i < distanceSelected.size(); i++) {
                    distanceSelected.get(i).setSelected(false);
                }
                for (int i = 0; i < designationSelected.size(); i++) {
                    designationSelected.get(i).setSelected(false);
                }
                for (int i = 0; i < qualificationSelected.size(); i++) {
                    qualificationSelected.get(i).setSelected(false);
                }
                for (int i = 0; i < expSelected.size(); i++) {
                    expSelected.get(i).setSelected(false);
                }
                for (int i = 0; i < industrySelected.size(); i++) {
                    industrySelected.get(i).setSelected(false);
                }
                for (int i = 0; i < salarySelected.size(); i++) {
                    salarySelected.get(i).setSelected(false);
                }
                hideKeyboardFrom(mContext,et_search);
                et_search.setText("");
                et_search.setVisibility(View.GONE);
                data.clear();
                setSelectedFilterHighlight();
                addDataToDataArray();
                if (filterAdapter != null)
                    filterAdapter.notifyDataSetChanged();
                AppController.ShowDialogue("", mContext);
                getHomeJobList();
                break;
            case R.id.tv_apply:
                ArrayList<String> jobtypeStrings = new ArrayList<>();
                ArrayList<String> locationStrings = new ArrayList<>();
                ArrayList<String> distanceStrings = new ArrayList<>();
                ArrayList<String> designationStrings = new ArrayList<>();
                ArrayList<String> qualificationStrings = new ArrayList<>();
                ArrayList<String> expStrings = new ArrayList<>();
                ArrayList<String> industryStrings = new ArrayList<>();
                ArrayList<String> salaryStrings = new ArrayList<>();
                for (int i = 0; i < jobTypeSelected.size(); i++) {
                    if (jobTypeSelected.get(i).isSelected()) {
                        jobtypeStrings.add(jobTypeSelected.get(i).getId());
                    }
                }
                for (int i = 0; i < locationSelected.size(); i++) {
                    if (locationSelected.get(i).isSelected()) {
                        locationStrings.add(locationSelected.get(i).getId());
                    }
                }
                for (int i = 0; i < distanceSelected.size(); i++) {
                    if (distanceSelected.get(i).isSelected()) {
                        distanceStrings.add(distanceSelected.get(i).getId());
                    }
                }
                for (int i = 0; i < designationSelected.size(); i++) {
                    if (designationSelected.get(i).isSelected()) {
                        designationStrings.add(designationSelected.get(i).getId());
                    }
                }
                for (int i = 0; i < qualificationSelected.size(); i++) {
                    if (qualificationSelected.get(i).isSelected()) {
                        qualificationStrings.add(qualificationSelected.get(i).getId());
                    }
                }
                for (int i = 0; i < expSelected.size(); i++) {
                    if (expSelected.get(i).isSelected()) {
                        expStrings.add(expSelected.get(i).getId());
                    }
                }
                for (int i = 0; i < industrySelected.size(); i++) {
                    if (industrySelected.get(i).isSelected()) {
                        industryStrings.add(industrySelected.get(i).getId());
                    }
                }
                for (int i = 0; i < salarySelected.size(); i++) {
                    if (salarySelected.get(i).isSelected()) {
                        salaryStrings.add(salarySelected.get(i).getId());
                    }
                }

                HashMap<String, String> hashMap = new HashMap<>();

                if (jobtypeStrings.size() > 0) {
                    hashMap.put(ParaName.KEY_JOBTYPEID, jobtypeStrings.toString());
                }
                if (locationStrings.size() > 0) {
                    hashMap.put(ParaName.KEY_LOCATIONID, locationStrings.toString());
                }
                if (distanceStrings.size() > 0) {
                    hashMap.put(ParaName.KEY_DISTANCEID, distanceStrings.toString());
                }
                if (designationStrings.size() > 0) {
                    hashMap.put(ParaName.KEY_DESIGNATIONID, designationStrings.toString());
                }
                if (qualificationStrings.size() > 0) {
                    hashMap.put(ParaName.KEY_DEGREEID, qualificationStrings.toString());
                }
                if (expStrings.size() > 0) {
                    hashMap.put(ParaName.KEY_EXPERIENCEID, expStrings.toString());
                }
                if (industryStrings.size() > 0) {
                    hashMap.put(ParaName.KEY_INDUSTRY, industryStrings.toString());
                }
                if (salaryStrings.size() > 0) {
                    hashMap.put(ParaName.KEY_RANGEID, salaryStrings.toString());
                }
                if (hashMap.size()>0){
                    hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                    if (rest.isInterentAvaliable()) {
                        AppController.ShowDialogue("", mContext);
                        getFilterJobList(hashMap);
                    } else rest.isInterentAvaliable();
                }else rest.showToast("Select at least 1 filter value");


                break;

            default:
                break;
        }


    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (filterAdapter != null)
            filterAdapter.getFilter().filter(s.toString().trim());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    public class SwipeDeckAdapter extends BaseAdapter {

        private final LayoutInflater inflater;

        private final Context context;

        @SuppressLint("NewApi")
        public SwipeDeckAdapter(ArrayList<UserJobListing.Data.Jobs_Listing> mArrayUserListing1, Context context) {
            mArrayUserListing.clear();
            mArrayUserListing = mArrayUserListing1;
            this.context = context;

            inflater = getLayoutInflater();
        }

        @Override
        public int getCount() {
            return mArrayUserListing.size();
        }

        @Override
        public Object getItem(int position) {
            return mArrayUserListing.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = convertView;
            if (v == null) {
                v = inflater.inflate(R.layout.employeeehomerowitms, parent, false);
            }
            CardView cardview1 = v.findViewById(R.id.cardview1);
            ImageView building = v.findViewById(R.id.building);
            ImageView save = v.findViewById(R.id.save);
            TextView designationnm = v.findViewById(R.id.designationnm);

            TextView locationtxt = v.findViewById(R.id.locationtxt);
            TextView skillls = v.findViewById(R.id.skillls);

            designationnm.setText(mArrayUserListing.get(position).getCompany_name());
              save.setBackgroundResource(R.drawable.white_light_semiround_bg);
            locationtxt.setText(MessageFormat.format("{0}, {1}", mArrayUserListing.get(position).getJob_city(), mArrayUserListing.get(position).getJob_state()));
            skillls.setText(mArrayUserListing.get(position).getJob_skills());
            if (mArrayUserListing.get(position).getUser_savedjob_status().equalsIgnoreCase("saved")) {
                save.setImageResource(R.drawable.img_saved);
            } else {
                save.setImageResource(R.drawable.img_save);
            }
            Glide.with(mContext)
                    .load(mArrayUserListing.get(position).getCompany_logo())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * 16))))
                    .into(building);

            save.setOnClickListener(view -> {
                if (!mArrayUserListing.get(position).getUser_savedjob_status().equalsIgnoreCase("Saved")) {
                    if (rest.isInterentAvaliable()) {
                        AppController.ShowDialogue("", mContext);
                        callSaveJobService(save, position, mArrayUserListing.get(position).getJob_post_id(), "1");
                    } else rest.AlertForInternet();

                } else {
                    if (rest.isInterentAvaliable()) {
                        AppController.ShowDialogue("", mContext);
                        callSaveJobService(save, position, mArrayUserListing.get(position).getJob_post_id(), "0");
                    } else rest.AlertForInternet();

                }

            });

            cardview1.setOnClickListener(view -> {
                mContext.startActivity(new Intent(mContext, JobDetail.class).putExtra("from", MatchFragments.class.getSimpleName()).putExtra("id", mArrayUser.get(position).getJob_post_id()));

            });

            return v;
        }
    }

    private void callSaveJobService(ImageView save, int position, String job_post_id, String s) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
        hashMap.put(ParaName.KEY_JOBPOSTID, job_post_id);
        hashMap.put(ParaName.KEY_SAVESTATUS, s);

        SwipeeApiClient.swipeeServiceInstance().postSaveJob(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {

                    if (s.equalsIgnoreCase("1")) {
                        save.setImageResource(R.drawable.img_saved);
                        mArrayUserListing.get(position).setUser_savedjob_status("Saved");
                    } else {
                        save.setImageResource(R.drawable.img_save);
                        mArrayUserListing.get(position).setUser_savedjob_status("");
                    }
                    pos = position;
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

    private void getHomeJobList() {
        SwipeeApiClient.swipeeServiceInstance().getHomeList(Config.GetUserToken()).enqueue(new Callback<UserJobListing>() {
            @Override
            public void onResponse(@NonNull Call<UserJobListing> call, @NonNull Response<UserJobListing> response) {
                AppController.dismissProgressdialog();
                parseResponse(response, 1);
            }

            @Override
            public void onFailure(@NonNull Call<UserJobListing> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }

    private void getFilterJobList(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().postFilterSearch(hashMap).enqueue(new Callback<UserJobListing>() {
            @Override
            public void onResponse(@NonNull Call<UserJobListing> call, @NonNull Response<UserJobListing> response) {
                AppController.dismissProgressdialog();
                parseResponse(response, 2);
            }

            @Override
            public void onFailure(@NonNull Call<UserJobListing> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }

    private void parseResponse(Response<UserJobListing> response, int i) {
        if (response.code() == 200 && response.body() != null) {
            mArrayUser.clear();
            if (i == 1) {
                if (filter_sheetBottom != null)
                    filter_sheetBottom.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (SeekerHomeActivity.instance != null) {
                    SeekerHomeActivity.instance.isFilterShowing = false;
                    SeekerHomeActivity.instance.filter_icon.setImageDrawable(mContext.getDrawable(R.drawable.ic_filter_unfill));
                }
            } else {
                if (filter_sheetBottom != null)
                    filter_sheetBottom.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (SeekerHomeActivity.instance != null) {
                    SeekerHomeActivity.instance.isFilterShowing = false;
                    SeekerHomeActivity.instance.filter_icon.setImageDrawable(mContext.getDrawable(R.drawable.ic_filter_fill));
                }
            }
            UserJobListing companyuserlisting = response.body();
            if (companyuserlisting.getCode() == 200) {

                if (companyuserlisting.getData().getPackage_expire() != null) {
                    mArrayUser = companyuserlisting.getData().getJobs_listing();
                    left_swipes = companyuserlisting.getData().getLeft_swipes();
                    packgeID = companyuserlisting.getData().getPackage_id();
                    Config.SetPACKAGEEXP(companyuserlisting.getData().getPackage_expire());
                    Config.SetLeftApplyCount(companyuserlisting.getData().getLeft_apply_jobs());
                    if (companyuserlisting.getData().getPackage_expire().equalsIgnoreCase("N")) {
                        if (mArrayUser != null) {
                            try {
                                adapter = new SwipeDeckAdapter(mArrayUser, mContext);
                                swipeDeck.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                                if (mArrayUser.size() != 0) {
                                    iv_accept.setVisibility(View.VISIBLE);
                                    iv_reject.setVisibility(View.VISIBLE);
                                    nolay.setVisibility(View.GONE);
                                    if (left_swipes > 0 || packgeID.equalsIgnoreCase("3")) {
                                        noleftswipelay.setVisibility(View.GONE);
                                        datalay.setVisibility(View.VISIBLE);
                                    } else {
                                        noleftswipelay.setVisibility(View.VISIBLE);
                                        datalay.setVisibility(View.GONE);
                                    }
                                } else {
                                    nolay.setVisibility(View.VISIBLE);
                                    iv_nodata.setImageResource(R.drawable.ic_no_data);
                                    nodatatxt.setText(getResources().getString(R.string.nodatatxt1));
                                    noleftswipelay.setVisibility(View.GONE);
                                    datalay.setVisibility(View.GONE);
                                }
                            } catch (Exception ignored) {
                            }
                        } else {
                            iv_nodata.setImageResource(R.drawable.ic_no_data);
                            nolay.setVisibility(View.VISIBLE);
                            nodatatxt.setText(getResources().getString(R.string.nodatatxt1));
                            datalay.setVisibility(View.GONE);
                            noleftswipelay.setVisibility(View.GONE);
                        }
                    } else {
                        nolay.setVisibility(View.VISIBLE);
                        datalay.setVisibility(View.GONE);
                        iv_nodata.setImageResource(R.drawable.ic_plan_exp);
                        nodatatxt.setText(R.string.packageexpseekr);
                    }
                    if (countersInterface!=null)
                    countersInterface.notification(companyuserlisting.getData().getUser_notification_counter(), 0, 0);

                } else {
                    iv_nodata.setImageResource(R.drawable.ic_no_data);
                    nolay.setVisibility(View.VISIBLE);
                    nodatatxt.setText(getResources().getString(R.string.nodatatxt1First));
                    noleftswipelay.setVisibility(View.GONE);
                    datalay.setVisibility(View.GONE);
                }

            }else if (companyuserlisting.getCode() == 203){
                rest.showToast(companyuserlisting.getMessage());
                AppController.loggedOut(mContext);
                getActivity().finish();
            }else {
                if (mArrayUser != null) {
                    if (mArrayUser.size() == 0) {
                        iv_nodata.setImageResource(R.drawable.ic_no_data);
                        nolay.setVisibility(View.VISIBLE);
                        nodatatxt.setText(getResources().getString(R.string.nodatatxt1));
                        noleftswipelay.setVisibility(View.GONE);
                        datalay.setVisibility(View.GONE);
                    } else {
                        nolay.setVisibility(View.GONE);
                        datalay.setVisibility(View.VISIBLE);
                        noleftswipelay.setVisibility(View.GONE);
                    }
                } else {
                    iv_nodata.setImageResource(R.drawable.ic_no_data);
                    nolay.setVisibility(View.VISIBLE);
                    nodatatxt.setText(getResources().getString(R.string.nodatatxt1First));
                    noleftswipelay.setVisibility(View.GONE);
                    datalay.setVisibility(View.GONE);
                }
            }
        } else {
            AppController.dismissProgressdialog();
            rest.showToast("Something went wrong");
        }
    }


    public void callJobTypeSheet() {
        if (SeekerHomeActivity.instance != null) {
            SeekerHomeActivity.instance.isFilterShowing = true;
        }
        filter_sheetBottom.setState(BottomSheetBehavior.STATE_EXPANDED);
        filter_sheetBottom.setDraggable(false);
        filter_sheetBottom.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    filter_sheetBottom.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });


    }

    public void callFilter(boolean show) {
        if (filter_sheetBottom != null && filter_sheetBottom.getState()==BottomSheetBehavior.STATE_EXPANDED) {
            filter_sheetBottom.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }else {
            if (isFilterLoaded) {
                if (filter_sheetBottom != null)
                    callJobTypeSheet();
            } else {
                if (rest.isInterentAvaliable()) {
                    AppController.ShowDialogue("", mContext);
                    getFilterData();
                } else rest.AlertForInternet();
            }
        }

    }

    private void getFilterData() {
        SwipeeApiClient.swipeeServiceInstance().getFilterData(Config.GetUserToken()).enqueue(new Callback<FilterModel>() {
            @Override
            public void onResponse(@NonNull Call<FilterModel> call, @NonNull Response<FilterModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    FilterModel filterModel = response.body();
                    if (filterModel.getCode()==203){
                        rest.showToast(filterModel.getMessage());
                        AppController.loggedOut(mContext);
                        getActivity().finish();
                    }else
                    if (filterModel.isStatus()) {
                        mJOnTypeArrayList = filterModel.getData().getJob_type();
                        mDistanceArrayList = filterModel.getData().getDistance();
                        mDesignationArrayList = filterModel.getData().getDesignation();
                        mQualificationArrayList = filterModel.getData().getQualification();
                        mExperience_nameArrayList = filterModel.getData().getExperience();
                        mIndustryArrayList = filterModel.getData().getIndustry();
                        mSalaryArrayList = filterModel.getData().getSalary();
                        mLocationArrayList = filterModel.getData().getLocation();

                        if (mJOnTypeArrayList != null) {
                            if (mJOnTypeArrayList.size() != 0) {
                                jobTypeSelected.clear();
                                for (int i = 0; i < mJOnTypeArrayList.size(); i++) {
                                    String name = mJOnTypeArrayList.get(i).getJob_type_name();
                                    String id = mJOnTypeArrayList.get(i).getJob_type_id();
                                    boolean isselected = mJOnTypeArrayList.get(i).isSelected();
                                    FilterModelSelected modelSelected = new FilterModelSelected(name, id, "",isselected,false);
                                    jobTypeSelected.add(jobTypeSelected.size(), modelSelected);
                                }
                                jobtype.setVisibility(View.VISIBLE);
                            } else {
                                jobtype.setVisibility(View.GONE);
                            }
                        } else {
                            jobtype.setVisibility(View.GONE);
                        }
                        if (mLocationArrayList != null) {
                            if (mLocationArrayList.size() != 0) {
                                locationSelected.clear();
                                for (int i = 0; i < mLocationArrayList.size(); i++) {
                                    String name = mLocationArrayList.get(i).getLocation_name();
                                    String id = mLocationArrayList.get(i).getLocation_id();
                                    String state = mLocationArrayList.get(i).getState_name();
                                    boolean isselected = mLocationArrayList.get(i).isSelected();
                                    FilterModelSelected modelSelected = new FilterModelSelected(name, id,state, isselected,false);
                                    locationSelected.add(locationSelected.size(), modelSelected);
                                }
                                location.setVisibility(View.VISIBLE);
                            } else location.setVisibility(View.GONE);
                        } else {
                            location.setVisibility(View.GONE);
                        }

                        if (mDistanceArrayList != null) {
                            if (mDistanceArrayList.size() != 0) {
                                distance.setVisibility(View.VISIBLE);
                                distanceSelected.clear();
                                for (int i = 0; i < mDistanceArrayList.size(); i++) {
                                    String name = mDistanceArrayList.get(i).getDistance_name();
                                    String id = mDistanceArrayList.get(i).getDistance_id();
                                    boolean isselected = mDistanceArrayList.get(i).isSelected();
                                    FilterModelSelected modelSelected = new FilterModelSelected(name, id, "",isselected,true);
                                    distanceSelected.add(distanceSelected.size(), modelSelected);
                                }
                            } else {
                                distance.setVisibility(View.GONE);
                            }
                        } else {
                            distance.setVisibility(View.GONE);
                        }

                        if (mDesignationArrayList != null) {
                            if (mDesignationArrayList.size() != 0) {
                                designationSelected.clear();
                                for (int i = 0; i < mDesignationArrayList.size(); i++) {
                                    String name = mDesignationArrayList.get(i).getDesignation_name();
                                    String id = mDesignationArrayList.get(i).getDesignation_id();
                                    boolean isSelected = mDesignationArrayList.get(i).isSelected();
                                    FilterModelSelected modelSelected = new FilterModelSelected(name, id, "",isSelected,false);
                                    designationSelected.add(designationSelected.size(), modelSelected);
                                }
                                designation.setVisibility(View.VISIBLE);
                            } else {
                                designation.setVisibility(View.GONE);
                            }
                        } else {
                            designation.setVisibility(View.GONE);
                        }


                        if (mQualificationArrayList != null) {
                            if (mQualificationArrayList.size() != 0) {
                                qualificationSelected.clear();
                                for (int i = 0; i < mQualificationArrayList.size(); i++) {
                                    String name = mQualificationArrayList.get(i).getDegree_name();
                                    String id = mQualificationArrayList.get(i).getDegree_id();
                                    boolean isselected = mQualificationArrayList.get(i).isSelected();
                                    FilterModelSelected modelSelected = new FilterModelSelected(name, id,"", isselected,false);
                                    qualificationSelected.add(qualificationSelected.size(), modelSelected);
                                }

                                qualifications.setVisibility(View.VISIBLE);
                            } else {
                                qualifications.setVisibility(View.GONE);
                            }
                        } else {
                            qualifications.setVisibility(View.GONE);
                        }

                        if (mExperience_nameArrayList != null) {
                            if (mExperience_nameArrayList.size() != 0) {
                                expSelected.clear();
                                for (int i = 0; i < mExperience_nameArrayList.size(); i++) {
                                    String name = mExperience_nameArrayList.get(i).getExperience_name();
                                    String id = mExperience_nameArrayList.get(i).getExperience_id();
                                    boolean isselected = mExperience_nameArrayList.get(i).isSelected();
                                    FilterModelSelected modelSelected = new FilterModelSelected(name, id,"", isselected,false);
                                    expSelected.add(expSelected.size(), modelSelected);
                                }
                                experience.setVisibility(View.VISIBLE);
                            } else {
                                experience.setVisibility(View.GONE);
                            }
                        } else {
                            experience.setVisibility(View.GONE);
                        }

                        if (mIndustryArrayList != null) {
                            if (mIndustryArrayList.size() != 0) {
                                industrySelected.clear();
                                for (int i = 0; i < mIndustryArrayList.size(); i++) {
                                    String name = mIndustryArrayList.get(i).getIndustry_name();
                                    String id = mIndustryArrayList.get(i).getIndustry_id();
                                    boolean isselected = mIndustryArrayList.get(i).isSelected();
                                    FilterModelSelected modelSelected = new FilterModelSelected(name, id,"", isselected,false);
                                    industrySelected.add(industrySelected.size(), modelSelected);
                                }
                                industrys.setVisibility(View.VISIBLE);
                            } else {
                                industrys.setVisibility(View.GONE);
                            }
                        } else {
                            industrys.setVisibility(View.GONE);
                        }


                        if (mSalaryArrayList != null) {
                            if (mSalaryArrayList.size() != 0) {
                                salarySelected.clear();
                                for (int i = 0; i < mSalaryArrayList.size(); i++) {
                                    String name = mSalaryArrayList.get(i).getSalary_range();
                                    String id = mSalaryArrayList.get(i).getRange_id();
                                    boolean isselected = mSalaryArrayList.get(i).isSelected();
                                    FilterModelSelected modelSelected = new FilterModelSelected(name, id, "",isselected,false);
                                    salarySelected.add(salarySelected.size(), modelSelected);
                                }

                                salary.setVisibility(View.VISIBLE);
                            } else {
                                salary.setVisibility(View.GONE);
                            }
                        } else {
                            salary.setVisibility(View.GONE);
                        }

                        addDataToDataArray();
                        filterAdapter = new FilterAdapter(MatchFragments.this, data);
                        listview.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                        listview.setAdapter(filterAdapter);
                        callJobTypeSheet();
                        isFilterLoaded = true;

                    }

                } else {
                    AppController.dismissProgressdialog();
                    rest.showToast("Something went wrong");
                }
            }

            @Override
            public void onFailure(@NonNull Call<FilterModel> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });

    }

    private void addDataToDataArray() {
        hideKeyboardFrom(mContext,et_search);
                et_search.setText("");
        resetColor();
        if (jobTypeSelected.size() > 0) {

            data.clear();
            data.addAll(jobTypeSelected);
            adapterAttachedFor = "JobType";
            jobtype.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (locationSelected.size() > 0) {
            data.clear();
            data.addAll(locationSelected);
            adapterAttachedFor = "Location";
            location.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (distanceSelected.size() > 0) {
            data.clear();
            data.addAll(distanceSelected);
            adapterAttachedFor = "Distance";
            distance.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (designationSelected.size() > 0) {
            data.clear();
            data.addAll(designationSelected);
            adapterAttachedFor = "Designation";
            designation.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (qualificationSelected.size() > 0) {
            data.clear();
            data.addAll(qualificationSelected);
            adapterAttachedFor = "Qualification";
            qualifications.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (expSelected.size() > 0) {
            data.clear();
            data.addAll(expSelected);
            adapterAttachedFor = "Experience";
            experience.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (industrySelected.size() > 0) {
            data.clear();
            data.addAll(industrySelected);
            adapterAttachedFor = "Industry";
            industrys.setTextColor(getResources().getColor(R.color.colorPrimary));
        } else if (salarySelected.size() > 0) {
            data.clear();
            data.addAll(salarySelected);
            adapterAttachedFor = "Salary";
            salary.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    public void setSelectedFilterPosition(String pos, boolean set) {

        if (adapterAttachedFor.equalsIgnoreCase("JobType")) {
            for (int i=0;i<jobTypeSelected.size();i++){
                if (pos.equalsIgnoreCase(jobTypeSelected.get(i).getId())){
                    jobTypeSelected.get(i).setSelected(set);
                    break;
                }
            }

        } else if (adapterAttachedFor.equalsIgnoreCase("Location")) {
            for (int i=0;i<locationSelected.size();i++){
                if (pos.equalsIgnoreCase(locationSelected.get(i).getId())){
                    locationSelected.get(i).setSelected(set);
                    break;
                }
            }
        } else if (adapterAttachedFor.equalsIgnoreCase("Distance")) {
            for (int i=0;i<distanceSelected.size();i++){
                if (pos.equalsIgnoreCase(distanceSelected.get(i).getId())){
                    distanceSelected.get(i).setSelected(set);
                    break;
                }
            }
        } else if (adapterAttachedFor.equalsIgnoreCase("Designation")) {
            for (int i=0;i<designationSelected.size();i++){
                if (pos.equalsIgnoreCase(designationSelected.get(i).getId())){
                    designationSelected.get(i).setSelected(set);
                    break;
                }
            }
        } else if (adapterAttachedFor.equalsIgnoreCase("Qualification")) {
            for (int i=0;i<qualificationSelected.size();i++){
                if (pos.equalsIgnoreCase(qualificationSelected.get(i).getId())){
                    qualificationSelected.get(i).setSelected(set);
                    break;
                }
            }
        } else if (adapterAttachedFor.equalsIgnoreCase("Experience")) {
            for (int i=0;i<expSelected.size();i++){
                if (pos.equalsIgnoreCase(expSelected.get(i).getId())){
                    expSelected.get(i).setSelected(set);
                    break;
                }
            }
        } else if (adapterAttachedFor.equalsIgnoreCase("Industry")) {
            for (int i=0;i<industrySelected.size();i++){
                if (pos.equalsIgnoreCase(industrySelected.get(i).getId())){
                    industrySelected.get(i).setSelected(set);
                    break;
                }
            }
        } else if (adapterAttachedFor.equalsIgnoreCase("Salary")) {
            for (int i=0;i<salarySelected.size();i++){
                if (pos.equalsIgnoreCase(salarySelected.get(i).getId())){
                    salarySelected.get(i).setSelected(set);
                    break;
                }
            }
        }

        setSelectedFilterHighlight();

    }

    private void postLikeDislike(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().postJobAcceptReject(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject repo=response.body();
                    if (response.body().get("code").getAsInt()==203){
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        getActivity().finish();
                    }else if (response.body().get("code").getAsInt()==200 &&repo.get("status").getAsBoolean()){
                        rest.showToast(repo.get("message").getAsString());
                        left_swipes=left_swipes-1;
                        if (hashMap.get(ParaName.KEY_USERSTATUS)!=null)
                            if (Objects.requireNonNull(hashMap.get(ParaName.KEY_USERSTATUS)).equalsIgnoreCase("A")){
                                mArrayUser.get(pos).setUser_match_status("A");
                                if (mArrayUser.get(pos).getCompany_match_status() != null) {
                                    if (mArrayUser.get(pos).getCompany_match_status().equalsIgnoreCase("A")) {
                                        mContext.startActivity(new Intent(mContext, JobDetail.class).putExtra("from", MatchFragments.class.getSimpleName()).putExtra("id", mArrayUser.get(pos).getJob_post_id()));
                                    }
                                }
                            }
                    }  else if (response.body().get("code").getAsInt()==401){
                        rest.showToast(repo.get("message").getAsString());
                }


                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }


    private void setSelectedFilterHighlight(){
        ArrayList<String> jobtypeStrings = new ArrayList<>();
        ArrayList<String> locationStrings = new ArrayList<>();
        ArrayList<String> distanceStrings = new ArrayList<>();
        ArrayList<String> designationStrings = new ArrayList<>();
        ArrayList<String> qualificationStrings = new ArrayList<>();
        ArrayList<String> expStrings = new ArrayList<>();
        ArrayList<String> industryStrings = new ArrayList<>();
        ArrayList<String> salaryStrings = new ArrayList<>();
        for (int i = 0; i < jobTypeSelected.size(); i++) {
            if (jobTypeSelected.get(i).isSelected()) {
                jobtypeStrings.add(jobTypeSelected.get(i).getId());
            }
        }
        for (int i = 0; i < locationSelected.size(); i++) {
            if (locationSelected.get(i).isSelected()) {
                locationStrings.add(locationSelected.get(i).getId());
            }
        }
        for (int i = 0; i < distanceSelected.size(); i++) {
            if (distanceSelected.get(i).isSelected()) {
                distanceStrings.add(distanceSelected.get(i).getId());
            }
        }
        for (int i = 0; i < designationSelected.size(); i++) {
            if (designationSelected.get(i).isSelected()) {
                designationStrings.add(designationSelected.get(i).getId());
            }
        }
        for (int i = 0; i < qualificationSelected.size(); i++) {
            if (qualificationSelected.get(i).isSelected()) {
                qualificationStrings.add(qualificationSelected.get(i).getId());
            }
        }
        for (int i = 0; i < expSelected.size(); i++) {
            if (expSelected.get(i).isSelected()) {
                expStrings.add(expSelected.get(i).getId());
            }
        }
        for (int i = 0; i < industrySelected.size(); i++) {
            if (industrySelected.get(i).isSelected()) {
                industryStrings.add(industrySelected.get(i).getId());
            }
        }
        for (int i = 0; i < salarySelected.size(); i++) {
            if (salarySelected.get(i).isSelected()) {
                salaryStrings.add(salarySelected.get(i).getId());
            }
        }
        if (jobtypeStrings.size()>0){
            iv_jobtype.setVisibility(View.VISIBLE);
        }else iv_jobtype.setVisibility(View.GONE);

        if (locationStrings.size()>0){
            iv_location.setVisibility(View.VISIBLE);
        }else iv_location.setVisibility(View.GONE);

        if (distanceStrings.size()>0){
            iv_distance.setVisibility(View.VISIBLE);
        }else iv_distance.setVisibility(View.GONE);

        if (designationStrings.size()>0){
            iv_designation.setVisibility(View.VISIBLE);
        }else iv_designation.setVisibility(View.GONE);

        if (qualificationStrings.size()>0){
            iv_qualifications.setVisibility(View.VISIBLE);
        }else iv_qualifications.setVisibility(View.GONE);

        if (expStrings.size()>0){
            iv_experience.setVisibility(View.VISIBLE);
        }else iv_experience.setVisibility(View.GONE);

        if (industryStrings.size()>0){
            iv_industrys.setVisibility(View.VISIBLE);
        }else iv_industrys.setVisibility(View.GONE);

        if (salaryStrings.size()>0){
            iv_salary.setVisibility(View.VISIBLE);
        }else iv_salary.setVisibility(View.GONE);
    }


    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
