package com.electronicstore.helper;

import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDetails {

    private Double totalPrice;
    private int totalQuantity;
}
