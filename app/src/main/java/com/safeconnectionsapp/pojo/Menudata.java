package com.safeconnectionsapp.pojo;

/**
 * Created by SATHISH on 29-Jun-17.
 */

public class Menudata {
    String menuid, menuname, image;
    boolean selectionstatus;



    public Menudata(String menuid, String menuname, String image, boolean selectionstatus) {
        this.menuid = menuid;
        this.menuname = menuname;
        this.image = image;
        this.selectionstatus = selectionstatus;
    }

    public String getMenuid() {
        return menuid;
    }

    public void setMenuid(String menuid) {
        this.menuid = menuid;
    }

    public String getMenuname() {
        return menuname;
    }

    public void setMenuname(String menuname) {
        this.menuname = menuname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isSelectionstatus() {
        return selectionstatus;
    }

    public void setSelectionstatus(boolean selectionstatus) {
        this.selectionstatus = selectionstatus;
    }
}


