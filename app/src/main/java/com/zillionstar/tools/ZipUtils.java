package com.zillionstar.tools;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    private static final int BUFF_SIZE = 1048576;
    private static final String HTTP_UTF8 = "UTF-8";

    public static void zipFiles(Collection<File> resFileList, File zipFile) throws IOException {
        Throwable th;
        ZipOutputStream zipout = null;
        try {
            ZipOutputStream zipout2 = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile), 1048576));
            try {
                for (File resFile : resFileList) {
                    zipFile(resFile, zipout2, "");
                }
                if (zipout2 != null) {
                    zipout2.close();
                }
            } catch (Throwable th2) {
                th = th2;
                zipout = zipout2;
            }
        } catch (Throwable th3) {
            th = th3;
            if (zipout != null) {
                zipout.close();
            }
        }
    }

    public static void zipFiles(Collection<File> resFileList, File zipFile, String comment) throws IOException {
        Throwable th;
        ZipOutputStream zipout = null;
        try {
            ZipOutputStream zipout2 = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile), 1048576));
            try {
                for (File resFile : resFileList) {
                    zipFile(resFile, zipout2, "");
                }
                zipout2.setComment(comment);
                if (zipout2 != null) {
                    zipout2.close();
                }
            } catch (Throwable th2) {
                th = th2;
                zipout = zipout2;
            }
        } catch (Throwable th3) {
            th = th3;
            if (zipout != null) {
                zipout.close();
            }
        }
    }

    public static void upZipFile(File zipFile, String folderPath) throws ZipException, IOException {
        Throwable th;
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdirs();
        }
        ZipFile zf = new ZipFile(zipFile);
        InputStream in = null;
        OutputStream outputStream = null;
        try {
            Enumeration<?> entries = zf.entries();
            OutputStream out = null;
            while (entries.hasMoreElements()) {
                try {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    in = zf.getInputStream(entry);
                    File desFile = new File(new String(new StringBuilder(String.valueOf(folderPath)).append(File.separator).append(entry.getName()).toString().getBytes("8859_1"), HTTP_UTF8));
                    if (!desFile.exists()) {
                        File fileParentDir = desFile.getParentFile();
                        if (!fileParentDir.exists()) {
                            fileParentDir.mkdirs();
                        }
                        desFile.createNewFile();
                    }
                    outputStream = new FileOutputStream(desFile);
                    byte[] buffer = new byte[1048576];
                    while (true) {
                        int realLength = in.read(buffer);
                        if (realLength <= 0) {
                            break;
                        }
                        outputStream.write(buffer, 0, realLength);
                    }
                    out = outputStream;
                } catch (Throwable th2) {
                    th = th2;
                    outputStream = out;
                }
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
                return;
            }
            return;
        } catch (Throwable th3) {
            th = th3;
        }
        if (in != null) {
            in.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }

    public static ArrayList<File> upZipSelectedFile(File zipFile, String folderPath, String nameContains) throws ZipException, IOException {
        Throwable th;
        ArrayList<File> fileList = new ArrayList();
        File desDir = new File(folderPath);
        if (!desDir.exists()) {
            desDir.mkdir();
        }
        ZipFile zf = new ZipFile(zipFile);
        InputStream in = null;
        OutputStream outputStream = null;
        try {
            Enumeration<?> entries = zf.entries();
            OutputStream out = null;
            while (entries.hasMoreElements()) {
                try {
                    ZipEntry entry = (ZipEntry) entries.nextElement();
                    if (entry.getName().contains(nameContains)) {
                        in = zf.getInputStream(entry);
                        File desFile = new File(new String(new StringBuilder(String.valueOf(folderPath)).append(File.separator).append(entry.getName()).toString().getBytes("8859_1"), HTTP_UTF8));
                        if (!desFile.exists()) {
                            File fileParentDir = desFile.getParentFile();
                            if (!fileParentDir.exists()) {
                                fileParentDir.mkdirs();
                            }
                            desFile.createNewFile();
                        }
                        outputStream = new FileOutputStream(desFile);
                        byte[] buffer = new byte[1048576];
                        while (true) {
                            int realLength = in.read(buffer);
                            if (realLength <= 0) {
                                break;
                            }
                            outputStream.write(buffer, 0, realLength);
                        }
                        fileList.add(desFile);
                        out = outputStream;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    outputStream = out;
                }
            }
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            return fileList;
        } catch (Throwable th3) {
            th = th3;
        }
        if (in != null) {
            in.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
        return null;
    }

    public static ArrayList<String> getEntriesNames(File zipFile) throws ZipException, IOException {
        ArrayList<String> entryNames = new ArrayList();
        Enumeration<?> entries = getEntriesEnumeration(zipFile);
        while (entries.hasMoreElements()) {
            entryNames.add(new String(getEntryName((ZipEntry) entries.nextElement()).getBytes(HTTP_UTF8), "8859_1"));
        }
        return entryNames;
    }

    public static Enumeration<?> getEntriesEnumeration(File zipFile) throws ZipException, IOException {
        return new ZipFile(zipFile).entries();
    }

    public static String getEntryComment(ZipEntry entry) throws UnsupportedEncodingException {
        return new String(entry.getComment().getBytes(HTTP_UTF8), "8859_1");
    }

    public static String getEntryName(ZipEntry entry) throws UnsupportedEncodingException {
        return new String(entry.getName().getBytes(HTTP_UTF8), "8859_1");
    }

    private static void zipFile(File resFile, ZipOutputStream zipout, String rootpath) throws FileNotFoundException, IOException {
        Throwable th;
        String rootpath2 = new String(new StringBuilder(String.valueOf(rootpath)).append(rootpath.trim().length() == 0 ? "" : File.separator).append(resFile.getName()).toString().getBytes("8859_1"), HTTP_UTF8);
        BufferedInputStream in = null;
        try {
            if (resFile.isDirectory()) {
                for (File file : resFile.listFiles()) {
                    zipFile(file, zipout, rootpath2);
                }
            } else {
                byte[] buffer = new byte[1048576];
                BufferedInputStream in2 = new BufferedInputStream(new FileInputStream(resFile), 1048576);
                try {
                    zipout.putNextEntry(new ZipEntry(rootpath2));
                    while (true) {
                        int realLength = in2.read(buffer);
                        if (realLength == -1) {
                            break;
                        }
                        zipout.write(buffer, 0, realLength);
                    }
                    in2.close();
                    zipout.flush();
                    zipout.closeEntry();
                    in = in2;
                } catch (Throwable th2) {
                    th = th2;
                    in = in2;
                    if (in != null) {
                        in.close();
                    }
                    throw th;
                }
            }
            if (in != null) {
                in.close();
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }
}
