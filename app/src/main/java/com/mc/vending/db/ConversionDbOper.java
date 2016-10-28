package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.ConversionData;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConversionDbOper {
    private String insertSql = "INSERT INTO Conversion(CN1_ID,CN1_Upid,CN1_Cpid,CN1_Proportion,CN1_Operation,CN1_CreateUser,CN1_CreateTime,CN1_ModifyUser,CN1_ModifyTime,CN1_RowVersion) VALUES(?,?,?,?,?,?,?,?,?,?);";

    public ConversionData findConversionByCpid(String cn1Cpid) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM Conversion WHERE CN1_Cpid = ? ORDER by CN1_CreateTime desc limit 1", new String[]{cn1Cpid});
        if (!c.moveToNext()) {
            return null;
        }
        ConversionData conversionData = new ConversionData();
        conversionData.setCn1Id(c.getString(c.getColumnIndex("CN1_ID")));
        conversionData.setCn1Upid(c.getString(c.getColumnIndex("CN1_Upid")));
        conversionData.setCn1Cpid(c.getString(c.getColumnIndex("CN1_Cpid")));
        conversionData.setCn1Proportion(c.getString(c.getColumnIndex("CN1_Proportion")));
        conversionData.setCn1Operation(c.getString(c.getColumnIndex("CN1_Operation")));
        conversionData.setCn1CreateUser(c.getString(c.getColumnIndex("CN1_CreateUser")));
        conversionData.setCn1CreateTime(c.getString(c.getColumnIndex("CN1_CreateTime")));
        conversionData.setCn1ModifyUser(c.getString(c.getColumnIndex("CN1_ModifyUser")));
        conversionData.setCn1ModifyTime(c.getString(c.getColumnIndex("CN1_ModifyTime")));
        conversionData.setCn1RowVersion(c.getString(c.getColumnIndex("CN1_RowVersion")));
        return conversionData;
    }

    public Map<String, Object> findConversionByUpid(String cn1Upid) {
        Map<String, Object> cpIdWithScaleList = new HashMap();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT CN1_ID,CN1_Cpid,CN1_Proportion,CN1_RowVersion FROM Conversion WHERE CN1_Upid = ? ORDER by CN1_CreateTime desc limit 1", new String[]{cn1Upid});
        if (c.moveToNext()) {
            cpIdWithScaleList.put(c.getString(c.getColumnIndex("CN1_Cpid")), c.getString(c.getColumnIndex("CN1_Proportion")));
        }
        return cpIdWithScaleList;
    }

    public boolean addConversionData(ConversionData convertionData) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement(this.insertSql);
        int i = 1 + 1;
        stat.bindString(1, convertionData.getCn1Id());
        int i2 = i + 1;
        stat.bindString(i, convertionData.getCn1Upid());
        i = i2 + 1;
        stat.bindString(i2, convertionData.getCn1Cpid());
        i2 = i + 1;
        stat.bindString(i, convertionData.getCn1Proportion());
        i = i2 + 1;
        stat.bindString(i2, convertionData.getCn1Operation());
        i2 = i + 1;
        stat.bindString(i, convertionData.getCreateUser());
        i = i2 + 1;
        stat.bindString(i2, convertionData.getCreateTime());
        i2 = i + 1;
        stat.bindString(i, convertionData.getModifyUser());
        i = i2 + 1;
        stat.bindString(i2, convertionData.getModifyTime());
        i2 = i + 1;
        stat.bindString(i, convertionData.getRowVersion());
        return stat.executeInsert() > 0;
    }

    public boolean batchAddConversionData(List<ConversionData> list) {
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (ConversionData conversionData : list) {
                SQLiteStatement stat = db.compileStatement(this.insertSql);
                int i = 1 + 1;
                stat.bindString(1, conversionData.getCn1Id());
                int i2 = i + 1;
                stat.bindString(i, conversionData.getCn1Upid());
                i = i2 + 1;
                stat.bindString(i2, conversionData.getCn1Cpid());
                i2 = i + 1;
                stat.bindString(i, conversionData.getCn1Proportion());
                i = i2 + 1;
                stat.bindString(i2, conversionData.getCn1Operation());
                i2 = i + 1;
                stat.bindString(i, conversionData.getCreateUser());
                i = i2 + 1;
                stat.bindString(i2, conversionData.getCreateTime());
                i2 = i + 1;
                stat.bindString(i, conversionData.getModifyUser());
                i = i2 + 1;
                stat.bindString(i2, conversionData.getModifyTime());
                i2 = i + 1;
                stat.bindString(i, conversionData.getRowVersion());
                stat.executeInsert();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        } catch (SQLException e) {
            db.endTransaction();
            e.printStackTrace();
            return false;
        }
    }

    public boolean batchDeleteConversion(List<String> dbPra) {
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (String string : dbPra) {
                db.compileStatement("DELETE FROM Conversion where CN1_Upid='" + string.split(",")[0] + "' and CN1_Cpid='" + string.split(",")[1] + "'").executeInsert();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            return true;
        } catch (SQLException e) {
            db.endTransaction();
            e.printStackTrace();
            return false;
        }
    }
}
