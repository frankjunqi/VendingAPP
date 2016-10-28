package com.mc.vending.data;

import java.io.Serializable;

public class ProductGroupDetailData implements Serializable {
    private static final long serialVersionUID = -2932329074755520269L;
    private String pg2CreateTime;
    private String pg2CreateUser;
    private Integer pg2GroupQty;
    private String pg2Id;
    private String pg2M02Id;
    private String pg2ModifyTime;
    private String pg2ModifyUser;
    private String pg2Pd1Id;
    private String pg2Pg1Id;
    private String pg2RowVersion;

    public String getPg2Id() {
        return this.pg2Id;
    }

    public void setPg2Id(String value) {
        this.pg2Id = value;
    }

    public String getPg2M02Id() {
        return this.pg2M02Id;
    }

    public void setPg2M02Id(String value) {
        this.pg2M02Id = value;
    }

    public String getPg2Pg1Id() {
        return this.pg2Pg1Id;
    }

    public void setPg2Pg1Id(String value) {
        this.pg2Pg1Id = value;
    }

    public String getPg2Pd1Id() {
        return this.pg2Pd1Id;
    }

    public void setPg2Pd1Id(String value) {
        this.pg2Pd1Id = value;
    }

    public Integer getPg2GroupQty() {
        return this.pg2GroupQty;
    }

    public void setPg2GroupQty(Integer value) {
        this.pg2GroupQty = value;
    }

    public String getPg2CreateUser() {
        return this.pg2CreateUser;
    }

    public void setPg2CreateUser(String value) {
        this.pg2CreateUser = value;
    }

    public String getPg2CreateTime() {
        return this.pg2CreateTime;
    }

    public void setPg2CreateTime(String value) {
        this.pg2CreateTime = value;
    }

    public String getPg2ModifyUser() {
        return this.pg2ModifyUser;
    }

    public void setPg2ModifyUser(String value) {
        this.pg2ModifyUser = value;
    }

    public String getPg2ModifyTime() {
        return this.pg2ModifyTime;
    }

    public void setPg2ModifyTime(String value) {
        this.pg2ModifyTime = value;
    }

    public String getPg2RowVersion() {
        return this.pg2RowVersion;
    }

    public void setPg2RowVersion(String value) {
        this.pg2RowVersion = value;
    }

    public String toString() {
        return "ProductGroupDetailData [pg2Id=" + this.pg2Id + ", pg2M02Id=" + this.pg2M02Id + ", pg2Pg1Id=" + this.pg2Pg1Id + ", pg2Pd1Id=" + this.pg2Pd1Id + ", pg2GroupQty=" + this.pg2GroupQty + ", pg2CreateUser=" + this.pg2CreateUser + ", pg2CreateTime=" + this.pg2CreateTime + ", pg2ModifyUser=" + this.pg2ModifyUser + ", pg2ModifyTime=" + this.pg2ModifyTime + ", pg2RowVersion=" + this.pg2RowVersion + "]";
    }
}
