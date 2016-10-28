package com.mc.vending.activitys.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.service.TestMaterialService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.ZillionLog;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.SerialTools;
import com.zillion.evm.jssc.SerialPortException;

public class MC_PickTestActivity extends BaseActivity implements MC_SerialToolsListener {
    private TextView alert_msg;
    private TextView alert_msg_title;
    private Button back;
    private EditText et_channle_number;
    private RelativeLayout layout_step1;
    private Button operate;
    private TextView tv_public_title;
    private VendingChnData vendingChn;
    private VendingCardPowerWrapperData wrapperData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_test);
        ActivityManagerTool.getActivityManager().add(this);
        initComponents();
        initObject();
        getParam();
        stopLoading();
    }

    private void getParam() {
        this.wrapperData = (VendingCardPowerWrapperData) getIntent().getSerializableExtra("wrapperData");
    }

    private void initComponents() {
        this.back = (Button) findViewById(R.id.back);
        this.operate = (Button) findViewById(R.id.operate);
        this.tv_public_title = (TextView) findViewById(R.id.tv_public_title);
        this.layout_step1 = (RelativeLayout) findViewById(R.id.layout_step1);
        this.et_channle_number = (EditText) findViewById(R.id.et_channle_number);
        this.alert_msg_title = (TextView) findViewById(R.id.alert_msg_title);
        this.alert_msg = (TextView) findViewById(R.id.alert_msg);
    }

    private void initObject() {
        this.tv_public_title.setText(getResources().getString(R.string.set_pick_test));
        this.back.setVisibility(0);
        this.back.setEnabled(true);
        this.operate.setVisibility(0);
        this.operate.setEnabled(true);
        this.operate.setText(getResources().getString(R.string.btn_test));
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

    private void openKeyBoard() {
        SerialTools.getInstance().openKeyBoard();
        SerialTools.getInstance().addToolsListener(this);
    }

    private void closeKeyBoard() {
        try {
            SerialTools.getInstance().closeKeyBoard();
            SerialTools.getInstance().addToolsListener(this);
        } catch (SerialPortException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            e.printStackTrace();
        }
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
        pickTest();
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
                hiddenAlertMsg();
                if (!StringHelper.isEmpty(this.et_channle_number.getText().toString(), true)) {
                    this.et_channle_number.setText("");
                }
            } else if (SerialTools.FUNCTION_KEY_CONFIRM.equals(value)) {
                pickTest();
            } else {
                resetTextView(SerialTools.getInstance().getKeyValue(value), serialType);
            }
        }
    }

    private void resetTextView(String value, int serialType) {
        if (1 == serialType) {
            this.et_channle_number.setText(new StringBuilder(String.valueOf(this.et_channle_number.getText().toString())).append(value).toString());
        }
    }

    private void pickTest() {
        ServiceResult<VendingChnData> result = TestMaterialService.getInstance().testMaterial(this.et_channle_number.getText().toString());
        if (result.isSuccess()) {
            this.vendingChn = (VendingChnData) result.getResult();
            if (this.vendingChn.getVc1Type().equals("0")) {
                ZillionLog.i("售货机领料测试", "vendingChn:" + this.et_channle_number.getText().toString() + "==CusEmpId:" + this.wrapperData.getCusEmpId() + "==Vc2Cd1Id:" + this.wrapperData.getVendingCardPowerData().getVc2Cd1Id());
                SerialTools.getInstance().openVender(ConvertHelper.toInt(this.vendingChn.getVc1LineNum(), Integer.valueOf(0)).intValue(), ConvertHelper.toInt(this.vendingChn.getVc1ColumnNum(), Integer.valueOf(0)).intValue());
                return;
            }
            ZillionLog.i("格子机领料测试", "vendingChn:" + this.et_channle_number.getText().toString() + "==CusEmpId:" + this.wrapperData.getCusEmpId() + "==Vc2Cd1Id:" + this.wrapperData.getVendingCardPowerData().getVc2Cd1Id());
            SerialTools.getInstance().openStore(ConvertHelper.toInt(this.vendingChn.getVc1LineNum(), Integer.valueOf(0)).intValue(), ConvertHelper.toInt(this.vendingChn.getVc1ColumnNum(), Integer.valueOf(0)).intValue(), ConvertHelper.toInt(this.vendingChn.getVc1Height(), Integer.valueOf(0)).intValue());
            return;
        }
        resetAlertMsg(result.getMessage());
    }

    public void serialReturn(String value, int serialType, Object userInfo) {
    }
}
