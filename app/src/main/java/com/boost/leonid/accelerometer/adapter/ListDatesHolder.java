package com.boost.leonid.accelerometer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.boost.leonid.accelerometer.R;
import com.boost.leonid.accelerometer.model.Coordinates;


public class ListDatesHolder extends RecyclerView.ViewHolder {
    private TextView mDate;
    private TextView mTime;

    public ListDatesHolder(View itemView) {
        super(itemView);

        mDate = (TextView) itemView.findViewById(R.id.list_date);
        mTime = (TextView) itemView.findViewById(R.id.list_time);
    }
    public void bind(Coordinates coordinates){
        mDate.setText(coordinates.getDate());
        mTime.setText(coordinates.getTime());
    }
}
