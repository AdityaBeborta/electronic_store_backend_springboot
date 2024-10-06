package com.electronicstore.dtos;

import com.electronicstore.entities.Cart;
import com.electronicstore.entities.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@ToString
public class CartItemDto {
    private int cartItemId;
    private int quantity;
    private double totalPriceAsPerQuantity;
    private ProductDto product;
    @JsonIgnore
    private CartDto cart;
}
