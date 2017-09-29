package cn.ssic.moverlogic.bean;

import java.io.Serializable;
import java.util.List;

import cn.ssic.moverlogic.net2request.ClientDetail;
import cn.ssic.moverlogic.net2request.JobAddress;
import cn.ssic.moverlogic.net2request.JobCharge;
import cn.ssic.moverlogic.net2request.JobSheetDetail;
import cn.ssic.moverlogic.net2request.Notes;
import cn.ssic.moverlogic.net2request.Payment;
import cn.ssic.moverlogic.net2request.RosterAttachItem;
import cn.ssic.moverlogic.net2request.RunningTotal;
import cn.ssic.moverlogic.net2request.TimeSheet;
import cn.ssic.moverlogic.net2request.Totalinventory;
import cn.ssic.moverlogic.net2request.VehicleSheet;

/**
 * Created by Administrator on 2016/8/31.
 */
public class GetJobSheetRespBean extends RespBean implements Serializable {
    private JobSheetDetail jobDetails;
    private List<Totalinventory> totalInventory;
    private List<RosterAttachItem> attachedItems;
    private List<JobAddress> addressDetails;
    private RunningTotal runningTotal;
    private VehicleSheet vehicle;
    private TimeSheet timeSheet;
    private Notes jobNotes;
    private JobCharge jobCharges;
    private ClientDetail clientDetails;

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    private String terms;


    public List<Payment> getPayment() {
        return payment;
    }

    public void setPayment(List<Payment> payment) {
        this.payment = payment;
    }

    private List<Payment> payment;
    private GetRosterJobRespBean.ContactDetailsBean contactDetails;

    public GetRosterJobRespBean.ContactDetailsBean getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(GetRosterJobRespBean.ContactDetailsBean contactDetails) {
        this.contactDetails = contactDetails;
    }

    public JobSheetDetail getJobDetails() {
        return jobDetails;
    }

    public void setJobDetails(JobSheetDetail jobDetails) {
        this.jobDetails = jobDetails;
    }

    public List<Totalinventory> getTotalInventory() {
        return totalInventory;
    }

    public void setTotalInventory(List<Totalinventory> totalInventory) {
        this.totalInventory = totalInventory;
    }

    public List<RosterAttachItem> getAttachedItems() {
        return attachedItems;
    }

    public void setAttachedItems(List<RosterAttachItem> attachedItems) {
        this.attachedItems = attachedItems;
    }

    public List<JobAddress> getAddressDetails() {
        return addressDetails;
    }

    public void setAddressDetails(List<JobAddress> addressDetails) {
        this.addressDetails = addressDetails;
    }

    public RunningTotal getRunningTotal() {
        return runningTotal;
    }

    public void setRunningTotal(RunningTotal runningTotal) {
        this.runningTotal = runningTotal;
    }

    public VehicleSheet getVehicle() {
        return vehicle;
    }

    public void setVehicle(VehicleSheet vehicle) {
        this.vehicle = vehicle;
    }

    public TimeSheet getTimeSheet() {
        return timeSheet;
    }

    public void setTimeSheet(TimeSheet timeSheet) {
        this.timeSheet = timeSheet;
    }

    public Notes getJobNotes() {
        return jobNotes;
    }

    public void setJobNotes(Notes jobNotes) {
        this.jobNotes = jobNotes;
    }

    public JobCharge getJobCharges() {
        return jobCharges;
    }

    public void setJobCharges(JobCharge jobCharges) {
        this.jobCharges = jobCharges;
    }

    public ClientDetail getClientDetails() {
        return clientDetails;
    }

    public void setClientDetails(ClientDetail clientDetails) {
        this.clientDetails = clientDetails;
    }
}
