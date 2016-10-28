package com.mc.vending.parse;

import android.util.Log;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.UsedRecordData;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.db.UsedRecordDbOper;
import com.mc.vending.parse.listener.DataParseListener;
import com.mc.vending.tools.ZillionLog;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class UsedRecordUploadDataParse implements DataParseListener {
    private static UsedRecordUploadDataParse instance = null;

    public static UsedRecordUploadDataParse getInstance() {
        if (instance == null) {
            instance = new UsedRecordUploadDataParse();
        }
        return instance;
    }

    public void requestStockTransactionData(String optType, String requestURL, String vendingId, String cardId) {
        JSONArray jSONArray;
        Exception e;
        List<UsedRecordData> datas = new UsedRecordDbOper().findDataToUpload();
        try {
            JSONArray jsonArray = new JSONArray();
            try {
                for (UsedRecordData data : datas) {
                    JSONObject json = new JSONObject();
                    json.put("ID", data.getUr1ID());
                    jsonArray.put(json);
                }
                if (jsonArray.length() > 0) {
                    new DataParseHelper(this).requestSubmitServer(optType, jsonArray, requestURL, (Object) datas);
                    jSONArray = jsonArray;
                    return;
                }
            } catch (Exception e2) {
                e = e2;
                jSONArray = jsonArray;
                e.printStackTrace();
                ZillionLog.e(getClass().toString(), "======>>>>>库存交易记录网络请求数据异常!");
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            ZillionLog.e(getClass().toString(), "======>>>>>库存交易记录网络请求数据异常!");
        }
    }

    public void parseJson(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            List<StockTransactionData> datas = (List) baseData.getUserObject();
            if (!datas.isEmpty()) {
                if (new StockTransactionDbOper().batchUpdateUploadStatus(datas)) {
                    Log.i("[stockTransaction]:", "======>>>>>库存交易记录上传状态批量更新成功!" + datas.size());
                } else {
                    ZillionLog.e("[stockTransaction]:", "==========>>>>库存交易记录上传状态批量增加失败!");
                }
            }
        }
    }

    public void parseRequestError(BaseData baseData) {
    }
}
