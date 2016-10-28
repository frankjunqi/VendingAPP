package com.mc.vending.parse;

import com.mc.vending.application.CustomApplication;
import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.InterfaceData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.VendingData;
import com.mc.vending.db.InterfaceDbOper;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.db.VendingDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VendingDataParse implements DataParseListener, DataParseRequestListener {
    private static VendingDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        this.listener = null;
    }

    public static VendingDataParse getInstance() {
        if (instance == null) {
            instance = new VendingDataParse();
        }
        return instance;
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                VendingData vendingData = parse(baseData.getData());
                if (vendingData != null || baseData.getDeleteFlag().booleanValue()) {
                    VendingDbOper vendingDbOper = new VendingDbOper();
                    VendingData vending = vendingDbOper.getVending();
                    if (vending != null) {
                        if (vending.getVd1Id().equals(vendingData.getVd1Id())) {
                            if (vendingDbOper.updateVending(vendingData)) {
                                ZillionLog.i("======>>>>>售货机更新成功!");
                                syncByWsid(parseWsid(baseData.getWsidData()), vendingData.getVd1Id());
                            } else {
                                ZillionLog.e("==========>>>>>售货机更新失败!");
                            }
                        }
                    } else if (vendingDbOper.addVending(vendingData)) {
                        ZillionLog.i("======>>>>>售货机增加成功!");
                        syncByWsid(parseWsid(baseData.getWsidData()), vendingData.getVd1Id());
                    } else {
                        ZillionLog.e("==========>>>>>售货机增加失败!");
                    }
                    if (this.listener != null) {
                        this.listener.parseRequestFinised(baseData);
                    }
                } else if (this.listener != null) {
                    this.listener.parseRequestFinised(baseData);
                }
            } else if (this.listener != null) {
                this.listener.parseRequestFailure(baseData);
            }
        } else if (this.listener != null) {
            this.listener.parseRequestFailure(baseData);
        }
    }

    private void syncByWsid(List<String> wsidList, String vendingId) {
        Map<String, InterfaceData> configMap = new HashMap();
        for (InterfaceData config : new InterfaceDbOper().findAll()) {
            configMap.put(new StringBuilder(String.valueOf(config.getM03Target().trim())).append("_").append(config.getM03Optype().trim()).toString(), config);
        }
        if (wsidList != null) {
            ZillionLog.i(getClass().getName(), wsidList);
            for (String wsid : wsidList) {
                if (wsid.equals(Constant.METHOD_WSID_VENDINGCHN)) {
                    new VendingChnDataParse().requestVendingChnData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VENDINGCHN, vendingId);
                } else if (wsid.equals(Constant.METHOD_WSID_VENDINGPICTURE)) {
                    new VendingPictureDataParse().requestVendingPictureData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VENDINGPICTURE, vendingId);
                } else if (wsid.equals(Constant.METHOD_WSID_VENDINGPROLINK)) {
                    new VendingProLinkDataParse().requestVendingProLinkData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VENDINGPROLINK, vendingId);
                } else if (wsid.equals(Constant.METHOD_WSID_PRODUCT)) {
                    InterfaceData productConfig = (InterfaceData) configMap.get("c81e6175-a15c-47b8-a3e2-a6c2fbf9d98b_GetData");
                    new ProductDataParse().requestProductData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_PRODUCT, vendingId, productConfig != null ? productConfig.getM03RowCount() : 10);
                } else if (wsid.equals(Constant.METHOD_WSID_PRODUCTPICTURE)) {
                    InterfaceData productPictureConfig = (InterfaceData) configMap.get("0cec0063-a032-4a37-aa45-889d554023d8_GetData");
                    new ProductPictureDataParse().requestProductPictureData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_PRODUCTPICTURE, vendingId, productPictureConfig != null ? productPictureConfig.getM03RowCount() : 10);
                } else if (wsid.equals(Constant.METHOD_WSID_SUPPLIER)) {
                    InterfaceData supplierConfig = (InterfaceData) configMap.get("66b91d60-808b-4109-9dc2-2b9f08349bee_GetData");
                    new SupplierDataParse().requestSupplierData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_SUPPLIER, vendingId, supplierConfig != null ? supplierConfig.getM03RowCount() : 10);
                } else if (wsid.equals(Constant.METHOD_WSID_STATION)) {
                    InterfaceData stationConfig = (InterfaceData) configMap.get("72be83fe-24ca-4bf5-9851-248c3391d67a_GetData");
                    new StationDataParse().requestStationData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_STATION, vendingId, stationConfig != null ? stationConfig.getM03RowCount() : 10);
                } else if (wsid.equals(Constant.METHOD_WSID_VENDINGCARDPOWER)) {
                    new VendingCardPowerDataParse().requestVendingCardPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VENDINGCARDPOWER, vendingId);
                } else if (wsid.equals(Constant.METHOD_WSID_PRODUCTMATERIAKPOWER)) {
                    new ProductMaterialPowerDataParse().requestProductMaterialPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_PRODUCTMATERIAKPOWER, vendingId);
                } else if (wsid.equals(Constant.METHOD_WSID_CARD)) {
                    new CardDataParse().requestCardData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CARD, vendingId);
                } else if (wsid.equals(Constant.METHOD_WSID_CUSEMPCARDPOWER)) {
                    new CusEmpCardPowerDataParse().requestCusEmpCardPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CUSEMPCARDPOWER, vendingId);
                } else if (wsid.equals(Constant.METHOD_WSID_CUSTOMEREMPLINK)) {
                    new CustomerEmpLinkDataParse().requestCustomerEmpLinkData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CUSTOMEREMPLINK, vendingId);
                } else if (wsid.equals(Constant.METHOD_WSID_CUSTOMER)) {
                    new CustomerDataParse().requestCustomerData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CUSTOMER, vendingId);
                } else if (wsid.equals(Constant.METHOD_WSID_PRODUCTGROUP)) {
                    new ProductGroupDataParse().requestProductGroupData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_PRODUCTGROUP, vendingId);
                } else if (wsid.equals(Constant.METHOD_WSID_PRODUCTGROUPPOWER)) {
                    new ProductGroupPowerDataParse().requestProductGroupPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_PRODUCTGROUPPOWER, vendingId);
                } else if (wsid.equals("7f342da0-05be-4f3a-96c3-28072ec31e7a")) {
                    InterfaceData replenishmentConfig = (InterfaceData) configMap.get("7f342da0-05be-4f3a-96c3-28072ec31e7a_GetData");
                    new ReplenishmentDataParse().requestReplenishmentData(Constant.HTTP_OPERATE_TYPE_GETDATA, "7f342da0-05be-4f3a-96c3-28072ec31e7a", vendingId, replenishmentConfig != null ? replenishmentConfig.getM03RowCount() : 10);
                } else if (wsid.equals(Constant.METHOD_WSID_PASSWORD)) {
                    new VendingPasswordDataParse().requestVendingPasswordData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_PASSWORD, vendingId);
                } else if (wsid.equals(Constant.METHOD_WSID_CONFIG)) {
                    new ConfigDataParse().requestConfigData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CONFIG, vendingId);
                } else if (wsid.equals(Constant.METHOD_WSID_RETURNS_FORWARD)) {
                    new RetreatHeadDataParse().requestReturnForward(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_RETURNS_FORWARD, vendingId, 10);
                } else if (wsid.equals(Constant.METHOD_WSID_PRODUCTCARDPOWER)) {
                    new ProductCardPowerDataParse().requestProductCardPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_PRODUCTCARDPOWER, vendingId);
                } else if (wsid.equals(Constant.METHOD_WSID_USEDRECORD)) {
                    new UsedRecordDownloadDataParse().requestUsedRecordData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_USEDRECORD, vendingId);
                } else if (wsid.equals(Constant.METHOD_WSID_SYN_STOCK)) {
                    if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")).intValue() > 218) {
                        synchronousStock(vendingId);
                    }
                } else if (wsid.equals(Constant.METHOD_WSID_CONVERSION)) {
                    new ConvertionDataParse().requestConvertionData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CONVERSION, vendingId);
                }
            }
        }
    }

    private void synchronousStock(String vendingId) {
        ZillionLog.i("上传交易记录--同步库存：" + StockTransactionDataParse.getInstance().isSync);
        if (StockTransactionDataParse.getInstance().isSync) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            synchronousStock(vendingId);
            return;
        }
        List<StockTransactionData> datas = new StockTransactionDbOper().findStockTransactionDataToUpload();
        if (datas == null || datas.size() == 0) {
            new VendingChnStockDataParse().requestVendingChnStockData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_SYN_STOCK, vendingId);
            return;
        }
        StockTransactionDataParse stockTransactionDataParse = StockTransactionDataParse.getInstance();
        stockTransactionDataParse.setListener(this);
        stockTransactionDataParse.requestStockTransactionData(Constant.HTTP_OPERATE_TYPE_INSERT, Constant.METHOD_WSID_STOCKTRANSACTION, vendingId, datas);
    }

    public void requestVendingData(String optType, String requestURL, String vendingCode, boolean init) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_CODE", vendingCode);
            json.put("Init", init ? "1" : "0");
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            ZillionLog.e(getClass().getName(), "======>>>>>售货机网络请求数据异常!" + e.getMessage(), e);
        }
    }

    private List<String> parseWsid(JSONArray jsonArray) {
        List<String> wsidList = new ArrayList();
        if (jsonArray == null) {
            return null;
        }
        int i = 0;
        while (i < jsonArray.length()) {
            try {
                wsidList.add(jsonArray.getJSONObject(i).getString("WSID"));
                i++;
            } catch (JSONException e) {
                ZillionLog.e(getClass().getName(), "======>>>>>售货机WSID解析数据异常!" + e.getMessage(), e);
                return wsidList;
            }
        }
        return wsidList;
    }

    public VendingData parse(JSONArray jsonArray) {
        Exception e;
        VendingData data = null;
        if (jsonArray == null) {
            return null;
        }
        int i = 0;
        while (i < jsonArray.length()) {
            try {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                if (jsonObj == null) {
                    i++;
                } else {
                    String createUser = jsonObj.getString("CreateUser");
                    String createTime = jsonObj.getString("CreateTime");
                    String modifyUser = jsonObj.getString("ModifyUser");
                    String modifyTime = jsonObj.getString("ModifyTime");
                    String rowVersion = new StringBuilder(String.valueOf(new Date().getTime())).toString();
                    VendingData data2 = new VendingData();
                    try {
                        data2.setVd1Id(jsonObj.getString("ID"));
                        data2.setVd1M02Id(jsonObj.getString("VD1_M02_ID"));
                        data2.setVd1Code(jsonObj.getString("VD1_CODE"));
                        data2.setVd1Manufacturer(jsonObj.getString("VD1_manufacturer"));
                        data2.setVd1Vm1Id(jsonObj.getString("VD1_VM1_ID"));
                        data2.setVd1LastVersion(jsonObj.getString("VD1_LastVersion"));
                        data2.setVd1LwhSize(jsonObj.getString("VD1_LWHSize"));
                        data2.setVd1Color(jsonObj.getString("VD1_Color"));
                        data2.setVd1InstallAddress(jsonObj.getString("VD1_InstallAddress"));
                        data2.setVd1Coordinate(jsonObj.getString("VD1_Coordinate"));
                        data2.setVd1St1Id(jsonObj.getString("VD1_ST1_ID"));
                        data2.setVd1EmergencyRel(jsonObj.getString("VD1_EmergencyRel"));
                        data2.setVd1EmergencyRelPhone(jsonObj.getString("VD1_EmergencyRelPhone"));
                        data2.setVd1OnlineStatus(jsonObj.getString("VD1_OnlineStatus"));
                        data2.setVd1Status(jsonObj.getString("VD1_Status"));
                        try {
                            data2.setVd1CardType(jsonObj.getString("VD1_CardType"));
                        } catch (Exception e2) {
                            data2.setVd1CardType("1");
                        }
                        if (!jsonObj.isNull("VD1_DubugStatus")) {
                            saveVendDubugStatus(jsonObj.getString("VD1_DubugStatus"));
                        }
                        data2.setLogVersion(jsonObj.getString("LogVision"));
                        data2.setVd1CreateUser(createUser);
                        data2.setVd1CreateTime(createTime);
                        data2.setVd1ModifyUser(modifyUser);
                        data2.setVd1ModifyTime(modifyTime);
                        data2.setVd1RowVersion(rowVersion);
                        data = data2;
                    } catch (Exception e3) {
                        e = e3;
                        data = data2;
                        ZillionLog.e(getClass().getName(), "======>>>>>售货机解析数据异常!" + e.getMessage(), e);
                        return data;
                    }
                    return data;
                }
            } catch (Exception e4) {
                e = e4;
            }
        }
        return data;
    }

    private void saveVendDubugStatus(String key) {
        if (key == null || key.equals("")) {
            key = "0";
        }
        CustomApplication.getContext().getSharedPreferences(Constant.SHARED_VEND_CODE_KEY, 0).edit().putString(Constant.SHARED_VEND_DEBUG_STATUS, key).commit();
    }

    public void parseRequestError(BaseData baseData) {
        if (this.listener != null) {
            this.listener.parseRequestFailure(baseData);
        }
    }

    public void parseRequestFinised(BaseData baseData) {
        if (Constant.METHOD_WSID_STOCKTRANSACTION.equals(baseData.getRequestURL())) {
            StockTransactionDataParse.getInstance().isSync = false;
            List<StockTransactionData> datas = (List) baseData.getUserObject();
            if (datas != null && datas.size() > 0) {
                synchronousStock(((StockTransactionData) datas.get(0)).getTs1Vd1Id());
            }
        }
    }

    public void parseRequestFailure(BaseData baseData) {
    }
}
