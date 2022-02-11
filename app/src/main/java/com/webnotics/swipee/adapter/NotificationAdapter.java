package com.webnotics.swipee.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.activity.AppointmentDetail;
import com.webnotics.swipee.activity.NotificationActivity;
import com.webnotics.swipee.activity.Seeker.FirstVideoActivity;
import com.webnotics.swipee.activity.Seeker.JobDetail;
import com.webnotics.swipee.activity.company.NotificationAppointmentAction;
import com.webnotics.swipee.activity.company.UserDetail;
import com.webnotics.swipee.model.seeker.NotificationModel;
import com.webnotics.swipee.rest.ParaName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<NotificationModel.Data> data;


    public NotificationAdapter(Context mContext, ArrayList<NotificationModel.Data> data) {

        // TODO Auto-generated constructor stub

        this.mContext = mContext;
        this.data = data;


    }


    @NonNull
    @Override
    public NotificationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_notification, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_name.setText(data.get(position).getFirst_name());
        holder.tv_text.setText(data.get(position).getNotification_text());
        if (Config.isSeeker())
            holder.tv_name.setText(data.get(position).getCompany_name());
        else   holder.tv_name.setText(data.get(position).getFirst_name());
        holder.tv_createdat.setText(data.get(position).getCreated_at());
        Glide.with(mContext).load(data.get(position).getNotification_image()).
                error(R.drawable.ic_profile_select).placeholder(R.drawable.ic_profile_select).into(holder.civ_logo);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   if (data.get(position).getPayload_data().size()>0){
                       JsonObject object=data.get(position).getPayload_data();
                       String type=object.has("notify_category")?object.get("notify_category").getAsString():"";
                       if (type.equalsIgnoreCase("bulk_job_post_notification")) {
                           Intent resultIntent = new Intent(mContext, JobDetail.class);
                           resultIntent.putExtra("id", object.get("job_id").getAsString());
                           resultIntent.putExtra("from", NotificationActivity.class.getSimpleName());
                           mContext.startActivity(resultIntent);
                       } else if (type.equalsIgnoreCase("user_swap_job")) {
                           Intent resultIntent = new Intent(mContext, UserDetail.class);
                           resultIntent.putExtra("job_id", object.get("job_id").getAsString());
                           resultIntent.putExtra("id", object.get("user_id").getAsString());
                           resultIntent.putExtra("from", NotificationActivity.class.getSimpleName());
                           resultIntent.putExtra("name", "");
                           mContext.startActivity(resultIntent);
                       }  else if (type.equalsIgnoreCase("company_swap_profile")) {
                           Intent resultIntent = new Intent(mContext, JobDetail.class);
                           resultIntent.putExtra("id", object.get("job_id").getAsString());
                           resultIntent.putExtra("from", NotificationActivity.class.getSimpleName());
                           mContext.startActivity(resultIntent);
                       } else if (type.equalsIgnoreCase("company_change_appointment_status")) {
                           Intent resultIntent;
                           if (object.has("appointment_status") && object.get("appointment_status").getAsString().equalsIgnoreCase("A")){
                               resultIntent = new Intent(mContext, AppointmentDetail.class);
                               resultIntent.putExtra(ParaName.KEY_APPOINTMENTID, object.get("appointment_id").getAsString());
                               resultIntent.putExtra("from", NotificationActivity.class.getSimpleName());
                           }else {
                                resultIntent = new Intent(mContext, JobDetail.class);
                               resultIntent.putExtra("id", object.get("job_id").getAsString());
                               resultIntent.putExtra("from", NotificationActivity.class.getSimpleName());
                           }
                           mContext.startActivity(resultIntent);
                       }else if (type.equalsIgnoreCase("user_change_appointment_status")) {
                           Intent resultIntent;
                           if (object.has("appointment_status") && object.get("appointment_status").getAsString().equalsIgnoreCase("A")){
                               resultIntent = new Intent(mContext, AppointmentDetail.class);
                               resultIntent.putExtra(ParaName.KEY_APPOINTMENTID, object.get("appointment_id").getAsString());
                               resultIntent.putExtra("from", NotificationActivity.class.getSimpleName());
                           }else {
                               resultIntent = new Intent(mContext, UserDetail.class);
                               resultIntent.putExtra("job_id", object.get("job_id").getAsString());
                               resultIntent.putExtra("id", object.get("user_id").getAsString());
                               resultIntent.putExtra("from", NotificationActivity.class.getSimpleName());
                               resultIntent.putExtra("name", object.get("first_name").getAsString());
                           }
                           mContext.startActivity(resultIntent);




                       }else if (type.equalsIgnoreCase("user_booked_appointment")) {
                           String dtStart = object.get("appointment_start_time").getAsString();
                           String end =  object.get("appointment_end_time").getAsString();
                           SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                           SimpleDateFormat formatout = new SimpleDateFormat("hh:mm aa");
                           Date date,dateend;
                           String datefinal="";
                           try {
                               date = format.parse(dtStart);
                               dateend = format.parse(end);
                               String date1=  formatout.format(date);
                               String dateend1=  formatout.format(dateend);
                               datefinal=  object.get("appointment_date").getAsString()+ "  "+(date1+ " - "+dateend1).toUpperCase(Locale.ROOT);
                           } catch (ParseException e) {
                               e.printStackTrace();
                           }

                           Intent resultIntent = new Intent(mContext, NotificationAppointmentAction.class);
                           resultIntent.putExtra("company_id", object.get("company_id").getAsString());
                           resultIntent.putExtra("company_logo", object.get("user_profile").getAsString());
                           resultIntent.putExtra("appointment_type",  object.get("appointment_type").getAsString());
                           resultIntent.putExtra("user_id",  object.get("user_id").getAsString());
                           resultIntent.putExtra("company_name", object.get("first_name").getAsString()+" " +object.get("last_name").getAsString());
                           resultIntent.putExtra("posted_by", object.get("first_name").getAsString()+" " +object.get("last_name").getAsString());
                           resultIntent.putExtra("appointment_number",  object.get("appointment_number").getAsString());
                           resultIntent.putExtra("appointment_id",  object.get("appointment_id").getAsString());
                           resultIntent.putExtra("company_country_name",  object.get("country_name").getAsString());
                           resultIntent.putExtra("company_state_name", object.get("state_name").getAsString());
                           resultIntent.putExtra("company_city_name",  object.get("city_name").getAsString());
                           resultIntent.putExtra("is_own_job", "Y");
                           resultIntent.putExtra("date", datefinal);
                           resultIntent.putExtra("from", NotificationActivity.class.getSimpleName());
                           mContext.startActivity(resultIntent);
                       }else if (type.equalsIgnoreCase("employer_booked_appointment")){
                           String dtStart = object.get("appointment_start_time").getAsString();
                           String end = object.get("appointment_end_time").getAsString();
                           SimpleDateFormat format = new SimpleDateFormat("hh:mm");
                           SimpleDateFormat formatout = new SimpleDateFormat("hh:mm aa");
                           Date date,dateend;
                           String datefinal="";
                           try {
                               date = format.parse(dtStart);
                               dateend = format.parse(end);
                               String date1=  formatout.format(date);
                               String dateend1=  formatout.format(dateend);
                               datefinal= object.get("appointment_date").getAsString()+ "  "+(date1+ " - "+dateend1).toUpperCase(Locale.ROOT);
                           } catch (ParseException e) {
                               e.printStackTrace();
                           }

                           Intent resultIntent = new Intent(mContext, NotificationAppointmentAction.class);
                           resultIntent.putExtra("company_id",  object.get("company_id").getAsString());
                           resultIntent.putExtra("company_logo",  object.get("company_logo").getAsString());
                           resultIntent.putExtra("appointment_type",  object.get("appointment_type").getAsString());
                           resultIntent.putExtra("user_id",  object.get("user_id").getAsString());
                           resultIntent.putExtra("company_name",  object.get("company_name").getAsString());
                           resultIntent.putExtra("posted_by",  object.get("company_name").getAsString());
                           resultIntent.putExtra("appointment_number",  object.get("appointment_number").getAsString());
                           resultIntent.putExtra("appointment_id",  object.get("appointment_id").getAsString());
                           resultIntent.putExtra("company_country_name",  object.get("country_name").getAsString());
                           resultIntent.putExtra("company_state_name",  object.get("state_name").getAsString());
                           resultIntent.putExtra("company_city_name",  object.get("city_name").getAsString());
                           resultIntent.putExtra("is_own_job", "Y");
                           resultIntent.putExtra("date", datefinal);
                           resultIntent.putExtra("from", NotificationActivity.class.getSimpleName());
                           mContext.startActivity(resultIntent);
                       }else if (type.equalsIgnoreCase("video_call") ||type.equalsIgnoreCase("audio_call")){
                           String appointment_date=object.has("appointment_date")?object.get("appointment_date").getAsString():"";
                           String appointment_start_at=object.has("appointment_start_at")?object.get("appointment_start_at").getAsString():"";
                           String appointment_end_at=object.has("appointment_end_at")?object.get("appointment_end_at").getAsString():"";
                           Date calendarDate = Calendar.getInstance().getTime();
                           SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                           try {
                               Date date2 = formatDate.parse(appointment_date + " " + appointment_start_at);
                               Date date3 = formatDate.parse(appointment_date + " " + appointment_end_at);
                               if (date2 != null && date3 != null) {
                                   if (date2.before(calendarDate) && date3.after(calendarDate)) {

                                       Intent resultIntent = new Intent(mContext, FirstVideoActivity.class);
                                       resultIntent.putExtra("user_access_token", object.get("user_access_token").getAsString());
                                       resultIntent.putExtra("appointment_type", object.get("appointment_type").getAsString());
                                       resultIntent.putExtra("room_name", object.get("room_name").getAsString());
                                       resultIntent.putExtra("user_name", object.get("user_name").getAsString());
                                       resultIntent.putExtra("user_mobile", object.get("user_mobile").getAsString());
                                       resultIntent.putExtra("user_phone_code", object.get("user_phone_code").getAsString());
                                       resultIntent.putExtra("user_profile", object.get("user_profile").getAsString());
                                       resultIntent.putExtra("Aid", object.get("appointment_id").getAsString());
                                       resultIntent.putExtra("role_id", object.get("role_id").getAsString());
                                       resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                       mContext.startActivity(resultIntent);
                                   }
                               }
                           } catch (ParseException e) {
                               e.printStackTrace();
                           }

                       }

                   }
               }catch (Exception e){}
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_createdat, tv_name, tv_text;
        CircleImageView civ_logo;

        public MyViewHolder(View view) {
            super(view);

            tv_createdat = view.findViewById(R.id.tv_createdat);
            tv_name = view.findViewById(R.id.tv_name);
            tv_text = view.findViewById(R.id.tv_text);
            civ_logo = view.findViewById(R.id.civ_logo);

        }
    }


}





