package com.mc.vending.tools;

public class SystemException extends Exception {
    private static final long serialVersionUID = -8649925931214974641L;

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }
}
