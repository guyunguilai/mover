package cn.ssic.moverlogic.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2016/8/31.
 */
public class AddExtraChargeReqBean extends ReqBean implements Serializable{
    private int jobId;
    private int chargeType  ;
    private double charge ;
    private int quantity  ;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getChargeType() {
        return chargeType;
    }

    public void setChargeType(int chargeType) {
        this.chargeType = chargeType;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
