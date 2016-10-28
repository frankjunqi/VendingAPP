package com.mc.vending.data;

import java.io.Serializable;

public class InventoryHeadData implements Serializable {
    private static final long serialVersionUID = 2208124900220315884L;
    private String ih1Ce1Id;
    private String ih1CreateTime;
    private String ih1CreateUser;
    private String ih1IHcode;
    private String ih1Id;
    private String ih1M02Id;
    private String ih1ModifyTime;
    private String ih1ModifyUser;
    private String ih1PlanDate;
    private String ih1RowVersion;
    private String ih1Type;

    public String getIh1Id() {
        return this.ih1Id;
    }

    public void setIh1Id(String value) {
        this.ih1Id = value;
    }

    public String getIh1M02Id() {
        return this.ih1M02Id;
    }

    public void setIh1M02Id(String value) {
        this.ih1M02Id = value;
    }

    public String getIh1IHcode() {
        return this.ih1IHcode;
    }

    public void setIh1IHcode(String value) {
        this.ih1IHcode = value;
    }

    public String getIh1Type() {
        return this.ih1Type;
    }

    public void setIh1Type(String value) {
        this.ih1Type = value;
    }

    public String getIh1PlanDate() {
        return this.ih1PlanDate;
    }

    public void setIh1PlanDate(String value) {
        this.ih1PlanDate = value;
    }

    public String getIh1Ce1Id() {
        return this.ih1Ce1Id;
    }

    public void setIh1Ce1Id(String ih1Ce1Id) {
        this.ih1Ce1Id = ih1Ce1Id;
    }

    public String getIh1CreateUser() {
        return this.ih1CreateUser;
    }

    public void setIh1CreateUser(String value) {
        this.ih1CreateUser = value;
    }

    public String getIh1CreateTime() {
        return this.ih1CreateTime;
    }

    public void setIh1CreateTime(String value) {
        this.ih1CreateTime = value;
    }

    public String getIh1ModifyUser() {
        return this.ih1ModifyUser;
    }

    public void setIh1ModifyUser(String value) {
        this.ih1ModifyUser = value;
    }

    public String getIh1ModifyTime() {
        return this.ih1ModifyTime;
    }

    public void setIh1ModifyTime(String value) {
        this.ih1ModifyTime = value;
    }

    public String getIh1RowVersion() {
        return this.ih1RowVersion;
    }

    public void setIh1RowVersion(String value) {
        this.ih1RowVersion = value;
    }
}
