package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.SupplierData;
import com.mc.vending.db.SupplierDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class SupplierDataParse implements DataParseListener {
    private static SupplierDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static SupplierDataParse getInstance() {
        if (instance == null) {
            instance = new SupplierDataParse();
        }
        return instance;
    }

    public void requestSupplierData(String optType, String requestURL, String vendingId, int rowCount) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            json.put("RowCount", rowCount);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>供应商网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<SupplierData> list = parse(baseData.getData());
                if (!list.isEmpty()) {
                    SupplierDbOper supplierDbOper = new SupplierDbOper();
                    Map<String, String> map = supplierDbOper.findAllMap();
                    List<SupplierData> addList = new ArrayList();
                    List<SupplierData> updateList = new ArrayList();
                    for (SupplierData supplier : list) {
                        if (map.get(supplier.getSp1Id()) == null) {
                            addList.add(supplier);
                        } else {
                            updateList.add(supplier);
                        }
                    }
                    if (addList.isEmpty()) {
                        if (updateList.isEmpty()) {
                            new DataParseHelper(this).sendLogVersion(((SupplierData) list.get(0)).getLogVersion());
                        } else if (supplierDbOper.batchUpdateSupplier(updateList)) {
                            Log.i("[supplier]:", "==========>>>>>供应商批量更新成功!==========" + updateList.size());
                            new DataParseHelper(this).sendLogVersion(((SupplierData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[supplier]:", "==========>>>>>供应商批量更新失败!");
                        }
                    } else if (supplierDbOper.batchAddSupplier(addList)) {
                        Log.i("[supplier]:", "======>>>>>供应商批量增加成功!" + addList.size());
                        new DataParseHelper(this).sendLogVersion(((SupplierData) list.get(0)).getLogVersion());
                    } else {
                        ZillionLog.e("[supplier]:", "==========>>>>供应商批量增加失败!");
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

    public List<SupplierData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<SupplierData> list = new ArrayList();
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
                    SupplierData data = new SupplierData();
                    data.setSp1Id(jsonObj.getString("ID"));
                    data.setSp1M02Id(jsonObj.getString("SP1_M02_ID"));
                    data.setSp1Code(jsonObj.getString("SP1_CODE"));
                    data.setSp1Name(jsonObj.getString("SP1_Name"));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setSp1CreateUser(createUser);
                    data.setSp1CreateTime(createTime);
                    data.setSp1ModifyUser(modifyUser);
                    data.setSp1ModifyTime(modifyTime);
                    data.setSp1RowVersion(rowVersion);
                    list.add(data);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>供应商解析数据异常!");
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
