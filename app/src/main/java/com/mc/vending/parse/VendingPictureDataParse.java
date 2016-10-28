package com.mc.vending.parse;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.VendingPictureData;
import com.mc.vending.db.VendingPictureDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VendingPictureDataParse implements DataParseListener {
    private static VendingPictureDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static VendingPictureDataParse getInstance() {
        if (instance == null) {
            instance = new VendingPictureDataParse();
        }
        return instance;
    }

    public void requestVendingPictureData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>售货机待机图片网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<VendingPictureData> list = parse(baseData.getData());
                if (!list.isEmpty() || baseData.getDeleteFlag().booleanValue()) {
                    VendingPictureDbOper vendingPictureDbOper = new VendingPictureDbOper();
                    if (vendingPictureDbOper.deleteAll()) {
                        if (!vendingPictureDbOper.batchAddVendingPicture(list)) {
                            ZillionLog.e("[vendingPicture]:", "==========>>>>>售货机待机图片批量增加失败!");
                        } else if (list != null && list.size() > 0) {
                            new DataParseHelper(this).sendLogVersion(((VendingPictureData) list.get(0)).getLogVersion());
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

    public List<VendingPictureData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<VendingPictureData> list = new ArrayList();
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
                    VendingPictureData data = new VendingPictureData();
                    data.setVp2Id(jsonObj.getString("ID"));
                    data.setVp2M02Id(jsonObj.getString("VP2_M02_ID"));
                    data.setVp2Seq(jsonObj.getString("VP2_Seq"));
                    data.setVp2FilePath(jsonObj.getString("VP2_FilePath"));
                    data.setVp2RunTime(ConvertHelper.toInt(jsonObj.getString("VP2_RunTime"), Integer.valueOf(0)).intValue());
                    data.setVp2Type(jsonObj.getString("VP2_Type"));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setVp2CreateUser(createUser);
                    data.setVp2CreateTime(createTime);
                    data.setVp2ModifyUser(modifyUser);
                    data.setVp2ModifyTime(modifyTime);
                    data.setVp2RowVersion(rowVersion);
                    list.add(data);
                }
                i++;
            } catch (JSONException e) {
                e.printStackTrace();
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
