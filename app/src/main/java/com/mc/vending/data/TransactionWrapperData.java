package com.mc.vending.data;

import java.io.Serializable;

public class TransactionWrapperData implements Serializable {
    private static final long serialVersionUID = -3580961585440586985L;
    private String createTime;
    private String createUser;
    private String name;
    private String phone;
    private int transQty;

    public String getCreateUser() {
        return this.createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getTransQty() {
        return this.transQty;
    }

    public void setTransQty(int transQty) {
        this.transQty = transQty;
    }
}
