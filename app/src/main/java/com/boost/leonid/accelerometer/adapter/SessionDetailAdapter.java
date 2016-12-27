package com.boost.leonid.accelerometer.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boost.leonid.accelerometer.R;
import com.boost.leonid.accelerometer.model.AccelerometerData;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class SessionDetailAdapter extends RecyclerView.Adapter<SessionDetailAdapter.SessionDetailHolder>{
    private List<AccelerometerData> mAccelerometerDataList;
    private Context mContext;

    public SessionDetailAdapter(Context context, List<AccelerometerData> list){
            mContext = context;
            mAccelerometerDataList = list;
    }
    @Override
    public SessionDetailHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.detail_list_item, parent, false);

        return new SessionDetailHolder(view);
    }

    @Override
    public int getItemCount() {
        return mAccelerometerDataList.size();
    }

    @Override
    public void onBindViewHolder(SessionDetailHolder holder, int position) {
        holder.bind(mAccelerometerDataList.get(position));
    }

    class SessionDetailHolder extends RecyclerView.ViewHolder{
        private TextView mTime;
        private TextView mXAxis;
        private TextView mYAxis;
        private TextView mZAxis;

        SessionDetailHolder(View itemView) {
            super(itemView);

            mTime = (TextView) itemView.findViewById(R.id.tv_detail_time);
            mXAxis = (TextView) itemView.findViewById(R.id.tv_detail_x);
            mYAxis = (TextView) itemView.findViewById(R.id.tv_detail_y);
            mZAxis = (TextView) itemView.findViewById(R.id.tv_detail_z);
        }

        void bind(AccelerometerData data){
            mTime.setText(getTimeFromLong(data.getUnixTime()));
            mXAxis.setText(String.format(Locale.getDefault(), "%.2f", data.getX()));
            mYAxis.setText(String.format(Locale.getDefault(), "%.2f", data.getY()));
            mZAxis.setText(String.format(Locale.getDefault(), "%.2f", data.getZ()));
        }

        private String getTimeFromLong(long time){
            Date date = new Date(time);
            Format format = new SimpleDateFormat("mm:ss", Locale.getDefault());
            return format.format(date);
        }
    }
}
