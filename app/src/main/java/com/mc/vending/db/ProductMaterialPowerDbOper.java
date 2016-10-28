package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.ProductMaterialPowerData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.List;

public class ProductMaterialPowerDbOper {
    public List<ProductMaterialPowerData> findAll() {
        List<ProductMaterialPowerData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM ProductMaterialPower", null);
        while (c.moveToNext()) {
            ProductMaterialPowerData productMaterialPower = new ProductMaterialPowerData();
            productMaterialPower.setPm1Id(c.getString(c.getColumnIndex("PM1_ID")));
            productMaterialPower.setPm1M02Id(c.getString(c.getColumnIndex("PM1_M02_ID")));
            productMaterialPower.setPm1Cu1Id(c.getString(c.getColumnIndex("PM1_CU1_ID")));
            productMaterialPower.setPm1Vc2Id(c.getString(c.getColumnIndex("PM1_VC2_ID")));
            productMaterialPower.setPm1Vp1Id(c.getString(c.getColumnIndex("PM1_VP1_ID")));
            productMaterialPower.setPm1Power(c.getString(c.getColumnIndex("PM1_Power")));
            productMaterialPower.setPm1OnceQty(Integer.valueOf(c.getInt(c.getColumnIndex("PM1_OnceQty"))));
            productMaterialPower.setPm1Period(c.getString(c.getColumnIndex("PM1_Period")));
            productMaterialPower.setPm1IntervalStart(c.getString(c.getColumnIndex("PM1_IntervalStart")));
            productMaterialPower.setPm1IntervalFinish(c.getString(c.getColumnIndex("PM1_IntervalFinish")));
            productMaterialPower.setPm1StartDate(c.getString(c.getColumnIndex("PM1_StartDate")));
            productMaterialPower.setPm1PeriodQty(Integer.valueOf(c.getInt(c.getColumnIndex("PM1_PeriodQty"))));
            productMaterialPower.setPm1CreateUser(c.getString(c.getColumnIndex("PM1_CreateUser")));
            productMaterialPower.setPm1CreateTime(c.getString(c.getColumnIndex("PM1_CreateTime")));
            productMaterialPower.setPm1ModifyUser(c.getString(c.getColumnIndex("PM1_ModifyUser")));
            productMaterialPower.setPm1ModifyTime(c.getString(c.getColumnIndex("PM1_ModifyTime")));
            productMaterialPower.setPm1RowVersion(c.getString(c.getColumnIndex("PM1_RowVersion")));
            list.add(productMaterialPower);
        }
        return list;
    }

