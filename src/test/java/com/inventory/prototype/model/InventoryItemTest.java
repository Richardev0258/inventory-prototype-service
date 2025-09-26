package com.inventory.prototype.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventoryItemTest {

    @Test
    void testConstructorAndGetters() {
        InventoryItem item = new InventoryItem("SKU123", "Test Item", 10);

        assertNull(item.getId());
        assertEquals("SKU123", item.getSku());
        assertEquals("Test Item", item.getName());
        assertEquals(10, item.getQuantity());
        assertEquals(0, item.getVersion());
    }

    @Test
    void testSetters() {
        InventoryItem item = new InventoryItem();
        item.setId(1L);
        item.setSku("SKU999");
        item.setName("Updated");
        item.setQuantity(50);
        item.setVersion(2);

        assertEquals(1L, item.getId());
        assertEquals("SKU999", item.getSku());
        assertEquals("Updated", item.getName());
        assertEquals(50, item.getQuantity());
        assertEquals(2, item.getVersion());
    }
}
