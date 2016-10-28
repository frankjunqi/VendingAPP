package com.mc.vending.data;

import java.io.Serializable;
import java.util.List;

public class ProductGroupHeadData extends BaseParseData implements Serializable {
    private static final long serialVersionUID = -2797875315912200009L;
    private List<ProductGroupDetailData> children;
    private String pg1Code;
    private String pg1CreateTime;
    private String pg1CreateUser;
    private String pg1Cu1Id;
    private String pg1Id;
    private String pg1M02Id;
    private String pg1ModifyTime;
    private String pg1ModifyUser;
    private String pg1Name;
    private String pg1RowVersion;

    public String getPg1Id() {
        return this.pg1Id;
    }

    public void setPg1Id(String value) {
        this.pg1Id = value;
    }

    public String getPg1M02Id() {
        return this.pg1M02Id;
    }

    public void setPg1M02Id(String value) {
        this.pg1M02Id = value;
    }

    public String getPg1Cu1Id() {
        return this.pg1Cu1Id;
    }

    public void setPg1Cu1Id(String value) {
        this.pg1Cu1Id = value;
    }

    public String getPg1Code() {
        return this.pg1Code;
    }

    public void setPg1Code(String value) {
        this.pg1Code = value;
    }

    public String getPg1Name() {
        return this.pg1Name;
    }

    public void setPg1Name(String value) {
        this.pg1Name = value;
    }

    public String getPg1CreateUser() {
        return this.pg1CreateUser;
    }

    public void setPg1CreateUser(String value) {
        this.pg1CreateUser = value;
    }

    public String getPg1CreateTime() {
        return this.pg1CreateTime;
    }

    public void setPg1CreateTime(String value) {
        this.pg1CreateTime = value;
    }

    public String getPg1ModifyUser() {
        return this.pg1ModifyUser;
    }

    public void setPg1ModifyUser(String value) {
        this.pg1ModifyUser = value;
    }

    public String getPg1ModifyTime() {
        return this.pg1ModifyTime;
    }

    public void setPg1ModifyTime(String value) {
        this.pg1ModifyTime = value;
    }

    public String getPg1RowVersion() {
        return this.pg1RowVersion;
    }

    public void setPg1RowVersion(String value) {
        this.pg1RowVersion = value;
    }

    public List<ProductGroupDetailData> getChildren() {
        return this.children;
    }

    public void setChildren(List<ProductGroupDetailData> children) {
        this.children = children;
    }

    public String toString() {
        return "ProductGroupHeadData [pg1Id=" + this.pg1Id + ", pg1M02Id=" + this.pg1M02Id + ", pg1Cu1Id=" + this.pg1Cu1Id + ", pg1Code=" + this.pg1Code + ", pg1Name=" + this.pg1Name + ", pg1CreateUser=" + this.pg1CreateUser + ", pg1CreateTime=" + this.pg1CreateTime + ", pg1ModifyUser=" + this.pg1ModifyUser + ", pg1ModifyTime=" + this.pg1ModifyTime + ", pg1RowVersion=" + this.pg1RowVersion + ", children=" + this.children + "]";
    }
}
