package com.webnotics.swipee.activity.company;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.text.MessageFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobPostRule extends AppCompatActivity {
    Rest rest;
    Context mContext;
    TextView tv_data, tv_gotit, tv_datavalue;
    ImageView iv_back;
    private int job_post_id = 0;
    private String company_featured_id="";
    private String total_post_limit="";
    private String used_listing="";
    private String featured_package_expire="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_post_rule);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        rest = new Rest(mContext);

        tv_data = findViewById(R.id.tv_data);
        iv_back = findViewById(R.id.iv_back);
        tv_gotit = findViewById(R.id.tv_gotit);
        tv_datavalue = findViewById(R.id.tv_datavalue);

        if (getIntent() != null) {
            job_post_id = getIntent().getIntExtra("job_post_id", 0);
            if (rest.isInterentAvaliable()) {
                AppController.ShowDialogue("", mContext);
                getPostRule(String.valueOf(job_post_id));
            }
        }

        iv_back.setOnClickListener(v -> {
                    startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", JobPostRule.class.getSimpleName()));
                    finish();
                }

        );
        tv_gotit.setOnClickListener(v ->{
            callDialog();
        } );
    }

    private void getPostRule(String id) {
        SwipeeApiClient.swipeeServiceInstance().jobpostrule(Config.GetUserToken(), id).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject respo = response.body();
                        if (respo.get("code").getAsInt() == 200 && respo.get("status").getAsBoolean()) {
                            JsonObject data = respo.get("data").getAsJsonObject();
                            String page_title = data.has("page_title") ? data.get("page_title").getAsString() : "";
                            String page_description = data.has("page_description") ? data.get("page_description").getAsString() : "";
                            tv_data.setText(Html.fromHtml(page_title));
                            tv_datavalue.setText(Html.fromHtml(page_description));
                            JsonObject featured_package_details= data.has("featured_package_details")?data.get("featured_package_details").getAsJsonObject():new JsonObject();
                             company_featured_id= featured_package_details.has("company_featured_id")?featured_package_details.get("company_featured_id").getAsString():"";
                             total_post_limit= featured_package_details.has("total_post_limit")?featured_package_details.get("total_post_limit").getAsString():"";
                             used_listing= featured_package_details.has("used_listing")?featured_package_details.get("used_listing").getAsString():"";
                             featured_package_expire= featured_package_details.has("featured_package_expire")?featured_package_details.get("featured_package_expire").getAsString():"";

                        } else if (respo.get("code").getAsInt() == 203) {
                            rest.showToast(respo.get("message").getAsString());
                            AppController.loggedOut(mContext);
                            finish();
                        }
                }


            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }

    private void callDialog() {
        Dialog progressdialog = new Dialog(mContext);
        progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressdialog.setContentView(R.layout.posted_job_popup);
        progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        progressdialog.getWindow().setAttributes(lp);
        progressdialog.setCancelable(false);
        TextView tv_submit=progressdialog.findViewById(R.id.tv_feature);
        TextView tv_detail=progressdialog.findViewById(R.id.tv_detail);
        if (featured_package_expire.equalsIgnoreCase("N") &&!TextUtils.isEmpty(total_post_limit) &&!TextUtils.isEmpty(used_listing) &&!total_post_limit.equalsIgnoreCase(used_listing)){
            tv_submit.setText("Post featured job");
            tv_detail.setText(MessageFormat.format("Total Post Limit: {0}\n\nUsed Post Limit: {1}\n\nThis job will be publish as feature job, after 24 hours.", total_post_limit, total_post_limit));
        }else {
            tv_detail.setText("You don't have any active feature job package. If you want to post this job as a featured job purchase a package or you can publish your job.");
            tv_submit.setText("Purchase feature");
        }

        progressdialog.findViewById(R.id.tv_publish).setOnClickListener(v -> {

            progressdialog.dismiss();
            AppController.ShowDialogue("", mContext);
            publishJob(String.valueOf(job_post_id));
        });
        progressdialog.findViewById(R.id.tv_feature).setOnClickListener(v ->{
            progressdialog.dismiss();
            if (featured_package_expire.equalsIgnoreCase("N") &&!TextUtils.isEmpty(total_post_limit)&&!TextUtils.isEmpty(used_listing) &&!total_post_limit.equalsIgnoreCase(used_listing)){
                AppController.ShowDialogue("", mContext);
                publishFeaturedJob(String.valueOf(job_post_id));
            }else {
               callPurchaseFeatured();
            }
        });
        try {
            progressdialog.show();
        } catch (Exception e) {
        }

    }

    private void callPurchaseFeatured() {
        mContext.startActivity(new Intent(mContext, FeaturedPlan.class).putExtra(ParaName.KEY_JOBPOSTID,job_post_id));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", JobPostRule.class.getSimpleName()));
        finish();
        super.onBackPressed();
    }

    private void publishJob(String id) {
        SwipeeApiClient.swipeeServiceInstance().publishJob(Config.GetUserToken(), id, "N").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject respo = response.body();
                    if (respo != null)
                        if (respo.get("code").getAsInt() == 200 && respo.get("status").getAsBoolean()) {
                            rest.showToast(respo.get("message").getAsString());
                            callDialogFinish();
                        } else if (respo.get("code").getAsInt() == 203) {
                            rest.showToast(respo.get("message").getAsString());
                            AppController.loggedOut(mContext);
                            finish();
                        } else {
                            rest.showToast(respo.get("message").getAsString());
                            startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", JobPostRule.class.getSimpleName()));
                            finish();
                        }
                }


            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }
    private void publishFeaturedJob(String id) {
        SwipeeApiClient.swipeeServiceInstance().publishFeaturedJob(Config.GetUserToken(), id, "Y").enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject respo = response.body();
                    if (respo != null)
                        if (respo.get("code").getAsInt() == 200 && respo.get("status").getAsBoolean()) {
                            rest.showToast(respo.get("message").getAsString());
                            startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", JobPostRule.class.getSimpleName()));
                            finish();
                        } else if (respo.get("code").getAsInt() == 203) {
                            rest.showToast(respo.get("message").getAsString());
                            AppController.loggedOut(mContext);
                            finish();
                        } else {
                            rest.showToast(respo.get("message").getAsString());
                            startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", JobPostRule.class.getSimpleName()));
                            finish();
                        }
                }


            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }

    private void callDialogFinish() {

        Dialog progressdialog = new Dialog(mContext);
        progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressdialog.setContentView(R.layout.postedfinal_job_popup);
        progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        progressdialog.getWindow().setAttributes(lp);
        progressdialog.setCancelable(false);

        progressdialog.findViewById(R.id.iv_cross).setOnClickListener(v -> progressdialog.dismiss());
        progressdialog.findViewById(R.id.tv_submit).setOnClickListener(v -> progressdialog.dismiss());
        try {
            progressdialog.show();
        } catch (Exception e) {
        }

        progressdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", JobPostRule.class.getSimpleName()));
                finish();
            }
        });

    }

}