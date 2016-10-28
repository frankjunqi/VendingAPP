package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.ChnStockWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.List;

public class VendingChnDbOper {
    public List<VendingChnData> findAll() {
        List<VendingChnData> vendingChnList = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM VendingChn", null);
        while (c.moveToNext()) {
            VendingChnData vendingChn = new VendingChnData();
            vendingChn.setVc1Id(c.getString(c.getColumnIndex("VC1_ID")));
            vendingChn.setVc1M02Id(c.getString(c.getColumnIndex("VC1_M02_ID")));
            vendingChn.setVc1Vd1Id(c.getString(c.getColumnIndex("VC1_VD1_ID")));
            vendingChn.setVc1Code(c.getString(c.getColumnIndex("VC1_CODE")));
            vendingChn.setVc1Type(c.getString(c.getColumnIndex("VC1_Type")));
            vendingChn.setVc1Capacity(Integer.valueOf(c.getInt(c.getColumnIndex("VC1_Capacity"))));
            vendingChn.setVc1ThreadSize(c.getString(c.getColumnIndex("VC1_ThreadSize")));
            vendingChn.setVc1Pd1Id(c.getString(c.getColumnIndex("VC1_PD1_ID")));
            vendingChn.setVc1SaleType(c.getString(c.getColumnIndex("VC1_SaleType")));
            vendingChn.setVc1Sp1Id(c.getString(c.getColumnIndex("VC1_SP1_ID")));
            vendingChn.setVc1BorrowStatus(c.getString(c.getColumnIndex("VC1_BorrowStatus")));
            vendingChn.setVc1Status(c.getString(c.getColumnIndex("VC1_Status")));
            vendingChn.setVc1LineNum(c.getString(c.getColumnIndex("VC1_LineNum")));
            vendingChn.setVc1ColumnNum(c.getString(c.getColumnIndex("VC1_ColumnNum")));
            vendingChn.setVc1Height(c.getString(c.getColumnIndex("VC1_Height")));
            vendingChn.setVc1CreateUser(c.getString(c.getColumnIndex("VC1_CreateUser")));
            vendingChn.setVc1CreateTime(c.getString(c.getColumnIndex("VC1_CreateTime")));
            vendingChn.setVc1ModifyUser(c.getString(c.getColumnIndex("VC1_ModifyUser")));
            vendingChn.setVc1ModifyTime(c.getString(c.getColumnIndex("VC1_ModifyTime")));
            vendingChn.setVc1RowVersion(c.getString(c.getColumnIndex("VC1_RowVersion")));
            vendingChnList.add(vendingChn);
        }
        return vendingChnList;
    }

    public List<VendingChnData> findAllVendingChn(String saleType) {
        List<VendingChnData> vendingChnList = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM VendingChn WHERE VC1_SaleType<>? ORDER BY VC1_CODE", new String[]{saleType});
        while (c.moveToNext()) {
            VendingChnData vendingChn = new VendingChnData();
            vendingChn.setVc1Id(c.getString(c.getColumnIndex("VC1_ID")));
            vendingChn.setVc1M02Id(c.getString(c.getColumnIndex("VC1_M02_ID")));
            vendingChn.setVc1Vd1Id(c.getString(c.getColumnIndex("VC1_VD1_ID")));
            vendingChn.setVc1Code(c.getString(c.getColumnIndex("VC1_CODE")));
            vendingChn.setVc1Type(c.getString(c.getColumnIndex("VC1_Type")));
            vendingChn.setVc1Capacity(Integer.valueOf(c.getInt(c.getColumnIndex("VC1_Capacity"))));
            vendingChn.setVc1ThreadSize(c.getString(c.getColumnIndex("VC1_ThreadSize")));
            vendingChn.setVc1Pd1Id(c.getString(c.getColumnIndex("VC1_PD1_ID")));
            vendingChn.setVc1SaleType(c.getString(c.getColumnIndex("VC1_SaleType")));
            vendingChn.setVc1Sp1Id(c.getString(c.getColumnIndex("VC1_SP1_ID")));
            vendingChn.setVc1BorrowStatus(c.getString(c.getColumnIndex("VC1_BorrowStatus")));
            vendingChn.setVc1Status(c.getString(c.getColumnIndex("VC1_Status")));
            vendingChn.setVc1LineNum(c.getString(c.getColumnIndex("VC1_LineNum")));
            vendingChn.setVc1ColumnNum(c.getString(c.getColumnIndex("VC1_ColumnNum")));
            vendingChn.setVc1Height(c.getString(c.getColumnIndex("VC1_Height")));
            vendingChn.setVc1CreateUser(c.getString(c.getColumnIndex("VC1_CreateUser")));
            vendingChn.setVc1CreateTime(c.getString(c.getColumnIndex("VC1_CreateTime")));
            vendingChn.setVc1ModifyUser(c.getString(c.getColumnIndex("VC1_ModifyUser")));
            vendingChn.setVc1ModifyTime(c.getString(c.getColumnIndex("VC1_ModifyTime")));
            vendingChn.setVc1RowVersion(c.getString(c.getColumnIndex("VC1_RowVersion")));
            vendingChnList.add(vendingChn);
        }
        return vendingChnList;
    }

