package com.webnotics.swipee.adapter.seeeker;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.webnotics.swipee.R;
import com.webnotics.swipee.fragments.seeker.PlansFragments;

import java.text.MessageFormat;


public class PlanAdapter extends RecyclerView.Adapter<PlanAdapter.MyViewHolder> {

    PlansFragments mContext;
    JsonArray data;
    private MyViewHolder oldHolder;


    public PlanAdapter(PlansFragments mContext, JsonArray data) {

        // TODO Auto-generated constructor stub

        this.mContext = mContext;
        this.data = data;


    }


    @NonNull
    @Override
    public PlanAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_plan_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlanAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_name.setText(data.get(position).getAsJsonObject().get("package_name").getAsString());
        holder.tv_amount.setText(MessageFormat.format("Rs {0}", data.get(position).getAsJsonObject().get("package_price").getAsInt()));
        holder.tv_plandetail.setText(Html.fromHtml(data.get(position).getAsJsonObject().get("package_description").getAsString()));
      // String period=data.get(position).getAsJsonObject().get("package_name").getAsString();
        holder.rl_planDetail.setBackgroundResource(R.drawable.gray_stroke_round_bk);
        if (position==0){
            oldHolder=holder;
            holder.rb_plan.setChecked(true);
            holder.rl_planDetail.setBackgroundResource(R.drawable.primary_stroke_round);
            int is_purchase=data.get(position).getAsJsonObject().get("is_purchase").getAsInt();
            int package_price=data.get(position).getAsJsonObject().get("package_price").getAsInt();
            String package_id=data.get(position).getAsJsonObject().get("package_id").getAsString();
            String package_name=data.get(position).getAsJsonObject().get("package_name").getAsString();
            String package_type=data.get(position).getAsJsonObject().get("package_type").getAsString();
            String post_limit=data.get(position).getAsJsonObject().get("post_limit").getAsString();
            mContext.setPlan(is_purchase,package_price,package_id,package_name,package_type,post_limit);
        }
        holder.rb_plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldHolder.rb_plan.setChecked(false);
                oldHolder.rl_planDetail.setBackgroundResource(R.drawable.gray_stroke_round_bk);
                oldHolder=holder;
                holder.rb_plan.setChecked(true);
                holder.rl_planDetail.setBackgroundResource(R.drawable.primary_stroke_round);
                int is_purchase=data.get(position).getAsJsonObject().get("is_purchase").getAsInt();
                int package_price=data.get(position).getAsJsonObject().get("package_price").getAsInt();
                String package_id=data.get(position).getAsJsonObject().get("package_id").getAsString();
                String package_name=data.get(position).getAsJsonObject().get("package_name").getAsString();
                String package_type=data.get(position).getAsJsonObject().get("package_type").getAsString();
                String post_limit=data.get(position).getAsJsonObject().get("post_limit").getAsString();
                mContext.setPlan(is_purchase,package_price,package_id,package_name,package_type,post_limit);


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

        TextView tv_name,tv_plandetail,tv_amount,tv_duration;
        RadioButton rb_plan;
        RelativeLayout rl_planDetail;

        public MyViewHolder(View view) {
            super(view);

            tv_name = view.findViewById(R.id.tv_name);
            tv_plandetail = view.findViewById(R.id.tv_plandetail);
            tv_amount = view.findViewById(R.id.tv_amount);
            tv_duration = view.findViewById(R.id.tv_duration);
            rb_plan = view.findViewById(R.id.rb_plan);
            rl_planDetail = view.findViewById(R.id.rl_planDetail);

        }
    }


}





