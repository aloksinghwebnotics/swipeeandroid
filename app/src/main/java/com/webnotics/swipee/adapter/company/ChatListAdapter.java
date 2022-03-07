package com.webnotics.swipee.adapter.company;

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
import com.webnotics.swipee.R;
import com.webnotics.swipee.UrlManager.Config;
import com.webnotics.swipee.chat.MainChatActivity;
import com.webnotics.swipee.model.RecentChatModel;

import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<RecentChatModel.Data> data;


    public ChatListAdapter(Context mContext, ArrayList<RecentChatModel.Data> data) {

        // TODO Auto-generated constructor stub

        this.mContext = mContext;
        this.data = data;


    }


    @NonNull
    @Override
    public ChatListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_chatlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Glide.with(mContext).load(data.get(position).getUser_profile()).
                error(R.drawable.ic_profile_select).placeholder(R.drawable.ic_profile_select).into(holder.civ_logo);
        holder.tv_name.setText(Config.isSeeker()?data.get(position).getCompany_name():data.get(position).getFirst_name());
        holder.tv_lastchat.setText(data.get(position).getLast_msg_content());

        if (Config.isSeeker())
            holder.tv_action.setVisibility(View.GONE);
        else holder.tv_action.setVisibility(View.GONE);
        holder.tv_action.setText(data.get(position).getCompany_action());
        holder.tv_unseencount.setText(MessageFormat.format("{0}", data.get(position).getUnseen_msg_count()));
        String date=data.get(position).getMsg_created_at();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat formatout = new SimpleDateFormat("dd MMM hh:mm aa");
        Date dateFinal;
        String date1="";
        try {
            dateFinal = format.parse(date);
            date1=  formatout.format(dateFinal);
            holder.tv_createdat.setText(date1);

        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, MainChatActivity.class)
            .putExtra("image",data.get(position).getUser_profile())
            .putExtra("msg_id",data.get(position).getMsg_id())
            .putExtra("action",data.get(position).getCompany_action())
            .putExtra("appointment_id",data.get(position).getAppointment_id())
            .putExtra("appointment_number",data.get(position).getAppointment_number())
            .putExtra("user_id",Config.isSeeker()?data.get(position).getCompany_id():data.get(position).getUser_id())
            .putExtra("sender_id",data.get(position).getMsg_sender_id())
            .putExtra("receiver_id",data.get(position).getMsg_receiver_id())
            .putExtra("name",Config.isSeeker()? data.get(position).getCompany_name():data.get(position).getFirst_name())
        ));/* holder.itemView.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, ChatActivity.class)
            .putExtra("image",data.get(position).getUser_profile())
            .putExtra("msg_id",data.get(position).getMsg_id())
            .putExtra("action",data.get(position).getCompany_action())
            .putExtra("appointment_id",data.get(position).getAppointment_id())
            .putExtra("user_id",data.get(position).getUser_id())
            .putExtra("sender_id",data.get(position).getMsg_sender_id())
            .putExtra("receiver_id",data.get(position).getMsg_receiver_id())
            .putExtra("name",Config.isSeeker()? data.get(position).getCompany_name():data.get(position).getFirst_name())
        ));*/

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

        TextView tv_name, tv_lastchat,tv_createdat,tv_unseencount,tv_action;
        CircleImageView civ_logo;

        public MyViewHolder(View view) {
            super(view);

            tv_name = view.findViewById(R.id.tv_name);
            tv_lastchat = view.findViewById(R.id.tv_lastchat);
            civ_logo = view.findViewById(R.id.civ_logo);
            tv_createdat = view.findViewById(R.id.tv_createdat);
            tv_unseencount = view.findViewById(R.id.tv_unseencount);
            tv_action = view.findViewById(R.id.tv_action);

        }
    }


}





