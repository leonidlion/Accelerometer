package com.boost.leonid.accelerometer.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.boost.leonid.accelerometer.R;
import com.boost.leonid.accelerometer.adapter.PagerAdapter;
import com.boost.leonid.accelerometer.model.Coordinates;
import com.boost.leonid.accelerometer.ui.fragment.ListDatesFragment;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements ListDatesFragment.ClickItemListener{
    private static final String TAG = "MainActivity";

    private FirebaseDatabase mDatabase;
    private FragmentPagerAdapter mPagerAdapter;
    private static final int TAB_LIST_DATES = 0;
    private static final int TAB_GRAPH      = 1;

    /**
     * Init views
     */
    @BindView(R.id.main_pager)
    ViewPager mViewPager;
    @BindView(R.id.toolbar_actionbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        mDatabase = FirebaseDatabase.getInstance();
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.tab_titles));
        mViewPager = (ViewPager) findViewById(R.id.main_pager);
        mViewPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }

    public void writeTestDataAcc(View view) {
        List<Double> testList = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            testList.add(new Random().nextDouble());
        }
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String formatDate = sdf.format(date);
        sdf = new SimpleDateFormat("hh:mm");
        String formatTime = sdf.format(date);

        String randomAccKey = mDatabase.getReference().child("users").child("acc_data").push().getKey();
        Coordinates coordinates = new Coordinates(formatDate, formatTime, testList, testList, testList, getDeviceModel());

        Map<String, Object> mapToInsert = new HashMap<>();
        mapToInsert.put("/users/" + getUid() + "/acc_data/"  + randomAccKey, coordinates.toMap());

        mDatabase.getReference().updateChildren(mapToInsert);

    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
    }

    @Override
    public void onItemClick(String refKey) {
        mViewPager.setCurrentItem(TAB_GRAPH, true);
        Log.d(TAG, refKey);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_logout:
                break;
            case R.id.menu_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
