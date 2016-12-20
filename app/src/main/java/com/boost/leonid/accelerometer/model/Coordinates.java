package com.boost.leonid.accelerometer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Coordinates {
    private String date;
    private String startTime;
    private String endTime;
    private List<Float> x;
    private List<Float> y;
    private List<Float> z;
    private String model;

    public Coordinates(){
        // required for calls getValue(Coordinates.class)
    }

    public Coordinates(String date, String startTime, String model) {
        this.date = date;
        this.startTime = startTime;
        this.model = model;
        x = new ArrayList<>();
        y = new ArrayList<>();
        z = new ArrayList<>();
    }
    public void addCoord(float[] coordinates){
        x.add(coordinates[0]);
        y.add(coordinates[1]);
        z.add(coordinates[2]);
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<Float> getX() {
        return x;
    }

    public void setX(List<Float> x) {
        this.x = x;
    }

    public List<Float> getY() {
        return y;
    }

    public void setY(List<Float> y) {
        this.y = y;
    }

    public List<Float> getZ() {
        return z;
    }

    public void setZ(List<Float> z) {
        this.z = z;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getModel() {
        return model;
    }

    public Map<String, Object> allToMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("date", date);
        result.put("startTime", startTime);
        result.put("x", x);
        result.put("y", y);
        result.put("z", z);
        result.put("model", model);
        return result;
    }
}
