package com.webnotics.swipee.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.JsonObject;
import com.shantanudeshmukh.linkedinsdk.LinkedInBuilder;
import com.shantanudeshmukh.linkedinsdk.helpers.LinkedInUser;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.Seeker.SeekerHomeActivity;
import com.webnotics.swipee.activity.company.CompanyHomeActivity;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    EditText et_email, et_password;
    TextView tv_forgot, donothaveanaccount, tv_seeker, tv_recruiter;
    CheckBox cb_remember;
    Button btn_signin;
    ImageView iv_google,facebook,likedinnn,iv_show_or_hide_pass;
    Rest rest;
    private final Context mContext = LoginActivity.this;
    boolean isSeeker = true;

    private int RC_SIGN_IN = 0;

    private GoogleApiClient mGoogleApiClient;
    private String personName = "";
    private String personPhotoUrl = "";
    private String email = "";
    private String socialid = "";
    private String socialtype = "";
    private LoginButton btnLogin;
    private CallbackManager callbackManager;
    private boolean show=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            if (SeekerHomeActivity.instance != null)
                SeekerHomeActivity.instance.onBackPressed();
            if (CompanyHomeActivity.instance != null)
                CompanyHomeActivity.instance.onBackPressed();
        } catch (Exception ignored) {

        } finally {
            rest = new Rest(mContext);
            donothaveanaccount = findViewById(R.id.donothaveanaccount);
            et_email = findViewById(R.id.et_email);
            et_password = findViewById(R.id.et_password);
            tv_forgot = findViewById(R.id.tv_forgot);
            cb_remember = findViewById(R.id.cb_remember);
            btn_signin = findViewById(R.id.btn_signin);
            tv_seeker = findViewById(R.id.tv_seeker);
            tv_recruiter = findViewById(R.id.tv_recruiter);
            iv_google = findViewById(R.id.iv_google);
            btnLogin = findViewById(R.id.login_button);
            facebook = findViewById(R.id.facebook);
            likedinnn = findViewById(R.id.likedinnn);
            iv_show_or_hide_pass = findViewById(R.id.iv_show_or_hide_pass);

            FirebaseApp.initializeApp(mContext);
            /*FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, LoginActivity.class.getSimpleName());
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);*/
            tv_seeker.setBackgroundResource(R.drawable.white_semiround_bg);
            tv_recruiter.setBackgroundResource(0);
            tv_seeker.setTextColor(getColor(R.color.colorPrimary));
            tv_recruiter.setTextColor(getColor(R.color.white));
            donothaveanaccount.setText(Html.fromHtml(getString(R.string.donthaveaccount)));
            btn_signin.setOnClickListener(this);
            donothaveanaccount.setOnClickListener(this);
            tv_forgot.setOnClickListener(this);
            tv_seeker.setOnClickListener(this);
            tv_recruiter.setOnClickListener(this);
            tv_recruiter.setOnClickListener(this);
            iv_google.setOnClickListener(this);
            facebook.setOnClickListener(this);
            likedinnn.setOnClickListener(this);
            Config.clear(mContext);
            isSeeker = true;
            et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            btnLogin = findViewById(R.id.login_button);
            btnLogin.setReadPermissions(Arrays.asList("public_profile, email"));
            callbackManager = CallbackManager.Factory.create();

            /*Register facebook call back with button click that is use to generate Access token */
            // Callback registration
            btnLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

                @Override
                public void onSuccess(LoginResult loginResult) {
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            (object, response) -> {
                                Log.v("Main", response.toString());
                                setProfileToView(object);
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,first_name,last_name,email");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {

                }

                @Override
                public void onError(FacebookException exception) {
                    Toast.makeText(mContext, getResources().getString(R.string.errorloginfb), Toast.LENGTH_SHORT).show();
                }
            });

            iv_show_or_hide_pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (show){
                        iv_show_or_hide_pass.setImageResource(R.drawable.img_show_password);
                        et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        show=false;
                    }else {
                        iv_show_or_hide_pass.setImageResource(R.drawable.img_hide_password);
                        et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        show=true;
                    }
                }
            });


        }

    }


    private void setProfileToView(JSONObject object) {
        facebook.setClickable(true);
        try {
            personName = object.getString("first_name") + " " + object.getString("last_name");

            email = object.getString("email");
            socialid = object.getString("id");
            String image_url = "https://graph.facebook.com/" + socialid + "/picture?type=normal";
            socialtype = "facebook_social";
            Config.SetPICKURI(image_url);

           if (email!=null){
               setSocialLogin();
           }

        } catch (JSONException e) {
            e.printStackTrace();


        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_signin:

                String email1 = et_email.getText().toString();
                String password = et_password.getText().toString();
                if (TextUtils.isEmpty(email1)) {
                    rest.showToast("Please Enter Email Id");
                } else if (!Config.isEmailValid(email1)) {
                    rest.showToast("Please Enter Valid Email Id");
                } else if (TextUtils.isEmpty(password)) {
                    rest.showToast("Please Enter Valid Password");
                } else {
                    ///hit
                    if (rest.isInterentAvaliable()) {
                        AppController.ShowDialogue("", mContext);
                        ///hit
                        //  FirebaseApp.initializeApp(mContext);

                        Calendar calendar = Calendar.getInstance();
                        String time_zone = calendar.getTimeZone().getID().toString();
                        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                        String phone_code = "+91";
                         String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                        //        String device_token = FirebaseInstanceId.getInstance().getId();
                        String user_app_language = Locale.getDefault().getDisplayLanguage().toLowerCase(Locale.ROOT);

                        Log.d("djdjdjdj", "android_id  " + refreshedToken);
                        HashMap<String, String> hashMap = new HashMap();
                        hashMap.put(ParaName.KEY_EMAIL, email1);
                        hashMap.put(ParaName.KEY_PASSWORD, password);
                        hashMap.put(ParaName.KEY_DEVICENAME, "Android");
                        hashMap.put(ParaName.KEY_DEVICETOKEN, refreshedToken);
                        hashMap.put(ParaName.KEY_DEVICEID, android_id);
                        hashMap.put(ParaName.KEY_LANGUAGE, user_app_language);
                        hashMap.put(ParaName.KEY_TIMEZONE, time_zone);

                        if (isSeeker)
                            callSeekerLoginService(hashMap);
                        else callRecruiterLoginService(hashMap);
                    } else {
                        rest.AlertForInternet();
                    }

                }

                break;

            case R.id.donothaveanaccount:
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class).putExtra("isSeeker", isSeeker));
                break;
            case R.id.tv_forgot:
                startActivity(new Intent(LoginActivity.this, ForgotPassword.class).putExtra("isSeeker", isSeeker));
                break;
            case R.id.iv_google:
                email = "";
                callGoogleLogin();
                break;
            case R.id.facebook:
                facebook.setClickable(false);
                RC_SIGN_IN = 8;
                LoginManager.getInstance().logOut();
                btnLogin.performClick();
                break;
            case R.id.likedinnn:
                likedinnn.setClickable(false);
                RC_SIGN_IN = 9;
                CookieSyncManager.createInstance(this);

                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();
                LinkedInBuilder.getInstance(LoginActivity.this)
                        .setClientID(getString(R.string.linkdin_clientid))
                        .setClientSecret(getString(R.string.linkdin_secret))
                        .setRedirectURI("https://swipee.in")
                        .authenticate(9);
                break;
            case R.id.tv_seeker:
                tv_seeker.setBackgroundResource(R.drawable.white_semiround_bg);
                tv_recruiter.setBackgroundResource(0);
                tv_seeker.setTextColor(getColor(R.color.colorPrimary));
                tv_recruiter.setTextColor(getColor(R.color.white));
                isSeeker = true;
                break;
            case R.id.tv_recruiter:
                tv_seeker.setBackgroundResource(0);
                tv_recruiter.setBackgroundResource(R.drawable.white_semiround_bg);
                tv_seeker.setTextColor(getColor(R.color.white));
                tv_recruiter.setTextColor(getColor(R.color.colorPrimary));
                isSeeker = false;
                break;

            default:
                break;
        }
    }

    private void callGoogleLogin() {
        if (rest.isInterentAvaliable()) {
            iv_google.setClickable(false);
            RC_SIGN_IN = 7;
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                    .requestEmail()
                    .build();
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(mContext, gso);
            googleSignInClient.signOut();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(signInIntent, RC_SIGN_IN);
            OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
            if (opr.isDone()) {
                GoogleSignInResult result = opr.get();
                handleSignInResult(result);
            } else {
                opr.setResultCallback(this::handleSignInResult);
            }
        } else {
            rest.AlertForInternet();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        iv_google.setClickable(true);
        Log.d("", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            if ((acct != null ? acct.getDisplayName() : null) != null) {
                personName = acct.getDisplayName();
                if (acct.getPhotoUrl() != null) {
                    personPhotoUrl = acct.getPhotoUrl().toString();
                    Config.SetPICKURI(acct.getPhotoUrl().toString());
                }

                email = acct.getEmail();
                socialid = acct.getId();

                Log.e("loginimageurl", "Name: " + personName + ", email: " + email
                        + ", Image: " + personPhotoUrl);
                socialtype = "gmail_social";
               setSocialLogin();

            }

        } else {
            mGoogleApiClient.stopAutoManage(LoginActivity.this);
            mGoogleApiClient.disconnect();
            Log.d("", "onConnectionFailed:" + "error");
        }
    }

    private void setSocialLogin() {
        if (!TextUtils.isEmpty(email)) {
            if (rest.isInterentAvaliable()) {
                AppController.ShowDialogue("", mContext);
                ///hit
                Calendar calendar = Calendar.getInstance();
                String time_zone = calendar.getTimeZone().getID().toString();
                @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
                String phone_code = "+91";
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                String device_token = FirebaseInstanceId.getInstance().getId();
                String user_app_language = Locale.getDefault().getDisplayLanguage().toLowerCase(Locale.ROOT);

                Log.d("djdjdjdj", "android_id  " + refreshedToken);
                HashMap<String, String> hashMap = new HashMap();
                hashMap.put(ParaName.KEY_DEVICENAME, "Android");
                hashMap.put(ParaName.KEY_DEVICETOKEN, refreshedToken);
                hashMap.put(ParaName.KEY_DEVICEID, android_id);
                hashMap.put(ParaName.KEY_LANGUAGE, user_app_language);
                hashMap.put(ParaName.KEY_SOCIALTYPE, socialtype);
                hashMap.put(ParaName.KEY_OAUTHUID, socialid);
                hashMap.put(ParaName.KEY_TIMEZONE, time_zone);
                if (isSeeker){
                    hashMap.put(ParaName.KEY_EMAIL, email);
                    callSeekerSocialLoginService(hashMap);
                }
                else {
                    hashMap.put(ParaName.KEY_COMPANYEMAIL,email);
                    callRecruiterSocialLoginService(hashMap);
                }
            } else {
                rest.AlertForInternet();
            }
        }
    }


    private void callRecruiterLoginService(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().loginCompany(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    Log.d("responce", responseBody.toString());
                    if (responseBody.get("code").getAsInt() == 200 && responseBody.get("status").getAsBoolean()) {
                        JsonObject data = responseBody.has("data") ? responseBody.get("data").getAsJsonObject() : null;
                        if (data != null && !data.isJsonNull()) {
                            String company_name = data.has("company_name") ? data.get("company_name").isJsonNull() ? "" : data.get("company_name").getAsString() : "";
                            String fname = data.has("first_name") ? data.get("first_name").isJsonNull() ? "" : data.get("first_name").getAsString() : "";
                            String last_name = data.has("last_name") ? data.get("last_name").isJsonNull() ? "" : data.get("last_name").getAsString() : "";
                            String company_logo = data.has("company_logo") ? data.get("company_logo").isJsonNull() ? "" : data.get("company_logo").getAsString() : "";
                            String email = data.has("email") ? data.get("email").isJsonNull() ? "" : data.get("email").getAsString() : "";
                            String mobile_no = data.has("mobile_no") ? data.get("mobile_no").isJsonNull() ? "" : data.get("mobile_no").getAsString() : "";
                            String user_token = data.has("user_token") ? data.get("user_token").isJsonNull() ? "" : data.get("user_token").getAsString() : "";
                            String profile_status = data.has("profile_status") ? data.get("profile_status").isJsonNull() ? "" : data.get("profile_status").getAsString() : "";
                            String mobile_verify = data.has("mobile_verify") ? data.get("mobile_verify").isJsonNull() ? "" : data.get("mobile_verify").getAsString() : "";
                            String email_verify = data.has("email_verify") ? data.get("email_verify").isJsonNull() ? "" : data.get("email_verify").getAsString() : "";
                            String phone_code = data.has("phone_code") ? data.get("phone_code").isJsonNull() ? "" : data.get("phone_code").getAsString() : "";
                            String latitude = data.has("latitude") ? data.get("latitude").isJsonNull() ? "" : data.get("latitude").getAsString() : "";
                            String longitude = data.has("longitude") ? data.get("longitude").isJsonNull() ? "" : data.get("longitude").getAsString() : "";
                            Config.setRemember(cb_remember.isChecked());
                            Config.SetEmail(email);
                            Config.SetFName(fname);
                            Config.SetMobileNo(mobile_no);
                            Config.SetLName(last_name);
                            Config.SetName(fname + " " + last_name);
                            Config.SetPICKURI(company_logo);
                            Config.SetUserToken(user_token);
                            Config.SetPhoneCode(phone_code);
                            Config.SetCompanyName(company_name);
                            Config.SetLat(latitude);
                            Config.SetLongg(longitude);
                            Config.SetIsSeeker(false);
                            if (!email_verify.equalsIgnoreCase("Y")) {
                                startActivity(new Intent(LoginActivity.this, BasicInfoActivity.class).putExtra("fragment", "email").putExtra("isSeeker", false));
                                finish();
                            } else if (!mobile_verify.equalsIgnoreCase("Y")) {
                                startActivity(new Intent(LoginActivity.this, BasicInfoActivity.class).putExtra("fragment", "mobile").putExtra("isSeeker", false));
                                finish();
                            } else if (!profile_status.equalsIgnoreCase("Y")) {
                                startActivity(new Intent(LoginActivity.this, BasicInfoActivity.class).putExtra("fragment", "profileinfo").putExtra("isSeeker", false));
                                finish();
                            } else {
                                Config.SetIsProfileUpdate(profile_status);
                                Config.SetIsUserLogin(true);
                                startActivity(new Intent(LoginActivity.this, CompanyHomeActivity.class));
                                finish();
                            }
                        }
                    } else {
                        if (responseBody.has("message"))
                            rest.showToast(responseBody.get("message").getAsString());
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
    private void callRecruiterSocialLoginService(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().socialLoginCompany(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    Log.d("responce", responseBody.toString());
                    if (responseBody.get("code").getAsInt() == 200 && responseBody.get("status").getAsBoolean()) {
                        JsonObject data = responseBody.has("data") ? responseBody.get("data").getAsJsonObject() : null;
                        if (data != null && !data.isJsonNull()) {
                            String company_name = data.has("company_name") ? data.get("company_name").isJsonNull() ? "" : data.get("company_name").getAsString() : "";
                            String fname = data.has("first_name") ? data.get("first_name").isJsonNull() ? "" : data.get("first_name").getAsString() : "";
                            String last_name = data.has("last_name") ? data.get("last_name").isJsonNull() ? "" : data.get("last_name").getAsString() : "";
                            String company_logo = data.has("company_logo") ? data.get("company_logo").isJsonNull() ? "" : data.get("company_logo").getAsString() : "";
                            String email = data.has("email") ? data.get("email").isJsonNull() ? "" : data.get("email").getAsString() : "";
                            String mobile_no = data.has("mobile_no") ? data.get("mobile_no").isJsonNull() ? "" : data.get("mobile_no").getAsString() : "";
                            String user_token = data.has("user_token") ? data.get("user_token").isJsonNull() ? "" : data.get("user_token").getAsString() : "";
                            String profile_status = data.has("profile_status") ? data.get("profile_status").isJsonNull() ? "" : data.get("profile_status").getAsString() : "";
                            String mobile_verify = data.has("mobile_verify") ? data.get("mobile_verify").isJsonNull() ? "" : data.get("mobile_verify").getAsString() : "";
                            String email_verify = data.has("email_verify") ? data.get("email_verify").isJsonNull() ? "" : data.get("email_verify").getAsString() : "";
                            String phone_code = data.has("phone_code") ? data.get("phone_code").isJsonNull() ? "" : data.get("phone_code").getAsString() : "";
                            String latitude = data.has("latitude") ? data.get("latitude").isJsonNull() ? "" : data.get("latitude").getAsString() : "";
                            String longitude = data.has("longitude") ? data.get("longitude").isJsonNull() ? "" : data.get("longitude").getAsString() : "";
                            boolean is_social_login = data.has("is_social_login") && (!data.get("is_social_login").isJsonNull() && data.get("is_social_login").getAsBoolean());

                                Config.SetCompanyName(personName);
                                Config.SetPICKURI(personPhotoUrl);

                           if (is_social_login){
                               if (!TextUtils.isEmpty(mobile_no)){
                                   Config.setRemember(cb_remember.isChecked());
                                   Config.SetEmail(email);
                                   Config.SetMobileNo(mobile_no);
                                   Config.SetPICKURI(company_logo);
                                   Config.SetUserToken(user_token);
                                   Config.SetPhoneCode(phone_code);
                                   Config.SetLat(latitude);
                                   Config.SetLongg(longitude);
                                   Config.SetIsSeeker(false);

                                   if (!email_verify.equalsIgnoreCase("Y")) {
                                       startActivity(new Intent(LoginActivity.this, BasicInfoActivity.class).putExtra("fragment", "email").putExtra("isSeeker", false));
                                       finish();
                                   } else if (!mobile_verify.equalsIgnoreCase("Y")) {
                                       startActivity(new Intent(LoginActivity.this, BasicInfoActivity.class).putExtra("fragment", "mobile").putExtra("isSeeker", false));
                                       finish();
                                   } else if (!profile_status.equalsIgnoreCase("Y")) {
                                       startActivity(new Intent(LoginActivity.this, BasicInfoActivity.class).putExtra("fragment", "profileinfo").putExtra("isSeeker", false));
                                       finish();
                                   } else {
                                       Config.SetIsProfileUpdate(profile_status);
                                       Config.SetIsUserLogin(true);
                                       startActivity(new Intent(LoginActivity.this, CompanyHomeActivity.class));
                                       finish();
                                   }
                               }else {
                                   startActivity(new Intent(LoginActivity.this, SocialSignUpActivity.class).putExtra("isSeeker", isSeeker).putExtra(ParaName.KEY_OAUTHUID, socialid).putExtra(ParaName.KEY_SOCIALTYPE, socialtype).putExtra(ParaName.KEY_EMAIL, hashMap.get(ParaName.KEY_COMPANYEMAIL)));

                               }

                           }else {
                               startActivity(new Intent(LoginActivity.this, SocialSignUpActivity.class).putExtra("isSeeker", isSeeker).putExtra(ParaName.KEY_OAUTHUID, socialid).putExtra(ParaName.KEY_SOCIALTYPE, socialtype).putExtra(ParaName.KEY_EMAIL, hashMap.get(ParaName.KEY_COMPANYEMAIL)));

                           }


                        }
                    } else {
                        if (responseBody.has("message"))
                            rest.showToast(responseBody.get("message").getAsString());
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


    private void callSeekerLoginService(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().loginUser(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    Log.d("responce", responseBody.toString());
                    if (responseBody.get("code").getAsInt() == 200 && responseBody.get("status").getAsBoolean()) {
                        JsonObject data = responseBody.has("data") ? responseBody.get("data").getAsJsonObject() : null;
                        if (data != null && !data.isJsonNull()) {
                            String fname = data.has("first_name") ? data.get("first_name").isJsonNull() ? "" : data.get("first_name").getAsString() : "";
                            String last_name = data.has("last_name") ? data.get("last_name").isJsonNull() ? "" : data.get("last_name").getAsString() : "";
                            String user_profile = data.has("user_profile") ? data.get("user_profile").isJsonNull() ? "" : data.get("user_profile").getAsString() : "";
                            String email = data.has("email") ? data.get("email").isJsonNull() ? "" : data.get("email").getAsString() : "";
                            String mobile_no = data.has("mobile_no") ? data.get("mobile_no").isJsonNull() ? "" : data.get("mobile_no").getAsString() : "";
                            String middle_name = data.has("middle_name") ? data.get("middle_name").isJsonNull() ? "" : data.get("middle_name").getAsString() : "";
                            String user_token = data.has("user_token") ? data.get("user_token").isJsonNull() ? "" : data.get("user_token").getAsString() : "";
                            String is_profile_updated = data.has("is_profile_updated") ? data.get("is_profile_updated").isJsonNull() ? "" : data.get("is_profile_updated").getAsString() : "";
                            String mobile_verify = data.has("mobile_verify") ? data.get("mobile_verify").isJsonNull() ? "" : data.get("mobile_verify").getAsString() : "";
                            String email_verify = data.has("email_verify") ? data.get("email_verify").isJsonNull() ? "" : data.get("email_verify").getAsString() : "";
                            String phone_code = data.has("phone_code") ? data.get("phone_code").isJsonNull() ? "" : data.get("phone_code").getAsString() : "";
                            String latitude = data.has("latitude") ? data.get("latitude").isJsonNull() ? "" : data.get("latitude").getAsString() : "";
                            String longitude = data.has("longitude") ? data.get("longitude").isJsonNull() ? "" : data.get("longitude").getAsString() : "";
                            Config.setRemember(cb_remember.isChecked());
                            Config.SetEmail(email);
                            Config.SetFName(fname);
                            Config.SetMobileNo(mobile_no);
                            Config.SetLName(last_name);
                            Config.SetName(fname + ((TextUtils.isEmpty(middle_name) ? "" : " " + middle_name)) + ((TextUtils.isEmpty(last_name) ? "" : " " + last_name)));
                            Config.SetUserToken(user_token);
                            Config.SetPhoneCode(phone_code);
                            Config.SetIsSeeker(true);
                            Config.SetLat(latitude);
                            Config.SetPICKURI(user_profile);
                            Config.SetLongg(longitude);
                            if (!email_verify.equalsIgnoreCase("Y")) {
                                startActivity(new Intent(LoginActivity.this, BasicInfoActivity.class).putExtra("fragment", "email").putExtra("isSeeker", true));
                                finish();
                            } else if (!mobile_verify.equalsIgnoreCase("Y")) {
                                startActivity(new Intent(LoginActivity.this, BasicInfoActivity.class).putExtra("fragment", "mobile").putExtra("isSeeker", true));
                                finish();
                            } else if (!is_profile_updated.equalsIgnoreCase("Y")) {
                                startActivity(new Intent(LoginActivity.this, BasicInfoActivity.class).putExtra("fragment", "profileinfo").putExtra("isSeeker", true));
                                finish();
                            } else {
                                Config.SetIsProfileUpdate(is_profile_updated);
                                Config.SetIsUserLogin(true);
                                startActivity(new Intent(LoginActivity.this, SeekerHomeActivity.class));
                                finish();
                            }
                        }
                    } else {
                        if (responseBody.has("message"))
                            rest.showToast(responseBody.get("message").getAsString());
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
    private void callSeekerSocialLoginService(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().socialLoginUser(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    Log.d("responce", responseBody.toString());
                    if (responseBody.get("code").getAsInt() == 200 && responseBody.get("status").getAsBoolean()) {
                        JsonObject data = responseBody.has("data") ? responseBody.get("data").getAsJsonObject() : null;
                        if (data != null && !data.isJsonNull()) {
                            String fname = data.has("first_name") ? data.get("first_name").isJsonNull() ? "" : data.get("first_name").getAsString() : "";
                            String last_name = data.has("last_name") ? data.get("last_name").isJsonNull() ? "" : data.get("last_name").getAsString() : "";
                            String user_profile = data.has("user_profile") ? data.get("user_profile").isJsonNull() ? "" : data.get("user_profile").getAsString() : "";
                            String email = data.has("email") ? data.get("email").isJsonNull() ? "" : data.get("email").getAsString() : "";
                            String mobile_no = data.has("mobile_no") ? data.get("mobile_no").isJsonNull() ? "" : data.get("mobile_no").getAsString() : "";
                            String middle_name = data.has("middle_name") ? data.get("middle_name").isJsonNull() ? "" : data.get("middle_name").getAsString() : "";
                            String user_token = data.has("user_token") ? data.get("user_token").isJsonNull() ? "" : data.get("user_token").getAsString() : "";
                            String is_profile_updated = data.has("is_profile_updated") ? data.get("is_profile_updated").isJsonNull() ? "" : data.get("is_profile_updated").getAsString() : "";
                            String mobile_verify = data.has("mobile_verify") ? data.get("mobile_verify").isJsonNull() ? "" : data.get("mobile_verify").getAsString() : "";
                            String email_verify = data.has("email_verify") ? data.get("email_verify").isJsonNull() ? "" : data.get("email_verify").getAsString() : "";
                            String phone_code = data.has("phone_code") ? data.get("phone_code").isJsonNull() ? "" : data.get("phone_code").getAsString() : "";
                            String latitude = data.has("latitude") ? data.get("latitude").isJsonNull() ? "" : data.get("latitude").getAsString() : "";
                            String longitude = data.has("longitude") ? data.get("longitude").isJsonNull() ? "" : data.get("longitude").getAsString() : "";
                            boolean is_social_login = data.has("is_social_login") && (!data.get("is_social_login").isJsonNull() && data.get("is_social_login").getAsBoolean());
                            if (personName.contains(" ")){
                                String first_name=personName.substring(0,personName.indexOf(" "));
                                String lastq_name=!personName.substring(personName.indexOf(" ")+1).isEmpty()?personName.substring(personName.indexOf(" ")+1):"";
                                Config.SetFName(first_name);
                                Config.SetLName(lastq_name);

                            }else {
                                Config.SetFName(personName);
                                Config.SetLName("");
                            }
                            Config.SetName(personName);

                            Config.SetPICKURI(personPhotoUrl);
                            if (is_social_login){
                                if (!TextUtils.isEmpty(mobile_no)){
                                    Config.setRemember(cb_remember.isChecked());
                                    Config.SetEmail(email);
                                    Config.SetMobileNo(mobile_no);
                                    Config.SetUserToken(user_token);
                                    Config.SetPhoneCode(phone_code);
                                    Config.SetIsSeeker(true);
                                    Config.SetLat(latitude);
                                    Config.SetPICKURI(user_profile);
                                    Config.SetLongg(longitude);
                                    if (!email_verify.equalsIgnoreCase("Y")) {
                                        startActivity(new Intent(LoginActivity.this, BasicInfoActivity.class).putExtra("fragment", "email").putExtra("isSeeker", true));
                                        finish();
                                    } else if (!mobile_verify.equalsIgnoreCase("Y")) {
                                        startActivity(new Intent(LoginActivity.this, BasicInfoActivity.class).putExtra("fragment", "mobile").putExtra("isSeeker", true));
                                        finish();
                                    } else if (!is_profile_updated.equalsIgnoreCase("Y")) {
                                        startActivity(new Intent(LoginActivity.this, BasicInfoActivity.class).putExtra("fragment", "profileinfo").putExtra("isSeeker", true));
                                        finish();
                                    } else {
                                        Config.SetIsProfileUpdate(is_profile_updated);
                                        Config.SetIsUserLogin(true);
                                        startActivity(new Intent(LoginActivity.this, SeekerHomeActivity.class));
                                        finish();
                                    }
                                }else {
                                    startActivity(new Intent(LoginActivity.this, SocialSignUpActivity.class).putExtra("isSeeker", isSeeker).putExtra(ParaName.KEY_OAUTHUID, socialid).putExtra(ParaName.KEY_SOCIALTYPE, socialtype).putExtra(ParaName.KEY_EMAIL, hashMap.get(ParaName.KEY_EMAIL)));
                                }

                            }else {
                                startActivity(new Intent(LoginActivity.this, SocialSignUpActivity.class).putExtra("isSeeker", isSeeker).putExtra(ParaName.KEY_OAUTHUID, socialid).putExtra(ParaName.KEY_SOCIALTYPE, socialtype).putExtra(ParaName.KEY_EMAIL, hashMap.get(ParaName.KEY_EMAIL)));
                            }

                        }
                    } else {
                        if (responseBody.has("message"))
                            rest.showToast(responseBody.get("message").getAsString());
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


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (RC_SIGN_IN) {

            case 7:
                iv_google.setClickable(true);
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result != null)
                    handleSignInResult(result);
                break;

            case 8:
                facebook.setClickable(true);
                callbackManager.onActivityResult(requestCode, resultCode, data);
                break;

            case 9:
                likedinnn.setClickable(true);
                //Successfully signed in and retrieved data
                try {
                    if (data != null) {
                        if (data.getParcelableExtra("social_login")!=null){
                            LinkedInUser user = data.getParcelableExtra("social_login");
                            setUserData(user);
                        }else {
                            Log.d("LINKEDINID", ""+data.getExtras());
                        }

                    }
                }catch (Exception ignored){}

                break;
        }
    }
    private void setUserData(LinkedInUser user) {


        try {
            if (user!=null){
                if (user.getId()!=null){

                    personName = user.getFirstName() + " " + user.getLastName();
                    email = user.getEmail();
                    personPhotoUrl=user.getProfileUrl();
                    socialtype = "linkd_social";
                    socialid = user.getId();
                    Log.d("LINKEDINID", ""+personPhotoUrl);
                   if (email!=null){
                       setSocialLogin();
                   }
                }
            }
        }catch (Exception ignored){}



    }
}