package com.zillionstar.tools;

import com.mc.vending.config.Constant;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {
    private static final int TIMEOUT_IN_MILLIONS = 5000;

    static class AnonymousClass1 extends Thread {
        private final /* synthetic */ CallBack val$callBack;
        private final /* synthetic */ String val$urlStr;

        AnonymousClass1(String str, CallBack callBack) {
            this.val$urlStr = str;
            this.val$callBack = callBack;
        }

        public void run() {
            try {
                String result = HttpUtils.doGet(this.val$urlStr);
                if (this.val$callBack != null) {
                    this.val$callBack.onRequestComplete(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class AnonymousClass2 extends Thread {
        private final /* synthetic */ CallBack val$callBack;
        private final /* synthetic */ String val$params;
        private final /* synthetic */ String val$urlStr;

        AnonymousClass2(String str, String str2, CallBack callBack) {
            this.val$urlStr = str;
            this.val$params = str2;
            this.val$callBack = callBack;
        }

        public void run() {
            try {
                String result = HttpUtils.doPost(this.val$urlStr, this.val$params);
                if (this.val$callBack != null) {
                    this.val$callBack.onRequestComplete(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface CallBack {
        void onRequestComplete(String str);
    }

    public static void doGetAsyn(String urlStr, CallBack callBack) {
        new AnonymousClass1(urlStr, callBack).start();
    }

    public static void doPostAsyn(String urlStr, String params, CallBack callBack) throws Exception {
        new AnonymousClass2(urlStr, params, callBack).start();
    }

    public static String doGet(String urlStr) {
        Exception e;
        Throwable th;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try {
            URL url = new URL(urlStr);
            URL url2;
            try {
                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(5000);
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                conn.setRequestProperty("accept", "*/*");
                conn.setRequestProperty("connection", "Keep-Alive");
                if (conn.getResponseCode() == 200) {
                    is = conn.getInputStream();
                    ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                    try {
                        byte[] buf = new byte[128];
                        while (true) {
                            int len = is.read(buf);
                            if (len == -1) {
                                break;
                            }
                            baos2.write(buf, 0, len);
                        }
                        baos2.flush();
                        String byteArrayOutputStream = baos2.toString();
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e2) {
                            }
                        }
                        if (baos2 != null) {
                            try {
                                baos2.close();
                            } catch (IOException e3) {
                            }
                        }
                        conn.disconnect();
                        baos = baos2;
                        url2 = url;
                        return byteArrayOutputStream;
                    } catch (Exception e4) {
                        e = e4;
                        baos = baos2;
                        url2 = url;
                    } catch (Throwable th2) {
                        th = th2;
                        baos = baos2;
                        url2 = url;
                    }
                } else {
                    throw new RuntimeException(" responseCode is not 200 ... ");
                }
            } catch (Exception e5) {
                e = e5;
                url2 = url;
                try {
                    e.printStackTrace();
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e6) {
                        }
                    }
                    if (baos != null) {
                        try {
                            baos.close();
                        } catch (IOException e7) {
                        }
                    }
                    conn.disconnect();
                    return null;
                } catch (Throwable th3) {
                    th = th3;
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e8) {
                        }
                    }
                    if (baos != null) {
                        try {
                            baos.close();
                        } catch (IOException e9) {
                        }
                    }
                    conn.disconnect();
                }
            } catch (Throwable th4) {
                th = th4;
                url2 = url;
                if (is != null) {
                    is.close();
                }
                if (baos != null) {
                    baos.close();
                }
                conn.disconnect();
            }
        } catch (Exception e10) {
            e = e10;
            e.printStackTrace();
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            conn.disconnect();
            return null;
        }
        return null;
    }

    public static String doPost(String url, String param) {
        Exception e;
        Throwable th;
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestMethod("POST");
            conn.setRequestProperty(Constant.HEADER_KEY_CONTENT_TYPE, Constant.HEADER_VALUE_CONTENT_TYPE);
            conn.setRequestProperty("charset", "utf-8");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            if (!(param == null || param.trim().equals(""))) {
                PrintWriter out2 = new PrintWriter(conn.getOutputStream());
                try {
                    out2.print(param);
                    out2.flush();
                    out = out2;
                } catch (Exception e2) {
                    e = e2;
                    out = out2;
                    try {
                        e.printStackTrace();
                        if (out != null) {
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                        return result;
                    } catch (Throwable th2) {
                        th = th2;
                        if (out != null) {
                            out.close();
                        }
                        if (in != null) {
                            in.close();
                        }
                    }
                } catch (Throwable th3) {
                    out = out2;
                    if (out != null) {
                        out.close();
                    }
                    if (in != null) {
                        in.close();
                    }
                }
            }
            BufferedReader in2 = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while (true) {
                try {
                    String line = in2.readLine();
                    if (line == null) {
                        break;
                    }
                    result = new StringBuilder(String.valueOf(result)).append(line).toString();
                } catch (Exception e3) {
                    e = e3;
                    in = in2;
                } catch (Throwable th4) {
                    th = th4;
                    in = in2;
                }
            }
            if (out != null) {
                out.close();
            }
            if (in2 != null) {
                in2.close();
                in = in2;
                return result;
            }
            in = in2;
        } catch (Exception e4) {
            e = e4;
            e.printStackTrace();
            if (out != null) {
                out.close();
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            return result;
        }
        return result;
    }
}
