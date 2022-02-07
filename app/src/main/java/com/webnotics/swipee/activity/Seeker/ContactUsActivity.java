package com.webnotics.swipee.activity.Seeker;

import android.content.Context;
import android.os.Bundle;
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
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.rest.Rest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {
    Context mContext;
    Rest rest;
    ImageView iv_back;
    TextView tv_email, tv_mobile, tv_next;
    EditText et_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        rest = new Rest(mContext);
        iv_back = findViewById(R.id.iv_back);
        tv_email = findViewById(R.id.tv_email);
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_next = findViewById(R.id.tv_next);
        et_msg = findViewById(R.id.et_msg);
        tv_email.setText(Config.GetEmail());
        tv_mobile.setText(Config.GetMobileNo());

        iv_back.setOnClickListener(this);
        tv_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_next:
                if (et_msg.getText().toString().replaceAll(" ", "").isEmpty()) {
                    rest.showToast("Write your message");
                } else {
                    if (rest.isInterentAvaliable()) {
                        AppController.ShowDialogue("", mContext);
                        if (Config.isSeeker())
                            callContactService(et_msg.getText().toString());
                        else callCompanyContactService(et_msg.getText().toString());
                    } else rest.AlertForInternet();
                }
                break;

            default:
                break;
        }

    }

    private void callContactService(String msg) {
        SwipeeApiClient.swipeeServiceInstance().postContact(Config.GetUserToken(), Config.GetEmail(), Config.GetPhoneCode(), Config.GetMobileNo(), msg).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    boolean status = responseBody.has("status") && responseBody.get("status").getAsBoolean();
                    String msg = responseBody.has("message") ? responseBody.get("message").getAsString() : "";
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(msg);
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (status) {
                        rest.showToast(msg);
                        finish();
                    }
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

    private void callCompanyContactService(String msg) {
        SwipeeApiClient.swipeeServiceInstance().postCompanyContact(Config.GetUserToken(), Config.GetEmail(), Config.GetPhoneCode(), Config.GetMobileNo(), msg).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    boolean status = responseBody.has("status") && responseBody.get("status").getAsBoolean();
                    String msg = responseBody.has("message") ? responseBody.get("message").getAsString() : "";
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(msg);
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (status) {
                        rest.showToast(msg);
                        finish();
                    }
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
}