package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.VendingPasswordData;
import com.mc.vending.tools.ZillionLog;
import java.util.List;

public class VendingPasswordDbOper {
    public VendingPasswordData getVendingPassword() {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT VP3_ID,VP3_Password FROM VendingPassword ORDER BY VP3_CreateTime DESC limit 1", null);
        if (!c.moveToNext()) {
            return null;
        }
        VendingPasswordData vendingPassword = new VendingPasswordData();
        vendingPassword.setVp3Id(c.getString(c.getColumnIndex("VP3_ID")));
        vendingPassword.setVp3Password(c.getString(c.getColumnIndex("VP3_Password")));
        return vendingPassword;
    }

    public VendingPasswordData getVendingPasswordByPassword(String password) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT VP3_ID,VP3_Password FROM VendingPassword WHERE VP3_Password=? ORDER BY VP3_CreateTime DESC limit 1", new String[]{password});
        if (!c.moveToNext()) {
            return null;
        }
        VendingPasswordData vendingPassword = new VendingPasswordData();
        vendingPassword = new VendingPasswordData();
        vendingPassword.setVp3Id(c.getString(c.getColumnIndex("VP3_ID")));
        vendingPassword.setVp3Password(c.getString(c.getColumnIndex("VP3_Password")));
        return vendingPassword;
    }

    public boolean addVendingPassword(VendingPasswordData vendingPassword) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into VendingPassword(VP3_ID,VP3_M02_ID,VP3_Password,VP3_CreateUser,VP3_CreateTime,VP3_ModifyUser,VP3_ModifyTime,VP3_RowVersion)values(?,?,?,?,?,?,?,?)");
        stat.bindString(1, vendingPassword.getVp3Id());
        stat.bindString(2, vendingPassword.getVp3M02Id());
        stat.bindString(3, vendingPassword.getVp3Password());
        stat.bindString(4, vendingPassword.getVp3CreateUser());
        stat.bindString(5, vendingPassword.getVp3CreateTime());
        stat.bindString(6, vendingPassword.getVp3ModifyUser());
        stat.bindString(7, vendingPassword.getVp3ModifyTime());
        stat.bindString(8, vendingPassword.getVp3RowVersion());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchAddVendingPassword(List<VendingPasswordData> list) {
        String insertSql = "insert into VendingPassword(VP3_ID,VP3_M02_ID,VP3_Password,VP3_CreateUser,VP3_CreateTime,VP3_ModifyUser,VP3_ModifyTime,VP3_RowVersion)values(?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (VendingPasswordData vendingPassword : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, vendingPassword.getVp3Id());
                stat.bindString(2, vendingPassword.getVp3M02Id());
                stat.bindString(3, vendingPassword.getVp3Password());
                stat.bindString(4, vendingPassword.getVp3CreateUser());
                stat.bindString(5, vendingPassword.getVp3CreateTime());
                stat.bindString(6, vendingPassword.getVp3ModifyUser());
                stat.bindString(7, vendingPassword.getVp3ModifyTime());
                stat.bindString(8, vendingPassword.getVp3RowVersion());
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

    public Boolean updateVendingPassword(VendingPasswordData vendingPassword) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("UPDATE VendingPassword SET VP3_Password=? WHERE VP3_ID = ? ");
        stat.bindString(1, vendingPassword.getVp3Password());
        stat.bindString(2, vendingPassword.getVp3Id());
        return ((long) stat.executeUpdateDelete()) > 0 ? Boolean.valueOf(true) : Boolean.valueOf(false);
    }

    public Boolean batchUpdateVendingPassword(List<VendingPasswordData> list) {
        boolean flag = false;
        String updateSql = "UPDATE VendingPassword SET VP3_Password=? WHERE VP3_ID = ? ";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (VendingPasswordData vendingPassword : list) {
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindString(1, vendingPassword.getVp3Password());
                stat.bindString(2, vendingPassword.getVp3Id());
                stat.executeUpdateDelete();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            flag = true;
        } catch (SQLException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            db.endTransaction();
            e.printStackTrace();
        }
        return Boolean.valueOf(flag);
    }

    public boolean deleteAll() {
        String deleteSql = "DELETE FROM VendingPassword";
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
