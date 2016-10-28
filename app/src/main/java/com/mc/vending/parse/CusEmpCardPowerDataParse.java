package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.CusEmpCardPowerData;
import com.mc.vending.db.CusEmpCardPowerDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class CusEmpCardPowerDataParse implements DataParseListener {
    private static CusEmpCardPowerDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static CusEmpCardPowerDataParse getInstance() {
        if (instance == null) {
            instance = new CusEmpCardPowerDataParse();
        }
        return instance;
    }

    public void requestCusEmpCardPowerData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>客户员工卡/密码权限网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<CusEmpCardPowerData> list = parse(baseData.getData());
                if (!list.isEmpty() || baseData.getDeleteFlag().booleanValue()) {
                    CusEmpCardPowerDbOper cusEmpCardPowerDbOper = new CusEmpCardPowerDbOper();
                    if (cusEmpCardPowerDbOper.deleteAll()) {
                        if (cusEmpCardPowerDbOper.batchAddCusEmpCardPower(list)) {
                            Log.i("[cusEmpCardPower]:", "客户员工卡/密码权限批量增加成功!======" + list.size());
                            new DataParseHelper(this).sendLogVersion(((CusEmpCardPowerData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[cusEmpCardPower]:", "客户员工卡/密码权限批量增加失败!");
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

    public List<CusEmpCardPowerData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<CusEmpCardPowerData> list = new ArrayList();
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
                    CusEmpCardPowerData data = new CusEmpCardPowerData();
                    data.setCe2Id(jsonObj.getString("ID"));
                    data.setCe2M02Id(jsonObj.getString("CE2_M02_ID"));
                    data.setCe2Ce1Id(jsonObj.getString("CE2_CE1_ID"));
                    data.setCe2Cd1Id(jsonObj.getString("CE2_CD1_ID"));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setCe2CreateUser(createUser);
                    data.setCe2CreateTime(createTime);
                    data.setCe2ModifyUser(modifyUser);
                    data.setCe2ModifyTime(modifyTime);
                    data.setCe2RowVersion(rowVersion);
                    list.add(data);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>客户员工卡/密码权限解析数据异常!");
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
