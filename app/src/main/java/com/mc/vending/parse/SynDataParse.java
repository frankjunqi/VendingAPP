package com.mc.vending.parse;

import com.mc.vending.data.BaseData;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import org.json.JSONObject;

public class SynDataParse implements DataParseListener {
    private static SynDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static SynDataParse getInstance() {
        if (instance == null) {
            instance = new SynDataParse();
        }
        return instance;
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
            }
        } else if (this.listener != null) {
            this.listener.parseRequestFailure(baseData);
        }
    }

    public void requestSynData(String optType, String requestURL) {
        try {
            new DataParseHelper(this).requestSubmitServer(optType, new JSONObject(), requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>同步预先请求数据异常!");
        }
    }

    public void parseRequestError(BaseData baseData) {
        if (this.listener != null) {
            this.listener.parseRequestFailure(baseData);
        }
    }
}
