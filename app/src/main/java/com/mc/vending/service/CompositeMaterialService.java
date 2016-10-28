package com.mc.vending.service;

import com.mc.vending.data.ChnStockWrapperData;
import com.mc.vending.data.ProductGroupHeadData;
import com.mc.vending.data.ProductGroupHeadWrapperData;
import com.mc.vending.data.ProductGroupPowerData;
import com.mc.vending.data.ProductGroupWrapperData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.VendingCardPowerData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingChnStockData;
import com.mc.vending.data.VendingChnWrapperData;
import com.mc.vending.db.ProductGroupDetailDbOper;
import com.mc.vending.db.ProductGroupHeadDbOper;
import com.mc.vending.db.ProductGroupPowerDbOper;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.db.VendingChnDbOper;
import com.mc.vending.db.VendingChnStockDbOper;
import com.mc.vending.tools.BusinessException;
import com.mc.vending.tools.DateHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.ZillionLog;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompositeMaterialService extends BasicService {
    private static CompositeMaterialService instance;

    public static CompositeMaterialService getInstance() {
        if (instance == null) {
            instance = new CompositeMaterialService();
        }
        return instance;
    }

    public ServiceResult<List<ProductGroupHeadData>> findAllProductGroupHead() {
        ServiceResult<List<ProductGroupHeadData>> result = new ServiceResult();
        try {
            List<ProductGroupHeadData> list = new ProductGroupHeadDbOper().findAllProductGroupHead();
            result.setSuccess(true);
            result.setResult(list);
        } catch (BusinessException be) {
            ZillionLog.e(getClass().toString(), "======>>>>查询售货机产品组合列表发生异常", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(getClass().toString(), "======>>>>查询售货机产品组合列表发生异常", e);
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>查询售货机产品组合列表发生异常!");
        }
        return result;
    }

    public ServiceResult<ProductGroupHeadWrapperData> checkProductGroupStock(String pgCode) {
        ServiceResult<ProductGroupHeadWrapperData> result = new ServiceResult();
        try {
            ProductGroupHeadData productGroupHead = new ProductGroupHeadDbOper().getProductGroupHeadByCode(pgCode);
            if (productGroupHead == null) {
                throw new BusinessException("产品组合编号 " + pgCode + " 不存在,请重新输入!");
            }
            String pg1Id = productGroupHead.getPg1Id();
            List<ProductGroupWrapperData> wrapperList = new ProductGroupDetailDbOper().findProductGroupDetail(pg1Id);
            VendingChnDbOper vendingChnDbOper = new VendingChnDbOper();
            for (ProductGroupWrapperData productGroupWrapperData : wrapperList) {
                String skuId = productGroupWrapperData.getSkuId();
                int groupQty = productGroupWrapperData.getGroupQty();
                List<ChnStockWrapperData> chnStockList = vendingChnDbOper.findVendingChnBySkuId(skuId, "0", "0");
                int stockTotal = 0;
                int tmpQty = groupQty;
                List<VendingChnWrapperData> chnWrapperList = new ArrayList();
                for (ChnStockWrapperData chnStock : chnStockList) {
                    VendingChnWrapperData chnWrapper = new VendingChnWrapperData();
                    int quantity = chnStock.getQuantity();
                    stockTotal += quantity;
                    int qty;
                    if (tmpQty <= quantity) {
                        qty = tmpQty;
                        chnWrapper.setChnStock(chnStock);
                        chnWrapper.setQty(qty);
                        chnWrapperList.add(chnWrapper);
                        break;
                    }
                    qty = quantity;
                    chnWrapper.setChnStock(chnStock);
                    chnWrapper.setQty(qty);
                    chnWrapperList.add(chnWrapper);
                    tmpQty -= quantity;
                }
                if (stockTotal < groupQty) {
                    throw new BusinessException(productGroupWrapperData.getProductName() + "库存数量不足,不能领料，请重新输入!");
                }
                productGroupWrapperData.setChnWrapperList(chnWrapperList);
            }
            ProductGroupHeadWrapperData productGroupHeadWrapperData = new ProductGroupHeadWrapperData();
            productGroupHeadWrapperData.setPg1Id(pg1Id);
            productGroupHeadWrapperData.setWrapperList(wrapperList);
            result.setSuccess(true);
            result.setResult(productGroupHeadWrapperData);
            return result;
        } catch (BusinessException be) {
            ZillionLog.e(getClass().toString(), "======>>>>查询产品组合明细发生异常", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(getClass().toString(), "======>>>>查询产品组合明细发生异常", e);
            e.printStackTrace();
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>查询产品组合明细发生异常!");
        }
        return null;
    }

    public ServiceResult<Boolean> checkProductGroupPower(VendingCardPowerWrapperData vendingCardPowerWrapper, ProductGroupHeadWrapperData productGroupHeadWrapperData, String vendingId) {
        ServiceResult<Boolean> result = new ServiceResult();
        try {
            VendingCardPowerData vendingCardPowerData = vendingCardPowerWrapper.getVendingCardPowerData();
            String cusId = vendingCardPowerData.getVc2Cu1Id();
            String cardId = vendingCardPowerData.getVc2Cd1Id();
            ProductGroupPowerData productGroupPower = new ProductGroupPowerDbOper().getProductGroupPower(cusId, productGroupHeadWrapperData.getPg1Id(), cardId);
            if (productGroupPower == null) {
                throw new BusinessException("输入的卡号或密码无权限领料，请重新输入");
            } else if ("1".equals(productGroupPower.getPp1Power())) {
                throw new BusinessException("输入的卡号或密码无权限领料,请重新输入!");
            } else {
                String period = productGroupPower.getPp1Period();
                String intervalStart = productGroupPower.getPp1IntervalStart();
                String intervalFinish = productGroupPower.getPp1IntervalFinish();
                String startDate = productGroupPower.getPp1StartDate();
                int periodQty = productGroupPower.getPp1PeriodNum().intValue();
                List<ProductGroupWrapperData> list = productGroupHeadWrapperData.getWrapperList();
                if (!StringHelper.isEmpty(period, true)) {
                    Date date = getDate(period, intervalStart, intervalFinish, startDate);
                    StockTransactionDbOper stockTransactionDb = new StockTransactionDbOper();
                    for (ProductGroupWrapperData productGroupWrapperData : list) {
                        int inputQty = productGroupWrapperData.getGroupQty();
                        String skuId = productGroupWrapperData.getSkuId();
                        String productName = productGroupWrapperData.getProductName();
                        int transQtyTotal = 0;
                        if (date != null) {
                            transQtyTotal = stockTransactionDb.getTransQtyCountAddCardId(StockTransactionData.BILL_TYPE_GET, vendingId, skuId, DateHelper.format(date, "yyyy-MM-dd HH:mm:ss"), cardId);
                        }
                        int total = transQtyTotal * -1;
                        if (total + inputQty > periodQty * inputQty) {
                            throw new BusinessException("产品: " + productName + "的领料权限是" + (periodQty * inputQty) + ",累积已领取 " + total + ",不允许超领，请重新输入!");
                        }
                    }
                }
                result.setSuccess(true);
                result.setResult(Boolean.valueOf(true));
                return result;
            }
        } catch (BusinessException be) {
            ZillionLog.e(getClass().toString(), "======>>>>检查产品组合权限逻辑发生异常", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(getClass().toString(), "======>>>>检查产品组合权限逻辑发生异常", e);
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>检查产品组合权限逻辑发生异常!");
        }
        return null;
    }

    public void addStockTransaction1(List<ProductGroupWrapperData> wrapperList, VendingCardPowerWrapperData vendingCardPowerWrapper) {
        List<StockTransactionData> list = new ArrayList();
        List<VendingChnStockData> updateChnStockList = new ArrayList();
        for (ProductGroupWrapperData wrapper : wrapperList) {
            for (VendingChnWrapperData chnWrapper : wrapper.getChnWrapperList()) {
                int qty = chnWrapper.getQty();
                VendingChnData vendingChn = chnWrapper.getChnStock().getVendingChn();
                list.add(buildStockTransaction(qty * -1, StockTransactionData.BILL_TYPE_GET, "", vendingChn, vendingCardPowerWrapper));
                int getQty = qty * -1;
                updateChnStockList.add(buildVendingChnStock(vendingChn.getVc1Vd1Id(), vendingChn.getVc1Code(), vendingChn.getVc1Pd1Id(), getQty));
            }
        }
        if (new StockTransactionDbOper().batchAddStockTransaction(list).booleanValue() && !updateChnStockList.isEmpty()) {
            new VendingChnStockDbOper().batchUpdateVendingChnStock(updateChnStockList).booleanValue();
        }
    }
}
