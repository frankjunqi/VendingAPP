package com.mc.vending.activitys;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mc.vending.R;
import com.mc.vending.activitys.pick.MC_NormalPickActivity;
import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.VersionData;
import com.mc.vending.db.AssetsDatabaseManager;
import com.mc.vending.parse.AutherDataParse;
import com.mc.vending.parse.InitDataParse;
import com.mc.vending.parse.VendingDataExistParse;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.service.TaskService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.HttpHelper;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.SystemUiHider;
import com.mc.vending.tools.Tools;
import com.mc.vending.tools.ZillionLog;

public class MainActivity extends Activity implements DataParseRequestListener {
    private Builder adb;
    private TextView alert_msg;
    private TextView alert_msg_title;
    private String configURL;
    private EditText et_server_url;
    private EditText et_vend_code;
    private boolean isUserOperate;
    private RelativeLayout layout_image;
    private RelativeLayout layout_init;
    private SystemUiHider mSystemUiHider;
    private ProgressDialog progressDialog;
    private String pwdKey;
    private String vendCode;

    protected void onCreate(Bundle savedInstanceState) {
        ZillionLog.i("main", "onCreate");
        startTrafficService();
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_main);
        initComponse();
        initObject();
        AssetsDatabaseManager.initManager(this);
        HttpHelper.getHeaderMap();
        ActivityManagerTool.getActivityManager().add(this);
        requestGetClientVersionServer();
        this.isUserOperate = false;
        Toast.makeText(this,"toast",Toast.LENGTH_SHORT).show();
        try {
            TaskService.getTotalCacheSize(this);
        } catch (Exception e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            e.printStackTrace();
        }
        goNormalPickAcitivity();
    }

    private void startTrafficService() {
        startService(new Intent(this, TaskService.class));
    }

    protected void onResume() {
        super.onResume();
    }

    protected void onStart() {
        super.onStart();
    }

    protected void onDestroy() {
        ActivityManagerTool.getActivityManager().removeActivity(this);
        super.onDestroy();
    }

    private void initComponse() {
        this.layout_init = (RelativeLayout) findViewById(R.id.layout_init);
        this.layout_image = (RelativeLayout) findViewById(R.id.layout_image);
        this.et_server_url = (EditText) findViewById(R.id.et_server_url);
        this.et_vend_code = (EditText) findViewById(R.id.et_vend_code);
    }

    private void initObject() {
        Constant.BODY_VALUE_UDID = Tools.readTelephoneSerialNum(this);
        this.layout_image.setVisibility(0);
        this.alert_msg_title = (TextView) findViewById(R.id.alert_msg_title);
        this.alert_msg = (TextView) findViewById(R.id.alert_msg);
        this.et_server_url.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                InputMethodManager inputManager = (InputMethodManager) MainActivity.this.getSystemService("input_method");
                if (!hasFocus) {
                    return;
                }
                if (MainActivity.this.et_server_url == v) {
                    inputManager.showSoftInput(MainActivity.this.et_server_url, 4);
                } else {
                    inputManager.showSoftInput(MainActivity.this.et_vend_code, 4);
                }
            }
        });
        this.configURL = getConfigURL();
        this.pwdKey = getPwdKey();
        this.vendCode = getVendCode();
        Constant.SERVER_URL = this.configURL;
    }

    private void resetAlertMsg(String msg) {
        this.alert_msg.setText(msg);
        this.alert_msg_title.setVisibility(0);
        this.alert_msg.setVisibility(0);
    }

    private void hiddenAlertMsg() {
        this.alert_msg.setText("");
        this.alert_msg_title.setVisibility(4);
        this.alert_msg.setVisibility(4);
    }

    public void initClicked(View view) {
        this.isUserOperate = true;
        if (!Tools.isAccessNetwork(this)) {
            resetAlertMsg("没有链接到网络，请检查网络链接！");
        } else if (StringHelper.isEmpty(this.et_server_url.getText().toString(), true)) {
            resetAlertMsg("请输入服务器地址！");
        } else if (StringHelper.isEmpty(this.et_vend_code.getText().toString(), true)) {
            resetAlertMsg("请输入售货机编号！");
        } else {
            savePwdKey("");
            saveConfigURL("");
            saveVendCode("");
            this.configURL = this.et_server_url.getText().toString();
            this.vendCode = this.et_vend_code.getText().toString();
            this.pwdKey = getPwdKey();
            Constant.SERVER_URL = this.configURL;
            hiddenAlertMsg();
            initPwdKey();
        }
    }

    private void initConfigURL() {
        this.configURL = getConfigURL();
        this.pwdKey = getPwdKey();
        this.vendCode = getVendCode();
        this.et_server_url.setText(this.configURL);
        this.et_vend_code.setText(this.vendCode);
        if (StringHelper.isEmpty(this.configURL, true) || StringHelper.isEmpty(this.pwdKey, true)) {
            this.layout_init.setVisibility(0);
            this.layout_image.setVisibility(4);
            return;
        }
        Constant.SERVER_URL = this.configURL;
        initPwdKey();
    }

    private void initPwdKey() {
        if (StringHelper.isEmpty(this.pwdKey, true)) {
            requestPwd();
            return;
        }
        Constant.BODY_VALUE_PWD = this.pwdKey;
        goNormalPickAcitivity();
    }

    private void goNormalPickAcitivity() {
        Intent intent = new Intent();
        intent.putExtra("vendCode", this.vendCode);
        intent.setClass(this, MC_NormalPickActivity.class);
        startActivity(intent);
        finish();
    }

    private String getConfigURL() {
        return getSharedPreferences(Constant.SHARED_CONFIG, 0).getString(Constant.SHARED_CONFIG_URL, "");
    }

    private void saveConfigURL(String key) {
        getSharedPreferences(Constant.SHARED_CONFIG, 0).edit().putString(Constant.SHARED_CONFIG_URL, key).commit();
    }

    private String getVendCode() {
        return getSharedPreferences(Constant.SHARED_VEND_CODE_KEY, 0).getString(Constant.SHARED_VEND_CODE, "");
    }

    private void saveVendCode(String key) {
        getSharedPreferences(Constant.SHARED_VEND_CODE_KEY, 0).edit().putString(Constant.SHARED_VEND_CODE, key).commit();
    }

    private String getPwdKey() {
        return getSharedPreferences("pwd", 0).getString(Constant.SHARED_PWD_KEY, "");
    }

    private void savePwdKey(String key) {
        getSharedPreferences("pwd", 0).edit().putString(Constant.SHARED_PWD_KEY, key).commit();
    }

    private void requestPwd() {
        AutherDataParse parse = new AutherDataParse();
        parse.setListener(this);
        parse.requestAutherData(Constant.HTTP_OPERATE_TYPE_DESGET, Constant.METHOD_WSID_AUTHER, Tools.readTelephoneSerialNum(this));
    }

    public void requestVendingExistParse() {
        VendingDataExistParse parse = new VendingDataExistParse();
        parse.setListener(this);
        parse.requestVendingExistData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VENDING, this.vendCode, false);
    }

    public void parseRequestFinised(BaseData baseData) {
        if (!baseData.isSuccess().booleanValue() && Constant.METHOD_WSID_CHECK_INIT.equals(baseData.getRequestURL())) {
            resetAlertMsg("售货机已经初始化，不允许重复初始化，请与管理员联系！");
        } else if (!baseData.isSuccess().booleanValue()) {
        } else {
            if (Constant.METHOD_WSID_AUTHER.equals(baseData.getRequestURL())) {
                this.pwdKey = (String) baseData.getUserObject();
                requestVendingExistParse();
            } else if (Constant.METHOD_WSID_VERSION.equals(baseData.getRequestURL())) {
                processUpdate(baseData);
            } else if (Constant.METHOD_WSID_VENDING.equals(baseData.getRequestURL())) {
                if (!((Boolean) baseData.getUserObject()).booleanValue()) {
                    this.layout_image.setVisibility(4);
                    resetAlertMsg("售货机编号不正确，请联系系统管理员");
                } else if (this.isUserOperate) {
                    requestInitPermission();
                } else {
                    savePwdKey(this.pwdKey);
                    saveConfigURL(this.configURL);
                    saveVendCode(this.vendCode);
                    Constant.BODY_VALUE_PWD = this.pwdKey;
                    initPwdKey();
                }
            } else if (Constant.METHOD_WSID_CHECK_INIT.equals(baseData.getRequestURL())) {
                savePwdKey(this.pwdKey);
                saveConfigURL(this.configURL);
                saveVendCode(this.vendCode);
                Constant.BODY_VALUE_PWD = this.pwdKey;
                initPwdKey();
            }
        }
    }

    private void processUpdate(BaseData baseData) {
        try {
            ZillionLog.i("processUpdate");
            String version = ((VersionData) baseData.getUserObject()).getVersion().replace(".", "");
            String locaVersion = Constant.HEADER_VALUE_CLIENTVER.replace(".", "");
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    MainActivity.this.initConfigURL();
                }
            }, 10);
        } catch (Exception e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            initConfigURL();
        }
    }

    public void parseRequestFailure(BaseData baseData) {
        ZillionLog.i("main parseRequestFailure==>" + ((String) Constant.WSIDNAMEMAP.get(baseData.getRequestURL())));
        this.layout_image.setVisibility(4);
        if (baseData.HTTP_STATUS != 0) {
            resetAlertMsg("服务器连接异常，请检查网络连接或服务器地址是否正确！");
        } else if (Constant.METHOD_WSID_VERSION.equals(baseData.getRequestURL())) {
            initConfigURL();
        } else if (Constant.METHOD_WSID_AUTHER.equals(baseData.getRequestURL())) {
            resetAlertMsg("认证连接异常，请检查网络连接或服务器地址是否正确！");
        } else if (Constant.METHOD_WSID_VENDING.equals(baseData.getRequestURL())) {
            if (!((Boolean) baseData.getUserObject()).booleanValue()) {
                resetAlertMsg("售货机编号不正确，请联系系统管理员");
            }
        } else if (Constant.METHOD_WSID_CHECK_INIT.equals(baseData.getRequestURL())) {
            resetAlertMsg("请求初始化权限失败！");
        }
    }

    private void requestGetClientVersionServer() {
        if (!Tools.isAccessNetwork(this)) {
            initConfigURL();
        } else if (StringHelper.isEmpty(this.configURL, true)) {
            this.layout_image.setVisibility(4);
        } else {
            initConfigURL();
        }
    }

    public void finishClicked(View view) {
        new Builder(this).setMessage(getString(R.string.PUBLIC_EXIST_TITLE)).setPositiveButton(getString(R.string.PUBLIC_EXIST), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finishApp();
            }
        }).setNegativeButton(getString(R.string.PUBLIC_CANCEL), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();
    }

    private void finishApp() {
        ActivityManagerTool.getActivityManager().exit();
    }

    private void requestInitPermission() {
        InitDataParse parse = new InitDataParse();
        parse.setListener(this);
        parse.requestInitData(Constant.HTTP_OPERATE_TYPE_CHECK, Constant.METHOD_WSID_CHECK_INIT, this.et_vend_code.getText().toString());
    }
}
