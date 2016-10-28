package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.VendingData;
import com.mc.vending.tools.ZillionLog;

public class VendingDbOper {
    public VendingData getVending() {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT VD1_ID,VD1_CODE,VD1_Status,VD1_CardType FROM Vending limit 1", null);
        if (!c.moveToNext()) {
            return null;
        }
        VendingData vending = new VendingData();
        vending.setVd1Id(c.getString(c.getColumnIndex("VD1_ID")));
        vending.setVd1Code(c.getString(c.getColumnIndex("VD1_CODE")));
        vending.setVd1Status(c.getString(c.getColumnIndex("VD1_Status")));
        vending.setVd1CardType(c.getString(c.getColumnIndex("VD1_CardType")));
        return vending;
    }

    public boolean addVending(VendingData vending) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into Vending(VD1_ID,VD1_M02_ID,VD1_CODE,VD1_Manufacturer,VD1_VM1_ID,VD1_LastVersion,VD1_LWHSize,VD1_Color,VD1_InstallAddress,VD1_Coordinate,VD1_ST1_ID,VD1_EmergencyRel,VD1_EmergencyRelPhone,VD1_OnlineStatus,VD1_Status,VD1_CreateUser,VD1_CreateTime,VD1_ModifyUser,VD1_ModifyTime,VD1_RowVersion,VD1_CardType) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        stat.bindString(1, vending.getVd1Id());
        stat.bindString(2, vending.getVd1M02Id());
        stat.bindString(3, vending.getVd1Code());
        stat.bindString(4, vending.getVd1Manufacturer());
        stat.bindString(5, vending.getVd1Vm1Id());
        stat.bindString(6, vending.getVd1LastVersion());
        stat.bindString(7, vending.getVd1LwhSize());
        stat.bindString(8, vending.getVd1Color());
        stat.bindString(9, vending.getVd1InstallAddress());
        stat.bindString(10, vending.getVd1Coordinate());
        stat.bindString(11, vending.getVd1St1Id());
        stat.bindString(12, vending.getVd1EmergencyRel());
        stat.bindString(13, vending.getVd1EmergencyRelPhone());
        stat.bindString(14, vending.getVd1OnlineStatus());
        stat.bindString(15, vending.getVd1Status());
        stat.bindString(16, vending.getVd1CreateUser());
        stat.bindString(17, vending.getVd1CreateTime());
        stat.bindString(18, vending.getVd1ModifyUser());
        stat.bindString(19, vending.getVd1ModifyTime());
        stat.bindString(20, vending.getVd1RowVersion());
        stat.bindString(21, vending.getVd1CardType());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public boolean updateVending(VendingData vending) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("UPDATE Vending SET VD1_M02_ID=?,VD1_CODE=?,VD1_Manufacturer=?,VD1_VM1_ID=?,VD1_LastVersion=?,VD1_LWHSize=?,VD1_Color=?,VD1_InstallAddress=?,VD1_Coordinate=?,VD1_ST1_ID=?,VD1_EmergencyRel=?,VD1_EmergencyRelPhone=?,VD1_OnlineStatus=?,VD1_Status=?,VD1_CreateUser=?,VD1_CreateTime=?,VD1_ModifyUser=?,VD1_ModifyTime=?,VD1_RowVersion=?,VD1_CardType=? WHERE VD1_ID=?");
        stat.bindString(1, vending.getVd1M02Id());
        stat.bindString(2, vending.getVd1Code());
        stat.bindString(3, vending.getVd1Manufacturer());
        stat.bindString(4, vending.getVd1Vm1Id());
        stat.bindString(5, vending.getVd1LastVersion());
        stat.bindString(6, vending.getVd1LwhSize());
        stat.bindString(7, vending.getVd1Color());
        stat.bindString(8, vending.getVd1InstallAddress());
        stat.bindString(9, vending.getVd1Coordinate());
        stat.bindString(10, vending.getVd1St1Id());
        stat.bindString(11, vending.getVd1EmergencyRel());
        stat.bindString(12, vending.getVd1EmergencyRelPhone());
        stat.bindString(13, vending.getVd1OnlineStatus());
        stat.bindString(14, vending.getVd1Status());
        stat.bindString(15, vending.getVd1CreateUser());
        stat.bindString(16, vending.getVd1CreateTime());
        stat.bindString(17, vending.getVd1ModifyUser());
        stat.bindString(18, vending.getVd1ModifyTime());
        stat.bindString(19, vending.getVd1RowVersion());
        stat.bindString(20, vending.getVd1CardType());
        stat.bindString(21, vending.getVd1Id());
        if (((long) stat.executeUpdateDelete()) > 0) {
            return true;
        }
        return false;
    }

    public boolean deleteAll() {
        String deleteSql = "DELETE FROM Vending";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            db.compileStatement(deleteSql).executeInsert();
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
