package com.electronicstore.services.impl;

import com.electronicstore.dtos.CreateOrderRequest;
import com.electronicstore.dtos.OrderDto;
import com.electronicstore.entities.*;
import com.electronicstore.exceptions.BadApiRequest;
import com.electronicstore.exceptions.ResourceNotFoundException;
import com.electronicstore.repositories.CartRepository;
import com.electronicstore.repositories.OrderRepository;
import com.electronicstore.repositories.UserRepository;
import com.electronicstore.services.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
public class OrderServiceImpl implements OrderService {

    private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public OrderDto placeOrder(CreateOrderRequest createOrderRequest) {
        //find the user with user id
        User user = this.userRepository.findById(createOrderRequest.getUserId()).orElseThrow(() -> new ResourceNotFoundException("user", "user id", createOrderRequest.getUserId()));
        //from user get the user's cart
        Cart cart = user.getCart();
        //if user's cart is empty throw exception it means the user don't have a cart
        if (cart == null) {
            throw new ResourceNotFoundException("cart", "user id", createOrderRequest.getUserId());
        }
        //it means that user have a cart so check if he has some items in the cart
        List<CartItem> cartItems = cart.getCartItems();
        if (cartItems.size() == 0) {
            throw new BadApiRequest("This cart don't have any items in it please add some items before placing order");
        }
        //now it means that user have a cart and the cart have cart items so now we are good to place an order
        //calculate the total price from the cart items
        double totalCalculatedOrderPrice = cartItems.stream().mapToDouble(CartItem::getTotalPriceAsPerQuantity).sum();
        logger.info("total calculated order price " + totalCalculatedOrderPrice);

        //set the remaining details to Order from createOrderRequest
        Order order = Order.builder().orderStatus(createOrderRequest.getOrderStatus()).paymentStatus(createOrderRequest.getPaymentStatus()).billingName(createOrderRequest.getBillingName())
                .orderPlacedDate(new Date()).totalOrderAmount(totalCalculatedOrderPrice).orderId(UUID.randomUUID().toString()).paymentMode(createOrderRequest.getPaymentMode())
                .billingAddress(createOrderRequest.getBillingAddress()).billingPhone(createOrderRequest.getBillingPhone()).orderDeliveredDate(null).user(user).build();
//        logger.info("order details without orderItems {}", order);

        //now map the cart item details with order item details
        List<OrderItem> orderItemList = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setTotalOrderItemQuantity(cartItem.getQuantity());
            orderItem.setTotalOrderPriceAsPerQuantity(cartItem.getProduct().getPrice());
            orderItem.setProduct(cartItem.getProduct());
            return orderItem;
        }).collect(Collectors.toList());
//        logger.info("order item list {}", orderItemList);
        //set the order items in the order
        order.setOrderItems(orderItemList);
        //now empty the cartItem and update the cart
        cartItems.clear();
        //save the order to repo
        Order placedOrder = this.orderRepository.save(order);
        logger.info("order details {}", order);
        //save the updated cart to DB
        Cart updatedCart = this.cartRepository.save(cart);
        logger.info("updated cart {}", updatedCart);

        return this.modelMapper.map(order, OrderDto.class);
    }
}
