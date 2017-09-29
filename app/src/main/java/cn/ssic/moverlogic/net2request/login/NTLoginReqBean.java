package cn.ssic.moverlogic.net2request.login;

import cn.ssic.moverlogic.net2request.NTReqBean;

/**
 * Created by Administrator on 2016/9/6.
 */
public class NTLoginReqBean extends NTReqBean {
    private String username;
    private String pwd;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
