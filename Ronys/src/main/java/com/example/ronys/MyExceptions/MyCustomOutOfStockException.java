package com.example.ronys.MyExceptions;

public class MyCustomOutOfStockException extends RuntimeException {
    public MyCustomOutOfStockException(String message) {
        super(message);
    }

    public MyCustomOutOfStockException(String message, Throwable cause) {
        super(message, cause);
    }
}
