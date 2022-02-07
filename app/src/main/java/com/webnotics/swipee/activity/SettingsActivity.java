package com.webnotics.swipee.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.webnotics.swipee.BuildConfig;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.Seeker.ContactUsActivity;
import com.webnotics.swipee.rest.ApiUrls;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_back;
    ImageView civ_profile;
    TextView tv_contactus,tv_privacy,tv_imprint,tv_logout,appversion;
    Context mContext;
    Rest rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        setContentView(R.layout.activity_settings);
        mContext=this;
        rest=new Rest(mContext);

        iv_back=findViewById(R.id.iv_back);
        civ_profile=findViewById(R.id.civ_profile);

        appversion=findViewById(R.id.appversion);
        appversion.setText(String.format("App Version: %s", BuildConfig.VERSION_NAME));

        tv_contactus=findViewById(R.id.tv_contactus);
        tv_privacy=findViewById(R.id.tv_privacy);
        tv_imprint=findViewById(R.id.tv_imprint);
        tv_logout=findViewById(R.id.tv_logout);

        iv_back.setOnClickListener(this);
        tv_contactus.setOnClickListener(this);
        tv_privacy.setOnClickListener(this);
        tv_imprint.setOnClickListener(this);
        tv_logout.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_contactus:
                startActivity(new Intent(mContext, ContactUsActivity.class));
                break;
            case R.id.tv_privacy:

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ApiUrls.POLICY_URL));
                    startActivity(browserIntent);

                break;
            case R.id.tv_imprint:

                    Intent browser = new Intent(Intent.ACTION_VIEW, Uri.parse(ApiUrls.TERMS_URL));
                    startActivity(browser);

                break;
            case R.id.tv_logout:
                Dialog progressdialog = new Dialog(mContext);
                progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                progressdialog.setContentView(R.layout.logout_popup);
                progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.gravity = Gravity.CENTER;
                progressdialog.getWindow().setAttributes(lp);

                progressdialog.findViewById(R.id.tv_yes).setOnClickListener(v1 -> {
                    progressdialog.dismiss();
                    AppController.ShowDialogue("",mContext);
                    callLogoutService();
                });
                progressdialog.findViewById(R.id.tv_cancel).setOnClickListener(v12 -> progressdialog.dismiss());
                try {
                    progressdialog.show();
                }catch (Exception e){}

                break;

            default:break;
        }

    }

    private void callLogoutService() {
        SwipeeApiClient.swipeeServiceInstance().userLogout(Config.GetUserToken()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    if (!responseBody.isJsonNull()){
                        if (responseBody.get("status").getAsBoolean()){
                            Config.clear(mContext);
                           /* if (SeekerHomeActivity.instance!=null)
                                SeekerHomeActivity.instance.onBackPressed();
                            if (CompanyHomeActivity.instance!=null)
                                CompanyHomeActivity.instance.onBackPressed();
                            startActivity(new Intent(mContext, LoginActivity.class));
                            finish();*/

                            Intent intent = new Intent(mContext, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("EXIT", true);
                            mContext.startActivity(intent);
                            finish();

                        }else rest.showToast(responseBody.get("message").getAsString());

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