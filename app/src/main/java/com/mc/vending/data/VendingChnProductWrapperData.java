package com.mc.vending.data;

import java.io.Serializable;

public class VendingChnProductWrapperData implements Serializable {
    private static final long serialVersionUID = -6195552913543872304L;
    private int actQty;
    private String productName;
    private int stock;
    private VendingChnData vendingChn;

    public int getStock() {
        return this.stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public VendingChnData getVendingChn() {
        return this.vendingChn;
    }

    public void setVendingChn(VendingChnData vendingChn) {
        this.vendingChn = vendingChn;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getActQty() {
        return this.actQty;
    }

    public void setActQty(int actQty) {
        this.actQty = actQty;
    }
}
