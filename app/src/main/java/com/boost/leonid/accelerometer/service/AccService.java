package com.boost.leonid.accelerometer.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.boost.leonid.accelerometer.R;
import com.boost.leonid.accelerometer.model.Coordinates;
import com.boost.leonid.accelerometer.ui.settings.SettingsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class AccService extends Service implements SensorEventListener{
    private static final String TAG = "AccService";
    private static final String EXTRA_MSG_ARRAY = "ARRAY";

    private static SharedPreferences mPreferences;
    private FirebaseDatabase mDatabase;
    private SensorManager mSensorManager;
    private Coordinates mCoordinates;
    private Handler mHandler;
    private Bundle mBundle;
    private int mInterval;
    private String mUser_id;
    private String mStartDate, mStartTime;
    private String mRootKeyOfAccData;
    private static boolean isRunning;

    private Map<String, Object> mMapToInsert = new HashMap<>();
    private int mRemainingTime;
    public static final String EXTRA_USER_ID = "user_id";


    public static void setServiceAlarm(Context context, boolean isOn) {
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Calendar currentCalendar = Calendar.getInstance();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mPreferences.getLong(SettingsFragment.KEY_PREF_TIME_WHEN_START, 0));

        currentCalendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        currentCalendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        currentCalendar.set(Calendar.SECOND, 0);
        currentCalendar.set(Calendar.MILLISECOND, 0);

        Log.d(TAG, currentCalendar.toString());

        Intent i = new Intent(context, AccService.class);
        i.putExtra(EXTRA_USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());
        PendingIntent pi = PendingIntent.getService(
                context, 0, i, 0);

        AlarmManager alarmManager = (AlarmManager)
                context.getSystemService(Context.ALARM_SERVICE);

        if (isOn) {
            isRunning = true;
            alarmManager.set(AlarmManager.RTC, currentCalendar.getTimeInMillis(), pi);
            Log.d(TAG, "start alarm");
        } else {
            isRunning = false;
            alarmManager.cancel(pi);
            pi.cancel();
            Log.d(TAG, "stop alarm");
        }
    }
    public static boolean isServiceAlarmOn(Context context) {
        Intent i = new Intent(context, AccService.class);
        PendingIntent pi = PendingIntent.getService(
                context, 0, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        initStartDateTime();
        mUser_id = intent.getStringExtra(EXTRA_USER_ID);

        mDatabase = FirebaseDatabase.getInstance();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        mRootKeyOfAccData = mDatabase.getReference().child("users").child(mUser_id).child("acc_data").push().getKey();
        mInterval = Integer.parseInt(mPreferences.getString(SettingsFragment.KEY_PREF_INTERVAL, getString(R.string.set_interval_def_value)));
        mRemainingTime = Integer.parseInt(mPreferences.getString(SettingsFragment.KEY_PREF_TIME_DURATION, getString(R.string.set_duration_def_value)));
        mCoordinates = new Coordinates(mStartDate, mStartTime, Build.MODEL, mInterval);
        Log.d(TAG, String.valueOf(mRemainingTime));
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        return super.onStartCommand(intent, flags, startId);
    }

    private void initStartDateTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        mStartDate = dateFormat.format(date);

        dateFormat = new SimpleDateFormat("HH:mm:ss");
        mStartTime = dateFormat.format(date);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");

        mBundle = new Bundle();

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:
                        mRemainingTime -= mInterval;
                        Log.d(TAG, String.valueOf(mRemainingTime));
                        if (mRemainingTime >= 0) {
                            Log.d(TAG, "handleMessage");
                            mCoordinates.addCoord(msg.getData().getFloatArray(EXTRA_MSG_ARRAY));
                            mMapToInsert.clear();
                            mMapToInsert.put("/users/" + mUser_id + "/acc_data/" + mRootKeyOfAccData, mCoordinates.allToMap());
                            mDatabase.getReference().updateChildren(mMapToInsert);
                            this.removeCallbacksAndMessages(null);
                        }else {
                            stopSelf();
                        }
                        break;
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        mSensorManager.unregisterListener(this);
        mHandler.removeCallbacksAndMessages(null);
        setServiceAlarm(this, false);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (isRunning) {
            mBundle.clear();
            mBundle.putFloatArray(EXTRA_MSG_ARRAY, sensorEvent.values);
            Message msg = new Message();
            msg.setData(mBundle);
            mHandler.sendMessageDelayed(msg, mInterval * 1000);
        }else {
            stopSelf();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}