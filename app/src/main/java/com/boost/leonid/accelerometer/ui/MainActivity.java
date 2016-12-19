package com.boost.leonid.accelerometer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.boost.leonid.accelerometer.R;
import com.boost.leonid.accelerometer.adapter.PagerAdapter;
import com.boost.leonid.accelerometer.service.AccService;
import com.boost.leonid.accelerometer.ui.fragment.ListDatesFragment;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements ListDatesFragment.ClickItemListener{
    private static final String TAG = "MainActivity";

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

        mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.tab_titles));
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
    public void onItemClick(String refKey) {
        mViewPager.setCurrentItem(TAB_GRAPH, true);
        Log.d(TAG, refKey);
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
                Intent intent = new Intent(this, AccService.class);
                intent.putExtra(AccService.EXTRA_USER_ID, getUid());
                startService(intent);
                break;
            case R.id.menu_stop:
                stopService(new Intent(this, AccService.class));
                break;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.menu_settings:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
