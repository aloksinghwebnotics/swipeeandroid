package com.webnotics.swipee.activity.company;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.NotificationActivity;
import com.webnotics.swipee.activity.Seeker.JobDetail;
import com.webnotics.swipee.activity.SettingsActivity;
import com.webnotics.swipee.fragments.company.CompanyChatFragments;
import com.webnotics.swipee.fragments.company.CompanyMatchFragments;
import com.webnotics.swipee.fragments.company.CompanyPlansFragments;
import com.webnotics.swipee.fragments.company.CompanyProfileFragments;
import com.webnotics.swipee.fragments.company.PostJobFragments;
import com.webnotics.swipee.interfaces.CountersInterface;
import com.webnotics.swipee.model.company.CompanyProfileModel;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.text.MessageFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CompanyHomeActivity extends AppCompatActivity implements View.OnClickListener, CountersInterface, PaymentResultWithDataListener {
    Context mContext;
    @SuppressLint("StaticFieldLeak")
    public static CompanyHomeActivity instance;
    public boolean isFilterShowing = false;
    public CountersInterface countersInterface;
    LinearLayout nearlay, matchlay, planlay, chatlay, accountlay;
    LinearLayout bottomlay, ll_nav_home, ll_nav_liked, ll_nav_matched, ll_nav_near, ll_nav_posted, ll_nav_appoiment, ll_nav_setting;
    ImageView nearimg, matchimg, planimg, imgchat, imgaccount, menu, search_icon, notification;
    public ImageView filter_icon;
    private TextView neartxt, matchtxt, planstxt, chattxt, accounttxt, counter11, tv_name, tv_email;
    private DrawerLayout drawer;
    RelativeLayout headerlay, notilay;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    CircleImageView civ_profile;
    public CompanyProfileModel employeeuserdetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_home);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        instance = this;
        initiate();
        countersInterface = this;
        civ_profile.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(Config.GetPICKURI()))
                AppController.callFullImage(mContext, Config.GetPICKURI());
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.menu:
                tv_name.setText(Config.GeCompanyName());
                tv_email.setText(Config.GetEmail());
                Glide.with(mContext).load(Config.GetPICKURI()).error(R.drawable.ic_profile_select).placeholder(R.drawable.ic_profile_select).into(civ_profile);
                isDrawerOpen();
                break;

            case R.id.notilay:
                startActivity(new Intent(mContext, NotificationActivity.class));
                break;

            case R.id.filter_icon:
                if (isFilterShowing) {
                    if (CompanyMatchFragments.instance != null) {
                        CompanyMatchFragments.instance.callFilter(false);
                    }
                } else {
                    if (CompanyMatchFragments.instance != null) {
                        CompanyMatchFragments.instance.callFilter(true);
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
                startActivity(new Intent(this, LikedUserActivity.class));
                break;

            case R.id.ll_nav_matched:
                isDrawerOpen();
                startActivity(new Intent(this, MatchedUserActivity.class));
                break;

            case R.id.ll_nav_near:
                isDrawerOpen();
                startActivity(new Intent(this, CompanyNearBy.class));
                break;

            case R.id.ll_nav_posted:
                isDrawerOpen();
                startActivity(new Intent(this, PostedJobActivity.class));
                break;

            case R.id.ll_nav_appoiment:
                isDrawerOpen();
                startActivity(new Intent(this, CompanyAppoimentActivity.class));
                break;

            case R.id.ll_nav_setting:
                isDrawerOpen();
                startActivity(new Intent(this, SettingsActivity.class));
                break;

            default:
                break;
        }

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
        ll_nav_near = findViewById(R.id.ll_nav_near);
        ll_nav_posted = findViewById(R.id.ll_nav_posted);
        ll_nav_appoiment = findViewById(R.id.ll_nav_appoiment);
        ll_nav_setting = findViewById(R.id.ll_nav_setting);

        civ_profile = findViewById(R.id.civ_profile);
        tv_name = findViewById(R.id.tv_name);
        tv_email = findViewById(R.id.tv_email);
        tv_name.setText(Config.GeCompanyName());
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
        ll_nav_near.setOnClickListener(this);
        ll_nav_posted.setOnClickListener(this);
        ll_nav_appoiment.setOnClickListener(this);
        ll_nav_setting.setOnClickListener(this);
        notilay.setOnClickListener(this);

        setMatchFragment();
        getProfileData();

       /* try {
            SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
            SimpleDateFormat formatout = new SimpleDateFormat("dd MM yyyy");
            if (TextUtils.isEmpty(Config.GetLocationRefreshDate())){
                startService(new Intent(this,  MyIntentService.class));
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
                            startService(new Intent(this,  MyIntentService.class));
                        }
                    }else {
                        Log.d("hhhhh","Hit from null");
                        startService(new Intent(this,  MyIntentService.class));
                    }
                }else   startService(new Intent(this,  MyIntentService.class));

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    public void setMatchFragment() {

        try {
            if (fragmentTransaction != null) {
                CompanyMatchFragments basefragment = new CompanyMatchFragments();
                fragmentManager.beginTransaction().replace(R.id.homegragment, basefragment).commit();
            }
        } catch (Exception ignored) {
        }
        matchimg.setImageResource(R.drawable.ic_match_selected);
        nearimg.setImageResource(R.drawable.ic_post_job_unselected);
        planimg.setImageResource(R.drawable.ic_plan_unselect);
        imgchat.setImageResource(R.drawable.ic_chat_unselect);
        imgaccount.setImageResource(R.drawable.ic_profile_dashboard);

        matchtxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        neartxt.setTextColor(getResources().getColor(R.color.gray));
        planstxt.setTextColor(getResources().getColor(R.color.gray));
        chattxt.setTextColor(getResources().getColor(R.color.gray));
        accounttxt.setTextColor(getResources().getColor(R.color.gray));
        headerlay.setVisibility(View.VISIBLE);
    }

    private void setNearFragment() {
        try {
            if (fragmentTransaction != null) {
                PostJobFragments basefragment = new PostJobFragments();
                fragmentManager.beginTransaction().replace(R.id.homegragment, basefragment).commit();
            }
        } catch (Exception ignored) {
        }
/*
        startActivity(new Intent(mContext,JobPostRule.class).putExtra("job_post_id",334));
*/
        matchimg.setImageResource(R.drawable.ic_match_unselected);
        nearimg.setImageResource(R.drawable.ic_post_job_selected);
        planimg.setImageResource(R.drawable.ic_plan_unselect);
        imgchat.setImageResource(R.drawable.ic_chat_unselect);
        imgaccount.setImageResource(R.drawable.ic_profile_dashboard);

        matchtxt.setTextColor(getResources().getColor(R.color.gray));
        neartxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        planstxt.setTextColor(getResources().getColor(R.color.gray));
        chattxt.setTextColor(getResources().getColor(R.color.gray));
        accounttxt.setTextColor(getResources().getColor(R.color.gray));
        headerlay.setVisibility(View.GONE);
    }

    public void setPlansFragment() {
        try {
            if (fragmentTransaction != null) {
                CompanyPlansFragments basefragment = new CompanyPlansFragments();
                fragmentManager.beginTransaction().replace(R.id.homegragment, basefragment).commit();
            }
        } catch (Exception ignored) {
        }
        matchimg.setImageResource(R.drawable.ic_match_unselected);
        nearimg.setImageResource(R.drawable.ic_post_job_unselected);
        planimg.setImageResource(R.drawable.ic_plan_select);
        imgchat.setImageResource(R.drawable.ic_chat_unselect);
        imgaccount.setImageResource(R.drawable.ic_profile_dashboard);

        matchtxt.setTextColor(getResources().getColor(R.color.gray));
        neartxt.setTextColor(getResources().getColor(R.color.gray));
        planstxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        chattxt.setTextColor(getResources().getColor(R.color.gray));
        accounttxt.setTextColor(getResources().getColor(R.color.gray));
        headerlay.setVisibility(View.GONE);
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
        nearimg.setImageResource(R.drawable.ic_post_job_unselected);
        planimg.setImageResource(R.drawable.ic_plan_unselect);
        imgchat.setImageResource(R.drawable.ic_chat_select);
        imgaccount.setImageResource(R.drawable.ic_profile_dashboard);

        matchtxt.setTextColor(getResources().getColor(R.color.gray));
        neartxt.setTextColor(getResources().getColor(R.color.gray));
        planstxt.setTextColor(getResources().getColor(R.color.gray));
        chattxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        accounttxt.setTextColor(getResources().getColor(R.color.gray));
        headerlay.setVisibility(View.GONE);
    }

    private void setProfileFragment() {
        try {
            if (fragmentTransaction != null) {
                CompanyProfileFragments basefragment = new CompanyProfileFragments();
                fragmentManager.beginTransaction().replace(R.id.homegragment, basefragment).commit();
            }
        } catch (Exception ignored) {
        }
        matchimg.setImageResource(R.drawable.ic_match_unselected);
        nearimg.setImageResource(R.drawable.ic_post_job_unselected);
        planimg.setImageResource(R.drawable.ic_plan_unselect);
        imgchat.setImageResource(R.drawable.ic_chat_unselect);
        imgaccount.setImageResource(R.drawable.ic_profile_select);

        matchtxt.setTextColor(getResources().getColor(R.color.gray));
        neartxt.setTextColor(getResources().getColor(R.color.gray));
        planstxt.setTextColor(getResources().getColor(R.color.gray));
        chattxt.setTextColor(getResources().getColor(R.color.gray));
        accounttxt.setTextColor(getResources().getColor(R.color.colorPrimary));
        headerlay.setVisibility(View.GONE);
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
            if (intent.getStringExtra("from").equalsIgnoreCase(CreateAppointment.class.getSimpleName())) {
                setMatchFragment();
            } else if (intent.getStringExtra("from").equalsIgnoreCase(CompanyAppoimentActivity.class.getSimpleName())) {
                setMatchFragment();
            } else if (intent.getStringExtra("from").equalsIgnoreCase(UserDetail.class.getSimpleName())) {
                setMatchFragment();
            } else if (intent.getStringExtra("from").equalsIgnoreCase(JobDetail.class.getSimpleName())) {
                setMatchFragment();
            } else if (intent.getStringExtra("from").equalsIgnoreCase("chat")) {
                setChatFragment();
            } else setMatchFragment();

        } else setMatchFragment();

    }


    private void getProfileData() {
        SwipeeApiClient.swipeeServiceInstance().companyDetail(Config.GetUserToken()).enqueue(new Callback<CompanyProfileModel>() {
            @Override
            public void onResponse(@NonNull Call<CompanyProfileModel> call, @NonNull Response<CompanyProfileModel> response) {
                if (response.code() == 200 && response.body() != null) {
                    if (response.body().isStatus()) {
                        employeeuserdetail = response.body();
                        if (employeeuserdetail.getCode() == 200 && employeeuserdetail.getData() != null) {
                            Config.SetEmail(employeeuserdetail.getData().getCompany_email());
                            Config.SetMobileNo(employeeuserdetail.getData().getMobile());
                            Config.SetPhoneCode(employeeuserdetail.getData().getPhone_code());
                            Config.SetCountry(employeeuserdetail.getData().getCountry());
                            Config.SetCountryName(employeeuserdetail.getData().getCountry());
                            Config.SetStateName(employeeuserdetail.getData().getState());
                            Config.SetCityName(employeeuserdetail.getData().getCity());
                            Config.SetState(employeeuserdetail.getData().getState());
                            Config.SetCity(employeeuserdetail.getData().getCity());
                            Config.SetStateId(employeeuserdetail.getData().getState_id());
                            Config.SetCityId(employeeuserdetail.getData().getCity_id());
                            Config.SetCountryId(employeeuserdetail.getData().getCountry_id());
                            Config.SetEmailVERIFY(true);
                            Config.SetMobileVERIFY(true);
                            Config.SetIsUserLogin(true);
                            Config.SetId(employeeuserdetail.getData().getCompany_id());
                            Config.SetIndustryId(employeeuserdetail.getData().getIndustry_id());
                            Config.SetCompanyName(employeeuserdetail.getData().getCompany_name());
                            Config.SetType("Company");
                            Config.SetLat(employeeuserdetail.getData().getLatitude());
                            Config.SetPICKURI(employeeuserdetail.getData().getCompany_logo());
                            Config.SetLongg(employeeuserdetail.getData().getLongitude());
                            Config.SetIndustry(employeeuserdetail.getData().getIndustry_name());
                            Config.SetRadiuss(String.valueOf(employeeuserdetail.getData().getDefault_radius()));

                        }
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<CompanyProfileModel> call, @NonNull Throwable t) {

            }
        });

    }

   /* *//**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     *//*
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            if (CompanyPlansFragments.instance != null){
                CompanyPlansFragments.instance.setTransactionData(razorpayPaymentID);
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
        Log.d("sssssssss", paymentData.getData().toString());
        Log.d("sssssssss", paymentData.getPaymentId());
        Log.d("sssssssss", paymentData.getOrderId());
        Log.d("sssssssss", paymentData.getSignature());
        try {
            if (CompanyPlansFragments.instance != null){
                CompanyPlansFragments.instance.setTransactionData(paymentData);
            }
        } catch (Exception e) {
            Log.e("RazorPay", "Exception in onPaymentSuccess", e);
        }

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {

    }
}