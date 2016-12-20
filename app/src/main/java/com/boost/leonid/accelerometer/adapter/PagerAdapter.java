package com.boost.leonid.accelerometer.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.boost.leonid.accelerometer.ui.fragment.GraphFragment;
import com.boost.leonid.accelerometer.ui.fragment.ListDatesFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    private static final String TAG         = "PagerAdapter";
    private static final int FRAGMENT_LIST  = 0;
    private static final int FRAGMENT_GRAPH = 1;

    private Bundle mBundle;

    private final String[] mFragmentTitles;

    public PagerAdapter(FragmentManager fm, String[] titles, Bundle args){
        super(fm);
        Log.d("ADAPTER", "CREATE");
        mFragmentTitles = titles;
        mBundle = args;
    }

    public void updateGraph(Bundle args){
        Log.d("ADAPTER", "updateGraph");
        mBundle = args;
        getItem(FRAGMENT_GRAPH);
    }
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Fragment getItem(int position) {
        Log.d("ADAPTER", "getItem");
        Fragment fragment = null;
        switch (position){
            case FRAGMENT_LIST:
                Log.d(TAG, "fragment list");
                fragment = new ListDatesFragment();
                break;
            case FRAGMENT_GRAPH:
                Log.d(TAG, "graph");
                fragment = new GraphFragment();
                fragment.setArguments(mBundle);
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return mFragmentTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles[position];
    }
}
