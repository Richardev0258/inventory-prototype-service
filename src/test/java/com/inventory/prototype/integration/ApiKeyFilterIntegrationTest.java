package com.inventory.prototype.integration;

import com.inventory.prototype.model.InventoryItem;
import com.inventory.prototype.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = "spring.profiles.active=test")
@AutoConfigureMockMvc
class ApiKeyFilterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InventoryRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
        repository.save(new InventoryItem("SKU123", "Laptop", 5));
    }

    @Test
    void testUnauthorizedWithoutApiKey() throws Exception {
        mockMvc.perform(get("/api/inventory/SKU123"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized: missing or invalid API Key"));
    }

    @Test
    void testUnauthorizedWithInvalidApiKey() throws Exception {
        mockMvc.perform(get("/api/inventory/SKU123")
                        .header("X-API-KEY", "WRONG_KEY"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Unauthorized: missing or invalid API Key"));
    }

    @Test
    void testAuthorizedWithValidApiKey() throws Exception {
        mockMvc.perform(get("/api/inventory/SKU123")
                        .header("X-API-KEY", "INVENTORY_SERVICE_KEY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU123"))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.quantity").value(5));
    }
}
