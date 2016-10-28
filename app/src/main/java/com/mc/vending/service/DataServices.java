package com.mc.vending.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import com.mc.vending.config.Constant;
import com.mc.vending.data.BaseData;
import com.mc.vending.data.InterfaceData;
import com.mc.vending.data.VendingData;
import com.mc.vending.db.InterfaceDbOper;
import com.mc.vending.db.VendingDbOper;
import com.mc.vending.db.VersionDbOper;
import com.mc.vending.parse.AutherDataParse;
import com.mc.vending.parse.CardDataParse;
import com.mc.vending.parse.ConfigDataParse;
import com.mc.vending.parse.CusEmpCardPowerDataParse;
import com.mc.vending.parse.CustomerDataParse;
import com.mc.vending.parse.CustomerEmpLinkDataParse;
import com.mc.vending.parse.InventoryHistoryDataParse;
import com.mc.vending.parse.ProductDataParse;
import com.mc.vending.parse.ProductGroupDataParse;
import com.mc.vending.parse.ProductGroupPowerDataParse;
import com.mc.vending.parse.ProductMaterialPowerDataParse;
import com.mc.vending.parse.ProductPictureDataParse;
import com.mc.vending.parse.ReplenishmentDataParse;
import com.mc.vending.parse.StationDataParse;
import com.mc.vending.parse.StockTransactionDataParse;
import com.mc.vending.parse.SupplierDataParse;
import com.mc.vending.parse.SynDataParse;
import com.mc.vending.parse.VendingCardPowerDataParse;
import com.mc.vending.parse.VendingChnDataParse;
import com.mc.vending.parse.VendingDataParse;
import com.mc.vending.parse.VendingPasswordDataParse;
import com.mc.vending.parse.VendingPictureDataParse;
import com.mc.vending.parse.VendingProLinkDataParse;
import com.mc.vending.parse.VendingStatusDataParse;
import com.mc.vending.parse.listener.DataParseRequestListener;
import com.mc.vending.parse.listener.RequestDataFinishListener;
import com.mc.vending.tools.DateHelper;
import com.mc.vending.tools.StringHelper;
import com.mc.vending.tools.ZillionLog;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint({"HandlerLeak"})
public class DataServices extends Service implements DataParseRequestListener, Serializable {
    public static final int MESSAGE_AUTHER = 1;
    public static final int MESSAGE_CARD = 13;
    public static final int MESSAGE_CONFIG = 2;
    public static final int MESSAGE_CUSEMPCARDPOWER = 14;
    public static final int MESSAGE_CUSTOMER = 16;
    public static final int MESSAGE_CUSTOMEREMPLINK = 15;
    public static final int MESSAGE_INVENTORY = 23;
    public static final int MESSAGE_PRODUCT = 7;
    public static final int MESSAGE_PRODUCTGROUP = 17;
    public static final int MESSAGE_PRODUCTGROUPPOWER = 18;
    public static final int MESSAGE_PRODUCTMATERIAKPOWER = 12;
    public static final int MESSAGE_PRODUCTPICTURE = 8;
    public static final int MESSAGE_REPLENISHMENT = 19;
    public static final int MESSAGE_REPLENISHMENT_DIFF = 22;
    public static final int MESSAGE_REPLENISHMENT_STATUS = 21;
    public static final int MESSAGE_STATION = 10;
    public static final int MESSAGE_STOCKTRANSACTION = 24;
    public static final int MESSAGE_SUPPLIER = 9;
    public static final int MESSAGE_VENDING = 3;
    public static final int MESSAGE_VENDINGCARDPOWER = 11;
    public static final int MESSAGE_VENDINGCHN = 5;
    public static final int MESSAGE_VENDINGPASSWORD = 25;
    public static final int MESSAGE_VENDINGPICTURE = 4;
    public static final int MESSAGE_VENDINGPROLINK = 6;
    public static final int MESSAGE_VENDING_STSATUS = 20;
    private static final long serialVersionUID = -8326654980490455778L;
    public static boolean synDataFlag = false;
    private TimerTask cardTask = null;
    private Timer cardTimer;
    private int cardTimerLength = 120000;
    private Map<String, InterfaceData> configMap = new HashMap();
    private TimerTask configTask = null;
    private Timer configTimer;
    private int configTimerLength = 120000;
    private TimerTask cusEmpCardPowerTask = null;
    private Timer cusEmpCardPowerTimer;
    private int cusEmpCardPowerTimerLength = 120000;
    private TimerTask customerEmpLinkTask = null;
    private Timer customerEmpLinkTimer;
    private int customerEmpLinkTimerLength = 120000;
    private TimerTask customerTask = null;
    private Timer customerTimer;
    private int customerTimerLength = 120000;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    DataServices.this.requestAutherParse();
                    break;
                case 2:
                    DataServices.this.requestConfigParse();
                    break;
                case 3:
                    DataServices.this.requestVendingParse();
                    break;
                case 4:
                    DataServices.this.requestVendingPictureParse();
                    break;
                case 5:
                    DataServices.this.requestVendingChnParse();
                    break;
                case 6:
                    DataServices.this.requestVendingProLinkParse();
                    break;
                case 7:
                    DataServices.this.requestProductParse();
                    break;
                case 8:
                    DataServices.this.requestProductPictureParse();
                    break;
                case 9:
                    DataServices.this.requestSupplierParse();
                    break;
                case 10:
                    DataServices.this.requestStationParse();
                    break;
                case 11:
                    DataServices.this.requestVendingCardPowerParse();
                    break;
                case 12:
                    DataServices.this.requestProductMaterialPowerParse();
                    break;
                case DataServices.MESSAGE_CARD /*13*/:
                    DataServices.this.requestCardParse();
                    break;
                case DataServices.MESSAGE_CUSEMPCARDPOWER /*14*/:
                    DataServices.this.requestCusEmpCardPowerParse();
                    break;
                case 15:
                    DataServices.this.requestCustomerEmpLinkParse();
                    break;
                case 16:
                    DataServices.this.requestCustomerParse();
                    break;
                case DataServices.MESSAGE_PRODUCTGROUP /*17*/:
                    DataServices.this.requestProductGroupParse();
                    break;
                case DataServices.MESSAGE_PRODUCTGROUPPOWER /*18*/:
                    DataServices.this.requestProductGroupPowerParse();
                    break;
                case 19:
                    DataServices.this.requestReplenishmentParse();
                    break;
                case DataServices.MESSAGE_VENDING_STSATUS /*20*/:
                    VendingStatusDataParse.getInstance().requestVendingData(Constant.HTTP_OPERATE_TYPE_UPDATESTATUS, Constant.METHOD_WSID_VENDING_STSATUS, DataServices.this.vendingId);
                    break;
                case DataServices.MESSAGE_REPLENISHMENT_STATUS /*21*/:
                    ReplenishmentDataParse.getInstance().requestReplenishmentData(Constant.HTTP_OPERATE_TYPE_UPDATESTATUS, "7f342da0-05be-4f3a-96c3-28072ec31e7a", DataServices.this.vendingId, DataServices.this.replenishmentRowCount);
                    break;
                case DataServices.MESSAGE_REPLENISHMENT_DIFF /*22*/:
                    ReplenishmentDataParse.getInstance().requestReplenishmentData(Constant.HTTP_OPERATE_TYPE_UPDATEDETAILDIFFERENTIAQTY, "7f342da0-05be-4f3a-96c3-28072ec31e7a", DataServices.this.vendingId, DataServices.this.replenishmentRowCount);
                    break;
                case DataServices.MESSAGE_INVENTORY /*23*/:
                    InventoryHistoryDataParse.getInstance().requestInventoryHistoryData(Constant.HTTP_OPERATE_TYPE_INSERT, Constant.METHOD_WSID_INVENTORY, DataServices.this.vendingId);
                    break;
                case 24:
                    ZillionLog.i("上传交易记录--自动任务：" + StockTransactionDataParse.getInstance().isSync);
                    if (!StockTransactionDataParse.getInstance().isSync) {
                        StockTransactionDataParse.getInstance().requestStockTransactionData(Constant.HTTP_OPERATE_TYPE_INSERT, Constant.METHOD_WSID_STOCKTRANSACTION, DataServices.this.vendingId);
                        break;
                    }
                    break;
                case DataServices.MESSAGE_VENDINGPASSWORD /*25*/:
                    DataServices.this.requestVendingPasswordParse();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    public boolean initFlag = false;
    private TimerTask inventoryTask = null;
    private Timer inventoryTimer;
    private int inventoryTimerLength = 120000;
    private RequestDataFinishListener normalListener;
    private TimerTask productGroupPowerTask = null;
    private Timer productGroupPowerTimer;
    private int productGroupPowerTimerLength = 120000;
    private TimerTask productGroupTask = null;
    private Timer productGroupTimer;
    private int productGroupTimerLength = 120000;
    private TimerTask productMaterialPowerTask = null;
    private Timer productMaterialPowerTimer;
    private int productMaterialPowerTimerLength = 120000;
    private int productPictureRowCount = 10;
    private TimerTask productPictureTask = null;
    private Timer productPictureTimer;
    private int productPictureTimerLength = 120000;
    private int productRowCount = 10;
    private TimerTask productTask = null;
    private Timer productTimer;
    private int productTimerLength = 120000;
    private TimerTask replenishmentDiffTask = null;
    private Timer replenishmentDiffTimer;
    private int replenishmentDiffTimerLength = 120000;
    private int replenishmentRowCount = 10;
    private TimerTask replenishmentStatusTask = null;
    private Timer replenishmentStatusTimer;
    private int replenishmentStatusTimerLength = 120000;
    private TimerTask replenishmentTask = null;
    private Timer replenishmentTimer;
    private int replenishmentTimerLength = 120000;
    private RequestDataFinishListener settingListener;
    private String sql201 = "ALTER TABLE Vending add COLUMN VD1_CardType varchar(10) not null DEFAULT '1';";
    private String sql210 = "ALTER TABLE Card add COLUMN CD1_ProductPower varchar(1) not null DEFAULT '0';";
    private String sql2101 = "CREATE TABLE if not exists ProductCardPower ( PC1_ID  varchar(100) NOT NULL, PC1_VD1_ID varchar(100),PC1_M02_ID varchar(100),PC1_CU1_ID varchar(100),PC1_CD1_ID varchar(100),PC1_VP1_ID varchar(100),PC1_Power  varchar(100),PC1_OnceQty varchar(100),PC1_Period varchar(100),PC1_IntervalStart  varchar(100),PC1_IntervalFinish varchar(100),PC1_StartDate  varchar(100),PC1_PeriodQty  varchar(100), CreateUser  varchar(100), CreateTime  varchar(100), ModifyUser  varchar(100), ModifyTime  varchar(100), RowVersion  varchar(100), PRIMARY KEY (PC1_ID) );";
    private String sql2102 = "CREATE TABLE if not exists UsedRecord ( UR1_ID varchar(100) NOT NULL ,UR1_M02_ID varchar(100),UR1_CD1_ID varchar(100),UR1_VD1_ID varchar(100),UR1_PD1_ID varchar(100),UR1_Quantity   varchar(100),uploadStatus varchar(1) not null DEFAULT '0', CreateUser  varchar(100), CreateTime  varchar(100), ModifyUser  varchar(100), ModifyTime  varchar(100), RowVersion  varchar(100), PRIMARY KEY (UR1_ID) );";
    private String sql2103 = "CREATE TABLE if not exists Conversion ( CN1_ID varchar(100) NOT NULL ,CN1_Upid varchar(100),CN1_Cpid varchar(100),CN1_Proportion varchar(100),CN1_Operation varchar(100),CN1_CreateUser  varchar(100), CN1_CreateTime  varchar(100), CN1_ModifyUser  varchar(100), CN1_ModifyTime  varchar(100), CN1_RowVersion  varchar(100), PRIMARY KEY (CN1_ID) );";
    private String sql2104 = "alter table VendingChn add column VC1_CN1_ID varchar(50)";
    private int stationRowCount = 10;
    private TimerTask stationTask = null;
    private Timer stationTimer;
    private int stationTimerLength = 120000;
    private TimerTask stockTransactionTask = null;
    private Timer stockTransactionTimer;
    private int stockTransactionTimerLength = 120000;
    private int supplierRowCount = 10;
    private TimerTask supplierTask = null;
    private Timer supplierTimer;
    private int supplierTimerLength = 120000;
    private Map<String, String> taskMap;
    private TimerTask vendingCardPowerTask = null;
    private Timer vendingCardPowerTimer;
    private int vendingCardPowerTimerLength = 120000;
    private TimerTask vendingChnTask = null;
    private Timer vendingChnTimer;
    private int vendingChnTimerLength = 120000;
    public String vendingCode = "";
    public String vendingId = "";
    private TimerTask vendingPasswordTask = null;
    private Timer vendingPasswordTimer;
    private int vendingPasswordTimerLength = 120000;
    private TimerTask vendingPictureTask = null;
    private Timer vendingPictureTimer;
    private int vendingPictureTimerLength = 120000;
    private TimerTask vendingProLinkTask = null;
    private Timer vendingProLinkTimer;
    private int vendingProLinkTimerLength = 120000;
    private TimerTask vendingStatusTask = null;
    private Timer vendingStatusTimer;
    private int vendingStatusTimerLength = 120000;
    private TimerTask vendingTask = null;
    private Timer vendingTimer;
    private int vendingTimerLength = 120000;

