package com.mc.vending.tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDBHelper extends SQLiteOpenHelper {
    public static final String CREATE_TABLE = "create table tbl_favorite (restaurant_id text);";
    public static final String DATABASE_NAME = "favorite.db";
    public static final int DATABASE_VERSION = 2;
    public static final String RESTAURANT_ID = "restaurant_id";
    public static final String TABLE_NAME = "tbl_favorite";
    public static final String[] columns = new String[]{RESTAURANT_ID};
    SQLiteDatabase db;
    public ArrayList<String> restaurantIdList;

    public FavoriteDBHelper(Context context, int version) {
        super(context, DATABASE_NAME, null, 2);
        this.db = null;
        this.db = getWritableDatabase();
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists tbl_favorite");
        onCreate(db);
    }

    public void close() {
        if (this.db != null) {
            this.db.close();
        }
    }

    public Boolean queryRestaurant(String[] restaurantIds) {
        Boolean returnFlag = Boolean.valueOf(false);
        Cursor cursor = this.db.query(TABLE_NAME, columns, "restaurant_id=?", restaurantIds, null, null, null);
        if (cursor.moveToNext()) {
            returnFlag = Boolean.valueOf(true);
        }
        if (cursor != null) {
            cursor.close();
        }
        close();
        return returnFlag;
    }

    public List<String> queryAllRestaurant() {
        this.restaurantIdList = new ArrayList();
        Cursor cursor = this.db.query(TABLE_NAME, columns, null, null, null, null, null);
        while (cursor.moveToNext()) {
            this.restaurantIdList.add(cursor.getString(cursor.getColumnIndex(RESTAURANT_ID)));
        }
        if (cursor != null) {
            cursor.close();
        }
        close();
        return this.restaurantIdList;
    }

    public Boolean insertFavorite(String restaurantId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RESTAURANT_ID, restaurantId);
        this.db.insert(TABLE_NAME, null, contentValues);
        close();
        return Boolean.valueOf(true);
    }

    public Boolean deleteFavorite(String restaurantId) {
        String[] args = new String[]{restaurantId};
        this.db.delete(TABLE_NAME, "restaurant_id=? ", args);
        close();
        return Boolean.valueOf(true);
    }
}
