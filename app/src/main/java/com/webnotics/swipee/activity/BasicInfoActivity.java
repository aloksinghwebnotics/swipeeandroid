package com.webnotics.swipee.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.webnotics.swipee.R;
import com.webnotics.swipee.fragments.EmailVerificationScreen;
import com.webnotics.swipee.fragments.MobileVerificationScreen;
import com.webnotics.swipee.fragments.company.ProfileDetailScreen;
import com.webnotics.swipee.fragments.seeker.ProfileInfoScreen;


public class BasicInfoActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 211;
    @SuppressLint("StaticFieldLeak")
    public static BasicInfoActivity instance;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public boolean isSeeker = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basicinfo);
        instance = this;
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        Intent intent = getIntent();
        if (intent != null) {
            String fragmentOpen = intent.getStringExtra("fragment");
            isSeeker = intent.getBooleanExtra("isSeeker", true);
            if (fragmentOpen.equalsIgnoreCase("profileinfo")) {
                attachProfileInfo();
            } else if (fragmentOpen.equalsIgnoreCase("mobile")) {
                attachMobileVerification();
            } else {
                attachEmailVerification();
            }
        } else attachEmailVerification();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA,  Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_ASK_PERMISSIONS);
        }

    }

    private void attachEmailVerification() {
        try {
            if (fragmentTransaction != null) {
                EmailVerificationScreen basefragment = new EmailVerificationScreen();
                fragmentManager.beginTransaction().replace(R.id.frame_fragment, basefragment).commit();
            }
        } catch (Exception ignored) {
        }
    }

    public void attachProfileInfo() {
        if (isSeeker) {
            try {
                if (fragmentTransaction != null) {
                    ProfileInfoScreen basefragment = new ProfileInfoScreen();
                    fragmentManager.beginTransaction().replace(R.id.frame_fragment, basefragment).commit();
                }
            } catch (Exception ignored) {
            }
        } else {
            try {
                if (fragmentTransaction != null) {
                    ProfileDetailScreen basefragment = new ProfileDetailScreen();
                    fragmentManager.beginTransaction().replace(R.id.frame_fragment, basefragment).commit();
                }
            } catch (Exception ignored) {
            }
        }
    }

    public void attachMobileVerification() {
        try {
            if (fragmentTransaction != null) {
                MobileVerificationScreen basefragment = new MobileVerificationScreen();
                fragmentManager.beginTransaction().replace(R.id.frame_fragment, basefragment).commit();
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onResume() {

        super.onResume();
    }


    @Override
    public void onClick(View view) {

    }
}