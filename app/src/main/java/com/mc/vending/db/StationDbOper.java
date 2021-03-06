package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.StationData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StationDbOper {
    public List<StationData> findAll() {
        List<StationData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM Station", null);
        while (c.moveToNext()) {
            StationData station = new StationData();
            station.setSt1Id(c.getString(c.getColumnIndex("ST1_ID")));
            station.setSt1M02Id(c.getString(c.getColumnIndex("ST1_M02_ID")));
            station.setSt1Code(c.getString(c.getColumnIndex("ST1_CODE")));
            station.setSt1Name(c.getString(c.getColumnIndex("ST1_Name")));
            station.setSt1Ce1Id(c.getString(c.getColumnIndex("ST1_CE1_ID")));
            station.setSt1Wh1Id(c.getString(c.getColumnIndex("ST1_WH1_ID")));
            station.setSt1Coordinate(c.getString(c.getColumnIndex("ST1_Coordinate")));
            station.setSt1Address(c.getString(c.getColumnIndex("ST1_Address")));
            station.setSt1Status(c.getString(c.getColumnIndex("ST1_Status")));
            station.setSt1CreateUser(c.getString(c.getColumnIndex("ST1_CreateUser")));
            station.setSt1CreateTime(c.getString(c.getColumnIndex("ST1_CreateTime")));
            station.setSt1ModifyUser(c.getString(c.getColumnIndex("ST1_ModifyUser")));
            station.setSt1ModifyTime(c.getString(c.getColumnIndex("ST1_ModifyTime")));
            station.setSt1RowVersion(c.getString(c.getColumnIndex("ST1_RowVersion")));
            list.add(station);
        }
        return list;
    }

    public Map<String, String> findAllMap() {
        Map<String, String> map = new HashMap();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT ST1_ID,ST1_CODE FROM Station", null);
        while (c.moveToNext()) {
            map.put(c.getString(c.getColumnIndex("ST1_ID")), c.getString(c.getColumnIndex("ST1_CODE")));
        }
        return map;
    }

    public boolean addStation(StationData station) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into Station(ST1_ID,ST1_M02_ID,ST1_CODE,ST1_Name,ST1_CE1_ID,ST1_WH1_ID,ST1_Coordinate,ST1_Address,ST1_Status,ST1_CreateUser,ST1_CreateTime,ST1_ModifyUser,ST1_ModifyTime,ST1_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        stat.bindString(1, station.getSt1Id());
        stat.bindString(2, station.getSt1M02Id());
        stat.bindString(3, station.getSt1Code());
        stat.bindString(4, station.getSt1Name());
        stat.bindString(5, station.getSt1Ce1Id());
        stat.bindString(6, station.getSt1Wh1Id());
        stat.bindString(7, station.getSt1Coordinate());
        stat.bindString(8, station.getSt1Address());
        stat.bindString(9, station.getSt1Status());
        stat.bindString(10, station.getSt1CreateUser());
        stat.bindString(11, station.getSt1CreateTime());
        stat.bindString(12, station.getSt1ModifyUser());
        stat.bindString(13, station.getSt1ModifyTime());
        stat.bindString(14, station.getSt1RowVersion());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchAddStation(List<StationData> list) {
        String insertSql = "insert into Station(ST1_ID,ST1_M02_ID,ST1_CODE,ST1_Name,ST1_CE1_ID,ST1_WH1_ID,ST1_Coordinate,ST1_Address,ST1_Status,ST1_CreateUser,ST1_CreateTime,ST1_ModifyUser,ST1_ModifyTime,ST1_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (StationData station : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, station.getSt1Id());
                stat.bindString(2, station.getSt1M02Id());
                stat.bindString(3, station.getSt1Code());
                stat.bindString(4, station.getSt1Name());
                stat.bindString(5, station.getSt1Ce1Id());
                stat.bindString(6, station.getSt1Wh1Id());
                stat.bindString(7, station.getSt1Coordinate());
                stat.bindString(8, station.getSt1Address());
                stat.bindString(9, station.getSt1Status());
                stat.bindString(10, station.getSt1CreateUser());
                stat.bindString(11, station.getSt1CreateTime());
                stat.bindString(12, station.getSt1ModifyUser());
                stat.bindString(13, station.getSt1ModifyTime());
                stat.bindString(14, station.getSt1RowVersion());
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

    public boolean batchUpdateStation(List<StationData> list) {
        String insertSql = "UPDATE Station SET ST1_M02_ID=?,ST1_CODE=?,ST1_Name=?,ST1_CE1_ID=?,ST1_WH1_ID=?,ST1_Coordinate=?,ST1_Address=?,ST1_Status=?,ST1_CreateUser=?,ST1_CreateTime=?,ST1_ModifyUser=?,ST1_ModifyTime=?,ST1_RowVersion=? WHERE ST1_ID=?";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (StationData station : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, station.getSt1M02Id());
                stat.bindString(2, station.getSt1Code());
                stat.bindString(3, station.getSt1Name());
                stat.bindString(4, station.getSt1Ce1Id());
                stat.bindString(5, station.getSt1Wh1Id());
                stat.bindString(6, station.getSt1Coordinate());
                stat.bindString(7, station.getSt1Address());
                stat.bindString(8, station.getSt1Status());
                stat.bindString(9, station.getSt1CreateUser());
                stat.bindString(10, station.getSt1CreateTime());
                stat.bindString(11, station.getSt1ModifyUser());
                stat.bindString(12, station.getSt1ModifyTime());
                stat.bindString(13, station.getSt1RowVersion());
                stat.bindString(14, station.getSt1Id());
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
        String deleteSql = "DELETE FROM Station";
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
