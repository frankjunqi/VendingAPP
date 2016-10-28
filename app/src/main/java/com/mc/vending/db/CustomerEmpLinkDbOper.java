package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.CustomerEmpLinkData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.List;

public class CustomerEmpLinkDbOper {
    public List<CustomerEmpLinkData> findAll() {
        List<CustomerEmpLinkData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM CustomerEmpLink", null);
        while (c.moveToNext()) {
            CustomerEmpLinkData customerEmpLink = new CustomerEmpLinkData();
            customerEmpLink.setCe1Id(c.getString(c.getColumnIndex("CE1_ID")));
            customerEmpLink.setCe1M02Id(c.getString(c.getColumnIndex("CE1_M02_ID")));
            customerEmpLink.setCe1Cu1Id(c.getString(c.getColumnIndex("CE1_CU1_ID")));
            customerEmpLink.setCe1Code(c.getString(c.getColumnIndex("CE1_CODE")));
            customerEmpLink.setCe1Name(c.getString(c.getColumnIndex("CE1_Name")));
            customerEmpLink.setCe1EnglishName(c.getString(c.getColumnIndex("CE1_EnglishName")));
            customerEmpLink.setCe1Sex(c.getString(c.getColumnIndex("CE1_Sex")));
            customerEmpLink.setCe1Dp1Id(c.getString(c.getColumnIndex("CE1_DP1_ID")));
            customerEmpLink.setCe1DicIdJob(c.getString(c.getColumnIndex("CE1_Dic_ID_JOB")));
            customerEmpLink.setCe1Phone(c.getString(c.getColumnIndex("CE1_Phone")));
            customerEmpLink.setCe1Status(c.getString(c.getColumnIndex("CE1_Status")));
            customerEmpLink.setCe1Remark(c.getString(c.getColumnIndex("CE1_Remark")));
            customerEmpLink.setCe1CreateUser(c.getString(c.getColumnIndex("CE1_CreateUser")));
            customerEmpLink.setCe1CreateTime(c.getString(c.getColumnIndex("CE1_CreateTime")));
            customerEmpLink.setCe1ModifyUser(c.getString(c.getColumnIndex("CE1_ModifyUser")));
            customerEmpLink.setCe1ModifyTime(c.getString(c.getColumnIndex("CE1_ModifyTime")));
            customerEmpLink.setCe1RowVersion(c.getString(c.getColumnIndex("CE1_RowVersion")));
            list.add(customerEmpLink);
        }
        return list;
    }

