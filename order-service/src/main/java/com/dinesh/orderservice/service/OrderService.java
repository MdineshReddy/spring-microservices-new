package com.dinesh.orderservice.service;

import com.dinesh.orderservice.dto.InventoryResponse;
import com.dinesh.orderservice.dto.OrderRequest;
import com.dinesh.orderservice.entity.Order;
import com.dinesh.orderservice.entity.OrderLineItem;
import com.dinesh.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClient webClient;

    public String placeOrder(OrderRequest orderRequest) {
        List<OrderLineItem> orderLineItems = orderRequest.getOrderLineItemDtoList().stream()
                .map(o->OrderLineItem.builder()
                        .price(o.getPrice())
                        .quantity(o.getQuantity())
                        .skuCode(o.getSkuCode())
                        .build())
                .toList();
        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemList(orderLineItems).build();

        List<String> skuCodes = order.getOrderLineItemList().stream().map(orderLineItem -> orderLineItem.getSkuCode()).toList();

        InventoryResponse[] inventoryResponses = webClient.get()
                .uri("http://localhost:8082/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        Boolean allProductsInStock = inventoryResponses.length == skuCodes.size() && Arrays.asList(inventoryResponses).stream().allMatch(inventoryResponse -> inventoryResponse.isInStock());

        if(allProductsInStock){
            orderRepository.save(order);
            return "Order Placed Successfully";
        } else {
         throw new IllegalArgumentException("Product is not in stock, please try again later!");
        }
    }
}
