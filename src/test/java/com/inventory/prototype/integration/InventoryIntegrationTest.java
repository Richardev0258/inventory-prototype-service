package com.inventory.prototype.integration;

import com.inventory.prototype.model.InventoryItem;
import com.inventory.prototype.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InventoryRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void testSetStockAndGetItem() throws Exception {
        mockMvc.perform(post("/api/inventory")
                        .param("sku", "SKU123")
                        .param("name", "Test Item")
                        .param("quantity", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku", is("SKU123")))
                .andExpect(jsonPath("$.name", is("Test Item")))
                .andExpect(jsonPath("$.quantity", is(20)));

        mockMvc.perform(get("/api/inventory/SKU123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku", is("SKU123")))
                .andExpect(jsonPath("$.quantity", is(20)));
    }

    @Test
    void testGetAllItems() throws Exception {
        repository.save(new InventoryItem("SKU111", "Tablet", 7));
        repository.save(new InventoryItem("SKU222", "Monitor", 15));

        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("SKU111"))
                .andExpect(jsonPath("$[1].sku").value("SKU222"));
    }

    @Test
    void testReserveAndReleaseStock() throws Exception {
        repository.save(new InventoryItem("SKU999", "Reservable", 10));

        mockMvc.perform(post("/api/inventory/SKU999/reserve")
                        .param("quantity", "5"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/inventory/SKU999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(5)));

        mockMvc.perform(post("/api/inventory/SKU999/release")
                        .param("quantity", "3"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/inventory/SKU999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantity", is(8)));
    }

    @Test
    void testReserveStockNotEnough() throws Exception {
        repository.save(new InventoryItem("SKU555", "LowStock", 2));

        mockMvc.perform(post("/api/inventory/SKU555/reserve")
                        .param("quantity", "5"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testItemNotFound() throws Exception {
        mockMvc.perform(get("/api/inventory/UNKNOWN"))
                .andExpect(status().isNotFound());
    }
}
