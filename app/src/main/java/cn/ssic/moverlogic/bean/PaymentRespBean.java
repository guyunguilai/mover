package cn.ssic.moverlogic.bean;

import java.io.Serializable;
import java.util.Date;

import cn.ssic.moverlogic.net2request.Payment;

/**
 * Created by Administrator on 2016/8/31.
 */
public class PaymentRespBean extends ReqBean implements Serializable{
    double amount;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
