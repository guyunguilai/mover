package cn.ssic.moverlogic.net2request;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */

public class JobCharge {
    private Timecalc timeCharges;
    private List<Discounts> discounts;

    private List<ChargeNameAndValue> extraCharges;
    private VehicleCharges vehicleCharges;

    public List<TransactionFee> getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(List<TransactionFee> transactionFee) {
        this.transactionFee = transactionFee;
    }

    private List<TransactionFee> transactionFee;

    public Timecalc getTimeCharges() {
        return timeCharges;
    }

    public void setTimeCharges(Timecalc timeCharges) {
        this.timeCharges = timeCharges;
    }

    public List<Discounts> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<Discounts> discounts) {
        this.discounts = discounts;
    }

    public List<ChargeNameAndValue> getExtraCharges() {
        return extraCharges;
    }

    public void setExtraCharges(List<ChargeNameAndValue> extraCharges) {
        this.extraCharges = extraCharges;
    }

    public VehicleCharges getVehicleCharges() {
        return vehicleCharges;
    }

    public void setVehicleCharges(VehicleCharges vehicleCharges) {
        this.vehicleCharges = vehicleCharges;
    }
}
