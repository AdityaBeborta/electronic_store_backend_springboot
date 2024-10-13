package com.electronicstore.services;
import com.electronicstore.dtos.CreateOrderRequest;
import com.electronicstore.dtos.OrderDto;
public interface OrderService {

    public OrderDto placeOrder(CreateOrderRequest createOrderRequest);
}
