package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.CusEmpCardPowerData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.List;

public class CusEmpCardPowerDbOper {
    public List<CusEmpCardPowerData> findAll() {
        List<CusEmpCardPowerData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM CusEmpCardPower", null);
        while (c.moveToNext()) {
            CusEmpCardPowerData cusEmpCardPower = new CusEmpCardPowerData();
            cusEmpCardPower.setCe2Id(c.getString(c.getColumnIndex("CE2_ID")));
            cusEmpCardPower.setCe2M02Id(c.getString(c.getColumnIndex("CE2_M02_ID")));
            cusEmpCardPower.setCe2Ce1Id(c.getString(c.getColumnIndex("CE2_CE1_ID")));
            cusEmpCardPower.setCe2Cd1Id(c.getString(c.getColumnIndex("CE2_CD1_ID")));
            cusEmpCardPower.setCe2CreateUser(c.getString(c.getColumnIndex("CE2_CreateUser")));
            cusEmpCardPower.setCe2CreateTime(c.getString(c.getColumnIndex("CE2_CreateTime")));
            cusEmpCardPower.setCe2ModifyUser(c.getString(c.getColumnIndex("CE2_ModifyUser")));
            cusEmpCardPower.setCe2ModifyTime(c.getString(c.getColumnIndex("CE2_ModifyTime")));
            cusEmpCardPower.setCe2RowVersion(c.getString(c.getColumnIndex("CE2_RowVersion")));
            list.add(cusEmpCardPower);
        }
        return list;
    }

    public CusEmpCardPowerData getCusEmpCardPowerByCardId(String cardId) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT CE2_ID,CE2_CE1_ID,CE2_CD1_ID FROM CusEmpCardPower WHERE CE2_CD1_ID=? limit 1", new String[]{cardId});
        if (!c.moveToNext()) {
            return null;
        }
        CusEmpCardPowerData cusEmpCardPower = new CusEmpCardPowerData();
        cusEmpCardPower.setCe2Id(c.getString(c.getColumnIndex("CE2_ID")));
        cusEmpCardPower.setCe2Ce1Id(c.getString(c.getColumnIndex("CE2_CE1_ID")));
        cusEmpCardPower.setCe2Cd1Id(c.getString(c.getColumnIndex("CE2_CD1_ID")));
        return cusEmpCardPower;
    }

    public boolean addCusEmpCardPower(CusEmpCardPowerData cusEmpCardPower) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into CusEmpCardPower(CE2_ID,CE2_M02_ID,CE2_CE1_ID,CE2_CD1_ID,CE2_CreateUser,CE2_CreateTime,CE2_ModifyUser,CE2_ModifyTime,CE2_RowVersion)values(?,?,?,?,?,?,?,?,?)");
        stat.bindString(1, cusEmpCardPower.getCe2Id());
        stat.bindString(2, cusEmpCardPower.getCe2M02Id());
        stat.bindString(3, cusEmpCardPower.getCe2Ce1Id());
        stat.bindString(4, cusEmpCardPower.getCe2Cd1Id());
        stat.bindString(5, cusEmpCardPower.getCe2CreateUser());
        stat.bindString(6, cusEmpCardPower.getCe2CreateTime());
        stat.bindString(7, cusEmpCardPower.getCe2ModifyUser());
        stat.bindString(8, cusEmpCardPower.getCe2ModifyTime());
        stat.bindString(9, cusEmpCardPower.getCe2RowVersion());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchAddCusEmpCardPower(List<CusEmpCardPowerData> list) {
        String insertSql = "insert into CusEmpCardPower(CE2_ID,CE2_M02_ID,CE2_CE1_ID,CE2_CD1_ID,CE2_CreateUser,CE2_CreateTime,CE2_ModifyUser,CE2_ModifyTime,CE2_RowVersion)values(?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (CusEmpCardPowerData cusEmpCardPower : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, cusEmpCardPower.getCe2Id());
                stat.bindString(2, cusEmpCardPower.getCe2M02Id());
                stat.bindString(3, cusEmpCardPower.getCe2Ce1Id());
                stat.bindString(4, cusEmpCardPower.getCe2Cd1Id());
                stat.bindString(5, cusEmpCardPower.getCe2CreateUser());
                stat.bindString(6, cusEmpCardPower.getCe2CreateTime());
                stat.bindString(7, cusEmpCardPower.getCe2ModifyUser());
                stat.bindString(8, cusEmpCardPower.getCe2ModifyTime());
                stat.bindString(9, cusEmpCardPower.getCe2RowVersion());
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
        String deleteSql = "DELETE FROM CusEmpCardPower";
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
