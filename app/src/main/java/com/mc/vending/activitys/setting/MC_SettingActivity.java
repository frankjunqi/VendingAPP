package com.mc.vending.activitys.setting;

import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.activitys.pick.MC_NormalPickActivity;
import com.mc.vending.adapter.MC_SetAdapter;
import com.mc.vending.config.Constant;
import com.mc.vending.config.MC_Config;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingData;
import com.mc.vending.db.AssetsDatabaseManager;
import com.mc.vending.db.StockTransactionDbOper;
import com.mc.vending.db.VendingDbOper;
import com.mc.vending.parse.InitDataParse;
import com.mc.vending.parse.StockTransactionDataParse;
import com.mc.vending.parse.VendingChnStockDataParse;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.parse.listener.RequestDataFinishListener;
import com.mc.vending.service.DataServices;
import com.mc.vending.service.DataServices.ServiceBinder;
import com.mc.vending.service.ReplenishmentService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.ZillionLog;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.SerialTools;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MC_SettingActivity extends BaseActivity implements MC_SerialToolsListener, RequestDataFinishListener, DataParseRequestListener {
    public static final int SET_DifferenceReplenishAll = 12;
    public static final int SET_DifferenceReplenishment = 2;
    public static final int SET_ERROR = 0;
    public static final int SET_Inventory = 4;
    public static final int SET_PickTest = 7;
    public static final int SET_ReturnsForward = 5;
    public static final int SET_ReturnsReverse = 6;
    public static final int SET_SELECT = 998;
    public static final int SET_START_LOADING = 10;
    public static final int SET_SUCCESS = 999;
    public static final int SET_Synchronous = 9;
    public static final int SET_SynchronousStock = 8;
    public static final int SET_UrgentReplenishment = 3;
    public static final int SET_replenishAll = 11;
    public static final int SET_replenishment = 1;
    private MC_SetAdapter adapter;
    private TextView alert_msg;
    private TextView alert_msg_title;
    private Button back;
    private ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            MC_SettingActivity.this.dataServices = ((ServiceBinder) service).getService();
            MC_SettingActivity.this.startSynData();
        }

        public void onServiceDisconnected(ComponentName name) {
            MC_SettingActivity.this.dataServices = null;
        }
    };
    private List<String> dataList;
    private DataServices dataServices;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MC_SettingActivity.this.stopLoading();
                    MC_SettingActivity.this.resetAlertMsg((String) msg.obj);
                    return;
                case 1:
                    MC_SettingActivity.this.stopLoading();
                    MC_SettingActivity.this.resetAlertMsg((String) msg.obj);
                    return;
                case 9:
                    MC_SettingActivity.this.startService();
                    return;
                case 10:
                    MC_SettingActivity.this.stopLoading();
                    MC_SettingActivity.this.resetAlertMsg("数据同步完成");
                    return;
                case 11:
                    MC_SettingActivity.this.stopLoading();
                    MC_SettingActivity.this.resetAlertMsg((String) msg.obj);
                    return;
                default:
                    return;
            }
        }
    };
    private ListView listView;
    private TimerTask mTimerTask;
    private final int normalPickPlayerTimer = 1000;
    private final int normalPickTimeCount = 180000;
    private int normalPickTimeOut;
    private TimerTask task;
    private Timer timer;
    private TextView tv_public_title;
    private VendingData vendData;
    private VendingCardPowerWrapperData wrapperData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ActivityManagerTool.getActivityManager().add(this);
        initParam();
        initComponents();
        initObject();
        this.adapter = new MC_SetAdapter(this, this.dataList, this.listView);
        this.listView.setAdapter(this.adapter);
        stopLoading();
    }

    private void initParam() {
        this.wrapperData = (VendingCardPowerWrapperData) getIntent().getSerializableExtra("wrapperData");
        this.vendData = (VendingData) getIntent().getSerializableExtra("vendData");
    }

    private void initComponents() {
        this.tv_public_title = (TextView) findViewById(R.id.tv_public_title);
        this.back = (Button) findViewById(R.id.back);
        this.listView = (ListView) findViewById(R.id.listView);
        this.alert_msg_title = (TextView) findViewById(R.id.alert_msg_title);
        this.alert_msg = (TextView) findViewById(R.id.alert_msg);
    }

    private void initObject() {
        LayoutParams lp = (LayoutParams) this.tv_public_title.getLayoutParams();
        lp.addRule(11);
        lp.setMargins(150, 5, 20, 5);
        this.tv_public_title.setLayoutParams(lp);
        this.tv_public_title.setTextSize(25.0f);
        this.back.setVisibility(0);
        this.back.setEnabled(true);
        this.dataList = new ArrayList();
        this.dataList.add(getResources().getString(R.string.set_replenishment));
        this.dataList.add(getResources().getString(R.string.set_difference_replenishment));
        this.dataList.add(getResources().getString(R.string.set_urgent_replenishment));
        this.dataList.add(getResources().getString(R.string.set_replenishAll));
        this.dataList.add(getResources().getString(R.string.set_difference_replenishAll));
        this.dataList.add(getResources().getString(R.string.set_inventory));
        this.dataList.add(getResources().getString(R.string.set_returns_forward_list));
        this.dataList.add(getResources().getString(R.string.set_returns_reverse));
        this.dataList.add(getResources().getString(R.string.set_pick_test));
        this.dataList.add(getResources().getString(R.string.set_synchronous_stock));
        this.dataList.add(getResources().getString(R.string.set_synchronous));
        this.dataList.add(getResources().getString(R.string.set_init));
        this.dataList.add(getResources().getString(R.string.set_finish));
        this.tv_public_title.setText(new StringBuilder(String.valueOf(getResources().getString(R.string.set_vending_number))).append(this.vendData.getVd1Code()).toString());
        this.listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
                MC_SettingActivity.this.hiddenAlertMsg();
                MC_SettingActivity.this.resetTimer();
                switch (position) {
                    case 0:
                        MC_SettingActivity.this.setReplenishment();
                        return;
                    case 1:
                        MC_SettingActivity.this.setDifferenceReplenishment();
                        return;
                    case 2:
                        MC_SettingActivity.this.setUrgentReplenishment();
                        return;
                    case 3:
                        MC_SettingActivity.this.setReplenishAll();
                        return;
                    case 4:
                        MC_SettingActivity.this.setDifferenceReplenishAll();
                        return;
                    case 5:
                        MC_SettingActivity.this.setInventory();
                        return;
                    case 6:
                        MC_SettingActivity.this.setReturnForwardList();
                        return;
                    case 7:
                        MC_SettingActivity.this.setReturnsReverse();
                        return;
                    case 8:
                        MC_SettingActivity.this.setPickTest();
                        return;
                    case 9:
                        MC_SettingActivity.this.setSynchronousStock();
                        return;
                    case 10:
                        MC_SettingActivity.this.setSynchronous();
                        return;
                    case 11:
                        MC_SettingActivity.this.setInit();
                        return;
                    case 12:
                        MC_SettingActivity.this.setFinishApp();
                        return;
                    default:
                        return;
                }
            }
        });
    }

    public void backClicked(View view) {
        finish();
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

    private void setReplenishment() {
        startLoading();
        new Thread(new Runnable() {
            public void run() {
                ServiceResult<Boolean> result = ReplenishmentService.getInstance().oneKeyReplenishment(MC_SettingActivity.this.wrapperData);
                if (result.isSuccess()) {
                    Message msg = MC_SettingActivity.this.handler.obtainMessage();
                    msg.obj = "一键补货完成,补货单号：" + result.getMessage();
                    msg.what = 1;
                    MC_SettingActivity.this.handler.sendMessage(msg);
                    return;
                }
                Message msg = MC_SettingActivity.this.handler.obtainMessage();
                msg.obj = result.getMessage();
                msg.what = 0;
                MC_SettingActivity.this.handler.sendMessage(msg);
            }
        }).start();
    }

    private void setReplenishAll() {
        startLoading();
        new Thread(new Runnable() {
            public void run() {
                ServiceResult<Boolean> result = ReplenishmentService.getInstance().oneKeyReplenishAll(MC_SettingActivity.this.wrapperData);
                if (result.isSuccess()) {
                    Message msg = MC_SettingActivity.this.handler.obtainMessage();
                    msg.obj = "一键补满完成,补货单号：" + result.getMessage();
                    msg.what = 11;
                    MC_SettingActivity.this.handler.sendMessage(msg);
                    return;
                }
                Message msg = MC_SettingActivity.this.handler.obtainMessage();
                msg.obj = result.getMessage();
                msg.what = 0;
                MC_SettingActivity.this.handler.sendMessage(msg);
            }
        }).start();
    }

    private void setDifferenceReplenishment() {
        Intent intent = new Intent();
        intent.putExtra("wrapperData", this.wrapperData);
        intent.setClass(this, MC_DifferenceReplenishmentOrderActivity.class);
        startActivity(intent);
    }

    private void setDifferenceReplenishAll() {
        Intent intent = new Intent();
        intent.putExtra("wrapperData", this.wrapperData);
        intent.setClass(this, MC_DifferenceReplenishAllOrderActivity.class);
        startActivity(intent);
    }

    private void setReturnForwardList() {
        Intent intent = new Intent();
        intent.putExtra("wrapperData", this.wrapperData);
        intent.setClass(this, MC_ReturnForwardListActivity.class);
        startActivity(intent);
    }

    private void setUrgentReplenishment() {
        Intent intent = new Intent();
        intent.putExtra("wrapperData", this.wrapperData);
        intent.setClass(this, MC_UrgentReplenishmentActivity.class);
        startActivity(intent);
    }

    private void setInventory() {
        Intent intent = new Intent();
        intent.putExtra("wrapperData", this.wrapperData);
        intent.setClass(this, MC_InventoryActivity.class);
        startActivity(intent);
    }

    private void setReturnsForward() {
        Intent intent = new Intent();
        intent.putExtra("wrapperData", this.wrapperData);
        intent.setClass(this, MC_ReturnsForwardActivity.class);
        startActivity(intent);
    }

    private void setReturnsReverse() {
        Intent intent = new Intent();
        intent.putExtra("wrapperData", this.wrapperData);
        intent.setClass(this, MC_ReturnsReverseActivity.class);
        startActivity(intent);
    }

    private void setPickTest() {
        Intent intent = new Intent();
        intent.putExtra("wrapperData", this.wrapperData);
        intent.setClass(this, MC_PickTestActivity.class);
        startActivity(intent);
    }

    private void setSynchronousStock() {
        if (isAccessNetwork()) {
            startLoading();
            synchronousStock();
            return;
        }
        resetAlertMsg("没有链接到网络，请检查网络链接！");
    }

    private void synchronousStock() {
        ZillionLog.i("上传交易记录--同步库存：" + StockTransactionDataParse.getInstance().isSync);
        if (StockTransactionDataParse.getInstance().isSync) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            ZillionLog.i("再次同步库存");
            synchronousStock();
            return;
        }
        List<StockTransactionData> datas = new StockTransactionDbOper().findStockTransactionDataToUpload();
        if (datas == null || datas.size() == 0) {
            ZillionLog.i("没有交易记录，直接同步库存");
            VendingChnStockDataParse parse = new VendingChnStockDataParse();
            parse.setListener(this);
            parse.requestVendingChnStockData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_SYN_STOCK, this.vendData.getVd1Id());
            return;
        }
        ZillionLog.i("有交易记录，先同步交易记录");
        StockTransactionDataParse stockTransactionDataParse = StockTransactionDataParse.getInstance();
        stockTransactionDataParse.setListener(this);
        stockTransactionDataParse.requestStockTransactionData(Constant.HTTP_OPERATE_TYPE_INSERT, Constant.METHOD_WSID_STOCKTRANSACTION, this.vendData.getVd1Id(), datas);
    }

    private void setSynchronous() {
        if (isAccessNetwork()) {
            Message message = new Message();
            message.what = 9;
            this.handler.sendMessage(message);
            return;
        }
        resetAlertMsg("没有链接到网络，请检查网络链接！");
    }

    private void setInit() {
        if (isAccessNetwork()) {
            startLoading();
            VendingData vending = new VendingDbOper().getVending();
            InitDataParse parse = new InitDataParse();
            parse.setListener(this);
            parse.requestInitData(Constant.HTTP_OPERATE_TYPE_CHECK, Constant.METHOD_WSID_CHECK_INIT, vending.getVd1Code());
            return;
        }
        resetAlertMsg("没有链接到网络，请检查网络链接！");
    }

    private void setFinishApp() {
        new Builder(this).setMessage(getString(R.string.PUBLIC_EXIST_TITLE)).setPositiveButton(getString(R.string.PUBLIC_EXIST), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                MC_SettingActivity.this.finishApp();
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

    private void initFinishMe() {
        AssetsDatabaseManager.getManager().closeDatabase(MC_Config.SQLITE_DATABASE_NAME);
        AssetsDatabaseManager.getManager().delDatabase();
        savePwdKey("");
        setResult(9999, new Intent());
        finish();
    }

    private void savePwdKey(String key) {
        getSharedPreferences("pwd", 0).edit().putString(Constant.SHARED_PWD_KEY, key).commit();
    }

    private String getPwdKey() {
        return getSharedPreferences("pwd", 0).getString(Constant.SHARED_PWD_KEY, "");
    }

    protected void onPause() {
        cancelImageTask();
        super.onPause();
    }

    protected void onResume() {
        startTimerTask();
        openKeyBoard();
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        ActivityManagerTool.getActivityManager().removeActivity(this);
        super.onDestroy();
    }

    private void openKeyBoard() {
        SerialTools.getInstance().openKeyBoard();
        SerialTools.getInstance().addToolsListener(this);
    }

    public void serialReturn(String value, int serialType) {
        switch (serialType) {
            case 1:
                keyBoardReturn(value, serialType);
                return;
            default:
                return;
        }
    }

    private void keyBoardReturn(String value, int serialType) {
        if (!SerialTools.FUNCTION_KEY_COMBINATION.equals(value) && !SerialTools.FUNCTION_KEY_BORROW.equals(value) && !SerialTools.FUNCTION_KEY_BACK.equals(value) && !SerialTools.FUNCTION_KEY_SET.equals(value)) {
            if (SerialTools.FUNCTION_KEY_CANCEL.equals(value)) {
                finish();
            } else {
                SerialTools.FUNCTION_KEY_CONFIRM.equals(value);
            }
        }
    }

    private void startService() {
        if (this.dataServices != null) {
            startSynData();
        } else {
            bindService(new Intent(this, DataServices.class), this.conn, 1);
        }
    }

    private void startSynData() {
        if (this.dataServices != null) {
            startLoading();
            resetAlertMsg("数据同步中...");
            this.dataServices.setSettingListener(this);
            this.dataServices.synData();
        }
    }

    public void serialReturn(String value, int serialType, Object userInfo) {
    }

    public void requestFinished() {
        stopLoading();
        this.dataServices.setSettingListener(null);
        resetAlertMsg("数据同步完成。");
    }

    public void requestFailure() {
        stopLoading();
        this.dataServices.setSettingListener(null);
        resetAlertMsg("数据同步失败。");
    }

    public void parseRequestFinised(BaseData baseData) {
        stopLoading();
        if (!baseData.isSuccess().booleanValue() && Constant.METHOD_WSID_CHECK_INIT.equals(baseData.getRequestURL())) {
            resetAlertMsg("售货机已经初始化，不允许重复初始化，请与管理员联系！");
        } else if (Constant.METHOD_WSID_CHECK_INIT.equals(baseData.getRequestURL())) {
            new Builder(this).setMessage(getString(R.string.alert_init_msg)).setPositiveButton(getString(R.string.BTN_INIT), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    MC_SettingActivity.this.initFinishMe();
                }
            }).setNegativeButton(getString(R.string.PUBLIC_CANCEL), new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create().show();
        } else if (Constant.METHOD_WSID_SYN_STOCK.equals(baseData.getRequestURL())) {
            resetAlertMsg("库存同步成功！");
        } else if (Constant.METHOD_WSID_STOCKTRANSACTION.equals(baseData.getRequestURL())) {
            VendingChnStockDataParse parse = new VendingChnStockDataParse();
            parse.setListener(this);
            parse.requestVendingChnStockData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_SYN_STOCK, this.vendData.getVd1Id());
        }
    }

    public void parseRequestFailure(BaseData baseData) {
        stopLoading();
        if (Constant.METHOD_WSID_CHECK_INIT.equals(baseData.getRequestURL())) {
            resetAlertMsg("请求初始化权限失败！");
        } else if (Constant.METHOD_WSID_SYN_STOCK.equals(baseData.getRequestURL())) {
            String message = baseData.getReturnMessage();
            if (message == null || message.equals("")) {
                resetAlertMsg("同步库存操作失败！");
            } else {
                resetAlertMsg(message);
            }
        }
    }

    private void resetTimer() {
        this.normalPickTimeOut = 0;
    }

    private void startTimerTask() {
        this.mTimerTask = new TimerTask() {
            public void run() {
                MC_SettingActivity mC_SettingActivity = MC_SettingActivity.this;
                mC_SettingActivity.normalPickTimeOut = mC_SettingActivity.normalPickTimeOut + 1000;
                if (MC_SettingActivity.this.normalPickTimeOut == 180000) {
                    Intent intent = new Intent();
                    intent.setClass(MC_SettingActivity.this, MC_NormalPickActivity.class);
                    MC_SettingActivity.this.startActivity(intent);
                }
            }
        };
        this.normalPickTimeOut = 0;
        this.timer = new Timer();
        this.timer.schedule(this.mTimerTask, 1, 1000);
    }

    private void cancelImageTask() {
        this.mTimerTask.cancel();
    }
}
