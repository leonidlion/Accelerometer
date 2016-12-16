package com.boost.leonid.accelerometer.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Coordinates {
    private String date;
    private String time;
    private List<Double> x;
    private List<Double> y;
    private List<Double> z;
    private String model;

    public Coordinates(){
        // required for calls getValue(Coordinates.class)
    }
    public Coordinates(String date, String time, List<Double> x, List<Double> y, List<Double> z, String model) {
        this.date = date;
        this.time = time;
        this.x = x;
        this.y = y;
        this.z = z;
        this.model = model;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<Double> getX() {
        return x;
    }

    public void setX(List<Double> x) {
        this.x = x;
    }

    public List<Double> getY() {
        return y;
    }

    public void setY(List<Double> y) {
        this.y = y;
    }

    public List<Double> getZ() {
        return z;
    }

    public void setZ(List<Double> z) {
        this.z = z;
    }

    public String getModel() {
        return model;
    }

    public void SetModel(String deviceModel) {
        this.model = deviceModel;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("date", date);
        result.put("time", time);
        result.put("x", x);
        result.put("y", y);
        result.put("z", z);
        result.put("model", model);
        return result;
    }
}
