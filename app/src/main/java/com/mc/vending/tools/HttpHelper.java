package com.mc.vending.tools;

import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.parse.StockTransactionDataParse;
import com.mc.vending.tools.utils.DES;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpHelper {
    public static final int HTTP_STATUS_ERROR = 1;
    public static final int HTTP_STATUS_PRASE_ERROR = 3;
    public static final int HTTP_STATUS_SERVER_ERROR = 2;
    public static final int HTTP_STATUS_SUCCESS = 0;
    public static Map<String, String> headerMap;

    public static Map<String, String> getHeaderMap() {
        if (headerMap == null) {
            headerMap = new HashMap();
            intHeader();
        }
        return headerMap;
    }

    public static void intHeader() {
        headerMap.put(Constant.HEADER_KEY_CONTENT_TYPE, Constant.HEADER_VALUE_CONTENT_TYPE);
        headerMap.put(Constant.HEADER_KEY_CLIENTVER, Constant.HEADER_VALUE_CLIENTVER);
    }

    public static void setHeader(String key, String value) {
        if (!"".equals(key)) {
            headerMap.put(key, value);
        }
    }

    public static BaseData requestToParse(Class className, String method, JSONObject param, BaseData baseData) throws JSONException {
        baseData.HTTP_STATUS = 1;
        ZillionLog.i("request", Constant.WSIDNAMEMAP.get(method));
        List<BasicNameValuePair> postData = new ArrayList();
        JSONObject json = new JSONObject();
        json.put(Constant.BODY_KEY_METHOD, method);
        json.put(Constant.BODY_KEY_UDID, Constant.BODY_VALUE_UDID);
        json.put(Constant.BODY_KEY_APP, Constant.BODY_VALUE_APP);
        json.put(Constant.BODY_KEY_USER, "");
        json.put("pwd", DES.getEncrypt());
        if (param != null) {
            JSONArray array = new JSONArray();
            array.put(param);
            json.put("data", array);
        }
        postData.add(new BasicNameValuePair("data", json.toString()));
        System.setProperty("http.keepAlive", "false");
        if (Constant.SERVER_URL == null || Constant.SERVER_URL.equals("")) {
            ZillionLog.i("SERVER_URL NULL", Constant.SERVER_URL);
            Constant.SERVER_URL = Tools.getConfigUrl();
        }
        HttpPost httpPost = new HttpPost(Constant.SERVER_URL);
        httpPost.setHeader(Constant.HEADER_KEY_CONTENT_TYPE, (String) getHeaderMap().get(Constant.HEADER_KEY_CONTENT_TYPE));
        httpPost.setHeader(Constant.HEADER_KEY_CLIENTVER, (String) getHeaderMap().get(Constant.HEADER_KEY_CLIENTVER));
        BasicHttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
        HttpConnectionParams.setSoTimeout(httpParams, 30000);
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        HttpResponse response = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(postData, "UTF-8"));
            response = httpClient.execute(httpPost);
            if (response.getStatusLine().getStatusCode() == 200) {
                baseData.HTTP_STATUS = 0;
            } else {
                baseData.HTTP_STATUS = 2;
                httpPost.abort();
            }
        } catch (Exception e) {
            if (baseData.getRequestURL().equals(Constant.METHOD_WSID_STOCKTRANSACTION)) {
                StockTransactionDataParse.getInstance().isSync = false;
            }
            ZillionLog.i("SERVER_URL1", Constant.SERVER_URL);
            ZillionLog.e("HttpHelp1", e.getMessage(), e);
            baseData.HTTP_STATUS = 2;
        }
        if (baseData.HTTP_STATUS != 2) {
            baseData.responseHeader = response.getAllHeaders();
            try {
                baseData.init(new JSONObject(EntityUtils.toString(response.getEntity())));
                baseData.HTTP_STATUS = 0;
            } catch (Exception e2) {
                if (baseData.getRequestURL().equals(Constant.METHOD_WSID_STOCKTRANSACTION)) {
                    StockTransactionDataParse.getInstance().isSync = false;
                }
                ZillionLog.i("SERVER_URL2", Constant.SERVER_URL);
                ZillionLog.e("HttpHelp2", e2.getMessage(), e2);
                baseData.HTTP_STATUS = 3;
            }
        }
        return baseData;
    }
}
