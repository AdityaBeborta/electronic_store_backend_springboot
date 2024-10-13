package com.electronicstore.dtos;

import com.electronicstore.entities.Order;
import com.electronicstore.entities.Product;

public class OrderItemDto {
    private int orderItemId;
    private int totalOrderItemQuantity;
    private double totalOrderPriceAsPerQuantity;
    private Product product;
    private Order order;
}
