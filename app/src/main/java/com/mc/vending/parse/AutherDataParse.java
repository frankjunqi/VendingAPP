package com.mc.vending.parse;

import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AutherDataParse implements DataParseListener {
    private static AutherDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static AutherDataParse getInstance() {
        if (instance == null) {
            instance = new AutherDataParse();
        }
        return instance;
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                JSONArray jsonArray = baseData.getData();
                if (jsonArray == null) {
                    try {
                        if (this.listener != null) {
                            this.listener.parseRequestFailure(baseData);
                        }
                    } catch (Exception e) {
                        ZillionLog.e(getClass().toString(), "======>>>>>接口认证解析数据异常!", e);
                        e.printStackTrace();
                    }
                } else {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = null;
                        try {
                            jsonObj = jsonArray.getJSONObject(i);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (jsonObj != null) {
                            try {
                                baseData.setUserObject(jsonObj.getString("Key"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (this.listener != null) {
                        this.listener.parseRequestFinised(baseData);
                    }
                }
            } else if (this.listener != null) {
                this.listener.parseRequestFailure(baseData);
            }
        } else if (this.listener != null) {
            this.listener.parseRequestFailure(baseData);
        }
    }

    public void requestAutherData(String optType, String requestURL, String deviceId) {
        JSONObject json = new JSONObject();
        try {
            json.put(Constant.BODY_KEY_UDID, deviceId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            ZillionLog.e(getClass().toString(), "======>>>>>接口认证网络请求数据异常!", e);
            e.printStackTrace();
        }
    }

    public void parseRequestError(BaseData baseData) {
        if (this.listener != null) {
            this.listener.parseRequestFailure(baseData);
        }
    }
}
