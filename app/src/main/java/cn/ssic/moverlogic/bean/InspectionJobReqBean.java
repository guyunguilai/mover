package cn.ssic.moverlogic.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/31.
 */
public class InspectionJobReqBean extends RespBean implements Serializable{
    private int jobId;
    private int estimateTimeFrom;
    private int estimateTimeTo;
    private boolean termsOfService;
    private boolean chargesAndCosts ;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getEstimateTimeFrom() {
        return estimateTimeFrom;
    }

    public void setEstimateTimeFrom(int estimateTimeFrom) {
        this.estimateTimeFrom = estimateTimeFrom;
    }

    public int getEstimateTimeTo() {
        return estimateTimeTo;
    }

    public void setEstimateTimeTo(int estimateTimeTo) {
        this.estimateTimeTo = estimateTimeTo;
    }

    public boolean isTermsOfService() {
        return termsOfService;
    }

    public void setTermsOfService(boolean termsOfService) {
        this.termsOfService = termsOfService;
    }

    public boolean isChargesAndCosts() {
        return chargesAndCosts;
    }

    public void setChargesAndCosts(boolean chargesAndCosts) {
        this.chargesAndCosts = chargesAndCosts;
    }
}
