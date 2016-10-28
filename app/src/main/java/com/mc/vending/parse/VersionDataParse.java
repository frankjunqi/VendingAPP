package com.mc.vending.parse;

import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.VersionData;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VersionDataParse implements DataParseListener {
    private static VersionDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static VersionDataParse getInstance() {
        if (instance == null) {
            instance = new VersionDataParse();
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
                        e.printStackTrace();
                        ZillionLog.e(getClass().toString(), "======>>>>>版本信息接口解析异常!");
                    }
                } else {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = null;
                        try {
                            jsonObj = jsonArray.getJSONObject(i);

                            if (jsonObj != null) {
                                VersionData data = new VersionData();
                                data.setVersion(jsonObj.getString("AP1_Version"));
                                data.setDownloadURL(jsonObj.getString("AP1_FilePath"));
                                baseData.setUserObject(data);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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

    public void requestVersionData(String optType, String requestURL) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_CurrentVersion", Constant.HEADER_VALUE_CLIENTVER);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>版本信息接口请求数据异常!");
        }
    }

    public void parseRequestError(BaseData baseData) {
        if (this.listener != null) {
            this.listener.parseRequestFailure(baseData);
        }
    }
}
