package com.webnotics.swipee.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.Seeker.AppoimentActivity;
import com.webnotics.swipee.activity.Seeker.JobDetail;
import com.webnotics.swipee.activity.Seeker.SeekerHomeActivity;
import com.webnotics.swipee.activity.company.CompanyAppoimentActivity;
import com.webnotics.swipee.activity.company.CompanyHomeActivity;
import com.webnotics.swipee.activity.company.UserDetail;
import com.webnotics.swipee.call.AudioActivity;
import com.webnotics.swipee.call.VideoActivity;
import com.webnotics.swipee.chat.MainChatActivity;
import com.webnotics.swipee.model.TwillioDetailModel;
import com.webnotics.swipee.model.company.AppointmentDetailModel;
import com.webnotics.swipee.model.seeker.SeekerAppointmentDetailModel;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentDetail extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE_ASK_PERMISSIONS = 211;
    Context mContext;
    Rest rest;
    TextView tv_name,tv_phone,tv_email,tv_designation,tv_experience,tv_skill,tv_appointmentnumber,
            tv_appointmenttime,tv_mode,tv_start_appointment;
    ImageView iv_back,iv_profile;
    private String appointmentDate="";
    private String startAt="";
    private String endAt="";
    private String appointment_id="";
    private String user_id="";
    private String appointment_number="";
    private String appointment_type="";
    private int isLive=0;
    private String user_profile="";
    private String user_phone="";
    private String from="";
    private String name="";
    private String name1="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext=this;
        rest=new Rest(mContext);

        tv_name=findViewById(R.id.tv_name);
        tv_phone=findViewById(R.id.tv_phone);
        tv_email=findViewById(R.id.tv_email);
        tv_designation=findViewById(R.id.tv_designation);
        tv_experience=findViewById(R.id.tv_experience);
        tv_skill=findViewById(R.id.tv_skill);
        tv_appointmentnumber=findViewById(R.id.tv_appointmentnumber);
        tv_appointmenttime=findViewById(R.id.tv_appointmenttime);
        tv_mode=findViewById(R.id.tv_mode);
        tv_start_appointment=findViewById(R.id.tv_start_appointment);
        iv_back=findViewById(R.id.iv_back);
        iv_profile=findViewById(R.id.iv_profile);
        iv_back.setOnClickListener(this);


        if (getIntent()!=null){
             appointment_id=getIntent().getStringExtra(ParaName.KEY_APPOINTMENTID)!=null?getIntent().getStringExtra(ParaName.KEY_APPOINTMENTID):"";
             from=getIntent().getStringExtra("from")!=null?getIntent().getStringExtra("from"):"";
            if (!TextUtils.isEmpty(appointment_id)){
                if (rest.isInterentAvaliable()){
                    AppController.ShowDialogue("",mContext);
                    if (Config.isSeeker())
                        callSeekerAppointmentDetail(appointment_id);
                    else
                    callAppointmentDetail(appointment_id);
                }else rest.AlertForInternet();
            }
        }
    }

    private void callSeekerAppointmentDetail(String appointmentId) {
        SwipeeApiClient.swipeeServiceInstance().getSeekerAppointmentDetail(Config.GetUserToken(),appointmentId).enqueue(new Callback<SeekerAppointmentDetailModel>() {
            @Override
            public void onResponse(@NonNull Call<SeekerAppointmentDetailModel> call, @NonNull Response<SeekerAppointmentDetailModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    SeekerAppointmentDetailModel responseBody = response.body();
                    if (responseBody.getCode()==203){
                        rest.showToast(responseBody.getMessage());
                       setLogoutSeeker();
                    }else if (responseBody.getCode()==200 &&responseBody.isStatus()){
                        name=responseBody.getData().getCompany_name();
                        name1=responseBody.getData().getCompany_name();
                        tv_name.setText(MessageFormat.format("{0}",responseBody.getData().getCompany_name()));
                        tv_email.setText(MessageFormat.format("Email: {0}", responseBody.getData().getCompany_email()));
                        tv_phone.setText(MessageFormat.format("Phone: {0}{1}", responseBody.getData().getPhone_code(), responseBody.getData().getMobile()));
                        tv_designation.setText(MessageFormat.format("Industry: {0}", responseBody.getData().getIndustry_name()));
                        tv_experience.setText(MessageFormat.format("Company Size: {0}", responseBody.getData().getCompany_size()));
                        tv_skill.setText(MessageFormat.format("Address: {0}, {1}, {2}", responseBody.getData().getCompany_address(), responseBody.getData().getCity(), responseBody.getData().getState()));
                        tv_appointmentnumber.setText(MessageFormat.format("{0}", responseBody.getData().getAppointment_number()));
                        appointmentDate=responseBody.getData().getAppointment_date();
                        startAt=responseBody.getData().getAppointment_start_at();
                        endAt=responseBody.getData().getAppointment_end_at();
                        appointment_id=responseBody.getData().getAppointment_id();
                        appointment_type=responseBody.getData().getAppointment_type();
                        appointment_number=responseBody.getData().getAppointment_number();
                        user_id=responseBody.getData().getCompany_id();
                        user_profile=responseBody.getData().getCompany_logo();
                        user_phone=responseBody.getData().getPhone_code()+" "+ responseBody.getData().getMobile();
                        isLive=responseBody.getData().getIs_live();
                        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                        SimpleDateFormat formatout = new SimpleDateFormat("hh:mm aa");
                        Date date, dateend;
                        try {
                            date = format.parse(responseBody.getData().getAppointment_start_at());
                            dateend = format.parse(responseBody.getData().getAppointment_end_at());
                            String date1 = formatout.format(date);
                            String dateend1 = formatout.format(dateend);
                            tv_appointmenttime.setText(MessageFormat.format("{0} {1}", responseBody.getData().getAppointment_date(), (date1 + " - " + dateend1).toUpperCase(Locale.ROOT)));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (appointment_type.equalsIgnoreCase("chat")) {
                            tv_mode.setText("  Chat");
                            tv_start_appointment.setText("Start Chat");
                            tv_mode.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.chat_img),null, null,  null);
                        } else if (appointment_type.equalsIgnoreCase("online_meeting")) {
                            tv_mode.setText("  Video Call");
                            tv_start_appointment.setText("Start Video Call");
                            tv_mode.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.ic_video_player),null, null,  null);
                        } else if (appointment_type.equalsIgnoreCase("call")) {
                            tv_mode.setText("  Audio Call");
                            tv_start_appointment.setText("Start Audio Call");
                            tv_mode.setCompoundDrawablesWithIntrinsicBounds( mContext.getDrawable(R.drawable.ic_telephone_meeting),null, null, null);
                        }
                        Glide.with(mContext).load(user_profile)
                                .apply(new RequestOptions().placeholder(R.drawable.white_light_semiround_bg)
                                        .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * 24))))
                                        .error(R.drawable.white_light_semiround_bg)).into(iv_profile);

                        if (responseBody.getData().getIs_live()==1) {
                            tv_start_appointment.setBackgroundResource(R.drawable.primary_semiround_bg);
                            tv_start_appointment.setOnClickListener(AppointmentDetail.this);
                        } else{
                            tv_start_appointment.setBackgroundResource(R.drawable.gray_semiround_bg);
                            tv_start_appointment.setOnClickListener(null);
                        }
                    }
                } else {
                    rest.showToast("Something went wrong");
                }

            }

            @Override
            public void onFailure(@NonNull Call<SeekerAppointmentDetailModel> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });

    }

    private void callAppointmentDetail(String appointmentId) {
        SwipeeApiClient.swipeeServiceInstance().getAppointmentDetail(Config.GetUserToken(),appointmentId).enqueue(new Callback<AppointmentDetailModel>() {
            @Override
            public void onResponse(@NonNull Call<AppointmentDetailModel> call, @NonNull Response<AppointmentDetailModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    AppointmentDetailModel responseBody = response.body();
                    if (responseBody.getCode()==203){
                        rest.showToast(responseBody.getMessage());
                        setLogoutRecruiter();
                    }else if (responseBody.getCode()==200 &&responseBody.isStatus()){
                        name=responseBody.getData().getFirst_name();
                        name1=responseBody.getData().getFirst_name()+" "+responseBody.getData().getLast_name();
                        tv_name.setText(MessageFormat.format("{0} {1}", responseBody.getData().getFirst_name(), responseBody.getData().getLast_name()));
                        tv_email.setText(MessageFormat.format("Email: {0}", responseBody.getData().getEmail()));
                        tv_phone.setText(MessageFormat.format("Phone: {0}{1}", responseBody.getData().getPhone_code(), responseBody.getData().getMobile_no()));
                        tv_designation.setText(MessageFormat.format("Designation: {0}", responseBody.getData().getDesignation()));
                        tv_experience.setText(MessageFormat.format("Exp: {0}", responseBody.getData().getWork_experience()));
                        tv_skill.setText(MessageFormat.format("Skills: {0}", responseBody.getData().getSkills()));
                        tv_appointmentnumber.setText(MessageFormat.format("{0}", responseBody.getData().getAppointment_number()));
                         appointmentDate=responseBody.getData().getAppointment_date();
                         startAt=responseBody.getData().getAppointment_start_at();
                         endAt=responseBody.getData().getAppointment_end_at();
                        appointment_id=responseBody.getData().getAppointment_id();
                        appointment_type=responseBody.getData().getAppointment_type();
                        appointment_number=responseBody.getData().getAppointment_number();
                        user_id=responseBody.getData().getUser_id();
                        user_profile=responseBody.getData().getUser_profile();
                        user_phone=responseBody.getData().getPhone_code()+" "+ responseBody.getData().getMobile_no();
                        isLive=responseBody.getData().getIs_live();
                        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
                        SimpleDateFormat formatout = new SimpleDateFormat("hh:mm aa");
                        Date date, dateend;
                        try {
                            date = format.parse(responseBody.getData().getAppointment_start_at());
                            dateend = format.parse(responseBody.getData().getAppointment_end_at());
                            String date1 = formatout.format(date);
                            String dateend1 = formatout.format(dateend);
                            tv_appointmenttime.setText(MessageFormat.format("{0} {1}", responseBody.getData().getAppointment_date(), (date1 + " - " + dateend1).toUpperCase(Locale.ROOT)));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        if (appointment_type.equalsIgnoreCase("chat")) {
                            tv_mode.setText("  Chat");
                            tv_start_appointment.setText("Start Chat");
                            tv_mode.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.chat_img),null, null,  null);
                        } else if (appointment_type.equalsIgnoreCase("online_meeting")) {
                            tv_mode.setText("  Video Call");
                            tv_start_appointment.setText("Start Video Call");
                            tv_mode.setCompoundDrawablesWithIntrinsicBounds(mContext.getDrawable(R.drawable.ic_video_player),null, null,  null);
                        } else if (appointment_type.equalsIgnoreCase("call")) {
                            tv_mode.setText("  Audio Call");
                            tv_start_appointment.setText("Start Audio Call");
                            tv_mode.setCompoundDrawablesWithIntrinsicBounds( mContext.getDrawable(R.drawable.ic_telephone_meeting),null, null, null);
                        }
                        Glide.with(mContext).load(user_profile)
                                .apply(new RequestOptions().placeholder(R.drawable.white_light_semiround_bg)
                                        .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * 24))))
                                        .error(R.drawable.white_light_semiround_bg)).into(iv_profile);

                             if (responseBody.getData().getIs_live()==1) {
                                 tv_start_appointment.setBackgroundResource(R.drawable.primary_semiround_bg);
                                 tv_start_appointment.setOnClickListener(AppointmentDetail.this);
                             } else{
                                 tv_start_appointment.setBackgroundResource(R.drawable.gray_semiround_bg);
                                 tv_start_appointment.setOnClickListener(AppointmentDetail.this);
                             }
                    }

                } else {
                    rest.showToast("Something went wrong");
                }

            }

            @Override
            public void onFailure(@NonNull Call<AppointmentDetailModel> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_start_appointment:
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) !=
                        PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{
                                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO},
                            REQUEST_CODE_ASK_PERMISSIONS);
                } else {
                    if (isLive==0) {
                        if (!TextUtils.isEmpty(appointment_id)&&!TextUtils.isEmpty(appointment_number)){
                            if (appointment_type.equalsIgnoreCase("chat")) {
                               startActivity(new Intent(mContext, MainChatActivity.class)
                                        .putExtra("image",user_profile)
                                        .putExtra("msg_id","0")
                                        .putExtra("action","")
                                        .putExtra("appointment_id",appointment_id)
                                        .putExtra("appointment_number",appointment_number)
                                        .putExtra("user_id",user_id)
                                        .putExtra("sender_id",Config.GetId())
                                        .putExtra("receiver_id",user_id)
                                        .putExtra("name",name));
                                            finish();
                            } else if (appointment_type.equalsIgnoreCase("online_meeting")) {
                                AppController.ShowDialogue("",mContext);
                                if (Config.isSeeker())
                                callSeekerVideoDetail();
                               else callVideoDetail();
                            } else if (appointment_type.equalsIgnoreCase("call")) {
                                AppController.ShowDialogue("",mContext);
                                if (Config.isSeeker())
                                    callSeekerAudioDetail();
                                else
                                    callAudioDetail();
                            }
                        }
                    }
                }

                break;

            case R.id.iv_back:
                onBackPressed();
                break;

            default:break;
        }

    }

    private void callSeekerAudioDetail() {
        SwipeeApiClient.swipeeServiceInstance().getSeekerStartAudio(Config.GetUserToken(),appointment_id,user_id,appointment_number).enqueue(new Callback<TwillioDetailModel>() {
            @Override
            public void onResponse(@NonNull Call<TwillioDetailModel> call, @NonNull Response<TwillioDetailModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    TwillioDetailModel responseBody = response.body();
                    if (responseBody.getCode()==203){
                        rest.showToast(responseBody.getMessage());
                        setLogoutSeeker();
                    }else
                    if (responseBody.getCode()==200 &&responseBody.isStatus()){
                        String appointment_id=responseBody.getData().getAppointment_id();
                        String room_name=responseBody.getData().getRoom_name();
                        String room_sid=responseBody.getData().getRoom_sid();
                        String company_id=responseBody.getData().getCompany_id();
                        String company_access_token=responseBody.getData().getCompany_access_token();
                        String user_id=responseBody.getData().getUser_id();
                        String user_access_token=responseBody.getData().getUser_access_token();
                        String created_role_id=responseBody.getData().getCreated_role_id();
                        Intent intent = new Intent(mContext, AudioActivity.class);
                        intent.putExtra("accestoken", company_access_token);
                        intent.putExtra("rroom",room_name);
                        intent.putExtra("imgurl", user_profile);
                        intent.putExtra("user_name",tv_name.getText().toString());
                        intent.putExtra("phnno", user_phone);
                        intent.putExtra("appointment_id",appointment_id);
                        startActivity(intent);
                         if (from.equalsIgnoreCase(NotificationActivity.class.getSimpleName())){
                            if (NotificationActivity.instance!=null)
                                NotificationActivity.instance.finish();
                        }else if ( from.equalsIgnoreCase(JobDetail.class.getSimpleName())){
                             if (JobDetail.instance!=null)
                                 JobDetail.instance.setBackPressed();
                         }else
                        if (!from.equalsIgnoreCase("Notification")){
                            if (AppoimentActivity.instance!=null)
                                AppoimentActivity.instance.onBackPressed();
                        }
                        finish();
                    }

                } else {
                    rest.showToast("Something went wrong");
                }

            }

            @Override
            public void onFailure(@NonNull Call<TwillioDetailModel> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });

    }

    private void callSeekerVideoDetail() {
        SwipeeApiClient.swipeeServiceInstance().getSeekerStartVideo(Config.GetUserToken(),appointment_id,user_id,appointment_number).enqueue(new Callback<TwillioDetailModel>() {
            @Override
            public void onResponse(@NonNull Call<TwillioDetailModel> call, @NonNull Response<TwillioDetailModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    TwillioDetailModel responseBody = response.body();
                    if (responseBody.getCode()==203){
                        rest.showToast(responseBody.getMessage());
                        setLogoutSeeker();

                    }else
                    if (responseBody.getCode()==200 &&responseBody.isStatus()){
                        String appointment_id=responseBody.getData().getAppointment_id();
                        String room_name=responseBody.getData().getRoom_name();
                        String room_sid=responseBody.getData().getRoom_sid();
                        String company_id=responseBody.getData().getCompany_id();
                        String company_access_token=responseBody.getData().getCompany_access_token();
                        String user_id=responseBody.getData().getUser_id();
                        String user_access_token=responseBody.getData().getUser_access_token();
                        String created_role_id=responseBody.getData().getCreated_role_id();
                        Intent intent = new Intent(mContext, VideoActivity.class);
                        intent.putExtra("accestoken", company_access_token);
                        intent.putExtra("rroom", room_name);
                        intent.putExtra("appointment_id", appointment_id);
                        intent.putExtra("name", name1);
                        mContext.startActivity(intent);
                        if (from.equalsIgnoreCase(NotificationActivity.class.getSimpleName())){
                            if (NotificationActivity.instance!=null)
                                NotificationActivity.instance.finish();
                        }else if ( from.equalsIgnoreCase(JobDetail.class.getSimpleName())){
                            if (JobDetail.instance!=null)
                                JobDetail.instance.setBackPressed();
                        }else
                        if (!from.equalsIgnoreCase("Notification")){
                            if (AppoimentActivity.instance!=null)
                                AppoimentActivity.instance.onBackPressed();
                        }
                        finish();
                    }

                } else {
                    rest.showToast("Something went wrong");
                }

            }

            @Override
            public void onFailure(@NonNull Call<TwillioDetailModel> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });
    }

    private void setLogoutSeeker() {
        if (from.equalsIgnoreCase(NotificationActivity.class.getSimpleName())){
            if (NotificationActivity.instance!=null)
                NotificationActivity.instance.finish();
            if (Config.isSeeker()){
                startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "match"));
            }else {
                startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", "match"));
            }
        }else if ( from.equalsIgnoreCase(JobDetail.class.getSimpleName())){
            if (JobDetail.instance!=null)
                JobDetail.instance.setBackPressed();
        }else
        if (!from.equalsIgnoreCase("Notification")){
            if (AppoimentActivity.instance!=null)
                AppoimentActivity.instance.onBackPressed();
        }
        AppController.loggedOut(mContext);
    }


    private void callVideoDetail() {
        SwipeeApiClient.swipeeServiceInstance().getStartVideo(Config.GetUserToken(),appointment_id,user_id,appointment_number).enqueue(new Callback<TwillioDetailModel>() {
            @Override
            public void onResponse(@NonNull Call<TwillioDetailModel> call, @NonNull Response<TwillioDetailModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    TwillioDetailModel responseBody = response.body();
                    if (responseBody.getCode()==203){
                        rest.showToast(responseBody.getMessage());
                        setLogoutRecruiter();

                    }else
                    if (responseBody.getCode()==200 &&responseBody.isStatus()){
                        String appointment_id=responseBody.getData().getAppointment_id();
                        String room_name=responseBody.getData().getRoom_name();
                        String room_sid=responseBody.getData().getRoom_sid();
                        String company_id=responseBody.getData().getCompany_id();
                        String company_access_token=responseBody.getData().getCompany_access_token();
                        String user_id=responseBody.getData().getUser_id();
                        String user_access_token=responseBody.getData().getUser_access_token();
                        String created_role_id=responseBody.getData().getCreated_role_id();
                        Intent intent = new Intent(mContext, VideoActivity.class);
                        intent.putExtra("accestoken", company_access_token);
                        intent.putExtra("rroom", room_name);
                        intent.putExtra("appointment_id", appointment_id);
                        intent.putExtra("name", name1);
                        mContext.startActivity(intent);
                        if (from.equalsIgnoreCase(NotificationActivity.class.getSimpleName())){
                            if (NotificationActivity.instance!=null)
                                NotificationActivity.instance.finish();
                        }else  if (from.equalsIgnoreCase(UserDetail.class.getSimpleName())){
                            if (UserDetail.instance!=null)
                                UserDetail.instance.finish();
                        }else
                        if (!from.equalsIgnoreCase("Notification")){
                            if (CompanyAppoimentActivity.instance!=null)
                                CompanyAppoimentActivity.instance.onBackPressed();
                        }
                        finish();
                    }

                } else {
                    rest.showToast("Something went wrong");
                }

            }

            @Override
            public void onFailure(@NonNull Call<TwillioDetailModel> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });
    }

    private void setLogoutRecruiter() {
        if (from.equalsIgnoreCase(NotificationActivity.class.getSimpleName())){
            if (NotificationActivity.instance!=null)
                NotificationActivity.instance.finish();
            if (Config.isSeeker()){
                startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "match"));
            }else {
                startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", "match"));
            }
        }else if ( from.equalsIgnoreCase(UserDetail.class.getSimpleName())){
            if (UserDetail.instance!=null)
                UserDetail.instance.setBackPressed();
        }else
        if (!from.equalsIgnoreCase("Notification")){
            if (CompanyAppoimentActivity.instance!=null)
                CompanyAppoimentActivity.instance.onBackPressed();
        }
        AppController.loggedOut(mContext);
    }

    private void callAudioDetail() {
        SwipeeApiClient.swipeeServiceInstance().getStartAudio(Config.GetUserToken(),appointment_id,user_id,appointment_number).enqueue(new Callback<TwillioDetailModel>() {
            @Override
            public void onResponse(@NonNull Call<TwillioDetailModel> call, @NonNull Response<TwillioDetailModel> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    TwillioDetailModel responseBody = response.body();
                    if (responseBody.getCode()==203){
                        rest.showToast(responseBody.getMessage());
                        setLogoutRecruiter();

                    }else
                    if (responseBody.getCode()==200 &&responseBody.isStatus()){
                        String appointment_id=responseBody.getData().getAppointment_id();
                        String room_name=responseBody.getData().getRoom_name();
                        String room_sid=responseBody.getData().getRoom_sid();
                        String company_id=responseBody.getData().getCompany_id();
                        String company_access_token=responseBody.getData().getCompany_access_token();
                        String user_id=responseBody.getData().getUser_id();
                        String user_access_token=responseBody.getData().getUser_access_token();
                        String created_role_id=responseBody.getData().getCreated_role_id();
                        Intent intent = new Intent(mContext, AudioActivity.class);
                        intent.putExtra("accestoken", company_access_token);
                        intent.putExtra("rroom",room_name);
                        intent.putExtra("imgurl", user_profile);
                        intent.putExtra("user_name",tv_name.getText().toString());
                        intent.putExtra("phnno", user_phone);
                        intent.putExtra("appointment_id",appointment_id);
                        startActivity(intent);
                         if (from.equalsIgnoreCase(NotificationActivity.class.getSimpleName())){
                            if (NotificationActivity.instance!=null)
                                NotificationActivity.instance.finish();
                        }else  if (from.equalsIgnoreCase(UserDetail.class.getSimpleName())){
                            if (UserDetail.instance!=null)
                                UserDetail.instance.finish();
                        }else
                        if (!from.equalsIgnoreCase("Notification")){
                            if (CompanyAppoimentActivity.instance!=null)
                                CompanyAppoimentActivity.instance.onBackPressed();
                        }
                        finish();
                    }

                } else {
                    rest.showToast("Something went wrong");
                }

            }

            @Override
            public void onFailure(@NonNull Call<TwillioDetailModel> call, @NonNull Throwable t) {

                AppController.dismissProgressdialog();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (from.equalsIgnoreCase("Notification")){
            if (Config.isSeeker()){
                startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "match"));
            }else {
                startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", "match"));
            }
        }
        super.onBackPressed();
    }
}