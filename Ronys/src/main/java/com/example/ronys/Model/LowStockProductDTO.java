package com.example.ronys.Model;// LowStockProductDTO.java

public class LowStockProductDTO {
    private Long productCode;
    private String color;
    private int quantity;

    // Constructor, getters, and setters


    public Long getProductCode() {
        return productCode;
    }

    public void setProductCode(Long productCode) {
        this.productCode = productCode;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LowStockProductDTO(Long productCode, String color, int quantity) {
        this.productCode = productCode;
        this.color = color;
        this.quantity = quantity;
    }
}
