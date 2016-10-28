package com.mc.vending.data;

import java.io.Serializable;

public class UsedRecordData extends BaseParseData implements Serializable {
    public static final String UPLOAD_LOAD = "1";
    public static final String UPLOAD_UNLOAD = "0";
    private static final long serialVersionUID = 1270490246534021822L;
    private String uploadStatus;
    private String ur1CD1_ID;
    private String ur1ID;
    private String ur1M02_ID;
    private String ur1PD1_ID;
    private String ur1Quantity;
    private String ur1VD1_ID;

    public String getUr1ID() {
        return this.ur1ID;
    }

    public void setUr1ID(String ur1id) {
        this.ur1ID = ur1id;
    }

    public String getUr1M02_ID() {
        return this.ur1M02_ID;
    }

    public void setUr1M02_ID(String ur1m02_ID) {
        this.ur1M02_ID = ur1m02_ID;
    }

    public String getUr1CD1_ID() {
        return this.ur1CD1_ID;
    }

    public void setUr1CD1_ID(String ur1cd1_ID) {
        this.ur1CD1_ID = ur1cd1_ID;
    }

    public String getUr1VD1_ID() {
        return this.ur1VD1_ID;
    }

    public void setUr1VD1_ID(String ur1vd1_ID) {
        this.ur1VD1_ID = ur1vd1_ID;
    }

    public String getUr1PD1_ID() {
        return this.ur1PD1_ID;
    }

    public void setUr1PD1_ID(String ur1pd1_ID) {
        this.ur1PD1_ID = ur1pd1_ID;
    }

    public String getUr1Quantity() {
        return this.ur1Quantity;
    }

    public void setUr1Quantity(String ur1Quantity) {
        this.ur1Quantity = ur1Quantity;
    }

    public String getUploadStatus() {
        return this.uploadStatus;
    }

    public void setUploadStatus(String uploadStatus) {
        this.uploadStatus = uploadStatus;
    }

    public String toString() {
        return "UsedRecordData [ur1ID=" + this.ur1ID + ", ur1M02_ID=" + this.ur1M02_ID + ", ur1CD1_ID=" + this.ur1CD1_ID + ", ur1VD1_ID=" + this.ur1VD1_ID + ", ur1PD1_ID=" + this.ur1PD1_ID + ", ur1Quantity=" + this.ur1Quantity + ", uploadStatus=" + this.uploadStatus + "]";
    }
}
