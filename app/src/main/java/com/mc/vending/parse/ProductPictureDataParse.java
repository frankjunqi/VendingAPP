package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.ProductPictureData;
import com.mc.vending.db.ProductPictureDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductPictureDataParse implements DataParseListener {
    private static ProductPictureDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static ProductPictureDataParse getInstance() {
        if (instance == null) {
            instance = new ProductPictureDataParse();
        }
        return instance;
    }

    public void requestProductPictureData(String optType, String requestURL, String vendingId, int rowCount) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            json.put("RowCount", rowCount);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>产品图片网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<ProductPictureData> list = parse(baseData.getData());
                if (!list.isEmpty()) {
                    ProductPictureDbOper productPictureDbOper = new ProductPictureDbOper();
                    Map<String, String> map = productPictureDbOper.findAllMap();
                    List<ProductPictureData> addList = new ArrayList();
                    List<ProductPictureData> updateList = new ArrayList();
                    for (ProductPictureData productPicture : list) {
                        if (map.get(productPicture.getPp1Id()) == null) {
                            addList.add(productPicture);
                        } else {
                            updateList.add(productPicture);
                        }
                    }
                    if (addList.isEmpty()) {
                        if (updateList.isEmpty()) {
                            new DataParseHelper(this).sendLogVersion(((ProductPictureData) list.get(0)).getLogVersion());
                        } else if (productPictureDbOper.batchUpdateProductPicture(updateList)) {
                            Log.i("[productPicture]:", "==========>>>>>产品图片批量更新成功!==========" + updateList.size());
                            new DataParseHelper(this).sendLogVersion(((ProductPictureData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[productPicture]:", "==========>>>>>产品图片批量更新失败!");
                        }
                    } else if (productPictureDbOper.batchAddProductPicture(addList)) {
                        Log.i("[productPicture]:", "======>>>>>产品图片批量增加成功!==========" + addList.size());
                        new DataParseHelper(this).sendLogVersion(((ProductPictureData) list.get(0)).getLogVersion());
                    } else {
                        ZillionLog.e("[productPicture]:", "==========>>>>>产品图片批量增加失败!");
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

    public List<ProductPictureData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<ProductPictureData> list = new ArrayList();
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
                    ProductPictureData data = new ProductPictureData();
                    data.setPp1Id(jsonObj.getString("ID"));
                    data.setPp1M02Id(jsonObj.getString("PP1_M02_ID"));
                    data.setPp1Pd1Id(jsonObj.getString("PP1_PD1_ID"));
                    data.setPp1FilePath(jsonObj.getString("PP1_FilePath"));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    data.setPp1CreateUser(createUser);
                    data.setPp1CreateTime(createTime);
                    data.setPp1ModifyUser(modifyUser);
                    data.setPp1ModifyTime(modifyTime);
                    data.setPp1RowVersion(rowVersion);
                    list.add(data);
                }
                i++;
            } catch (JSONException e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>产品图片解析数据异常!");
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
