package com.webnotics.swipee.adapter.seeeker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webnotics.swipee.CustomUi.PopinsRegularTextView;
import com.webnotics.swipee.R;
import com.webnotics.swipee.activity.Seeker.WorkExperience;
import com.webnotics.swipee.model.seeker.JobTitleModel;

import java.util.ArrayList;


public class JobTitleAdapter extends RecyclerView.Adapter<JobTitleAdapter.MyViewHolder> {

    WorkExperience mContext;
    ArrayList<JobTitleModel> data;
    private MyViewHolder oldHolder;


    public JobTitleAdapter(WorkExperience mContext, ArrayList<JobTitleModel> data) {

        // TODO Auto-generated constructor stub

        this.mContext = mContext;
        this.data = data;


    }


    @NonNull
    @Override
    public JobTitleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_state, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull JobTitleAdapter.MyViewHolder holder, int position) {
        holder.tv_item.setText(data.get(position).getName());
        String id = data.get(position).getId();
        String name = data.get(position).getName();
        holder.radioButton.setClickable(false);
        if(data.get(position).isSelected()){
            holder.radioButton.setChecked(true);
            oldHolder = holder;
            mContext.jobId = id;
            mContext.jobName = name;
        }else holder.radioButton.setChecked(false);
        holder.itemView.setOnClickListener(v -> {
            if (oldHolder != null) oldHolder.radioButton.setChecked(false);
            holder.radioButton.setChecked(true);
            oldHolder = holder;
            mContext.jobId = id;
            mContext.jobName = name;
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

        PopinsRegularTextView tv_item;
        RadioButton radioButton;

        public MyViewHolder(View view) {
            super(view);

            tv_item = view.findViewById(R.id.tv_item);
            radioButton = view.findViewById(R.id.radioButton);

        }
    }


}





