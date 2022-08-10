package com.swipee.in.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.swipee.in.R;
import com.swipee.in.UrlManager.Config;
import com.swipee.in.activity.Seeker.SeekerHomeActivity;
import com.swipee.in.activity.company.CompanyHomeActivity;
import com.swipee.in.activity.company.UserDetail;

import org.json.JSONObject;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashScreenDeepLink extends AppCompatActivity {
    Context mContext = SplashScreenDeepLink.this;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        //   initView();
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null) {
            FirebaseDynamicLinks.getInstance()
                    .getDynamicLink(getIntent())
                    .addOnSuccessListener(this, pendingDynamicLinkData -> {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            try {
                                String json = Objects.requireNonNull(deepLink).getQueryParameter("jsondata");
                                Config.SetDeeplinkData(json);
                                JSONObject obj = new JSONObject(json);
                                if (obj.has("type")) {
                                    String type = obj.getString("type");/////
                                    if (type.equalsIgnoreCase("user_profile")) {
                                        String id = obj.has("id") ? obj.getString("id") : "";
                                        String job_id = obj.has("job_id") ? obj.getString("job_id") : "";
                                        String name = obj.has("name") ? obj.getString("name") : "";
                                        String apply_id = obj.has("apply_id") ? obj.getString("apply_id") : "";
                                        if (TextUtils.isEmpty(id)) {
                                            initView();
                                        } else {
                                            if (Config.GetIsUserLogin() && Config.isRemember()) {
                                                if (!Config.isSeeker()) {
                                                    Config.setUserStats(false);
                                                    Config.SetDeeplinkData("");
                                                    new Handler().postDelayed(() -> {
                                                        startActivity(new Intent(mContext, UserDetail.class).
                                                                putExtra("apply_id", apply_id).putExtra("id", id).putExtra("job_id", job_id).putExtra("name", name)
                                                                .putExtra("from", SplashScreenDeepLink.class.getSimpleName()));
                                                        finish();
                                                    }, 2000);
                                                } else initView();
                                            } else initView();
                                        }
                                    } else if (type.equalsIgnoreCase("company_profile")) {

                                        String id = obj.has("company_id") ? obj.getString("company_id") : "";
                                        if (TextUtils.isEmpty(id)) {
                                            initView();
                                        } else {
                                            if (Config.GetIsUserLogin() && Config.isRemember()) {
                                                if (Config.isSeeker()) {
                                                    Config.setUserStats(false);
                                                    Config.SetDeeplinkData("");
                                                    new Handler().postDelayed(() -> {
                                                        startActivity(new Intent(mContext, CompanyProfile.class).putExtra("company_id", id)
                                                                .putExtra("from", SplashScreenDeepLink.class.getSimpleName()));
                                                        finish();
                                                    }, 2000);
                                                } else initView();
                                            } else initView();
                                        }
                                    } else if (type.equalsIgnoreCase("job_detail")) {
                                        String id = obj.has("id") ? obj.getString("id") : "";
                                        String apply_id = obj.has("apply_id") ? obj.getString("apply_id") : "";
                                        if (TextUtils.isEmpty(id)) {
                                            initView();
                                        } else {
                                            if (Config.GetIsUserLogin() && Config.isRemember()) {
                                                if (Config.isSeeker()) {
                                                    Config.setUserStats(false);
                                                    Config.SetDeeplinkData("");
                                                    new Handler().postDelayed(() -> {
                                                        Intent resultIntent = new Intent(mContext, JobDetail.class);
                                                        resultIntent.putExtra("id", id);
                                                        resultIntent.putExtra("apply_id", apply_id);
                                                        resultIntent.putExtra("from", SplashScreenDeepLink.class.getSimpleName());
                                                        startActivity(resultIntent);
                                                        finish();
                                                    }, 2000);
                                                } else initView();
                                            } else initView();
                                        }
                                    } else initView();
                                } else initView();
                            } catch (Exception e) {
                                initView();
                            }
                        } else initView();

                    }).addOnFailureListener(e -> initView());
        } else initView();

    }

    private void initView() {

        Config.setUserStats(false);
        new Handler().postDelayed(() -> {
            if (Config.GetIsUserLogin() && Config.isRemember()) {
                if (Config.isSeeker()) {
                    Intent i = new Intent(this, SeekerHomeActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                } else {
                    Intent i = new Intent(this, CompanyHomeActivity.class);
                    startActivity(i);
                    overridePendingTransition(R.anim.enter, R.anim.exit);
                    finish();
                }
            } else {
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                finish();
            }

        }, 2500);
    }


}