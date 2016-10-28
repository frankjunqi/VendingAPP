package com.mc.vending.db;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import com.mc.vending.data.CardData;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.List;

public class CardDbOper {
    public List<CardData> findAll() {
        List<CardData> list = new ArrayList();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT * FROM Card", null);
        while (c.moveToNext()) {
            CardData card = new CardData();
            card.setCd1Id(c.getString(c.getColumnIndex("CD1_ID")));
            card.setCd1M02Id(c.getString(c.getColumnIndex("CD1_M02_ID")));
            card.setCd1SerialNo(c.getString(c.getColumnIndex("CD1_SerialNo")));
            card.setCd1Code(c.getString(c.getColumnIndex("CD1_CODE")));
            card.setCd1Type(c.getString(c.getColumnIndex("CD1_Type")));
            card.setCd1Password(c.getString(c.getColumnIndex("CD1_Password")));
            card.setCd1Purpose(c.getString(c.getColumnIndex("CD1_Purpose")));
            card.setCd1Status(c.getString(c.getColumnIndex("CD1_Status")));
            card.setCd1CustomerStatus(c.getString(c.getColumnIndex("CD1_CustomerStatus")));
            card.setCd1ProductPower(c.getString(c.getColumnIndex("CD1_ProductPower")));
            card.setCd1CreateUser(c.getString(c.getColumnIndex("CD1_CreateUser")));
            card.setCd1CreateTime(c.getString(c.getColumnIndex("CD1_CreateTime")));
            card.setCd1ModifyUser(c.getString(c.getColumnIndex("CD1_ModifyUser")));
            card.setCd1ModifyTime(c.getString(c.getColumnIndex("CD1_ModifyTime")));
            card.setCd1RowVersion(c.getString(c.getColumnIndex("CD1_RowVersion")));
            list.add(card);
        }
        return list;
    }

    public CardData getCardById(String cardId) {
        CardData cardData = new CardData();
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT cd1_serialNo,cd1_password FROM Card where cd1_id=?", new String[]{cardId});
        while (c.moveToNext()) {
            cardData.setCd1SerialNo(c.getString(c.getColumnIndex("CD1_SerialNo")));
            cardData.setCd1Password(c.getString(c.getColumnIndex("CD1_Password")));
        }
        return cardData;
    }

