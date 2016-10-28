package com.mc.vending.tools;

public class BusinessException extends RuntimeException {
    private static final long serialVersionUID = 6832695224568830049L;
    private String code;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, String code) {
        super(message);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
