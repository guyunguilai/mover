package cn.ssic.moverlogic.net2request;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/31.
 */
public class Pause implements Serializable{
    String min;
    String s;
    String hour;

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
