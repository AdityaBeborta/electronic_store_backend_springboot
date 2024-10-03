package com.electronicstore.dtos;

import com.electronicstore.entities.CartItem;
import com.electronicstore.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CartDto {
    private String cartId;
    private Date createdDate;
    private double totalCartPrice;
    private int totalNumberOfItemsInCart;
    private UserDto userDto;
    private List<CartItemDto> cartItemDto=new ArrayList<>();
}
