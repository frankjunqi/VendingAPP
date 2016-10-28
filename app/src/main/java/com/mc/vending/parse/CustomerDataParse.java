package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.CustomerData;
import com.mc.vending.db.CustomerDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class CustomerDataParse implements DataParseListener {
    private static CustomerDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static CustomerDataParse getInstance() {
        if (instance == null) {
            instance = new CustomerDataParse();
        }
        return instance;
    }

    public void requestCustomerData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>客户网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<CustomerData> list = parse(baseData.getData());
                if (!list.isEmpty() || baseData.getDeleteFlag().booleanValue()) {
                    CustomerDbOper customerDbOper = new CustomerDbOper();
                    if (customerDbOper.deleteAll()) {
                        if (customerDbOper.batchAddCustomer(list)) {
                            Log.i("[customer]:", "客户批量增加成功!======" + list.size());
                            new DataParseHelper(this).sendLogVersion(((CustomerData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[customer]:", "客户批量增加失败!");
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

    public List<CustomerData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<CustomerData> list = new ArrayList();
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
                    CustomerData data = new CustomerData();
                    data.setCu1Id(jsonObj.getString("ID"));
                    data.setCu1M02Id(jsonObj.getString("CU1_M02_ID"));
                    data.setCu1Code(jsonObj.getString("CU1_CODE"));
                    data.setCu1Name(jsonObj.getString("CU1_Name"));
                    data.setCu1Relation(jsonObj.getString("CU1_Relation"));
                    data.setCu1RelationPhone(jsonObj.getString("CU1_RelationPhone"));
                    data.setCu1Saler(jsonObj.getString("CU1_Saler"));
                    data.setCu1SalerPhone(jsonObj.getString("CU1_SalerPhone"));
                    data.setCu1Country(jsonObj.getString("CU1_Country"));
                    data.setCu1City(jsonObj.getString("CU1_City"));
                    data.setCu1Area(jsonObj.getString("CU1_Area"));
                    data.setCu1Address(jsonObj.getString("CU1_Address"));
                    data.setCu1LastImportTime(jsonObj.getString("CU1_LastImportTime"));
                    data.setCu1CodeFather(jsonObj.getString("CU1_CODE_Father"));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setCu1CreateUser(createUser);
                    data.setCu1CreateTime(createTime);
                    data.setCu1ModifyUser(modifyUser);
                    data.setCu1ModifyTime(modifyTime);
                    data.setCu1RowVersion(rowVersion);
                    list.add(data);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>客户解析数据异常!");
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
