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
import com.webnotics.swipee.activity.Seeker.FirstVideoActivity;
import com.webnotics.swipee.model.seeker.NotificationModel;

import java.util.ArrayList;

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
                if (data.get(position).getPayload_data().size()>0){
                    JsonObject object=data.get(position).getPayload_data();
                    String type=object.has("notify_category")?object.get("notify_category").getAsString():"";
                    if (type.equalsIgnoreCase("video_call") ||type.equalsIgnoreCase("audio_call")){
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





