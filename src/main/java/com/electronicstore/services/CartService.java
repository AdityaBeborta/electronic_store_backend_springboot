package com.electronicstore.services;

import com.electronicstore.dtos.CartDto;
import com.electronicstore.helper.ApiResponseMessage;
import com.electronicstore.helper.CartItemRequest;

public interface CartService {

    CartDto addItemsToCart(String userId,String productId, int quantity);

    CartDto removeItemFromCart(String userId, CartItemRequest cartItemRequest);

    ApiResponseMessage removeAllItemsFromCart(String userId);

    CartDto getCartByUserId(String userId);
}
