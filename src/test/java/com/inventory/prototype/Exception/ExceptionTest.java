package com.inventory.prototype.Exception;

import com.inventory.prototype.exception.InventoryNotFoundException;
import com.inventory.prototype.exception.NotEnoughStockException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionTests {

    @Test
    void testNotEnoughStockExceptionMessage() {
        NotEnoughStockException ex = new NotEnoughStockException("SKU123");
        assertEquals("Not enough stock for SKU: SKU123", ex.getMessage());
    }

    @Test
    void testInventoryNotFoundExceptionMessage() {
        InventoryNotFoundException ex = new InventoryNotFoundException("SKU456");
        assertEquals("Inventory item not found for SKU: SKU456", ex.getMessage());
    }
}
