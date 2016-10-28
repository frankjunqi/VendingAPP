package com.mc.vending.tools.utils;

import android.support.v4.view.MotionEventCompat;
import com.mc.vending.tools.ZillionLog;
import java.math.BigInteger;

public class MyFunc {
    public static final String cmdBeep = "030FFF00F0";
    public static final String cmdCloseKeyBoard = "02303234353030343703";
    public static final String cmdGetSerialNo = "01F0F0";
    public static final String cmdOpenKeyBoard = "02303234353033343403";

    public static int isOdd(int num) {
        return num & 1;
    }

    public static int HexToInt(String inHex) {
        return Integer.parseInt(inHex, 16);
    }

    public static byte HexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }

    public static String Byte2Hex(Byte inByte) {
        return String.format("%02x", new Object[]{inByte}).toUpperCase();
    }

    public static String ByteArrToHex(byte[] inBytArr) {
        StringBuilder strBuilder = new StringBuilder();
        for (byte valueOf : inBytArr) {
            strBuilder.append(Byte2Hex(Byte.valueOf(valueOf)));
        }
        return strBuilder.toString();
    }

    public static String ByteArrToHex(byte[] inBytArr, boolean isSplit) {
        StringBuilder strBuilder = new StringBuilder();
        for (byte valueOf : inBytArr) {
            strBuilder.append(Byte2Hex(Byte.valueOf(valueOf)));
            if (isSplit) {
                strBuilder.append(" ");
            }
        }
        return strBuilder.toString();
    }

    public static String ByteArrToHex(byte[] inBytArr, int offset, int byteCount) {
        StringBuilder strBuilder = new StringBuilder();
        int j = byteCount;
        for (int i = offset; i < j; i++) {
            strBuilder.append(Byte2Hex(Byte.valueOf(inBytArr[i])));
        }
        return strBuilder.toString();
    }

    public static byte[] HexToByteArr(String inHex) {
        byte[] result;
        int hexlen = inHex.length();
        if (isOdd(hexlen) == 1) {
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = HexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    public static int getBCC(String cmd) {
        int bcc = 0;
        for (int i = 1; i <= cmd.length() / 2; i++) {
            bcc ^= Integer.parseInt(cmd.substring((i * 2) - 2, i * 2), 16);
        }
        return bcc;
    }

    public static boolean checkBCC(byte[] inBytArr) {
        byte bcc = (byte) 0;
        for (int i = 1; i <= inBytArr.length - 2; i++) {
            bcc ^= inBytArr[i];
        }
        return inBytArr[inBytArr.length + -1] == bcc;
    }

    public static String getRFIDSerialNo(String strHex) {
        String result = "";
        try {
            String Str = strHex.replaceAll("\\s*", "");
            byte[] arrHex = HexToByteArr(Str);
            if (arrHex.length != 7) {
                result = null;
            } else if (arrHex[0] != arrHex.length - 2) {
                result = null;
            } else if (checkBCC(arrHex) && arrHex[0] == (byte) 5) {
                String str = Str.substring(4, 12);
                String tmp = "";
                for (int i = str.length() - 2; i >= 0; i -= 2) {
                    tmp = new StringBuilder(String.valueOf(tmp)).append(str.substring(i, i + 2)).toString();
                }
                result = new BigInteger(tmp, 16).toString();
            }
            if (result == null) {
                return getIDSerialNo(strHex);
            }
            return result;
        } catch (Exception e) {
            ZillionLog.e("getRFIDSerialNo", e.getMessage(), e);
            return null;
        }
    }

    public static String getIDSerialNo(String strHex) {
        try {
            String Str = strHex.replaceAll("\\s*", "");
            if (Str.length() != 28 || !Str.substring(0, 2).equals("02") || !Str.substring(Str.length() - 6).equals("0D0A03")) {
                return null;
            }
            Str = Str.substring(2, 22);
            String tmp = "";
            for (int i = 0; i <= Str.length() - 2; i += 2) {
                tmp = new StringBuilder(String.valueOf(tmp)).append((char) Integer.parseInt(Str.substring(i, i + 2), 16)).toString();
            }
            return tmp;
        } catch (Exception e) {
            ZillionLog.e("getIDSerialNo", e.getMessage(), e);
            return null;
        }
    }

    public static void main(String[] args) {
        System.out.println(cmdOpenVender(1, 3));
    }

    public static String getVenderCommand(String cmd) {
        String cmdString = cmd.replaceAll("\\s*", "");
        return ("AA" + cmdString + Integer.toHexString(getBCC(cmdString)) + "AC").toUpperCase();
    }

    public static String cmdOpenVender(int col, int row) {
        return getVenderCommand("53" + String.format("%02x%02x000000", new Object[]{Integer.valueOf(col + 48), Integer.valueOf(row + 48)}));
    }

    public static char convertIntToAscii(int a) {
        return (a < 0 || a > MotionEventCompat.ACTION_MASK) ? '\u0000' : (char) a;
    }

    public static String cmdCheckVender(int col, int row) {
        return getVenderCommand("52" + String.format("%02x%02x000000", new Object[]{Integer.valueOf(col + 48), Integer.valueOf(row + 48)}));
    }

    public static int getSum(String cmd) {
        int sum = 0;
        String inHex = cmd.replaceAll("\\s*", "");
        for (int i = 1; i <= cmd.length() / 2; i++) {
            sum += Integer.parseInt(inHex.substring((i * 2) - 2, i * 2), 16);
        }
        return sum;
    }

    public static String getStoreCommand(String cmd) {
        return (cmd.replaceAll("\\s*", "") + String.format("%04x", new Object[]{Integer.valueOf(getSum(cmd))})).toUpperCase();
    }

    public static String cmdOpenStoreDoor(int type, int number, int door) {
        return getStoreCommand(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(String.format("%04x", new Object[]{Integer.valueOf(type)}))).append(String.format("%04x", new Object[]{Integer.valueOf(number)})).append("00 01 00 01").toString())).append(String.format("%02x", new Object[]{Integer.valueOf(door)})).toString());
    }

    public static String cmdCheckStoreDoor(int type, int number) {
        return getStoreCommand(new StringBuilder(String.valueOf(String.format("%04x", new Object[]{Integer.valueOf(type)}))).append(String.format("%04x", new Object[]{Integer.valueOf(number)})).append("00 02 00 01 00").toString());
    }

    public static String cmdCheckStoreStatus(int type, int number) {
        return getStoreCommand(new StringBuilder(String.valueOf(String.format("%04x", new Object[]{Integer.valueOf(type)}))).append(String.format("%04x", new Object[]{Integer.valueOf(number)})).append("00 E0 00 01 00").toString());
    }

    public static String cmdOpenKeyBoard() {
        return "02303234353033343403";
    }

    public static String cmdCloseKeyBoard() {
        return "02303234353030343703";
    }

    public static String cmdGetSerialNo() {
        return "01F0F0";
    }

    public static String cmdBeep() {
        return "030FFF00F0";
    }
}
