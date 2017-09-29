package cn.ssic.moverlogic.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/31.
 */
public class PauseJobReqBean extends ReqBean implements Serializable{
    private int jobId;
    private int reason ;
    private Date startTime;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

}
