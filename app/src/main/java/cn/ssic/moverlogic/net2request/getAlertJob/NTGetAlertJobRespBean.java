package cn.ssic.moverlogic.net2request.getAlertJob;

import cn.ssic.moverlogic.net2request.JobDetail;
import cn.ssic.moverlogic.net2request.NTRespBean;

/**
 * Created by Administrator on 2016/9/9.
 */
public class NTGetAlertJobRespBean extends NTRespBean{
    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        public JobDetail jobdetail;

        public JobDetail getJobdetail() {
            return jobdetail;
        }

        public void setJobdetail(JobDetail jobdetail) {
            this.jobdetail = jobdetail;
        }
    }
}
