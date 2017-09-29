package cn.ssic.moverlogic.net2request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/31.
 */
public class RunningTotal implements Serializable{
    String total;
    String tax;
    String totalDiscounts;
    String totalVehicleCharges;
    String subTotal;
    String totalExtraCharges;
    public String getTotalDiscounts() {
        return totalDiscounts;
    }

    public void setTotalDiscounts(String totalDiscounts) {
        this.totalDiscounts = totalDiscounts;
    }
    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotalVehicleCharges() {
        return totalVehicleCharges;
    }

    public void setTotalVehicleCharges(String totalVehicleCharges) {
        this.totalVehicleCharges = totalVehicleCharges;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getTotalExtraCharges() {
        return totalExtraCharges;
    }

    public void setTotalExtraCharges(String totalExtraCharges) {
        this.totalExtraCharges = totalExtraCharges;
    }
}