    public CustomerEmpLinkData getCustomerEmpLinkByCeId(String ceId) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM CustomerEmpLink WHERE CE1_ID=? limit 1", new String[]{ceId});
        if (!c.moveToNext()) {
            return null;
        }
        CustomerEmpLinkData customerEmpLink = new CustomerEmpLinkData();
        customerEmpLink.setCe1Id(c.getString(c.getColumnIndex("CE1_ID")));
        customerEmpLink.setCe1M02Id(c.getString(c.getColumnIndex("CE1_M02_ID")));
        customerEmpLink.setCe1Cu1Id(c.getString(c.getColumnIndex("CE1_CU1_ID")));
        customerEmpLink.setCe1Code(c.getString(c.getColumnIndex("CE1_CODE")));
        customerEmpLink.setCe1Name(c.getString(c.getColumnIndex("CE1_Name")));
        customerEmpLink.setCe1EnglishName(c.getString(c.getColumnIndex("CE1_EnglishName")));
        customerEmpLink.setCe1Sex(c.getString(c.getColumnIndex("CE1_Sex")));
        customerEmpLink.setCe1Dp1Id(c.getString(c.getColumnIndex("CE1_DP1_ID")));
        customerEmpLink.setCe1DicIdJob(c.getString(c.getColumnIndex("CE1_Dic_ID_JOB")));
        customerEmpLink.setCe1Phone(c.getString(c.getColumnIndex("CE1_Phone")));
        customerEmpLink.setCe1Status(c.getString(c.getColumnIndex("CE1_Status")));
        customerEmpLink.setCe1Remark(c.getString(c.getColumnIndex("CE1_Remark")));
        customerEmpLink.setCe1CreateUser(c.getString(c.getColumnIndex("CE1_CreateUser")));
        customerEmpLink.setCe1CreateTime(c.getString(c.getColumnIndex("CE1_CreateTime")));
        customerEmpLink.setCe1ModifyUser(c.getString(c.getColumnIndex("CE1_ModifyUser")));
        customerEmpLink.setCe1ModifyTime(c.getString(c.getColumnIndex("CE1_ModifyTime")));
        customerEmpLink.setCe1RowVersion(c.getString(c.getColumnIndex("CE1_RowVersion")));
        return customerEmpLink;
    }

    public boolean addCustomerEmpLink(CustomerEmpLinkData customerEmpLink) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into CustomerEmpLink(CE1_ID,CE1_M02_ID,CE1_CU1_ID,CE1_CODE,CE1_Name,CE1_EnglishName,CE1_Sex,CE1_DP1_ID,CE1_Dic_ID_JOB,CE1_Phone,CE1_Status,CE1_Remark,CE1_CreateUser,CE1_CreateTime,CE1_ModifyUser,CE1_ModifyTime,CE1_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        stat.bindString(1, customerEmpLink.getCe1Id());
        stat.bindString(2, customerEmpLink.getCe1M02Id());
        stat.bindString(3, customerEmpLink.getCe1Cu1Id());
        stat.bindString(4, customerEmpLink.getCe1Code());
        stat.bindString(5, customerEmpLink.getCe1Name());
        stat.bindString(6, customerEmpLink.getCe1EnglishName());
        stat.bindString(7, customerEmpLink.getCe1Sex());
        stat.bindString(8, customerEmpLink.getCe1Dp1Id());
        stat.bindString(9, customerEmpLink.getCe1DicIdJob());
        stat.bindString(10, customerEmpLink.getCe1Phone());
        stat.bindString(11, customerEmpLink.getCe1Status());
        stat.bindString(12, customerEmpLink.getCe1Remark());
        stat.bindString(13, customerEmpLink.getCe1CreateUser());
        stat.bindString(14, customerEmpLink.getCe1CreateTime());
        stat.bindString(15, customerEmpLink.getCe1ModifyUser());
        stat.bindString(16, customerEmpLink.getCe1ModifyTime());
        stat.bindString(17, customerEmpLink.getCe1RowVersion());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchAddCustomerEmpLink(List<CustomerEmpLinkData> list) {
        String insertSql = "insert into CustomerEmpLink(CE1_ID,CE1_M02_ID,CE1_CU1_ID,CE1_CODE,CE1_Name,CE1_EnglishName,CE1_Sex,CE1_DP1_ID,CE1_Dic_ID_JOB,CE1_Phone,CE1_Status,CE1_Remark,CE1_CreateUser,CE1_CreateTime,CE1_ModifyUser,CE1_ModifyTime,CE1_RowVersion)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (CustomerEmpLinkData customerEmpLink : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, customerEmpLink.getCe1Id());
                stat.bindString(2, customerEmpLink.getCe1M02Id());
                stat.bindString(3, customerEmpLink.getCe1Cu1Id());
                stat.bindString(4, customerEmpLink.getCe1Code());
                stat.bindString(5, customerEmpLink.getCe1Name());
                stat.bindString(6, customerEmpLink.getCe1EnglishName());
                stat.bindString(7, customerEmpLink.getCe1Sex());
                stat.bindString(8, customerEmpLink.getCe1Dp1Id());
                stat.bindString(9, customerEmpLink.getCe1DicIdJob());
                stat.bindString(10, customerEmpLink.getCe1Phone());
                stat.bindString(11, customerEmpLink.getCe1Status());
                stat.bindString(12, customerEmpLink.getCe1Remark());
                stat.bindString(13, customerEmpLink.getCe1CreateUser());
                stat.bindString(14, customerEmpLink.getCe1CreateTime());
                stat.bindString(15, customerEmpLink.getCe1ModifyUser());
                stat.bindString(16, customerEmpLink.getCe1ModifyTime());
                stat.bindString(17, customerEmpLink.getCe1RowVersion());
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
        String deleteSql = "DELETE FROM CustomerEmpLink";
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
