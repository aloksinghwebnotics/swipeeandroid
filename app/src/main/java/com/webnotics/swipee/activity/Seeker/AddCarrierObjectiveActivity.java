package com.webnotics.swipee.activity.Seeker;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.fragments.seeker.ProfileFragments;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.rest.Rest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class  AddCarrierObjectiveActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private Rest rest;
    EditText et_objective;
    TextView tv_save;
    ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_carrier_objective);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        rest = new Rest(mContext);

        et_objective = findViewById(R.id.et_objective);
        tv_save = findViewById(R.id.tv_save);
        iv_back = findViewById(R.id.iv_back);
        if (getIntent() != null) {
            String obj = getIntent().getStringExtra("data");
            et_objective.setText(obj);
        }

        tv_save.setOnClickListener(this);
        iv_back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                if (TextUtils.isEmpty(et_objective.getText().toString().trim())) {
                    rest.showToast("Enter career objective");
                } else {
                    AppController.ShowDialogue("", mContext);
                    saveObjective(et_objective.getText().toString());
                }

                break;
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }

    }

    private void saveObjective(String obj) {
        SwipeeApiClient.swipeeServiceInstance().addObjective(Config.GetUserToken(), obj).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (response.body().get("status").getAsBoolean()) {
                        if (ProfileFragments.instance != null)
                            ProfileFragments.instance.setObjective(obj);
                        rest.showToast(response.body().get("message").getAsString());
                        finish();
                    }
                } else rest.showToast("Something went wrong");
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }
}