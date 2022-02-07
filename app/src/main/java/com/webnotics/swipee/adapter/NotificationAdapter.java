package com.webnotics.swipee.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.webnotics.swipee.R;
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
    public void onBindViewHolder(@NonNull NotificationAdapter.MyViewHolder holder, int position) {
        holder.tv_name.setText(data.get(position).getFirst_name());
        holder.tv_text.setText(data.get(position).getNotification_text());
        holder.tv_createdat.setText(data.get(position).getCreated_at());
        Glide.with(mContext).load(data.get(position).getNotification_image()).
                error(R.drawable.ic_profile_select).placeholder(R.drawable.ic_profile_select).into(holder.civ_logo);
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





