package com.mc.vending.data;

import java.io.Serializable;

public class ProductPictureData extends BaseParseData implements Serializable {
    private static final long serialVersionUID = 7136345374079474515L;
    private String pp1CreateTime;
    private String pp1CreateUser;
    private String pp1FilePath;
    private String pp1Id;
    private String pp1M02Id;
    private String pp1ModifyTime;
    private String pp1ModifyUser;
    private String pp1Pd1Id;
    private String pp1RowVersion;

    public String getPp1Id() {
        return this.pp1Id;
    }

    public void setPp1Id(String value) {
        this.pp1Id = value;
    }

    public String getPp1M02Id() {
        return this.pp1M02Id;
    }

    public void setPp1M02Id(String value) {
        this.pp1M02Id = value;
    }

    public String getPp1Pd1Id() {
        return this.pp1Pd1Id;
    }

    public void setPp1Pd1Id(String value) {
        this.pp1Pd1Id = value;
    }

    public String getPp1FilePath() {
        return this.pp1FilePath;
    }

    public void setPp1FilePath(String value) {
        this.pp1FilePath = value;
    }

    public String getPp1CreateUser() {
        return this.pp1CreateUser;
    }

    public void setPp1CreateUser(String value) {
        this.pp1CreateUser = value;
    }

    public String getPp1CreateTime() {
        return this.pp1CreateTime;
    }

    public void setPp1CreateTime(String value) {
        this.pp1CreateTime = value;
    }

    public String getPp1ModifyUser() {
        return this.pp1ModifyUser;
    }

    public void setPp1ModifyUser(String value) {
        this.pp1ModifyUser = value;
    }

    public String getPp1ModifyTime() {
        return this.pp1ModifyTime;
    }

    public void setPp1ModifyTime(String value) {
        this.pp1ModifyTime = value;
    }

    public String getPp1RowVersion() {
        return this.pp1RowVersion;
    }

    public void setPp1RowVersion(String value) {
        this.pp1RowVersion = value;
    }

    public String toString() {
        return "ProductPictureData [pp1Id=" + this.pp1Id + ", pp1M02Id=" + this.pp1M02Id + ", pp1Pd1Id=" + this.pp1Pd1Id + ", pp1FilePath=" + this.pp1FilePath + ", pp1CreateUser=" + this.pp1CreateUser + ", pp1CreateTime=" + this.pp1CreateTime + ", pp1ModifyUser=" + this.pp1ModifyUser + ", pp1ModifyTime=" + this.pp1ModifyTime + ", pp1RowVersion=" + this.pp1RowVersion + "]";
    }
}
