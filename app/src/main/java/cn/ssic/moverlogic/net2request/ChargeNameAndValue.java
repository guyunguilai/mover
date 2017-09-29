package cn.ssic.moverlogic.net2request;

/**
 * Created by Administrator on 2016/9/27.
 */

public class ChargeNameAndValue {
    private String chargeValue;
    private String chargeName;
    private String extraChargeName;
    private String extraChargeQuantity;
    private String extraChargeValue;

    public String getExtraChargeName() {
        return extraChargeName;
    }

    public void setExtraChargeName(String extraChargeName) {
        this.extraChargeName = extraChargeName;
    }

    public String getExtraChargeQuantity() {
        return extraChargeQuantity;
    }

    public void setExtraChargeQuantity(String extraChargeQuantity) {
        this.extraChargeQuantity = extraChargeQuantity;
    }

    public String getExtraChargeValue() {
        return extraChargeValue;
    }

    public void setExtraChargeValue(String extraChargeValue) {
        this.extraChargeValue = extraChargeValue;
    }


    public String getChargeValue() {
        return chargeValue;
    }

    public void setChargeValue(String chargeValue) {
        this.chargeValue = chargeValue;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }
}
