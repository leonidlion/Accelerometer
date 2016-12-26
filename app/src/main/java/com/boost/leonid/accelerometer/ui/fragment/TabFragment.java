package com.boost.leonid.accelerometer.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boost.leonid.accelerometer.R;
import com.boost.leonid.accelerometer.adapter.PagerAdapter;
import com.boost.leonid.accelerometer.model.AccelerometerData;
import com.boost.leonid.accelerometer.model.HistoryItem;

import java.util.List;

public class TabFragment extends BaseFragment {
    private static final String TAG = "TabFragment";
    private static final int LAYOUT = R.layout.fragment_tab;
    private PagerAdapter mPagerAdapter;
    private ViewPager mViewPager;
    private Bundle mBundle = new Bundle();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);

        mPagerAdapter = new PagerAdapter(getChildFragmentManager(), getResources().getStringArray(R.array.tab_titles), mBundle);
        mViewPager = (ViewPager) view.findViewById(R.id.main_pager);
        mViewPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        return view;
    }

/*    @Override
    public void onItemClick(HistoryItem refKey) {
        Log.d(TAG, "onItemClick");
*//*
        mBundle.clear();
        mBundle.putFloatArray(GraphFragment.BUNDLE_X, listToArray(model.getX()));
        mBundle.putFloatArray(GraphFragment.BUNDLE_Y, listToArray(model.getY()));
        mBundle.putFloatArray(GraphFragment.BUNDLE_Z, listToArray(model.getZ()));
        mBundle.putInt(GraphFragment.BUNDLE_INTERVAL, model.getInterval());

        mPagerAdapter.updateGraph(mBundle);
        mPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(TAB_GRAPH, true);*//*
    }*/

    private float[] listToArray(List<Float> list){
        float[] coordinates = new float[list.size()];
        for (int i = 0; i < coordinates.length; i++){
            coordinates[i] = list.get(i);
        }
        return coordinates;
    }

    public static Fragment newInstance(List<AccelerometerData> model) {
        TabFragment fragment = new TabFragment();

        return fragment;
    }
}
