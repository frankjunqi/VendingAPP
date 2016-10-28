package com.mc.vending.service;

import com.mc.vending.data.CardData;
import com.mc.vending.data.CusEmpCardPowerData;
import com.mc.vending.data.CustomerEmpLinkData;
import com.mc.vending.data.ReplenishmentDetailData;
import com.mc.vending.data.ReplenishmentHeadData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.SupplierData;
import com.mc.vending.data.VendingCardPowerData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingChnProductWrapperData;
import com.mc.vending.data.VendingChnStockData;
import com.mc.vending.data.VendingData;
import com.mc.vending.db.CardDbOper;
import com.mc.vending.db.CusEmpCardPowerDbOper;
import com.mc.vending.db.CustomerEmpLinkDbOper;
import com.mc.vending.db.ProductDbOper;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.db.SupplierDbOper;
import com.mc.vending.db.VendingCardPowerDbOper;
import com.mc.vending.db.VendingChnDbOper;
import com.mc.vending.db.VendingChnStockDbOper;
import com.mc.vending.db.VendingDbOper;
import com.mc.vending.tools.BusinessException;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.DateHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.SystemException;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BasicService {
    public ServiceResult<VendingData> checkVending() {
        ServiceResult<VendingData> result = new ServiceResult();
        try {
            VendingData vending = validateVending();
            result.setSuccess(true);
            result.setResult(vending);
        } catch (BusinessException be) {
            ZillionLog.e(getClass().getName(), "======>>>>检查售货机是否可用发生异常1", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(getClass().getName(), "======>>>>检查售货机是否可用发生异常0", e);
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障!>>检查售货机是否可用发生异常");
        }
        return result;
    }

    public ServiceResult<VendingChnData> checkVendingChn(String vendingChnCode, String methodType) {
        ServiceResult<VendingChnData> result = new ServiceResult();
        try {
            VendingChnData vendingChn = validateVendingChn(vendingChnCode, methodType);
            result.setSuccess(true);
            result.setResult(vendingChn);
        } catch (BusinessException be) {
            ZillionLog.e(getClass().getName(), "======>>>>检查售货机货道发生异常!", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(getClass().getName(), "======>>>>检查售货机货道发生异常!", e);
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障!>>检查售货机货道发生异常!");
        }
        return result;
    }

    public ServiceResult<VendingCardPowerWrapperData> checkCardPowerOut(String paramType, String paramValue, String vendingId) {
        ServiceResult<VendingCardPowerWrapperData> result = new ServiceResult();
        try {
            VendingCardPowerWrapperData vendingCardPowerWrapper = validateCardPower(paramType, paramValue, vendingId);
            result.setSuccess(true);
            result.setResult(vendingCardPowerWrapper);
        } catch (BusinessException be) {
            ZillionLog.e(getClass().toString(), "======>>>>检查检查卡/密码权限发生异常!" + be.getMessage(), be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(getClass().toString(), "======>>>>检查检查卡/密码权限发生异常!" + e.getMessage(), e);
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障!>>检查检查卡/密码权限发生异常!");
        }
        return result;
    }

    public ServiceResult<VendingCardPowerWrapperData> checkCardPowerInner(String serialNo, String vendingId) {
        ServiceResult<VendingCardPowerWrapperData> result = new ServiceResult();
        try {
            VendingCardPowerWrapperData vendingCardPowerWrapper = validateCardPower(serialNo, vendingId);
            result.setSuccess(true);
            result.setResult(vendingCardPowerWrapper);
        } catch (BusinessException be) {
            ZillionLog.e(getClass().toString(), "======>>>>检查检查卡/密码权限发生异常!" + be.getMessage(), be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(getClass().toString(), "======>>>>检查检查卡/密码权限发生异常!" + e.getMessage(), e);
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障>>检查检查卡/密码权限发生异常!");
        }
        return result;
    }

    private VendingData validateVending() throws SystemException {
        VendingData vending = new VendingDbOper().getVending();
        if (vending == null) {
            throw new SystemException("系统数据异常!");
        }
        if ("0".equals(vending.getVd1Status())) {
            return vending;
        }
        throw new BusinessException("本台售货无法使用,请与系统管理员联系!");
    }

    private VendingChnData validateVendingChn(String vendingChnCode, String methodType) {
        vendingChnCode = StringHelper.nullSafeString(vendingChnCode).trim();
        if (StringHelper.isEmpty(vendingChnCode, true)) {
            throw new BusinessException("货道号不能为空,请重新输入!");
        }
        VendingChnData vendingChn = new VendingChnDbOper().getVendingChnByCode(vendingChnCode);
        if (vendingChn == null) {
            throw new BusinessException("货道号  " + vendingChnCode + " 不存在,请重新输入!");
        }
        if ("0".equals(vendingChn.getVc1Status())) {
            String vendingChnSaleType = vendingChn.getVc1SaleType();
            String skuId = vendingChn.getVc1Pd1Id();
            if ("1".equals(methodType)) {
                if ("2".equals(vendingChnSaleType)) {
                    throw new BusinessException("货道号 " + vendingChnCode + " 只能'借/还',不能领料,请重新输入!");
                } else if (StringHelper.isEmpty(skuId, true)) {
                    throw new BusinessException("货道号 " + vendingChnCode + " 没有产品信息,不能领料，请重新输入!");
                }
            }
            if ("2".equals(methodType)) {
                if ("2".equals(vendingChnSaleType)) {
                    if ("0".equals(vendingChn.getVc1Type())) {
                        throw new BusinessException("货道号 " + vendingChnCode + " 非格子机,不能借/还,请重新输入！");
                    }
                }
                throw new BusinessException("货道号 " + vendingChnCode + " 不能'借/还',请重新输入!");
            }
            return vendingChn;
        }
        throw new BusinessException("货道号 " + vendingChnCode + " 已冻结,请与系统管理员联系!");
    }

    private VendingCardPowerWrapperData validateCardPower(String paramType, String paramValue, String vendingId) throws SystemException {
        CardDbOper cardDb = new CardDbOper();
        CardData card = null;
        if (CardData.CARD_SERIALNO_PARAM.equals(paramType)) {
            paramValue = StringHelper.nullSafeString(paramValue).trim();
            if (StringHelper.isEmpty(paramValue, true)) {
                throw new BusinessException("卡序列号不能为空,请重新输入!");
            }
            card = cardDb.getCardBySerialNo(paramValue);
        }
        if (CardData.CARD_PASSWORD_PARAM.equals(paramType)) {
            paramValue = StringHelper.nullSafeString(paramValue).trim();
            if (StringHelper.isEmpty(paramValue, true)) {
                throw new BusinessException("卡密码不能为空,请重新输入!");
            }
            card = cardDb.getCardByPassword(paramValue);
        }
        if (card == null) {
            throw new BusinessException("输入的卡号或密码不存在,请重新输入!");
        }
        String cardPuductPowerType = card.getCd1ProductPower();
        if ("1".equals(card.getCd1Type())) {
            String customerStatus = card.getCd1CustomerStatus();
            String status = card.getCd1Status();
            if ("0".equals(customerStatus) && "0".equals(status)) {
                String cardId = card.getCd1Id();
                CusEmpCardPowerData cusEmpCardPower = new CusEmpCardPowerDbOper().getCusEmpCardPowerByCardId(cardId);
                if (cusEmpCardPower == null) {
                    throw new BusinessException("输入的卡号或密码未分配给员工,请与系统管理员联系!");
                }
                String cusEmpId = cusEmpCardPower.getCe2Ce1Id();
                CustomerEmpLinkData customerEmpLink = new CustomerEmpLinkDbOper().getCustomerEmpLinkByCeId(cusEmpId);
                if (customerEmpLink == null) {
                    throw new SystemException("数据异常,客户不存在!");
                }
                String cusId = customerEmpLink.getCe1Cu1Id();
                if ("2".equals(customerEmpLink.getCe1Status())) {
                    throw new BusinessException("员工状态不可用,请重新输入!");
                }
                VendingCardPowerData vendingCardPower = new VendingCardPowerDbOper().getVendingCardPower(cusId, vendingId, cardId);
                if (vendingCardPower == null) {
                    throw new BusinessException("输入的卡号或密码未分配给本台售货机,请与系统管理员联系!");
                }
                VendingCardPowerWrapperData vendingCardPowerWrapper = new VendingCardPowerWrapperData();
                vendingCardPowerWrapper.setVendingCardPowerData(vendingCardPower);
                vendingCardPowerWrapper.setCusEmpId(cusEmpId);
                vendingCardPowerWrapper.setCardPuductPowerType(cardPuductPowerType);
                return vendingCardPowerWrapper;
            }
            throw new BusinessException("输入的卡号或密码不可用,请重新输入!");
        }
        throw new BusinessException("输入的卡号或密码不能领料,请重新输入!");
    }

    private VendingCardPowerWrapperData validateCardPower(String serialNo, String vendingId) throws SystemException {
        serialNo = StringHelper.nullSafeString(serialNo).trim();
        if (StringHelper.isEmpty(serialNo, true)) {
            throw new BusinessException("卡序列号不能为空,请重新输入!");
        }
        CardData card = new CardDbOper().getCardBySerialNo(serialNo);
        if (card == null) {
            throw new BusinessException("输入的卡号不存在,请重新输入!");
        }
        if ("0".equals(card.getCd1Type())) {
            if ("1".equals(card.getCd1Status())) {
                throw new BusinessException("输入的卡号不可用，请重新输入！");
            }
            String cardId = card.getCd1Id();
            CusEmpCardPowerData cusEmpCardPower = new CusEmpCardPowerDbOper().getCusEmpCardPowerByCardId(cardId);
            if (cusEmpCardPower == null) {
                throw new BusinessException("输入的卡号或密码未分配给员工,请与系统管理员联系!");
            }
            String cusEmpId = cusEmpCardPower.getCe2Ce1Id();
            VendingCardPowerData vendingCardPower = new VendingCardPowerDbOper().getVendingCardPower(vendingId, cardId);
            if (vendingCardPower == null) {
                throw new BusinessException("输入的卡号未分配给本台售货机，请与系统管理员联系！");
            }
            VendingCardPowerWrapperData vendingCardPowerWrapper = new VendingCardPowerWrapperData();
            vendingCardPowerWrapper.setVendingCardPowerData(vendingCardPower);
            vendingCardPowerWrapper.setCusEmpId(cusEmpId);
            return vendingCardPowerWrapper;
        }
        throw new BusinessException("输入的卡号不是震坤行内部卡，请重新输入！");
    }

    public Date getDate(String periodStr, String intervalStartStr, String intervalFinishStr, String startDateStr) {
        if (StringHelper.isEmpty(startDateStr, true)) {
            return null;
        }
        Date startDate = DateHelper.parse(startDateStr, "yyyy-MM-dd HH:mm:ss");
        int period = ConvertHelper.toInt(periodStr, Integer.valueOf(0)).intValue();
        if (period >= 0 && period <= 3) {
            Date date = null;
            Date tmpDate = new Date();
            int intervalStart = ConvertHelper.toInt(intervalStartStr, Integer.valueOf(0)).intValue();
            switch (period) {
                case 0:
                    date = DateHelper.add(tmpDate, 1, -intervalStart);
                    break;
                case 1:
                    date = DateHelper.add(tmpDate, 2, -intervalStart);
                    break;
                case 2:
                    date = DateHelper.getDateZero(DateHelper.add(DateHelper.add(tmpDate, 5, -intervalStart), 5, 1));
                    break;
                case 3:
                    date = DateHelper.add(tmpDate, 10, -intervalStart);
                    break;
            }
            if (date.before(startDate)) {
                return startDate;
            }
            return date;
        } else if (period != 4) {
            return null;
        } else {
            Date currentDate = new Date();
            if (currentDate.before(startDate)) {
                return null;
            }
            String startYMD = DateHelper.format(currentDate, "yyyy-MM-dd");
            Date intervalStart2 = DateHelper.parse(new StringBuilder(String.valueOf(startYMD)).append(" ").append(intervalStartStr).toString(), "yyyy-MM-dd HH:mm:ss");
            return (currentDate.before(intervalStart2) || currentDate.after(DateHelper.parse(new StringBuilder(String.valueOf(startYMD)).append(" ").append(intervalFinishStr).toString(), "yyyy-MM-dd HH:mm:ss"))) ? null : intervalStart2;
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println((String) new ArrayList().get(1));
        } catch (Exception e) {
            for (StackTraceElement stackTraceElement : e.getStackTrace()) {
            }
        }
    }

    public Date getDate2(String periodStr, String intervalStartStr, String intervalFinishStr, String startDateStr) {
        if (StringHelper.isEmpty(startDateStr, true)) {
            return null;
        }
        Date startDate = DateHelper.parse(startDateStr, "yyyy-MM-dd HH:mm:ss");
        int period = ConvertHelper.toInt(periodStr, Integer.valueOf(0)).intValue();
        if (period >= 0 && period <= 3) {
            Date date = null;
            Date tmpDate = new Date();
            int intervalStart = ConvertHelper.toInt(intervalStartStr, Integer.valueOf(0)).intValue();
            switch (period) {
                case 0:
                    date = DateHelper.parse(new StringBuilder(String.valueOf(DateHelper.format(DateHelper.add(tmpDate, 1, (int) (DateHelper.cha(tmpDate, startDate, 1) % ((long) intervalStart))), "yyyy-01-01"))).append(" 00:00:00").toString(), "yyyy-MM-dd HH:mm:ss");
                    break;
                case 1:
                    date = DateHelper.parse(new StringBuilder(String.valueOf(DateHelper.format(DateHelper.add(tmpDate, 2, (int) (DateHelper.cha(tmpDate, startDate, 2) % ((long) intervalStart))), "yyyy-MM-01"))).append(" 00:00:00").toString(), "yyyy-MM-dd HH:mm:ss");
                    break;
                case 2:
                    date = DateHelper.getDateZero(DateHelper.add(tmpDate, 5, (int) (DateHelper.cha(tmpDate, startDate, 5) % ((long) intervalStart))));
                    break;
                case 3:
                    date = DateHelper.parse(new StringBuilder(String.valueOf(DateHelper.format(DateHelper.add(tmpDate, 10, (int) (DateHelper.cha(tmpDate, startDate, 10) % ((long) intervalStart))), "yyyy-MM-dd HH"))).append(":00:00").toString(), "yyyy-MM-dd HH:mm:ss");
                    break;
            }
            if (date.before(startDate)) {
                return startDate;
            }
            return date;
        } else if (period != 4) {
            return null;
        } else {
            Date currentDate = new Date();
            if (currentDate.before(startDate)) {
                return null;
            }
            String startYMD = DateHelper.format(currentDate, "yyyy-MM-dd");
            Date intervalStart2 = DateHelper.parse(new StringBuilder(String.valueOf(startYMD)).append(" ").append(intervalStartStr).toString(), "yyyy-MM-dd HH:mm:ss");
            return (currentDate.before(intervalStart2) || currentDate.after(DateHelper.parse(new StringBuilder(String.valueOf(startYMD)).append(" ").append(intervalFinishStr).toString(), "yyyy-MM-dd HH:mm:ss"))) ? null : intervalStart2;
        }
    }

    public StockTransactionData buildStockTransaction(int qty, String billType, String billCode, VendingChnData vendingChn, VendingCardPowerWrapperData vendingCardPowerWrapper) {
        String cardId = vendingCardPowerWrapper.getVendingCardPowerData().getVc2Cd1Id();
        String cusEmpId = vendingCardPowerWrapper.getCusEmpId();
        String vendingId = vendingChn.getVc1Vd1Id();
        String vendingChnCode = vendingChn.getVc1Code();
        String skuId = vendingChn.getVc1Pd1Id();
        String saleType = vendingChn.getVc1SaleType();
        SupplierData supplier = new SupplierDbOper().getSupplierBySpId(vendingChn.getVc1Sp1Id());
        String spCode = "";
        String spName = "";
        if (supplier != null) {
            spCode = supplier.getSp1Code();
            spName = supplier.getSp1Name();
        }
        StockTransactionData stockTransaction = new StockTransactionData();
        stockTransaction.setTs1Id(UUID.randomUUID().toString());
        stockTransaction.setTs1BillType(billType);
        stockTransaction.setTs1M02Id("");
        stockTransaction.setTs1BillCode(billCode);
        stockTransaction.setTs1Cd1Id(cardId);
        stockTransaction.setTs1Vd1Id(vendingId);
        stockTransaction.setTs1Pd1Id(skuId);
        stockTransaction.setTs1TransQty(Integer.valueOf(qty));
        stockTransaction.setTs1Vc1Code(vendingChnCode);
        stockTransaction.setTs1TransType(saleType);
        stockTransaction.setTs1Sp1Code(spCode);
        stockTransaction.setTs1Sp1Name(spName);
        stockTransaction.setTs1CreateUser(cusEmpId);
        stockTransaction.setTs1UploadStatus("0");
        Date currentDate = DateHelper.currentDateTime();
        String dateTime = DateHelper.format(currentDate, "yyyy-MM-dd HH:mm:ss");
        stockTransaction.setTs1CreateTime(dateTime);
        stockTransaction.setTs1ModifyUser(cusEmpId);
        stockTransaction.setTs1ModifyTime(dateTime);
        stockTransaction.setTs1RowVersion(new StringBuilder(String.valueOf(currentDate.getTime())).toString());
        return stockTransaction;
    }

    public StockTransactionData buildStockTransaction(int qty, String billType, String billCode, String vendingId, String vendingChnCode, String skuId, String saleType, String supplierId, VendingCardPowerWrapperData vendingCardPowerWrapper) {
        String cardId = vendingCardPowerWrapper.getVendingCardPowerData().getVc2Cd1Id();
        String cusEmpId = vendingCardPowerWrapper.getCusEmpId();
        SupplierData supplier = new SupplierDbOper().getSupplierBySpId(supplierId);
        String spCode = "";
        String spName = "";
        if (supplier != null) {
            spCode = supplier.getSp1Code();
            spName = supplier.getSp1Name();
        }
        StockTransactionData stockTransaction = new StockTransactionData();
        stockTransaction.setTs1Id(UUID.randomUUID().toString());
        stockTransaction.setTs1BillType(billType);
        stockTransaction.setTs1M02Id("");
        stockTransaction.setTs1BillCode(billCode);
        stockTransaction.setTs1Cd1Id(cardId);
        stockTransaction.setTs1Vd1Id(vendingId);
        stockTransaction.setTs1Pd1Id(skuId);
        stockTransaction.setTs1TransQty(Integer.valueOf(qty));
        stockTransaction.setTs1Vc1Code(vendingChnCode);
        stockTransaction.setTs1TransType(saleType);
        stockTransaction.setTs1Sp1Code(spCode);
        stockTransaction.setTs1Sp1Name(spName);
        stockTransaction.setTs1CreateUser(cusEmpId);
        stockTransaction.setTs1UploadStatus("0");
        Date currentDate = DateHelper.currentDateTime();
        stockTransaction.setTs1CreateTime(DateHelper.format(currentDate, "yyyy-MM-dd HH:mm:ss"));
        stockTransaction.setTs1ModifyUser(cusEmpId);
        stockTransaction.setTs1ModifyTime(DateHelper.format(currentDate, "yyyy-MM-dd HH:mm:ss"));
        stockTransaction.setTs1RowVersion(new StringBuilder(String.valueOf(currentDate.getTime())).toString());
        return stockTransaction;
    }

    public List<VendingChnData> findAllVendingChnBySaleType() {
        return new VendingChnDbOper().findAllVendingChn("2");
    }

    public List<VendingChnProductWrapperData> findChnCodeProductName() {
        return findChnCodeProductName(new VendingChnDbOper().findAllVendingChn("2"));
    }

    public List<VendingChnProductWrapperData> findChnCodeProductName(List<VendingChnData> vendingChnList) {
        List<VendingChnProductWrapperData> returnList = new ArrayList();
        Map<String, String> map = new ProductDbOper().findAllProduct();
        for (VendingChnData vendingChn : vendingChnList) {
            VendingChnProductWrapperData wrapper = new VendingChnProductWrapperData();
            String skuId = vendingChn.getVc1Pd1Id();
            if (!(StringHelper.isEmpty(skuId, true) || map.get(skuId) == null)) {
                wrapper.setProductName((String) map.get(skuId));
                wrapper.setVendingChn(vendingChn);
                returnList.add(wrapper);
            }
        }
        return returnList;
    }

    public VendingChnStockData buildVendingChnStock(String vendingId, String vendingChnCode, String skuId, int qty) {
        VendingChnStockData stockData = new VendingChnStockData();
        stockData.setVs1Id(UUID.randomUUID().toString());
        stockData.setVs1M02Id("");
        stockData.setVs1Vd1Id(vendingId);
        stockData.setVs1Vc1Code(vendingChnCode);
        stockData.setVs1Pd1Id(skuId);
        stockData.setVs1Quantity(Integer.valueOf(qty));
        Date currentDate = DateHelper.currentDateTime();
        stockData.setVs1CreateUser("");
        stockData.setVs1CreateTime(DateHelper.format(currentDate, "yyyy-MM-dd HH:mm:ss"));
        stockData.setVs1ModifyUser("");
        stockData.setVs1ModifyTime(DateHelper.format(currentDate, "yyyy-MM-dd HH:mm:ss"));
        stockData.setVs1RowVersion(new StringBuilder(String.valueOf(currentDate.getTime())).toString());
        return stockData;
    }

    public ReplenishmentHeadData buildReplenishmentHead(String rhCode, String vendingId) {
        ReplenishmentHeadData data = new ReplenishmentHeadData();
        data.setRh1Id(UUID.randomUUID().toString());
        data.setRh1M02Id("");
        data.setRh1Rhcode(rhCode);
        data.setRh1RhType("3");
        data.setRh1Cu1Id("");
        data.setRh1Vd1Id(vendingId);
        data.setRh1Wh1Id("");
        data.setRh1Ce1IdPh("");
        data.setRh1DistributionRemark("");
        data.setRh1St1Id("");
        data.setRh1Ce1IdBh("");
        data.setRh1ReplenishRemark("");
        data.setRh1ReplenishReason("");
        data.setRh1OrderStatus("1");
        data.setRh1DownloadStatus("1");
        data.setLogVersion("");
        data.setRh1UploadStatus("1");
        Date currentDate = DateHelper.currentDateTime();
        data.setRh1CreateUser("System");
        data.setRh1CreateTime(DateHelper.format(currentDate, "yyyy-MM-dd HH:mm:ss"));
        data.setRh1ModifyUser("System");
        data.setRh1ModifyTime(DateHelper.format(currentDate, "yyyy-MM-dd HH:mm:ss"));
        data.setRh1RowVersion(new StringBuilder(String.valueOf(currentDate.getTime())).toString());
        return data;
    }

    public ReplenishmentDetailData buildReplenishmentDetail(String rh1Id, String vc1Code, String pd1Id, String saleType, Integer actualQty, Integer differentiaQty) {
        ReplenishmentDetailData detail = new ReplenishmentDetailData();
        detail.setRh2Id(UUID.randomUUID().toString());
        detail.setRh2M02Id("");
        detail.setRh2Rh1Id(rh1Id);
        detail.setRh2Vc1Code(vc1Code);
        detail.setRh2Pd1Id(pd1Id);
        detail.setRh2SaleType(saleType);
        detail.setRh2Sp1Id("");
        detail.setRh2ActualQty(actualQty);
        detail.setRh2DifferentiaQty(differentiaQty);
        detail.setRh2Rp1Id("");
        detail.setRh2UploadStatus("1");
        Date currentDate = DateHelper.currentDateTime();
        detail.setRh2CreateUser("System");
        detail.setRh2CreateTime(DateHelper.format(currentDate, "yyyy-MM-dd HH:mm:ss"));
        detail.setRh2ModifyUser("System");
        detail.setRh2ModifyTime(DateHelper.format(currentDate, "yyyy-MM-dd HH:mm:ss"));
        detail.setRh2RowVersion(new StringBuilder(String.valueOf(currentDate.getTime())).toString());
        return detail;
    }

    public void saveStockTransaction(int inputQty, VendingChnData vendingChn, VendingCardPowerWrapperData vendingCardPowerWrapper) {
        if (new StockTransactionDbOper().addStockTransaction(buildStockTransaction(inputQty * -1, StockTransactionData.BILL_TYPE_GET, "", vendingChn, vendingCardPowerWrapper))) {
            new VendingChnStockDbOper().updateStockQuantity(inputQty * -1, vendingChn);
        }
    }
}
