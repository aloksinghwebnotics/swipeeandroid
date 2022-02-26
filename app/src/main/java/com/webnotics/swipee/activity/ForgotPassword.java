package com.webnotics.swipee.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {
    EditText et_email;
    TextView tv_seeker, tv_recruiter;
    Button btn_submit;
    Rest rest;
    ImageView iv_back;
    private final Context mContext = ForgotPassword.this;
    private boolean isSeeker=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        rest = new Rest(mContext);
        et_email = findViewById(R.id.et_email);
        btn_submit = findViewById(R.id.btn_submit);
        tv_seeker = findViewById(R.id.tv_seeker);
        tv_recruiter = findViewById(R.id.tv_recruiter);
        iv_back = findViewById(R.id.iv_back);
        if (getIntent()!=null){
            isSeeker=getIntent().getBooleanExtra("isSeeker",true);
        }
        if (isSeeker){
            tv_seeker.setBackgroundResource(R.drawable.white_semiround_bg);
            tv_recruiter.setBackgroundResource(0);
            tv_seeker.setTextColor(getColor(R.color.colorPrimary));
            tv_recruiter.setTextColor(getColor(R.color.white));
        }else {
            tv_seeker.setBackgroundResource(0);
            tv_recruiter.setBackgroundResource(R.drawable.white_semiround_bg);
            tv_seeker.setTextColor(getColor(R.color.white));
            tv_recruiter.setTextColor(getColor(R.color.colorPrimary));
        }
        btn_submit.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_recruiter.setOnClickListener(this);
        tv_seeker.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit:

                String email = et_email.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    rest.showToast("Please enter email address.");
                } else if (!Config.isEmailValid(email)) {
                    rest.showToast("Please enter a valid email address.");
                } else {
                    if (rest.isInterentAvaliable()) {
                         AppController.ShowDialogue("", mContext);
                         if (isSeeker)
                                   seekerForgot(email);
                         else recruiterForgot(email);
                    } else {
                        rest.AlertForInternet();
                    }
                }
                break;

            case R.id.tv_seeker:
                tv_seeker.setBackgroundResource(R.drawable.white_semiround_bg);
                tv_recruiter.setBackgroundResource(0);
                tv_seeker.setTextColor(getColor(R.color.colorPrimary));
                tv_recruiter.setTextColor(getColor(R.color.white));
                isSeeker=true;
                break;
            case R.id.tv_recruiter:
                tv_seeker.setBackgroundResource(0);
                tv_recruiter.setBackgroundResource(R.drawable.white_semiround_bg);
                tv_seeker.setTextColor(getColor(R.color.white));
                tv_recruiter.setTextColor(getColor(R.color.colorPrimary));
                isSeeker=false;
                break;
            case R.id.iv_back:
                finish();
                break;

            default:
                break;
        }
    }

    private void seekerForgot(String email) {
        SwipeeApiClient.swipeeServiceInstance().forgotSeeker(email).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200 && response.body()!=null){
                    JsonObject responseBody= response.body();
                    if (responseBody.has("message"))
                    startActivity(new Intent(mContext,ForgotConfirm.class).putExtra("isSeeker",isSeeker).putExtra("email",email));
                    finish();
                }else {
                    rest.showToast("Something went wrong");
                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }
    private void recruiterForgot(String email) {
        SwipeeApiClient.swipeeServiceInstance().forgotRecruiter(email).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200 && response.body()!=null){
                    JsonObject responseBody= response.body();
                    if (responseBody.has("message"))
                    startActivity(new Intent(mContext,ForgotConfirm.class).putExtra("isSeeker",isSeeker).putExtra("email",email));
                    finish();
                }else {
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