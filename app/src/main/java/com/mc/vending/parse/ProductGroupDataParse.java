package com.mc.vending.parse;

import android.util.Log;

import com.mc.vending.data.BaseData;
import com.mc.vending.data.ProductGroupDetailData;
import com.mc.vending.data.ProductGroupHeadData;
import com.mc.vending.db.ProductGroupHeadDbOper;
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

public class ProductGroupDataParse implements DataParseListener {
    private static ProductGroupDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static ProductGroupDataParse getInstance() {
        if (instance == null) {
            instance = new ProductGroupDataParse();
        }
        return instance;
    }

    public void requestProductGroupData(String optType, String requestURL, String vendingId) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "==========>>>>>产品组合网络请求异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<ProductGroupHeadData> list = parse(baseData.getData());
                if (!list.isEmpty() || baseData.getDeleteFlag().booleanValue()) {
                    ProductGroupHeadDbOper productGroupHeadDbOper = new ProductGroupHeadDbOper();
                    if (productGroupHeadDbOper.deleteAll()) {
                        if (productGroupHeadDbOper.batchAddProductGroupHead(list)) {
                            Log.i("[productGroup]:", "==========>>>>>产品组合批量增加成功!==========" + list.size());
                            new DataParseHelper(this).sendLogVersion(((ProductGroupHeadData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[productGroup]:", "==========>>>>>产品组合批量增加失败!");
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

    public List<ProductGroupHeadData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<ProductGroupHeadData> list = new ArrayList();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = null;
            try {
                jsonObj = jsonArray.getJSONObject(i);

                if (jsonObj != null) {
                    String createUser = jsonObj.getString("CreateUser");
                    String createTime = jsonObj.getString("CreateTime");
                    String modifyUser = jsonObj.getString("ModifyUser");
                    String modifyTime = jsonObj.getString("ModifyTime");
                    String rowVersion = new StringBuilder(String.valueOf(new Date().getTime())).toString();
                    ProductGroupHeadData data = new ProductGroupHeadData();
                    data.setPg1Id(jsonObj.getString("ID"));
                    data.setPg1M02Id(jsonObj.getString("PG1_M02_ID"));
                    data.setPg1Cu1Id(jsonObj.getString("PG1_CU1_ID"));
                    data.setPg1Code(jsonObj.getString("PG1_CODE"));
                    data.setPg1Name(jsonObj.getString("PG1_Name"));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setPg1CreateUser(createUser);
                    data.setPg1CreateTime(createTime);
                    data.setPg1ModifyUser(modifyUser);
                    data.setPg1ModifyTime(modifyTime);
                    data.setPg1RowVersion(rowVersion);
                    List<ProductGroupDetailData> children = new ArrayList();
                    if (jsonObj.get("Detail") instanceof JSONArray) {
                        JSONArray detailArray = jsonObj.getJSONArray("Detail");
                        if (detailArray != null) {
                            int j = 0;
                            while (j < detailArray.length()) {
                                try {
                                    JSONObject detailJsonObj = detailArray.getJSONObject(j);
                                    if (detailJsonObj != null) {
                                        String detil_createUser = detailJsonObj.getString("CreateUser");
                                        String detil_createTime = detailJsonObj.getString("CreateTime");
                                        String detil_modifyUser = detailJsonObj.getString("ModifyUser");
                                        String detil_modifyTime = detailJsonObj.getString("ModifyTime");
                                        ProductGroupDetailData detail = new ProductGroupDetailData();
                                        detail.setPg2Id(detailJsonObj.getString("ID"));
                                        detail.setPg2M02Id(detailJsonObj.getString("PG2_M02_ID"));
                                        detail.setPg2Pg1Id(detailJsonObj.getString("PG2_PG1_ID"));
                                        detail.setPg2Pd1Id(detailJsonObj.getString("PG2_PD1_ID"));
                                        detail.setPg2GroupQty(ConvertHelper.toInt(detailJsonObj.getString("PG2_GroupQty"), Integer.valueOf(0)));
                                        detail.setPg2CreateUser(detil_createUser);
                                        detail.setPg2CreateTime(detil_createTime);
                                        detail.setPg2ModifyUser(detil_modifyUser);
                                        detail.setPg2ModifyTime(detil_modifyTime);
                                        detail.setPg2RowVersion(rowVersion);
                                        children.add(detail);
                                    }
                                    j++;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    ZillionLog.e(getClass().toString(), "==========>>>>>产品组合数据解析异常!");
                                    return list;
                                }
                            }
                            data.setChildren(children);
                        }
                    }
                    list.add(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
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
