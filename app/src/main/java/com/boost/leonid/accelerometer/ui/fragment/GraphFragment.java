package com.boost.leonid.accelerometer.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boost.leonid.accelerometer.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GraphFragment extends Fragment {
    private static final String TAG = "GraphFragment";

    @BindView(R.id.line_graph)
    LineChart mLineChart;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        ButterKnife.bind(this, view);

        if (getArguments().size() != 0){
            initGraph();
            for (float x : getArguments().getFloatArray("y")){
                Log.d(TAG, String.valueOf(x));
            }
        }

        return view;
    }

    private void initGraph() {
        List<Entry> list = getAxis(getArguments().getFloatArray("y"));
        LineDataSet dataSet = new LineDataSet(list, "Label");
        dataSet.setColor(Color.RED);
        LineData lineData = new LineData(dataSet);
        mLineChart.setData(lineData);
        mLineChart.invalidate();
    }

    private List<Entry> getAxis(float[] axis){
        List<Entry> list = new ArrayList<>();

        int sizeList = axis.length;
        int time = sizeList * 5; // TODO 5 take from settings

        int timeArr[] = new int[sizeList];
        timeArr[0] = 0;
        for (int i = 1; i < timeArr.length; i++){
            timeArr[i] = i * 5;
        }

        for (int i = 0; i < sizeList; i++){
            list.add(new Entry(timeArr[i], axis[i]));
        }

        return list;
    }
}
