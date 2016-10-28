package com.mc.vending.tools;

import java.util.regex.Pattern;

public final class StringHelper {
    public static boolean isEmpty(String value, boolean trim, char... trimChars) {
        if (trim) {
            if (value == null || trim(value, trimChars).length() <= 0) {
                return true;
            }
            return false;
        } else if (value == null || value.length() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEmpty(String value, boolean trim) {
        return isEmpty(value, trim, ' ');
    }

    public static boolean isEmpty(String value) {
        return isEmpty(value, false);
    }

    public static String nullSafeString(String value) {
        return value == null ? "" : value;
    }

    public static String trim(String value) {
        return trim(3, value, ' ');
    }

    public static String trim(String value, char... chars) {
        return trim(3, value, chars);
    }

    public static String trimStart(String value, char... chars) {
        return trim(1, value, chars);
    }

    public static String trimEnd(String value, char... chars) {
        return trim(2, value, chars);
    }

    private static String trim(int mode, String value, char... chars) {
        if (value == null || value.length() <= 0) {
            return value;
        }
        int index;
        int index2;
        int startIndex = 0;
        int endIndex = value.length();
        if (mode != 1) {
            if (mode == 3) {
                index = 0;
            }
            if (startIndex >= endIndex) {
                return "";
            }
            if (mode == 2 || mode == 3) {
                index = endIndex - 1;
                while (index >= 0) {
                    index2 = index - 1;
                    if (contains(chars, value.charAt(index))) {
                        break;
                    }
                    endIndex--;
                    index = index2;
                }
                index2 = index;
            }
            if (startIndex >= endIndex) {
                return "";
            }
            return (startIndex == 0 || endIndex != value.length() - 1) ? value.substring(startIndex, endIndex) : value;
        } else {
            index = 0;
        }
        while (index < endIndex) {
            index2 = index + 1;
            if (!contains(chars, value.charAt(index))) {
                break;
            }
            startIndex++;
            index = index2;
        }
        index2 = index;
        if (startIndex >= endIndex) {
            return "";
        }
        index = endIndex - 1;
        while (index >= 0) {
            index2 = index - 1;
            if (contains(chars, value.charAt(index))) {
                break;
            }
            endIndex--;
            index = index2;
        }
        index2 = index;
        if (startIndex >= endIndex) {
            return "";
        }
        if (startIndex == 0) {
        }
        return "";
    }

    private static boolean contains(char[] chars, char chr) {
        if (chars == null || chars.length <= 0) {
            return false;
        }
        for (char c : chars) {
            if (c == chr) {
                return true;
            }
        }
        return false;
    }

    public static boolean isEmail(String email) {
        if (email == null) {
            return false;
        }
        try {
            email = email.replaceAll(" ", "");
            if ("".equals(email)) {
                return false;
            }
            return match("^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$", email);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isMobile(String phone) {
        if (phone == null) {
            return false;
        }
        try {
            phone = phone.replaceAll(" ", "");
            if ("".equals(phone)) {
                return false;
            }
            return match("^1[3|4|5|8]\\d{9}$", phone);
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean match(String regex, String str) {
        return Pattern.compile(regex).matcher(str).matches();
    }

    public static boolean isNumeric(String str) {
        if (Pattern.compile("[0-9]*").matcher(str).matches()) {
            return true;
        }
        return false;
    }

    public static String autoCompletionCode(String str) {
        str = nullSafeString(str).trim();
        int length = str.length();
        if (length <= 0 || length > 8) {
            return str;
        }
        String tmpStr = "";
        for (int i = 0; i < 8 - length; i++) {
            tmpStr = new StringBuilder(String.valueOf(tmpStr)).append("0").toString();
        }
        return new StringBuilder(String.valueOf(tmpStr)).append(str).toString();
    }
}
