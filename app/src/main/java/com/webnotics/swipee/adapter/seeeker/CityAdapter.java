package com.webnotics.swipee.adapter.seeeker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.webnotics.swipee.CustomUi.PopinsRegularTextView;
import com.webnotics.swipee.R;
import com.webnotics.swipee.activity.Seeker.AddCityActivity;
import com.webnotics.swipee.model.seeker.CityModel;

import java.util.ArrayList;


public class CityAdapter extends RecyclerView.Adapter<CityAdapter.MyViewHolder> {

    AddCityActivity mContext;
    ArrayList<CityModel.Data> data;
    private MyViewHolder oldHolder;


    public CityAdapter(AddCityActivity mContext, ArrayList<CityModel.Data> data) {

        // TODO Auto-generated constructor stub

        this.mContext = mContext;
        this.data = data;


    }


    @NonNull
    @Override
    public CityAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.child_state, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.MyViewHolder holder, int position) {
        holder.tv_item.setText(data.get(position).getCity_name());
        String cityId = data.get(position).getCity_id();
        String cityName = data.get(position).getCity_name();
        holder.radioButton.setClickable(false);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (oldHolder != null) {
                    oldHolder.radioButton.setChecked(false);
                }
                holder.radioButton.setChecked(true);
                oldHolder = holder;
                mContext.cityId = cityId;
                mContext.cityName = cityName;
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





