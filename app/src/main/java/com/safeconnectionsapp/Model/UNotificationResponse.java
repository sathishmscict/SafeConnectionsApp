package com.safeconnectionsapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by SATHISH on 01-Jul-17.
 */

public class UNotificationResponse {

    @SerializedName("MESSAGE")
    @Expose
    private String mESSAGE;
    @SerializedName("ORIGINAL_ERROR")
    @Expose
    private String oRIGINALERROR;
    @SerializedName("ERROR_STATUS")
    @Expose
    private boolean eRRORSTATUS;
    @SerializedName("RECORDS")
    @Expose
    private boolean rECORDS;

    public String getMESSAGE() {
        return mESSAGE;
    }

    public void setMESSAGE(String mESSAGE) {
        this.mESSAGE = mESSAGE;
    }

    public String getORIGINALERROR() {
        return oRIGINALERROR;
    }

    public void setORIGINALERROR(String oRIGINALERROR) {
        this.oRIGINALERROR = oRIGINALERROR;
    }

    public boolean isERRORSTATUS() {
        return eRRORSTATUS;
    }

    public void setERRORSTATUS(boolean eRRORSTATUS) {
        this.eRRORSTATUS = eRRORSTATUS;
    }

    public boolean isRECORDS() {
        return rECORDS;
    }

    public void setRECORDS(boolean rECORDS) {
        this.rECORDS = rECORDS;
    }

}
