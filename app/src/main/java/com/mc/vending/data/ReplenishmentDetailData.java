package com.mc.vending.data;

import java.io.Serializable;

public class ReplenishmentDetailData implements Serializable {
    public static final String UPLOAD_LOAD = "1";
    public static final String UPLOAD_UNLOAD = "0";
    private static final long serialVersionUID = -6106463384508774391L;
    private Integer rh2ActualQty;
    private String rh2CreateTime;
    private String rh2CreateUser;
    private Integer rh2DifferentiaQty;
    private String rh2Id;
    private String rh2M02Id;
    private String rh2ModifyTime;
    private String rh2ModifyUser;
    private String rh2Pd1Id;
    private String rh2Rh1Id;
    private String rh2RowVersion;
    private String rh2Rp1Id;
    private String rh2SaleType;
    private String rh2Sp1Id;
    private String rh2UploadStatus;
    private String rh2Vc1Code;

    public String getRh2Id() {
        return this.rh2Id;
    }

    public void setRh2Id(String value) {
        this.rh2Id = value;
    }

    public String getRh2M02Id() {
        return this.rh2M02Id;
    }

    public void setRh2M02Id(String value) {
        this.rh2M02Id = value;
    }

    public String getRh2Rh1Id() {
        return this.rh2Rh1Id;
    }

    public void setRh2Rh1Id(String value) {
        this.rh2Rh1Id = value;
    }

    public String getRh2Vc1Code() {
        return this.rh2Vc1Code;
    }

    public void setRh2Vc1Code(String value) {
        this.rh2Vc1Code = value;
    }

    public String getRh2Pd1Id() {
        return this.rh2Pd1Id;
    }

    public void setRh2Pd1Id(String value) {
        this.rh2Pd1Id = value;
    }

    public String getRh2SaleType() {
        return this.rh2SaleType;
    }

    public void setRh2SaleType(String value) {
        this.rh2SaleType = value;
    }

    public String getRh2Sp1Id() {
        return this.rh2Sp1Id;
    }

    public void setRh2Sp1Id(String value) {
        this.rh2Sp1Id = value;
    }

    public Integer getRh2ActualQty() {
        return this.rh2ActualQty;
    }

    public void setRh2ActualQty(Integer value) {
        this.rh2ActualQty = value;
    }

    public Integer getRh2DifferentiaQty() {
        return this.rh2DifferentiaQty;
    }

    public void setRh2DifferentiaQty(Integer value) {
        this.rh2DifferentiaQty = value;
    }

    public String getRh2Rp1Id() {
        return this.rh2Rp1Id;
    }

    public void setRh2Rp1Id(String value) {
        this.rh2Rp1Id = value;
    }

    public String getRh2UploadStatus() {
        return this.rh2UploadStatus;
    }

    public void setRh2UploadStatus(String rh2UploadStatus) {
        this.rh2UploadStatus = rh2UploadStatus;
    }

    public String getRh2CreateUser() {
        return this.rh2CreateUser;
    }

    public void setRh2CreateUser(String value) {
        this.rh2CreateUser = value;
    }

    public String getRh2CreateTime() {
        return this.rh2CreateTime;
    }

    public void setRh2CreateTime(String value) {
        this.rh2CreateTime = value;
    }

    public String getRh2ModifyUser() {
        return this.rh2ModifyUser;
    }

    public void setRh2ModifyUser(String value) {
        this.rh2ModifyUser = value;
    }

    public String getRh2ModifyTime() {
        return this.rh2ModifyTime;
    }

    public void setRh2ModifyTime(String value) {
        this.rh2ModifyTime = value;
    }

    public String getRh2RowVersion() {
        return this.rh2RowVersion;
    }

    public void setRh2RowVersion(String value) {
        this.rh2RowVersion = value;
    }

    public String toString() {
        return "ReplenishmentDetailData [rh2Id=" + this.rh2Id + ", rh2M02Id=" + this.rh2M02Id + ", rh2Rh1Id=" + this.rh2Rh1Id + ", rh2Vc1Code=" + this.rh2Vc1Code + ", rh2Pd1Id=" + this.rh2Pd1Id + ", rh2SaleType=" + this.rh2SaleType + ", rh2Sp1Id=" + this.rh2Sp1Id + ", rh2ActualQty=" + this.rh2ActualQty + ", rh2DifferentiaQty=" + this.rh2DifferentiaQty + ", rh2Rp1Id=" + this.rh2Rp1Id + ", rh2UploadStatus=" + this.rh2UploadStatus + ", rh2CreateUser=" + this.rh2CreateUser + ", rh2CreateTime=" + this.rh2CreateTime + ", rh2ModifyUser=" + this.rh2ModifyUser + ", rh2ModifyTime=" + this.rh2ModifyTime + ", rh2RowVersion=" + this.rh2RowVersion + "]";
    }
}
