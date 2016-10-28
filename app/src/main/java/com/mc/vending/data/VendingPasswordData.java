package com.mc.vending.data;

import java.io.Serializable;

public class VendingPasswordData extends BaseParseData implements Serializable {
    private static final long serialVersionUID = -7731245339685646725L;
    private String vp3CreateTime;
    private String vp3CreateUser;
    private String vp3Id;
    private String vp3M02Id;
    private String vp3ModifyTime;
    private String vp3ModifyUser;
    private String vp3Password;
    private String vp3RowVersion;

    public String getVp3Id() {
        return this.vp3Id;
    }

    public void setVp3Id(String vp3Id) {
        this.vp3Id = vp3Id;
    }

    public String getVp3M02Id() {
        return this.vp3M02Id;
    }

    public void setVp3M02Id(String vp3m02Id) {
        this.vp3M02Id = vp3m02Id;
    }

    public String getVp3Password() {
        return this.vp3Password;
    }

    public void setVp3Password(String vp3Password) {
        this.vp3Password = vp3Password;
    }

    public String getVp3CreateUser() {
        return this.vp3CreateUser;
    }

    public void setVp3CreateUser(String vp3CreateUser) {
        this.vp3CreateUser = vp3CreateUser;
    }

    public String getVp3CreateTime() {
        return this.vp3CreateTime;
    }

    public void setVp3CreateTime(String vp3CreateTime) {
        this.vp3CreateTime = vp3CreateTime;
    }

    public String getVp3ModifyUser() {
        return this.vp3ModifyUser;
    }

    public void setVp3ModifyUser(String vp3ModifyUser) {
        this.vp3ModifyUser = vp3ModifyUser;
    }

    public String getVp3ModifyTime() {
        return this.vp3ModifyTime;
    }

    public void setVp3ModifyTime(String vp3ModifyTime) {
        this.vp3ModifyTime = vp3ModifyTime;
    }

    public String getVp3RowVersion() {
        return this.vp3RowVersion;
    }

    public void setVp3RowVersion(String vp3RowVersion) {
        this.vp3RowVersion = vp3RowVersion;
    }
}
