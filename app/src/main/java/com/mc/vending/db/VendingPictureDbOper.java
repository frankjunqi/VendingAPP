package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.VendingPictureData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.List;

public class VendingPictureDbOper {
    public List<VendingPictureData> findVendingPicture() {
        List<VendingPictureData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT VP2_ID,VP2_Seq,VP2_FilePath,VP2_RunTime FROM VendingPicture WHERE VP2_Type=1 ORDER BY VP2_Seq", null);
        while (c.moveToNext()) {
            VendingPictureData vendingPicture = new VendingPictureData();
            vendingPicture.setVp2Id(c.getString(c.getColumnIndex("VP2_ID")));
            vendingPicture.setVp2Seq(c.getString(c.getColumnIndex("VP2_Seq")));
            vendingPicture.setVp2FilePath(c.getString(c.getColumnIndex("VP2_FilePath")));
            vendingPicture.setVp2RunTime(c.getInt(c.getColumnIndex("VP2_RunTime")));
            list.add(vendingPicture);
        }
        return list;
    }

    public VendingPictureData getDefaultVendingPicture() {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT VP2_ID,VP2_Seq,VP2_FilePath,VP2_RunTime FROM VendingPicture WHERE VP2_Type=0 ORDER BY VP2_Seq limit 1", null);
        if (!c.moveToNext()) {
            return null;
        }
        VendingPictureData vendingPicture = new VendingPictureData();
        vendingPicture.setVp2Id(c.getString(c.getColumnIndex("VP2_ID")));
        vendingPicture.setVp2Seq(c.getString(c.getColumnIndex("VP2_Seq")));
        vendingPicture.setVp2FilePath(c.getString(c.getColumnIndex("VP2_FilePath")));
        vendingPicture.setVp2RunTime(c.getInt(c.getColumnIndex("VP2_RunTime")));
        return vendingPicture;
    }

    public boolean addVendingPicture(VendingPictureData vendingPicture) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into VendingPicture(VP2_ID,VP2_M02_ID,VP2_Seq,VP2_FilePath,VP2_RunTime,VP2_Type,VP2_CreateUser,VP2_CreateTime,VP2_ModifyUser,VP2_ModifyTime,VP2_RowVersion) values(?,?,?,?,?,?,?,?,?,?,?)");
        stat.bindString(1, vendingPicture.getVp2Id());
        stat.bindString(2, vendingPicture.getVp2M02Id());
        stat.bindString(3, vendingPicture.getVp2Seq());
        stat.bindString(4, vendingPicture.getVp2FilePath());
        stat.bindLong(5, (long) vendingPicture.getVp2RunTime());
        stat.bindString(6, vendingPicture.getVp2Type());
        stat.bindString(7, vendingPicture.getVp2CreateUser());
        stat.bindString(8, vendingPicture.getVp2CreateTime());
        stat.bindString(9, vendingPicture.getVp2ModifyUser());
        stat.bindString(10, vendingPicture.getVp2ModifyTime());
        stat.bindString(11, vendingPicture.getVp2RowVersion());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchAddVendingPicture(List<VendingPictureData> list) {
        String insertSql = "insert into VendingPicture(VP2_ID,VP2_M02_ID,VP2_Seq,VP2_FilePath,VP2_RunTime,VP2_Type,VP2_CreateUser,VP2_CreateTime,VP2_ModifyUser,VP2_ModifyTime,VP2_RowVersion) values(?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (VendingPictureData vendingPicture : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, vendingPicture.getVp2Id());
                stat.bindString(2, vendingPicture.getVp2M02Id());
                stat.bindString(3, vendingPicture.getVp2Seq());
                stat.bindString(4, vendingPicture.getVp2FilePath());
                stat.bindLong(5, (long) vendingPicture.getVp2RunTime());
                stat.bindString(6, vendingPicture.getVp2Type());
                stat.bindString(7, vendingPicture.getVp2CreateUser());
                stat.bindString(8, vendingPicture.getVp2CreateTime());
                stat.bindString(9, vendingPicture.getVp2ModifyUser());
                stat.bindString(10, vendingPicture.getVp2ModifyTime());
                stat.bindString(11, vendingPicture.getVp2RowVersion());
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
        String deleteSql = "DELETE FROM VendingPicture";
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
