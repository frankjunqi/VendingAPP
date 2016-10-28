package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StockTransactionDbOper {
    public int getTransQtyCount(String billType, String vendingId, String skuId, String vendingChnCode, String startDate) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT TS1_TransQty FROM StockTransaction WHERE TS1_BillType=? and TS1_VD1_ID=? and TS1_PD1_ID=? and TS1_VC1_CODE=? and TS1_CreateTime>=?", new String[]{billType, vendingId, skuId, vendingChnCode, startDate});
        int transQtyCount = 0;
        while (c.moveToNext()) {
            transQtyCount += c.getInt(c.getColumnIndex("TS1_TransQty"));
        }
        return transQtyCount;
    }

    public void clearStockTransaction(String startDate) {
        String updateSql = "delete from StockTransaction where TS1_UploadStatus='1' and substr(TS1_createtime,0,11) <= ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        Date curr = new Date();
        try {
            db.beginTransaction();
            SQLiteStatement stat = db.compileStatement(updateSql);
            stat.bindString(1, startDate);
            int i = stat.executeUpdateDelete();
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (SQLException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            db.endTransaction();
            e.printStackTrace();
        }
    }

    public int getTransQtyCount(String billType, String vendingId, String skuId, String vendingChnCode, String startDate, String cardId) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT TS1_TransQty FROM StockTransaction WHERE TS1_BillType=? and TS1_VD1_ID=? and TS1_PD1_ID=? and TS1_CreateTime>=? and TS1_CD1_ID =?", new String[]{billType, vendingId, skuId, startDate, cardId});
        int transQtyCount = 0;
        while (c.moveToNext()) {
            transQtyCount += c.getInt(c.getColumnIndex("TS1_TransQty"));
        }
        return transQtyCount;
    }

    public int getTransQtyCount(String billType, String vendingId, String skuId, String startDate) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT TS1_TransQty FROM StockTransaction WHERE TS1_BillType=? and TS1_VD1_ID=? and TS1_PD1_ID=? and TS1_CreateTime>=?", new String[]{billType, vendingId, skuId, startDate});
        int transQtyCount = 0;
        while (c.moveToNext()) {
            transQtyCount += c.getInt(c.getColumnIndex("TS1_TransQty"));
        }
        return transQtyCount;
    }

    public int getTransQtyCountAddCardId(String billType, String vendingId, String skuId, String startDate, String cardId) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT TS1_TransQty FROM StockTransaction WHERE TS1_BillType=? and TS1_VD1_ID=? and TS1_PD1_ID=? and TS1_CreateTime>=? and TS1_CD1_ID =?", new String[]{billType, vendingId, skuId, startDate, cardId});
        int transQtyCount = 0;
        while (c.moveToNext()) {
            transQtyCount += c.getInt(c.getColumnIndex("TS1_TransQty"));
        }
        return transQtyCount;
    }

    public StockTransactionData getVendingChnByCode(String vendingId, String vcCode, String billType) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM StockTransaction WHERE TS1_VD1_ID = ? and TS1_VC1_CODE = ? and TS1_BillType = ? ORDER by TS1_CreateTime desc limit 1", new String[]{vendingId, vcCode, billType});
        if (!c.moveToNext()) {
            return null;
        }
        StockTransactionData stockTransaction = new StockTransactionData();
        stockTransaction.setTs1Id(c.getString(c.getColumnIndex("TS1_ID")));
        stockTransaction.setTs1M02Id(c.getString(c.getColumnIndex("TS1_M02_ID")));
        stockTransaction.setTs1BillType(c.getString(c.getColumnIndex("TS1_BillType")));
        stockTransaction.setTs1BillCode(c.getString(c.getColumnIndex("TS1_BillCode")));
        stockTransaction.setTs1Cd1Id(c.getString(c.getColumnIndex("TS1_CD1_ID")));
        stockTransaction.setTs1Vd1Id(c.getString(c.getColumnIndex("TS1_VD1_ID")));
        stockTransaction.setTs1Pd1Id(c.getString(c.getColumnIndex("TS1_PD1_ID")));
        stockTransaction.setTs1Vc1Code(c.getString(c.getColumnIndex("TS1_VC1_CODE")));
        stockTransaction.setTs1TransQty(Integer.valueOf(c.getInt(c.getColumnIndex("TS1_TransQty"))));
        stockTransaction.setTs1TransType(c.getString(c.getColumnIndex("TS1_TransType")));
        stockTransaction.setTs1Sp1Code(c.getString(c.getColumnIndex("TS1_SP1_CODE")));
        stockTransaction.setTs1Sp1Name(c.getString(c.getColumnIndex("TS1_SP1_Name")));
        stockTransaction.setTs1Sp1Name(c.getString(c.getColumnIndex("TS1_UploadStatus")));
        stockTransaction.setTs1CreateUser(c.getString(c.getColumnIndex("TS1_CreateUser")));
        stockTransaction.setTs1CreateTime(c.getString(c.getColumnIndex("TS1_CreateTime")));
        stockTransaction.setTs1ModifyUser(c.getString(c.getColumnIndex("TS1_ModifyUser")));
        stockTransaction.setTs1ModifyTime(c.getString(c.getColumnIndex("TS1_ModifyTime")));
        stockTransaction.setTs1RowVersion(c.getString(c.getColumnIndex("TS1_RowVersion")));
        return stockTransaction;
    }

    public boolean addStockTransaction(StockTransactionData stockTransaction) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into StockTransaction(TS1_ID,TS1_M02_ID,TS1_BillType,TS1_BillCode,TS1_CD1_ID,TS1_VD1_ID,TS1_PD1_ID,TS1_VC1_CODE,TS1_TransQty,TS1_TransType,TS1_SP1_CODE,TS1_SP1_Name,TS1_UploadStatus,TS1_CreateUser,TS1_CreateTime,TS1_ModifyUser,TS1_ModifyTime,TS1_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        stat.bindString(1, stockTransaction.getTs1Id());
        stat.bindString(2, stockTransaction.getTs1M02Id());
        stat.bindString(3, stockTransaction.getTs1BillType());
        stat.bindString(4, stockTransaction.getTs1BillCode());
        stat.bindString(5, stockTransaction.getTs1Cd1Id());
        stat.bindString(6, stockTransaction.getTs1Vd1Id());
        stat.bindString(7, stockTransaction.getTs1Pd1Id());
        stat.bindString(8, stockTransaction.getTs1Vc1Code());
        stat.bindLong(9, (long) stockTransaction.getTs1TransQty().intValue());
        stat.bindString(10, stockTransaction.getTs1TransType());
        stat.bindString(11, stockTransaction.getTs1Sp1Code());
        stat.bindString(12, stockTransaction.getTs1Sp1Name());
        stat.bindString(13, stockTransaction.getTs1UploadStatus());
        stat.bindString(14, stockTransaction.getTs1CreateUser());
        stat.bindString(15, stockTransaction.getTs1CreateTime());
        stat.bindString(16, stockTransaction.getTs1ModifyUser());
        stat.bindString(17, stockTransaction.getTs1ModifyTime());
        stat.bindString(18, stockTransaction.getTs1RowVersion());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public Boolean batchAddStockTransaction(List<StockTransactionData> list) {
        boolean flag = false;
        String insertSql = "insert into StockTransaction(TS1_ID,TS1_M02_ID,TS1_BillType,TS1_BillCode,TS1_CD1_ID,TS1_VD1_ID,TS1_PD1_ID,TS1_VC1_CODE,TS1_TransQty,TS1_TransType,TS1_SP1_CODE,TS1_SP1_Name,TS1_UploadStatus,TS1_CreateUser,TS1_CreateTime,TS1_ModifyUser,TS1_ModifyTime,TS1_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (StockTransactionData stockTransaction : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, stockTransaction.getTs1Id());
                stat.bindString(2, stockTransaction.getTs1M02Id());
                stat.bindString(3, stockTransaction.getTs1BillType());
                stat.bindString(4, stockTransaction.getTs1BillCode());
                stat.bindString(5, stockTransaction.getTs1Cd1Id());
                stat.bindString(6, stockTransaction.getTs1Vd1Id());
                stat.bindString(7, stockTransaction.getTs1Pd1Id());
                stat.bindString(8, stockTransaction.getTs1Vc1Code());
                stat.bindLong(9, (long) stockTransaction.getTs1TransQty().intValue());
                stat.bindString(10, stockTransaction.getTs1TransType());
                stat.bindString(11, stockTransaction.getTs1Sp1Code());
                stat.bindString(12, stockTransaction.getTs1Sp1Name());
                stat.bindString(13, stockTransaction.getTs1UploadStatus());
                stat.bindString(14, stockTransaction.getTs1CreateUser());
                stat.bindString(15, stockTransaction.getTs1CreateTime());
                stat.bindString(16, stockTransaction.getTs1ModifyUser());
                stat.bindString(17, stockTransaction.getTs1ModifyTime());
                stat.bindString(18, stockTransaction.getTs1RowVersion());
                stat.executeInsert();
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

    public List<StockTransactionData> findStockTransactionDataToUpload() {
        List<StockTransactionData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM StockTransaction WHERE TS1_UploadStatus = ? limit 50", new String[]{"0"});
        while (c.moveToNext()) {
            StockTransactionData stockTransaction = new StockTransactionData();
            stockTransaction.setTs1Id(c.getString(c.getColumnIndex("TS1_ID")));
            stockTransaction.setTs1M02Id(c.getString(c.getColumnIndex("TS1_M02_ID")));
            stockTransaction.setTs1BillType(c.getString(c.getColumnIndex("TS1_BillType")));
            stockTransaction.setTs1BillCode(c.getString(c.getColumnIndex("TS1_BillCode")));
            stockTransaction.setTs1Cd1Id(c.getString(c.getColumnIndex("TS1_CD1_ID")));
            stockTransaction.setTs1Vd1Id(c.getString(c.getColumnIndex("TS1_VD1_ID")));
            stockTransaction.setTs1Pd1Id(c.getString(c.getColumnIndex("TS1_PD1_ID")));
            stockTransaction.setTs1Vc1Code(c.getString(c.getColumnIndex("TS1_VC1_CODE")));
            stockTransaction.setTs1TransQty(Integer.valueOf(c.getInt(c.getColumnIndex("TS1_TransQty"))));
            stockTransaction.setTs1TransType(c.getString(c.getColumnIndex("TS1_TransType")));
            stockTransaction.setTs1Sp1Code(c.getString(c.getColumnIndex("TS1_SP1_CODE")));
            stockTransaction.setTs1Sp1Name(c.getString(c.getColumnIndex("TS1_SP1_Name")));
            stockTransaction.setTs1UploadStatus(c.getString(c.getColumnIndex("TS1_UploadStatus")));
            stockTransaction.setTs1CreateUser(c.getString(c.getColumnIndex("TS1_CreateUser")));
            stockTransaction.setTs1CreateTime(c.getString(c.getColumnIndex("TS1_CreateTime")));
            stockTransaction.setTs1ModifyUser(c.getString(c.getColumnIndex("TS1_ModifyUser")));
            stockTransaction.setTs1ModifyTime(c.getString(c.getColumnIndex("TS1_ModifyTime")));
            stockTransaction.setTs1RowVersion(c.getString(c.getColumnIndex("TS1_RowVersion")));
            list.add(stockTransaction);
        }
        return list;
    }

    public boolean updateUploadStatusByTs1Id(String ts1Id) {
        String updateSql = "update StockTransaction set TS1_UploadStatus = ? where TS1_ID = ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            SQLiteStatement stat = db.compileStatement(updateSql);
            stat.bindString(1, "1");
            stat.bindString(2, ts1Id);
            int i = stat.executeUpdateDelete();
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

    public boolean batchUpdateUploadStatus(List<StockTransactionData> stockTransactionList) {
        String updateSql = "update StockTransaction set TS1_UploadStatus = ? where TS1_ID = ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (StockTransactionData stockTransaction : stockTransactionList) {
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindString(1, "1");
                stat.bindString(2, stockTransaction.getTs1Id());
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

    public String getVendingStoreLastPicker(String vendingId, String vendingChnCode) {
        String rtnStr = "";
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT ts1_cd1_id FROM StockTransaction where ts1_vd1_id=? and ts1_vc1_code=? and ts1_billtype='4' order by ts1_createtime desc limit 1", new String[]{vendingId, vendingChnCode});
        while (c.moveToNext()) {
            rtnStr = c.getString(c.getColumnIndex("TS1_CD1_ID"));
        }
        return rtnStr;
    }
}
