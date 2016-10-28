package com.mc.vending.data;

import java.io.Serializable;

public class VendingProLinkData extends BaseParseData implements Serializable {
    private static final long serialVersionUID = -2869075970367560196L;
    private String vp1CreateTime;
    private String vp1CreateUser;
    private String vp1Id;
    private String vp1M02Id;
    private String vp1ModifyTime;
    private String vp1ModifyUser;
    private String vp1Pd1Id;
    private Integer vp1PromptValue;
    private String vp1RowVersion;
    private String vp1Vd1Id;
    private Integer vp1WarningValue;

    public String getVp1Id() {
        return this.vp1Id;
    }

    public void setVp1Id(String value) {
        this.vp1Id = value;
    }

    public String getVp1M02Id() {
        return this.vp1M02Id;
    }

    public void setVp1M02Id(String value) {
        this.vp1M02Id = value;
    }

    public String getVp1Vd1Id() {
        return this.vp1Vd1Id;
    }

    public void setVp1Vd1Id(String value) {
        this.vp1Vd1Id = value;
    }

    public String getVp1Pd1Id() {
        return this.vp1Pd1Id;
    }

    public void setVp1Pd1Id(String value) {
        this.vp1Pd1Id = value;
    }

    public Integer getVp1PromptValue() {
        return this.vp1PromptValue;
    }

    public void setVp1PromptValue(Integer value) {
        this.vp1PromptValue = value;
    }

    public Integer getVp1WarningValue() {
        return this.vp1WarningValue;
    }

    public void setVp1WarningValue(Integer value) {
        this.vp1WarningValue = value;
    }

    public String getVp1CreateUser() {
        return this.vp1CreateUser;
    }

    public void setVp1CreateUser(String value) {
        this.vp1CreateUser = value;
    }

    public String getVp1CreateTime() {
        return this.vp1CreateTime;
    }

    public void setVp1CreateTime(String value) {
        this.vp1CreateTime = value;
    }

    public String getVp1ModifyUser() {
        return this.vp1ModifyUser;
    }

    public void setVp1ModifyUser(String value) {
        this.vp1ModifyUser = value;
    }

    public String getVp1ModifyTime() {
        return this.vp1ModifyTime;
    }

    public void setVp1ModifyTime(String value) {
        this.vp1ModifyTime = value;
    }

    public String getVp1RowVersion() {
        return this.vp1RowVersion;
    }

    public void setVp1RowVersion(String value) {
        this.vp1RowVersion = value;
    }

    public String toString() {
        return "VendingProLinkData [vp1Id=" + this.vp1Id + ", vp1M02Id=" + this.vp1M02Id + ", vp1Vd1Id=" + this.vp1Vd1Id + ", vp1Pd1Id=" + this.vp1Pd1Id + ", vp1PromptValue=" + this.vp1PromptValue + ", vp1WarningValue=" + this.vp1WarningValue + ", vp1CreateUser=" + this.vp1CreateUser + ", vp1CreateTime=" + this.vp1CreateTime + ", vp1ModifyUser=" + this.vp1ModifyUser + ", vp1ModifyTime=" + this.vp1ModifyTime + ", vp1RowVersion=" + this.vp1RowVersion + "]";
    }
}
