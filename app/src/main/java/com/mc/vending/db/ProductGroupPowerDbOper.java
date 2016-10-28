package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.ProductGroupPowerData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.List;

public class ProductGroupPowerDbOper {
    public List<ProductGroupPowerData> findAll() {
        List<ProductGroupPowerData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM productGroupPower", null);
        while (c.moveToNext()) {
            ProductGroupPowerData productGroupPower = new ProductGroupPowerData();
            productGroupPower.setPp1Id(c.getString(c.getColumnIndex("PP1_ID")));
            productGroupPower.setPp1M02Id(c.getString(c.getColumnIndex("PP1_M02_ID")));
            productGroupPower.setPp1Cu1Id(c.getString(c.getColumnIndex("PP1_CU1_ID")));
            productGroupPower.setPp1Pg1Id(c.getString(c.getColumnIndex("PP1_PG1_ID")));
            productGroupPower.setPp1Cd1Id(c.getString(c.getColumnIndex("PP1_CD1_ID")));
            productGroupPower.setPp1Power(c.getString(c.getColumnIndex("PP1_Power")));
            productGroupPower.setPp1Period(c.getString(c.getColumnIndex("PP1_Period")));
            productGroupPower.setPp1IntervalStart(c.getString(c.getColumnIndex("PP1_IntervalStart")));
            productGroupPower.setPp1IntervalFinish(c.getString(c.getColumnIndex("PP1_IntervalFinish")));
            productGroupPower.setPp1StartDate(c.getString(c.getColumnIndex("PP1_StartDate")));
            productGroupPower.setPp1PeriodNum(Integer.valueOf(c.getInt(c.getColumnIndex("PP1_PeriodNum"))));
            productGroupPower.setPp1CreateUser(c.getString(c.getColumnIndex("PP1_CreateUser")));
            productGroupPower.setPp1CreateTime(c.getString(c.getColumnIndex("PP1_CreateTime")));
            productGroupPower.setPp1ModifyUser(c.getString(c.getColumnIndex("PP1_ModifyUser")));
            productGroupPower.setPp1ModifyTime(c.getString(c.getColumnIndex("PP1_ModifyTime")));
            productGroupPower.setPp1RowVersion(c.getString(c.getColumnIndex("PP1_RowVersion")));
            list.add(productGroupPower);
        }
        return list;
    }

