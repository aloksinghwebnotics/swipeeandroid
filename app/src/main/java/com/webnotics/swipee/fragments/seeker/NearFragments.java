package com.webnotics.swipee.fragments.seeker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.Seeker.JobListActivity;
import com.webnotics.swipee.activity.Seeker.SearchIndustryActivity;
import com.webnotics.swipee.activity.Seeker.SeekerHomeActivity;
import com.webnotics.swipee.adapter.seeeker.CompanyListAdapter;
import com.webnotics.swipee.fragments.Basefragment;
import com.webnotics.swipee.model.seeker.UserRaderViewModel;
import com.webnotics.swipee.radarview.LatLongCs;
import com.webnotics.swipee.radarview.ObjectModel;
import com.webnotics.swipee.radarview.Radar;
import com.webnotics.swipee.radarview.RadarViewC;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.rest.Rest;
import com.xw.repo.BubbleSeekBar;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NearFragments extends Basefragment implements View.OnClickListener {
    Context mContext;
    Rest rest;
    ImageView iv_back, iv_back1;
    Radar radar;
    RelativeLayout rl_allcompany, rl_main;
    ArrayList<ObjectModel> mDataSet = new ArrayList<>();
    RadarViewC mRadarCustom;
    ArrayList<UserRaderViewModel.Data.Companies_Listing> mArrayLisCompaniesListing = new ArrayList<>();
    RecyclerView rv_company;
    BubbleSeekBar bubbleSeekBar;
    int radiessprogress = 10;
    String industys = "";
    TextView viewalltext, nodatatxt, tv_search;
    String id = "";
    public static NearFragments instance;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.near_screen, container, false);
        mContext = getActivity();
        rest = new Rest(mContext);
        instance = this;
        iv_back = rootView.findViewById(R.id.iv_back);
        iv_back1 = rootView.findViewById(R.id.iv_back1);
        rv_company = rootView.findViewById(R.id.rv_company);
        mRadarCustom = rootView.findViewById(R.id.mRadarCustom);
        rl_allcompany = rootView.findViewById(R.id.rl_allcompany);
        rl_main = rootView.findViewById(R.id.rl_main);
        rl_allcompany.setVisibility(View.GONE);
        bubbleSeekBar = rootView.findViewById(R.id.seekbar);
        bubbleSeekBar.setProgress(radiessprogress);
        viewalltext = rootView.findViewById(R.id.tv_viewAll);
        RelativeLayout nodatalay = rootView.findViewById(R.id.nolay);
        nodatatxt = rootView.findViewById(R.id.nodatatxt);
        tv_search = rootView.findViewById(R.id.tv_search);
        radar = rootView.findViewById(R.id.radar);
        radar.start();
        viewalltext.setVisibility(View.GONE);
        viewalltext.setOnClickListener(this);
        tv_search.setOnClickListener(this);
        bubbleSeekBar.setProgress(10);
        if (Config.GetPACKAGEEXP().equalsIgnoreCase("N")) {
            nodatalay.setVisibility(View.GONE);
            AppController.ShowDialogue("", mContext);
            getUserSearchRadarView(String.valueOf(radiessprogress), industys);
        } else {
            nodatalay.setVisibility(View.VISIBLE);
            nodatatxt.setText(R.string.packageexpseekr);
        }

        iv_back.setOnClickListener(v -> {
            backPressed();
        });
        iv_back1.setOnClickListener(v -> {
            rl_allcompany.setVisibility(View.GONE);
        });
        bubbleSeekBar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                radiessprogress = progress;
                if (fromUser) {
                    if (progress <= 15) {
                        bubbleSeekBar.setProgress(10);
                    } else if (progress <= 25) {
                        bubbleSeekBar.setProgress(20);
                    } else if (progress <= 35) {
                        bubbleSeekBar.setProgress(30);
                    } else if (progress <= 45) {
                        bubbleSeekBar.setProgress(40);
                    } else if (progress <= 55) {
                        bubbleSeekBar.setProgress(50);
                    } else if (progress <= 65) {
                        bubbleSeekBar.setProgress(60);
                    } else if (progress <= 75) {
                        bubbleSeekBar.setProgress(70);
                    } else if (progress <= 85) {
                        bubbleSeekBar.setProgress(80);
                    } else if (progress <= 95) {
                        bubbleSeekBar.setProgress(90);
                    } else if (progress <= 100) {
                        bubbleSeekBar.setProgress(100);
                    }
                }
            }

            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                radiessprogress = progress;
                 if (progress <= 15) {
                    bubbleSeekBar.setProgress(10);
                    AppController.ShowDialogue("", mContext);
                    getUserSearchRadarView(String.valueOf(10), industys);
                } else if (progress <= 25) {
                    bubbleSeekBar.setProgress(20);
                    AppController.ShowDialogue("", mContext);
                    getUserSearchRadarView(String.valueOf(20), industys);
                } else if (progress <= 35) {
                    bubbleSeekBar.setProgress(30);
                    AppController.ShowDialogue("", mContext);
                    getUserSearchRadarView(String.valueOf(30), industys);
                } else if (progress <= 45) {
                    bubbleSeekBar.setProgress(40);
                    AppController.ShowDialogue("", mContext);
                    getUserSearchRadarView(String.valueOf(40), industys);
                } else if (progress <= 55) {
                    bubbleSeekBar.setProgress(50);
                    AppController.ShowDialogue("", mContext);
                    getUserSearchRadarView(String.valueOf(50), industys);
                } else if (progress <= 65) {
                    bubbleSeekBar.setProgress(60);
                    AppController.ShowDialogue("", mContext);
                    getUserSearchRadarView(String.valueOf(60), industys);
                } else if (progress <= 75) {
                    bubbleSeekBar.setProgress(70);
                    AppController.ShowDialogue("", mContext);
                    getUserSearchRadarView(String.valueOf(70), industys);
                } else if (progress <= 85) {
                    bubbleSeekBar.setProgress(80);
                    AppController.ShowDialogue("", mContext);
                    getUserSearchRadarView(String.valueOf(80), industys);
                } else if (progress <= 95) {
                    bubbleSeekBar.setProgress(90);
                    AppController.ShowDialogue("", mContext);
                    getUserSearchRadarView(String.valueOf(90), industys);
                } else if (progress <= 100) {
                    bubbleSeekBar.setProgress(100);
                    AppController.ShowDialogue("", mContext);
                    getUserSearchRadarView(String.valueOf(100), industys);
                }
            }

            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {

            }
        });
        return rootView;
    }

    private void getUserSearchRadarView(String radius, String industys) {
        SwipeeApiClient.swipeeServiceInstance().getUserRadar(Config.GetUserToken(), industys, radius).enqueue(new Callback<UserRaderViewModel>() {
            @Override
            public void onResponse(@NonNull Call<UserRaderViewModel> call, @NonNull Response<UserRaderViewModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    UserRaderViewModel userRaderView = (UserRaderViewModel) response.body();
                    if (userRaderView.getCode() == 200 && userRaderView.getData().getCompanies_Listing() != null) {
                        mArrayLisCompaniesListing = userRaderView.getData().getCompanies_Listing();
                        loadTempData();
                        if (userRaderView.getData().getCompanies_Listing().size() > 0) {
                            rv_company.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            CompanyListAdapter stateAdapter = new CompanyListAdapter(mContext, mArrayLisCompaniesListing);
                            rv_company.setAdapter(stateAdapter);
                            viewalltext.setVisibility(View.VISIBLE);
                        } else viewalltext.setVisibility(View.GONE);
                    } else  if (userRaderView.getCode()==203){
                        rest.showToast(userRaderView.getMessage());
                        AppController.loggedOut(mContext);
                        getActivity().finish();
                    }else {
                        Toast.makeText(mContext, userRaderView.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    AppController.dismissProgressdialog();
                    rest.showToast("Something went wrong");
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserRaderViewModel> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });

    }

    @Override
    public int setContentView() {
        return R.layout.near_screen;
    }

    @Override
    protected void backPressed() {
        if (rl_allcompany.getVisibility() == View.VISIBLE) {
            rl_allcompany.setVisibility(View.GONE);
        } else {
            rl_main.setVisibility(View.GONE);
            radar.stop();
            if (SeekerHomeActivity.instance != null) {
                SeekerHomeActivity.instance.setMatchFragment();
            }
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                Intent intent = new Intent(mContext, SearchIndustryActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_viewAll:
                rl_allcompany.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void setIndustryData(String name, String id) {
        if (tv_search != null) {
            tv_search.setText(name);
            industys = id;
            AppController.ShowDialogue("", mContext);
            getUserSearchRadarView(String.valueOf(radiessprogress), industys);
        }
    }

    private void loadTempData() {
        mDataSet.clear();
        mRadarCustom.removeAllViews();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View mCenterView = inflater.inflate(R.layout.layout_center, null);
        ImageView img_profile=mCenterView.findViewById(R.id.img_profile);
        Glide.with(mContext).load(Config.GetPICKURI()).error(R.drawable.ic_profile_select).into(img_profile);

        LatLongCs latLongCs = new LatLongCs(
                Double.parseDouble(Config.GetLat()),
                Double.parseDouble(Config.GetLongg()));
        if (mArrayLisCompaniesListing.size() > 10) {
            for (int i = 0; i < 10; i++) {
                de.hdodenhof.circleimageview.CircleImageView t1 = new de.hdodenhof.circleimageview.CircleImageView(mContext);
                t1.setLayoutParams(new ViewGroup.LayoutParams(52, 52));
                if (!getActivity().isFinishing()) {
                    Glide.with(mContext)
                            .load(mArrayLisCompaniesListing.get(i).getCompany_logo())
                            .apply(new RequestOptions().placeholder(R.drawable.ic_profile_select).error(R.drawable.ic_profile_select).override(80, 80))
                            .into(t1);
                }
                if (mArrayLisCompaniesListing.get(i).getCompany_radius() != null) {
                    if (mArrayLisCompaniesListing.get(i).getCompany_radius().equalsIgnoreCase("")) {
                        mDataSet.add(new ObjectModel(Double.parseDouble(mArrayLisCompaniesListing.get(i).getCompany_latitude()),
                                Double.parseDouble(mArrayLisCompaniesListing.get(i).getCompany_longitude()),
                                Double.parseDouble("10"), t1, mArrayLisCompaniesListing.get(i).getCompany_id()));

                    } else {
                        mDataSet.add(new ObjectModel(Double.parseDouble(mArrayLisCompaniesListing.get(i).getCompany_latitude()),
                                Double.parseDouble(mArrayLisCompaniesListing.get(i).getCompany_longitude()),
                                Double.parseDouble(mArrayLisCompaniesListing.get(i).getCompany_radius()), t1, mArrayLisCompaniesListing.get(i).getCompany_id()));

                    }
                } else {
                    mDataSet.add(new ObjectModel(Double.parseDouble(mArrayLisCompaniesListing.get(i).getCompany_latitude()),
                            Double.parseDouble(mArrayLisCompaniesListing.get(i).getCompany_longitude()),
                            Double.parseDouble("10"), t1, mArrayLisCompaniesListing.get(i).getCompany_id()));

                }

            }
        } else {
            for (int i = 0; i < mArrayLisCompaniesListing.size(); i++) {
                de.hdodenhof.circleimageview.CircleImageView t1 = new de.hdodenhof.circleimageview.CircleImageView(mContext);
                t1.setLayoutParams(new ViewGroup.LayoutParams(52, 52));
                if (!getActivity().isFinishing()) {
                    Glide.with(mContext)
                            .load(mArrayLisCompaniesListing.get(i).getCompany_logo())
                            .apply(new RequestOptions().placeholder(R.drawable.ic_profile_select).error(R.drawable.ic_profile_select).override(80, 80))
                            .into(t1);
                }

                if (mArrayLisCompaniesListing.get(i).getCompany_radius() != null) {
                    if (mArrayLisCompaniesListing.get(i).getCompany_radius().equalsIgnoreCase("")) {
                        mDataSet.add(new ObjectModel(Double.parseDouble(mArrayLisCompaniesListing.get(i).getCompany_latitude()),
                                Double.parseDouble(mArrayLisCompaniesListing.get(i).getCompany_longitude()),
                                Double.parseDouble("10"), t1, mArrayLisCompaniesListing.get(i).getCompany_id()));

                    } else {
                        mDataSet.add(new ObjectModel(Double.parseDouble(mArrayLisCompaniesListing.get(i).getCompany_latitude()),
                                Double.parseDouble(mArrayLisCompaniesListing.get(i).getCompany_longitude()),
                                Double.parseDouble(mArrayLisCompaniesListing.get(i).getCompany_radius()), t1, mArrayLisCompaniesListing.get(i).getCompany_id()));

                    }
                } else {
                    mDataSet.add(new ObjectModel(Double.parseDouble(mArrayLisCompaniesListing.get(i).getCompany_latitude()),
                            Double.parseDouble(mArrayLisCompaniesListing.get(i).getCompany_longitude()),
                            Double.parseDouble("10"), t1, mArrayLisCompaniesListing.get(i).getCompany_id()));

                }
            }
        }

        mRadarCustom.setupData(Double.parseDouble("" + radiessprogress * 1000), mDataSet, latLongCs, mCenterView);

        mRadarCustom.setUpCallBack((objectModel, view) -> {
            id = ((ObjectModel) objectModel).getUserid();
            mContext.startActivity(new Intent(mContext, JobListActivity.class).putExtra("id", id));
        });

    }

}
