package com.mc.vending.service;

import com.mc.vending.data.CardData;
import com.mc.vending.data.ConversionData;
import com.mc.vending.data.ProductCardPowerData;
import com.mc.vending.data.ProductMaterialPowerData;
import com.mc.vending.data.ProductPictureData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.VendingChnStockData;
import com.mc.vending.data.VendingProLinkData;
import com.mc.vending.db.CardDbOper;
import com.mc.vending.db.ConversionDbOper;
import com.mc.vending.db.ProductCardPowerDbOper;
import com.mc.vending.db.ProductMaterialPowerDbOper;
import com.mc.vending.db.ProductPictureDbOper;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.db.UsedRecordDbOper;
import com.mc.vending.db.VendingChnStockDbOper;
import com.mc.vending.db.VendingProLinkDbOper;
import com.mc.vending.tools.BusinessException;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.DateHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.ZillionLog;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class GeneralMaterialService extends BasicService {
    private static GeneralMaterialService instance;

    public static GeneralMaterialService getInstance() {
        if (instance == null) {
            instance = new GeneralMaterialService();
        }
        return instance;
    }

    public ProductPictureData getPictureBySkuId(String skuId) {
        return new ProductPictureDbOper().getProductPictureBySku(skuId);
    }

    public int getVendingChnStock(String vendingId, String vendingChnCode) {
        VendingChnStockData vendingChnStock = new VendingChnStockDbOper().getStockByVidAndVcCode(vendingId, vendingChnCode);
        if (vendingChnStock == null) {
            return 0;
        }
        return vendingChnStock.getVs1Quantity().intValue();
    }

    public ServiceResult<Boolean> checkProductMaterialPower(String vendingId, String skuId, String cusId, String vc2Id, int inputQty, String vendingChnCode, String cardProductPower, String cardId) {
        ServiceResult<Boolean> result = new ServiceResult();
        try {
            if ("1".equals(cardProductPower)) {
                handlerMaterialPowerProduct(vendingId, skuId, cardId, inputQty, vendingChnCode);
            } else if ("0".equals(cardProductPower)) {
                handlerMaterialPower(vendingId, skuId, cusId, vc2Id, inputQty, vendingChnCode, cardId);
            }
            result.setSuccess(true);
            result.setResult(Boolean.valueOf(true));
        } catch (BusinessException be) {
            ZillionLog.e(getClass().toString(), "======>>>>检查产品领料权限发生异常", be);
            result.setMessage(be.getMessage());
            result.setCode("1");
            result.setSuccess(false);
        } catch (Exception e) {
            ZillionLog.e(getClass().toString(), "======>>>>检查产品领料权限发生异常", e);
            result.setSuccess(false);
            result.setCode("0");
            result.setMessage("售货机系统故障!>>检查产品领料权限发生异常");
        }
        return result;
    }

    private void handlerMaterialPower(String vendingId, String skuId, String cusId, String vc2Id, int inputQty, String vendingChnCode, String cardId) {
        boolean isPackageChn = false;
        Map<String, Object> cpIdWithScaleList = new HashMap();
        int preInputQty = 0;
        String operation = "包";
        vc2Id = StringHelper.nullSafeString(vc2Id).trim();
        String preSkuId = skuId;
        ProductMaterialPowerDbOper productMaterialPowerDbOper = new ProductMaterialPowerDbOper();
        List<String> list = productMaterialPowerDbOper.findVendingProLinkByVcId(vc2Id);
        if (!list.isEmpty()) {
            ConversionDbOper conversionDbOper = new ConversionDbOper();
            ConversionData conversionData = conversionDbOper.findConversionByCpid(skuId);
            if (conversionData != null) {
                skuId = conversionData.getCn1Upid();
                preInputQty = inputQty;
                inputQty *= ConvertHelper.toInt(conversionData.getCn1Proportion(), Integer.valueOf(inputQty)).intValue();
                operation = StringHelper.isEmpty(conversionData.getCn1Operation()) ? "包" : conversionData.getCn1Operation();
                isPackageChn = true;
            }
            cpIdWithScaleList = conversionDbOper.findConversionByUpid(skuId);
            if (cpIdWithScaleList != null) {
            }
            if (list.contains(skuId)) {
                VendingProLinkData vendingProLink = new VendingProLinkDbOper().getVendingProLinkByVidAndSkuId(vendingId, skuId);
                if (vendingProLink == null) {
                    throw new BusinessException("售货机产品 不存在!");
                }
                ProductMaterialPowerData productMaterialPower = productMaterialPowerDbOper.getVendingProLinkByVidAndSkuId(cusId, vc2Id, vendingProLink.getVp1Id());
                if (productMaterialPower == null) {
                    return;
                }
                if ("1".equals(productMaterialPower.getPm1Power())) {
                    throw new BusinessException("输入的卡号或密码无权限领料,请重新输入!");
                }
                int onceQty = productMaterialPower.getPm1OnceQty().intValue();
                String period = productMaterialPower.getPm1Period();
                String intervalStart = productMaterialPower.getPm1IntervalStart();
                String intervalFinish = productMaterialPower.getPm1IntervalFinish();
                String startDate = productMaterialPower.getPm1StartDate();
                int periodQty = productMaterialPower.getPm1PeriodQty().intValue();
                if (onceQty != 0 && inputQty > onceQty) {
                    throw new BusinessException("一次最多领取 " + onceQty + " 数量,请重新输入!");
                } else if (!StringHelper.isEmpty(period, true)) {
                    Date date = getDate2(period, intervalStart, intervalFinish, startDate);
                    int transQtyTotal = 0;
                    if (date != null) {
                        String dateStr = DateHelper.format(date, "yyyy-MM-dd HH:mm:ss");
                        try {
                            UsedRecordDbOper usedRecordDbOper = new UsedRecordDbOper();
                            StockTransactionDbOper stockTransactionDb = new StockTransactionDbOper();
                            transQtyTotal = usedRecordDbOper.getTransQtyCount(cardId, skuId, dateStr);
                            int transQtyTotal1 = stockTransactionDb.getTransQtyCount(StockTransactionData.BILL_TYPE_GET, vendingId, skuId, vendingChnCode, dateStr, cardId);
                            if (cpIdWithScaleList != null) {
                                for (Entry<String, Object> entry : cpIdWithScaleList.entrySet()) {
                                    String innerCpId = (String) entry.getKey();
                                    int innerScale = ConvertHelper.toInt(entry.getValue(), Integer.valueOf(1)).intValue();
                                    transQtyTotal += usedRecordDbOper.getTransQtyCount(cardId, innerCpId, dateStr) * innerScale;
                                    transQtyTotal1 += stockTransactionDb.getTransQtyCount(StockTransactionData.BILL_TYPE_GET, vendingId, innerCpId, vendingChnCode, dateStr, cardId) * innerScale;
                                }
                            }
                            transQtyTotal *= -1;
                            if (transQtyTotal1 < transQtyTotal) {
                                transQtyTotal = transQtyTotal1;
                            }
                        } catch (Throwable e) {
                            ZillionLog.e(getClass().toString(), "产品领料权限检查错误", e);
                            throw new BusinessException("产品领料权限检查错误,库存交易记录数：" + transQtyTotal);
                        }
                    }
                    int total = transQtyTotal * -1;
                    if (inputQty > periodQty) {
                        if (isPackageChn) {
                            throw new BusinessException("你的领料权限是" + periodQty + "，输入了" + inputQty + "(" + preInputQty + operation + ")" + "，不允许超领！");
                        }
                        throw new BusinessException("你的领料权限是" + periodQty + "，输入了" + inputQty + "，不允许超领！");
                    } else if (total + inputQty > periodQty) {
                        throw new BusinessException("你的领料权限是" + periodQty + "，你已领取" + total + "，不允许超领！");
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            throw new BusinessException("输入的卡号或密码无权限领料，请重新输入！");
        }
    }

    private void handlerMaterialPowerProduct(String vendingId, String skuId, String cardId, int inputQty, String vendingChnCode) {
        boolean isPackageChn = false;
        Map<String, Object> cpIdWithScaleList = new HashMap();
        int preInputQty = 0;
        String operation = "包";
        cardId = StringHelper.nullSafeString(cardId).trim();
        String preSkuId = skuId;
        ProductCardPowerDbOper dbOper = new ProductCardPowerDbOper();
        if (new VendingProLinkDbOper().getVendingProLinkByVidAndSkuId(vendingId, skuId) == null) {
            throw new BusinessException("售货机产品 不存在!");
        }
        List<String> list = dbOper.getVendingProLinkByCid(cardId);
        if (!list.isEmpty()) {
            ConversionDbOper conversionDbOper = new ConversionDbOper();
            ConversionData conversionData = conversionDbOper.findConversionByCpid(skuId);
            if (conversionData != null) {
                skuId = conversionData.getCn1Upid();
                preInputQty = inputQty;
                inputQty *= ConvertHelper.toInt(conversionData.getCn1Proportion(), Integer.valueOf(inputQty)).intValue();
                operation = StringHelper.isEmpty(conversionData.getCn1Operation()) ? "包" : conversionData.getCn1Operation();
                isPackageChn = true;
            }
            cpIdWithScaleList = conversionDbOper.findConversionByUpid(skuId);
            if (cpIdWithScaleList != null) {
            }
            if (list.contains(skuId)) {
                ProductCardPowerData powerData = dbOper.getVendingProLinkByVidAndSkuId(cardId, skuId);
                if (powerData == null) {
                    return;
                }
                if ("1".equals(powerData.getPc1Power())) {
                    throw new BusinessException("输入的卡号或密码无产品领料权限,请重新输入!");
                }
                int onceQty = 0;
                if (!(powerData.getPc1OnceQty() == null || powerData.getPc1OnceQty().equals(""))) {
                    onceQty = Integer.valueOf(powerData.getPc1OnceQty()).intValue();
                }
                String period = powerData.getPc1Period();
                String intervalStart = powerData.getPc1IntervalStart();
                String intervalFinish = powerData.getPc1IntervalFinish();
                String startDate = powerData.getPc1StartDate();
                int periodQty = Double.valueOf(powerData.getPc1PeriodQty()).intValue();
                if (onceQty != 0 && inputQty > onceQty) {
                    throw new BusinessException("一次最多领取 " + onceQty + " 数量,请重新输入!");
                } else if (!StringHelper.isEmpty(period, true)) {
                    Date date = getDate2(period, intervalStart, intervalFinish, startDate);
                    int transQtyTotal = 0;
                    if (date != null) {
                        String dateStr = DateHelper.format(date, "yyyy-MM-dd HH:mm:ss");
                        try {
                            UsedRecordDbOper usedRecordDbOper = new UsedRecordDbOper();
                            StockTransactionDbOper stockTransactionDb = new StockTransactionDbOper();
                            transQtyTotal = usedRecordDbOper.getTransQtyCount(cardId, skuId, dateStr);
                            int transQtyTotal1 = stockTransactionDb.getTransQtyCount(StockTransactionData.BILL_TYPE_GET, vendingId, skuId, vendingChnCode, dateStr, cardId);
                            if (cpIdWithScaleList != null) {
                                for (Entry<String, Object> entry : cpIdWithScaleList.entrySet()) {
                                    String innerCpId = (String) entry.getKey();
                                    int innerScale = ConvertHelper.toInt(entry.getValue(), Integer.valueOf(1)).intValue();
                                    transQtyTotal += usedRecordDbOper.getTransQtyCount(cardId, innerCpId, dateStr) * innerScale;
                                    transQtyTotal1 += stockTransactionDb.getTransQtyCount(StockTransactionData.BILL_TYPE_GET, vendingId, innerCpId, vendingChnCode, dateStr, cardId) * innerScale;
                                }
                            }
                            transQtyTotal *= -1;
                            if (transQtyTotal1 < transQtyTotal) {
                                transQtyTotal = transQtyTotal1;
                            }
                        } catch (Throwable e) {
                            ZillionLog.e(getClass().toString(), "产品领料权限检查错误", e);
                            e.printStackTrace();
                            throw new BusinessException("产品领料权限检查错误,产品领用数：" + transQtyTotal);
                        }
                    }
                    int total = transQtyTotal * -1;
                    if (inputQty > periodQty) {
                        if (isPackageChn) {
                            throw new BusinessException("你的领料权限是" + periodQty + "，输入了" + inputQty + "(" + preInputQty + operation + ")" + "，不允许超领！");
                        }
                        throw new BusinessException("你的领料权限是" + periodQty + "，输入了" + inputQty + "，不允许超领！");
                    } else if (total + inputQty > periodQty) {
                        throw new BusinessException("你的领料权限是" + periodQty + "，你已领取" + total + "，不允许超领！");
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            throw new BusinessException("输入的卡号或密码无权限领料，请重新输入！");
        }
    }

    public String getVendingStoreLastPicker(String vendingId, String vendingChnCode, boolean isRfid) {
        String cardId = "";
        CardData cardData = new CardDbOper().getCardById(new StockTransactionDbOper().getVendingStoreLastPicker(vendingId, vendingChnCode));
        if (isRfid) {
            return cardData.getCd1SerialNo();
        }
        return cardData.getCd1Password();
    }
}
