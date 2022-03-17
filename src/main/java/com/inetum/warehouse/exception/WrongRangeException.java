package com.inetum.warehouse.exception;

public class WrongRangeException extends RuntimeException {
    public WrongRangeException(String msg) {
        super(msg);
    }
}