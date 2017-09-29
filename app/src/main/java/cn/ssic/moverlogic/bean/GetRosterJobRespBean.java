package cn.ssic.moverlogic.bean;

import java.util.List;

import cn.ssic.moverlogic.net2request.ClientCharge;
import cn.ssic.moverlogic.net2request.ClientDetail;
import cn.ssic.moverlogic.net2request.JobAddress;
import cn.ssic.moverlogic.net2request.JobDetail;
import cn.ssic.moverlogic.net2request.Notes;
import cn.ssic.moverlogic.net2request.Payment;
import cn.ssic.moverlogic.net2request.RosterAttachItem;
import cn.ssic.moverlogic.net2request.Totalinventory;
import cn.ssic.moverlogic.net2request.Vehicle;

/**
 * Created by Administrator on 2016/8/31.
 */
public class GetRosterJobRespBean extends RespBean {
    private List<RosterAttachItem> attachedItems;
    private ClientDetail clientDetail;
    private ClientCharge clientCharge;
    private JobDetail jobDetail;
    private List<Payment> payment;
    private List<Totalinventory> totalInventory;
    private Vehicle vehicle;
    private List<JobAddress> addressDetail;
    private Notes notes;
    private ContactDetailsBean contactDetails;


    public List<Payment> getPayment() {
        return payment;
    }

    public void setPayment(List<Payment> payment) {
        this.payment = payment;
    }

    public List<Totalinventory> getTotalinventory() {
        return totalInventory;
    }

    public void setTotalinventory(List<Totalinventory> totalinventory) {
        this.totalInventory = totalinventory;
    }

    public List<RosterAttachItem> getAttacheditems() {
        return attachedItems;
    }

    public void setAttacheditems(List<RosterAttachItem> attacheditems) {
        this.attachedItems = attacheditems;
    }

    public ClientDetail getClientdetail() {
        return clientDetail;
    }

    public void setClientdetail(ClientDetail clientdetail) {
        this.clientDetail = clientdetail;
    }

    public ClientCharge getClientcharge() {
        return clientCharge;
    }

    public void setClientcharge(ClientCharge clientcharge) {
        this.clientCharge = clientcharge;
    }

    public JobDetail getJobdetail() {
        return jobDetail;
    }

    public void setJobdetail(JobDetail jobdetail) {
        this.jobDetail = jobdetail;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public List<JobAddress> getAddressDetails() {
        return addressDetail;
    }

    public void setAddressDetails(List<JobAddress> addressDetails) {
        this.addressDetail = addressDetails;
    }

    public Notes getNotes() {
        return notes;
    }

    public void setNotes(Notes notes) {
        this.notes = notes;
    }

    public ContactDetailsBean getContactDetails() {
        return contactDetails;
    }

    public void setContactDetails(ContactDetailsBean contactDetails) {
        this.contactDetails = contactDetails;
    }

    public static class ContactDetailsBean {
        /**
         * vehicleName : 福特
         * vehicleType : Truck-Pantech
         */

        private CrewVehicleBean crewVehicle;
        /**
         * email2 : 4242342342
         * email1 : 2423423424
         * notes : null
         * name : yangsuryang
         * phone2 : 424242424234
         * phone1 : 343243242
         */

        private List<ContactBean> contact;
        /**
         * phone : 4342344
         * name : a1sura1
         */

        private List<CrewBean> crew;

        public CrewVehicleBean getCrewVehicle() {
            return crewVehicle;
        }

        public void setCrewVehicle(CrewVehicleBean crewVehicle) {
            this.crewVehicle = crewVehicle;
        }

        public List<ContactBean> getContact() {
            return contact;
        }

        public void setContact(List<ContactBean> contact) {
            this.contact = contact;
        }

        public List<CrewBean> getCrew() {
            return crew;
        }

        public void setCrew(List<CrewBean> crew) {
            this.crew = crew;
        }

        public static class CrewVehicleBean {
            private String vehicleName;
            private String vehicleType;

            public String getVehicleName() {
                return vehicleName;
            }

            public void setVehicleName(String vehicleName) {
                this.vehicleName = vehicleName;
            }

            public String getVehicleType() {
                return vehicleType;
            }

            public void setVehicleType(String vehicleType) {
                this.vehicleType = vehicleType;
            }
        }

        public static class ContactBean {
            private String email2;
            private String email1;
            private String notes;
            private String name;
            private String phone2;
            private String phone1;

            public String getEmail2() {
                return email2;
            }

            public void setEmail2(String email2) {
                this.email2 = email2;
            }

            public String getEmail1() {
                return email1;
            }

            public void setEmail1(String email1) {
                this.email1 = email1;
            }

            public String getNotes() {
                return notes;
            }

            public void setNotes(String notes) {
                this.notes = notes;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPhone2() {
                return phone2;
            }

            public void setPhone2(String phone2) {
                this.phone2 = phone2;
            }

            public String getPhone1() {
                return phone1;
            }

            public void setPhone1(String phone1) {
                this.phone1 = phone1;
            }
        }

        public static class CrewBean {
            private String phone;
            private String name;

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
