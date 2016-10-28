package com.mc.vending.parse;

import com.mc.vending.data.BaseData;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import org.json.JSONObject;

public class InitDataParse implements DataParseListener {
    private static InitDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static InitDataParse getInstance() {
        if (instance == null) {
            instance = new InitDataParse();
        }
        return instance;
    }

    public void parseJson(BaseData baseData) {
        if (this.listener != null) {
            this.listener.parseRequestFinised(baseData);
        }
    }

    public void requestInitData(String optType, String requestURL, String vendingCode) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_CODE", vendingCode);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>初始化请求验证网络请求数据异常!");
        }
    }

    public void parseRequestError(BaseData baseData) {
        if (this.listener != null) {
            this.listener.parseRequestFailure(baseData);
        }
    }
}
