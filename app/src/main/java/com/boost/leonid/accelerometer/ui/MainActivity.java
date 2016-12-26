package com.boost.leonid.accelerometer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.boost.leonid.accelerometer.R;
import com.boost.leonid.accelerometer.model.AccelerometerData;
import com.boost.leonid.accelerometer.service.AccService;
import com.boost.leonid.accelerometer.ui.fragment.SessionListFragment;
import com.boost.leonid.accelerometer.ui.fragment.TabFragment;
import com.boost.leonid.accelerometer.ui.settings.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements SessionListFragment.SessionListInteractionListener{
    private static final String TAG             = "MainActivity";
    private static final int LAYOUT             = R.layout.activity_main;
    private static final int FRAME_CONTAINER    = R.id.main_frame_layout;
    private Menu mMenu;

    @BindView(R.id.toolbar_actionbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT);
        ButterKnife.bind(this);

        initToolbar();

        getSupportFragmentManager().beginTransaction()
                .add(FRAME_CONTAINER, new SessionListFragment())
                .addToBackStack(null)
                .commit();
    }
    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.menu_stop).setEnabled(false);
        mMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_start:
                if (!AccService.isServiceAlarmOn(this)){
                    AccService.startAlarmManager(this);
                    mMenu.findItem(R.id.menu_stop).setEnabled(true);
                    item.setEnabled(false);
                }
                break;
            case R.id.menu_stop:
                if (AccService.isServiceAlarmOn(this)){
                    AccService.stopAlarmManager();
                    mMenu.findItem(R.id.menu_start).setEnabled(true);
                    item.setEnabled(false);
                }
                break;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, AuthorizeActivity.class));
                finish();
                break;
            case R.id.menu_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSessionItemClick(List<AccelerometerData> model) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out)
                .replace(FRAME_CONTAINER, TabFragment.newInstance(model))
                .commit();
    }
}
