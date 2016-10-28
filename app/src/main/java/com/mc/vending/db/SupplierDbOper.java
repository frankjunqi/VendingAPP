package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.SupplierData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplierDbOper {
    public List<SupplierData> findAll() {
        List<SupplierData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM Supplier", null);
        while (c.moveToNext()) {
            SupplierData supplier = new SupplierData();
            supplier.setSp1Id(c.getString(c.getColumnIndex("SP1_ID")));
            supplier.setSp1M02Id(c.getString(c.getColumnIndex("SP1_M02_ID")));
            supplier.setSp1Code(c.getString(c.getColumnIndex("SP1_CODE")));
            supplier.setSp1Name(c.getString(c.getColumnIndex("SP1_Name")));
            supplier.setSp1CreateUser(c.getString(c.getColumnIndex("SP1_CreateUser")));
            supplier.setSp1CreateTime(c.getString(c.getColumnIndex("SP1_CreateTime")));
            supplier.setSp1ModifyUser(c.getString(c.getColumnIndex("SP1_ModifyUser")));
            supplier.setSp1ModifyTime(c.getString(c.getColumnIndex("SP1_ModifyTime")));
            supplier.setSp1RowVersion(c.getString(c.getColumnIndex("SP1_RowVersion")));
            list.add(supplier);
        }
        return list;
    }

    public Map<String, String> findAllMap() {
        Map<String, String> map = new HashMap();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT SP1_ID,SP1_CODE FROM Supplier", null);
        while (c.moveToNext()) {
            map.put(c.getString(c.getColumnIndex("SP1_ID")), c.getString(c.getColumnIndex("SP1_CODE")));
        }
        return map;
    }

    public SupplierData getSupplierBySpId(String spId) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT SP1_ID,SP1_CODE,SP1_Name FROM Supplier WHERE SP1_ID=? limit 1", new String[]{spId});
        if (!c.moveToNext()) {
            return null;
        }
        SupplierData supplier = new SupplierData();
        supplier.setSp1Id(c.getString(c.getColumnIndex("SP1_ID")));
        supplier.setSp1Code(c.getString(c.getColumnIndex("SP1_CODE")));
        supplier.setSp1Name(c.getString(c.getColumnIndex("SP1_Name")));
        return supplier;
    }

    public boolean addSupplier(SupplierData supplier) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into Supplier(SP1_ID,SP1_M02_ID,SP1_CODE,SP1_Name,SP1_CreateUser,SP1_CreateTime,SP1_ModifyUser,SP1_ModifyTime,SP1_RowVersion)values(?,?,?,?,?,?,?,?,?)");
        stat.bindString(1, supplier.getSp1Id());
        stat.bindString(2, supplier.getSp1M02Id());
        stat.bindString(3, supplier.getSp1Code());
        stat.bindString(4, supplier.getSp1Name());
        stat.bindString(5, supplier.getSp1CreateUser());
        stat.bindString(6, supplier.getSp1CreateTime());
        stat.bindString(7, supplier.getSp1ModifyUser());
        stat.bindString(8, supplier.getSp1ModifyTime());
        stat.bindString(9, supplier.getSp1RowVersion());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchAddSupplier(List<SupplierData> list) {
        String insertSql = "insert into Supplier(SP1_ID,SP1_M02_ID,SP1_CODE,SP1_Name,SP1_CreateUser,SP1_CreateTime,SP1_ModifyUser,SP1_ModifyTime,SP1_RowVersion)values(?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (SupplierData supplier : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, supplier.getSp1Id());
                stat.bindString(2, supplier.getSp1M02Id());
                stat.bindString(3, supplier.getSp1Code());
                stat.bindString(4, supplier.getSp1Name());
                stat.bindString(5, supplier.getSp1CreateUser());
                stat.bindString(6, supplier.getSp1CreateTime());
                stat.bindString(7, supplier.getSp1ModifyUser());
                stat.bindString(8, supplier.getSp1ModifyTime());
                stat.bindString(9, supplier.getSp1RowVersion());
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

    public boolean batchUpdateSupplier(List<SupplierData> list) {
        String insertSql = "UPDATE Supplier SET SP1_M02_ID=?,SP1_CODE=?,SP1_Name=?,SP1_CreateUser=?,SP1_CreateTime=?,SP1_ModifyUser=?,SP1_ModifyTime=?,SP1_RowVersion=? WHERE SP1_ID=?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (SupplierData supplier : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, supplier.getSp1M02Id());
                stat.bindString(2, supplier.getSp1Code());
                stat.bindString(3, supplier.getSp1Name());
                stat.bindString(4, supplier.getSp1CreateUser());
                stat.bindString(5, supplier.getSp1CreateTime());
                stat.bindString(6, supplier.getSp1ModifyUser());
                stat.bindString(7, supplier.getSp1ModifyTime());
                stat.bindString(8, supplier.getSp1RowVersion());
                stat.bindString(9, supplier.getSp1Id());
                stat.executeUpdateDelete();
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

    public boolean deleteAll() {
        String deleteSql = "DELETE FROM Supplier";
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
