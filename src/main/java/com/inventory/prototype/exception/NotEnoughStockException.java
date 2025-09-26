package com.inventory.prototype.exception;


public class NotEnoughStockException extends RuntimeException {
    public NotEnoughStockException(String sku) {
        super("Not enough stock for SKU: " + sku);
    }
}
