package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.InterfaceData;
import com.mc.vending.db.InterfaceDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConfigDataParse implements DataParseListener {
    private static ConfigDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static ConfigDataParse getInstance() {
        if (instance == null) {
            instance = new ConfigDataParse();
        }
        return instance;
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData.getData() != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<InterfaceData> list = parse(baseData.getData());
                if (!list.isEmpty() || baseData.getDeleteFlag().booleanValue()) {
                    InterfaceDbOper interfaceDbOper = new InterfaceDbOper();
                    if (interfaceDbOper.deleteAll()) {
                        if (interfaceDbOper.batchAddInterface(list)) {
                            Log.i("[interface]:", "接口配置批量增加成功!======" + list.size());
                            new DataParseHelper(this).sendLogVersion(((InterfaceData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[interface]:", "接口配置批量增加失败!");
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

    public void requestConfigData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>接口配置网络请求数据异常!");
        }
    }

    public List<InterfaceData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<InterfaceData> list = new ArrayList();
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
                    InterfaceData data = new InterfaceData();
                    data.setM03Id(jsonObj.getString("ID"));
                    data.setM03M02Id(jsonObj.getString("M03_M02_ID"));
                    data.setM03Name(jsonObj.getString("M03_Name"));
                    data.setM03Target(jsonObj.getString("M03_Target"));
                    data.setM03Optype(jsonObj.getString("M03_Optype"));
                    data.setM03Remark(jsonObj.getString("M03_Remark"));
                    data.setM03ExeInterval(ConvertHelper.toInt(jsonObj.getString("M03_ExeInterval"), Integer.valueOf(0)).intValue());
                    data.setM03StartTime(jsonObj.getString("M03_StartTime"));
                    data.setM03EndTime(jsonObj.getString("M03_EndTime"));
                    data.setM03ExeIndex(ConvertHelper.toInt(jsonObj.getString("M03_ExeIndex"), Integer.valueOf(0)).intValue());
                    data.setM03RowCount(ConvertHelper.toInt(jsonObj.getString("M03_RowCount"), Integer.valueOf(0)).intValue());
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setM03CreateUser(createUser);
                    data.setM03CreateTime(createTime);
                    data.setM03ModifyUser(modifyUser);
                    data.setM03ModifyTime(modifyTime);
                    data.setM03RowVersion(rowVersion);
                    list.add(data);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>接口配置解析数据异常!");
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
