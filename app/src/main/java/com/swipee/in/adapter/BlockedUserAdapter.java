package com.swipee.in.adapter;

import android.annotation.SuppressLint;
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
import com.swipee.in.R;
import com.swipee.in.activity.BlockedUsers;
import com.swipee.in.model.company.LikedUserModel;

import java.text.MessageFormat;
import java.util.ArrayList;


public class BlockedUserAdapter extends RecyclerView.Adapter<BlockedUserAdapter.MyViewHolder> {

    BlockedUsers mContext;
    ArrayList<LikedUserModel> data;


    public BlockedUserAdapter(BlockedUsers mContext, ArrayList<LikedUserModel> data) {

        // TODO Auto-generated constructor stub

        this.mContext = mContext;
        this.data = data;
    }


    @NonNull
    @Override
    public BlockedUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_blockedusers, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockedUserAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.tv_companyname.setText(data.get(position).getSkill_name());
        holder.tv_name.setText(data.get(position).getFirst_name());
        holder.tv_unblock.setText(String.format("Unblock %s", data.get(position).getFirst_name()));
        holder.tv_loaction.setText(MessageFormat.format("{0}, {1}", data.get(position).getCity(), data.get(position).getState()));
        Glide.with(mContext)
                .load(data.get(position).getUser_profile())
                .error(R.drawable.lightgray_round_bg)
                .placeholder(R.drawable.lightgray_round_bg)
                .transform(new MultiTransformation(new CenterCrop(),new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density*12))))
                .into(holder.iv_backimg);

        holder.tv_unblock.setOnClickListener(view -> mContext.unBlockUser(data.get(position).getUser_id(),data.get(position).getFirst_name(),data.get(position).getJob_id()));

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

        TextView tv_companyname,tv_name,tv_loaction,tv_unblock;
        ImageView iv_backimg;

        public MyViewHolder(View view) {
            super(view);

            tv_companyname = view.findViewById(R.id.tv_companyname);
            tv_name = view.findViewById(R.id.tv_name);
            tv_loaction = view.findViewById(R.id.tv_loaction);
            iv_backimg = view.findViewById(R.id.iv_backimg);
            tv_unblock = view.findViewById(R.id.tv_unblock);
        }
    }

}





