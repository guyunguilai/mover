package cn.ssic.moverlogic.bean;

import java.io.Serializable;

import cn.ssic.moverlogic.net2request.JobDetail;
import cn.ssic.moverlogic.net2request.getAlertJob.NTGetAlertJobRespBean;

/**
 * Created by Administrator on 2016/8/31.
 */
public class GetAlertJobRespBean extends RespBean implements Serializable{
    public JobDetail jobDetail;

    public JobDetail getJobdetail() {
        return jobDetail;
    }

    public void setJobdetail(JobDetail jobdetail) {
        this.jobDetail = jobdetail;
    }
}
