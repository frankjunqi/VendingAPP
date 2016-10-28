package com.mc.vending.tools;

import java.io.Serializable;

public class ServiceResult<T> implements Serializable {
    private static final long serialVersionUID = 3765720967319047788L;
    private String code;
    private boolean isSuccess = true;
    private String message;
    private T result;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return this.isSuccess;
    }

    public void setSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getResult() {
        return this.result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public String toString() {
        return "ServiceResult [result=" + this.result + ", message=" + this.message + ", isSuccess=" + this.isSuccess + ", code=" + this.code + "]";
    }
}
