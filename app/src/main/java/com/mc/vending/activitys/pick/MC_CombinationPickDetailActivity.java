package com.mc.vending.activitys.pick;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.adapter.MC_CombinationPickDetailAdapter;
import com.mc.vending.config.Constant;
import com.mc.vending.data.CardData;
import com.mc.vending.data.ProductGroupHeadWrapperData;
import com.mc.vending.data.ProductGroupWrapperData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingChnWrapperData;
import com.mc.vending.data.VendingData;
import com.mc.vending.service.CompositeMaterialService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.ZillionLog;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.MyFunc;
import com.mc.vending.tools.utils.SerialTools;
import com.zillion.evm.jssc.SerialPortException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MC_CombinationPickDetailActivity extends BaseActivity implements MC_SerialToolsListener {
    private MC_CombinationPickDetailAdapter adapter;
    private TextView alert_msg;
    private TextView alert_msg_title;
    private String combinationNumber;
    private EditText et_password;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    MC_CombinationPickDetailActivity.this.keyBoardReturn((String) msg.obj, msg.what);
                    return;
                case 2:
                    MC_CombinationPickDetailActivity.this.cardPasswordValidate();
                    return;
                case 6:
                    Map<String, Object> map = (Map<String, Object>) msg.obj;
                    MC_CombinationPickDetailActivity.this.openVender((String) map.get("keyValue"), (VendingChnData) map.get("vendingChn"));
                    return;
                default:
                    return;
            }
        }
    };
    private ProductGroupHeadWrapperData headWrapperData;
    boolean isOperating = false;
    private boolean isRFID;
    private ListView listView;
    private TextView tv_public_title;
    private List<VendingChnData> vendChnList;
    private VendingData vendData;
    private VendingCardPowerWrapperData wrapperData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combination_pick_detail);
        ActivityManagerTool.getActivityManager().add(this);
        getParam();
        initComponents();
        initObject();
        stopLoading();
    }

    private void getParam() {
        this.headWrapperData = (ProductGroupHeadWrapperData) getIntent().getSerializableExtra("ProductGroupHeadWrapperData");
        this.vendData = (VendingData) getIntent().getSerializableExtra("vendData");
        this.combinationNumber = (String) getIntent().getSerializableExtra("combinationNumber");
    }

    private void initComponents() {
        this.tv_public_title = (TextView) findViewById(R.id.tv_public_title);
        this.et_password = (EditText) findViewById(R.id.et_password);
        this.listView = (ListView) findViewById(R.id.listView);
        this.alert_msg_title = (TextView) findViewById(R.id.alert_msg_title);
        this.alert_msg = (TextView) findViewById(R.id.alert_msg);
    }

    private void initObject() {
        this.adapter = new MC_CombinationPickDetailAdapter(this, this.headWrapperData.getWrapperList(), this.listView);
        this.listView.setAdapter(this.adapter);
        this.tv_public_title.setTextSize(30.0f);
        this.tv_public_title.setText("组合编号：" + this.combinationNumber);
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
        openRFID();
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

    private void openRFID() {
        SerialTools.getInstance().openRFIDReader();
        SerialTools.getInstance().addToolsListener(this);
    }

    private void closeRFID() {
        try {
            SerialTools.getInstance().closeRFIDReader();
        } catch (SerialPortException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void closeVender() {
        try {
            SerialTools.getInstance().closeVender();
        } catch (SerialPortException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            e.printStackTrace();
        }
    }

    public void serialReturn(String value, int serialType, Object userInfo) {
        Message msg = new Message();
        msg.what = serialType;
        msg.obj = value;
        switch (serialType) {
            case 1:
                if (!this.isOperating) {
                    this.handler.sendMessage(msg);
                    this.isRFID = false;
                    return;
                }
                return;
            case 2:
                value = MyFunc.getRFIDSerialNo(value);
                if (!StringHelper.isEmpty(value, true)) {
                    resetTextView(value, serialType);
                    closeRFID();
                    this.isRFID = true;
                    this.handler.sendMessage(msg);
                    return;
                }
                return;
            case 4:
                this.handler.sendMessage(msg);
                return;
            case 6:
                Map<String, Object> map = new HashMap();
                map.put("keyValue", value);
                map.put("vendingChn", userInfo);
                msg.obj = map;
                this.handler.sendMessage(msg);
                return;
            default:
                return;
        }
    }

    public void serialReturn(String value, int serialType) {
    }

    private void keyBoardReturn(String value, int serialType) {
        if (!SerialTools.FUNCTION_KEY_COMBINATION.equals(value) && !SerialTools.FUNCTION_KEY_BORROW.equals(value) && !SerialTools.FUNCTION_KEY_BACK.equals(value) && !SerialTools.FUNCTION_KEY_SET.equals(value)) {
            if (SerialTools.FUNCTION_KEY_CANCEL.equals(value)) {
                hiddenAlertMsg();
                if (StringHelper.isEmpty(this.et_password.getText().toString(), true)) {
                    closeRFID();
                    finish();
                    return;
                }
                this.et_password.setText("");
                openRFID();
            } else if (SerialTools.FUNCTION_KEY_CONFIRM.equals(value)) {
                cardPasswordValidate();
            } else {
                resetTextView(SerialTools.getInstance().getKeyValue(value), serialType);
            }
        }
    }

    private void resetTextView(String value, int serialType) {
        if (1 == serialType) {
            this.et_password.setText(new StringBuilder(String.valueOf(this.et_password.getText().toString())).append(value).toString());
        } else if (2 == serialType && !StringHelper.isEmpty(value, true)) {
            this.et_password.setText(value);
        }
    }

    private void cardPasswordValidate() {
        closeRFID();
        if (!StringHelper.isEmpty(this.et_password.getText().toString(), true)) {
            ServiceResult<VendingCardPowerWrapperData> wrapperResult = CompositeMaterialService.getInstance().checkCardPowerOut(this.isRFID ? CardData.CARD_SERIALNO_PARAM : CardData.CARD_PASSWORD_PARAM, this.et_password.getText().toString(), this.vendData.getVd1Id());
            if (wrapperResult.isSuccess()) {
                this.wrapperData = (VendingCardPowerWrapperData) wrapperResult.getResult();
                ServiceResult<Boolean> result = CompositeMaterialService.getInstance().checkProductGroupPower(this.wrapperData, this.headWrapperData, this.vendData.getVd1Id());
                if (result.isSuccess()) {
                    this.vendChnList = new ArrayList();
                    for (ProductGroupWrapperData data : this.headWrapperData.getWrapperList()) {
                        for (VendingChnWrapperData wrapperData : data.getChnWrapperList()) {
                            VendingChnData vendingChn = wrapperData.getChnStock().getVendingChn();
                            int qty = wrapperData.getQty();
                            if (vendingChn.getVc1Type().equals("0")) {
                                vendingChn.setInputQty(qty);
                                this.vendChnList.add(vendingChn);
                            }
                        }
                    }
                    this.isOperating = true;
                    openVender(null, null);
                    return;
                }
                resetAlertMsg(result.getMessage());
                return;
            }
            resetAlertMsg(wrapperResult.getMessage());
        }
    }

    private void openVender(String status, VendingChnData mVendingChn) {
        if (status == null && this.vendChnList.size() > 0) {
            VendingChnData vendingChn = (VendingChnData) this.vendChnList.get(0);
            SerialTools.getInstance().openVender(ConvertHelper.toInt(vendingChn.getVc1LineNum(), Integer.valueOf(0)).intValue(), ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), Integer.valueOf(0)).intValue(), vendingChn);
            try {
                Thread.sleep(Constant.TIME_INTERNAL);
            } catch (InterruptedException e) {
                ZillionLog.e(getClass().getName(), e.getMessage(), e);
                e.printStackTrace();
            }
        }
        if (status == null) {
            return;
        }
        if (status.contains("31") || status.contains("32")) {
            boolean flag;
            int failureCount;
            int i = 0;
            VendingChnData vendingChn;
            while (i < this.vendChnList.size()) {
                vendingChn = (VendingChnData) this.vendChnList.get(i);
                if (!mVendingChn.getVc1Code().equals(vendingChn.getVc1Code()) || vendingChn.getInputQty() <= 0) {
                    i++;
                } else {
                    if (status.contains("31")) {
                        pickSuccess(vendingChn);
                    } else {
                        vendingChn.setFailureQty(vendingChn.getFailureQty() + 1);
                    }
                    vendingChn.setInputQty(vendingChn.getInputQty() - 1);
                    flag = false;
                    failureCount = 0;
                    for (i = 0; i < this.vendChnList.size(); i++) {
                        vendingChn = (VendingChnData) this.vendChnList.get(i);
                        failureCount += vendingChn.getFailureQty();
                        if (vendingChn.getInputQty() > 0) {
                            SerialTools.getInstance().openVender(ConvertHelper.toInt(vendingChn.getVc1LineNum(), Integer.valueOf(0)).intValue(), ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), Integer.valueOf(0)).intValue(), vendingChn);
                            try {
                                Thread.sleep(Constant.TIME_INTERNAL);
                            } catch (InterruptedException e2) {
                                ZillionLog.e(getClass().getName(), e2.getMessage(), e2);
                                e2.printStackTrace();
                            }
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        this.isOperating = false;
                        closeVender();
                        if (failureCount <= 0) {
                            resetAlertMsg("领料成功,失败数量：" + failureCount);
                        } else {
                            resetAlertMsg("领料成功！");
                        }
                        this.et_password.setText("");
                        this.isRFID = false;
                        successBack();
                    }
                }
            }
            flag = false;
            failureCount = 0;
            for (i = 0; i < this.vendChnList.size(); i++) {
                vendingChn = (VendingChnData) this.vendChnList.get(i);
                failureCount += vendingChn.getFailureQty();
                if (vendingChn.getInputQty() > 0) {
                    SerialTools.getInstance().openVender(ConvertHelper.toInt(vendingChn.getVc1LineNum(), Integer.valueOf(0)).intValue(), ConvertHelper.toInt(vendingChn.getVc1ColumnNum(), Integer.valueOf(0)).intValue(), vendingChn);
                    try {
                        Thread.sleep(Constant.TIME_INTERNAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                this.isOperating = false;
                closeVender();
                if (failureCount <= 0) {
                    resetAlertMsg("领料成功！");
                } else {
                    resetAlertMsg("领料成功,失败数量：" + failureCount);
                }
                this.et_password.setText("");
                this.isRFID = false;
                successBack();
            }
        }
    }

    private void pickSuccess(VendingChnData vendingChn) {
        CompositeMaterialService.getInstance().saveStockTransaction(1, vendingChn, this.wrapperData);
    }

    private void pickFailure() {
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
                MC_CombinationPickDetailActivity.this.closeMe();
            }
        }).start();
    }

    public void closeMe() {
        setResult(1001, new Intent(this, MC_CombinationPickActivity.class));
        finish();
    }
}
