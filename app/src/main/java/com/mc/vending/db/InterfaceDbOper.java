package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.InterfaceData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.List;

public class InterfaceDbOper {
    public List<InterfaceData> findAll() {
        List<InterfaceData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM Interface ORDER BY M03_ExeIndex", null);
        while (c.moveToNext()) {
            InterfaceData data = new InterfaceData();
            data.setM03Id(c.getString(c.getColumnIndex("M03_ID")));
            data.setM03M02Id(c.getString(c.getColumnIndex("M03_M02_ID")));
            data.setM03Name(c.getString(c.getColumnIndex("M03_Name")));
            data.setM03Target(c.getString(c.getColumnIndex("M03_Target")));
            data.setM03Optype(c.getString(c.getColumnIndex("M03_Optype")));
            data.setM03Remark(c.getString(c.getColumnIndex("M03_Remark")));
            data.setM03ExeInterval(c.getInt(c.getColumnIndex("M03_ExeInterval")));
            data.setM03StartTime(c.getString(c.getColumnIndex("M03_StartTime")));
            data.setM03EndTime(c.getString(c.getColumnIndex("M03_EndTime")));
            data.setM03ExeIndex(c.getInt(c.getColumnIndex("M03_ExeIndex")));
            data.setM03RowCount(c.getInt(c.getColumnIndex("M03_RowCount")));
            data.setM03CreateUser(c.getString(c.getColumnIndex("M03_CreateUser")));
            data.setM03CreateTime(c.getString(c.getColumnIndex("M03_CreateTime")));
            data.setM03ModifyUser(c.getString(c.getColumnIndex("M03_ModifyUser")));
            data.setM03ModifyTime(c.getString(c.getColumnIndex("M03_ModifyTime")));
            data.setM03RowVersion(c.getString(c.getColumnIndex("M03_RowVersion")));
            list.add(data);
        }
        return list;
    }

    public boolean batchAddInterface(List<InterfaceData> list) {
        String insertSql = "insert into Interface(M03_ID,M03_M02_ID,M03_Name,M03_Target,M03_Optype,M03_Remark,M03_ExeInterval,M03_StartTime,M03_EndTime,M03_ExeIndex,M03_RowCount,M03_CreateUser,M03_CreateTime,M03_ModifyUser,M03_ModifyTime,M03_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (InterfaceData data : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, data.getM03Id());
                stat.bindString(2, data.getM03M02Id());
                stat.bindString(3, data.getM03Name());
                stat.bindString(4, data.getM03Target());
                stat.bindString(5, data.getM03Optype());
                stat.bindString(6, data.getM03Remark());
                stat.bindLong(7, (long) data.getM03ExeInterval());
                stat.bindString(8, data.getM03StartTime());
                stat.bindString(9, data.getM03EndTime());
                stat.bindLong(10, (long) data.getM03ExeIndex());
                stat.bindLong(11, (long) data.getM03RowCount());
                stat.bindString(12, data.getM03CreateUser());
                stat.bindString(13, data.getM03CreateTime());
                stat.bindString(14, data.getM03ModifyUser());
                stat.bindString(15, data.getM03ModifyTime());
                stat.bindString(16, data.getM03RowVersion());
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

    public boolean deleteAll() {
        String deleteSql = "DELETE FROM Interface";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            db.compileStatement(deleteSql).executeUpdateDelete();
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
