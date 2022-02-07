package com.webnotics.swipee.adapter.seeeker;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.webnotics.swipee.R;
import com.webnotics.swipee.model.seeker.LangModel;

import java.util.ArrayList;


public class LangAdapter extends BaseAdapter {

    Activity mContext;
    ArrayList<LangModel> languageArraylist;

    public LangAdapter(Context mContext, ArrayList<LangModel> languageArraylist) {

        // TODO Auto-generated constructor stub

        this.mContext = (Activity) mContext;
        this.languageArraylist = languageArraylist;

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return languageArraylist.size();
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return languageArraylist.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return languageArraylist.indexOf(getItem(pos));
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = mContext.getLayoutInflater();
        convertView = inflater.inflate(R.layout.addbenefitsrowitems, null);
        TextView nameTxt = convertView.findViewById(R.id.tv_countryname);
        CheckBox mCheckImgView = convertView.findViewById(R.id.chkbox);
        //SET DATA TO THEM
        nameTxt.setText(languageArraylist.get(pos).getName());
        mCheckImgView.setChecked(languageArraylist.get(pos).isSelected());

        mCheckImgView.setOnClickListener(view -> {

            if (!languageArraylist.get(pos).isSelected()) {
                LangModel langmodel = new LangModel();
                langmodel.setId(languageArraylist.get(pos).getId());
                langmodel.setName(languageArraylist.get(pos).getName());
                langmodel.setSelected(true);
                languageArraylist.set(pos, langmodel);
                notifyDataSetChanged();
            } else {
                LangModel langmodel = new LangModel();
                langmodel.setId(languageArraylist.get(pos).getId());
                langmodel.setName(languageArraylist.get(pos).getName());
                langmodel.setSelected(false);
                languageArraylist.set(pos, langmodel);
                notifyDataSetChanged();
            }
        });
        nameTxt.setOnClickListener(view -> {

            if (!languageArraylist.get(pos).isSelected()) {
                LangModel langmodel = new LangModel();
                langmodel.setId(languageArraylist.get(pos).getId());
                langmodel.setName(languageArraylist.get(pos).getName());
                langmodel.setSelected(true);
                languageArraylist.set(pos, langmodel);
                notifyDataSetChanged();
            } else {
                LangModel langmodel = new LangModel();
                langmodel.setId(languageArraylist.get(pos).getId());
                langmodel.setName(languageArraylist.get(pos).getName());
                langmodel.setSelected(false);
                languageArraylist.set(pos, langmodel);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }


}




