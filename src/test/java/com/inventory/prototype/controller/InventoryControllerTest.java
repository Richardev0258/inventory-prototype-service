package com.inventory.prototype.controller;

import com.inventory.prototype.service.InventoryCommandService;
import com.inventory.prototype.service.InventoryQueryService;
import com.inventory.prototype.model.InventoryItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class InventoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private InventoryQueryService queryService;

    @Mock
    private InventoryCommandService commandService;

    @InjectMocks
    private InventoryController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void testGetItemFound() throws Exception {
        InventoryItem item = new InventoryItem("SKU123", "Test Item", 10);
        when(queryService.getItem("SKU123")).thenReturn(Optional.of(item));

        mockMvc.perform(get("/api/inventory/SKU123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU123"))
                .andExpect(jsonPath("$.name").value("Test Item"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @Test
    void testGetAllItems() throws Exception {
        InventoryItem item1 = new InventoryItem("SKU123", "Laptop", 10);
        InventoryItem item2 = new InventoryItem("SKU999", "Phone", 5);

        when(queryService.getAllItems()).thenReturn(List.of(item1, item2));

        mockMvc.perform(get("/api/inventory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].sku").value("SKU123"))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].quantity").value(10))
                .andExpect(jsonPath("$[1].sku").value("SKU999"))
                .andExpect(jsonPath("$[1].name").value("Phone"))
                .andExpect(jsonPath("$[1].quantity").value(5));
    }

    @Test
    void testGetItemNotFound() throws Exception {
        when(queryService.getItem("SKU404")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/inventory/SKU404"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testSetStock() throws Exception {
        InventoryItem item = new InventoryItem("SKU001", "Phone", 5);
        when(commandService.setStock(eq("SKU001"), eq("Phone"), eq(5))).thenReturn(item);

        mockMvc.perform(post("/api/inventory")
                        .param("sku", "SKU001")
                        .param("name", "Phone")
                        .param("quantity", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sku").value("SKU001"))
                .andExpect(jsonPath("$.name").value("Phone"))
                .andExpect(jsonPath("$.quantity").value(5));
    }

    @Test
    void testReserveStock() throws Exception {
        doNothing().when(commandService).reserveStock("SKU123", 3);

        mockMvc.perform(post("/api/inventory/SKU123/reserve")
                        .param("quantity", "3"))
                .andExpect(status().isOk());

        verify(commandService, times(1)).reserveStock("SKU123", 3);
    }

    @Test
    void testReleaseStock() throws Exception {
        doNothing().when(commandService).releaseStock("SKU123", 2);

        mockMvc.perform(post("/api/inventory/SKU123/release")
                        .param("quantity", "2"))
                .andExpect(status().isOk());

        verify(commandService, times(1)).releaseStock("SKU123", 2);
    }
}