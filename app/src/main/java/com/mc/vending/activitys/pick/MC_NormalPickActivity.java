package com.mc.vending.activitys.pick;

import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mc.vending.R;
import com.mc.vending.activitys.BaseActivity;
import com.mc.vending.activitys.MC_ImagePlayerActivity;
import com.mc.vending.activitys.MainActivity;
import com.mc.vending.activitys.VersionActivity;
import com.mc.vending.activitys.setting.MC_SettingActivity;
import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.CardData;
import com.mc.vending.data.ProductPictureData;
import com.mc.vending.data.VendingCardPowerWrapperData;
import com.mc.vending.data.VendingChnData;
import com.mc.vending.data.VendingData;
import com.mc.vending.data.VendingPictureData;
import com.mc.vending.data.VersionData;
import com.mc.vending.db.VendingDbOper;
import com.mc.vending.db.VendingPasswordDbOper;
import com.mc.vending.db.VendingPictureDbOper;
import com.mc.vending.parse.VersionDataParse;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.parse.listener.RequestDataFinishListener;
import com.mc.vending.service.CompositeMaterialService;
import com.mc.vending.service.DataServices;
import com.mc.vending.service.DataServices.ServiceBinder;
import com.mc.vending.service.GeneralMaterialService;
import com.mc.vending.service.ReplenishmentService;
import com.mc.vending.tools.ActivityManagerTool;
import com.mc.vending.tools.AsyncImageLoader;
import com.mc.vending.tools.AsyncImageLoader.ImageCallback;
import com.mc.vending.tools.ConvertHelper;
import com.mc.vending.tools.ServiceResult;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.ZillionLog;
import com.mc.vending.tools.utils.MC_SerialToolsListener;
import com.mc.vending.tools.utils.MyFunc;
import com.mc.vending.tools.utils.SerialTools;
import com.zillion.evm.jssc.SerialPortException;
import java.util.Timer;
import java.util.TimerTask;

