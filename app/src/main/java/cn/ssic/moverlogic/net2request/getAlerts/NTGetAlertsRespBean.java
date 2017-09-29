package cn.ssic.moverlogic.net2request.getAlerts;

import java.util.List;

import cn.ssic.moverlogic.net2request.BasePay;
import cn.ssic.moverlogic.net2request.ExtraPay;
import cn.ssic.moverlogic.net2request.Job;
import cn.ssic.moverlogic.net2request.JobAddress;
import cn.ssic.moverlogic.net2request.NTRespBean;

/**
 * Created by Administrator on 2016/9/9.
 */
public class NTGetAlertsRespBean extends NTRespBean{
    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        public List<Job> jobs;

        public List<Job> getJobs() {
            return jobs;
        }

        public void setJobs(List<Job> jobs) {
            this.jobs = jobs;
        }
    }
}
