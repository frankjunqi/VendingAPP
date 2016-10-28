package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.VendingProLinkData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.List;

public class VendingProLinkDbOper {
    public List<VendingProLinkData> findAll() {
        List<VendingProLinkData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM VendingProLink", null);
        while (c.moveToNext()) {
            VendingProLinkData vendingProLink = new VendingProLinkData();
            vendingProLink.setVp1Id(c.getString(c.getColumnIndex("VP1_ID")));
            vendingProLink.setVp1M02Id(c.getString(c.getColumnIndex("VP1_M02_ID")));
            vendingProLink.setVp1Vd1Id(c.getString(c.getColumnIndex("VP1_VD1_ID")));
            vendingProLink.setVp1Pd1Id(c.getString(c.getColumnIndex("VP1_PD1_ID")));
            vendingProLink.setVp1PromptValue(Integer.valueOf(c.getInt(c.getColumnIndex("VP1_PromptValue"))));
            vendingProLink.setVp1WarningValue(Integer.valueOf(c.getInt(c.getColumnIndex("VP1_WarningValue"))));
            vendingProLink.setVp1CreateUser(c.getString(c.getColumnIndex("VP1_CreateUser")));
            vendingProLink.setVp1CreateTime(c.getString(c.getColumnIndex("VP1_CreateTime")));
            vendingProLink.setVp1ModifyUser(c.getString(c.getColumnIndex("VP1_ModifyUser")));
            vendingProLink.setVp1ModifyTime(c.getString(c.getColumnIndex("VP1_ModifyTime")));
            vendingProLink.setVp1RowVersion(c.getString(c.getColumnIndex("VP1_RowVersion")));
            list.add(vendingProLink);
        }
        return list;
    }

    public VendingProLinkData getVendingProLinkByVidAndSkuId(String vendingId, String skuId) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT VP1_ID,VP1_VD1_ID,VP1_PD1_ID FROM VendingProLink WHERE VP1_VD1_ID=? and VP1_PD1_ID=? limit 1", new String[]{vendingId, skuId});
        if (!c.moveToNext()) {
            return null;
        }
        VendingProLinkData vendingProLink = new VendingProLinkData();
        vendingProLink.setVp1Id(c.getString(c.getColumnIndex("VP1_ID")));
        vendingProLink.setVp1Vd1Id(c.getString(c.getColumnIndex("VP1_VD1_ID")));
        vendingProLink.setVp1Pd1Id(c.getString(c.getColumnIndex("VP1_PD1_ID")));
        return vendingProLink;
    }

    public boolean addVendingProLink(VendingProLinkData vendingProLink) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into VendingProLink(VP1_ID,VP1_M02_ID,VP1_VD1_ID,VP1_PD1_ID,VP1_PromptValue,VP1_WarningValue,VP1_CreateUser,VP1_CreateTime,VP1_ModifyUser,VP1_ModifyTime,VP1_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?)");
        stat.bindString(1, vendingProLink.getVp1Id());
        stat.bindString(2, vendingProLink.getVp1M02Id());
        stat.bindString(3, vendingProLink.getVp1Vd1Id());
        stat.bindString(4, vendingProLink.getVp1Pd1Id());
        stat.bindLong(5, (long) vendingProLink.getVp1PromptValue().intValue());
        stat.bindLong(6, (long) vendingProLink.getVp1WarningValue().intValue());
        stat.bindString(7, vendingProLink.getVp1CreateUser());
        stat.bindString(8, vendingProLink.getVp1CreateTime());
        stat.bindString(9, vendingProLink.getVp1ModifyUser());
        stat.bindString(10, vendingProLink.getVp1ModifyTime());
        stat.bindString(11, vendingProLink.getVp1RowVersion());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchAddVendingProLink(List<VendingProLinkData> list) {
        String insertSql = "insert into VendingProLink(VP1_ID,VP1_M02_ID,VP1_VD1_ID,VP1_PD1_ID,VP1_PromptValue,VP1_WarningValue,VP1_CreateUser,VP1_CreateTime,VP1_ModifyUser,VP1_ModifyTime,VP1_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (VendingProLinkData vendingProLink : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, vendingProLink.getVp1Id());
                stat.bindString(2, vendingProLink.getVp1M02Id());
                stat.bindString(3, vendingProLink.getVp1Vd1Id());
                stat.bindString(4, vendingProLink.getVp1Pd1Id());
                stat.bindLong(5, (long) vendingProLink.getVp1PromptValue().intValue());
                stat.bindLong(6, (long) vendingProLink.getVp1WarningValue().intValue());
                stat.bindString(7, vendingProLink.getVp1CreateUser());
                stat.bindString(8, vendingProLink.getVp1CreateTime());
                stat.bindString(9, vendingProLink.getVp1ModifyUser());
                stat.bindString(10, vendingProLink.getVp1ModifyTime());
                stat.bindString(11, vendingProLink.getVp1RowVersion());
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
        String deleteSql = "DELETE FROM VendingProLink";
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
