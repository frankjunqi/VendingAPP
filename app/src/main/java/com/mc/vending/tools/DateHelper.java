package com.mc.vending.tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.log4j.helpers.FileWatchdog;

public class DateHelper {
    private static long MS_IN_DAY = 86400000;

    public static Date currentDateTime() {
        return new Date();
    }

    public static Date currentDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    public static Date getDateZero(Date date) {
        return parse(format(date, "yyyy-MM-dd") + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
    }

    public static Date currentTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis() % MS_IN_DAY);
        calendar.set(2, 0);
        calendar.set(1, 1);
        calendar.set(5, 1);
        return calendar.getTime();
    }

    public static Date getDate(int year, int month, int dayOfMonth) {
        if (month <= 0 || month > 12) {
            throw new RuntimeException("有效月份值必须从1开始，小于等于12，1表示1月，2表示2月，以此类推");
        } else if (year < 0) {
            throw new RuntimeException("年份必须大于等于0");
        } else {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month - 1, dayOfMonth, 0, 0, 0);
            calendar.set(14, 0);
            return calendar.getTime();
        }
    }

    public static String format(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        pattern = pattern == null ? "" : pattern.trim();
        if ("".equals(pattern)) {
            return date.toString();
        }
        try {
            return new SimpleDateFormat(pattern).format(date);
        } catch (Throwable th) {
            RuntimeException runtimeException = new RuntimeException("无法格式化日期时间：" + date.toString() + ", 格式：" + pattern);
        }
        return "";
    }

    public static Date parse(String dateString, String pattern) {
        Date date = null;
        dateString = dateString == null ? "" : dateString.trim();
        pattern = pattern == null ? "" : pattern.trim();
        if (!("".equals(dateString) || "".equals(pattern))) {
            try {
                date = new SimpleDateFormat(pattern).parse(dateString);
            } catch (Exception e) {
            }
        }
        return date;
    }

    public static Date parse(String dateString) {
        Date date = null;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        dateString = dateString == null ? "" : dateString.trim();
        pattern = pattern == null ? "" : pattern.trim();
        if (!("".equals(dateString) || "".equals(pattern))) {
            try {
                date = new SimpleDateFormat(pattern).parse(dateString);
            } catch (Exception e) {
            }
        }
        return date;
    }

    public static Date add(Date date, int field, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.add(field, amount);
        return calendar.getTime();
    }

    public static Date truncateTime(Date date, int dateIndex) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(5, dateIndex);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    public static Date truncateTime(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(11, 0);
        calendar.set(12, 0);
        calendar.set(13, 0);
        calendar.set(14, 0);
        return calendar.getTime();
    }

    public static Date truncateDate(Date date) {
        if (date == null) {
            return null;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(1, 1);
        calendar.set(2, 0);
        calendar.set(5, 0);
        return calendar.getTime();
    }

    public static long cha(Date startTime, Date endTime, int type) {
        long cha = 0;
        long diff = endTime.getTime() - startTime.getTime();
        switch (type) {
            case 1:
                cha = (long) (endTime.getYear() - startTime.getYear());
                break;
            case 2:
                cha = (long) (((endTime.getYear() - startTime.getYear()) * 12) + (endTime.getMonth() - startTime.getMonth()));
                break;
            case 5:
                cha = diff / 86400000;
                break;
            case 10:
                diff = endTime.getTime() - getDateZero(endTime).getTime();
                cha = (diff % 86400000) / 3600000;
                break;
        }
        long hour = (diff % 86400000) / 3600000;
        long min = ((diff % 86400000) % 3600000) / FileWatchdog.DEFAULT_DELAY;
        long sec = (((diff % 86400000) % 3600000) % FileWatchdog.DEFAULT_DELAY) / 1000;
        System.out.println("时间相差：" + (diff / 86400000) + "天" + hour + "小时" + min + "分钟" + sec + "秒。");
        return cha;
    }

    public static void main(String[] args) {
        Date cDate = new Date();
        System.out.println(cDate);
        System.out.println(cha(parse("2015-09-14 10:10:22"), cDate, 5));
        System.out.println(cha(parse("2015-09-18 10:10:22"), cDate, 10));
        System.out.println(cha(parse("2013-09-18 10:10:22"), cDate, 1));
        System.out.println(cha(parse("2015-09-18 10:10:22"), cDate, 2));
    }
}
