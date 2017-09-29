package cn.ssic.moverlogic.net2request.confirmJob;

import cn.ssic.moverlogic.net2request.NTReqBean;

/**
 * Created by Administrator on 2016/9/9.
 */
public class NTConfirmJobReqBean extends NTReqBean {
    int jobId;//job的主键id
    int appJobId;//app_job的主键id
    int status;//job和alert需要更新的状态

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getAppJobId() {
        return appJobId;
    }

    public void setAppJobId(int appJobId) {
        this.appJobId = appJobId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
