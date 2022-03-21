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

import com.webnotics.swipee.R;
import com.webnotics.swipee.activity.Seeker.AppliedJobsActivity;
import com.webnotics.swipee.activity.Seeker.FeaturedJobsActivity;
import com.webnotics.swipee.activity.JobDetail;
import com.webnotics.swipee.activity.Seeker.SavedJobsActivity;
import com.webnotics.swipee.model.seeker.AppliedJobData;

import java.text.MessageFormat;
import java.util.ArrayList;


public class AppliedJobsAdapter extends RecyclerView.Adapter<AppliedJobsAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<AppliedJobData> data;


    public AppliedJobsAdapter(Context mContext, ArrayList<AppliedJobData> data) {

        // TODO Auto-generated constructor stub

        this.mContext = mContext;
        this.data = data;


    }


    @NonNull
    @Override
    public AppliedJobsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_appliedjobs, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AppliedJobsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tv_title.setText(data.get(position).getJob_title());

        holder.tv_companyname.setText(data.get(position).getCompany_name());
        holder.tv_experience.setText(MessageFormat.format("{0} Yrs", data.get(position).getJob_experience()));
        holder.tv_jobdesc.setText(data.get(position).getJob_skills());
        holder.tv_loaction.setText(MessageFormat.format("{0}, {1}", data.get(position).getJob_city(), data.get(position).getJob_state()));
        if (mContext instanceof AppliedJobsActivity) {
            holder.tv_applystatus.setText(data.get(position).getUser_job_status());
            holder.tv_applystatus.setVisibility(View.GONE);
            holder.iv_favorite.setVisibility(View.GONE);
            holder.tv_view.setVisibility(View.GONE);
        } else if (mContext instanceof SavedJobsActivity) {
            holder.tv_applystatus.setVisibility(View.GONE);
            holder.iv_favorite.setVisibility(View.VISIBLE);
            holder.tv_view.setVisibility(View.GONE);
        } else if (mContext instanceof FeaturedJobsActivity) {
            holder.tv_applystatus.setVisibility(View.GONE);
            holder.iv_favorite.setVisibility(View.GONE);
            holder.tv_view.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            String from = mContext instanceof AppliedJobsActivity ? AppliedJobsActivity.class.getSimpleName() : mContext instanceof SavedJobsActivity ? SavedJobsActivity.class.getSimpleName() :
                    mContext instanceof FeaturedJobsActivity ? FeaturedJobsActivity.class.getSimpleName():"";
            mContext.startActivity(new Intent(mContext, JobDetail.class).putExtra("from", from).putExtra("id", data.get(position).getJob_post_id()));
        });
        holder.iv_favorite.setOnClickListener(v -> {
            if (SavedJobsActivity.instance!=null)
                SavedJobsActivity.instance.callSaveJobService(data.get(position).getJob_post_id(), position);
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

        TextView tv_title, tv_companyname, tv_experience, tv_loaction, tv_jobdesc, tv_applystatus, tv_view;
        ImageView iv_favorite;

        public MyViewHolder(View view) {
            super(view);

            tv_title = view.findViewById(R.id.tv_title);
            tv_companyname = view.findViewById(R.id.tv_companyname);
            tv_experience = view.findViewById(R.id.tv_experience);
            tv_loaction = view.findViewById(R.id.tv_loaction);
            tv_jobdesc = view.findViewById(R.id.tv_jobdesc);
            tv_applystatus = view.findViewById(R.id.tv_applystatus);
            iv_favorite = view.findViewById(R.id.iv_favorite);
            tv_view = view.findViewById(R.id.tv_view);

        }
    }


}





