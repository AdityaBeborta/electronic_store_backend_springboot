package com.electronicstore.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.Name;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "`order`")
@Builder
public class Order {
    @Id
    private String orderId;
    private String orderStatus;
    private Date orderPlacedDate;
    private Date orderDeliveredDate;
    private String paymentStatus;
    private String paymentMode;
    private String billingAddress;
    private String billingPhone;
    private String billingName;
    private double totalOrderAmount;
    //multiple orders can belong to one user
    @ManyToOne
    private User user;

    //one order can have multiple order items - one order can have multiple products in it

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true,mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();
}
