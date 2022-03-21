package com.webnotics.swipee.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.potyvideo.library.AndExoPlayerView;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.model.company.CompanyProfileModel;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyProfile extends AppCompatActivity {
    Context mContext;
    Rest rest;
    CircleImageView civ_profile;
    TextView tv_companyname,tv_video,tv_industry,tv_companysize,tv_website,tv_founded,tv_companyaddress,tv_companyemail,tv_companymobile;
    ImageView iv_back,iv_video,iv_videoplay;
    AndExoPlayerView vv_video;
    LinearLayout ll_main;
    RelativeLayout rl_video;
    private String civ_url="";
    private String uri="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext=this;
        rest=new Rest(mContext);

        civ_profile=findViewById(R.id.civ_profile);
        iv_back=findViewById(R.id.iv_back);
        vv_video=findViewById(R.id.vv_video);
        tv_companyname=findViewById(R.id.tv_companyname);
        tv_industry=findViewById(R.id.tv_industry);
        tv_companysize=findViewById(R.id.tv_companysize);
        tv_website=findViewById(R.id.tv_website);
        tv_founded=findViewById(R.id.tv_founded);
        tv_companyaddress=findViewById(R.id.tv_companyaddress);
        ll_main=findViewById(R.id.ll_main);
        iv_video=findViewById(R.id.iv_video);
        iv_videoplay=findViewById(R.id.iv_videoplay);
        rl_video=findViewById(R.id.rl_video);
        tv_video=findViewById(R.id.tv_video);
        tv_companyemail=findViewById(R.id.tv_companyemail);
        tv_companymobile=findViewById(R.id.tv_companymobile);
        if (getIntent() != null) {
            String company_id = getIntent().getStringExtra("company_id") != null ? getIntent().getStringExtra("company_id") : "";
            if (rest.isInterentAvaliable()) {
                AppController.ShowDialogue("", mContext);
                if (Config.isSeeker())
                getProfileData(company_id);
                else getProfileData();
            } else rest.AlertForInternet();
        }

        civ_profile.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(civ_url))
                AppController.callFullImage(mContext,civ_url);
        });

        iv_videoplay.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(uri)){
                vv_video.setSource(uri);
                vv_video.setVisibility(View.VISIBLE);
                rl_video.setVisibility(View.GONE);
            }

        });
        iv_back.setOnClickListener(v -> finish());
    }


    @Override
    protected void onPause() {
        if (vv_video!=null) vv_video.pausePlayer();
        super.onPause();
    }

    private void getProfileData(String company_id) {
        SwipeeApiClient.swipeeServiceInstance().companyDetailById(Config.GetUserToken(),company_id).enqueue(new Callback<CompanyProfileModel>() {
            @Override
            public void onResponse(@NonNull Call<CompanyProfileModel> call, @NonNull Response<CompanyProfileModel> response) {
                AppController.dismissProgressdialog();
                parsResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<CompanyProfileModel> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });

    }

    private void getProfileData() {
        SwipeeApiClient.swipeeServiceInstance().companyDetail(Config.GetUserToken()).enqueue(new Callback<CompanyProfileModel>() {
            @Override
            public void onResponse(@NonNull Call<CompanyProfileModel> call, @NonNull Response<CompanyProfileModel> response) {
                AppController.dismissProgressdialog();
                parsResponse(response);
            }

            @Override
            public void onFailure(@NonNull Call<CompanyProfileModel> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }

    private void parsResponse(Response<CompanyProfileModel> response) {
        if (response.code() == 200 && response.body() != null) {
            if (response.body().getCode()==203){
                rest.showToast(response.body().getMessage());
                AppController.loggedOut(mContext);
                onBackPressed();
            }else if (response.body().isStatus()){
                CompanyProfileModel employeeuserdetail = response.body();
                if (employeeuserdetail.getCode() == 200 && employeeuserdetail.getData()!=null) {
                    String state=employeeuserdetail.getData().getState();
                    String city=employeeuserdetail.getData().getCity();
                    String address=employeeuserdetail.getData().getCompany_address();
                    tv_companyaddress.setText(String.format("%s, %s, %s", address, city, state));
                    civ_url=employeeuserdetail.getData().getCompany_logo();
                    Glide.with(mContext)
                            .load(employeeuserdetail.getData().getCompany_logo())
                            .apply(new RequestOptions().placeholder(R.drawable.ic_pick).error(R.drawable.ic_pick))
                            .into(civ_profile);
                    tv_companyname.setText(employeeuserdetail.getData().getCompany_name());
                    tv_industry.setText(employeeuserdetail.getData().getIndustry_name());
                    tv_companysize.setText(employeeuserdetail.getData().getCompany_size());
                    tv_founded.setText(employeeuserdetail.getData().getCompany_founded());
                    tv_website.setText(employeeuserdetail.getData().getCompany_url());
                    tv_companymobile.setText(String.format("%s%s", employeeuserdetail.getData().getPhone_code(), employeeuserdetail.getData().getMobile()));
                    tv_companyemail.setText(employeeuserdetail.getData().getCompany_email());
                    uri=employeeuserdetail.getData().getCompany_video();
                    if (employeeuserdetail.getData().getCompany_video()!=null && !TextUtils.isEmpty(employeeuserdetail.getData().getCompany_video())){
                        Glide.with(mContext)
                                .load(employeeuserdetail.getData().getCompany_video())
                                .transform(new MultiTransformation(new CenterCrop(),new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density*16))))
                                .into(iv_video);
                        rl_video.setVisibility(View.VISIBLE);
                        tv_video.setVisibility(View.VISIBLE);
                    }else{
                        tv_video.setVisibility(View.GONE);
                        rl_video.setVisibility(View.GONE);
                    }
                    ll_main.setVisibility(View.VISIBLE);
                } else rest.showToast(response.body().getMessage());
            }else rest.showToast(response.body().getMessage());
        } else rest.showToast("Something went wrong");
    }
}