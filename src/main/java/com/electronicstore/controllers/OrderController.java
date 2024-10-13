package com.electronicstore.controllers;

import com.electronicstore.dtos.CreateOrderRequest;
import com.electronicstore.dtos.OrderDto;
import com.electronicstore.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDto> placeOrder(@RequestBody CreateOrderRequest createOrderRequest){
        return new ResponseEntity<>(this.orderService.placeOrder(createOrderRequest), HttpStatus.CREATED);
    }
}
