package com.inventory.prototype.service;

import com.inventory.prototype.model.InventoryItem;

import java.util.List;
import java.util.Optional;

public interface InventoryQueryService {
    Optional<InventoryItem> getItem(String sku);
    List<InventoryItem> getAllItems();
}
