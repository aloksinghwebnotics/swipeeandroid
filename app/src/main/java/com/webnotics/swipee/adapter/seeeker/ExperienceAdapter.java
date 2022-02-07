package com.webnotics.swipee.adapter.seeeker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.webnotics.swipee.R;
import com.webnotics.swipee.activity.Seeker.WorkExperience;
import com.webnotics.swipee.fragments.seeker.ProfileFragments;
import com.webnotics.swipee.model.seeker.EmployeeUserDetails;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExperienceAdapter extends BaseAdapter {

    Activity mContext;

    ArrayList<EmployeeUserDetails.Data.UserWorkExperience> mUserWOrkArrayList;

    public ExperienceAdapter(Context mContext, ArrayList<EmployeeUserDetails.Data.UserWorkExperience> mUserWOrkArrayList) {

        // TODO Auto-generated constructor stub

        this.mContext = (Activity) mContext;
        this.mUserWOrkArrayList = mUserWOrkArrayList;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mUserWOrkArrayList.size();
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return mUserWOrkArrayList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return mUserWOrkArrayList.indexOf(getItem(pos));
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = mContext.getLayoutInflater();
        convertView = inflater.inflate(R.layout.experiencerowitems, null);

        RelativeLayout rellay = convertView.findViewById(R.id.rellay);
        TextView mtxtnmae = convertView.findViewById(R.id.cmpnyname);
        TextView mtxtyear = convertView.findViewById(R.id.year);
        TextView mtxtdesn = convertView.findViewById(R.id.desn);
        TextView descer = convertView.findViewById(R.id.descer);
        ImageView iv_edit = convertView.findViewById(R.id.iv_edit);
        ImageView iv_delete = convertView.findViewById(R.id.iv_delete);
        mtxtnmae.setText(mUserWOrkArrayList.get(pos).getCompany_name());
        mtxtdesn.setText(mUserWOrkArrayList.get(pos).getExperience_title());
        //   descer.setText(mUserWOrkArrayList.get(pos).getDescription());
        descer.setVisibility(View.GONE);
        if (mUserWOrkArrayList.size()>1){
            iv_delete.setVisibility(View.VISIBLE);
        }else iv_delete.setVisibility(View.GONE);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatout = new SimpleDateFormat("MMM yyyy");
        if (mUserWOrkArrayList.get(pos).getCurrently_working().equalsIgnoreCase("N")) {
            Date date,dateend;
            try {
                date = format.parse(mUserWOrkArrayList.get(pos).getStart_date());
                dateend = format.parse(mUserWOrkArrayList.get(pos).getEnd_date());
                String date1=  formatout.format(date);
                String dateend1=  formatout.format(dateend);
                mtxtyear.setText(date1+ " - "+dateend1);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } else {
            Date date;
            try {
                date = format.parse(mUserWOrkArrayList.get(pos).getStart_date());

                String date1=  formatout.format(date);
                mtxtyear.setText(date1+ ""+mContext.getString(R.string.present));
            } catch (ParseException e) {
                e.printStackTrace();
            }

           }
        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ProfileFragments.instance!=null)
                    ProfileFragments.instance.deleteExperience(mUserWOrkArrayList.get(pos).getUser_experience_id());
            }
        });
            iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, WorkExperience.class).putExtra("user_experience_id",mUserWOrkArrayList.get(pos).getUser_experience_id())
                .putExtra("experience_title",mUserWOrkArrayList.get(pos).getExperience_title())
                        .putExtra("functional_id",mUserWOrkArrayList.get(pos).getFunctional_id())
                        .putExtra("company_name",mUserWOrkArrayList.get(pos).getCompany_name())
                        .putExtra("start_date",mUserWOrkArrayList.get(pos).getStart_date())
                        .putExtra("end_date",mUserWOrkArrayList.get(pos).getEnd_date())
                        .putExtra("currently_working",mUserWOrkArrayList.get(pos).getCurrently_working())
                        .putExtra("description",mUserWOrkArrayList.get(pos).getDescription())
                        .putExtra("edit",true)
                );
            }
        });

        return convertView;
    }


}
