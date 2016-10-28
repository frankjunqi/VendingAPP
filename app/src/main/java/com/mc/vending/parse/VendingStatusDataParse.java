package com.mc.vending.parse;

import com.mc.vending.data.BaseData;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.tools.ZillionLog;
import org.json.JSONObject;

public class VendingStatusDataParse implements DataParseListener {
    private static VendingStatusDataParse instance = null;

    public static VendingStatusDataParse getInstance() {
        if (instance == null) {
            instance = new VendingStatusDataParse();
        }
        return instance;
    }

    public void parseJson(BaseData baseData) {
        if (!baseData.isSuccess().booleanValue()) {
        }
    }

    public void requestVendingData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>售货机联机状态网络请求数据异常!");
        }
    }

    public void parseRequestError(BaseData baseData) {
    }
}
