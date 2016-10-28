package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.db.VendingChnDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class VendingChnDataParse implements DataParseListener {
    private static VendingChnDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static VendingChnDataParse getInstance() {
        if (instance == null) {
            instance = new VendingChnDataParse();
        }
        return instance;
    }

    public void requestVendingChnData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>售货机货道网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<VendingChnData> list = parse(baseData.getData());
                if (!list.isEmpty() || baseData.getDeleteFlag().booleanValue()) {
                    VendingChnDbOper vendingChnDbOper = new VendingChnDbOper();
                    if (vendingChnDbOper.deleteAll()) {
                        if (vendingChnDbOper.batchAddVendingChn(list)) {
                            Log.i("[vendingChn]:", "======>>>>>售货机货道批量增加成功!" + list.size());
                            new DataParseHelper(this).sendLogVersion(((VendingChnData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[vendingChn]:", "==========>>>>>售货机货道批量增加失败!");
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

    public List<VendingChnData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<VendingChnData> list = new ArrayList();
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
                    VendingChnData data = new VendingChnData();
                    data.setVc1Id(jsonObj.getString("ID"));
                    data.setVc1M02Id(jsonObj.getString("VC1_M02_ID"));
                    data.setVc1Vd1Id(jsonObj.getString("VC1_VD1_ID"));
                    data.setVc1Code(jsonObj.getString("VC1_CODE"));
                    data.setVc1Type(jsonObj.getString("VC1_Type"));
                    data.setVc1Capacity(ConvertHelper.toInt(jsonObj.getString("VC1_Capacity"), Integer.valueOf(0)));
                    data.setVc1ThreadSize(jsonObj.getString("VC1_ThreadSize"));
                    data.setVc1Pd1Id(jsonObj.getString("VC1_PD1_ID"));
                    data.setVc1SaleType(jsonObj.getString("VC1_SaleType"));
                    data.setVc1Sp1Id(jsonObj.getString("VC1_SP1_ID"));
                    data.setVc1BorrowStatus(jsonObj.getString("VC1_BorrowStatus"));
                    data.setVc1Status(jsonObj.getString("VC1_Status"));
                    data.setVc1LineNum(jsonObj.getString("VC1_LineNum"));
                    data.setVc1ColumnNum(jsonObj.getString("VC1_ColumnNum"));
                    data.setVc1Height(jsonObj.getString("VC1_Height"));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setVc1CreateUser(createUser);
                    data.setVc1CreateTime(createTime);
                    data.setVc1ModifyUser(modifyUser);
                    data.setVc1ModifyTime(modifyTime);
                    data.setVc1RowVersion(rowVersion);
                    list.add(data);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>售货机货道解析数据异常!");
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
