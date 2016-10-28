package com.mc.vending.db;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import com.mc.vending.config.MC_Config;
import com.mc.vending.tools.ZillionLog;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class AssetsDatabaseManager {
    private static String databasepath = "/data/data/%s/database";
    private static AssetsDatabaseManager mInstance = null;
    private static String tag = "AssetsDatabase";
    private Context context = null;
    private Map<String, SQLiteDatabase> databases = new HashMap();

    public static void initManager(Context context) {
        if (mInstance == null) {
            mInstance = new AssetsDatabaseManager(context);
        }
    }

    public static AssetsDatabaseManager getManager() {
        return mInstance;
    }

    private AssetsDatabaseManager(Context context) {
        this.context = context;
    }

    public void delDatabase() {
        try {
            File file = new File(getDatabaseFile(MC_Config.SQLITE_DATABASE_NAME));
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
        }
    }

    public SQLiteDatabase getDatabase(String dbfile) {
        dbfile = MC_Config.SQLITE_DATABASE_NAME;
        if (this.databases.get(dbfile) != null) {
            return (SQLiteDatabase) this.databases.get(dbfile);
        }
        if (this.context == null) {
            return null;
        }
        String spath = getDatabaseFilepath();
        String sfile = getDatabaseFile(dbfile);
        File file = new File(sfile);
        SharedPreferences dbs = this.context.getSharedPreferences(AssetsDatabaseManager.class.toString(), 0);
        if (!(dbs.getBoolean(dbfile, false) && file.exists())) {
            file = new File(spath);
            if ((!file.exists() && !file.mkdirs()) || !copyAssetsToFilesystem(dbfile, sfile)) {
                return null;
            }
            dbs.edit().putBoolean(dbfile, true).commit();
        }
        SQLiteDatabase db = SQLiteDatabase.openDatabase(sfile, null, 16);
        if (db != null) {
            this.databases.put(dbfile, db);
        }
        return db;
    }

    public SQLiteDatabase getDatabase() {
        String dbfile = MC_Config.SQLITE_DATABASE_NAME;
        if (this.databases.get(dbfile) != null) {
            return (SQLiteDatabase) this.databases.get(dbfile);
        }
        if (this.context == null) {
            return null;
        }
        String spath = getDatabaseFilepath();
        String sfile = getDatabaseFile(dbfile);
        File file = new File(sfile);
        SharedPreferences dbs = this.context.getSharedPreferences(AssetsDatabaseManager.class.toString(), 0);
        if (!(dbs.getBoolean(dbfile, false) && file.exists())) {
            file = new File(spath);
            if ((!file.exists() && !file.mkdirs()) || !copyAssetsToFilesystem(dbfile, sfile)) {
                return null;
            }
            dbs.edit().putBoolean(dbfile, true).commit();
        }
        SQLiteDatabase db = SQLiteDatabase.openDatabase(sfile, null, 16);
        if (db != null) {
            this.databases.put(dbfile, db);
        }
        return db;
    }

    private String getDatabaseFilepath() {
        return String.format(databasepath, new Object[]{this.context.getApplicationInfo().packageName});
    }

    private String getDatabaseFile(String dbfile) {
        return getDatabaseFilepath() + "/" + dbfile;
    }

    private boolean copyAssetsToFilesystem(String assetsSrc, String des) {
        Exception e;
        InputStream istream = null;
        OutputStream ostream = null;
        try {
            istream = this.context.getAssets().open(assetsSrc);
            OutputStream ostream2 = new FileOutputStream(des);
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    int length = istream.read(buffer);
                    if (length <= 0) {
                        istream.close();
                        ostream2.close();
                        ostream = ostream2;
                        return true;
                    }
                    ostream2.write(buffer, 0, length);
                }
            } catch (Exception e2) {
                e = e2;
                ostream = ostream2;
                ZillionLog.e(getClass().getName(), e.getMessage(), e);
                e.printStackTrace();
                if (istream != null) {
                    try {
                        istream.close();
                    } catch (Exception ee) {
                        ZillionLog.e(getClass().getName(), ee.getMessage(), ee);
                        ee.printStackTrace();
                        return false;
                    }
                }
                if (ostream != null) {
                    return false;
                }
                ostream.close();
                return false;
            }
        } catch (Exception e3) {
            e = e3;
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            e.printStackTrace();
            if (istream != null) {
                try {
                    istream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (ostream != null) {
                return false;
            }
            try {
                ostream.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return false;
        }
    }

    public boolean closeDatabase(String dbfile) {
        if (this.databases.get(dbfile) == null) {
            return false;
        }
        ((SQLiteDatabase) this.databases.get(dbfile)).close();
        this.databases.remove(dbfile);
        return true;
    }

    public static void closeAllDatabase() {
        if (mInstance != null) {
            for (int i = 0; i < mInstance.databases.size(); i++) {
                if (mInstance.databases.get(Integer.valueOf(i)) != null) {
                    ((SQLiteDatabase) mInstance.databases.get(Integer.valueOf(i))).close();
                }
            }
            mInstance.databases.clear();
        }
    }
}
