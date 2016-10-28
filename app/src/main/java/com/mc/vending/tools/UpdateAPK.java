package com.mc.vending.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.config.Constant;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UpdateAPK {
    private static final String TAG = "AutoUpdate";
    public Activity activity = null;
    String cacheFilePath;
    String checkApk;
    private String currentTempFilePath = "";
    private ProgressDialog dialog;
    private String fileEx = "";
    private String fileNa = "";
    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                UpdateAPK.this.pb.setProgress((int) ((UpdateAPK.this.length * 100) / UpdateAPK.this.totalLength));
                UpdateAPK.this.num.setText(new StringBuilder(String.valueOf((UpdateAPK.this.length * 100) / UpdateAPK.this.totalLength)).append("%  ").append(String.format("%1$04.2f", new Object[]{Float.valueOf(((float) UpdateAPK.this.length) / 1024.0f)})).append("KB/").append(String.format("%1$04.2f", new Object[]{Float.valueOf(((float) UpdateAPK.this.totalLength) / 1024.0f)})).append("KB").toString());
            }
        }
    };
    long length;
    TextView num;
    ProgressBar pb;
    public String strURL;
    long totalLength;
    Button tv;
    public int versionCode = 0;
    public String versionName = "";

    public UpdateAPK(Activity activity, String strURL) {
        this.activity = activity;
        this.pb = (ProgressBar) activity.findViewById(R.id.progress);
        this.num = (TextView) activity.findViewById(R.id.progressNum);
        this.strURL = strURL;
        this.cacheFilePath = Constant.DOWNLOAD_URL;
    }

    public void check() {
        if (isNetworkAvailable(this.activity)) {
            downloadTheFile(this.strURL);
            Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")).intValue();
        }
    }

    private void uploadCheckApk() {
        this.checkApk = new StringBuilder(String.valueOf(this.strURL.substring(0, this.strURL.lastIndexOf("/")))).append("/vendingCheck.apk").toString();
        ZillionLog.i(this.checkApk);
        try {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        URLConnection conn = new URL(UpdateAPK.this.checkApk).openConnection();
                        conn.setDoInput(true);
                        conn.connect();
                        InputStream is = conn.getInputStream();
                        UpdateAPK.this.totalLength = (long) conn.getContentLength();
                        if (UpdateAPK.this.totalLength < 0) {
                            UpdateAPK.this.totalLength = 1887437;
                        }
                        if (is == null) {
                            throw new RuntimeException("stream is null");
                        }
                        File path = new File(UpdateAPK.this.cacheFilePath);
                        if (!path.exists()) {
                            path.mkdirs();
                        }
                        File myTempFile = new File(new StringBuilder(String.valueOf(UpdateAPK.this.cacheFilePath)).append("vendingCheck.apk").toString());
                        UpdateAPK.this.currentTempFilePath = myTempFile.getAbsolutePath();
                        FileOutputStream fos = new FileOutputStream(myTempFile);
                        byte[] buf = new byte[1024];
                        while (true) {
                            int numread = is.read(buf);
                            if (numread <= 0) {
                                UpdateAPK.this.openFile(myTempFile);
                                try {
                                    is.close();
                                    return;
                                } catch (Exception ex) {
                                    Log.e(UpdateAPK.TAG, "getDataSource() error: " + ex.getMessage(), ex);
                                    return;
                                }
                            }
                            fos.write(buf, 0, numread);
                            UpdateAPK.this.length = myTempFile.length();
                            UpdateAPK.this.handler.sendMessage(UpdateAPK.this.handler.obtainMessage(1));
                        }
                    } catch (Exception e) {
                        Log.e(UpdateAPK.TAG, e.getMessage(), e);
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            this.activity.setResult(9998, new Intent());
            this.activity.finish();
        }
    }

    public static boolean isNetworkAvailable(Context ctx) {
        try {
            NetworkInfo info = ((ConnectivityManager) ctx.getSystemService("connectivity")).getActiveNetworkInfo();
            if (info == null || !info.isConnected()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void showUpdateDialog() {
        AlertDialog alert = new Builder(this.activity).setTitle("提示").setIcon(R.drawable.ic_launcher).setMessage("是否现在更新？?").setPositiveButton("立即更新", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                UpdateAPK.this.downloadTheFile(UpdateAPK.this.strURL);
            }
        }).setNegativeButton("取消", new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        }).show();
    }

    public void showWaitDialog() {
        this.dialog = new ProgressDialog(this.activity);
        this.dialog.setMessage("等待更新...");
        this.dialog.setIndeterminate(true);
        this.dialog.setCancelable(true);
        this.dialog.show();
    }

    public void getCurrentVersion() {
        try {
            PackageInfo info = this.activity.getPackageManager().getPackageInfo(this.activity.getPackageName(), 0);
            this.versionCode = info.versionCode;
            this.versionName = info.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void downloadTheFile(final String strPath) {
        this.fileEx = this.strURL.substring(this.strURL.lastIndexOf(".") + 1, this.strURL.length()).toLowerCase();
        this.fileNa = this.strURL.substring(this.strURL.lastIndexOf("/") + 1, this.strURL.length());
        try {
            new Thread(new Runnable() {
                public void run() {
                    try {
                        Tools.deleteAllFile(UpdateAPK.this.cacheFilePath);
                        UpdateAPK.this.doDownloadTheFile(strPath);
                    } catch (Exception e) {
                        Log.e(UpdateAPK.TAG, e.getMessage(), e);
                    }
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
            this.activity.setResult(9998, new Intent());
            this.activity.finish();
        }
    }

    private void doDownloadTheFile(String strPath) throws Exception {
        if (URLUtil.isNetworkUrl(strPath)) {
            URLConnection conn = new URL(strPath).openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            this.totalLength = (long) conn.getContentLength();
            if (this.totalLength < 0) {
                this.totalLength = 1887437;
            }
            if (is == null) {
                throw new RuntimeException("stream is null");
            }
            File path = new File(this.cacheFilePath);
            if (!path.exists()) {
                path.mkdirs();
            }
            File myTempFile = new File(this.cacheFilePath + this.fileNa);
            this.currentTempFilePath = myTempFile.getAbsolutePath();
            FileOutputStream fos = new FileOutputStream(myTempFile);
            byte[] buf = new byte[1024];
            while (true) {
                int numread = is.read(buf);
                if (numread <= 0) {
                    openFile(myTempFile);
                    try {
                        is.close();
                        return;
                    } catch (Exception ex) {
                        Log.e(TAG, "getDataSource() error: " + ex.getMessage(), ex);
                        return;
                    }
                }
                fos.write(buf, 0, numread);
                this.length = myTempFile.length();
                this.handler.sendMessage(this.handler.obtainMessage(1));
            }
        } else {
            this.activity.setResult(9998, new Intent());
            this.activity.finish();
        }
    }

    private void openFile(File f) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(f), getMIMEType(f));
        this.activity.startActivity(intent);
        this.activity.finish();
    }

    public void delFile() {
        File myFile = new File(this.currentTempFilePath);
        if (myFile.exists()) {
            myFile.delete();
        }
    }

    private String getMIMEType(File f) {
        return "application/vnd.android.package-archive";
    }
}
