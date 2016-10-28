package com.mc.vending.service;

import com.mc.vending.data.InventoryHistoryData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingChnProductWrapperData;
import com.mc.vending.data.VendingChnStockData;
import com.mc.vending.db.InventoryHistoryDbOper;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.db.VendingChnStockDbOper;
import com.mc.vending.tools.DateHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InventoryService extends BasicService {
    private static InventoryService instance;

    public static InventoryService getInstance() {
        if (instance == null) {
            instance = new InventoryService();
        }
        return instance;
    }

    public ServiceResult<Boolean> saveInventoryHistory(String inventoryCode, List<VendingChnProductWrapperData> list, VendingCardPowerWrapperData vendingCardPowerWrapper) {
        ServiceResult<Boolean> result = new ServiceResult();
        try {
            List<InventoryHistoryData> inventoryHistoryList = new ArrayList();
            List<StockTransactionData> stockTransactionList = new ArrayList();
            List<VendingChnStockData> updateChnStockList = new ArrayList();
            List<VendingChnStockData> addChnStockList = new ArrayList();
            VendingChnStockDbOper vendingChnStockDbOper = new VendingChnStockDbOper();
            Map<String, Integer> stockMap = vendingChnStockDbOper.getStockMap();
            for (VendingChnProductWrapperData vendingChnProductWrapper : list) {
                VendingChnData vendingChn = vendingChnProductWrapper.getVendingChn();
                int actQty = vendingChnProductWrapper.getActQty();
                int calcMaxQty = calcActInventoryCount(vendingChn);
                if (actQty > calcMaxQty || calcMaxQty == 0) {
                    actQty = calcMaxQty;
                }
                if (actQty != 0) {
                    int stockQty;
                    inventoryHistoryList.add(buildInventoryHistory(actQty, stockMap, inventoryCode, vendingChn, vendingCardPowerWrapper));
                    String vendingChnCode = vendingChn.getVc1Code();
                    String vendingId = vendingChn.getVc1Vd1Id();
                    String skuId = vendingChn.getVc1Pd1Id();
                    if (stockMap.get(vendingChnCode) == null) {
                        stockQty = 0;
                    } else {
                        stockQty = ((Integer) stockMap.get(vendingChnCode)).intValue();
                    }
                    int difQty = actQty - stockQty;
                    if (difQty != 0) {
                        stockTransactionList.add(buildStockTransaction(difQty, "3", inventoryCode, vendingChn, vendingCardPowerWrapper));
                        VendingChnStockData vendingChnStockData = buildVendingChnStock(vendingId, vendingChnCode, skuId, difQty);
                        if (stockMap.get(vendingChnCode) != null) {
                            updateChnStockList.add(vendingChnStockData);
                        } else {
                            addChnStockList.add(vendingChnStockData);
                        }
                    }
                }
            }
            if (new InventoryHistoryDbOper().batchAddInventoryHistory(inventoryHistoryList) && new StockTransactionDbOper().batchAddStockTransaction(stockTransactionList).booleanValue()) {
                if (!addChnStockList.isEmpty()) {
                    vendingChnStockDbOper.batchAddVendingChnStock(addChnStockList);
                }
                if (!updateChnStockList.isEmpty()) {
                    vendingChnStockDbOper.batchUpdateVendingChnStock(updateChnStockList).booleanValue();
                }
            }
            result.setSuccess(true);
            result.setResult(Boolean.valueOf(true));
        } catch (Throwable be) {
            ZillionLog.e(getClass().toString(), "======>>>>现场盘点发生异常", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        }
        return result;
    }

    public InventoryHistoryData buildInventoryHistory(int actQty, Map<String, Integer> stockMap, String inventoryCode, VendingChnData vendingChn, VendingCardPowerWrapperData vendingCardPowerWrapper) {
        Date currentDate = DateHelper.currentDateTime();
        String dateTime = DateHelper.format(currentDate, "yyyy-MM-dd HH:mm:ss");
        String cusId = vendingCardPowerWrapper.getVendingCardPowerData().getVc2Cu1Id();
        String cusEmpId = vendingCardPowerWrapper.getCusEmpId();
        String vendingId = vendingChn.getVc1Vd1Id();
        String vendingChnCode = vendingChn.getVc1Code();
        String skuId = vendingChn.getVc1Pd1Id();
        int stockQty = stockMap.get(vendingChnCode) == null ? 0 : ((Integer) stockMap.get(vendingChnCode)).intValue();
        int difQty = actQty - stockQty;
        InventoryHistoryData inventoryHistory = new InventoryHistoryData();
        inventoryHistory.setIh3Id(UUID.randomUUID().toString());
        inventoryHistory.setIh3M02Id("");
        inventoryHistory.setIh3IHcode(inventoryCode);
        inventoryHistory.setIh3ActualDate(dateTime);
        inventoryHistory.setIh3Cu1Id(cusId);
        inventoryHistory.setIh3InventoryPeople(cusEmpId);
        inventoryHistory.setIh3Vd1Id(vendingId);
        inventoryHistory.setIh3Vc1Code(vendingChnCode);
        inventoryHistory.setIh3Pd1Id(skuId);
        inventoryHistory.setIh3Quantity(Integer.valueOf(stockQty));
        inventoryHistory.setIh3InventoryQty(Integer.valueOf(actQty));
        inventoryHistory.setIh3DifferentiaQty(Integer.valueOf(difQty));
        inventoryHistory.setIh3CreateUser(cusEmpId);
        inventoryHistory.setIh3UploadStatus("0");
        inventoryHistory.setIh3CreateTime(dateTime);
        inventoryHistory.setIh3ModifyUser(cusEmpId);
        inventoryHistory.setIh3ModifyTime(dateTime);
        inventoryHistory.setIh3RowVersion(new StringBuilder(String.valueOf(currentDate.getTime())).toString());
        return inventoryHistory;
    }

    private int calcActInventoryCount(VendingChnData vendingChn) {
        String vendingId = vendingChn.getVc1Vd1Id();
        String vendingChnCode = vendingChn.getVc1Code();
        int vendingChnStockCapacity = vendingChn.getVc1Capacity().intValue();
        int stockCount = GeneralMaterialService.getInstance().getVendingChnStock(vendingId, vendingChnCode);
        if (vendingChnStockCapacity < 0) {
            vendingChnStockCapacity = 0;
        }
        if (stockCount < 0) {
            stockCount = 0;
        }
        int intRtn = vendingChnStockCapacity - stockCount;
        if (intRtn < 0) {
            return 0;
        }
        return intRtn;
    }
}
