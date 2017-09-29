package cn.ssic.moverlogic.net2request;

/**
 * Created by Administrator on 2016/9/7.
 */
public class TransactionFee {
    private double rate;

    public String getTransactionFeeName() {
        return transactionFeeName;
    }

    public void setTransactionFeeName(String transactionFeeName) {
        this.transactionFeeName = transactionFeeName;
    }

    public String getTransactionFeeValue() {
        return transactionFeeValue;
    }

    public void setTransactionFeeValue(String transactionFeeValue) {
        this.transactionFeeValue = transactionFeeValue;
    }

    private String transactionFeeName;
    private String transactionFeeValue;

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }
}