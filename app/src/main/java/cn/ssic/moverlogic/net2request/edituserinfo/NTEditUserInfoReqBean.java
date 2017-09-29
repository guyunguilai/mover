package cn.ssic.moverlogic.net2request.edituserinfo;

import cn.ssic.moverlogic.net2request.NTReqBean;
import cn.ssic.moverlogic.net2request.Staff;

/**
 * Created by Administrator on 2016/9/7.
 */
public class NTEditUserInfoReqBean extends NTReqBean {
    int uid;
    private String username;
    private String password;
    private String npassword;
    private String firstname;
    private String surname;
    private String email;
    private String phone;
    private short isAlert;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNpassword() {
        return npassword;
    }

    public void setNpassword(String npassword) {
        this.npassword = npassword;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public short getIsAlert() {
        return isAlert;
    }

    public void setIsAlert(short isAlert) {
        this.isAlert = isAlert;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
