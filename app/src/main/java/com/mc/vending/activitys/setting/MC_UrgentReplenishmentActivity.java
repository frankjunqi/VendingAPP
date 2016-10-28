package com.mc.vending.activitys.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.adapter.MC_UrgentReplenishmentAdapter;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnProductWrapperData;
import com.mc.vending.service.ReplenishmentService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.DateHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.ZillionLog;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.MyFunc;
import com.mc.vending.tools.utils.SerialTools;
import com.zillion.evm.jssc.SerialPortException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MC_UrgentReplenishmentActivity extends BaseActivity implements MC_SerialToolsListener {
    private MC_UrgentReplenishmentAdapter adapter;
    private TextView alert_msg;
    private TextView alert_msg_title;
    private Button back;
    private Button btn_open_all_store;
    private List<VendingChnProductWrapperData> dataList;
    private EditText et_order_number;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MC_UrgentReplenishmentActivity.this.stopLoading();
                    MC_UrgentReplenishmentActivity.this.resetAlertMsg((String) msg.obj);
                    return;
                case 3:
                    MC_UrgentReplenishmentActivity.this.stopLoading();
                    MC_UrgentReplenishmentActivity.this.resetAlertMsg((String) msg.obj);
                    MC_UrgentReplenishmentActivity.this.successBack();
                    return;
                case MC_SettingActivity.SET_SUCCESS /*999*/:
                    MC_UrgentReplenishmentActivity.this.adapter = new MC_UrgentReplenishmentAdapter(MC_UrgentReplenishmentActivity.this, MC_UrgentReplenishmentActivity.this.dataList, MC_UrgentReplenishmentActivity.this.listView);
                    MC_UrgentReplenishmentActivity.this.listView.setAdapter(MC_UrgentReplenishmentActivity.this.adapter);
                    MC_UrgentReplenishmentActivity.this.stopLoading();
                    return;
                default:
                    return;
            }
        }
    };
    private ListView listView;
    Handler mhandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    MC_UrgentReplenishmentActivity.this.keyBoardReturn((String) msg.obj, msg.what);
                    return;
                default:
                    return;
            }
        }
    };
    OnKeyListener onKey = new OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode != 66) {
                return false;
            }
            MC_UrgentReplenishmentActivity.this.hiddenKeyBoard(MC_UrgentReplenishmentActivity.this.et_order_number);
            return true;
        }
    };
    private Button operate;
    private String orderNumber;
    private TextView tv_number_title;
    private TextView tv_public_title;
    private VendingCardPowerWrapperData wrapperData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgent_replenishment);
        ActivityManagerTool.getActivityManager().add(this);
        getParam();
        initComponents();
        initObject();
        startLoading();
    }

    private void getParam() {
        this.wrapperData = (VendingCardPowerWrapperData) getIntent().getSerializableExtra("wrapperData");
    }

    private void initComponents() {
        this.tv_public_title = (TextView) findViewById(R.id.tv_public_title);
        this.back = (Button) findViewById(R.id.back);
        this.operate = (Button) findViewById(R.id.operate);
        this.btn_open_all_store = (Button) findViewById(R.id.btn_open_all_store);
        this.alert_msg_title = (TextView) findViewById(R.id.alert_msg_title);
        this.alert_msg = (TextView) findViewById(R.id.alert_msg);
        this.et_order_number = (EditText) findViewById(R.id.et_order_number);
        this.tv_number_title = (TextView) findViewById(R.id.tv_number_title);
        this.listView = (ListView) findViewById(R.id.listView);
    }

    private void initObject() {
        this.orderNumber = "R" + DateHelper.format(new Date(), "yyyy");
        this.tv_public_title.setText(getResources().getString(R.string.set_urgent_replenishment));
        this.tv_number_title.setText(new StringBuilder(String.valueOf(getResources().getString(R.string.urgent_replenishment_order_id))).append(" ").append(this.orderNumber).toString());
        this.back.setVisibility(0);
        this.back.setEnabled(true);
        this.operate.setVisibility(0);
        this.operate.setEnabled(true);
        this.operate.setText(getResources().getString(R.string.PUBLIC_SAVE));
        this.dataList = new ArrayList();
        this.et_order_number.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                ((InputMethodManager) MC_UrgentReplenishmentActivity.this.getSystemService("input_method")).showSoftInput(MC_UrgentReplenishmentActivity.this.et_order_number, 4);
            }
        });
        this.btn_open_all_store.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                for (int j = 1; j < 19; j++) {
                    try {
                        SerialTools.getInstance().openStore(2, 1, j);
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        this.et_order_number.setOnKeyListener(this.onKey);
        requestLst();
    }

    private void requestLst() {
        new Thread(new Runnable() {
            public void run() {
                MC_UrgentReplenishmentActivity.this.dataList = ReplenishmentService.getInstance().findChnCodeProductName();
                MC_UrgentReplenishmentActivity.this.handler.sendEmptyMessage(MC_SettingActivity.SET_SUCCESS);
            }
        }).start();
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

    protected void onPause() {
        super.onPause();
    }

    protected void onResume() {
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

    public void backClicked(View view) {
        finish();
    }

    public void saveClicked(View view) {
        hiddenKeyBoard(this.et_order_number);
        if (StringHelper.isEmpty(this.et_order_number.getText().toString(), true)) {
            resetAlertMsg("请输入紧急补货单号！");
            return;
        }
        startLoading();
        new Thread(new Runnable() {
            public void run() {
                ServiceResult<Boolean> result = ReplenishmentService.getInstance().emergencyReplenishment(new StringBuilder(String.valueOf(MC_UrgentReplenishmentActivity.this.orderNumber)).append(StringHelper.autoCompletionCode(StringHelper.trim(MC_UrgentReplenishmentActivity.this.et_order_number.getText().toString()))).toString(), MC_UrgentReplenishmentActivity.this.dataList, MC_UrgentReplenishmentActivity.this.wrapperData);
                if (result.isSuccess()) {
                    Message msg = MC_UrgentReplenishmentActivity.this.handler.obtainMessage();
                    msg.obj = "紧急补货完成";
                    msg.what = 3;
                    MC_UrgentReplenishmentActivity.this.handler.sendMessage(msg);
                    return;
                }
                Message msg = MC_UrgentReplenishmentActivity.this.handler.obtainMessage();
                msg.obj = result.getMessage();
                msg.what = 0;
                MC_UrgentReplenishmentActivity.this.handler.sendMessage(msg);
            }
        }).start();
    }

    public void sumClicked(View view) {
        int index = Integer.parseInt(String.valueOf(view.getTag())) - 100;
        VendingChnProductWrapperData data = (VendingChnProductWrapperData) this.dataList.get(index);
        data.setActQty(data.getActQty() + 1);
        reloadTableWithLine(index);
    }

    public void subClicked(View view) {
        int index = Integer.parseInt(String.valueOf(view.getTag()));
        VendingChnProductWrapperData data = (VendingChnProductWrapperData) this.dataList.get(index);
        int number = data.getActQty();
        if (number > 0) {
            number--;
        }
        data.setActQty(number);
        reloadTableWithLine(index);
    }

    private void reloadTableWithLine(int index) {
        this.adapter.reloadViewHolder(index);
    }

    private void openKeyBoard() {
        SerialTools.getInstance().addToolsListener(this);
        SerialTools.getInstance().openKeyBoard();
    }

    private void closeKeyBoard() {
        try {
            SerialTools.getInstance().addToolsListener(this);
            SerialTools.getInstance().closeKeyBoard();
        } catch (SerialPortException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void serialReturn(String value, int serialType) {
        Message msg = new Message();
        msg.what = serialType;
        msg.obj = value;
        switch (serialType) {
            case 1:
                this.mhandler.sendMessage(msg);
                return;
            case 2:
                value = MyFunc.getRFIDSerialNo(value);
                return;
            default:
                return;
        }
    }

    public void serialReturn(String value, int serialType, Object userInfo) {
    }

    private void keyBoardReturn(String value, int serialType) {
        if (!SerialTools.FUNCTION_KEY_COMBINATION.equals(value) && !SerialTools.FUNCTION_KEY_BORROW.equals(value) && !SerialTools.FUNCTION_KEY_BACK.equals(value) && !SerialTools.FUNCTION_KEY_SET.equals(value)) {
            if (SerialTools.FUNCTION_KEY_CANCEL.equals(value)) {
                this.et_order_number.setText("");
            } else if (!SerialTools.FUNCTION_KEY_CONFIRM.equals(value)) {
                resetTextView(SerialTools.getInstance().getKeyValue(value), serialType);
            }
        }
    }

    private void resetTextView(String value, int serialType) {
        this.et_order_number.setText(new StringBuilder(String.valueOf(this.et_order_number.getText().toString())).append(value).toString());
    }

    private void successBack() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    ZillionLog.e(getClass().getName(), e.getMessage(), e);
                    e.printStackTrace();
                }
                MC_UrgentReplenishmentActivity.this.closeMe();
            }
        }).start();
    }

    public void closeMe() {
        finish();
    }
}
