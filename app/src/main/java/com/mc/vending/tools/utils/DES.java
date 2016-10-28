package com.mc.vending.tools.utils;

import com.mc.vending.config.Constant;
import com.mc.vending.tools.DateHelper;
import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DES {
    public static String getEncrypt() {
        try {
            return Encrypt(DateHelper.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"), Constant.DES_VI, Constant.BODY_VALUE_PWD);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String Encrypt(String message, String vi, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(1, SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key.getBytes("UTF-8"))), new IvParameterSpec(vi.getBytes("UTF-8")));
        return Base64.encode(cipher.doFinal(message.getBytes("UTF-8")));
    }

    public static String Decrypt(String message, String vi, String key) throws Exception {
        Base64 base64 = new Base64();
        byte[] byteSrc = Base64.decode(message);
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(2, SecretKeyFactory.getInstance("DES").generateSecret(new DESKeySpec(key.getBytes("UTF-8"))), new IvParameterSpec(vi.getBytes("UTF-8")));
        return new String(cipher.doFinal(byteSrc));
    }
}
