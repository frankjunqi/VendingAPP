package com.mc.vending.data;

import java.io.Serializable;

public class ProductData extends BaseParseData implements Serializable {
    public static final int ProductData_status1 = 1;
    private static final long serialVersionUID = -2974800842806130086L;
    private String pd1Brand;
    private String pd1Code;
    private String pd1Description;
    private String pd1Id;
    private String pd1LastImportTime;
    private String pd1M02Id;
    private String pd1ManufactureModel;
    private String pd1Name;
    private String pd1Package;
    private String pd1Size;
    private String pd1Unit;

    public String getPd1Id() {
        return this.pd1Id;
    }

    public void setPd1Id(String value) {
        this.pd1Id = value;
    }

    public String getPd1M02Id() {
        return this.pd1M02Id;
    }

    public void setPd1M02Id(String value) {
        this.pd1M02Id = value;
    }

    public String getPd1Code() {
        return this.pd1Code;
    }

    public void setPd1Code(String value) {
        this.pd1Code = value;
    }

    public String getPd1Name() {
        return this.pd1Name;
    }

    public void setPd1Name(String value) {
        this.pd1Name = value;
    }

    public String getPd1Description() {
        return this.pd1Description;
    }

    public void setPd1Description(String value) {
        this.pd1Description = value;
    }

    public String getPd1ManufactureModel() {
        return this.pd1ManufactureModel;
    }

    public void setPd1ManufactureModel(String value) {
        this.pd1ManufactureModel = value;
    }

    public String getPd1Size() {
        return this.pd1Size;
    }

    public void setPd1Size(String value) {
        this.pd1Size = value;
    }

    public String getPd1Brand() {
        return this.pd1Brand;
    }

    public void setPd1Brand(String value) {
        this.pd1Brand = value;
    }

    public String getPd1Package() {
        return this.pd1Package;
    }

    public void setPd1Package(String value) {
        this.pd1Package = value;
    }

    public String getPd1Unit() {
        return this.pd1Unit;
    }

    public void setPd1Unit(String value) {
        this.pd1Unit = value;
    }

    public String getPd1LastImportTime() {
        return this.pd1LastImportTime;
    }

    public void setPd1LastImportTime(String value) {
        this.pd1LastImportTime = value;
    }

    public String toString() {
        return "ProductData [pd1Id=" + this.pd1Id + ", pd1M02Id=" + this.pd1M02Id + ", pd1Code=" + this.pd1Code + ", pd1Name=" + this.pd1Name + ", pd1Description=" + this.pd1Description + ", pd1ManufactureModel=" + this.pd1ManufactureModel + ", pd1Size=" + this.pd1Size + ", pd1Brand=" + this.pd1Brand + ", pd1Package=" + this.pd1Package + ", pd1Unit=" + this.pd1Unit + ", pd1LastImportTime=" + this.pd1LastImportTime + "]";
    }
}
