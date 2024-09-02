package com.aayush.microservice.service;

import com.aayush.microservice.client.InventoryClient;
import com.aayush.microservice.dto.OrderRequest;
import com.aayush.microservice.model.Order;
import com.aayush.microservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;

    public void placeOrder(OrderRequest orderRequest) {
        if (orderRequest == null) {
            throw new IllegalArgumentException("OrderRequest must not be null");
        }

        try {
            var isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());

            if (isProductInStock) {
                var order = mapToOrder(orderRequest);
                orderRepository.save(order);
                log.info("Order placed successfully with order number: {}", order.getOrderNumber());
            } else {
                log.warn("Product with skuCode {} is not in stock.", orderRequest.skuCode());
                // Handle out-of-stock situation, e.g., throw an exception or notify the user
                throw new RuntimeException("Product is out of stock");
            }
        } catch (Exception e) {
            log.error("Failed to place order: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to place order", e);
        }
    }

    private Order mapToOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setPrice(orderRequest.price());
        order.setQuantity(orderRequest.quantity());
        order.setSkuCode(orderRequest.skuCode());
        return order;
    }
}
