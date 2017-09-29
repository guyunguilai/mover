package cn.ssic.moverlogic.net2request;

/**
 * Created by Administrator on 2016/9/27.
 */

public class VehicleCharges {
    private String callOutFee;
    private String rateType;
    private String returnFee;
    private double rate;

    public String getCalloutfee() {
        return callOutFee;
    }

    public void setCalloutfee(String calloutfee) {
        this.callOutFee = calloutfee;
    }

    public String getRatetype() {
        return rateType;
    }

    public void setRatetype(String ratetype) {
        this.rateType = ratetype;
    }

    public String getReturnfee() {
        return returnFee;
    }

    public void setReturnfee(String returnfee) {
        this.returnFee = returnfee;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}
