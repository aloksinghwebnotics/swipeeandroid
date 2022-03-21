package com.webnotics.swipee.adapter.seeeker;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.webnotics.swipee.R;
import com.webnotics.swipee.interfaces.AddSkillInterface;
import com.webnotics.swipee.model.AddSkillsModel;

import java.util.ArrayList;

public class AddSkillAdapter extends BaseAdapter implements Filterable {

    Activity mContext;
    ArrayList<AddSkillsModel> mArrayListSkills;
    CustomFilter filter;
    ArrayList<AddSkillsModel> filterList;
    AddSkillInterface addSkillsInterface;

    public AddSkillAdapter(Context mContext, ArrayList<AddSkillsModel> mArrayListSkills, AddSkillInterface addSkillsInterface) {

        // TODO Auto-generated constructor stub

        this.mContext = (Activity) mContext;
        this.mArrayListSkills = mArrayListSkills;
        this.filterList = mArrayListSkills;
        this.addSkillsInterface = addSkillsInterface;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mArrayListSkills.size();
    }

    @Override
    public Object getItem(int pos) {
        // TODO Auto-generated method stub
        return mArrayListSkills.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        // TODO Auto-generated method stub
        return mArrayListSkills.indexOf(getItem(pos));
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = mContext.getLayoutInflater();
        convertView = inflater.inflate(R.layout.addskilssrowitens, null);
        TextView nameTxt = (TextView) convertView.findViewById(R.id.tv_countryname);
        CheckBox mCheckImgView = convertView.findViewById(R.id.chkimg);
        TextView tv_state = convertView.findViewById(R.id.tv_state);
        //SET DATA TO THEM
        mCheckImgView.setClickable(false);
        nameTxt.setText(mArrayListSkills.get(pos).getSkill_name());
        if (!TextUtils.isEmpty(mArrayListSkills.get(pos).getStatename())){
            tv_state.setVisibility(View.VISIBLE);
            tv_state.setText(mArrayListSkills.get(pos).getStatename());
        }else tv_state.setVisibility(View.GONE);
        mCheckImgView.setChecked(mArrayListSkills.get(pos).isSelected());

        convertView.setOnClickListener(view -> {

            if (!mArrayListSkills.get(pos).isSelected()) {
                AddSkillsModel model = new AddSkillsModel();
                model.setSkill_name(mArrayListSkills.get(pos).getSkill_name());
                model.setSelected(true);
                model.setSkill_id(mArrayListSkills.get(pos).getSkill_id());
                model.setStatename(mArrayListSkills.get(pos).getStatename());

                mArrayListSkills.set(pos, model);

                notifyDataSetChanged();
                for (int j = 0; j < filterList.size(); j++)
                    if (filterList.get(j).getSkill_name().equalsIgnoreCase(mArrayListSkills.get(pos).getSkill_name())) {
                        model = new AddSkillsModel();
                        model.setSkill_name(filterList.get(j).getSkill_name());
                        model.setSkill_id(filterList.get(j).getSkill_id());
                        model.setStatename(filterList.get(j).getStatename());

                        model.setSelected(true);
                        filterList.set(j, model);
                        notifyDataSetChanged();
                    }
                addSkillsInterface.skill(mArrayListSkills.get(pos).getSkill_name(), mArrayListSkills.get(pos).getSkill_id());
            }


        });
        return convertView;
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        if (filter == null) filter = new CustomFilter();

        return filter;
    }

    //INNER CLASS
    class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                //CONSTARINT TO UPPER
                constraint = constraint.toString().toUpperCase();
                ArrayList<AddSkillsModel> filters = new ArrayList<AddSkillsModel>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).getSkill_name().toUpperCase().contains(constraint) ||filterList.get(i).getStatename().toUpperCase().contains(constraint)) {
                        AddSkillsModel p = new AddSkillsModel();
                        p.setSkill_name(filterList.get(i).getSkill_name());
                        p.setSelected(filterList.get(i).isSelected());
                        p.setSkill_id(filterList.get(i).getSkill_id());
                        p.setStatename(filterList.get(i).getStatename());
                        filters.add(p);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            } else {
                results.count = filterList.size();
                results.values = filterList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // TODO Auto-generated method stub
            mArrayListSkills = (ArrayList<AddSkillsModel>) results.values;
            notifyDataSetChanged();
        }

    }
}
