package com.webnotics.swipee.activity.company;

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
import android.widget.LinearLayout;
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
import com.webnotics.swipee.rest.ParaName;
import com.webnotics.swipee.rest.Rest;
import com.webnotics.swipee.rest.SwipeeApiClient;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.IntStream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateAppointment extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_back, iv_profile, iv_video, iv_chat, iv_call;
    TextView tv_name, tv_location, tv_date, tv_time, tv_appointment;
    FlowLayout flowlayout;
    LinearLayout ll_audio, ll_vedio, ll_chat;

    Rest rest;
    Context mContext;
    private String user_id = "";
    ArrayList<String> slotList = new ArrayList<>();
    private Dialog progressdialog;
    private String slotSelected = "";
    private String appointmentType = "";
    private String job_id = "";
    private String apply_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getColor(R.color.white));
        mContext = this;
        rest = new Rest(mContext);

        iv_back = findViewById(R.id.iv_back);
        iv_profile = findViewById(R.id.iv_profile);
        iv_video = findViewById(R.id.iv_video);
        iv_chat = findViewById(R.id.iv_chat);
        iv_call = findViewById(R.id.iv_call);
        tv_name = findViewById(R.id.tv_name);
        tv_location = findViewById(R.id.tv_location);
        tv_date = findViewById(R.id.tv_date);
        tv_time = findViewById(R.id.tv_time);
        tv_appointment = findViewById(R.id.tv_appointment);
        flowlayout = findViewById(R.id.flowlayout);
        ll_audio = findViewById(R.id.ll_audio);
        ll_vedio = findViewById(R.id.ll_vedio);
        ll_chat = findViewById(R.id.ll_chat);

        iv_back.setOnClickListener(this);
        ll_vedio.setOnClickListener(this);
        ll_chat.setOnClickListener(this);
        ll_audio.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        tv_time.setOnClickListener(this);
        tv_appointment.setOnClickListener(this);


        if (getIntent() != null) {

            user_id = getIntent().getStringExtra("user_id") != null ? getIntent().getStringExtra("user_id") : "";
            String first_name = getIntent().getStringExtra("first_name") != null ? getIntent().getStringExtra("first_name") : "";
            String last_name = getIntent().getStringExtra("last_name") != null ? getIntent().getStringExtra("last_name") : "";
            String user_profile = getIntent().getStringExtra("user_profile") != null ? getIntent().getStringExtra("user_profile") : "";
            String city = getIntent().getStringExtra("city") != null ? getIntent().getStringExtra("city") : "";
            String state = getIntent().getStringExtra("state") != null ? getIntent().getStringExtra("state") : "";
            String job_skills = getIntent().getStringExtra("job_skills") != null ? getIntent().getStringExtra("job_skills") : "";
            job_id = getIntent().getStringExtra("job_id") != null ? getIntent().getStringExtra("job_id") : "";
            apply_id = getIntent().getStringExtra("apply_id") != null ? getIntent().getStringExtra("apply_id") : "";
            tv_name.setText(MessageFormat.format("{0} {1}", first_name, last_name));
            tv_location.setText(MessageFormat.format("{0}, {1}", city, state));
            Glide.with(mContext).load(user_profile)
                    .apply(new RequestOptions().placeholder(R.drawable.white_light_semiround_bg)
                            .transform(new MultiTransformation(new CenterCrop(), new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density * 24))))
                            .error(R.drawable.white_light_semiround_bg)).into(iv_profile);
            if (!TextUtils.isEmpty(job_skills)) {
                try {
                    JSONArray jsonData = new JSONArray(job_skills);
                    setFlowData(jsonData);
                } catch (JSONException ignored) {}
            }
            iv_profile.setOnClickListener(v -> {
                if (!TextUtils.isEmpty(user_profile))
                    AppController.callFullImage(mContext, user_profile);
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
                    if (!TextUtils.isEmpty(user_id)) {
                        slotList.clear();
                        tv_time.setText("");
                        slotSelected = "";
                        AppController.ShowDialogue("", mContext);
                        getAppointmentSlot(tv_date.getText().toString(), user_id);
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void setFlowData(JSONArray jsonData) {
        if (jsonData.length() > 0) {
            IntStream.range(0, jsonData.length()).forEach(k -> {
                try {
                    String data = jsonData.get(k).toString();

                    LinearLayout linearLayout = new LinearLayout(mContext);
                    LinearLayout linearLayoutF = new LinearLayout(mContext);
                    FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                            FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                    LinearLayout.LayoutParams layoutParamsF = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, (int) (mContext.getResources().getDisplayMetrics().density * 32));
                    layoutParamsF.setMargins((int) (mContext.getResources().getDisplayMetrics().density * 5), 0, (int) (mContext.getResources().getDisplayMetrics().density * 5), (int) (mContext.getResources().getDisplayMetrics().density * 8));
                    linearLayoutF.setLayoutParams(layoutParams);
                    linearLayout.setLayoutParams(layoutParamsF);
                    linearLayout.setPadding((int) (mContext.getResources().getDisplayMetrics().density * 8), 0, (int) (mContext.getResources().getDisplayMetrics().density * 8), 0);
                    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                    linearLayout.setGravity(Gravity.CENTER);
                    linearLayout.setBackgroundResource(R.drawable.primary_semiround_bg);
                    PopinsRegularTextView bt = new PopinsRegularTextView(mContext);
                    bt.setText(data);
                    bt.setAllCaps(false);
                    bt.setTextSize(12f);
                    bt.setTag(data);
                    bt.setMaxLines(1);
                    bt.setEllipsize(TextUtils.TruncateAt.END);
                    bt.setTextColor(mContext.getResources().getColor(R.color.white));
                    LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams1.setMargins(0, 0, 0, 0);
                    bt.setLayoutParams(layoutParams1);
                    linearLayout.addView(bt);
                    linearLayoutF.addView(linearLayout);
                    linearLayoutF.setTag(data);
                    flowlayout.addView(linearLayoutF);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_vedio:
                appointmentType = "online_meeting";
                iv_video.setImageResource(R.drawable.ic_icon_awesome_video_fill);
                iv_chat.setImageResource(R.drawable.ic_chat);
                iv_call.setImageResource(R.drawable.ic_telephone1);

                break;
            case R.id.ll_chat:
                appointmentType = "chat";
                iv_video.setImageResource(R.drawable.ic_icon_awesome_video);
                iv_chat.setImageResource(R.drawable.ic_icon_material_chat_bubble);
                iv_call.setImageResource(R.drawable.ic_telephone1);
                break;
            case R.id.ll_audio:
                appointmentType = "call";
                iv_video.setImageResource(R.drawable.ic_icon_awesome_video);
                iv_chat.setImageResource(R.drawable.chat_primary);
                iv_call.setImageResource(R.drawable.ic_telephonefill);
                break;
            case R.id.tv_date:
                setDate();
                break;
            case R.id.tv_time:
                if (TextUtils.isEmpty(tv_date.getText().toString())){
                    rest.showToast("Please select appointment date first.");
                }else {
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
                        SizeAdapter appliedJobsAdapter = new SizeAdapter(mContext);

                        recyclerView.setAdapter(appliedJobsAdapter);

                        progressdialog.findViewById(R.id.tv_yes).setOnClickListener(v1 -> {

                            if (TextUtils.isEmpty(slotSelected)) rest.showToast("Select Time Slot");
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
                }


                break;
            case R.id.tv_appointment:
                if (TextUtils.isEmpty(tv_date.getText().toString()))
                    rest.showToast("Select Appointment Date");
                else if (TextUtils.isEmpty(tv_time.getText().toString()))
                    rest.showToast("Select Appointment Time");
                else if (TextUtils.isEmpty(appointmentType))
                    rest.showToast("Select Appointment Mode");
                else {
                    if (rest.isInterentAvaliable()) {
                        HashMap<String, String> hashMap = new HashMap<>();
                        String time = tv_time.getText().toString();
                        String start = time.substring(0, time.indexOf(" - "));
                        String end = time.substring(time.indexOf("-") + 2);
                        hashMap.put(ParaName.KEYTOKEN, Config.GetUserToken());
                        hashMap.put(ParaName.KEY_APPOINTMENTDATE, tv_date.getText().toString());
                        hashMap.put(ParaName.KEY_APPOINTMENTTYPE, appointmentType);
                        hashMap.put(ParaName.KEY_JOBID, job_id);
                        hashMap.put(ParaName.KEY_APLLYID, apply_id);
                        hashMap.put(ParaName.KEY_UID, user_id);
                        hashMap.put(ParaName.KEY_ENDTIME, end);
                        hashMap.put(ParaName.KEY_STARTTIME, start);
                        hashMap.put(ParaName.KEY_UNIQUENOTIFYNUMBER, "");
                        AppController.ShowDialogue("", mContext);
                        createAppointment(hashMap);
                    }
                }

                break;

            default:
                break;
        }

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
                        AppController.loggedOut(mContext);
                        finish();
                    } else if (responceBody.get("status").getAsBoolean()) {
                        if (UserDetail.instance != null) UserDetail.instance.setBackPressed();
                        startActivity(new Intent(mContext, CompanyHomeActivity.class).putExtra("from", CreateAppointment.class.getSimpleName()));
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
        SwipeeApiClient.swipeeServiceInstance().getAppointmentSlot(Config.GetUserToken(), date, uid).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                AppController.dismissProgressdialog();
                if (response.code() == 200 && response.body() != null) {
                    JsonObject responceBody = response.body();
                    if (response.body().get("code").getAsInt() == 203) {
                        rest.showToast(response.body().get("message").getAsString());
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

    public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.MyViewHolder> {

        Context mContext;
        MyViewHolder oldHolder;

        public SizeAdapter(Context mContext) {
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
        public void onBindViewHolder(@NonNull SizeAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
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