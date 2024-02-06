package com.dinesh.inventoryservice.service;

import com.dinesh.inventoryservice.dto.InventoryResponse;
import com.dinesh.inventoryservice.entity.Inventory;
import com.dinesh.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        List<InventoryResponse> inventoryResponses = inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(i -> InventoryResponse.builder()
                        .skuCode(i.getSkuCode()).isInStock(i.getQuantity()>0)
                        .build())
                .toList();
        return inventoryResponses;
     }
}
