package com.example.ronys.MyExceptions;

public class MyCustomDuplicateKeyException extends RuntimeException {

    public MyCustomDuplicateKeyException(String message) {
        super(message);
    }

    public MyCustomDuplicateKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
