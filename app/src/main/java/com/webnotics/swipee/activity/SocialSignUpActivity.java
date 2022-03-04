package com.webnotics.swipee.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings.Secure;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SocialSignUpActivity extends AppCompatActivity implements View.OnClickListener {


    Context mContext = SocialSignUpActivity.this;
    Rest rest;
    TextView donothaveanaccount, tv_seeker, tv_recruiter,tv_email;
    Button btn_signup;
    EditText et_mobile, et_password,et_email;


    FusedLocationProviderClient mFusedLocationClient;
    private String lat = "0";
    private String longg = "0";
    private boolean isSeeker=true;
    private String oauth_id="";
    private String email="";
    private String social_type="";
    private boolean is_email=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_signup_screen);
        rest = new Rest(mContext);
        if (getIntent() != null) {
            isSeeker = getIntent().getBooleanExtra("isSeeker",true);
              oauth_id = getIntent().getStringExtra(ParaName.KEY_OAUTHUID)!=null?getIntent().getStringExtra(ParaName.KEY_OAUTHUID):"";
              email = getIntent().getStringExtra(ParaName.KEY_EMAIL)!=null? getIntent().getStringExtra(ParaName.KEY_EMAIL):"";
              social_type = getIntent().getStringExtra(ParaName.KEY_SOCIALTYPE)!=null?getIntent().getStringExtra(ParaName.KEY_SOCIALTYPE):"";
              is_email = getIntent().getBooleanExtra("is_email", true);
        }
        donothaveanaccount = findViewById(R.id.donothaveanaccount);
        btn_signup = findViewById(R.id.btn_signup);
        tv_email = findViewById(R.id.tv_email);
        et_password = findViewById(R.id.et_password);
        et_mobile = findViewById(R.id.et_mobile);
        tv_seeker = findViewById(R.id.tv_seeker);
        tv_recruiter = findViewById(R.id.tv_recruiter);
        et_email = findViewById(R.id.et_email);
        tv_email.setText(email);

        if (is_email){
            et_email.setVisibility(View.VISIBLE);
            tv_email.setVisibility(View.GONE);
        }else {
            tv_email.setVisibility(View.VISIBLE);
            et_email.setVisibility(View.GONE);
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
        /////////////
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            int REQUEST_CODE_ASK_PERMISSIONS = 111;
            requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
        } else {
            try {
                getLastLocation();
            }catch (Exception e){}
        }

        FirebaseApp.initializeApp(mContext);

        btn_signup.setOnClickListener(this::onClick);

    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                            if (location == null) {
                                requestNewLocationData();
                            } else {
                                lat = location.getLatitude() + "";
                                longg = location.getLongitude() + "";
                                getAddress(mContext, location.getLatitude(), location.getLongitude());

                            }

                    }
                });

       /* try {
            mFusedLocationClient.getLastLocation().addOnCompleteListener(
                    task -> {
                        Location location = task.getResult();
                        if (location == null) {
                            requestNewLocationData();
                        } else {
                            lat = location.getLatitude() + "";
                            longg = location.getLongitude() + "";
                            getAddress(mContext, location.getLatitude(), location.getLongitude());

                        }
                    }
            );
        }catch (Exception e){}*/

    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(
                mLocationRequest, mLocationCallback,
                Looper.myLooper()
        );

    }


    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            lat = mLastLocation.getLatitude() + "";
            longg =  mLastLocation.getLongitude() + "";
            getAddress(mContext, mLastLocation.getLatitude(), mLastLocation.getLongitude());
        }
    };

    public void getAddress(Context context, double LATITUDE, double LONGITUDE) {

        //Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {
                lat = LATITUDE + "";
                longg = LONGITUDE + "";

            }
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_signup:

                String mobile = et_mobile.getText().toString();
              /*  if (lat.equalsIgnoreCase("0") ||longg.equalsIgnoreCase("0")){
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED) {
                        int REQUEST_CODE_ASK_PERMISSIONS = 111;
                        requestPermissions(new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                REQUEST_CODE_ASK_PERMISSIONS);
                    } else {
                        try {
                            getLastLocation();
                        }catch (Exception e){}
                    }

                }
                else*/ if (is_email && TextUtils.isEmpty(et_email.getText().toString())){
                    rest.showToast("Please enter email address");
                }else   if (is_email && !Config.isEmailValid(et_email.getText().toString())){
                    rest.showToast("Please enter a valid email address");
                }else if (TextUtils.isEmpty(mobile)) {
                    rest.showToast("Please enter phone number");
                } else if (mobile.length() < 10) {
                    rest.showToast("Please enter 10 digit phone number");
                }  else {
                    ///hit
                    //
                     if (is_email)
                         email=et_email.getText().toString();
                    Calendar calendar = Calendar.getInstance();
                    String time_zone = calendar.getTimeZone().getID();
                    String country = Locale.getDefault().getCountry();
                    String android_id = Secure.getString(getContentResolver(), Secure.ANDROID_ID);
                    String phone_code = "+91";
                     String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                    //        String device_token = FirebaseInstanceId.getInstance().getId();
                    String user_app_language = Locale.getDefault().getDisplayLanguage().toLowerCase(Locale.ROOT);

                    Log.d("djdjdjdj", "android_id  " + android_id);
                    HashMap<String, String> hashMap = new HashMap();
                    hashMap.put(ParaName.KEY_DEVICENAME, "Android");
                    hashMap.put(ParaName.KEY_LAT, lat);
                    hashMap.put(ParaName.KEY_LONG, longg);
                    hashMap.put(ParaName.KEY_DEVICETOKEN, refreshedToken);
                    hashMap.put(ParaName.KEY_DEVICEID, android_id);
                    hashMap.put(ParaName.KEY_LANGUAGE, user_app_language);
                    hashMap.put(ParaName.KEY_TIMEZONE, time_zone);
                    hashMap.put(ParaName.KEY_OAUTHUID, oauth_id);
                    hashMap.put(ParaName.KEY_SOCIALTYPE, social_type);
                    if (rest.isInterentAvaliable()) {
                        AppController.ShowDialogue("", mContext);
                        if (isSeeker){
                            hashMap.put(ParaName.KEY_EMAIL, email);
                            hashMap.put(ParaName.KEY_MOBILENO, mobile);
                            hashMap.put(ParaName.KEY_PASSWORD, "");
                            hashMap.put(ParaName.KEY_PHONECODE, phone_code);
                            hashMap.put(ParaName.KEY_ISEMAILVERIFY, is_email?"N":"Y");
                            callSeekerSignUpService(hashMap);
                        } else {
                            hashMap.put(ParaName.KEY_COMPANYEMAIL, email);
                            hashMap.put(ParaName.KEY_COMPANYPHONE, mobile);
                            hashMap.put(ParaName.KEY_COMPANYPASSWORD, "");
                            hashMap.put(ParaName.KEY_COMPANYPHONECODE, phone_code);
                            hashMap.put(ParaName.KEY_ISEMAILVERIFY, is_email?"N":"Y");
                            callSignUpCompany(hashMap);
                        }
                    } else {
                        rest.AlertForInternet();
                    }
                }
                break;
            default:
                break;
        }

    }

    private void callSeekerSignUpService(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().signUpUser(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    if (responseBody.get("code").getAsInt()==200 &&responseBody.get("status").getAsBoolean()) {
                        JsonObject data = responseBody.has("data") ? responseBody.get("data").getAsJsonObject() : null;
                        if (data != null && !data.isJsonNull()) {
                            String is_email_verify = data.has("is_email_verify") ? data.get("is_email_verify").isJsonNull() ? "" : data.get("is_email_verify").getAsString() : "";
                            String is_mobile_verify = data.has("is_mobile_verify") ? data.get("is_mobile_verify").isJsonNull() ? "" : data.get("is_mobile_verify").getAsString() : "";
                            Config.SetEmail(hashMap.get(ParaName.KEY_EMAIL));
                            Config.SetPhoneCode(hashMap.get(ParaName.KEY_PHONECODE));
                            Config.SetMobileNo(hashMap.get(ParaName.KEY_MOBILENO));
                            Config.SetLat(hashMap.get(ParaName.KEY_LAT));
                            Config.SetLongg(hashMap.get(ParaName.KEY_LONG));
                            String token = responseBody.get("data").getAsJsonObject().has("user_token") ? responseBody.get("data").getAsJsonObject().get("user_token").getAsString() : "";
                            Config.SetUserToken(token);
                            Config.SetIsSeeker(true);
                            if (!is_email_verify.equalsIgnoreCase("Y")){
                                startActivity(new Intent(mContext, BasicInfoActivity.class).putExtra("fragment", "email").putExtra("isSeeker",true));
                            }else  if (!is_mobile_verify.equalsIgnoreCase("Y")){
                                startActivity(new Intent(mContext, BasicInfoActivity.class).putExtra("fragment", "mobile").putExtra("isSeeker",true));
                            }else    startActivity(new Intent(mContext, BasicInfoActivity.class).putExtra("fragment", "email").putExtra("isSeeker",true));
                            finish();
                        }

                    } else {
                        if (responseBody.has("message"))
                            rest.showToast(responseBody.get("message").getAsString().replace(". ", ".\n"));
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

    private void callSignUpCompany(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().signUpCompany(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    Log.d("dbhdfhkjgd", responseBody.toString());
                    if (responseBody.get("code").getAsInt()==200 && responseBody.get("status").getAsBoolean()) {
                        JsonObject data = responseBody.has("data") ? responseBody.get("data").getAsJsonObject() : null;
                        if (data != null && !data.isJsonNull()) {
                            String is_email_verify = data.has("is_email_verify") ? data.get("is_email_verify").isJsonNull() ? "" : data.get("is_email_verify").getAsString() : "";
                            String is_mobile_verify = data.has("is_mobile_verify") ? data.get("is_mobile_verify").isJsonNull() ? "" : data.get("is_mobile_verify").getAsString() : "";
                            Config.SetEmail(hashMap.get(ParaName.KEY_COMPANYEMAIL));
                            Config.SetPhoneCode(hashMap.get(ParaName.KEY_COMPANYPHONECODE));
                            Config.SetMobileNo(hashMap.get(ParaName.KEY_COMPANYPHONE));
                            Config.SetLat(hashMap.get(ParaName.KEY_LAT));
                            Config.SetLongg(hashMap.get(ParaName.KEY_LONG));
                            String token = responseBody.get("data").getAsJsonObject().has("user_token") ? responseBody.get("data").getAsJsonObject().get("user_token").getAsString() : "";
                            Config.SetUserToken(token);
                            Config.SetIsSeeker(false);
                            if (!is_email_verify.equalsIgnoreCase("Y")){
                                startActivity(new Intent(mContext, BasicInfoActivity.class).putExtra("fragment", "email").putExtra("isSeeker",false));
                            }else  if (!is_mobile_verify.equalsIgnoreCase("Y")){
                                startActivity(new Intent(mContext, BasicInfoActivity.class).putExtra("fragment", "mobile").putExtra("isSeeker",false));
                            }else    startActivity(new Intent(mContext, BasicInfoActivity.class).putExtra("fragment", "email").putExtra("isSeeker",false));
                            finish();
                        }

                    } else {
                        if (responseBody.has("message"))
                            rest.showToast(responseBody.get("message").getAsString().replace(". ", ".\n"));
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