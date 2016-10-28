package com.mc.vending.data;

import java.io.Serializable;

public class ReplenishmentDetailWrapperData implements Serializable {
    private static final long serialVersionUID = -6446590272795987752L;
    private ReplenishmentDetailData ReplenishmentDetail;
    private String productName;

    public ReplenishmentDetailData getReplenishmentDetail() {
        return this.ReplenishmentDetail;
    }

    public void setReplenishmentDetail(ReplenishmentDetailData replenishmentDetail) {
        this.ReplenishmentDetail = replenishmentDetail;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}
