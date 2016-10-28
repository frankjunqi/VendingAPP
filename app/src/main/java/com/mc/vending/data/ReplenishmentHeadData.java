package com.mc.vending.data;

import java.io.Serializable;
import java.util.List;

public class ReplenishmentHeadData extends BaseParseData implements Serializable {
    public static final String DOWNLOAD_DOWN = "1";
    public static final String DOWNLOAD_UNDOWN = "0";
    public static final String ORDERSTATUS_CLOSED = "2";
    public static final String ORDERSTATUS_CREATED = "0";
    public static final String ORDERSTATUS_FINISHED = "1";
    public static final String RH_TYPE_All = "3";
    public static final String RH_TYPE_EMERGENCY = "1";
    public static final String RH_TYPE_PLAN = "0";
    public static final String UPLOAD_LOAD = "1";
    public static final String UPLOAD_UNLOAD = "0";
    private static final long serialVersionUID = 2701126889875907490L;
    private List<ReplenishmentDetailData> children;
    private String rh1Ce1IdBh;
    private String rh1Ce1IdPh;
    private String rh1CreateTime;
    private String rh1CreateUser;
    private String rh1Cu1Id;
    private String rh1DistributionRemark;
    private String rh1DownloadStatus;
    private String rh1Id;
    private String rh1M02Id;
    private String rh1ModifyTime;
    private String rh1ModifyUser;
    private String rh1OrderStatus;
    private String rh1ReplenishReason;
    private String rh1ReplenishRemark;
    private String rh1RhType;
    private String rh1Rhcode;
    private String rh1RowVersion;
    private String rh1St1Id;
    private String rh1UploadStatus;
    private String rh1Vd1Id;
    private String rh1Wh1Id;

    public String getRh1Id() {
        return this.rh1Id;
    }

    public void setRh1Id(String value) {
        this.rh1Id = value;
    }

    public String getRh1M02Id() {
        return this.rh1M02Id;
    }

    public void setRh1M02Id(String value) {
        this.rh1M02Id = value;
    }

    public String getRh1Rhcode() {
        return this.rh1Rhcode;
    }

    public void setRh1Rhcode(String value) {
        this.rh1Rhcode = value;
    }

    public String getRh1RhType() {
        return this.rh1RhType;
    }

    public void setRh1RhType(String value) {
        this.rh1RhType = value;
    }

    public String getRh1Cu1Id() {
        return this.rh1Cu1Id;
    }

    public void setRh1Cu1Id(String value) {
        this.rh1Cu1Id = value;
    }

    public String getRh1Vd1Id() {
        return this.rh1Vd1Id;
    }

    public void setRh1Vd1Id(String value) {
        this.rh1Vd1Id = value;
    }

    public String getRh1Wh1Id() {
        return this.rh1Wh1Id;
    }

    public void setRh1Wh1Id(String value) {
        this.rh1Wh1Id = value;
    }

    public String getRh1Ce1IdPh() {
        return this.rh1Ce1IdPh;
    }

    public void setRh1Ce1IdPh(String rh1Ce1IdPh) {
        this.rh1Ce1IdPh = rh1Ce1IdPh;
    }

    public String getRh1DistributionRemark() {
        return this.rh1DistributionRemark;
    }

    public void setRh1DistributionRemark(String value) {
        this.rh1DistributionRemark = value;
    }

    public String getRh1St1Id() {
        return this.rh1St1Id;
    }

    public void setRh1St1Id(String value) {
        this.rh1St1Id = value;
    }

    public String getRh1Ce1IdBh() {
        return this.rh1Ce1IdBh;
    }

    public void setRh1Ce1IdBh(String rh1Ce1IdBh) {
        this.rh1Ce1IdBh = rh1Ce1IdBh;
    }

    public String getRh1ReplenishRemark() {
        return this.rh1ReplenishRemark;
    }

    public void setRh1ReplenishRemark(String value) {
        this.rh1ReplenishRemark = value;
    }

    public String getRh1ReplenishReason() {
        return this.rh1ReplenishReason;
    }

    public void setRh1ReplenishReason(String value) {
        this.rh1ReplenishReason = value;
    }

    public String getRh1OrderStatus() {
        return this.rh1OrderStatus;
    }

    public void setRh1OrderStatus(String value) {
        this.rh1OrderStatus = value;
    }

    public String getRh1DownloadStatus() {
        return this.rh1DownloadStatus;
    }

    public void setRh1DownloadStatus(String value) {
        this.rh1DownloadStatus = value;
    }

    public String getRh1UploadStatus() {
        return this.rh1UploadStatus;
    }

    public void setRh1UploadStatus(String rh1UploadStatus) {
        this.rh1UploadStatus = rh1UploadStatus;
    }

    public String getRh1CreateUser() {
        return this.rh1CreateUser;
    }

    public void setRh1CreateUser(String value) {
        this.rh1CreateUser = value;
    }

    public String getRh1CreateTime() {
        return this.rh1CreateTime;
    }

    public void setRh1CreateTime(String value) {
        this.rh1CreateTime = value;
    }

    public String getRh1ModifyUser() {
        return this.rh1ModifyUser;
    }

    public void setRh1ModifyUser(String value) {
        this.rh1ModifyUser = value;
    }

    public String getRh1ModifyTime() {
        return this.rh1ModifyTime;
    }

    public void setRh1ModifyTime(String value) {
        this.rh1ModifyTime = value;
    }

    public String getRh1RowVersion() {
        return this.rh1RowVersion;
    }

    public void setRh1RowVersion(String value) {
        this.rh1RowVersion = value;
    }

    public List<ReplenishmentDetailData> getChildren() {
        return this.children;
    }

    public void setChildren(List<ReplenishmentDetailData> children) {
        this.children = children;
    }

    public String toString() {
        return "ReplenishmentHeadData [rh1Id=" + this.rh1Id + ", rh1M02Id=" + this.rh1M02Id + ", rh1Rhcode=" + this.rh1Rhcode + ", rh1RhType=" + this.rh1RhType + ", rh1Cu1Id=" + this.rh1Cu1Id + ", rh1Vd1Id=" + this.rh1Vd1Id + ", rh1Wh1Id=" + this.rh1Wh1Id + ", rh1Ce1IdPh=" + this.rh1Ce1IdPh + ", rh1DistributionRemark=" + this.rh1DistributionRemark + ", rh1St1Id=" + this.rh1St1Id + ", rh1Ce1IdBh=" + this.rh1Ce1IdBh + ", rh1ReplenishRemark=" + this.rh1ReplenishRemark + ", rh1ReplenishReason=" + this.rh1ReplenishReason + ", rh1OrderStatus=" + this.rh1OrderStatus + ", rh1DownloadStatus=" + this.rh1DownloadStatus + ", rh1UploadStatus=" + this.rh1UploadStatus + ", rh1CreateUser=" + this.rh1CreateUser + ", rh1CreateTime=" + this.rh1CreateTime + ", rh1ModifyUser=" + this.rh1ModifyUser + ", rh1ModifyTime=" + this.rh1ModifyTime + ", rh1RowVersion=" + this.rh1RowVersion + ", children=" + this.children + "]";
    }
}
