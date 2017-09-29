package cn.ssic.moverlogic.net2request;

import java.util.List;

/**
 * Created by Administrator on 2016/9/21.
 */

public class JobSheetDetail {
    private JobTime time;
    private List<JobAddress> addressDetails;
    private Payment payment;
    private List<Extra> extraEquipment;
    private Vehicle vehicle;
    private Job job;
    private Estimatedtime estimatedtime;
    private TimeDate timeDate;
    private JobSheetType jobType;
    private VechicleDetails vehicleDetails;
    private ClientCharge clientCharges;

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
        return estimatedtime;
    }

    public void setEstimatedtime(Estimatedtime estimatedtime) {
        this.estimatedtime = estimatedtime;
    }

    public TimeDate getTimedate() {
        return timeDate;
    }

    public void setTimedate(TimeDate timedate) {
        this.timeDate = timedate;
    }

    public JobSheetType getJobtype() {
        return jobType;
    }

    public void setJobtype(JobSheetType jobtype) {
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

    public List<Extra> getExtra() {
        return extraEquipment;
    }

    public void setExtra(List<Extra> extra) {
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
