package com.electronicstore.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CartItem {

    //CartItem is like a single item in the cart
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;
    private int quantity;
    private double totalPriceAsPerQuantity;

    //Mapping CartItem with product
    //it is like one cart item can have only one product but quantity may differ
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    //Mapping CartItem with Cart
    //it is like multiple cart item can belong to one cart
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;




}
