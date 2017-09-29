package cn.ssic.moverlogic.net2request;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */

public class JobDetail {
    private JobTime time;
    private List<JobAddress> addressDetails;
    private Payment payment;
    private List<Equipment> extraEquipment;
    private Vehicle vehicle;
    private Job job;
    private Estimatedtime estimatedTime;
    private TimeDate timeDate;
    private JobType jobType;
    private VechicleDetails vehicleDetails;
    private ClientCharge clientCharges;

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    private String terms;

    public ClientCharge getClientCharges() {
        return clientCharges;
    }

    public void setClientCharges(ClientCharge clientCharges) {
        this.clientCharges = clientCharges;
    }

    public VechicleDetails getVehicleDetails() {
        return vehicleDetails;
    }

    public void setVehicleDetails(VechicleDetails vehicleDetails) {
        this.vehicleDetails = vehicleDetails;
    }

    public Estimatedtime getEstimatedtime() {
        return estimatedTime;
    }

    public void setEstimatedtime(Estimatedtime estimatedtime) {
        this.estimatedTime = estimatedtime;
    }

    public TimeDate getTimedate() {
        return timeDate;
    }

    public void setTimedate(TimeDate timedate) {
        this.timeDate = timedate;
    }

    public JobType getJobtype() {
        return jobType;
    }

    public void setJobtype(JobType jobtype) {
        this.jobType = jobtype;
    }

    public JobTime getTime() {
        return time;
    }

    public void setTime(JobTime time) {
        this.time = time;
    }

    public List<JobAddress> getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(List<JobAddress> addressDetails) {
        this.addressDetails = addressDetails;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public List<Equipment> getEquipment() {
        return extraEquipment;
    }

    public void setEquipment(List<Equipment> extra) {
        this.extraEquipment = extra;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}
