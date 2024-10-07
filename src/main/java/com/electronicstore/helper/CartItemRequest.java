package com.electronicstore.helper;

import lombok.Data;

@Data
public class CartItemRequest {
    private String productId;
    private int quantity;
}
