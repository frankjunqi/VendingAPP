package com.mc.vending.parse;

import com.mc.vending.data.BaseData;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import org.json.JSONArray;
import org.json.JSONObject;

public class VendingDataExistParse implements DataParseListener {
    private static VendingDataExistParse instance = null;
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

    public static VendingDataExistParse getInstance() {
        if (instance == null) {
            instance = new VendingDataExistParse();
        }
        return instance;
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            baseData.setUserObject(Boolean.valueOf(parse(baseData.getData())));
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
        } else if (this.listener != null) {
            this.listener.parseRequestFailure(baseData);
        }
    }

    public void requestVendingExistData(String optType, String requestURL, String vendingCode, boolean init) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_CODE", vendingCode);
            json.put("Init", init ? "1" : "0");
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>售货机存在请求数据异常!");
        }
    }

    public boolean parse(JSONArray jsonArray) {
        boolean flag = false;
        if (jsonArray == null) {
            return false;
        }
        int i = 0;
        while (i < jsonArray.length()) {
            try {
                if (jsonArray.getJSONObject(i) != null) {
                    flag = true;
                    break;
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>售货机验证存在异常!");
            }
        }
        return flag;
    }

    public void parseRequestError(BaseData baseData) {
        if (this.listener != null) {
            this.listener.parseRequestFailure(baseData);
        }
    }
}
