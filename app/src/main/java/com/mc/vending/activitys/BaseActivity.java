package com.mc.vending.activitys;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.mc.vending.R;
import com.mc.vending.data.BaseData;
import com.mc.vending.db.AssetsDatabaseManager;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.SystemUiHider;
import com.mc.vending.tools.SystemUiHider.OnVisibilityChangeListener;
import com.mc.vending.tools.Tools;
import com.mc.vending.tools.ZillionLog;

public class BaseActivity extends Activity {
    protected static final int ERROR = 9999;
    protected static final int FAILURE = 8888;
    protected static final int SUCCESS = 0;
    protected static boolean isNetworkEnabled = false;
    public static Boolean isPostClientInfo = Boolean.valueOf(false);
    protected static ProgressDialog progressDialog;
    private Builder adb;
    private Thread downLoadData;
    private Boolean isMainPage = Boolean.valueOf(true);
    protected boolean isSuccessRequest;
    protected SystemUiHider mSystemUiHider;
    private BaseData postClientData;
    private Toast toast;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        AssetsDatabaseManager.initManager(this);
    }

    protected void setIsMainPage(Boolean isMainPage) {
        this.isMainPage = isMainPage;
    }

    public void sendAlertMessage(String message) {
        try {
            this.adb = new Builder(this);
            this.adb.setTitle(R.string.alert_msg_title);
            this.adb.setMessage(message);
            this.adb.setNegativeButton(getString(R.string.PUBLIC_OK), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            this.adb.show();
        } catch (Exception e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void startActivity(Intent intent) {
        startLoading();
        super.startActivity(intent);
    }

    public void startLoading() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
        }
        try {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage(getString(R.string.PUBLIC_LOADING));
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setProgressStyle(0);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(true);
            progressDialog.show();
        } catch (Exception e2) {
            ZillionLog.e(getClass().getName(), e2.getMessage(), e2);
            e2.printStackTrace();
        }
    }

    public void stopLoading() {
        try {
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public boolean requestServer() {
        this.isSuccessRequest = false;
        if (Tools.isAccessNetwork(this)) {
            startLoading();
            return true;
        }
        sendAlertMessage(getString(R.string.PUBLIC_NETWORK_ERROR));
        return false;
    }

    public boolean isAccessNetwork() {
        if (Tools.isAccessNetwork(this)) {
            return true;
        }
        return false;
    }

    protected boolean responseServer() {
        stopLoading();
        this.isSuccessRequest = true;
        return true;
    }

    protected boolean responseServer(BaseData data) {
        stopLoading();
        if (data.HTTP_STATUS != 0) {
            sendAlertMessage(getString(R.string.PUBLIC_REQUEST_SERVER_ERROR));
            return false;
        }
        this.isSuccessRequest = true;
        return true;
    }

    public void call(String phoneNo) {
        startActivity(new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNo)));
    }

    public void showToast(String message) {
        this.toast = Toast.makeText(getApplicationContext(), message, 1);
        this.toast.setGravity(17, 0, 0);
        this.toast.show();
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    protected void onPause() {
        super.onPause();
    }

    public void showExitDialog() {
        new Builder(this).setMessage(getString(R.string.PUBLIC_EXIST_TITLE)).setPositiveButton(getString(R.string.PUBLIC_EXIST), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                ActivityManagerTool.getActivityManager().exit();
                BaseActivity.this.finish();
            }
        }).setNegativeButton(getString(R.string.PUBLIC_CANCEL), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    public void hiddenSystemUI(View contentView) {
        this.mSystemUiHider = SystemUiHider.getInstance(this, contentView, 6);
        this.mSystemUiHider.setup();
        this.mSystemUiHider.hide();
        this.mSystemUiHider.setOnVisibilityChangeListener(new OnVisibilityChangeListener() {
            public void onVisibilityChange(boolean visible) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    ZillionLog.e(getClass().getName(), e.getMessage(), e);
                    e.printStackTrace();
                }
                BaseActivity.this.mSystemUiHider.hide();
            }
        });
    }

    public void hiddenKeyBoard(EditText et) {
        ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(et.getWindowToken(), 0);
    }
}
