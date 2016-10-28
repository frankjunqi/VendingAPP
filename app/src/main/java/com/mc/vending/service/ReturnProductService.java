package com.mc.vending.service;

import com.mc.vending.data.RetreatHeadData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingChnProductWrapperData;
import com.mc.vending.data.VendingChnStockData;
import com.mc.vending.db.RetreatDetailDbOper;
import com.mc.vending.db.RetreatHeadDbOper;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.db.VendingChnStockDbOper;
import com.mc.vending.tools.BusinessException;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ReturnProductService extends BasicService {
    private static ReturnProductService instance;

    public static ReturnProductService getInstance() {
        if (instance == null) {
            instance = new ReturnProductService();
        }
        return instance;
    }

    public ServiceResult<Boolean> positiveReturnStockTransaction(String returnCode, List<VendingChnProductWrapperData> list, VendingCardPowerWrapperData vendingCardPowerWrapper, String rt1Id) {
        ServiceResult<Boolean> result = new ServiceResult();
        try {
            returnStockTransaction(returnCode, list, vendingCardPowerWrapper, rt1Id);
            result.setSuccess(true);
            result.setResult(Boolean.valueOf(true));
        } catch (BusinessException be) {
            ZillionLog.e(getClass().toString(), "======>>>>正向退货 发生异常", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(getClass().toString(), "======>>>>正向退货 发生异常", e);
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>正向退货 发生异常!");
        }
        return result;
    }

    private void returnStockTransaction(String returnCode, List<VendingChnProductWrapperData> list, VendingCardPowerWrapperData vendingCardPowerWrapper, String rt1Id) {
        List<StockTransactionData> stockTransactionList = new ArrayList();
        List<VendingChnStockData> updateChnStockList = new ArrayList();
        List<VendingChnStockData> addChnStockList = new ArrayList();
        VendingChnStockDbOper vendingChnStockDbOper = new VendingChnStockDbOper();
        Map<String, Integer> vendingChnStockMap = vendingChnStockDbOper.getStockMap();
        Map<String, Integer> vendingChnQtyMap = new HashMap();
        for (VendingChnProductWrapperData vendingChnProductWrapper : list) {
            VendingChnData vendingChn = vendingChnProductWrapper.getVendingChn();
            int actQty = vendingChnProductWrapper.getActQty();
            if (actQty != 0) {
                stockTransactionList.add(buildStockTransaction(actQty * -1, StockTransactionData.BILL_TYPE_RETURN, returnCode, vendingChn, vendingCardPowerWrapper));
                String vendingId = vendingChn.getVc1Vd1Id();
                String vendingChnCode = vendingChn.getVc1Code();
                VendingChnStockData vendingChnStockData = buildVendingChnStock(vendingId, vendingChnCode, vendingChn.getVc1Pd1Id(), actQty * -1);
                if (vendingChnStockMap.get(vendingChnCode) != null) {
                    updateChnStockList.add(vendingChnStockData);
                } else {
                    addChnStockList.add(vendingChnStockData);
                }
            }
            vendingChnQtyMap.put(vendingChn.getVc1Code(), Integer.valueOf(actQty));
        }
        if (stockTransactionList.isEmpty()) {
            throw new BusinessException("暂无退货产品记录，请重新填写退货数量!");
        }
        boolean flag = new StockTransactionDbOper().batchAddStockTransaction(stockTransactionList).booleanValue();
        if (rt1Id != null && flag) {
            flag = new RetreatHeadDbOper().updateState(rt1Id, vendingChnQtyMap);
        }
        if (flag) {
            if (!addChnStockList.isEmpty()) {
                vendingChnStockDbOper.batchAddVendingChnStock(addChnStockList);
            }
            if (!updateChnStockList.isEmpty()) {
                vendingChnStockDbOper.batchUpdateVendingChnStock(updateChnStockList).booleanValue();
            }
        }
    }

    public ServiceResult<Boolean> reverseReturnStockTransaction(List<VendingChnProductWrapperData> list, VendingCardPowerWrapperData vendingCardPowerWrapper) {
        ServiceResult<Boolean> result = new ServiceResult();
        try {
            returnStockTransaction("", list, vendingCardPowerWrapper);
            result.setSuccess(true);
            result.setResult(Boolean.valueOf(true));
        } catch (BusinessException be) {
            ZillionLog.e(getClass().toString(), "======>>>>反向退货 发生异常", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(getClass().toString(), "======>>>>反向退货 发生异常", e);
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>反向退货 发生异常!");
        }
        return result;
    }

    private void returnStockTransaction(String returnCode, List<VendingChnProductWrapperData> list, VendingCardPowerWrapperData vendingCardPowerWrapper) {
        returnStockTransaction(returnCode, list, vendingCardPowerWrapper, null);
    }

    public void buildRetreatHead(String returnCode, VendingCardPowerWrapperData vendingCardPowerWrapper) {
        RetreatHeadData retreatHead = new RetreatHeadData();
        retreatHead.setRt1Id(UUID.randomUUID().toString());
        retreatHead.setRt1M02Id("");
        retreatHead.setRt1Rtcode(returnCode);
        if (StringHelper.isEmpty(returnCode, true)) {
            retreatHead.setRt1Type("1");
        } else {
            retreatHead.setRt1Type("0");
        }
        String cusEmpId = vendingCardPowerWrapper.getCusEmpId();
        String cusId = vendingCardPowerWrapper.getVendingCardPowerData().getVc2Cu1Id();
        String vendingId = vendingCardPowerWrapper.getVendingCardPowerData().getVc2Vd1Id();
        retreatHead.setRt1Cu1Id(cusId);
        retreatHead.setRt1Vd1Id(vendingId);
        retreatHead.setRt1Ce1Id(cusEmpId);
        retreatHead.setRt1Status("0");
    }

    public List<VendingChnProductWrapperData> getRetreatDetailsByRtId(String rtId) {
        return new RetreatDetailDbOper().findVendingChnProductWrapperDataByRtId(rtId);
    }

    public ServiceResult<List<RetreatHeadData>> getRetreatHeadList() {
        ServiceResult<List<RetreatHeadData>> result = new ServiceResult();
        try {
            List<RetreatHeadData> replenishmentHeadList = new RetreatHeadDbOper().findAll();
            result.setSuccess(true);
            result.setResult(replenishmentHeadList);
        } catch (BusinessException be) {
            ZillionLog.e(getClass().toString(), "======>>>>查询补化主表记录发生异常", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(getClass().toString(), "======>>>>查询补化主表记录发生异常", e);
            e.printStackTrace();
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>查询补化主表记录发生异常!");
        }
        return result;
    }
}
