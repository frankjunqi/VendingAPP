package com.mc.vending.tools;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

public class CreatShortCut {
    public boolean hasShortcut(Activity con, String appName) {
        boolean isInstallShortcut = false;
        String AUTHORITY = "com.android.launcher.settings";
        Cursor c = con.getContentResolver().query(Uri.parse("content://com.android.launcher.settings/favorites?notify=true"), new String[]{"title"}, "title=?", new String[]{appName.trim()}, null);
        if (c != null && c.getCount() > 0) {
            isInstallShortcut = true;
        }
        if (c != null) {
            try {
                c.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("异常", "在关闭资源的时候----CreatShortCut类的64行");
            }
        }
        return isInstallShortcut;
    }

    public void delShortcut(Activity con, String appName) {
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        shortcut.putExtra("android.intent.extra.shortcut.NAME", appName);
        shortcut.putExtra("android.intent.extra.shortcut.INTENT", new Intent("android.intent.action.MAIN").setComponent(new ComponentName(con.getPackageName(), con.getPackageName() + "." + con.getLocalClassName())));
        con.sendBroadcast(shortcut);
    }
}
