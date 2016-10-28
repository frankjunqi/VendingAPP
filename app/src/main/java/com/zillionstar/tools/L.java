package com.zillionstar.tools;

import android.util.Log;

public class L {
    private static final String CLASS_METHOD_LINE_FORMAT = "%s.%s";
    public static String TAG = "way";
    public static final int levelD = 1;
    public static final int levelE = 3;
    public static final int levelI = 2;
    public static final int levelV = 0;
    public static int logLevel = 1;

    private L() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void i(String msg) {
        StackTraceElement traceElement = Thread.currentThread().getStackTrace()[3];
        TAG = String.format(CLASS_METHOD_LINE_FORMAT, new Object[]{traceElement.getClassName(), traceElement.getMethodName(), Integer.valueOf(traceElement.getLineNumber()), traceElement.getFileName()});
        if (logLevel <= 2) {
            Log.i(TAG, msg);
        }
    }

    public static void d(String msg) {
        StackTraceElement traceElement = Thread.currentThread().getStackTrace()[3];
        TAG = String.format(CLASS_METHOD_LINE_FORMAT, new Object[]{traceElement.getClassName(), traceElement.getMethodName(), Integer.valueOf(traceElement.getLineNumber()), traceElement.getFileName()});
        if (logLevel <= 1) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        StackTraceElement traceElement = Thread.currentThread().getStackTrace()[3];
        TAG = String.format(CLASS_METHOD_LINE_FORMAT, new Object[]{traceElement.getClassName(), traceElement.getMethodName(), Integer.valueOf(traceElement.getLineNumber()), traceElement.getFileName()});
        if (logLevel <= 3) {
            Log.e(TAG, msg);
        }
    }

    public static void v(String msg) {
        StackTraceElement traceElement = Thread.currentThread().getStackTrace()[3];
        TAG = String.format(CLASS_METHOD_LINE_FORMAT, new Object[]{traceElement.getClassName(), traceElement.getMethodName(), Integer.valueOf(traceElement.getLineNumber()), traceElement.getFileName()});
        if (logLevel <= 0) {
            Log.v(TAG, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (logLevel <= 2) {
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (logLevel <= 1) {
            Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (logLevel <= 3) {
            Log.i(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (logLevel <= 0) {
            Log.i(tag, msg);
        }
    }
}
