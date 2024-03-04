package com.bitespeed.us.exception;

public class GlobalException extends RuntimeException {

    private String message;
    private Exception exception;
    private FaultCode faultCode;

    public GlobalException(String message) {
        super(message);
        this.message = message;
    }

    public GlobalException(String message, Exception exception) {
        super(message, exception);
        this.message = message;
        this.exception = exception;
    }

    public GlobalException(String message, FaultCode faultCode) {
        super(message);
        this.message = message;
        this.faultCode = faultCode;
    }

    public GlobalException(String message, Exception exception, FaultCode faultCode) {
        super(message, exception);
        this.message = message;
        this.exception = exception;
        this.faultCode = faultCode;
    }

}
