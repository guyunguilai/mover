package cn.ssic.moverlogic.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/31.
 */
public class PayCashReqBean extends ReqBean implements Serializable{
    private int jobId;
    private int payType ;//（1、现金，2、信用卡）
    private double payment  ;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }
}
