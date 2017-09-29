package cn.ssic.moverlogic.bean;

import java.io.Serializable;
import java.util.List;

import cn.ssic.moverlogic.net2request.Job;
import cn.ssic.moverlogic.net2request.getAlerts.NTGetAlertsRespBean;

/**
 * Created by Administrator on 2016/8/31.
 */
public class GetAlertsRespBean extends RespBean implements Serializable{
    private List<Job> cancelJob;
    private List<Job> availableNow;
    private List<Job> newJob;
    private List<Job> changeJob;
    private int unreadNumber;
    private int activeJobUnreadNuber;

    public int getUnreadNumber() {
        return unreadNumber;
    }

    public void setUnreadNumber(int unreadNumber) {
        this.unreadNumber = unreadNumber;
    }

    public int getActiveJobUnreadNuber() {
        return activeJobUnreadNuber;
    }

    public void setActiveJobUnreadNuber(int activeJobUnreadNuber) {
        this.activeJobUnreadNuber = activeJobUnreadNuber;
    }

    public List<Job> getCancelJob() {
        return cancelJob;
    }

    public void setCancelJob(List<Job> cancelJob) {
        this.cancelJob = cancelJob;
    }

    public List<Job> getAvailableNow() {
        return availableNow;
    }

    public void setAvailableNow(List<Job> availableNow) {
        this.availableNow = availableNow;
    }

    public List<Job> getNewJob() {
        return newJob;
    }

    public void setNewJob(List<Job> newJob) {
        this.newJob = newJob;
    }

    public List<Job> getChangeJob() {
        return changeJob;
    }

    public void setChangeJob(List<Job> changeJob) {
        this.changeJob = changeJob;
    }
}
