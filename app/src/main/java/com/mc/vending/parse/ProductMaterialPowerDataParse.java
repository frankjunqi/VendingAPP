package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.ProductMaterialPowerData;
import com.mc.vending.db.ProductMaterialPowerDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class ProductMaterialPowerDataParse implements DataParseListener {
    private static ProductMaterialPowerDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static ProductMaterialPowerDataParse getInstance() {
        if (instance == null) {
            instance = new ProductMaterialPowerDataParse();
        }
        return instance;
    }

    public void requestProductMaterialPowerData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>产品领料权限数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<ProductMaterialPowerData> list = parse(baseData.getData());
                if (!list.isEmpty() || baseData.getDeleteFlag().booleanValue()) {
                    ProductMaterialPowerDbOper productMaterialPowerDbOper = new ProductMaterialPowerDbOper();
                    if (productMaterialPowerDbOper.deleteAll()) {
                        if (productMaterialPowerDbOper.batchAddProductMaterialPower(list)) {
                            Log.i("[productMaterialPower]:", "======>>>>>产品领料权限批量增加成功!====" + list.size());
                            new DataParseHelper(this).sendLogVersion(((ProductMaterialPowerData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[productMaterialPower]:", "==========>>>>>产品领料权限批量增加失败!");
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

    public List<ProductMaterialPowerData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<ProductMaterialPowerData> list = new ArrayList();
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
                    ProductMaterialPowerData data = new ProductMaterialPowerData();
                    data.setPm1Id(jsonObj.getString("ID"));
                    data.setPm1M02Id(jsonObj.getString("PM1_M02_ID"));
                    data.setPm1Cu1Id(jsonObj.getString("PM1_CU1_ID"));
                    data.setPm1Vc2Id(jsonObj.getString("PM1_VC2_ID"));
                    data.setPm1Vp1Id(jsonObj.getString("PM1_VP1_ID"));
                    data.setPm1Power(jsonObj.getString("PM1_Power"));
                    data.setPm1OnceQty(ConvertHelper.toInt(jsonObj.getString("PM1_OnceQty"), Integer.valueOf(0)));
                    data.setPm1Period(jsonObj.getString("PM1_Period"));
                    data.setPm1IntervalStart(jsonObj.getString("PM1_IntervalStart"));
                    data.setPm1IntervalFinish(jsonObj.getString("PM1_IntervalFinish"));
                    data.setPm1StartDate(jsonObj.getString("PM1_StartDate"));
                    data.setPm1PeriodQty(ConvertHelper.toInt(jsonObj.getString("PM1_PeriodQty"), Integer.valueOf(0)));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setPm1CreateUser(createUser);
                    data.setPm1CreateTime(createTime);
                    data.setPm1ModifyUser(modifyUser);
                    data.setPm1ModifyTime(modifyTime);
                    data.setPm1RowVersion(rowVersion);
                    list.add(data);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>产品领料权限解析数据异常!");
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
