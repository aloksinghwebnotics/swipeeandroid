package com.webnotics.swipee.activity.Seeker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.webnotics.swipee.adapter.seeeker.StateAdapter;
import com.webnotics.swipee.fragments.company.ProfileDetailScreen;
import com.webnotics.swipee.fragments.seeker.ProfileInfoScreen;
import com.webnotics.swipee.model.seeker.StateModel;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.rest.Rest;

import java.util.ArrayList;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddStateActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    public String stateName = "";
    RecyclerView rv_stateList;
    ImageView iv_back;
    TextView tv_next;
    Context mContext;
    Rest rest;
    public String stateId = "";
    EditText et_search;
    private StateAdapter stateAdapter;
    private String state_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_state);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        rest = new Rest(mContext);

        rv_stateList = findViewById(R.id.rv_stateList);
        tv_next = findViewById(R.id.tv_next);
        iv_back = findViewById(R.id.iv_back);
        et_search = findViewById(R.id.et_search);

        if (getIntent() != null)
            state_id = getIntent().getStringExtra("state_id") != null ? getIntent().getStringExtra("state_id") : "";

        if (rest.isInterentAvaliable()) {
            AppController.ShowDialogue("", mContext);
            getStateList("101");
        } else rest.AlertForInternet();

        iv_back.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        et_search.addTextChangedListener(this);
    }

    private void getStateList(String id) {
        SwipeeApiClient.swipeeServiceInstance().getStateList(Config.GetUserToken(), id).enqueue(new Callback<StateModel>() {
            @Override
            public void onResponse(@NonNull Call<StateModel> call, @NonNull Response<StateModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200) {
                    StateModel stateModel = response.body();
                    if (stateModel != null)
                        if (stateModel.isStatus() && stateModel.getCode() == 200) {
                            ArrayList<StateModel.Data> data = stateModel.getData();
                            if (!TextUtils.isEmpty(state_id))
                                IntStream.range(0, data.size()).filter(i -> data.get(i).getState_id().equalsIgnoreCase(state_id)).findFirst().ifPresent(i -> data.get(i).setSelected(true));
                            rv_stateList.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
                            rv_stateList.setNestedScrollingEnabled(false);
                            stateAdapter = new StateAdapter(AddStateActivity.this, data);
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
            public void onFailure(@NonNull Call<StateModel> call, @NonNull Throwable t) {

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

                if (TextUtils.isEmpty(stateId)) {
                    rest.showToast("Select State");
                } else {
                    if (getIntent() != null) {
                        if (getIntent().getStringExtra("from").equalsIgnoreCase(EditProfileActivity.class.getSimpleName())) {
                            startActivity(new Intent(mContext, EditProfileActivity.class).putExtra("from", "State").putExtra("id", stateId).putExtra("name", stateName));
                        } else if (getIntent().getStringExtra("from").equalsIgnoreCase(CompanyEditProfile.class.getSimpleName())) {
                            startActivity(new Intent(mContext, CompanyEditProfile.class).putExtra("from", "State").putExtra("id", stateId).putExtra("name", stateName));
                        } else if (getIntent().getStringExtra("from").equalsIgnoreCase(ProfileDetailScreen.class.getSimpleName())) {
                            if (ProfileDetailScreen.instance != null) {
                                ProfileDetailScreen.instance.setStateData(stateName, stateId);
                            }
                        } else if (getIntent().getStringExtra("from").equalsIgnoreCase(ProfileInfoScreen.class.getSimpleName())) {
                            if (ProfileInfoScreen.instance != null) {
                                ProfileInfoScreen.instance.setStateData(stateName, stateId);
                            }
                        }
                        finish();
                    }


                }
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
        if (stateAdapter != null)
            stateAdapter.getFilter().filter(s.toString().trim());
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}