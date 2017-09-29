package cn.ssic.moverlogic.net2request;

import java.util.List;

import cn.ssic.moverlogic.net2request.Equipment;
import cn.ssic.moverlogic.net2request.VechicleDetails;

/**
 * @Author: he.zhao
 * @Date:on 2017/1/12.
 * @E-mail:377855879@qq.com
 */

public class VDetails {
    public String gvm;
    public String tm;
    public String truckType;
    public String capacity;
    public int men;

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

    public String getTruckType() {
        return truckType;
    }

    public void setTruckType(String truckType) {
        this.truckType = truckType;
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
}
