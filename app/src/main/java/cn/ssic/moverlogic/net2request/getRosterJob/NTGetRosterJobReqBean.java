package cn.ssic.moverlogic.net2request.getRosterJob;

import cn.ssic.moverlogic.net2request.NTReqBean;

/**
 * Created by Administrator on 2016/9/9.
 */
public class NTGetRosterJobReqBean extends NTReqBean {
    int jobId;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }
}
