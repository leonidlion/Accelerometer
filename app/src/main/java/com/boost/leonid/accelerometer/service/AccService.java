package com.boost.leonid.accelerometer.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.boost.leonid.accelerometer.model.Coordinates;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class AccService extends Service implements SensorEventListener{
    private static final String TAG = "AccService";

    public static final String EXTRA_USER_ID = "user_id";

    private static final int SAVE_DELAY_MS = 5000;
    private static final String EXTRA_MSG_ARRAY = "ARRAY";

    private FirebaseDatabase mDatabase;
    private SensorManager mSensorManager;
    private String mUser_id;
    private String mStartDate, mStartTime, mEndTime;
    private String mStartKey;
    private Map<String, Object> mapToInsert = new HashMap<>();
    private Coordinates mCoordinates;
    private Handler mHandler;
    private Bundle mBundle;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        initStartDateTime();
        mUser_id = intent.getStringExtra(EXTRA_USER_ID);
        mDatabase = FirebaseDatabase.getInstance();
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mStartKey = mDatabase.getReference().child("users").child(mUser_id).child("acc_data").push().getKey();
        mCoordinates = new Coordinates(mStartDate, mStartTime, Build.MODEL);
        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        return super.onStartCommand(intent, flags, startId);
    }

    private void initStartDateTime() {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        mStartDate = dateFormat.format(date);

        dateFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());
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
                        Log.d(TAG, "handleMessage");

                        mCoordinates.addCoord(msg.getData().getFloatArray(EXTRA_MSG_ARRAY));
                        mapToInsert.clear();
                        mapToInsert.put("/users/" + mUser_id + "/acc_data/" + mStartKey, mCoordinates.allToMap());
                        mDatabase.getReference().updateChildren(mapToInsert);

                        this.removeCallbacksAndMessages(null);
                        break;
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        mEndTime = new SimpleDateFormat("hh:mm:ss", Locale.getDefault()).format(new Date());
        mSensorManager.unregisterListener(this);
        mHandler.removeCallbacksAndMessages(null);
        mDatabase.getReference().child("users").child(mUser_id).child("acc_data").child(mStartKey).child("endTime").setValue(mEndTime);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        mBundle.clear();
        mBundle.putFloatArray(EXTRA_MSG_ARRAY, sensorEvent.values);
        Message msg = new Message();
        msg.setData(mBundle);
        mHandler.sendMessageDelayed(msg, SAVE_DELAY_MS);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}