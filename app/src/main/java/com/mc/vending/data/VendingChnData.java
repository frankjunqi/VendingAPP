package com.mc.vending.data;

import java.io.Serializable;

public class VendingChnData extends BaseParseData implements Serializable {
    public static final String VENDINGCHN_BORROWSTATUS_BORROW = "0";
    public static final String VENDINGCHN_BORROWSTATUS_RETURN = "1";
    public static final String VENDINGCHN_COMMAND_BORROW = "1";
    public static final String VENDINGCHN_COMMAND_RETURN = "2";
    public static final String VENDINGCHN_METHOD_BORROW = "2";
    public static final String VENDINGCHN_METHOD_GENERAL = "1";
    public static final String VENDINGCHN_SALETYPE_BORROW = "2";
    public static final String VENDINGCHN_SALETYPE_JISHOU = "1";
    public static final String VENDINGCHN_SALETYPE_SELF = "0";
    public static final String VENDINGCHN_STATUS_FROZENING = "1";
    public static final String VENDINGCHN_STATUS_FROZEN_FINISH = "2";
    public static final String VENDINGCHN_STATUS_NORMAL = "0";
    public static final String VENDINGCHN_TYPE_CELL = "1";
    public static final String VENDINGCHN_TYPE_VENDING = "0";
    private static final long serialVersionUID = 1591303617438650753L;
    private int failureQty = 0;
    private int inputQty = 0;
    private String vc1BorrowStatus;
    private Integer vc1Capacity;
    private String vc1Code;
    private String vc1ColumnNum;
    private String vc1CreateTime;
    private String vc1CreateUser;
    private String vc1Height;
    private String vc1Id;
    private String vc1LineNum;
    private String vc1M02Id;
    private String vc1ModifyTime;
    private String vc1ModifyUser;
    private String vc1Pd1Id;
    private String vc1RowVersion;
    private String vc1SaleType;
    private String vc1Sp1Id;
    private String vc1Status;
    private String vc1ThreadSize;
    private String vc1Type;
    private String vc1Vd1Id;

    public String getVc1Id() {
        return this.vc1Id;
    }

    public void setVc1Id(String value) {
        this.vc1Id = value;
    }

    public String getVc1M02Id() {
        return this.vc1M02Id;
    }

    public void setVc1M02Id(String value) {
        this.vc1M02Id = value;
    }

    public String getVc1Vd1Id() {
        return this.vc1Vd1Id;
    }

    public void setVc1Vd1Id(String value) {
        this.vc1Vd1Id = value;
    }

    public String getVc1Code() {
        return this.vc1Code;
    }

    public void setVc1Code(String value) {
        this.vc1Code = value;
    }

    public String getVc1Type() {
        return this.vc1Type;
    }

    public void setVc1Type(String value) {
        this.vc1Type = value;
    }

    public Integer getVc1Capacity() {
        return this.vc1Capacity;
    }

    public void setVc1Capacity(Integer value) {
        this.vc1Capacity = value;
    }

    public String getVc1ThreadSize() {
        return this.vc1ThreadSize;
    }

    public void setVc1ThreadSize(String value) {
        this.vc1ThreadSize = value;
    }

    public String getVc1Pd1Id() {
        return this.vc1Pd1Id;
    }

    public void setVc1Pd1Id(String value) {
        this.vc1Pd1Id = value;
    }

    public String getVc1SaleType() {
        return this.vc1SaleType;
    }

    public void setVc1SaleType(String value) {
        this.vc1SaleType = value;
    }

    public String getVc1Sp1Id() {
        return this.vc1Sp1Id;
    }

    public void setVc1Sp1Id(String value) {
        this.vc1Sp1Id = value;
    }

    public String getVc1BorrowStatus() {
        return this.vc1BorrowStatus;
    }

    public void setVc1BorrowStatus(String value) {
        this.vc1BorrowStatus = value;
    }

    public String getVc1Status() {
        return this.vc1Status;
    }

    public void setVc1Status(String value) {
        this.vc1Status = value;
    }

    public String getVc1LineNum() {
        return this.vc1LineNum;
    }

    public void setVc1LineNum(String value) {
        this.vc1LineNum = value;
    }

    public String getVc1ColumnNum() {
        return this.vc1ColumnNum;
    }

    public void setVc1ColumnNum(String value) {
        this.vc1ColumnNum = value;
    }

    public String getVc1Height() {
        return this.vc1Height;
    }

    public void setVc1Height(String value) {
        this.vc1Height = value;
    }

    public String getVc1CreateUser() {
        return this.vc1CreateUser;
    }

    public void setVc1CreateUser(String value) {
        this.vc1CreateUser = value;
    }

    public String getVc1CreateTime() {
        return this.vc1CreateTime;
    }

    public void setVc1CreateTime(String value) {
        this.vc1CreateTime = value;
    }

    public String getVc1ModifyUser() {
        return this.vc1ModifyUser;
    }

    public void setVc1ModifyUser(String value) {
        this.vc1ModifyUser = value;
    }

    public String getVc1ModifyTime() {
        return this.vc1ModifyTime;
    }

    public void setVc1ModifyTime(String value) {
        this.vc1ModifyTime = value;
    }

    public String getVc1RowVersion() {
        return this.vc1RowVersion;
    }

    public void setVc1RowVersion(String value) {
        this.vc1RowVersion = value;
    }

    public int getFailureQty() {
        return this.failureQty;
    }

    public void setFailureQty(int failureQty) {
        this.failureQty = failureQty;
    }

    public int getInputQty() {
        return this.inputQty;
    }

    public void setInputQty(int inputQty) {
        this.inputQty = inputQty;
    }

    public String toString() {
        return "VendingChnData [vc1Id=" + this.vc1Id + ", vc1M02Id=" + this.vc1M02Id + ", vc1Vd1Id=" + this.vc1Vd1Id + ", vc1Code=" + this.vc1Code + ", vc1Type=" + this.vc1Type + ", vc1Capacity=" + this.vc1Capacity + ", vc1ThreadSize=" + this.vc1ThreadSize + ", vc1Pd1Id=" + this.vc1Pd1Id + ", vc1SaleType=" + this.vc1SaleType + ", vc1Sp1Id=" + this.vc1Sp1Id + ", vc1BorrowStatus=" + this.vc1BorrowStatus + ", vc1Status=" + this.vc1Status + ", vc1LineNum=" + this.vc1LineNum + ", vc1ColumnNum=" + this.vc1ColumnNum + ", vc1Height=" + this.vc1Height + ", vc1CreateUser=" + this.vc1CreateUser + ", vc1CreateTime=" + this.vc1CreateTime + ", vc1ModifyUser=" + this.vc1ModifyUser + ", vc1ModifyTime=" + this.vc1ModifyTime + ", vc1RowVersion=" + this.vc1RowVersion + "]";
    }
}
