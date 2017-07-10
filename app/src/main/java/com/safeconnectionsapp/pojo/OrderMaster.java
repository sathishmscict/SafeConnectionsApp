package com.safeconnectionsapp.pojo;

/**
 * Created by SATHISH on 07-Jul-17.
 */

public class OrderMaster {

    String orderdate , totalamount ,orderid , productname,product_qty , price,image;

    public OrderMaster(String orderdate, String totalamount, String orderid, String productname, String product_qty, String price, String image) {
        this.orderdate = orderdate;
        this.totalamount = totalamount;
        this.orderid = orderid;
        this.productname = productname;
        this.product_qty = product_qty;
        this.price = price;
        this.image = image;
    }


    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
