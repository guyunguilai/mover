package cn.ssic.moverlogic.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/31.
 */
public class InspectSignRespBean extends RespBean implements Serializable{
    private String signUrl;

    public String getSignUrl() {
        return signUrl;
    }

    public void setSignUrl(String signUrl) {
        this.signUrl = signUrl;
    }
}
