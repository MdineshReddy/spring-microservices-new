package com.dinesh.inventoryservice.controller;

import com.dinesh.inventoryservice.dto.InventoryResponse;
import com.dinesh.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class  InventoryController {
    @Autowired
    private InventoryService inventoryService;

    // /api/inventory?skuCode=Apple&skuCode=Samsung
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
        return inventoryService.isInStock(skuCode);
    }
}
