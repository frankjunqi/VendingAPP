package com.mc.vending.db;

import com.mc.vending.tools.ZillionLog;

public class VersionDbOper {
    private String checkClumn = "PRAGMA table_info (table_name)";

    public static void exec(String sql) {
        try {
            ZillionLog.i("VersionDbOper", sql);
            AssetsDatabaseManager.getManager().getDatabase().execSQL(sql);
        } catch (Exception e) {
            ZillionLog.e("VersionDbOper", e.getMessage(), e);
        }
    }
}
