package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.RetreatDetailData;
import com.mc.vending.data.RetreatHeadData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RetreatHeadDbOper {
    public List<RetreatHeadData> findAll() {
        List<RetreatHeadData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM RetreatHead order by RT1_Status,RT1_RTCode desc", null);
        while (c.moveToNext()) {
            RetreatHeadData data = new RetreatHeadData();
            data.setRt1Id(c.getString(c.getColumnIndex("RT1_ID")));
            data.setRt1Ce1Id(c.getString(c.getColumnIndex("RT1_CE1_ID")));
            data.setRt1M02Id(c.getString(c.getColumnIndex("RT1_M02_ID")));
            data.setRt1Cu1Id(c.getString(c.getColumnIndex("RT1_CU1_ID")));
            data.setRt1Rtcode(c.getString(c.getColumnIndex("RT1_RTCode")));
            data.setRt1Status(c.getString(c.getColumnIndex("RT1_Status")));
            data.setRt1Type(c.getString(c.getColumnIndex("RT1_Type")));
            data.setRt1Vd1Id(c.getString(c.getColumnIndex("RT1_VD1_ID")));
            data.setCreateUser(c.getString(c.getColumnIndex("RT1_CreateUser")));
            data.setCreateTime(c.getString(c.getColumnIndex("RT1_CreateTime")));
            data.setModifyUser(c.getString(c.getColumnIndex("RT1_ModifyUser")));
            data.setModifyTime(c.getString(c.getColumnIndex("RT1_ModifyTime")));
            data.setRowVersion(c.getString(c.getColumnIndex("RT1_RowVersion")));
            data.setRetreatDetailDatas(new RetreatDetailDbOper().findRetreatDetailDataByRtId(data.getRt1Id()));
            list.add(data);
        }
        return list;
    }

    public boolean batchAddReturnForward(List<RetreatHeadData> list) {
        String insertSql = "insert into RetreatHead (RT1_id,RT1_M02_ID,RT1_RTCode,RT1_Type,RT1_CU1_ID,RT1_VD1_ID,RT1_CE1_ID,RT1_Status,RT1_CreateUser,RT1_CreateTime,RT1_ModifyUser,RT1_ModifyTime,RT1_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String insertDetail = "insert into RetreatDetail (RT2_ID,RT2_M02_ID,RT2_RT1_ID,RT2_VC1_CODE,RT2_PD1_ID,RT2_SaleType,RT2_SP1_ID,RT2_PlanQty,RT2_ActualQty,RT2_CreateUser,RT2_CreateTime,RT2_ModifyUser,RT2_ModifyTime,RT2_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (RetreatHeadData data : list) {
                db.compileStatement("DELETE FROM RetreatHead where RT1_id='" + data.getRt1Id() + "'").executeUpdateDelete();
                db.compileStatement("DELETE FROM RetreatDetail where RT2_RT1_ID='" + data.getRt1Id() + "'").executeUpdateDelete();
                SQLiteStatement stat = db.compileStatement(insertSql);
                int i = 1 + 1;
                stat.bindString(1, data.getRt1Id());
                int i2 = i + 1;
                stat.bindString(i, data.getRt1M02Id());
                i = i2 + 1;
                stat.bindString(i2, data.getRt1Rtcode());
                i2 = i + 1;
                stat.bindString(i, data.getRt1Type());
                i = i2 + 1;
                stat.bindString(i2, data.getRt1Cu1Id());
                i2 = i + 1;
                stat.bindString(i, data.getRt1Vd1Id());
                i = i2 + 1;
                stat.bindString(i2, data.getRt1Ce1Id());
                i2 = i + 1;
                stat.bindString(i, data.getRt1Status());
                i = i2 + 1;
                stat.bindString(i2, data.getCreateUser());
                i2 = i + 1;
                stat.bindString(i, data.getCreateTime());
                i = i2 + 1;
                stat.bindString(i2, data.getModifyUser());
                i2 = i + 1;
                stat.bindString(i, data.getModifyTime());
                i = i2 + 1;
                stat.bindString(i2, data.getRowVersion());
                stat.executeInsert();
                i2 = i;
                for (RetreatDetailData detailData : data.getRetreatDetailDatas()) {
                    stat = db.compileStatement(insertDetail);
                    i = 1 + 1;
                    stat.bindString(1, detailData.getRt2Id());
                    i2 = i + 1;
                    stat.bindString(i, detailData.getRt2M02Id());
                    i = i2 + 1;
                    stat.bindString(i2, detailData.getRt2Rt1Id());
                    i2 = i + 1;
                    stat.bindString(i, detailData.getRt2Vc1Code());
                    i = i2 + 1;
                    stat.bindString(i2, detailData.getRt2Pd1Id());
                    i2 = i + 1;
                    stat.bindString(i, detailData.getRt2SaleType());
                    i = i2 + 1;
                    stat.bindString(i2, detailData.getRt2Sp1Id());
                    i2 = i + 1;
                    stat.bindLong(i, (long) detailData.getRt2PlanQty().intValue());
                    i = i2 + 1;
                    stat.bindLong(i2, (long) detailData.getRt2ActualQty().intValue());
                    i2 = i + 1;
                    stat.bindString(i, detailData.getCreateUser());
                    i = i2 + 1;
                    stat.bindString(i2, detailData.getCreateTime());
                    i2 = i + 1;
                    stat.bindString(i, detailData.getModifyUser());
                    i = i2 + 1;
                    stat.bindString(i2, detailData.getModifyTime());
                    i2 = i + 1;
                    stat.bindString(i, detailData.getRowVersion());
                    stat.executeInsert();
                }
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        } catch (SQLException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            db.endTransaction();
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateState(String rt1Id, Map<String, Integer> vendingChnQtyMap) {
        String sql = "update RetreatHead set RT1_Status='1' where RT1_id='" + rt1Id + "'";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            db.compileStatement(sql).executeUpdateDelete();
            for (String vendingchn : vendingChnQtyMap.keySet()) {
                db.compileStatement("update RetreatDetail set rt2_actualqty=" + vendingChnQtyMap.get(vendingchn) + " where rt2_RT1_id='" + rt1Id + "' and rt2_vc1_code='" + vendingchn + "'").executeUpdateDelete();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        } catch (SQLException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            db.endTransaction();
            e.printStackTrace();
            return false;
        }
    }

    public void clearRetreat(String startDate) {
        String deleteHeadSql = "DELETE FROM RetreatHead where RT1_Status='1' and substr(RT1_CreateTime,0,11) <= '" + startDate + "' ";
        String deleteDetailSql = "DELETE FROM ReplenishmentDetail where substr(Rt2_createtime,0,11) <= '" + startDate + "' ";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            db.compileStatement(deleteDetailSql).executeInsert();
            db.compileStatement(deleteHeadSql).executeInsert();
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (SQLException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            db.endTransaction();
            e.printStackTrace();
        }
    }

    public Map<String, String> findAllMap() {
        Map<String, String> map = new HashMap();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT RT1_ID,RT1_RTCode FROM RetreatHead ", null);
        while (c.moveToNext()) {
            map.put(c.getString(c.getColumnIndex("RT1_ID")), c.getString(c.getColumnIndex("RT1_RTCode")));
        }
        return map;
    }

    public boolean batchUpdateReturnForward(List<RetreatHeadData> list) {
        String headSql = "UPDATE RetreatHead SET RT1_Status=? WHERE RT1_ID=? ";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (RetreatHeadData replenishmentHead : list) {
                SQLiteStatement stat = db.compileStatement(headSql);
                stat.bindString(1, replenishmentHead.getRt1Status());
                stat.bindString(2, replenishmentHead.getRt1Id());
                long executeUpdateDelete = (long) stat.executeUpdateDelete();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        } catch (SQLException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            db.endTransaction();
            e.printStackTrace();
            return false;
        }
    }
}
