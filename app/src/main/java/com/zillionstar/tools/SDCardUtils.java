package com.zillionstar.tools;

import android.os.Environment;
import android.os.StatFs;
import java.io.File;

public class SDCardUtils {
    private SDCardUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals("mounted");
    }

    public static String getSDCardPath() {
        return new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append(File.separator).toString();
    }

    public static long getSDCardAllSize() {
        if (!isSDCardEnable()) {
            return 0;
        }
        StatFs stat = new StatFs(getSDCardPath());
        return ((long) stat.getAvailableBlocks()) * (((long) stat.getAvailableBlocks()) - 4);
    }

    public static long getFreeBytes(String filePath) {
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        return ((long) stat.getBlockSize()) * (((long) stat.getAvailableBlocks()) - 4);
    }

    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }
}
