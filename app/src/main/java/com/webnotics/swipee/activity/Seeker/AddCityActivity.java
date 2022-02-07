package com.webnotics.swipee.activity.Seeker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.company.CompanyEditProfile;
import com.webnotics.swipee.adapter.seeeker.CityAdapter;
import com.webnotics.swipee.fragments.company.ProfileDetailScreen;
import com.webnotics.swipee.fragments.seeker.ProfileInfoScreen;
import com.webnotics.swipee.model.seeker.CityModel;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.rest.Rest;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCityActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView rv_stateList;
    ImageView iv_back;
    TextView tv_next;
    Context mContext;
    Rest rest;
    public String cityId = "";
    public String cityName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_city);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        rest = new Rest(mContext);

        rv_stateList = findViewById(R.id.rv_stateList);
        tv_next = findViewById(R.id.tv_next);
        iv_back = findViewById(R.id.iv_back);
        Intent intent = getIntent();
        if (intent != null)
            if (intent.getStringExtra("id") != null) {
                if (rest.isInterentAvaliable()) {
                    AppController.ShowDialogue("", mContext);
                    getCityList(intent.getStringExtra("id"));
                } else rest.AlertForInternet();
                iv_back.setOnClickListener(this);
                tv_next.setOnClickListener(this);
            }


    }

    private void getCityList(String id) {
        SwipeeApiClient.swipeeServiceInstance().getCityList(Config.GetUserToken(), id).enqueue(new Callback<CityModel>() {
            @Override
            public void onResponse(@NonNull Call<CityModel> call, @NonNull Response<CityModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200) {
                    CityModel stateModel = response.body();
                    if (stateModel != null)
                        if (stateModel.isStatus() && stateModel.getCode() == 200) {
                            ArrayList<CityModel.Data> data = stateModel.getData();
                            rv_stateList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            rv_stateList.setNestedScrollingEnabled(false);
                            CityAdapter stateAdapter = new CityAdapter(AddCityActivity.this, data);
                            rv_stateList.setAdapter(stateAdapter);
                            rv_stateList.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
                        } else if (stateModel.getCode() == 203) {
                            rest.showToast(stateModel.getMessage());
                            AppController.loggedOut(mContext);
                            finish();
                        }
                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<CityModel> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_next:
                if (TextUtils.isEmpty(cityId)) {
                    rest.showToast("Select City");
                } else {
                    if (getIntent() != null)
                        if (getIntent().getStringExtra("from").equalsIgnoreCase(EditProfileActivity.class.getSimpleName())) {
                            startActivity(new Intent(mContext, EditProfileActivity.class).putExtra("from", "City").putExtra("id", cityId).putExtra("name", cityName));
                        } else if (getIntent().getStringExtra("from").equalsIgnoreCase(CompanyEditProfile.class.getSimpleName())) {
                            startActivity(new Intent(mContext, CompanyEditProfile.class).putExtra("from", "City").putExtra("id", cityId).putExtra("name", cityName));
                        } else if (getIntent().getStringExtra("from").equalsIgnoreCase(ProfileDetailScreen.class.getSimpleName())) {
                            if (ProfileDetailScreen.instance != null) {
                                ProfileDetailScreen.instance.setCityData(cityName, cityId);
                            }
                        } else if (getIntent().getStringExtra("from").equalsIgnoreCase(ProfileInfoScreen.class.getSimpleName())) {
                            if (ProfileInfoScreen.instance != null) {
                                ProfileInfoScreen.instance.setCityData(cityName, cityId);
                            }
                        }
                    finish();
                }
                break;

            default:
                break;
        }

    }
}