    public ProductGroupPowerData getProductGroupPower(String cusId, String pg1Id, String cardId) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM productGroupPower WHERE PP1_CU1_ID=? and PP1_PG1_ID=? and PP1_CD1_ID=? limit 1", new String[]{cusId, pg1Id, cardId});
        if (!c.moveToNext()) {
            return null;
        }
        ProductGroupPowerData productGroupPower = new ProductGroupPowerData();
        productGroupPower.setPp1Id(c.getString(c.getColumnIndex("PP1_ID")));
        productGroupPower.setPp1M02Id(c.getString(c.getColumnIndex("PP1_M02_ID")));
        productGroupPower.setPp1Cu1Id(c.getString(c.getColumnIndex("PP1_CU1_ID")));
        productGroupPower.setPp1Pg1Id(c.getString(c.getColumnIndex("PP1_PG1_ID")));
        productGroupPower.setPp1Cd1Id(c.getString(c.getColumnIndex("PP1_CD1_ID")));
        productGroupPower.setPp1Power(c.getString(c.getColumnIndex("PP1_Power")));
        productGroupPower.setPp1Period(c.getString(c.getColumnIndex("PP1_Period")));
        productGroupPower.setPp1IntervalStart(c.getString(c.getColumnIndex("PP1_IntervalStart")));
        productGroupPower.setPp1IntervalFinish(c.getString(c.getColumnIndex("PP1_IntervalFinish")));
        productGroupPower.setPp1StartDate(c.getString(c.getColumnIndex("PP1_StartDate")));
        productGroupPower.setPp1PeriodNum(Integer.valueOf(c.getInt(c.getColumnIndex("PP1_PeriodNum"))));
        productGroupPower.setPp1CreateUser(c.getString(c.getColumnIndex("PP1_CreateUser")));
        productGroupPower.setPp1CreateTime(c.getString(c.getColumnIndex("PP1_CreateTime")));
        productGroupPower.setPp1ModifyUser(c.getString(c.getColumnIndex("PP1_ModifyUser")));
        productGroupPower.setPp1ModifyTime(c.getString(c.getColumnIndex("PP1_ModifyTime")));
        productGroupPower.setPp1RowVersion(c.getString(c.getColumnIndex("PP1_RowVersion")));
        return productGroupPower;
    }

    public boolean addProductGroupPower(ProductGroupPowerData productGroupPower) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into ProductGroupPower(PP1_ID,PP1_M02_ID,PP1_CU1_ID,PP1_PG1_ID,PP1_CD1_ID,PP1_Power,PP1_Period,PP1_IntervalStart,PP1_IntervalFinish,PP1_StartDate,PP1_PeriodNum,PP1_CreateUser,PP1_CreateTime,PP1_ModifyUser,PP1_ModifyTime,PP1_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        stat.bindString(1, productGroupPower.getPp1Id());
        stat.bindString(2, productGroupPower.getPp1M02Id());
        stat.bindString(3, productGroupPower.getPp1Cu1Id());
        stat.bindString(4, productGroupPower.getPp1Pg1Id());
        stat.bindString(5, productGroupPower.getPp1Cd1Id());
        stat.bindString(6, productGroupPower.getPp1Power());
        stat.bindString(7, productGroupPower.getPp1Period());
        stat.bindString(8, productGroupPower.getPp1IntervalStart());
        stat.bindString(9, productGroupPower.getPp1IntervalFinish());
        stat.bindString(10, productGroupPower.getPp1StartDate());
        stat.bindLong(11, (long) productGroupPower.getPp1PeriodNum().intValue());
        stat.bindString(12, productGroupPower.getPp1CreateUser());
        stat.bindString(13, productGroupPower.getPp1CreateTime());
        stat.bindString(14, productGroupPower.getPp1ModifyUser());
        stat.bindString(15, productGroupPower.getPp1ModifyTime());
        stat.bindString(16, productGroupPower.getPp1RowVersion());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchAddProductGroupPower(List<ProductGroupPowerData> list) {
        String insertSql = "insert into ProductGroupPower(PP1_ID,PP1_M02_ID,PP1_CU1_ID,PP1_PG1_ID,PP1_CD1_ID,PP1_Power,PP1_Period,PP1_IntervalStart,PP1_IntervalFinish,PP1_StartDate,PP1_PeriodNum,PP1_CreateUser,PP1_CreateTime,PP1_ModifyUser,PP1_ModifyTime,PP1_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (ProductGroupPowerData productGroupPower : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, productGroupPower.getPp1Id());
                stat.bindString(2, productGroupPower.getPp1M02Id());
                stat.bindString(3, productGroupPower.getPp1Cu1Id());
                stat.bindString(4, productGroupPower.getPp1Pg1Id());
                stat.bindString(5, productGroupPower.getPp1Cd1Id());
                stat.bindString(6, productGroupPower.getPp1Power());
                stat.bindString(7, productGroupPower.getPp1Period());
                stat.bindString(8, productGroupPower.getPp1IntervalStart());
                stat.bindString(9, productGroupPower.getPp1IntervalFinish());
                stat.bindString(10, productGroupPower.getPp1StartDate());
                stat.bindLong(11, (long) productGroupPower.getPp1PeriodNum().intValue());
                stat.bindString(12, productGroupPower.getPp1CreateUser());
                stat.bindString(13, productGroupPower.getPp1CreateTime());
                stat.bindString(14, productGroupPower.getPp1ModifyUser());
                stat.bindString(15, productGroupPower.getPp1ModifyTime());
                stat.bindString(16, productGroupPower.getPp1RowVersion());
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
        String deleteSql = "DELETE FROM ProductGroupPower";
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
