package cn.ssic.moverlogic.net2request.startJob;

import java.io.Serializable;

import cn.ssic.moverlogic.net2request.NTRespBean;

/**
 * Created by Administrator on 2016/9/5.
 */
public class NTStartJobRespBean extends NTRespBean implements Serializable {
    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data{

    }

}