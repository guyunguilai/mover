package cn.ssic.moverlogic.net2request;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */

public class ClientCharge {
    private VehicleCharges vehicleCharges;
    private List<ChargeNameAndValue> extraCharges;
    private List<Discounts> discounts;
    private List<TransactionFee> transactionFee;
    private Timecalc timeCalculation;

    public List<TransactionFee> getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(List<TransactionFee> transactionFee) {
        this.transactionFee = transactionFee;
    }

    public Timecalc getTimeCalculation() {
        return timeCalculation;
    }

    public void setTimeCalculation(Timecalc timeCalculation) {
        this.timeCalculation = timeCalculation;
    }

    public VehicleCharges getVehicleCharges() {
        return vehicleCharges;
    }

    public void setVehicleCharges(VehicleCharges vehicleCharges) {
        this.vehicleCharges = vehicleCharges;
    }

    public VehicleCharges getVehiclecharges() {
        return vehicleCharges;
    }

    public void setVehiclecharges(VehicleCharges vehiclecharges) {
        this.vehicleCharges = vehiclecharges;
    }

    public List<ChargeNameAndValue> getExtraCharges() {
        return extraCharges;
    }

    public void setExtraCharges(List<ChargeNameAndValue> extraCharges) {
        this.extraCharges = extraCharges;
    }

    public List<Discounts> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<Discounts> discounts) {
        this.discounts = discounts;
    }

}
