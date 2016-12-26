package com.boost.leonid.accelerometer.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.boost.leonid.accelerometer.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GraphFragment extends Fragment {
    private static final String TAG = "GraphFragment";
    private static final int X_AXIS = 0;
    private static final int Y_AXIS = 1;
    private static final int Z_AXIS = 2;

    private int mInterval = 1000;

    private List<Entry> mEntryListX;
    private List<Entry> mEntryListY;
    private List<Entry> mEntryListZ;
    private LineData mLineData = new LineData();

    public static final String BUNDLE_X = "x";
    public static final String BUNDLE_Y = "y";
    public static final String BUNDLE_Z = "z";
    public static final String BUNDLE_INTERVAL = "interval";

    @BindView(R.id.line_graph)
    LineChart mChart;
    @BindView(R.id.check_x)
    CheckBox mCheckBoxX;
    @BindView(R.id.check_y)
    CheckBox mCheckBoxY;
    @BindView(R.id.check_z)
    CheckBox mCheckBoxZ;

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

        mCheckBoxX.setVisibility(View.INVISIBLE);
        mCheckBoxY.setVisibility(View.INVISIBLE);
        mCheckBoxZ.setVisibility(View.INVISIBLE);

        if (getArguments().size() != 0){
            initChart();
            initAxisDataEntry();
            initCheckListener();
        } else {
            mChart.setNoDataText(getString(R.string.graph_title_data_empty));
        }

        return view;
    }

    private void initChart() {
        mChart.setNoDataText(getString(R.string.graph_title_choice_axis));
        mChart.getDescription().setEnabled(false);
        mChart.getAxisRight().setEnabled(false);

        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.getXAxis().setDrawGridLines(true);

        mChart.getAxisLeft().setDrawZeroLine(true);
        mChart.getAxisLeft().setZeroLineColor(Color.BLACK);
        mChart.getAxisLeft().setDrawGridLines(true);

        Legend l = mChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
    }

    private void initAxisDataEntry() {
        mInterval = getArguments().getInt(BUNDLE_INTERVAL);
        mEntryListX = floatArrayToListEntry(getArguments().getFloatArray(BUNDLE_X));
        mEntryListY = floatArrayToListEntry(getArguments().getFloatArray(BUNDLE_Y));
        mEntryListZ = floatArrayToListEntry(getArguments().getFloatArray(BUNDLE_Z));
        Log.d(TAG, String.valueOf(mInterval));
    }

    private void initCheckListener() {
        mCheckBoxX.setVisibility(View.VISIBLE);
        mCheckBoxY.setVisibility(View.VISIBLE);
        mCheckBoxZ.setVisibility(View.VISIBLE);
        mLineData.clearValues();
        mCheckBoxX.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            LineDataSet dataX = getDataSet(mEntryListX, X_AXIS, Color.RED);
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Log.d(TAG, "x check");
                    // first time action
                    mLineData.addDataSet(dataX);
                    mChart.setData(mLineData);
                    mChart.animateX(1000);
                    mChart.invalidate();
                }else {
                    Log.d(TAG, "x un_check");
                    mLineData.removeDataSet(dataX);
                    mChart.notifyDataSetChanged();
                    mChart.invalidate();
                }
            }
        });
        mCheckBoxY.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            LineDataSet data = getDataSet(mEntryListY, Y_AXIS, Color.GREEN);
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // second time action
                if (b){
                    Log.d(TAG, "y check");
                    mLineData.addDataSet(data);
                    mChart.setData(mLineData);
                    mChart.animateX(1000);
                    mChart.invalidate();
                }else {
                    Log.d(TAG, "y un_check");
                    mLineData.removeDataSet(data);
                    mChart.notifyDataSetChanged();
                    mChart.invalidate();
                }
            }
        });
        mCheckBoxZ.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            LineDataSet data = getDataSet(mEntryListZ, Z_AXIS, Color.BLUE);
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                // third time action
                if (b){
                    Log.d(TAG, "z check");
                    mLineData.addDataSet(data);
                    mChart.setData(mLineData);
                    mChart.animateX(1000);
                    mChart.invalidate();
                }else {
                    Log.d(TAG, "z un_check");
                    mLineData.removeDataSet(data);
                    mChart.notifyDataSetChanged();
                    mChart.invalidate();
                }
            }
        });
    }

    private LineDataSet getDataSet(List<Entry> listAxisData, int axis, int color){
        LineDataSet dataSet;
        switch (axis){
            case X_AXIS:
                dataSet = new LineDataSet(listAxisData, getString(R.string.check_x));
                break;
            case Y_AXIS:
                dataSet = new LineDataSet(listAxisData, getString(R.string.check_y));
                break;
            case Z_AXIS:
                dataSet = new LineDataSet(listAxisData, getString(R.string.check_z));
                break;
            default: return null;
        }
        dataSet.setColor(color);
        dataSet.setCircleColor(color);
        dataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        return dataSet;
    }

    private List<Entry> floatArrayToListEntry(float[] axis){
        List<Entry> list = new ArrayList<>();

        int sizeTimeList = axis.length;
        int timeArr[] = new int[sizeTimeList];
        timeArr[0] = 0;
        for (int i = 1; i < timeArr.length; i++){
            timeArr[i] = i * mInterval;
        }

        for (int i = 0; i < sizeTimeList; i++){
            list.add(new Entry(timeArr[i], axis[i]));
        }

        return list;
    }
}
