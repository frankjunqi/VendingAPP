package com.mc.vending.data;

import java.io.Serializable;

public class ConversionData extends BaseParseData implements Serializable {
    private static final long serialVersionUID = -7443975919101786265L;
    private String cn1Cpid;
    private String cn1CreateTime;
    private String cn1CreateUser;
    private String cn1Id;
    private String cn1ModifyTime;
    private String cn1ModifyUser;
    private String cn1Operation;
    private String cn1Proportion;
    private String cn1RowVersion;
    private String cn1Upid;

    public String getCn1Id() {
        return this.cn1Id;
    }

    public void setCn1Id(String cn1Id) {
        this.cn1Id = cn1Id;
    }

    public String getCn1Upid() {
        return this.cn1Upid;
    }

    public void setCn1Upid(String cn1Upid) {
        this.cn1Upid = cn1Upid;
    }

    public String getCn1Cpid() {
        return this.cn1Cpid;
    }

    public void setCn1Cpid(String cn1Cpid) {
        this.cn1Cpid = cn1Cpid;
    }

    public String getCn1Proportion() {
        return this.cn1Proportion;
    }

    public void setCn1Proportion(String cn1Proportion) {
        this.cn1Proportion = cn1Proportion;
    }

    public String getCn1Operation() {
        return this.cn1Operation;
    }

    public void setCn1Operation(String cn1Operation) {
        this.cn1Operation = cn1Operation;
    }

    public String getCn1CreateUser() {
        return this.cn1CreateUser;
    }

    public void setCn1CreateUser(String cn1CreateUser) {
        this.cn1CreateUser = cn1CreateUser;
    }

    public String getCn1CreateTime() {
        return this.cn1CreateTime;
    }

    public void setCn1CreateTime(String cn1CreateTime) {
        this.cn1CreateTime = cn1CreateTime;
    }

    public String getCn1ModifyUser() {
        return this.cn1ModifyUser;
    }

    public void setCn1ModifyUser(String cn1ModifyUser) {
        this.cn1ModifyUser = cn1ModifyUser;
    }

    public String getCn1ModifyTime() {
        return this.cn1ModifyTime;
    }

    public void setCn1ModifyTime(String cn1ModifyTime) {
        this.cn1ModifyTime = cn1ModifyTime;
    }

    public String getCn1RowVersion() {
        return this.cn1RowVersion;
    }

    public void setCn1RowVersion(String cn1RowVersion) {
        this.cn1RowVersion = cn1RowVersion;
    }

    public String toString() {
        return "Conversion [cn1Id=" + this.cn1Id + ", cn1Upid=" + this.cn1Upid + ", cn1Proportion=" + this.cn1Proportion + ", cn1Operation=" + this.cn1Operation + ", cn1CreateUser=" + this.cn1CreateUser + ", cn1CreateTime=" + this.cn1CreateTime + ", cn1ModifyUser=" + this.cn1ModifyUser + ", cn1ModifyTime=" + this.cn1ModifyTime + ", cn1RowVersion=" + this.cn1RowVersion + "]";
    }
}
