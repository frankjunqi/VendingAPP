package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.UsedRecordData;
import com.mc.vending.db.UsedRecordDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class UsedRecordDownloadDataParse implements DataParseListener {
    private static UsedRecordDownloadDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static UsedRecordDownloadDataParse getInstance() {
        if (instance == null) {
            instance = new UsedRecordDownloadDataParse();
        }
        return instance;
    }

    public void requestUsedRecordData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>卡与产品领用网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<UsedRecordData> list = parse(baseData.getData());
                if (!list.isEmpty() || baseData.getDeleteFlag().booleanValue()) {
                    UsedRecordDbOper dbOperbOper = new UsedRecordDbOper();
                    List<String> dbPra = new ArrayList();
                    for (UsedRecordData data : list) {
                        dbPra.add(data.getUr1CD1_ID() + "," + data.getUr1VD1_ID());
                    }
                    if (dbOperbOper.batchDeleteVendingCard(dbPra)) {
                        Log.i("[UsedRecord]:", "卡与产品领用批量删除成功!");
                        if (dbOperbOper.batchAddUsedRecord(list)) {
                            Log.i("[UsedRecord]:", "卡与产品领用批量增加成功!======" + list.size());
                            new DataParseHelper(this).sendLogVersion(((UsedRecordData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[UsedRecord]:", "卡与产品领用批量增加失败!");
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

    public List<UsedRecordData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<UsedRecordData> list = new ArrayList();
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
                    UsedRecordData data = new UsedRecordData();
                    data.setUr1ID(jsonObj.getString("ID"));
                    data.setUr1M02_ID(jsonObj.getString("UR1_M02_ID"));
                    data.setUr1CD1_ID(jsonObj.getString("UR1_CD1_ID"));
                    data.setUr1VD1_ID(jsonObj.getString("UR1_VD1_ID"));
                    data.setUr1PD1_ID(jsonObj.getString("UR1_PD1_ID"));
                    data.setUr1Quantity(jsonObj.getString("UR1_Quantity"));
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
                ZillionLog.e(getClass().toString(), "======>>>>>卡与产品领用解析网络数据异常!");
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
