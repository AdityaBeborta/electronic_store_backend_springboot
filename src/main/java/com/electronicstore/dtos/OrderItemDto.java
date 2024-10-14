package com.electronicstore.dtos;

import com.electronicstore.entities.Order;
import com.electronicstore.entities.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data

public class OrderItemDto {
    private int orderItemId;
    private int totalOrderItemQuantity;
    private double totalOrderPriceAsPerQuantity;
    private ProductDto product;
    @JsonIgnore
    private OrderDto order;
}
