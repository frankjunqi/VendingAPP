package com.mc.vending.service;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.TrafficStats;
import android.os.IBinder;
import com.mc.vending.tools.ZillionLog;
import java.util.ArrayList;
import java.util.List;

public class TrafficService extends Service implements Runnable {
    private List<String> packageNames = new ArrayList();
    private Thread traThread;

    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onCreate() {
        super.onCreate();
        ZillionLog.i("TrafficService", "create");
        for (PackageInfo packageInfo : getPackageManager().getInstalledPackages(0)) {
            this.packageNames.add(packageInfo.packageName);
        }
    }

    private int getUid(String packageName) {
        int uid = 0;
        try {
            return getPackageManager().getApplicationInfo(packageName, 1).uid;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
            return uid;
        }
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        this.traThread = new Thread(this);
        this.traThread.start();
    }

    protected void getTraffic() {
        long rxall = TrafficStats.getTotalRxBytes();
        long txall = TrafficStats.getTotalTxBytes();
        ZillionLog.i("TrafficService", "all = " + rxall + "/" + txall + "/" + (rxall + txall));
        for (String packageName : this.packageNames) {
            int uid = getUid(packageName);
            long rxuid = TrafficStats.getUidRxBytes(uid);
            long txuid = TrafficStats.getUidTxBytes(uid);
            long all = rxuid + txuid;
            if (all > 0) {
                ZillionLog.i("TrafficService", new StringBuilder(String.valueOf(packageName)).append("(").append(uid).append(")").append(" = ").append(rxuid).append("/").append(txuid).append("/").append(all).toString());
            }
        }
    }

    public void run() {
        while (true) {
            try {
                getTraffic();
                Thread.currentThread();
                Thread.sleep(3600000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
