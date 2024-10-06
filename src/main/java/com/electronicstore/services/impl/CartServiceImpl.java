package com.electronicstore.services.impl;

import com.electronicstore.dtos.CartDto;
import com.electronicstore.dtos.CartItemDto;
import com.electronicstore.entities.Cart;
import com.electronicstore.entities.CartItem;
import com.electronicstore.entities.Product;
import com.electronicstore.entities.User;
import com.electronicstore.exceptions.BadApiRequest;
import com.electronicstore.exceptions.ResourceNotFoundException;
import com.electronicstore.helper.ApiResponseMessage;
import com.electronicstore.helper.CartItemDetails;
import com.electronicstore.repositories.CartItemRepository;
import com.electronicstore.repositories.CartRepository;
import com.electronicstore.repositories.ProductRepository;
import com.electronicstore.repositories.UserRepository;
import com.electronicstore.services.CartService;
import com.electronicstore.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CartDto addItemsToCart(String userId, String productId, int quantity) {
        AtomicReference<Product> product = new AtomicReference<>(this.productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product", "product id", productId)));
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "user id", userId));
        AtomicBoolean updated = new AtomicBoolean(false);
        //check if the required quantity exists for the product the user is requesting for
        if (product.get().getQuantity() < quantity) {
            throw new BadApiRequest("out of stock");
        }
        product.set(this.modelMapper.map(this.productService.updateProductQuantityByProductId(productId, quantity), Product.class));
        Cart cart = user.getCart();
        if (cart == null) {
            logger.info("cart does not exist");
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedDate(new Date());
        }
        List<CartItem> cartItems = cart.getCartItems();
        //check if the product already exist
        List<CartItem> updatedItems = cartItems.stream().map(item -> {
            if (item.getProduct().getProductId().equals(productId)) {
                logger.info("product already exist so increase the quantity and price");
                item.setQuantity(item.getQuantity() + quantity);
                item.setTotalPriceAsPerQuantity(item.getQuantity() * product.get().getPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());
        //set items in the cart
        cart.setCartItems(updatedItems);
        if (!updated.get()) {
            logger.info("add new items");
            CartItem cartItem = CartItem.builder()
                    .product(product.get())
                    .cart(cart)
                    .quantity(quantity)
                    .totalPriceAsPerQuantity(quantity * product.get().getPrice())
                    .build();
            cart.getCartItems().add(cartItem);
        }
        //calculate total amount for all the items and number of items present in the cart
        CartItemDetails cartDetails = getCartDetails(cart);
        cart.setUser(user);
        cart.setTotalCartPrice(cartDetails.getTotalPrice());
        cart.setTotalNumberOfItemsInCart(cartDetails.getTotalQuantity());
        Cart updatedCart = cartRepository.save(this.modelMapper.map(cart, Cart.class));
        return modelMapper.map(updatedCart, CartDto.class);
    }

    @Override
    public ApiResponseMessage removeItemFromCart(String userId, int cartItemId) {
        return null;
    }

    @Override
    public ApiResponseMessage removeAllItemsFromCart(String userId) {
        return null;
    }

    @Override
    public CartDto getCartByUserId(String userId) {
        User user = this.userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user", "user id", userId));
        //get the cart from user
        Cart cart = user.getCart();
        if(cart==null){
            throw new ResourceNotFoundException("cart ","user id",userId);
        }
        return this.modelMapper.map(cart,CartDto.class);
    }

    public CartItemDetails getCartDetails(Cart cart) {
        //iterate and calculate the total price
        double totalPrice = cart.getCartItems().stream().mapToDouble(CartItem::getTotalPriceAsPerQuantity).sum();
        //get total count
        int sum = (int) cart.getCartItems().stream().filter(cartItem -> (cartItem.getCart().getCartId()).equals(cart.getCartId())).mapToDouble(CartItem::getQuantity).sum();
        return CartItemDetails.builder().totalPrice(totalPrice).totalQuantity(sum).build();
    }
}
