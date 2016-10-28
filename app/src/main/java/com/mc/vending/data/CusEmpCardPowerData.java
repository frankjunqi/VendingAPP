package com.mc.vending.data;

import java.io.Serializable;

public class CusEmpCardPowerData extends BaseParseData implements Serializable {
    private static final long serialVersionUID = 3710661701687481442L;
    private String ce2Cd1Id;
    private String ce2Ce1Id;
    private String ce2CreateTime;
    private String ce2CreateUser;
    private String ce2Id;
    private String ce2M02Id;
    private String ce2ModifyTime;
    private String ce2ModifyUser;
    private String ce2RowVersion;

    public String getCe2Id() {
        return this.ce2Id;
    }

    public void setCe2Id(String value) {
        this.ce2Id = value;
    }

    public String getCe2M02Id() {
        return this.ce2M02Id;
    }

    public void setCe2M02Id(String value) {
        this.ce2M02Id = value;
    }

    public String getCe2Ce1Id() {
        return this.ce2Ce1Id;
    }

    public void setCe2Ce1Id(String value) {
        this.ce2Ce1Id = value;
    }

    public String getCe2Cd1Id() {
        return this.ce2Cd1Id;
    }

    public void setCe2Cd1Id(String value) {
        this.ce2Cd1Id = value;
    }

    public String getCe2CreateUser() {
        return this.ce2CreateUser;
    }

    public void setCe2CreateUser(String value) {
        this.ce2CreateUser = value;
    }

    public String getCe2CreateTime() {
        return this.ce2CreateTime;
    }

    public void setCe2CreateTime(String value) {
        this.ce2CreateTime = value;
    }

    public String getCe2ModifyUser() {
        return this.ce2ModifyUser;
    }

    public void setCe2ModifyUser(String value) {
        this.ce2ModifyUser = value;
    }

    public String getCe2ModifyTime() {
        return this.ce2ModifyTime;
    }

    public void setCe2ModifyTime(String value) {
        this.ce2ModifyTime = value;
    }

    public String getCe2RowVersion() {
        return this.ce2RowVersion;
    }

    public void setCe2RowVersion(String value) {
        this.ce2RowVersion = value;
    }

    public String toString() {
        return "CusEmpCardPowerData [ce2Id=" + this.ce2Id + ", ce2M02Id=" + this.ce2M02Id + ", ce2Ce1Id=" + this.ce2Ce1Id + ", ce2Cd1Id=" + this.ce2Cd1Id + ", ce2CreateUser=" + this.ce2CreateUser + ", ce2CreateTime=" + this.ce2CreateTime + ", ce2ModifyUser=" + this.ce2ModifyUser + ", ce2ModifyTime=" + this.ce2ModifyTime + ", ce2RowVersion=" + this.ce2RowVersion + "]";
    }
}
