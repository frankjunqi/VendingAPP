package com.mc.vending.data;

import java.io.Serializable;

public class VendingData extends BaseParseData implements Serializable {
    public static final String VENDING_ONLINESTATUS_OFF = "1";
    public static final String VENDING_ONLINESTATUS_ON = "0";
    public static final String VENDING_STATUS_DELETE = "2";
    public static final String VENDING_STATUS_NO = "1";
    public static final String VENDING_STATUS_YES = "0";
    public static final String VENDING_TELE_400 = "4006809696";
    private static final long serialVersionUID = -6990282001803903087L;
    private String vd1CardType;
    private String vd1Code;
    private String vd1Color;
    private String vd1Coordinate;
    private String vd1CreateTime;
    private String vd1CreateUser;
    private String vd1EmergencyRel;
    private String vd1EmergencyRelPhone;
    private String vd1Id;
    private String vd1InstallAddress;
    private String vd1LastVersion;
    private String vd1LwhSize;
    private String vd1M02Id;
    private String vd1Manufacturer;
    private String vd1ModifyTime;
    private String vd1ModifyUser;
    private String vd1OnlineStatus;
    private String vd1RowVersion;
    private String vd1St1Id;
    private String vd1Status;
    private String vd1Vm1Id;

    public String getVd1Id() {
        return this.vd1Id;
    }

    public void setVd1Id(String value) {
        this.vd1Id = value;
    }

    public String getVd1M02Id() {
        return this.vd1M02Id;
    }

    public void setVd1M02Id(String value) {
        this.vd1M02Id = value;
    }

    public String getVd1Code() {
        return this.vd1Code;
    }

    public void setVd1Code(String value) {
        this.vd1Code = value;
    }

    public String getVd1Manufacturer() {
        return this.vd1Manufacturer;
    }

    public void setVd1Manufacturer(String value) {
        this.vd1Manufacturer = value;
    }

    public String getVd1Vm1Id() {
        return this.vd1Vm1Id;
    }

    public void setVd1Vm1Id(String value) {
        this.vd1Vm1Id = value;
    }

    public String getVd1LastVersion() {
        return this.vd1LastVersion;
    }

    public void setVd1LastVersion(String value) {
        this.vd1LastVersion = value;
    }

    public String getVd1LwhSize() {
        return this.vd1LwhSize;
    }

    public void setVd1LwhSize(String value) {
        this.vd1LwhSize = value;
    }

    public String getVd1Color() {
        return this.vd1Color;
    }

    public void setVd1Color(String value) {
        this.vd1Color = value;
    }

    public String getVd1InstallAddress() {
        return this.vd1InstallAddress;
    }

    public void setVd1InstallAddress(String value) {
        this.vd1InstallAddress = value;
    }

    public String getVd1Coordinate() {
        return this.vd1Coordinate;
    }

    public void setVd1Coordinate(String value) {
        this.vd1Coordinate = value;
    }

    public String getVd1St1Id() {
        return this.vd1St1Id;
    }

    public void setVd1St1Id(String value) {
        this.vd1St1Id = value;
    }

    public String getVd1EmergencyRel() {
        return this.vd1EmergencyRel;
    }

    public void setVd1EmergencyRel(String value) {
        this.vd1EmergencyRel = value;
    }

    public String getVd1EmergencyRelPhone() {
        return this.vd1EmergencyRelPhone;
    }

    public void setVd1EmergencyRelPhone(String value) {
        this.vd1EmergencyRelPhone = value;
    }

    public String getVd1OnlineStatus() {
        return this.vd1OnlineStatus;
    }

    public void setVd1OnlineStatus(String value) {
        this.vd1OnlineStatus = value;
    }

    public String getVd1Status() {
        return this.vd1Status;
    }

    public void setVd1Status(String value) {
        this.vd1Status = value;
    }

    public String getVd1CreateUser() {
        return this.vd1CreateUser;
    }

    public void setVd1CreateUser(String value) {
        this.vd1CreateUser = value;
    }

    public String getVd1CreateTime() {
        return this.vd1CreateTime;
    }

    public void setVd1CreateTime(String value) {
        this.vd1CreateTime = value;
    }

    public String getVd1ModifyUser() {
        return this.vd1ModifyUser;
    }

    public void setVd1ModifyUser(String value) {
        this.vd1ModifyUser = value;
    }

    public String getVd1ModifyTime() {
        return this.vd1ModifyTime;
    }

    public void setVd1ModifyTime(String value) {
        this.vd1ModifyTime = value;
    }

    public String getVd1RowVersion() {
        return this.vd1RowVersion;
    }

    public void setVd1RowVersion(String value) {
        this.vd1RowVersion = value;
    }

    public String getVd1CardType() {
        return this.vd1CardType;
    }

    public void setVd1CardType(String vd1CardType) {
        this.vd1CardType = vd1CardType;
    }

    public String toString() {
        return "VendingData [vd1Id=" + this.vd1Id + ", vd1M02Id=" + this.vd1M02Id + ", vd1Code=" + this.vd1Code + ", vd1Manufacturer=" + this.vd1Manufacturer + ", vd1Vm1Id=" + this.vd1Vm1Id + ", vd1LastVersion=" + this.vd1LastVersion + ", vd1LwhSize=" + this.vd1LwhSize + ", vd1Color=" + this.vd1Color + ", vd1InstallAddress=" + this.vd1InstallAddress + ", vd1Coordinate=" + this.vd1Coordinate + ", vd1St1Id=" + this.vd1St1Id + ", vd1EmergencyRel=" + this.vd1EmergencyRel + ", vd1EmergencyRelPhone=" + this.vd1EmergencyRelPhone + ", vd1OnlineStatus=" + this.vd1OnlineStatus + ", vd1Status=" + this.vd1Status + ", vd1CreateUser=" + this.vd1CreateUser + ", vd1CreateTime=" + this.vd1CreateTime + ", vd1ModifyUser=" + this.vd1ModifyUser + ", vd1ModifyTime=" + this.vd1ModifyTime + ", vd1RowVersion=" + this.vd1RowVersion + ", vd1CardType=" + this.vd1CardType + "]";
    }
}
