package cn.ssic.moverlogic.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/31.
 */
public class EditUserImgRespBean extends RespBean implements Serializable{
    String photoUrl;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
