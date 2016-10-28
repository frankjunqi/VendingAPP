package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.ProductCardPowerData;
import com.mc.vending.db.ProductCardPowerDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class ProductCardPowerDataParse implements DataParseListener {
    private static ProductCardPowerDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static ProductCardPowerDataParse getInstance() {
        if (instance == null) {
            instance = new ProductCardPowerDataParse();
        }
        return instance;
    }

    public void requestProductCardPowerData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>卡与产品权限网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<ProductCardPowerData> list = parse(baseData.getData());
                if (!list.isEmpty() || baseData.getDeleteFlag().booleanValue()) {
                    ProductCardPowerDbOper cardDbOper = new ProductCardPowerDbOper();
                    List<String> dbPra = new ArrayList();
                    for (ProductCardPowerData data : list) {
                        dbPra.add(data.getPc1CD1_ID() + "," + data.getPc1VD1_ID());
                    }
                    if (cardDbOper.batchDeleteVendingCard(dbPra)) {
                        Log.i("[ProductCardPower]:", "卡与产品权限批量删除成功!");
                        if (cardDbOper.batchAddProductCardPower(list)) {
                            Log.i("[ProductCardPower]:", "卡与产品权限批量增加成功!======" + list.size());
                            new DataParseHelper(this).sendLogVersion(((ProductCardPowerData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[ProductCardPower]:", "卡与产品权限批量增加失败!");
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

    public List<ProductCardPowerData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<ProductCardPowerData> list = new ArrayList();
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
                    ProductCardPowerData data = new ProductCardPowerData();
                    data.setPc1ID(jsonObj.getString("ID"));
                    data.setPc1M02_ID(jsonObj.getString("PC1_M02_ID"));
                    data.setPc1VD1_ID(jsonObj.getString("PC1_VD1_ID"));
                    data.setPc1CU1_ID(jsonObj.getString("PC1_CU1_ID"));
                    data.setPc1CD1_ID(jsonObj.getString("PC1_CD1_ID"));
                    data.setPc1VP1_ID(jsonObj.getString("PC1_PD1_ID"));
                    data.setPc1Power(jsonObj.getString("PC1_Power"));
                    data.setPc1OnceQty(jsonObj.getString("PC1_OnceQty"));
                    data.setPc1Period(jsonObj.getString("PC1_Period"));
                    data.setPc1IntervalStart(jsonObj.getString("PC1_IntervalStart"));
                    data.setPc1IntervalFinish(jsonObj.getString("PC1_IntervalFinish"));
                    data.setPc1StartDate(jsonObj.getString("PC1_StartDate"));
                    data.setPc1PeriodQty(jsonObj.getString("PC1_PeriodQty"));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setCreateUser(createUser);
                    data.setCreateTime(createTime);
                    data.setModifyUser(modifyUser);
                    data.setModifyTime(modifyTime);
                    data.setRowVersion(rowVersion);
                    list.add(data);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>卡与产品权限解析网络数据异常!");
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