public class MC_NormalPickActivity extends BaseActivity implements MC_SerialToolsListener, RequestDataFinishListener, DataParseRequestListener {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$mc$vending$activitys$pick$MC_NormalPickActivity$OPERATE_STEP = null;
    private static final int MESSAGE_Image_player = 99;
    private static final String TAG = MC_NormalPickActivity.class.getName();
    public static boolean isTheSameStoreOpenerFlow = false;
    private TextView alert_msg;
    private TextView alert_msg_title;
    private AsyncImageLoader asyncImageLoader;
    private Button btn_out;
    private Button btn_version;
    private ServiceConnection conn = new ServiceConnection() {
        public void onServiceConnected(ComponentName name, IBinder service) {
            MC_NormalPickActivity.this.dataServices = ((ServiceBinder) service).getService();
            ZillionLog.i(getClass().getName(), "onServiceConnected");
            MC_NormalPickActivity.this.resetVendStatus();
        }

        public void onServiceDisconnected(ComponentName name) {
            MC_NormalPickActivity.this.dataServices = null;
        }
    };
    public DataServices dataServices;
    private EditText et_card_password;
    private EditText et_card_password_set;
    private EditText et_channle_number;
    private EditText et_pick_number;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    MC_NormalPickActivity.this.hiddenAlertMsg();
                    MC_NormalPickActivity.this.keyBoardReturn((String) msg.obj, msg.what);
                    return;
                case 2:
                    MC_NormalPickActivity.this.hiddenAlertMsg();
                    if (MC_NormalPickActivity.this.operateStep == OPERATE_STEP.OPERATE_STEP_3) {
                        if (MC_NormalPickActivity.isTheSameStoreOpenerFlow) {
                            MC_NormalPickActivity.isTheSameStoreOpenerFlow = false;
                            MC_NormalPickActivity.this.isRFID = true;
                            MC_NormalPickActivity.this.TheSameStoreOpenerLogic();
                            return;
                        }
                        MC_NormalPickActivity.this.cardPasswordValidate();
                        return;
                    } else if (MC_NormalPickActivity.this.operateStep == OPERATE_STEP.OPERATE_STEP_SET) {
                        MC_NormalPickActivity.this.setValidate();
                        return;
                    } else {
                        return;
                    }
                case 5:
                    MC_NormalPickActivity.this.openedStore((String) msg.obj);
                    return;
                case 6:
                    MC_NormalPickActivity.this.openVender((String) msg.obj);
                    return;
                case MC_NormalPickActivity.MESSAGE_Image_player /*99*/:
                    MC_NormalPickActivity.this.goImagePlayerAcitvity();
                    return;
                default:
                    return;
            }
        }
    };
    private final int imagePlayerTimeCount = 60000;
    private int imagePlayerTimeOut;
    private final int imagePlayerTimer = 1000;
    boolean isOperating = false;
    private boolean isRFID;
    private boolean isStoreChecked;
    private ImageView iv_sku;
    private RelativeLayout layout_step1;
    private RelativeLayout layout_step2;
    private RelativeLayout layout_step3;
    private RelativeLayout layout_step_set;
    private TimerTask mTimerTask;
    private OPERATE_STEP operateStep;
    private EditText password;
    private int stockCount;
    private Timer timer;
    private String vendCode;
    private VendingData vendData;
    private VendingChnData vendingChn;
    private VersionData versionData = new VersionData();
    private VendingCardPowerWrapperData wrapperData;

    private enum OPERATE_STEP {
        OPERATE_STEP_1,
        OPERATE_STEP_2,
        OPERATE_STEP_3,
        OPERATE_STEP_SET
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$mc$vending$activitys$pick$MC_NormalPickActivity$OPERATE_STEP() {
        int[] iArr = $SWITCH_TABLE$com$mc$vending$activitys$pick$MC_NormalPickActivity$OPERATE_STEP;
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
            try {
                iArr[OPERATE_STEP.OPERATE_STEP_3.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[OPERATE_STEP.OPERATE_STEP_SET.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            $SWITCH_TABLE$com$mc$vending$activitys$pick$MC_NormalPickActivity$OPERATE_STEP = iArr;
        }
        return iArr;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acticity_normal_pick);
        ActivityManagerTool.getActivityManager().add(this);
        requestGetClientVersionServer();
        getParam();
        initComponents();
        initObject();
        resetViews();
        startService();
    }

    private void requestGetClientVersionServer() {
        this.btn_version = (Button) findViewById(R.id.btn_version);
        this.btn_version.setVisibility(4);
        VersionDataParse parse = new VersionDataParse();
        parse.setListener(this);
        parse.requestVersionData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VERSION);
    }

    public void parseRequestFinised(BaseData baseData) {
        if (baseData.isSuccess().booleanValue() && Constant.METHOD_WSID_VERSION.equals(baseData.getRequestURL())) {
            this.versionData = (VersionData) baseData.getUserObject();
            if (Integer.parseInt(this.versionData.getVersion().replace(".", "")) > Integer.parseInt(Constant.HEADER_VALUE_CLIENTVER.replace(".", ""))) {
                Intent intent = new Intent(this, VersionActivity.class);
                intent.putExtra("url", this.versionData.getDownloadURL());
                intent.putExtra("vermsg", new StringBuilder(String.valueOf(this.versionData.getVersion())).toString());
                startActivity(intent);
                finish();
                return;
            }
            this.btn_version.setVisibility(4);
        }
    }

    public void parseRequestFailure(BaseData baseData) {
        ZillionLog.e("normal parseRequestFailure==>" + ((String) Constant.WSIDNAMEMAP.get(baseData.getRequestURL())));
    }

    private void getParam() {
        this.vendCode = getIntent().getStringExtra("vendCode");
    }

    private void resetVendStatus() {
        if (!isAccessNetwork()) {
            resetAlertMsg("没有链接到网络，请检查网络链接！");
        } else if (new VendingDbOper().getVending() == null) {
            startLoading();
            this.dataServices.setNormalListener(this);
            this.dataServices.resetVending(this.vendCode);
        }
    }

    private void startService() {
        bindService(new Intent(this, DataServices.class), this.conn, 1);
    }

    private void resetTimer() {
        this.imagePlayerTimeOut = 0;
    }

    private void initComponents() {
        this.iv_sku = (ImageView) findViewById(R.id.iv_sku);
        this.layout_step1 = (RelativeLayout) findViewById(R.id.layout_step1);
        this.layout_step2 = (RelativeLayout) findViewById(R.id.layout_step2);
        this.layout_step3 = (RelativeLayout) findViewById(R.id.layout_step3);
        this.layout_step_set = (RelativeLayout) findViewById(R.id.layout_step_set);
        this.et_channle_number = (EditText) findViewById(R.id.et_channle_number);
        this.et_pick_number = (EditText) findViewById(R.id.et_pick_number);
        this.et_card_password = (EditText) findViewById(R.id.et_card_password);
        this.et_card_password_set = (EditText) findViewById(R.id.et_card_password_set);
        this.alert_msg_title = (TextView) findViewById(R.id.alert_msg_title);
        this.alert_msg = (TextView) findViewById(R.id.alert_msg);
        this.btn_out = (Button) findViewById(R.id.btn_out);
    }

    private void initObject() {
        this.operateStep = OPERATE_STEP.OPERATE_STEP_1;
        this.btn_out.setOnLongClickListener(new OnLongClickListener() {
            public boolean onLongClick(View arg0) {
                MC_NormalPickActivity.this.password = new EditText(MC_NormalPickActivity.this);
                MC_NormalPickActivity.this.password.setInputType(129);
                new Builder(MC_NormalPickActivity.this).setTitle("请输入密码").setView(MC_NormalPickActivity.this.password).setCancelable(false).setPositiveButton("确定", new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (!StringHelper.isEmpty(MC_NormalPickActivity.this.password.getText().toString(), true)) {
                            if (new VendingPasswordDbOper().getVendingPasswordByPassword(MC_NormalPickActivity.this.password.getText().toString()) == null) {
                                MC_NormalPickActivity.this.resetAlertMsg("密码错误");
                                return;
                            }
                            ActivityManagerTool.getActivityManager().exit();
                            MC_NormalPickActivity.this.finish();
                        }
                    }
                }).setNegativeButton("取消", null).show();
                return false;
            }
        });
    }

    private void clearEditText() {
        this.et_channle_number.setText("");
        this.et_pick_number.setText("");
        this.et_card_password.setText("");
        this.et_card_password_set.setText("");
    }

    private void resetInputStatus() {
        this.operateStep = OPERATE_STEP.OPERATE_STEP_1;
        clearEditText();
        hiddenAlertMsg();
        resetViews();
        resetSKUImage();
    }

    private void resetSKUImage() {
        this.iv_sku.setImageResource(R.drawable.default_home);
        VendingPictureData data = new VendingPictureDbOper().getDefaultVendingPicture();
        if (data != null) {
            loadImage(data.getVp2FilePath());
        }
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

    private void openRFID() {
        SerialTools.getInstance().openRFIDReader();
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

    private void closeStore() {
        try {
            SerialTools.getInstance().closeStore();
        } catch (SerialPortException e) {
            ZillionLog.e(getClass().getName(), e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void goImagePlayerAcitvity() {
        if (new VendingPictureDbOper().findVendingPicture().size() > 0) {
            Intent intent = new Intent();
            intent.setClass(this, MC_ImagePlayerActivity.class);
            startActivity(intent);
        }
    }

    private void startTimerTask() {
        this.mTimerTask = new TimerTask() {
            public void run() {
                MC_NormalPickActivity mC_NormalPickActivity = MC_NormalPickActivity.this;
                mC_NormalPickActivity.imagePlayerTimeOut = mC_NormalPickActivity.imagePlayerTimeOut + 1000;
                if (MC_NormalPickActivity.this.imagePlayerTimeOut == 60000) {
                    Message msg = new Message();
                    msg.what = MC_NormalPickActivity.MESSAGE_Image_player;
                    MC_NormalPickActivity.this.handler.sendMessage(msg);
                }
            }
        };
        this.imagePlayerTimeOut = 0;
        this.timer = new Timer();
        this.timer.schedule(this.mTimerTask, 1, 1000);
    }

    private void cancelImageTask() {
        this.mTimerTask.cancel();
    }

    private void resetViews() {
        switch ($SWITCH_TABLE$com$mc$vending$activitys$pick$MC_NormalPickActivity$OPERATE_STEP()[this.operateStep.ordinal()]) {
            case 1:
                this.layout_step1.setVisibility(0);
                this.layout_step2.setVisibility(4);
                this.layout_step3.setVisibility(4);
                this.layout_step_set.setVisibility(4);
                return;
            case 2:
                this.layout_step1.setVisibility(4);
                this.layout_step2.setVisibility(0);
                this.layout_step3.setVisibility(4);
                this.layout_step_set.setVisibility(4);
                return;
            case 3:
                this.layout_step1.setVisibility(4);
                this.layout_step2.setVisibility(4);
                this.layout_step3.setVisibility(0);
                this.layout_step_set.setVisibility(4);
                return;
            case 4:
                this.layout_step1.setVisibility(4);
                this.layout_step2.setVisibility(4);
                this.layout_step3.setVisibility(4);
                this.layout_step_set.setVisibility(0);
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
        cancelImageTask();
        super.onPause();
    }

    protected void onResume() {
        isTheSameStoreOpenerFlow = false;
        openKeyBoard();
        startTimerTask();
        resetInputStatus();
        closeRFID();
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onDestroy() {
        ActivityManagerTool.getActivityManager().removeActivity(this);
        super.onDestroy();
        unbindService(this.conn);
    }

    public void serialReturn(String value, int serialType) {
        Message msg = new Message();
        msg.what = serialType;
        msg.obj = value;
        switch (serialType) {
            case 1:
                if (!this.isOperating) {
                    resetTimer();
                    this.handler.sendMessage(msg);
                    this.isRFID = false;
                    return;
                }
                return;
            case 2:
                value = MyFunc.getRFIDSerialNo(value);
                if (!StringHelper.isEmpty(value, true)) {
                    resetTimer();
                    resetTextView(value, serialType);
                    this.isRFID = true;
                    closeRFID();
                    this.handler.sendMessage(msg);
                    return;
                }
                return;
            case 5:
                this.handler.sendMessage(msg);
                return;
            case 6:
                resetTimer();
                this.handler.sendMessage(msg);
                return;
            default:
                return;
        }
    }

    private void keyBoardReturn(String value, int serialType) {
        ServiceResult<VendingData> result;
        Intent intent;
        Bundle bundle;
        if (SerialTools.FUNCTION_KEY_COMBINATION.equals(value)) {
            if (this.operateStep == OPERATE_STEP.OPERATE_STEP_1) {
                result = CompositeMaterialService.getInstance().checkVending();
                if (result.isSuccess()) {
                    this.vendData = (VendingData) result.getResult();
                    intent = new Intent();
                    intent.putExtra("vendData", this.vendData);
                    intent.setClass(this, MC_CombinationPickActivity.class);
                    startActivity(intent);
                    return;
                }
                resetAlertMsg(result.getMessage());
            }
        } else if (SerialTools.FUNCTION_KEY_BORROW.equals(value)) {
            if (this.operateStep == OPERATE_STEP.OPERATE_STEP_1) {
                result = CompositeMaterialService.getInstance().checkVending();
                if (result.isSuccess()) {
                    this.vendData = (VendingData) result.getResult();
                    intent = new Intent();
                    bundle = new Bundle();
                    bundle.putInt("operateType", 0);
                    intent.putExtras(bundle);
                    intent.setClass(this, MC_BorrowBackAcitvity.class);
                    startActivity(intent);
                    return;
                }
                resetAlertMsg(result.getMessage());
            }
        } else if (SerialTools.FUNCTION_KEY_BACK.equals(value)) {
            if (this.operateStep == OPERATE_STEP.OPERATE_STEP_1) {
                result = CompositeMaterialService.getInstance().checkVending();
                if (result.isSuccess()) {
                    this.vendData = (VendingData) result.getResult();
                    intent = new Intent();
                    bundle = new Bundle();
                    bundle.putInt("operateType", 1);
                    intent.putExtras(bundle);
                    intent.setClass(this, MC_BorrowBackAcitvity.class);
                    startActivity(intent);
                    return;
                }
                resetAlertMsg(result.getMessage());
            }
        } else if (SerialTools.FUNCTION_KEY_SET.equals(value)) {
            if (this.operateStep == OPERATE_STEP.OPERATE_STEP_1) {
                this.operateStep = OPERATE_STEP.OPERATE_STEP_SET;
                resetViews();
                openRFID();
            }
        } else if (SerialTools.FUNCTION_KEY_CANCEL.equals(value)) {
            hiddenAlertMsg();
            switch ($SWITCH_TABLE$com$mc$vending$activitys$pick$MC_NormalPickActivity$OPERATE_STEP()[this.operateStep.ordinal()]) {
                case 1:
                    isTheSameStoreOpenerFlow = false;
                    if (!StringHelper.isEmpty(this.et_channle_number.getText().toString(), true)) {
                        this.et_channle_number.setText("");
                        return;
                    }
                    return;
                case 2:
                    if (this.vendingChn.getVc1Type().equals("1")) {
                        this.operateStep = OPERATE_STEP.OPERATE_STEP_1;
                        resetViews();
                        return;
                    } else if (StringHelper.isEmpty(this.et_pick_number.getText().toString(), true)) {
                        this.operateStep = OPERATE_STEP.OPERATE_STEP_1;
                        resetViews();
                        return;
                    } else {
                        this.et_pick_number.setText("");
                        return;
                    }
                case 3:
                    if (StringHelper.isEmpty(this.et_card_password.getText().toString(), true)) {
                        closeRFID();
                        this.operateStep = OPERATE_STEP.OPERATE_STEP_2;
                        resetViews();
                        return;
                    }
                    this.et_card_password.setText("");
                    openRFID();
                    return;
                case 4:
                    if (StringHelper.isEmpty(this.et_card_password_set.getText().toString(), true)) {
                        closeRFID();
                        this.operateStep = OPERATE_STEP.OPERATE_STEP_1;
                        resetViews();
                        return;
                    }
                    this.et_card_password_set.setText("");
                    openRFID();
                    return;
                default:
                    return;
            }
        } else if (SerialTools.FUNCTION_KEY_CONFIRM.equals(value)) {
            switch ($SWITCH_TABLE$com$mc$vending$activitys$pick$MC_NormalPickActivity$OPERATE_STEP()[this.operateStep.ordinal()]) {
                case 1:
                    if (isTheSameStoreOpenerFlow) {
                        isTheSameStoreOpenerFlow = true;
                        openRFID();
                        this.operateStep = OPERATE_STEP.OPERATE_STEP_3;
                        resetViews();
                        hiddenAlertMsg();
                        return;
                    }
                    channelValidate();
                    return;
                case 2:
                    pickNumberValidate();
                    return;
                case 3:
                    if (isTheSameStoreOpenerFlow) {
                        this.isRFID = false;
                        TheSameStoreOpenerLogic();
                        return;
                    }
                    cardPasswordValidate();
                    return;
                case 4:
                    setValidate();
                    return;
                default:
                    return;
            }
        } else {
            resetTextView(SerialTools.getInstance().getKeyValue(value), serialType);
        }
    }

    private void channelValidate() {
        this.et_pick_number.setText("");
        if (StringHelper.isEmpty(this.et_channle_number.getText().toString(), true)) {
            resetAlertMsg(getResources().getString(R.string.placeholder_channle_number));
        } else if (this.et_channle_number.getText().toString().equals("4006809696")) {
            startSynData();
        } else {
            ServiceResult<VendingData> result = GeneralMaterialService.getInstance().checkVending();
            if (result.isSuccess()) {
                ServiceResult<VendingChnData> vendingResult = GeneralMaterialService.getInstance().checkVendingChn(this.et_channle_number.getText().toString(), "1");
                if (vendingResult.isSuccess()) {
                    this.vendingChn = (VendingChnData) vendingResult.getResult();
                    ProductPictureData picData = GeneralMaterialService.getInstance().getPictureBySkuId(this.vendingChn.getVc1Pd1Id());
                    if (picData != null) {
                        Log.i(getClass().getName().toString(), "pp1_filePath===" + picData.getPp1FilePath());
                        loadImage(picData.getPp1FilePath());
                    }
                    this.stockCount = GeneralMaterialService.getInstance().getVendingChnStock(this.vendingChn.getVc1Vd1Id(), this.vendingChn.getVc1Code());
                    if (this.stockCount <= 0) {
                        String rtnStr = "";
                        if (this.vendingChn.getVc1Type().equals("1")) {
                            isTheSameStoreOpenerFlow = true;
                            rtnStr = "库存为：0,库存量不足。如需继续领料请按确认键";
                        } else {
                            rtnStr = "库存为：0,库存量不足,不能领料";
                        }
                        resetAlertMsg(rtnStr);
                        return;
                    } else if (this.vendingChn.getVc1Type().equals("1")) {
                        this.et_pick_number.setText(String.valueOf(this.stockCount));
                        resetAlertMsg("格子机器库存数量：" + this.stockCount + ", 格子机默认全部领取");
                        if (this.stockCount > 0) {
                            this.operateStep = OPERATE_STEP.OPERATE_STEP_2;
                            resetViews();
                            return;
                        }
                        return;
                    } else {
                        this.operateStep = OPERATE_STEP.OPERATE_STEP_2;
                        resetViews();
                        hiddenAlertMsg();
                        return;
                    }
                }
                resetAlertMsg(vendingResult.getMessage());
                return;
            }
            resetAlertMsg(result.getMessage());
        }
    }

    private void pickNumberValidate() {
        if (StringHelper.isEmpty(this.et_pick_number.getText().toString(), true)) {
            resetAlertMsg(getResources().getString(R.string.placeholder_pick_number));
            return;
        }
        if (this.vendingChn.getVc1Type().equals("0")) {
            int inputCount = ConvertHelper.toInt(this.et_pick_number.getText().toString(), Integer.valueOf(0)).intValue();
            if (inputCount == 0 || inputCount > this.stockCount) {
                resetAlertMsg("货道号" + this.vendingChn.getVc1Code() + "的库存数量=" + this.stockCount + "，库存不足，请重新输入！");
                return;
            }
        }
        openRFID();
        this.operateStep = OPERATE_STEP.OPERATE_STEP_3;
        resetViews();
        hiddenAlertMsg();
    }

    private void cardPasswordValidate() {
        closeRFID();
        if (StringHelper.isEmpty(this.et_card_password.getText().toString(), true)) {
            resetAlertMsg(getResources().getString(R.string.placeholder_card_pwd));
            return;
        }
        ServiceResult<VendingCardPowerWrapperData> result = GeneralMaterialService.getInstance().checkCardPowerOut(this.isRFID ? CardData.CARD_SERIALNO_PARAM : CardData.CARD_PASSWORD_PARAM, this.et_card_password.getText().toString(), this.vendingChn.getVc1Vd1Id());
        if (result.isSuccess()) {
            this.wrapperData = (VendingCardPowerWrapperData) result.getResult();
            if (this.wrapperData == null) {
                ZillionLog.i(TAG, "pickSuccess:wrapperData:is null--02");
            }
            ServiceResult<Boolean> materialResult = GeneralMaterialService.getInstance().checkProductMaterialPower(this.vendingChn.getVc1Vd1Id(), this.vendingChn.getVc1Pd1Id(), this.wrapperData.getVendingCardPowerData().getVc2Cu1Id(), this.wrapperData.getVendingCardPowerData().getVc2Id(), ConvertHelper.toInt(this.et_pick_number.getText().toString(), Integer.valueOf(0)).intValue(), this.vendingChn.getVc1Code(), this.wrapperData.getCardPuductPowerType(), this.wrapperData.getVendingCardPowerData().getVc2Cd1Id());
            if (materialResult.isSuccess()) {
                hiddenAlertMsg();
                if (this.vendingChn.getVc1Type().equals("0")) {
                    this.vendingChn.setInputQty(ConvertHelper.toInt(this.et_pick_number.getText().toString(), Integer.valueOf(0)).intValue());
                    this.isOperating = true;
                    openVender(null);
                    return;
                }
                this.isStoreChecked = false;
                ZillionLog.i("格子机领料", "vendingChn:" + this.et_channle_number.getText().toString() + "==CusEmpId:" + this.wrapperData.getCusEmpId() + "==Vc2Cd1Id:" + this.wrapperData.getVendingCardPowerData().getVc2Cd1Id());
                SerialTools.getInstance().addToolsListener(this);
                SerialTools.getInstance().openStore(ConvertHelper.toInt(this.vendingChn.getVc1LineNum(), Integer.valueOf(0)).intValue(), ConvertHelper.toInt(this.vendingChn.getVc1ColumnNum(), Integer.valueOf(0)).intValue(), ConvertHelper.toInt(this.vendingChn.getVc1Height(), Integer.valueOf(0)).intValue());
                openStoreDirect();
                return;
            }
            resetAlertMsg(materialResult.getMessage());
            return;
        }
        resetAlertMsg(result.getMessage());
    }

    private void openStoreDirect() {
        pickSuccess(this.stockCount, this.vendingChn);
        resetInputStatus();
        ZillionLog.i("格子机领料", "成功");
        resetAlertMsg("领料成功！");
        this.stockCount = 0;
        this.isStoreChecked = true;
        this.isOperating = false;
    }

    private void openVender(String status) {
        int q;
        if (this.vendingChn == null) {
            q = 0;
        } else {
            q = this.vendingChn.getInputQty();
        }
        if (status == null || status.contains("31") || status.contains("32")) {
            if (status != null && (status.contains("31") || status.contains("32"))) {
                if (status.contains("31")) {
                    pickSuccess(1, this.vendingChn);
                } else {
                    this.vendingChn.setFailureQty(this.vendingChn.getFailureQty() + 1);
                }
                this.vendingChn.setInputQty(this.vendingChn.getInputQty() - 1);
                if (this.vendingChn.getInputQty() <= 0) {
                    closeVender();
                    resetInputStatus();
                    if (this.vendingChn.getFailureQty() > 0) {
                        resetAlertMsg("货道状态错误,失败数量：" + this.vendingChn.getFailureQty());
                    } else {
                        resetAlertMsg("领料成功！");
                    }
                    this.isOperating = false;
                    this.vendingChn = null;
                    this.wrapperData = null;
                    this.stockCount = 0;
                    this.isRFID = false;
                    if (this.wrapperData == null) {
                        ZillionLog.i(TAG, "pickSuccess:wrapperData:is null--00");
                        return;
                    }
                    return;
                }
            }
            SerialTools.getInstance().openVender(ConvertHelper.toInt(this.vendingChn.getVc1LineNum(), Integer.valueOf(0)).intValue(), ConvertHelper.toInt(this.vendingChn.getVc1ColumnNum(), Integer.valueOf(0)).intValue());
            try {
                Thread.sleep(Constant.TIME_INTERNAL);
            } catch (InterruptedException e) {
                ZillionLog.e(TAG, e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }

    private void openedStore(String input) {
    }

    private void setValidate() {
        closeRFID();
        if (StringHelper.isEmpty(this.et_card_password_set.getText().toString(), true)) {
            resetAlertMsg(getResources().getString(R.string.placeholder_card_pwd));
            return;
        }
        ServiceResult<VendingData> vendResult = GeneralMaterialService.getInstance().checkVending();
        if (vendResult.isSuccess()) {
            this.vendData = (VendingData) vendResult.getResult();
            ServiceResult<VendingCardPowerWrapperData> result = ReplenishmentService.getInstance().checkCardPowerInner(this.et_card_password_set.getText().toString(), this.vendData.getVd1Id());
            if (result.isSuccess()) {
                this.wrapperData = (VendingCardPowerWrapperData) result.getResult();
                if (this.wrapperData == null) {
                    ZillionLog.i(TAG, "pickSuccess:wrapperData:is null--03");
                }
                hiddenAlertMsg();
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                intent.putExtra("wrapperData", this.wrapperData);
                intent.putExtra("vendData", this.vendData);
                intent.putExtras(bundle);
                intent.setClass(this, MC_SettingActivity.class);
                ZillionLog.i("管理员进入", "manager setting:" + this.et_card_password_set.getText().toString());
                startActivityForResult(intent, 1000);
                return;
            }
            resetAlertMsg(result.getMessage());
            return;
        }
        resetAlertMsg(vendResult.getMessage());
    }

    private void pickSuccess(int count, VendingChnData vendingChn) {
        this.isStoreChecked = false;
        if (this.wrapperData == null) {
            ZillionLog.i(TAG, "pickSuccess:wrapperData:is null--01");
        } else {
            GeneralMaterialService.getInstance().saveStockTransaction(count, vendingChn, this.wrapperData);
        }
    }

    private void pickFailure() {
    }

    private void loadImage(String imageURL) {
        this.asyncImageLoader = new AsyncImageLoader();
        Drawable cachedImage = this.asyncImageLoader.loadDrawable(imageURL, new ImageCallback() {
            public void imageLoaded(Drawable imageDrawable, String imageUrl, String tag) {
                MC_NormalPickActivity.this.iv_sku.setImageDrawable(imageDrawable);
            }
        });
        if (cachedImage != null) {
            this.iv_sku.setImageDrawable(cachedImage);
        }
    }

    private void resetTextView(String value, int serialType) {
        switch ($SWITCH_TABLE$com$mc$vending$activitys$pick$MC_NormalPickActivity$OPERATE_STEP()[this.operateStep.ordinal()]) {
            case 1:
                this.et_channle_number.setText(new StringBuilder(String.valueOf(this.et_channle_number.getText().toString())).append(value).toString());
                return;
            case 2:
                if (this.vendingChn.getVc1Type().equals("0")) {
                    this.et_pick_number.setText(new StringBuilder(String.valueOf(this.et_pick_number.getText().toString())).append(value).toString());
                    return;
                }
                return;
            case 3:
                if (1 == serialType) {
                    this.et_card_password.setText(new StringBuilder(String.valueOf(this.et_card_password.getText().toString())).append(value).toString());
                    return;
                } else if (2 == serialType && !StringHelper.isEmpty(value, true)) {
                    this.et_card_password.setText(value);
                    return;
                } else {
                    return;
                }
            case 4:
                if (1 == serialType) {
                    this.et_card_password_set.setText(new StringBuilder(String.valueOf(this.et_card_password_set.getText().toString())).append(value).toString());
                    return;
                } else if (2 == serialType && !StringHelper.isEmpty(value, true)) {
                    this.et_card_password_set.setText(value);
                    return;
                } else {
                    return;
                }
            default:
                return;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (resultCode == 1003) {
                startSynData();
            } else if (resultCode == 9999) {
                goMainActivity();
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startSynData() {
        if (!isAccessNetwork()) {
            resetAlertMsg("没有链接到网络，请检查网络链接！");
        } else if (this.dataServices != null) {
            startLoading();
            resetAlertMsg("数据同步中...");
            this.dataServices.setNormalListener(this);
            this.dataServices.synData();
        } else {
            resetAlertMsg("程序异常");
        }
    }

    private void goMainActivity() {
        Intent intent = new Intent();
        intent.setClass(this, MainActivity.class);
        startActivity(intent);
    }

    public void serialReturn(String value, int serialType, Object userInfo) {
    }

    public void requestFinished() {
        stopLoading();
        resetInputStatus();
        resetAlertMsg("数据同步完成");
        this.dataServices.setNormalListener(null);
    }

    public void requestFailure() {
        stopLoading();
        resetInputStatus();
        resetAlertMsg("数据同步失败");
        this.dataServices.setNormalListener(null);
    }

    private void TheSameStoreOpenerLogic() {
        String cardCodeNew = this.et_card_password.getText().toString();
        String cardCodeOld = GeneralMaterialService.getInstance().getVendingStoreLastPicker(this.vendingChn.getVc1Vd1Id(), this.vendingChn.getVc1Code(), this.isRFID);
        if (cardCodeOld == null || !cardCodeOld.equals(cardCodeNew)) {
            resetAlertMsg("输入的卡号或密码无权限打开该格子柜，请检查后重新输入！");
            return;
        }
        SerialTools.getInstance().openStore(ConvertHelper.toInt(this.vendingChn.getVc1LineNum(), Integer.valueOf(0)).intValue(), ConvertHelper.toInt(this.vendingChn.getVc1ColumnNum(), Integer.valueOf(0)).intValue(), ConvertHelper.toInt(this.vendingChn.getVc1Height(), Integer.valueOf(0)).intValue());
        resetInputStatus();
        isTheSameStoreOpenerFlow = false;
        resetAlertMsg("验证通过，打开格子机！");
    }
}
