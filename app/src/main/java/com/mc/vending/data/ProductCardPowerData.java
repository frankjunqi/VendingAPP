package com.mc.vending.data;

import java.io.Serializable;

public class ProductCardPowerData extends BaseParseData implements Serializable {
    private static final long serialVersionUID = 7257563796867011252L;
    private String pc1CD1_ID;
    private String pc1CU1_ID;
    private String pc1ID;
    private String pc1IntervalFinish;
    private String pc1IntervalStart;
    private String pc1M02_ID;
    private String pc1OnceQty;
    private String pc1Period;
    private String pc1PeriodQty;
    private String pc1Power;
    private String pc1StartDate;
    private String pc1VD1_ID;
    private String pc1VP1_ID;

    public String getPc1VD1_ID() {
        return this.pc1VD1_ID;
    }

    public void setPc1VD1_ID(String pc1vd1_ID) {
        this.pc1VD1_ID = pc1vd1_ID;
    }

    public String getPc1ID() {
        return this.pc1ID;
    }

    public void setPc1ID(String pc1id) {
        this.pc1ID = pc1id;
    }

    public String getPc1M02_ID() {
        return this.pc1M02_ID;
    }

    public void setPc1M02_ID(String pc1m02_ID) {
        this.pc1M02_ID = pc1m02_ID;
    }

    public String getPc1CU1_ID() {
        return this.pc1CU1_ID;
    }

    public void setPc1CU1_ID(String pc1cu1_ID) {
        this.pc1CU1_ID = pc1cu1_ID;
    }

    public String getPc1CD1_ID() {
        return this.pc1CD1_ID;
    }

    public void setPc1CD1_ID(String pc1cd1_ID) {
        this.pc1CD1_ID = pc1cd1_ID;
    }

    public String getPc1VP1_ID() {
        return this.pc1VP1_ID;
    }

    public void setPc1VP1_ID(String pc1vp1_ID) {
        this.pc1VP1_ID = pc1vp1_ID;
    }

    public String getPc1Power() {
        return this.pc1Power;
    }

    public void setPc1Power(String pc1Power) {
        this.pc1Power = pc1Power;
    }

    public String getPc1OnceQty() {
        return this.pc1OnceQty;
    }

    public void setPc1OnceQty(String pc1OnceQty) {
        this.pc1OnceQty = pc1OnceQty;
    }

    public String getPc1Period() {
        return this.pc1Period;
    }

    public void setPc1Period(String pc1Period) {
        this.pc1Period = pc1Period;
    }

    public String getPc1IntervalStart() {
        return this.pc1IntervalStart;
    }

    public void setPc1IntervalStart(String pc1IntervalStart) {
        this.pc1IntervalStart = pc1IntervalStart;
    }

    public String getPc1IntervalFinish() {
        return this.pc1IntervalFinish;
    }

    public void setPc1IntervalFinish(String pc1IntervalFinish) {
        this.pc1IntervalFinish = pc1IntervalFinish;
    }

    public String getPc1StartDate() {
        return this.pc1StartDate;
    }

    public void setPc1StartDate(String pc1StartDate) {
        this.pc1StartDate = pc1StartDate;
    }

    public String getPc1PeriodQty() {
        return this.pc1PeriodQty;
    }

    public void setPc1PeriodQty(String pc1PeriodQty) {
        this.pc1PeriodQty = pc1PeriodQty;
    }

    public String toString() {
        return "ProductCardPowerData [pc1ID=" + this.pc1ID + ", pc1M02_ID=" + this.pc1M02_ID + ", pc1CU1_ID=" + this.pc1CU1_ID + ", pc1CD1_ID=" + this.pc1CD1_ID + ", pc1VP1_ID=" + this.pc1VP1_ID + ", pc1Power=" + this.pc1Power + ", pc1OnceQty=" + this.pc1OnceQty + ", pc1Period=" + this.pc1Period + ", pc1IntervalStart=" + this.pc1IntervalStart + ", pc1IntervalFinish=" + this.pc1IntervalFinish + ", pc1StartDate=" + this.pc1StartDate + ", pc1PeriodQty=" + this.pc1PeriodQty + "] ";
    }
}
