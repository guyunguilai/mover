package cn.ssic.moverlogic.net2request.checkVersion;

import cn.ssic.moverlogic.net2request.NTReqBean;

/**
 * Created by Administrator on 2017/3/22.
 */

public class NTCheckVersionReqBean extends NTReqBean {
    private String devicesId;

    public String getDevicesId() {
        return devicesId;
    }

    public void setDevicesId(String devicesId) {
        this.devicesId = devicesId;
    }
}
