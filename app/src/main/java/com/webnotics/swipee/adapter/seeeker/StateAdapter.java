package com.webnotics.swipee.adapter.seeeker;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webnotics.swipee.CustomUi.PopinsRegularTextView;
import com.webnotics.swipee.R;
import com.webnotics.swipee.activity.Seeker.AddStateActivity;
import com.webnotics.swipee.model.seeker.StateModel;

import java.util.ArrayList;
import java.util.stream.IntStream;


public class StateAdapter extends RecyclerView.Adapter<StateAdapter.MyViewHolder> implements Filterable {

    AddStateActivity mContext;
    ArrayList<StateModel.Data> data;
    ArrayList<StateModel.Data> filterList;
    private MyViewHolder oldHolder;
    CustomFilter filter;


    public StateAdapter(AddStateActivity mContext, ArrayList<StateModel.Data> data) {

        // TODO Auto-generated constructor stub

        this.mContext = mContext;
        this.data = data;
        this.filterList = data;


    }


    @NonNull
    @Override
    public StateAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_state, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StateAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.tv_item.setText(data.get(position).getState_name());
        String stateId = data.get(position).getState_id();
        String stateName = data.get(position).getState_name();
        holder.radioButton.setClickable(false);
        if (data.get(position).isSelected()){
            holder.radioButton.setChecked(true);
            oldHolder = holder;
            mContext.stateId = stateId;
            mContext.stateName = stateName;
        }else holder.radioButton.setChecked(false);
        holder.itemView.setOnClickListener(v -> {
            if (oldHolder != null) oldHolder.radioButton.setChecked(false);
            IntStream.range(0, data.size()).forEach(i -> data.get(i).setSelected(false));
            holder.radioButton.setChecked(true);
            oldHolder = holder;
            mContext.stateId = stateId;
            mContext.stateName = stateName;
            data.get(position).setSelected(true);
            IntStream.range(0, filterList.size()).forEach(i -> {
                filterList.get(i).setSelected(false);
                if (filterList.get(i).getState_id().equalsIgnoreCase(data.get(position).getState_id()))
                    filterList.get(i).setSelected(true);
            });
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

        PopinsRegularTextView tv_item;
        RadioButton radioButton;

        public MyViewHolder(View view) {
            super(view);

            tv_item = view.findViewById(R.id.tv_item);
            radioButton = view.findViewById(R.id.radioButton);

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
                ArrayList<StateModel.Data> filters = new ArrayList<StateModel.Data>();
                //get specific items
                for (int i = 0; i < filterList.size(); i++)
                    if (filterList.get(i).getState_name().toUpperCase().contains(constraint)) {
                        StateModel.Data p = new StateModel.Data();
                        p.setState_id(filterList.get(i).getState_id());
                        p.setState_name(filterList.get(i).getState_name());
                        p.setCountry_id(filterList.get(i).getCountry_id());
                        p.setSelected(filterList.get(i).isSelected());
                        filters.add(p);
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
            data = (ArrayList<StateModel.Data>) results.values;
            notifyDataSetChanged();
        }

    }
}





