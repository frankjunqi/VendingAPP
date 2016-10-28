package com.zillionstar.tools;

import android.util.Log;
import de.mindpipe.android.logging.log4j.LogConfigurator;
import java.io.File;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class ZillionLog {
    private static Logger gLogger;
    private static ZillionLog instance = new ZillionLog();

    public void configLog() {
        Log.i("ZillionLog", "configLog");
        LogConfigurator logConfigurator = new LogConfigurator();
        String MYLOG_PATH_SDCARD_DIR = "/mnt/sdcard1/com.zillionstar/log/vending.log";
        if (!new File("/mnt/sdcard1/").exists() || new File("/mnt/sdcard1/").list() == null || new File("/mnt/sdcard1/").list().length <= 0) {
            MYLOG_PATH_SDCARD_DIR = "/data/data/com.mc.vending/logs/vending.log";
        }
        logConfigurator.setFileName(MYLOG_PATH_SDCARD_DIR);
        logConfigurator.setFilePattern("%d::%t::%c::%m%n");
        logConfigurator.setRootLevel(Level.DEBUG);
        logConfigurator.setMaxFileSize(10485760);
        logConfigurator.configure();
    }

    public static void d(String msg) {
        gLogger = Logger.getLogger("");
        gLogger.debug(msg);
    }

    public static void i(Object msg) {
        gLogger = Logger.getLogger("");
        gLogger.info(msg);
    }

    public static void e(Object msg) {
        gLogger = Logger.getLogger("");
        gLogger.error(msg);
    }

    public static void i(String tag, Object msg) {
        gLogger = Logger.getLogger(tag);
        gLogger.info(msg);
    }

    public static void e(String tag, Object msg) {
        gLogger = Logger.getLogger(tag);
        gLogger.error(msg);
    }

    private ZillionLog() {
        configLog();
    }

    public static ZillionLog getInstance() {
        return instance;
    }
}
