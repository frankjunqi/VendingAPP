package com.mc.vending.data;

import java.io.Serializable;

public class CustomerEmpLinkData extends BaseParseData implements Serializable {
    public static final String CUS_EMP_STATUS_DISABLE = "2";
    public static final String CUS_EMP_STATUS_ENABLE = "1";
    private static final long serialVersionUID = 8737298947585844776L;
    private String ce1Code;
    private String ce1CreateTime;
    private String ce1CreateUser;
    private String ce1Cu1Id;
    private String ce1DicIdJob;
    private String ce1Dp1Id;
    private String ce1EnglishName;
    private String ce1Id;
    private String ce1M02Id;
    private String ce1ModifyTime;
    private String ce1ModifyUser;
    private String ce1Name;
    private String ce1Phone;
    private String ce1Remark;
    private String ce1RowVersion;
    private String ce1Sex;
    private String ce1Status;

    public String getCe1Id() {
        return this.ce1Id;
    }

    public void setCe1Id(String value) {
        this.ce1Id = value;
    }

    public String getCe1M02Id() {
        return this.ce1M02Id;
    }

    public void setCe1M02Id(String value) {
        this.ce1M02Id = value;
    }

    public String getCe1Cu1Id() {
        return this.ce1Cu1Id;
    }

    public void setCe1Cu1Id(String value) {
        this.ce1Cu1Id = value;
    }

    public String getCe1Code() {
        return this.ce1Code;
    }

    public void setCe1Code(String value) {
        this.ce1Code = value;
    }

    public String getCe1Name() {
        return this.ce1Name;
    }

    public void setCe1Name(String value) {
        this.ce1Name = value;
    }

    public String getCe1EnglishName() {
        return this.ce1EnglishName;
    }

    public void setCe1EnglishName(String value) {
        this.ce1EnglishName = value;
    }

    public String getCe1Sex() {
        return this.ce1Sex;
    }

    public void setCe1Sex(String value) {
        this.ce1Sex = value;
    }

    public String getCe1Dp1Id() {
        return this.ce1Dp1Id;
    }

    public void setCe1Dp1Id(String value) {
        this.ce1Dp1Id = value;
    }

    public String getCe1DicIdJob() {
        return this.ce1DicIdJob;
    }

    public void setCe1DicIdJob(String value) {
        this.ce1DicIdJob = value;
    }

    public String getCe1Phone() {
        return this.ce1Phone;
    }

    public void setCe1Phone(String value) {
        this.ce1Phone = value;
    }

    public String getCe1Status() {
        return this.ce1Status;
    }

    public void setCe1Status(String value) {
        this.ce1Status = value;
    }

    public String getCe1Remark() {
        return this.ce1Remark;
    }

    public void setCe1Remark(String value) {
        this.ce1Remark = value;
    }

    public String getCe1CreateUser() {
        return this.ce1CreateUser;
    }

    public void setCe1CreateUser(String value) {
        this.ce1CreateUser = value;
    }

    public String getCe1CreateTime() {
        return this.ce1CreateTime;
    }

    public void setCe1CreateTime(String value) {
        this.ce1CreateTime = value;
    }

    public String getCe1ModifyUser() {
        return this.ce1ModifyUser;
    }

    public void setCe1ModifyUser(String value) {
        this.ce1ModifyUser = value;
    }

    public String getCe1ModifyTime() {
        return this.ce1ModifyTime;
    }

    public void setCe1ModifyTime(String value) {
        this.ce1ModifyTime = value;
    }

    public String getCe1RowVersion() {
        return this.ce1RowVersion;
    }

    public void setCe1RowVersion(String value) {
        this.ce1RowVersion = value;
    }

    public String toString() {
        return "CustomerEmpLinkData [ce1Id=" + this.ce1Id + ", ce1M02Id=" + this.ce1M02Id + ", ce1Cu1Id=" + this.ce1Cu1Id + ", ce1Code=" + this.ce1Code + ", ce1Name=" + this.ce1Name + ", ce1EnglishName=" + this.ce1EnglishName + ", ce1Sex=" + this.ce1Sex + ", ce1Dp1Id=" + this.ce1Dp1Id + ", ce1DicIdJob=" + this.ce1DicIdJob + ", ce1Phone=" + this.ce1Phone + ", ce1Status=" + this.ce1Status + ", ce1Remark=" + this.ce1Remark + ", ce1CreateUser=" + this.ce1CreateUser + ", ce1CreateTime=" + this.ce1CreateTime + ", ce1ModifyUser=" + this.ce1ModifyUser + ", ce1ModifyTime=" + this.ce1ModifyTime + ", ce1RowVersion=" + this.ce1RowVersion + "]";
    }
}
