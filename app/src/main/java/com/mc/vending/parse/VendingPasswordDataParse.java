package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.VendingPasswordData;
import com.mc.vending.db.VendingPasswordDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;

public class VendingPasswordDataParse implements DataParseListener {
    private static VendingPasswordDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static VendingPasswordDataParse getInstance() {
        if (instance == null) {
            instance = new VendingPasswordDataParse();
        }
        return instance;
    }

    public void requestVendingPasswordData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>售货机强制密码网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                VendingPasswordData vendingPasswordData = parse(baseData.getData());
                if (vendingPasswordData != null || baseData.getDeleteFlag().booleanValue()) {
                    VendingPasswordDbOper vendingPasswordDbOper = new VendingPasswordDbOper();
                    VendingPasswordData vendingPassword = vendingPasswordDbOper.getVendingPassword();
                    if (vendingPassword != null) {
                        if (vendingPassword.getVp3Id().equals(vendingPasswordData.getVp3Id())) {
                            if (vendingPasswordDbOper.updateVendingPassword(vendingPasswordData).booleanValue()) {
                                Log.i("[vendingPassword]:", "======>>>>>售货机强制密码更新成功!");
                                new DataParseHelper(this).sendLogVersion(vendingPasswordData.getLogVersion());
                            } else {
                                ZillionLog.e("[vendingPassword]:", "==========>>>>>售货机强制密码更新失败!");
                            }
                        }
                    } else if (vendingPasswordDbOper.addVendingPassword(vendingPasswordData)) {
                        Log.i("[vendingPassword]:", "======>>>>>售货机强制密码增加成功!");
                        new DataParseHelper(this).sendLogVersion(vendingPasswordData.getLogVersion());
                    } else {
                        ZillionLog.e("[vendingPassword]:", "==========>>>>>售货机强制密码增加失败!");
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

    public VendingPasswordData parse(JSONArray jsonArray) {
        Exception e;
        VendingPasswordData data = null;
        if (jsonArray == null) {
            return null;
        }
        int i = 0;
        while (i < jsonArray.length()) {
            try {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                if (jsonObj == null) {
                    i++;
                } else {
                    String createUser = jsonObj.getString("CreateUser");
                    String createTime = jsonObj.getString("CreateTime");
                    String modifyUser = jsonObj.getString("ModifyUser");
                    String modifyTime = jsonObj.getString("ModifyTime");
                    String rowVersion = new StringBuilder(String.valueOf(new Date().getTime())).toString();
                    VendingPasswordData data2 = new VendingPasswordData();
                    try {
                        data2.setVp3Id(jsonObj.getString("ID"));
                        data2.setVp3M02Id(jsonObj.getString("VP3_M02_ID"));
                        data2.setVp3Password(jsonObj.getString("VP3_Password"));
                        data2.setLogVersion(jsonObj.getString("LogVision"));
                        data2.setVp3CreateUser(createUser);
                        data2.setVp3CreateTime(createTime);
                        data2.setVp3ModifyUser(modifyUser);
                        data2.setVp3ModifyTime(modifyTime);
                        data2.setVp3RowVersion(rowVersion);
                        data = data2;
                        break;
                    } catch (Exception e2) {
                        e = e2;
                        data = data2;
                        e.printStackTrace();
                        ZillionLog.e(getClass().toString(), "======>>>>>售货机强制密码解析网络数据异常!");
                        return data;
                    }
                }
            } catch (Exception e3) {
                e = e3;
            }
        }
        return data;
    }

    public void parseRequestError(BaseData baseData) {
        if (this.listener != null) {
            this.listener.parseRequestFailure(baseData);
        }
    }
}
