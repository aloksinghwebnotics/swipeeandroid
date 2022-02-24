package com.webnotics.swipee.fragments.company;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.potyvideo.library.AndExoPlayerView;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.company.CompanyEditProfile;
import com.webnotics.swipee.activity.company.CompanyHomeActivity;
import com.webnotics.swipee.fragments.Basefragment;
import com.webnotics.swipee.model.company.CompanyProfileModel;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CompanyProfileFragments extends Basefragment implements View.OnClickListener {

    Context mContext;
    Rest rest;
    CircleImageView civ_profile;
    TextView tv_companyname, tv_companyaddress, tv_edit, tv_industry, tv_companysize, tv_website, tv_founded, tv_video;
    ImageView iv_video, iv_videoplay;
    private String pincode = "";
    private String address = "";
    private String civ_url = "";
    @SuppressLint("StaticFieldLeak")
    public static CompanyProfileFragments instance;

    public boolean isHit = false;
    RelativeLayout rl_video;
    private String videoUrl = "";
    private AndExoPlayerView vv_video;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.company_profile_screen, container, false);
        mContext = getActivity();
        rest = new Rest(mContext);
        instance = this;

        civ_profile = rootView.findViewById(R.id.civ_profile);
        vv_video = rootView.findViewById(R.id.vv_video);
        tv_companyname = rootView.findViewById(R.id.tv_companyname);
        tv_companyaddress = rootView.findViewById(R.id.tv_companyaddress);
        tv_edit = rootView.findViewById(R.id.tv_edit);
        tv_industry = rootView.findViewById(R.id.tv_industry);
        tv_companysize = rootView.findViewById(R.id.tv_companysize);
        tv_website = rootView.findViewById(R.id.tv_website);
        tv_founded = rootView.findViewById(R.id.tv_founded);
        iv_video = rootView.findViewById(R.id.iv_video);
        iv_videoplay = rootView.findViewById(R.id.iv_videoplay);
        rl_video = rootView.findViewById(R.id.rl_video);
        tv_video = rootView.findViewById(R.id.tv_video);

        tv_edit.setOnClickListener(this);
        civ_profile.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(civ_url))
                AppController.callFullImage(mContext, civ_url);
        });
        iv_videoplay.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(videoUrl))
             //   VideoPlayerActivity.start(getActivity(),videoUrl);
              callPlayer(videoUrl);
        });

        if (CompanyHomeActivity.instance != null) {
            if (CompanyHomeActivity.instance.employeeuserdetail != null) {
                CompanyProfileModel employeeuserdetail = CompanyHomeActivity.instance.employeeuserdetail;
                if (employeeuserdetail.isStatus()) {
                    setDataResponse(employeeuserdetail);
                } else {
                    hitService();
                }
            } else hitService();
        } else {
            hitService();
        }
        return rootView;
    }

    private void setDataResponse(CompanyProfileModel employeeuserdetail) {
        if (employeeuserdetail.getCode() == 200 && employeeuserdetail.getData() != null) {
            Config.SetEmail(employeeuserdetail.getData().getCompany_email());
            Config.SetMobileNo(employeeuserdetail.getData().getMobile());
            Config.SetPhoneCode(employeeuserdetail.getData().getPhone_code());
            Config.SetCountry(employeeuserdetail.getData().getCountry());
            Config.SetCountryName(employeeuserdetail.getData().getCountry());
            Config.SetStateName(employeeuserdetail.getData().getState());
            Config.SetCityName(employeeuserdetail.getData().getCity());
            Config.SetState(employeeuserdetail.getData().getState());
            Config.SetCity(employeeuserdetail.getData().getCity());
            Config.SetStateId(employeeuserdetail.getData().getState_id());
            Config.SetCityId(employeeuserdetail.getData().getCity_id());
            Config.SetCountryId(employeeuserdetail.getData().getCountry_id());
            pincode = employeeuserdetail.getData().getCompany_pincode();
            address = employeeuserdetail.getData().getCompany_address();
            Config.SetEmailVERIFY(true);
            Config.SetMobileVERIFY(true);
            Config.SetIsUserLogin(true);
            Config.SetId(employeeuserdetail.getData().getCompany_id());
            Config.SetIndustryId(employeeuserdetail.getData().getIndustry_id());
            Config.SetCompanyName(employeeuserdetail.getData().getCompany_name());
            Config.SetLat(employeeuserdetail.getData().getLatitude());
            Config.SetPICKURI(employeeuserdetail.getData().getCompany_logo());
            Config.SetLongg(employeeuserdetail.getData().getLongitude());
            Config.SetIndustry(employeeuserdetail.getData().getIndustry_name());
            Config.SetRadiuss(String.valueOf(employeeuserdetail.getData().getDefault_radius()));
            vv_video.setVisibility(View.GONE);
            civ_url = employeeuserdetail.getData().getCompany_logo();
            Glide.with(mContext)
                    .load(employeeuserdetail.getData().getCompany_logo())
                    .apply(new RequestOptions().placeholder(R.drawable.ic_profile_select).error(R.drawable.ic_profile_select))
                    .into(civ_profile);
            tv_companyname.setText(employeeuserdetail.getData().getCompany_name());
            tv_industry.setText(employeeuserdetail.getData().getIndustry_name());
            tv_companysize.setText(employeeuserdetail.getData().getCompany_size());
            tv_founded.setText(employeeuserdetail.getData().getCompany_founded());
            tv_website.setText(employeeuserdetail.getData().getCompany_url());
            tv_companyaddress.setText(String.format("%s, %s, %s", address, employeeuserdetail.getData().getCity(), employeeuserdetail.getData().getState()));
            if (employeeuserdetail.getData().getCompany_video() != null && !TextUtils.isEmpty(employeeuserdetail.getData().getCompany_video())) {
                videoUrl = employeeuserdetail.getData().getCompany_video();
                Glide.with(mContext)
                        .load(employeeuserdetail.getData().getCompany_video())
                        .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * 16))))
                        .into(iv_video);
                rl_video.setVisibility(View.VISIBLE);
                tv_video.setVisibility(View.VISIBLE);
            } else {
                tv_video.setVisibility(View.GONE);
                rl_video.setVisibility(View.GONE);
            }

        } else {
            rest.showToast(employeeuserdetail.getMessage());
        }
    }


    @Override
    public int setContentView() {
        return R.layout.company_profile_screen;
    }

    @Override
    protected void backPressed() {
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.tv_edit:
                mContext.startActivity(new Intent(mContext, CompanyEditProfile.class)
                        .putExtra("company_size", tv_companysize.getText().toString())
                        .putExtra("website", tv_website.getText().toString())
                        .putExtra("founded", tv_founded.getText().toString())
                        .putExtra("pincode", pincode)
                        .putExtra("address", address)
                        .putExtra("videoUrl", videoUrl)
                );
                break;

            default:
                break;
        }

    }


    @Override
    public void onPause() {
        if (vv_video!=null)
            vv_video.pausePlayer();
        super.onPause();
    }

    private void callPlayer(String url) {
        vv_video.setSource(url);
        vv_video.setVisibility(View.VISIBLE);
        rl_video.setVisibility(View.GONE);
    }

    public void hitService() {
        isHit = false;
        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            getProfileData();
        } else rest.AlertForInternet();
    }

    @Override
    public void onResume() {
        if (isHit)
            hitService();

        super.onResume();
    }

    private void getProfileData() {
        SwipeeApiClient.swipeeServiceInstance().companyDetail(Config.GetUserToken()).enqueue(new Callback<CompanyProfileModel>() {
            @Override
            public void onResponse(@NonNull Call<CompanyProfileModel> call, @NonNull Response<CompanyProfileModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().getCode() == 203) {
                        rest.showToast(response.body().getMessage());
                        AppController.loggedOut(mContext);
                        getActivity().finish();
                    } else if (response.body().getCode() == 200 && response.body().isStatus()) {
                        if (CompanyHomeActivity.instance != null)
                            CompanyHomeActivity.instance.employeeuserdetail = response.body();
                        setDataResponse(response.body());
                    } else {
                        rest.showToast(response.body().getMessage());
                    }
                } else {
                    rest.showToast("Something went wrong");
                }

            }

            @Override
            public void onFailure(@NonNull Call<CompanyProfileModel> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
