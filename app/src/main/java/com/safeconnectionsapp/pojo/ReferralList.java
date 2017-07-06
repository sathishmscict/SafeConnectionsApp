package com.safeconnectionsapp.pojo;

/**
 * Created by SATHISH on 17-May-17.
 */

public class ReferralList {
    String name,price,mobile,datetime,status;

    public ReferralList(String name, String price, String mobile, String datetime, String status) {
        this.name = name;
        this.price = price;
        this.mobile = mobile;
        this.datetime = datetime;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
