package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingChnStockData;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VendingChnStockDbOper {
    public List<VendingChnStockData> findAll() {
        List<VendingChnStockData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM VendingChnStock", null);
        while (c.moveToNext()) {
            VendingChnStockData vendingChnStock = new VendingChnStockData();
            vendingChnStock.setVs1Id(c.getString(c.getColumnIndex("VS1_ID")));
            vendingChnStock.setVs1M02Id(c.getString(c.getColumnIndex("VS1_M02_ID")));
            vendingChnStock.setVs1Vd1Id(c.getString(c.getColumnIndex("VS1_VD1_ID")));
            vendingChnStock.setVs1Vc1Code(c.getString(c.getColumnIndex("VS1_VC1_CODE")));
            vendingChnStock.setVs1Pd1Id(c.getString(c.getColumnIndex("VS1_PD1_ID")));
            vendingChnStock.setVs1Quantity(Integer.valueOf(c.getInt(c.getColumnIndex("VS1_Quantity"))));
            vendingChnStock.setVs1CreateUser(c.getString(c.getColumnIndex("VS1_CreateUser")));
            vendingChnStock.setVs1CreateTime(c.getString(c.getColumnIndex("VS1_CreateTime")));
            vendingChnStock.setVs1ModifyUser(c.getString(c.getColumnIndex("VS1_ModifyUser")));
            vendingChnStock.setVs1ModifyTime(c.getString(c.getColumnIndex("VS1_ModifyTime")));
            vendingChnStock.setVs1RowVersion(c.getString(c.getColumnIndex("VS1_RowVersion")));
            list.add(vendingChnStock);
        }
        return list;
    }

    public List<VendingChnStockData> findAllByChn() {
        List<VendingChnStockData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM VendingChnStock vcs left join VendingChn vc on vcs.VS1_PD1_ID = vc.VC1_PD1_ID and vcs.VS1_VC1_CODE = vc.VC1_CODE where vc.VC1_Status=0", null);
        while (c.moveToNext()) {
            VendingChnStockData vendingChnStock = new VendingChnStockData();
            vendingChnStock.setVs1Id(c.getString(c.getColumnIndex("VS1_ID")));
            vendingChnStock.setVs1M02Id(c.getString(c.getColumnIndex("VS1_M02_ID")));
            vendingChnStock.setVs1Vd1Id(c.getString(c.getColumnIndex("VS1_VD1_ID")));
            vendingChnStock.setVs1Vc1Code(c.getString(c.getColumnIndex("VS1_VC1_CODE")));
            vendingChnStock.setVs1Pd1Id(c.getString(c.getColumnIndex("VS1_PD1_ID")));
            vendingChnStock.setVs1Quantity(Integer.valueOf(c.getInt(c.getColumnIndex("VS1_Quantity"))));
            vendingChnStock.setVs1CreateUser(c.getString(c.getColumnIndex("VS1_CreateUser")));
            vendingChnStock.setVs1CreateTime(c.getString(c.getColumnIndex("VS1_CreateTime")));
            vendingChnStock.setVs1ModifyUser(c.getString(c.getColumnIndex("VS1_ModifyUser")));
            vendingChnStock.setVs1ModifyTime(c.getString(c.getColumnIndex("VS1_ModifyTime")));
            vendingChnStock.setVs1RowVersion(c.getString(c.getColumnIndex("VS1_RowVersion")));
            list.add(vendingChnStock);
        }
        return list;
    }

    public Map<String, VendingChnStockData> getStockDataMap() {
        List<VendingChnStockData> chnStockDatas = findAllByChn();
        Map<String, VendingChnStockData> map = new HashMap();
        for (VendingChnStockData vendingChnStockData : chnStockDatas) {
            map.put(vendingChnStockData.getVs1Vc1Code(), vendingChnStockData);
        }
        return map;
    }

    public Map<String, Integer> getStockMap() {
        Map<String, Integer> map = new HashMap();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM VendingChnStock", null);
        while (c.moveToNext()) {
            map.put(c.getString(c.getColumnIndex("VS1_VC1_CODE")), Integer.valueOf(c.getInt(c.getColumnIndex("VS1_Quantity"))));
        }
        return map;
    }

    public VendingChnStockData getStockByVidAndVcCode(String vendingId, String vendingChnCode) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT VS1_ID,VS1_VD1_ID,VS1_VC1_CODE,VS1_PD1_ID,VS1_Quantity FROM VendingChnStock WHERE VS1_VD1_ID=? and VS1_VC1_CODE=? ORDER BY VS1_QUANTITY DESC limit 1", new String[]{vendingId, vendingChnCode});
        if (!c.moveToNext()) {
            return null;
        }
        VendingChnStockData vendingChnStock = new VendingChnStockData();
        vendingChnStock.setVs1Id(c.getString(c.getColumnIndex("VS1_ID")));
        vendingChnStock.setVs1Vd1Id(c.getString(c.getColumnIndex("VS1_VD1_ID")));
        vendingChnStock.setVs1Vc1Code(c.getString(c.getColumnIndex("VS1_VC1_CODE")));
        vendingChnStock.setVs1Pd1Id(c.getString(c.getColumnIndex("VS1_PD1_ID")));
        vendingChnStock.setVs1Quantity(Integer.valueOf(c.getInt(c.getColumnIndex("VS1_Quantity"))));
        return vendingChnStock;
    }

    public List<VendingChnStockData> findStockBySkuId(String skuIds) {
        if (StringHelper.isEmpty(skuIds, true)) {
            return new ArrayList(0);
        }
        List<VendingChnStockData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM VendingChnStock WHERE VS1_PD1_ID IN(" + skuIds + ") ORDER by TS1_VC1_CODE", null);
        while (c.moveToNext()) {
            VendingChnStockData vendingChnStock = new VendingChnStockData();
            vendingChnStock.setVs1Id(c.getString(c.getColumnIndex("VS1_ID")));
            vendingChnStock.setVs1M02Id(c.getString(c.getColumnIndex("VS1_M02_ID")));
            vendingChnStock.setVs1Vd1Id(c.getString(c.getColumnIndex("VS1_VD1_ID")));
            vendingChnStock.setVs1Vc1Code(c.getString(c.getColumnIndex("VS1_VC1_CODE")));
            vendingChnStock.setVs1Pd1Id(c.getString(c.getColumnIndex("VS1_PD1_ID")));
            vendingChnStock.setVs1Quantity(Integer.valueOf(c.getInt(c.getColumnIndex("VS1_Quantity"))));
            vendingChnStock.setVs1CreateUser(c.getString(c.getColumnIndex("VS1_CreateUser")));
            vendingChnStock.setVs1CreateTime(c.getString(c.getColumnIndex("VS1_CreateTime")));
            vendingChnStock.setVs1ModifyUser(c.getString(c.getColumnIndex("VS1_ModifyUser")));
            vendingChnStock.setVs1ModifyTime(c.getString(c.getColumnIndex("VS1_ModifyTime")));
            vendingChnStock.setVs1RowVersion(c.getString(c.getColumnIndex("VS1_RowVersion")));
            list.add(vendingChnStock);
        }
        return list;
    }

    public boolean addVendingChnStock(VendingChnStockData vendingChnStock) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into VendingChnStock(VS1_ID,VS1_M02_ID,VS1_VD1_ID,VS1_VC1_CODE,VS1_PD1_ID,VS1_Quantity,VS1_CreateUser,VS1_CreateTime,VS1_ModifyUser,VS1_ModifyTime,VS1_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?)");
        stat.bindString(1, vendingChnStock.getVs1Id());
        stat.bindString(2, vendingChnStock.getVs1M02Id());
        stat.bindString(3, vendingChnStock.getVs1Vd1Id());
        stat.bindString(4, vendingChnStock.getVs1Vc1Code());
        stat.bindString(5, vendingChnStock.getVs1Pd1Id());
        stat.bindLong(6, (long) vendingChnStock.getVs1Quantity().intValue());
        stat.bindString(7, vendingChnStock.getVs1CreateUser());
        stat.bindString(8, vendingChnStock.getVs1CreateTime());
        stat.bindString(9, vendingChnStock.getVs1ModifyUser());
        stat.bindString(10, vendingChnStock.getVs1ModifyTime());
        stat.bindString(11, vendingChnStock.getVs1RowVersion());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchAddVendingChnStock(List<VendingChnStockData> list) {
        String insertSql = "insert into VendingChnStock(VS1_ID,VS1_M02_ID,VS1_VD1_ID,VS1_VC1_CODE,VS1_PD1_ID,VS1_Quantity,VS1_CreateUser,VS1_CreateTime,VS1_ModifyUser,VS1_ModifyTime,VS1_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (VendingChnStockData vendingChnStock : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, vendingChnStock.getVs1Id());
                stat.bindString(2, vendingChnStock.getVs1M02Id());
                stat.bindString(3, vendingChnStock.getVs1Vd1Id());
                stat.bindString(4, vendingChnStock.getVs1Vc1Code());
                stat.bindString(5, vendingChnStock.getVs1Pd1Id());
                stat.bindLong(6, (long) vendingChnStock.getVs1Quantity().intValue());
                stat.bindString(7, vendingChnStock.getVs1CreateUser());
                stat.bindString(8, vendingChnStock.getVs1CreateTime());
                stat.bindString(9, vendingChnStock.getVs1ModifyUser());
                stat.bindString(10, vendingChnStock.getVs1ModifyTime());
                stat.bindString(11, vendingChnStock.getVs1RowVersion());
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

    public Boolean batchUpdateVendingChnStock(List<VendingChnStockData> list) {
        boolean flag = false;
        String updateSql = "UPDATE VendingChnStock SET VS1_Quantity = VS1_Quantity + ? ,VS1_PD1_ID = ?WHERE VS1_VD1_ID = ? and VS1_VC1_CODE = ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (VendingChnStockData vendingChnStock : list) {
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindLong(1, (long) vendingChnStock.getVs1Quantity().intValue());
                stat.bindString(2, vendingChnStock.getVs1Pd1Id());
                stat.bindString(3, vendingChnStock.getVs1Vd1Id());
                stat.bindString(4, vendingChnStock.getVs1Vc1Code());
                stat.executeUpdateDelete();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            flag = true;
        } catch (SQLException e) {
            db.endTransaction();
            e.printStackTrace();
        }
        return Boolean.valueOf(flag);
    }

    public Boolean updateStockQuantity(int inputQty, VendingChnData vendingChn) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("UPDATE VendingChnStock SET VS1_Quantity = VS1_Quantity + ? WHERE VS1_VD1_ID = ? and VS1_VC1_CODE = ? and VS1_PD1_ID = ? ");
        stat.bindLong(1, (long) inputQty);
        stat.bindString(2, vendingChn.getVc1Vd1Id());
        stat.bindString(3, vendingChn.getVc1Code());
        stat.bindString(4, vendingChn.getVc1Pd1Id());
        return ((long) stat.executeUpdateDelete()) > 0 ? Boolean.valueOf(true) : Boolean.valueOf(false);
    }

    public boolean deleteAll() {
        String chnSql = "DELETE FROM VendingChnStock";
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

    public boolean batchDeleteVendingChnStock(List<VendingChnStockData> list) {
        String updateSql = "delete from VendingChnStock  WHERE VS1_VC1_CODE = ?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (VendingChnStockData vendingChnStock : list) {
                SQLiteStatement stat = db.compileStatement(updateSql);
                stat.bindString(1, vendingChnStock.getVs1Vc1Code());
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
}
