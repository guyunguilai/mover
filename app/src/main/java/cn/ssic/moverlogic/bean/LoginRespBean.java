package cn.ssic.moverlogic.bean;

import java.io.Serializable;

import cn.ssic.moverlogic.net2request.Staff;

/**
 * Created by Administrator on 2016/8/31.
 */
public class LoginRespBean extends RespBean implements Serializable{
    private Staff staff;

    public Staff getStaff() {
        return staff;
    }

    public void setStaff(Staff staff) {
        this.staff = staff;
    }

}
