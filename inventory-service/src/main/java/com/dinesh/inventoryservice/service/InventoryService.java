package com.dinesh.inventoryservice.service;

import com.dinesh.inventoryservice.dto.InventoryResponse;
import com.dinesh.inventoryservice.entity.Inventory;
import com.dinesh.inventoryservice.repository.InventoryRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InventoryService {
    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    @SneakyThrows // do not use in production
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        log.info("Wait Started");
        Thread.sleep(10000);
        log.info("Wait Ended");
        List<InventoryResponse> inventoryResponses = inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(i -> InventoryResponse.builder()
                        .skuCode(i.getSkuCode()).isInStock(i.getQuantity()>0)
                        .build())
                .toList();
        return inventoryResponses;
     }
}
