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


public class EmailVerificationScreen extends Basefragment  implements View.OnClickListener {
    RelativeLayout rl_email,rl_otp;
    TextView tv_next,tv_your_email,tv_resend;
    EditText et_email,et_otp1,et_otp2,et_otp3,et_otp4;
    ImageView iv_emailedit;
    Rest rest;
    private Context mContext;
    private boolean isSeeker;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.emailverification_screen, container, false);
        mContext=getActivity();
          rest=new Rest(mContext);
        rl_email=rootView.findViewById(R.id.rl_email);
        rl_otp=rootView.findViewById(R.id.rl_otp);
        tv_next=rootView.findViewById(R.id.tv_next);
        et_email=rootView.findViewById(R.id.et_email);
        et_otp1=rootView.findViewById(R.id.et_otp1);
        et_otp2=rootView.findViewById(R.id.et_otp2);
        et_otp3=rootView.findViewById(R.id.et_otp3);
        et_otp4=rootView.findViewById(R.id.et_otp4);
        tv_resend=rootView.findViewById(R.id.tv_resend);
        tv_your_email=rootView.findViewById(R.id.tv_your_email);
        iv_emailedit=rootView.findViewById(R.id.iv_emailedit);
        et_email.setText(Config.GetEmail());
        tv_your_email.setText(MessageFormat.format("Your Email\n{0}", Config.GetEmail()));
        rl_otp.setVisibility(View.VISIBLE);
        rl_email.setVisibility(View.GONE);
        tv_next.setOnClickListener(this);
        iv_emailedit.setOnClickListener(this);
        tv_resend.setOnClickListener(this);
        isSeeker=BasicInfoActivity.instance.isSeeker;

        et_otp1.addTextChangedListener(textWatcher);
        et_otp2.addTextChangedListener(textWatcher);
        et_otp3.addTextChangedListener(textWatcher);
        et_otp4.addTextChangedListener(textWatcher);
        return rootView;
    }
    @Override
    public int setContentView() {
        return R.layout.emailverification_screen;
    }

    @Override
    protected void backPressed() {
              getActivity().finish();
    }

    @Override
    public void onClick(View view) {
         switch (view.getId()){
             case R.id.tv_next:
                   if (rl_otp.getVisibility()==View.VISIBLE){
                      callCheckOTP();
                   }else {
                         String email=et_email.getText().toString();
                       if (TextUtils.isEmpty(email)){
                           rest.showToast("Please Enter Email Id");
                       }else if (!Config.isEmailValid(email)){
                           rest.showToast("Please Enter Valid Email Id");
                       }else{
                           if (rest.isInterentAvaliable()) {
                               AppController.ShowDialogue("", mContext);
                               if (isSeeker)
                               callChangeEmail(email);
                               else callChangeCompanyEmail(email);
                           } else {
                               rest.AlertForInternet();
                           }
                       }
                   }
                 break;

                   case R.id.iv_emailedit:
                       rl_otp.setVisibility(View.GONE);
                       rl_email.setVisibility(View.VISIBLE);

                 break;

                 case R.id.tv_resend:
                     if (rest.isInterentAvaliable()) {
                         AppController.ShowDialogue("", mContext);
                         if (isSeeker)
                         resendOtp();
                         else callChangeCompanyEmail(Config.GetEmail());
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
                if (isSeeker){
                    callOTPVerify(otp);
                }else {
                    callCompanyOTPVerify(otp);
                }

            } else {
                rest.AlertForInternet();
            }
        }

    }

    private void resendOtp() {
        SwipeeApiClient.swipeeServiceInstance().resendEmailOtp(Config.GetUserToken(),Config.GetEmail()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200 && response.body()!=null){
                    JsonObject responseBody= response.body();
                    if (response.body().get("code").getAsInt()==203){
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        getActivity().finish();
                    }else
                    if (responseBody.has("message"))
                        rest.showToast(responseBody.get("message").getAsString());
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
    private void callOTPVerify(String otp) {
        SwipeeApiClient.swipeeServiceInstance().verifyEmailOtp(Config.GetUserToken(),otp).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200 && response.body()!=null){
                    JsonObject responseBody= response.body();
                    Log.d("dbhdfhkjgd",responseBody.toString());
                    if (responseBody.get("status").getAsBoolean()){
                        Config.SetEmailVERIFY(true);
                        if (BasicInfoActivity.instance!=null)
                            BasicInfoActivity.instance.attachMobileVerification();
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
        SwipeeApiClient.swipeeServiceInstance().verifyCompanyEmailOtp(Config.GetUserToken(),otp).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200 && response.body()!=null){
                    JsonObject responseBody= response.body();
                    Log.d("dbhdfhkjgd",responseBody.toString());
                    if (responseBody.get("status").getAsBoolean()){
                        Config.SetEmailVERIFY(true);
                        if (BasicInfoActivity.instance!=null)
                            BasicInfoActivity.instance.attachMobileVerification();
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


    private void callChangeEmail(String email) {
        SwipeeApiClient.swipeeServiceInstance().verifyChangeEmail(Config.GetUserToken(),email).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200 && response.body()!=null){
                    JsonObject responseBody= response.body();
                    Log.d("dbhdfhkjgd",responseBody.toString());
                    if (responseBody.get("status").getAsBoolean()){
                        Config.SetEmail(email);
                        rest.showToast(responseBody.get("message").getAsString());
                        et_email.setText(Config.GetEmail());
                        tv_your_email.setText(MessageFormat.format("Your Email\n{0}", Config.GetEmail()));
                        rl_otp.setVisibility(View.VISIBLE);
                        rl_email.setVisibility(View.GONE);
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
    private void callChangeCompanyEmail(String email) {
        SwipeeApiClient.swipeeServiceInstance().verifyChangeCompanyEmail(Config.GetUserToken(),email).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200 && response.body()!=null){
                    JsonObject responseBody= response.body();
                    Log.d("dbhdfhkjgd",responseBody.toString());
                    if (responseBody.get("status").getAsBoolean()){
                        Config.SetEmail(email);
                        rest.showToast(responseBody.get("message").getAsString());
                        et_email.setText(Config.GetEmail());
                        tv_your_email.setText(MessageFormat.format("Your Email\n{0}", Config.GetEmail()));
                        rl_otp.setVisibility(View.VISIBLE);
                        rl_email.setVisibility(View.GONE);
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
}
