package com.zillion.evm.jssc;

import android.util.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class SerialNativeInterface {
    public static final long ERR_INCORRECT_SERIAL_PORT = -4;
    public static final long ERR_PERMISSION_DENIED = -3;
    public static final long ERR_PORT_BUSY = -1;
    public static final long ERR_PORT_NOT_FOUND = -2;
    public static final int OS_LINUX = 0;
    public static final int OS_MAC_OS_X = 3;
    public static final int OS_SOLARIS = 2;
    public static final int OS_WINDOWS = 1;
    public static final String PROPERTY_JSSC_IGNPAR = "JSSC_IGNPAR";
    public static final String PROPERTY_JSSC_NO_TIOCEXCL = "JSSC_NO_TIOCEXCL";
    public static final String PROPERTY_JSSC_PARMRK = "JSSC_PARMRK";
    private static final String libMinorSuffix = "0";
    private static final String libVersion = "2.8";
    private static int osType = -1;

    public static native String getNativeLibraryVersion();

    public native boolean closePort(long j);

    public native int[] getBuffersBytesCount(long j);

    public native int getEventsMask(long j);

    public native int getFlowControlMode(long j);

    public native int[] getLinesStatus(long j);

    public native String[] getSerialPortNames();

    public native long openPort(String str, boolean z);

    public native boolean purgePort(long j, int i);

    public native byte[] readBytes(long j, int i);

    public native boolean sendBreak(long j, int i);

    public native boolean setDTR(long j, boolean z);

    public native boolean setEventsMask(long j, int i);

    public native boolean setFlowControlMode(long j, int i);

    public native boolean setParams(long j, int i, int i2, int i3, int i4, boolean z, boolean z2, int i5);

    public native boolean setRTS(long j, boolean z);

    public native int[][] waitEvents(long j);

    public native boolean writeBytes(long j, byte[] bArr);

    static {
        try {
            System.loadLibrary("jssc");
        } catch (UnsatisfiedLinkError e) {
            Log.d("jssc", "jssc library not found!");
        }
    }

    private static boolean isLibFolderExist(String libFolderPath) {
        File folder = new File(libFolderPath);
        if (folder.exists() && folder.isDirectory()) {
            return true;
        }
        return false;
    }

    private static boolean isLibFileExist(String libFilePath) {
        File folder = new File(libFilePath);
        if (folder.exists() && folder.isFile()) {
            return true;
        }
        return false;
    }

    private static boolean extractLib(String libFilePath, String osName, String libName) {
        File libFile = new File(libFilePath);
        FileOutputStream output = null;
        InputStream input = SerialNativeInterface.class.getResourceAsStream("/libs/" + osName + "/" + libName);
        if (input == null) {
            return false;
        }
        byte[] buffer = new byte[4096];
        try {
            FileOutputStream output2 = new FileOutputStream(libFilePath);
            while (true) {
                try {
                    int read = input.read(buffer);
                    if (read == -1) {
                        output2.close();
                        input.close();
                        output = output2;
                        return true;
                    }
                    output2.write(buffer, 0, read);
                } catch (Exception e) {
                    output = output2;
                }
            }
        } catch (Exception e2) {
            try {
                output.close();
                if (libFile.exists()) {
                    libFile.delete();
                }
            } catch (Exception e3) {
            }
            try {
                input.close();
                return false;
            } catch (Exception e4) {
                return false;
            }
        }
    }

    public static int getOsType() {
        return osType;
    }

    public static String getLibraryVersion() {
        return "2.8.0";
    }

    public static String getLibraryBaseVersion() {
        return libVersion;
    }

    public static String getLibraryMinorSuffix() {
        return "0";
    }
}
