package com.webnotics.swipee.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.BasicInfoActivity;
import com.webnotics.swipee.rest.SwipeeApiClient;
import com.webnotics.swipee.rest.Rest;

import java.text.MessageFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MobileVerificationScreen extends Basefragment  implements View.OnClickListener {
    RelativeLayout rl_mobile,rl_otp;
    TextView tv_next,tv_your_mobile,tv_resend;
    EditText et_mobile,et_otp1,et_otp2,et_otp3,et_otp4;
    ImageView iv_mobileEdit;
    Rest rest;
    Context mContext;
    private boolean isSeeker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.mobileverification_screen, container, false);
            mContext=getActivity();
            rest=new Rest(mContext);
        rl_mobile=rootView.findViewById(R.id.rl_mobile);
        rl_otp=rootView.findViewById(R.id.rl_otp);
        tv_next=rootView.findViewById(R.id.tv_next);
        et_mobile=rootView.findViewById(R.id.et_mobile);
        tv_your_mobile=rootView.findViewById(R.id.tv_your_mobile);
        iv_mobileEdit=rootView.findViewById(R.id.iv_mobileEdit);
        tv_resend=rootView.findViewById(R.id.tv_resend);
        et_otp1=rootView.findViewById(R.id.et_otp1);
        et_otp2=rootView.findViewById(R.id.et_otp2);
        et_otp3=rootView.findViewById(R.id.et_otp3);
        et_otp4=rootView.findViewById(R.id.et_otp4);
        et_mobile.setText(Config.GetMobileNo());
        isSeeker=BasicInfoActivity.instance.isSeeker;

        tv_your_mobile.setText(MessageFormat.format("Your Number {0} {1}", Config.GetPhoneCode(), Config.GetMobileNo()));
        rl_otp.setVisibility(View.VISIBLE);
        rl_mobile.setVisibility(View.GONE);
        tv_next.setOnClickListener(this);
        iv_mobileEdit.setOnClickListener(this);
        tv_resend.setOnClickListener(this);
        et_otp1.addTextChangedListener(textWatcher);
        et_otp2.addTextChangedListener(textWatcher);
        et_otp3.addTextChangedListener(textWatcher);
        et_otp4.addTextChangedListener(textWatcher);
        return rootView;
    }
    @Override
    public int setContentView() {
        return R.layout.mobileverification_screen;
    }

    @Override
    protected void backPressed()     {
        getActivity().finish();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_next:
                if (rl_otp.getVisibility()==View.VISIBLE){
                    callCheckOTP();
                }else {
                        String mobile=et_mobile.getText().toString();
                    if (TextUtils.isEmpty(mobile)){
                        rest.showToast("Please Enter Mobile Number");
                    }else if (mobile.length()<10){
                        rest.showToast("Please Enter Valid Mobile Number");
                    }else {
                        if (rest.isInterentAvaliable()) {
                            AppController.ShowDialogue("", mContext);
                            if (isSeeker)
                            callChangeMobile(mobile);
                            else  callChangeCompanyMobile(mobile);
                        } else {
                            rest.AlertForInternet();
                        }
                    }
                }
                break;

                case R.id.iv_mobileEdit:
                    rl_otp.setVisibility(View.GONE);
                    rl_mobile.setVisibility(View.VISIBLE);
                break;

                case R.id.tv_resend:
                    if (rest.isInterentAvaliable()) {
                        AppController.ShowDialogue("", mContext);
                       if (isSeeker)
                        resendOtp();
                       else  callChangeCompanyMobile(Config.GetMobileNo());;
                    } else {
                        rest.AlertForInternet();
                    }
                break;
            default:break;
        }
    }
    private void callCheckOTP() {
        String otp=et_otp1.getText().toString()+et_otp2.getText().toString()+et_otp3.getText().toString()+et_otp4.getText().toString();
        if (otp.length()!=4){
            rest.showToast("Enter Valid OTP");
        }else {
            if (rest.isInterentAvaliable()) {
                AppController.ShowDialogue("", mContext);
                if (isSeeker)
                callOTPVerify(otp);
                else  callCompanyOTPVerify(otp);
            } else {
                rest.AlertForInternet();
            }
        }

    }


    private void callOTPVerify(String otp) {
        SwipeeApiClient.swipeeServiceInstance().verifyMobileOtp(Config.GetUserToken(),otp).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200 && response.body()!=null){
                    JsonObject responseBody= response.body();
                    if (responseBody.get("status").getAsBoolean()){
                        Config.SetMobileVERIFY(true);
                        if (BasicInfoActivity.instance!=null)
                            BasicInfoActivity.instance.attachProfileInfo();
                            rest.showToast(responseBody.get("message").getAsString());
                    }else {
                        if (responseBody.has("message"))
                            rest.showToast(responseBody.get("message").getAsString());
                    }
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
    private void callCompanyOTPVerify(String otp) {
        SwipeeApiClient.swipeeServiceInstance().verifyCompanyMobileOtp(Config.GetUserToken(),otp).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200 && response.body()!=null){
                    JsonObject responseBody= response.body();
                    Log.d("dbhdfhkjgd",responseBody.toString());
                    if (responseBody.get("status").getAsBoolean()){
                        Config.SetMobileVERIFY(true);
                        if (BasicInfoActivity.instance!=null)
                            BasicInfoActivity.instance.attachProfileInfo();
                        rest.showToast(responseBody.get("message").getAsString());
                    }else {
                        if (responseBody.has("message"))
                            rest.showToast(responseBody.get("message").getAsString());
                    }
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
    private void callChangeMobile(String mobile) {
        SwipeeApiClient.swipeeServiceInstance().verifyChangeMobile(Config.GetUserToken(),mobile,Config.GetPhoneCode()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200 && response.body()!=null){
                    JsonObject responseBody= response.body();
                    Log.d("dbhdfhkjgd",responseBody.toString());
                    if (responseBody.get("status").getAsBoolean()){
                        Config.SetMobileNo(mobile);
                        rest.showToast(responseBody.get("message").getAsString());
                        et_mobile.setText(Config.GetMobileNo());
                        tv_your_mobile.setText(MessageFormat.format("Your Number {0} {1}", Config.GetPhoneCode(), Config.GetMobileNo()));
                        rl_otp.setVisibility(View.VISIBLE);
                        rl_mobile.setVisibility(View.GONE);
                    }else {
                        if (responseBody.has("message"))
                            rest.showToast(responseBody.get("message").getAsString());
                    }
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

    private void callChangeCompanyMobile(String mobile) {
        SwipeeApiClient.swipeeServiceInstance().verifyChangeCompanyMobile(Config.GetUserToken(),mobile,Config.GetPhoneCode()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200 && response.body()!=null){
                    JsonObject responseBody= response.body();
                    Log.d("dbhdfhkjgd",responseBody.toString());
                    if (responseBody.get("status").getAsBoolean()){
                        Config.SetMobileNo(mobile);
                        rest.showToast(responseBody.get("message").getAsString());
                        et_mobile.setText(Config.GetMobileNo());
                        tv_your_mobile.setText(MessageFormat.format("Your Number {0} {1}", Config.GetPhoneCode(), Config.GetMobileNo()));
                        rl_otp.setVisibility(View.VISIBLE);
                        rl_mobile.setVisibility(View.GONE);
                    }else {
                        if (responseBody.has("message"))
                            rest.showToast(responseBody.get("message").getAsString());
                    }
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

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (getActivity().getCurrentFocus().getId() == et_otp1.getId()){
                if (et_otp1.getText().toString().length()>0){
                    et_otp2.requestFocus();
                }
            }else  if (getActivity().getCurrentFocus().getId() == et_otp2.getId()){
                if (et_otp2.getText().toString().length()>0){
                    et_otp3.requestFocus();
                } else  et_otp1.requestFocus();
            } else  if (getActivity().getCurrentFocus().getId() == et_otp3.getId()){
                if (et_otp3.getText().toString().length()>0){
                    et_otp4.requestFocus();
                } else  et_otp2.requestFocus();
            } else  if (getActivity().getCurrentFocus().getId() == et_otp4.getId()){
                if (et_otp4.getText().toString().length()>0){
                    et_otp4.requestFocus();
                } else  et_otp3.requestFocus();
            }


        }
    };

    private void resendOtp() {
        SwipeeApiClient.swipeeServiceInstance().resendMobileOtp(Config.GetUserToken(),Config.GetMobileNo(),Config.GetPhoneCode()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200 && response.body()!=null){
                    JsonObject responseBody= response.body();
                    if (response.body().get("code").getAsInt()==203){
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        getActivity().finish();
                    }else{
                        if (responseBody.has("message"))
                            rest.showToast(responseBody.get("message").getAsString());
                    }

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

    private void resendCompanyOtp() {
        SwipeeApiClient.swipeeServiceInstance().companyMobileResend(Config.GetUserToken(),Config.GetMobileNo(),Config.GetPhoneCode()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200 && response.body()!=null){
                    JsonObject responseBody= response.body();
                    if (response.body().get("code").getAsInt()==203){
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        getActivity().finish();
                    }else{
                        if (responseBody.has("message"))
                            rest.showToast(responseBody.get("message").getAsString());
                    }

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
