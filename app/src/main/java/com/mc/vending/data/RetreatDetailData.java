package com.mc.vending.data;

import java.io.Serializable;

public class RetreatDetailData extends BaseParseData implements Serializable {
    private static final long serialVersionUID = -2864876118133865574L;
    private Integer rt2ActualQty;
    private String rt2Id;
    private String rt2M02Id;
    private String rt2Pd1Id;
    private Integer rt2PlanQty;
    private String rt2Rt1Id;
    private String rt2SaleType;
    private String rt2Sp1Id;
    private String rt2Vc1Code;

    public String getRt2Id() {
        return this.rt2Id;
    }

    public void setRt2Id(String value) {
        this.rt2Id = value;
    }

    public String getRt2M02Id() {
        return this.rt2M02Id;
    }

    public void setRt2M02Id(String value) {
        this.rt2M02Id = value;
    }

    public String getRt2Rt1Id() {
        return this.rt2Rt1Id;
    }

    public void setRt2Rt1Id(String value) {
        this.rt2Rt1Id = value;
    }

    public String getRt2Vc1Code() {
        return this.rt2Vc1Code;
    }

    public void setRt2Vc1Code(String value) {
        this.rt2Vc1Code = value;
    }

    public String getRt2Pd1Id() {
        return this.rt2Pd1Id;
    }

    public void setRt2Pd1Id(String value) {
        this.rt2Pd1Id = value;
    }

    public String getRt2SaleType() {
        return this.rt2SaleType;
    }

    public void setRt2SaleType(String value) {
        this.rt2SaleType = value;
    }

    public String getRt2Sp1Id() {
        return this.rt2Sp1Id;
    }

    public void setRt2Sp1Id(String value) {
        this.rt2Sp1Id = value;
    }

    public Integer getRt2PlanQty() {
        return this.rt2PlanQty;
    }

    public void setRt2PlanQty(Integer value) {
        this.rt2PlanQty = value;
    }

    public Integer getRt2ActualQty() {
        return this.rt2ActualQty;
    }

    public void setRt2ActualQty(Integer value) {
        this.rt2ActualQty = value;
    }

    public String toString() {
        return "RetreatDetailData [rt2Id=" + this.rt2Id + ", rt2M02Id=" + this.rt2M02Id + ", rt2Rt1Id=" + this.rt2Rt1Id + ", rt2Vc1Code=" + this.rt2Vc1Code + ", rt2Pd1Id=" + this.rt2Pd1Id + ", rt2SaleType=" + this.rt2SaleType + ", rt2Sp1Id=" + this.rt2Sp1Id + ", rt2PlanQty=" + this.rt2PlanQty + ", rt2ActualQty=" + this.rt2ActualQty + "]";
    }
}
