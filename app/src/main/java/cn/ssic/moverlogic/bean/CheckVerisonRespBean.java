package cn.ssic.moverlogic.bean;

import java.io.Serializable;

import cn.ssic.moverlogic.net2request.Staff;

/**
 * Created by Administrator on 2016/8/31.
 */
public class CheckVerisonRespBean extends RespBean implements Serializable{
    private int version;
    private String url;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



}
