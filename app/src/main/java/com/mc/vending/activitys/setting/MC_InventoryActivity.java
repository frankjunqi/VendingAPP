package com.mc.vending.activitys.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
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
import com.mc.vending.service.InventoryService;
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

public class MC_InventoryActivity extends BaseActivity implements MC_SerialToolsListener {
    private MC_UrgentReplenishmentAdapter adapter;
    private TextView alert_msg;
    private TextView alert_msg_title;
    private Button back;
    private List<VendingChnProductWrapperData> dataList;
    private EditText et_order_number;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MC_InventoryActivity.this.stopLoading();
                    MC_InventoryActivity.this.resetAlertMsg((String) msg.obj);
                    return;
                case 4:
                    MC_InventoryActivity.this.stopLoading();
                    MC_InventoryActivity.this.resetAlertMsg((String) msg.obj);
                    MC_InventoryActivity.this.finish();
                    return;
                case MC_SettingActivity.SET_SUCCESS /*999*/:
                    MC_InventoryActivity.this.adapter = new MC_UrgentReplenishmentAdapter(MC_InventoryActivity.this, MC_InventoryActivity.this.dataList, MC_InventoryActivity.this.listView);
                    MC_InventoryActivity.this.listView.setAdapter(MC_InventoryActivity.this.adapter);
                    MC_InventoryActivity.this.stopLoading();
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
                    MC_InventoryActivity.this.keyBoardReturn((String) msg.obj, msg.what);
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
            MC_InventoryActivity.this.hiddenKeyBoard(MC_InventoryActivity.this.et_order_number);
            return true;
        }
    };
    private Button operate;
    private String orderNumber;
    private TextView set_difference_replenishment;
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
        this.alert_msg_title = (TextView) findViewById(R.id.alert_msg_title);
        this.alert_msg = (TextView) findViewById(R.id.alert_msg);
        this.et_order_number = (EditText) findViewById(R.id.et_order_number);
        this.tv_number_title = (TextView) findViewById(R.id.tv_number_title);
        this.set_difference_replenishment = (TextView) findViewById(R.id.set_difference_replenishment);
        this.listView = (ListView) findViewById(R.id.listView);
    }

    private void initObject() {
        this.orderNumber = "I" + DateHelper.format(new Date(), "yyyy");
        this.tv_public_title.setText(getResources().getString(R.string.set_inventory));
        this.tv_number_title.setText(new StringBuilder(String.valueOf(getResources().getString(R.string.inventory_order_id))).append(" ").append(this.orderNumber).toString());
        this.set_difference_replenishment.setText(getResources().getString(R.string.inventory_number));
        this.back.setVisibility(0);
        this.back.setEnabled(true);
        this.operate.setVisibility(0);
        this.operate.setEnabled(true);
        this.operate.setText(getResources().getString(R.string.PUBLIC_SAVE));
        this.dataList = new ArrayList();
        this.et_order_number.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                ((InputMethodManager) MC_InventoryActivity.this.getSystemService("input_method")).showSoftInput(MC_InventoryActivity.this.et_order_number, 4);
            }
        });
        this.et_order_number.setOnKeyListener(this.onKey);
        requestLst();
    }

    private void requestLst() {
        new Thread(new Runnable() {
            public void run() {
                MC_InventoryActivity.this.dataList = InventoryService.getInstance().findChnCodeProductName();
                MC_InventoryActivity.this.handler.sendEmptyMessage(MC_SettingActivity.SET_SUCCESS);
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
        if (StringHelper.isEmpty(this.et_order_number.getText().toString(), true)) {
            resetAlertMsg("请输入盘点单号！");
            return;
        }
        startLoading();
        new Thread(new Runnable() {
            public void run() {
                ServiceResult<Boolean> result = InventoryService.getInstance().saveInventoryHistory(new StringBuilder(String.valueOf(MC_InventoryActivity.this.orderNumber)).append(StringHelper.autoCompletionCode(StringHelper.trim(MC_InventoryActivity.this.et_order_number.getText().toString()))).toString(), MC_InventoryActivity.this.dataList, MC_InventoryActivity.this.wrapperData);
                if (result.isSuccess()) {
                    Message msg = MC_InventoryActivity.this.handler.obtainMessage();
                    msg.obj = "盘点完成";
                    msg.what = 4;
                    MC_InventoryActivity.this.handler.sendMessage(msg);
                    return;
                }
                Message msg = MC_InventoryActivity.this.handler.obtainMessage();
                msg.obj = result.getMessage();
                msg.what = 0;
                MC_InventoryActivity.this.handler.sendMessage(msg);
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
}
