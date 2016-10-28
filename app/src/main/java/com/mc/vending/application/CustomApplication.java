package com.mc.vending.application;

import android.app.Application;
import android.content.Intent;
import com.mc.vending.activitys.MainActivity;
import com.mc.vending.tools.ZillionLog;
import java.lang.Thread.UncaughtExceptionHandler;

public class CustomApplication extends Application implements UncaughtExceptionHandler {
    private static CustomApplication instance;

    public static CustomApplication getContext() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        instance = this;
        //ZillionLog.i("CustomApplication", "onCreate");
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void uncaughtException(Thread thread, Throwable ex) {
        ZillionLog.e(getClass().getName(), ex.getMessage(), ex);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(335544320);
        startActivity(intent);
    }
}
