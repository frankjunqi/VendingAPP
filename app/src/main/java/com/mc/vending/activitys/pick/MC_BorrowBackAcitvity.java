package com.mc.vending.activitys.pick;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.data.CardData;
import com.mc.vending.data.TransactionWrapperData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.service.BorrowMaterialService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.ZillionLog;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.MyFunc;
import com.mc.vending.tools.utils.SerialTools;
import com.zillion.evm.jssc.SerialPortException;

public class MC_BorrowBackAcitvity extends BaseActivity implements MC_SerialToolsListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$mc$vending$activitys$pick$MC_BorrowBackAcitvity$OPERATE_STEP = null;
    public static final int OPERATE_BACK = 1;
    public static final int OPERATE_BORROW = 0;
    private static final String TAG = MC_BorrowBackAcitvity.class.getName();
    private TextView alert_msg;
    private TextView alert_msg_title;
    private EditText et_card_password;
    private EditText et_channle_number;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    MC_BorrowBackAcitvity.this.hiddenAlertMsg();
                    MC_BorrowBackAcitvity.this.keyBoardReturn((String) msg.obj, msg.what);
                    return;
                case 2:
                    MC_BorrowBackAcitvity.this.hiddenAlertMsg();
                    MC_BorrowBackAcitvity.this.cardPasswordValidate();
                    return;
                case 5:
                    MC_BorrowBackAcitvity.this.openedStore((String) msg.obj);
                    return;
                default:
                    return;
            }
        }
    };
    private boolean isRFID;
    private boolean isStoreChecked;
    private RelativeLayout layout_step1;
    private RelativeLayout layout_step2;
    private OPERATE_STEP operateStep;
    private int operateType;
    private String operateTypeStr;
    private Bundle selectBundle;
    private TransactionWrapperData transcationData;
    private TextView tv_public_title;
    private VendingChnData vendingChn;
    private VendingCardPowerWrapperData wrapperData;

    private enum OPERATE_STEP {
        OPERATE_STEP_1,
        OPERATE_STEP_2
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$mc$vending$activitys$pick$MC_BorrowBackAcitvity$OPERATE_STEP() {
        int[] iArr = $SWITCH_TABLE$com$mc$vending$activitys$pick$MC_BorrowBackAcitvity$OPERATE_STEP;
        if (iArr == null) {
            iArr = new int[OPERATE_STEP.values().length];
            try {
                iArr[OPERATE_STEP.OPERATE_STEP_1.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[OPERATE_STEP.OPERATE_STEP_2.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            $SWITCH_TABLE$com$mc$vending$activitys$pick$MC_BorrowBackAcitvity$OPERATE_STEP = iArr;
        }
        return iArr;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_back);
        ActivityManagerTool.getActivityManager().add(this);
        initParam();
        initComponents();
        initObject();
        resetViews();
        stopLoading();
    }

    private void initParam() {
        this.selectBundle = getIntent().getExtras();
        if (this.selectBundle != null && this.selectBundle.containsKey("operateType")) {
            this.operateType = this.selectBundle.getInt("operateType");
        }
    }

    private void initComponents() {
        this.tv_public_title = (TextView) findViewById(R.id.tv_public_title);
        this.layout_step1 = (RelativeLayout) findViewById(R.id.layout_step1);
        this.layout_step2 = (RelativeLayout) findViewById(R.id.layout_step2);
        this.et_channle_number = (EditText) findViewById(R.id.et_channle_number);
        this.et_card_password = (EditText) findViewById(R.id.et_card_password);
        this.alert_msg_title = (TextView) findViewById(R.id.alert_msg_title);
        this.alert_msg = (TextView) findViewById(R.id.alert_msg);
    }

    private void initObject() {
        this.operateStep = OPERATE_STEP.OPERATE_STEP_1;
        switch (this.operateType) {
            case 0:
                this.operateTypeStr = "1";
                this.tv_public_title.setText(getResources().getString(R.string.operate_borrow));
                return;
            case 1:
                this.operateTypeStr = "2";
                this.tv_public_title.setText(getResources().getString(R.string.operate_back));
                return;
            default:
                return;
        }
    }

    private void clearEditText() {
        this.et_channle_number.setText("");
        this.et_card_password.setText("");
    }

    private void resetInputStatus() {
        this.operateStep = OPERATE_STEP.OPERATE_STEP_1;
        clearEditText();
        hiddenAlertMsg();
        resetViews();
    }

    private void resetViews() {
        switch ($SWITCH_TABLE$com$mc$vending$activitys$pick$MC_BorrowBackAcitvity$OPERATE_STEP()[this.operateStep.ordinal()]) {
            case 1:
                this.layout_step1.setVisibility(0);
                this.layout_step2.setVisibility(4);
                return;
            case 2:
                this.layout_step1.setVisibility(4);
                this.layout_step2.setVisibility(0);
                return;
            default:
                return;
        }
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
        resetInputStatus();
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
            ZillionLog.e(TAG, e.getMessage(), e);
        }
    }

    public void serialReturn(String value, int serialType) {
        Message msg = new Message();
        msg.what = serialType;
        msg.obj = value;
        switch (serialType) {
            case 1:
                this.handler.sendMessage(msg);
                this.isRFID = false;
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
            case 3:
                this.handler.sendMessage(msg);
                return;
            case 5:
                this.handler.sendMessage(msg);
                return;
            default:
                return;
        }
    }

    private void keyBoardReturn(String value, int serialType) {
        if (!SerialTools.FUNCTION_KEY_COMBINATION.equals(value) && !SerialTools.FUNCTION_KEY_BORROW.equals(value) && !SerialTools.FUNCTION_KEY_BACK.equals(value) && !SerialTools.FUNCTION_KEY_SET.equals(value)) {
            if (SerialTools.FUNCTION_KEY_CANCEL.equals(value)) {
                hiddenAlertMsg();
                switch ($SWITCH_TABLE$com$mc$vending$activitys$pick$MC_BorrowBackAcitvity$OPERATE_STEP()[this.operateStep.ordinal()]) {
                    case 1:
                        if (StringHelper.isEmpty(this.et_channle_number.getText().toString(), true)) {
                            finish();
                            return;
                        } else {
                            this.et_channle_number.setText("");
                            return;
                        }
                    case 2:
                        if (StringHelper.isEmpty(this.et_card_password.getText().toString(), true)) {
                            closeRFID();
                            this.operateStep = OPERATE_STEP.OPERATE_STEP_1;
                            resetViews();
                            return;
                        }
                        this.et_card_password.setText("");
                        openRFID();
                        return;
                    default:
                        return;
                }
            } else if (SerialTools.FUNCTION_KEY_CONFIRM.equals(value)) {
                switch ($SWITCH_TABLE$com$mc$vending$activitys$pick$MC_BorrowBackAcitvity$OPERATE_STEP()[this.operateStep.ordinal()]) {
                    case 1:
                        channelValidate();
                        return;
                    case 2:
                        cardPasswordValidate();
                        return;
                    default:
                        return;
                }
            } else {
                resetTextView(SerialTools.getInstance().getKeyValue(value), serialType);
            }
        }
    }

    private void resetTextView(String value, int serialType) {
        switch ($SWITCH_TABLE$com$mc$vending$activitys$pick$MC_BorrowBackAcitvity$OPERATE_STEP()[this.operateStep.ordinal()]) {
            case 1:
                this.et_channle_number.setText(new StringBuilder(String.valueOf(this.et_channle_number.getText().toString())).append(value).toString());
                return;
            case 2:
                if (1 == serialType) {
                    this.et_card_password.setText(new StringBuilder(String.valueOf(this.et_card_password.getText().toString())).append(value).toString());
                    return;
                } else if (2 == serialType && !StringHelper.isEmpty(value, true)) {
                    this.et_card_password.setText(value);
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    private void channelValidate() {
        if (StringHelper.isEmpty(this.et_channle_number.getText().toString(), true)) {
            resetAlertMsg(getResources().getString(R.string.placeholder_channle_number));
            return;
        }
        ServiceResult<VendingChnData> vendingResult = BorrowMaterialService.getInstance().checkVendingChn(this.et_channle_number.getText().toString(), "2");
        if (vendingResult.isSuccess()) {
            this.vendingChn = (VendingChnData) vendingResult.getResult();
            this.transcationData = BorrowMaterialService.getInstance().getStockTransaction(this.vendingChn);
            ServiceResult<Boolean> result = BorrowMaterialService.getInstance().checkBorrowStatus(this.vendingChn, this.transcationData, this.operateTypeStr);
            if (result.isSuccess()) {
                this.operateStep = OPERATE_STEP.OPERATE_STEP_2;
                resetViews();
                hiddenAlertMsg();
                openRFID();
                return;
            }
            resetAlertMsg(result.getMessage());
            return;
        }
        resetAlertMsg(vendingResult.getMessage());
    }

    private void cardPasswordValidate() {
        closeRFID();
        if (StringHelper.isEmpty(this.et_card_password.getText().toString(), true)) {
            resetAlertMsg(getResources().getString(R.string.placeholder_card_pwd));
            return;
        }
        ServiceResult<VendingCardPowerWrapperData> result = BorrowMaterialService.getInstance().checkCardPowerOut(this.isRFID ? CardData.CARD_SERIALNO_PARAM : CardData.CARD_PASSWORD_PARAM, this.et_card_password.getText().toString(), this.vendingChn.getVc1Vd1Id());
        if (result.isSuccess()) {
            this.wrapperData = (VendingCardPowerWrapperData) result.getResult();
            ServiceResult<Boolean> borrowResult = BorrowMaterialService.getInstance().checkBorrowCustomer(this.vendingChn.getVc1BorrowStatus(), this.wrapperData.getCusEmpId(), this.operateTypeStr, this.transcationData);
            if (borrowResult.isSuccess()) {
                hiddenAlertMsg();
                if (this.vendingChn.getVc1Type().equals("1")) {
                    this.isStoreChecked = false;
                    SerialTools.getInstance().openStore(ConvertHelper.toInt(this.vendingChn.getVc1LineNum(), Integer.valueOf(0)).intValue(), ConvertHelper.toInt(this.vendingChn.getVc1ColumnNum(), Integer.valueOf(0)).intValue(), ConvertHelper.toInt(this.vendingChn.getVc1Height(), Integer.valueOf(0)).intValue());
                    openStoreDirect();
                    return;
                }
                return;
            }
            resetAlertMsg(borrowResult.getMessage());
            return;
        }
        resetAlertMsg(result.getMessage());
    }

    private void openStoreDirect() {
        pickSuccess();
        resetInputStatus();
        if (this.operateType == 0) {
            resetAlertMsg("借料成功！");
        } else {
            resetAlertMsg("还料成功！");
        }
        this.isStoreChecked = true;
        successBack();
    }

    private void openedStore(String input) {
    }

    private void pickSuccess() {
        this.isStoreChecked = true;
        BorrowMaterialService.getInstance().saveStockTransaction(this.vendingChn, this.wrapperData, this.operateTypeStr);
        this.vendingChn = null;
        this.wrapperData = null;
        this.transcationData = null;
        this.isRFID = false;
    }

    private void pickFailure() {
    }

    public void serialReturn(String value, int serialType, Object userInfo) {
    }

    private void successBack() {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    ZillionLog.e(getClass().getName(), e.getMessage(), e);
                }
                MC_BorrowBackAcitvity.this.closeMe();
            }
        }).start();
    }

    public void closeMe() {
        finish();
    }
}
