package com.boost.leonid.accelerometer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.boost.leonid.accelerometer.R;
import com.boost.leonid.accelerometer.adapter.PagerAdapter;
import com.boost.leonid.accelerometer.model.Coordinates;
import com.boost.leonid.accelerometer.service.AccService;
import com.boost.leonid.accelerometer.ui.fragment.ListDatesFragment;
import com.boost.leonid.accelerometer.ui.settings.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements ListDatesFragment.ClickItemListener{
    private static final String TAG = "MainActivity";

    private PagerAdapter mPagerAdapter;
    private static final int TAB_GRAPH    = 1;
    private Bundle mBundle = new Bundle();
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

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.tab_titles), mBundle);
        mViewPager = (ViewPager) findViewById(R.id.main_pager);
        mViewPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
    }

    @Override
    public void onItemClick(Coordinates model) {
        Log.d(TAG, "onItemClick");

        mBundle.clear();
        mBundle.putFloatArray("x", listToArray(model.getX()));
        mBundle.putFloatArray("y", listToArray(model.getY()));
        mBundle.putFloatArray("z", listToArray(model.getZ()));
        mBundle.putInt("interval", model.getInterval());

        mPagerAdapter.updateGraph(mBundle);
        mPagerAdapter.notifyDataSetChanged();
        mViewPager.setCurrentItem(TAB_GRAPH, true);
    }

    private float[] listToArray(List<Float> list){
        float[] coordinates = new float[list.size()];
        for (int i = 0; i < coordinates.length; i++){
            coordinates[i] = list.get(i);
        }
        return coordinates;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_start:
                if (!AccService.isServiceAlarmOn(this)){
                    AccService.setServiceAlarm(this, true);
                    Log.d(TAG, "started");
                }
                break;
            case R.id.menu_stop:
                if (AccService.isServiceAlarmOn(this)){
                    AccService.setServiceAlarm(this, false);
                    Log.d(TAG, "stopped");
                }
                break;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
