package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.BaseParseData;
import com.mc.vending.data.RetreatDetailData;
import com.mc.vending.data.RetreatHeadData;
import com.mc.vending.db.RetreatHeadDbOper;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class RetreatHeadDataParse extends BaseDataParse {
    private static RetreatHeadDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static RetreatHeadDataParse getInstance() {
        if (instance == null) {
            instance = new RetreatHeadDataParse();
        }
        return instance;
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<RetreatHeadData> list = parse(baseData.getData());
                if (!list.isEmpty()) {
                    RetreatHeadDbOper dbOper = new RetreatHeadDbOper();
                    List<RetreatHeadData> addList = new ArrayList();
                    List<RetreatHeadData> updateList = new ArrayList();
                    Map<String, String> map = dbOper.findAllMap();
                    for (RetreatHeadData retreatHeadData : list) {
                        if (map.containsKey(retreatHeadData.getRt1Id())) {
                            updateList.add(retreatHeadData);
                        } else {
                            addList.add(retreatHeadData);
                        }
                    }
                    if (addList.isEmpty()) {
                        if (updateList.isEmpty()) {
                            callBackLogversion((BaseParseData) list.get(0));
                        } else if (dbOper.batchUpdateReturnForward(updateList)) {
                            Log.i("[ReturnForward]:", "======>>>>>批量更新成功!==========" + list.size());
                            callBackLogversion((BaseParseData) list.get(0));
                        } else {
                            ZillionLog.e("[ReturnForward]:", "==========>>>>>批量更新失败!");
                        }
                    } else if (dbOper.batchAddReturnForward(addList)) {
                        Log.i("[ReturnForward]:", "======>>>>>批量增加成功!==========" + list.size());
                        callBackLogversion((BaseParseData) list.get(0));
                    } else {
                        ZillionLog.e("[ReturnForward]:", "==========>>>>>批量增加失败!");
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

    public void requestReturnForward(String optType, String requestURL, String vendingId, int rowCount) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            json.put("RowCount", rowCount);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL, Integer.valueOf(rowCount));
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>退货单网络请求数据异常!");
        }
    }

    public List<RetreatHeadData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<RetreatHeadData> list = new ArrayList();
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
                    RetreatHeadData data = new RetreatHeadData();
                    data.setRt1Id(jsonObj.getString("ID"));
                    data.setRt1Ce1Id(jsonObj.getString("RT1_CE1_ID"));
                    data.setRt1Cu1Id(jsonObj.getString("RT1_CU1_ID"));
                    data.setRt1M02Id(jsonObj.getString("RT1_M02_ID"));
                    data.setRt1Rtcode(jsonObj.getString("RT1_RTCode"));
                    data.setRt1Status(jsonObj.getString("RT1_Status"));
                    data.setRt1Type(jsonObj.getString("RT1_Type"));
                    data.setRt1Vd1Id(jsonObj.getString("RT1_VD1_ID"));
                    data.setCreateUser(createUser);
                    data.setCreateTime(createTime);
                    data.setModifyUser(modifyUser);
                    data.setModifyTime(modifyTime);
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setRowVersion(rowVersion);
                    JSONArray details = new JSONArray();
                    details = jsonObj.getJSONArray("Detail");
                    List<RetreatDetailData> retreatDetailDatas = new ArrayList();
                    for (int j = 0; j < details.length(); j++) {
                        JSONObject detail = details.getJSONObject(j);
                        if (detail != null) {
                            rowVersion = new StringBuilder(String.valueOf(new Date().getTime())).toString();
                            RetreatDetailData detailData = new RetreatDetailData();
                            detailData.setRt2Id(detail.getString("ID"));
                            detailData.setRt2M02Id(detail.getString("RT2_M02_ID"));
                            detailData.setRt2Pd1Id(detail.getString("RT2_PD1_ID"));
                            detailData.setRt2Rt1Id(detail.getString("RT2_RT1_ID"));
                            detailData.setRt2SaleType(detail.getString("RT2_SaleType"));
                            detailData.setRt2Sp1Id(detail.getString("RT2_SP1_ID"));
                            detailData.setRt2Vc1Code(detail.getString("RT2_VC1_CODE"));
                            detailData.setRt2PlanQty(Integer.valueOf(detail.getInt("RT2_PlanQty")));
                            try {
                                detailData.setRt2ActualQty(Integer.valueOf(detail.getInt("RT2_ActualQty")));
                            } catch (Exception e) {
                                detailData.setRt2ActualQty(Integer.valueOf(0));
                            }
                            detailData.setCreateTime(createTime);
                            detailData.setCreateUser(createUser);
                            detailData.setModifyTime(modifyTime);
                            detailData.setModifyUser(modifyUser);
                            detailData.setRowVersion(rowVersion);
                            retreatDetailDatas.add(detailData);
                        }
                    }
                    data.setRetreatDetailDatas(retreatDetailDatas);
                    list.add(data);
                }
                i++;
            } catch (Exception e2) {
                e2.printStackTrace();
                Log.e(getClass().toString(), "======>>>>>解析数据异常!");
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
