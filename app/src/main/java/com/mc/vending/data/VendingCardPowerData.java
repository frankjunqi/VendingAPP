package com.mc.vending.data;

import java.io.Serializable;

public class VendingCardPowerData extends BaseParseData implements Serializable {
    private static final long serialVersionUID = -581326770697470250L;
    private String vc2Cd1Id;
    private String vc2CreateTime;
    private String vc2CreateUser;
    private String vc2Cu1Id;
    private String vc2Id;
    private String vc2M02Id;
    private String vc2ModifyTime;
    private String vc2ModifyUser;
    private String vc2RowVersion;
    private String vc2Vd1Id;

    public String getVc2Id() {
        return this.vc2Id;
    }

    public void setVc2Id(String value) {
        this.vc2Id = value;
    }

    public String getVc2M02Id() {
        return this.vc2M02Id;
    }

    public void setVc2M02Id(String value) {
        this.vc2M02Id = value;
    }

    public String getVc2Cu1Id() {
        return this.vc2Cu1Id;
    }

    public void setVc2Cu1Id(String value) {
        this.vc2Cu1Id = value;
    }

    public String getVc2Vd1Id() {
        return this.vc2Vd1Id;
    }

    public void setVc2Vd1Id(String value) {
        this.vc2Vd1Id = value;
    }

    public String getVc2Cd1Id() {
        return this.vc2Cd1Id;
    }

    public void setVc2Cd1Id(String value) {
        this.vc2Cd1Id = value;
    }

    public String getVc2CreateUser() {
        return this.vc2CreateUser;
    }

    public void setVc2CreateUser(String value) {
        this.vc2CreateUser = value;
    }

    public String getVc2CreateTime() {
        return this.vc2CreateTime;
    }

    public void setVc2CreateTime(String value) {
        this.vc2CreateTime = value;
    }

    public String getVc2ModifyUser() {
        return this.vc2ModifyUser;
    }

    public void setVc2ModifyUser(String value) {
        this.vc2ModifyUser = value;
    }

    public String getVc2ModifyTime() {
        return this.vc2ModifyTime;
    }

    public void setVc2ModifyTime(String value) {
        this.vc2ModifyTime = value;
    }

    public String getVc2RowVersion() {
        return this.vc2RowVersion;
    }

    public void setVc2RowVersion(String value) {
        this.vc2RowVersion = value;
    }

    public String toString() {
        return "VendingCardPowerData [vc2Id=" + this.vc2Id + ", vc2M02Id=" + this.vc2M02Id + ", vc2Cu1Id=" + this.vc2Cu1Id + ", vc2Vd1Id=" + this.vc2Vd1Id + ", vc2Cd1Id=" + this.vc2Cd1Id + ", vc2CreateUser=" + this.vc2CreateUser + ", vc2CreateTime=" + this.vc2CreateTime + ", vc2ModifyUser=" + this.vc2ModifyUser + ", vc2ModifyTime=" + this.vc2ModifyTime + ", vc2RowVersion=" + this.vc2RowVersion + "]";
    }
}
