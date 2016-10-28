package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.VendingProLinkData;
import com.mc.vending.db.VendingProLinkDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class VendingProLinkDataParse implements DataParseListener {
    private static VendingProLinkDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static VendingProLinkDataParse getInstance() {
        if (instance == null) {
            instance = new VendingProLinkDataParse();
        }
        return instance;
    }

    public void requestVendingProLinkData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>售货机产品网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<VendingProLinkData> list = parse(baseData.getData());
                if (!list.isEmpty() || baseData.getDeleteFlag().booleanValue()) {
                    VendingProLinkDbOper vendingProLinkDbOper = new VendingProLinkDbOper();
                    if (vendingProLinkDbOper.deleteAll()) {
                        if (vendingProLinkDbOper.batchAddVendingProLink(list)) {
                            Log.i("[vendingProLink]:", "======>>>>>售货机产品批量增加成功!" + list.size());
                            new DataParseHelper(this).sendLogVersion(((VendingProLinkData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[vendingProLink]:", "==========>>>>>售货机产品批量增加失败!");
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

    public List<VendingProLinkData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<VendingProLinkData> list = new ArrayList();
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
                    VendingProLinkData data = new VendingProLinkData();
                    data.setVp1Id(jsonObj.getString("ID"));
                    data.setVp1M02Id(jsonObj.getString("VP1_M02_ID"));
                    data.setVp1Vd1Id(jsonObj.getString("VP1_VD1_ID"));
                    data.setVp1Pd1Id(jsonObj.getString("VP1_PD1_ID"));
                    data.setVp1PromptValue(ConvertHelper.toInt(jsonObj.getString("VP1_PromptValue"), Integer.valueOf(0)));
                    data.setVp1WarningValue(ConvertHelper.toInt(jsonObj.getString("VP1_WarningValue"), Integer.valueOf(0)));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setVp1CreateUser(createUser);
                    data.setVp1CreateTime(createTime);
                    data.setVp1ModifyUser(modifyUser);
                    data.setVp1ModifyTime(modifyTime);
                    data.setVp1RowVersion(rowVersion);
                    list.add(data);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>售货机产品解析数据异常!");
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
