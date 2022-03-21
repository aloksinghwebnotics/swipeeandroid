package com.webnotics.swipee.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.webnotics.swipee.CustomUi.FlowLayout;
import com.webnotics.swipee.CustomUi.PopinsRegularTextView;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.AppController;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.Seeker.SeekerHomeActivity;
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RescheduleAppointment extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_back, iv_profile;
    TextView tv_name, tv_location,tv_datetime, tv_date, tv_postedfor, tv_time, tv_appointment, tv_jobtitle, tv_appint_number, tv_mode;
    FlowLayout flowlayout;

    Rest rest;
    Context mContext;
    private String company_id = "";
    ArrayList<String> slotList = new ArrayList<>();
    private Dialog progressdialog;
    private String slotSelected = "";
    private String job_id = "";
    private String apply_id = "";
    private String appointmentId = "";
    private String from = "";
    private String appointment_type = "";
    private String notify_number="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reschedule_appointment);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        rest = new Rest(mContext);

        iv_back = findViewById(R.id.iv_back);
        iv_profile = findViewById(R.id.iv_profile);
        tv_name = findViewById(R.id.tv_name);
        tv_location = findViewById(R.id.tv_location);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        tv_appointment = findViewById(R.id.tv_appointment);
        flowlayout = findViewById(R.id.flowlayout);
        tv_postedfor = findViewById(R.id.tv_postedfor);
        tv_jobtitle = findViewById(R.id.tv_jobtitle);
        tv_appint_number = findViewById(R.id.tv_appint_number);
        tv_datetime = findViewById(R.id.tv_datetime);
        tv_mode = findViewById(R.id.tv_mode);

        iv_back.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        tv_appointment.setOnClickListener(this);


        if (getIntent() != null) {

            company_id = getIntent().getStringExtra("company_id") != null ? getIntent().getStringExtra("company_id") : "";
            notify_number = getIntent().getStringExtra("notify_number") != null ? getIntent().getStringExtra("notify_number") : "";
            String company_logo = getIntent().getStringExtra("company_logo") != null ? getIntent().getStringExtra("company_logo") : "";
            String company_name = getIntent().getStringExtra("company_name") != null ? getIntent().getStringExtra("company_name") : "";
            String city = getIntent().getStringExtra("company_city_name") != null ? getIntent().getStringExtra("company_city_name") : "";
            String state = getIntent().getStringExtra("company_state_name") != null ? getIntent().getStringExtra("company_state_name") : "";
            String company_country_name = getIntent().getStringExtra("company_country_name") != null ? getIntent().getStringExtra("company_country_name") : "";
            String posted_by = getIntent().getStringExtra("posted_by") != null ? getIntent().getStringExtra("posted_by") : "";
            String is_own_job = getIntent().getStringExtra("is_own_job") != null ? getIntent().getStringExtra("is_own_job") : "";
            job_id = getIntent().getStringExtra("job_id") != null ? getIntent().getStringExtra("job_id") : "";
            apply_id = getIntent().getStringExtra("apply_id") != null ? getIntent().getStringExtra("apply_id") : "";
            appointmentId = getIntent().getStringExtra("appointment_id") != null ? getIntent().getStringExtra("appointment_id") : "";
            from = getIntent().getStringExtra("from") != null ? getIntent().getStringExtra("from") : "";
            String appointment_number = getIntent().getStringExtra("appointment_number") != null ? getIntent().getStringExtra("appointment_number") : "";
            appointment_type = getIntent().getStringExtra("appointment_type") != null ? getIntent().getStringExtra("appointment_type") : "";
            String job_title = getIntent().getStringExtra("job_title") != null ? getIntent().getStringExtra("job_title") : "";
            String date = getIntent().getStringExtra("date") != null ? getIntent().getStringExtra("date") : "";
            tv_name.setText(posted_by);
            tv_jobtitle.setText(job_title);
            tv_datetime.setText(date);
            tv_appint_number.setText(MessageFormat.format("{0}", appointment_number));
            if (appointment_type.equalsIgnoreCase("online_meeting")) {
                tv_mode.setText("Video Call ");
                tv_mode.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getDrawable(R.drawable.ic_video_player), null);
            } else if (appointment_type.equalsIgnoreCase("call")) {
                tv_mode.setText("Audio Call ");
                tv_mode.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getDrawable(R.drawable.ic_telephone_meeting), null);
            } else if (appointment_type.equalsIgnoreCase("chat")) {
                tv_mode.setText("Chat ");
                tv_mode.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getDrawable(R.drawable.chat_img), null);
            }
            if (is_own_job.equalsIgnoreCase("N")) {
                tv_postedfor.setVisibility(View.VISIBLE);
                tv_postedfor.setText(MessageFormat.format("Posted for - {0}", company_name));
            } else tv_postedfor.setVisibility(View.GONE);
            tv_location.setText(MessageFormat.format("{0}, {1}, {2}", city, state, company_country_name));
            Glide.with(mContext).load(company_logo)
                    .apply(new RequestOptions().placeholder(R.drawable.white_light_semiround_bg)
                            .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * 24))))
                            .error(R.drawable.white_light_semiround_bg)).into(iv_profile);

            iv_profile.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(company_logo))
                    AppController.callFullImage(mContext, company_logo);
            });
        }
    }

    private void setDate() {
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                (view, year, monthOfYear, dayOfMonth) -> {
                    int mothfinal = monthOfYear + 1;
                    tv_date.setText(year + "-" + mothfinal + "-" + dayOfMonth);
                    if (!TextUtils.isEmpty(company_id)) {
                        slotList.clear();
                        tv_time.setText("");
                        slotSelected = "";
                        AppController.ShowDialogue("", mContext);
                        if (Config.isSeeker()) getAppointmentSlot(tv_date.getText().toString(), company_id);
                        else getAppointmentSlotCompany(tv_date.getText().toString(), company_id);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_date:
                setDate();
                break;
            case R.id.tv_time:
                if (slotList.size() > 0) {
                    progressdialog = new Dialog(mContext);
                    progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    progressdialog.setContentView(R.layout.time_slot_popup);
                    progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                    lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                    lp.gravity = Gravity.CENTER;
                    progressdialog.getWindow().setAttributes(lp);
                    RecyclerView recyclerView = progressdialog.findViewById(R.id.rv_sizelist);
                    recyclerView.setLayoutManager(new GridLayoutManager(mContext, 2));
                    recyclerView.setNestedScrollingEnabled(false);
                    SizeAdapter appliedJobsAdapter = new SizeAdapter();

                    recyclerView.setAdapter(appliedJobsAdapter);

                    progressdialog.findViewById(R.id.tv_yes).setOnClickListener(v1 -> {

                        if (TextUtils.isEmpty(slotSelected)) rest.showToast("Select time slot");
                        else {
                            tv_time.setText(slotSelected);
                            progressdialog.dismiss();
                        }
                    });
                    progressdialog.findViewById(R.id.tv_cancel).setOnClickListener(v12 -> progressdialog.dismiss());
                    try {
                        progressdialog.show();
                    } catch (Exception ignored) {}

                }

                break;
            case R.id.tv_appointment:
                if (TextUtils.isEmpty(tv_date.getText().toString())) rest.showToast("Select appointment date");
                else if (TextUtils.isEmpty(tv_time.getText().toString())) rest.showToast("Select appointment time");
                else {
                    if (!Config.isSeeker()) {
                        if (rest.isInterentAvaliable()) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            String time = tv_time.getText().toString();
                            String start = time.substring(0, time.indexOf(" - "));
                            String end = time.substring(time.indexOf("-") + 2);
                            hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                            hashMap.put(ParaName.KEY_APPOINTMENTDATE, tv_date.getText().toString());
                            hashMap.put(ParaName.KEY_APPOINTMENTTYPE, appointment_type);
                            hashMap.put(ParaName.KEY_JOBID, job_id);
                            hashMap.put(ParaName.KEY_APLLYID, apply_id);
                            hashMap.put(ParaName.KEY_UID, company_id);
                            hashMap.put(ParaName.KEY_ENDTIME, end);
                            hashMap.put(ParaName.KEY_STARTTIME, start);
                            hashMap.put(ParaName.KEY_APPOINTMENTID, appointmentId);
                            hashMap.put(ParaName.KEY_UNIQUENOTIFYNUMBER, notify_number);
                            AppController.ShowDialogue("", mContext);
                            createAppointment(hashMap);
                        } else rest.AlertForInternet();
                    } else if (Config.isSeeker()) {
                        if (rest.isInterentAvaliable()) {
                            HashMap<String, String> hashMap = new HashMap<>();
                            String time = tv_time.getText().toString();
                            String start = time.substring(0, time.indexOf(" - "));
                            String end = time.substring(time.indexOf("-") + 2);
                            hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                            hashMap.put(ParaName.KEY_APPOINTMENTDATE, tv_date.getText().toString());
                            hashMap.put(ParaName.KEY_APPOINTMENTTYPE, appointment_type);
                            hashMap.put(ParaName.KEY_COMPANYID, company_id);
                            hashMap.put(ParaName.KEY_APPOINTMENTID, appointmentId);
                            hashMap.put(ParaName.KEY_ENDTIME, end);
                            hashMap.put(ParaName.KEY_STARTTIME, start);
                            hashMap.put(ParaName.KEY_UNIQUENOTIFYNUMBER, notify_number);
                            AppController.ShowDialogue("", mContext);
                            rescheduleAppointment(hashMap);
                        } else rest.AlertForInternet();
                    }
                }
                break;

            default:
                break;
        }

    }

    private void rescheduleAppointment(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().rescheduleAppointment(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responceBody = response.body();
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        if (from.equalsIgnoreCase(JobDetail.class.getSimpleName())){
                            if (AppointmentAction.instance != null)
                                AppointmentAction.instance.setBackPressed();
                        } else if (from.equalsIgnoreCase(NotificationAppointmentAction.class.getSimpleName())){
                            if (NotificationAppointmentAction.instance!=null)
                                NotificationAppointmentAction.instance.setBackPressed();
                            startActivity(new Intent(mContext, SeekerHomeActivity.class).putExtra("from", "match"));
                        }
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (response.body().get("code").getAsInt() == 200 && responceBody.get("status").getAsBoolean()) {
                        rest.showToast(responceBody.get("message").getAsString());
                        if (from.equalsIgnoreCase(JobDetail.class.getSimpleName())){
                            if (AppointmentAction.instance != null)
                                AppointmentAction.instance.setBackPressed();
                        } else if (from.equalsIgnoreCase(NotificationAppointmentAction.class.getSimpleName())){
                            if (NotificationAppointmentAction.instance!=null)
                                NotificationAppointmentAction.instance.onBackPressed();
                        }
                        finish();
                    } else rest.showToast(responceBody.get("message").getAsString());

                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }

    private void getAppointmentSlot(String date, String uid) {
        SwipeeApiClient.swipeeServiceInstance().getSeekerAppointmentSlot(Config.GetUserToken(), date, uid).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responseBody = response.body();
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        if (AppointmentAction.instance != null)
                            AppointmentAction.instance.setBackPressed();
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (responseBody.get("status").getAsBoolean()) {
                        JsonObject data = responseBody.has("data") ? responseBody.get("data").getAsJsonObject() : new JsonObject();
                        JsonArray available_open_slots = data.has("available_open_slots") ? data.get("available_open_slots").getAsJsonArray() : new JsonArray();
                        slotList = new ArrayList<>();
                        IntStream.range(0, available_open_slots.size()).forEach(i -> slotList.add(slotList.size(), available_open_slots.get(i).getAsString()));
                    }

                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }

    private void getAppointmentSlotCompany(String date, String uid) {
        SwipeeApiClient.swipeeServiceInstance().getAppointmentSlot(Config.GetUserToken(), date, uid).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responceBody = response.body();
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        if (AppointmentAction.instance != null)
                            AppointmentAction.instance.setBackPressed();
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (responceBody.get("status").getAsBoolean()) {
                        JsonObject data = responceBody.has("data") ? responceBody.get("data").getAsJsonObject() : new JsonObject();
                        JsonArray available_open_slots = data.has("available_open_slots") ? data.get("available_open_slots").getAsJsonArray() : new JsonArray();
                        slotList = new ArrayList<>();
                        IntStream.range(0, available_open_slots.size()).forEach(i -> slotList.add(slotList.size(), available_open_slots.get(i).getAsString()));
                    }

                } else rest.showToast("Something went wrong");

            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }


    private void createAppointment(HashMap<String, String> hashMap) {
        SwipeeApiClient.swipeeServiceInstance().createAppointment(hashMap).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responceBody = response.body();
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
                        if (AppointmentAction.instance != null)
                            AppointmentAction.instance.setBackPressed();
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (responceBody.get("status").getAsBoolean()) {
                        if (AppointmentAction.instance != null)
                            AppointmentAction.instance.setBackPressed();
                        finish();
                    } else rest.showToast(responceBody.get("message").getAsString());
                } else rest.showToast("Something went wrong");
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                AppController.dismissProgressdialog();
            }
        });
    }


    public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.MyViewHolder> {


        MyViewHolder oldHolder;

        public SizeAdapter() {
            // TODO Auto-generated constructor stub
        }


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.child_time_slot, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.tv_item.setText(slotList.get(position));
            holder.tv_item.setOnClickListener(v -> {
                if (oldHolder != null) {
                    oldHolder.itemView.setBackgroundResource(R.drawable.primary_stroke_semiround);
                    oldHolder.tv_item.setTextColor(getColor(R.color.black));
                }
                oldHolder = holder;
                holder.itemView.setBackgroundResource(R.drawable.primary_semiround_bg);
                holder.tv_item.setTextColor(getColor(R.color.white));
                slotSelected = slotList.get(position);

            });
        }


        @Override
        public long getItemId(int pos) {

            return 0;
        }

        @Override
        public int getItemCount() {
            return slotList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            PopinsRegularTextView tv_item;

            public MyViewHolder(View view) {
                super(view);

                tv_item = view.findViewById(R.id.tv_item);

            }
        }
    }
}