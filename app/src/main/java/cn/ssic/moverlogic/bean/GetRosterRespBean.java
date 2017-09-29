package cn.ssic.moverlogic.bean;

import java.io.Serializable;
import java.util.List;

import cn.ssic.moverlogic.net2request.Job;

/**
 * Created by Administrator on 2016/8/31.
 */
public class GetRosterRespBean extends RespBean implements Serializable {
    public List<Job> today;
    public List<Job> upcoming;
    public List<Job> tomorrow;
    public int unreadAlertJob;
    public int unreadActiveJob;

    public int getUnreadAlertJob() {
        return unreadAlertJob;
    }

    public void setUnreadAlertJob(int unreadAlertJob) {
        this.unreadAlertJob = unreadAlertJob;
    }

    public int getUnreadActiveJob() {
        return unreadActiveJob;
    }

    public void setUnreadActiveJob(int unreadActiveJob) {
        this.unreadActiveJob = unreadActiveJob;
    }

    public List<Job> getUpcoming() {
        return upcoming;
    }

    public void setUpcoming(List<Job> upcoming) {
        this.upcoming = upcoming;
    }

    public List<Job> getTomorrow() {
        return tomorrow;
    }

    public void setTomorrow(List<Job> tomorrow) {
        this.tomorrow = tomorrow;
    }

    public List<Job> getToday() {
        return today;
    }

    public void setToday(List<Job> today) {
        this.today = today;
    }
}
