package cn.ssic.moverlogic.net2request;

import android.text.SpannableString;

/**
 * Created by Administrator on 2016/9/21.
 */

public class Job {
    private String address;
    private double moveSize;
    private String estime;
    private String moveDate;
    private String startTime;
    private String week;
    private int status;
    private String type;
    private int jobId;
    private int day;
    private int readMark;
    private int changeStatus;

    public int getChangeStatus() {
        return changeStatus;
    }

    public void setChangeStatus(int changeStatus) {
        this.changeStatus = changeStatus;
    }



    public double getMoveSize() {
        return moveSize;
    }

    public void setMoveSize(double moveSize) {
        this.moveSize = moveSize;
    }

    public int getReadMark() {
        return readMark;
    }

    public void setReadMark(int readMark) {
        this.readMark = readMark;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
    /*Alerts list中刚开始查询出来都有蓝色newjobs、黄色jobchanges以及红色cancellations的job list
        绿色available now的job则是蓝色newjobs的第一条job数据
        如果蓝色newjobs中状态发生改变，则会归类到黄色jobchanges中
        status等于0，则job颜色为蓝色newjobs
        status等于1,则job颜色为黄色jobchanges
        status等于3,则job颜色为红色cancellations*/

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getMovesize() {
        return moveSize;
    }

    public void setMovesize(double movesize) {
        this.moveSize = movesize;
    }

    public String getEstime() {
        return estime;
    }

    public void setEstime(String estime) {
        this.estime = estime;
    }

    public String getMoveDate() {
        return moveDate;
    }

    public void setMoveDate(String moveDate) {
        this.moveDate = moveDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
