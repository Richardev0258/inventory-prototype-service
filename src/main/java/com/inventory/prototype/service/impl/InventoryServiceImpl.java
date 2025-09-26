package com.inventory.prototype.service.impl;

import com.inventory.prototype.exception.InventoryNotFoundException;
import com.inventory.prototype.exception.NotEnoughStockException;
import com.inventory.prototype.model.InventoryItem;
import com.inventory.prototype.repository.InventoryRepository;
import com.inventory.prototype.service.InventoryCommandService;
import com.inventory.prototype.service.InventoryQueryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryQueryService, InventoryCommandService {

    private final InventoryRepository repository;

    public InventoryServiceImpl(InventoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<InventoryItem> getItem(String sku) {
        return repository.findBySku(sku);
    }

    @Override
    @Transactional
    public InventoryItem setStock(String sku, String name, int quantity) {
        return repository.findBySku(sku)
                .map(item -> {
                    item.setName(name);
                    item.setQuantity(quantity);
                    return repository.save(item);
                })
                .orElseGet(() -> repository.save(new InventoryItem(sku, name, quantity)));
    }

    @Override
    @Transactional
    public void reserveStock(String sku, int quantity) {
        InventoryItem item = repository.findBySku(sku)
                .orElseThrow(() -> new InventoryNotFoundException(sku));

        if (item.getQuantity() < quantity) {
            throw new NotEnoughStockException(sku);
        }

        item.setQuantity(item.getQuantity() - quantity);
        repository.save(item);
    }

    @Override
    @Transactional
    public void releaseStock(String sku, int quantity) {
        InventoryItem item = repository.findBySku(sku)
                .orElseThrow(() -> new InventoryNotFoundException(sku));

        item.setQuantity(item.getQuantity() + quantity);
        repository.save(item);
    }

    @Override
    public List<InventoryItem> getAllItems() {
        return repository.findAll();
    }
}