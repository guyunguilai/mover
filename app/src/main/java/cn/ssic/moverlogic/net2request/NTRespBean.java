package cn.ssic.moverlogic.net2request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/1.
 */
public class NTRespBean implements Serializable {
    public String status;
    public String msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
