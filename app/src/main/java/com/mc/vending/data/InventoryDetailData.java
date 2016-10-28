package com.mc.vending.data;

import java.io.Serializable;

public class InventoryDetailData implements Serializable {
    private static final String IH2_STATUS_NO = "0";
    private static final String IH2_STATUS_YES = "1";
    private static final long serialVersionUID = -1671821675652345706L;
    private String ih2CreateTime;
    private String ih2CreateUser;
    private String ih2Cu1Id;
    private String ih2Id;
    private String ih2Ih1Id;
    private String ih2M02Id;
    private String ih2ModifyTime;
    private String ih2ModifyUser;
    private String ih2RowVersion;
    private String ih2St1Id;
    private String ih2St1StationMaster;
    private String ih2Status;
    private String ih2Vd1Id;

    public String getIh2Id() {
        return this.ih2Id;
    }

    public void setIh2Id(String value) {
        this.ih2Id = value;
    }

    public String getIh2M02Id() {
        return this.ih2M02Id;
    }

    public void setIh2M02Id(String value) {
        this.ih2M02Id = value;
    }

    public String getIh2Ih1Id() {
        return this.ih2Ih1Id;
    }

    public void setIh2Ih1Id(String value) {
        this.ih2Ih1Id = value;
    }

    public String getIh2St1Id() {
        return this.ih2St1Id;
    }

    public void setIh2St1Id(String value) {
        this.ih2St1Id = value;
    }

    public String getIh2St1StationMaster() {
        return this.ih2St1StationMaster;
    }

    public void setIh2St1StationMaster(String value) {
        this.ih2St1StationMaster = value;
    }

    public String getIh2Cu1Id() {
        return this.ih2Cu1Id;
    }

    public void setIh2Cu1Id(String value) {
        this.ih2Cu1Id = value;
    }

    public String getIh2Vd1Id() {
        return this.ih2Vd1Id;
    }

    public void setIh2Vd1Id(String value) {
        this.ih2Vd1Id = value;
    }

    public String getIh2Status() {
        return this.ih2Status;
    }

    public void setIh2Status(String value) {
        this.ih2Status = value;
    }

    public String getIh2CreateUser() {
        return this.ih2CreateUser;
    }

    public void setIh2CreateUser(String value) {
        this.ih2CreateUser = value;
    }

    public String getIh2CreateTime() {
        return this.ih2CreateTime;
    }

    public void setIh2CreateTime(String value) {
        this.ih2CreateTime = value;
    }

    public String getIh2ModifyUser() {
        return this.ih2ModifyUser;
    }

    public void setIh2ModifyUser(String value) {
        this.ih2ModifyUser = value;
    }

    public String getIh2ModifyTime() {
        return this.ih2ModifyTime;
    }

    public void setIh2ModifyTime(String value) {
        this.ih2ModifyTime = value;
    }

    public String getIh2RowVersion() {
        return this.ih2RowVersion;
    }

    public void setIh2RowVersion(String value) {
        this.ih2RowVersion = value;
    }
}
