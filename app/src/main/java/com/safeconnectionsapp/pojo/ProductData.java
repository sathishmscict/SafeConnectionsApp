package com.safeconnectionsapp.pojo;

/**
 * Created by Sathish Gadde on 30-Jan-17.
 */

public class ProductData {


    String productid;
    String category_id;
    String image_url;

    String price;

    String mrp;
    String ship_date;


    String productname;
    String description;





    String quantity;





    boolean selectionStatus;


    String userid;

    /*Construtor for display single product images in SingleItemActivity*/
    public ProductData(String image_url) {
        this.image_url = image_url;
    }
    public ProductData(String image_url, boolean selectionStatus) {
        this.image_url = image_url;
        this.selectionStatus = selectionStatus;
    }

    public ProductData(String productid, String pname, String image_url, String mrp, String price, String userid, String Quantity) {
        this.productid = productid;
        this.image_url = image_url;
        this.mrp = mrp;
        this.price = price;
        this.userid = userid;
        this.productname =pname;
        this.quantity = Quantity;


    }


    //Display Products in NHC APP
    public ProductData(String productid, String category_id, String image_url, String price,  String mrp,  String productname, String description, String quantity, boolean selectionStatus) {
        this.productid = productid;
        this.category_id = category_id;
        this.image_url = image_url;
        this.price = price;
        this.mrp = mrp;

        this.productname = productname;
        this.description = description;
        this.quantity = quantity;
        this.selectionStatus = selectionStatus;
    }

    public ProductData(String shippingcost, String productid, String productname, String description, String category_id, String image_url, String seller_id, String price, String offer_value, String offer_tag, String color, String weight, String height, String length, String width, String size, String mrp, String ship_date, String brand, String inventory, String committed_qty, String quantity, String mlmDiscount) {

        this.productid = productid;
        this.productname = productname;
        this.description = description;
        this.category_id = category_id;
        this.image_url = image_url;

        this.price = price;

        this.mrp = mrp;
        this.ship_date = ship_date;


        this.quantity = quantity;




    }




    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public boolean isSelectionStatus() {
        return selectionStatus;
    }

    public void setSelectionStatus(boolean selectionStatus) {
        this.selectionStatus = selectionStatus;
    }
    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }



    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }



    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getShip_date() {
        return ship_date;
    }

    public void setShip_date(String ship_date) {
        this.ship_date = ship_date;
    }



}
