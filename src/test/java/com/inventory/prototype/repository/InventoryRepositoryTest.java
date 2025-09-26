package com.inventory.prototype.repository;

import com.inventory.prototype.model.InventoryItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository repository;

    @Test
    void testSaveAndFindBySku() {
        InventoryItem item = new InventoryItem("SKU123", "Test Item", 100);
        repository.save(item);

        Optional<InventoryItem> found = repository.findBySku("SKU123");

        assertTrue(found.isPresent());
        assertEquals("Test Item", found.get().getName());
        assertEquals(100, found.get().getQuantity());
    }

    @Test
    void testFindBySkuNotFound() {
        Optional<InventoryItem> found = repository.findBySku("SKU999");
        assertTrue(found.isEmpty());
    }
}
