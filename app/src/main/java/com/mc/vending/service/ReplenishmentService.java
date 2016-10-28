package com.mc.vending.service;

import com.mc.vending.data.ReplenishmentDetailData;
import com.mc.vending.data.ReplenishmentDetailWrapperData;
import com.mc.vending.data.ReplenishmentHeadData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingChnProductWrapperData;
import com.mc.vending.data.VendingChnStockData;
import com.mc.vending.db.ProductDbOper;
import com.mc.vending.db.ReplenishmentDetailDbOper;
import com.mc.vending.db.ReplenishmentHeadDbOper;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.db.VendingChnDbOper;
import com.mc.vending.db.VendingChnStockDbOper;
import com.mc.vending.tools.BusinessException;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.DateHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.Tools;
import com.mc.vending.tools.ZillionLog;
import com.mc.vending.tools.utils.SerialTools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReplenishmentService extends BasicService {
    private static ReplenishmentService instance;

    public static ReplenishmentService getInstance() {
        if (instance == null) {
            instance = new ReplenishmentService();
        }
        return instance;
    }

    public ServiceResult<Boolean> oneKeyReplenishment(VendingCardPowerWrapperData vendingCardPowerWrapper) {
        ServiceResult<Boolean> result = new ServiceResult();
        try {
            ReplenishmentHeadData replenishmentHead = new ReplenishmentHeadDbOper().getReplenishmentHeadByOrderStatus("0");
            if (replenishmentHead == null) {
                ZillionLog.e(getClass().getName(), "oneKeyReplenishment err 不存在未完成的补货单,请与计划员联系!");
                throw new BusinessException("不存在未完成的补货单,请与计划员联系!");
            }
            String rh1Id = replenishmentHead.getRh1Id();
            String vendingId = replenishmentHead.getRh1Vd1Id();
            String rh1Rhcode = replenishmentHead.getRh1Rhcode();
            List<ReplenishmentDetailData> list = new ReplenishmentDetailDbOper().findReplenishmentDetailkByRh1Id(rh1Id);
            List<StockTransactionData> stockTransactionList = new ArrayList();
            List<VendingChnStockData> updateChnStockList = new ArrayList();
            List<VendingChnStockData> addChnStockList = new ArrayList();
            VendingChnStockDbOper vendingChnStockDbOper = new VendingChnStockDbOper();
            Map<String, Integer> vendingChnStockMap = vendingChnStockDbOper.getStockMap();
            for (ReplenishmentDetailData replenishmentDetail : list) {
                String vendingChnCode = replenishmentDetail.getRh2Vc1Code();
                String skuId = replenishmentDetail.getRh2Pd1Id();
                String saleType = replenishmentDetail.getRh2SaleType();
                int qty = replenishmentDetail.getRh2ActualQty().intValue();
                stockTransactionList.add(buildStockTransaction(qty, "0", rh1Rhcode, vendingId, vendingChnCode, skuId, saleType, replenishmentDetail.getRh2Sp1Id(), vendingCardPowerWrapper));
                VendingChnData vendingChn = new VendingChnDbOper().getVendingChnByCode(vendingChnCode);
                if (vendingChn.getVc1Type().equals("1")) {
                    ZillionLog.i("格子机补货，打开格子机==========================", vendingChnCode);
                    SerialTools.getInstance().openStore(ConvertHelper.toInt(vendingChn.getVc1LineNum(), Integer.valueOf(0)).intValue(), ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), Integer.valueOf(0)).intValue(), ConvertHelper.toInt(vendingChn.getVc1Height(), Integer.valueOf(0)).intValue());
                }
                VendingChnStockData vendingChnStockData = buildVendingChnStock(vendingId, vendingChnCode, skuId, qty);
                if (vendingChnStockMap.get(vendingChnCode) != null) {
                    updateChnStockList.add(vendingChnStockData);
                } else {
                    addChnStockList.add(vendingChnStockData);
                }
            }
            if (new StockTransactionDbOper().batchAddStockTransaction(stockTransactionList).booleanValue()) {
                if (!addChnStockList.isEmpty()) {
                    vendingChnStockDbOper.batchAddVendingChnStock(addChnStockList);
                }
                if (!updateChnStockList.isEmpty()) {
                    vendingChnStockDbOper.batchUpdateVendingChnStock(updateChnStockList).booleanValue();
                }
            }
            new ReplenishmentHeadDbOper().updateOrderStatusByRh1Id(rh1Id, "1");
            result.setSuccess(true);
            result.setResult(Boolean.valueOf(true));
            result.setMessage(rh1Rhcode);
            return result;
        } catch (BusinessException be) {
            ZillionLog.e(getClass().toString(), "======>>>>一键补货发生异常", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(getClass().toString(), "======>>>>一键补货发生异常", e);
            e.printStackTrace();
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>一键补货发生异常!");
        }
        return null;
    }

    public ServiceResult<Boolean> oneKeyReplenishAll(VendingCardPowerWrapperData vendingCardPowerWrapper) {
        ServiceResult<Boolean> result = new ServiceResult();
        try {
            if (new ReplenishmentHeadDbOper().getReplenishmentHeadByOrderStatus("0") != null) {
                throw new BusinessException("存在未完成的补货单,请先进行一键补货!");
            }
            String vendingId = vendingCardPowerWrapper.getVendingCardPowerData().getVc2Vd1Id();
            List<VendingChnData> vendingChns = new VendingChnDbOper().findAll();
            List<StockTransactionData> sList = new ArrayList();
            List<VendingChnStockData> updateChnStockList = new ArrayList();
            List<VendingChnStockData> addChnStockList = new ArrayList();
            List<ReplenishmentDetailData> detailDatas = new ArrayList();
            String rhCode = Tools.getVendCode() + DateHelper.format(DateHelper.currentDateTime(), "yyMMddHHmmss");
            ReplenishmentHeadData rHead = buildReplenishmentHead(rhCode, vendingId);
            Map<String, VendingChnStockData> chnStockDataMap = new VendingChnStockDbOper().getStockDataMap();
            for (VendingChnData chnData : vendingChns) {
                if (chnData.getVc1Type().equals("0") && chnData.getVc1Status().equals("0") && !chnData.getVc1SaleType().equals("2")) {
                    int qty;
                    if (chnStockDataMap.containsKey(chnData.getVc1Code())) {
                        VendingChnStockData chnStockData = (VendingChnStockData) chnStockDataMap.get(chnData.getVc1Code());
                        qty = chnData.getVc1Capacity().intValue() - chnStockData.getVs1Quantity().intValue();
                        if (qty > 0 && qty <= chnData.getVc1Capacity().intValue()) {
                            chnStockData.setVs1Quantity(Integer.valueOf(qty));
                            updateChnStockList.add(chnStockData);
                        }
                    } else {
                        qty = chnData.getVc1Capacity().intValue();
                        if (qty > 0 && qty <= chnData.getVc1Capacity().intValue()) {
                            addChnStockList.add(buildVendingChnStock(vendingId, chnData.getVc1Code(), chnData.getVc1Pd1Id(), qty));
                        }
                    }
                    if (qty > 0 && qty <= chnData.getVc1Capacity().intValue()) {
                        sList.add(buildStockTransaction(qty, StockTransactionData.BILL_TYPE_All, rhCode, vendingId, chnData.getVc1Code(), chnData.getVc1Pd1Id(), chnData.getVc1SaleType(), chnData.getVc1Sp1Id(), vendingCardPowerWrapper));
                        detailDatas.add(buildReplenishmentDetail(rHead.getRh1Id(), chnData.getVc1Code(), chnData.getVc1Pd1Id(), chnData.getVc1SaleType(), Integer.valueOf(qty), Integer.valueOf(0)));
                    }
                }
            }
            if (detailDatas == null || detailDatas.size() == 0) {
                throw new BusinessException("没有要补货的货道!");
            }
            boolean flag = new StockTransactionDbOper().batchAddStockTransaction(sList).booleanValue();
            if (flag) {
                if (!addChnStockList.isEmpty()) {
                    new VendingChnStockDbOper().batchDeleteVendingChnStock(addChnStockList);
                    flag = new VendingChnStockDbOper().batchAddVendingChnStock(addChnStockList);
                }
                if (flag) {
                    if (!updateChnStockList.isEmpty()) {
                        flag = new VendingChnStockDbOper().batchUpdateVendingChnStock(updateChnStockList).booleanValue();
                    }
                    if (flag && detailDatas != null && detailDatas.size() > 0) {
                        rHead.setChildren(detailDatas);
                        List<ReplenishmentHeadData> rHeadList = new ArrayList();
                        rHeadList.add(rHead);
                        flag = new ReplenishmentHeadDbOper().batchAddReplenishmentHead(rHeadList);
                    }
                }
            }
            result.setSuccess(true);
            result.setResult(Boolean.valueOf(true));
            result.setMessage(rhCode);
            return result;
        } catch (BusinessException be) {
            ZillionLog.e(getClass().toString(), "======>>>>一键补满发生异常1", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Throwable e) {
            ZillionLog.e(getClass().toString(), "======>>>>一键补满发生异常2", e);
            e.printStackTrace();
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>一键补货发生异常!");
        }
        return null;
    }

    public ServiceResult<List<ReplenishmentHeadData>> getReplenishmentHead(String rhType) {
        ServiceResult<List<ReplenishmentHeadData>> result = new ServiceResult();
        try {
            List<ReplenishmentHeadData> replenishmentHeadList = new ReplenishmentHeadDbOper().findReplenishmentHeadByOrderStatus("1", rhType);
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

    public List<ReplenishmentDetailWrapperData> findReplenishmentDetail(String rh1Id) {
        List<ReplenishmentDetailWrapperData> returnList = new ArrayList();
        List<ReplenishmentDetailData> list = new ReplenishmentDetailDbOper().findReplenishmentDetailkByRh1Id(rh1Id);
        Map<String, String> map = new ProductDbOper().findAllProduct();
        for (ReplenishmentDetailData replenishmentDetail : list) {
            String productName = (String) map.get(replenishmentDetail.getRh2Pd1Id());
            ReplenishmentDetailWrapperData wrapperData = new ReplenishmentDetailWrapperData();
            wrapperData.setReplenishmentDetail(replenishmentDetail);
            wrapperData.setProductName(productName);
            returnList.add(wrapperData);
        }
        return returnList;
    }

    public ServiceResult<Boolean> updateReplenishmentDetail(ReplenishmentHeadData replenishmentHead, List<ReplenishmentDetailWrapperData> list, VendingCardPowerWrapperData vendingCardPowerWrapper, String billType) {
        ServiceResult<Boolean> result = new ServiceResult();
        try {
            String vendingId = replenishmentHead.getRh1Vd1Id();
            String rh1Rhcode = replenishmentHead.getRh1Rhcode();
            List<StockTransactionData> stockTransactionList = new ArrayList();
            List<ReplenishmentDetailData> newList = new ArrayList();
            List<VendingChnStockData> updateChnStockList = new ArrayList();
            List<VendingChnStockData> addChnStockList = new ArrayList();
            VendingChnStockDbOper vendingChnStockDbOper = new VendingChnStockDbOper();
            Map<String, Integer> vendingChnStockMap = vendingChnStockDbOper.getStockMap();
            for (ReplenishmentDetailWrapperData replenishmentDetailWrapper : list) {
                ReplenishmentDetailData replenishmentDetail = replenishmentDetailWrapper.getReplenishmentDetail();
                int differentiaQty = replenishmentDetail.getRh2DifferentiaQty().intValue();
                if (differentiaQty != 0) {
                    String vendingChnCode = replenishmentDetail.getRh2Vc1Code();
                    String skuId = replenishmentDetail.getRh2Pd1Id();
                    stockTransactionList.add(buildStockTransaction(differentiaQty, billType, rh1Rhcode, vendingId, vendingChnCode, skuId, replenishmentDetail.getRh2SaleType(), replenishmentDetail.getRh2Sp1Id(), vendingCardPowerWrapper));
                    VendingChnStockData vendingChnStockData = buildVendingChnStock(vendingId, vendingChnCode, skuId, differentiaQty);
                    if (vendingChnStockMap.get(vendingChnCode) != null) {
                        updateChnStockList.add(vendingChnStockData);
                    } else {
                        addChnStockList.add(vendingChnStockData);
                    }
                    newList.add(replenishmentDetail);
                }
            }
            if (newList.isEmpty()) {
                throw new BusinessException("暂无补货差异记录，请重新填写补货差异数量!");
            }
            if (new ReplenishmentDetailDbOper().batchUpdateDifferentiaQty(newList) && new StockTransactionDbOper().batchAddStockTransaction(stockTransactionList).booleanValue()) {
                if (!addChnStockList.isEmpty()) {
                    vendingChnStockDbOper.batchAddVendingChnStock(addChnStockList);
                }
                if (!updateChnStockList.isEmpty()) {
                    vendingChnStockDbOper.batchUpdateVendingChnStock(updateChnStockList).booleanValue();
                }
            }
            result.setSuccess(true);
            result.setResult(Boolean.valueOf(true));
            return result;
        } catch (BusinessException be) {
            ZillionLog.e(getClass().toString(), "======>>>>批量更新保存补货差异发生异常", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(getClass().toString(), "======>>>>批量更新保存补货差异发生异常", e);
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>批量更新保存补货差异发生异常!");
        }
        return null;
    }

    public ServiceResult<Boolean> emergencyReplenishment(String billCode, List<VendingChnProductWrapperData> list, VendingCardPowerWrapperData vendingCardPowerWrapper) {
        ServiceResult<Boolean> result = new ServiceResult();
        try {
            List<StockTransactionData> stockTransactionList = new ArrayList();
            List<VendingChnStockData> updateChnStockList = new ArrayList();
            List<VendingChnStockData> addChnStockList = new ArrayList();
            VendingChnStockDbOper vendingChnStockDbOper = new VendingChnStockDbOper();
            Map<String, Integer> vendingChnStockMap = vendingChnStockDbOper.getStockMap();
            for (VendingChnProductWrapperData vendingChnProductWrapper : list) {
                VendingChnData vendingChn = vendingChnProductWrapper.getVendingChn();
                int actQty = vendingChnProductWrapper.getActQty();
                int calcMaxQty = calcActReplenishmentCount(vendingChn);
                if (actQty > calcMaxQty || calcMaxQty == 0) {
                    actQty = calcMaxQty;
                }
                if (actQty != 0) {
                    if (vendingChn.getVc1Type().equals("1")) {
                        ZillionLog.i(getClass().getName(), vendingChn.getVc1Code() + "格子机补货，打开格子机");
                        SerialTools.getInstance().openStore(ConvertHelper.toInt(vendingChn.getVc1LineNum(), Integer.valueOf(0)).intValue(), ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), Integer.valueOf(0)).intValue(), ConvertHelper.toInt(vendingChn.getVc1Height(), Integer.valueOf(0)).intValue());
                    }
                    stockTransactionList.add(buildStockTransaction(actQty, "2", billCode, vendingChn, vendingCardPowerWrapper));
                    String vendingId = vendingChn.getVc1Vd1Id();
                    String vendingChnCode = vendingChn.getVc1Code();
                    VendingChnStockData vendingChnStockData = buildVendingChnStock(vendingId, vendingChnCode, vendingChn.getVc1Pd1Id(), actQty);
                    if (vendingChnStockMap.get(vendingChnCode) != null) {
                        updateChnStockList.add(vendingChnStockData);
                    } else {
                        addChnStockList.add(vendingChnStockData);
                    }
                }
            }
            if (stockTransactionList.isEmpty()) {
                throw new BusinessException("暂无紧急补货记录，请重新填写补货数量!");
            }
            if (new StockTransactionDbOper().batchAddStockTransaction(stockTransactionList).booleanValue()) {
                if (!addChnStockList.isEmpty()) {
                    vendingChnStockDbOper.batchAddVendingChnStock(addChnStockList);
                }
                if (!updateChnStockList.isEmpty()) {
                    vendingChnStockDbOper.batchUpdateVendingChnStock(updateChnStockList).booleanValue();
                }
            }
            result.setSuccess(true);
            result.setResult(Boolean.valueOf(true));
            return result;
        } catch (BusinessException be) {
            ZillionLog.e(getClass().toString(), "======>>>>紧急补货发生异常", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(getClass().toString(), "======>>>>紧急补货发生异常", e);
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>紧急补货发生异常!");
        }
        return null;
    }

    private int calcActReplenishmentCount(VendingChnData vendingChn) {
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
