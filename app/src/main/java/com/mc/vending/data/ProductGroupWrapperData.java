package com.mc.vending.data;

import java.io.Serializable;
import java.util.List;

public class ProductGroupWrapperData implements Serializable {
    private static final long serialVersionUID = -977644926373807778L;
    private List<VendingChnWrapperData> chnWrapperList;
    private int groupQty;
    private String productName;
    private String skuId;

    public String getSkuId() {
        return this.skuId;
    }

    public void setSkuId(String skuId) {
        this.skuId = skuId;
    }

    public String getProductName() {
        return this.productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getGroupQty() {
        return this.groupQty;
    }

    public void setGroupQty(int groupQty) {
        this.groupQty = groupQty;
    }

    public List<VendingChnWrapperData> getChnWrapperList() {
        return this.chnWrapperList;
    }

    public void setChnWrapperList(List<VendingChnWrapperData> chnWrapperList) {
        this.chnWrapperList = chnWrapperList;
    }
}
