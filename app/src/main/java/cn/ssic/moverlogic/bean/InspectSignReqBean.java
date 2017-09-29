package cn.ssic.moverlogic.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/31.
 */
public class InspectSignReqBean extends RespBean implements Serializable{
    private int jobId ;
    private boolean sustomerSigned;
    private boolean npDamages ;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public boolean isSustomerSigned() {
        return sustomerSigned;
    }

    public void setSustomerSigned(boolean sustomerSigned) {
        this.sustomerSigned = sustomerSigned;
    }

    public boolean isNpDamages() {
        return npDamages;
    }

    public void setNpDamages(boolean npDamages) {
        this.npDamages = npDamages;
    }
}
