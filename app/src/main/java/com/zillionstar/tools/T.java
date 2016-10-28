package com.zillionstar.tools;

import android.content.Context;
import android.widget.Toast;

public class T {
    public static boolean isShow = true;

    private T() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void showShort(Context context, CharSequence message) {
        if (isShow) {
            Toast.makeText(context, message, 0).show();
        }
    }

    public static void showShort(Context context, int message) {
        if (isShow) {
            Toast.makeText(context, message, 0).show();
        }
    }

    public static void showLong(Context context, CharSequence message) {
        if (isShow) {
            Toast.makeText(context, message, 1).show();
        }
    }

    public static void showLong(Context context, int message) {
        if (isShow) {
            Toast.makeText(context, message, 1).show();
        }
    }

    public static void show(Context context, CharSequence message, int duration) {
        if (isShow) {
            Toast.makeText(context, message, duration).show();
        }
    }

    public static void show(Context context, int message, int duration) {
        if (isShow) {
            Toast.makeText(context, message, duration).show();
        }
    }
}
