package com.webnotics.swipee.adapter.seeeker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webnotics.swipee.CustomUi.PopinsRegularTextView;
import com.webnotics.swipee.R;
import com.webnotics.swipee.activity.Seeker.AddStateActivity;
import com.webnotics.swipee.model.seeker.StateModel;

import java.util.ArrayList;


public class StateAdapter extends RecyclerView.Adapter<StateAdapter.MyViewHolder> {

    AddStateActivity mContext;
    ArrayList<StateModel.Data> data;
    private MyViewHolder oldHolder;


    public StateAdapter(AddStateActivity mContext, ArrayList<StateModel.Data> data) {

        // TODO Auto-generated constructor stub

        this.mContext = mContext;
        this.data = data;


    }


    @NonNull
    @Override
    public StateAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_state, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StateAdapter.MyViewHolder holder, int position) {
        holder.tv_item.setText(data.get(position).getState_name());
        String stateId = data.get(position).getState_id();
        String stateName = data.get(position).getState_name();
        holder.radioButton.setClickable(false);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldHolder != null) {
                    oldHolder.radioButton.setChecked(false);
                }
                holder.radioButton.setChecked(true);
                oldHolder = holder;
                mContext.stateId = stateId;
                mContext.stateName = stateName;
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        PopinsRegularTextView tv_item;
        RadioButton radioButton;

        public MyViewHolder(View view) {
            super(view);

            tv_item = view.findViewById(R.id.tv_item);
            radioButton = view.findViewById(R.id.radioButton);

        }
    }


}





