package cn.ssic.moverlogic.net2request;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */

public class Vehicle {
    VechicleDetails vechicleDetails;
    List<Equipment> equipment;
    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }

    public VechicleDetails getVehicleDetails() {
        return vechicleDetails;
    }

    public void setVehicleDetails(VechicleDetails vechicleDetails) {
        this.vechicleDetails = vechicleDetails;
    }
}
