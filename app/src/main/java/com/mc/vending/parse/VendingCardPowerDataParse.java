package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.VendingCardPowerData;
import com.mc.vending.db.VendingCardPowerDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class VendingCardPowerDataParse implements DataParseListener {
    private static VendingCardPowerDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static VendingCardPowerDataParse getInstance() {
        if (instance == null) {
            instance = new VendingCardPowerDataParse();
        }
        return instance;
    }

    public void requestVendingCardPowerData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>售货机卡/密码权限网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<VendingCardPowerData> list = parse(baseData.getData());
                if (!list.isEmpty() || baseData.getDeleteFlag().booleanValue()) {
                    VendingCardPowerDbOper vendingCardPowerDbOper = new VendingCardPowerDbOper();
                    if (vendingCardPowerDbOper.deleteAll()) {
                        if (vendingCardPowerDbOper.batchAddVendingCardPower(list)) {
                            Log.i("[vendingCardPower]:", "======>>>>>售货机卡/密码权限批量增加成功!" + list.size());
                            new DataParseHelper(this).sendLogVersion(((VendingCardPowerData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[vendingCardPower]:", "==========>>>>>售货机卡/密码权限批量增加失败!");
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

    public List<VendingCardPowerData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<VendingCardPowerData> list = new ArrayList();
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
                    VendingCardPowerData data = new VendingCardPowerData();
                    data.setVc2Id(jsonObj.getString("ID"));
                    data.setVc2M02Id(jsonObj.getString("VC2_M02_ID"));
                    data.setVc2Cu1Id(jsonObj.getString("VC2_CU1_ID"));
                    data.setVc2Vd1Id(jsonObj.getString("VC2_VD1_ID"));
                    data.setVc2Cd1Id(jsonObj.getString("VC2_CD1_ID"));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setVc2CreateUser(createUser);
                    data.setVc2CreateTime(createTime);
                    data.setVc2ModifyUser(modifyUser);
                    data.setVc2ModifyTime(modifyTime);
                    data.setVc2RowVersion(rowVersion);
                    list.add(data);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>售货机卡/密码权限解析数据异常!");
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
