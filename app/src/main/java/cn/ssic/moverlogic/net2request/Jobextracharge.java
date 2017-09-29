package cn.ssic.moverlogic.net2request;

import java.util.List;

/**
 * Created by Administrator on 2016/9/27.
 */

public class Jobextracharge {
    private String jobExtraCharge;

    public String getJobExtraCharge() {
        return jobExtraCharge;
    }

    public void setJobExtraCharge(String jobExtraCharge) {
        this.jobExtraCharge = jobExtraCharge;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private int id;
    private double charge;
    private String description;

    public String getJobextracharge() {
        return jobExtraCharge;
    }

    public void setJobextracharge(String jobextracharge) {
        this.jobExtraCharge = jobextracharge;
    }
}