    public ProductMaterialPowerData getVendingProLinkByVidAndSkuId(String cusId, String vc2Id, String vp1Id) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT PM1_ID,PM1_CU1_ID,PM1_VC2_ID,PM1_VP1_ID,PM1_Power,PM1_OnceQty,PM1_Period,PM1_IntervalStart,PM1_IntervalFinish,PM1_StartDate,PM1_PeriodQty FROM ProductMaterialPower WHERE PM1_CU1_ID=? and PM1_VC2_ID=? and PM1_VP1_ID=? limit 1", new String[]{cusId, vc2Id, vp1Id});
        if (!c.moveToNext()) {
            return null;
        }
        ProductMaterialPowerData productMaterialPower = new ProductMaterialPowerData();
        productMaterialPower.setPm1Id(c.getString(c.getColumnIndex("PM1_ID")));
        productMaterialPower.setPm1Cu1Id(c.getString(c.getColumnIndex("PM1_CU1_ID")));
        productMaterialPower.setPm1Vc2Id(c.getString(c.getColumnIndex("PM1_VC2_ID")));
        productMaterialPower.setPm1Vp1Id(c.getString(c.getColumnIndex("PM1_VP1_ID")));
        productMaterialPower.setPm1Power(c.getString(c.getColumnIndex("PM1_Power")));
        productMaterialPower.setPm1OnceQty(Integer.valueOf(c.getInt(c.getColumnIndex("PM1_OnceQty"))));
        productMaterialPower.setPm1Period(c.getString(c.getColumnIndex("PM1_Period")));
        productMaterialPower.setPm1IntervalStart(c.getString(c.getColumnIndex("PM1_IntervalStart")));
        productMaterialPower.setPm1IntervalFinish(c.getString(c.getColumnIndex("PM1_IntervalFinish")));
        productMaterialPower.setPm1StartDate(c.getString(c.getColumnIndex("PM1_StartDate")));
        productMaterialPower.setPm1PeriodQty(Integer.valueOf(c.getInt(c.getColumnIndex("PM1_PeriodQty"))));
        return productMaterialPower;
    }

    public List<String> findVendingProLinkByVcId(String vcId) {
        List<String> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT vp.VP1_PD1_ID VP1_PD1_ID FROM VendingProLink vp join ProductMaterialPower pm ON vp.VP1_ID=pm.PM1_VP1_ID WHERE PM1_VC2_ID=?", new String[]{vcId});
        while (c.moveToNext()) {
            list.add(c.getString(c.getColumnIndex("VP1_PD1_ID")));
        }
        return list;
    }

    public boolean addProductMaterialPower(ProductMaterialPowerData productMaterialPower) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into ProductMaterialPower(PM1_ID,PM1_M02_ID,PM1_CU1_ID,PM1_VC2_ID,PM1_VP1_ID,PM1_Power,PM1_OnceQty,PM1_Period,PM1_IntervalStart,PM1_IntervalFinish,PM1_StartDate,PM1_PeriodQty,PM1_CreateUser,PM1_CreateTime,PM1_ModifyUser,PM1_ModifyTime,PM1_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        stat.bindString(1, productMaterialPower.getPm1Id());
        stat.bindString(2, productMaterialPower.getPm1M02Id());
        stat.bindString(3, productMaterialPower.getPm1Cu1Id());
        stat.bindString(4, productMaterialPower.getPm1Vc2Id());
        stat.bindString(5, productMaterialPower.getPm1Vp1Id());
        stat.bindString(6, productMaterialPower.getPm1Power());
        stat.bindLong(7, (long) productMaterialPower.getPm1OnceQty().intValue());
        stat.bindString(8, productMaterialPower.getPm1Period());
        stat.bindString(9, productMaterialPower.getPm1IntervalStart());
        stat.bindString(10, productMaterialPower.getPm1IntervalFinish());
        stat.bindString(11, productMaterialPower.getPm1StartDate());
        stat.bindLong(12, (long) productMaterialPower.getPm1PeriodQty().intValue());
        stat.bindString(13, productMaterialPower.getPm1CreateUser());
        stat.bindString(14, productMaterialPower.getPm1CreateTime());
        stat.bindString(15, productMaterialPower.getPm1ModifyUser());
        stat.bindString(16, productMaterialPower.getPm1ModifyTime());
        stat.bindString(17, productMaterialPower.getPm1RowVersion());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchAddProductMaterialPower(List<ProductMaterialPowerData> list) {
        String insertSql = "insert into ProductMaterialPower(PM1_ID,PM1_M02_ID,PM1_CU1_ID,PM1_VC2_ID,PM1_VP1_ID,PM1_Power,PM1_OnceQty,PM1_Period,PM1_IntervalStart,PM1_IntervalFinish,PM1_StartDate,PM1_PeriodQty,PM1_CreateUser,PM1_CreateTime,PM1_ModifyUser,PM1_ModifyTime,PM1_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (ProductMaterialPowerData productMaterialPower : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, productMaterialPower.getPm1Id());
                stat.bindString(2, productMaterialPower.getPm1M02Id());
                stat.bindString(3, productMaterialPower.getPm1Cu1Id());
                stat.bindString(4, productMaterialPower.getPm1Vc2Id());
                stat.bindString(5, productMaterialPower.getPm1Vp1Id());
                stat.bindString(6, productMaterialPower.getPm1Power());
                stat.bindLong(7, (long) productMaterialPower.getPm1OnceQty().intValue());
                stat.bindString(8, productMaterialPower.getPm1Period());
                stat.bindString(9, productMaterialPower.getPm1IntervalStart());
                stat.bindString(10, productMaterialPower.getPm1IntervalFinish());
                stat.bindString(11, productMaterialPower.getPm1StartDate());
                stat.bindLong(12, (long) productMaterialPower.getPm1PeriodQty().intValue());
                stat.bindString(13, productMaterialPower.getPm1CreateUser());
                stat.bindString(14, productMaterialPower.getPm1CreateTime());
                stat.bindString(15, productMaterialPower.getPm1ModifyUser());
                stat.bindString(16, productMaterialPower.getPm1ModifyTime());
                stat.bindString(17, productMaterialPower.getPm1RowVersion());
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
        String deleteSql = "DELETE FROM ProductMaterialPower";
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