    public CardData getCardBySerialNo(String cardSerialNo) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT CD1_ID,CD1_SerialNo,CD1_CODE,CD1_Type,CD1_Password,CD1_Purpose,CD1_Status,CD1_CustomerStatus,CD1_ProductPower FROM Card WHERE (CD1_SerialNo=? or CD1_Password=?) limit 1", new String[]{cardSerialNo, cardSerialNo});
        if (!c.moveToNext()) {
            return null;
        }
        CardData card = new CardData();
        card.setCd1Id(c.getString(c.getColumnIndex("CD1_ID")));
        card.setCd1SerialNo(c.getString(c.getColumnIndex("CD1_SerialNo")));
        card.setCd1Code(c.getString(c.getColumnIndex("CD1_CODE")));
        card.setCd1Type(c.getString(c.getColumnIndex("CD1_Type")));
        card.setCd1Password(c.getString(c.getColumnIndex("CD1_Password")));
        card.setCd1Purpose(c.getString(c.getColumnIndex("CD1_Purpose")));
        card.setCd1Status(c.getString(c.getColumnIndex("CD1_Status")));
        card.setCd1CustomerStatus(c.getString(c.getColumnIndex("CD1_CustomerStatus")));
        card.setCd1ProductPower(c.getString(c.getColumnIndex("CD1_ProductPower")));
        return card;
    }

    public CardData getCardByPassword(String password) {
        Cursor c = AssetsDatabaseManager.getManager().getDatabase().rawQuery("SELECT CD1_ID,CD1_SerialNo,CD1_CODE,CD1_Type,CD1_Password,CD1_Purpose,CD1_Status,CD1_CustomerStatus,CD1_ProductPower FROM Card WHERE CD1_Password=? limit 1", new String[]{password});
        if (!c.moveToNext()) {
            return null;
        }
        CardData card = new CardData();
        card.setCd1Id(c.getString(c.getColumnIndex("CD1_ID")));
        card.setCd1SerialNo(c.getString(c.getColumnIndex("CD1_SerialNo")));
        card.setCd1Code(c.getString(c.getColumnIndex("CD1_CODE")));
        card.setCd1Type(c.getString(c.getColumnIndex("CD1_Type")));
        card.setCd1Password(c.getString(c.getColumnIndex("CD1_Password")));
        card.setCd1Purpose(c.getString(c.getColumnIndex("CD1_Purpose")));
        card.setCd1Status(c.getString(c.getColumnIndex("CD1_Status")));
        card.setCd1CustomerStatus(c.getString(c.getColumnIndex("CD1_CustomerStatus")));
        card.setCd1ProductPower(c.getString(c.getColumnIndex("CD1_ProductPower")));
        return card;
    }

    public boolean addCard(CardData card) {
        SQLiteStatement stat = AssetsDatabaseManager.getManager().getDatabase().compileStatement("insert into Card(CD1_ID,CD1_M02_ID,CD1_SerialNo,CD1_CODE,CD1_Type,CD1_Password,CD1_Purpose,CD1_Status,CD1_CustomerStatus,CD1_CreateUser,CD1_CreateTime,CD1_ModifyUser,CD1_ModifyTime,CD1_RowVersion,CD1_ProductPower)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
        stat.bindString(1, card.getCd1Id());
        stat.bindString(2, card.getCd1M02Id());
        stat.bindString(3, card.getCd1SerialNo());
        stat.bindString(4, card.getCd1Code());
        stat.bindString(5, card.getCd1Type());
        stat.bindString(6, card.getCd1Password());
        stat.bindString(7, card.getCd1Purpose());
        stat.bindString(8, card.getCd1Status());
        stat.bindString(9, card.getCd1CustomerStatus());
        stat.bindString(10, card.getCd1CreateUser());
        stat.bindString(11, card.getCd1CreateTime());
        stat.bindString(12, card.getCd1ModifyUser());
        stat.bindString(13, card.getCd1ModifyTime());
        stat.bindString(14, card.getCd1RowVersion());
        stat.bindString(15, card.getCd1ProductPower());
        if (stat.executeInsert() > 0) {
            return true;
        }
        return false;
    }

    public boolean batchAddCard(List<CardData> list) {
        String insertSql = "insert into Card(CD1_ID,CD1_M02_ID,CD1_SerialNo,CD1_CODE,CD1_Type,CD1_Password,CD1_Purpose,CD1_Status,CD1_CustomerStatus,CD1_CreateUser,CD1_CreateTime,CD1_ModifyUser,CD1_ModifyTime,CD1_RowVersion,CD1_ProductPower)values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = AssetsDatabaseManager.getManager().getDatabase();
        try {
            db.beginTransaction();
            for (CardData card : list) {
                SQLiteStatement stat = db.compileStatement(insertSql);
                stat.bindString(1, card.getCd1Id());
                stat.bindString(2, card.getCd1M02Id());
                stat.bindString(3, card.getCd1SerialNo());
                stat.bindString(4, card.getCd1Code());
                stat.bindString(5, card.getCd1Type());
                stat.bindString(6, card.getCd1Password());
                stat.bindString(7, card.getCd1Purpose());
                stat.bindString(8, card.getCd1Status());
                stat.bindString(9, card.getCd1CustomerStatus());
                stat.bindString(10, card.getCd1CreateUser());
                stat.bindString(11, card.getCd1CreateTime());
                stat.bindString(12, card.getCd1ModifyUser());
                stat.bindString(13, card.getCd1ModifyTime());
                stat.bindString(14, card.getCd1RowVersion());
                stat.bindString(15, card.getCd1ProductPower());
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
        String deleteSql = "DELETE FROM Card";
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
