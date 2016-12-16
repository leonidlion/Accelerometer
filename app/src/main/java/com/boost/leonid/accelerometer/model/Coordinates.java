package com.boost.leonid.accelerometer.model;

import java.util.List;

public class Coordinates {
    private String date;
    private String time;
    private List<Double> x;
    private List<Double> y;
    private List<Double> z;

    public Coordinates(String date, String time, List<Double> x, List<Double> y, List<Double> z) {
        this.date = date;
        this.time = time;
        this.x = x;
        this.y = y;
        this.z = z;
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
}
