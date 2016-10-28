package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.ProductGroupPowerData;
import com.mc.vending.db.ProductGroupPowerDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class ProductGroupPowerDataParse implements DataParseListener {
    private static ProductGroupPowerDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static ProductGroupPowerDataParse getInstance() {
        if (instance == null) {
            instance = new ProductGroupPowerDataParse();
        }
        return instance;
    }

    public void requestProductGroupPowerData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>产品组合权限网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<ProductGroupPowerData> list = parse(baseData.getData());
                if (!list.isEmpty() || baseData.getDeleteFlag().booleanValue()) {
                    ProductGroupPowerDbOper productGroupPowerDbOper = new ProductGroupPowerDbOper();
                    if (productGroupPowerDbOper.deleteAll()) {
                        if (productGroupPowerDbOper.batchAddProductGroupPower(list)) {
                            Log.i("[productGroupPower]:", "==========>>>>>产品组合权限批量增加成功!==========" + list.size());
                            new DataParseHelper(this).sendLogVersion(((ProductGroupPowerData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[productGroupPower]:", "==========>>>>>产品组合权限批量增加失败!");
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

    public List<ProductGroupPowerData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<ProductGroupPowerData> list = new ArrayList();
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
                    ProductGroupPowerData data = new ProductGroupPowerData();
                    data.setPp1Id(jsonObj.getString("ID"));
                    data.setPp1M02Id(jsonObj.getString("PP1_M02_ID"));
                    data.setPp1Cu1Id(jsonObj.getString("PP1_CU1_ID"));
                    data.setPp1Pg1Id(jsonObj.getString("PP1_PG1_ID"));
                    data.setPp1Cd1Id(jsonObj.getString("PP1_CD1_ID"));
                    data.setPp1Power(jsonObj.getString("PP1_Power"));
                    data.setPp1Period(jsonObj.getString("PP1_Period"));
                    data.setPp1IntervalStart(jsonObj.getString("PP1_IntervalStart"));
                    data.setPp1IntervalFinish(jsonObj.getString("PP1_IntervalFinish"));
                    data.setPp1StartDate(jsonObj.getString("PP1_StartDate"));
                    data.setPp1PeriodNum(ConvertHelper.toInt(jsonObj.getString("PP1_PeriodNum"), Integer.valueOf(0)));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setPp1CreateUser(createUser);
                    data.setPp1CreateTime(createTime);
                    data.setPp1ModifyUser(modifyUser);
                    data.setPp1ModifyTime(modifyTime);
                    data.setPp1RowVersion(rowVersion);
                    list.add(data);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>产品组合权限解析数据异常!");
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
