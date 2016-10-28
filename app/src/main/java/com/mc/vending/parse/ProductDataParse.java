package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.ProductData;
import com.mc.vending.db.ProductDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class ProductDataParse implements DataParseListener {
    private static ProductDataParse instance = null;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static ProductDataParse getInstance() {
        if (instance == null) {
            instance = new ProductDataParse();
        }
        return instance;
    }

    public void requestProductData(String optType, String requestURL, String vendingId, int rowCount) {
        JSONObject json = new JSONObject();
        try {
            json.put("VD1_ID", vendingId);
            json.put("RowCount", rowCount);
            new DataParseHelper(this).requestSubmitServer(optType, json, requestURL);
        } catch (Exception e) {
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "==========>>>>>产品网络请求异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData != null && baseData.getData() != null && baseData.getData().length() != 0) {
                List<ProductData> list = parse(baseData.getData());
                if (!list.isEmpty()) {
                    ProductDbOper productDbOper = new ProductDbOper();
                    Map<String, String> map = productDbOper.findAllProduct();
                    List<ProductData> addList = new ArrayList();
                    List<ProductData> updateList = new ArrayList();
                    for (ProductData product : list) {
                        if (map.get(product.getPd1Id()) == null) {
                            addList.add(product);
                        } else {
                            updateList.add(product);
                        }
                    }
                    if (addList.isEmpty()) {
                        if (updateList.isEmpty()) {
                            new DataParseHelper(this).sendLogVersion(((ProductData) list.get(0)).getLogVersion());
                        } else if (productDbOper.batchUpdateProduct(updateList)) {
                            Log.i("[product]:", "==========>>>>>产品批量更新成功!==========" + updateList.size());
                            new DataParseHelper(this).sendLogVersion(((ProductData) list.get(0)).getLogVersion());
                        } else {
                            ZillionLog.e("[product]:", "==========>>>>>产品批量更新失败!");
                        }
                    } else if (productDbOper.batchAddProduct(addList)) {
                        Log.i("[product]:", "==========>>>>>产品批量增加成功!==========" + addList.size());
                        new DataParseHelper(this).sendLogVersion(((ProductData) list.get(0)).getLogVersion());
                    } else {
                        ZillionLog.e("[product]:", "==========>>>>>产品批量增加失败!");
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

    public List<ProductData> parse(JSONArray jsonArray) {
        if (jsonArray == null) {
            return new ArrayList(0);
        }
        List<ProductData> list = new ArrayList();
        int i = 0;
        while (i < jsonArray.length()) {
            try {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                if (jsonObj != null) {
                    ProductData data = new ProductData();
                    data.setPd1Id(jsonObj.getString("ID"));
                    data.setPd1M02Id(jsonObj.getString("PD1_M02_ID"));
                    data.setPd1Code(jsonObj.getString("PD1_CODE"));
                    data.setPd1Name(jsonObj.getString("PD1_Name"));
                    data.setPd1ManufactureModel(jsonObj.getString("PD1_ManufactureModel"));
                    data.setPd1Size(jsonObj.getString("PD1_Size"));
                    data.setPd1Brand(jsonObj.getString("PD1_Brand"));
                    data.setPd1Package(jsonObj.getString("PD1_Package"));
                    data.setPd1Unit(jsonObj.getString("PD1_Unit"));
                    data.setPd1LastImportTime(jsonObj.getString("PD1_LastImportTime"));
                    data.setPd1Description(jsonObj.getString("PD1_Description"));
                    data.setLogVersion(jsonObj.getString("LogVision"));
                    list.add(data);
                }
                i++;
            } catch (Exception e) {
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "==========>>>>>产品数据解析异常!");
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
