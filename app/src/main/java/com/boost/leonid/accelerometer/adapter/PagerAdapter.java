package com.boost.leonid.accelerometer.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import com.boost.leonid.accelerometer.ui.fragment.GraphFragment;
import com.boost.leonid.accelerometer.ui.fragment.ListDatesFragment;

public class PagerAdapter extends FragmentPagerAdapter {
    private final Fragment[] mFragments = new Fragment[]{
            new ListDatesFragment(),
            new GraphFragment()
    };
    private final String[] mFragmentTitles;


    public PagerAdapter(FragmentManager fm, String[] stringArray) {
        super(fm);
        mFragmentTitles = stringArray;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitles[position];
    }
}
