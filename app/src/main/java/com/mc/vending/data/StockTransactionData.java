package com.mc.vending.data;

import java.io.Serializable;

public class StockTransactionData implements Serializable {
    public static final String BILL_TYPE_All = "7";
    public static final String BILL_TYPE_BORROW = "6";
    public static final String BILL_TYPE_DIFF = "1";
    public static final String BILL_TYPE_DIFFAll = "8";
    public static final String BILL_TYPE_EMERGENCY = "2";
    public static final String BILL_TYPE_GET = "4";
    public static final String BILL_TYPE_PANDIAN = "3";
    public static final String BILL_TYPE_PLAN = "0";
    public static final String BILL_TYPE_RETURN = "5";
    public static final String STOCKTRANSACTION_POWER_NO = "1";
    public static final String STOCKTRANSACTION_POWER_YES = "0";
    public static final String UPLOAD_LOAD = "1";
    public static final String UPLOAD_UNLOAD = "0";
    private static final long serialVersionUID = 4542416370363666200L;
    private String ts1BillCode;
    private String ts1BillType;
    private String ts1Cd1Id;
    private String ts1CreateTime;
    private String ts1CreateUser;
    private String ts1Id;
    private String ts1M02Id;
    private String ts1ModifyTime;
    private String ts1ModifyUser;
    private String ts1Pd1Id;
    private String ts1RowVersion;
    private String ts1Sp1Code;
    private String ts1Sp1Name;
    private Integer ts1TransQty;
    private String ts1TransType;
    private String ts1UploadStatus;
    private String ts1Vc1Code;
    private String ts1Vd1Id;

    public String getTs1Id() {
        return this.ts1Id;
    }

    public void setTs1Id(String value) {
        this.ts1Id = value;
    }

    public String getTs1M02Id() {
        return this.ts1M02Id;
    }

    public void setTs1M02Id(String value) {
        this.ts1M02Id = value;
    }

    public String getTs1BillType() {
        return this.ts1BillType;
    }

    public void setTs1BillType(String value) {
        this.ts1BillType = value;
    }

    public String getTs1BillCode() {
        return this.ts1BillCode;
    }

    public void setTs1BillCode(String value) {
        this.ts1BillCode = value;
    }

    public String getTs1Cd1Id() {
        return this.ts1Cd1Id;
    }

    public void setTs1Cd1Id(String value) {
        this.ts1Cd1Id = value;
    }

    public String getTs1Vd1Id() {
        return this.ts1Vd1Id;
    }

    public void setTs1Vd1Id(String value) {
        this.ts1Vd1Id = value;
    }

    public String getTs1Pd1Id() {
        return this.ts1Pd1Id;
    }

    public void setTs1Pd1Id(String value) {
        this.ts1Pd1Id = value;
    }

    public String getTs1Vc1Code() {
        return this.ts1Vc1Code;
    }

    public void setTs1Vc1Code(String value) {
        this.ts1Vc1Code = value;
    }

    public Integer getTs1TransQty() {
        return this.ts1TransQty;
    }

    public void setTs1TransQty(Integer value) {
        this.ts1TransQty = value;
    }

    public String getTs1TransType() {
        return this.ts1TransType;
    }

    public void setTs1TransType(String value) {
        this.ts1TransType = value;
    }

    public String getTs1Sp1Code() {
        return this.ts1Sp1Code;
    }

    public void setTs1Sp1Code(String value) {
        this.ts1Sp1Code = value;
    }

    public String getTs1Sp1Name() {
        return this.ts1Sp1Name;
    }

    public void setTs1Sp1Name(String value) {
        this.ts1Sp1Name = value;
    }

    public String getTs1UploadStatus() {
        return this.ts1UploadStatus;
    }

    public void setTs1UploadStatus(String ts1UploadStatus) {
        this.ts1UploadStatus = ts1UploadStatus;
    }

    public String getTs1CreateUser() {
        return this.ts1CreateUser;
    }

    public void setTs1CreateUser(String value) {
        this.ts1CreateUser = value;
    }

    public String getTs1CreateTime() {
        return this.ts1CreateTime;
    }

    public void setTs1CreateTime(String value) {
        this.ts1CreateTime = value;
    }

    public String getTs1ModifyUser() {
        return this.ts1ModifyUser;
    }

    public void setTs1ModifyUser(String value) {
        this.ts1ModifyUser = value;
    }

    public String getTs1ModifyTime() {
        return this.ts1ModifyTime;
    }

    public void setTs1ModifyTime(String value) {
        this.ts1ModifyTime = value;
    }

    public String getTs1RowVersion() {
        return this.ts1RowVersion;
    }

    public void setTs1RowVersion(String value) {
        this.ts1RowVersion = value;
    }

    public String toString() {
        return "StockTransactionData [ts1Id=" + this.ts1Id + ", ts1M02Id=" + this.ts1M02Id + ", ts1BillType=" + this.ts1BillType + ", ts1BillCode=" + this.ts1BillCode + ", ts1Cd1Id=" + this.ts1Cd1Id + ", ts1Vd1Id=" + this.ts1Vd1Id + ", ts1Pd1Id=" + this.ts1Pd1Id + ", ts1Vc1Code=" + this.ts1Vc1Code + ", ts1TransQty=" + this.ts1TransQty + ", ts1TransType=" + this.ts1TransType + ", ts1Sp1Code=" + this.ts1Sp1Code + ", ts1Sp1Name=" + this.ts1Sp1Name + ", ts1UploadStatus=" + this.ts1UploadStatus + ", ts1CreateUser=" + this.ts1CreateUser + ", ts1CreateTime=" + this.ts1CreateTime + ", ts1ModifyUser=" + this.ts1ModifyUser + ", ts1ModifyTime=" + this.ts1ModifyTime + ", ts1RowVersion=" + this.ts1RowVersion + "]";
    }
}
