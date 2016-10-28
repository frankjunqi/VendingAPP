package com.mc.vending.data;

import java.io.Serializable;

public class VendingChnWrapperData implements Serializable {
    private static final long serialVersionUID = -6703331592768913133L;
    private ChnStockWrapperData chnStock;
    private int qty;

    public ChnStockWrapperData getChnStock() {
        return this.chnStock;
    }

    public void setChnStock(ChnStockWrapperData chnStock) {
        this.chnStock = chnStock;
    }

    public int getQty() {
        return this.qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
