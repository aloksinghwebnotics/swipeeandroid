package com.webnotics.swipee.adapter.company;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webnotics.swipee.R;
import com.webnotics.swipee.fragments.company.CompanyMatchFragments;
import com.webnotics.swipee.model.seeker.FilterModelSelected;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.stream.IntStream;


public class CompanyFilterAdapter extends RecyclerView.Adapter<CompanyFilterAdapter.MyViewHolder> implements Filterable {

    CompanyMatchFragments mContext;
    ArrayList<FilterModelSelected> data;
    ArrayList<FilterModelSelected> data1;
    CustomFilter filter;
    private MyViewHolder oldHolder;

    public CompanyFilterAdapter(CompanyMatchFragments mContext, ArrayList<FilterModelSelected> data) {

        // TODO Auto-generated constructor stub

        this.mContext = mContext;
        this.data = data;
        this.data1 = data;

    }


    @NonNull
    @Override
    public CompanyFilterAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_filter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tv_state.setText(data.get(position).getState());
        if (data.get(position).isDistance()) {
            holder.rb_radio.setVisibility(View.VISIBLE);
            holder.chkimg.setVisibility(View.GONE);
            holder.chkimg.setClickable(false);
            holder.rb_radio.setClickable(false);
            holder.rb_radio.setChecked(data.get(position).isSelected());
            oldHolder = holder;
            holder.tv_item.setText(MessageFormat.format("Less than {0} km", data.get(position).getName()));
        } else {
            holder.tv_item.setText(data.get(position).getName());
            holder.chkimg.setClickable(false);
            holder.rb_radio.setClickable(false);
            holder.chkimg.setChecked(data.get(position).isSelected());
            holder.rb_radio.setVisibility(View.GONE);
            holder.chkimg.setVisibility(View.VISIBLE);
        }
        if (TextUtils.isEmpty(data.get(position).getState())) holder.tv_state.setVisibility(View.GONE);
        else holder.tv_state.setVisibility(View.VISIBLE);

        holder.itemView.setOnClickListener(v -> {
            if (data.get(position).isDistance()) {
                if (oldHolder != null) oldHolder.rb_radio.setChecked(false);
                oldHolder = holder;
                if (data.get(position).isSelected()) {
                    IntStream.range(0, data.size()).forEach(i -> {
                        mContext.setSelectedFilterPosition(data.get(i).getId(), false);
                        data.get(i).setSelected(false);
                        data1.get(i).setSelected(false);
                    });
                    holder.rb_radio.setChecked(false);
                    data.get(position).setSelected(false);
                    mContext.setSelectedFilterPosition(data.get(position).getId(), false);
                    IntStream.range(0, data1.size()).filter(i -> data.get(position).getId().equalsIgnoreCase(data1.get(i).getId())).findFirst().ifPresent(i -> data1.get(i).setSelected(false));
                } else {
                    IntStream.range(0, data.size()).forEach(i -> {
                        mContext.setSelectedFilterPosition(data.get(i).getId(), false);
                        data.get(i).setSelected(false);
                        data1.get(i).setSelected(false);
                    });
                    holder.rb_radio.setChecked(true);
                    data.get(position).setSelected(true);
                    mContext.setSelectedFilterPosition(data.get(position).getId(), true);
                    IntStream.range(0, data1.size()).filter(i -> data.get(position).getId().equalsIgnoreCase(data1.get(i).getId())).findFirst().ifPresent(i -> data1.get(i).setSelected(true));
                }
            } else {
                if (data.get(position).isSelected()) {
                    holder.chkimg.setChecked(false);
                    data.get(position).setSelected(false);
                    mContext.setSelectedFilterPosition(data.get(position).getId(), false);
                    IntStream.range(0, data1.size()).filter(i -> data.get(position).getId().equalsIgnoreCase(data1.get(i).getId())).findFirst().ifPresent(i -> data1.get(i).setSelected(false));
                } else {
                    holder.chkimg.setChecked(true);
                    data.get(position).setSelected(true);
                    mContext.setSelectedFilterPosition(data.get(position).getId(), true);
                    IntStream.range(0, data1.size()).filter(i -> data.get(position).getId().equalsIgnoreCase(data1.get(i).getId())).findFirst().ifPresent(i -> data1.get(i).setSelected(true));
                }
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

    @Override
    public Filter getFilter() {
        if (filter == null) filter = new CustomFilter();
        return filter;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_item, tv_state;
        CheckBox chkimg;
        RadioButton rb_radio;

        public MyViewHolder(View view) {
            super(view);

            tv_item = view.findViewById(R.id.tv_item);
            tv_state = view.findViewById(R.id.tv_state);
            chkimg = view.findViewById(R.id.chkimg);
            rb_radio = view.findViewById(R.id.rb_radio);

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
                ArrayList<FilterModelSelected> filters = new ArrayList<FilterModelSelected>();
                //get specific items
                for (int i = 0; i < data1.size(); i++)
                    if (data1.get(i).getName().toUpperCase().contains(constraint) || data1.get(i).getState().toUpperCase().contains(constraint)) {
                        FilterModelSelected p = new FilterModelSelected();
                        p.setName(data1.get(i).getName());
                        p.setSelected(data1.get(i).isSelected());
                        p.setId(data1.get(i).getId());
                        p.setState(data1.get(i).getState());
                        p.setDistance(data1.get(i).isDistance());
                        filters.add(p);
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
            data = (ArrayList<FilterModelSelected>) results.values;
            notifyDataSetChanged();
        }

    }
}






