package com.safeconnectionsapp.pojo;

/**
 * Created by Satish Gadde on 18-06-2016.
 */
public class Category {


    int categoryid;
  String categoryname,imageurl;

    /*public Category(int categoryid, String categoryname, String imageurl) {
        this.categoryid = categoryid;
        this.categoryname = categoryname;
        this.imageurl = imageurl;
    }*/

    public Category(int categoryid, String categoryname, String imageurl) {
        this.categoryid = categoryid;
        this.categoryname = categoryname;
        this.imageurl = imageurl;


    }



    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
    }


    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }
	
}
