package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.ProductCardPowerData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.List;

public class ProductCardPowerDbOper {
    String insertSql = "insert into ProductCardPower (PC1_ID,PC1_VD1_ID,PC1_M02_ID,PC1_CU1_ID,PC1_CD1_ID,PC1_VP1_ID,PC1_Power,PC1_OnceQty,PC1_Period,PC1_IntervalStart,PC1_IntervalFinish,PC1_StartDate,PC1_PeriodQty,CreateUser,CreateTime,ModifyUser,ModifyTime,RowVersion)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public List<ProductCardPowerData> findAll() {
        List<ProductCardPowerData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM ProductCardPower", null);
        while (c.moveToNext()) {
            ProductCardPowerData card = new ProductCardPowerData();
            card.setPc1ID(c.getString(c.getColumnIndex("PC1_ID")));
            card.setPc1M02_ID(c.getString(c.getColumnIndex("PC1_M02_ID")));
            card.setPc1VD1_ID(c.getString(c.getColumnIndex("PC1_VD1_ID")));
            card.setPc1CU1_ID(c.getString(c.getColumnIndex("PC1_CU1_ID")));
            card.setPc1CD1_ID(c.getString(c.getColumnIndex("PC1_CD1_ID")));
            card.setPc1VP1_ID(c.getString(c.getColumnIndex("PC1_VP1_ID")));
            card.setPc1Power(c.getString(c.getColumnIndex("PC1_Power")));
            card.setPc1OnceQty(c.getString(c.getColumnIndex("PC1_OnceQty")));
            card.setPc1Period(c.getString(c.getColumnIndex("PC1_Period")));
            card.setPc1IntervalStart(c.getString(c.getColumnIndex("PC1_IntervalStart")));
            card.setPc1IntervalFinish(c.getString(c.getColumnIndex("PC1_IntervalFinish")));
            card.setPc1StartDate(c.getString(c.getColumnIndex("PC1_StartDate")));
            card.setPc1PeriodQty(c.getString(c.getColumnIndex("PC1_PeriodQty")));
            card.setCreateUser(c.getString(c.getColumnIndex("CreateUser")));
            card.setCreateTime(c.getString(c.getColumnIndex("CreateTime")));
            card.setModifyUser(c.getString(c.getColumnIndex("ModifyUser")));
            card.setModifyTime(c.getString(c.getColumnIndex("ModifyTime")));
            card.setRowVersion(c.getString(c.getColumnIndex("RowVersion")));
            list.add(card);
        }
        return list;
    }

    public ProductCardPowerData getProductCardPowerBySerialNo(String cardId) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM ProductCardPower WHERE PC1_CD1_ID=? limit 1", new String[]{cardId});
        if (!c.moveToNext()) {
            return null;
        }
        ProductCardPowerData card = new ProductCardPowerData();
        card.setPc1ID(c.getString(c.getColumnIndex("PC1_ID")));
        card.setPc1M02_ID(c.getString(c.getColumnIndex("PC1_M02_ID")));
        card.setPc1VD1_ID(c.getString(c.getColumnIndex("PC1_VD1_ID")));
        card.setPc1CU1_ID(c.getString(c.getColumnIndex("PC1_CU1_ID")));
        card.setPc1CD1_ID(c.getString(c.getColumnIndex("PC1_CD1_ID")));
        card.setPc1VP1_ID(c.getString(c.getColumnIndex("PC1_VP1_ID")));
        card.setPc1Power(c.getString(c.getColumnIndex("PC1_Power")));
        card.setPc1OnceQty(c.getString(c.getColumnIndex("PC1_OnceQty")));
        card.setPc1Period(c.getString(c.getColumnIndex("PC1_Period")));
        card.setPc1IntervalStart(c.getString(c.getColumnIndex("PC1_IntervalStart")));
        card.setPc1IntervalFinish(c.getString(c.getColumnIndex("PC1_IntervalFinish")));
        card.setPc1StartDate(c.getString(c.getColumnIndex("PC1_StartDate")));
        card.setPc1PeriodQty(c.getString(c.getColumnIndex("PC1_PeriodQty")));
        card.setCreateUser(c.getString(c.getColumnIndex("CreateUser")));
        card.setCreateTime(c.getString(c.getColumnIndex("CreateTime")));
        card.setModifyUser(c.getString(c.getColumnIndex("ModifyUser")));
        card.setModifyTime(c.getString(c.getColumnIndex("ModifyTime")));
        card.setRowVersion(c.getString(c.getColumnIndex("RowVersion")));
        return card;
    }

    public boolean addProductCardPower(ProductCardPowerData card) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement(this.insertSql);
        int i = 1 + 1;
        stat.bindString(1, card.getPc1ID());
        int i2 = i + 1;
        stat.bindString(i, card.getPc1VD1_ID());
        i = i2 + 1;
        stat.bindString(i2, card.getPc1M02_ID());
        i2 = i + 1;
        stat.bindString(i, card.getPc1CU1_ID());
        i = i2 + 1;
        stat.bindString(i2, card.getPc1CD1_ID());
        i2 = i + 1;
        stat.bindString(i, card.getPc1VP1_ID());
        i = i2 + 1;
        stat.bindString(i2, card.getPc1Power());
        i2 = i + 1;
        stat.bindString(i, card.getPc1OnceQty());
        i = i2 + 1;
        stat.bindString(i2, card.getPc1Period());
        i2 = i + 1;
        stat.bindString(i, card.getPc1IntervalStart());
        i = i2 + 1;
        stat.bindString(i2, card.getPc1IntervalFinish());
        i2 = i + 1;
        stat.bindString(i, card.getPc1StartDate());
        i = i2 + 1;
        stat.bindString(i2, card.getPc1PeriodQty());
        i2 = i + 1;
        stat.bindString(i, card.getCreateUser());
        i = i2 + 1;
        stat.bindString(i2, card.getCreateTime());
        i2 = i + 1;
        stat.bindString(i, card.getModifyUser());
        i = i2 + 1;
        stat.bindString(i2, card.getModifyTime());
        i2 = i + 1;
        stat.bindString(i, card.getRowVersion());
        return stat.executeInsert() > 0;
    }

    public boolean batchAddProductCardPower(List<ProductCardPowerData> list) {
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (ProductCardPowerData card : list) {
                SQLiteStatement stat = db.compileStatement(this.insertSql);
                int i = 1 + 1;
                stat.bindString(1, card.getPc1ID());
                int i2 = i + 1;
                stat.bindString(i, card.getPc1VD1_ID());
                i = i2 + 1;
                stat.bindString(i2, card.getPc1M02_ID());
                i2 = i + 1;
                stat.bindString(i, card.getPc1CU1_ID());
                i = i2 + 1;
                stat.bindString(i2, card.getPc1CD1_ID());
                i2 = i + 1;
                stat.bindString(i, card.getPc1VP1_ID());
                i = i2 + 1;
                stat.bindString(i2, card.getPc1Power());
                i2 = i + 1;
                stat.bindString(i, card.getPc1OnceQty());
                i = i2 + 1;
                stat.bindString(i2, card.getPc1Period());
                i2 = i + 1;
                stat.bindString(i, card.getPc1IntervalStart());
                i = i2 + 1;
                stat.bindString(i2, card.getPc1IntervalFinish());
                i2 = i + 1;
                stat.bindString(i, card.getPc1StartDate());
                i = i2 + 1;
                stat.bindString(i2, card.getPc1PeriodQty());
                i2 = i + 1;
                stat.bindString(i, card.getCreateUser());
                i = i2 + 1;
                stat.bindString(i2, card.getCreateTime());
                i2 = i + 1;
                stat.bindString(i, card.getModifyUser());
                i = i2 + 1;
                stat.bindString(i2, card.getModifyTime());
                i2 = i + 1;
                stat.bindString(i, card.getRowVersion());
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
                db.compileStatement("DELETE FROM ProductCardPower where PC1_CD1_ID='" + string.split(",")[0] + "' and PC1_VD1_ID='" + string.split(",")[1] + "'").executeInsert();
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

    public ProductCardPowerData getVendingProLinkByVidAndSkuId(String cardId, String vp1Id) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT PC1_ID,PC1_CU1_ID,PC1_CD1_ID,PC1_VP1_ID,PC1_Power,PC1_OnceQty,PC1_Period,PC1_IntervalStart,PC1_IntervalFinish,PC1_StartDate,PC1_PeriodQty FROM ProductCardPower WHERE PC1_CD1_ID=? and PC1_VP1_ID=? limit 1", new String[]{cardId, vp1Id});
        if (!c.moveToNext()) {
            return null;
        }
        ProductCardPowerData data = new ProductCardPowerData();
        data.setPc1ID(c.getString(c.getColumnIndex("PC1_ID")));
        data.setPc1CU1_ID(c.getString(c.getColumnIndex("PC1_CU1_ID")));
        data.setPc1CD1_ID(c.getString(c.getColumnIndex("PC1_CD1_ID")));
        data.setPc1VP1_ID(c.getString(c.getColumnIndex("PC1_VP1_ID")));
        data.setPc1Power(c.getString(c.getColumnIndex("PC1_Power")));
        data.setPc1OnceQty(c.getString(c.getColumnIndex("PC1_OnceQty")));
        data.setPc1Period(c.getString(c.getColumnIndex("PC1_Period")));
        data.setPc1IntervalStart(c.getString(c.getColumnIndex("PC1_IntervalStart")));
        data.setPc1IntervalFinish(c.getString(c.getColumnIndex("PC1_IntervalFinish")));
        data.setPc1StartDate(c.getString(c.getColumnIndex("PC1_StartDate")));
        data.setPc1PeriodQty(c.getString(c.getColumnIndex("PC1_PeriodQty")));
        return data;
    }

    public List<String> getVendingProLinkByCid(String cardId) {
        List<String> data = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT PC1_VP1_ID FROM ProductCardPower WHERE PC1_CD1_ID=?", new String[]{cardId});
        while (c.moveToNext()) {
            data.add(c.getString(c.getColumnIndex("PC1_VP1_ID")));
        }
        return data;
    }
}
