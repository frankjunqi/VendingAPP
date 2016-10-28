package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.ProductPictureData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductPictureDbOper {
    public List<ProductPictureData> findAll() {
        List<ProductPictureData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM ProductPicture", null);
        while (c.moveToNext()) {
            ProductPictureData pp = new ProductPictureData();
            pp.setPp1Id(c.getString(c.getColumnIndex("PP1_ID")));
            pp.setPp1M02Id(c.getString(c.getColumnIndex("PP1_M02_ID")));
            pp.setPp1Pd1Id(c.getString(c.getColumnIndex("PP1_PD1_ID")));
            pp.setPp1FilePath(c.getString(c.getColumnIndex("PP1_FilePath")));
            pp.setPp1CreateUser(c.getString(c.getColumnIndex("PP1_CreateUser")));
            pp.setPp1CreateTime(c.getString(c.getColumnIndex("PP1_CreateTime")));
            pp.setPp1ModifyUser(c.getString(c.getColumnIndex("PP1_ModifyUser")));
            pp.setPp1ModifyTime(c.getString(c.getColumnIndex("PP1_ModifyTime")));
            pp.setPp1RowVersion(c.getString(c.getColumnIndex("PP1_RowVersion")));
            list.add(pp);
        }
        return list;
    }

    public Map<String, String> findAllMap() {
        Map<String, String> map = new HashMap();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT PP1_ID,PP1_PD1_ID FROM ProductPicture", null);
        while (c.moveToNext()) {
            map.put(c.getString(c.getColumnIndex("PP1_ID")), c.getString(c.getColumnIndex("PP1_PD1_ID")));
        }
        return map;
    }

    public ProductPictureData getProductPictureBySku(String sku) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT PP1_ID,PP1_PD1_ID,PP1_FilePath FROM ProductPicture WHERE PP1_PD1_ID=? limit 1", new String[]{sku});
        if (!c.moveToNext()) {
            return null;
        }
        ProductPictureData pp = new ProductPictureData();
        pp.setPp1Id(c.getString(c.getColumnIndex("PP1_ID")));
        pp.setPp1Pd1Id(c.getString(c.getColumnIndex("PP1_PD1_ID")));
        pp.setPp1FilePath(c.getString(c.getColumnIndex("PP1_FilePath")));
        return pp;
    }

    public boolean addProductPicture(ProductPictureData productPicture) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into ProductPicture(PP1_ID,PP1_M02_ID,PP1_PD1_ID,PP1_FilePath,PP1_CreateUser,PP1_CreateTime,PP1_ModifyUser,PP1_ModifyTime,PP1_RowVersion)values(?,?,?,?,?,?,?,?,?)");
        stat.bindString(1, productPicture.getPp1Id());
        stat.bindString(2, productPicture.getPp1M02Id());
        stat.bindString(3, productPicture.getPp1Pd1Id());
        stat.bindString(4, productPicture.getPp1FilePath());
        stat.bindString(5, productPicture.getPp1CreateUser());
        stat.bindString(6, productPicture.getPp1CreateTime());
        stat.bindString(7, productPicture.getPp1ModifyUser());
        stat.bindString(8, productPicture.getPp1ModifyTime());
        stat.bindString(9, productPicture.getPp1RowVersion());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchAddProductPicture(List<ProductPictureData> list) {
        String insertSql = "insert into ProductPicture(PP1_ID,PP1_M02_ID,PP1_PD1_ID,PP1_FilePath,PP1_CreateUser,PP1_CreateTime,PP1_ModifyUser,PP1_ModifyTime,PP1_RowVersion)values(?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (ProductPictureData productPicture : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, productPicture.getPp1Id());
                stat.bindString(2, productPicture.getPp1M02Id());
                stat.bindString(3, productPicture.getPp1Pd1Id());
                stat.bindString(4, productPicture.getPp1FilePath());
                stat.bindString(5, productPicture.getPp1CreateUser());
                stat.bindString(6, productPicture.getPp1CreateTime());
                stat.bindString(7, productPicture.getPp1ModifyUser());
                stat.bindString(8, productPicture.getPp1ModifyTime());
                stat.bindString(9, productPicture.getPp1RowVersion());
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

    public boolean batchUpdateProductPicture(List<ProductPictureData> list) {
        String updateSql = "UPDATE ProductPicture SET PP1_M02_ID=?,PP1_PD1_ID=?,PP1_FilePath=?,PP1_CreateUser=?,PP1_CreateTime=?,PP1_ModifyUser=?,PP1_ModifyTime=?,PP1_RowVersion=? WHERE PP1_ID=?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (ProductPictureData productPicture : list) {
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindString(1, productPicture.getPp1M02Id());
                stat.bindString(2, productPicture.getPp1Pd1Id());
                stat.bindString(3, productPicture.getPp1FilePath());
                stat.bindString(4, productPicture.getPp1CreateUser());
                stat.bindString(5, productPicture.getPp1CreateTime());
                stat.bindString(6, productPicture.getPp1ModifyUser());
                stat.bindString(7, productPicture.getPp1ModifyTime());
                stat.bindString(8, productPicture.getPp1RowVersion());
                stat.bindString(9, productPicture.getPp1Id());
                stat.executeUpdateDelete();
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
        String deleteSql = "DELETE FROM ProductPicture";
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
