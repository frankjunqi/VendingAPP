package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.VendingChnStockData;
import com.mc.vending.db.VendingChnStockDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class VendingChnStockDataParse implements DataParseListener {
    private static VendingChnStockDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static VendingChnStockDataParse getInstance() {
        if (instance == null) {
            instance = new VendingChnStockDataParse();
        }
        return instance;
    }

    public void requestVendingChnStockData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>售货机货道库存网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<VendingChnStockData> list = parse(baseData.getData());
                if (!list.isEmpty()) {
                    VendingChnStockDbOper vendingChnStockDbOper = new VendingChnStockDbOper();
                    if (vendingChnStockDbOper.deleteAll()) {
                        if (vendingChnStockDbOper.batchAddVendingChnStock(list)) {
                            Log.i("[vendingChnStock]:", "======>>>>>售货机货道库存批量增加成功!" + list.size());
                            if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")).intValue() >= Constant.VERSION_STOCK_SYNC) {
                                new DataParseHelper(this).sendLogVersion(((VendingChnStockData) list.get(0)).getLogVersion());
                            }
                        } else {
                            ZillionLog.e("[vendingChnStock]:", "==========>>>>>售货机货道库存批量增加失败!");
                        }
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

    public List<VendingChnStockData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<VendingChnStockData> list = new ArrayList();
        int i = 0;
        while (i < jsonArray.length()) {
            try {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                if (jsonObj != null) {
                    String createUser = jsonObj.getString("CreateUser");
                    String createTime = jsonObj.getString("CreateTime");
                    String modifyUser = jsonObj.getString("ModifyUser");
                    String modifyTime = jsonObj.getString("ModifyTime");
                    String rowVersion = new StringBuilder(String.valueOf(new Date().getTime())).toString();
                    VendingChnStockData data = new VendingChnStockData();
                    data.setVs1Id(jsonObj.getString("ID"));
                    data.setVs1M02Id(jsonObj.getString("VS1_M02_ID"));
                    data.setVs1Vd1Id(jsonObj.getString("VS1_VD1_ID"));
                    data.setVs1Vc1Code(jsonObj.getString("VS1_VC1_CODE"));
                    data.setVs1Pd1Id(jsonObj.getString("VS1_PD1_ID"));
                    data.setVs1Quantity(ConvertHelper.toInt(jsonObj.getString("VS1_Quantity"), Integer.valueOf(0)));
                    if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")).intValue() >= Constant.VERSION_STOCK_SYNC) {
                        data.setLogVersion(jsonObj.getString("LogVision"));
                    }
                    data.setVs1CreateUser(createUser);
                    data.setVs1CreateTime(createTime);
                    data.setVs1ModifyUser(modifyUser);
                    data.setVs1ModifyTime(modifyTime);
                    data.setVs1RowVersion(rowVersion);
                    list.add(data);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>售货机货道库存解析数据异常!");
                return list;
            }
        }
        return list;
    }

    public void parseRequestError(BaseData baseData) {
        if (this.listener != null) {
            this.listener.parseRequestFailure(baseData);
        }
    }
}
