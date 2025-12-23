package com.ecommerce.order.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(Long productId) {
        super("Product with ID " + productId + " is out of stock");
    }
}
