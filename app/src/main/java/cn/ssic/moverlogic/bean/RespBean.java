package cn.ssic.moverlogic.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/30.
 */
public class RespBean<T> implements Serializable{
    public String status;
    public String msg;
    public T data;

    public T getResp() {
        return data;
    }

    public void setResp(T resp) {
        this.data = resp;
    }

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

    public String toString(){
        return "status = " + status + " msg = " + msg + " data = " + data;
    }



}
