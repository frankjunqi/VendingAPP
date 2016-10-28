package com.mc.vending.data;

import java.io.Serializable;

public class InventoryHistoryData implements Serializable {
    public static final String UPLOAD_LOAD = "1";
    public static final String UPLOAD_UNLOAD = "0";
    private static final long serialVersionUID = 5194870158930913281L;
    private String ih3ActualDate;
    private String ih3CreateTime;
    private String ih3CreateUser;
    private String ih3Cu1Id;
    private Integer ih3DifferentiaQty;
    private String ih3IHcode;
    private String ih3Id;
    private String ih3InventoryPeople;
    private Integer ih3InventoryQty;
    private String ih3M02Id;
    private String ih3ModifyTime;
    private String ih3ModifyUser;
    private String ih3Pd1Id;
    private Integer ih3Quantity;
    private String ih3RowVersion;
    private String ih3UploadStatus;
    private String ih3Vc1Code;
    private String ih3Vd1Id;

    public String getIh3Id() {
        return this.ih3Id;
    }

    public void setIh3Id(String value) {
        this.ih3Id = value;
    }

    public String getIh3M02Id() {
        return this.ih3M02Id;
    }

    public void setIh3M02Id(String value) {
        this.ih3M02Id = value;
    }

    public String getIh3IHcode() {
        return this.ih3IHcode;
    }

    public void setIh3IHcode(String value) {
        this.ih3IHcode = value;
    }

    public String getIh3ActualDate() {
        return this.ih3ActualDate;
    }

    public void setIh3ActualDate(String ih3ActualDate) {
        this.ih3ActualDate = ih3ActualDate;
    }

    public String getIh3Cu1Id() {
        return this.ih3Cu1Id;
    }

    public void setIh3Cu1Id(String value) {
        this.ih3Cu1Id = value;
    }

    public String getIh3InventoryPeople() {
        return this.ih3InventoryPeople;
    }

    public void setIh3InventoryPeople(String value) {
        this.ih3InventoryPeople = value;
    }

    public String getIh3Vd1Id() {
        return this.ih3Vd1Id;
    }

    public void setIh3Vd1Id(String value) {
        this.ih3Vd1Id = value;
    }

    public String getIh3Vc1Code() {
        return this.ih3Vc1Code;
    }

    public void setIh3Vc1Code(String value) {
        this.ih3Vc1Code = value;
    }

    public String getIh3Pd1Id() {
        return this.ih3Pd1Id;
    }

    public void setIh3Pd1Id(String value) {
        this.ih3Pd1Id = value;
    }

    public Integer getIh3Quantity() {
        return this.ih3Quantity;
    }

    public void setIh3Quantity(Integer value) {
        this.ih3Quantity = value;
    }

    public Integer getIh3InventoryQty() {
        return this.ih3InventoryQty;
    }

    public void setIh3InventoryQty(Integer value) {
        this.ih3InventoryQty = value;
    }

    public Integer getIh3DifferentiaQty() {
        return this.ih3DifferentiaQty;
    }

    public void setIh3DifferentiaQty(Integer value) {
        this.ih3DifferentiaQty = value;
    }

    public String getIh3UploadStatus() {
        return this.ih3UploadStatus;
    }

    public void setIh3UploadStatus(String ih3UploadStatus) {
        this.ih3UploadStatus = ih3UploadStatus;
    }

    public String getIh3CreateUser() {
        return this.ih3CreateUser;
    }

    public void setIh3CreateUser(String value) {
        this.ih3CreateUser = value;
    }

    public String getIh3CreateTime() {
        return this.ih3CreateTime;
    }

    public void setIh3CreateTime(String value) {
        this.ih3CreateTime = value;
    }

    public String getIh3ModifyUser() {
        return this.ih3ModifyUser;
    }

    public void setIh3ModifyUser(String value) {
        this.ih3ModifyUser = value;
    }

    public String getIh3ModifyTime() {
        return this.ih3ModifyTime;
    }

    public void setIh3ModifyTime(String value) {
        this.ih3ModifyTime = value;
    }

    public String getIh3RowVersion() {
        return this.ih3RowVersion;
    }

    public void setIh3RowVersion(String value) {
        this.ih3RowVersion = value;
    }
}
