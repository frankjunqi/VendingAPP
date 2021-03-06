package com.mc.vending.data;

import java.io.Serializable;

public class InterfaceData extends BaseParseData implements Serializable {
    private static final long serialVersionUID = -8664724073361702365L;
    private String m03CreateTime;
    private String m03CreateUser;
    private String m03EndTime;
    private int m03ExeIndex;
    private int m03ExeInterval;
    private String m03Id;
    private String m03M02Id;
    private String m03ModifyTime;
    private String m03ModifyUser;
    private String m03Name;
    private String m03Optype;
    private String m03Remark;
    private int m03RowCount;
    private String m03RowVersion;
    private String m03StartTime;
    private String m03Target;

    public String getM03Id() {
        return this.m03Id;
    }

    public void setM03Id(String m03Id) {
        this.m03Id = m03Id;
    }

    public String getM03M02Id() {
        return this.m03M02Id;
    }

    public void setM03M02Id(String m03m02Id) {
        this.m03M02Id = m03m02Id;
    }

    public String getM03Name() {
        return this.m03Name;
    }

    public void setM03Name(String m03Name) {
        this.m03Name = m03Name;
    }

    public String getM03Target() {
        return this.m03Target;
    }

    public void setM03Target(String m03Target) {
        this.m03Target = m03Target;
    }

    public String getM03Optype() {
        return this.m03Optype;
    }

    public void setM03Optype(String m03Optype) {
        this.m03Optype = m03Optype;
    }

    public String getM03Remark() {
        return this.m03Remark;
    }

    public void setM03Remark(String m03Remark) {
        this.m03Remark = m03Remark;
    }

    public int getM03ExeInterval() {
        return this.m03ExeInterval;
    }

    public void setM03ExeInterval(int m03ExeInterval) {
        this.m03ExeInterval = m03ExeInterval;
    }

    public String getM03StartTime() {
        return this.m03StartTime;
    }

    public void setM03StartTime(String m03StartTime) {
        this.m03StartTime = m03StartTime;
    }

    public String getM03EndTime() {
        return this.m03EndTime;
    }

    public void setM03EndTime(String m03EndTime) {
        this.m03EndTime = m03EndTime;
    }

    public int getM03ExeIndex() {
        return this.m03ExeIndex;
    }

    public void setM03ExeIndex(int m03ExeIndex) {
        this.m03ExeIndex = m03ExeIndex;
    }

    public int getM03RowCount() {
        return this.m03RowCount;
    }

    public void setM03RowCount(int m03RowCount) {
        this.m03RowCount = m03RowCount;
    }

    public String getM03CreateUser() {
        return this.m03CreateUser;
    }

    public void setM03CreateUser(String m03CreateUser) {
        this.m03CreateUser = m03CreateUser;
    }

    public String getM03CreateTime() {
        return this.m03CreateTime;
    }

    public void setM03CreateTime(String m03CreateTime) {
        this.m03CreateTime = m03CreateTime;
    }

    public String getM03ModifyUser() {
        return this.m03ModifyUser;
    }

    public void setM03ModifyUser(String m03ModifyUser) {
        this.m03ModifyUser = m03ModifyUser;
    }

    public String getM03ModifyTime() {
        return this.m03ModifyTime;
    }

    public void setM03ModifyTime(String m03ModifyTime) {
        this.m03ModifyTime = m03ModifyTime;
    }

    public String getM03RowVersion() {
        return this.m03RowVersion;
    }

    public void setM03RowVersion(String m03RowVersion) {
        this.m03RowVersion = m03RowVersion;
    }

    public String toString() {
        return "InterfaceData [m03Id=" + this.m03Id + ", m03M02Id=" + this.m03M02Id + ", m03Name=" + this.m03Name + ", m03Target=" + this.m03Target + ", m03Optype=" + this.m03Optype + ", m03Remark=" + this.m03Remark + ", m03ExeInterval=" + this.m03ExeInterval + ", m03StartTime=" + this.m03StartTime + ", m03EndTime=" + this.m03EndTime + ", m03ExeIndex=" + this.m03ExeIndex + ", m03RowCount=" + this.m03RowCount + ", m03CreateUser=" + this.m03CreateUser + ", m03CreateTime=" + this.m03CreateTime + ", m03ModifyUser=" + this.m03ModifyUser + ", m03ModifyTime=" + this.m03ModifyTime + ", m03RowVersion=" + this.m03RowVersion + "]";
    }
}
