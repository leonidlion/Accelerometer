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

import com.boost.leonid.accelerometer.Const;
import com.boost.leonid.accelerometer.R;
import com.boost.leonid.accelerometer.model.AccelerometerData;
import com.boost.leonid.accelerometer.model.HistoryItem;
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
    private static final int MSG_UPDATE_WHAT = 0;

    private static AlarmManager mAlarmManager;
    private static Calendar sCalendar = Calendar.getInstance();
    private static SharedPreferences mPreferences;
    private static PendingIntent sPendingIntent;
    private static boolean isRunning;

    private FirebaseDatabase mDatabase;
    private SensorManager mSensorManager;
    private HistoryItem mHistoryItem;
    private Handler mHandler;
    private Bundle mArgsForMessage;
    private int mInterval;
    private String mUser_id;
    private String mStartDate, mStartTime;
    private String mSessionId;
    private Map<String, Object> mMapToInsert = new HashMap<>();
    private int mRemainingTime;
    private AccelerometerData mAccelerometerData;

    public static final String EXTRA_USER_ID = "user_id";

    public static void startAlarmManager(Context context){
        Log.d(TAG, "startAlarm");
        isRunning = true;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        initCalendarForAlarm();
        Intent i = new Intent(context, AccService.class);
        i.putExtra(EXTRA_USER_ID, FirebaseAuth.getInstance().getCurrentUser().getUid());
        sPendingIntent = PendingIntent.getService(context, 0, i, 0);
        mAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC, sCalendar.getTimeInMillis(), sPendingIntent);
    }

    private static void initCalendarForAlarm() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mPreferences.getLong(SettingsFragment.KEY_PREF_TIME_WHEN_START, 0));

        sCalendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        sCalendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));
        sCalendar.set(Calendar.SECOND, 0);
        sCalendar.set(Calendar.MILLISECOND, 0);
    }

    public static void stopAlarmManager(){
        Log.d(TAG, "stopAlarm");
        isRunning = false;
        mAlarmManager.cancel(sPendingIntent);
        sPendingIntent.cancel();
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

        mSessionId = Const.getDataCoordinatesOfUserReference(mUser_id).push().getKey();
        mInterval = Integer.parseInt(mPreferences.getString(SettingsFragment.KEY_PREF_INTERVAL, getString(R.string.set_interval_def_value)));
        mRemainingTime = Integer.parseInt(mPreferences.getString(SettingsFragment.KEY_PREF_TIME_DURATION, getString(R.string.set_duration_def_value)));

        mHistoryItem = new HistoryItem(mStartDate, mStartTime, mInterval,Build.MODEL);
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

        mArgsForMessage = new Bundle();

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case MSG_UPDATE_WHAT:
                        mRemainingTime -= mInterval;
                        Log.d(TAG, String.valueOf(mRemainingTime));
                        if (mRemainingTime >= 0) {
                            Log.d(TAG, "handleMessage");
                            mAccelerometerData = new AccelerometerData();
                            mAccelerometerData.setUnixTime(System.currentTimeMillis());
                            mAccelerometerData.setFloatArrayToVariables(msg.getData().getFloatArray(EXTRA_MSG_ARRAY));
                            mHistoryItem.addAccelerometerData(mAccelerometerData);
                            mMapToInsert.clear();
                            mMapToInsert.put(Const.getPathToDataForMapInsert(mUser_id, mSessionId), mHistoryItem.allToMap());
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
        stopAlarmManager();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (isRunning) {
            mArgsForMessage.clear();
            mArgsForMessage.putFloatArray(EXTRA_MSG_ARRAY, sensorEvent.values);
            Message msg = new Message();
            msg.setData(mArgsForMessage);
            msg.what = MSG_UPDATE_WHAT;
            mHandler.sendMessageDelayed(msg, mInterval * 1000);
        }else {
            stopSelf();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}