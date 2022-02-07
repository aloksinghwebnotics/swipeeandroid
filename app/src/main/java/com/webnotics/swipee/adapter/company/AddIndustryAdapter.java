package com.webnotics.swipee.adapter.company;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webnotics.swipee.R;
import com.webnotics.swipee.activity.company.AddIndustryActivity;
import com.webnotics.swipee.model.company.CommonModel;

import java.util.ArrayList;


public class AddIndustryAdapter extends RecyclerView.Adapter<AddIndustryAdapter.MyViewHolder>  implements Filterable {

    AddIndustryActivity mContext;
    ArrayList<CommonModel> data;
    ArrayList<CommonModel> data1;
    private MyViewHolder oldHolder;
    CustomFilter filter;

    public AddIndustryAdapter(AddIndustryActivity mContext, ArrayList<CommonModel> data) {

        // TODO Auto-generated constructor stub

        this.mContext = mContext;
        this.data = data;
        this.data1 = data;


    }


    @NonNull
    @Override
    public AddIndustryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_add_industry, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddIndustryAdapter.MyViewHolder holder, int position) {
        holder.tv_item.setText(data.get(position).getName());
        holder.tv_state.setText(data.get(position).getLevel());
        if (TextUtils.isEmpty(data.get(position).getLevel())){
            holder.tv_state.setVisibility(View.GONE);
        }else holder.tv_state.setVisibility(View.VISIBLE);
        String id = data.get(position).getId();
        String name = data.get(position).getName();
        holder.radioButton.setClickable(false);
        holder.radioButton.setChecked(data.get(position).isSelected());
        if (data.get(position).isSelected()){
            holder.radioButton.setChecked(true);
            oldHolder = holder;
            mContext.id = id;
            mContext.name = name;
        }
        holder.itemView.setOnClickListener(v -> {
            if (oldHolder != null) {
                oldHolder.radioButton.setChecked(false);
            }
            holder.radioButton.setChecked(true);
            oldHolder = holder;
            mContext.id = id;
            mContext.name = name;
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

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new CustomFilter();
        }

        return filter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_item,tv_state;
        RadioButton radioButton;

        public MyViewHolder(View view) {
            super(view);

            tv_item = view.findViewById(R.id.tv_item);
            radioButton = view.findViewById(R.id.radioButton);
            tv_state = view.findViewById(R.id.tv_state);

        }
    }

    class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            // TODO Auto-generated method stub

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                //CONSTARINT TO UPPER
                constraint = constraint.toString().toUpperCase();
                ArrayList<CommonModel> filters = new ArrayList<CommonModel>();
                //get specific items
                for (int i = 0; i < data1.size(); i++) {
                    if (data1.get(i).getName().toUpperCase().contains(constraint) || data1.get(i).getLevel().toUpperCase().contains(constraint)) {
                        CommonModel p = new CommonModel();
                        p.setName(data1.get(i).getName());
                        p.setSelected(data1.get(i).isSelected());
                        p.setId(data1.get(i).getId());
                        p.setLevel(data1.get(i).getLevel());
                        filters.add(p);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            } else {
                results.count = data1.size();
                results.values = data1;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            // TODO Auto-generated method stub
            data = (ArrayList<CommonModel>) results.values;
            notifyDataSetChanged();
        }

    }
}





