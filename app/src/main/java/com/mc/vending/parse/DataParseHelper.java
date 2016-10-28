package com.mc.vending.parse;

import android.os.Handler;
import android.os.Message;
import com.mc.vending.application.CustomApplication;
import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.tools.HttpHelper;
import com.mc.vending.tools.ZillionLog;
import org.json.JSONArray;
import org.json.JSONObject;

public class DataParseHelper {
    private BaseData baseData = new BaseData();
    private Thread downLoadData;
    public Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (DataParseHelper.this.baseData.getReturnCode().equals("1")) {
                ZillionLog.e(getClass().getName(), new StringBuilder(String.valueOf(DataParseHelper.this.baseData.getRequestURL())).append(":").append(DataParseHelper.this.baseData.getReturnMessage()).toString());
            }
            switch (DataParseHelper.this.baseData.HTTP_STATUS) {
                case 0:
                    if (DataParseHelper.this.listener != null) {
                        DataParseHelper.this.listener.parseJson(DataParseHelper.this.baseData);
                        return;
                    }
                    return;
                case 2:
                    if (DataParseHelper.this.listener != null) {
                        DataParseHelper.this.listener.parseRequestError(DataParseHelper.this.baseData);
                        return;
                    }
                    return;
                case 3:
                    if (DataParseHelper.this.listener != null) {
                        DataParseHelper.this.listener.parseRequestError(DataParseHelper.this.baseData);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private DataParseListener listener;

    public DataParseHelper(DataParseListener pListener) {
        this.listener = pListener;
    }

    private static String getVendCode() {
        return CustomApplication.getContext().getSharedPreferences(Constant.SHARED_VEND_CODE_KEY, 0).getString(Constant.SHARED_VEND_CODE, "");
    }

    public void requestSubmitServer(final String optType, final JSONArray objArray, final String requestURL) {
        this.downLoadData = new Thread(new Runnable() {
            public void run() {
                JSONObject param = new JSONObject();
                try {
                    param.put("optype", optType);
                    param.put("tbname", "Value");
                    JSONArray rows = new JSONArray();
                    for (int i = 0; i < objArray.length(); i++) {
                        JSONObject aa = (JSONObject) objArray.get(i);
                        String vd = DataParseHelper.getVendCode();
                        if (!(vd == null || vd.equals(""))) {
                            aa.put("VD1_CODE", vd);
                        }
                        aa.put("VD1_CurrentVersion", Constant.HEADER_VALUE_CLIENTVER);
                        rows.put(aa);
                    }
                    param.put("rows", rows);
                    DataParseHelper.this.baseData = new BaseData();
                    DataParseHelper.this.baseData.setRequestURL(requestURL);
                    DataParseHelper.this.baseData.setOptType(optType);
                    HttpHelper.requestToParse(DataParseHelper.class, requestURL, param, DataParseHelper.this.baseData);
                } catch (Exception e) {
                    ZillionLog.e(getClass().getName(), "异步网络请求异常!", e);
                }
                DataParseHelper.this.handler.sendEmptyMessage(DataParseHelper.this.baseData.HTTP_STATUS);
            }
        });
        this.downLoadData.start();
    }

    public void requestSubmitServer(final String optType, final JSONObject obj, final String requestURL) {
        this.downLoadData = new Thread(new Runnable() {
            public void run() {
                JSONObject param = new JSONObject();
                try {
                    param.put("optype", optType);
                    param.put("tbname", "Value");
                    if (obj != null) {
                        String vd = DataParseHelper.getVendCode();
                        if (!(vd == null || vd.equals(""))) {
                            obj.put("VD1_CODE", vd);
                        }
                        obj.put("VD1_CurrentVersion", Constant.HEADER_VALUE_CLIENTVER);
                        JSONArray rows = new JSONArray();
                        rows.put(obj);
                        param.put("rows", rows);
                    }
                    DataParseHelper.this.baseData = new BaseData();
                    DataParseHelper.this.baseData.setRequestURL(requestURL);
                    DataParseHelper.this.baseData.setOptType(optType);
                    HttpHelper.requestToParse(DataParseHelper.class, requestURL, param, DataParseHelper.this.baseData);
                } catch (Exception e) {
                    e.printStackTrace();
                    ZillionLog.e(getClass().toString(), "++++++++++++>>>>>异步网络请求异常!", e);
                }
                DataParseHelper.this.handler.sendEmptyMessage(DataParseHelper.this.baseData.HTTP_STATUS);
            }
        });
        this.downLoadData.start();
    }

    public void requestSubmitServer(String optType, JSONArray objArray, String requestURL, Object userObject) {
        final String str = optType;
        final JSONArray jSONArray = objArray;
        final Object obj = userObject;
        final String str2 = requestURL;
        this.downLoadData = new Thread(new Runnable() {
            public void run() {
                JSONObject param = new JSONObject();
                try {
                    param.put("optype", str);
                    param.put("tbname", "Value");
                    JSONArray rows = new JSONArray();
                    for (int i = 0; i < jSONArray.length(); i++) {
                        JSONObject aa = (JSONObject) jSONArray.get(i);
                        String vd = DataParseHelper.getVendCode();
                        if (!(vd == null || vd.equals(""))) {
                            aa.put("VD1_CODE", vd);
                        }
                        aa.put("VD1_CurrentVersion", Constant.HEADER_VALUE_CLIENTVER);
                        rows.put(aa);
                    }
                    param.put("rows", rows);
                    DataParseHelper.this.baseData = new BaseData();
                    DataParseHelper.this.baseData.setUserObject(obj);
                    DataParseHelper.this.baseData.setRequestURL(str2);
                    DataParseHelper.this.baseData.setOptType(str);
                    HttpHelper.requestToParse(DataParseHelper.class, str2, param, DataParseHelper.this.baseData);
                } catch (Exception e) {
                    e.printStackTrace();
                    ZillionLog.e(getClass().toString(), "======>>>>>异步网络请求异常!", e);
                }
                DataParseHelper.this.handler.sendEmptyMessage(DataParseHelper.this.baseData.HTTP_STATUS);
            }
        });
        this.downLoadData.start();
    }

    public void sendLogVersion(String logVersion) {
        JSONObject json = new JSONObject();
        try {
            json.put("LogVersionID", logVersion);
            requestSubmitServer("Update", json, Constant.METHOD_WSID_SYN_LOGVERSION);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>sendLogVersion请求数据异常!");
        }
    }

    public void requestSubmitServer(String optType, JSONObject obj, String requestURL, Object userObject) {
        final String str = optType;
        final JSONObject jSONObject = obj;
        final Object obj2 = userObject;
        final String str2 = requestURL;
        this.downLoadData = new Thread(new Runnable() {
            public void run() {
                JSONObject param = new JSONObject();
                try {
                    param.put("optype", str);
                    param.put("tbname", "Value");
                    if (jSONObject != null) {
                        String vd = DataParseHelper.getVendCode();
                        if (!(vd == null || vd.equals(""))) {
                            jSONObject.put("VD1_CODE", vd);
                        }
                        jSONObject.put("VD1_CurrentVersion", Constant.HEADER_VALUE_CLIENTVER);
                        JSONArray rows = new JSONArray();
                        rows.put(jSONObject);
                        param.put("rows", rows);
                    }
                    DataParseHelper.this.baseData = new BaseData();
                    DataParseHelper.this.baseData.setUserObject(obj2);
                    DataParseHelper.this.baseData.setRequestURL(str2);
                    DataParseHelper.this.baseData.setOptType(str);
                    HttpHelper.requestToParse(DataParseHelper.class, str2, param, DataParseHelper.this.baseData);
                } catch (Exception e) {
                    e.printStackTrace();
                    ZillionLog.e(getClass().toString(), "-------->>>>>异步网络请求异常!", e);
                }
                DataParseHelper.this.handler.sendEmptyMessage(DataParseHelper.this.baseData.HTTP_STATUS);
            }
        });
        this.downLoadData.start();
    }
}
