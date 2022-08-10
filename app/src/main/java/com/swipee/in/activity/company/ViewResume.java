package com.swipee.in.activity.company;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.swipee.in.R;
import com.swipee.in.UrlManager.AppController;
import com.swipee.in.UrlManager.Config;
import com.swipee.in.model.company.ResumesModel;
import com.swipee.in.rest.ParaName;
import com.swipee.in.rest.Rest;
import com.swipee.in.rest.SwipeeApiClient;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewResume extends AppCompatActivity implements View.OnClickListener {
    WebView wv_url;
    TextView tv_title,tv_view_detail;
    ImageView iv_back,iv_accept,iv_reject;
    private ResumesModel.Users_Listing myObject;
    private final Context mContext=ViewResume.this;
    Rest rest;
    @SuppressLint("StaticFieldLeak")
    public static ViewResume instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_resume);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        instance=this;
        wv_url=findViewById(R.id.wv_url);
        tv_title=findViewById(R.id.tv_title);
        iv_back=findViewById(R.id.iv_back);
        iv_accept=findViewById(R.id.iv_accept);
        iv_reject=findViewById(R.id.iv_reject);
        tv_view_detail=findViewById(R.id.tv_view_detail);
        if( getIntent()!=null){
            myObject = (ResumesModel.Users_Listing) getIntent().getSerializableExtra("data");
            if (myObject!=null){
                rest=new Rest(mContext);
                if (!myObject.getCompany_match_status().equals("A")){
                    iv_accept.setVisibility(View.VISIBLE);
                    iv_reject.setVisibility(View.VISIBLE);
                    tv_view_detail.setVisibility(View.GONE);
                    iv_accept.setOnClickListener(this);
                    iv_reject.setOnClickListener(this);
                }else {
                    iv_accept.setVisibility(View.GONE);
                    iv_reject.setVisibility(View.GONE);
                    tv_view_detail.setVisibility(View.VISIBLE);
                    tv_view_detail.setOnClickListener(this);
                }
                String url=myObject.getUser_resumes().getCv_file_link();
                String title=myObject.getFirst_name();
                tv_title.setText(String.format("%s's Profile", title));
                wv_url.getSettings().setJavaScriptEnabled(true);
                wv_url.getSettings().setAllowContentAccess(true);
                wv_url.getSettings().setAllowFileAccess(true);
                wv_url.getSettings().setAllowFileAccessFromFileURLs(true);
                wv_url.getSettings().setBlockNetworkImage(false);
                wv_url.getSettings().setSupportZoom(true);
                wv_url.getSettings().setDomStorageEnabled(true);
                wv_url.getSettings().setLoadWithOverviewMode(true);
                wv_url.getSettings().setPluginState(WebSettings.PluginState.ON);
                AppController.ShowDialogue("", ViewResume.this);
                wv_url.setWebViewClient(new MyWebViewClient());
                if (url.contains(".pdf")|| url.contains(".docx") || url.contains(".doc") || url.contains(".txt"))
                    wv_url.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);
                else{
                     wv_url.getSettings().setUseWideViewPort(true);
                   wv_url.getSettings().setBuiltInZoomControls(true);
                    wv_url.loadUrl(url);
                }
            }
        }

        iv_back.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.iv_reject:
                if (!TextUtils.isEmpty(myObject.getUser_id())) {

                    HashMap<String, String> hashMap1 = new HashMap<>();
                    hashMap1.put(ParaName.KEYTOKEN, Config.GetUserToken());
                    hashMap1.put(ParaName.KEY_MATCHID, myObject.getMatch_id());
                    hashMap1.put(ParaName.KEY_COMPANYSTATUS, "R");
                    hashMap1.put(ParaName.KEY_UID, myObject.getUser_id());
                    hashMap1.put(ParaName.KEY_JOBID, myObject.getJob_id());
                    if (rest.isInterentAvaliable()) {
                        AppController.ShowDialogue("", mContext);
                        postLikeDislike(hashMap1);
                    } else rest.AlertForInternet();
                }
                break;
            case R.id.iv_accept:

                if (!TextUtils.isEmpty(myObject.getUser_id())) {

                    HashMap<String, String> hashMap1 = new HashMap<>();
                    hashMap1.put(ParaName.KEYTOKEN, Config.GetUserToken());
                    hashMap1.put(ParaName.KEY_MATCHID, myObject.getMatch_id());
                    hashMap1.put(ParaName.KEY_COMPANYSTATUS, "A");
                    hashMap1.put(ParaName.KEY_UID, myObject.getUser_id());
                    hashMap1.put(ParaName.KEY_JOBID, myObject.getJob_id());
                    if (rest.isInterentAvaliable()) {
                        AppController.ShowDialogue("", mContext);
                        postLikeDislike(hashMap1);
                    } else rest.AlertForInternet();
                }
            case R.id.tv_view_detail:
                Intent resultIntent = new Intent(mContext, UserDetail.class);
                resultIntent.putExtra("job_id", myObject.getJob_id());
                resultIntent.putExtra("id", myObject.getUser_id());
                resultIntent.putExtra("from", ViewResume.class.getSimpleName());
                resultIntent.putExtra("name", myObject.getFirst_name());
                mContext.startActivity(resultIntent);

                break;

            default:
                break;
        }
    }

    private static class MyWebViewClient extends WebViewClient {
        private boolean isLoad=true;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            AppController.dismissProgressdialog();
            if (isLoad){
                isLoad=false;
                view.loadUrl(url);
            }
            super.onPageFinished(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    }
    @Override
    public void onBackPressed() {
        if (wv_url.canGoBack()) wv_url.goBack();
        else{
            if (wv_url!=null){
                wv_url.clearCache(false);
                wv_url.clearHistory();
                wv_url.clearFormData();
                wv_url.clearMatches();
                wv_url.clearView();
            }
            finish();
        }
    }

    private void postLikeDislike(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().postUSerAcceptReject(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject repo = response.body();
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        setBackPressed();
                        AppController.loggedOut(mContext);
                    } else if (repo.get("status").getAsBoolean()) {
                        setBackPressed();
                    } else rest.showToast(repo.get("message").getAsString());
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }

    public void setBackPressed() {
        if(ResumesActivity.instance!=null)
            ResumesActivity.instance.finish();
        finish();
    }
}