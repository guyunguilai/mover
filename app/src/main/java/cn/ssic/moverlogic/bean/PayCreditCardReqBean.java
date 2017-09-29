package cn.ssic.moverlogic.bean;

import java.io.Serializable;

import cn.ssic.moverlogic.net2request.TransactionFee;

/**
 * Created by Administrator on 2016/8/31.
 */
public class PayCreditCardReqBean extends ReqBean implements Serializable{
    private int jobId;
    private int payType;//（1、现金，2、信用卡）
    private int cardType;
    private int cardYear;
    private int cardMonth;
    private String cardNo;
    private double payment;

    public String getCardCvv() {
        return cardCvv;
    }

    public void setCardCvv(String cardCvv) {
        this.cardCvv = cardCvv;
    }

    private String cardCvv;
    private TransactionFee transactionFee;

    public TransactionFee getTransactionFee() {
        return transactionFee;
    }

    public void setTransactionFee(TransactionFee transactionFee) {
        this.transactionFee = transactionFee;
    }

    public int getCardType() {
        return cardType;
    }

    public void setCardType(int cardType) {
        this.cardType = cardType;
    }

    public int getCardYear() {
        return cardYear;
    }

    public void setCardYear(int cardYear) {
        this.cardYear = cardYear;
    }

    public int getCardMonth() {
        return cardMonth;
    }

    public void setCardMonth(int cardMonth) {
        this.cardMonth = cardMonth;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public double getPayment() {
        return payment;
    }

    public void setPayment(double payment) {
        this.payment = payment;
    }
}
