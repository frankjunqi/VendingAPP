package com.mc.vending.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import com.mc.vending.db.ReplenishmentHeadDbOper;
import com.mc.vending.db.RetreatHeadDbOper;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.tools.ZillionLog;
import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.helpers.FileWatchdog;

public class TaskService extends Service {
    private static final String exeTime = "01:00";
    private Context context;
    private Handler handler = new Handler();
    private Runnable task = new Runnable() {
        public void run() {
            TaskService.this.handler.postDelayed(this, FileWatchdog.DEFAULT_DELAY);
            Date curr = new Date();
            String startDate = "";
            SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
            Calendar start_c = Calendar.getInstance();
            start_c.setTime(curr);
            start_c.add(2, -1);
            startDate = dft.format(start_c.getTime());
            if (TaskService.exeTime.equals(new SimpleDateFormat("HH:mm").format(curr))) {
                try {
                    TaskService.clearAllCache(TaskService.this.context);
                    TaskService.clearLog();
                    TaskService.this.clearStockTransaction(startDate);
                    TaskService.this.clearReplenishment(startDate);
                    TaskService.this.clearRetreat(startDate);
                } catch (Exception e) {
                    ZillionLog.e(getClass().getName(), e.getMessage(), e);
                }
            }
        }
    };

    public static String getTotalCacheSize(Context context) throws Exception {
        long cacheSize = getFolderSize(context.getCacheDir());
        ZillionLog.i("context.getCacheDir():" + context.getCacheDir());
        if (Environment.getExternalStorageState().equals("mounted")) {
            cacheSize += getFolderSize(context.getExternalCacheDir());
            ZillionLog.i("context.getExternalCacheDir():" + context.getExternalCacheDir());
        }
        return getFormatSize((double) cacheSize);
    }

    public static void clearAllCache(Context context) {
        deleteDir(context.getCacheDir());
        if (Environment.getExternalStorageState().equals("mounted")) {
            deleteDir(context.getExternalCacheDir());
        }
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String file : children) {
                if (!deleteDir(new File(dir, file))) {
                    return false;
                }
            }
        }
        return dir.delete();
    }

    public static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size += getFolderSize(fileList[i]);
                } else {
                    size += fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static String getFormatSize(double size) {
        double kiloByte = size / 1024.0d;
        if (kiloByte < 1.0d) {
            return "0K";
        }
        double megaByte = kiloByte / 1024.0d;
        if (megaByte < 1.0d) {
            return new StringBuilder(String.valueOf(new BigDecimal(Double.toString(kiloByte)).setScale(2, 4).toPlainString())).append("KB").toString();
        }
        double gigaByte = megaByte / 1024.0d;
        if (gigaByte < 1.0d) {
            return new StringBuilder(String.valueOf(new BigDecimal(Double.toString(megaByte)).setScale(2, 4).toPlainString())).append("MB").toString();
        }
        double teraBytes = gigaByte / 1024.0d;
        if (teraBytes < 1.0d) {
            return new StringBuilder(String.valueOf(new BigDecimal(Double.toString(gigaByte)).setScale(2, 4).toPlainString())).append("GB").toString();
        }
        return new StringBuilder(String.valueOf(new BigDecimal(teraBytes).setScale(2, 4).toPlainString())).append("TB").toString();
    }

    public void onCreate() {
        super.onCreate();
        this.context = this;
        this.handler.post(this.task);
    }

    private void reboot() {
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(335544320);
        startActivity(intent);
    }

    private void clearStockTransaction(String startDate) {
        new StockTransactionDbOper().clearStockTransaction(startDate);
    }

    private void clearReplenishment(String startDate) {
        new ReplenishmentHeadDbOper().clearReplenishment(startDate);
    }

    private void clearRetreat(String startDate) {
        new RetreatHeadDbOper().clearRetreat(startDate);
    }

    private static void clearLog() {
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
