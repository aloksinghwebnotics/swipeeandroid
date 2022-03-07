package com.webnotics.swipee.adapter.company;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webnotics.swipee.R;
import com.webnotics.swipee.activity.JobDetail;
import com.webnotics.swipee.activity.company.PostedJobActivity;
import com.webnotics.swipee.model.company.PostedJobModel;
import com.webnotics.swipee.rest.ParaName;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class PostedJobAdapter extends RecyclerView.Adapter<PostedJobAdapter.MyViewHolder> {

    PostedJobActivity mContext;
    ArrayList<PostedJobModel.Data> data;
    public PostedJobAdapter(PostedJobActivity mContext, ArrayList<PostedJobModel.Data> data) {

        // TODO Auto-generated constructor stub

        this.mContext = mContext;
        this.data = data;


    }


    @NonNull
    @Override
    public PostedJobAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_posted_jobs, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostedJobAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

                  holder.tv_title.setText(data.get(position).getJob_title());
                  holder.tv_location.setText(MessageFormat.format("{0}, {1}, {2}", data.get(position).getJob_city(), data.get(position).getJob_state(), data.get(position).getJob_country()));
                  holder.tv_experience.setText(MessageFormat.format("{0} Yrs", data.get(position).getJob_experience()));
                  holder.tv_jobtype.setText(data.get(position).getJob_type());
                  holder.tv_degree.setText(data.get(position).getJob_degree());
                  holder.tv_salary.setText(MessageFormat.format("{0} {1}", data.get(position).getJob_salery(), mContext.getString(R.string.salarypermonth)));
                  if (data.get(position).getJobTypeCount()==1){
                      holder.tv_hide.setVisibility(View.GONE);
                      holder.tv_close.setVisibility(View.GONE);
                      holder.tv_jobstatus.setText("Closed");
                      holder.tv_jobstatus.setBackgroundResource(R.drawable.red_top_bottom_corner_bg);
                      holder.tv_jobstatus.setTextColor(mContext.getColor(R.color.white));

                  }else   if (data.get(position).getJobTypeCount()==2){
                      holder.tv_hide.setVisibility(View.INVISIBLE);
                      holder.tv_close.setVisibility(View.VISIBLE);
                      holder.tv_close.setText("Make Active");
                      holder.tv_jobstatus.setText("Inactive");
                      holder.tv_close.setBackgroundResource(R.drawable.primary_semiround_bg);
                      holder.tv_close.setTextColor(mContext.getColor(R.color.white));
                      holder.tv_jobstatus.setBackgroundResource(R.drawable.gray_top_bottom_corner_bg);
                      holder.tv_jobstatus.setTextColor(mContext.getColor(R.color.black));

                  }else{
                      holder.tv_hide.setVisibility(View.VISIBLE);
                      holder.tv_close.setVisibility(View.VISIBLE);
                      holder.tv_close.setText("Close Hiring");
                      holder.tv_jobstatus.setText("Active");
                      holder.tv_close.setBackgroundResource(R.drawable.primary_stroke_semiround);
                      holder.tv_close.setTextColor(mContext.getColor(R.color.colorPrimary));
                      holder.tv_jobstatus.setBackgroundResource(R.drawable.green_top_bottom_corner_bg);
                      holder.tv_jobstatus.setTextColor(mContext.getColor(R.color.white));
                  }
                      holder.tv_hide.setOnClickListener(v -> {
                          HashMap<String, String> hashMap=new HashMap<>();
                          hashMap.put(ParaName.KEY_JOBPOSTID,data.get(position).getJob_post_id());
                          hashMap.put(ParaName.KEY_JOBSTATUS,"N");
                          hashMap.put(ParaName.KEY_ISHIRINGCLOSED,"N");
                          mContext.setJob(data.get(position).getJob_post_id(),hashMap,2);
                      });

                  holder.itemView.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, JobDetail.class).putExtra("from", PostedJobActivity.class.getSimpleName()).putExtra("id",data.get(position).getJob_post_id())));
                  holder.tv_close.setOnClickListener(v -> {

                      if (data.get(position).getJobTypeCount()==2 ){
                          HashMap<String, String> hashMap=new HashMap<>();
                          hashMap.put(ParaName.KEY_JOBPOSTID,data.get(position).getJob_post_id());
                          hashMap.put(ParaName.KEY_JOBSTATUS,"Y");
                          hashMap.put(ParaName.KEY_ISHIRINGCLOSED,"N");
                          mContext.setJob(data.get(position).getJob_post_id(),hashMap,0);
                      }  else if (data.get(position).getJobTypeCount()==0){
                          Dialog progressdialog = new Dialog(mContext);
                          progressdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                          progressdialog.setContentView(R.layout.close_job_popup);
                          progressdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                          WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                          lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                          lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                          lp.gravity = Gravity.CENTER;
                          progressdialog.getWindow().setAttributes(lp);

                          RadioButton radioButton1=progressdialog.findViewById(R.id.radioButton1);
                          radioButton1.setChecked(true);
                          RadioButton radioButton2=progressdialog.findViewById(R.id.radioButton2);
                          RadioButton radioButton3=progressdialog.findViewById(R.id.radioButton3);
                          RadioButton radioButton4=progressdialog.findViewById(R.id.radioButton4);
                          RadioButton radioButton5=progressdialog.findViewById(R.id.radioButton5);

                          progressdialog.findViewById(R.id.tv_close).setOnClickListener(v13 -> {
                              String reason="";
                              if (radioButton1.isChecked()){
                                  reason=radioButton1.getText().toString();
                              }else   if (radioButton2.isChecked()){
                                  reason=radioButton2.getText().toString();
                              }else   if (radioButton3.isChecked()){
                                  reason=radioButton3.getText().toString();
                              }else   if (radioButton4.isChecked()){
                                  reason=radioButton4.getText().toString();
                              }else   if (radioButton5.isChecked()){
                                  reason=radioButton5.getText().toString();
                              }
                              HashMap<String, String> hashMap=new HashMap<>();
                              hashMap.put(ParaName.KEY_JOBPOSTID,data.get(position).getJob_post_id());
                              hashMap.put(ParaName.KEY_ISHIRINGCLOSED,"Y");
                              hashMap.put(ParaName.KEY_JOBSTATUS,"N");
                              hashMap.put(ParaName.KEY_CLOSINGREASON,reason);
                              mContext.setJob(data.get(position).getJob_post_id(),hashMap,1);
                              progressdialog.dismiss();

                          });
                          progressdialog.findViewById(R.id.tv_later).setOnClickListener(v1 -> progressdialog.dismiss());
                          progressdialog.findViewById(R.id.iv_close).setOnClickListener(v12 -> progressdialog.dismiss());
                          try {
                              progressdialog.show();
                          }catch (Exception ignored){}
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

          TextView tv_jobstatus,tv_title,tv_location,tv_experience,tv_jobtype,tv_degree,tv_salary,tv_hide,tv_close;

        public MyViewHolder(View view) {
            super(view);

            tv_jobstatus=view.findViewById(R.id.tv_jobstatus);
            tv_title=view.findViewById(R.id.tv_title);
            tv_location=view.findViewById(R.id.tv_location);
            tv_experience=view.findViewById(R.id.tv_experience);
            tv_jobtype=view.findViewById(R.id.tv_jobtype);
            tv_degree=view.findViewById(R.id.tv_degree);
            tv_salary=view.findViewById(R.id.tv_salary);
            tv_hide=view.findViewById(R.id.tv_hide);
            tv_close=view.findViewById(R.id.tv_close);


        }
    }


}





