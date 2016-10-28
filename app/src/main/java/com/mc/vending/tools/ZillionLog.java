package com.mc.vending.tools;

import android.content.Context;
import android.util.Log;
import com.mc.vending.application.CustomApplication;
import com.mc.vending.config.Constant;
import com.mc.vending.tools.utils.DES;
import de.mindpipe.android.logging.log4j.LogConfigurator;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class ZillionLog {
    private static String configUrl;
    private static Logger gLogger;
    private static ZillionLog instance = new ZillionLog();
    private static int logSize = 500;
    private static String vendingCode;

    static class AnonymousClass1 implements Runnable {
        private final /* synthetic */ String val$errorMsg;

        AnonymousClass1(String str) {
            this.val$errorMsg = str;
        }

        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("optype", Constant.HTTP_OPERATE_TYPE_INSERT);
                param.put("tbname", "Value");
                JSONArray rows = new JSONArray();
                JSONObject aa = new JSONObject();
                aa.put("IF2_VD1_ID", ZillionLog.vendingCode);
                aa.put("IF2_WSID", "");
                aa.put("IF2_ErrorMessage", this.val$errorMsg);
                rows.put(aa);
                param.put("rows", rows);
                List<BasicNameValuePair> postData = new ArrayList();
                JSONObject json = new JSONObject();
                json.put(Constant.BODY_KEY_METHOD, Constant.METHOD_WSID_VENDINGRUNERROR);
                json.put(Constant.BODY_KEY_UDID, Constant.BODY_VALUE_UDID);
                json.put(Constant.BODY_KEY_APP, Constant.BODY_VALUE_APP);
                json.put(Constant.BODY_KEY_USER, "");
                json.put("pwd", DES.getEncrypt());
                if (param != null) {
                    JSONArray array = new JSONArray();
                    array.put(param);
                    json.put("data", array);
                }
                postData.add(new BasicNameValuePair("data", json.toString()));
                System.setProperty("http.keepAlive", "false");
                ZillionLog.i("zillionlog configUrl", ZillionLog.configUrl);
                HttpPost httpPost = new HttpPost(ZillionLog.configUrl);
                httpPost.setHeader(Constant.HEADER_KEY_CONTENT_TYPE, (String) HttpHelper.getHeaderMap().get(Constant.HEADER_KEY_CONTENT_TYPE));
                httpPost.setHeader(Constant.HEADER_KEY_CLIENTVER, (String) HttpHelper.getHeaderMap().get(Constant.HEADER_KEY_CLIENTVER));
                BasicHttpParams httpParams = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
                HttpConnectionParams.setSoTimeout(httpParams, 30000);
                HttpClient httpClient = new DefaultHttpClient(httpParams);
                HttpResponse response = null;
                httpPost.setEntity(new UrlEncodedFormEntity(postData, "UTF-8"));
                try {
                    response = httpClient.execute(httpPost);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (response.getStatusLine().getStatusCode() != 200) {
                    httpPost.abort();
                }
            } catch (Exception e2) {
                ZillionLog.i("Exception e", e2.getMessage());
            }
        }
    }

    public void configLog() {
        Log.i("ZillionLog", "configLog");
        LogConfigurator logConfigurator = new LogConfigurator();
        String MYLOG_PATH_SDCARD_DIR = "/mnt/sdcard1/com.zillionstar/log/vending.log";
        if (!new File("/mnt/sdcard1/").exists() || new File("/mnt/sdcard1/").list() == null || new File("/mnt/sdcard1/").list().length <= 0) {
            MYLOG_PATH_SDCARD_DIR = "/storage/sdcard0/Download/vendinglog/vending.txt";
        }
        logConfigurator.setFileName(MYLOG_PATH_SDCARD_DIR);
        logConfigurator.setFilePattern("%d::%t::%c::%m%n");
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setMaxFileSize(10485760);
        logConfigurator.configure();
        Context context = CustomApplication.getContext();
        configUrl = context.getSharedPreferences(Constant.SHARED_CONFIG, 0).getString(Constant.SHARED_CONFIG_URL, "");
        vendingCode = context.getSharedPreferences(Constant.SHARED_VEND_CODE_KEY, 0).getString(Constant.SHARED_VEND_CODE, "");
    }

    public static void d(String msg) {
        gLogger = Logger.getLogger("");
        gLogger.debug(msg);
    }

    public static void i(Object msg) {
        gLogger = Logger.getLogger("");
        gLogger.info(msg);
        sendLog((String) msg, 2);
    }

    public static void i(String tag, Object msg) {
        gLogger = Logger.getLogger(tag);
        gLogger.info(msg);
        sendLog(new StringBuilder(String.valueOf(tag)).append(":").append(msg).toString(), 2);
    }

    private static void sendLog(String eString, int level) {
        String vendingDebugStatus = CustomApplication.getContext().getSharedPreferences(Constant.SHARED_VEND_CODE_KEY, 0).getString(Constant.SHARED_VEND_DEBUG_STATUS, "");
        if (vendingDebugStatus != null && !vendingDebugStatus.equals("") && Integer.valueOf(vendingDebugStatus).intValue() >= level) {
            if (eString.length() > logSize) {
                eString = eString.substring(0, logSize);
            }
            requestToParse(eString);
        }
    }

    public static void e(Object msg) {
        gLogger = Logger.getLogger("");
        gLogger.error(msg);
        sendLog((String) msg, 1);
    }

    public static void e(String tag, Object msg) {
        gLogger = Logger.getLogger("E:" + tag);
        gLogger.error(msg);
        sendLog(new StringBuilder(String.valueOf(tag)).append(":").append(msg).toString(), 1);
    }

    public static void e(String tag, Object msg, Throwable e) {
        gLogger = Logger.getLogger("E:" + tag);
        gLogger.error(msg, e);
        sendLog(new StringBuilder(String.valueOf(tag)).append(":").append(msg).append(Tools.getStackTrace(e)).toString(), 1);
    }

    private ZillionLog() {
        configLog();
    }

    public static ZillionLog getInstance() {
        return instance;
    }

    private static void requestToParse(String errorMsg) {
        new Thread(new AnonymousClass1(errorMsg)).start();
    }
}
