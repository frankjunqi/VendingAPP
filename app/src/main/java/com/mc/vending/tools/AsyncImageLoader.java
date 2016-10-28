package com.mc.vending.tools;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import com.mc.vending.config.Constant;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

public class AsyncImageLoader {
    Drawable drawable;
    private String filePath;
    private HashMap<String, SoftReference<Drawable>> imageCache;

    public interface ImageCallback {
        void imageLoaded(Drawable drawable, String str, String str2);
    }

    public AsyncImageLoader() {
        this.imageCache = new HashMap();
        this.filePath = Constant.DOWNLOAD_URL;
    }

    public AsyncImageLoader(String filePath) {
        this.imageCache = new HashMap();
        this.filePath = filePath;
    }

    public Drawable loadDrawable(final String imageUrl, final ImageCallback imageCallback, final String size) {
        if (this.imageCache.containsKey(imageUrl)) {
            SoftReference<Drawable> softReference = (SoftReference) this.imageCache.get(imageUrl);
            if (this.drawable != null) {
                this.drawable = null;
            }
            this.drawable = (Drawable) softReference.get();
            if (this.drawable != null) {
                return this.drawable;
            }
        }
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                imageCallback.imageLoaded((Drawable) message.obj, imageUrl, size);
            }
        };
        new Thread() {
            public void run() {
                if (imageUrl != null && !"".equals(imageUrl)) {
                    if (AsyncImageLoader.this.drawable != null) {
                        AsyncImageLoader.this.drawable = null;
                    }
                    AsyncImageLoader.this.drawable = AsyncImageLoader.this.loadImageFromUrl(imageUrl);
                    if (AsyncImageLoader.this.drawable != null) {
                        String fileName = Tools.getFileName(imageUrl);
                        if (!(AsyncImageLoader.this.drawable == null || AsyncImageLoader.this.filePath == null || AsyncImageLoader.this.filePath.equals(""))) {
                            Tools.saveImageToSD(((BitmapDrawable) AsyncImageLoader.this.drawable).getBitmap(), AsyncImageLoader.this.filePath, fileName);
                        }
                        AsyncImageLoader.this.imageCache.put(imageUrl, new SoftReference(AsyncImageLoader.this.drawable));
                        handler.sendMessage(handler.obtainMessage(0, AsyncImageLoader.this.drawable));
                    }
                }
            }
        }.start();
        return null;
    }

    public Drawable loadDrawable(final String imageUrl, final ImageCallback imageCallback) {
        if (this.imageCache.containsKey(imageUrl)) {
            Drawable drawable = (Drawable) ((SoftReference) this.imageCache.get(imageUrl)).get();
            if (drawable != null) {
                return drawable;
            }
        }
        final Handler handler = new Handler() {
            public void handleMessage(Message message) {
                imageCallback.imageLoaded((Drawable) message.obj, imageUrl, imageUrl);
            }
        };
        new Thread() {
            public void run() {
                if (AsyncImageLoader.this.drawable != null) {
                    AsyncImageLoader.this.drawable = null;
                }
                String fileName = Tools.getFileName(imageUrl);
                AsyncImageLoader.this.drawable = AsyncImageLoader.this.loadImageFromSD(fileName);
                if (AsyncImageLoader.this.drawable == null) {
                    AsyncImageLoader.this.drawable = AsyncImageLoader.this.loadImageFromUrl(imageUrl);
                }
                if (AsyncImageLoader.this.drawable != null) {
                    if (!(AsyncImageLoader.this.drawable == null || AsyncImageLoader.this.filePath == null || AsyncImageLoader.this.filePath.equals(""))) {
                        Tools.saveImageToSD(((BitmapDrawable) AsyncImageLoader.this.drawable).getBitmap(), AsyncImageLoader.this.filePath, fileName);
                    }
                    AsyncImageLoader.this.imageCache.put(imageUrl, new SoftReference(AsyncImageLoader.this.drawable));
                    handler.sendMessage(handler.obtainMessage(0, AsyncImageLoader.this.drawable));
                }
            }
        }.start();
        return null;
    }

    private Drawable loadImageFromSD(String fileName) {
        Drawable d = null;
        try {
            d = Drawable.createFromPath(this.filePath + fileName);
        } catch (Exception e) {
            ZillionLog.e("loadImageFromSD  " + e.getMessage());
        }
        return d;
    }

    private Drawable loadImageFromUrl(String url) {
        InputStream i = null;
        try {
            System.setProperty("http.keepAlive", "false");
            URL m = new URL(url);
            if (m == null || m.openStream() == null) {
                return null;
            }
            i = m.openStream();
            try {
                Drawable d = Drawable.createFromStream(i, "src");
                if (i != null) {
                    try {
                        i.close();
                    } catch (IOException e) {
                        ZillionLog.e(e.getMessage());
                        return null;
                    }
                }
                return d;
            } catch (Exception e2) {
                ZillionLog.e(e2.getMessage());
                return null;
            }
        } catch (Exception e22) {
            ZillionLog.e(e22.getMessage());
            if (i == null) {
                return null;
            }
            try {
                i.close();
                return null;
            } catch (IOException e1) {
                ZillionLog.e(e1.getMessage());
                return null;
            }
        }
    }
}
