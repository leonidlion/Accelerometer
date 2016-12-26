package com.boost.leonid.accelerometer.model;


import com.boost.leonid.accelerometer.Const;

import java.util.HashMap;
import java.util.Map;

public class AccelerometerData {
    private long unixTime;
    private float x;
    private float y;
    private float z;

    public AccelerometerData(){

    }

    public void setFloatArrayToVariables(float[] arr){
        x = arr[0];
        y = arr[1];
        z = arr[2];
    }
    public AccelerometerData(long unixTime, float x, float y, float z) {
        this.unixTime = unixTime;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public long getUnixTime() {
        return unixTime;
    }

    public void setUnixTime(long unixTime) {
        this.unixTime = unixTime;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Map<String, Object> allToMap(){
        Map<String, Object> result = new HashMap<>();
        result.put(Const.PUT_MAP_TIME, unixTime);
        result.put(Const.PUT_MAP_X, x);
        result.put(Const.PUT_MAP_Y, y);
        result.put(Const.PUT_MAP_Z, z);
        return result;
    }
}
