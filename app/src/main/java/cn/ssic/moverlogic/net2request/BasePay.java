package cn.ssic.moverlogic.net2request;

/**
 * Created by Administrator on 2016/9/9.
 */
public class BasePay {
    public String payAmount;
    public String callOutFee;
    public String returnFee;
    public String payType;

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getCalloutfee() {
        return callOutFee;
    }

    public void setCalloutfee(String calloutfee) {
        this.callOutFee = calloutfee;
    }

    public String getReturnfee() {
        return returnFee;
    }

    public void setReturnfee(String returnfee) {
        this.returnFee = returnfee;
    }

}
