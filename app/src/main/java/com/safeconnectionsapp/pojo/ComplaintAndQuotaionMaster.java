package com.safeconnectionsapp.pojo;

/**
 * Created by PC2 on 27-Mar-17.
 */

public class ComplaintAndQuotaionMaster {




    String complaintId, categoryname, complaintdescr, complaintDate, complaintTime,totalamount,quantity,price,imageurl,complainttype;
    boolean complaintStatus;


    public ComplaintAndQuotaionMaster(String complaintId,String complaintdescr ,String imageurl,boolean status,String type)
    {

        this.complaintId = complaintId;
        this.imageurl = imageurl;
        this.complaintdescr = complaintdescr;
        this.complaintStatus = status;
        this.complainttype = type;



    }
    public ComplaintAndQuotaionMaster(String complaintId, String categoryname, String complaintdescr, String complaintDate, String complaintTime, String totalamount, String quantity, String price, String imageurl) {
        this.complaintId = complaintId;
        this.categoryname = categoryname;
        this.complaintdescr = complaintdescr;
        this.complaintDate = complaintDate;
        this.complaintTime = complaintTime;
        this.totalamount = totalamount;
        this.quantity = quantity;
        this.price = price;
        this.imageurl = imageurl;
    }

    public ComplaintAndQuotaionMaster(String complaintId, String categoryname, String complaintdescr, String complaintDate, String complaintTime) {
        this.complaintId = complaintId;
        this.categoryname = categoryname;
        this.complaintdescr = complaintdescr;
        this.complaintDate = complaintDate;
        this.complaintTime = complaintTime;
    }

    public String getComplainttype() {
        return complainttype;
    }

    public void setComplainttype(String complainttype) {
        this.complainttype = complainttype;
    }

    public boolean isComplaintStatus() {
        return complaintStatus;
    }

    public void setComplaintStatus(boolean complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
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

