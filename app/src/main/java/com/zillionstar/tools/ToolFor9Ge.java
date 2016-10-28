package com.zillionstar.tools;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class ToolFor9Ge {
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / ((float) width);
        float scaleHeight = ((float) newHeight) / ((float) height);
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    public static boolean checkNetworkInfo(Context mContext) {
        ConnectivityManager conMan = (ConnectivityManager) mContext.getSystemService("connectivity");
        State mobile = conMan.getNetworkInfo(0).getState();
        State wifi = conMan.getNetworkInfo(1).getState();
        if (mobile == State.CONNECTED || mobile == State.CONNECTING) {
            return true;
        }
        if (wifi == State.CONNECTED || wifi == State.CONNECTING) {
            return true;
        }
        return false;
    }

    public static String getFileName(String pathandname) {
        int start = pathandname.lastIndexOf("/");
        int end = pathandname.lastIndexOf(".");
        if (start == -1 || end == -1) {
            return null;
        }
        return pathandname.substring(start + 1, end);
    }

    public static String getBase64FromPath(String path) {
        String base64 = "";
        try {
            File file = new File(path);
            byte[] buffer = new byte[(((int) file.length()) + 100)];
            base64 = Base64.encodeToString(buffer, 0, new FileInputStream(file).read(buffer), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return base64;
    }

    public static Bitmap getBitmapFromPath(String path, int w, int h) {
        Options opts = new Options();
        opts.inJustDecodeBounds = true;
        opts.inPreferredConfig = Config.ARGB_8888;
        BitmapFactory.decodeFile(path, opts);
        int width = opts.outWidth;
        int height = opts.outHeight;
        float scaleWidth = 0.0f;
        float scaleHeight = 0.0f;
        if (width > w || height > h) {
            scaleWidth = ((float) width) / ((float) w);
            scaleHeight = ((float) height) / ((float) h);
        }
        opts.inJustDecodeBounds = false;
        opts.inSampleSize = (int) Math.max(scaleWidth, scaleHeight);
        return Bitmap.createScaledBitmap((Bitmap) new WeakReference(BitmapFactory.decodeFile(path, opts)).get(), w, h, true);
    }

    public static String getBase64FromBitmap(Bitmap bitmap, int bitmapQuality) {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, bitmapQuality, bStream);
        return Base64.encodeToString(bStream.toByteArray(), 0);
    }

    public static Bitmap getBitmapFromBase64(String string) {
        byte[] bitmapArray = null;
        try {
            bitmapArray = Base64.decode(string, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.lang.String convertStreamToString(java.io.InputStream r6) {
        /*
        r2 = new java.io.BufferedReader;
        r4 = new java.io.InputStreamReader;
        r4.<init>(r6);
        r2.<init>(r4);
        r3 = new java.lang.StringBuilder;
        r3.<init>();
        r1 = 0;
    L_0x0010:
        r1 = r2.readLine();	 Catch:{ IOException -> 0x0035 }
        if (r1 != 0) goto L_0x001e;
    L_0x0016:
        r6.close();	 Catch:{ IOException -> 0x004c }
    L_0x0019:
        r4 = r3.toString();
        return r4;
    L_0x001e:
        r4 = new java.lang.StringBuilder;	 Catch:{ IOException -> 0x0035 }
        r5 = java.lang.String.valueOf(r1);	 Catch:{ IOException -> 0x0035 }
        r4.<init>(r5);	 Catch:{ IOException -> 0x0035 }
        r5 = "/n";
        r4 = r4.append(r5);	 Catch:{ IOException -> 0x0035 }
        r4 = r4.toString();	 Catch:{ IOException -> 0x0035 }
        r3.append(r4);	 Catch:{ IOException -> 0x0035 }
        goto L_0x0010;
    L_0x0035:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ all -> 0x0042 }
        r6.close();	 Catch:{ IOException -> 0x003d }
        goto L_0x0019;
    L_0x003d:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0019;
    L_0x0042:
        r4 = move-exception;
        r6.close();	 Catch:{ IOException -> 0x0047 }
    L_0x0046:
        throw r4;
    L_0x0047:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0046;
    L_0x004c:
        r0 = move-exception;
        r0.printStackTrace();
        goto L_0x0019;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zillionstar.tools.ToolFor9Ge.convertStreamToString(java.io.InputStream):java.lang.String");
    }

    public static void changeFonts(ViewGroup root, String path, Activity act) {
        Typeface tf = Typeface.createFromAsset(act.getAssets(), path);
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTypeface(tf);
            } else if (v instanceof Button) {
                ((Button) v).setTypeface(tf);
            } else if (v instanceof EditText) {
                ((EditText) v).setTypeface(tf);
            } else if (v instanceof ViewGroup) {
                changeFonts((ViewGroup) v, path, act);
            }
        }
    }

    public static void changeTextSize(ViewGroup root, int size, Activity act) {
        for (int i = 0; i < root.getChildCount(); i++) {
            View v = root.getChildAt(i);
            if (v instanceof TextView) {
                ((TextView) v).setTextSize((float) size);
            } else if (v instanceof Button) {
                ((Button) v).setTextSize((float) size);
            } else if (v instanceof EditText) {
                ((EditText) v).setTextSize((float) size);
            } else if (v instanceof ViewGroup) {
                changeTextSize((ViewGroup) v, size, act);
            }
        }
    }

    public static void changeWH(View v, int W, int H) {
        LayoutParams params = v.getLayoutParams();
        params.width = W;
        params.height = H;
        v.setLayoutParams(params);
    }

    public static void changeH(View v, int H) {
        LayoutParams params = v.getLayoutParams();
        params.height = H;
        v.setLayoutParams(params);
    }
}
