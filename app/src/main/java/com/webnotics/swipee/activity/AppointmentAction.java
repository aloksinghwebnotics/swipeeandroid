package com.webnotics.swipee.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.Seeker.JobDetail;
import com.webnotics.swipee.activity.company.UserDetail;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentAction extends AppCompatActivity {

    ImageView iv_back, iv_profile;
    TextView tv_username, tv_name, tv_location, tv_jobtitle;
    RecyclerView rv_appointment;
    Context mContext;
    private static String action = "";
    private String company_id = "";
    private String notify_number = "";
    private String company_logo = "";
    private String company_name = "";
    private String city = "";
    private String state = "";
    private String posted_by = "";
    private String is_own_job = "";
    private String job_id = "";
    private String apply_id = "";
    private String from = "";
    private String company_country_name = "";
    private Rest rest;
    @SuppressLint("StaticFieldLeak")
    public static AppointmentAction instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_action);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.colorPrimary));
        mContext = this;
        rest = new Rest(mContext);
        instance = this;

        iv_back = findViewById(R.id.iv_back);
        iv_profile = findViewById(R.id.iv_profile);
        tv_username = findViewById(R.id.tv_username);
        tv_name = findViewById(R.id.tv_name);
        tv_location = findViewById(R.id.tv_location);
        rv_appointment = findViewById(R.id.rv_appointment);
        tv_jobtitle = findViewById(R.id.tv_jobtitle);

        iv_back.setOnClickListener(v -> finish());


        if (getIntent() != null) {

            company_id = getIntent().getStringExtra("company_id") != null ? getIntent().getStringExtra("company_id") : "";
            company_logo = getIntent().getStringExtra("company_logo") != null ? getIntent().getStringExtra("company_logo") : "";
            company_name = getIntent().getStringExtra("company_name") != null ? getIntent().getStringExtra("company_name") : "";
            city = getIntent().getStringExtra("company_city_name") != null ? getIntent().getStringExtra("company_city_name") : "";
            state = getIntent().getStringExtra("company_state_name") != null ? getIntent().getStringExtra("company_state_name") : "";
            posted_by = getIntent().getStringExtra("posted_by") != null ? getIntent().getStringExtra("posted_by") : "";
            is_own_job = getIntent().getStringExtra("is_own_job") != null ? getIntent().getStringExtra("is_own_job") : "";
            job_id = getIntent().getStringExtra("job_id") != null ? getIntent().getStringExtra("job_id") : "";
            apply_id = getIntent().getStringExtra("apply_id") != null ? getIntent().getStringExtra("apply_id") : "";
            from = getIntent().getStringExtra("from") != null ? getIntent().getStringExtra("from") : "";
            notify_number = getIntent().getStringExtra("notify_number") != null ? getIntent().getStringExtra("notify_number") : "";
            action = getIntent().getStringExtra("action") != null ? getIntent().getStringExtra("action") : "";
            company_country_name = getIntent().getStringExtra("company_country_name") != null ? getIntent().getStringExtra("company_country_name") : "";


            if (action.equalsIgnoreCase("cancel")) {
                tv_username.setText("Cancel Appointment");
            } else tv_username.setText("Reschedule Appointment");
            Glide.with(mContext).load(company_logo)
                    .apply(new RequestOptions().placeholder(R.drawable.white_light_semiround_bg)
                            .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * 24))))
                            .error(R.drawable.white_light_semiround_bg)).into(iv_profile);
            tv_name.setText(company_name);
            tv_location.setText(MessageFormat.format("{0}, {1}, {2}", city, state, company_country_name));
            rv_appointment.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            JsonArray jsonArray = new JsonArray();
            if (from.equalsIgnoreCase(UserDetail.class.getSimpleName())) {
                if (UserDetail.instance != null) {
                    jsonArray = UserDetail.instance.appointment_data;
                    ArrayList<JsonObject> jsonObjectArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        if (jsonArray.get(i).getAsJsonObject().get("appointment_status").getAsString().equalsIgnoreCase("A")) {
                            jsonObjectArrayList.add(jsonObjectArrayList.size(), jsonArray.get(i).getAsJsonObject());
                        }
                    }
                    AppointmentActionAdapter actionAdapter = new AppointmentActionAdapter(mContext, jsonObjectArrayList);
                    rv_appointment.setAdapter(actionAdapter);
                }

            } else if (from.equalsIgnoreCase(JobDetail.class.getSimpleName())) {
                if (JobDetail.instance != null) {
                    jsonArray = JobDetail.instance.appointments;
                    ArrayList<JsonObject> jsonObjectArrayList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        if (jsonArray.get(i).getAsJsonObject().get("appointment_status").getAsString().equalsIgnoreCase("A")) {
                            jsonObjectArrayList.add(jsonObjectArrayList.size(), jsonArray.get(i).getAsJsonObject());
                        }
                    }
                    AppointmentActionAdapter actionAdapter = new AppointmentActionAdapter(mContext, jsonObjectArrayList);
                    rv_appointment.setAdapter(actionAdapter);
                }
            }


        }
    }


    public class AppointmentActionAdapter extends RecyclerView.Adapter<AppointmentActionAdapter.MyViewHolder> {

        Context mContext;
        ArrayList<JsonObject> data;


        public AppointmentActionAdapter(Context mContext, ArrayList<JsonObject> data) {

            // TODO Auto-generated constructor stub

            this.mContext = mContext;
            this.data = data;


        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.child_appointment_action, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

            JsonObject jsonObject = data.get(position);
            String appointment_id = jsonObject.has("appointment_id") ? jsonObject.get("appointment_id").getAsString() : "";
            String appointment_number = jsonObject.has("appointment_number") ? jsonObject.get("appointment_number").getAsString() : "";
            String appointment_type = jsonObject.has("appointment_type") ? jsonObject.get("appointment_type").getAsString() : "";
            String appointment_date = jsonObject.has("appointment_date") ? jsonObject.get("appointment_date").getAsString() : "";
            String appointment_start_at = jsonObject.has("appointment_start_at") ? jsonObject.get("appointment_start_at").getAsString() : "";
            String appointment_end_at = jsonObject.has("appointment_end_at") ? jsonObject.get("appointment_end_at").getAsString() : "";
            String appointment_status = jsonObject.has("appointment_status") ? jsonObject.get("appointment_status").getAsString() : "";
            String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
            String job_title = jsonObject.has("job_title") ? jsonObject.get("job_title").getAsString() : "";
            holder.tv_jobtitle.setText(job_title);
            if (appointment_type.equalsIgnoreCase("online_meeting")) {
                holder.iv_call.setImageResource(R.drawable.ic_video_player);
            } else if (appointment_type.equalsIgnoreCase("call")) {
                holder.iv_call.setImageResource(R.drawable.ic_telephone_meeting);
            } else if (appointment_type.equalsIgnoreCase("chat")) {
                holder.iv_call.setImageResource(R.drawable.chat_img);
            }
            holder.tv_profile.setText(appointment_number);
            SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
            SimpleDateFormat formatout = new SimpleDateFormat("hh:mm aa");
            Date date, dateend;
            try {
                date = format.parse(appointment_start_at);
                dateend = format.parse(appointment_end_at);
                String date1 = formatout.format(date);
                String dateend1 = formatout.format(dateend);
                holder.tv_duration.setText((date1 + " - " + dateend1).toUpperCase(Locale.ROOT));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.tv_date.setText(appointment_date);
            if (action.equalsIgnoreCase("Cancel")) {
                holder.tv_action.setText("Cancel Appointment");
            } else if (action.equalsIgnoreCase("Reschedule")) {
                holder.tv_action.setText("Reschedule Appointment");
            }

            holder.tv_action.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (action.equalsIgnoreCase("Cancel")) {
                        callCancel(jsonObject);
                    } else if (action.equalsIgnoreCase("Reschedule")) {
                        callReschedule(jsonObject);
                    }
                }
            });

        }


        @Override
        public long getItemId(int pos) {

            return 0;
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView tv_profile, tv_duration, tv_date, tv_action, tv_jobtitle;
            ImageView iv_call;

            public MyViewHolder(View view) {
                super(view);

                tv_profile = view.findViewById(R.id.tv_profile);
                tv_duration = view.findViewById(R.id.tv_duration);
                tv_date = view.findViewById(R.id.tv_date);
                tv_action = view.findViewById(R.id.tv_action);
                iv_call = view.findViewById(R.id.iv_call);
                tv_jobtitle = view.findViewById(R.id.tv_jobtitle);
            }
        }


    }

    private void callReschedule(JsonObject jsonObject) {
        String appointment_id = jsonObject.has("appointment_id") ? jsonObject.get("appointment_id").getAsString() : "";
        String appointment_number = jsonObject.has("appointment_number") ? jsonObject.get("appointment_number").getAsString() : "";
        String appointment_type = jsonObject.has("appointment_type") ? jsonObject.get("appointment_type").getAsString() : "";
        String appointment_date = jsonObject.has("appointment_date") ? jsonObject.get("appointment_date").getAsString() : "";
        String appointment_start_at = jsonObject.has("appointment_start_at") ? jsonObject.get("appointment_start_at").getAsString() : "";
        String appointment_end_at = jsonObject.has("appointment_end_at") ? jsonObject.get("appointment_end_at").getAsString() : "";
        String appointment_status = jsonObject.has("appointment_status") ? jsonObject.get("appointment_status").getAsString() : "";
        String status = jsonObject.has("status") ? jsonObject.get("status").getAsString() : "";
        String job_title = jsonObject.has("job_title") ? jsonObject.get("job_title").getAsString() : "";
        String job_id = jsonObject.has("job_id") ? jsonObject.get("job_id").getAsString() : "";
        String apply_id = jsonObject.has("apply_id") ? jsonObject.get("apply_id").getAsString() : "";
        SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat formatout = new SimpleDateFormat("hh:mm aa");
        Date date,dateend;
        String datefinal="";
        try {
            date = format.parse(appointment_start_at);
            dateend = format.parse(appointment_end_at);
            String date1=  formatout.format(date);
            String dateend1=  formatout.format(dateend);
           datefinal= appointment_date+ "  "+(date1+ " - "+dateend1).toUpperCase(Locale.ROOT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startActivity(new Intent(mContext, RescheduleAppointment.class)
                .putExtra("company_id", company_id)
                .putExtra("company_logo", company_logo)
                .putExtra("company_name", company_name)
                .putExtra("company_city_name", city)
                .putExtra("company_state_name", state)
                .putExtra("company_country_name", company_country_name)
                .putExtra("posted_by", posted_by)
                .putExtra("is_own_job", is_own_job)
                .putExtra("apply_id", apply_id)
                .putExtra("job_id", job_id)
                .putExtra("appointment_id", appointment_id)
                .putExtra("appointment_number", appointment_number)
                .putExtra("appointment_type", appointment_type)
                .putExtra("job_title", job_title)
                .putExtra("notify_number", notify_number)
                .putExtra("from", from)
                .putExtra("date", datefinal)
        );
    }

    public void callCancel(JsonObject jsonObject) {
        Dialog progressdialog = new Dialog(mContext);
        progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressdialog.setContentView(R.layout.cancel_appointment_popup);
        progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;
        progressdialog.getWindow().setAttributes(lp);

        progressdialog.findViewById(R.id.tv_yes).setOnClickListener(v -> {
            progressdialog.dismiss();
            if (rest.isInterentAvaliable()) {
                AppController.ShowDialogue("", mContext);
                cancelAppointment(jsonObject.get("appointment_id").getAsString(),jsonObject.get("appointment_number").getAsString());
            } else {
                rest.AlertForInternet();
            }

        });
        progressdialog.findViewById(R.id.tv_cancel).setOnClickListener(v -> progressdialog.dismiss());
        try {
            progressdialog.show();
        } catch (Exception e) {
        }
    }

    public void cancelAppointment(String id, String appointment_number) {
        AppController.ShowDialogue("", mContext);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(ParaName.KEY_APPOINTMENTID, id);
        hashMap.put(ParaName.KEY_APPOINTMENTSTATUS, "C");
        hashMap.put(ParaName.KEY_APPOINTMENTNUMBER, appointment_number);
        hashMap.put(ParaName.KEY_USERID, company_id);
        hashMap.put(ParaName.KEY_UNIQUENOTIFYNUMBER, notify_number);
        hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
        SwipeeApiClient.swipeeServiceInstance().setAppointmentStatus(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responceBody = response.body();
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        AppController.loggedOut(mContext);
                        setBackPressed();
                    } else if (response.body().get("code").getAsInt() == 200 && responceBody.get("status").getAsBoolean()) {
                        rest.showToast(responceBody.get("message").getAsString());
                        setBackPressed();
                    } else rest.showToast(responceBody.get("message").getAsString());

                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });

    }

    public void setBackPressed() {
        if (from.equalsIgnoreCase(UserDetail.class.getSimpleName())) {
            if (UserDetail.instance != null)
                UserDetail.instance.setBackPressed();
        } else if (from.equalsIgnoreCase(JobDetail.class.getSimpleName())) {
            if (JobDetail.instance != null)
                JobDetail.instance.setBackPressed();
        }
        onBackPressed();
    }
}