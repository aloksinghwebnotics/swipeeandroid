package com.webnotics.swipee.adapter.seeeker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.webnotics.swipee.R;
import com.webnotics.swipee.activity.AppointmentDetail;
import com.webnotics.swipee.activity.Seeker.AppointmentActivity;
import com.webnotics.swipee.activity.JobDetail;
import com.webnotics.swipee.model.seeker.AppointmentModel;
import com.webnotics.swipee.rest.ParaName;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<AppointmentModel.Data> data;
    public AppointmentAdapter(Context mContext, ArrayList<AppointmentModel.Data> data) {

        // TODO Auto-generated constructor stub

        this.mContext = mContext;
        this.data = data;


    }


    @NonNull
    @Override
    public AppointmentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_appointment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_name.setText(data.get(position).getCompany_name());
        holder.tv_profile.setText(data.get(position).getJob_title());
        holder.tv_date.setText(data.get(position).getAppointment_date());
        holder.tv_location.setText(MessageFormat.format("{0}, {1}, {2}", data.get(position).getJob_city(), data.get(position).getJob_state(), data.get(position).getJob_country()));
        if (data.get(position).getAppointment_type().equalsIgnoreCase("online_meeting")){
            holder.iv_call.setImageResource(R.drawable.ic_video_player);
            holder.tv_vedio.setText("Video Call");
        }else if (data.get(position).getAppointment_type().equalsIgnoreCase("call")){
            holder.iv_call.setImageResource(R.drawable.ic_telephone_meeting);
            holder.tv_vedio.setText("Audio Call");
        }else if (data.get(position).getAppointment_type().equalsIgnoreCase("chat")){
            holder.iv_call.setImageResource(R.drawable.chat_img);
            holder.tv_vedio.setText("Chat");
        }
        Glide.with(mContext)
                .load(data.get(position).getCompany_logo())
                .error(R.drawable.ic_swipee_black)
                .transform(new MultiTransformation(new CenterCrop(),new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density*14))))
                .into(holder.civ_logo);
        String dtStart = data.get(position).getAppointment_start_at();
        String end = data.get(position).getAppointment_end_at();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat formatout = new SimpleDateFormat("hh:mm a");
        Date date,dateend;
        try {
            date = format.parse(dtStart);
            dateend = format.parse(end);
              String date1=  formatout.format(date);
              String dateend1=  formatout.format(dateend);
            holder.tv_duration.setText((date1+ " - "+dateend1).toUpperCase(Locale.ROOT));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.iv_call.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, AppointmentDetail.class).putExtra(ParaName.KEY_APPOINTMENTID,data.get(position).getAppointment_id())));
        holder.itemView.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, JobDetail.class).putExtra("from", AppointmentActivity.class.getSimpleName()).putExtra("id",data.get(position).getJob_id())));

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

        TextView tv_profile, tv_name, tv_location,tv_duration,tv_vedio,tv_date;
        ImageView civ_logo,iv_call;

        public MyViewHolder(View view) {
            super(view);

            tv_profile = view.findViewById(R.id.tv_profile);
            tv_name = view.findViewById(R.id.tv_name);
            tv_location = view.findViewById(R.id.tv_location);
            civ_logo = view.findViewById(R.id.civ_logo);
            tv_duration = view.findViewById(R.id.tv_duration);
            iv_call = view.findViewById(R.id.iv_call);
            tv_vedio = view.findViewById(R.id.tv_vedio);
            tv_date = view.findViewById(R.id.tv_date);

        }
    }


}





