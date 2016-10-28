package com.mc.vending.data;

import java.io.Serializable;

public class SupplierData extends BaseParseData implements Serializable {
    private static final long serialVersionUID = 5658447978045405122L;
    private String sp1Code;
    private String sp1CreateTime;
    private String sp1CreateUser;
    private String sp1Id;
    private String sp1M02Id;
    private String sp1ModifyTime;
    private String sp1ModifyUser;
    private String sp1Name;
    private String sp1RowVersion;

    public String getSp1Id() {
        return this.sp1Id;
    }

    public void setSp1Id(String value) {
        this.sp1Id = value;
    }

    public String getSp1M02Id() {
        return this.sp1M02Id;
    }

    public void setSp1M02Id(String value) {
        this.sp1M02Id = value;
    }

    public String getSp1Code() {
        return this.sp1Code;
    }

    public void setSp1Code(String value) {
        this.sp1Code = value;
    }

    public String getSp1Name() {
        return this.sp1Name;
    }

    public void setSp1Name(String value) {
        this.sp1Name = value;
    }

    public String getSp1CreateUser() {
        return this.sp1CreateUser;
    }

    public void setSp1CreateUser(String value) {
        this.sp1CreateUser = value;
    }

    public String getSp1CreateTime() {
        return this.sp1CreateTime;
    }

    public void setSp1CreateTime(String value) {
        this.sp1CreateTime = value;
    }

    public String getSp1ModifyUser() {
        return this.sp1ModifyUser;
    }

    public void setSp1ModifyUser(String value) {
        this.sp1ModifyUser = value;
    }

    public String getSp1ModifyTime() {
        return this.sp1ModifyTime;
    }

    public void setSp1ModifyTime(String value) {
        this.sp1ModifyTime = value;
    }

    public String getSp1RowVersion() {
        return this.sp1RowVersion;
    }

    public void setSp1RowVersion(String value) {
        this.sp1RowVersion = value;
    }

    public String toString() {
        return "SupplierData [sp1Id=" + this.sp1Id + ", sp1M02Id=" + this.sp1M02Id + ", sp1Code=" + this.sp1Code + ", sp1Name=" + this.sp1Name + ", sp1CreateUser=" + this.sp1CreateUser + ", sp1CreateTime=" + this.sp1CreateTime + ", sp1ModifyUser=" + this.sp1ModifyUser + ", sp1ModifyTime=" + this.sp1ModifyTime + ", sp1RowVersion=" + this.sp1RowVersion + "]";
    }
}
