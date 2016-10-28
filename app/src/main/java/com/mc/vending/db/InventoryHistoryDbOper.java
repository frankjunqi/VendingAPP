package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.InventoryHistoryData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.List;

public class InventoryHistoryDbOper {
    public boolean addInventoryHistory(InventoryHistoryData inventoryHistory) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into InventoryHistory(IH3_ID,IH3_M02_ID,IH3_IHCODE,IH3_ActualDate,IH3_CU1_ID,IH3_InventoryPeople,IH3_VD1_ID,IH3_VC1_CODE,IH3_PD1_ID,IH3_Quantity,IH3_InventoryQty,IH3_DifferentiaQty,IH3_UploadStatus,IH3_CreateUser,IH3_CreateTime,IH3_ModifyUser,IH3_ModifyTime,IH3_RowVersion) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        stat.bindString(1, inventoryHistory.getIh3Id());
        stat.bindString(2, inventoryHistory.getIh3M02Id());
        stat.bindString(3, inventoryHistory.getIh3IHcode());
        stat.bindString(4, inventoryHistory.getIh3ActualDate());
        stat.bindString(5, inventoryHistory.getIh3Cu1Id());
        stat.bindString(6, inventoryHistory.getIh3InventoryPeople());
        stat.bindString(7, inventoryHistory.getIh3Vd1Id());
        stat.bindString(8, inventoryHistory.getIh3Vc1Code());
        stat.bindString(9, inventoryHistory.getIh3Pd1Id());
        stat.bindLong(10, (long) inventoryHistory.getIh3Quantity().intValue());
        stat.bindLong(11, (long) inventoryHistory.getIh3InventoryQty().intValue());
        stat.bindLong(12, (long) inventoryHistory.getIh3DifferentiaQty().intValue());
        stat.bindString(13, inventoryHistory.getIh3UploadStatus());
        stat.bindString(14, inventoryHistory.getIh3CreateUser());
        stat.bindString(15, inventoryHistory.getIh3CreateTime());
        stat.bindString(16, inventoryHistory.getIh3ModifyUser());
        stat.bindString(17, inventoryHistory.getIh3ModifyTime());
        stat.bindString(18, inventoryHistory.getIh3RowVersion());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchAddInventoryHistory(List<InventoryHistoryData> list) {
        String insertSql = "insert into InventoryHistory(IH3_ID,IH3_M02_ID,IH3_IHCODE,IH3_ActualDate,IH3_CU1_ID,IH3_InventoryPeople,IH3_VD1_ID,IH3_VC1_CODE,IH3_PD1_ID,IH3_Quantity,IH3_InventoryQty,IH3_DifferentiaQty,IH3_UploadStatus,IH3_CreateUser,IH3_CreateTime,IH3_ModifyUser,IH3_ModifyTime,IH3_RowVersion) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (InventoryHistoryData inventoryHistory : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, inventoryHistory.getIh3Id());
                stat.bindString(2, inventoryHistory.getIh3M02Id());
                stat.bindString(3, inventoryHistory.getIh3IHcode());
                stat.bindString(4, inventoryHistory.getIh3ActualDate());
                stat.bindString(5, inventoryHistory.getIh3Cu1Id());
                stat.bindString(6, inventoryHistory.getIh3InventoryPeople());
                stat.bindString(7, inventoryHistory.getIh3Vd1Id());
                stat.bindString(8, inventoryHistory.getIh3Vc1Code());
                stat.bindString(9, inventoryHistory.getIh3Pd1Id());
                stat.bindLong(10, (long) inventoryHistory.getIh3Quantity().intValue());
                stat.bindLong(11, (long) inventoryHistory.getIh3InventoryQty().intValue());
                stat.bindLong(12, (long) inventoryHistory.getIh3DifferentiaQty().intValue());
                stat.bindString(13, inventoryHistory.getIh3UploadStatus());
                stat.bindString(14, inventoryHistory.getIh3CreateUser());
                stat.bindString(15, inventoryHistory.getIh3CreateTime());
                stat.bindString(16, inventoryHistory.getIh3ModifyUser());
                stat.bindString(17, inventoryHistory.getIh3ModifyTime());
                stat.bindString(18, inventoryHistory.getIh3RowVersion());
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

    public List<InventoryHistoryData> findInventoryHistoryDataToUpload() {
        List<InventoryHistoryData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM InventoryHistory WHERE IH3_UploadStatus=?", new String[]{"0"});
        while (c.moveToNext()) {
            InventoryHistoryData inventoryHistory = new InventoryHistoryData();
            inventoryHistory.setIh3Id(c.getString(c.getColumnIndex("IH3_ID")));
            inventoryHistory.setIh3M02Id(c.getString(c.getColumnIndex("IH3_M02_ID")));
            inventoryHistory.setIh3IHcode(c.getString(c.getColumnIndex("IH3_IHCODE")));
            inventoryHistory.setIh3ActualDate(c.getString(c.getColumnIndex("IH3_ActualDate")));
            inventoryHistory.setIh3Cu1Id(c.getString(c.getColumnIndex("IH3_CU1_ID")));
            inventoryHistory.setIh3InventoryPeople(c.getString(c.getColumnIndex("IH3_InventoryPeople")));
            inventoryHistory.setIh3Vd1Id(c.getString(c.getColumnIndex("IH3_VD1_ID")));
            inventoryHistory.setIh3Vc1Code(c.getString(c.getColumnIndex("IH3_VC1_CODE")));
            inventoryHistory.setIh3Pd1Id(c.getString(c.getColumnIndex("IH3_PD1_ID")));
            inventoryHistory.setIh3Quantity(Integer.valueOf(c.getInt(c.getColumnIndex("IH3_Quantity"))));
            inventoryHistory.setIh3InventoryQty(Integer.valueOf(c.getInt(c.getColumnIndex("IH3_InventoryQty"))));
            inventoryHistory.setIh3DifferentiaQty(Integer.valueOf(c.getInt(c.getColumnIndex("IH3_DifferentiaQty"))));
            inventoryHistory.setIh3CreateUser(c.getString(c.getColumnIndex("IH3_CreateUser")));
            inventoryHistory.setIh3CreateTime(c.getString(c.getColumnIndex("IH3_CreateTime")));
            list.add(inventoryHistory);
        }
        return list;
    }

    public boolean updateUploadStatusByIh3Id(String ih3Id) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("update InventoryHistory set IH3_UploadStatus=? where IH3_ID=?");
        stat.bindString(1, "1");
        stat.bindString(2, ih3Id);
        if (stat.executeUpdateDelete() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchUpdateUploadStatus(List<InventoryHistoryData> inventoryHistoryList) {
        String updateSql = "update InventoryHistory set IH3_UploadStatus=? where IH3_ID=?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (InventoryHistoryData inventoryHistoryData : inventoryHistoryList) {
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindString(1, "1");
                stat.bindString(2, inventoryHistoryData.getIh3Id());
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
