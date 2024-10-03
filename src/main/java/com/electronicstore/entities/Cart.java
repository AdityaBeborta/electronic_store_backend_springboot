package com.electronicstore.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class Cart {

    private String cartId;
    private Date createdDate;
    private double totalCartPrice;
    private int totalNumberOfItemsInCart;

    //creating a mapping for cart in such a way that one cart can belong to one user
    @OneToOne
    private User user;

    //creating mapping for cartItem in such a way that one cart can have multiple cart item
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL,mappedBy = "cart")
    private List<CartItem> cartItems=new ArrayList<>();
}
