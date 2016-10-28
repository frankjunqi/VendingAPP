package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.ProductData;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDbOper {
    public Map<String, String> findAllProduct() {
        Map<String, String> map = new HashMap();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT PD1_ID, PD1_Name FROM Product", null);
        while (c.moveToNext()) {
            map.put(c.getString(c.getColumnIndex("PD1_ID")), c.getString(c.getColumnIndex("PD1_Name")));
        }
        return map;
    }

    public List<ProductData> findProductByIds(String ids) {
        if (StringHelper.isEmpty(ids, true)) {
            return new ArrayList(0);
        }
        List<ProductData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM Product WHERE PD1_ID IN(" + ids + ")", null);
        while (c.moveToNext()) {
            ProductData product = new ProductData();
            product.setPd1Id(c.getString(c.getColumnIndex("PD1_ID")));
            product.setPd1M02Id(c.getString(c.getColumnIndex("PD1_M02_ID")));
            product.setPd1Code(c.getString(c.getColumnIndex("PD1_CODE")));
            product.setPd1Name(c.getString(c.getColumnIndex("PD1_Name")));
            product.setPd1Description(c.getString(c.getColumnIndex("PD1_Description")));
            product.setPd1ManufactureModel(c.getString(c.getColumnIndex("PD1_ManufactureModel")));
            product.setPd1Size(c.getString(c.getColumnIndex("PD1_Size")));
            product.setPd1Brand(c.getString(c.getColumnIndex("PD1_Brand")));
            product.setPd1Package(c.getString(c.getColumnIndex("PD1_Package")));
            product.setPd1Unit(c.getString(c.getColumnIndex("PD1_Unit")));
            product.setPd1LastImportTime(c.getString(c.getColumnIndex("PD1_LastImportTime")));
            list.add(product);
        }
        return list;
    }

    public ProductData getProductById(String id) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM Product WHERE PD1_ID=? limit 1", new String[]{id});
        if (!c.moveToNext()) {
            return null;
        }
        ProductData product = new ProductData();
        product.setPd1Id(c.getString(c.getColumnIndex("PD1_ID")));
        product.setPd1M02Id(c.getString(c.getColumnIndex("PD1_M02_ID")));
        product.setPd1Code(c.getString(c.getColumnIndex("PD1_CODE")));
        product.setPd1Name(c.getString(c.getColumnIndex("PD1_Name")));
        product.setPd1Description(c.getString(c.getColumnIndex("PD1_Description")));
        product.setPd1ManufactureModel(c.getString(c.getColumnIndex("PD1_ManufactureModel")));
        product.setPd1Size(c.getString(c.getColumnIndex("PD1_Size")));
        product.setPd1Brand(c.getString(c.getColumnIndex("PD1_Brand")));
        product.setPd1Package(c.getString(c.getColumnIndex("PD1_Package")));
        product.setPd1Unit(c.getString(c.getColumnIndex("PD1_Unit")));
        product.setPd1LastImportTime(c.getString(c.getColumnIndex("PD1_LastImportTime")));
        return product;
    }

    public boolean addProduct(ProductData product) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into Product(PD1_ID,PD1_M02_ID,PD1_CODE,PD1_Name,PD1_Description,PD1_ManufactureModel,PD1_Size,PD1_Brand,PD1_Package,PD1_Unit,PD1_LastImportTime) values(?,?,?,?,?,?,?,?,?,?,?)");
        stat.bindString(1, product.getPd1Id());
        stat.bindString(2, product.getPd1M02Id());
        stat.bindString(3, product.getPd1Code());
        stat.bindString(4, product.getPd1Name());
        stat.bindString(5, product.getPd1Description());
        stat.bindString(6, product.getPd1ManufactureModel());
        stat.bindString(7, product.getPd1Size());
        stat.bindString(8, product.getPd1Brand());
        stat.bindString(9, product.getPd1Package());
        stat.bindString(10, product.getPd1Unit());
        stat.bindString(11, product.getPd1LastImportTime());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchAddProduct(List<ProductData> list) {
        String insertSql = "insert into Product(PD1_ID,PD1_M02_ID,PD1_CODE,PD1_Name,PD1_Description,PD1_ManufactureModel,PD1_Size,PD1_Brand,PD1_Package,PD1_Unit,PD1_LastImportTime)values(?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (ProductData product : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, product.getPd1Id());
                stat.bindString(2, product.getPd1M02Id());
                stat.bindString(3, product.getPd1Code());
                stat.bindString(4, product.getPd1Name());
                stat.bindString(5, product.getPd1Description());
                stat.bindString(6, product.getPd1ManufactureModel());
                stat.bindString(7, product.getPd1Size());
                stat.bindString(8, product.getPd1Brand());
                stat.bindString(9, product.getPd1Package());
                stat.bindString(10, product.getPd1Unit());
                stat.bindString(11, product.getPd1LastImportTime());
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

    public boolean batchUpdateProduct(List<ProductData> list) {
        String updateSql = "UPDATE Product SET PD1_M02_ID=?,PD1_CODE=?,PD1_Name=?,PD1_Description=?,PD1_ManufactureModel=?,PD1_Size=?,PD1_Brand=?,PD1_Package=?,PD1_Unit=?,PD1_LastImportTime=? WHERE PD1_ID=?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (ProductData product : list) {
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindString(1, product.getPd1M02Id());
                stat.bindString(2, product.getPd1Code());
                stat.bindString(3, product.getPd1Name());
                stat.bindString(4, product.getPd1Description());
                stat.bindString(5, product.getPd1ManufactureModel());
                stat.bindString(6, product.getPd1Size());
                stat.bindString(7, product.getPd1Brand());
                stat.bindString(8, product.getPd1Package());
                stat.bindString(9, product.getPd1Unit());
                stat.bindString(10, product.getPd1LastImportTime());
                stat.bindString(11, product.getPd1Id());
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
        String deleteSql = "DELETE FROM Product";
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
