package com.foursquare.takehome;

import android.support.annotation.NonNull;

public final class Person implements Comparable<Person> {
    private int id;
    private String name;
    private long arriveTime;
    private long leaveTime;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getArriveTime() {
        return arriveTime;
    }

    public long getLeaveTime() {
        return leaveTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArriveTime(long arriveTime) {
        this.arriveTime = arriveTime;
    }

    public void setLeaveTime(long leaveTime) {
        this.leaveTime = leaveTime;
    }

    @Override
    public int compareTo(@NonNull Person p) {
        if(this.getArriveTime() - p.getArriveTime() == 0)
            return 0;
        if(this.getArriveTime() - p.getArriveTime() > 0)
            return 1;
        else
            return -1;
    }
}
