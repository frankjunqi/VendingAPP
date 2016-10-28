package com.mc.vending.data;

import java.io.Serializable;

public class VendingCardPowerWrapperData implements Serializable {
    private static final long serialVersionUID = -3924255313116862126L;
    private String cardPuductPowerType;
    private String cusEmpId;
    private VendingCardPowerData vendingCardPowerData;

    public String getCardPuductPowerType() {
        return this.cardPuductPowerType;
    }

    public void setCardPuductPowerType(String cardPuductPowerType) {
        this.cardPuductPowerType = cardPuductPowerType;
    }

    public VendingCardPowerData getVendingCardPowerData() {
        return this.vendingCardPowerData;
    }

    public void setVendingCardPowerData(VendingCardPowerData vendingCardPowerData) {
        this.vendingCardPowerData = vendingCardPowerData;
    }

    public String getCusEmpId() {
        return this.cusEmpId;
    }

    public void setCusEmpId(String cusEmpId) {
        this.cusEmpId = cusEmpId;
    }
}
