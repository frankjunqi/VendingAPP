package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.ReplenishmentDetailData;
import com.mc.vending.data.ReplenishmentHeadData;
import com.mc.vending.db.ReplenishmentDetailDbOper;
import com.mc.vending.db.ReplenishmentHeadDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class ReplenishmentDataParse implements DataParseListener {
    private static ReplenishmentDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static ReplenishmentDataParse getInstance() {
        if (instance == null) {
            instance = new ReplenishmentDataParse();
        }
        return instance;
    }

    public void requestReplenishmentData(String optType, String requestURL, String vendingId, int rowCount) {
        try {
            JSONObject json;
            JSONArray jsonArray;
            if (Constant.HTTP_OPERATE_TYPE_GETDATA.equals(optType)) {
                json = new JSONObject();
                json.put("VD1_ID", vendingId);
                json.put("RowCount", rowCount);
                new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
            } else if (Constant.HTTP_OPERATE_TYPE_UPDATESTATUS.equals(optType)) {
                List<ReplenishmentHeadData> datas = new ReplenishmentHeadDbOper().findReplenishmentHeadToUpload();
                if (!datas.isEmpty()) {
                    jsonArray = new JSONArray();
                    for (ReplenishmentHeadData replenishmentHeadData : datas) {
                        json = new JSONObject();
                        json.put("VD1_ID", vendingId);
                        json.put("RH1_ID", replenishmentHeadData.getRh1Id());
                        json.put("RH1_OrderStatus", replenishmentHeadData.getRh1OrderStatus());
                        jsonArray.put(json);
                    }
                    if (jsonArray.length() > 0) {
                        new DataParseHelper(this).requestSubmitServer(optType, jsonArray, requestURL, (Object) datas);
                    }
                }
            } else if (Constant.HTTP_OPERATE_TYPE_UPDATEDETAILDIFFERENTIAQTY.equals(optType)) {
                List<ReplenishmentDetailData> datas2 = new ReplenishmentDetailDbOper().findReplenishmentDetailToUpload();
                if (!datas2.isEmpty()) {
                    jsonArray = new JSONArray();
                    for (ReplenishmentDetailData replenishmentDetailData : datas2) {
                        json = new JSONObject();
                        json.put("VD1_ID", vendingId);
                        json.put("RH2_ID", replenishmentDetailData.getRh2Id());
                        json.put("RH2_DifferentiaQty", replenishmentDetailData.getRh2DifferentiaQty());
                        jsonArray.put(json);
                    }
                    if (jsonArray.length() > 0) {
                        new DataParseHelper(this).requestSubmitServer(optType, jsonArray, requestURL, (Object) datas2);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>补货单/补货单状态更新/补化差异网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (Constant.HTTP_OPERATE_TYPE_UPDATESTATUS.equals(baseData.getOptType())) {
            if (baseData.isSuccess().booleanValue()) {
                List<ReplenishmentHeadData> datas = (List) baseData.getUserObject();
                if (!datas.isEmpty()) {
                    if (new ReplenishmentHeadDbOper().batchUpdateUploadStatus(datas)) {
                        Log.i("[replenishment]:", "======>>>>>补货单状态更新上传状态批量更新成功!" + datas.size());
                    } else {
                        ZillionLog.e("[replenishment]:", "==========>>>>>补货单状态更新上传状态批量更新失败!");
                    }
                }
            }
        } else if (Constant.HTTP_OPERATE_TYPE_UPDATEDETAILDIFFERENTIAQTY.equals(baseData.getOptType())) {
            if (baseData.isSuccess().booleanValue()) {
                List<ReplenishmentDetailData> datas2 = (List) baseData.getUserObject();
                if (!datas2.isEmpty()) {
                    if (new ReplenishmentDetailDbOper().batchUpdateUploadStatus(datas2)) {
                        Log.i("[replenishment]:", "======>>>>>补货差异上传状态批量更新成功!" + datas2.size());
                    } else {
                        ZillionLog.e("[replenishment]:", "==========>>>>>补货差异上传状态批量更新失败!");
                    }
                }
            }
        } else if (!Constant.HTTP_OPERATE_TYPE_GETDATA.equals(baseData.getOptType())) {
        } else {
            if (baseData.isSuccess().booleanValue()) {
                if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                    List<ReplenishmentHeadData> list = parse(baseData.getData());
                    if (!list.isEmpty()) {
                        ReplenishmentHeadDbOper replenishmentHeadDbOper = new ReplenishmentHeadDbOper();
                        List<ReplenishmentHeadData> addList = new ArrayList();
                        List<ReplenishmentHeadData> updateList = new ArrayList();
                        Map<String, String> map = replenishmentHeadDbOper.findAllMap();
                        for (ReplenishmentHeadData replenishmentHead : list) {
                            if (map.containsKey(replenishmentHead.getRh1Id())) {
                                updateList.add(replenishmentHead);
                            } else {
                                addList.add(replenishmentHead);
                            }
                        }
                        if (addList.isEmpty()) {
                            if (updateList.isEmpty()) {
                                new DataParseHelper(this).sendLogVersion(((ReplenishmentHeadData) list.get(0)).getLogVersion());
                            } else if (replenishmentHeadDbOper.batchUpdateReplenishmentHead(updateList)) {
                                Log.i("[replenishment]:", "======>>>>>补货单批量更新订单状态成功!" + updateList.size());
                                new DataParseHelper(this).sendLogVersion(((ReplenishmentHeadData) list.get(0)).getLogVersion());
                            } else {
                                ZillionLog.e("[replenishment]:", "==========>>>>>补货单批量更新订单失败!");
                            }
                        } else if (replenishmentHeadDbOper.batchAddReplenishmentHead(addList)) {
                            Log.i("[replenishment]:", "======>>>>>补货单批量增加成功!" + addList.size());
                            new DataParseHelper(this).sendLogVersion(((ReplenishmentHeadData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[replenishment]:", "==========>>>>>补货单批量增加失败!");
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
    }

    public List<ReplenishmentHeadData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<ReplenishmentHeadData> list = new ArrayList();
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
                    ReplenishmentHeadData data = new ReplenishmentHeadData();
                    data.setRh1Id(jsonObj.getString("ID"));
                    data.setRh1M02Id(jsonObj.getString("RH1_M02_ID"));
                    data.setRh1Rhcode(jsonObj.getString("RH1_RHCODE"));
                    data.setRh1RhType(jsonObj.getString("RH1_RhType"));
                    data.setRh1Cu1Id(jsonObj.getString("RH1_CU1_ID"));
                    data.setRh1Vd1Id(jsonObj.getString("RH1_VD1_ID"));
                    data.setRh1Wh1Id(jsonObj.getString("RH1_WH1_ID"));
                    data.setRh1Ce1IdPh(jsonObj.getString("RH1_CE1_ID_PH"));
                    data.setRh1DistributionRemark(jsonObj.getString("RH1_DistributionRemark"));
                    data.setRh1St1Id(jsonObj.getString("RH1_ST1_ID"));
                    data.setRh1Ce1IdBh(jsonObj.getString("RH1_CE1_ID_BH"));
                    data.setRh1ReplenishRemark(jsonObj.getString("RH1_ReplenishRemark"));
                    data.setRh1ReplenishReason(jsonObj.getString("RH1_ReplenishReason"));
                    data.setRh1OrderStatus(jsonObj.getString("RH1_OrderStatus"));
                    data.setRh1DownloadStatus(jsonObj.getString("RH1_DownloadStatus"));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setRh1UploadStatus("0");
                    data.setRh1CreateUser(createUser);
                    data.setRh1CreateTime(createTime);
                    data.setRh1ModifyUser(modifyUser);
                    data.setRh1ModifyTime(modifyTime);
                    data.setRh1RowVersion(rowVersion);
                    List<ReplenishmentDetailData> children = new ArrayList();
                    if (jsonObj.get("Detail") instanceof JSONArray) {
                        JSONArray detailArray = jsonObj.getJSONArray("Detail");
                        if (detailArray != null) {
                            for (int j = 0; j < detailArray.length(); j++) {
                                JSONObject detailJsonObj = detailArray.getJSONObject(j);
                                if (detailJsonObj != null) {
                                    String detil_createUser = detailJsonObj.getString("CreateUser");
                                    String detil_createTime = detailJsonObj.getString("CreateTime");
                                    String detil_modifyUser = detailJsonObj.getString("ModifyUser");
                                    String detil_modifyTime = detailJsonObj.getString("ModifyTime");
                                    ReplenishmentDetailData detail = new ReplenishmentDetailData();
                                    detail.setRh2Id(detailJsonObj.getString("ID"));
                                    detail.setRh2M02Id(detailJsonObj.getString("RH2_M02_ID"));
                                    detail.setRh2Rh1Id(detailJsonObj.getString("RH2_RH1_ID"));
                                    detail.setRh2Vc1Code(detailJsonObj.getString("RH2_VC1_CODE"));
                                    detail.setRh2Pd1Id(detailJsonObj.getString("RH2_PD1_ID"));
                                    detail.setRh2SaleType(detailJsonObj.getString("RH2_SaleType"));
                                    detail.setRh2Sp1Id(detailJsonObj.getString("RH2_SP1_ID"));
                                    detail.setRh2ActualQty(ConvertHelper.toInt(detailJsonObj.getString("RH2_ActualQty"), Integer.valueOf(0)));
                                    detail.setRh2DifferentiaQty(ConvertHelper.toInt(detailJsonObj.getString("RH2_DifferentiaQty"), Integer.valueOf(0)));
                                    detail.setRh2Rp1Id(detailJsonObj.getString("RH2_RP1_ID"));
                                    detail.setRh2UploadStatus("0");
                                    detail.setRh2CreateUser(detil_createUser);
                                    detail.setRh2CreateTime(detil_createTime);
                                    detail.setRh2ModifyUser(detil_modifyUser);
                                    detail.setRh2ModifyTime(detil_modifyTime);
                                    detail.setRh2RowVersion(rowVersion);
                                    children.add(detail);
                                }
                            }
                        }
                        data.setChildren(children);
                    }
                    list.add(data);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>补货单解析数据异常!");
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
