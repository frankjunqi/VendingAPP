package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.CardData;
import com.mc.vending.db.CardDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class CardDataParse implements DataParseListener {
    private static CardDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static CardDataParse getInstance() {
        if (instance == null) {
            instance = new CardDataParse();
        }
        return instance;
    }

    public void requestCardData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>卡/密码网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<CardData> list = parse(baseData.getData());
                if (!list.isEmpty() || baseData.getDeleteFlag().booleanValue()) {
                    CardDbOper cardDbOper = new CardDbOper();
                    if (cardDbOper.deleteAll()) {
                        if (cardDbOper.batchAddCard(list)) {
                            Log.i("[card]:", "卡/密码批量增加成功!======" + list.size());
                            new DataParseHelper(this).sendLogVersion(((CardData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[card]:", "卡/密码批量增加失败!");
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

    public List<CardData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<CardData> list = new ArrayList();
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
                    CardData data = new CardData();
                    data.setCd1Id(jsonObj.getString("ID"));
                    data.setCd1M02Id(jsonObj.getString("CD1_M02_ID"));
                    data.setCd1SerialNo(jsonObj.getString("CD1_SerialNo"));
                    data.setCd1Code(jsonObj.getString("CD1_CODE"));
                    data.setCd1Type(jsonObj.getString("CD1_Type"));
                    data.setCd1Password(jsonObj.getString("CD1_Password"));
                    data.setCd1Purpose(jsonObj.getString("CD1_Purpose"));
                    data.setCd1Status(jsonObj.getString("CD1_Status"));
                    data.setCd1CustomerStatus(jsonObj.getString("CD1_CustomerStatus"));
                    data.setCd1ProductPower(jsonObj.getString("CD1_ProductPower"));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setCd1CreateUser(createUser);
                    data.setCd1CreateTime(createTime);
                    data.setCd1ModifyUser(modifyUser);
                    data.setCd1ModifyTime(modifyTime);
                    data.setCd1RowVersion(rowVersion);
                    list.add(data);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>卡/密码解析网络数据异常!");
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
