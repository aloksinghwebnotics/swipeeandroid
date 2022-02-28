package com.webnotics.swipee.activity.Seeker;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.company.CompanyEditProfile;
import com.webnotics.swipee.activity.company.CompanyNearBy;
import com.webnotics.swipee.adapter.seeeker.SearchIndustryAdapter;
import com.webnotics.swipee.fragments.company.ProfileDetailScreen;
import com.webnotics.swipee.fragments.seeker.NearFragments;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.rest.Rest;

import java.util.ArrayList;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchIndustryActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    Context mContext;
    Rest rest;
    ListView mListView;
    EditText et_search;
    ImageView iv_back;
    TextView tv_title;
    private SearchIndustryAdapter collegeAdapter;
    private JsonObject responseBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_college);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));

        mContext = this;
        rest = new Rest(mContext);
        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            callDesIndustryList();
        } else rest.AlertForInternet();

        mListView = findViewById(R.id.mlistview);
        et_search = findViewById(R.id.et_search);
        iv_back = findViewById(R.id.iv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("Industry");

        iv_back.setOnClickListener(this);
        et_search.addTextChangedListener(this);


    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (collegeAdapter != null)
            collegeAdapter.getFilter().filter(s.toString().trim());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            default:
                break;

        }

    }

    private void callDesIndustryList() {
        SwipeeApiClient.swipeeServiceInstance().getDesiredIndustry(Config.GetUserToken()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    responseBody = response.body();
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (response.body().get("code").getAsInt() == 200) {
                        JsonArray mArrayListData = responseBody.has("data") ? responseBody.get("data").getAsJsonArray() : new JsonArray();
                        ArrayList<JsonObject> arrayList = new ArrayList<>();
                        IntStream.range(0, mArrayListData.size()).forEach(i -> arrayList.add(arrayList.size(), mArrayListData.get(i).getAsJsonObject()));
                        collegeAdapter = new SearchIndustryAdapter(SearchIndustryActivity.this, arrayList);
                        mListView.setAdapter(collegeAdapter);
                    } else rest.showToast("Something went wrong");

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

    public void selectedData(String name, String id) {
        if (CompanyEditProfile.instance != null) {
            CompanyEditProfile.instance.selectIndustry(name, id);
        } else if (ProfileDetailScreen.instance != null) {
            ProfileDetailScreen.instance.selectIndustry(name, id);
        } else if (NearFragments.instance != null) {
            NearFragments.instance.setIndustryData(name, id);
        } else if (CompanyNearBy.instance != null) {
            CompanyNearBy.instance.setIndustryData(name, id);
        }
        finish();
    }

}