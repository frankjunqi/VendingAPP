package com.mc.vending.data;

public class BaseParseData {
    private String CreateTime;
    private String CreateUser;
    private String ModifyTime;
    private String ModifyUser;
    private String RowVersion;
    private String logVersion;

    public String getRowVersion() {
        return this.RowVersion;
    }

    public void setRowVersion(String rowVersion) {
        this.RowVersion = rowVersion;
    }

    public String getLogVersion() {
        return this.logVersion;
    }

    public void setLogVersion(String logVersion) {
        this.logVersion = logVersion;
    }

    public String getCreateUser() {
        return this.CreateUser;
    }

    public void setCreateUser(String createUser) {
        this.CreateUser = createUser;
    }

    public String getCreateTime() {
        return this.CreateTime;
    }

    public void setCreateTime(String createTime) {
        this.CreateTime = createTime;
    }

    public String getModifyUser() {
        return this.ModifyUser;
    }

    public void setModifyUser(String modifyUser) {
        this.ModifyUser = modifyUser;
    }

    public String getModifyTime() {
        return this.ModifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.ModifyTime = modifyTime;
    }
}
