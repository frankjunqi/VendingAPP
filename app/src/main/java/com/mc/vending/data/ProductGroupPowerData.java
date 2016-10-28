package com.mc.vending.data;

import java.io.Serializable;

public class ProductGroupPowerData extends BaseParseData implements Serializable {
    public static final String GROUP_POWER_NO = "1";
    public static final String GROUP_POWER_YES = "0";
    private static final long serialVersionUID = -7560204188382045659L;
    private String pp1Cd1Id;
    private String pp1CreateTime;
    private String pp1CreateUser;
    private String pp1Cu1Id;
    private String pp1Id;
    private String pp1IntervalFinish;
    private String pp1IntervalStart;
    private String pp1M02Id;
    private String pp1ModifyTime;
    private String pp1ModifyUser;
    private String pp1Period;
    private Integer pp1PeriodNum;
    private String pp1Pg1Id;
    private String pp1Power;
    private String pp1RowVersion;
    private String pp1StartDate;

    public String getPp1M02Id() {
        return this.pp1M02Id;
    }

    public void setPp1M02Id(String pp1m02Id) {
        this.pp1M02Id = pp1m02Id;
    }

    public String getPp1Cu1Id() {
        return this.pp1Cu1Id;
    }

    public void setPp1Cu1Id(String pp1Cu1Id) {
        this.pp1Cu1Id = pp1Cu1Id;
    }

    public String getPp1Id() {
        return this.pp1Id;
    }

    public void setPp1Id(String pp1Id) {
        this.pp1Id = pp1Id;
    }

    public String getPp1Pg1Id() {
        return this.pp1Pg1Id;
    }

    public void setPp1Pg1Id(String pp1Pg1Id) {
        this.pp1Pg1Id = pp1Pg1Id;
    }

    public String getPp1Cd1Id() {
        return this.pp1Cd1Id;
    }

    public void setPp1Cd1Id(String pp1Cd1Id) {
        this.pp1Cd1Id = pp1Cd1Id;
    }

    public String getPp1Power() {
        return this.pp1Power;
    }

    public void setPp1Power(String pp1Power) {
        this.pp1Power = pp1Power;
    }

    public String getPp1Period() {
        return this.pp1Period;
    }

    public void setPp1Period(String pp1Period) {
        this.pp1Period = pp1Period;
    }

    public String getPp1IntervalStart() {
        return this.pp1IntervalStart;
    }

    public void setPp1IntervalStart(String pp1IntervalStart) {
        this.pp1IntervalStart = pp1IntervalStart;
    }

    public String getPp1IntervalFinish() {
        return this.pp1IntervalFinish;
    }

    public void setPp1IntervalFinish(String pp1IntervalFinish) {
        this.pp1IntervalFinish = pp1IntervalFinish;
    }

    public String getPp1StartDate() {
        return this.pp1StartDate;
    }

    public void setPp1StartDate(String pp1StartDate) {
        this.pp1StartDate = pp1StartDate;
    }

    public Integer getPp1PeriodNum() {
        return this.pp1PeriodNum;
    }

    public void setPp1PeriodNum(Integer pp1PeriodNum) {
        this.pp1PeriodNum = pp1PeriodNum;
    }

    public String getPp1CreateUser() {
        return this.pp1CreateUser;
    }

    public void setPp1CreateUser(String pp1CreateUser) {
        this.pp1CreateUser = pp1CreateUser;
    }

    public String getPp1CreateTime() {
        return this.pp1CreateTime;
    }

    public void setPp1CreateTime(String pp1CreateTime) {
        this.pp1CreateTime = pp1CreateTime;
    }

    public String getPp1ModifyUser() {
        return this.pp1ModifyUser;
    }

    public void setPp1ModifyUser(String pp1ModifyUser) {
        this.pp1ModifyUser = pp1ModifyUser;
    }

    public String getPp1ModifyTime() {
        return this.pp1ModifyTime;
    }

    public void setPp1ModifyTime(String pp1ModifyTime) {
        this.pp1ModifyTime = pp1ModifyTime;
    }

    public String getPp1RowVersion() {
        return this.pp1RowVersion;
    }

    public void setPp1RowVersion(String pp1RowVersion) {
        this.pp1RowVersion = pp1RowVersion;
    }

    public String toString() {
        return "ProductGroupPowerData [pp1M02Id=" + this.pp1M02Id + ", pp1Cu1Id=" + this.pp1Cu1Id + ", pp1Id=" + this.pp1Id + ", pp1Pg1Id=" + this.pp1Pg1Id + ", pp1Cd1Id=" + this.pp1Cd1Id + ", pp1Power=" + this.pp1Power + ", pp1Period=" + this.pp1Period + ", pp1IntervalStart=" + this.pp1IntervalStart + ", pp1IntervalFinish=" + this.pp1IntervalFinish + ", pp1StartDate=" + this.pp1StartDate + ", pp1PeriodNum=" + this.pp1PeriodNum + ", pp1CreateUser=" + this.pp1CreateUser + ", pp1CreateTime=" + this.pp1CreateTime + ", pp1ModifyUser=" + this.pp1ModifyUser + ", pp1ModifyTime=" + this.pp1ModifyTime + ", pp1RowVersion=" + this.pp1RowVersion + "]";
    }
}
