package com.webnotics.swipee.adapter.seeeker;

import android.annotation.SuppressLint;
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
import com.webnotics.swipee.activity.Seeker.AddEducation;
import com.webnotics.swipee.fragments.seeker.ProfileFragments;
import com.webnotics.swipee.model.seeker.EmployeeUserDetails;

import java.text.MessageFormat;
import java.util.ArrayList;

public class UserEducationAdapter extends BaseAdapter {

    Activity mContext;

    ArrayList<EmployeeUserDetails.Data.User_Eductaion> mUserEduArrayList;

    public UserEducationAdapter(Context mContext, ArrayList<EmployeeUserDetails.Data.User_Eductaion> mUserEduArrayList) {

        // TODO Auto-generated constructor stub

        this.mContext = (Activity) mContext;
        this.mUserEduArrayList = mUserEduArrayList;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mUserEduArrayList.size();
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return mUserEduArrayList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return mUserEduArrayList.indexOf(getItem(pos));
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = mContext.getLayoutInflater();
        convertView = inflater.inflate(R.layout.experiencerowitems, null);
        ImageView iv_edit = convertView.findViewById(R.id.iv_edit);
        ImageView iv_delete = convertView.findViewById(R.id.iv_delete);
        TextView mtxtnmae = convertView.findViewById(R.id.cmpnyname);
        TextView mtxtyear = convertView.findViewById(R.id.year);
        TextView mtxtdesn = convertView.findViewById(R.id.desn);
        TextView descer = convertView.findViewById(R.id.descer);
        RelativeLayout rellay = convertView.findViewById(R.id.rellay);
        mtxtnmae.setText(mUserEduArrayList.get(pos).getCollege_university_name());
        mtxtdesn.setText(mUserEduArrayList.get(pos).getDegree_name());
        descer.setText("");
        descer.setVisibility(View.GONE);
        if (mUserEduArrayList.size()>1){
            iv_delete.setVisibility(View.VISIBLE);
        }else iv_delete.setVisibility(View.GONE);

        if (mUserEduArrayList.get(pos).getCurrently_pursuing().equalsIgnoreCase("N")) {
            mtxtyear.setText(MessageFormat.format("{0} - {1}", mUserEduArrayList.get(pos).getFrom(), mUserEduArrayList.get(pos).getTo()));

        } else {
            mtxtyear.setText(MessageFormat.format("{0}{1}", mUserEduArrayList.get(pos).getFrom(), mContext.getString(R.string.present)));
        }
        iv_edit.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           mContext.startActivity(new Intent(mContext, AddEducation.class).putExtra("user_education_id",mUserEduArrayList.get(pos).getUser_education_id())
                                                   .putExtra("college_university_name",mUserEduArrayList.get(pos).getCollege_university_name())
                                                   .putExtra("college_university_id",mUserEduArrayList.get(pos).getCollege_university_id())
                                                   .putExtra("degree_level_id",mUserEduArrayList.get(pos).getDegree_level_id())
                                                   .putExtra("start_date",mUserEduArrayList.get(pos).getStart_date())
                                                   .putExtra("end_date",mUserEduArrayList.get(pos).getEnd_date())
                                                   .putExtra("degree_level",mUserEduArrayList.get(pos).getDegree_level())
                                                   .putExtra("degree_type_id",mUserEduArrayList.get(pos).getDegree_type_id())
                                                   .putExtra("degree_name",mUserEduArrayList.get(pos).getDegree_name())
                                                   .putExtra("currently_pursuing",mUserEduArrayList.get(pos).getCurrently_pursuing())
                                                   .putExtra("edit",true)

                                           );
                                       }


                                   });

        iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        if (ProfileFragments.instance!=null)
                            ProfileFragments.instance.deleteEducation(mUserEduArrayList.get(pos).getUser_education_id());
            }
        });
       /* rellay.setOnClickListener(view -> {
            Intent intent  =   new Intent(mContext, AddEducation.class);
            intent.putExtra("value", "update");
            intent.putParcelableArrayListExtra("data", mUserEduArrayList);
            intent.putExtra("pos", pos);
            mContext.startActivity(intent);
        });*/
        return convertView;
    }


}

