package com.mc.vending.data;

import java.io.Serializable;
import java.util.List;

public class RetreatHeadData extends BaseParseData implements Serializable {
    public static final String RETREAT_STATUS_CREATE = "0";
    public static final String RETREAT_STATUS_FINISH = "1";
    public static final String RETREAT_TYPE_POSITIVE = "0";
    public static final String RETREAT_TYPE_REVERSE = "1";
    private static final long serialVersionUID = -7560204188382045659L;
    private List<RetreatDetailData> retreatDetailDatas;
    private String rt1Ce1Id;
    private String rt1Cu1Id;
    private String rt1Id;
    private String rt1M02Id;
    private String rt1Rtcode;
    private String rt1Status;
    private String rt1Type;
    private String rt1Vd1Id;

    public String getRt1Id() {
        return this.rt1Id;
    }

    public void setRt1Id(String value) {
        this.rt1Id = value;
    }

    public String getRt1M02Id() {
        return this.rt1M02Id;
    }

    public void setRt1M02Id(String value) {
        this.rt1M02Id = value;
    }

    public String getRt1Rtcode() {
        return this.rt1Rtcode;
    }

    public void setRt1Rtcode(String value) {
        this.rt1Rtcode = value;
    }

    public String getRt1Type() {
        return this.rt1Type;
    }

    public void setRt1Type(String value) {
        this.rt1Type = value;
    }

    public String getRt1Cu1Id() {
        return this.rt1Cu1Id;
    }

    public void setRt1Cu1Id(String value) {
        this.rt1Cu1Id = value;
    }

    public String getRt1Vd1Id() {
        return this.rt1Vd1Id;
    }

    public void setRt1Vd1Id(String value) {
        this.rt1Vd1Id = value;
    }

    public String getRt1Ce1Id() {
        return this.rt1Ce1Id;
    }

    public void setRt1Ce1Id(String rt1Ce1Id) {
        this.rt1Ce1Id = rt1Ce1Id;
    }

    public String getRt1Status() {
        return this.rt1Status;
    }

    public void setRt1Status(String value) {
        this.rt1Status = value;
    }

    public List<RetreatDetailData> getRetreatDetailDatas() {
        return this.retreatDetailDatas;
    }

    public void setRetreatDetailDatas(List<RetreatDetailData> retreatDetailDatas) {
        this.retreatDetailDatas = retreatDetailDatas;
    }

    public String toString() {
        return "RetreatHeadData [rt1Id=" + this.rt1Id + ", rt1M02Id=" + this.rt1M02Id + ", rt1Rtcode=" + this.rt1Rtcode + ", rt1Type=" + this.rt1Type + ", rt1Cu1Id=" + this.rt1Cu1Id + ", rt1Vd1Id=" + this.rt1Vd1Id + ", rt1Ce1Id=" + this.rt1Ce1Id + ", rt1Status=" + this.rt1Status + "]";
    }
}
