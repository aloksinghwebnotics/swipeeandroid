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
import com.webnotics.swipee.R;
import com.webnotics.swipee.activity.Seeker.JobDetail;
import com.webnotics.swipee.activity.Seeker.JobListActivity;
import com.webnotics.swipee.model.seeker.AppliedJobData;

import java.util.ArrayList;


public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<AppliedJobData> data;


    public JobListAdapter(Context mContext, ArrayList<AppliedJobData> data) {

        // TODO Auto-generated constructor stub

        this.mContext = mContext;
        this.data = data;
    }


    @NonNull
    @Override
    public JobListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_joblist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JobListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.tv_name.setText(data.get(position).getJob_title());
        holder.tv_text.setText(data.get(position).getJob_skills());
        Glide.with(mContext)
                .load(data.get(position).getCompany_logo())
                .error(R.drawable.ic_profile_select)
                .placeholder(R.drawable.ic_profile_select)
                .into(holder.civ_logo);
        holder.itemView.setOnClickListener(v -> {
            String from=mContext instanceof JobListActivity?JobListActivity.class.getSimpleName():"";
            mContext.startActivity(new Intent(mContext, JobDetail.class).putExtra("from",from).putExtra("id",data.get(position).getJob_post_id()));
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

        TextView tv_name,tv_text;
        ImageView civ_logo;

        public MyViewHolder(View view) {
            super(view);

            tv_name = view.findViewById(R.id.tv_name);
            tv_text = view.findViewById(R.id.tv_text);
            civ_logo = view.findViewById(R.id.civ_logo);

        }
    }


}





