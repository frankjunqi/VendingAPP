package com.mc.vending.data;

import java.io.Serializable;

public class StationData extends BaseParseData implements Serializable {
    private static final long serialVersionUID = 3151758757494795240L;
    private String st1Address;
    private String st1Ce1Id;
    private String st1Code;
    private String st1Coordinate;
    private String st1CreateTime;
    private String st1CreateUser;
    private String st1Id;
    private String st1M02Id;
    private String st1ModifyTime;
    private String st1ModifyUser;
    private String st1Name;
    private String st1RowVersion;
    private String st1Status;
    private String st1Wh1Id;

    public String getSt1Id() {
        return this.st1Id;
    }

    public void setSt1Id(String value) {
        this.st1Id = value;
    }

    public String getSt1M02Id() {
        return this.st1M02Id;
    }

    public void setSt1M02Id(String value) {
        this.st1M02Id = value;
    }

    public String getSt1Code() {
        return this.st1Code;
    }

    public void setSt1Code(String value) {
        this.st1Code = value;
    }

    public String getSt1Name() {
        return this.st1Name;
    }

    public void setSt1Name(String value) {
        this.st1Name = value;
    }

    public String getSt1Ce1Id() {
        return this.st1Ce1Id;
    }

    public void setSt1Ce1Id(String st1Ce1Id) {
        this.st1Ce1Id = st1Ce1Id;
    }

    public String getSt1Wh1Id() {
        return this.st1Wh1Id;
    }

    public void setSt1Wh1Id(String value) {
        this.st1Wh1Id = value;
    }

    public String getSt1Coordinate() {
        return this.st1Coordinate;
    }

    public void setSt1Coordinate(String value) {
        this.st1Coordinate = value;
    }

    public String getSt1Address() {
        return this.st1Address;
    }

    public void setSt1Address(String value) {
        this.st1Address = value;
    }

    public String getSt1Status() {
        return this.st1Status;
    }

    public void setSt1Status(String value) {
        this.st1Status = value;
    }

    public String getSt1CreateUser() {
        return this.st1CreateUser;
    }

    public void setSt1CreateUser(String value) {
        this.st1CreateUser = value;
    }

    public String getSt1CreateTime() {
        return this.st1CreateTime;
    }

    public void setSt1CreateTime(String value) {
        this.st1CreateTime = value;
    }

    public String getSt1ModifyUser() {
        return this.st1ModifyUser;
    }

    public void setSt1ModifyUser(String value) {
        this.st1ModifyUser = value;
    }

    public String getSt1ModifyTime() {
        return this.st1ModifyTime;
    }

    public void setSt1ModifyTime(String value) {
        this.st1ModifyTime = value;
    }

    public String getSt1RowVersion() {
        return this.st1RowVersion;
    }

    public void setSt1RowVersion(String value) {
        this.st1RowVersion = value;
    }
}
