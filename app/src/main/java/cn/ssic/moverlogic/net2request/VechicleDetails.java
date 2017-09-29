package cn.ssic.moverlogic.net2request;

import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */

public class VechicleDetails {
    public String gvm;
    public String tm;
    public String capacity;
//    private List<Equipment> equipment;
    public int men;
    public String type;
    private String vehicleType;
    private String rateType;
    private String returnFee;
    private double rate;
    private String callOutFee;
    private String truckType;
    private String truckGvm;
    private String truckTm;
    private String truckCapacity;
//    private VDetails vechicleDetails;

    public String getTruckGvm() {
        return truckGvm;
    }
    public void setTruckGvm(String truckGvm) {
        this.truckGvm = truckGvm;
    }

    public String getTruckTm() {
        return truckTm;
    }

    public void setTruckTm(String truckTm) {
        this.truckTm = truckTm;
    }

    public String getTruckCapacity() {
        return truckCapacity;
    }

    public void setTruckCapacity(String truckCapacity) {
        this.truckCapacity = truckCapacity;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
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

    public String getCalloutfee() {
        return callOutFee;
    }

    public void setCalloutfee(String calloutfee) {
        this.callOutFee = calloutfee;
    }

    public String getTruckType() {
        return truckType;
    }

    public void setTruckType(String truckType) {
        this.truckType = truckType;
    }

//    public VDetails getVechicledetails() {
//        return vechicleDetails;
//    }
//
//    public void setVechicledetails(VDetails vechicledetails) {
//        this.vechicleDetails = vechicledetails;
//    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

//    public List<Equipment> getEquipment() {
//        return equipment;
//    }
//
//    public void setEquipment(List<Equipment> equipment) {
//        this.equipment = equipment;
//    }

    public String getGvm() {
        return gvm;
    }

    public void setGvm(String gvm) {
        this.gvm = gvm;
    }

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public int getMen() {
        return men;
    }

    public void setMen(int men) {
        this.men = men;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
