package com.boost.leonid.accelerometer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Coordinates {
    private String date;
    private String time;
    private List<Float> x;
    private List<Float> y;
    private List<Float> z;
    private String model;

    public Coordinates(){
        // required for calls getValue(Coordinates.class)
    }
    public Coordinates(String date, String time, List<Float> x, List<Float> y, List<Float> z, String model) {
        this.date = date;
        this.time = time;
        this.x = x;
        this.y = y;
        this.z = z;
        this.model = model;
    }
    public Coordinates(String date, String time, String model) {
        this.date = date;
        this.time = time;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public void SetModel(String deviceModel) {
        this.model = deviceModel;
    }

    public Map<String, Object> allToMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("date", date);
        result.put("time", time);
        result.put("x", x);
        result.put("y", y);
        result.put("z", z);
        result.put("model", model);
        return result;
    }
    public Map<String, Object> headerToMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("date", date);
        result.put("time", time);
        result.put("model", model);
        return result;
    }
    public Map<String, Object> coordinatesToMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("x", x);
        result.put("y", y);
        result.put("z", z);
        return result;
    }
}