    public VendingChnData getVendingChnByCode(String vendingChnCode) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT VC1_ID,VC1_VD1_ID,VC1_CODE,VC1_Type,VC1_PD1_ID,VC1_SaleType,VC1_SP1_ID,VC1_BorrowStatus,VC1_Status,VC1_LineNum,VC1_ColumnNum,VC1_Height FROM VendingChn WHERE VC1_CODE=?  limit 1", new String[]{vendingChnCode});
        if (!c.moveToNext()) {
            return null;
        }
        VendingChnData vendingChn = new VendingChnData();
        vendingChn.setVc1Id(c.getString(c.getColumnIndex("VC1_ID")));
        vendingChn.setVc1Vd1Id(c.getString(c.getColumnIndex("VC1_VD1_ID")));
        vendingChn.setVc1Code(c.getString(c.getColumnIndex("VC1_CODE")));
        vendingChn.setVc1Type(c.getString(c.getColumnIndex("VC1_Type")));
        vendingChn.setVc1Pd1Id(c.getString(c.getColumnIndex("VC1_PD1_ID")));
        vendingChn.setVc1SaleType(c.getString(c.getColumnIndex("VC1_SaleType")));
        vendingChn.setVc1Sp1Id(c.getString(c.getColumnIndex("VC1_SP1_ID")));
        vendingChn.setVc1BorrowStatus(c.getString(c.getColumnIndex("VC1_BorrowStatus")));
        vendingChn.setVc1Status(c.getString(c.getColumnIndex("VC1_Status")));
        vendingChn.setVc1LineNum(c.getString(c.getColumnIndex("VC1_LineNum")));
        vendingChn.setVc1ColumnNum(c.getString(c.getColumnIndex("VC1_ColumnNum")));
        vendingChn.setVc1Height(c.getString(c.getColumnIndex("VC1_Height")));
        return vendingChn;
    }

    public VendingChnData getVendingChnByCode(String vendingChnCode, String vcId) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM VendingChn WHERE VC1_CODE=? and VC1_VD1_ID=? limit 1", new String[]{vendingChnCode, vcId});
        if (!c.moveToNext()) {
            return null;
        }
        VendingChnData vendingChn = new VendingChnData();
        vendingChn.setVc1Id(c.getString(c.getColumnIndex("VC1_ID")));
        vendingChn.setVc1M02Id(c.getString(c.getColumnIndex("VC1_M02_ID")));
        vendingChn.setVc1Vd1Id(c.getString(c.getColumnIndex("VC1_VD1_ID")));
        vendingChn.setVc1Code(c.getString(c.getColumnIndex("VC1_CODE")));
        vendingChn.setVc1Type(c.getString(c.getColumnIndex("VC1_Type")));
        vendingChn.setVc1Capacity(Integer.valueOf(c.getInt(c.getColumnIndex("VC1_Capacity"))));
        vendingChn.setVc1ThreadSize(c.getString(c.getColumnIndex("VC1_ThreadSize")));
        vendingChn.setVc1Pd1Id(c.getString(c.getColumnIndex("VC1_PD1_ID")));
        vendingChn.setVc1SaleType(c.getString(c.getColumnIndex("VC1_SaleType")));
        vendingChn.setVc1Sp1Id(c.getString(c.getColumnIndex("VC1_SP1_ID")));
        vendingChn.setVc1BorrowStatus(c.getString(c.getColumnIndex("VC1_BorrowStatus")));
        vendingChn.setVc1Status(c.getString(c.getColumnIndex("VC1_Status")));
        vendingChn.setVc1LineNum(c.getString(c.getColumnIndex("VC1_LineNum")));
        vendingChn.setVc1ColumnNum(c.getString(c.getColumnIndex("VC1_ColumnNum")));
        vendingChn.setVc1Height(c.getString(c.getColumnIndex("VC1_Height")));
        vendingChn.setVc1CreateUser(c.getString(c.getColumnIndex("VC1_CreateUser")));
        vendingChn.setVc1CreateTime(c.getString(c.getColumnIndex("VC1_CreateTime")));
        vendingChn.setVc1ModifyUser(c.getString(c.getColumnIndex("VC1_ModifyUser")));
        vendingChn.setVc1ModifyTime(c.getString(c.getColumnIndex("VC1_ModifyTime")));
        vendingChn.setVc1RowVersion(c.getString(c.getColumnIndex("VC1_RowVersion")));
        return vendingChn;
    }

    public List<VendingChnData> findVendingChnBySkuIds(String skuIds) {
        if (StringHelper.isEmpty(skuIds, true)) {
            return new ArrayList(0);
        }
        List<VendingChnData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM VendingChn WHERE VC1_Status=1 and VC1_Type=1 and VC1_PD1_ID IN(" + skuIds + ") ORDER by VC1_CODE", null);
        while (c.moveToNext()) {
            VendingChnData vendingChn = new VendingChnData();
            vendingChn.setVc1Id(c.getString(c.getColumnIndex("VC1_ID")));
            vendingChn.setVc1M02Id(c.getString(c.getColumnIndex("VC1_M02_ID")));
            vendingChn.setVc1Vd1Id(c.getString(c.getColumnIndex("VC1_VD1_ID")));
            vendingChn.setVc1Code(c.getString(c.getColumnIndex("VC1_CODE")));
            vendingChn.setVc1Type(c.getString(c.getColumnIndex("VC1_Type")));
            vendingChn.setVc1Capacity(Integer.valueOf(c.getInt(c.getColumnIndex("VC1_Capacity"))));
            vendingChn.setVc1ThreadSize(c.getString(c.getColumnIndex("VC1_ThreadSize")));
            vendingChn.setVc1Pd1Id(c.getString(c.getColumnIndex("VC1_PD1_ID")));
            vendingChn.setVc1SaleType(c.getString(c.getColumnIndex("VC1_SaleType")));
            vendingChn.setVc1Sp1Id(c.getString(c.getColumnIndex("VC1_SP1_ID")));
            vendingChn.setVc1BorrowStatus(c.getString(c.getColumnIndex("VC1_BorrowStatus")));
            vendingChn.setVc1Status(c.getString(c.getColumnIndex("VC1_Status")));
            vendingChn.setVc1LineNum(c.getString(c.getColumnIndex("VC1_LineNum")));
            vendingChn.setVc1ColumnNum(c.getString(c.getColumnIndex("VC1_ColumnNum")));
            vendingChn.setVc1Height(c.getString(c.getColumnIndex("VC1_Height")));
            vendingChn.setVc1CreateUser(c.getString(c.getColumnIndex("VC1_CreateUser")));
            vendingChn.setVc1CreateTime(c.getString(c.getColumnIndex("VC1_CreateTime")));
            vendingChn.setVc1ModifyUser(c.getString(c.getColumnIndex("VC1_ModifyUser")));
            vendingChn.setVc1ModifyTime(c.getString(c.getColumnIndex("VC1_ModifyTime")));
            vendingChn.setVc1RowVersion(c.getString(c.getColumnIndex("VC1_RowVersion")));
            list.add(vendingChn);
        }
        return list;
    }

    public List<ChnStockWrapperData> findVendingChnBySkuId(String skuId, String chnStatus, String chnType) {
        if (StringHelper.isEmpty(skuId, true)) {
            return new ArrayList(0);
        }
        List<ChnStockWrapperData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT chn.*,stock.VS1_Quantity VS1_Quantity FROM (SELECT * FROM VendingChn WHERE VC1_Status = ? and VC1_Type = ? and VC1_PD1_ID = ? ) chn join (SELECT VS1_PD1_ID,VS1_VC1_CODE,VS1_Quantity FROM VendingChnStock WHERE VS1_PD1_ID = ? ) stock on chn.VC1_PD1_ID=stock.VS1_PD1_ID and chn.VC1_CODE=stock.VS1_VC1_CODE ORDER by chn.VC1_CODE", new String[]{chnStatus, chnType, skuId, skuId});
        while (c.moveToNext()) {
            ChnStockWrapperData chnStock = new ChnStockWrapperData();
            VendingChnData vendingChn = new VendingChnData();
            vendingChn.setVc1Id(c.getString(c.getColumnIndex("VC1_ID")));
            vendingChn.setVc1M02Id(c.getString(c.getColumnIndex("VC1_M02_ID")));
            vendingChn.setVc1Vd1Id(c.getString(c.getColumnIndex("VC1_VD1_ID")));
            vendingChn.setVc1Code(c.getString(c.getColumnIndex("VC1_CODE")));
            vendingChn.setVc1Type(c.getString(c.getColumnIndex("VC1_Type")));
            vendingChn.setVc1Capacity(Integer.valueOf(c.getInt(c.getColumnIndex("VC1_Capacity"))));
            vendingChn.setVc1ThreadSize(c.getString(c.getColumnIndex("VC1_ThreadSize")));
            vendingChn.setVc1Pd1Id(c.getString(c.getColumnIndex("VC1_PD1_ID")));
            vendingChn.setVc1SaleType(c.getString(c.getColumnIndex("VC1_SaleType")));
            vendingChn.setVc1Sp1Id(c.getString(c.getColumnIndex("VC1_SP1_ID")));
            vendingChn.setVc1BorrowStatus(c.getString(c.getColumnIndex("VC1_BorrowStatus")));
            vendingChn.setVc1Status(c.getString(c.getColumnIndex("VC1_Status")));
            vendingChn.setVc1LineNum(c.getString(c.getColumnIndex("VC1_LineNum")));
            vendingChn.setVc1ColumnNum(c.getString(c.getColumnIndex("VC1_ColumnNum")));
            vendingChn.setVc1Height(c.getString(c.getColumnIndex("VC1_Height")));
            vendingChn.setVc1CreateUser(c.getString(c.getColumnIndex("VC1_CreateUser")));
            vendingChn.setVc1CreateTime(c.getString(c.getColumnIndex("VC1_CreateTime")));
            vendingChn.setVc1ModifyUser(c.getString(c.getColumnIndex("VC1_ModifyUser")));
            vendingChn.setVc1ModifyTime(c.getString(c.getColumnIndex("VC1_ModifyTime")));
            vendingChn.setVc1RowVersion(c.getString(c.getColumnIndex("VC1_RowVersion")));
            chnStock.setVendingChn(vendingChn);
            chnStock.setQuantity(c.getInt(c.getColumnIndex("VS1_Quantity")));
            list.add(chnStock);
        }
        return list;
    }

    public boolean addVendingChn(VendingChnData vendingChn) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into VendingChn(VC1_ID,VC1_M02_ID,VC1_VD1_ID,VC1_CODE,VC1_Type,VC1_Capacity,VC1_ThreadSize,VC1_PD1_ID,VC1_SaleType,VC1_SP1_ID,VC1_BorrowStatus,VC1_Status,VC1_LineNum,VC1_ColumnNum,VC1_Height,VC1_CreateUser,VC1_CreateTime,VC1_ModifyUser,VC1_ModifyTime,VC1_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        stat.bindString(1, vendingChn.getVc1Id());
        stat.bindString(2, vendingChn.getVc1M02Id());
        stat.bindString(3, vendingChn.getVc1Vd1Id());
        stat.bindString(4, vendingChn.getVc1Code());
        stat.bindString(5, vendingChn.getVc1Type());
        stat.bindLong(6, (long) vendingChn.getVc1Capacity().intValue());
        stat.bindString(7, vendingChn.getVc1ThreadSize());
        stat.bindString(8, vendingChn.getVc1Pd1Id());
        stat.bindString(9, vendingChn.getVc1SaleType());
        stat.bindString(10, vendingChn.getVc1Sp1Id());
        stat.bindString(11, vendingChn.getVc1BorrowStatus());
        stat.bindString(12, vendingChn.getVc1Status());
        stat.bindString(13, vendingChn.getVc1LineNum());
        stat.bindString(14, vendingChn.getVc1ColumnNum());
        stat.bindString(15, vendingChn.getVc1Height());
        stat.bindString(16, vendingChn.getVc1CreateUser());
        stat.bindString(17, vendingChn.getVc1CreateTime());
        stat.bindString(18, vendingChn.getVc1ModifyUser());
        stat.bindString(19, vendingChn.getVc1ModifyTime());
        stat.bindString(20, vendingChn.getVc1RowVersion());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchAddVendingChn(List<VendingChnData> list) {
        String chnSql = "insert into VendingChn(VC1_ID,VC1_M02_ID,VC1_VD1_ID,VC1_CODE,VC1_Type,VC1_Capacity,VC1_ThreadSize,VC1_PD1_ID,VC1_SaleType,VC1_SP1_ID,VC1_BorrowStatus,VC1_Status,VC1_LineNum,VC1_ColumnNum,VC1_Height,VC1_CreateUser,VC1_CreateTime,VC1_ModifyUser,VC1_ModifyTime,VC1_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (VendingChnData vendingChn : list) {
                SQLiteStatement stat = db.compileStatement(chnSql);
                stat.bindString(1, vendingChn.getVc1Id());
                stat.bindString(2, vendingChn.getVc1M02Id());
                stat.bindString(3, vendingChn.getVc1Vd1Id());
                stat.bindString(4, vendingChn.getVc1Code());
                stat.bindString(5, vendingChn.getVc1Type());
                stat.bindLong(6, (long) vendingChn.getVc1Capacity().intValue());
                stat.bindString(7, vendingChn.getVc1ThreadSize());
                stat.bindString(8, vendingChn.getVc1Pd1Id());
                stat.bindString(9, vendingChn.getVc1SaleType());
                stat.bindString(10, vendingChn.getVc1Sp1Id());
                stat.bindString(11, vendingChn.getVc1BorrowStatus());
                stat.bindString(12, vendingChn.getVc1Status());
                stat.bindString(13, vendingChn.getVc1LineNum());
                stat.bindString(14, vendingChn.getVc1ColumnNum());
                stat.bindString(15, vendingChn.getVc1Height());
                stat.bindString(16, vendingChn.getVc1CreateUser());
                stat.bindString(17, vendingChn.getVc1CreateTime());
                stat.bindString(18, vendingChn.getVc1ModifyUser());
                stat.bindString(19, vendingChn.getVc1ModifyTime());
                stat.bindString(20, vendingChn.getVc1RowVersion());
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

    public Boolean updateBorrowStatus(String borrowStatus, String vcCode) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("UPDATE VendingChn SET VC1_BorrowStatus = ? WHERE VS1_VC1_CODE = ?");
        stat.bindString(1, borrowStatus);
        stat.bindString(2, vcCode);
        return stat.executeInsert() > 0 ? Boolean.valueOf(true) : Boolean.valueOf(false);
    }

    public boolean deleteAll() {
        String chnSql = "DELETE FROM VendingChn";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            db.compileStatement(chnSql).executeInsert();
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
