package com.electronicstore.dtos;

import com.electronicstore.entities.OrderItem;
import com.electronicstore.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class OrderDto {
    private String orderId;
    private String orderStatus;
    private Date orderPlacedDate;
    private Date orderReceivedDate;
    private String paymentStatus;
    private String paymentMode;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private double totalOrderAmount;
    //multiple orders can belong to one user
    @JsonIgnore
    private User user;
    //one order can have multiple order items - one order can have multiple products in it
    private List<OrderItem> orderItems = new ArrayList<>();

    @Override
    public String toString() {
        return "OrderDto{" +
                "orderId='" + orderId + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", orderPlacedDate=" + orderPlacedDate +
                ", orderReceivedDate=" + orderReceivedDate +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paymentMode='" + paymentMode + '\'' +
                ", billingAddress='" + billingAddress + '\'' +
                ", billingPhone='" + billingPhone + '\'' +
                ", billingName='" + billingName + '\'' +
                ", totalOrderAmount=" + totalOrderAmount +
                ", user=" + user +
                ", orderItems=" + orderItems +
                '}';
    }
}