    public class ServiceBinder extends Binder {
        public DataServices getService() {
            return DataServices.this;
        }
    }

    public RequestDataFinishListener getSettingListener() {
        return this.settingListener;
    }

    public void setSettingListener(RequestDataFinishListener settingListener) {
        this.settingListener = settingListener;
    }

    public RequestDataFinishListener getNormalListener() {
        return this.normalListener;
    }

    public void setNormalListener(RequestDataFinishListener normalListener) {
        this.normalListener = normalListener;
    }

    public void onCreate() {
        if (Integer.valueOf(Constant.HEADER_VALUE_CLIENTVER.replace(".", "")).intValue() >= 230) {
            VersionDbOper.exec(this.sql2103);
            VersionDbOper.exec(this.sql2104);
        }
        this.taskMap = new HashMap();
        super.onCreate();
    }

    public IBinder onBind(Intent arg0) {
        IBinder result = null;
        if (null == null) {
            result = new ServiceBinder();
        }
        requestConfig();
        initParam();
        return result;
    }

    private void initParam() {
        for (InterfaceData config : new InterfaceDbOper().findAll()) {
            String wsid = config.getM03Target().trim();
            this.configMap.put(new StringBuilder(String.valueOf(wsid)).append("_").append(config.getM03Optype().trim()).toString(), config);
        }
        resetTimerLength();
        startUploadTimer();
        startDownloadTimer();
    }

