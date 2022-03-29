package com.webnotics.swipee.activity.company;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.Seeker.SearchIndustryActivity;
import com.webnotics.swipee.adapter.company.UserListAdapter;
import com.webnotics.swipee.model.company.CompanyRaderViewModel;
import com.webnotics.swipee.radarview.LatLongCs;
import com.webnotics.swipee.radarview.ObjectModel;
import com.webnotics.swipee.radarview.Radar;
import com.webnotics.swipee.radarview.RadarViewC;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.rest.Rest;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;
import java.util.stream.IntStream;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyNearBy extends AppCompatActivity implements View.OnClickListener {
    Context mContext;
    Rest rest;
    ImageView iv_back, iv_back1;
    Radar radar;
    RelativeLayout rl_allcompany, rl_main;
    ArrayList<ObjectModel> mDataSet = new ArrayList<>();
    RadarViewC mRadarCustom;
    ArrayList<CompanyRaderViewModel.Data.User_Listing> mArrayListUserListing = new ArrayList<>();
    RecyclerView rv_company;
    BubbleSeekBar bubbleSeekBar;
    int radiessprogress = 5;
    String industys = "";
    TextView viewalltext, nodatatxt, tv_search;
    String id = "";
    @SuppressLint("StaticFieldLeak")
    public static CompanyNearBy instance;
    private View mCenterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_near_by);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));


        mContext = this;
        rest = new Rest(mContext);
        instance = this;
        iv_back = findViewById(R.id.iv_back);
        iv_back1 = findViewById(R.id.iv_back1);
        rv_company = findViewById(R.id.rv_company);
        mRadarCustom = findViewById(R.id.mRadarCustom);
        rl_allcompany = findViewById(R.id.rl_allcompany);
        rl_main = findViewById(R.id.rl_main);
        rl_allcompany.setVisibility(View.GONE);
        bubbleSeekBar = findViewById(R.id.seekbar);
        bubbleSeekBar.setProgress(radiessprogress);
        viewalltext = findViewById(R.id.tv_viewAll);
        RelativeLayout nodatalay = findViewById(R.id.nolay);
        nodatatxt = findViewById(R.id.nodatatxt);
        tv_search = findViewById(R.id.tv_search);
        radar = findViewById(R.id.radar);
        radar.start();
        viewalltext.setVisibility(View.GONE);
        viewalltext.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        tv_search.setHint(Config.GetIndustry());
        bubbleSeekBar.setProgress(10);
        if (Config.GetPACKAGEEXP().equalsIgnoreCase("N")) {
            industys = Config.GetIndustryId();
            nodatalay.setVisibility(View.GONE);
            AppController.ShowDialogue("", mContext);
            getCompanySearchRadarView(String.valueOf(radiessprogress), industys);
        } else {
            nodatalay.setVisibility(View.VISIBLE);
            nodatatxt.setText(R.string.packageexpseekr);
        }
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mCenterView = inflater.inflate(R.layout.layout_center, null);
        ImageView img_profile = mCenterView.findViewById(R.id.img_profile);
        Glide.with(mContext).load(Config.GetPICKURI()).error(R.drawable.ic_profile_select).into(img_profile);
        iv_back.setOnClickListener(v -> onBackPressed());
        iv_back1.setOnClickListener(v -> rl_allcompany.setVisibility(View.GONE));
        bubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                radiessprogress = progress;
                if (fromUser) {
                    if (progress <= 15) bubbleSeekBar.setProgress(10);
                    else if (progress <= 25) bubbleSeekBar.setProgress(20);
                    else if (progress <= 35) bubbleSeekBar.setProgress(30);
                    else if (progress <= 45) bubbleSeekBar.setProgress(40);
                    else if (progress <= 55) bubbleSeekBar.setProgress(50);
                    else if (progress <= 65) bubbleSeekBar.setProgress(60);
                    else if (progress <= 75) bubbleSeekBar.setProgress(70);
                    else if (progress <= 85) bubbleSeekBar.setProgress(80);
                    else if (progress <= 95) bubbleSeekBar.setProgress(90);
                    else if (progress <= 100) bubbleSeekBar.setProgress(100);
                }
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                radiessprogress = progress;
                if (progress <= 15) {
                    bubbleSeekBar.setProgress(10);
                    AppController.ShowDialogue("", mContext);
                    getCompanySearchRadarView(String.valueOf(10), industys);
                } else if (progress <= 25) {
                    bubbleSeekBar.setProgress(20);
                    AppController.ShowDialogue("", mContext);
                    getCompanySearchRadarView(String.valueOf(20), industys);
                } else if (progress <= 35) {
                    bubbleSeekBar.setProgress(30);
                    AppController.ShowDialogue("", mContext);
                    getCompanySearchRadarView(String.valueOf(30), industys);
                } else if (progress <= 45) {
                    bubbleSeekBar.setProgress(40);
                    AppController.ShowDialogue("", mContext);
                    getCompanySearchRadarView(String.valueOf(40), industys);
                } else if (progress <= 55) {
                    bubbleSeekBar.setProgress(50);
                    AppController.ShowDialogue("", mContext);
                    getCompanySearchRadarView(String.valueOf(50), industys);
                } else if (progress <= 65) {
                    bubbleSeekBar.setProgress(60);
                    AppController.ShowDialogue("", mContext);
                    getCompanySearchRadarView(String.valueOf(60), industys);
                } else if (progress <= 75) {
                    bubbleSeekBar.setProgress(70);
                    AppController.ShowDialogue("", mContext);
                    getCompanySearchRadarView(String.valueOf(70), industys);
                } else if (progress <= 85) {
                    bubbleSeekBar.setProgress(80);
                    AppController.ShowDialogue("", mContext);
                    getCompanySearchRadarView(String.valueOf(80), industys);
                } else if (progress <= 95) {
                    bubbleSeekBar.setProgress(90);
                    AppController.ShowDialogue("", mContext);
                    getCompanySearchRadarView(String.valueOf(90), industys);
                } else if (progress <= 100) {
                    bubbleSeekBar.setProgress(100);
                    AppController.ShowDialogue("", mContext);
                    getCompanySearchRadarView(String.valueOf(100), industys);
                }
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_search:
                Intent intent = new Intent(mContext, SearchIndustryActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_viewAll:
                rl_allcompany.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        instance=null;
        super.onDestroy();
    }

    private void getCompanySearchRadarView(String radius, String industys) {
        SwipeeApiClient.swipeeServiceInstance().getCompanyRadar(Config.GetUserToken(), industys, radius).enqueue(new Callback<CompanyRaderViewModel>() {
            @Override
            public void onResponse(@NonNull Call<CompanyRaderViewModel> call, @NonNull Response<CompanyRaderViewModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    CompanyRaderViewModel userRaderView = (CompanyRaderViewModel) response.body();
                    if (userRaderView.getCode() == 200 && userRaderView.getData().getUsers_listing() != null) {
                        mArrayListUserListing = userRaderView.getData().getUsers_listing();
                        loadTempData(mCenterView);
                        if (userRaderView.getData().getUsers_listing().size() > 0) {
                            rv_company.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            UserListAdapter stateAdapter = new UserListAdapter(mContext, mArrayListUserListing);
                            rv_company.setAdapter(stateAdapter);
                            viewalltext.setVisibility(View.VISIBLE);
                        } else viewalltext.setVisibility(View.GONE);
                    } else if (userRaderView.getCode() == 203) {
                        rest.showToast(userRaderView.getMessage());
                        AppController.loggedOut(mContext);
                        finish();
                    } else Toast.makeText(mContext, userRaderView.getMessage(), Toast.LENGTH_SHORT).show();
                } else rest.showToast("Something went wrong");
            }

            @Override
            public void onFailure(@NonNull Call<CompanyRaderViewModel> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });

    }

    public void setIndustryData(String name, String id) {
        if (tv_search != null) {
            tv_search.setHint("");
            tv_search.setText(name);
            industys = id;
            AppController.ShowDialogue("", mContext);
            getCompanySearchRadarView(String.valueOf(radiessprogress), industys);
        }
    }

    private void loadTempData(View mCenterView) {
        mDataSet.clear();
        mRadarCustom.removeAllViews();


        LatLongCs latLongCs = new LatLongCs(
                Double.parseDouble(Config.GetLat()),
                Double.parseDouble(Config.GetLongg()));
        if (mArrayListUserListing.size() > 10) {
            IntStream.range(0, 10).forEach(i -> {
                CircleImageView t1 = new CircleImageView(mContext);
                t1.setLayoutParams(new ViewGroup.LayoutParams(52, 52));
                if (!isFinishing()) {
                    Glide.with(mContext)
                            .load(mArrayListUserListing.get(i).getUser_profile())
                            .apply(new RequestOptions().placeholder(R.drawable.ic_profile_select).error(R.drawable.ic_profile_select).override(80, 80))
                            .into(t1);
                }
                if (mArrayListUserListing.get(i).getUser_radius() != null) {
                    if (mArrayListUserListing.get(i).getUser_radius().equalsIgnoreCase("")) {
                        mDataSet.add(new ObjectModel(Double.parseDouble(mArrayListUserListing.get(i).getLatitude()),
                                Double.parseDouble(mArrayListUserListing.get(i).getLongitude()),
                                Double.parseDouble("10"), t1, mArrayListUserListing.get(i).getUser_id()));
                    } else {
                        mDataSet.add(new ObjectModel(Double.parseDouble(mArrayListUserListing.get(i).getLatitude()),
                                Double.parseDouble(mArrayListUserListing.get(i).getLongitude()),
                                Double.parseDouble(mArrayListUserListing.get(i).getUser_radius()), t1, mArrayListUserListing.get(i).getUser_id()));
                    }
                } else {
                    mDataSet.add(new ObjectModel(Double.parseDouble(mArrayListUserListing.get(i).getLatitude()),
                            Double.parseDouble(mArrayListUserListing.get(i).getLongitude()),
                            Double.parseDouble("10"), t1, mArrayListUserListing.get(i).getUser_id()));
                }
            });
        } else {
            IntStream.range(0, mArrayListUserListing.size()).forEach(i -> {
                CircleImageView t1 = new CircleImageView(mContext);
                t1.setLayoutParams(new ViewGroup.LayoutParams(52, 52));
                if (!isFinishing()) {
                    Glide.with(mContext)
                            .load(mArrayListUserListing.get(i).getUser_profile())
                            .apply(new RequestOptions().placeholder(R.drawable.ic_profile_select).error(R.drawable.ic_profile_select).override(80, 80))
                            .into(t1);
                }
                if (mArrayListUserListing.get(i).getUser_radius() != null) {
                    if (mArrayListUserListing.get(i).getUser_radius().equalsIgnoreCase("")) {
                        mDataSet.add(new ObjectModel(Double.parseDouble(mArrayListUserListing.get(i).getLatitude()),
                                Double.parseDouble(mArrayListUserListing.get(i).getLongitude()),
                                Double.parseDouble("10"), t1, mArrayListUserListing.get(i).getUser_id()));
                    } else {
                        mDataSet.add(new ObjectModel(Double.parseDouble(mArrayListUserListing.get(i).getLatitude()),
                                Double.parseDouble(mArrayListUserListing.get(i).getLongitude()),
                                Double.parseDouble(mArrayListUserListing.get(i).getUser_radius()), t1, mArrayListUserListing.get(i).getUser_id()));
                    }
                } else {
                    mDataSet.add(new ObjectModel(Double.parseDouble(mArrayListUserListing.get(i).getLatitude()),
                            Double.parseDouble(mArrayListUserListing.get(i).getLongitude()),
                            Double.parseDouble("10"), t1, mArrayListUserListing.get(i).getUser_id()));
                }
            });
        }

        mRadarCustom.setupData(Double.parseDouble("" + radiessprogress * 2000), mDataSet, latLongCs, mCenterView);

        mRadarCustom.setUpCallBack((objectModel, view) -> {
            id = ((ObjectModel) objectModel).getUserid();
            String jobid = "";
            String name = "";
            for (int i = 0; i < mArrayListUserListing.size(); i++) {
                if (mArrayListUserListing.get(i).getUser_id().equalsIgnoreCase(id)) {
                    jobid = mArrayListUserListing.get(i).getJob_id();
                    name = mArrayListUserListing.get(i).getFirst_name();
                    break;
                }
            }
            String from = CompanyNearBy.class.getSimpleName();
            mContext.startActivity(new Intent(mContext, UserDetail.class).putExtra("from", from).
                    putExtra("id", id).putExtra("job_id", jobid).putExtra("name", name));
        });

    }
}