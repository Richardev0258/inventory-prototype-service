package com.inventory.prototype.service.impl;

import com.inventory.prototype.exception.InventoryNotFoundException;
import com.inventory.prototype.exception.NotEnoughStockException;
import com.inventory.prototype.model.InventoryItem;
import com.inventory.prototype.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InventoryServiceImplTest {

    @Mock
    private InventoryRepository repository;

    @InjectMocks
    private InventoryServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetItem() {
        InventoryItem item = new InventoryItem("sku1", "Item 1", 10);
        when(repository.findBySku("sku1")).thenReturn(Optional.of(item));

        Optional<InventoryItem> result = service.getItem("sku1");

        assertTrue(result.isPresent());
        assertEquals("Item 1", result.get().getName());
    }

    @Test
    void testGetAllItems() {
        InventoryItem item1 = new InventoryItem("SKU123", "Laptop", 10);
        InventoryItem item2 = new InventoryItem("SKU999", "Phone", 5);

        when(repository.findAll()).thenReturn(List.of(item1, item2));

        List<InventoryItem> result = service.getAllItems();

        assertEquals(2, result.size());
        assertEquals("SKU123", result.get(0).getSku());
        assertEquals("Phone", result.get(1).getName());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testSetStockNewItem() {
        InventoryItem item = new InventoryItem("sku2", "Item 2", 20);
        when(repository.findBySku("sku2")).thenReturn(Optional.empty());
        when(repository.save(any(InventoryItem.class))).thenReturn(item);

        InventoryItem result = service.setStock("sku2", "Item 2", 20);

        assertEquals(20, result.getQuantity());
        verify(repository, times(1)).save(any(InventoryItem.class));
    }

    @Test
    void testSetStockUpdateItem() {
        InventoryItem existing = new InventoryItem("sku3", "Old", 5);
        when(repository.findBySku("sku3")).thenReturn(Optional.of(existing));
        when(repository.save(existing)).thenReturn(existing);

        InventoryItem result = service.setStock("sku3", "Updated", 15);

        assertEquals("Updated", result.getName());
        assertEquals(15, result.getQuantity());
    }

    @Test
    void testReserveStockSuccess() {
        InventoryItem item = new InventoryItem("sku4", "Item 4", 10);
        when(repository.findBySku("sku4")).thenReturn(Optional.of(item));

        service.reserveStock("sku4", 5);

        assertEquals(5, item.getQuantity());
        verify(repository).save(item);
    }

    @Test
    void testReserveStockNotEnough() {
        InventoryItem item = new InventoryItem("sku5", "Item 5", 3);
        when(repository.findBySku("sku5")).thenReturn(Optional.of(item));

        assertThrows(NotEnoughStockException.class, () -> service.reserveStock("sku5", 5));
    }

    @Test
    void testReserveStockNotFound() {
        when(repository.findBySku("skuX")).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class, () -> service.reserveStock("skuX", 1));
    }

    @Test
    void testReleaseStock() {
        InventoryItem item = new InventoryItem("sku6", "Item 6", 5);
        when(repository.findBySku("sku6")).thenReturn(Optional.of(item));

        service.releaseStock("sku6", 5);

        assertEquals(10, item.getQuantity());
        verify(repository).save(item);
    }

    @Test
    void testReleaseStockNotFound() {
        when(repository.findBySku("skuY")).thenReturn(Optional.empty());

        assertThrows(InventoryNotFoundException.class, () -> service.releaseStock("skuY", 5));
    }
}
