package com.mc.vending.data;

import java.io.Serializable;

public class ProductMaterialPowerData extends BaseParseData implements Serializable {
    public static final String MATERIAL_POWER_NO = "1";
    public static final String MATERIAL_POWER_YES = "0";
    private static final long serialVersionUID = 6916042653966560600L;
    private String pm1CreateTime;
    private String pm1CreateUser;
    private String pm1Cu1Id;
    private String pm1Id;
    private String pm1IntervalFinish;
    private String pm1IntervalStart;
    private String pm1M02Id;
    private String pm1ModifyTime;
    private String pm1ModifyUser;
    private Integer pm1OnceQty;
    private String pm1Period;
    private Integer pm1PeriodQty;
    private String pm1Power;
    private String pm1RowVersion;
    private String pm1StartDate;
    private String pm1Vc2Id;
    private String pm1Vp1Id;

    public String getPm1Id() {
        return this.pm1Id;
    }

    public void setPm1Id(String value) {
        this.pm1Id = value;
    }

    public String getPm1M02Id() {
        return this.pm1M02Id;
    }

    public void setPm1M02Id(String value) {
        this.pm1M02Id = value;
    }

    public String getPm1Cu1Id() {
        return this.pm1Cu1Id;
    }

    public void setPm1Cu1Id(String value) {
        this.pm1Cu1Id = value;
    }

    public String getPm1Vc2Id() {
        return this.pm1Vc2Id;
    }

    public void setPm1Vc2Id(String value) {
        this.pm1Vc2Id = value;
    }

    public String getPm1Vp1Id() {
        return this.pm1Vp1Id;
    }

    public void setPm1Vp1Id(String value) {
        this.pm1Vp1Id = value;
    }

    public String getPm1Power() {
        return this.pm1Power;
    }

    public void setPm1Power(String value) {
        this.pm1Power = value;
    }

    public Integer getPm1OnceQty() {
        return this.pm1OnceQty;
    }

    public void setPm1OnceQty(Integer value) {
        this.pm1OnceQty = value;
    }

    public String getPm1Period() {
        return this.pm1Period;
    }

    public void setPm1Period(String value) {
        this.pm1Period = value;
    }

    public String getPm1IntervalStart() {
        return this.pm1IntervalStart;
    }

    public void setPm1IntervalStart(String value) {
        this.pm1IntervalStart = value;
    }

    public String getPm1IntervalFinish() {
        return this.pm1IntervalFinish;
    }

    public void setPm1IntervalFinish(String value) {
        this.pm1IntervalFinish = value;
    }

    public String getPm1StartDate() {
        return this.pm1StartDate;
    }

    public void setPm1StartDate(String value) {
        this.pm1StartDate = value;
    }

    public Integer getPm1PeriodQty() {
        return this.pm1PeriodQty;
    }

    public void setPm1PeriodQty(Integer value) {
        this.pm1PeriodQty = value;
    }

    public String getPm1CreateUser() {
        return this.pm1CreateUser;
    }

    public void setPm1CreateUser(String value) {
        this.pm1CreateUser = value;
    }

    public String getPm1CreateTime() {
        return this.pm1CreateTime;
    }

    public void setPm1CreateTime(String value) {
        this.pm1CreateTime = value;
    }

    public String getPm1ModifyUser() {
        return this.pm1ModifyUser;
    }

    public void setPm1ModifyUser(String value) {
        this.pm1ModifyUser = value;
    }

    public String getPm1ModifyTime() {
        return this.pm1ModifyTime;
    }

    public void setPm1ModifyTime(String value) {
        this.pm1ModifyTime = value;
    }

    public String getPm1RowVersion() {
        return this.pm1RowVersion;
    }

    public void setPm1RowVersion(String value) {
        this.pm1RowVersion = value;
    }

    public String toString() {
        return "ProductMaterialPowerData [pm1Id=" + this.pm1Id + ", pm1M02Id=" + this.pm1M02Id + ", pm1Cu1Id=" + this.pm1Cu1Id + ", pm1Vc2Id=" + this.pm1Vc2Id + ", pm1Vp1Id=" + this.pm1Vp1Id + ", pm1Power=" + this.pm1Power + ", pm1OnceQty=" + this.pm1OnceQty + ", pm1Period=" + this.pm1Period + ", pm1IntervalStart=" + this.pm1IntervalStart + ", pm1IntervalFinish=" + this.pm1IntervalFinish + ", pm1StartDate=" + this.pm1StartDate + ", pm1PeriodQty=" + this.pm1PeriodQty + ", pm1CreateUser=" + this.pm1CreateUser + ", pm1CreateTime=" + this.pm1CreateTime + ", pm1ModifyUser=" + this.pm1ModifyUser + ", pm1ModifyTime=" + this.pm1ModifyTime + ", pm1RowVersion=" + this.pm1RowVersion + "]";
    }
}
