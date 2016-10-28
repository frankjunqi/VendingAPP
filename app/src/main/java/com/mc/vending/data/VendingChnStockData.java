package com.mc.vending.data;

import java.io.Serializable;

public class VendingChnStockData extends BaseParseData implements Serializable {
    private static final long serialVersionUID = -2632596560892226177L;
    private String vs1CreateTime;
    private String vs1CreateUser;
    private String vs1Id;
    private String vs1M02Id;
    private String vs1ModifyTime;
    private String vs1ModifyUser;
    private String vs1Pd1Id;
    private Integer vs1Quantity;
    private String vs1RowVersion;
    private String vs1Vc1Code;
    private String vs1Vd1Id;

    public String getVs1Id() {
        return this.vs1Id;
    }

    public void setVs1Id(String value) {
        this.vs1Id = value;
    }

    public String getVs1M02Id() {
        return this.vs1M02Id;
    }

    public void setVs1M02Id(String value) {
        this.vs1M02Id = value;
    }

    public String getVs1Vd1Id() {
        return this.vs1Vd1Id;
    }

    public void setVs1Vd1Id(String value) {
        this.vs1Vd1Id = value;
    }

    public String getVs1Vc1Code() {
        return this.vs1Vc1Code;
    }

    public void setVs1Vc1Code(String value) {
        this.vs1Vc1Code = value;
    }

    public String getVs1Pd1Id() {
        return this.vs1Pd1Id;
    }

    public void setVs1Pd1Id(String value) {
        this.vs1Pd1Id = value;
    }

    public Integer getVs1Quantity() {
        return this.vs1Quantity;
    }

    public void setVs1Quantity(Integer value) {
        this.vs1Quantity = value;
    }

    public String getVs1CreateUser() {
        return this.vs1CreateUser;
    }

    public void setVs1CreateUser(String value) {
        this.vs1CreateUser = value;
    }

    public String getVs1CreateTime() {
        return this.vs1CreateTime;
    }

    public void setVs1CreateTime(String value) {
        this.vs1CreateTime = value;
    }

    public String getVs1ModifyUser() {
        return this.vs1ModifyUser;
    }

    public void setVs1ModifyUser(String value) {
        this.vs1ModifyUser = value;
    }

    public String getVs1ModifyTime() {
        return this.vs1ModifyTime;
    }

    public void setVs1ModifyTime(String value) {
        this.vs1ModifyTime = value;
    }

    public String getVs1RowVersion() {
        return this.vs1RowVersion;
    }

    public void setVs1RowVersion(String value) {
        this.vs1RowVersion = value;
    }

    public String toString() {
        return "VendingChnStockData [vs1Id=" + this.vs1Id + ", vs1M02Id=" + this.vs1M02Id + ", vs1Vd1Id=" + this.vs1Vd1Id + ", vs1Vc1Code=" + this.vs1Vc1Code + ", vs1Pd1Id=" + this.vs1Pd1Id + ", vs1Quantity=" + this.vs1Quantity + ", vs1CreateUser=" + this.vs1CreateUser + ", vs1CreateTime=" + this.vs1CreateTime + ", vs1ModifyUser=" + this.vs1ModifyUser + ", vs1ModifyTime=" + this.vs1ModifyTime + ", vs1RowVersion=" + this.vs1RowVersion + "]";
    }
}
