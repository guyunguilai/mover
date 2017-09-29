package cn.ssic.moverlogic.bean;

import java.io.Serializable;

import cn.ssic.moverlogic.net2request.Job;
import cn.ssic.moverlogic.net2request.JobDetail;
import cn.ssic.moverlogic.net2request.JobPage;

/**
 * Created by Administrator on 2016/8/31.
 */
public class ActiveJobRespBean extends RespBean implements Serializable{
    private int jobId;

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(int runStatus) {
        this.runStatus = runStatus;
    }

    public String getRunDate() {
        return runDate;
    }

    public void setRunDate(String runDate) {
        this.runDate = runDate;
    }

    private int currentPage;
    private int runStatus;
    private String runDate;

    public int getSkipMark() {
        return skipMark;
    }

    public void setSkipMark(int skipMark) {
        this.skipMark = skipMark;
    }

    public int getNode() {
        return node;
    }

    public void setNode(int node) {
        this.node = node;
    }

    private int skipMark;
    private int node;
    private Job job;
    private JobPage page;
    private int unreadNumber;
    private int unreadAlertJob;

    public int getUnreadNumber() {
        return unreadNumber;
    }

    public void setUnreadNumber(int unreadNumber) {
        this.unreadNumber = unreadNumber;
    }

    public int getUnreadAlertJob() {
        return unreadAlertJob;
    }

    public void setUnreadAlertJob(int unreadAlertJob) {
        this.unreadAlertJob = unreadAlertJob;
    }

    public JobPage getPage() {
        return page;
    }

    public void setPage(JobPage page) {
        this.page = page;
    }


    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
}
