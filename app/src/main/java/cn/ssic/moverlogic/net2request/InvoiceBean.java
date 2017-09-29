package cn.ssic.moverlogic.net2request;

import java.io.Serializable;

import cn.ssic.moverlogic.bean.RespBean;

/**
 * Created by Administrator on 2016/8/31.
 */
public class InvoiceBean extends RespBean implements Serializable{
    public String isInvoice;

    public String getIsInvoice() {
        return isInvoice;
    }

    public void setIsInvoice(String isInvoice) {
        this.isInvoice = isInvoice;
    }
}
