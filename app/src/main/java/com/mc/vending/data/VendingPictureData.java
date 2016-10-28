package com.mc.vending.data;

import java.io.Serializable;

public class VendingPictureData extends BaseParseData implements Serializable {
    public static final String VENDING_PICTURE_DAIJI = "1";
    public static final String VENDING_PICTURE_DEFAULT = "0";
    private static final long serialVersionUID = 1459829408727533938L;
    private String vp2CreateTime;
    private String vp2CreateUser;
    private String vp2FilePath;
    private String vp2Id;
    private String vp2M02Id;
    private String vp2ModifyTime;
    private String vp2ModifyUser;
    private String vp2RowVersion;
    private int vp2RunTime;
    private String vp2Seq;
    private String vp2Type;

    public String getVp2Id() {
        return this.vp2Id;
    }

    public void setVp2Id(String vp2Id) {
        this.vp2Id = vp2Id;
    }

    public String getVp2M02Id() {
        return this.vp2M02Id;
    }

    public void setVp2M02Id(String vp2m02Id) {
        this.vp2M02Id = vp2m02Id;
    }

    public String getVp2Seq() {
        return this.vp2Seq;
    }

    public void setVp2Seq(String vp2Seq) {
        this.vp2Seq = vp2Seq;
    }

    public String getVp2FilePath() {
        return this.vp2FilePath;
    }

    public void setVp2FilePath(String vp2FilePath) {
        this.vp2FilePath = vp2FilePath;
    }

    public int getVp2RunTime() {
        return this.vp2RunTime;
    }

    public void setVp2RunTime(int vp2RunTime) {
        this.vp2RunTime = vp2RunTime;
    }

    public String getVp2Type() {
        return this.vp2Type;
    }

    public void setVp2Type(String vp2Type) {
        this.vp2Type = vp2Type;
    }

    public String getVp2CreateUser() {
        return this.vp2CreateUser;
    }

    public void setVp2CreateUser(String vp2CreateUser) {
        this.vp2CreateUser = vp2CreateUser;
    }

    public String getVp2CreateTime() {
        return this.vp2CreateTime;
    }

    public void setVp2CreateTime(String vp2CreateTime) {
        this.vp2CreateTime = vp2CreateTime;
    }

    public String getVp2ModifyUser() {
        return this.vp2ModifyUser;
    }

    public void setVp2ModifyUser(String vp2ModifyUser) {
        this.vp2ModifyUser = vp2ModifyUser;
    }

    public String getVp2ModifyTime() {
        return this.vp2ModifyTime;
    }

    public void setVp2ModifyTime(String vp2ModifyTime) {
        this.vp2ModifyTime = vp2ModifyTime;
    }

    public String getVp2RowVersion() {
        return this.vp2RowVersion;
    }

    public void setVp2RowVersion(String vp2RowVersion) {
        this.vp2RowVersion = vp2RowVersion;
    }

    public String toString() {
        return "VendingPictureData [vp2Id=" + this.vp2Id + ", vp2M02Id=" + this.vp2M02Id + ", vp2Seq=" + this.vp2Seq + ", vp2FilePath=" + this.vp2FilePath + ", vp2RunTime=" + this.vp2RunTime + ", vp2CreateUser=" + this.vp2CreateUser + ", vp2CreateTime=" + this.vp2CreateTime + ", vp2ModifyUser=" + this.vp2ModifyUser + ", vp2ModifyTime=" + this.vp2ModifyTime + ", vp2RowVersion=" + this.vp2RowVersion + "]";
    }
}
