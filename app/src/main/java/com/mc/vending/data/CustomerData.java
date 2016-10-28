package com.mc.vending.data;

import java.io.Serializable;

public class CustomerData extends BaseParseData implements Serializable {
    private static final long serialVersionUID = 6793590734045578599L;
    private String cu1Address;
    private String cu1Area;
    private String cu1City;
    private String cu1Code;
    private String cu1CodeFather;
    private String cu1Country;
    private String cu1CreateTime;
    private String cu1CreateUser;
    private String cu1Id;
    private String cu1LastImportTime;
    private String cu1M02Id;
    private String cu1ModifyTime;
    private String cu1ModifyUser;
    private String cu1Name;
    private String cu1Relation;
    private String cu1RelationPhone;
    private String cu1RowVersion;
    private String cu1Saler;
    private String cu1SalerPhone;

    public String getCu1Id() {
        return this.cu1Id;
    }

    public void setCu1Id(String value) {
        this.cu1Id = value;
    }

    public String getCu1M02Id() {
        return this.cu1M02Id;
    }

    public void setCu1M02Id(String value) {
        this.cu1M02Id = value;
    }

    public String getCu1Code() {
        return this.cu1Code;
    }

    public void setCu1Code(String value) {
        this.cu1Code = value;
    }

    public String getCu1Name() {
        return this.cu1Name;
    }

    public void setCu1Name(String value) {
        this.cu1Name = value;
    }

    public String getCu1Relation() {
        return this.cu1Relation;
    }

    public void setCu1Relation(String value) {
        this.cu1Relation = value;
    }

    public String getCu1RelationPhone() {
        return this.cu1RelationPhone;
    }

    public void setCu1RelationPhone(String value) {
        this.cu1RelationPhone = value;
    }

    public String getCu1Saler() {
        return this.cu1Saler;
    }

    public void setCu1Saler(String value) {
        this.cu1Saler = value;
    }

    public String getCu1SalerPhone() {
        return this.cu1SalerPhone;
    }

    public void setCu1SalerPhone(String value) {
        this.cu1SalerPhone = value;
    }

    public String getCu1Country() {
        return this.cu1Country;
    }

    public void setCu1Country(String value) {
        this.cu1Country = value;
    }

    public String getCu1City() {
        return this.cu1City;
    }

    public void setCu1City(String value) {
        this.cu1City = value;
    }

    public String getCu1Area() {
        return this.cu1Area;
    }

    public void setCu1Area(String value) {
        this.cu1Area = value;
    }

    public String getCu1Address() {
        return this.cu1Address;
    }

    public void setCu1Address(String value) {
        this.cu1Address = value;
    }

    public String getCu1LastImportTime() {
        return this.cu1LastImportTime;
    }

    public void setCu1LastImportTime(String value) {
        this.cu1LastImportTime = value;
    }

    public String getCu1CodeFather() {
        return this.cu1CodeFather;
    }

    public void setCu1CodeFather(String cu1CodeFather) {
        this.cu1CodeFather = cu1CodeFather;
    }

    public String getCu1CreateUser() {
        return this.cu1CreateUser;
    }

    public void setCu1CreateUser(String value) {
        this.cu1CreateUser = value;
    }

    public String getCu1CreateTime() {
        return this.cu1CreateTime;
    }

    public void setCu1CreateTime(String value) {
        this.cu1CreateTime = value;
    }

    public String getCu1ModifyUser() {
        return this.cu1ModifyUser;
    }

    public void setCu1ModifyUser(String value) {
        this.cu1ModifyUser = value;
    }

    public String getCu1ModifyTime() {
        return this.cu1ModifyTime;
    }

    public void setCu1ModifyTime(String value) {
        this.cu1ModifyTime = value;
    }

    public String getCu1RowVersion() {
        return this.cu1RowVersion;
    }

    public void setCu1RowVersion(String value) {
        this.cu1RowVersion = value;
    }

    public String toString() {
        return "CustomerData [cu1Id=" + this.cu1Id + ", cu1M02Id=" + this.cu1M02Id + ", cu1Code=" + this.cu1Code + ", cu1Name=" + this.cu1Name + ", cu1Relation=" + this.cu1Relation + ", cu1RelationPhone=" + this.cu1RelationPhone + ", cu1Saler=" + this.cu1Saler + ", cu1SalerPhone=" + this.cu1SalerPhone + ", cu1Country=" + this.cu1Country + ", cu1City=" + this.cu1City + ", cu1Area=" + this.cu1Area + ", cu1Address=" + this.cu1Address + ", cu1LastImportTime=" + this.cu1LastImportTime + ", cu1CodeFather=" + this.cu1CodeFather + ", cu1CreateUser=" + this.cu1CreateUser + ", cu1CreateTime=" + this.cu1CreateTime + ", cu1ModifyUser=" + this.cu1ModifyUser + ", cu1ModifyTime=" + this.cu1ModifyTime + ", cu1RowVersion=" + this.cu1RowVersion + "]";
    }
}
