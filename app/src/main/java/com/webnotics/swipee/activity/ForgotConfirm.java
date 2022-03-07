package com.webnotics.swipee.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotConfirm extends AppCompatActivity {

    TextView tv_emailsent,tv_emailresend;
    Button btn_submit;
    ImageView iv_back;
    Rest rest;
    Context mContext;
    private String email="";
    boolean isSeeker=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_confirm);
        mContext=this;
        rest=new Rest(mContext);

        btn_submit=findViewById(R.id.btn_submit);
        tv_emailsent=findViewById(R.id.tv_emailsent);
        tv_emailresend=findViewById(R.id.tv_emailresend);
        iv_back=findViewById(R.id.iv_back);
        tv_emailresend.setText(Html.fromHtml(getString(R.string.didntgetemail)));

        if (getIntent()!=null){
            email=getIntent().getStringExtra("email")!=null? getIntent().getStringExtra("email"):"";
            isSeeker=getIntent().getBooleanExtra("isSeeker",true);
        }

        tv_emailsent.setText("We have send introduction link to pick a new password to "+email);

        tv_emailresend.setOnClickListener(v -> {
            if (rest.isInterentAvaliable()) {
                AppController.ShowDialogueWhite("", mContext);
                if (isSeeker)
                    seekerForgot(email);
                else recruiterForgot(email);
            } else {
                rest.AlertForInternet();
            }
        });
        iv_back.setOnClickListener(v -> finish());
        btn_submit.setOnClickListener(v -> {
            try {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException ignored) {

            }
        });
    }

    private void seekerForgot(String email) {
        SwipeeApiClient.swipeeServiceInstance().forgotSeeker(email).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200 && response.body()!=null){
                    JsonObject responseBody= response.body();
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
    private void recruiterForgot(String email) {
        SwipeeApiClient.swipeeServiceInstance().forgotRecruiter(email).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code()==200 && response.body()!=null){
                    JsonObject responseBody= response.body();
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

}