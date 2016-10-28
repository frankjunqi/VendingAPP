package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.ReplenishmentDetailData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.List;

public class ReplenishmentDetailDbOper {
    public List<ReplenishmentDetailData> findAll() {
        List<ReplenishmentDetailData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM ReplenishmentDetail", null);
        while (c.moveToNext()) {
            ReplenishmentDetailData replenishmentDetail = new ReplenishmentDetailData();
            replenishmentDetail.setRh2Id(c.getString(c.getColumnIndex("RH2_ID")));
            replenishmentDetail.setRh2M02Id(c.getString(c.getColumnIndex("RH2_M02_ID")));
            replenishmentDetail.setRh2Rh1Id(c.getString(c.getColumnIndex("RH2_RH1_ID")));
            replenishmentDetail.setRh2Vc1Code(c.getString(c.getColumnIndex("RH2_VC1_CODE")));
            replenishmentDetail.setRh2Pd1Id(c.getString(c.getColumnIndex("RH2_PD1_ID")));
            replenishmentDetail.setRh2SaleType(c.getString(c.getColumnIndex("RH2_SaleType")));
            replenishmentDetail.setRh2Sp1Id(c.getString(c.getColumnIndex("RH2_SP1_ID")));
            replenishmentDetail.setRh2ActualQty(Integer.valueOf(c.getInt(c.getColumnIndex("RH2_ActualQty"))));
            replenishmentDetail.setRh2DifferentiaQty(Integer.valueOf(c.getInt(c.getColumnIndex("RH2_DifferentiaQty"))));
            replenishmentDetail.setRh2Rp1Id(c.getString(c.getColumnIndex("RH2_RP1_ID")));
            replenishmentDetail.setRh2UploadStatus(c.getString(c.getColumnIndex("RH2_UploadStatus")));
            replenishmentDetail.setRh2CreateUser(c.getString(c.getColumnIndex("RH2_CreateUser")));
            replenishmentDetail.setRh2CreateTime(c.getString(c.getColumnIndex("RH2_CreateTime")));
            replenishmentDetail.setRh2ModifyUser(c.getString(c.getColumnIndex("RH2_ModifyUser")));
            replenishmentDetail.setRh2ModifyTime(c.getString(c.getColumnIndex("RH2_ModifyTime")));
            replenishmentDetail.setRh2RowVersion(c.getString(c.getColumnIndex("RH2_RowVersion")));
            list.add(replenishmentDetail);
        }
        return list;
    }

    public List<ReplenishmentDetailData> findReplenishmentDetailkByRh1Id(String rh1Id) {
        List<ReplenishmentDetailData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM ReplenishmentDetail WHERE RH2_RH1_ID=? ORDER BY RH2_VC1_CODE ASC ", new String[]{rh1Id});
        while (c.moveToNext()) {
            ReplenishmentDetailData replenishmentDetail = new ReplenishmentDetailData();
            replenishmentDetail.setRh2Id(c.getString(c.getColumnIndex("RH2_ID")));
            replenishmentDetail.setRh2M02Id(c.getString(c.getColumnIndex("RH2_M02_ID")));
            replenishmentDetail.setRh2Rh1Id(c.getString(c.getColumnIndex("RH2_RH1_ID")));
            replenishmentDetail.setRh2Vc1Code(c.getString(c.getColumnIndex("RH2_VC1_CODE")));
            replenishmentDetail.setRh2Pd1Id(c.getString(c.getColumnIndex("RH2_PD1_ID")));
            replenishmentDetail.setRh2SaleType(c.getString(c.getColumnIndex("RH2_SaleType")));
            replenishmentDetail.setRh2Sp1Id(c.getString(c.getColumnIndex("RH2_SP1_ID")));
            replenishmentDetail.setRh2ActualQty(Integer.valueOf(c.getInt(c.getColumnIndex("RH2_ActualQty"))));
            replenishmentDetail.setRh2DifferentiaQty(Integer.valueOf(c.getInt(c.getColumnIndex("RH2_DifferentiaQty"))));
            replenishmentDetail.setRh2Rp1Id(c.getString(c.getColumnIndex("RH2_RP1_ID")));
            replenishmentDetail.setRh2UploadStatus(c.getString(c.getColumnIndex("RH2_UploadStatus")));
            replenishmentDetail.setRh2CreateUser(c.getString(c.getColumnIndex("RH2_CreateUser")));
            replenishmentDetail.setRh2CreateTime(c.getString(c.getColumnIndex("RH2_CreateTime")));
            replenishmentDetail.setRh2ModifyUser(c.getString(c.getColumnIndex("RH2_ModifyUser")));
            replenishmentDetail.setRh2ModifyTime(c.getString(c.getColumnIndex("RH2_ModifyTime")));
            replenishmentDetail.setRh2RowVersion(c.getString(c.getColumnIndex("RH2_RowVersion")));
            list.add(replenishmentDetail);
        }
        return list;
    }

    public boolean updateDifferentiaQty(int differentiaQty, int id) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("UPDATE ReplenishmentDetail SET RH2_DifferentiaQty = ? WHERE  ID = ?");
        stat.bindLong(1, (long) differentiaQty);
        stat.bindLong(2, (long) id);
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchUpdateDifferentiaQty(List<ReplenishmentDetailData> list) {
        String updateSql = "UPDATE ReplenishmentDetail SET RH2_DifferentiaQty = ? WHERE RH2_ID = ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (ReplenishmentDetailData replenishmentDetail : list) {
                String rh2Id = replenishmentDetail.getRh2Id();
                int differentiaQty = replenishmentDetail.getRh2DifferentiaQty().intValue();
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindLong(1, (long) differentiaQty);
                stat.bindString(2, rh2Id);
                stat.executeInsert();
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

    public List<ReplenishmentDetailData> findReplenishmentDetailToUpload() {
        List<ReplenishmentDetailData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT RH2_ID,RH2_DifferentiaQty FROM ReplenishmentDetail WHERE RH2_DifferentiaQty != 0 and RH2_UploadStatus = ?", new String[]{"0"});
        while (c.moveToNext()) {
            ReplenishmentDetailData replenishmentDetail = new ReplenishmentDetailData();
            replenishmentDetail.setRh2Id(c.getString(c.getColumnIndex("RH2_ID")));
            replenishmentDetail.setRh2DifferentiaQty(Integer.valueOf(c.getInt(c.getColumnIndex("RH2_DifferentiaQty"))));
            list.add(replenishmentDetail);
        }
        return list;
    }

    public boolean updateUploadStatusByRH2Id(String rh2Id) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("update ReplenishmentDetail set RH2_UploadStatus = ? where RH2_ID = ?");
        stat.bindString(1, "1");
        stat.bindString(2, rh2Id);
        if (stat.executeUpdateDelete() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchUpdateUploadStatus(List<ReplenishmentDetailData> replenishmentDetailList) {
        String updateSql = "update ReplenishmentDetail set RH2_UploadStatus = ? where RH2_ID = ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (ReplenishmentDetailData replenishmentDetail : replenishmentDetailList) {
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindString(1, "1");
                stat.bindString(2, replenishmentDetail.getRh2Id());
                stat.executeUpdateDelete();
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
