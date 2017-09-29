package cn.ssic.moverlogic.bean;

import java.io.Serializable;
import java.util.List;

import cn.ssic.moverlogic.net2request.CardType;
import cn.ssic.moverlogic.net2request.CreditCardType;
import cn.ssic.moverlogic.net2request.Payment;

/**
 * Created by Administrator on 2016/8/31.
 */
public class GetCreditCardTypeRespBean extends ReqBean implements Serializable{
    private List<CardType> creditCardType;

    public List<CardType> getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(List<CardType> creditCardType) {
        this.creditCardType = creditCardType;
    }
}
