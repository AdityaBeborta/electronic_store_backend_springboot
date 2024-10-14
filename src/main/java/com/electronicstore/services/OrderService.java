package com.electronicstore.services;
import com.electronicstore.dtos.CreateOrderRequest;
import com.electronicstore.dtos.OrderDto;
import com.electronicstore.entities.Order;

import java.util.List;

public interface OrderService {

    //method used to place order
    public OrderDto placeOrder(CreateOrderRequest createOrderRequest);

    //get the order details by user
    List<OrderDto> getOrdersByUserId(String userId);

    //get order details by order id
    OrderDto getOrderDetailsByOrderId(String orderId);


}
