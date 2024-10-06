package com.electronicstore.controllers;

import com.electronicstore.dtos.CartDto;
import com.electronicstore.services.CartService;
import com.electronicstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/electronicstore/cart/v1")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("user/{userId}/product/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDto> addItemsToCart(@PathVariable String userId, @PathVariable String productId, @PathVariable int quantity) {
        CartDto cartDto = cartService.addItemsToCart(userId, productId,quantity);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CartDto> getCartByUserId(@PathVariable String userId){
        CartDto cartByUserId = this.cartService.getCartByUserId(userId);
        return new ResponseEntity<>(cartByUserId,HttpStatus.OK);
    }


}
