package com.boost.leonid.accelerometer.model;

import com.boost.leonid.accelerometer.Const;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryItem {
    private String startDate;
    private String startTime;
    private int interval;
    private String deviceModel;
    private List<AccelerometerData> coordinates = new ArrayList<>();

    public HistoryItem(){
    }

    public HistoryItem(String startDate, String startTime, int interval, String deviceModel) {
        this.startDate = startDate;
        this.startTime = startTime;
        this.interval = interval;
        this.deviceModel = deviceModel;
    }


    public void addAccelerometerData(AccelerometerData accelerometerData){
        coordinates.add(accelerometerData);
    }
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public List<AccelerometerData> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(List<AccelerometerData> coordinates) {
        this.coordinates = coordinates;
    }

    public Map<String, Object> allToMap(){
        Map<String, Object> result = new HashMap<>();
        result.put(Const.PUT_MAP_START_DATE, startDate);
        result.put(Const.PUT_MAP_START_TIME, startTime);
        result.put(Const.PUT_MAP_INTERVAL, interval);
        result.put(Const.PUT_MAP_DEVICE_MODEL, deviceModel);
        result.put(Const.PUT_MAP_COORDINATES, coordinates);
        return result;
    }
}
