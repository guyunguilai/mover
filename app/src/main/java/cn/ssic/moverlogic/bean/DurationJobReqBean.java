package cn.ssic.moverlogic.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/31.
 */
public class DurationJobReqBean extends ReqBean implements Serializable{
    private int jobId;
    private int id ;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
