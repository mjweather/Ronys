package com.example.ronys.MyExceptions;

public class MyCustomDuplicateSupplierException extends Exception {
    public MyCustomDuplicateSupplierException(String message) {
        super(message);
    }

    public MyCustomDuplicateSupplierException(String message, Throwable cause) {
        super(message, cause);
    }
}
