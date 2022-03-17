package com.inetum.warehouse.exception;

public class EmptyOrderException extends RuntimeException {
    public EmptyOrderException(String msg) {
        super(msg);
    }
}