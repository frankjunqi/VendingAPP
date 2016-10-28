package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.CustomerEmpLinkData;
import com.mc.vending.db.CustomerEmpLinkDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class CustomerEmpLinkDataParse implements DataParseListener {
    private static CustomerEmpLinkDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static CustomerEmpLinkDataParse getInstance() {
        if (instance == null) {
            instance = new CustomerEmpLinkDataParse();
        }
        return instance;
    }

    public void requestCustomerEmpLinkData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>客户员工网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<CustomerEmpLinkData> list = parse(baseData.getData());
                if (!list.isEmpty() || baseData.getDeleteFlag().booleanValue()) {
                    CustomerEmpLinkDbOper customerEmpLinkDbOper = new CustomerEmpLinkDbOper();
                    if (customerEmpLinkDbOper.deleteAll()) {
                        if (customerEmpLinkDbOper.batchAddCustomerEmpLink(list)) {
                            Log.i("[customerEmpLink]:", "客户员工批量增加成功!======" + list.size());
                            new DataParseHelper(this).sendLogVersion(((CustomerEmpLinkData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[customerEmpLink]:", "客户员工批量增加失败!");
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

    public List<CustomerEmpLinkData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<CustomerEmpLinkData> list = new ArrayList();
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
                    CustomerEmpLinkData data = new CustomerEmpLinkData();
                    data.setCe1Id(jsonObj.getString("ID"));
                    data.setCe1M02Id(jsonObj.getString("CE1_M02_ID"));
                    data.setCe1Cu1Id(jsonObj.getString("CE1_CU1_ID"));
                    data.setCe1Code(jsonObj.getString("CE1_CODE"));
                    data.setCe1Name(jsonObj.getString("CE1_Name"));
                    data.setCe1EnglishName(jsonObj.getString("CE1_EnglishName"));
                    data.setCe1Sex(jsonObj.getString("CE1_Sex"));
                    data.setCe1Dp1Id(jsonObj.getString("CE1_DP1_ID"));
                    data.setCe1DicIdJob(jsonObj.getString("CE1_Dic_ID_JOB"));
                    data.setCe1Phone(jsonObj.getString("CE1_Phone"));
                    data.setCe1Status(jsonObj.getString("CE1_Status"));
                    data.setCe1Remark(jsonObj.getString("CE1_Remark"));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setCe1CreateUser(createUser);
                    data.setCe1CreateTime(createTime);
                    data.setCe1ModifyUser(modifyUser);
                    data.setCe1ModifyTime(modifyTime);
                    data.setCe1RowVersion(rowVersion);
                    list.add(data);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>客户员工解析数据异常!");
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
