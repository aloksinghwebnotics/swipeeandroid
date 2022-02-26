package com.webnotics.swipee.activity.Seeker;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.NotificationActivity;
import com.webnotics.swipee.activity.SettingsActivity;
import com.webnotics.swipee.fragments.company.CompanyChatFragments;
import com.webnotics.swipee.fragments.seeker.MatchFragments;
import com.webnotics.swipee.fragments.seeker.NearFragments;
import com.webnotics.swipee.fragments.seeker.PlansFragments;
import com.webnotics.swipee.fragments.seeker.ProfileFragments;
import com.webnotics.swipee.interfaces.CountersInterface;
import com.webnotics.swipee.model.seeker.EmployeeUserDetails;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeekerHomeActivity extends AppCompatActivity implements View.OnClickListener, CountersInterface, PaymentResultWithDataListener {
    Context mContext;
    LinearLayout nearlay, matchlay, planlay, chatlay, accountlay;
    LinearLayout bottomlay, ll_nav_home, ll_nav_liked, ll_nav_matched, ll_nav_saved, ll_nav_applied, ll_nav_featured, ll_nav_appoiment, ll_nav_setting;
    ImageView nearimg, matchimg, planimg, imgchat, imgaccount, menu, search_icon, notification;
    public ImageView filter_icon;
    private TextView neartxt, matchtxt, planstxt, chattxt, accounttxt, counter11, tv_name, tv_email;
    private DrawerLayout drawer;
    RelativeLayout headerlay, notilay,rl_match_top,rl_near_top;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    CircleImageView civ_profile;
    public TextView tv_viewAll;

    @SuppressLint("StaticFieldLeak")
    public static SeekerHomeActivity instance;
    public boolean isFilterShowing = false;
    public CountersInterface countersInterface;
    public EmployeeUserDetails employeeuserdetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboardemployee);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        instance = this;

        initiate();
        countersInterface = this;
    }

    private void initiate() {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        counter11 = findViewById(R.id.counter11);
        notilay = findViewById(R.id.notilay);

        nearlay = findViewById(R.id.ll_near);
        matchlay = findViewById(R.id.ll_match);
        planlay = findViewById(R.id.ll_plans);
        chatlay = findViewById(R.id.ll_chat);
        accountlay = findViewById(R.id.ll_profile);

        rl_match_top=findViewById(R.id.rl_match_top);
        rl_near_top=findViewById(R.id.rl_near_top);
        tv_viewAll=findViewById(R.id.tv_viewAll);

        nearimg = findViewById(R.id.iv_near);
        matchimg = findViewById(R.id.iv_match);
        planimg = findViewById(R.id.iv_plans);
        imgchat = findViewById(R.id.iv_chat);
        imgaccount = findViewById(R.id.iv_profile);

        neartxt = findViewById(R.id.tv_near);
        matchtxt = findViewById(R.id.tv_match);
        planstxt = findViewById(R.id.tv_plans);
        chattxt = findViewById(R.id.tv_chat);
        accounttxt = findViewById(R.id.tv_profile);

        menu = findViewById(R.id.menu);
        search_icon = findViewById(R.id.search_icon);
        notification = findViewById(R.id.notification);
        filter_icon = findViewById(R.id.filter_icon);
        drawer = findViewById(R.id.drawer_layout);
        headerlay = findViewById(R.id.headerlay);
        bottomlay = findViewById(R.id.bottomlay);


        ll_nav_home = findViewById(R.id.ll_nav_home);
        ll_nav_liked = findViewById(R.id.ll_nav_liked);
        ll_nav_matched = findViewById(R.id.ll_nav_matched);
        ll_nav_saved = findViewById(R.id.ll_nav_saved);
        ll_nav_applied = findViewById(R.id.ll_nav_applied);
        ll_nav_featured = findViewById(R.id.ll_nav_featured);
        ll_nav_appoiment = findViewById(R.id.ll_nav_appoiment);
        ll_nav_setting = findViewById(R.id.ll_nav_setting);
        civ_profile = findViewById(R.id.civ_profile);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_name.setText(TextUtils.isEmpty(Config.GetName()) ? Config.GetFName() + " " + Config.GetLName() : Config.GetName());
        tv_email.setText(Config.GetEmail());
        Glide.with(mContext).load(Config.GetPICKURI()).error(R.drawable.ic_profile_select).placeholder(R.drawable.ic_profile_select).into(civ_profile);

        nearlay.setOnClickListener(this);
        matchlay.setOnClickListener(this);
        planlay.setOnClickListener(this);
        chatlay.setOnClickListener(this);
        accountlay.setOnClickListener(this);
        menu.setOnClickListener(this);
        search_icon.setOnClickListener(this);
        filter_icon.setOnClickListener(this);
        ll_nav_home.setOnClickListener(this);
        ll_nav_liked.setOnClickListener(this);
        ll_nav_matched.setOnClickListener(this);
        ll_nav_saved.setOnClickListener(this);
        ll_nav_applied.setOnClickListener(this);
        ll_nav_featured.setOnClickListener(this);
        ll_nav_appoiment.setOnClickListener(this);
        ll_nav_setting.setOnClickListener(this);
        notilay.setOnClickListener(this);
        tv_viewAll.setOnClickListener(this);

        civ_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(Config.GetPICKURI()))
                    AppController.callFullImage(mContext, Config.GetPICKURI());
            }
        });

        setMatchFragment();
        getProfileData();

        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatout = new SimpleDateFormat("dd MM yyyy");
            if (TextUtils.isEmpty(Config.GetCollegeRefreshDate())){
                callCollegeList();
            }else {
                Date d1   = format.parse(Config.GetCollegeRefreshDate());
                Date d2 = format.parse(Calendar.getInstance().getTime().toString());
                if (d1!=null && d2!=null){
                    String date1=formatout.format(d1);
                    String date2=formatout.format(d2);
                    Date final1=formatout.parse(date1);
                    Date final2=formatout.parse(date2);
                    if (final1!=null && final2!=null){
                        if(final1.compareTo(final2) != 0) {
                            callCollegeList();
                        }else {
                            callLocation();
                        }
                    }else {

                        callCollegeList();
                    }
                }else callCollegeList();



            }
        } catch (ParseException e) {
            e.printStackTrace();
        }




    }

    private void callLocation() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
            SimpleDateFormat formatout = new SimpleDateFormat("dd MM yyyy");
            if (TextUtils.isEmpty(Config.GetLocationRefreshDate())){
                callLocationList();
            }else {
                Date d1   = format.parse(Config.GetLocationRefreshDate());
                Date d2 = format.parse(Calendar.getInstance().getTime().toString());
                if (d1!=null && d2!=null){
                    String date1=formatout.format(d1);
                    String date2=formatout.format(d2);
                    Date final1=formatout.parse(date1);
                    Date final2=formatout.parse(date2);
                    if (final1!=null && final2!=null){
                        if(final1.compareTo(final2) != 0) {
                            Log.d("hhhhh","Hit from date");
                            callLocationList();
                        }else {

                        }
                    }else {
                        Log.d("hhhhh","Hit from null");
                        callLocationList();
                    }
                }else    callLocationList();

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void callCollegeList() {
        SwipeeApiClient.swipeeServiceInstance().getCollege().enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    JsonArray mArrayListData = responseBody.has("data") ? responseBody.get("data").getAsJsonArray() : new JsonArray();
                    if (mArrayListData.size()>0){
                        writeCollege(mArrayListData.toString());
                        Config.SetCollegeRefreshDate(Calendar.getInstance().getTime().toString());
                        callLocation();
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });
    }

    private void writeCollege(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("college.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void callLocationList() {
        SwipeeApiClient.swipeeServiceInstance().getLocation(Config.GetUserToken()).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    if (response.body().get("code").getAsInt() == 200) {
                        JsonArray mArrayListData = responseBody.has("data") ? responseBody.get("data").getAsJsonArray() : new JsonArray();
                        writeToFile(mArrayListData.toString());
                        Config.SetLocationRefreshDate(Calendar.getInstance().getTime().toString());

                    }


                }

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }

    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("location.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    public void setMatchFragment() {

        try {
            if (fragmentTransaction != null) {
                MatchFragments basefragment = new MatchFragments();
                fragmentManager.beginTransaction().replace(R.id.homegragment, basefragment).commit();
            }
        } catch (Exception ignored) {
        }
        matchimg.setImageResource(R.drawable.ic_match_selected);
        nearimg.setImageResource(R.drawable.ic_near_unselect);
        planimg.setImageResource(R.drawable.ic_plan_unselect);
        imgchat.setImageResource(R.drawable.ic_chat_unselect);
        imgaccount.setImageResource(R.drawable.ic_profile_dashboard);

        matchtxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        neartxt.setTextColor(getResources().getColor(R.color.gray));
        planstxt.setTextColor(getResources().getColor(R.color.gray));
        chattxt.setTextColor(getResources().getColor(R.color.gray));
        accounttxt.setTextColor(getResources().getColor(R.color.gray));
        rl_match_top.setVisibility(View.VISIBLE);
        rl_near_top.setVisibility(View.GONE);
    }

    private void setNearFragment() {

        try {
            if (fragmentTransaction != null) {
                NearFragments basefragment = new NearFragments();
                fragmentManager.beginTransaction().replace(R.id.homegragment, basefragment).commit();
            }
        } catch (Exception ignored) {
        }
        matchimg.setImageResource(R.drawable.ic_match_unselected);
        nearimg.setImageResource(R.drawable.ic_near_select);
        planimg.setImageResource(R.drawable.ic_plan_unselect);
        imgchat.setImageResource(R.drawable.ic_chat_unselect);
        imgaccount.setImageResource(R.drawable.ic_profile_dashboard);

        matchtxt.setTextColor(getResources().getColor(R.color.gray));
        neartxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        planstxt.setTextColor(getResources().getColor(R.color.gray));
        chattxt.setTextColor(getResources().getColor(R.color.gray));
        accounttxt.setTextColor(getResources().getColor(R.color.gray));
        rl_match_top.setVisibility(View.GONE);
        rl_near_top.setVisibility(View.VISIBLE);
    }

    public void setPlansFragment() {
        try {
            if (fragmentTransaction != null) {
                PlansFragments basefragment = new PlansFragments();
                fragmentManager.beginTransaction().replace(R.id.homegragment, basefragment).commit();
            }
        } catch (Exception ignored) {
        }
        matchimg.setImageResource(R.drawable.ic_match_unselected);
        nearimg.setImageResource(R.drawable.ic_near_unselect);
        planimg.setImageResource(R.drawable.ic_plan_select);
        imgchat.setImageResource(R.drawable.ic_chat_unselect);
        imgaccount.setImageResource(R.drawable.ic_profile_dashboard);

        matchtxt.setTextColor(getResources().getColor(R.color.gray));
        neartxt.setTextColor(getResources().getColor(R.color.gray));
        planstxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        chattxt.setTextColor(getResources().getColor(R.color.gray));
        accounttxt.setTextColor(getResources().getColor(R.color.gray));
        rl_match_top.setVisibility(View.GONE);
        rl_near_top.setVisibility(View.GONE);
    }

    private void setChatFragment() {
        try {
            if (fragmentTransaction != null) {
                CompanyChatFragments basefragment = new CompanyChatFragments();
                fragmentManager.beginTransaction().replace(R.id.homegragment, basefragment).commit();
            }
        } catch (Exception ignored) {
        }
        matchimg.setImageResource(R.drawable.ic_match_unselected);
        nearimg.setImageResource(R.drawable.ic_near_unselect);
        planimg.setImageResource(R.drawable.ic_plan_unselect);
        imgchat.setImageResource(R.drawable.ic_chat_select);
        imgaccount.setImageResource(R.drawable.ic_profile_dashboard);

        matchtxt.setTextColor(getResources().getColor(R.color.gray));
        neartxt.setTextColor(getResources().getColor(R.color.gray));
        planstxt.setTextColor(getResources().getColor(R.color.gray));
        chattxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        accounttxt.setTextColor(getResources().getColor(R.color.gray));
        rl_match_top.setVisibility(View.GONE);
        rl_near_top.setVisibility(View.GONE);
    }

    private void setProfileFragment() {
        try {
            if (fragmentTransaction != null) {
                ProfileFragments basefragment = new ProfileFragments();
                fragmentManager.beginTransaction().replace(R.id.homegragment, basefragment).commit();
            }
        } catch (Exception ignored) {
        }
        matchimg.setImageResource(R.drawable.ic_match_unselected);
        nearimg.setImageResource(R.drawable.ic_near_unselect);
        planimg.setImageResource(R.drawable.ic_plan_unselect);
        imgchat.setImageResource(R.drawable.ic_chat_unselect);
        imgaccount.setImageResource(R.drawable.ic_profile_select);

        matchtxt.setTextColor(getResources().getColor(R.color.gray));
        neartxt.setTextColor(getResources().getColor(R.color.gray));
        planstxt.setTextColor(getResources().getColor(R.color.gray));
        chattxt.setTextColor(getResources().getColor(R.color.gray));
        accounttxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        rl_match_top.setVisibility(View.GONE);
        rl_near_top.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.menu:
                tv_name.setText(TextUtils.isEmpty(Config.GetName()) ? Config.GetFName() + " " + Config.GetLName() : Config.GetName());
                tv_email.setText(Config.GetEmail());
                Glide.with(mContext).load(Config.GetPICKURI()).error(R.drawable.ic_profile_select).placeholder(R.drawable.ic_profile_select).into(civ_profile);
                isDrawerOpen();
                break;

            case R.id.notilay:
                startActivity(new Intent(mContext, NotificationActivity.class));
                break;

            case R.id.filter_icon:
                if (isFilterShowing) {
                    if (MatchFragments.instance != null) {
                        MatchFragments.instance.callFilter(false);
                    }
                } else {
                    if (MatchFragments.instance != null) {
                        MatchFragments.instance.callFilter(true);
                    }
                }

                break;
            case R.id.ll_near:
                setNearFragment();
                break;

            case R.id.ll_match:
                setMatchFragment();
                break;

            case R.id.ll_nav_home:
                setMatchFragment();
                isDrawerOpen();
                break;

            case R.id.ll_plans:
                setPlansFragment();
                break;

            case R.id.ll_chat:
                setChatFragment();
                break;
            case R.id.ll_profile:
                setProfileFragment();
                break;

            case R.id.ll_nav_liked:
                isDrawerOpen();
                startActivity(new Intent(this, LikedJobsActivity.class));
                break;

            case R.id.ll_nav_matched:
                isDrawerOpen();
                startActivity(new Intent(this, MatchedCompanyActivity.class));
                break;

            case R.id.ll_nav_saved:
                isDrawerOpen();
                startActivity(new Intent(this, SavedJobsActivity.class));
                break;

            case R.id.ll_nav_applied:
                isDrawerOpen();
                startActivity(new Intent(this, AppliedJobsActivity.class));
                break;

            case R.id.ll_nav_featured:
                isDrawerOpen();
                startActivity(new Intent(this, FeaturedJobsActivity.class));
                break;

            case R.id.ll_nav_appoiment:
                isDrawerOpen();
                startActivity(new Intent(this, AppoimentActivity.class));
                break;

            case R.id.ll_nav_setting:
                isDrawerOpen();
                startActivity(new Intent(this, SettingsActivity.class));
                break;
 case R.id.tv_viewAll:
                 tv_viewAll.setVisibility(View.GONE);
                 if (NearFragments.instance!=null)
              NearFragments.instance.setViewAll();
                break;

            default:
                break;
        }

    }


    @Override
    public void notification(int counter, int lftdays, int postlimit) {
        if (counter > 0) {
            counter11.setText(MessageFormat.format("{0}", counter));
            counter11.setVisibility(View.VISIBLE);
        } else {
            counter11.setText(MessageFormat.format("{0}", counter));
            counter11.setVisibility(View.GONE);
        }
    }

    public void isDrawerOpen() {
        if (drawer != null)
            if (drawer.isDrawerOpen(Gravity.LEFT)) {
                drawer.closeDrawer(Gravity.LEFT);
            } else {
                drawer.openDrawer(Gravity.LEFT);
            }
    }

    @Override
    public void onBackPressed() {
        if (drawer != null)
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawers();
                return;
            }

        super.onBackPressed();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && intent.getStringExtra("from") != null) {
            if (intent.getStringExtra("from").equalsIgnoreCase("near")) {
                setNearFragment();
            } else if (intent.getStringExtra("from").equalsIgnoreCase("chat")) {
                setChatFragment();
            } else setMatchFragment();

        } else
            setMatchFragment();
    }


    private void getProfileData() {
        SwipeeApiClient.swipeeServiceInstance().userdetail(Config.GetUserToken()).enqueue(new Callback<EmployeeUserDetails>() {
            @Override
            public void onResponse(@NonNull Call<EmployeeUserDetails> call, @NonNull Response<EmployeeUserDetails> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isStatus()) {
                        employeeuserdetail = response.body();
                        if (employeeuserdetail.getCode() == 200) {
                            if (employeeuserdetail.getData().getUser_profile_data() != null) {
                                Config.SetEmail(employeeuserdetail.getData().getUser_profile_data().getEmail());
                                Config.SetMobileNo(employeeuserdetail.getData().getUser_profile_data().getMobile_no());
                                Config.SetPhoneCode(employeeuserdetail.getData().getUser_profile_data().getPhone_code());
                                Config.SetCountry(employeeuserdetail.getData().getUser_profile_data().getCountry());
                                Config.SetCountryName(employeeuserdetail.getData().getUser_profile_data().getCountry());
                                Config.SetStateName(employeeuserdetail.getData().getUser_profile_data().getState());
                                Config.SetCityName(employeeuserdetail.getData().getUser_profile_data().getCity());
                                Config.SetState(employeeuserdetail.getData().getUser_profile_data().getState());
                                Config.SetCity(employeeuserdetail.getData().getUser_profile_data().getCity());
                                Config.SetDob(employeeuserdetail.getData().getUser_profile_data().getUser_dob());
                                Config.SetStateId(employeeuserdetail.getData().getUser_profile_data().getState_id());
                                Config.SetCityId(employeeuserdetail.getData().getUser_profile_data().getCity_id());
                                Config.SetGender(employeeuserdetail.getData().getUser_profile_data().getGender());
                                Config.SetEmailVERIFY(true);
                                Config.SetMobileVERIFY(true);
                                Config.SetIsUserLogin(true);
                                Config.SetId(employeeuserdetail.getData().getUser_profile_data().getUser_id());
                                if (employeeuserdetail.getData().getUser_profile_data().getMiddle_name().length() != 0) {
                                    Config.SetName(employeeuserdetail.getData().getUser_profile_data().getFirst_name() + " " +
                                            employeeuserdetail.getData().getUser_profile_data().getMiddle_name() + " " +
                                            employeeuserdetail.getData().getUser_profile_data().getLast_name());
                                } else {
                                    Config.SetName(employeeuserdetail.getData().getUser_profile_data().getFirst_name() + " " +
                                            employeeuserdetail.getData().getUser_profile_data().getLast_name());
                                }
                                Config.SetFName(employeeuserdetail.getData().getUser_profile_data().getFirst_name());
                                Config.SetLName(employeeuserdetail.getData().getUser_profile_data().getLast_name());
                                Config.SetLat(employeeuserdetail.getData().getUser_profile_data().getLatitude());
                                Config.SetPICKURI(employeeuserdetail.getData().getUser_profile_data().getUser_profile());
                                Config.SetLongg(employeeuserdetail.getData().getUser_profile_data().getLongitude());
                                Config.SetRadiuss(String.valueOf(employeeuserdetail.getData()
                                        .getUser_profile_data().getDefault_radius()));

                            }
                            if (employeeuserdetail.getData().getUser_resumes() != null) {
                                if (employeeuserdetail.getData().getUser_resumes().getCv_file() != null) {
                                    Config.SetCVID(employeeuserdetail.getData().getUser_resumes().getCv_id());
                                    Config.SetCVTITLE(employeeuserdetail.getData().getUser_resumes().getCv_title());
                                    Config.SetCVFILE(employeeuserdetail.getData().getUser_resumes().getCv_file());
                                    Config.SetCVFILELINK(employeeuserdetail.getData().getUser_resumes().getCv_file_link());
                                }

                            }
                        }
                    }

                }

            }

            @Override
            public void onFailure(@NonNull Call<EmployeeUserDetails> call, @NonNull Throwable t) {
            }
        });
    }
/*
    *//**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     *//*
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            if (PlansFragments.instance != null){
                PlansFragments.instance.setTransactionData(razorpayPaymentID);
            }
        } catch (Exception e) {
            Log.e("RazorPay", "Exception in onPaymentSuccess", e);
        }
    }

    *//**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     *//*
    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        Log.d("RazorPay", "Exception in onPaymentError " + response);
        try {
            Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("RazorPay", "Exception in onPaymentError", e);
        }
    }*/

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {

        try {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            if (PlansFragments.instance != null){
                PlansFragments.instance.setTransactionData(paymentData);
            }
        } catch (Exception e) {
            Log.e("RazorPay", "Exception in onPaymentSuccess", e);
        }

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

    }
}