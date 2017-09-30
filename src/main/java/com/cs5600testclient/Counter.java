package com.cs5600testclient;

/**
 * Created by saikikwok on 29/09/2017.
 */
public class Counter {
    private int activeThreadCount = 0;

    public synchronized void incrementCount() {
        activeThreadCount++;
    }

    public synchronized int getCount() {
        return activeThreadCount;
    }

}