package com.mc.vending.tools;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build.VERSION;
import android.os.Environment;
import android.os.StatFs;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mc.vending.R;
import com.mc.vending.application.CustomApplication;
import com.mc.vending.config.Constant;
import com.mc.vending.config.MC_Config;
import com.mc.vending.data.StockTransactionData;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Random;

public class Tools {
    public static final double EARTH_RADIUS = 6378137.0d;
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    public int responseValue;

    static class AnonymousClass1 implements Runnable {
        private final /* synthetic */ String val$filePath;

        AnonymousClass1(String str) {
            this.val$filePath = str;
        }

        public void run() {
            File display = new File(this.val$filePath);
            if (display.exists()) {
                File[] items = display.listFiles();
                int i = display.listFiles().length;
                for (int j = 0; j < i; j++) {
                    if (items[j].isFile()) {
                        items[j].delete();
                    }
                }
            }
        }
    }

    public static String removeLine(String val) {
        if (val.startsWith("{&&}")) {
            val = removeLine(val.substring(4).trim());
        }
        if (val.endsWith("{&&}")) {
            val = removeLine(val.substring(0, val.length() - 4).trim());
        }
        if (val.contains("{&&}{&&}")) {
            val = removeLine(val.replace("{&&}{&&}", "{&&}"));
        }
        return val.replace("{&&}", "<br />");
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Config config;
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (drawable.getOpacity() != PixelFormat.OPAQUE) {
            config = Config.ARGB_8888;
        } else {
            config = Config.RGB_565;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        matrix.postScale(((float) w) / ((float) width), ((float) h) / ((float) height));
        return new BitmapDrawable(Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true));
    }

    public static Bitmap zoomDrawableToBitmap(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        matrix.postScale(((float) w) / ((float) width), ((float) h) / ((float) height));
        return Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
    }

    public static boolean hasTelphoneMode(Context activityContext) {
        if (((TelephonyManager) activityContext.getSystemService(Context.TELEPHONY_SERVICE)).getPhoneType() == 0) {
            return false;
        }
        return true;
    }

    public static String nullToStr(String str) {
        if (isNull(str).booleanValue()) {
            return "";
        }
        return str;
    }

    public static String getSDKVersion(Context context) {
        String ver = "";
        String verInt = VERSION.SDK;
        if ("2".equals(verInt)) {
            return "1.1";
        }
        if ("3".equals(verInt)) {
            return "1.5";
        }
        if (StockTransactionData.BILL_TYPE_GET.equals(verInt)) {
            return "1.6";
        }
        if (StockTransactionData.BILL_TYPE_RETURN.equals(verInt)) {
            return "2.0";
        }
        if (StockTransactionData.BILL_TYPE_BORROW.equals(verInt)) {
            return "2.0.1";
        }
        if (StockTransactionData.BILL_TYPE_All.equals(verInt)) {
            return "2.1";
        }
        if (StockTransactionData.BILL_TYPE_DIFFAll.equals(verInt)) {
            return "2.2";
        }
        if ("9".equals(verInt)) {
            return "2.3.1";
        }
        if (MC_Config.PAGE_SIZE.equals(verInt)) {
            return "2.3.3";
        }
        if ("11".equals(verInt)) {
            return "3.0";
        }
        if ("12".equals(verInt)) {
            return "3.1";
        }
        if ("13".equals(verInt)) {
            return "3.2";
        }
        if ("14".equals(verInt)) {
            return "4.0";
        }
        if ("15".equals(verInt)) {
            return "4.0.3";
        }
        if ("16".equals(verInt)) {
            return "4.1.2";
        }
        if ("17".equals(verInt)) {
            return "4.2.2";
        }
        return "other";
    }

    public static String getCarrier(Context context) {
        String imsi = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        if (imsi == null || "".equals(imsi)) {
            return null;
        }
        if (imsi.startsWith("46000") || imsi.startsWith("46002")) {
            return "ChinaMobile";
        }
        if (imsi.startsWith("46001")) {
            return "ChinaUnicom";
        }
        if (imsi.startsWith("46003")) {
            return "ChinaTelecom";
        }
        return "othes";
    }

