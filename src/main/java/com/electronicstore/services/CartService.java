package com.electronicstore.services;

import com.electronicstore.dtos.CartDto;
import com.electronicstore.helper.ApiResponseMessage;

public interface CartService {

    CartDto addItemsToCart(String userId,String productId, int quantity);

    ApiResponseMessage removeItemFromCart(String userId, int cartItemId);

    ApiResponseMessage removeAllItemsFromCart(String userId);

    CartDto getCartByUserId(String userId);
}
