package com.inventory.prototype.service;

import com.inventory.prototype.model.InventoryItem;

public interface InventoryCommandService {
    InventoryItem setStock(String sku, String name, int quantity);
    void reserveStock(String sku, int quantity);
    void releaseStock(String sku, int quantity);
}
