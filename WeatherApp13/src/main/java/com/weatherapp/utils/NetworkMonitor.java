package com.weatherapp.utils;

public class NetworkMonitor {

    private long startTime;

    public void startTimer() {
        startTime = System.currentTimeMillis();
    }

    public long stopTimer() {
        return System.currentTimeMillis() - startTime;
    }

    public String formatResponseTime(long milliseconds) {
        return "Response time: " + milliseconds + " ms";
    }
}