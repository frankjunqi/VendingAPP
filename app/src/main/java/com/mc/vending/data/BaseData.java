package com.mc.vending.data;

import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ZillionLog;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BaseData {
    public int HTTP_STATUS;
    private boolean deleteFlag;
    private boolean isSuccess;
    private JSONObject json;
    private String optType;
    private String requestURL;
    public Header[] responseHeader;
    private String returnCode;
    private String returnMessage;
    private int total;
    private Object userObject;

    public String getRequestURL() {
        return this.requestURL;
    }

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }

    public String getOptType() {
        return this.optType;
    }

    public void setOptType(String optType) {
        this.optType = optType;
    }

    public Object getUserObject() {
        return this.userObject;
    }

    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    public String getReturnCode() {
        try {
            if (this.json == null || this.json.isNull("result")) {
                return "";
            }
            this.returnCode = this.json.getString("result");
            return this.returnCode;
        } catch (JSONException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            e.printStackTrace();
        }
        return "";
    }

    public String getReturnMessage() {
        try {
            if (this.json == null || this.json.isNull("message")) {
                return "";
            }
            this.returnMessage = this.json.getString("message");
            return this.returnMessage;
        } catch (JSONException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            e.printStackTrace();
        }
        return "";
    }

    public Boolean isSuccess() {
        if ("0".equals(getReturnCode())) {
            this.isSuccess = true;
        }
        return Boolean.valueOf(this.isSuccess);
    }

    public int getTotal() {
        try {
            if (this.json == null || this.json.isNull("total")) {
                return 0;
            }
            this.total = ConvertHelper.toInt(this.json.getString("total"), Integer.valueOf(0)).intValue();
            return this.total;
        } catch (JSONException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            e.printStackTrace();
        }
        return 0;
    }

    public Boolean getDeleteFlag() {
        try {
            if (this.json == null || this.json.isNull("deleteflag")) {
                return Boolean.valueOf(false);
            }
            if ("0".equals(this.json.getString("deleteflag"))) {
                this.deleteFlag = false;
            } else {
                this.deleteFlag = true;
            }
            return Boolean.valueOf(this.deleteFlag);
        } catch (JSONException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            e.printStackTrace();
        }

        return false;
    }

    public void init(JSONObject json) {
        this.json = json;
    }

    public JSONArray getData() {
        try {
            if (this.json == null || this.json.isNull("data") || this.json.get("data") == null) {
                return null;
            }
            if (this.json.get("data") instanceof JSONArray) {
                return ((JSONObject) this.json.getJSONArray("data").get(0)).getJSONArray("rows");
            }
            return this.json.getJSONObject("data").getJSONArray("rows");
        } catch (JSONException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            return null;
        }
    }

    public JSONArray getWsidData() {
        try {
            if (this.json == null || this.json.isNull("data") || this.json.get("data") == null) {
                return null;
            }
            return ((JSONObject) this.json.getJSONArray("data").get(1)).getJSONArray("rows");
        } catch (JSONException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    public static Object fromJsonObjectToBean(JSONObject json, Class pojo) throws Exception {
        Field[] fields = pojo.getDeclaredFields();
        Object obj = pojo.newInstance();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            try {
                json.get(name);
                if (!(json.get(name) == null || "".equals(json.getString(name)))) {
                    if (field.getType().equals(Long.class) || field.getType().equals(Long.TYPE)) {
                        field.set(obj, Long.valueOf(Long.parseLong(json.getString(name))));
                    } else if (field.getType().equals(String.class)) {
                        field.set(obj, json.getString(name));
                    } else if (field.getType().equals(Double.class) || field.getType().equals(Double.TYPE)) {
                        field.set(obj, Double.valueOf(Double.parseDouble(json.getString(name))));
                    } else if (field.getType().equals(Integer.class) || field.getType().equals(Integer.TYPE)) {
                        field.set(obj, Integer.valueOf(Integer.parseInt(json.getString(name))));
                    } else if (field.getType().equals(Date.class)) {
                        field.set(obj, Long.valueOf(Date.parse(json.getString(name))));
                    }
                }
            } catch (Exception ex) {
                ZillionLog.e("BaseData fromJsonObjectToBean", ex.getMessage(), ex);
            }
        }
        return obj;
    }

    public String toString() {
        Object subList;
        StringBuilder append = new StringBuilder("BaseData [json=").append(this.json).append(", responseHeader=");
        if (this.responseHeader != null) {
            subList = Arrays.asList(this.responseHeader).subList(0, Math.min(this.responseHeader.length, 10));
        } else {
            subList = null;
        }
        return append.append(subList).append(", returnCode=").append(this.returnCode).append(", returnMessage=").append(this.returnMessage).append(", total=").append(this.total).append(", requestURL=").append(this.requestURL).append(", optType=").append(this.optType).append(", deleteFlag=").append(this.deleteFlag).append(", isSuccess=").append(this.isSuccess).append(", HTTP_STATUS=").append(this.HTTP_STATUS).append(", userObject=").append(this.userObject).append("]").toString();
    }
}
