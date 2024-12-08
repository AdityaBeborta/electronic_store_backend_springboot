package com.electronicstore.controllers;

import com.electronicstore.dtos.CreateOrderRequest;
import com.electronicstore.dtos.OrderDto;
import com.electronicstore.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/electronicstore/orders/v1")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @PostMapping("/placeOrder")
    public ResponseEntity<OrderDto> placeOrder(@RequestBody CreateOrderRequest createOrderRequest) {
        return new ResponseEntity<>(this.orderService.placeOrder(createOrderRequest), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getUsersOrder(@PathVariable String userId) {
        List<OrderDto> ordersByUserId = this.orderService.getOrdersByUserId(userId);
        return new ResponseEntity<>(ordersByUserId, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderDetailsByOrderId(@PathVariable String orderId) {
        OrderDto orderDetailsByOrderId = this.orderService.getOrderDetailsByOrderId(orderId);
        return new ResponseEntity<>(orderDetailsByOrderId, HttpStatus.OK);
    }
}
