package com.electronicstore.dtos;

import com.electronicstore.entities.Cart;
import com.electronicstore.entities.Product;
import jakarta.persistence.*;
import lombok.*;

@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartItemDto {
    private int cartItemId;
    private int quantity;
    private double totalPriceAsPerQuantity;
    private ProductDto productDto;
    private CartDto cartDto;
}
