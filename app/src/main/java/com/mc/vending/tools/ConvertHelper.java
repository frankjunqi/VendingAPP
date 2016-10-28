package com.mc.vending.tools;

import java.math.BigDecimal;
import java.math.BigInteger;

public final class ConvertHelper {
    public static Boolean toBool(String value, Boolean defaultValue) {
        if (value == null || value.length() <= 0) {
            return defaultValue;
        }
        value = value.trim().toLowerCase();
        if (value.length() <= 0) {
            return defaultValue;
        }
        if ("false".equals(value) || "0".equals(value)) {
            return Boolean.valueOf(false);
        }
        if ("true".equals(value) || "1".equals(value)) {
            return Boolean.valueOf(true);
        }
        return Boolean.valueOf(true);
    }

    public static Boolean toBool(Object value, Boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        Class<?> clazz = value.getClass();
        if (Boolean.class.equals(clazz)) {
            return (Boolean) value;
        }
        if (String.class.equals(clazz)) {
            return toBool(String.valueOf(value), defaultValue);
        }
        if (Integer.class.equals(clazz)) {
            return ((Integer) value).intValue() != 0 ? Boolean.valueOf(true) : Boolean.valueOf(false);
        } else {
            if (Byte.class.equals(clazz)) {
                return ((Byte) value).byteValue() != (byte) 0 ? Boolean.valueOf(true) : Boolean.valueOf(false);
            } else {
                if (Short.class.equals(clazz)) {
                    return ((Short) value).shortValue() != (short) 0 ? Boolean.valueOf(true) : Boolean.valueOf(false);
                } else {
                    if (Long.class.equals(clazz)) {
                        return ((Long) value).longValue() != 0 ? Boolean.valueOf(true) : Boolean.valueOf(false);
                    } else {
                        if (Double.class.equals(clazz)) {
                            return ((Double) value).doubleValue() != 0.0d ? Boolean.valueOf(true) : Boolean.valueOf(false);
                        } else {
                            if (Float.class.equals(clazz)) {
                                return ((Float) value).floatValue() != 0.0f ? Boolean.valueOf(true) : Boolean.valueOf(false);
                            } else {
                                if (BigDecimal.class.equals(clazz)) {
                                    return ((BigDecimal) value).compareTo(BigDecimal.ZERO) != 0 ? Boolean.valueOf(true) : Boolean.valueOf(false);
                                } else {
                                    if (BigInteger.class.equals(clazz)) {
                                        return ((BigInteger) value).compareTo(BigInteger.ZERO) != 0 ? Boolean.valueOf(true) : Boolean.valueOf(false);
                                    } else {
                                        return defaultValue;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static Integer toInt(String value, Integer defaultValue) {
        if (value != null && value.trim().length() > 0) {
            if (value.indexOf(46) >= 0) {
                try {
                    defaultValue = Integer.valueOf(Double.valueOf(value).intValue());
                } catch (Exception e) {
                }
            } else {
                try {
                    defaultValue = Integer.valueOf(value);
                } catch (Exception e2) {
                }
            }
        }
        return defaultValue;
    }

    public static Integer toInt(Object value, Integer defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        Class<?> cls = value.getClass();
        if (Integer.class.equals(cls)) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return Integer.valueOf(((Number) value).intValue());
        }
        if (String.class.equals(cls)) {
            return toInt((String) value, defaultValue);
        }
        return defaultValue;
    }

    public static Long toLong(String value, Long defaultValue) {
        if (value != null && value.trim().length() > 0) {
            if (value.indexOf(46) >= 0) {
                try {
                    defaultValue = Long.valueOf(Double.valueOf(value).longValue());
                } catch (Exception e) {
                }
            } else {
                try {
                    defaultValue = Long.valueOf(value);
                } catch (Exception e2) {
                }
            }
        }
        return defaultValue;
    }

    public static Long toLong(Object value, Long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        Class<?> cls = value.getClass();
        if (Long.class.equals(cls)) {
            return (Long) value;
        }
        if (value instanceof Number) {
            return Long.valueOf(((Number) value).longValue());
        }
        if (String.class.equals(cls)) {
            return toLong((String) value, defaultValue);
        }
        return defaultValue;
    }

    public static Float toFloat(String value, Float defaultValue) {
        if (value != null && value.trim().length() > 0) {
            try {
                defaultValue = Float.valueOf(value);
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public static Float toFloat(Object value, Float defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        Class<?> cls = value.getClass();
        if (Float.class.equals(cls)) {
            return (Float) value;
        }
        if (value instanceof Number) {
            return Float.valueOf(((Number) value).floatValue());
        }
        if (String.class.equals(cls)) {
            return toFloat((String) value, defaultValue);
        }
        return defaultValue;
    }

    public static Double toDouble(String value, Double defaultValue) {
        if (value != null && value.trim().length() > 0) {
            try {
                defaultValue = Double.valueOf(value);
            } catch (Exception e) {
            }
        }
        return defaultValue;
    }

    public static Double toDouble(Object value, Double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        Class<?> cls = value.getClass();
        if (Double.class.equals(cls)) {
            return (Double) value;
        }
        if (value instanceof Number) {
            return Double.valueOf(((Number) value).doubleValue());
        }
        if (String.class.equals(cls)) {
            return toDouble((String) value, defaultValue);
        }
        return defaultValue;
    }

    public static BigDecimal toDecimal(String value, BigDecimal defaultValue) {
        if (value == null || value.trim().length() <= 0) {
            return defaultValue;
        }
        try {
            return new BigDecimal(value);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static BigDecimal toDecimal(Object value, BigDecimal defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        Class clazz = value.getClass();
        if (BigDecimal.class.equals(clazz)) {
            return (BigDecimal) value;
        }
        if (Double.class.equals(clazz)) {
            return toDecimal(value.toString(), defaultValue);
        }
        if (Float.class.equals(clazz)) {
            return toDecimal(value.toString(), defaultValue);
        }
        if (String.class.equals(clazz)) {
            return toDecimal((String) value, defaultValue);
        }
        if (Integer.class.equals(clazz)) {
            return new BigDecimal(((Integer) value).intValue());
        }
        if (Short.class.equals(clazz)) {
            return new BigDecimal(((Short) value).shortValue());
        }
        if (Byte.class.equals(clazz)) {
            return new BigDecimal(((Byte) value).byteValue());
        }
        if (Long.class.equals(clazz)) {
            return new BigDecimal(((Long) value).longValue());
        }
        return defaultValue;
    }
}
