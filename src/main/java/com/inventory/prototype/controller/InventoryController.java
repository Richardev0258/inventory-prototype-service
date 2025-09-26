package com.inventory.prototype.controller;

import com.inventory.prototype.model.InventoryItem;
import com.inventory.prototype.service.InventoryCommandService;
import com.inventory.prototype.service.InventoryQueryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryQueryService queryService;
    private final InventoryCommandService commandService;

    public InventoryController(InventoryQueryService queryService,
                               InventoryCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    @GetMapping("/{sku}")
    public ResponseEntity<InventoryItem> getItem(@PathVariable String sku) {
        Optional<InventoryItem> item = queryService.getItem(sku);
        return item.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<InventoryItem> setStock(
            @RequestParam String sku,
            @RequestParam String name,
            @RequestParam int quantity) {
        return ResponseEntity.ok(commandService.setStock(sku, name, quantity));
    }

    @PostMapping("/{sku}/reserve")
    public ResponseEntity<Void> reserveStock(@PathVariable String sku,
                                             @RequestParam int quantity) {
        commandService.reserveStock(sku, quantity);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{sku}/release")
    public ResponseEntity<Void> releaseStock(@PathVariable String sku,
                                             @RequestParam int quantity) {
        commandService.releaseStock(sku, quantity);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<InventoryItem>> getAllItems() {
        return ResponseEntity.ok(queryService.getAllItems());
    }
}
