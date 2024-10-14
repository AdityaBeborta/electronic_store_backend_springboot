package com.electronicstore.services;
import com.electronicstore.dtos.CreateOrderRequest;
import com.electronicstore.dtos.OrderDto;

import java.util.List;

public interface OrderService {

    public OrderDto placeOrder(CreateOrderRequest createOrderRequest);


}
