package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.tools.ZillionLog;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class StockTransactionDataParse implements DataParseListener {
    private static StockTransactionDataParse instance = null;
    public boolean isSync = false;
    private DataParseRequestListener listener;

    public DataParseRequestListener getListener() {
        return this.listener;
    }

    public void setListener(DataParseRequestListener listener) {
        this.listener = listener;
    }

    public static StockTransactionDataParse getInstance() {
        if (instance == null) {
            instance = new StockTransactionDataParse();
        }
        return instance;
    }

    public void requestStockTransactionData(String optType, String requestURL, String vendingId) {
        Exception e;
        this.isSync = true;
        ZillionLog.i(getClass().getName(), "上传交易记录01：" + this.isSync);
        List<StockTransactionData> datas = new StockTransactionDbOper().findStockTransactionDataToUpload();
        try {
            JSONArray jsonArray = new JSONArray();
            JSONArray jSONArray;
            try {
                for (StockTransactionData data : datas) {
                    JSONObject json = new JSONObject();
                    json.put("ID", data.getTs1Id());
                    json.put("TS1_M02_ID", data.getTs1M02Id());
                    json.put("TS1_BillType", data.getTs1BillType());
                    json.put("TS1_BillCode", data.getTs1BillCode());
                    json.put("TS1_CD1_ID", data.getTs1Cd1Id());
                    json.put("TS1_VD1_ID", data.getTs1Vd1Id());
                    json.put("TS1_PD1_ID", data.getTs1Pd1Id());
                    json.put("TS1_VC1_CODE", data.getTs1Vc1Code());
                    json.put("TS1_TransQty", data.getTs1TransQty());
                    json.put("TS1_TransType", data.getTs1TransType());
                    json.put("TS1_SP1_CODE", data.getTs1Sp1Code());
                    json.put("TS1_SP1_Name", data.getTs1Sp1Name());
                    json.put("CreateUser", data.getTs1CreateUser());
                    json.put("CreateTime", data.getTs1CreateTime());
                    jsonArray.put(json);
                }
                if (jsonArray.length() > 0) {
                    ZillionLog.i(getClass().getName(), "上传交易记录02：" + this.isSync);
                    new DataParseHelper(this).requestSubmitServer(optType, jsonArray, requestURL, (Object) datas);
                    jSONArray = jsonArray;
                    return;
                }
                this.isSync = false;
                ZillionLog.i(getClass().getName(), "上传交易记录03：" + this.isSync);
                jSONArray = jsonArray;
            } catch (Exception e2) {
                e = e2;
                jSONArray = jsonArray;
                this.isSync = false;
                ZillionLog.i(getClass().getName(), "上传交易记录04：" + this.isSync);
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>库存交易记录网络请求数据异常!");
            }
        } catch (Exception e3) {
            e = e3;
            this.isSync = false;
            ZillionLog.i(getClass().getName(), "上传交易记录04：" + this.isSync);
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>库存交易记录网络请求数据异常!");
        }
    }

    public void requestStockTransactionData(String optType, String requestURL, String vendingId, List<StockTransactionData> datas) {
        Exception e;
        this.isSync = true;
        ZillionLog.i(getClass().getName(), "上传交易记录12：" + this.isSync);
        try {
            JSONArray jsonArray = new JSONArray();
            JSONArray jSONArray;
            try {
                for (StockTransactionData data : datas) {
                    JSONObject json = new JSONObject();
                    json.put("ID", data.getTs1Id());
                    json.put("TS1_M02_ID", data.getTs1M02Id());
                    json.put("TS1_BillType", data.getTs1BillType());
                    json.put("TS1_BillCode", data.getTs1BillCode());
                    json.put("TS1_CD1_ID", data.getTs1Cd1Id());
                    json.put("TS1_VD1_ID", data.getTs1Vd1Id());
                    json.put("TS1_PD1_ID", data.getTs1Pd1Id());
                    json.put("TS1_VC1_CODE", data.getTs1Vc1Code());
                    json.put("TS1_TransQty", data.getTs1TransQty());
                    json.put("TS1_TransType", data.getTs1TransType());
                    json.put("TS1_SP1_CODE", data.getTs1Sp1Code());
                    json.put("TS1_SP1_Name", data.getTs1Sp1Name());
                    json.put("CreateUser", data.getTs1CreateUser());
                    json.put("CreateTime", data.getTs1CreateTime());
                    jsonArray.put(json);
                }
                if (jsonArray.length() > 0) {
                    new DataParseHelper(this).requestSubmitServer(optType, jsonArray, requestURL, (Object) datas);
                    jSONArray = jsonArray;
                    return;
                }
                this.isSync = false;
                ZillionLog.i(getClass().getName(), "上传交易记录13：" + this.isSync);
                jSONArray = jsonArray;
            } catch (Exception e2) {
                e = e2;
                jSONArray = jsonArray;
                this.isSync = false;
                ZillionLog.i(getClass().getName(), "上传交易记录14：" + this.isSync);
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>库存交易记录网络请求数据异常!");
            }
        } catch (Exception e3) {
            e = e3;
            this.isSync = false;
            ZillionLog.i(getClass().getName(), "上传交易记录14：" + this.isSync);
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>库存交易记录网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        ZillionLog.i(getClass().getName(), baseData);
        ZillionLog.i(getClass().getName(), "上传交易记录24：" + this.isSync);
        if (baseData.isSuccess().booleanValue()) {
            ZillionLog.i(getClass().getName(), "上传交易记录23：" + this.isSync);
            List<StockTransactionData> datas = (List) baseData.getUserObject();
            if (datas.isEmpty()) {
                if (this.listener != null) {
                    this.listener.parseRequestFinised(baseData);
                }
                this.isSync = false;
                return;
            }
            if (new StockTransactionDbOper().batchUpdateUploadStatus(datas)) {
                Log.i("[stockTransaction]:", "======>>>>>库存交易记录上传状态批量更新成功!" + datas.size());
            } else {
                ZillionLog.e("[stockTransaction]:", "==========>>>>库存交易记录上传状态批量增加失败!");
            }
            this.isSync = false;
            ZillionLog.i(getClass().getName(), "上传交易记录：" + this.isSync);
            if (this.listener != null) {
                this.listener.parseRequestFinised(baseData);
                return;
            }
            return;
        }
        ZillionLog.i(getClass().getName(), "上传交易记录22：" + this.isSync);
        if (this.listener != null) {
            this.listener.parseRequestFailure(baseData);
        }
        this.isSync = false;
    }

    public void parseRequestError(BaseData baseData) {
    }
}
