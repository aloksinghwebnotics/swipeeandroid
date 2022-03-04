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
import com.webnotics.swipee.activity.JobDetail;
import com.webnotics.swipee.activity.Seeker.LikedJobsActivity;
import com.webnotics.swipee.activity.Seeker.MatchedCompanyActivity;
import com.webnotics.swipee.model.seeker.AppliedJobData;

import java.text.MessageFormat;
import java.util.ArrayList;


public class LikedJobsAdapter extends RecyclerView.Adapter<LikedJobsAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<AppliedJobData> data;


    public LikedJobsAdapter(Context mContext, ArrayList<AppliedJobData> data) {

        // TODO Auto-generated constructor stub

        this.mContext = mContext;
        this.data = data;
    }


    @NonNull
    @Override
    public LikedJobsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_likedjobs, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull LikedJobsAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {


        holder.tv_companyname.setText(data.get(position).getJob_title());
        holder.tv_name.setText(data.get(position).getCompany_name());
        holder.tv_loaction.setText(MessageFormat.format("{0}, {1}", data.get(position).getJob_city(), data.get(position).getJob_state()));
        Glide.with(mContext)
                .load(data.get(position).getCompany_logo())
                .error(R.drawable.lightgray_round_bg)
                .placeholder(R.drawable.lightgray_round_bg)
                .transform(new MultiTransformation(new CenterCrop(),new RoundedCorners((int) (mContext.getResources().getDisplayMetrics().density*12))))
                .into(holder.iv_backimg);
        holder.itemView.setOnClickListener(v -> {
            String from=mContext instanceof LikedJobsActivity? LikedJobsActivity.class.getSimpleName():mContext instanceof MatchedCompanyActivity?MatchedCompanyActivity.class.getSimpleName():"";
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

        TextView tv_companyname,tv_name,tv_loaction;
        ImageView iv_backimg;

        public MyViewHolder(View view) {
            super(view);

            tv_companyname = view.findViewById(R.id.tv_companyname);
            tv_name = view.findViewById(R.id.tv_name);
            tv_loaction = view.findViewById(R.id.tv_loaction);
            iv_backimg = view.findViewById(R.id.iv_backimg);

        }
    }


}