    public void resetVending(String vcCode) {
        if (!StringHelper.isEmpty(vcCode, true)) {
            this.vendingCode = vcCode;
            this.initFlag = true;
            VendingDataParse parse = new VendingDataParse();
            parse.setListener(this);
            parse.requestVendingData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VENDING, this.vendingCode, this.initFlag);
        }
    }

    private void startConfigTimer() {
        cancelConfigTask();
        removeConfigTimer();
        initConfigTask();
        initConfigTimer();
        this.configTimer.schedule(this.configTask, DateHelper.truncateTime(new Date(), 1), (long) this.configTimerLength);
    }

    private void initConfigTimer() {
        this.configTimer = new Timer();
    }

    private void startUploadTimer() {
        cancelUploadTask();
        removeUploadTimer();
        initUploadTask();
        initUploadTimer();
        if (this.vendingStatusTimerLength > 0) {
            this.vendingStatusTimer.schedule(this.vendingStatusTask, (long) this.vendingStatusTimerLength, (long) this.vendingStatusTimerLength);
        }
        if (this.replenishmentStatusTimerLength > 0) {
            this.replenishmentStatusTimer.schedule(this.replenishmentStatusTask, (long) this.replenishmentStatusTimerLength, (long) this.replenishmentStatusTimerLength);
        }
        if (this.replenishmentDiffTimerLength > 0) {
            this.replenishmentDiffTimer.schedule(this.replenishmentDiffTask, (long) this.replenishmentDiffTimerLength, (long) this.replenishmentDiffTimerLength);
        }
        if (this.inventoryTimerLength > 0) {
            this.inventoryTimer.schedule(this.inventoryTask, (long) this.inventoryTimerLength, (long) this.inventoryTimerLength);
        }
        if (this.stockTransactionTimerLength > 0) {
            this.stockTransactionTimer.schedule(this.stockTransactionTask, (long) this.stockTransactionTimerLength, (long) this.stockTransactionTimerLength);
        }
    }

    private void startDownloadTimer() {
        cancelDownLoadTask();
        removeDownTimer();
        synDataFlag = true;
        this.vendingTimer = new Timer();
        this.vendingTask = new TimerTask() {
            public void run() {
                ZillionLog.i("vendingTask", "vendingTask start...");
                InterfaceData vendingConfig = (InterfaceData) DataServices.this.configMap.get("0f1e740c-c41a-484f-afe3-8e7f2eff99ee_GetData");
                if (true) {
                    Message message = new Message();
                    message.what = 3;
                    DataServices.this.handler.sendMessage(message);
                }
            }
        };
        if (this.vendingTimerLength > 0) {
            this.vendingTimer.schedule(this.vendingTask, (long) this.vendingTimerLength, (long) this.vendingTimerLength);
        }
    }

    private void resetTimerLength() {
        int m03ExeInterval;
        InterfaceData vendingConfig = (InterfaceData) this.configMap.get("0f1e740c-c41a-484f-afe3-8e7f2eff99ee_GetData");
        if (vendingConfig != null) {
            m03ExeInterval = vendingConfig.getM03ExeInterval() * 1000;
        } else {
            m03ExeInterval = this.vendingTimerLength;
        }
        this.vendingTimerLength = m03ExeInterval;
        ZillionLog.i(getClass().getName(), "resetTimerLength");
        InterfaceData vendingPictureConfig = (InterfaceData) this.configMap.get("c6c1dc3d-95f4-4e2f-80e8-6e3505d03895_GetData");
        if (vendingPictureConfig != null) {
            m03ExeInterval = vendingPictureConfig.getM03ExeInterval() * 1000;
        } else {
            m03ExeInterval = this.vendingPictureTimerLength;
        }
        this.vendingPictureTimerLength = m03ExeInterval;
        InterfaceData vendingChnConfig = (InterfaceData) this.configMap.get("7698adeb-d59b-4eb5-ba20-d635f988fa7c_GetData");
        if (vendingChnConfig != null) {
            m03ExeInterval = vendingChnConfig.getM03ExeInterval() * 1000;
        } else {
            m03ExeInterval = this.vendingChnTimerLength;
        }
        this.vendingChnTimerLength = m03ExeInterval;
        InterfaceData vendingProLinkConfig = (InterfaceData) this.configMap.get("7395899e-a13b-4de9-8d6b-d48d09a39915_GetData");
        if (vendingProLinkConfig != null) {
            m03ExeInterval = vendingProLinkConfig.getM03ExeInterval() * 1000;
        } else {
            m03ExeInterval = this.vendingProLinkTimerLength;
        }
        this.vendingProLinkTimerLength = m03ExeInterval;
        InterfaceData vendingCardPowerConfig = (InterfaceData) this.configMap.get("c2da7bde-f824-4066-bf93-fcdf94690ac0_GetData");
        this.vendingCardPowerTimerLength = vendingCardPowerConfig != null ? vendingCardPowerConfig.getM03ExeInterval() * 1000 : this.vendingCardPowerTimerLength;
        InterfaceData productMaterialPowerConfig = (InterfaceData) this.configMap.get("f821ae2c-1f02-4e6c-a7c4-e11661b4da60_GetData");
        this.productMaterialPowerTimerLength = productMaterialPowerConfig != null ? productMaterialPowerConfig.getM03ExeInterval() * 1000 : this.productMaterialPowerTimerLength;
        InterfaceData cardConfig = (InterfaceData) this.configMap.get("4f4e0f23-9b81-4b82-885d-6b9e07ebec18_GetData");
        this.cardTimerLength = cardConfig != null ? cardConfig.getM03ExeInterval() * 1000 : this.cardTimerLength;
        InterfaceData cusEmpCardPowerConfig = (InterfaceData) this.configMap.get("be98e646-5160-49ac-a986-05cd073393f7_GetData");
        this.cusEmpCardPowerTimerLength = cusEmpCardPowerConfig != null ? cusEmpCardPowerConfig.getM03ExeInterval() * 1000 : this.cusEmpCardPowerTimerLength;
        InterfaceData customerEmpLinkConfig = (InterfaceData) this.configMap.get("f42b379f-b3c9-4b09-96e1-4bd21c05ae7f_GetData");
        this.customerEmpLinkTimerLength = customerEmpLinkConfig != null ? customerEmpLinkConfig.getM03ExeInterval() * 1000 : this.customerEmpLinkTimerLength;
        InterfaceData customerConfig = (InterfaceData) this.configMap.get("5090a50f-fa68-4691-a0bb-af1b38676500_GetData");
        if (customerConfig != null) {
            m03ExeInterval = customerConfig.getM03ExeInterval() * 1000;
        } else {
            m03ExeInterval = this.customerTimerLength;
        }
        this.customerTimerLength = m03ExeInterval;
        InterfaceData productGroupConfig = (InterfaceData) this.configMap.get("de22c9b4-1be2-4301-a3cc-36c66a7aa9da_GetData");
        if (productGroupConfig != null) {
            m03ExeInterval = productGroupConfig.getM03ExeInterval() * 1000;
        } else {
            m03ExeInterval = this.productGroupTimerLength;
        }
        this.productGroupTimerLength = m03ExeInterval;
        InterfaceData productGroupPowerConfig = (InterfaceData) this.configMap.get("e8030392-15d7-467f-97ef-897a59dc039a_GetData");
        this.productGroupPowerTimerLength = productGroupPowerConfig != null ? productGroupPowerConfig.getM03ExeInterval() * 1000 : this.productGroupPowerTimerLength;
        InterfaceData productConfig = (InterfaceData) this.configMap.get("c81e6175-a15c-47b8-a3e2-a6c2fbf9d98b_GetData");
        if (productConfig != null) {
            m03ExeInterval = productConfig.getM03ExeInterval() * 1000;
        } else {
            m03ExeInterval = this.productTimerLength;
        }
        this.productTimerLength = m03ExeInterval;
        this.productRowCount = productConfig != null ? productConfig.getM03RowCount() : this.productRowCount;
        InterfaceData productPictureConfig = (InterfaceData) this.configMap.get("0cec0063-a032-4a37-aa45-889d554023d8_GetData");
        if (productPictureConfig != null) {
            m03ExeInterval = productPictureConfig.getM03ExeInterval() * 1000;
        } else {
            m03ExeInterval = this.productPictureTimerLength;
        }
        this.productPictureTimerLength = m03ExeInterval;
        if (productPictureConfig != null) {
            m03ExeInterval = productPictureConfig.getM03RowCount();
        } else {
            m03ExeInterval = this.productPictureRowCount;
        }
        this.productPictureRowCount = m03ExeInterval;
        InterfaceData supplierConfig = (InterfaceData) this.configMap.get("66b91d60-808b-4109-9dc2-2b9f08349bee_GetData");
        if (supplierConfig != null) {
            m03ExeInterval = supplierConfig.getM03ExeInterval() * 1000;
        } else {
            m03ExeInterval = this.supplierTimerLength;
        }
        this.supplierTimerLength = m03ExeInterval;
        this.supplierRowCount = supplierConfig != null ? supplierConfig.getM03RowCount() : this.supplierRowCount;
        InterfaceData stationConfig = (InterfaceData) this.configMap.get("72be83fe-24ca-4bf5-9851-248c3391d67a_GetData");
        if (stationConfig != null) {
            m03ExeInterval = stationConfig.getM03ExeInterval() * 1000;
        } else {
            m03ExeInterval = this.stationTimerLength;
        }
        this.stationTimerLength = m03ExeInterval;
        this.stationRowCount = stationConfig != null ? stationConfig.getM03RowCount() : this.stationRowCount;
        InterfaceData replenishmentConfig = (InterfaceData) this.configMap.get("7f342da0-05be-4f3a-96c3-28072ec31e7a_GetData");
        if (replenishmentConfig != null) {
            m03ExeInterval = replenishmentConfig.getM03ExeInterval() * 1000;
        } else {
            m03ExeInterval = this.replenishmentTimerLength;
        }
        this.replenishmentTimerLength = m03ExeInterval;
        if (replenishmentConfig != null) {
            m03ExeInterval = replenishmentConfig.getM03RowCount();
        } else {
            m03ExeInterval = this.replenishmentRowCount;
        }
        this.replenishmentRowCount = m03ExeInterval;
        InterfaceData vendingStatusConfig = (InterfaceData) this.configMap.get("badc8989-320d-4122-8b40-1e5b4ec2541c_UpdateStatus");
        if (vendingStatusConfig != null) {
            m03ExeInterval = vendingStatusConfig.getM03ExeInterval() * 1000;
        } else {
            m03ExeInterval = this.vendingStatusTimerLength;
        }
        this.vendingStatusTimerLength = m03ExeInterval;
        InterfaceData replenishmentStatusConfig = (InterfaceData) this.configMap.get("7f342da0-05be-4f3a-96c3-28072ec31e7a_UpdateStatus");
        this.replenishmentStatusTimerLength = replenishmentStatusConfig != null ? replenishmentStatusConfig.getM03ExeInterval() * 1000 : this.replenishmentStatusTimerLength;
        InterfaceData replenishmentDiffConfig = (InterfaceData) this.configMap.get("7f342da0-05be-4f3a-96c3-28072ec31e7a_UpdateDetailDifferentiaQty");
        this.replenishmentDiffTimerLength = replenishmentDiffConfig != null ? replenishmentDiffConfig.getM03ExeInterval() * 1000 : this.replenishmentDiffTimerLength;
        InterfaceData inventoryConfig = (InterfaceData) this.configMap.get("7774bf40-e172-4daf-88cb-e7cecfdc86bd_Insert");
        if (inventoryConfig != null) {
            m03ExeInterval = inventoryConfig.getM03ExeInterval() * 1000;
        } else {
            m03ExeInterval = this.inventoryTimerLength;
        }
        this.inventoryTimerLength = m03ExeInterval;
        InterfaceData stockTransactionConfig = (InterfaceData) this.configMap.get("f5051735-42a1-4003-afbf-b18b3d87c8f0_Insert");
        this.stockTransactionTimerLength = stockTransactionConfig != null ? stockTransactionConfig.getM03ExeInterval() * 1000 : this.stockTransactionTimerLength;
        InterfaceData vendingPasswordConfig = (InterfaceData) this.configMap.get("2f744c20-e5d9-4392-9ac2-1f4a6c7bdd00_GetData");
        this.vendingPasswordTimerLength = vendingPasswordConfig != null ? vendingPasswordConfig.getM03ExeInterval() * 1000 : this.vendingPasswordTimerLength;
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, 2, startId);
    }

    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    public void onDestroy() {
        ZillionLog.i(getClass().getName(), "销毁DataService");
        cancelConfigTask();
        removeConfigTimer();
        cancelDownLoadTask();
        removeDownTimer();
        cancelUploadTask();
        removeUploadTimer();
        super.onDestroy();
    }

    private void initUploadTimer() {
        this.vendingStatusTimer = new Timer();
        this.replenishmentStatusTimer = new Timer();
        this.replenishmentDiffTimer = new Timer();
        this.inventoryTimer = new Timer();
        this.stockTransactionTimer = new Timer();
    }

    private void initDownloadTimer() {
        this.vendingTimer = new Timer();
    }

    private void removeUploadTimer() {
        if (this.vendingStatusTimer != null) {
            this.vendingStatusTimer.cancel();
            this.vendingStatusTimer = null;
        }
        if (this.replenishmentDiffTimer != null) {
            this.replenishmentDiffTimer.cancel();
            this.replenishmentDiffTimer = null;
        }
        if (this.replenishmentStatusTimer != null) {
            this.replenishmentStatusTimer.cancel();
            this.replenishmentStatusTimer = null;
        }
        if (this.inventoryTimer != null) {
            this.inventoryTimer.cancel();
            this.inventoryTimer = null;
        }
        if (this.stockTransactionTimer != null) {
            this.stockTransactionTimer.cancel();
            this.stockTransactionTimer = null;
        }
    }

    private void removeConfigTimer() {
        if (this.configTimer != null) {
            this.configTimer.cancel();
            this.configTimer = null;
        }
    }

    private void removeDownTimer() {
        if (this.vendingTimer != null) {
            this.vendingTimer.cancel();
            this.vendingTimer = null;
        }
        if (this.vendingPictureTimer != null) {
            this.vendingPictureTimer.cancel();
            this.vendingPictureTimer = null;
        }
        if (this.vendingChnTimer != null) {
            this.vendingChnTimer.cancel();
            this.vendingChnTimer = null;
        }
        if (this.vendingProLinkTimer != null) {
            this.vendingProLinkTimer.cancel();
            this.vendingProLinkTimer = null;
        }
        if (this.productTimer != null) {
            this.productTimer.cancel();
            this.productTimer = null;
        }
        if (this.productPictureTimer != null) {
            this.productPictureTimer.cancel();
            this.productPictureTimer = null;
        }
        if (this.supplierTimer != null) {
            this.supplierTimer.cancel();
            this.supplierTimer = null;
        }
        if (this.stationTimer != null) {
            this.stationTimer.cancel();
            this.stationTimer = null;
        }
        if (this.vendingCardPowerTimer != null) {
            this.vendingCardPowerTimer.cancel();
            this.vendingCardPowerTimer = null;
        }
        if (this.productMaterialPowerTimer != null) {
            this.productMaterialPowerTimer.cancel();
            this.productMaterialPowerTimer = null;
        }
        if (this.cardTimer != null) {
            this.cardTimer.cancel();
            this.cardTimer = null;
        }
        if (this.cusEmpCardPowerTimer != null) {
            this.cusEmpCardPowerTimer.cancel();
            this.cusEmpCardPowerTimer = null;
        }
        if (this.customerEmpLinkTimer != null) {
            this.customerEmpLinkTimer.cancel();
            this.customerEmpLinkTimer = null;
        }
        if (this.customerTimer != null) {
            this.customerTimer.cancel();
            this.customerTimer = null;
        }
        if (this.productGroupTimer != null) {
            this.productGroupTimer.cancel();
            this.productGroupTimer = null;
        }
        if (this.productGroupPowerTimer != null) {
            this.productGroupPowerTimer.cancel();
            this.productGroupPowerTimer = null;
        }
        if (this.replenishmentTimer != null) {
            this.replenishmentTimer.cancel();
            this.replenishmentTimer = null;
        }
        if (this.vendingPasswordTimer != null) {
            this.vendingPasswordTimer.cancel();
            this.vendingPasswordTimer = null;
        }
    }

    private void cancelConfigTask() {
        if (this.configTask != null) {
            this.configTask.cancel();
            this.configTask = null;
        }
    }

    private void cancelUploadTask() {
        if (this.vendingStatusTask != null) {
            this.vendingStatusTask.cancel();
            this.vendingStatusTask = null;
        }
        if (this.replenishmentStatusTask != null) {
            this.replenishmentStatusTask.cancel();
            this.replenishmentStatusTask = null;
        }
        if (this.replenishmentDiffTask != null) {
            this.replenishmentDiffTask.cancel();
            this.replenishmentDiffTask = null;
        }
        if (this.inventoryTask != null) {
            this.inventoryTask.cancel();
            this.inventoryTask = null;
        }
        if (this.stockTransactionTask != null) {
            this.stockTransactionTask.cancel();
            this.stockTransactionTask = null;
        }
    }

    private void cancelDownLoadTask() {
        if (this.vendingTask != null) {
            this.vendingTask.cancel();
            this.vendingTask = null;
        }
        if (this.vendingPictureTask != null) {
            this.vendingPictureTask.cancel();
            this.vendingPictureTask = null;
        }
        if (this.vendingChnTask != null) {
            this.vendingChnTask.cancel();
            this.vendingChnTask = null;
        }
        if (this.vendingProLinkTask != null) {
            this.vendingProLinkTask.cancel();
            this.vendingProLinkTask = null;
        }
        if (this.productTask != null) {
            this.productTask.cancel();
            this.productTask = null;
        }
        if (this.productPictureTask != null) {
            this.productPictureTask.cancel();
            this.productPictureTask = null;
        }
        if (this.supplierTask != null) {
            this.supplierTask.cancel();
            this.supplierTask = null;
        }
        if (this.stationTask != null) {
            this.stationTask.cancel();
            this.stationTask = null;
        }
        if (this.vendingCardPowerTask != null) {
            this.vendingCardPowerTask.cancel();
            this.vendingCardPowerTask = null;
        }
        if (this.productMaterialPowerTask != null) {
            this.productMaterialPowerTask.cancel();
            this.productMaterialPowerTask = null;
        }
        if (this.cardTask != null) {
            this.cardTask.cancel();
            this.cardTask = null;
        }
        if (this.cusEmpCardPowerTask != null) {
            this.cusEmpCardPowerTask.cancel();
            this.cusEmpCardPowerTask = null;
        }
        if (this.customerEmpLinkTask != null) {
            this.customerEmpLinkTask.cancel();
            this.customerEmpLinkTask = null;
        }
        if (this.customerTask != null) {
            this.customerTask.cancel();
            this.customerTask = null;
        }
        if (this.productGroupTask != null) {
            this.productGroupTask.cancel();
            this.productGroupTask = null;
        }
        if (this.productGroupPowerTask != null) {
            this.productGroupPowerTask.cancel();
            this.productGroupPowerTask = null;
        }
        if (this.replenishmentTask != null) {
            this.replenishmentTask.cancel();
            this.replenishmentTask = null;
        }
        if (this.vendingPasswordTask != null) {
            this.vendingPasswordTask.cancel();
            this.vendingPasswordTask = null;
        }
    }

    private void initConfigTask() {
        this.configTask = new TimerTask() {
            public void run() {
                ZillionLog.i("vendingTask", "initConfigTask start..");
                if (DataServices.isTaskStart((InterfaceData) DataServices.this.configMap.get("5da83d05-e941-4059-ad49-85ebf2d32de3_GetData"))) {
                    Message message = new Message();
                    message.what = 2;
                    DataServices.this.handler.sendMessage(message);
                }
            }
        };
    }

    private void initUploadTask() {
        this.vendingStatusTask = new TimerTask() {
            public void run() {
                ZillionLog.i("vendingTask", "vendingStatusTask start..");
                if (DataServices.isTaskStart((InterfaceData) DataServices.this.configMap.get("badc8989-320d-4122-8b40-1e5b4ec2541c_UpdateStatus"))) {
                    Message message = new Message();
                    message.what = 20;
                    DataServices.this.handler.sendMessage(message);
                }
            }
        };
        this.replenishmentStatusTask = new TimerTask() {
            public void run() {
                ZillionLog.i("vendingTask", "replenishmentStatusTask start..");
                if (DataServices.isTaskStart((InterfaceData) DataServices.this.configMap.get("7f342da0-05be-4f3a-96c3-28072ec31e7a_UpdateStatus"))) {
                    Message message = new Message();
                    message.what = 21;
                    DataServices.this.handler.sendMessage(message);
                }
            }
        };
        this.replenishmentDiffTask = new TimerTask() {
            public void run() {
                ZillionLog.i("vendingTask", "replenishmentDiffTask start..");
                if (DataServices.isTaskStart((InterfaceData) DataServices.this.configMap.get("7f342da0-05be-4f3a-96c3-28072ec31e7a_UpdateDetailDifferentiaQty"))) {
                    Message message = new Message();
                    message.what = 22;
                    DataServices.this.handler.sendMessage(message);
                }
            }
        };
        this.inventoryTask = new TimerTask() {
            public void run() {
                ZillionLog.i("vendingTask", "inventoryTask start..");
                if (DataServices.isTaskStart((InterfaceData) DataServices.this.configMap.get("7774bf40-e172-4daf-88cb-e7cecfdc86bd_Insert"))) {
                    Message message = new Message();
                    message.what = 23;
                    DataServices.this.handler.sendMessage(message);
                }
            }
        };
        this.stockTransactionTask = new TimerTask() {
            public void run() {
                ZillionLog.i("vendingTask", "stockTransactionTask start..");
                if (DataServices.isTaskStart((InterfaceData) DataServices.this.configMap.get("f5051735-42a1-4003-afbf-b18b3d87c8f0_Insert"))) {
                    Message message = new Message();
                    message.what = 24;
                    DataServices.this.handler.sendMessage(message);
                }
            }
        };
    }

    private void initDownloadTask() {
        this.vendingTask = new TimerTask() {
            public void run() {
                if (DataServices.isTaskStart((InterfaceData) DataServices.this.configMap.get("0f1e740c-c41a-484f-afe3-8e7f2eff99ee_GetData"))) {
                    Message message = new Message();
                    message.what = 3;
                    DataServices.this.handler.sendMessage(message);
                }
            }
        };
    }

    public void requestConfigParse() {
        ConfigDataParse configParse = new ConfigDataParse();
        configParse.setListener(this);
        configParse.requestConfigData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CONFIG, this.vendingId);
    }

    public void requestAutherParse() {
        AutherDataParse parse = new AutherDataParse();
        parse.setListener(this);
        parse.requestAutherData(Constant.HTTP_OPERATE_TYPE_DESGET, Constant.METHOD_WSID_AUTHER, Constant.BODY_VALUE_UDID);
        this.taskMap.put(Constant.METHOD_WSID_AUTHER, "0");
    }

    public void requestVendingParse() {
        VendingDataParse parse = new VendingDataParse();
        parse.setListener(this);
        parse.requestVendingData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VENDING, this.vendingCode, this.initFlag);
        this.taskMap.put(Constant.METHOD_WSID_VENDING, "0");
    }

    public void requestVendingPictureParse() {
        VendingPictureDataParse parse = new VendingPictureDataParse();
        parse.setListener(this);
        parse.requestVendingPictureData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VENDINGPICTURE, this.vendingId);
        this.taskMap.put(Constant.METHOD_WSID_VENDINGPICTURE, "0");
    }

    public void requestVendingChnParse() {
        VendingChnDataParse parse = new VendingChnDataParse();
        parse.setListener(this);
        parse.requestVendingChnData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VENDINGCHN, this.vendingId);
        this.taskMap.put(Constant.METHOD_WSID_VENDINGCHN, "0");
    }

    public void requestVendingProLinkParse() {
        VendingProLinkDataParse parse = new VendingProLinkDataParse();
        parse.setListener(this);
        parse.requestVendingProLinkData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VENDINGPROLINK, this.vendingId);
        this.taskMap.put(Constant.METHOD_WSID_VENDINGPROLINK, "0");
    }

    public void requestProductParse() {
        ProductDataParse parse = new ProductDataParse();
        parse.setListener(this);
        parse.requestProductData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_PRODUCT, this.vendingId, this.productRowCount);
        this.taskMap.put(Constant.METHOD_WSID_PRODUCT, "0");
    }

    public void requestProductPictureParse() {
        ProductPictureDataParse parse = new ProductPictureDataParse();
        parse.setListener(this);
        parse.requestProductPictureData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_PRODUCTPICTURE, this.vendingId, this.productPictureRowCount);
        this.taskMap.put(Constant.METHOD_WSID_PRODUCTPICTURE, "0");
    }

    public void requestSupplierParse() {
        SupplierDataParse parse = new SupplierDataParse();
        parse.setListener(this);
        parse.requestSupplierData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_SUPPLIER, this.vendingId, this.supplierRowCount);
        this.taskMap.put(Constant.METHOD_WSID_SUPPLIER, "0");
    }

    public void requestStationParse() {
        StationDataParse parse = new StationDataParse();
        parse.setListener(this);
        parse.requestStationData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_STATION, this.vendingId, this.stationRowCount);
        this.taskMap.put(Constant.METHOD_WSID_STATION, "0");
    }

    public void requestVendingCardPowerParse() {
        VendingCardPowerDataParse parse = new VendingCardPowerDataParse();
        parse.setListener(this);
        parse.requestVendingCardPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_VENDINGCARDPOWER, this.vendingId);
        this.taskMap.put(Constant.METHOD_WSID_VENDINGCARDPOWER, "0");
    }

    public void requestProductMaterialPowerParse() {
        ProductMaterialPowerDataParse parse = new ProductMaterialPowerDataParse();
        parse.setListener(this);
        parse.requestProductMaterialPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_PRODUCTMATERIAKPOWER, this.vendingId);
        this.taskMap.put(Constant.METHOD_WSID_PRODUCTMATERIAKPOWER, "0");
    }

    public void requestCardParse() {
        CardDataParse parse = new CardDataParse();
        parse.setListener(this);
        parse.requestCardData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CARD, this.vendingId);
        this.taskMap.put(Constant.METHOD_WSID_CARD, "0");
    }

    public void requestCusEmpCardPowerParse() {
        CusEmpCardPowerDataParse parse = new CusEmpCardPowerDataParse();
        parse.setListener(this);
        parse.requestCusEmpCardPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CUSEMPCARDPOWER, this.vendingId);
        this.taskMap.put(Constant.METHOD_WSID_CUSEMPCARDPOWER, "0");
    }

    public void requestCustomerEmpLinkParse() {
        CustomerEmpLinkDataParse parse = new CustomerEmpLinkDataParse();
        parse.setListener(this);
        parse.requestCustomerEmpLinkData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CUSTOMEREMPLINK, this.vendingId);
        this.taskMap.put(Constant.METHOD_WSID_CUSTOMEREMPLINK, "0");
    }

    public void requestCustomerParse() {
        CustomerDataParse parse = new CustomerDataParse();
        parse.setListener(this);
        parse.requestCustomerData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CUSTOMER, this.vendingId);
        this.taskMap.put(Constant.METHOD_WSID_CUSTOMER, "0");
    }

    public void requestProductGroupParse() {
        ProductGroupDataParse parse = new ProductGroupDataParse();
        parse.setListener(this);
        parse.requestProductGroupData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_PRODUCTGROUP, this.vendingId);
        this.taskMap.put(Constant.METHOD_WSID_PRODUCTGROUP, "0");
    }

    public void requestProductGroupPowerParse() {
        ProductGroupPowerDataParse parse = new ProductGroupPowerDataParse();
        parse.setListener(this);
        parse.requestProductGroupPowerData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_PRODUCTGROUPPOWER, this.vendingId);
        this.taskMap.put(Constant.METHOD_WSID_PRODUCTGROUPPOWER, "0");
    }

    public void requestReplenishmentParse() {
        ReplenishmentDataParse parse = new ReplenishmentDataParse();
        parse.setListener(this);
        parse.requestReplenishmentData(Constant.HTTP_OPERATE_TYPE_GETDATA, "7f342da0-05be-4f3a-96c3-28072ec31e7a", this.vendingId, this.replenishmentRowCount);
        this.taskMap.put("7f342da0-05be-4f3a-96c3-28072ec31e7a", "0");
    }

    public void requestVendingPasswordParse() {
        VendingPasswordDataParse parse = new VendingPasswordDataParse();
        parse.setListener(this);
        parse.requestVendingPasswordData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_PASSWORD, this.vendingId);
        this.taskMap.put(Constant.METHOD_WSID_PASSWORD, "0");
    }

    public static boolean isTaskStart(InterfaceData config) {
        boolean flag = false;
        if (config == null) {
            return false;
        }
        String startTime = config.getM03StartTime();
        String endTime = config.getM03EndTime();
        Date currentDate = new Date();
        String currentYMD = DateHelper.format(currentDate, "yyyy-MM-dd");
        long current_time = currentDate.getTime();
        long start_time = DateHelper.parse(new StringBuilder(String.valueOf(currentYMD)).append(" ").append(startTime).toString(), "yyyy-MM-dd HH:mm:ss").getTime();
        long end_time = DateHelper.parse(new StringBuilder(String.valueOf(currentYMD)).append(" ").append(endTime).toString(), "yyyy-MM-dd HH:mm:ss").getTime();
        if (current_time >= start_time && current_time <= end_time) {
            flag = true;
        }
        return flag;
    }

    public void parseRequestFinised(BaseData baseData) {
        if (baseData.isSuccess().booleanValue()) {
            if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDING)) {
                if (synDataFlag) {
                    if (this.taskMap.containsKey(Constant.METHOD_WSID_VENDING)) {
                        this.taskMap.remove(Constant.METHOD_WSID_VENDING);
                    }
                    reCountMap();
                } else {
                    requestConfig();
                    this.initFlag = false;
                }
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CONFIG)) {
                ZillionLog.i(getClass().getName(), "parseRequestFinised:");
                startConfigTimer();
                initParam();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_AUTHER)) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_AUTHER)) {
                    this.taskMap.remove(Constant.METHOD_WSID_AUTHER);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDINGPICTURE)) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_VENDINGPICTURE)) {
                    this.taskMap.remove(Constant.METHOD_WSID_VENDINGPICTURE);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDINGCHN)) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_VENDINGCHN)) {
                    this.taskMap.remove(Constant.METHOD_WSID_VENDINGCHN);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDINGPROLINK)) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_VENDINGPROLINK)) {
                    this.taskMap.remove(Constant.METHOD_WSID_VENDINGPROLINK);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCT)) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_PRODUCT)) {
                    this.taskMap.remove(Constant.METHOD_WSID_PRODUCT);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCTPICTURE)) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_PRODUCTPICTURE)) {
                    this.taskMap.remove(Constant.METHOD_WSID_PRODUCTPICTURE);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_SUPPLIER)) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_SUPPLIER)) {
                    this.taskMap.remove(Constant.METHOD_WSID_SUPPLIER);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_STATION)) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_STATION)) {
                    this.taskMap.remove(Constant.METHOD_WSID_STATION);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDINGCARDPOWER)) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_VENDINGCARDPOWER)) {
                    this.taskMap.remove(Constant.METHOD_WSID_VENDINGCARDPOWER);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCTMATERIAKPOWER)) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_PRODUCTMATERIAKPOWER)) {
                    this.taskMap.remove(Constant.METHOD_WSID_PRODUCTMATERIAKPOWER);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CARD)) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_CARD)) {
                    this.taskMap.remove(Constant.METHOD_WSID_CARD);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CUSEMPCARDPOWER)) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_CUSEMPCARDPOWER)) {
                    this.taskMap.remove(Constant.METHOD_WSID_CUSEMPCARDPOWER);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CUSTOMEREMPLINK)) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_CUSTOMEREMPLINK)) {
                    this.taskMap.remove(Constant.METHOD_WSID_CUSTOMEREMPLINK);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CUSTOMER)) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_CUSTOMER)) {
                    this.taskMap.remove(Constant.METHOD_WSID_CUSTOMER);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCTGROUP)) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_PRODUCTGROUP)) {
                    this.taskMap.remove(Constant.METHOD_WSID_PRODUCTGROUP);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals("7f342da0-05be-4f3a-96c3-28072ec31e7a")) {
                if (this.taskMap.containsKey("7f342da0-05be-4f3a-96c3-28072ec31e7a")) {
                    this.taskMap.remove("7f342da0-05be-4f3a-96c3-28072ec31e7a");
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCTGROUPPOWER)) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_PRODUCTGROUPPOWER)) {
                    this.taskMap.remove(Constant.METHOD_WSID_PRODUCTGROUPPOWER);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PASSWORD)) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_PASSWORD)) {
                    this.taskMap.remove(Constant.METHOD_WSID_PASSWORD);
                }
                reCountMap();
            } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_SYNDATA)) {
                startDownloadTimer();
            }
        }
    }

    public void parseRequestFailure(BaseData baseData) {
        if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CONFIG)) {
            ZillionLog.i(getClass().getName(), "parseRequestFailure:");
            VendingData vending = new VendingDbOper().getVending();
            this.initFlag = false;
            if (vending != null) {
                this.vendingId = vending.getVd1Id();
                this.vendingCode = vending.getVd1Code();
                startConfigTimer();
            }
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDING)) {
            if (!synDataFlag) {
                if (this.taskMap.containsKey(Constant.METHOD_WSID_VENDING)) {
                    this.taskMap.remove(Constant.METHOD_WSID_VENDING);
                }
                reCountMap();
            }
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_AUTHER)) {
            if (this.taskMap.containsKey(Constant.METHOD_WSID_AUTHER)) {
                this.taskMap.remove(Constant.METHOD_WSID_AUTHER);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDINGPICTURE)) {
            if (this.taskMap.containsKey(Constant.METHOD_WSID_VENDINGPICTURE)) {
                this.taskMap.remove(Constant.METHOD_WSID_VENDINGPICTURE);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDINGCHN)) {
            if (this.taskMap.containsKey(Constant.METHOD_WSID_VENDINGCHN)) {
                this.taskMap.remove(Constant.METHOD_WSID_VENDINGCHN);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDINGPROLINK)) {
            if (this.taskMap.containsKey(Constant.METHOD_WSID_VENDINGPROLINK)) {
                this.taskMap.remove(Constant.METHOD_WSID_VENDINGPROLINK);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCT)) {
            if (this.taskMap.containsKey(Constant.METHOD_WSID_PRODUCT)) {
                this.taskMap.remove(Constant.METHOD_WSID_PRODUCT);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCTPICTURE)) {
            if (this.taskMap.containsKey(Constant.METHOD_WSID_PRODUCTPICTURE)) {
                this.taskMap.remove(Constant.METHOD_WSID_PRODUCTPICTURE);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_SUPPLIER)) {
            if (this.taskMap.containsKey(Constant.METHOD_WSID_SUPPLIER)) {
                this.taskMap.remove(Constant.METHOD_WSID_SUPPLIER);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_STATION)) {
            if (this.taskMap.containsKey(Constant.METHOD_WSID_STATION)) {
                this.taskMap.remove(Constant.METHOD_WSID_STATION);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_VENDINGCARDPOWER)) {
            if (this.taskMap.containsKey(Constant.METHOD_WSID_VENDINGCARDPOWER)) {
                this.taskMap.remove(Constant.METHOD_WSID_VENDINGCARDPOWER);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCTMATERIAKPOWER)) {
            if (this.taskMap.containsKey(Constant.METHOD_WSID_PRODUCTMATERIAKPOWER)) {
                this.taskMap.remove(Constant.METHOD_WSID_PRODUCTMATERIAKPOWER);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CARD)) {
            if (this.taskMap.containsKey(Constant.METHOD_WSID_CARD)) {
                this.taskMap.remove(Constant.METHOD_WSID_CARD);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CUSEMPCARDPOWER)) {
            if (this.taskMap.containsKey(Constant.METHOD_WSID_CUSEMPCARDPOWER)) {
                this.taskMap.remove(Constant.METHOD_WSID_CUSEMPCARDPOWER);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CUSTOMEREMPLINK)) {
            if (this.taskMap.containsKey(Constant.METHOD_WSID_CUSTOMEREMPLINK)) {
                this.taskMap.remove(Constant.METHOD_WSID_CUSTOMEREMPLINK);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_CUSTOMER)) {
            if (this.taskMap.containsKey(Constant.METHOD_WSID_CUSTOMER)) {
                this.taskMap.remove(Constant.METHOD_WSID_CUSTOMER);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCTGROUP)) {
            if (this.taskMap.containsKey(Constant.METHOD_WSID_PRODUCTGROUP)) {
                this.taskMap.remove(Constant.METHOD_WSID_PRODUCTGROUP);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals("7f342da0-05be-4f3a-96c3-28072ec31e7a")) {
            if (this.taskMap.containsKey("7f342da0-05be-4f3a-96c3-28072ec31e7a")) {
                this.taskMap.remove("7f342da0-05be-4f3a-96c3-28072ec31e7a");
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PRODUCTGROUPPOWER)) {
            if (this.taskMap.containsKey(Constant.METHOD_WSID_PRODUCTGROUPPOWER)) {
                this.taskMap.remove(Constant.METHOD_WSID_PRODUCTGROUPPOWER);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_PASSWORD)) {
            if (this.taskMap.containsKey(Constant.METHOD_WSID_PASSWORD)) {
                this.taskMap.remove(Constant.METHOD_WSID_PASSWORD);
            }
            reCountMap();
        } else if (baseData.getRequestURL().equals(Constant.METHOD_WSID_SYNDATA)) {
            if (this.settingListener != null) {
                this.settingListener.requestFailure();
            }
            if (this.normalListener != null) {
                this.normalListener.requestFailure();
            }
            synDataFlag = false;
        }
    }

    private void reCountMap() {
        if (this.taskMap != null && this.taskMap.size() == 0) {
            if (this.settingListener != null) {
                this.settingListener.requestFinished();
            }
            if (this.normalListener != null) {
                this.normalListener.requestFinished();
            }
            synDataFlag = false;
        }
    }

    public void requestConfig() {
        VendingData vending = new VendingDbOper().getVending();
        if (vending != null && !StringHelper.isEmpty(vending.getVd1Id(), true)) {
            ZillionLog.i(getClass().getName(), "requestConfig");
            this.vendingId = vending.getVd1Id();
            this.vendingCode = vending.getVd1Code();
            ConfigDataParse parse = new ConfigDataParse();
            parse.setListener(this);
            parse.requestConfigData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_CONFIG, this.vendingId);
        }
    }

    public void synData() {
        SynDataParse parse = new SynDataParse();
        parse.setListener(this);
        parse.requestSynData(Constant.HTTP_OPERATE_TYPE_GETDATA, Constant.METHOD_WSID_SYNDATA);
    }
}
