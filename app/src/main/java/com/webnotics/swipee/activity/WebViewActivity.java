package com.webnotics.swipee.activity;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.webnotics.swipee.R;

public class WebViewActivity extends AppCompatActivity {
     WebView wv_url;
     TextView tv_title;
     ImageView iv_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        wv_url=findViewById(R.id.wv_url);
        tv_title=findViewById(R.id.tv_title);
        iv_back=findViewById(R.id.iv_back);

        if( getIntent()!=null){
            String url=getIntent().getStringExtra("url");
            String title=getIntent().getStringExtra("title");
            tv_title.setText(title);
            wv_url.loadUrl(url);
            wv_url.getSettings().setAllowContentAccess(true);
            wv_url.getSettings().setAllowFileAccess(true);
            wv_url.getSettings().setAllowFileAccessFromFileURLs(true);
            wv_url.getSettings().setJavaScriptEnabled(true);
            wv_url.getSettings().setBlockNetworkImage(false);
            wv_url.getSettings().setDomStorageEnabled(false);
        }

        iv_back.setOnClickListener(v -> onBackPressed());


    }

    @Override
    public void onBackPressed() {
        if (wv_url.canGoBack()) wv_url.goBack();
        else finish();
    }
}