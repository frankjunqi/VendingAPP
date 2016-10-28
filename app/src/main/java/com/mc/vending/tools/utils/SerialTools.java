package com.mc.vending.tools.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.mc.vending.activitys.pick.MC_CombinationPickDetailActivity;
import com.mc.vending.config.Constant;
import com.mc.vending.data.StockTransactionData;
import com.mc.vending.db.VendingDbOper;
import com.mc.vending.tools.ZillionLog;
import com.zillion.evm.jssc.SerialPort;
import com.zillion.evm.jssc.SerialPortEvent;
import com.zillion.evm.jssc.SerialPortEventListener;
import com.zillion.evm.jssc.SerialPortException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class SerialTools {
    public static final String FUNCTION_KEY_BACK = "2A";
    public static final String FUNCTION_KEY_BORROW = "23";
    public static final String FUNCTION_KEY_CANCEL = "1B";
    public static final String FUNCTION_KEY_COMBINATION = "50";
    public static final String FUNCTION_KEY_CONFIRM = "0D";
    public static final String FUNCTION_KEY_SET = "08";
    public static final int MESSAGE_LOG_mKeyBoard = 1;
    public static final int MESSAGE_LOG_mRFIDReader = 2;
    public static final int MESSAGE_LOG_mStore = 4;
    public static final int MESSAGE_LOG_mStore_check = 5;
    public static final int MESSAGE_LOG_mVender = 3;
    public static final int MESSAGE_LOG_mVender_check = 6;
    private static final String PortName_mKeyBoard = Constant.SerialToolsPortName[2];
    private static final String PortName_mRFIDReader = Constant.SerialToolsPortName[1];
    private static final String PortName_mStore = Constant.SerialToolsPortName[3];
    private static final String PortName_mVender = Constant.SerialToolsPortName[0];
    private static final String TAG = SerialTools.class.getName();
    public static final String cmdBeep = "030FFF00F0";
    public static final String cmdCloseKeyBoard = "02303234353030343703";
    public static final String cmdGetSerialNo = "01F0F0";
    public static final String cmdOpenKeyBoard = "02303234353033343403";
    private static final int iDelay = 500;
    private static SerialTools instance = null;
    private static Map<String, String> keymap;
    public static List<String> storeMsg = new ArrayList();
    private SerialPortEventListener listener = new SerialPortEventListener() {
        public void serialEvent(SerialPortEvent event) {
            if (event.isRXCHAR()) {
                int obtain = 0;
                if (event.getEventValue() > 0) {
                    String data = null;
                    try {
                        if (SerialTools.PortName_mKeyBoard.equals(event.getPortName())) {
                            data = SerialTools.this.mKeyBoard.readHexString(event.getEventValue());
                            obtain = 1;
                        } else if (SerialTools.PortName_mRFIDReader.equals(event.getPortName())) {
                            data = SerialTools.this.mRFIDReader.readHexString(event.getEventValue());
                            obtain = 2;
                            Log.i(SerialTools.TAG, "Receive " + data.length() + " Bytes: " + data);
                        } else if (SerialTools.PortName_mVender.equals(event.getPortName())) {
                            obtain = 3;
                            data = SerialTools.this.mVender.readHexString(event.getEventValue());
                        } else if (SerialTools.PortName_mStore.equals(event.getPortName())) {
                            obtain = 4;
                            data = SerialTools.this.mStore.readHexString(event.getEventValue());
                        }
                        Message m = Message.obtain(SerialTools.this.mHandler, obtain);
                        m.obj = data;
                        SerialTools.this.mHandler.sendMessage(m);
                    } catch (SerialPortException e) {
                        e.printStackTrace();
                    }
                }
            } else if (!event.isCTS()) {
                event.isDSR();
            }
        }
    };
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (SerialTools.this.toolsListener != null && (SerialTools.this.toolsListener instanceof MC_CombinationPickDetailActivity)) {
                        SerialTools.this.toolsListener.serialReturn((String) msg.obj, msg.what, SerialTools.this.userInfo);
                        return;
                    } else if (SerialTools.this.toolsListener != null) {
                        SerialTools.this.toolsListener.serialReturn((String) msg.obj, msg.what);
                        return;
                    } else {
                        return;
                    }
                case 2:
                    if (MyFunc.getRFIDSerialNo((String) msg.obj) != null) {
                        try {
                            SerialTools.this.sendPortData(SerialTools.this.mRFIDReader, "030FFF00F0", true);
                        } catch (SerialPortException e) {
                            e.printStackTrace();
                        }
                    }
                    if (SerialTools.this.toolsListener != null && (SerialTools.this.toolsListener instanceof MC_CombinationPickDetailActivity)) {
                        SerialTools.this.toolsListener.serialReturn((String) msg.obj, msg.what, SerialTools.this.userInfo);
                        return;
                    } else if (SerialTools.this.toolsListener != null) {
                        SerialTools.this.toolsListener.serialReturn((String) msg.obj, msg.what);
                        return;
                    } else {
                        return;
                    }
                case 3:
                    if (SerialTools.this.mVender.getRequestMethod() == 3) {
                        SerialTools.this.checkVender(SerialTools.this.mVender.getCheckA(), SerialTools.this.mVender.getCheckB());
                        return;
                    }
                    msg.what = SerialTools.this.mVender.getRequestMethod();
                    if (SerialTools.this.toolsListener != null && (SerialTools.this.toolsListener instanceof MC_CombinationPickDetailActivity)) {
                        SerialTools.this.toolsListener.serialReturn((String) msg.obj, msg.what, SerialTools.this.userInfo);
                        return;
                    } else if (SerialTools.this.toolsListener != null) {
                        SerialTools.this.toolsListener.serialReturn((String) msg.obj, msg.what);
                        return;
                    } else {
                        return;
                    }
                case 4:
                    if (SerialTools.this.mStore.getRequestMethod() == 4) {
                        SerialTools.storeMsg = new ArrayList();
                        for (int i = 0; i < 5; i++) {
                            SerialTools.this.checkStore(SerialTools.this.mStore.getCheckA(), SerialTools.this.mStore.getCheckB());
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e2) {
                            }
                        }
                        SerialTools.storeMsg.add((String) msg.obj);
                        return;
                    }
                    msg.what = SerialTools.this.mStore.getRequestMethod();
                    if (SerialTools.this.toolsListener != null && (SerialTools.this.toolsListener instanceof MC_CombinationPickDetailActivity)) {
                        SerialTools.this.toolsListener.serialReturn((String) msg.obj, msg.what, SerialTools.this.userInfo);
                        return;
                    } else if (SerialTools.this.toolsListener != null) {
                        SerialTools.storeMsg.add((String) msg.obj);
                        SerialTools.this.toolsListener.serialReturn((String) msg.obj, msg.what);
                        return;
                    } else {
                        return;
                    }
                default:
                    return;
            }
        }
    };
    private final SerialPort mKeyBoard = new SerialPort(PortName_mKeyBoard);
    private final SerialPort mRFIDReader = new SerialPort(PortName_mRFIDReader);
    private SendThread mSendThread;
    private final SerialPort mStore = new SerialPort(PortName_mStore);
    private Timer mStoreTimer;
    private final SerialPort mVender = new SerialPort(PortName_mVender);
    private MC_SerialToolsListener toolsListener;
    public Object userInfo;

    private class SendThread extends Thread {
        public boolean suspendFlag;

        private SendThread() {
            this.suspendFlag = true;
        }

        public void run() {
            super.run();
            while (!isInterrupted()) {
                synchronized (this) {
                    while (this.suspendFlag) {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    SerialTools.this.sendPortData(SerialTools.this.mRFIDReader, "01F0F0", true);
                } catch (SerialPortException e2) {
                    e2.printStackTrace();
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e3) {
                    e3.printStackTrace();
                }
            }
        }

        public void setSuspendFlag() {
            this.suspendFlag = true;
        }

        public synchronized void setResume() {
            this.suspendFlag = false;
            notify();
        }
    }

    static {
        keymap = null;
        keymap = new HashMap();
        keymap.put("30", "0");
        keymap.put("31", "1");
        keymap.put("32", "2");
        keymap.put("33", "3");
        keymap.put("34", StockTransactionData.BILL_TYPE_GET);
        keymap.put("35", StockTransactionData.BILL_TYPE_RETURN);
        keymap.put("36", StockTransactionData.BILL_TYPE_BORROW);
        keymap.put("37", StockTransactionData.BILL_TYPE_All);
        keymap.put("38", StockTransactionData.BILL_TYPE_DIFFAll);
        keymap.put("39", "9");
    }

    public String getKeyValue(String key) {
        if (keymap.containsKey(key)) {
            return (String) keymap.get(key);
        }
        return "";
    }

    private SerialTools() {
    }

    public static SerialTools getInstance() {
        if (instance == null) {
            instance = new SerialTools();
        }
        return instance;
    }

    public void addToolsListener(MC_SerialToolsListener listener) {
        this.toolsListener = listener;
    }

    public void openKeyBoard() {
        try {
            if (this.mKeyBoard.isOpened() || this.mKeyBoard.openPort()) {
                try {
                    this.mKeyBoard.addEventListener(this.listener);
                } catch (Exception e) {
                }
                this.mKeyBoard.setParams(SerialPort.BAUDRATE_9600, 8, 1, 0);
                sendPortData(this.mKeyBoard, "02303234353033343403", true);
            }
        } catch (SerialPortException e2) {
        }
    }

    public void closeKeyBoard() throws SerialPortException {
        if (this.mKeyBoard.isOpened()) {
            sendPortData(this.mKeyBoard, "02303234353030343703", true);
        }
        this.mKeyBoard.closePort();
    }

    public void openRFIDReader() {
        try {
            if (this.mRFIDReader.isOpened() || this.mRFIDReader.openPort()) {
                try {
                    this.mRFIDReader.addEventListener(this.listener);
                } catch (Exception e) {
                }
                if (new VendingDbOper().getVending().getVd1CardType().equals("1")) {
                    this.mRFIDReader.setParams(SerialPort.BAUDRATE_19200, 8, 1, 0);
                    if (this.mSendThread == null) {
                        this.mSendThread = new SendThread();
                        this.mSendThread.start();
                    }
                    this.mSendThread.setResume();
                    return;
                }
                this.mRFIDReader.setParams(SerialPort.BAUDRATE_9600, 8, 1, 0);
            }
        } catch (SerialPortException e2) {
        }
    }

    public void closeRFIDReader() throws SerialPortException {
        try {
            if (!(this.mSendThread == null || this.mSendThread.isInterrupted())) {
                this.mSendThread.setSuspendFlag();
            }
            if (this.mRFIDReader.isOpened()) {
                this.mRFIDReader.closePort();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openStore(int a, int b, int c) {
        ZillionLog.i(TAG, "格子机开门：" + a + " " + b + " " + c);
        try {
            if (this.mStore.isOpened() || this.mStore.openPort()) {
                try {
                    this.mStore.addEventListener(this.listener);
                } catch (Exception e) {
                }
                this.mStore.setCheckA(a);
                this.mStore.setCheckB(b);
                this.mStore.setRequestMethod(4);
                this.mStore.setParams(SerialPort.BAUDRATE_38400, 8, 1, 0);
                sendPortData(this.mStore, MyFunc.cmdOpenStoreDoor(a, b, c), true);
            }
        } catch (SerialPortException e2) {
            ZillionLog.e(TAG, new StringBuilder(String.valueOf(a)).append(" ").append(b).append(" ").append(c).append(e2.getMessage()).toString(), e2);
            e2.printStackTrace();
        }
    }

    public void checkStore(int a, int b) {
        ZillionLog.i(TAG, "检查格子机状态： " + a + b);
        try {
            if (this.mStore.isOpened()) {
                try {
                    this.mStore.addEventListener(this.listener);
                } catch (Exception e) {
                }
                this.mStore.setCheckA(a);
                this.mStore.setCheckB(b);
                this.mStore.setRequestMethod(5);
                this.mStore.setParams(SerialPort.BAUDRATE_38400, 8, 1, 0);
                sendPortData(this.mStore, MyFunc.cmdCheckStoreDoor(a, b), true);
            }
        } catch (SerialPortException e2) {
            e2.printStackTrace();
        }
    }

    public void closeStore() throws SerialPortException {
        if (this.mStore.isOpened()) {
            this.mStore.closePort();
        }
    }

    public void openVender(int a, int b, Object mUserInfo) {
        this.userInfo = mUserInfo;
        try {
            if (this.mVender.isOpened() || this.mVender.openPort()) {
                try {
                    this.mVender.addEventListener(this.listener);
                } catch (Exception e) {
                }
                this.mVender.setCheckA(a);
                this.mVender.setCheckB(b);
                this.mVender.setRequestMethod(3);
                this.mVender.setParams(SerialPort.BAUDRATE_9600, 8, 1, 0);
                sendPortData(this.mVender, MyFunc.cmdOpenVender(a, b), true);
                Log.d(TAG, MyFunc.cmdOpenVender(a, b));
            }
        } catch (SerialPortException e2) {
            e2.printStackTrace();
        }
    }

    public void checkVender(int a, int b, Object mUserInfo) {
        ZillionLog.i(TAG, "checkVender mUserInfo:" + a + "," + b);
        this.userInfo = mUserInfo;
        try {
            if (this.mVender.isOpened() || this.mVender.openPort()) {
                try {
                    this.mVender.addEventListener(this.listener);
                } catch (Exception e) {
                }
                this.mVender.setCheckA(a);
                this.mVender.setCheckB(b);
                this.mVender.setRequestMethod(6);
                this.mVender.setParams(SerialPort.BAUDRATE_9600, 8, 1, 0);
                sendPortData(this.mVender, MyFunc.cmdCheckVender(a, b), true);
                Log.d(TAG, MyFunc.cmdCheckVender(a, b));
            }
        } catch (SerialPortException e2) {
            e2.printStackTrace();
        }
    }

    public void openVender(int a, int b) {
        try {
            if (this.mVender.isOpened() || this.mVender.openPort()) {
                try {
                    this.mVender.addEventListener(this.listener);
                } catch (Exception e) {
                }
                this.mVender.setCheckA(a);
                this.mVender.setCheckB(b);
                this.mVender.setRequestMethod(3);
                this.mVender.setParams(SerialPort.BAUDRATE_9600, 8, 1, 0);
                String c = MyFunc.cmdOpenVender(a, b);
                sendPortData(this.mVender, MyFunc.cmdOpenVender(a, b), true);
            }
        } catch (SerialPortException e2) {
            e2.printStackTrace();
        }
    }

    public void checkVender(int a, int b) {
        try {
            if (this.mVender.isOpened() || this.mVender.openPort()) {
                try {
                    this.mVender.addEventListener(this.listener);
                } catch (Exception e) {
                }
                this.mVender.setCheckA(a);
                this.mVender.setCheckB(b);
                this.mVender.setRequestMethod(6);
                this.mVender.setParams(SerialPort.BAUDRATE_9600, 8, 1, 0);
                String c = MyFunc.cmdCheckVender(a, b);
                sendPortData(this.mVender, MyFunc.cmdCheckVender(a, b), true);
            }
        } catch (SerialPortException e2) {
            e2.printStackTrace();
        }
    }

    public void closeVender() throws SerialPortException {
        if (this.mVender.isOpened()) {
            this.mVender.closePort();
        }
    }

    private void sendPortData(SerialPort ComPort, String data, boolean isHex) throws SerialPortException {
        if (ComPort != null && ComPort.isOpened()) {
            if (isHex) {
                ComPort.writeBytes(MyFunc.HexToByteArr(data));
            } else {
                ComPort.writeString(data);
            }
        }
    }
}
