package com.mc.vending.data;

import java.io.Serializable;

public class CardData extends BaseParseData implements Serializable {
    public static final String CARD_CUS_STATUS_NO = "1";
    public static final String CARD_CUS_STATUS_YES = "0";
    public static final String CARD_PASSWORD_PARAM = "password";
    public static final String CARD_PRODUCTPOWER_PRODUCT = "1";
    public static final String CARD_PRODUCTPOWER_VENDING = "0";
    public static final String CARD_SERIALNO_PARAM = "serialNo";
    public static final String CARD_STATUS_NO = "1";
    public static final String CARD_STATUS_YES = "0";
    public static final String CARD_TYPE_CUS = "1";
    public static final String CARD_TYPE_ZKH = "0";
    private static final long serialVersionUID = 4971250362917376782L;
    private String cd1Code;
    private String cd1CreateTime;
    private String cd1CreateUser;
    private String cd1CustomerStatus;
    private String cd1Id;
    private String cd1M02Id;
    private String cd1ModifyTime;
    private String cd1ModifyUser;
    private String cd1Password;
    private String cd1ProductPower;
    private String cd1Purpose;
    private String cd1RowVersion;
    private String cd1SerialNo;
    private String cd1Status;
    private String cd1Type;

    public String getCd1Id() {
        return this.cd1Id;
    }

    public void setCd1Id(String value) {
        this.cd1Id = value;
    }

    public String getCd1M02Id() {
        return this.cd1M02Id;
    }

    public void setCd1M02Id(String value) {
        this.cd1M02Id = value;
    }

    public String getCd1SerialNo() {
        return this.cd1SerialNo;
    }

    public void setCd1SerialNo(String value) {
        this.cd1SerialNo = value;
    }

    public String getCd1Code() {
        return this.cd1Code;
    }

    public void setCd1Code(String value) {
        this.cd1Code = value;
    }

    public String getCd1Type() {
        return this.cd1Type;
    }

    public void setCd1Type(String value) {
        this.cd1Type = value;
    }

    public String getCd1Password() {
        return this.cd1Password;
    }

    public void setCd1Password(String value) {
        this.cd1Password = value;
    }

    public String getCd1Purpose() {
        return this.cd1Purpose;
    }

    public void setCd1Purpose(String value) {
        this.cd1Purpose = value;
    }

    public String getCd1Status() {
        return this.cd1Status;
    }

    public void setCd1Status(String value) {
        this.cd1Status = value;
    }

    public String getCd1CustomerStatus() {
        return this.cd1CustomerStatus;
    }

    public void setCd1CustomerStatus(String value) {
        this.cd1CustomerStatus = value;
    }

    public String getCd1ProductPower() {
        return this.cd1ProductPower;
    }

    public void setCd1ProductPower(String cd1ProductPower) {
        this.cd1ProductPower = cd1ProductPower;
    }

    public String getCd1CreateUser() {
        return this.cd1CreateUser;
    }

    public void setCd1CreateUser(String value) {
        this.cd1CreateUser = value;
    }

    public String getCd1CreateTime() {
        return this.cd1CreateTime;
    }

    public void setCd1CreateTime(String value) {
        this.cd1CreateTime = value;
    }

    public String getCd1ModifyUser() {
        return this.cd1ModifyUser;
    }

    public void setCd1ModifyUser(String value) {
        this.cd1ModifyUser = value;
    }

    public String getCd1ModifyTime() {
        return this.cd1ModifyTime;
    }

    public void setCd1ModifyTime(String value) {
        this.cd1ModifyTime = value;
    }

    public String getCd1RowVersion() {
        return this.cd1RowVersion;
    }

    public void setCd1RowVersion(String value) {
        this.cd1RowVersion = value;
    }

    public String toString() {
        return "CardData [cd1Id=" + this.cd1Id + ", cd1M02Id=" + this.cd1M02Id + ", cd1SerialNo=" + this.cd1SerialNo + ", cd1Code=" + this.cd1Code + ", cd1Type=" + this.cd1Type + ", cd1Password=" + this.cd1Password + ", cd1Purpose=" + this.cd1Purpose + ", cd1Status=" + this.cd1Status + ", cd1CustomerStatus=" + this.cd1CustomerStatus + ", cd1ProductPower=" + this.cd1ProductPower + ", cd1CreateUser=" + this.cd1CreateUser + ", cd1CreateTime=" + this.cd1CreateTime + ", cd1ModifyUser=" + this.cd1ModifyUser + ", cd1ModifyTime=" + this.cd1ModifyTime + ", cd1RowVersion=" + this.cd1RowVersion + "]";
    }
}
