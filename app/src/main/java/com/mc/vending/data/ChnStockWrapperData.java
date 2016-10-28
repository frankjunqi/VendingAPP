package com.mc.vending.data;

import java.io.Serializable;

public class ChnStockWrapperData implements Serializable {
    private static final long serialVersionUID = 3040431937652936635L;
    private int quantity;
    private VendingChnData vendingChn;

    public VendingChnData getVendingChn() {
        return this.vendingChn;
    }

    public void setVendingChn(VendingChnData vendingChn) {
        this.vendingChn = vendingChn;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
