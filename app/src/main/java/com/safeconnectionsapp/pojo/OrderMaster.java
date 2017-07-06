package com.safeconnectionsapp.pojo;

/**
 * Created by PC2 on 27-Mar-17.
 */

public class OrderMaster {


    String complaintId, categoryname, complaintdescr, complaintDate, complaintTime;

    public OrderMaster(String complaintId, String categoryname, String complaintdescr, String complaintDate, String complaintTime) {
        this.complaintId = complaintId;
        this.categoryname = categoryname;
        this.complaintdescr = complaintdescr;
        this.complaintDate = complaintDate;
        this.complaintTime = complaintTime;
    }

    public String getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(String complaintId) {
        this.complaintId = complaintId;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getComplaintdescr() {
        return complaintdescr;
    }

    public void setComplaintdescr(String complaintdescr) {
        this.complaintdescr = complaintdescr;
    }

    public String getComplaintDate() {
        return complaintDate;
    }

    public void setComplaintDate(String complaintDate) {
        this.complaintDate = complaintDate;
    }

    public String getComplaintTime() {
        return complaintTime;
    }

    public void setComplaintTime(String complaintTime) {
        this.complaintTime = complaintTime;
    }
}