    public static String readTelephoneSerialNum(Context con) {
        return ((TelephonyManager) con.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    public static String md5(String source) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.update(source.getBytes());
            source = toHexString(digest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return source;
    }

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 240) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 15]);
        }
        return sb.toString();
    }

    public static String getFileNameFromURL(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static Bitmap getBitmapFromUrl(String urlStr) {
        try {
            URLConnection connection = new URL(urlStr).openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            Bitmap bitmap = BitmapFactory.decodeStream(bis);
            bis.close();
            inputStream.close();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        return null;
    }

    public static void saveImageToSDAndChangePostfix(Bitmap bitmap, String path, String fileName) {
        FileNotFoundException e;
        IOException e2;
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        BufferedOutputStream bos = null;
        try {
            BufferedOutputStream bos2 = new BufferedOutputStream(new FileOutputStream(new File(new StringBuilder(String.valueOf(path)).append(fileName.substring(0, fileName.lastIndexOf("."))).append(".dat").toString())));
            if (bitmap != null) {
                try {
                    bitmap.compress(CompressFormat.PNG, 100, bos2);
                } catch (Throwable th3) {
                    bos = bos2;
                    if (bos != null) {
                        bos.flush();
                        bos.close();
                    }
                }
            }
            if (bos2 != null) {
                try {
                    bos2.flush();
                    bos2.close();
                    bos = bos2;
                    return;
                } catch (IOException e22222) {
                    e22222.printStackTrace();
                }
            }
            bos = bos2;
        } catch (FileNotFoundException e5) {
            e = e5;
            e.printStackTrace();
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        } catch (IOException e6) {
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }
    }

    public static Drawable saveImageToSD(Bitmap bitmap, String path, String fileName) {
        FileNotFoundException e;
        IOException e2;
        int i;
        Throwable th;
        int i2 = 1;
        File dir = new File(path);
        BufferedOutputStream bos = null;
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            BufferedOutputStream bos2 = new BufferedOutputStream(new FileOutputStream(new File(new StringBuilder(String.valueOf(path)).append("/").append(fileName).toString())));
            if (bitmap != null) {
                try {
                    bitmap.compress(CompressFormat.PNG, 100, bos2);
                } catch (Throwable th3) {
                    th = th3;
                    bos = bos2;
                    if (bos != null) {
                        bos.flush();
                        bos.close();
                    }
                }
            }
            if (bos2 != null) {
                try {
                    bos2.flush();
                    bos2.close();
                    bos = bos2;
                } catch (IOException e22222) {
                    e22222.printStackTrace();
                }
                if (bitmap.getWidth() <= 1280) {
                    i = 1;
                } else {
                    i = 0;
                }
                if (bitmap.getHeight() <= 1280) {
                    i2 = 0;
                }
                if ((i | i2) == 0) {
                    return changeImageSize(path, fileName, bitmap);
                }
                return new BitmapDrawable(bitmap);
            }
            bos = bos2;
        } catch (FileNotFoundException e5) {
            e = e5;
            e.printStackTrace();
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
            if (bitmap.getWidth() <= 1280) {
                i = 1;
            } else {
                i = 0;
            }
            if (bitmap.getHeight() <= 1280) {
                i2 = 0;
            }
            if ((i | i2) == 0) {
                return changeImageSize(path, fileName, bitmap);
            }
            return new BitmapDrawable(bitmap);
        } catch (IOException e6) {
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
            if (bitmap.getWidth() <= 1280) {
                i = 0;
            } else {
                i = 1;
            }
            if (bitmap.getHeight() <= 1280) {
                i2 = 0;
            }
            if ((i | i2) == 0) {
                return new BitmapDrawable(bitmap);
            }
            return changeImageSize(path, fileName, bitmap);
        }
        if (bitmap.getWidth() <= 1280) {
            i = 0;
        } else {
            i = 1;
        }
        if (bitmap.getHeight() <= 1280) {
            i2 = 0;
        }
        if ((i | i2) == 0) {
            return new BitmapDrawable(bitmap);
        }
        return changeImageSize(path, fileName, bitmap);
    }

    public static Drawable changeImageSize(String path, String fileName, Bitmap bitmap) {
        FileNotFoundException e;
        IOException e2;
        Bitmap bits;
        Throwable th;
        float x = 1280.0f / ((float) bitmap.getWidth());
        float y = 1280.0f / ((float) bitmap.getHeight());
        float bit = x;
        if (x > y) {
            bit = y;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(bit, bit);
        Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        BufferedOutputStream bos = null;
        File imageFile = new File(new StringBuilder(String.valueOf(path)).append("/aa_").append(fileName).toString());
        if (imageFile.exists()) {
            imageFile.delete();
        }
        try {
            File imageFile2 = new File(new StringBuilder(String.valueOf(path)).append("/").append(fileName).toString());
            try {
                BufferedOutputStream bos2 = new BufferedOutputStream(new FileOutputStream(imageFile2));
                if (resizeBmp != null) {
                    try {
                        resizeBmp.compress(CompressFormat.PNG, 100, bos2);
                    } catch (Throwable th3) {
                        th = th3;
                        imageFile = imageFile2;
                        bos = bos2;
                        if (bos != null) {
                            bos.flush();
                            bos.close();
                        }
                    }
                }
                if (bos2 != null) {
                    try {
                        bos2.flush();
                        bos2.close();
                        imageFile = imageFile2;
                        bos = bos2;
                    } catch (IOException e22222) {
                        e22222.printStackTrace();
                    }
                    bits = null;
                    bits = BitmapFactory.decodeStream(new FileInputStream(imageFile));
                    return new BitmapDrawable(bits);
                }
                imageFile = imageFile2;
                bos = bos2;
            } catch (FileNotFoundException e5) {
                e = e5;
                imageFile = imageFile2;
                e.printStackTrace();
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
                bits = null;
                bits = BitmapFactory.decodeStream(new FileInputStream(imageFile));
                return new BitmapDrawable(bits);
            } catch (IOException e6) {
                imageFile = imageFile2;
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
                bits = null;
                bits = BitmapFactory.decodeStream(new FileInputStream(imageFile));
                return new BitmapDrawable(bits);
            } catch (Throwable th4) {
                th = th4;
                imageFile = imageFile2;
                if (bos != null) {
                    bos.flush();
                    bos.close();
                }
            }
        } catch (FileNotFoundException e7) {
            e = e7;
            e.printStackTrace();
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
            bits = null;
            try {
                bits = BitmapFactory.decodeStream(new FileInputStream(imageFile));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            return new BitmapDrawable(bits);
        } catch (IOException e8) {
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
            bits = null;
            try {
                bits = BitmapFactory.decodeStream(new FileInputStream(imageFile));
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            }
            return new BitmapDrawable(bits);
        }
        bits = null;
        try {
            bits = BitmapFactory.decodeStream(new FileInputStream(imageFile));
        } catch (Exception e9) {
            e9.printStackTrace();
        }
        return new BitmapDrawable(bits);
    }

    public static void saveImageToSDThum(InputStream is, String path, String fileName) {
        File dir = new File(path);
        BufferedOutputStream bos = null;
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String srcPath = new StringBuilder(String.valueOf(path)).append("/").append(fileName).toString();
        try {
            byte[] bs = new byte[1024];
            OutputStream os = new FileOutputStream(new StringBuilder(String.valueOf(path)).append("/").append(fileName).toString());
            while (true) {
                int len = is.read(bs);
                if (len == -1) {
                    break;
                }
                os.write(bs, 0, len);
            }
            os.close();
            is.close();
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        } catch (IOException e32) {
            e32.printStackTrace();
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e322) {
                    e322.printStackTrace();
                }
            }
        } catch (Throwable th) {
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e3222) {
                    e3222.printStackTrace();
                }
            }
        }
        Options newOpts = new Options();
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        int be = 1;
        if (w > h && ((float) w) > 250.0f) {
            be = (int) (((float) newOpts.outWidth) / 250.0f);
        } else if (w < h && ((float) h) > 250.0f) {
            be = (int) (((float) newOpts.outHeight) / 250.0f);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be;
        saveBitmap(BitmapFactory.decodeFile(srcPath, newOpts), new StringBuilder(String.valueOf(path)).append("/thumb").toString(), fileName);
    }

    public static void saveBitmap(Bitmap argbitmap, String path, String fileName) {
        FileNotFoundException e;
        IOException e2;
        Throwable th;
        File dir = new File(path);
        BufferedOutputStream bos = null;
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            BufferedOutputStream bos2 = new BufferedOutputStream(new FileOutputStream(new File(new StringBuilder(String.valueOf(path)).append("/").append(fileName).toString())));
            if (argbitmap != null) {
                try {
                    argbitmap.compress(CompressFormat.PNG, 20, bos2);
                } catch (Throwable th3) {
                    th = th3;
                    bos = bos2;
                    if (bos != null) {
                        bos.flush();
                        bos.close();
                    }
                }
            }
            if (bos2 != null) {
                try {
                    bos2.flush();
                    bos2.close();
                    bos = bos2;
                    return;
                } catch (IOException e22222) {
                    e22222.printStackTrace();
                }
            }
            bos = bos2;
        } catch (FileNotFoundException e5) {
            e = e5;
            e.printStackTrace();
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        } catch (IOException e6) {
            if (bos != null) {
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public static String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static String fileNameToMd5(String url) {
        return md5(url);
    }

    public static File getMapFile(String fileName, Context context) {
        String path = context.getExternalFilesDir(null) + File.separator + fileName;
        File tileFile = new File(path);
        if (!tileFile.exists()) {
            try {
                InputStream is = context.getAssets().open(fileName);
                if (is != null) {
                    FileOutputStream fo = new FileOutputStream(path);
                    byte[] b = new byte[1024];
                    while (true) {
                        int length = is.read(b);
                        if (length == -1) {
                            break;
                        }
                        fo.write(b, 0, length);
                    }
                    fo.flush();
                    fo.close();
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tileFile;
    }

    public static String getSourceIdFromAsset(Context context) {
        try {
            InputStream inputStream = context.getAssets().open("sourid.txt");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            while (true) {
                int len = inputStream.read(buf);
                if (len == -1) {
                    baos.flush();
                    baos.close();
                    inputStream.close();
                    return baos.toString();
                }
                baos.write(buf, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isAccessNetwork(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity.getActiveNetworkInfo() == null || !connectivity.getActiveNetworkInfo().isAvailable()) {
            return false;
        }
        return true;
    }

    public static String getAccessNetworkType(Context context) {
        NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null) {
            return null;
        }
        int type = info.getType();
        if (type == 1) {
            return "wifi";
        }
        if (type == 0) {
            return "3G/GPRS";
        }
        return null;
    }

    public static long getSDSize() {
        if (!"mounted".equals(Environment.getExternalStorageState())) {
            return -1;
        }
        StatFs statFs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        return ((long) statFs.getAvailableBlocks()) * ((long) statFs.getBlockSize());
    }

    public static String getNowTime() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
    }

    public static String getNowTimeStr() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    }

    public static String getSendTimeStr(String date) {
        String cd = getNowTimeStr().substring(0, 10);
        String dd = date.substring(0, 10);
        if (!cd.equals(dd)) {
            return dd;
        }
        try {
            return new SimpleDateFormat("HH:mm").format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getNowDateStr() {
        return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    public static Date stringToDate(String date) {
        Date createDate = null;
        try {
            createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (Exception e) {
        }
        return createDate;
    }

    public static String getNowTimeStr(int num) {
        return new SimpleDateFormat("dd-MM-yyyy").format(new Date(new Date().getTime() + ((long) (86400000 * num))));
    }

    public static String getNowTimeStrPad(int num) {
        return new SimpleDateFormat("dd.MM").format(new Date(new Date().getTime() + ((long) (86400000 * num))));
    }

    public static void deleteAllFile(String filePath) {
        new Thread(new AnonymousClass1(filePath)).start();
    }

    public static void deleteAllFiles(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            for (File path : file.listFiles()) {
                deleteAllFiles(path.getPath());
            }
            return;
        }
        file.delete();
    }

    public void setListViewHeightBasedOnChildren2(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {
            int totalHeight = 0;
            int len = listAdapter.getCount();
            for (int i = 0; i < len; i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight() + 10;
            }
            LayoutParams params = listView.getLayoutParams();
            params.height = (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + totalHeight;
            listView.setLayoutParams(params);
        }
    }

    public static String getLocalIpAddress() {
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            while (en.hasMoreElements()) {
                Enumeration<InetAddress> enumIpAddr = ((NetworkInterface) en.nextElement()).getInetAddresses();
                while (enumIpAddr.hasMoreElements()) {
                    InetAddress inetAddress = (InetAddress) enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    public static String getNetType(Context context) {
        return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo().getTypeName();
    }

    public static String getScreenSize(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels + "x" + metrics.heightPixels;
    }

    public static String getLocalMacAddress(Context context) {
        return ((WifiManager) ((Activity) context).getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
    }

    public static String getLanguageCode() {
        String languageCoade = "en-GB";
        languageCoade = Locale.getDefault().getLanguage();
        if ("zh".equals(languageCoade)) {
            return "zh-CN";
        }
        if ("en".equals(languageCoade)) {
            return "en-GB";
        }
        if ("fr".equals(languageCoade)) {
            return "fr-FR";
        }
        if ("it".equals(languageCoade)) {
            return "it-IT";
        }
        if ("de".equals(languageCoade)) {
            return "de-DE";
        }
        return languageCoade;
    }

    public static boolean isNum(String str) {
        return str.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$");
    }

    public static double getDistance(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = (3.141592653589793d * lat_a) / 180.0d;
        double radLat2 = (3.141592653589793d * lat_b) / 180.0d;
        return (double) (Math.round(10000.0d * ((2.0d * Math.asin(Math.sqrt(Math.pow(Math.sin((radLat1 - radLat2) / 2.0d), 2.0d) + ((Math.cos(radLat1) * Math.cos(radLat2)) * Math.pow(Math.sin((((lng_a - lng_b) * 3.141592653589793d) / 180.0d) / 2.0d), 2.0d))))) * EARTH_RADIUS)) / 10000);
    }

    public static String distance2Str(double distance) {
        DecimalFormat df = new DecimalFormat("0.00");
        if (distance >= 1000.0d) {
            return df.format(distance / 1000.0d) + " Km";
        }
        return df.format(distance) + " m";
    }

    public static Boolean isNull(String inputStr) {
        boolean z = inputStr == null || "".equals(inputStr.trim());
        return Boolean.valueOf(z);
    }

    public static Date String2Date(String inputStr) {
        if (isNull(inputStr).booleanValue()) {
            return new Date();
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(inputStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float roundPx = (float) pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Drawable Bitmap2Drawable(Context context, Bitmap bitmap) {
        if (bitmap != null) {
            return new BitmapDrawable(context.getResources(), bitmap);
        }
        return null;
    }

    public static Bitmap Bytes2Bitmap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        return null;
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Typeface getFonts(Context ac) {
        return Typeface.createFromAsset(ac.getAssets(), "fonts/Avenir.ttc");
    }

    public static void openCalendar(Activity activity, String title, String content) {
        ComponentName cn;
        ComponentName componentName;
        Exception e;
        Intent i = new Intent();
        try {
            cn = new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity");
            try {
                i.setComponent(cn);
                activity.startActivityForResult(i, 0);
                componentName = cn;
            } catch (Exception e2) {
                e = e2;
                e.printStackTrace();
                try {
                    try {
                        i.setComponent(new ComponentName("com.android.calendar", "com.android.calendar.LaunchActivity"));
                        activity.startActivityForResult(i, 0);
                    } catch (Exception e3) {
                        e = e3;
                        e.printStackTrace();
                    }
                } catch (Exception e4) {
                    e = e4;
                    componentName = cn;
                    e.printStackTrace();
                }
            }
        } catch (Exception e5) {
            e = e5;
            cn = null;
            e.printStackTrace();
            i.setComponent(new ComponentName("com.android.calendar", "com.android.calendar.LaunchActivity"));
            activity.startActivityForResult(i, 0);
        }
    }

    public static int getRandomStr(int max) {
        return new Random().nextInt(max) % (max + 1);
    }

    public static Object copy(Object oldObj) {
        Object obj = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(oldObj);
            out.flush();
            out.close();
            obj = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray())).readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }

    public static String getStackTrace(Throwable aThrowable) {
        Writer result = new StringWriter();
        aThrowable.printStackTrace(new PrintWriter(result));
        return result.toString();
    }

    public static String getCustomStackTrace(Throwable aThrowable) {
        StringBuilder result = new StringBuilder("BOO-BOO: ");
        result.append(aThrowable.toString());
        String NEW_LINE = System.getProperty("line.separator");
        result.append(NEW_LINE);
        for (StackTraceElement element : aThrowable.getStackTrace()) {
            result.append(element);
            result.append(NEW_LINE);
        }
        return result.toString();
    }

    public static String getVendCode() {
        return CustomApplication.getContext().getSharedPreferences(Constant.SHARED_VEND_CODE_KEY, 0).getString(Constant.SHARED_VEND_CODE, "");
    }

    public static String getConfigUrl() {
        return CustomApplication.getContext().getSharedPreferences(Constant.SHARED_CONFIG, 0).getString(Constant.SHARED_CONFIG_URL, "");
    }
}
