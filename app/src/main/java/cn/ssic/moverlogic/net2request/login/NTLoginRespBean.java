package cn.ssic.moverlogic.net2request.login;

import java.io.Serializable;

import cn.ssic.moverlogic.bean.RespBean;
import cn.ssic.moverlogic.net2request.NTRespBean;
import cn.ssic.moverlogic.net2request.Staff;

/**
 * Created by Administrator on 2016/9/1.
 */
public class NTLoginRespBean extends NTRespBean implements Serializable {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{
        private Staff staff;

        public Staff getStaff() {
            return staff;
        }

        public void setStaff(Staff staff) {
            this.staff = staff;
        }
    }

}
