package com.susy_xu.susy_todo.myClass;

import java.io.Serializable;

/**
 * Created by susy on 16/7/6.
 */
public class ScheduleItem implements Serializable {
    private int id;
    private String name;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private int alarm;
    private String type;
    private String comments;
    private int state;

    public ScheduleItem() {
    }

    public ScheduleItem(int id, String name, String startDate, String endDate, String startTime, String endTime, int alarm, String type, String comments, int state) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.alarm = alarm;
        this.type = type;
        this.comments = comments;
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
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

    public int getAlarm() {
        return alarm;
    }

    public void setAlarm(int alarm) {
        this.alarm = alarm;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
