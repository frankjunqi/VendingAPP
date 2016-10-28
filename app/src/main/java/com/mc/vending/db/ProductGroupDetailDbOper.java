package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.ProductGroupDetailData;
import com.mc.vending.data.ProductGroupWrapperData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.List;

public class ProductGroupDetailDbOper {
    public List<ProductGroupDetailData> findAll() {
        List<ProductGroupDetailData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM ProductGroupDetail", null);
        while (c.moveToNext()) {
            ProductGroupDetailData productGroupDetail = new ProductGroupDetailData();
            productGroupDetail.setPg2Id(c.getString(c.getColumnIndex("PG2_ID")));
            productGroupDetail.setPg2M02Id(c.getString(c.getColumnIndex("PG2_M02_ID")));
            productGroupDetail.setPg2Pg1Id(c.getString(c.getColumnIndex("PG2_PG1_ID")));
            productGroupDetail.setPg2Pd1Id(c.getString(c.getColumnIndex("PG2_PD1_ID")));
            productGroupDetail.setPg2GroupQty(Integer.valueOf(c.getInt(c.getColumnIndex("PG2_GroupQty"))));
            productGroupDetail.setPg2CreateUser(c.getString(c.getColumnIndex("PG2_CreateUser")));
            productGroupDetail.setPg2CreateTime(c.getString(c.getColumnIndex("PG2_CreateTime")));
            productGroupDetail.setPg2ModifyUser(c.getString(c.getColumnIndex("PG2_ModifyUser")));
            productGroupDetail.setPg2ModifyTime(c.getString(c.getColumnIndex("PG2_ModifyTime")));
            productGroupDetail.setPg2RowVersion(c.getString(c.getColumnIndex("PG2_RowVersion")));
            list.add(productGroupDetail);
        }
        return list;
    }

    public List<ProductGroupDetailData> findProductGroupDetailBySkuId(String pg1Id) {
        List<ProductGroupDetailData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT PG2_ID,PG2_PG1_ID,PG2_PD1_ID,PG2_GroupQty FROM ProductGroupDetail WHERE PG2_PG1_ID=?", new String[]{pg1Id});
        while (c.moveToNext()) {
            ProductGroupDetailData productGroupDetail = new ProductGroupDetailData();
            productGroupDetail.setPg2Id(c.getString(c.getColumnIndex("PG2_ID")));
            productGroupDetail.setPg2Pg1Id(c.getString(c.getColumnIndex("PG2_PG1_ID")));
            productGroupDetail.setPg2Pd1Id(c.getString(c.getColumnIndex("PG2_PD1_ID")));
            productGroupDetail.setPg2GroupQty(Integer.valueOf(c.getInt(c.getColumnIndex("PG2_GroupQty"))));
            list.add(productGroupDetail);
        }
        return list;
    }

    public List<ProductGroupWrapperData> findProductGroupDetail(String pg1Id) {
        List<ProductGroupWrapperData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT pg2.PG2_ID,pg2.PG2_PG1_ID,pg2.PG2_PD1_ID,pg2.PG2_GroupQty,p.PD1_Name FROM ProductGroupDetail pg2 join Product p ON pg2.PG2_PD1_ID=p.PD1_ID WHERE pg2.PG2_PG1_ID=? ORDER BY PG2_PD1_ID", new String[]{pg1Id});
        while (c.moveToNext()) {
            ProductGroupWrapperData productGroupWrapperData = new ProductGroupWrapperData();
            productGroupWrapperData.setSkuId(c.getString(c.getColumnIndex("PG2_PD1_ID")));
            productGroupWrapperData.setProductName(c.getString(c.getColumnIndex("PD1_Name")));
            productGroupWrapperData.setGroupQty(c.getInt(c.getColumnIndex("PG2_GroupQty")));
            list.add(productGroupWrapperData);
        }
        return list;
    }

    public boolean addProductGroupHead(ProductGroupDetailData productGroupDetail) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into ProductGroupDetail(PG2_ID,PG2_M02_ID,PG2_PG1_ID,PG2_PD1_ID,PG2_GroupQty,PG2_CreateUser,PG2_CreateTime,PG2_ModifyUser,PG2_ModifyTime,PG2_RowVersion)values(?,?,?,?,?,?,?,?,?,?)");
        stat.bindString(1, productGroupDetail.getPg2Id());
        stat.bindString(2, productGroupDetail.getPg2M02Id());
        stat.bindString(3, productGroupDetail.getPg2Pg1Id());
        stat.bindString(4, productGroupDetail.getPg2Pd1Id());
        stat.bindLong(5, (long) productGroupDetail.getPg2GroupQty().intValue());
        stat.bindString(6, productGroupDetail.getPg2CreateUser());
        stat.bindString(7, productGroupDetail.getPg2CreateTime());
        stat.bindString(8, productGroupDetail.getPg2ModifyUser());
        stat.bindString(9, productGroupDetail.getPg2ModifyTime());
        stat.bindString(10, productGroupDetail.getPg2RowVersion());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public void batchAddProductGroupHead(List<ProductGroupDetailData> list) {
        String insertSql = "insert into ProductGroupDetail(PG2_ID,PG2_M02_ID,PG2_PG1_ID,PG2_PD1_ID,PG2_GroupQty,PG2_CreateUser,PG2_CreateTime,PG2_ModifyUser,PG2_ModifyTime,PG2_RowVersion)values(?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (ProductGroupDetailData productGroupDetail : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, productGroupDetail.getPg2Id());
                stat.bindString(2, productGroupDetail.getPg2M02Id());
                stat.bindString(3, productGroupDetail.getPg2Pg1Id());
                stat.bindString(4, productGroupDetail.getPg2Pd1Id());
                stat.bindLong(5, (long) productGroupDetail.getPg2GroupQty().intValue());
                stat.bindString(6, productGroupDetail.getPg2CreateUser());
                stat.bindString(7, productGroupDetail.getPg2CreateTime());
                stat.bindString(8, productGroupDetail.getPg2ModifyUser());
                stat.bindString(9, productGroupDetail.getPg2ModifyTime());
                stat.bindString(10, productGroupDetail.getPg2RowVersion());
                stat.executeInsert();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (SQLException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            db.endTransaction();
            e.printStackTrace();
        }
    }

    public boolean deleteAll() {
        String deleteSql = "DELETE FROM ProductGroupDetail";
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
