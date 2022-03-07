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
import com.webnotics.swipee.activity.company.CompanyNearBy;
import com.webnotics.swipee.activity.company.UserDetail;
import com.webnotics.swipee.model.company.CompanyRaderViewModel;

import java.text.MessageFormat;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {

    Context mContext;
    ArrayList<CompanyRaderViewModel.Data.User_Listing> data;


    public UserListAdapter(Context mContext, ArrayList<CompanyRaderViewModel.Data.User_Listing> data) {

        // TODO Auto-generated constructor stub

        this.mContext = mContext;
        this.data = data;


    }


    @NonNull
    @Override
    public UserListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_companylist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_name.setText(MessageFormat.format("{0} {1}", data.get(position).getFirst_name(), data.get(position).getLast_name()));
        holder.tv_text.setText(MessageFormat.format("{0}, {1}, {2}", data.get(position).getCity(), data.get(position).getState(), data.get(position).getCountry()));
        Glide.with(mContext).load(data.get(position).getUser_profile()).
                error(R.drawable.ic_profile_select).placeholder(R.drawable.ic_profile_select).into(holder.civ_logo);

        holder.itemView.setOnClickListener(v -> {
            String from= CompanyNearBy.class.getSimpleName();
            mContext.startActivity(new Intent(mContext, UserDetail.class).putExtra("from",from).putExtra("name",data.get(position).getFirst_name()).
                    putExtra("id",data.get(position).getUser_id()).putExtra("job_id",data.get(position).getJob_id()));            });
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

        TextView tv_name, tv_text;
        CircleImageView civ_logo;

        public MyViewHolder(View view) {
            super(view);

            tv_name = view.findViewById(R.id.tv_name);
            tv_text = view.findViewById(R.id.tv_text);
            civ_logo = view.findViewById(R.id.civ_logo);

        }
    }


}





