package com.electronicstore.dtos;

import com.electronicstore.entities.CartItem;
import com.electronicstore.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartDto {
    private String cartId;
    private Date createdDate;
    private double totalCartPrice;
    private int totalNumberOfItemsInCart;
    @JsonIgnore
    private UserDto user;
    private List<CartItemDto> cartItems=new ArrayList<>();


}
