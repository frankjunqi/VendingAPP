package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.ConversionData;
import com.mc.vending.db.ConversionDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class ConvertionDataParse implements DataParseListener {
    private static ConvertionDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static ConvertionDataParse getInstance() {
        if (instance == null) {
            instance = new ConvertionDataParse();
        }
        return instance;
    }

    public void requestConvertionData(String optType, String requestURL, String cn1Id) {
        Log.i(getClass().getName(), cn1Id);
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", cn1Id);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(getClass().toString(), "======>>>>>单位转换关系表网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        Log.i(getClass().getName(), baseData.toString());
        if (baseData.isSuccess().booleanValue()) {
            List<ConversionData> list = parse(baseData.getData());
            if (!list.isEmpty() || baseData.getDeleteFlag().booleanValue()) {
                ConversionDbOper conversion = new ConversionDbOper();
                List<String> dbPra = new ArrayList();
                for (ConversionData data : list) {
                    dbPra.add(data.getCn1Upid() + "," + data.getCn1Cpid());
                }
                if (conversion.batchDeleteConversion(dbPra)) {
                    Log.i("[Conversion]:", "转换关系批量删除成功!======" + dbPra);
                    if (conversion.batchAddConversionData(list)) {
                        Log.i("[Conversion]:", "转换关系批量增加成功!======" + list.size());
                        new DataParseHelper(this).sendLogVersion(((ConversionData) list.get(0)).getLogVersion());
                    } else {
                        Log.i("[Conversion]:", "转换关系批量增加失败!");
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
    }

    public void parseRequestError(BaseData baseData) {
        if (this.listener != null) {
            this.listener.parseRequestFailure(baseData);
        }
    }

    public List<ConversionData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<ConversionData> list = new ArrayList();
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
                    ConversionData data = new ConversionData();
                    data.setCn1Id(jsonObj.getString("ID"));
                    data.setCn1Upid(jsonObj.getString("CN1_Upid"));
                    data.setCn1Cpid(jsonObj.getString("CN1_Cpid"));
                    data.setCn1Proportion(jsonObj.getString("CN1_Proportion"));
                    data.setCn1Operation(jsonObj.getString("CN1_Operation"));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setCreateUser(createUser);
                    data.setCreateTime(createTime);
                    data.setModifyUser(modifyUser);
                    data.setModifyTime(modifyTime);
                    data.setRowVersion(rowVersion);
                    list.add(data);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(getClass().toString(), "======>>>>>数据转换解析网络数据异常!");
                return list;
            }
        }
        return list;
    }
}
