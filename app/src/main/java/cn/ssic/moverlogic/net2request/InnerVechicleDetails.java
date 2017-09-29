package cn.ssic.moverlogic.net2request;

import java.util.List;

/**
 * Created by Administrator on 2016/10/20.
 */

public class InnerVechicleDetails {
    VechicleDetails vechicleDetails;
    List<Equipment> equipment;

    public VechicleDetails getVehicleDetails() {
        return vechicleDetails;
    }

    public void setVehicleDetails(VechicleDetails vehicleDetails) {
        this.vechicleDetails = vehicleDetails;
    }

    public List<Equipment> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<Equipment> equipment) {
        this.equipment = equipment;
    }
}
