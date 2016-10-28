package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.StationData;
import com.mc.vending.db.StationDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class StationDataParse implements DataParseListener {
    private static StationDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static StationDataParse getInstance() {
        if (instance == null) {
            instance = new StationDataParse();
        }
        return instance;
    }

    public void requestStationData(String optType, String requestURL, String vendingId, int rowCount) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            json.put("RowCount", rowCount);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>站点网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<StationData> list = parse(baseData.getData());
                if (!list.isEmpty()) {
                    StationDbOper stationDbOper = new StationDbOper();
                    Map<String, String> map = stationDbOper.findAllMap();
                    List<StationData> addList = new ArrayList();
                    List<StationData> updateList = new ArrayList();
                    for (StationData station : list) {
                        if (map.get(station.getSt1Id()) == null) {
                            addList.add(station);
                        } else {
                            updateList.add(station);
                        }
                    }
                    if (addList.isEmpty()) {
                        if (updateList.isEmpty()) {
                            new DataParseHelper(this).sendLogVersion(((StationData) list.get(0)).getLogVersion());
                        } else if (stationDbOper.batchUpdateStation(updateList)) {
                            Log.i("[station]:", "==========>>>>>站点批量更新成功!==========" + updateList.size());
                            new DataParseHelper(this).sendLogVersion(((StationData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[station]:", "==========>>>>>站点批量更新失败!");
                        }
                    } else if (stationDbOper.batchAddStation(addList)) {
                        Log.i("[station]: ", "======>>>>>站点批量更新成功!" + addList.size());
                        new DataParseHelper(this).sendLogVersion(((StationData) list.get(0)).getLogVersion());
                    } else {
                        ZillionLog.e("[station]:", "==========>>>>>站点批量增加失败!");
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

    public List<StationData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<StationData> list = new ArrayList();
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
                    StationData data = new StationData();
                    data.setSt1Id(jsonObj.getString("ID"));
                    data.setSt1M02Id(jsonObj.getString("ST1_M02_ID"));
                    data.setSt1Code(jsonObj.getString("ST1_CODE"));
                    data.setSt1Name(jsonObj.getString("ST1_Name"));
                    data.setSt1Ce1Id(jsonObj.getString("ST1_CE1_ID"));
                    data.setSt1Wh1Id(jsonObj.getString("ST1_WH1_ID"));
                    data.setSt1Coordinate(jsonObj.getString("ST1_Coordinate"));
                    data.setSt1Address(jsonObj.getString("ST1_Address"));
                    data.setSt1Status(jsonObj.getString("ST1_Status"));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setSt1CreateUser(createUser);
                    data.setSt1CreateTime(createTime);
                    data.setSt1ModifyUser(modifyUser);
                    data.setSt1ModifyTime(modifyTime);
                    data.setSt1RowVersion(rowVersion);
                    list.add(data);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>站点解析数据异常!");
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
