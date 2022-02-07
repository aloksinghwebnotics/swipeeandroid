package com.webnotics.swipee.adapter.seeeker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.webnotics.swipee.CustomUi.FlowLayout;
import com.webnotics.swipee.CustomUi.PopinsRegularTextView;
import com.webnotics.swipee.R;
import com.webnotics.swipee.model.seeker.EmployeeUserDetails;

import java.text.MessageFormat;
import java.util.ArrayList;

public class UserPreferenceAdapter extends BaseAdapter {

    Activity mContext;

    ArrayList<EmployeeUserDetails.Data.User_Preferences> mUserPrefArrayList;

    public UserPreferenceAdapter(Context mContext, ArrayList<EmployeeUserDetails.Data.User_Preferences> mUserPrefArrayList) {

        // TODO Auto-generated constructor stub

        this.mContext = (Activity) mContext;
        this.mUserPrefArrayList = mUserPrefArrayList;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mUserPrefArrayList.size();
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return mUserPrefArrayList.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return mUserPrefArrayList.indexOf(getItem(pos));
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = mContext.getLayoutInflater();
        convertView = inflater.inflate(R.layout.userpreferencerowitems, null);
        FlowLayout flowlayout = convertView.findViewById(R.id.flowlayout);
        FlowLayout flowlayout1 = convertView.findViewById(R.id.flowlayout1);
        TextView slalryss = convertView.findViewById(R.id.slalryss);
        ArrayList<EmployeeUserDetails.Data.User_Preferences.Location_Data> mlocdata = mUserPrefArrayList.get(pos).getLocation_data();
        for (int i = 0; i < mlocdata.size(); i++) {
            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout linearLayoutF = new LinearLayout(mContext);
            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                    FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams layoutParamsF = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, (int) (mContext.getResources().getDisplayMetrics().density*32));
            layoutParamsF.setMargins((int) (mContext.getResources().getDisplayMetrics().density*5), 0, (int) (mContext.getResources().getDisplayMetrics().density*5), (int) (mContext.getResources().getDisplayMetrics().density*8));
            linearLayoutF.setLayoutParams(layoutParams);
            linearLayout.setPadding((int) (mContext.getResources().getDisplayMetrics().density*8), 0, (int) (mContext.getResources().getDisplayMetrics().density*8), 0);
            linearLayout.setLayoutParams(layoutParamsF);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            //linearLayout.setOnClickListener(btnClickListener);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setBackgroundResource(R.drawable.primary_semiround_bg);
            PopinsRegularTextView bt = new PopinsRegularTextView(mContext);
            bt.setText(mlocdata.get(i).getLocation_name());
            bt.setAllCaps(false);
            bt.setTextSize(12f);
            bt.setMaxLines(1);
            bt.setEllipsize(TextUtils.TruncateAt.END);
            bt.setTag(mlocdata.get(i).getLocation_name());
            bt.setTextColor(mContext.getResources().getColor(R.color.white));
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(0, 0, 0, 0);
            bt.setLayoutParams(layoutParams1);

            linearLayout.addView(bt);
            linearLayoutF.addView(linearLayout);

            //linearLayout.setOnClickListener(btnClickListener);
            linearLayoutF.setTag(mlocdata.get(i).getLocation_name());
            flowlayout.addView(linearLayoutF);

        }

        ArrayList<EmployeeUserDetails.Data.User_Preferences.Industry_Data> mInddata = mUserPrefArrayList.get(pos).getIndustry_data();
        for (int i = 0; i < mInddata.size(); i++) {
            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout linearLayoutF = new LinearLayout(mContext);
            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                    FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams layoutParamsF = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, (int) (mContext.getResources().getDisplayMetrics().density*32));
            layoutParamsF.setMargins((int) (mContext.getResources().getDisplayMetrics().density*5), 0, (int) (mContext.getResources().getDisplayMetrics().density*5), (int) (mContext.getResources().getDisplayMetrics().density*8));
            linearLayoutF.setLayoutParams(layoutParams);
            linearLayout.setLayoutParams(layoutParamsF);
            linearLayout.setPadding((int) (mContext.getResources().getDisplayMetrics().density*8), 0, (int) (mContext.getResources().getDisplayMetrics().density*8), 0);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            //linearLayout.setOnClickListener(btnClickListener);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setBackgroundResource(R.drawable.primary_semiround_bg);
            PopinsRegularTextView bt = new PopinsRegularTextView(mContext);
            bt.setText(mInddata.get(i).getIndustry_name());
            bt.setAllCaps(false);
            bt.setTextSize(12f);
            bt.setMaxLines(1);
            bt.setEllipsize(TextUtils.TruncateAt.END);
            bt.setTag(mInddata.get(i).getIndustry_name());
            bt.setTextColor(mContext.getResources().getColor(R.color.white));
            LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams1.setMargins(0, 0, 0, 0);
            bt.setLayoutParams(layoutParams1);

            linearLayout.addView(bt);
            linearLayoutF.addView(linearLayout);
            //linearLayout.setOnClickListener(btnClickListener);
            linearLayoutF.setTag(mInddata.get(i).getIndustry_name());
            flowlayout1.addView(linearLayoutF);
        }
        slalryss.setText(MessageFormat.format("{0} {1} {2}", mContext.getString(R.string.expected_salary), mUserPrefArrayList.get(pos).getExpected_salary().expected_salary_number,mContext.getString(R.string.salarypermonth)));

        return convertView;
    }


}


