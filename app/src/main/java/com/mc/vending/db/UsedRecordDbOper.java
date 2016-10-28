package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.UsedRecordData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UsedRecordDbOper {
    String insertSql = "insert into UsedRecord(UR1_ID,UR1_M02_ID,UR1_CD1_ID,UR1_VD1_ID,UR1_PD1_ID,UR1_Quantity,CreateUser,CreateTime, ModifyUser,ModifyTime,RowVersion)values(?,?,?,?,?,?,?,?,?,?,?)";

    public List<UsedRecordData> findAll() {
        List<UsedRecordData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM UsedRecord", null);
        while (c.moveToNext()) {
            UsedRecordData data = new UsedRecordData();
            data.setUr1ID(c.getString(c.getColumnIndex("UR1_ID")));
            data.setUr1M02_ID(c.getString(c.getColumnIndex("UR1_M02_ID")));
            data.setUr1CD1_ID(c.getString(c.getColumnIndex("UR1_CD1_ID")));
            data.setUr1VD1_ID(c.getString(c.getColumnIndex("UR1_VD1_ID")));
            data.setUr1PD1_ID(c.getString(c.getColumnIndex("UR1_PD1_ID")));
            data.setUr1Quantity(c.getString(c.getColumnIndex("UR1_Quantity")));
            data.setCreateUser(c.getString(c.getColumnIndex("CreateUser")));
            data.setCreateTime(c.getString(c.getColumnIndex("CreateTime")));
            data.setModifyUser(c.getString(c.getColumnIndex("ModifyUser")));
            data.setModifyTime(c.getString(c.getColumnIndex("ModifyTime")));
            data.setRowVersion(c.getString(c.getColumnIndex("RowVersion")));
            list.add(data);
        }
        return list;
    }

    public Map<String, String> findAllUsed() {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM UsedRecord", null);
        Map<String, String> reMap = new HashMap();
        while (c.moveToNext()) {
            reMap.put(c.getString(c.getColumnIndex("UR1_ID")), c.getString(c.getColumnIndex("UR1_CD1_ID")));
        }
        return reMap;
    }

    public List<UsedRecordData> findDataToUpload() {
        List<UsedRecordData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM UsedRecord where UR1_CD1_ID", null);
        while (c.moveToNext()) {
            UsedRecordData data = new UsedRecordData();
            data.setUr1ID(c.getString(c.getColumnIndex("UR1_ID")));
            data.setUr1M02_ID(c.getString(c.getColumnIndex("UR1_M02_ID")));
            data.setUr1CD1_ID(c.getString(c.getColumnIndex("UR1_CD1_ID")));
            data.setUr1VD1_ID(c.getString(c.getColumnIndex("UR1_VD1_ID")));
            data.setUr1PD1_ID(c.getString(c.getColumnIndex("UR1_PD1_ID")));
            data.setUr1Quantity(c.getString(c.getColumnIndex("UR1_Quantity")));
            data.setCreateUser(c.getString(c.getColumnIndex("CreateUser")));
            data.setCreateTime(c.getString(c.getColumnIndex("CreateTime")));
            data.setModifyUser(c.getString(c.getColumnIndex("ModifyUser")));
            data.setModifyTime(c.getString(c.getColumnIndex("ModifyTime")));
            data.setRowVersion(c.getString(c.getColumnIndex("RowVersion")));
            list.add(data);
        }
        return list;
    }

    public int getTransQtyCount(String cardId, String skuId, String dateStr) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT max(UR1_Quantity) as UR1_Quantity FROM UsedRecord WHERE UR1_CD1_ID=? and UR1_PD1_ID =? and ModifyTime >=?", new String[]{cardId, skuId, dateStr});
        if (!c.moveToNext()) {
            return 0;
        }
        String a = c.getString(c.getColumnIndex("UR1_Quantity"));
        if (a == null) {
            a = "0";
        }
        return Double.valueOf(a).intValue();
    }

    public boolean addUsedRecord(UsedRecordData data) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement(this.insertSql);
        int i = 1 + 1;
        stat.bindString(1, data.getUr1ID());
        int i2 = i + 1;
        stat.bindString(i, data.getUr1M02_ID());
        i = i2 + 1;
        stat.bindString(i2, data.getUr1CD1_ID());
        i2 = i + 1;
        stat.bindString(i, data.getUr1VD1_ID());
        i = i2 + 1;
        stat.bindString(i2, data.getUr1PD1_ID());
        i2 = i + 1;
        stat.bindString(i, data.getUr1Quantity());
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
        return stat.executeInsert() > 0;
    }

    public boolean batchAddUsedRecord(List<UsedRecordData> list) {
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (UsedRecordData data : list) {
                SQLiteStatement stat = db.compileStatement(this.insertSql);
                int i = 1 + 1;
                stat.bindString(1, data.getUr1ID());
                int i2 = i + 1;
                stat.bindString(i, data.getUr1M02_ID());
                i = i2 + 1;
                stat.bindString(i2, data.getUr1CD1_ID());
                i2 = i + 1;
                stat.bindString(i, data.getUr1VD1_ID());
                i = i2 + 1;
                stat.bindString(i2, data.getUr1PD1_ID());
                i2 = i + 1;
                stat.bindString(i, data.getUr1Quantity());
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

    public boolean batchDeleteVendingCard(List<String> dbPra) {
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (String string : dbPra) {
                db.compileStatement("DELETE FROM UsedRecord where UR1_CD1_ID='" + string.split(",")[0] + "' and UR1_VD1_ID='" + string.split(",")[1] + "'").executeInsert();
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
