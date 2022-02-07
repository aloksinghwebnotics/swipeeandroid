package com.webnotics.swipee.adapter.seeeker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.webnotics.swipee.R;
import com.webnotics.swipee.activity.Seeker.SearchIndustryActivity;

import java.util.ArrayList;

public class SearchIndustryAdapter extends BaseAdapter implements Filterable {

    SearchIndustryActivity mContext;
    ArrayList<JsonObject> mArrayListSkills;
    CustomFilter filter;
    ArrayList<JsonObject> filterList;


    public SearchIndustryAdapter(SearchIndustryActivity mContext, ArrayList<JsonObject> mArrayListSkills) {

        // TODO Auto-generated constructor stub

        this.mContext = mContext;
        this.mArrayListSkills = mArrayListSkills;
        this.filterList = mArrayListSkills;
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
        convertView = inflater.inflate(R.layout.add_college_child, null);
        TextView nameTxt = (TextView) convertView.findViewById(R.id.tv_countryname);
        //SET DATA TO THEM
        nameTxt.setText(mArrayListSkills.get(pos).get("industry_name").getAsString());


        convertView.setOnClickListener(view -> {
            mContext.selectedData(mArrayListSkills.get(pos).get("industry_name").getAsString(), mArrayListSkills.get(pos).get("industry_id").getAsString());
        });
        return convertView;
    }

    @Override
    public Filter getFilter() {
        // TODO Auto-generated method stub
        if (filter == null) {
            filter = new CustomFilter();
        }

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
                ArrayList<JsonObject> filters = new ArrayList<JsonObject>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++) {
                    if (filterList.get(i).get("industry_name").getAsString().toUpperCase().contains(constraint)) {
                        JsonObject p = new JsonObject();
                        p.add("industry_id", filterList.get(i).get("industry_id"));
                        p.add("industry_name", filterList.get(i).get("industry_name"));
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
            mArrayListSkills = (ArrayList<JsonObject>) results.values;
            notifyDataSetChanged();
        }

    }
}
